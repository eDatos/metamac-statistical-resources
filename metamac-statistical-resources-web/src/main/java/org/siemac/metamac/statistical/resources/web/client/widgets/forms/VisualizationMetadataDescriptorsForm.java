package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;

public class VisualizationMetadataDescriptorsForm extends GroupDynamicForm {

    private static final String DIMENSIONS_VISUALISATIONS = "dataset-dim-codes";

    public VisualizationMetadataDescriptorsForm(String groupTitle) {
        super(groupTitle);
    }
    public VisualizationMetadataDescriptorsForm() {
        super(getConstants().datasetVisualisationMetadata());
        DimensionsVisualisationItem dimensionCodesVisualisationItem = new DimensionsVisualisationItem(DIMENSIONS_VISUALISATIONS,
                getConstants().dsdDimensionsVisualisation(), false);
        setFields(dimensionCodesVisualisationItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(List<RelatedResourceDto> headingDimensions, List<RelatedResourceDto> stubDimensions) {
        ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(headingDimensions, stubDimensions);
    }
}
