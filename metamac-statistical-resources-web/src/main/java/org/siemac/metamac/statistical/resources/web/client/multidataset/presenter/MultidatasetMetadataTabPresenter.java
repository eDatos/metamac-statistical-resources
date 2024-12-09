package org.siemac.metamac.statistical.resources.web.client.multidataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.presenter.StatisticalResourceMetadataBasePresenter;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.client.events.RequestMultidatasetVersionsReloadEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetMultidatasetEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedMultidatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers.MultidatasetMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.base.UpdateGeoCacheRelatedResourceAction;
import org.siemac.metamac.statistical.resources.web.shared.base.UpdateGeoCacheRelatedResourceResult;
import org.siemac.metamac.statistical.resources.web.shared.criteria.MultidatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.DeleteMultidatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.DeleteMultidatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.SaveMultidatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.SaveMultidatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionProcStatusAction.Builder;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionProcStatusResult;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

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

public class MultidatasetMetadataTabPresenter
        extends
            StatisticalResourceMetadataBasePresenter<MultidatasetMetadataTabPresenter.MultidatasetMetadataTabView, MultidatasetMetadataTabPresenter.MultidatasetMetadataTabProxy>
        implements
            MultidatasetMetadataTabUiHandlers {

    public interface MultidatasetMetadataTabView extends StatisticalResourceMetadataBasePresenter.StatisticalResourceMetadataBaseView, HasUiHandlers<MultidatasetMetadataTabUiHandlers> {

        void setMultidataset(MultidatasetVersionDto multidatasetDto);

        void setMultidatasetsForReplaces(GetMultidatasetVersionsResult result);

        void setStatisticalOperationsForReplacesSelection(List<ExternalItemDto> operationsList, ExternalItemDto selectedStatisticalOperation);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.multidatasetMetadataPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface MultidatasetMetadataTabProxy extends Proxy<MultidatasetMetadataTabPresenter>, Place {
    }

    @Inject
    public MultidatasetMetadataTabPresenter(EventBus eventBus, MultidatasetMetadataTabView view, MultidatasetMetadataTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, dispatcher, placeManager);
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbMetadata();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MultidatasetPresenter.TYPE_SetContextAreaMultidataset, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        String multidatasetCode = PlaceRequestUtils.getMultidatasetParamFromUrl(placeManager);
        if (!StringUtils.isBlank(operationCode) && !StringUtils.isBlank(multidatasetCode)) {
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
        String multidatasetCode = PlaceRequestUtils.getMultidatasetParamFromUrl(placeManager);
        String multidatasetUrn = CommonUtils.generateMultidatasetUrn(multidatasetCode);
        retrieveMultidataset(multidatasetUrn);
    }

    private void retrieveMultidataset(final String urn) {
        dispatcher.execute(new GetMultidatasetVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetMultidatasetVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedMultidatasetWarningMessageEvent.fire(MultidatasetMetadataTabPresenter.this, urn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetMultidatasetVersionResult result) {
                getView().setMultidataset(result.getMultidatasetVersionDto());
                SetMultidatasetEvent.fire(MultidatasetMetadataTabPresenter.this, result.getMultidatasetVersionDto());
            }
        });
    }

    @Override
    public void saveMultidataset(MultidatasetVersionDto multidatasetDto) {
        dispatcher.execute(new SaveMultidatasetVersionAction(multidatasetDto, StatisticalResourcesDefaults.getSelectedStatisticalOperation()),
                new WaitingAsyncCallbackHandlingError<SaveMultidatasetVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(SaveMultidatasetVersionResult result) {
                        fireSuccessMessage(getMessages().multidatasetSaved());
                        getView().setMultidataset(result.getSavedMultidatasetVersion());

                        SetMultidatasetEvent.fire(MultidatasetMetadataTabPresenter.this, result.getSavedMultidatasetVersion());
                    }
                });
    }

    @Override
    public void deleteMultidataset(String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new DeleteMultidatasetVersionsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteMultidatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteMultidatasetVersionsResult result) {
                fireSuccessMessage(getMessages().multidatasetDeleted());
                goToMultidatasetList();
            }
        });
    }

    //
    // LIFE CYCLE
    //

    @Override
    public void sendToProductionValidation(MultidatasetVersionDto multidatasetVersionDto) {
        dispatcher.execute(new UpdateMultidatasetVersionProcStatusAction(multidatasetVersionDto, LifeCycleActionEnum.SEND_TO_PRODUCTION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdateMultidatasetVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateMultidatasetVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToProductionValidation());
                        RequestMultidatasetVersionsReloadEvent.fire(MultidatasetMetadataTabPresenter.this, result.getMultidatasetVersionDto().getUrn());
                        getView().setMultidataset(result.getMultidatasetVersionDto());
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(MultidatasetVersionDto multidatasetVersionDto) {
        dispatcher.execute(new UpdateMultidatasetVersionProcStatusAction(multidatasetVersionDto, LifeCycleActionEnum.SEND_TO_DIFFUSION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdateMultidatasetVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateMultidatasetVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToDiffusionValidation());
                        RequestMultidatasetVersionsReloadEvent.fire(MultidatasetMetadataTabPresenter.this, result.getMultidatasetVersionDto().getUrn());
                        getView().setMultidataset(result.getMultidatasetVersionDto());
                    }
                });
    }

    @Override
    public void rejectValidation(MultidatasetVersionDto multidatasetVersionDto, String reasonOfRejection) {
        UpdateMultidatasetVersionProcStatusAction.Builder builder = new Builder(multidatasetVersionDto, LifeCycleActionEnum.REJECT_VALIDATION).reasonOfRejection(reasonOfRejection);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdateMultidatasetVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateMultidatasetVersionProcStatusResult result) {
                showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceRejectValidation());
                RequestMultidatasetVersionsReloadEvent.fire(MultidatasetMetadataTabPresenter.this, result.getMultidatasetVersionDto().getUrn());
                getView().setMultidataset(result.getMultidatasetVersionDto());
            }
        });
    }

    @Override
    public void publish(MultidatasetVersionDto multidatasetVersionDto) {
        dispatcher.execute(new UpdateMultidatasetVersionProcStatusAction(multidatasetVersionDto, LifeCycleActionEnum.PUBLISH),
                new WaitingAsyncCallbackHandlingError<UpdateMultidatasetVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateMultidatasetVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourcePublish());
                        RequestMultidatasetVersionsReloadEvent.fire(MultidatasetMetadataTabPresenter.this, result.getMultidatasetVersionDto().getUrn());
                        getView().setMultidataset(result.getMultidatasetVersionDto());
                    }
                });
    }

    // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
    // @Override
    // public void resendStreamMessage(MultidatasetVersionDto multidatasetVersionDto) {
    // dispatcher.execute(new ResendStreamMessageAction(multidatasetVersionDto), new WaitingAsyncCallbackHandlingError<ResendStreamMessageResult>(this) {
    //
    // @Override
    // public void onWaitSuccess(ResendStreamMessageResult result) {
    // RequestMultidatasetVersionsReloadEvent.fire(MultidatasetMetadataTabPresenter.this, result.getLifeCycleStatisticalResourceResultDto().getUrn());
    // getView().setMultidataset((MultidatasetVersionDto) result.getLifeCycleStatisticalResourceResultDto());
    // }
    // });
    // }

    @Override
    public void version(MultidatasetVersionDto multidataset, VersionTypeEnum versionType) {
        Builder builder = new Builder(multidataset, LifeCycleActionEnum.VERSION);
        builder.versionType(versionType);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdateMultidatasetVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateMultidatasetVersionProcStatusResult result) {
                fireSuccessMessage(getMessages().lifeCycleResourceVersion());
                RequestMultidatasetVersionsReloadEvent.fire(MultidatasetMetadataTabPresenter.this, result.getMultidatasetVersionDto().getUrn());
            }
        });
    }

    @Override
    public void previewData(MultidatasetVersionDto multidatasetVersionDto) {
        try {
            String url = MetamacPortalWebUtils.buildMultidatasetVersionUrl(multidatasetVersionDto, CommonUtils.getMetamacPortalBaseUrl());
            Window.open(url, "_blank", "");
        } catch (Exception e) {
            ShowMessageEvent.fireErrorMessage(this, e);
        }
    }

    private void showMessageAfterResourceLifeCycleUpdate(UpdateMultidatasetVersionProcStatusResult result, String message) {
        CommonUtils.showMessageAfterResourceLifeCycleUpdate(MultidatasetMetadataTabPresenter.this, result.getNotificationException(), message);
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveMultidatasetsForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria) {

        MultidatasetVersionWebCriteria multidatasetVersionWebCriteria = new MultidatasetVersionWebCriteria(criteria.getCriteria());
        multidatasetVersionWebCriteria.setOnlyLastVersion(criteria.isOnlyLastVersion());
        multidatasetVersionWebCriteria.setStatisticalOperationUrn(criteria.getStatisticalOperationUrn());
        multidatasetVersionWebCriteria.setProcStatus(ProcStatusEnum.PUBLISHED);

        dispatcher.execute(new GetMultidatasetVersionsAction(firstResult, maxResults, multidatasetVersionWebCriteria), new WaitingAsyncCallbackHandlingError<GetMultidatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetMultidatasetVersionsResult result) {
                getView().setMultidatasetsForReplaces(result);
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

    @Override
    public void updateGeoCacheRelatedResources(MultidatasetVersionDto publication) {
        dispatcher.execute(new UpdateGeoCacheRelatedResourceAction(publication.getUrn(), StatisticalResourceTypeEnum.MULTIDATASET),
                new WaitingAsyncCallbackHandlingError<UpdateGeoCacheRelatedResourceResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateGeoCacheRelatedResourceResult result) {
                        fireSuccessMessage(getMessages().publicationUpdateGeoCacheBackgroundInProgress());
                    }
                });
    }

    //
    // NAVIGATION
    //

    private void goToMultidatasetList() {
        placeManager.revealRelativePlace(-2);
    }
}
