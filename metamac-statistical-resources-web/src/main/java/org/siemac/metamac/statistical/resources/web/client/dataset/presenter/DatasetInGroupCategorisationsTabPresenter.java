package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.navigation.shared.ConstantsPlaceRequest;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetInGroupCategorisationsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectDatasetInGroupTabEvent;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCategoriesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCategoriesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCategorySchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCategorySchemesPaginatedListResult;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

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

public class DatasetInGroupCategorisationsTabPresenter
        extends
            Presenter<DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabView, DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabProxy>
        implements
            DatasetInGroupCategorisationsTabUiHandlers {

    private DispatchAsync dispatcher;
    private PlaceManager  placeManager;

    public interface DatasetInGroupCategorisationsTabView extends View, HasUiHandlers<DatasetInGroupCategorisationsTabUiHandlers> {

        void setCategoriesForCategorisations(List<ExternalItemDto> categories, Integer firstResultOut, Integer totalResults);

        void setCategorySchemesForCategorisations(List<ExternalItemDto> categorySchemes, Integer firstResultOut, Integer totalResults);

        List<CategorisationDto> getCategorisations();

    }

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetInGroupCategorisationsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetInGroupCategorisationsTabProxy extends Proxy<DatasetInGroupCategorisationsTabPresenter>, Place {
    }

    @Inject
    public DatasetInGroupCategorisationsTabPresenter(EventBus eventBus, DatasetInGroupCategorisationsTabView view, DatasetInGroupCategorisationsTabProxy proxy, DispatchAsync dispatcher,
            PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbDatasetSubjects();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DatasetsGroupPresenter.TYPE_SetContextAreaDataset, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectDatasetInGroupTabEvent.fire(this, DatasetTabTypeEnum.CATEGORISATIONS);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String origin = PlaceRequestUtils.getOriginDatasetDetailParamFromUrl(placeManager);
        if (origin != null && !ConstantsPlaceRequest.updateDatasetsInGroup.equals(origin)) {
            StatisticalResourcesWeb.showErrorPage();
        }

    }

    @Override
    public void retrieveCategoriesForCategorisations(int firstResult, int maxResults, SrmItemRestCriteria criteria) {
        dispatcher.execute(new GetCategoriesPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCategoriesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetCategoriesPaginatedListResult result) {
                getView().setCategoriesForCategorisations(result.getCategories(), result.getFirstResultOut(), result.getTotalResults());

            }
        });
    }

    @Override
    public void retrieveCategorySchemesForCategorisations(int firstResult, int maxResults, MetamacWebCriteria criteria) {
        dispatcher.execute(new GetCategorySchemesPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCategorySchemesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetCategorySchemesPaginatedListResult result) {
                getView().setCategorySchemesForCategorisations(result.getCategorySchemes(), result.getFirstResultOut(), result.getTotalResults());

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
}
