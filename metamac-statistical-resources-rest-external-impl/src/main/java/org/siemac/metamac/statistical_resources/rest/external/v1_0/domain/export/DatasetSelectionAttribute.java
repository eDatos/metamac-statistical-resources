package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.enume.LabelVisualisationModeEnum;

public class DatasetSelectionAttribute {

    private final String               id;
    private LabelVisualisationModeEnum labelVisualisationMode;

    public DatasetSelectionAttribute(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LabelVisualisationModeEnum getLabelVisualisationMode() {
        return labelVisualisationMode;
    }

    public void setLabelVisualisationMode(LabelVisualisationModeEnum labelVisualisationMode) {
        this.labelVisualisationMode = labelVisualisationMode;
    }
}
