package org.siemac.metamac.statistical.resources.web.client.publication.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationStructureDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.enums.PublicationTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectPublicationTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetPublicationEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedPublicationWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationStructureTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.base.GetLatestResourceVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.base.GetLatestResourceVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.criteria.StatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetsAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.DeletePublicationStructureElementAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.DeletePublicationStructureElementResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationStructureAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationStructureResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationsAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetPublicationsResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.SavePublicationStructureElementAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.SavePublicationStructureElementResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationStructureElementLocationAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationStructureElementLocationResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueriesAction;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueriesResult;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

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

public class PublicationStructureTabPresenter extends Presenter<PublicationStructureTabPresenter.PublicationStructureTabView, PublicationStructureTabPresenter.PublicationStructureTabProxy>
        implements
            PublicationStructureTabUiHandlers {

    private DispatchAsync dispatcher;
    private PlaceManager  placeManager;

    public interface PublicationStructureTabView extends View, HasUiHandlers<PublicationStructureTabUiHandlers> {

        void setPublicationStructure(PublicationStructureDto publicationStructureDto);
        void setPublicationStructure(PublicationStructureDto publicationStructureDto, NameableStatisticalResourceDto selectedElement);

        // Related resources
        void setDatasetsForCubes(GetDatasetsResult result);
        void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result);
        void setQueriesForCubes(GetQueriesResult result);
        void setPublicationsForCubes(GetPublicationsResult result);
        void setStatisticalOperationsForQuerySelection(GetStatisticalOperationsPaginatedListResult result);
        void setStatisticalOperationsForCollectionSelection(GetStatisticalOperationsPaginatedListResult result);

        void setMultidatasetsForCubes(GetMultidatasetsResult result);
        void setStatisticalOperationsForMultidatasetSelection(GetStatisticalOperationsPaginatedListResult result);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.publicationStructurePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface PublicationStructureTabProxy extends Proxy<PublicationStructureTabPresenter>, Place {
    }

    @Inject
    public PublicationStructureTabPresenter(EventBus eventBus, PublicationStructureTabView view, PublicationStructureTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbStructure();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, PublicationPresenter.TYPE_SetContextAreaPublication, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectPublicationTabEvent.fire(this, PublicationTabTypeEnum.STRUCTURE);
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
        String publicationVersionUrn = CommonUtils.generatePublicationUrn(publicationCode);
        retrievePublicationStructure(publicationVersionUrn);
    }

    private void retrievePublicationStructure(final String publicationVersionUrn) {
        dispatcher.execute(new GetPublicationStructureAction(publicationVersionUrn), new WaitingAsyncCallbackHandlingError<GetPublicationStructureResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedPublicationWarningMessageEvent.fire(PublicationStructureTabPresenter.this, publicationVersionUrn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetPublicationStructureResult result) {
                SetPublicationEvent.fire(PublicationStructureTabPresenter.this, result.getPublicationVersion());
                getView().setPublicationStructure(result.getPublicationStructureDto());
            }
        });
    }

    @Override
    public void saveElement(String publicationVersionUrn, NameableStatisticalResourceDto element) {
        dispatcher.execute(new SavePublicationStructureElementAction(publicationVersionUrn, element), new WaitingAsyncCallbackHandlingError<SavePublicationStructureElementResult>(this) {

            @Override
            public void onWaitSuccess(SavePublicationStructureElementResult result) {
                getView().setPublicationStructure(result.getPublicationStructureDto(), result.getSavedElement());
            }
        });
    }

    @Override
    public void deleteElement(String publicationVersionUrn, String elementUrn) {
        dispatcher.execute(new DeletePublicationStructureElementAction(publicationVersionUrn, elementUrn), new WaitingAsyncCallbackHandlingError<DeletePublicationStructureElementResult>(this) {

            @Override
            public void onWaitSuccess(DeletePublicationStructureElementResult result) {
                getView().setPublicationStructure(result.getPublicationStructureDto());
            }
        });
    }

    @Override
    public void updateElementLocation(String publicationVersionUrn, String elementUrn, String parentTargetUrn, Long orderInLevel) {
        dispatcher.execute(new UpdatePublicationStructureElementLocationAction(publicationVersionUrn, elementUrn, parentTargetUrn, orderInLevel),
                new WaitingAsyncCallbackHandlingError<UpdatePublicationStructureElementLocationResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdatePublicationStructureElementLocationResult result) {
                        getView().setPublicationStructure(result.getPublicationStructureDto());
                    }
                });
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveDatasetsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
        dispatcher.execute(new GetDatasetsAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetDatasetsResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetsResult result) {
                getView().setDatasetsForCubes(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForDatasetSelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForDatasetSelection(result);
            }
        });
    }

    @Override
    public void retrieveQueriesForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
        dispatcher.execute(new GetQueriesAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetQueriesResult>(this) {

            @Override
            public void onWaitSuccess(GetQueriesResult result) {
                getView().setQueriesForCubes(result);
            }
        });
    }

    @Override
    public void retrieveCollectionsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
        dispatcher.execute(new GetPublicationsAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetPublicationsResult>(this) {

            @Override
            public void onWaitSuccess(GetPublicationsResult result) {
                getView().setPublicationsForCubes(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForQuerySelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForQuerySelection(result);
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForCollectionSelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForCollectionSelection(result);
            }
        });
    }


    @Override
    public void retrieveStatisticalOperationsForMultidatasetSelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForMultidatasetSelection(result);
            }
        });
    }

    @Override
    public void retrieveMultidatasetsForCubes(int firstResult, int maxResults, StatisticalResourceWebCriteria criteria) {
        dispatcher.execute(new GetMultidatasetsAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetMultidatasetsResult>(this) {

            @Override
            public void onWaitSuccess(GetMultidatasetsResult result) {
                getView().setMultidatasetsForCubes(result);
            }
        });
    }

    //
    // IMPORTATION
    //

    @Override
    public void resourceImportationSucceed(String fileName, String publicationVersionUrn) {
        ShowMessageEvent.fireSuccessMessage(PublicationStructureTabPresenter.this, getMessages().publicationVersionStructureImported(fileName));
        retrievePublicationStructure(publicationVersionUrn);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void resourceImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(PublicationStructureTabPresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }

    @Override
    public void goToLastVersion(final String urn) {
        if (!StringUtils.isBlank(urn)) {
            dispatcher.execute(new GetLatestResourceVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetLatestResourceVersionResult>(this) {

                @Override
                public void onWaitSuccess(GetLatestResourceVersionResult result) {
                    LifeCycleStatisticalResourceDto resourceVersion = result.getResourceVersion();
                    String operationUrn = resourceVersion.getStatisticalOperation().getUrn();
                    String resourceUrn = resourceVersion.getUrn();
                    if (resourceVersion instanceof DatasetVersionDto) {
                        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDatasetPlaceRequest(operationUrn, resourceUrn));
                    } else if (resourceVersion instanceof QueryVersionDto) {
                        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteQueryPlaceRequest(operationUrn, resourceUrn));
                    } else if (resourceVersion instanceof MultidatasetVersionDto) {
                        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteMultidatasetPlaceRequest(operationUrn, resourceUrn));
                    } else if (resourceVersion instanceof PublicationVersionDto) {
                        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCollectionPlaceRequest(operationUrn, resourceUrn));
                    }
                }
            });
        }
    }
}
