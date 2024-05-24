package org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Key;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPart;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPartType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RegionReference;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.io.utils.ManipulateDataUtils;

import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;

public class ConstraintsValidator {

    public static boolean checkObservationAgaintsConstraintsKey(ObservationExtendedDto oservation, List<Key> constraintKeys, Map<String, CodeHierarchy> codeHierarchyMap) {
        Map<String, CodeDimensionDto> mapOfCodesDimension = createMapOfCodesDimension(oservation.getCodesDimension());

        // Check all keys definition from a restriction region
        Boolean keyPass = true;
        for (Key constraintKey : constraintKeys) {
            List<KeyPart> constraintKeyParts = constraintKey.getKeyParts().getKeyParts();
            String constraintKeyPartIdentifier = obtainDimensionPartidentification(constraintKeyParts);
            CodeDimensionDto codeDimensionDto = mapOfCodesDimension.get(constraintKeyPartIdentifier); // Current Key instance

            keyPass = checkConstraintDimensionAgainstDimensionValue(codeHierarchyMap, codeDimensionDto, constraintKey, constraintKeyParts);

            if (!keyPass) {
                return keyPass;
            }

        }
        return keyPass;
    }

    private static boolean checkConstraintDimensionAgainstDimensionValue(Map<String, CodeHierarchy> codeHierarchyMap, CodeDimensionDto codeDimensionDto, Key constraintKey,
            List<KeyPart> constraintKeyParts) {
        boolean match;
        if (codeDimensionDto == null) {
            // Wildcard
            match = true;
        } else {
            // Match a dimension (Not wild card)
            // Check if the dimension f
            boolean innerMatch = false;
            for (KeyPart keyPart : constraintKeyParts) {
                if (KeyPartType.NORMAL.equals(keyPart.getType())) {
                    if (keyPart.isCascadeValues()) {
                        // Cascade Value
                        innerMatch = checkCodeInCascadeHirarchy(codeDimensionDto, keyPart, codeHierarchyMap);
                    } else if (codeDimensionDto.getCodeDimensionId().equals(keyPart.getValue())) {
                        innerMatch = true;
                    } else {
                        innerMatch = false;
                    }
                } else if (KeyPartType.TIME_RANGE.equals(keyPart.getType())) {
                    innerMatch = checkTimeRangeValueAgaintsConstraint(codeDimensionDto, keyPart);
                }

                if (innerMatch) {
                    break;
                }
            }
            match = innerMatch;
        }

        // Update validation result against isIncluded flag
        return constraintKey.isIncluded() && match;

    }

    private static String obtainDimensionPartidentification(List<KeyPart> constraintKeyParts) {
        // All identifiers in KeyyPartes list are equal
        String keyPartIdentifier = null;
        for (KeyPart keyPart : constraintKeyParts) {
            if (keyPartIdentifier == null) {
                keyPartIdentifier = keyPart.getIdentifier();

            }
            if (!keyPartIdentifier.equals(keyPart.getIdentifier())) {
                throw new RuntimeException("The information model of constraint is corrupted!");
            }
        }
        return keyPartIdentifier;
    }

    private static Map<String, CodeDimensionDto> createMapOfCodesDimension(List<CodeDimensionDto> codesDimension) {
        Map<String, CodeDimensionDto> dimensionCodeMap = new HashMap<String, CodeDimensionDto>();
        for (CodeDimensionDto codeDimensionDto : codesDimension) {
            dimensionCodeMap.put(codeDimensionDto.getDimensionId(), codeDimensionDto);
        }
        return dimensionCodeMap;
    }

    private static boolean checkCodeInCascadeHirarchy(CodeDimensionDto codeDimensionDto, KeyPart keyPart, Map<String, CodeHierarchy> codeHierarchyMap) {
        // For all codes. Until one is found
        boolean match = false;
        for (CodeHierarchy codeHierarchy : codeHierarchyMap.values()) {
            // If the actual code
            if (codeHierarchy.getCode().equals(keyPart.getValue())) {
                if (keyPart.getValue().equals(codeDimensionDto.getCodeDimensionId())) {
                    match = true;
                    break; // Only break if match
                } else {
                    // Check in parent
                    match = checkCodeInCascadeHirarchy(codeHierarchy, codeDimensionDto, codeHierarchyMap);
                    if (match) {
                        break; // Only break if match, if not, continue iterating
                    }
                }
            }
        }
        return match;
    }

    private static boolean checkCodeInCascadeHirarchy(CodeHierarchy codeHierarchy, CodeDimensionDto codeDimensionDto, Map<String, CodeHierarchy> codeHierarchyMap) {
        if (codeHierarchy == null || codeHierarchy.getCode() == null) {
            return false;
        }

        if (codeHierarchy.getCode().equals(codeDimensionDto.getCodeDimensionId())) {
            return true;
        } else {
            // Check in parent recursive
            return checkCodeInCascadeHirarchy(codeHierarchy.getParent(), codeDimensionDto, codeHierarchyMap);
        }
    }

    private static boolean checkPeriod(CodeDimensionDto codeDimensionDto, KeyPart keyPart) {
        // Is before period
        TimeSdmx obsValue = new TimeSdmx(codeDimensionDto.getCodeDimensionId());
        TimeSdmx ckValue = new TimeSdmx(keyPart.getBeforePeriod());

        if (keyPart.isBeforePeriodInclusive()) {
            return obsValue.getEndDateTime().isBefore(ckValue.getStartDateTime()) || obsValue.getEndDateTime().equals(ckValue.getStartDateTime());
        } else {
            return obsValue.getEndDateTime().isBefore(ckValue.getStartDateTime());
        }
    }

    public static boolean checkTimeRangeValueAgaintsConstraint(CodeDimensionDto codeDimensionDto, KeyPart keyPart) {

        if (keyPart.getBeforePeriod() != null || keyPart.getAfterPeriod() != null) {
            return checkPeriod(codeDimensionDto, keyPart);
        } else if (keyPart.getStartPeriod() != null && keyPart.getEndPeriod() != null) {
            // Is range period
            return SdmxTimeUtils.isValidTimeInInterval(codeDimensionDto.getCodeDimensionId(), keyPart.getStartPeriod(), keyPart.isStartPeriodInclusive(), keyPart.getEndPeriod(),
                    keyPart.isEndPeriodInclusive());
        }

        return false;
    }

    public static void checkObservationContentConstraints(List<CodeDimensionDto> observationValuesByDimensionId, Key keysByDimensionId, Map<String, CodeHierarchy> codeHierarchyMap,
            List<MetamacExceptionItem> exceptions) {

        for (CodeDimensionDto codeDimensionDto : observationValuesByDimensionId) {
            if (!checkConstraintDimensionAgainstDimensionValue(codeHierarchyMap, codeDimensionDto, keysByDimensionId, keysByDimensionId.getKeyParts().getKeyParts())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.CONSTRAINT_UPDATE_CHECK_EXISTING_OBSERVATIONS_FAIL,
                        ManipulateDataUtils.toStringUnorderedKeyForObservation(Arrays.asList(codeDimensionDto))));
            }
        }
    }

    public static Key getConstraintKeysByDimensionId(RegionReference regionReference, String dimensionId) {
        List<Key> keies = regionReference.getKeys().getKeies();
        if (keies != null && !keies.isEmpty()) {
            for (Key constraintKey : regionReference.getKeys().getKeies()) {
                List<KeyPart> constraintKeyParts = constraintKey.getKeyParts().getKeyParts();
                if (constraintKeyParts != null && !constraintKeyParts.isEmpty()) {
                    String dimensionIdConstraint = constraintKeyParts.get(0).getIdentifier();
                    if (dimensionIdConstraint != null && dimensionIdConstraint.equals(dimensionId)) {
                        return constraintKey;
                    }
                }
            }
        }
        return null;
    }

    public static Map<String, CodeHierarchy> getCodeHierarchyMap(SrmRestInternalService srmRestInternalService, DsdDimensionDto selectedDimension) throws MetamacException {
        String codelistRepresentationUrn = selectedDimension.getCodelistRepresentationUrn();

        Map<String, CodeHierarchy> codeHierarchyMap = new HashMap<>();
        if (codelistRepresentationUrn != null) {

            Codes codes = null;

            codes = srmRestInternalService.retrieveCodesOfCodelistEfficiently(codelistRepresentationUrn);
            for (CodeResourceInternal codeType : codes.getCodes()) {
                ManipulateDataUtils.cacheCodeHierarchyGraph(codeHierarchyMap, codeType.getUrn(), codeType.getId(), codeType.getParent()); // Auxiliary data for content constraints validate
            }
        }
        String conceptSchemeRepresentationUrn = selectedDimension.getConceptSchemeRepresentationUrn();
        if (conceptSchemeRepresentationUrn != null) {
            Concepts concepts = srmRestInternalService.retrieveConceptsOfConceptSchemeEfficiently(conceptSchemeRepresentationUrn);

            for (ItemResourceInternal conceptType : concepts.getConcepts()) {
                ManipulateDataUtils.cacheCodeHierarchyGraph(codeHierarchyMap, conceptType.getUrn(), conceptType.getId(), conceptType.getParent()); // Auxiliary data for content constraints validate
            }
        }
        return codeHierarchyMap;
    }

}
