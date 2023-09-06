package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import static org.siemac.metamac.core.common.exception.CommonServiceExceptionType.UNKNOWN;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.LabelVisualisationModeEnum;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;

public class PlainTextExporter {

    private final ResourceAccess   datasetAccess;
    private final DatasetSelection datasetSelection;
    private final List<String>     selectedLanguages;
    private static final String    HEADER_OBSERVATION                    = "OBS_VALUE";
    private static final String    HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE   = "_CODE";
    private static final String    HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";

    public PlainTextExporter(ResourceAccess resourceAccess, List<String> selectedLanguages) {
        datasetAccess = resourceAccess;
        datasetSelection = resourceAccess.getDataSelection();
        this.selectedLanguages = selectedLanguages;
    }

    public List<PlainTextResource> writeObservationsAndAttributesWithObservationAttachmentLevel(String format) throws MetamacException {
        try {
            return getBodyForPlainTextObservations(format);
        } catch (Exception e) {
            throw new MetamacException(e, UNKNOWN, "Error exporting");
        }
    }

    private String getHeaderName(LabelVisualisationModeEnum labelVisualisation, String name, String format) {
        return processUnsupportedCharaters(name, format);
    }

    private String getHeaderNameCode(LabelVisualisationModeEnum labelVisualisation, String name, String format) {
        if (labelVisualisation.isLabelAndCode()) {
            return processUnsupportedCharaters(name + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, format);
        }
        return "";
    }

    private List<PlainTextResource> getBodyForPlainTextObservations(String format) {
        List<PlainTextResource> plainTextResourceAccessList = new ArrayList<PlainTextResource>();

        for (int i = 0; i < datasetSelection.getRows(); i++) {
            for (int j = 0; j < datasetSelection.getColumns(); j++) {
                PlainTextResource plainTextResourceAccess = new PlainTextResource();
                Map<String, String> permutationAtCell = datasetSelection.permutationAtCell(i, j);
                // The observation is complete
                // Dimension values
                for (String dimensionId : datasetAccess.getDimensionsOrderedForData()) {

                    String dimensionValueId = permutationAtCell.get(dimensionId);
                    LabelVisualisationModeEnum labelVisualisation = datasetAccess.getDimensionLabelVisualisationMode(dimensionId);

                    String headerName = getHeaderName(labelVisualisation, dimensionId, format);

                    if (labelVisualisation.isLabel()) {
                        InternationalString dimensionValueLabel = datasetAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                        plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, dimensionValueLabel, format));
                    }

                    // if label and code, it needs another column name for code.
                    if (labelVisualisation.isLabelAndCode()) {
                        headerName = getHeaderNameCode(labelVisualisation, dimensionId, format);
                    }

                    if (labelVisualisation.isCode()) {
                        plainTextResourceAccess.getFields().put(headerName, processUnsupportedCharaters(dimensionValueId, format));
                    }
                }
                // Observation
                String observation = datasetAccess.observationAtPermutation(permutationAtCell);

                plainTextResourceAccess.getFields().put(HEADER_OBSERVATION, processUnsupportedCharaters(observation, format));

                // Attributes
                for (Attribute attribute : datasetAccess.getAttributesMetadata()) {
                    if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                        continue; // only observation attachment level
                    }

                    String attributeId = attribute.getId();
                    LabelVisualisationModeEnum labelVisualisation = datasetAccess.getAttributeLabelVisualisationMode(attributeId);
                    String headerName = getHeaderName(labelVisualisation, attributeId, format);

                    String attributeValue = datasetAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
                    if (attributeValue == null) {
                        plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, new InternationalString(), format));
                    } else {

                        if (labelVisualisation.isLabel()) {
                            InternationalString attributeValueLabel = datasetAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                            if (attributeValueLabel != null) {
                                plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, attributeValueLabel, format));
                            } else {
                                plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, new InternationalString(), format));
                            }

                        }

                        // if label and code, it needs another column name for code.
                        if (labelVisualisation.isLabelAndCode()) {
                            headerName = getHeaderNameCode(labelVisualisation, attributeId, format);
                        }

                        if (labelVisualisation.isCode()) {
                            plainTextResourceAccess.getFields().put(headerName, processUnsupportedCharaters(attributeValue, format));
                        }
                    }
                }
                plainTextResourceAccessList.add(plainTextResourceAccess);
            }

        }
        return plainTextResourceAccessList;
    }

    private Map<String, String> internationalString2MapExport(String nameField, InternationalString source, String format) {
        Map<String, String> target = new LinkedHashMap<>();

        InternationalString copySource = new InternationalString();

        if (source != null) {
            copySource = source;
        }

        String header;
        for (String language : getSelectedLanguages()) {
            header = nameField + HEADER_INTERNATIONAL_STRING_SEPARATOR + language;
            target.put(header, getLocalisedStringByLang(copySource.getTexts(), language, format));
        }

        return target;
    }

    private static String getLocalisedStringByLang(List<LocalisedString> source, String language, String format) {
        // in the api, lamba functions is not allowed. the search is done with a loop.
        for (LocalisedString loc : source) {
            if (language.equals(loc.getLang())) {
                return processUnsupportedCharaters(loc.getValue(), format);
            }
        }
        return null;
    }

    private static String formattedText(String text, String format) {
        if ("csv".equals(format)) {
            text = StringEscapeUtils.escapeCsv(text);
        }
        return text;
    }

    // remove unsupported characters and if there are excluded characters, to put the value between on quotation marks
    private static String processUnsupportedCharaters(String string, String format) {
        if (StringUtils.isNotBlank(string)) {
            String value = string.replaceAll("[\n\t\r\b\f]", " ");
            return formattedText(value, format);
        } else {
            return null;
        }
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }
}