package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.navigation.shared.ConstantsPlaceRequest;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.base.presenter.StatisticalResourceMetadataBasePresenter;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetInGroupMetadataTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionMainCoveragesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionMainCoveragesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptSchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptSchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetGeographicalGranularitiesListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetGeographicalGranularitiesListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesListResult;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
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

public class DatasetInGroupMetadataTabPresenter
        extends
            StatisticalResourceMetadataBasePresenter<DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabView, DatasetInGroupMetadataTabPresenter.DatasetInGroupMetadataTabProxy>
        implements
            DatasetInGroupMetadataTabUiHandlers {

    public interface DatasetInGroupMetadataTabView extends StatisticalResourceMetadataBasePresenter.StatisticalResourceMetadataBaseView, HasUiHandlers<DatasetInGroupMetadataTabUiHandlers> {

        void setDataset(DatasetVersionDto datasetDto);

        void initDatasetForUpdateInGroup(DatasetVersionDto datasetDto);

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
    @NameToken(NameTokens.datasetInGroupMetadataPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetInGroupMetadataTabProxy extends Proxy<DatasetInGroupMetadataTabPresenter>, Place {
    }

    @Inject
    public DatasetInGroupMetadataTabPresenter(EventBus eventBus, DatasetInGroupMetadataTabView view, DatasetInGroupMetadataTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, dispatcher, placeManager);
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbMetadata();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DatasetsGroupPresenter.TYPE_SetContextAreaDataset, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        String origin = PlaceRequestUtils.getOriginDatasetDetailParamFromUrl(placeManager);
        if (!StringUtils.isBlank(operationCode) && origin != null && ConstantsPlaceRequest.updateDatasetsInGroup.equals(origin)) {
            loadInitialDataForUpdateInGroup();
        } else {
            StatisticalResourcesWeb.showErrorPage();
        }
    }

    private void loadInitialDataForUpdateInGroup() {
        List<String> datasetsIdentifiers = PlaceRequestUtils.getDatasetsInGroupParamFromUrl(placeManager);
        if (!datasetsIdentifiers.isEmpty()) {
            retrieveDataset(datasetsIdentifiers.get(0));
        }
    }

    public void retrieveDataset(String datasetIdentifier) {
        final String urn = CommonUtils.generateDatasetUrn(datasetIdentifier);
        dispatcher.execute(new GetDatasetVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetDatasetVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetInGroupMetadataTabPresenter.this, urn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasetVersionResult result) {
                getView().initDatasetForUpdateInGroup(result.getDatasetVersionDto());

            }
        });
    }

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
}