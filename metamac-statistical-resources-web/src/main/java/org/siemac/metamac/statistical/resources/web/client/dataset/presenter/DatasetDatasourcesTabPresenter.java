package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DecimalSeparatorTypeEnum;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetDatasourcesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectDatasetTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetDatasetEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CreateDatabaseDatasourceAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CreateDatabaseDatasourceResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesNotUsedAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesNotUsedResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportDatasourcesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportDatasourcesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetCodelistsWithVariableAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetCodelistsWithVariableResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsVariableMappingAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsVariableMappingResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasourcesByDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasourcesByDatasetResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class DatasetDatasourcesTabPresenter extends Presenter<DatasetDatasourcesTabPresenter.DatasetDatasourcesTabView, DatasetDatasourcesTabPresenter.DatasetDatasourcesTabProxy>
        implements
            DatasetDatasourcesTabUiHandlers {

    private DispatchAsync     dispatcher;
    private PlaceManager      placeManager;

    private DatasetVersionDto datasetVersion;

    public interface DatasetDatasourcesTabView extends View, HasUiHandlers<DatasetDatasourcesTabUiHandlers> {

        void setDatasetVersion(DatasetVersionDto datasetVersionDto);
        void setDatasources(String datasetUrn, List<DatasourceDto> datasources);
        void setCodelistsInVariable(String dimensionId, GetCodelistsWithVariableResult result);
        void setDimensionVariablesForDataset(String urn, Map<String, String> mapping);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetDatasourcesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetDatasourcesTabProxy extends Proxy<DatasetDatasourcesTabPresenter>, Place {
    }

    @Inject
    public DatasetDatasourcesTabPresenter(EventBus eventBus, DatasetDatasourcesTabView view, DatasetDatasourcesTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbDatasources();
    }

    private void setDataset(DatasetVersionDto datasetVersionDto) {
        this.datasetVersion = datasetVersionDto;
        getView().setDatasetVersion(datasetVersionDto);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DatasetPresenter.TYPE_SetContextAreaDataset, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectDatasetTabEvent.fire(this, DatasetTabTypeEnum.DATASOURCES);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        String datasetCode = PlaceRequestUtils.getDatasetParamFromUrl(placeManager);
        if (!StringUtils.isBlank(operationCode) && !StringUtils.isBlank(datasetCode)) {
            String operationUrn = CommonUtils.generateStatisticalOperationUrn(operationCode);

            if (!CommonUtils.isUrnFromSelectedStatisticalOperation(operationUrn)) {
                retrieveOperation(operationUrn);
            } else {
                loadInitialData();
            }

        } else {
            StatisticalResourcesWeb.showErrorPage();
        }
    }

    private void retrieveOperation(String urn) {
        dispatcher.execute(new GetStatisticalOperationAction(urn), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationResult result) {
                StatisticalResourcesDefaults.setSelectedStatisticalOperation(result.getOperation());
                loadInitialData();
            }
        });
    }

    private void loadInitialData() {
        String datasetCode = PlaceRequestUtils.getDatasetParamFromUrl(placeManager);
        String datasetUrn = CommonUtils.generateDatasetUrn(datasetCode);
        retrieveDatasourcesByDataset(datasetUrn, 0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
    }

    private void retrieveDatasourcesByDataset(final String datasetUrn, int firstResult, int maxResults) {
        dispatcher.execute(new GetDatasourcesByDatasetAction(datasetUrn), new WaitingAsyncCallbackHandlingError<GetDatasourcesByDatasetResult>(DatasetDatasourcesTabPresenter.this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetDatasourcesTabPresenter.this, datasetUrn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasourcesByDatasetResult result) {
                setDataset(result.getDatasetVersion());
                SetDatasetEvent.fire(DatasetDatasourcesTabPresenter.this, result.getDatasetVersion());
                getView().setDatasources(datasetUrn, result.getDatasourcesList());
            }
        });
    }

    @Override
    public void deleteDatasources(List<String> datasourcesUrns, boolean deleteAttributes) {
        dispatcher.execute(new DeleteDatasourcesAction(datasourcesUrns, deleteAttributes), new WaitingAsyncCallbackHandlingError<DeleteDatasourcesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                retrieveDatasourcesByDataset(datasetVersion.getUrn(), 0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
            }

            @Override
            public void onWaitSuccess(DeleteDatasourcesResult result) {
                fireSuccessMessage(getMessages().datasourcesDeleted(String.valueOf(result.getObservationsDeleted())));
                retrieveDatasourcesByDataset(datasetVersion.getUrn(), 0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
            }
        });
    }

    @Override
    public void deleteAllDatasourcesNotUsed(String datasetUrn, boolean deleteAttributes) {
        dispatcher.execute(new DeleteDatasourcesNotUsedAction(datasetUrn, deleteAttributes), new WaitingAsyncCallbackHandlingError<DeleteDatasourcesNotUsedResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDatasourcesNotUsedResult result) {
                if (result.getDatasourcesDeleted().isEmpty()) {
                    fireSuccessMessage(getMessages().datasourcesNotUsedEmptyDeleted());
                } else {
                    fireSuccessMessage(getMessages().datasourcesNotUsedDeleted(StringUtils.join(result.getDatasourcesDeleted().toArray(), ',')));
                }
                retrieveDatasourcesByDataset(datasetVersion.getUrn(), 0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
            }
        });
    }

    @Override
    public void retrieveAlternativeCodelistsForVariable(final String dimensionId, String variableUrn, int firstResult, int maxResults, SrmExternalResourceRestCriteria criteria) {
        dispatcher.execute(new GetCodelistsWithVariableAction(variableUrn, firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCodelistsWithVariableResult>(this) {

            @Override
            public void onWaitSuccess(GetCodelistsWithVariableResult result) {
                getView().setCodelistsInVariable(dimensionId, result);
            }
        });
    }

    @Override
    public void retrieveDimensionVariablesForDataset(final String datasetVersionUrn) {
        dispatcher.execute(new GetDatasetDimensionsVariableMappingAction(datasetVersionUrn), new WaitingAsyncCallbackHandlingError<GetDatasetDimensionsVariableMappingResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetDimensionsVariableMappingResult result) {
                getView().setDimensionVariablesForDataset(datasetVersionUrn, result.getDimensionsMapping());
            }
        });
    }

    //
    // IMPORTATION
    //

    @Override
    public void datasourcesImportationSucceed(String fileName) {
        ShowMessageEvent.fireSuccessMessage(DatasetDatasourcesTabPresenter.this, getMessages().datasourcesImportationPlanned());
        placeManager.revealCurrentPlace();
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void datasourcesImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(DatasetDatasourcesTabPresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
    }

    @Override
    public void saveDataset(DatasetVersionDto datasetVersionDto) {
        dispatcher.execute(new SaveDatasetVersionAction(datasetVersionDto, datasetVersionDto.getStatisticalOperation().getCode()),
                new WaitingAsyncCallbackHandlingError<SaveDatasetVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(SaveDatasetVersionResult result) {
                        fireSuccessMessage(getMessages().datasetSaved());
                        getView().setDatasetVersion(result.getSavedDatasetVersion());

                        SetDatasetEvent.fire(DatasetDatasourcesTabPresenter.this, result.getSavedDatasetVersion());
                    }
                });
    }

    @Override
    public void createDatabaseDatasource(String urn, String tablename) {
        dispatcher.execute(new CreateDatabaseDatasourceAction(urn, tablename), new WaitingAsyncCallbackHandlingError<CreateDatabaseDatasourceResult>(this) {

            @Override
            public void onWaitSuccess(CreateDatabaseDatasourceResult result) {
                ShowMessageEvent.fireSuccessMessage(DatasetDatasourcesTabPresenter.this, getMessages().databaseDatasourceCreated());
                retrieveDatasourcesByDataset(datasetVersion.getUrn(), 0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS);
            }
        });
    }

    @Override
    public void exportDatasources(String urn) {
        dispatcher.execute(new ExportDatasourcesAction(urn, DecimalSeparatorTypeEnum.POINT), new WaitingAsyncCallbackHandlingError<ExportDatasourcesResult>(this) {

            @Override
            public void onWaitSuccess(ExportDatasourcesResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });
    }
}
