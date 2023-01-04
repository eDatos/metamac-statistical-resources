package org.siemac.metamac.statistical.resources.web.client.query.view.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.HashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.statistical.resources.web.client.query.view.handlers.QueryListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.search.SearchSingleDatasetVersionRelatedResourcePaginatedWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;

import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewQueryWindow extends CustomWindow {

    private static final int                                         FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String                                      FIELD_SAVE             = "save-query";

    protected CustomDynamicForm                                      form;
    private QueryListUiHandlers                                      uiHandlers;
    private SearchExternalItemLinkItem                               relatedDatasetItem;
    private SearchSingleDatasetVersionRelatedResourcePaginatedWindow searchDatasetWindow;

    public NewQueryWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem nameItem = new RequiredTextItem(QueryDS.TITLE, getConstants().nameableStatisticalResourceTitle());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().queryCreate());

        relatedDatasetItem = createQueryDatasetItem();
        relatedDatasetItem.setRequired(true);

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(nameItem, relatedDatasetItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }
    public void setUiHandlers(QueryListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
    public QueryVersionDto getNewQueryDto() {
        QueryVersionDto queryDto = new QueryVersionDto();
        queryDto.setCode(form.getValueAsString(QueryDS.CODE));
        queryDto.setTitle(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(QueryDS.TITLE)));
        queryDto.setSelection(new HashMap<String, List<CodeItemDto>>());
        return queryDto;
    }

    public boolean validateForm() {
        return form.validate();
    }

    private SearchExternalItemLinkItem createQueryDatasetItem() {

        final SearchExternalItemLinkItem item = new SearchExternalItemLinkItem(QueryDS.RELATED_DATASET_VERSION, getConstants().datasetRelatedDSD()) {

            @Override
            public void onSearch() {

                searchDatasetWindow = new SearchSingleDatasetVersionRelatedResourcePaginatedWindow(getConstants().resourceSelection(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<DatasetVersionWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
                                retrieveDatasetsAsResources(firstResult, maxResults, criteria);
                            }
                        });

                // Load resources (to populate the selection window)
                retrieveStatisticalOperationsForDatasetSelection();

                searchDatasetWindow.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        ExternalItemDto selectedResource = ExternalItemUtils.getExternalItemDtoFromRelatedResourceDto(searchDatasetWindow.getSelectedResource(), TypeExternalArtefactsEnum.DATASET);
                        searchDatasetWindow.markForDestroy();
                        // Set selected resource in form
                        setRelatedDataset(selectedResource);
                        form.validate(false);
                    }
                });
            }
        };
        return item;
    }

    private void setRelatedDataset(ExternalItemDto relatedDsdDto) {
        form.setValue(QueryDS.RELATED_DATASET_VERSION, relatedDsdDto);
    }

    public void retrieveResourcesForRelatedDataset(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        uiHandlers.retrieveDatasetForRelatedDataset(firstResult, maxResults, criteria);
    }

    private void retrieveStatisticalOperationsForDatasetSelection() {
        uiHandlers.retrieveStatisticalOperationsForDatasetSelection();
    }

    public void setDatasetsForQuery(List<RelatedResourceDto> resourcesDtos, int firstResult, int elementsInPage, int totalResults) {
        if (searchDatasetWindow != null) {
            searchDatasetWindow.setResources(resourcesDtos);
            searchDatasetWindow.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public void setStatisticalOperationsForDatasetSelection(List<ExternalItemDto> externalItemsDtos) {
        if (searchDatasetWindow != null) {
            searchDatasetWindow.setStatisticalOperations(externalItemsDtos);
            retrieveDatasetsAsResources(0, StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS, searchDatasetWindow.getSearchCriteria());
        }
    }

    private void retrieveDatasetsAsResources(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        uiHandlers.retrieveDatasetsForQuery(firstResult, maxResults, criteria);
    }
}
