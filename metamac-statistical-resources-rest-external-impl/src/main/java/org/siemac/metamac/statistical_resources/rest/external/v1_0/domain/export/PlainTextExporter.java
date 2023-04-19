package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.core.common.exception.CommonServiceExceptionType.UNKNOWN;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.enume.LabelVisualisationModeEnum;

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

    public List<PlainTextResource> writeObservationsAndAttributesWithObservationAttachmentLevel() throws MetamacException {
        try {
            return getBodyForPlainTextObservations();
        } catch (Exception e) {
            throw new MetamacException(e, UNKNOWN, "Error exporting");
        }
    }

    private String getHeaderName(LabelVisualisationModeEnum labelVisualisation, String name) {
        return removeUnsupportedCharaters(name);
    }

    private String getHeaderNameCode(LabelVisualisationModeEnum labelVisualisation, String name) {
        if (labelVisualisation.isLabelAndCode()) {
            return removeUnsupportedCharaters(name + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE);
        }
        return "";
    }

    private List<PlainTextResource> getBodyForPlainTextObservations() {
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

                    String headerName = getHeaderName(labelVisualisation, dimensionId);

                    if (labelVisualisation.isLabel()) {
                        InternationalString dimensionValueLabel = datasetAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                        plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, dimensionValueLabel));
                    }

                    // if label and code, it needs another column name for code.
                    if (labelVisualisation.isLabelAndCode()) {
                        headerName = getHeaderNameCode(labelVisualisation, dimensionId);
                    }

                    if (labelVisualisation.isCode()) {
                        plainTextResourceAccess.getFields().put(headerName, removeUnsupportedCharaters(dimensionValueId));
                    }
                }
                // Observation
                String observation = datasetAccess.observationAtPermutation(permutationAtCell);

                plainTextResourceAccess.getFields().put(HEADER_OBSERVATION, removeUnsupportedCharaters(observation));

                // Attributes
                for (Attribute attribute : datasetAccess.getAttributesMetadata()) {
                    if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                        continue; // only observation attachment level
                    }

                    String attributeId = attribute.getId();
                    LabelVisualisationModeEnum labelVisualisation = datasetAccess.getAttributeLabelVisualisationMode(attributeId);
                    String headerName = getHeaderName(labelVisualisation, attributeId);

                    String attributeValue = datasetAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
                    if (attributeValue == null) {
                        plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, new InternationalString()));
                    } else {

                        if (labelVisualisation.isLabel()) {
                            InternationalString attributeValueLabel = datasetAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                            if (attributeValueLabel != null) {
                                plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, attributeValueLabel));
                            } else {
                                plainTextResourceAccess.getFields().putAll(internationalString2MapExport(headerName, new InternationalString()));
                            }

                        }

                        // if label and code, it needs another column name for code.
                        if (labelVisualisation.isLabelAndCode()) {
                            headerName = getHeaderNameCode(labelVisualisation, attributeId);
                        }

                        if (labelVisualisation.isCode()) {
                            plainTextResourceAccess.getFields().put(headerName, removeUnsupportedCharaters(attributeValue));
                        }
                    }
                }
                plainTextResourceAccessList.add(plainTextResourceAccess);
            }

        }
        return plainTextResourceAccessList;
    }

    private Map<String, String> internationalString2MapExport(String nameField, InternationalString source) {
        Map<String, String> target = new LinkedHashMap<>();

        InternationalString copySource = new InternationalString();

        if (source != null) {
            copySource = source;
        }

        String header;
        for (String language : getSelectedLanguages()) {
            header = nameField + HEADER_INTERNATIONAL_STRING_SEPARATOR + language;
            target.put(header, getLocalisedStringByLang(copySource.getTexts(), language));
        }

        return target;
    }

    private static String getLocalisedStringByLang(List<LocalisedString> source, String language) {
        // in the api, lamba functions is not allowed. the search is done with a loop.
        for (LocalisedString loc : source) {
            if (language.equals(loc.getLang())) {
                return removeUnsupportedCharaters(loc.getValue());
            }
        }
        return null;
    }

    public static String removeUnsupportedCharaters(String string) {
        if (StringUtils.isNotBlank(string)) {
            return string.replaceAll("[\n\t\r\b\f]", " ");
        } else {
            return null;
        }
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }

}