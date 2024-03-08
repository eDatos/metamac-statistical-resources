package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setRelatedResourcesValue;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataResourceRelationDescriptorsForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceListItem;

public class DatasetResourceRelationDescriptorsForm extends SiemacMetadataResourceRelationDescriptorsForm {

    public DatasetResourceRelationDescriptorsForm() {
        super();
        initDatasetResourceRelationDescriptorsForm();
    }

    private void initDatasetResourceRelationDescriptorsForm() {

        RelatedResourceListItem isRequiredBy = new RelatedResourceListItem(DatasetDS.IS_REQUIRED_BY, getConstants().siemacMetadataStatisticalResourceIsRequiredBy(), false,
                getRecordNavigationHandler());

        RelatedResourceListItem isPartOf = new RelatedResourceListItem(DatasetDS.IS_PART_OF, getConstants().siemacMetadataStatisticalResourceIsPartOf(), false, getRecordNavigationHandler());

        addFields(isPartOf, isRequiredBy);
    }

    public void setDatasetVersionDto(DatasetVersionDto dto) {
        setSiemacMetadataStatisticalResourceDto(dto);

        setRelatedResourcesValue(getItem(DatasetDS.IS_REQUIRED_BY), dto.getIsRequiredBy());
        setRelatedResourcesValue(getItem(DatasetDS.IS_PART_OF), dto.getIsPartOf());
    }
}
