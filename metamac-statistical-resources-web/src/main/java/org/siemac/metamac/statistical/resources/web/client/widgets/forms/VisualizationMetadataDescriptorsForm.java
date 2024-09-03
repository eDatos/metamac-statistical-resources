package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;

public class VisualizationMetadataDescriptorsForm extends GroupDynamicForm {

    public VisualizationMetadataDescriptorsForm(String groupTitle) {
        super(groupTitle);
    }
    public VisualizationMetadataDescriptorsForm() {
        super(getConstants().datasetVisualisationMetadata());
        DimensionsVisualisationItem dimensionCodesVisualisationItem = new DimensionsVisualisationItem("dataset-dim-codes",
                "pepito", false);
        setFields(dimensionCodesVisualisationItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(List<RelatedResourceDto> headingDimensions, List<RelatedResourceDto> stubDimensions) {
        ((DimensionsVisualisationItem) getItem("dataset-dim-codes")).setVisualisationDimensions(headingDimensions, stubDimensions);
    }
}
