package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setRelatedResourcesValue;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DatasetDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetMetadataCommonTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.SiemacMetadataResourceRelationDescriptorsEditionForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceListItem;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.gwtplatform.mvp.client.UiHandlers;

public class DatasetResourceRelationDescriptorsEditionForm extends SiemacMetadataResourceRelationDescriptorsEditionForm {

    private DatasetMetadataCommonTabUiHandlers uiHandlers;

    public DatasetResourceRelationDescriptorsEditionForm() {
        super();
        initDatasetResourceRelationDescriptorsEditionForm(false);
    }

    public DatasetResourceRelationDescriptorsEditionForm(boolean isMultipleUpdate) {
        super(isMultipleUpdate);
        initDatasetResourceRelationDescriptorsEditionForm(isMultipleUpdate);
    }

    private void initDatasetResourceRelationDescriptorsEditionForm(boolean isMultipleUpdate) {

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

    @Override
    public void setUiHandlers(UiHandlers uiHandlers) {
        this.uiHandlers = (DatasetMetadataCommonTabUiHandlers) uiHandlers;
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
