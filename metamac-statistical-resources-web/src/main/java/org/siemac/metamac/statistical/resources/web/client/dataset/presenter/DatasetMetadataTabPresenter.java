package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.presenter.StatisticalResourceMetadataBasePresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.client.events.RequestDatasetVersionsReloadEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetDatasetEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendStreamMessageAction;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendStreamMessageResult;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendXMessageAction;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendXMessageResult;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CopyDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CopyDatasetResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionMainCoveragesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionMainCoveragesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionProcStatusAction.Builder;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionProcStatusResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateGeographicCoverageVariableElementsCacheAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateGeographicCoverageVariableElementsCacheResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptSchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptSchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListResult;
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
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.google.gwt.user.client.Window;
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

public class DatasetMetadataTabPresenter extends StatisticalResourceMetadataBasePresenter<DatasetMetadataTabPresenter.DatasetMetadataTabView, DatasetMetadataTabPresenter.DatasetMetadataTabProxy>
        implements
            DatasetMetadataTabUiHandlers {

    public interface DatasetMetadataTabView extends StatisticalResourceMetadataBasePresenter.StatisticalResourceMetadataBaseView, HasUiHandlers<DatasetMetadataTabUiHandlers> {

        void setDataset(DatasetVersionDto datasetDto);

        void setMetadataVisualization(DatasetVersionDto datasetDto);

        // metadata fill methods
        void setDatasetsForReplaces(GetDatasetVersionsResult result);

        void setStatisticalOperationsForReplacesSelection(List<ExternalItemDto> results, ExternalItemDto defaultSelected);

        void setDatasetsMainCoverages(GetDatasetVersionMainCoveragesResult result);

        void setStatisticalOperationsForDsdSelection(List<ExternalItemDto> results, ExternalItemDto defaultSelected);

        void setDsdsForRelatedDsd(GetDsdsPaginatedListResult result);

        void setCodesForGeographicalGranularities(GetGeographicalGranularitiesListResult result);

        void setTemporalCodesForField(GetTemporalGranularitiesListResult result, DatasetMetadataExternalField field);

        void setConceptSchemesForStatisticalUnit(GetConceptSchemesPaginatedListResult result);

        void setConceptsForStatisticalUnit(GetConceptsPaginatedListResult result);

        void showInformationMessage(String title, String message);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetMetadataPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetMetadataTabProxy extends Proxy<DatasetMetadataTabPresenter>, Place {
    }

    @Inject
    public DatasetMetadataTabPresenter(EventBus eventBus, DatasetMetadataTabView view, DatasetMetadataTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, dispatcher, placeManager);
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbMetadata();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DatasetPresenter.TYPE_SetContextAreaDataset, this);
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
        retrieveDataset(datasetCode);
    }

    @Override
    public void retrieveDataset(String datasetIdentifier) {
        final String urn = CommonUtils.generateDatasetUrn(datasetIdentifier);
        dispatcher.execute(new GetDatasetVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetDatasetVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetMetadataTabPresenter.this, urn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasetVersionResult result) {
                getView().setDataset(result.getDatasetVersionDto());
                getView().setMetadataVisualization(result.getDatasetVersionDto());
                SetDatasetEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto());
            }
        });
    }

    @Override
    public void saveDataset(DatasetVersionDto datasetDto) {
        dispatcher.execute(new SaveDatasetVersionAction(datasetDto, datasetDto.getStatisticalOperation().getCode()), new WaitingAsyncCallbackHandlingError<SaveDatasetVersionResult>(this) {

            @Override
            public void onWaitSuccess(SaveDatasetVersionResult result) {
                fireSuccessMessage(getMessages().datasetSaved());
                getView().setDataset(result.getSavedDatasetVersion());
                getView().setMetadataVisualization(result.getSavedDatasetVersion());
                SetDatasetEvent.fire(DatasetMetadataTabPresenter.this, result.getSavedDatasetVersion());
            }
        });
    }

    @Override
    public void deleteDatasetVersion(String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new DeleteDatasetVersionsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteDatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDatasetVersionsResult result) {
                fireSuccessMessage(getMessages().datasetDeleted());
                goToDatasetList();
            }
        });
    }

    //
    // LIFE CYCLE
    //

    @Override
    public void sendToProductionValidation(DatasetVersionDto dataset) {
        dispatcher.execute(new UpdateDatasetVersionProcStatusAction(dataset, LifeCycleActionEnum.SEND_TO_PRODUCTION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateDatasetVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToProductionValidation());
                        RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto().getUrn());
                        getView().setDataset(result.getDatasetVersionDto());
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(DatasetVersionDto dataset) {
        dispatcher.execute(new UpdateDatasetVersionProcStatusAction(dataset, LifeCycleActionEnum.SEND_TO_DIFFUSION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateDatasetVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToDiffusionValidation());
                        RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto().getUrn());
                        getView().setDataset(result.getDatasetVersionDto());
                    }
                });
    }

    @Override
    public void rejectValidation(DatasetVersionDto dataset, String reasonOfRejection) {
        Builder builder = new Builder(dataset, LifeCycleActionEnum.REJECT_VALIDATION).reasonOfRejection(reasonOfRejection);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateDatasetVersionProcStatusResult result) {
                showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceRejectValidation());
                RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto().getUrn());
                getView().setDataset(result.getDatasetVersionDto());
            }
        });
    }

    @Override
    public void publish(DatasetVersionDto dataset) {
        dispatcher.execute(new UpdateDatasetVersionProcStatusAction(dataset, LifeCycleActionEnum.PUBLISH), new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateDatasetVersionProcStatusResult result) {
                showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourcePublish());
                RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto().getUrn());
                getView().setDataset(result.getDatasetVersionDto());
            }
        });
    }

    @Override
    public void resendStreamMessage(DatasetVersionDto dataset) {
        dispatcher.execute(new ResendStreamMessageAction(dataset), new WaitingAsyncCallbackHandlingError<ResendStreamMessageResult>(this) {

            @Override
            public void onWaitSuccess(ResendStreamMessageResult result) {
                RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getLifeCycleStatisticalResourceResultDto().getUrn());
                getView().setDataset((DatasetVersionDto) result.getLifeCycleStatisticalResourceResultDto());
            }
        });
    }

    @Override
    public void resendXMessage(DatasetVersionDto dataset) {
        dispatcher.execute(new ResendXMessageAction(dataset), new WaitingAsyncCallbackHandlingError<ResendXMessageResult>(this) {

            @Override
            public void onWaitSuccess(ResendXMessageResult result) {
                RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getLifeCycleStatisticalResourceResultDto().getUrn());
                getView().setDataset((DatasetVersionDto) result.getLifeCycleStatisticalResourceResultDto());
            }
        });
    }

    @Override
    public void updateGeocoverageCache(DatasetVersionDto dataset) {
        dispatcher.execute(new UpdateGeographicCoverageVariableElementsCacheAction.Builder().datasetVersionDto(dataset).build(),
                new WaitingAsyncCallbackHandlingError<UpdateGeographicCoverageVariableElementsCacheResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateGeographicCoverageVariableElementsCacheResult result) {
                        RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto().getUrn());
                        getView().setDataset(result.getDatasetVersionDto());
                    }
                });
    }

    @Override
    public void version(DatasetVersionDto dataset, VersionTypeEnum versionType) {
        Builder builder = new Builder(dataset, LifeCycleActionEnum.VERSION);
        builder.versionType(versionType);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateDatasetVersionProcStatusResult result) {
                getView().showInformationMessage(getMessages().datasetVersioning(), getMessages().datasetBackgroundVersionInProgress());
                RequestDatasetVersionsReloadEvent.fire(DatasetMetadataTabPresenter.this, result.getDatasetVersionDto().getUrn());
            }
        });
    }

    @Override
    public void previewData(DatasetVersionDto datasetVersionDto) {
        try {
            String url = MetamacPortalWebUtils.buildDatasetVersionUrl(datasetVersionDto, CommonUtils.getMetamacPortalBaseUrl());
            Window.open(url, "_blank", "");
        } catch (Exception e) {
            ShowMessageEvent.fireErrorMessage(this, e);
        }
    }

    private void showMessageAfterResourceLifeCycleUpdate(UpdateDatasetVersionProcStatusResult result, String message) {
        CommonUtils.showMessageAfterResourceLifeCycleUpdate(DatasetMetadataTabPresenter.this, result.getNotificationException(), message);
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveMainCoveragesForDatasetVersion(String datasetVersionUrn) {
        dispatcher.execute(new GetDatasetVersionMainCoveragesAction(datasetVersionUrn), new WaitingAsyncCallbackHandlingError<GetDatasetVersionMainCoveragesResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetVersionMainCoveragesResult result) {
                getView().setDatasetsMainCoverages(result);
            }
        });
    }

    @Override
    public void retrieveDatasetsForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria) {

        DatasetVersionWebCriteria versionableCriteria = new DatasetVersionWebCriteria(criteria.getCriteria());
        versionableCriteria.setOnlyLastVersion(criteria.isOnlyLastVersion());
        versionableCriteria.setStatisticalOperationUrn(criteria.getStatisticalOperationUrn());
        versionableCriteria.setProcStatus(ProcStatusEnum.PUBLISHED);

        dispatcher.execute(new GetDatasetVersionsAction(firstResult, maxResults, versionableCriteria), new WaitingAsyncCallbackHandlingError<GetDatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetVersionsResult result) {
                getView().setDatasetsForReplaces(result);
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
    public void retrieveCodesForGeographicalGranularities(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetGeographicalGranularitiesListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetGeographicalGranularitiesListResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalGranularitiesListResult result) {
                getView().setCodesForGeographicalGranularities(result);
            }
        });
    }

    @Override
    public void retrieveTemporalCodesForField(int firstResult, int maxResults, MetamacWebCriteria webCriteria, final DatasetMetadataExternalField field) {
        dispatcher.execute(new GetTemporalGranularitiesListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetTemporalGranularitiesListResult>(this) {

            @Override
            public void onWaitSuccess(GetTemporalGranularitiesListResult result) {
                getView().setTemporalCodesForField(result, field);
            }
        });
    }

    @Override
    public void retrieveConceptSchemesForStatisticalUnit(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetConceptSchemesPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetConceptSchemesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetConceptSchemesPaginatedListResult result) {
                getView().setConceptSchemesForStatisticalUnit(result);
            }
        });
    }

    @Override
    public void retrieveConceptsForStatisticalUnit(int firstResult, int maxResults, SrmItemRestCriteria criteria) {
        dispatcher.execute(new GetConceptsPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetConceptsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetConceptsPaginatedListResult result) {
                getView().setConceptsForStatisticalUnit(result);
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

    @Override
    public void retrieveStatisticalOperationsForReplacesSelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForReplacesSelection(result.getOperationsList(), StatisticalResourcesDefaults.getSelectedStatisticalOperation());
            }
        });
    }

    //
    // NAVIGATION
    //

    private void goToDatasetList() {
        placeManager.revealRelativePlace(-2);
    }

    @Override
    public void copyDataset(DatasetVersionDto dataset) {
        dispatcher.execute(new CopyDatasetAction(dataset), new WaitingAsyncCallbackHandlingError<CopyDatasetResult>(this) {

            @Override
            public void onWaitSuccess(CopyDatasetResult result) {
                fireSuccessMessage(getMessages().datasetCopied());
            }
        });
    }
}