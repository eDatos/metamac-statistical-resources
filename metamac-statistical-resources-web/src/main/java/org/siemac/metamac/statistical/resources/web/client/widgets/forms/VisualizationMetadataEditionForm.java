package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class VisualizationMetadataEditionForm extends GroupDynamicForm {

    public static final String DIMENSIONS_VISUALISATIONS = "dataset-dim-codes";
    public static final String MODIFY_DISTRIBUTION_DIMENSION = "dataset-mod-distribution";

    public VisualizationMetadataEditionForm(String groupTitle) {
        super(groupTitle);
    }
    public VisualizationMetadataEditionForm() {
        super(getConstants().datasetVisualisationMetadata());
        CustomCheckboxItem modifyDistributionDimension = new CustomCheckboxItem(MODIFY_DISTRIBUTION_DIMENSION, "Modificar distribución de las dimensiones");
        modifyDistributionDimension.setValue(false);
        modifyDistributionDimension.addChangedHandler(FormItemUtils.getMarkForRedrawChangedHandler(this));

        DimensionsVisualisationItem dimensionCodesVisualisationItem = new DimensionsVisualisationItem(DIMENSIONS_VISUALISATIONS,
                getConstants().dsdDimensionsVisualisation(), true);
        dimensionCodesVisualisationItem.setShowIfCondition(getUpdateFrecuencyFormItemIfFunction());
        setFields(modifyDistributionDimension, dimensionCodesVisualisationItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(List<RelatedResourceDto> headingDimensions, List<RelatedResourceDto> stubDimensions) {
        ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS)).setVisualisationDimensions(headingDimensions, stubDimensions);
        CustomCheckboxItem modifyDistributionDimension = (CustomCheckboxItem) getItem(MODIFY_DISTRIBUTION_DIMENSION);
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
        CustomCheckboxItem modifyDistributionDimension = (CustomCheckboxItem) getItem(MODIFY_DISTRIBUTION_DIMENSION);
        if (dto.getStubDimensions() != null) {
            dto.getStubDimensions().clear();
        }
        if (dto.getHeadingDimensions() != null) {
            dto.getHeadingDimensions().clear();
        }
        if (modifyDistributionDimension.getValueAsBoolean()) {
            DimensionsVisualisationItem dimensionVisualizationItem = ((DimensionsVisualisationItem) getItem(DIMENSIONS_VISUALISATIONS));
            dto.getStubDimensions().addAll(dimensionVisualizationItem.getStubDimensions());
            dto.getHeadingDimensions().addAll(dimensionVisualizationItem.getHeadingDimensions());
        }
        return dto;
    }

    private FormItemIfFunction getUpdateFrecuencyFormItemIfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return (Boolean)((CustomCheckboxItem) form.getItem(MODIFY_DISTRIBUTION_DIMENSION)).getValue();
            }
        };
    }
}
