package org.siemac.metamac.statistical.resources.web.client.query.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationPresenter;
import org.siemac.metamac.statistical.resources.web.client.query.view.handlers.QueryUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendStreamMessageAction;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendStreamMessageResult;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendXMessageAction;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendXMessageResult;
import org.siemac.metamac.statistical.resources.web.shared.base.UpdateGeoCacheRelatedResourceAction;
import org.siemac.metamac.statistical.resources.web.shared.base.UpdateGeoCacheRelatedResourceResult;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionCoverageAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionCoverageResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsIdsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsIdsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetAgenciesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetAgenciesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetAgencySchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetAgencySchemesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetResult;
import org.siemac.metamac.statistical.resources.web.shared.query.DeleteQueryVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.query.DeleteQueryVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueryVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.query.GetQueryVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.query.GetVersionsOfQueryAction;
import org.siemac.metamac.statistical.resources.web.shared.query.GetVersionsOfQueryResult;
import org.siemac.metamac.statistical.resources.web.shared.query.SaveQueryVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.query.SaveQueryVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionProcStatusAction.Builder;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionProcStatusResult;
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

public class QueryPresenter extends Presenter<QueryPresenter.QueryView, QueryPresenter.QueryProxy> implements QueryUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.queryPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface QueryProxy extends Proxy<QueryPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getQueryBreadCrumbTitle(placeRequest);
    }

    public interface QueryView extends View, HasUiHandlers<QueryUiHandlers> {

        void setQueryDto(QueryVersionDto queryDto);

        void setQueryVersionsAndSelectCurrent(String currentQueryUrn, List<QueryVersionBaseDto> queryVersionBaseDtos);

        void newQueryDto();

        void setDatasetsForQuery(GetDatasetVersionsResult result);

        void setStatisticalOperationsForDatasetSelection(GetStatisticalOperationsPaginatedListResult result);

        void setDatasetDimensionsIds(List<String> datasetDimensionsIds);

        void setDatasetDimensionCodes(String dimensionId, List<CodeItemDto> codesDimension);

        void setAgencySchemesForMaintainer(GetAgencySchemesPaginatedListResult result);

        void setAgenciesForMaintainer(GetAgenciesPaginatedListResult result);

        void showUnauthorizedResourceWarningMessage();

        void setTemporalCodesForField(GetTemporalGranularitiesByDatasetResult result);

        void setInformationLabelContents(QueryVersionDto queryVersionDto);
    }

    @Inject
    public QueryPresenter(EventBus eventBus, QueryView queryView, QueryProxy queryProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, queryView, queryProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, OperationPresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);

        if (!StringUtils.isBlank(operationCode)) {
            String operationUrn = CommonUtils.generateStatisticalOperationUrn(operationCode);
            retrieveOperation(operationUrn);
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
        String queryCode = PlaceRequestUtils.getQueryParamFromUrl(placeManager);
        if (StringUtils.isBlank(queryCode)) {
            getView().newQueryDto();
        } else {
            String urn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_QUERY_PREFIX, queryCode);
            retrieveQuery(urn);
            retrieveQueryVersions(urn);
        }
    }

    @Override
    public void retrieveQuery(String urn) {
        dispatcher.execute(new GetQueryVersionAction(urn), new WaitingAsyncCallbackHandlingError<GetQueryVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    getView().showUnauthorizedResourceWarningMessage();
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetQueryVersionResult result) {
                getView().setQueryDto(result.getQueryVersionDto());
            }
        });
    }

    @Override
    public void retrieveAgencySchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
        dispatcher.execute(new GetAgencySchemesPaginatedListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetAgencySchemesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetAgencySchemesPaginatedListResult result) {
                getView().setAgencySchemesForMaintainer(result);
            }
        });
    }

    @Override
    public void retrieveAgencies(int firstResult, int maxResults, SrmItemRestCriteria webCriteria) {
        dispatcher.execute(new GetAgenciesPaginatedListAction(firstResult, maxResults, webCriteria), new WaitingAsyncCallbackHandlingError<GetAgenciesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetAgenciesPaginatedListResult result) {
                getView().setAgenciesForMaintainer(result);
            }
        });
    }

    @Override
    public void saveQuery(final QueryVersionDto queryDto) {
        dispatcher.execute(new SaveQueryVersionAction(queryDto, StatisticalResourcesDefaults.getSelectedStatisticalOperation().getCode()),
                new WaitingAsyncCallbackHandlingError<SaveQueryVersionResult>(this) {

                    @Override
                    public void onWaitSuccess(SaveQueryVersionResult result) {
                        fireSuccessMessage(getMessages().querySaved());
                        getView().setQueryDto(result.getSavedQueryVersionDto());
                        updateUrlIfNeeded(result.getSavedQueryVersionDto());
                    }

                    private void updateUrlIfNeeded(QueryVersionDto query) {
                        if (queryDto.getId() == null) {
                            placeManager.revealRelativePlace(PlaceRequestUtils.buildRelativeQueryPlaceRequest(query.getUrn()), -1);
                        }
                    }
                });
    }

    @Override
    public void deleteQuery(String urn) {
        List<String> urns = new ArrayList<String>();
        urns.add(urn);
        dispatcher.execute(new DeleteQueryVersionsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteQueryVersionsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteQueryVersionsResult result) {
                fireSuccessMessage(getMessages().queryDeleted());
                goToQueries();
            }
        });
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveDatasetsForQuery(int firstResult, int maxResults, DatasetVersionWebCriteria criteria) {
        criteria.setHasData(true);
        dispatcher.execute(new GetDatasetVersionsAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetDatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetVersionsResult result) {
                getView().setDatasetsForQuery(result);
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
    public void retrieveDimensionsForDataset(String urn) {
        dispatcher.execute(new GetDatasetDimensionsIdsAction(urn), new WaitingAsyncCallbackHandlingError<GetDatasetDimensionsIdsResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetDimensionsIdsResult result) {
                getView().setDatasetDimensionsIds(result.getDatasetVersionDimensionsIds());
            }
        });
    }

    @Override
    public void retrieveDimensionCodesForDataset(String urn, final String dimensionId, MetamacWebCriteria webCriteria, List<String> temporalGranularities) {
        dispatcher.execute(new GetDatasetDimensionCoverageAction(urn, dimensionId, webCriteria, temporalGranularities), new WaitingAsyncCallbackHandlingError<GetDatasetDimensionCoverageResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetDimensionCoverageResult result) {
                getView().setDatasetDimensionCodes(dimensionId, result.getCodesDimension());
            }

        });
    }

    //
    // LIFE CYCLE
    //

    @Override
    public void sendToProductionValidation(QueryVersionDto query) {
        dispatcher.execute(new UpdateQueryVersionProcStatusAction(query, LifeCycleActionEnum.SEND_TO_PRODUCTION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdateQueryVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateQueryVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToProductionValidation());
                        retrieveQueryVersions(result.getQueryVersionDto().getUrn());
                        getView().setQueryDto(result.getQueryVersionDto());
                    }
                });
    }

    @Override
    public void sendToDiffusionValidation(QueryVersionDto query) {
        dispatcher.execute(new UpdateQueryVersionProcStatusAction(query, LifeCycleActionEnum.SEND_TO_DIFFUSION_VALIDATION),
                new WaitingAsyncCallbackHandlingError<UpdateQueryVersionProcStatusResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateQueryVersionProcStatusResult result) {
                        showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceSentToDiffusionValidation());
                        retrieveQueryVersions(result.getQueryVersionDto().getUrn());
                        getView().setQueryDto(result.getQueryVersionDto());
                    }
                });
    }

    @Override
    public void rejectValidation(QueryVersionDto query, String reasonOfRejection) {
        UpdateQueryVersionProcStatusAction.Builder builder = new Builder(query, LifeCycleActionEnum.REJECT_VALIDATION).reasonOfRejection(reasonOfRejection);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdateQueryVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateQueryVersionProcStatusResult result) {
                showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourceRejectValidation());
                retrieveQueryVersions(result.getQueryVersionDto().getUrn());
                getView().setQueryDto(result.getQueryVersionDto());
            }
        });
    }

    @Override
    public void publish(QueryVersionDto query) {

        dispatcher.execute(new UpdateQueryVersionProcStatusAction(query, LifeCycleActionEnum.PUBLISH), new WaitingAsyncCallbackHandlingError<UpdateQueryVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateQueryVersionProcStatusResult result) {
                showMessageAfterResourceLifeCycleUpdate(result, getMessages().lifeCycleResourcePublish());
                retrieveQueryVersions(result.getQueryVersionDto().getUrn());
                getView().setQueryDto(result.getQueryVersionDto());
            }
        });
    }

    @Override
    public void resendStreamMessage(QueryVersionDto query) {
        dispatcher.execute(new ResendStreamMessageAction(query), new WaitingAsyncCallbackHandlingError<ResendStreamMessageResult>(this) {

            @Override
            public void onWaitSuccess(ResendStreamMessageResult result) {
                retrieveQueryVersions(result.getLifeCycleStatisticalResourceResultDto().getUrn());
                getView().setQueryDto((QueryVersionDto) result.getLifeCycleStatisticalResourceResultDto());
            }
        });
    }

    @Override
    public void resendXMessage(QueryVersionDto query) {
        dispatcher.execute(new ResendXMessageAction(query), new WaitingAsyncCallbackHandlingError<ResendXMessageResult>(this) {

            @Override
            public void onWaitSuccess(ResendXMessageResult result) {
                retrieveQueryVersions(result.getLifeCycleStatisticalResourceResultDto().getUrn());
                getView().setQueryDto((QueryVersionDto) result.getLifeCycleStatisticalResourceResultDto());
            }
        });
    }

    @Override
    public void version(QueryVersionDto query, VersionTypeEnum versionType) {
        Builder builder = new Builder(query, LifeCycleActionEnum.VERSION);
        builder.versionType(versionType);
        dispatcher.execute(builder.build(), new WaitingAsyncCallbackHandlingError<UpdateQueryVersionProcStatusResult>(this) {

            @Override
            public void onWaitSuccess(UpdateQueryVersionProcStatusResult result) {
                fireSuccessMessage(getMessages().lifeCycleResourceVersion());
                retrieveQueryVersions(result.getQueryVersionDto().getUrn());
                getView().setQueryDto(result.getQueryVersionDto());
            }
        });
    }

    @Override
    public void previewData(QueryVersionDto queryVersionDto) {
        try {
            String url = MetamacPortalWebUtils.buildQueryVersionUrl(queryVersionDto, CommonUtils.getMetamacPortalBaseUrl());
            Window.open(url, "_blank", "");
        } catch (Exception e) {
            ShowMessageEvent.fireErrorMessage(this, e);
        }
    }

    @Override
    public void updateGeoCacheRelatedResources(final QueryVersionDto queryVersionDto) {
        dispatcher.execute(new UpdateGeoCacheRelatedResourceAction(queryVersionDto.getUrn(), StatisticalResourceTypeEnum.QUERY),
                new WaitingAsyncCallbackHandlingError<UpdateGeoCacheRelatedResourceResult>(this) {

                    @Override
                    public void onWaitSuccess(UpdateGeoCacheRelatedResourceResult result) {
                        fireSuccessMessage(getMessages().queryUpdateGeoCacheBackgroundInProgress());
                        retrieveQuery(queryVersionDto.getUrn());
                    }
                });
    }

    private void showMessageAfterResourceLifeCycleUpdate(UpdateQueryVersionProcStatusResult result, String message) {
        CommonUtils.showMessageAfterResourceLifeCycleUpdate(QueryPresenter.this, result.getNotificationException(), message);
    }

    private void retrieveQueryVersions(final String urn) {
        dispatcher.execute(new GetVersionsOfQueryAction(urn), new WaitingAsyncCallbackHandlingError<GetVersionsOfQueryResult>(this) {

            @Override
            public void onWaitSuccess(GetVersionsOfQueryResult result) {
                getView().setQueryVersionsAndSelectCurrent(urn, result.getQueryVersionBaseDtos());
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
    public void goToQueries() {
        placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteQueriesPlaceRequest(StatisticalResourcesDefaults.getSelectedStatisticalOperation().getUrn()));
    }

    @Override
    public void retrieveTemporalCodesForField(int firstResult, int maxResults, String datasetUrn, MetamacWebCriteria webCriteria) {
        dispatcher.execute(new GetTemporalGranularitiesByDatasetAction(firstResult, maxResults, datasetUrn, webCriteria),
                new WaitingAsyncCallbackHandlingError<GetTemporalGranularitiesByDatasetResult>(this) {

                    @Override
                    public void onWaitSuccess(GetTemporalGranularitiesByDatasetResult result) {
                        getView().setTemporalCodesForField(result);
                    }
                });
    }

}
