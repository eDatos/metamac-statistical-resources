package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getRelatedResourceValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setRelatedResourceValue;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.RelatedResourceLinkItem;
import org.siemac.metamac.statistical.resources.web.client.widgets.forms.fields.SearchRelatedResourceLinkItem;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchSingleVersionableStatisticalRelatedResourcePaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public abstract class SiemacMetadataResourceRelationDescriptorsEditionForm extends NavigationEnabledDynamicForm {

    private SearchSingleVersionableStatisticalRelatedResourcePaginatedWindow searchReplacesWindow;

    public SiemacMetadataResourceRelationDescriptorsEditionForm() {
        super(getConstants().formResourceRelationDescriptors());
        init(false);
    }

    public SiemacMetadataResourceRelationDescriptorsEditionForm(boolean isMultipleUpdate) {
        super(getConstants().formResourceRelationDescriptors());
        init(isMultipleUpdate);
    }

    public void init(boolean isMultipleUpdate) {
        SearchRelatedResourceLinkItem replaces = createReplacesItem(SiemacMetadataDS.REPLACES, getConstants().siemacMetadataStatisticalResourceReplaces());
        RelatedResourceLinkItem isReplacedBy = new RelatedResourceLinkItem(SiemacMetadataDS.IS_REPLACED_BY, getConstants().siemacMetadataStatisticalResourceIsReplacedBy(),
                getCustomLinkItemNavigationClickHandler());
        isReplacedBy.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        setFields(replaces, isReplacedBy);
    }
    //
    // SETTERS
    //

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        setRelatedResourceValue(getItem(SiemacMetadataDS.REPLACES), dto.getReplaces());
        setRelatedResourceValue(getItem(SiemacMetadataDS.IS_REPLACED_BY), dto.getIsReplacedBy());
    }

    private void setReplaces(RelatedResourceDto relatedResourceDto) {
        setRelatedResourceValue(getItem(SiemacMetadataDS.REPLACES), relatedResourceDto);
    }

    public void setRelatedResourcesForReplaces(List<RelatedResourceDto> relatedResourceDtos, int firstResult, int elementsInPage, int totalResults) {
        if (searchReplacesWindow != null) {
            searchReplacesWindow.setResources(relatedResourceDtos);
            searchReplacesWindow.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public void setStatisticalOperationsForReplacesSelection(List<ExternalItemDto> externalItemsDtos, ExternalItemDto defaultSelected) {
        if (searchReplacesWindow != null) {
            searchReplacesWindow.setStatisticalOperations(externalItemsDtos);
            searchReplacesWindow.setSelectedStatisticalOperation(defaultSelected);
        }
    }

    //
    // GETTER
    //
    public SiemacMetadataStatisticalResourceDto getSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {

        dto.setReplaces(getRelatedResourceValue(getItem(SiemacMetadataDS.REPLACES)));

        return dto;
    }

    //
    // FORM ITEM CREATION
    //
    private SearchRelatedResourceLinkItem createReplacesItem(String name, String title) {
        final SearchRelatedResourceLinkItem replacesItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());

        replacesItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchReplacesWindow = new SearchSingleVersionableStatisticalRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<VersionableStatisticalResourceWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria webCriteria) {
                                retrieveResourcesForReplaces(firstResult, maxResults, webCriteria);
                            }

                        });

                // Load resources (to populate the selection window)
                retrieveResourcesForReplaces(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchReplacesWindow.getSearchCriteria());

                retrieveStatisticalOperationsForReplacesSelection();

                searchReplacesWindow.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        RelatedResourceDto selectedResource = searchReplacesWindow.getSelectedResource();
                        searchReplacesWindow.markForDestroy();
                        // Set selected resource in form
                        setReplaces(selectedResource);
                        validate(false);
                    }
                });
            }
        });
        return replacesItem;
    }

    public abstract void setUiHandlers(UiHandlers uiHandlers);
    public abstract void retrieveResourcesForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria);
    public abstract void retrieveStatisticalOperationsForReplacesSelection();
}
