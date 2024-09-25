package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

public class VisualizationMetadataEditionForm extends GroupDynamicForm {

    public static final String DIMENSIONS_VISUALISATIONS = "dataset-dim-codes";
    public static final String MODIFY_DISTRIBUTION_DIMENSION = "dataset-mod-distribution";

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
        DimensionsVisualisationItem dimensionVisualizationItem = ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS));
        dimensionVisualizationItem.setVisualisationDimensions(headingDimensions, stubDimensions);
        CustomCheckboxItem modifyDistributionDimension = dimensionVisualizationItem.getModifyDistributionDimension();
        if (checkDatasetDimensionsModified(stubDimensions) || checkDatasetDimensionsModified(headingDimensions)) {
            modifyDistributionDimension.setValue(true);
        }
    }

    private boolean checkDatasetDimensionsModified(List<RelatedResourceDto> relatedResources) {
        for (RelatedResourceDto relatedResource : relatedResources) {
            if (relatedResource.getId() != null) {
                return true;
            }
        }
        return false;
    }

    public DatasetVersionDto getSiemacMetadataStatisticalResourceDto(DatasetVersionDto dto) {
        DimensionsVisualisationItem dimensionVisualizationItem = ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS));
        CustomCheckboxItem modifyDistributionDimension = dimensionVisualizationItem.getModifyDistributionDimension();
        if (dto.getStubDimensions() != null) {
            dto.getStubDimensions().clear();
        }
        if (dto.getHeadingDimensions() != null) {
            dto.getHeadingDimensions().clear();
        }
        if (modifyDistributionDimension.getValueAsBoolean()) {
            dto.getStubDimensions().addAll(dimensionVisualizationItem.getStubDimensions());
            dto.getHeadingDimensions().addAll(dimensionVisualizationItem.getHeadingDimensions());
        }
        return dto;
    }

    public QueryVersionDto getQueryVersionDto(QueryVersionDto dto) {
        DimensionsVisualisationItem dimensionVisualizationItem = ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS));
        CustomCheckboxItem modifyDistributionDimension = dimensionVisualizationItem.getModifyDistributionDimension();
        if (dto.getStubDimensions() != null) {
            dto.getStubDimensions().clear();
        }
        if (dto.getHeadingDimensions() != null) {
            dto.getHeadingDimensions().clear();
        }
        if (modifyDistributionDimension.getValueAsBoolean()) {
            dto.getStubDimensions().addAll(dimensionVisualizationItem.getStubDimensions());
            dto.getHeadingDimensions().addAll(dimensionVisualizationItem.getHeadingDimensions());
        }
        return dto;
    }
}
