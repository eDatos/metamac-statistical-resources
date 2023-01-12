package org.siemac.metamac.statistical.resources.web.client.query.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.base.view.LifeCycleBaseListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.statistical.resources.web.client.query.model.record.QueryRecord;
import org.siemac.metamac.statistical.resources.web.client.query.presenter.QueryListPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.utils.QueryClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.query.view.handlers.QueryListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.NewQueryWindow;
import org.siemac.metamac.statistical.resources.web.client.query.view.widgets.QueryVersionSearchSectionStack;
import org.siemac.metamac.statistical.resources.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.ValidationRejectionWindow;
import org.siemac.metamac.statistical.resources.web.shared.criteria.QueryVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueryVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class QueryListViewImpl extends LifeCycleBaseListViewImpl<QueryListUiHandlers> implements QueryListPresenter.QueryListView {

    private QueryVersionSearchSectionStack searchSectionStack;
    private NewQueryWindow newQueryWindow;

    @Inject
    public QueryListViewImpl() {
        super();

        // List

        listGrid.getListGrid().setDataSource(new QueryDS());
        listGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    QueryRecord record = (QueryRecord) event.getRecord();
                    getUiHandlers().goToQuery(record.getUrn());
                }
            }
        });
        listGrid.getListGrid().setFields(ResourceFieldUtils.getQueryListGridFields());

        // Delete confirmation window

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteQueries(getSelectedResourcesUrns());
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(QueryListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
        listGrid.setUiHandlers(uiHandlers);
    }

    public void setQueriesPaginatedList(GetQueryVersionsResult result) {
        QueryRecord[] records = StatisticalResourcesRecordUtils.getQueryRecords(result.getQueryVersionBaseDtos());
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(result.getPageNumber(), result.getQueryVersionBaseDtos().size(), result.getTotalResults());
    }

    @Override
    public void setQueryPaginatedList(GetQueryVersionsResult queriesPaginatedList) {
        setQueriesPaginatedList(queriesPaginatedList);
    }

    @Override
    public void setDatasetsForQuery(GetDatasetVersionsResult result) {
        List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getDatasetVersionBaseDtosAsRelatedResourceDtos(result.getDatasetVersionBaseDtos());
        newQueryWindow.setDatasetsForQuery(relatedResourceDtos, result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
    }

    @Override
    public void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result) {
        newQueryWindow.setStatisticalOperationsForDatasetSelection(result.getOperationsList());
    }

    @Override
    public void setDatasetDimensionCodes(String dimensionId, List<CodeItemDto> codesDimension) {
        newQueryWindow.setDatasetDimensionCodes(dimensionId, codesDimension);
    }

    @Override
    public void setDatasetDimensionsIds(List<String> datasetDimensionsIds) {
        newQueryWindow.setDatasetDimensionsIds(datasetDimensionsIds);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    //
    // LISTGRID
    //

    @Override
    public void retrieveResultSet(int firstResult, int maxResults) {
        getUiHandlers().retrieveQueries(firstResult, maxResults, getQueryVersionWebCriteria());
    }

    //
    // LISTGRID BUTTONS
    //

    // Create

    @Override
    public ClickHandler getNewButtonClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (StatisticalResourcesDefaults.defaultLanguage == null) { 
                    return; 
                }
                newQueryWindow = new NewQueryWindow(getConstants().queryCreate());
                newQueryWindow.setUiHandlers(getUiHandlers());
            }
        };
    }

    // Sent to production validation

    @Override
    protected ClickHandler getSendToProductionValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<QueryVersionBaseDto> queryVersionDtos = StatisticalResourcesRecordUtils.getQueryVersionDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().sendToProductionValidation(queryVersionDtos);
            }
        };
    }

    // Send to diffusion validation

    @Override
    protected ClickHandler getSendToDiffusionValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<QueryVersionBaseDto> queryVersionDtos = StatisticalResourcesRecordUtils.getQueryVersionDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().sendToDiffusionValidation(queryVersionDtos);
            }
        };
    }

    // Reject validation

    @Override
    protected ClickHandler getRejectValidationClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final List<QueryVersionBaseDto> queryVersionDtos = StatisticalResourcesRecordUtils.getQueryVersionDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                final ValidationRejectionWindow window = new ValidationRejectionWindow(getConstants().lifeCycleRejectValidation());
                window.show();
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        String reasonOfRejection = window.getReasonOfRejection();
                        window.markForDestroy();
                        getUiHandlers().rejectValidation(queryVersionDtos, reasonOfRejection);
                    }
                });
            }
        };
    }

    // Publish

    @Override
    protected ClickHandler getPublishClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<QueryVersionBaseDto> queryVersionDtos = StatisticalResourcesRecordUtils.getQueryVersionDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
                getUiHandlers().publish(queryVersionDtos);
            }
        };
    }

    // Version

    @Override
    protected void version(VersionTypeEnum versionType) {
        List<QueryVersionBaseDto> queryVersionDtos = StatisticalResourcesRecordUtils.getQueryVersionDtosFromListGridRecords(listGrid.getListGrid().getSelectedRecords());
        getUiHandlers().version(queryVersionDtos, versionType);
    }

    //
    // LISTGRID ACTIONS
    //

    @Override
    protected boolean canCreate() {
        return QueryClientSecurityUtils.canCreateQuery();
    }

    @Override
    protected boolean canDelete(ListGridRecord record) {
        return QueryClientSecurityUtils.canDeleteQueryVersion(getDtoFromRecord(record));
    }

    @Override
    protected boolean canSendToProductionValidation(ListGridRecord record) {
        return QueryClientSecurityUtils.canSendQueryVersionToProductionValidation(getDtoFromRecord(record));
    }

    @Override
    protected boolean canSendToDiffusionValidation(ListGridRecord record) {
        return QueryClientSecurityUtils.canSendQueryVersionToDiffusionValidation(getDtoFromRecord(record));
    }

    @Override
    protected boolean canRejectValidation(ListGridRecord record) {
        return QueryClientSecurityUtils.canSendQueryVersionToValidationRejected(getDtoFromRecord(record));
    }

    @Override
    protected boolean canPublish(ListGridRecord record) {
        return QueryClientSecurityUtils.canPublishQueryVersion(getDtoFromRecord(record));
    }

    @Override
    protected boolean canVersion(ListGridRecord record) {
        return QueryClientSecurityUtils.canVersionQueryVersion(getDtoFromRecord(record));
    }

    //
    // SEARCH
    //

    @Override
    protected BaseAdvancedSearchSectionStack createAdvacedSearchSectionStack() {
        searchSectionStack = new QueryVersionSearchSectionStack();
        return searchSectionStack;
    }

    @Override
    public QueryVersionWebCriteria getQueryVersionWebCriteria() {
        return searchSectionStack.getQueryVersionWebCriteria();
    }

    @Override
    public void setStatisticalOperationsForDatasetVersionSelectionInSearchSection(List<ExternalItemDto> results) {
        searchSectionStack.setStatisticalOperationsForDatasetVersionSelection(results);
    }

    @Override
    public void setDatasetVersionsForSearchSection(org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult result) {
        searchSectionStack.setDatasetVersions(result);
    };

    @Override
    public void setStatisticalOperationsForSearchSection(GetStatisticalOperationsPaginatedListResult result) {
        searchSectionStack.setStatisticalOperations(result);
    }

    private QueryVersionBaseDto getDtoFromRecord(ListGridRecord record) {
        QueryRecord queryRecord = (QueryRecord) record;
        return queryRecord.getQueryVersionBaseDto();
    }
}
