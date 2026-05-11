package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.RepresentationDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetAttributesTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectDatasetTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetDatasetEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetAttributeInstancesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetAttributeInstancesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteGranularityAttributeInstancesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteGranularityAttributeInstancesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportAttributesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportAttributesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetAttributeInstancesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetAttributeInstancesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetAttributesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetAttributesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsCoverageAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsCoverageResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetGranularityAttributeInstancesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetGranularityAttributeInstancesResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetAttributeInstanceAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveDatasetAttributeInstanceResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveGranularityAttributeInstanceAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveGranularityAttributeInstanceResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCodesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetCodesPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetConceptsPaginatedListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
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

public class DatasetAttributesTabPresenter extends Presenter<DatasetAttributesTabPresenter.DatasetAttributesTabView, DatasetAttributesTabPresenter.DatasetAttributesTabProxy>
        implements
            DatasetAttributesTabUiHandlers {

    private final DispatchAsync dispatcher;
    private final PlaceManager  placeManager;

    private String              datasetVersionUrn;

    public interface DatasetAttributesTabView extends View, HasUiHandlers<DatasetAttributesTabUiHandlers> {

        void setAttributes(DatasetVersionDto datasetVersionDto, List<DsdAttributeDto> attributes);
        void setAttributeInstances(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos);
        void setAttributeInstancesForRefresh(DsdAttributeDto dsdAttributeDto, List<DsdAttributeInstanceDto> dsdAttributeInstanceDtos);
        void setGranularityAttributeInstances(DsdAttributeDto dsdAttributeDto, List<DsdGranularityAttributeInstanceDto> instances);
        void setTemporalGranularities(List<ExternalItemDto> granularities);
        void setDimensionsCoverageValues(Map<String, List<CodeItemDto>> dimensionsCoverages);
        void setItemsForDatasetLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults);
        void setItemsForDimensionOrGroupLevelAttributeValueSelection(List<ExternalItemDto> externalItemDtos, int firstResult, int totalResults);
        void createImportAttributesWithMappingWindow(String datasetVersionUrn);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetAttributesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetAttributesTabProxy extends Proxy<DatasetAttributesTabPresenter>, Place {
    }

    @Inject
    public DatasetAttributesTabPresenter(EventBus eventBus, DatasetAttributesTabView view, DatasetAttributesTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbDatasetAttributes();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DatasetPresenter.TYPE_SetContextAreaDataset, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectDatasetTabEvent.fire(this, DatasetTabTypeEnum.ATTRIBUTES);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        String datasetCode = PlaceRequestUtils.getDatasetParamFromUrl(placeManager);
        if (!StringUtils.isBlank(operationCode) && !StringUtils.isBlank(datasetCode)) {
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
        String datasetCode = PlaceRequestUtils.getDatasetParamFromUrl(placeManager);
        datasetVersionUrn = CommonUtils.generateDatasetUrn(datasetCode);
        retrieveDatasetAndAttributes(datasetVersionUrn);
    }

    private void retrieveDatasetAndAttributes(final String datasetUrn) {
        dispatcher.execute(new GetDatasetVersionAction(datasetUrn), new WaitingAsyncCallbackHandlingError<GetDatasetVersionResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetAttributesTabPresenter.this, datasetUrn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasetVersionResult result) {
                SetDatasetEvent.fire(DatasetAttributesTabPresenter.this, result.getDatasetVersionDto());
                retrieveAttributes(result.getDatasetVersionDto());
            }
        });
    }

    private void retrieveAttributes(final DatasetVersionDto datasetVersionDto) {
        dispatcher.execute(new GetDatasetAttributesAction(datasetVersionDto.getUrn()), new WaitingAsyncCallbackHandlingError<GetDatasetAttributesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetAttributesTabPresenter.this, datasetVersionDto.getUrn());
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasetAttributesResult result) {
                getView().setAttributes(datasetVersionDto, result.getDatasetVersionAttributes());
            }
        });
    }

    @Override
    public void retrieveAttributeInstances(final DsdAttributeDto dsdAttributeDto) {
        dispatcher.execute(new GetDatasetAttributeInstancesAction(datasetVersionUrn, dsdAttributeDto.getIdentifier()), new WaitingAsyncCallbackHandlingError<GetDatasetAttributeInstancesResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetAttributeInstancesResult result) {
                getView().setAttributeInstances(dsdAttributeDto, result.getDsdAttributeInstanceDtos());
                if (CommonUtils.supportsGranularityAttributeInstances(dsdAttributeDto)) {
                    retrieveGranularityAttributeInstancesInternal(dsdAttributeDto);
                } else {
                    getView().setGranularityAttributeInstances(dsdAttributeDto, new ArrayList<DsdGranularityAttributeInstanceDto>());
                }
            }
        });
    }

    @Override
    public void retrieveAttributeInstancesForRefresh(final DsdAttributeDto dsdAttributeDto) {
        dispatcher.execute(new GetDatasetAttributeInstancesAction(datasetVersionUrn, dsdAttributeDto.getIdentifier()), new WaitingAsyncCallbackHandlingError<GetDatasetAttributeInstancesResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetAttributeInstancesResult result) {
                getView().setAttributeInstancesForRefresh(dsdAttributeDto, result.getDsdAttributeInstanceDtos());
                if (CommonUtils.supportsGranularityAttributeInstances(dsdAttributeDto)) {
                    retrieveGranularityAttributeInstancesInternal(dsdAttributeDto);
                } else {
                    getView().setGranularityAttributeInstances(dsdAttributeDto, new ArrayList<DsdGranularityAttributeInstanceDto>());
                }
            }
        });
    }

    private void retrieveGranularityAttributeInstancesInternal(final DsdAttributeDto dsdAttributeDto) {
        dispatcher.execute(new GetGranularityAttributeInstancesAction(datasetVersionUrn, dsdAttributeDto.getIdentifier()),
                new WaitingAsyncCallbackHandlingError<GetGranularityAttributeInstancesResult>(this) {

                    @Override
                    public void onWaitSuccess(GetGranularityAttributeInstancesResult result) {
                        getView().setGranularityAttributeInstances(dsdAttributeDto, result.getInstances());
                    }
                });
    }

    @Override
    public void saveAttributeInstance(final DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        dispatcher.execute(new SaveDatasetAttributeInstanceAction(datasetVersionUrn, dsdAttributeInstanceDto), new WaitingAsyncCallbackHandlingError<SaveDatasetAttributeInstanceResult>(this) {

            @Override
            public void onWaitSuccess(SaveDatasetAttributeInstanceResult result) {
                retrieveAttributeInstances(dsdAttributeDto);
            }
        });
    }

    @Override
    public void deleteAttributeInstance(final DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto) {
        List<String> uuids = Arrays.asList(dsdAttributeInstanceDto.getUuid());
        dispatcher.execute(new DeleteDatasetAttributeInstancesAction(dsdAttributeDto, datasetVersionUrn, uuids), new WaitingAsyncCallbackHandlingError<DeleteDatasetAttributeInstancesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDatasetAttributeInstancesResult result) {
                retrieveAttributeInstances(dsdAttributeDto);
            }
        });
    }

    @Override
    public void deleteAttributeInstances(final DsdAttributeDto dsdAttributeDto, List<String> uuids) {
        dispatcher.execute(new DeleteDatasetAttributeInstancesAction(dsdAttributeDto, datasetVersionUrn, uuids), new WaitingAsyncCallbackHandlingError<DeleteDatasetAttributeInstancesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDatasetAttributeInstancesResult result) {
                retrieveAttributeInstances(dsdAttributeDto);
            }
        });
    }

    @Override
    public void saveGranularityAttributeInstance(final DsdAttributeDto dsdAttributeDto, DsdGranularityAttributeInstanceDto dto) {
        dispatcher.execute(new SaveGranularityAttributeInstanceAction(datasetVersionUrn, dto), new WaitingAsyncCallbackHandlingError<SaveGranularityAttributeInstanceResult>(this) {

            @Override
            public void onWaitSuccess(SaveGranularityAttributeInstanceResult result) {
                retrieveAttributeInstances(dsdAttributeDto);
            }
        });
    }

    @Override
    public void deleteGranularityAttributeInstances(final DsdAttributeDto dsdAttributeDto, List<String> uuids) {
        dispatcher.execute(new DeleteGranularityAttributeInstancesAction(datasetVersionUrn, uuids), new WaitingAsyncCallbackHandlingError<DeleteGranularityAttributeInstancesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteGranularityAttributeInstancesResult result) {
                retrieveAttributeInstances(dsdAttributeDto);
            }
        });
    }

    @Override
    public void retrieveTemporalGranularitiesForAttribute(String urn) {
        dispatcher.execute(new GetTemporalGranularitiesByDatasetAction(0, 0, urn, new MetamacWebCriteria()), new WaitingAsyncCallbackHandlingError<GetTemporalGranularitiesByDatasetResult>(this) {

            @Override
            public void onWaitSuccess(GetTemporalGranularitiesByDatasetResult result) {
                getView().setTemporalGranularities(result.getTemporalGranularities());
            }
        });
    }

    @Override
    public void retrieveDimensionsCoverage(List<String> dimensionsIds, MetamacWebCriteria metamacWebCriteria) {
        dispatcher.execute(new GetDatasetDimensionsCoverageAction(datasetVersionUrn, dimensionsIds, metamacWebCriteria),
                new WaitingAsyncCallbackHandlingError<GetDatasetDimensionsCoverageResult>(this) {

                    @Override
                    public void onWaitSuccess(GetDatasetDimensionsCoverageResult result) {
                        getView().setDimensionsCoverageValues(result.getCodesDimensions());
                    }
                });
    }

    //
    // RELATED RESOURCES
    //

    @Override
    public void retrieveItemsFromItemSchemeForDatasetLevelAttribute(RepresentationDto representationDto, int firstResult, int maxResults, MetamacWebCriteria criteria) {

        SrmItemRestCriteria itemRestCriteria = new SrmItemRestCriteria();
        itemRestCriteria.setCriteria(criteria.getCriteria());

        if (!StringUtils.isBlank(representationDto.getCodelistRepresentationUrn())) {
            itemRestCriteria.setItemSchemeUrn(representationDto.getCodelistRepresentationUrn());
            retrieveCodesFromItemSchemeForDatasetLevelAttribute(firstResult, maxResults, itemRestCriteria);

        } else if (!StringUtils.isBlank(representationDto.getConceptSchemeRepresentationUrn())) {
            itemRestCriteria.setItemSchemeUrn(representationDto.getConceptSchemeRepresentationUrn());
            retrieveConceptsFromItemSchemeForDatasetLevelAttribute(firstResult, maxResults, itemRestCriteria);
        }
    }

    private void retrieveCodesFromItemSchemeForDatasetLevelAttribute(int firstResult, int maxResults, SrmItemRestCriteria criteria) {
        dispatcher.execute(new GetCodesPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCodesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetCodesPaginatedListResult result) {
                getView().setItemsForDatasetLevelAttributeValueSelection(result.getCodes(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    private void retrieveConceptsFromItemSchemeForDatasetLevelAttribute(int firstResult, int maxResults, SrmItemRestCriteria criteria) {
        dispatcher.execute(new GetConceptsPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetConceptsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetConceptsPaginatedListResult result) {
                getView().setItemsForDatasetLevelAttributeValueSelection(result.getConcepts(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void retrieveItemsFromItemSchemeForDimensionOrGroupLevelAttribute(RepresentationDto representationDto, int firstResult, int maxResults, MetamacWebCriteria criteria) {

        SrmItemRestCriteria itemRestCriteria = new SrmItemRestCriteria();
        itemRestCriteria.setCriteria(criteria.getCriteria());

        if (!StringUtils.isBlank(representationDto.getCodelistRepresentationUrn())) {
            itemRestCriteria.setItemSchemeUrn(representationDto.getCodelistRepresentationUrn());
            retrieveCodesFromItemSchemeForDimensionOrGroupLevelAttribute(firstResult, maxResults, itemRestCriteria);

        } else if (!StringUtils.isBlank(representationDto.getConceptSchemeRepresentationUrn())) {
            itemRestCriteria.setItemSchemeUrn(representationDto.getConceptSchemeRepresentationUrn());
            retrieveConceptsFromItemSchemeForDimensionOrGroupLevelAttribute(firstResult, maxResults, itemRestCriteria);
        }
    }

    private void retrieveCodesFromItemSchemeForDimensionOrGroupLevelAttribute(int firstResult, int maxResults, SrmItemRestCriteria criteria) {
        dispatcher.execute(new GetCodesPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCodesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetCodesPaginatedListResult result) {
                getView().setItemsForDimensionOrGroupLevelAttributeValueSelection(result.getCodes(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    private void retrieveConceptsFromItemSchemeForDimensionOrGroupLevelAttribute(int firstResult, int maxResults, SrmItemRestCriteria criteria) {
        dispatcher.execute(new GetConceptsPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetConceptsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetConceptsPaginatedListResult result) {
                getView().setItemsForDimensionOrGroupLevelAttributeValueSelection(result.getConcepts(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void attributesImportationSucceed(String fileName) {
        ShowMessageEvent.fireSuccessMessage(DatasetAttributesTabPresenter.this, getMessages().attributesImportationPlanned());
        placeManager.revealCurrentPlace();
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void attributesImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(DatasetAttributesTabPresenter.this, errorMessage);
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
    public void exportAttributes(String urn) {
        dispatcher.execute(new ExportAttributesAction(urn), new WaitingAsyncCallbackHandlingError<ExportAttributesResult>(this) {

            @Override
            public void onWaitSuccess(ExportAttributesResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });
    }
}
