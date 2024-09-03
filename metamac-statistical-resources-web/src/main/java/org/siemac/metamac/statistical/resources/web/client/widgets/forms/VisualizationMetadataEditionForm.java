package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;

public class VisualizationMetadataEditionForm extends GroupDynamicForm {

    private static final String DIMENSIONS_VISUALISATIONS = "dataset-dim-codes";

    public VisualizationMetadataEditionForm(String groupTitle) {
        super(groupTitle);
    }
    public VisualizationMetadataEditionForm() {
        super(getConstants().datasetVisualisationMetadata());
        DimensionsVisualisationItem dimensionCodesVisualisationItem = new DimensionsVisualisationItem(DIMENSIONS_VISUALISATIONS,
                getConstants().dsdDimensionsVisualisation(), true);
        setFields(dimensionCodesVisualisationItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(List<RelatedResourceDto> headingDimensions, List<RelatedResourceDto> stubDimensions) {
        ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(headingDimensions, stubDimensions);
    }
}
