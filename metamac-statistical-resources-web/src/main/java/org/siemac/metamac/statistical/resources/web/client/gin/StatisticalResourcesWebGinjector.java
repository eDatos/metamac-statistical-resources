package org.siemac.metamac.statistical.resources.web.client.gin;

import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
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
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetListPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetMetadataTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetPresenter;
import org.siemac.metamac.statistical.resources.web.client.multidataset.presenter.MultidatasetStructureTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationListPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationPresenter;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationResourcesPresenter;
import org.siemac.metamac.statistical.resources.web.client.presenter.ErrorPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.presenter.UnauthorizedPagePresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationListPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationMetadataTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.presenter.PublicationStructureTabPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.presenter.QueryListPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.presenter.QueryPresenter;
import org.siemac.metamac.web.common.client.gin.MetamacWebGinjector;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;

@GinModules({DispatchAsyncModule.class, ClientModule.class})
public interface StatisticalResourcesWebGinjector extends MetamacWebGinjector {

    LoggedInGatekeeper getLoggedInGatekeeper();

    Provider<MainPagePresenter> getMainPagePresenter();

    AsyncProvider<OperationListPresenter> getOperationListPresenter();
    AsyncProvider<OperationResourcesPresenter> getOperationResourcesPresenter();
    AsyncProvider<OperationPresenter> getOperationPresenter();
    AsyncProvider<DatasetListPresenter> getDatasetListPresenter();
    AsyncProvider<DatasetPresenter> getDatasetPresenter();
    AsyncProvider<DatasetMetadataTabPresenter> getDatasetMetadataTabPresenter();
    AsyncProvider<DatasetInGroupMetadataTabPresenter> getDatasetInGroupMetadataTabPresenter();
    AsyncProvider<DatasetConstraintsTabPresenter> getDatasetConstraintsTabPresenter();
    AsyncProvider<DatasetDatasourcesTabPresenter> getDatasetDatasourcesTabPresenter();
    AsyncProvider<DatasetAttributesTabPresenter> getDatasetAttributesTabPresenter();
    AsyncProvider<DatasetCategorisationsTabPresenter> getDatasetCategorisationsTabPresenter();
    AsyncProvider<PublicationListPresenter> getPublicationListPresenter();
    AsyncProvider<PublicationPresenter> getPublicationPresenter();
    AsyncProvider<PublicationMetadataTabPresenter> getPublicationMetadataTabPresenter();
    AsyncProvider<PublicationStructureTabPresenter> getPublicationStructureTabPresenter();
    AsyncProvider<QueryListPresenter> getQueryListPresenter();
    AsyncProvider<QueryPresenter> getQueryPresenter();
    AsyncProvider<MultidatasetListPresenter> getMultidatasetListPresenter();
    AsyncProvider<MultidatasetPresenter> getMultidatasetPresenter();
    AsyncProvider<MultidatasetMetadataTabPresenter> getMultidatasetMetadataTabPresenter();
    AsyncProvider<MultidatasetStructureTabPresenter> getMultidatasetStructureTabPresenter();
    AsyncProvider<DatasetsGroupPresenter> getDatasetsInGroupPresenter();
    AsyncProvider<DatasetInGroupCategorisationsTabPresenter> getDatasetsInGroupCategorisationsTabPresenter();

    AsyncProvider<ErrorPagePresenter> getErrorPagePresenter();
    AsyncProvider<UnauthorizedPagePresenter> getUnauthorizedPagePresenter();
}
