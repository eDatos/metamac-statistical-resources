package org.siemac.metamac.statistical.resources.web.client.gin;

import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesPlaceManager;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWebConstants;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWebMessages;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetAttributesTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetCategorisationsTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetConstraintsTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetDatasourcesTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupCategorisationsTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetInGroupMetadataTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetListPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetMetadataTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.presenter.DatasetsGroupPresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetAttributesTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetCategorisationsTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetConstraintsTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetDatasourcesTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetInGroupCategorisationsTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetInGroupMetadataTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetMetadataTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetViewImpl;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.DatasetsGroupViewImpl;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetListPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetMetadataTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetStructureTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.MultidatasetListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.MultidatasetMetadataTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.MultidatasetStructureTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.multidataset.view.MultidatasetViewImpl;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationListPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationResourcesPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.view.OperationListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.operation.view.OperationResourcesViewImpl;
import org.siemac.metamac.statistical.resources.web.client.operation.view.OperationViewImpl;
import org.siemac.metamac.statistical.resources.web.client.presenter.ErrorPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.presenter.UnauthorizedPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationListPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationMetadataTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationStructureTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.view.PublicationListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.publication.view.PublicationMetadataTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.publication.view.PublicationStructureTabViewImpl;
import org.siemac.metamac.statistical.resources.web.client.publication.view.PublicationViewImpl;
import org.siemac.metamac.statistical.resources.web.client.query.presenter.QueryListPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.presenter.QueryPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.view.QueryListViewImpl;
import org.siemac.metamac.statistical.resources.web.client.query.view.QueryViewImpl;
import org.siemac.metamac.statistical.resources.web.client.view.ErrorPageViewImpl;
import org.siemac.metamac.statistical.resources.web.client.view.MainPageViewImpl;
import org.siemac.metamac.statistical.resources.web.client.view.UnauthorizedPageViewImpl;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

public class ClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        // Default implementation of standard resources
        // |_ bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        // |_ bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
        // |_ bind(RootPresenter.class).asEagerSingleton();
        // |_ bind(PlaceManager.class).to(MyPlaceManager.class).in(Singleton.class);
        // |_ bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
        install(new DefaultModule(StatisticalResourcesPlaceManager.class));

        // Constants
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.operationsListPage);

        // Gate keeper
        bind(LoggedInGatekeeper.class).in(Singleton.class);

        // Presenters
        bindPresenter(MainPagePresenter.class, MainPagePresenter.MainPageView.class, MainPageViewImpl.class, MainPagePresenter.MainPageProxy.class);

        bindPresenter(OperationPresenter.class, OperationPresenter.OperationView.class, OperationViewImpl.class, OperationPresenter.OperationProxy.class);
        bindPresenter(OperationListPresenter.class, OperationListPresenter.OperationListView.class, OperationListViewImpl.class, OperationListPresenter.OperationListProxy.class);
        bindPresenter(OperationResourcesPresenter.class, OperationResourcesPresenter.OperationResourcesView.class, OperationResourcesViewImpl.class,
                OperationResourcesPresenter.OperationResourcesProxy.class);

        bindPresenter(DatasetListPresenter.class, DatasetListPresenter.DatasetListView.class, DatasetListViewImpl.class, DatasetListPresenter.DatasetListProxy.class);
        bindPresenter(DatasetPresenter.class, DatasetPresenter.DatasetView.class, DatasetViewImpl.class, DatasetPresenter.DatasetProxy.class);
        bindPresenter(DatasetMetadataTabPresenter.class, DatasetMetadataTabPresenter.DatasetMetadataTabView.class, DatasetMetadataTabViewImpl.class,
                DatasetMetadataTabPresenter.DatasetMetadataTabProxy.class);
        bindPresenter(DatasetConstraintsTabPresenter.class, DatasetConstraintsTabPresenter.DatasetConstraintsTabView.class, DatasetConstraintsTabViewImpl.class,
                DatasetConstraintsTabPresenter.DatasetConstraintsTabProxy.class);
        bindPresenter(DatasetDatasourcesTabPresenter.class, DatasetDatasourcesTabPresenter.DatasetDatasourcesTabView.class, DatasetDatasourcesTabViewImpl.class,
                DatasetDatasourcesTabPresenter.DatasetDatasourcesTabProxy.class);
        bindPresenter(DatasetAttributesTabPresenter.class, DatasetAttributesTabPresenter.DatasetAttributesTabView.class, DatasetAttributesTabViewImpl.class,
                DatasetAttributesTabPresenter.DatasetAttributesTabProxy.class);
        bindPresenter(DatasetCategorisationsTabPresenter.class, DatasetCategorisationsTabPresenter.DatasetCategorisationsTabView.class, DatasetCategorisationsTabViewImpl.class,
                DatasetCategorisationsTabPresenter.DatasetCategorisationsTabProxy.class);

        bindPresenter(PublicationListPresenter.class, PublicationListPresenter.PublicationListView.class, PublicationListViewImpl.class, PublicationListPresenter.PublicationListProxy.class);
        bindPresenter(PublicationPresenter.class, PublicationPresenter.PublicationView.class, PublicationViewImpl.class, PublicationPresenter.PublicationProxy.class);
        bindPresenter(PublicationMetadataTabPresenter.class, PublicationMetadataTabPresenter.PublicationMetadataTabView.class, PublicationMetadataTabViewImpl.class,
                PublicationMetadataTabPresenter.PublicationMetadataTabProxy.class);
        bindPresenter(PublicationStructureTabPresenter.class, PublicationStructureTabPresenter.PublicationStructureTabView.class, PublicationStructureTabViewImpl.class,
                PublicationStructureTabPresenter.PublicationStructureTabProxy.class);

        bindPresenter(MultidatasetListPresenter.class, MultidatasetListPresenter.MultidatasetListView.class, MultidatasetListViewImpl.class, MultidatasetListPresenter.MultidatasetListProxy.class);
        bindPresenter(MultidatasetPresenter.class, MultidatasetPresenter.MultidatasetView.class, MultidatasetViewImpl.class, MultidatasetPresenter.MultidatasetProxy.class);
        bindPresenter(MultidatasetMetadataTabPresenter.class, MultidatasetMetadataTabPresenter.MultidatasetMetadataTabView.class, MultidatasetMetadataTabViewImpl.class,
                MultidatasetMetadataTabPresenter.MultidatasetMetadataTabProxy.class);
        bindPresenter(MultidatasetStructureTabPresenter.class, MultidatasetStructureTabPresenter.MultidatasetStructureTabView.class, MultidatasetStructureTabViewImpl.class,
                MultidatasetStructureTabPresenter.MultidatasetStructureTabProxy.class);

        bindPresenter(QueryListPresenter.class, QueryListPresenter.QueryListView.class, QueryListViewImpl.class, QueryListPresenter.QueryListProxy.class);
        bindPresenter(QueryPresenter.class, QueryPresenter.QueryView.class, QueryViewImpl.class, QueryPresenter.QueryProxy.class);

        // Update datasets in a group
        bindPresenter(DatasetsGroupPresenter.class, DatasetsGroupPresenter.DatasetsGroupView.class, DatasetsGroupViewImpl.class, DatasetsGroupPresenter.DatasetsGroupProxy.class);
        bindPresenter(DatasetInGroupMetadataTabPresenter.class, DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabView.class, DatasetInGroupMetadataTabViewImpl.class,
                DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabProxy.class);
        bindPresenter(DatasetInGroupCategorisationsTabPresenter.class, DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabView.class,
                DatasetInGroupCategorisationsTabViewImpl.class, DatasetInGroupCategorisationsTabPresenter.DatasetInGroupCategorisationsTabProxy.class);

        // Error pages
        bindPresenter(ErrorPagePresenter.class, ErrorPagePresenter.ErrorPageView.class, ErrorPageViewImpl.class, ErrorPagePresenter.ErrorPageProxy.class);
        bindPresenter(UnauthorizedPagePresenter.class, UnauthorizedPagePresenter.UnauthorizedPageView.class, UnauthorizedPageViewImpl.class, UnauthorizedPagePresenter.UnauthorizedPageProxy.class);

        // Interfaces
        bind(StatisticalResourcesWebConstants.class).in(Singleton.class);
        bind(StatisticalResourcesWebMessages.class).in(Singleton.class);
    }

}
