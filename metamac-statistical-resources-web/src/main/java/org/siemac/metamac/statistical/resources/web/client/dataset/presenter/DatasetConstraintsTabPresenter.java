package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.ContentConstraintDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetConstraintsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectDatasetTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetDatasetEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CreateDatasetConstraintAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CreateDatasetConstraintResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetConstraintAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetConstraintResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetConstraintAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetConstraintResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetItemsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetItemsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveRegionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveRegionResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetSrmResourceRestrictionsListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetSrmResourceRestrictionsListResult;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
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

public class DatasetConstraintsTabPresenter extends Presenter<DatasetConstraintsTabPresenter.DatasetConstraintsTabView, DatasetConstraintsTabPresenter.DatasetConstraintsTabProxy>
        implements
            DatasetConstraintsTabUiHandlers {

    private DispatchAsync        dispatcher;
    private PlaceManager         placeManager;

    private DatasetVersionDto    datasetVersionDto;
    private ContentConstraintDto contentConstraintDto;

    public interface DatasetConstraintsTabView extends View, HasUiHandlers<DatasetConstraintsTabUiHandlers> {

        void setConstraint(DatasetVersionDto datasetVersionDto, ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto);
        void showDimensionConstraints(DsdDimensionDto dsdDimensionDto);
        void setRelatedDsdDimensions(List<DsdDimensionDto> dimensions);
        void setSrmResourceRestrictions(List<ExternalItemDto> restrictions, TypeExternalArtefactsEnum typeExternalItem);
        void setCodes(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos, String codeSrmRestriction);
        void setConcepts(DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> itemDtos);
        void updateDimensionsList(RegionValueDto regionValueDto);
        void setCodesWithSrmRestrictions(List<ItemDto> srmRestrictionCodes);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetConstraintsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetConstraintsTabProxy extends Proxy<DatasetConstraintsTabPresenter>, Place {
    }

    @Inject
    public DatasetConstraintsTabPresenter(EventBus eventBus, DatasetConstraintsTabView view, DatasetConstraintsTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DatasetPresenter.TYPE_SetContextAreaDataset, this);
    }

    @TitleFunction
    public String title() {
        return getConstants().breadcrumbDatasetConstraints();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectDatasetTabEvent.fire(this, DatasetTabTypeEnum.CONSTRAINTS);
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
        String datasetVersionCode = PlaceRequestUtils.getDatasetParamFromUrl(placeManager);
        String datasetVersionUrn = CommonUtils.generateDatasetUrn(datasetVersionCode);

        retrieveConstraint(datasetVersionUrn);
    }

    private void retrieveConstraint(final String datasetVersionUrn) {
        dispatcher.execute(new GetDatasetConstraintAction(datasetVersionUrn), new WaitingAsyncCallbackHandlingError<GetDatasetConstraintResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetConstraintsTabPresenter.this, datasetVersionUrn);
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasetConstraintResult result) {
                SetDatasetEvent.fire(DatasetConstraintsTabPresenter.this, result.getDatasetVersion());
                setConstraint(result.getDatasetVersion(), result.getContentConstraint(), result.getRegion());
                if (result.getContentConstraint() != null) {
                    // Load dimensions only if the constraint is enabled
                    retrieveDimensions(result.getDatasetVersion());
                }
            }
        });
    }

    @Override
    public void createConstraint() {
        dispatcher.execute(new CreateDatasetConstraintAction(datasetVersionDto.getUrn(), StatisticalResourcesDefaults.defaultAgency),
                new WaitingAsyncCallbackHandlingError<CreateDatasetConstraintResult>(this) {

                    @Override
                    public void onWaitSuccess(CreateDatasetConstraintResult result) {
                        fireSuccessMessage(getMessages().datasetConstraintEnabled());
                        setConstraint(datasetVersionDto, result.getContentConstraint(), null);
                        retrieveDimensions(datasetVersionDto);
                    }
                });
    }

    private void retrieveDimensions(final DatasetVersionDto datasetVersionDto) {
        dispatcher.execute(new GetDatasetDimensionsAction(datasetVersionDto.getRelatedDsd().getUrn()), new WaitingAsyncCallbackHandlingError<GetDatasetDimensionsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetConstraintsTabPresenter.this, datasetVersionDto.getUrn());
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetDatasetDimensionsResult result) {
                getView().setRelatedDsdDimensions(result.getDatasetVersionDimension());
            }
        });
    }

    private void setConstraint(DatasetVersionDto datasetVersionDto, ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto) {
        DatasetConstraintsTabPresenter.this.datasetVersionDto = datasetVersionDto;
        DatasetConstraintsTabPresenter.this.contentConstraintDto = contentConstraintDto;
        getView().setConstraint(datasetVersionDto, contentConstraintDto, regionValueDto);
    }

    @Override
    public void deleteConstraint(ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto) {
        dispatcher.execute(new DeleteDatasetConstraintAction(contentConstraintDto, regionValueDto), new WaitingAsyncCallbackHandlingError<DeleteDatasetConstraintResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDatasetConstraintResult result) {
                fireSuccessMessage(getMessages().datasetConstraintDisabled());
                getView().setConstraint(datasetVersionDto, null, null);
            }
        });
    }

    @Override
    public void retrieveCodes(DsdDimensionDto dsdDimensionDto, String codeSrmRestriction) {
        retrieveItems(dsdDimensionDto, dsdDimensionDto.getCodelistRepresentationUrn(), TypeExternalArtefactsEnum.CODELIST, codeSrmRestriction);
    }

    @Override
    public void retrieveRestrictions(final DsdDimensionDto dsdDimensionDto, final TypeExternalArtefactsEnum type) {
        SrmItemRestCriteria conditions = new SrmItemRestCriteria();
        conditions.setUrn(dsdDimensionDto.getCodelistRepresentationUrn());
        conditions.setExternalArtifactType(type);
        dispatcher.execute(new GetSrmResourceRestrictionsListAction(conditions), new WaitingAsyncCallbackHandlingError<GetSrmResourceRestrictionsListResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                if (CommonErrorUtils.isOperationNotAllowedException(caught)) {
                    ShowUnauthorizedDatasetWarningMessageEvent.fire(DatasetConstraintsTabPresenter.this, dsdDimensionDto.getCodelistRepresentationUrn());
                } else {
                    super.onWaitFailure(caught);
                }
            }
            @Override
            public void onWaitSuccess(GetSrmResourceRestrictionsListResult result) {
                getView().setSrmResourceRestrictions(result.getSrmResourceRestrictions(), type);
            }
        });
    }

    @Override
    public void retrieveConcepts(DsdDimensionDto dsdDimensionDto) {
        retrieveItems(dsdDimensionDto, dsdDimensionDto.getConceptSchemeRepresentationUrn(), TypeExternalArtefactsEnum.CONCEPT_SCHEME, null);
    }

    private void retrieveItems(final DsdDimensionDto dsdDimensionDto, String itemSchemeUrn, final TypeExternalArtefactsEnum itemSchemeType, final String codeSrmRestriction) {
        dispatcher.execute(new GetItemsAction(itemSchemeUrn, itemSchemeType, codeSrmRestriction), new WaitingAsyncCallbackHandlingError<GetItemsResult>(this) {

            @Override
            public void onWaitSuccess(GetItemsResult result) {
                if (TypeExternalArtefactsEnum.CODELIST.equals(itemSchemeType)) {
                    getView().setCodes(dsdDimensionDto, result.getItemScheme(), result.getItems(), codeSrmRestriction);
                } else if (TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(itemSchemeType)) {
                    getView().setConcepts(dsdDimensionDto, result.getItemScheme(), result.getItems());
                }
            }
        });
    }

    @Override
    public void saveRegion(String contentConstraintUrn, RegionValueDto regionToSave, final DsdDimensionDto selectedDimension) {
        dispatcher.execute(new SaveRegionAction(contentConstraintUrn, regionToSave), new WaitingAsyncCallbackHandlingError<SaveRegionResult>(this) {

            @Override
            public void onWaitSuccess(SaveRegionResult result) {
                fireSuccessMessage(getMessages().datasetConstraintSaved());
                getView().setConstraint(datasetVersionDto, contentConstraintDto, result.getSavedRegion());
                getView().updateDimensionsList(result.getSavedRegion());
                getView().showDimensionConstraints(selectedDimension);
            }
        });
    }

    @Override
    public void applySrmRestrictions(List<ItemDto> srmRestrictionCodes) {

    }
}
