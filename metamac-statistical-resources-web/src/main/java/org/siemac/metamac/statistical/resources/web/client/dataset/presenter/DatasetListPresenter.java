package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;
import static org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils.showMessageAfterMultipleResourcesLifeCycleUpdate;

import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.base.presenter.StatisticalResourceBaseListPresenter;
import org.siemac.metamac.statistical.resources.web.client.base.utils.SiemacMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationPresenter;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionsProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionsProcStatusAction.Builder;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionsProcStatusResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderSchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderSchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetGeographicalGranularitiesListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetGeographicalGranularitiesListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesListResult;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class DatasetListPresenter extends StatisticalResourceBaseListPresenter<DatasetListPresenter.DatasetListView, DatasetListPresenter.DatasetListProxy> implements DatasetListUiHandlers {

    private final PlaceManager placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetsListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetListProxy extends Proxy<DatasetListPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return getConstants().breadcrumbDatasets();
    }

    public interface DatasetListView extends StatisticalResourceBaseListPresenter.StatisticalResourceBaseListView, HasUiHandlers<DatasetListUiHandlers> {

        void setDatasetPaginatedList(GetDatasetVersionsResult datasetsPaginatedList);
        void setDsdsForRelatedDsd(GetDsdsPaginatedListResult result);
        void setStatisticalOperationsForDsdSelection(List<ExternalItemDto> results, ExternalItemDto defaultSelected);

        // Related resources
        void setGeographicGranularitiesForSearchSection(GetGeographicalGranularitiesListResult result);
        void setTemporalGranularitiesForSearchSection(GetTemporalGranularitiesListResult result);
        void setStatisticalOperationsForDsdSelectionInSearchSection(List<ExternalItemDto> results);
        void setDsdsForSearchSection(GetDsdsPaginatedListResult result);
        void setStatisticalOperationsForSearchSection(GetStatisticalOperationsPaginatedListResult result);

        // data provider y data provider schemes
        void setDataProviderSchemesForField(GetDataProviderSchemesPaginatedListResult result, SiemacMetadataExternalField field);
        void setDataProviderForField(GetDataProviderPaginatedListResult result, SiemacMetadataExternalField field);
        
        // Search
        void clearSearchSection();
        DatasetVersionWebCriteria getDatasetVersionWebCriteria();
    }

    @Inject
    public DatasetListPresenter(EventBus eventBus, DatasetListView datasetListView, DatasetListProxy datasetListProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, datasetListView, datasetListProxy, dispatcher);
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, OperationPresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(this, getConstants().datasets());
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        if (!StringUtils.isBlank(operationCode)) {
            String operationUrn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_OPERATION_PREFIX, operationCode);
            if (!CommonUtils.isUrnFromSelectedStatisticalOperation(operationUrn)) {
                retrieveOperation(operationUrn);
            } else {
                loadInitialData();
            }
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
        getView().clearSearchSection();

        DatasetVersionWebCriteria datasetVersionWebCriteria = new DatasetVersionWebCriteria();
        datasetVersionWebCriteria.setStatisticalOperationUrn(StatisticalResourcesDefaults.getSelectedStatisticalOperation().getUrn());

        retrieveDatasets(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, datasetVersionWebCriteria);
    }

    @Override
    public void retrieveDatasets(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        dispatcher.execute(new GetDatasetVersionsAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetDatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetVersionsResult result) {
                getView().setDatasetPaginatedList(result);
            }
        });
    }

    @Override
    public void createDataset(DatasetVersionDto datasetDto) {
        dispatcher.execute(new SaveDatasetVersionAction(datasetDto, StatisticalResourcesDefaults.getSelectedStatisticalOperation().getCode()),
                new WaitingAsyncCallbackHandlingError<SaveDatasetVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(SaveDatasetVersionResult result) {
                        fireSuccessMessage(getMessages().datasetSaved());
                        retrieveDatasets(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, getView().getDatasetVersionWebCriteria());
                    }
                });
    }

    @Override
    public void deleteDatasets(List<String> urnsFromSelected) {
        dispatcher.execute(new DeleteDatasetVersionsAction(urnsFromSelected), new WaitingAsyncCallbackHandlingError<DeleteDatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDatasetVersionsResult result) {
                fireSuccessMessage(getMessages().datasetsDeleted());
                retrieveDatasets(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, getView().getDatasetVersionWebCriteria());
            }
            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                retrieveDatasets(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, getView().getDatasetVersionWebCriteria());
            }
        });
    }

    @Override
    public void retrieveDsdsForRelatedDsd(int firstResult, int maxResults, DsdWebCriteria criteria) {
        dispatcher.execute(new GetDsdsPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetDsdsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetDsdsPaginatedListResult result) {
                getView().setDsdsForRelatedDsd(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForDsdSelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForDsdSelection(result.getOperationsList(), StatisticalResourcesDefaults.getSelectedStatisticalOperation());
            }
        });
    }

    //
    // LIFECYCLE
    //

    @Override
    public void sendToProductionValidation(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        UpdateDatasetVersionsProcStatusAction action = new UpdateDatasetVersionsProcStatusAction(datasetVersionBaseDtos, LifeCycleActionEnum.SEND_TO_PRODUCTION_VALIDATION);
        updateDatasetVersionProcStatus(action, getMessages().lifeCycleResourcesSentToProductionValidation());
    }

    @Override
    public void sendToDiffusionValidation(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        UpdateDatasetVersionsProcStatusAction action = new UpdateDatasetVersionsProcStatusAction(datasetVersionBaseDtos, LifeCycleActionEnum.SEND_TO_DIFFUSION_VALIDATION);
        updateDatasetVersionProcStatus(action, getMessages().lifeCycleResourcesSentToDiffusionValidation());
    }

    @Override
    public void rejectValidation(List<DatasetVersionBaseDto> datasetVersionBaseDtos, String reasonOfRejection) {
        UpdateDatasetVersionsProcStatusAction.Builder actionbuilder = new Builder(datasetVersionBaseDtos, LifeCycleActionEnum.REJECT_VALIDATION).reasonOfRejection(reasonOfRejection);
        updateDatasetVersionProcStatus(actionbuilder.build(), getMessages().lifeCycleResourcesRejectValidation());
    }

    @Override
    public void publish(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        UpdateDatasetVersionsProcStatusAction action = new UpdateDatasetVersionsProcStatusAction(datasetVersionBaseDtos, LifeCycleActionEnum.PUBLISH);
        updateDatasetVersionProcStatus(action, getMessages().lifeCycleResourcesPublish());
    }

    @Override
    public void version(List<DatasetVersionBaseDto> datasetVersionBaseDtos, VersionTypeEnum versionType) {
        Builder builder = new UpdateDatasetVersionsProcStatusAction.Builder(datasetVersionBaseDtos, LifeCycleActionEnum.VERSION);
        builder.versionType(versionType);
        updateDatasetVersionProcStatus(builder.build(), getMessages().lifeCycleResourcesVersion());
    }

    private void updateDatasetVersionProcStatus(UpdateDatasetVersionsProcStatusAction action, final String successMessage) {
        dispatcher.execute(action, new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionsProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateDatasetVersionsProcStatusResult result) {
                showMessageAfterMultipleResourcesLifeCycleUpdate(DatasetListPresenter.this, result.getNotificationException(), successMessage);
                retrieveDatasets(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, getView().getDatasetVersionWebCriteria());
            }

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                retrieveDatasets(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, getView().getDatasetVersionWebCriteria());
            }
        });
    }

    //
    // IMPORTATION
    //

    @Override
    public void datasourcesImportationSucceed(String fileName) {
        ShowMessageEvent.fireSuccessMessage(DatasetListPresenter.this, getMessages().datasourcesImportationPlanned());
    }

    @Override
    public void datasourcesImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(DatasetListPresenter.this, errorMessage);
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveGeographicGranularitiesForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetGeographicalGranularitiesListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetGeographicalGranularitiesListResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalGranularitiesListResult result) {
                getView().setGeographicGranularitiesForSearchSection(result);
            }
        });
    }

    @Override
    public void retrieveTemporalGranularitiesForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetTemporalGranularitiesListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetTemporalGranularitiesListResult>(this) {

            @Override
            public void onWaitSuccess(GetTemporalGranularitiesListResult result) {
                getView().setTemporalGranularitiesForSearchSection(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForDsdSelectionInSearchSection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForDsdSelectionInSearchSection(result.getOperationsList());
            }
        });
    }

    @Override
    public void retrieveDsdsForSearchSection(int firstResult, int maxResults, DsdWebCriteria criteria) {
        dispatcher.execute(new GetDsdsPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetDsdsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetDsdsPaginatedListResult result) {
                getView().setDsdsForSearchSection(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(firstResult, maxResults, criteria),
                new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

                    @Override
                    public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                        getView().setStatisticalOperationsForSearchSection(result);
                    }
                });
    }

    @Override
    public void retrieveDataProviderSchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria, final SiemacMetadataExternalField field) {
        dispatcher.execute(new GetDataProviderSchemesPaginatedListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetDataProviderSchemesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetDataProviderSchemesPaginatedListResult result) {
                getView().setDataProviderSchemesForField(result, field);
            }
        });
    }

    @Override
    public void retrieveDataProviderUnits(int firstResult, int maxResults, SrmItemRestCriteria webCriteria, final SiemacMetadataExternalField field) {
        dispatcher.execute(new GetDataProviderPaginatedListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetDataProviderPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetDataProviderPaginatedListResult result) {
                getView().setDataProviderForField(result, field);
            }
        });
    }
    
    //
    // NAVIGATION
    //

    @Override
    public void goToDataset(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeDatasetPlaceRequest(urn));
        }
    }
}
