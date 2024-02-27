package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setRelatedResourcesValue;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataResourceRelationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceListItem;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.gwtplatform.mvp.client.UiHandlers;

public class DatasetResourceRelationDescriptorsEditionForm extends SiemacMetadataResourceRelationDescriptorsEditionForm {

    private DatasetMetadataTabUiHandlers uiHandlers;

    public DatasetResourceRelationDescriptorsEditionForm() {
        super();
        initDatasetResourceRelationDescriptorsEditionForm();
    }

    private void initDatasetResourceRelationDescriptorsEditionForm() {

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

    @Override
    public void setUiHandlers(UiHandlers uiHandlers) {
        this.uiHandlers = (DatasetMetadataTabUiHandlers) uiHandlers;
    }

    @Override
    public void retrieveResourcesForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria) {
        uiHandlers.retrieveDatasetsForReplaces(firstResult, maxResults, criteria);
    }

    @Override
    public void retrieveStatisticalOperationsForReplacesSelection() {
        uiHandlers.retrieveStatisticalOperationsForReplacesSelection();
    }

    @Override
    public BaseUiHandlers getBaseUiHandlers() {
        return uiHandlers;
    }

}
