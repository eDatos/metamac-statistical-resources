package org.siemac.metamac.statistical.resources.web.client.operation.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.enums.StatisticalResourcesToolStripLayoutEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectMenuLayoutEvent;
import org.siemac.metamac.statistical.resources.web.client.operation.view.handlers.OperationListUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

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

public class OperationListPresenter extends Presenter<OperationListPresenter.OperationListView, OperationListPresenter.OperationListProxy> implements OperationListUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.operationsListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface OperationListProxy extends Proxy<OperationListPresenter>, Place {
    }

    public interface OperationListView extends View, HasUiHandlers<OperationListUiHandlers> {

        void setOperationPaginatedList(GetStatisticalOperationsPaginatedListResult datasetsPaginatedList);
    }

    @Inject
    public OperationListPresenter(EventBus eventBus, OperationListView operationListView, OperationListProxy operationListProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, operationListView, operationListProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return getConstants().breadcrumbOperations();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(this, getConstants().statisticalResourcesDashboard());
        SelectMenuLayoutEvent.fire(this, StatisticalResourcesToolStripLayoutEnum.STATISTIC_DESKTOP);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        retrieveOperations(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, null);

    }

    @Override
    public void retrieveOperations(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setOperationPaginatedList(result);
            }
        });
    }

    @Override
    public void goToOperation(String urn) {
        if (!StringUtils.isBlank(urn)) {
            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeOperationPlaceRequest(urn));
        }
    }

}
