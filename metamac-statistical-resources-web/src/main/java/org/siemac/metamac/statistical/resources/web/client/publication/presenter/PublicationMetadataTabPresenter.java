package org.siemac.metamac.statistical.resources.web.client.publication.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.presenter.StatisticalResourceMetadataBasePresenter;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.client.events.RequestPublicationVersionsReloadEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetPublicationEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedPublicationWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendStreamMessageAction;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendStreamMessageResult;
import org.siemac.metamac.statistical.resources.web.shared.criteria.PublicationVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.DeletePublicationVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.DeletePublicationVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.SavePublicationVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.SavePublicationVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdateGeoCacheRelatedResourceAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdateGeoCacheRelatedResourceResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationVersionProcStatusAction.Builder;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationVersionProcStatusResult;
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

public class PublicationMetadataTabPresenter
        extends
            StatisticalResourceMetadataBasePresenter<PublicationMetadataTabPresenter.PublicationMetadataTabView, PublicationMetadataTabPresenter.PublicationMetadataTabProxy>
        implements
            PublicationMetadataTabUiHandlers {

    public interface PublicationMetadataTabView extends StatisticalResourceMetadataBasePresenter.StatisticalResourceMetadataBaseView, HasUiHandlers<PublicationMetadataTabUiHandlers> {

        void setPublication(PublicationVersionDto publicationDto);

        void setPublicationsForReplaces(GetPublicationVersionsResult result);

        void setStatisticalOperationsForReplacesSelection(List<ExternalItemDto> operationsList, ExternalItemDto selectedStatisticalOperation);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.publicationMetadataPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface PublicationMetadataTabProxy extends Proxy<PublicationMetadataTabPresenter>, Place {
    }

    @Inject
    public PublicationMetadataTabPresenter(EventBus eventBus, PublicationMetadataTabView view, PublicationMetadataTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, dispatcher, placeManager);
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbMetadata();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, PublicationPresenter.TYPE_SetContextAreaPublication, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        String publicationCode = PlaceRequestUtils.getPublicationParamFromUrl(placeManager);
        if (!StringUtils.isBlank(operationCode) && !StringUtils.isBlank(publicationCode)) {
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
        String publicationCode = PlaceRequestUtils.getPublicationParamFromUrl(placeManager);
        String publicationUrn = CommonUtils.generatePublicationUrn(publicationCode);
        retrievePublication(publicationUrn);
    }

    private void retrievePublication(final String urn) {
        dispatcher.execute(new GetPublicationVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetPublicationVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedPublicationWarningMessageEvent.fire(PublicationMetadataTabPresenter.this, urn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetPublicationVersionResult result) {
                getView().setPublication(result.getPublicationVersionDto());
                SetPublicationEvent.fire(PublicationMetadataTabPresenter.this, result.getPublicationVersionDto());
            }
        });
    }

    @Override
    public void savePublication(PublicationVersionDto publicationDto) {
        dispatcher.execute(new SavePublicationVersionAction(publicationDto, StatisticalResourcesDefaults.getSelectedStatisticalOperation()),
                new WaitingAsyncCallbackHandlingError<SavePublicationVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(SavePublicationVersionResult result) {
                        fireSuccessMessage(getMessages().publicationSaved());
                        getView().setPublication(result.getSavedPublicationVersion());

                        SetPublicationEvent.fire(PublicationMetadataTabPresenter.this, result.getSavedPublicationVersion());
                    }
                });
    }

    @Override
    public void deletePublication(String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new DeletePublicationVersionsAction(urns), new WaitingAsyncCallbackHandlingError<DeletePublicationVersionsResult>(this) {

            @Override
            public void onWaitSuccess(DeletePublicationVersionsResult result) {
                fireSuccessMessage(getMessages().publicationDeleted());
                goToPublicationList();
            }
        });
    }

    //
    // LIFE CYCLE
    //

    @Override
    public void sendToProductionValidation(PublicationVersionDto publicationVersionDto) {
        dispatcher.execute(new UpdatePublicationVersionProcStatusAction(publicationVersionDto, LifeCycleActionEnum.SEND_TO_PRODUCTION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdatePublicationVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdatePublicationVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToProductionValidation());
                        RequestPublicationVersionsReloadEvent.fire(PublicationMetadataTabPresenter.this, result.getPublicationVersionDto().getUrn());
                        getView().setPublication(result.getPublicationVersionDto());
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(PublicationVersionDto publicationVersionDto) {
        dispatcher.execute(new UpdatePublicationVersionProcStatusAction(publicationVersionDto, LifeCycleActionEnum.SEND_TO_DIFFUSION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdatePublicationVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdatePublicationVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToDiffusionValidation());
                        RequestPublicationVersionsReloadEvent.fire(PublicationMetadataTabPresenter.this, result.getPublicationVersionDto().getUrn());
                        getView().setPublication(result.getPublicationVersionDto());
                    }
                });
    }

    @Override
    public void rejectValidation(PublicationVersionDto publicationVersionDto, String reasonOfRejection) {
        UpdatePublicationVersionProcStatusAction.Builder builder = new Builder(publicationVersionDto, LifeCycleActionEnum.REJECT_VALIDATION).reasonOfRejection(reasonOfRejection);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdatePublicationVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdatePublicationVersionProcStatusResult result) {
                showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceRejectValidation());
                RequestPublicationVersionsReloadEvent.fire(PublicationMetadataTabPresenter.this, result.getPublicationVersionDto().getUrn());
                getView().setPublication(result.getPublicationVersionDto());
            }
        });
    }

    @Override
    public void publish(PublicationVersionDto publicationVersionDto) {
        dispatcher.execute(new UpdatePublicationVersionProcStatusAction(publicationVersionDto, LifeCycleActionEnum.PUBLISH),
                new WaitingAsyncCallbackHandlingError<UpdatePublicationVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdatePublicationVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourcePublish());
                        RequestPublicationVersionsReloadEvent.fire(PublicationMetadataTabPresenter.this, result.getPublicationVersionDto().getUrn());
                        getView().setPublication(result.getPublicationVersionDto());
                    }
                });
    }

    @Override
    public void resendStreamMessage(PublicationVersionDto publicationVersionDto) {
        dispatcher.execute(new ResendStreamMessageAction(publicationVersionDto), new WaitingAsyncCallbackHandlingError<ResendStreamMessageResult>(this) {

            @Override
            public void onWaitSuccess(ResendStreamMessageResult result) {
                RequestPublicationVersionsReloadEvent.fire(PublicationMetadataTabPresenter.this, result.getLifeCycleStatisticalResourceResultDto().getUrn());
                getView().setPublication((PublicationVersionDto) result.getLifeCycleStatisticalResourceResultDto());
            }
        });
    }

    @Override
    public void version(PublicationVersionDto publication, VersionTypeEnum versionType) {
        Builder builder = new Builder(publication, LifeCycleActionEnum.VERSION);
        builder.versionType(versionType);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdatePublicationVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdatePublicationVersionProcStatusResult result) {
                fireSuccessMessage(getMessages().lifeCycleResourceVersion());
                RequestPublicationVersionsReloadEvent.fire(PublicationMetadataTabPresenter.this, result.getPublicationVersionDto().getUrn());
            }
        });
    }

    @Override
    public void previewData(PublicationVersionDto publicationVersionDto) {
        try {
            String url = MetamacPortalWebUtils.buildPublicationVersionUrl(publicationVersionDto, CommonUtils.getMetamacPortalBaseUrl());
            Window.open(url, "_blank", "");
        } catch (Exception e) {
            ShowMessageEvent.fireErrorMessage(this, e);
        }
    }

    private void showMessageAfterResourceLifeCycleUpdate(UpdatePublicationVersionProcStatusResult result, String message) {
        CommonUtils.showMessageAfterResourceLifeCycleUpdate(PublicationMetadataTabPresenter.this, result.getNotificationException(), message);
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrievePublicationsForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria) {

        PublicationVersionWebCriteria publicationVersionWebCriteria = new PublicationVersionWebCriteria(criteria.getCriteria());
        publicationVersionWebCriteria.setOnlyLastVersion(criteria.isOnlyLastVersion());
        publicationVersionWebCriteria.setStatisticalOperationUrn(criteria.getStatisticalOperationUrn());
        publicationVersionWebCriteria.setProcStatus(ProcStatusEnum.PUBLISHED);

        dispatcher.execute(new GetPublicationVersionsAction(firstResult, maxResults, publicationVersionWebCriteria), new WaitingAsyncCallbackHandlingError<GetPublicationVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetPublicationVersionsResult result) {
                getView().setPublicationsForReplaces(result);
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
    public void updateGeoCacheRelatedResources(PublicationVersionDto publication) {
        dispatcher.execute(new UpdateGeoCacheRelatedResourceAction(publication.getUrn(), StatisticalResourceTypeEnum.COLLECTION),
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

    private void goToPublicationList() {
        placeManager.revealRelativePlace(-2);
    }
}
