package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setRelatedResourcesValue;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataResourceRelationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceListItem;

public class DatasetResourceRelationDescriptorsForm extends SiemacMetadataResourceRelationDescriptorsForm {

    public DatasetResourceRelationDescriptorsForm() {
        super();
        initDatasetResourceRelationDescriptorsForm(false);
    }

    public DatasetResourceRelationDescriptorsForm(boolean isMultipleUpdate) {
        super(isMultipleUpdate);
        initDatasetResourceRelationDescriptorsForm(isMultipleUpdate);
    }

    private void initDatasetResourceRelationDescriptorsForm(boolean isMultipleUpdate) {

        RelatedResourceListItem isRequiredBy = new RelatedResourceListItem(DatasetDS.IS_REQUIRED_BY, getConstants().siemacMetadataStatisticalResourceIsRequiredBy(), false,
                getRecordNavigationHandler());
        isRequiredBy.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        RelatedResourceListItem isPartOf = new RelatedResourceListItem(DatasetDS.IS_PART_OF, getConstants().siemacMetadataStatisticalResourceIsPartOf(), false, getRecordNavigationHandler());
        isPartOf.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        addFields(isPartOf, isRequiredBy);
    }

    public void setDatasetVersionDto(DatasetVersionDto dto) {
        setSiemacMetadataStatisticalResourceDto(dto);

        setRelatedResourcesValue(getItem(DatasetDS.IS_REQUIRED_BY), dto.getIsRequiredBy());
        setRelatedResourcesValue(getItem(DatasetDS.IS_PART_OF), dto.getIsPartOf());
    }
}
