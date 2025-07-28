package org.siemac.metamac.statistical.resources.web.client.multidataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.enums.MultidatasetTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectMultidatasetTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetMultidatasetEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedMultidatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.handlers.MultidatasetStructureTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.base.GetLatestResourceVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.base.GetLatestResourceVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.criteria.StatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.DeleteMultidatasetCubeAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.DeleteMultidatasetCubeResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.GetMultidatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.SaveMultidatasetCubeAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.SaveMultidatasetCubeResult;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetCubeLocationAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetCubeLocationResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueriesAction;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueriesResult;
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

public class MultidatasetStructureTabPresenter extends Presenter<MultidatasetStructureTabPresenter.MultidatasetStructureTabView, MultidatasetStructureTabPresenter.MultidatasetStructureTabProxy>
        implements
            MultidatasetStructureTabUiHandlers {

    private DispatchAsync dispatcher;
    private PlaceManager  placeManager;

    public interface MultidatasetStructureTabView extends View, HasUiHandlers<MultidatasetStructureTabUiHandlers> {

        // Related resources
        void setDatasetsForCubes(GetDatasetsResult result);
        void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result);
        void setQueriesForCubes(GetQueriesResult result);
        void setStatisticalOperationsForQuerySelection(GetStatisticalOperationsPaginatedListResult result);

        void setMultidatasetVersion(MultidatasetVersionDto multidatasetVersionDto, MultidatasetCubeDto savedElement);
        void setMultidatasetVersion(MultidatasetVersionDto multidatasetVersionDto);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.multidatasetStructurePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface MultidatasetStructureTabProxy extends Proxy<MultidatasetStructureTabPresenter>, Place {
    }

    @Inject
    public MultidatasetStructureTabPresenter(EventBus eventBus, MultidatasetStructureTabView view, MultidatasetStructureTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
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
        RevealContentEvent.fire(this, MultidatasetPresenter.TYPE_SetContextAreaMultidataset, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectMultidatasetTabEvent.fire(this, MultidatasetTabTypeEnum.STRUCTURE);
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
        String multidatasetVersionUrn = CommonUtils.generateMultidatasetUrn(multidatasetCode);
        retrieveMultidatasetVersion(multidatasetVersionUrn);
    }

    private void retrieveMultidatasetVersion(final String multidatasetVersionUrn) {
        dispatcher.execute(new GetMultidatasetVersionAction(multidatasetVersionUrn), new WaitingAsyncCallbackHandlingError<GetMultidatasetVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedMultidatasetWarningMessageEvent.fire(MultidatasetStructureTabPresenter.this, multidatasetVersionUrn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetMultidatasetVersionResult result) {
                SetMultidatasetEvent.fire(MultidatasetStructureTabPresenter.this, result.getMultidatasetVersionDto());
                getView().setMultidatasetVersion(result.getMultidatasetVersionDto());
            }
        });
    }

    @Override
    public void saveMultidatasetCube(String multidatasetVersionUrn, MultidatasetCubeDto element) {
        dispatcher.execute(new SaveMultidatasetCubeAction(multidatasetVersionUrn, element), new WaitingAsyncCallbackHandlingError<SaveMultidatasetCubeResult>(this) {

            @Override
            public void onWaitSuccess(SaveMultidatasetCubeResult result) {
                getView().setMultidatasetVersion(result.getMultidatasetVersionDto(), result.getSavedElement());
            }
        });
    }

    @Override
    public void deleteCube(String multidatasetVersionUrn, String cubeUrn) {
        dispatcher.execute(new DeleteMultidatasetCubeAction(multidatasetVersionUrn, cubeUrn), new WaitingAsyncCallbackHandlingError<DeleteMultidatasetCubeResult>(this) {

            @Override
            public void onWaitSuccess(DeleteMultidatasetCubeResult result) {
                getView().setMultidatasetVersion(result.getMultidatasetVersionDto());
            }
        });
    }

    @Override
    public void updateCubeLocation(String multidatasetVersionUrn, String elementUrn, Long orderInMultidataset) {
        dispatcher.execute(new UpdateMultidatasetCubeLocationAction(multidatasetVersionUrn, elementUrn, orderInMultidataset),
                new WaitingAsyncCallbackHandlingError<UpdateMultidatasetCubeLocationResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateMultidatasetCubeLocationResult result) {
                        getView().setMultidatasetVersion(result.getMultidatasetVersionDto());
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
    public void retrieveStatisticalOperationsForQuerySelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForQuerySelection(result);
            }
        });
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
                    }
                }
            });
        }
    }

    @Override
    public void goToRelatedDatasetQuery(final String urn) {
        if (!StringUtils.isBlank(urn)) {
            String[] urnSplited = UrnUtils.splitUrnItemScheme(urn);
            boolean hasVersion = urnSplited.length == 3 && !StringUtils.isBlank(urnSplited[2]);

            if (!hasVersion) {
                dispatcher.execute(new GetLatestResourceVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetLatestResourceVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(GetLatestResourceVersionResult result) {
                        navigateToDatasetVersion(result.getResourceVersion());
                    }
                });
            } else {
                dispatcher.execute(new GetDatasetVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetDatasetVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(GetDatasetVersionResult result) {
                        navigateToDatasetVersion(result.getDatasetVersionDto());
                    }
                });
            }
        }
    }

    private void navigateToDatasetVersion(LifeCycleStatisticalResourceDto resourceVersion) {
        String operationUrn = resourceVersion.getStatisticalOperation().getUrn();
        String resourceUrn = resourceVersion.getUrn();
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteDatasetPlaceRequest(operationUrn, resourceUrn));
    }

}
