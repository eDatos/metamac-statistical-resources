package org.siemac.metamac.statistical.resources.web.client.dataset.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetsGroupUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.SelectDatasetInGroupTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SelectDatasetInGroupTabEvent.SelectDatasetInGroupTabHandler;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedDatasetWarningMessageEvent.ShowUnauthorizedDatasetWarningMessageHandler;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationPresenter;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.MultipleDatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetMultipleDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetMultipleDatasetVersionsResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionMetadataInGroupAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionMetadataInGroupResult;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class DatasetsGroupPresenter extends Presenter<DatasetsGroupPresenter.DatasetsGroupView, DatasetsGroupPresenter.DatasetsGroupProxy>
        implements
            DatasetsGroupUiHandlers,
            SelectDatasetInGroupTabHandler,
            ShowUnauthorizedDatasetWarningMessageHandler {

    private PlaceManager                              placeManager;
    private DispatchAsync                             dispatcher;
    private String                                    operationUrn;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaDataset         = new Type<RevealContentHandler<?>>();

    // @formatter:off
    private static final List<String>                 DATASET_GROUP_EXPECTED_NAME_TOKENS = Collections.unmodifiableList(
            Arrays.asList(
                    NameTokens.datasetInGroupMetadataPage, 
                    NameTokens.datasetInGroupCategorisationsPage));
    // @formatter:on

    public interface DatasetsGroupView extends View, HasUiHandlers<DatasetsGroupUiHandlers> {

        void setDataset(DatasetVersionDto datasetDto);
        void setDatasetVersionsSelected(List<DatasetVersionBaseDto> datasetVersionBaseDtos);
        void selectMetadataTab();
        void selectCategorisationsTab();
        void refreshStatusUpdateDatasetVersionInProgress(String urn, StreamMessageStatusEnum status, MetamacWebException notificationException);
        void showUnauthorizedResourceWarningMessage();
        void setDatasetsForAdd(GetDatasetVersionsResult result);
        void initChildViews();

    }

    @ProxyCodeSplit
    @NameToken(NameTokens.datasetsGroupPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DatasetsGroupProxy extends Proxy<DatasetsGroupPresenter>, Place {
    }

    @Inject
    public DatasetsGroupPresenter(EventBus eventBus, DatasetsGroupView view, DatasetsGroupProxy proxy, PlaceManager placeManager, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
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

        getView().selectMetadataTab();
        if (NameTokens.datasetsGroupPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
            goToDatasetInGroupMetadata();
            getView().initChildViews();
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
        String operationUrn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_OPERATION_PREFIX, operationCode);

        if (PlaceRequestUtils.isExpectedCurrentPlaceRequestNameToken(placeManager, DatasetsGroupPresenter.DATASET_GROUP_EXPECTED_NAME_TOKENS)) {
            if (!StringUtils.isBlank(operationCode)) {
                loadInitialData(operationUrn);
            } else {
                StatisticalResourcesWeb.showErrorPage();
            }
        }
    }

    @ProxyEvent
    @Override
    public void onSelectDatasetInGroupTab(SelectDatasetInGroupTabEvent event) {
        DatasetTabTypeEnum type = event.getDatasetTabTypeEnum();
        if (DatasetTabTypeEnum.CATEGORISATIONS.equals(type)) {
            getView().selectCategorisationsTab();
        } else {
            getView().selectMetadataTab();
        }
    }

    @ProxyEvent
    @Override
    public void onShowUnauthorizedDatasetWarningMessage(ShowUnauthorizedDatasetWarningMessageEvent event) {
        getView().showUnauthorizedResourceWarningMessage();
    }

    private void loadInitialData(String operationUrn) {
        this.operationUrn = operationUrn;
        List<String> datasetsIdentifiers = PlaceRequestUtils.getDatasetsInGroupParamFromUrl(placeManager);
        List<String> datasetsUrns = new ArrayList<String>();
        for (String id : datasetsIdentifiers) {
            datasetsUrns.add(CommonUtils.generateDatasetUrn(id));
        }

        retrieveDatasetVersions(datasetsUrns);

    }

    private void retrieveDatasetVersions(final List<String> datasetUrns) {

        MultipleDatasetVersionWebCriteria criteria = new MultipleDatasetVersionWebCriteria();
        criteria.setDatasetVersionUrns(datasetUrns);
        dispatcher.execute(new GetMultipleDatasetVersionsAction(0, StatisticalResourceWebConstants.MAIN_LIST_MAX_RESULTS, criteria),
                new WaitingAsyncCallbackHandlingError<GetMultipleDatasetVersionsResult>(this) {

                    @Override
                    public void onWaitSuccess(GetMultipleDatasetVersionsResult result) {
                        getView().setDatasetVersionsSelected(result.getDatasetVersionBaseDtos());
                    }
                });

    }

    @Override
    public void updateDatasets(final String urn, DatasetVersionDto datasetChangedMetadataDto, List<CategorisationDto> categorisations) {
        try {
            List<String> urns = new ArrayList<String>();
            urns.add(urn);
            dispatcher.execute(new UpdateDatasetVersionMetadataInGroupAction(urn, datasetChangedMetadataDto, categorisations),
                    new WaitingAsyncCallbackHandlingError<UpdateDatasetVersionMetadataInGroupResult>(this) {

                        @Override
                        public void onWaitFailure(Throwable caught) {
                            hideWaitPopup();
                            super.onWaitFailure(caught);
                        }

                        @Override
                        public void onWaitSuccess(UpdateDatasetVersionMetadataInGroupResult result) {
                            getView().refreshStatusUpdateDatasetVersionInProgress(urn, StreamMessageStatusEnum.SENT, result.getNotificationException());
                        }
                    });
        } catch (Exception e) {
            hideWaitPopup();
        }
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
    }

    @Override
    public void hideWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showUpdateResults(MetamacWebException notificationException) {
        if (notificationException == null) {
            ShowMessageEvent.fireSuccessMessage(DatasetsGroupPresenter.this, StatisticalResourcesWeb.getMessages().datasetGroupSaved());
        } else {
            ShowMessageEvent.fireWarningMessageWithError(DatasetsGroupPresenter.this, StatisticalResourcesWeb.getMessages().errorUpdateDatasestInGroup(), notificationException);
        }
    }

    @Override
    public void retrieveDatasets(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria, ProcStatusEnum status) {

        DatasetVersionWebCriteria versionableCriteria = new DatasetVersionWebCriteria(criteria.getCriteria());
        versionableCriteria.setOnlyLastVersion(criteria.isOnlyLastVersion());
        versionableCriteria.setStatisticalOperationUrn(operationUrn);
        versionableCriteria.setProcStatus(status);

        dispatcher.execute(new GetDatasetVersionsAction(firstResult, maxResults, versionableCriteria), new WaitingAsyncCallbackHandlingError<GetDatasetVersionsResult>(this) {

            @Override
            public void onWaitSuccess(GetDatasetVersionsResult result) {
                getView().setDatasetsForAdd(result);
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goToDatasetInGroupMetadata() {
        goToTab(NameTokens.datasetInGroupMetadataPage);
    }

    @Override
    public void goToDatasetCategorisations() {
        goToTab(NameTokens.datasetInGroupCategorisationsPage);
    }

    private void goToTab(String tabNameToken) {
        List<PlaceRequest> hierarchy = PlaceRequestUtils.getHierarchyUntilNameToken(placeManager, NameTokens.datasetsGroupPage);
        hierarchy.add(new PlaceRequest(tabNameToken));
        placeManager.revealPlaceHierarchy(hierarchy);
    }

    @Override
    public void showMessageMaxDatasetsExceeded(String message) {
        ShowMessageEvent.fireErrorMessage(DatasetsGroupPresenter.this, message);
    }

    @Override
    public void goTo(List<PlaceRequest> location) {
        // TODO Auto-generated method stub

    }

}
