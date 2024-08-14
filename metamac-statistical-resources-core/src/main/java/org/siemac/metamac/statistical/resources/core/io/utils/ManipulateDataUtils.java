package org.siemac.metamac.statistical.resources.core.io.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.CodeHierarchy;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.CodeHierarchyBuilder;

import com.arte.statistic.parser.sdmx.v2_1.domain.IdValuePair;

import es.gobcan.istac.edatos.dataset.repository.domain.AttributeAttachmentLevelEnum;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;

public class ManipulateDataUtils {

    public static String    DATASET           = "DATASET";
    public static Character PAIR_SEPARATOR    = ':';
    public static Character ELEMENT_SEPARATOR = ',';

    /**
     * Create a data source extra identification attribute
     *
     * @param keys
     * @param dataSourceId
     * @return AttributeDto
     */
    public static AttributeInstanceObservationDto createDataSourceIdentificationAttribute(List<CodeDimensionDto> keys, String dataSourceId) throws MetamacException {

        if (StringUtils.isEmpty(dataSourceId)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.TASK_DATASOURCE_ID).build();
        }

        // Data
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLabel(dataSourceId);
        localisedStringDto.setLocale(getLocaleDatasourceIdentificationAttribute());
        internationalStringDto.addText(localisedStringDto);

        AttributeInstanceObservationDto attributeDto = new AttributeInstanceObservationDto(StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID, internationalStringDto);

        return attributeDto;
    }

    /*
     * In SDMX the attributes aren't localized. For use localised in SDMX must be use a enumerated representation.
     * In this case, in the repo exists the code of enumerated representation, never the i18n of code.
     */
    public static String getLocaleDatasourceIdentificationAttribute() {
        return StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE;
    }

    /**
     * Generate key for attribute
     *
     * @param codesByDimension
     * @return
     */
    public static String toStringUnorderedKeyForAttribute(Map<String, List<String>> codesByDimension) {
        if (codesByDimension == null || codesByDimension.isEmpty()) {
            return DATASET;
        }

        return addKey(codesByDimension);
    }

    /**
     * Generate key for observation
     *
     * @param codesDimension
     * @return
     */
    public static String toStringUnorderedKeyForObservation(List<CodeDimensionDto> codesDimension) {
        if (codesDimension == null || codesDimension.size() == 0) {
            return null;
        }
        Map<String, List<String>> codesByDimensionMap = new HashMap<String, List<String>>();
        for (CodeDimensionDto codeDimension : codesDimension) {
            codesByDimensionMap.put(codeDimension.getDimensionId(), Arrays.asList(codeDimension.getCodeDimensionId()));
        }

        return addKey(codesByDimensionMap);
    }

    protected static String addKey(Map<String, List<String>> codesByDimension) {
        StringBuilder key = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : codesByDimension.entrySet()) {
            key.append(entry.getKey()).append(PAIR_SEPARATOR);
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                key.append("[*]");
            } else {
                key.append(entry.getValue());
            }
            key.append(ELEMENT_SEPARATOR);
        }

        if (key.charAt(key.length() - 1) == ELEMENT_SEPARATOR) {
            key.deleteCharAt(key.length() - 1);
        }

        return key.toString();
    }

    /**
     * Extract definition of attributes in the DSD to the repository definition
     *
     * @param attributes
     * @return
     */
    public static List<AttributeDto> extractDefinitionOfAttributes(Collection<DsdAttribute> attributes) {
        List<AttributeDto> result = new ArrayList<AttributeDto>(attributes.size());

        // Attributes
        AttributeDto attributeDto = new AttributeDto();

        // Extra Attribute with information about data source
        attributeDto.setAttributeId(StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID);
        attributeDto.setAttachmentLevel(AttributeAttachmentLevelEnum.OBSERVATION);

        result.add(attributeDto);

        // Other attributes
        for (DsdAttribute dsdAttribute : attributes) {
            attributeDto = new AttributeDto();
            attributeDto.setAttributeId(dsdAttribute.getComponentId());

            if (dsdAttribute.getAttributeRelationship().getNone() != null) {
                attributeDto.setAttachmentLevel(AttributeAttachmentLevelEnum.DATASET);
            } else if (dsdAttribute.getAttributeRelationship().getGroup() != null || !dsdAttribute.getAttributeRelationship().getDimensions().isEmpty()) {
                attributeDto.setAttachmentLevel(AttributeAttachmentLevelEnum.DIMENSION);
            } else if (dsdAttribute.getAttributeRelationship().getPrimaryMeasure() != null) {
                attributeDto.setAttachmentLevel(AttributeAttachmentLevelEnum.OBSERVATION);
            }

            result.add(attributeDto);
        }

        return result;
    }

    /**
     * Generate a key from a list of CodeDimensionDto
     *
     * @param codesDimension
     * @return
     */
    public static String generateKeyFromCodesDimensions(List<CodeDimensionDto> codesDimension) {
        StringBuilder key = new StringBuilder();

        for (CodeDimensionDto codeDimensionDto : codesDimension) {
            key.append(codeDimensionDto.getDimensionId()).append(PAIR_SEPARATOR).append(codeDimensionDto.getCodeDimensionId()).append(ELEMENT_SEPARATOR);
        }

        if (key.charAt(key.length() - 1) == ELEMENT_SEPARATOR) {
            key.deleteCharAt(key.length() - 1);
        }
        return key.toString();
    }

    /**
     * Generate a key from a list of IdValuePair
     *
     * @param codesDimensions
     * @return
     */
    public static String generateKeyFromIdValuePairs(List<IdValuePair> codesDimensions) {
        StringBuilder key = new StringBuilder(100);

        for (IdValuePair idValuePair : codesDimensions) {
            key.append(idValuePair.getCode()).append(PAIR_SEPARATOR).append(idValuePair.getValue()).append(ELEMENT_SEPARATOR);
        }

        if (key.charAt(key.length() - 1) == ELEMENT_SEPARATOR) {
            key.deleteCharAt(key.length() - 1);
        }
        return key.toString();
    }

    /**
     * Generate a key for attribute adding attribute id to an existing partial key
     *
     * @param attributeId
     * @param partialKey
     * @return
     */
    public static String generateKeyForAttribute(String attributeId, String partialKey) {
        StringBuilder key = new StringBuilder(attributeId);
        key.append(PAIR_SEPARATOR).append(partialKey);
        return key.toString();
    }

    /**
     * Instance of repository attribute to writer attribute
     *
     * @param attributeBasicDto
     * @return
     */
    public static IdValuePair attributeInstanceBasicDto2IdValuePair(AttributeInstanceBasicDto attributeBasicDto) {
        return new IdValuePair(attributeBasicDto.getAttributeId(), attributeBasicDto.getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE));
    }

    /**
     * Note: this method works because the creation of graph is iterate in depth first order
     *
     * @param codeUrn
     * @param codeId
     * @param codeParentUrn
     */
    public static Map<String, CodeHierarchy> cacheCodeHierarchyGraph(Map<String, CodeHierarchy> codeHierarchyMap, String codeUrn, String codeId, String codeParentUrn) {
        if (codeHierarchyMap == null) {
            codeHierarchyMap = new LinkedHashMap<String, CodeHierarchy>();
        }

        // For content constraints validate, create a auxiliary Map
        CodeHierarchy codeHierarchyParent = new CodeHierarchy();
        if (codeHierarchyMap.containsKey(codeParentUrn)) {
            codeHierarchyParent = codeHierarchyMap.get(codeParentUrn);
        }
        codeHierarchyMap.put(codeUrn, CodeHierarchyBuilder.codeHierarchy().withCode(codeId).withUrn(codeUrn).withParent(codeHierarchyParent).build());

        return codeHierarchyMap;
    }

};
