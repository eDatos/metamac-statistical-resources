package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

public class VisualizationMetadataDescriptorsForm extends GroupDynamicForm {

    private static final String DIMENSIONS_VISUALISATIONS     = "dataset-dim-codes";

    public VisualizationMetadataDescriptorsForm(String groupTitle) {
        super(groupTitle);
    }
    public VisualizationMetadataDescriptorsForm() {
        super(getConstants().datasetVisualisationMetadata());
        DimensionsVisualisationItem dimensionCodesVisualisationItem = new DimensionsVisualisationItem(DIMENSIONS_VISUALISATIONS, getConstants().dsdDimensionsVisualisation(), false);
        setFields(dimensionCodesVisualisationItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(List<RelatedResourceDto> headingDimensions, List<RelatedResourceDto> stubDimensions) {
        DimensionsVisualisationItem dimensionVisualizationItem = ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS));
        dimensionVisualizationItem.setVisualisationDimensions(headingDimensions, stubDimensions);
        CustomCheckboxItem modifyDistributionDimension = dimensionVisualizationItem.getModifyDistributionDimension();
        if (checkDatasetDimensionsModified(stubDimensions) || checkDatasetDimensionsModified(headingDimensions)) {
            modifyDistributionDimension.setValue(true);
        } else {
            modifyDistributionDimension.setValue(false);
        }
        modifyDistributionDimension.setCanEdit(false);
    }

    private boolean checkDatasetDimensionsModified(List<RelatedResourceDto> relatedResources) {
        for (RelatedResourceDto relatedResource : relatedResources) {
            if (relatedResource.getId() != null) {
                return true;
            }
        }
        return false;
    }

}
