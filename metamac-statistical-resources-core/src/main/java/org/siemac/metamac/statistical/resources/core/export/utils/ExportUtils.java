package org.siemac.metamac.statistical.resources.core.export.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.export.DatasetSelection;
import org.siemac.metamac.statistical.resources.core.export.enume.LabelVisualisationModeEnum;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;

public class ExportUtils {

    public static final String DEFAULT = "__default__";

    private ExportUtils() {
        // without impl
    }

    /**
     * Returns label in locale 'lang'. Null if it does not exist
     */
    public static String getLabel(InternationalString internationalString, String lang) {
        if (internationalString == null) {
            return null;
        }
        for (LocalisedString localisedString : internationalString.getTexts()) {
            if (localisedString.getLang().equals(lang)) {
                return localisedString.getValue();
            }
        }
        return null;
    }

    /**
     * Builds map indexed by dimensionValueId and value as localised title of the dimension value
     */
    public static Map<String, InternationalString> buildMapDimensionsValuesLocalisedLabels(Dimension dimension) throws MetamacException {
        Map<String, InternationalString> dimensionValuesLabels = null;
        if (dimension.getDimensionValues() instanceof EnumeratedDimensionValues) {
            EnumeratedDimensionValues dimensionValues = (EnumeratedDimensionValues) dimension.getDimensionValues();
            dimensionValuesLabels = new HashMap<String, InternationalString>(dimensionValues.getValues().size());
            for (EnumeratedDimensionValue dimensionValue : dimensionValues.getValues()) {
                String dimensionValueId = dimensionValue.getId();
                dimensionValuesLabels.put(dimensionValueId, dimensionValue.getName());
            }
        } else if (dimension.getDimensionValues() instanceof NonEnumeratedDimensionValues) {
            NonEnumeratedDimensionValues dimensionValues = (NonEnumeratedDimensionValues) dimension.getDimensionValues();
            dimensionValuesLabels = new HashMap<String, InternationalString>(dimensionValues.getValues().size());
            for (NonEnumeratedDimensionValue dimensionValue : dimensionValues.getValues()) {
                String dimensionValueId = dimensionValue.getId();
                dimensionValuesLabels.put(dimensionValueId, dimensionValue.getName());
            }
        } else {
            throw new MetamacException(CommonServiceExceptionType.UNKNOWN, "Dimension values unexpected: " + dimension.getDimensionValues().getClass().getCanonicalName());
        }
        return dimensionValuesLabels;
    }

    /**
     * Builds map indexed by dimensionValueId and value as title of the dimension value
     */
    public static Map<String, InternationalString> buildMapDimensionsValuesLabels(Dimension dimension) throws MetamacException {
        Map<String, InternationalString> dimensionValuesLabels = null;
        if (dimension.getDimensionValues() instanceof EnumeratedDimensionValues) {
            EnumeratedDimensionValues dimensionValues = (EnumeratedDimensionValues) dimension.getDimensionValues();
            dimensionValuesLabels = new HashMap<String, InternationalString>(dimensionValues.getValues().size());
            for (EnumeratedDimensionValue dimensionValue : dimensionValues.getValues()) {
                String dimensionValueId = dimensionValue.getId();
                dimensionValuesLabels.put(dimensionValueId, dimensionValue.getName());
            }
        } else if (dimension.getDimensionValues() instanceof NonEnumeratedDimensionValues) {
            NonEnumeratedDimensionValues dimensionValues = (NonEnumeratedDimensionValues) dimension.getDimensionValues();
            dimensionValuesLabels = new HashMap<String, InternationalString>(dimensionValues.getValues().size());
            for (NonEnumeratedDimensionValue dimensionValue : dimensionValues.getValues()) {
                String dimensionValueId = dimensionValue.getId();
                dimensionValuesLabels.put(dimensionValueId, dimensionValue.getName());
            }
        } else {
            throw new MetamacException(CommonServiceExceptionType.UNKNOWN, "Dimension values unexpected: " + dimension.getDimensionValues().getClass().getCanonicalName());
        }
        return dimensionValuesLabels;
    }

    /**
     * Builds a map indexed by dimensionId with a value as name of the dimension
     */
    public static Map<String, InternationalString> buildMapAttributesLabels(List<Attribute> attributes) throws MetamacException {
        Map<String, InternationalString> attributesValuesLabels = new HashMap<String, InternationalString>(attributes.size());

        for (Attribute attribute : attributes) {
            String attributeId = attribute.getId();
            attributesValuesLabels.put(attributeId, attribute.getName());
        }

        return attributesValuesLabels;
    }

    /**
     * Builds a map indexed by attributeId with a map indexed by attributeValueId and value as title of the attribute value
     */

    public static Map<String, InternationalString> buildMapAttributesValuesLabels(Attribute attribute) throws MetamacException {
        Map<String, InternationalString> attributeValuesLabels = null;
        if (attribute.getAttributeValues() == null) {
            attributeValuesLabels = new HashMap<String, InternationalString>();
        } else if (attribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
            EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getAttributeValues();
            attributeValuesLabels = new HashMap<String, InternationalString>(attributeValues.getValues().size());
            for (EnumeratedAttributeValue attributeValue : attributeValues.getValues()) {
                String attributeValueId = attributeValue.getId();
                attributeValuesLabels.put(attributeValueId, attributeValue.getName());
            }
        } else if (attribute.getAttributeValues() instanceof NonEnumeratedAttributeValues) {
            NonEnumeratedAttributeValues attributeValues = (NonEnumeratedAttributeValues) attribute.getAttributeValues();
            attributeValuesLabels = new HashMap<String, InternationalString>(attributeValues.getValues().size());
            for (NonEnumeratedAttributeValue attributeValue : attributeValues.getValues()) {
                String attributeValueId = attributeValue.getId();
                attributeValuesLabels.put(attributeValueId, attributeValue.getName());
            }
        } else {
            throw new MetamacException(CommonServiceExceptionType.UNKNOWN, "Attribute values unexpected: " + attribute.getAttributeValues().getClass().getCanonicalName());
        }
        return attributeValuesLabels;
    }

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, StatisticalResourcesRestConstants.DATA_SEPARATOR);
    }

    /**
     * Builds a map indexed by attributeId with the effective label visualisation mode.
     * If attributes has not attributes values in metadata, returns 'only code'
     * If configuration does not exist for component, returns default configuration
     */
    public static Map<String, LabelVisualisationModeEnum> buildMapAttributesLabelVisualisationMode(DatasetSelection datasetSelection, List<Attribute> attributes) {
        Map<String, LabelVisualisationModeEnum> labelVisualisationsMode = new HashMap<String, LabelVisualisationModeEnum>(attributes.size());
        for (Attribute attribute : attributes) {
            String attributeId = attribute.getId();
            LabelVisualisationModeEnum labelVisualisationMode = null;
            if (attribute.getAttributeValues() == null) {
                // Attribute has not translation for the codes
                labelVisualisationMode = LabelVisualisationModeEnum.CODE;
            } else {
                labelVisualisationMode = datasetSelection != null ? datasetSelection.getAttributeLabelVisualisationModel(attributeId) : null;
                if (labelVisualisationMode == null) {
                    // default value
                    labelVisualisationMode = LabelVisualisationModeEnum.CODE_AND_LABEL;
                }
            }
            labelVisualisationsMode.put(attributeId, labelVisualisationMode);
        }
        return labelVisualisationsMode;
    }

    public static String escapeNulls(String value) {
        return StringUtils.isEmpty(value) ? StringUtils.EMPTY : value;
    }

}
