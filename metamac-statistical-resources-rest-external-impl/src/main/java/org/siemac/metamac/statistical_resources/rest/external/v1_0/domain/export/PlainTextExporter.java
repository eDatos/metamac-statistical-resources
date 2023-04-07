package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.core.common.exception.CommonServiceExceptionType.UNKNOWN;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.entities.PlainTextResourceAccess;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.enume.LabelVisualisationModeEnum;

public class PlainTextExporter {

    private final ResourceAccess datasetAccess;
    private final DatasetSelection datasetSelection;
    private static final String HEADER_OBSERVATION = "OBS_VALUE";
    private static final String HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE = "_CODE";

    public PlainTextExporter(ResourceAccess resourceAccess) {
        datasetAccess = resourceAccess;
        datasetSelection = resourceAccess.getDataSelection();
    }

    public List<PlainTextResourceAccess> writeObservationsAndAttributesWithObservationAttachmentLevel() throws MetamacException {
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

    private List<PlainTextResourceAccess> getBodyForPlainTextObservations() {
        List<PlainTextResourceAccess> plainTextResourceAccessList = new ArrayList<PlainTextResourceAccess>();

        for (int i = 0; i < datasetSelection.getRows(); i++) {
            for (int j = 0; j < datasetSelection.getColumns(); j++) {
                PlainTextResourceAccess plainTextResourceAccess = new PlainTextResourceAccess();
                Map<String, String> permutationAtCell = datasetSelection.permutationAtCell(i, j);
                // The observation is complete
                // Dimension values
                for (String dimensionId : datasetAccess.getDimensionsOrderedForData()) {

                    String dimensionValueId = permutationAtCell.get(dimensionId);
                    LabelVisualisationModeEnum labelVisualisation = datasetAccess.getDimensionLabelVisualisationMode(dimensionId);

                    String headerName = getHeaderName(labelVisualisation, dimensionId);

                    if (labelVisualisation.isLabel()) {
                        String dimensionValueLabel = datasetAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                        plainTextResourceAccess.getFields().put(headerName, removeUnsupportedCharaters(dimensionValueLabel));
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
                        plainTextResourceAccess.getFields().put(headerName, null);
                    } else {

                        if (labelVisualisation.isLabel()) {
                            String attributeValueLabel = datasetAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                            plainTextResourceAccess.getFields().put(headerName,
                                    attributeValueLabel != null ? removeUnsupportedCharaters(attributeValueLabel) : removeUnsupportedCharaters(attributeValue));
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

    public static String removeUnsupportedCharaters(String string) {
        if (StringUtils.isNotBlank(string)) {
            string = string.replace('\n', ' ');
            string = string.replace('\t', ' ');
            string = string.replace('\r', ' ');
            string = string.replace('\b', ' ');
            string = string.replace('\f', ' ');
        } else {
            return null;
        }
        return string;
    }

}