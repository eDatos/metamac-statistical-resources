package org.siemac.metamac.statistical.resources.web.client.operation.view;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.operation.model.ds.OperationDS;
import org.siemac.metamac.statistical.resources.web.client.operation.model.record.OperationRecord;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationListPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.view.handlers.OperationListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.widgets.OperationsValuesSearchSectionStack;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class OperationListViewImpl extends ViewWithUiHandlers<OperationListUiHandlers> implements OperationListPresenter.OperationListView {

    private VLayout           panel;

    private PaginatedListGrid operationsList;
    
    private OperationsValuesSearchSectionStack searchSectionStack;

    @Inject
    public OperationListViewImpl() {
        super();
        searchSectionStack = new OperationsValuesSearchSectionStack();
        //TODO CAMBIAR MAXIMO PAGINACION MAIN_LIST_MAX_RESULTS
        operationsList = new PaginatedListGrid(50, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveOperations(firstResult, maxResults, null);
            }
        });
        operationsList.getListGrid().setAutoFitMaxRecords(StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
        operationsList.getListGrid().setDataSource(new OperationDS());
        operationsList.getListGrid().setUseAllDataSourceFields(false);

        ListGridField fieldIdentifier = new ListGridField(OperationDS.IDENTIFIER, getConstants().identifiableStatisticalResourceCode());
        fieldIdentifier.setAlign(Alignment.LEFT);
        ListGridField fieldTitle = new ListGridField(OperationDS.TITLE, getConstants().nameableStatisticalResourceTitle());
        operationsList.getListGrid().setFields(fieldIdentifier, fieldTitle);

        operationsList.setHeight100();

        panel = new VLayout();
        panel.addMember(searchSectionStack);
        panel.addMember(operationsList);

        bindEvents();
    }

    private void bindEvents() {
        operationsList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String urn = ((OperationRecord) event.getRecord()).getAttribute(OperationDS.URN);
                getUiHandlers().goToOperation(urn);
            }
        });
    }

    @Override
    public void setOperationPaginatedList(GetStatisticalOperationsPaginatedListResult operationsPaginatedList) {
        setOperationList(operationsPaginatedList.getOperationsList());
        operationsList.refreshPaginationInfo(operationsPaginatedList.getFirstResultOut(), operationsPaginatedList.getOperationsList().size(), operationsPaginatedList.getTotalResults());
    }

    private void setOperationList(List<ExternalItemDto> operationsExternalDtos) {
        OperationRecord[] records = new OperationRecord[operationsExternalDtos.size()];
        int index = 0;
        for (ExternalItemDto operationDto : operationsExternalDtos) {
            records[index++] = new OperationRecord(operationDto.getCode(), InternationalStringUtils.getLocalisedString(operationDto.getTitle()), operationDto.getUri(), operationDto.getUrn(),
                    operationDto);
        }
        operationsList.getListGrid().setData(records);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
    
    @Override
    public void setUiHandlers(OperationListUiHandlers handlers) {
        super.setUiHandlers(handlers);
        searchSectionStack.setUiHandlers(handlers);
    }
    
//    @Override
//    public MetamacWebCriteria getOperationWebCriteria() {
//        if (searchSectionStack == null) {
//            return new MetamacWebCriteria();
//        }
//        return searchSectionStack.getDataConfigurationWebCriteria();
//    }
}
