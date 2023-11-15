package org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Key;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPart;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPartType;

import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;

public class ConstraintsValidator {

    public static boolean checkObservationAgaintsConstraintsKey(ObservationExtendedDto oservation, List<Key> constraintKeys, Map<String, CodeHierarchy> codeHierarchyMap) {
        Map<String, CodeDimensionDto> mapOfCodesDimension = createMapOfCodesDimension(oservation.getCodesDimension());

        // Check all keys definition from a restriction region
        Boolean keyPass = null;
        for (Key constraintKey : constraintKeys) {
            List<KeyPart> constraintKeyParts = constraintKey.getKeyParts().getKeyParts();
            String constraintKeyPartIdentifier = obtainDimensionPartidentification(constraintKeyParts);

            CodeDimensionDto codeDimensionDto = mapOfCodesDimension.get(constraintKeyPartIdentifier); // Current Key instance
            boolean match = constraintKey.isIncluded();

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
            if (constraintKey.isIncluded()) {
                keyPass = match;
            } else {
                keyPass = !match;
            }

            // If the current key constraint is not met, then fails validation.
            if (!keyPass) {
                return keyPass;
            }
        }

        return keyPass;
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

    public static boolean checkTimeRangeValueAgaintsConstraint(CodeDimensionDto codeDimensionDto, KeyPart keyPart) {

        if (keyPart.getBeforePeriod() != null) {
            // Is before period
            TimeSdmx obsValue = new TimeSdmx(codeDimensionDto.getCodeDimensionId());
            TimeSdmx ckValue = new TimeSdmx(keyPart.getBeforePeriod());

            if (keyPart.isBeforePeriodInclusive()) {
                if (obsValue.getEndDateTime().isBefore(ckValue.getStartDateTime()) || obsValue.getEndDateTime().equals(ckValue.getStartDateTime())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (obsValue.getEndDateTime().isBefore(ckValue.getStartDateTime())) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (keyPart.getAfterPeriod() != null) {
            // Is after period
            TimeSdmx obsValue = new TimeSdmx(codeDimensionDto.getCodeDimensionId());
            TimeSdmx ckValue = new TimeSdmx(keyPart.getBeforePeriod());

            if (keyPart.isBeforePeriodInclusive()) {
                if (obsValue.getEndDateTime().isBefore(ckValue.getStartDateTime()) || obsValue.getEndDateTime().equals(ckValue.getStartDateTime())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (obsValue.getEndDateTime().isBefore(ckValue.getStartDateTime())) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (keyPart.getStartPeriod() != null && keyPart.getEndPeriod() != null) {
            // Is range period
            return SdmxTimeUtils.isValidTimeInInterval(codeDimensionDto.getCodeDimensionId(), keyPart.getStartPeriod(), keyPart.isStartPeriodInclusive(), keyPart.getEndPeriod(),
                    keyPart.isEndPeriodInclusive());
        }

        return false;
    }

}
