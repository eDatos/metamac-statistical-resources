package org.siemac.metamac.statistical.resources.web.client.publication.presenter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.LoggedInGatekeeper;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesDefaults;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.enums.PublicationTabTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.events.RequestPublicationVersionsReloadEvent;
import org.siemac.metamac.statistical.resources.web.client.events.RequestPublicationVersionsReloadEvent.RequestPublicationVersionsReloadHandler;
import org.siemac.metamac.statistical.resources.web.client.events.SelectPublicationTabEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SelectPublicationTabEvent.SelectPublicationTabHandler;
import org.siemac.metamac.statistical.resources.web.client.events.SetPublicationEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SetPublicationEvent.SetPublicationHandler;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedPublicationWarningMessageEvent;
import org.siemac.metamac.statistical.resources.web.client.events.ShowUnauthorizedPublicationWarningMessageEvent.ShowUnauthorizedPublicationWarningMessageHandler;
import org.siemac.metamac.statistical.resources.web.client.operation.presenter.OperationPresenter;
import org.siemac.metamac.statistical.resources.web.client.publication.view.handlers.PublicationUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetStatisticalOperationResult;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetVersionsOfPublicationAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.GetVersionsOfPublicationResult;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

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
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class PublicationPresenter extends Presenter<PublicationPresenter.PublicationView, PublicationPresenter.PublicationProxy>
        implements
            PublicationUiHandlers,
            RequestPublicationVersionsReloadHandler,
            SelectPublicationTabHandler,
            SetPublicationHandler,
            ShowUnauthorizedPublicationWarningMessageHandler {

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaPublication   = new Type<RevealContentHandler<?>>();

    // @formatter:off
    private static final List<String>                 PUBLICATION_EXPECTED_NAME_TOKENS = Collections.unmodifiableList(
            Arrays.asList(
                    NameTokens.publicationMetadataPage, 
                    NameTokens.publicationStructurePage));
    // @formatter:on

    @ProxyCodeSplit
    @NameToken(NameTokens.publicationPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface PublicationProxy extends Proxy<PublicationPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getPublicationBreadCrumbTitle(placeRequest);
    }

    public interface PublicationView extends View, HasUiHandlers<PublicationUiHandlers> {

        void setPublication(PublicationVersionDto publicationDto);
        void setPublicationVersionsAndSelectCurrent(String currentUrn, List<PublicationVersionBaseDto> publicationVersionBaseDtos);
        void selectMetadataTab();
        void selectStructureTab();
        void showUnauthorizedResourceWarningMessage();
    }

    @Inject
    public PublicationPresenter(EventBus eventBus, PublicationView publicationView, PublicationProxy publicationProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, publicationView, publicationProxy);
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

        // Redirect to metadata tab
        getView().selectMetadataTab();
        if (NameTokens.publicationPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
            goToPublicationMetadata();
            loadInitialData();
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        // This check is a workaround to solve the issue described in METAMAC-2920. Probably isn't the best way, and the correct way should be resolving the refresh problem that consists in don't show
        // the previous page before going to a new page.
        if (PlaceRequestUtils.isExpectedCurrentPlaceRequestNameToken(placeManager, PublicationPresenter.PUBLICATION_EXPECTED_NAME_TOKENS)) {
            String operationCode = PlaceRequestUtils.getOperationParamFromUrl(placeManager);
            String publicationCode = PlaceRequestUtils.getPublicationParamFromUrl(placeManager);
            if (!StringUtils.isBlank(operationCode) && !StringUtils.isBlank(publicationCode)) {
                String operationUrn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_OPERATION_PREFIX, operationCode);

                if (!CommonUtils.isUrnFromSelectedStatisticalOperation(operationUrn)) {
                    retrieveOperation(operationUrn);
                } else {
                    loadInitialData();
                }

            } else {
                StatisticalResourcesWeb.showErrorPage();
            }
        }
    }

    @ProxyEvent
    @Override
    public void onSelectPublicationTab(SelectPublicationTabEvent event) {
        PublicationTabTypeEnum type = event.getPublicationTabTypeEnum();
        if (PublicationTabTypeEnum.STRUCTURE.equals(type)) {
            getView().selectStructureTab();
        } else {
            getView().selectMetadataTab();
        }
    }

    @ProxyEvent
    @Override
    public void onRequestPublicationVersionsReload(RequestPublicationVersionsReloadEvent event) {
        retrievePublicationVersions(event.getPublicationVersionUrn());
        goToPublicationVersion(event.getPublicationVersionUrn());
    }

    @ProxyEvent
    @Override
    public void onSetPublicationVersion(SetPublicationEvent event) {
        getView().setPublication(event.getPublicationVersionDto());
    }

    @ProxyEvent
    @Override
    public void onShowUnauthorizedPublicationWarningMessage(ShowUnauthorizedPublicationWarningMessageEvent event) {
        retrievePublicationVersions(event.getUrn());
        getView().showUnauthorizedResourceWarningMessage();
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
        String publicationIdentifier = PlaceRequestUtils.getPublicationParamFromUrl(placeManager);
        String publicationVersionUrn = CommonUtils.generatePublicationUrn(publicationIdentifier);
        retrievePublicationVersions(publicationVersionUrn);
    }

    private void retrievePublicationVersions(final String urn) {
        dispatcher.execute(new GetVersionsOfPublicationAction(urn), new WaitingAsyncCallbackHandlingError<GetVersionsOfPublicationResult>(this) {

            @Override
            public void onWaitSuccess(GetVersionsOfPublicationResult result) {
                getView().setPublicationVersionsAndSelectCurrent(urn, result.getPublicationVersionBaseDtos());
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goToPublicationMetadata() {
        List<PlaceRequest> hierarchy = PlaceRequestUtils.getHierarchyUntilNameToken(placeManager, NameTokens.publicationPage);
        hierarchy.add(new PlaceRequest(NameTokens.publicationMetadataPage));
        placeManager.revealPlaceHierarchy(hierarchy);
    }

    @Override
    public void goToPublicationStructure() {
        List<PlaceRequest> hierarchy = PlaceRequestUtils.getHierarchyUntilNameToken(placeManager, NameTokens.publicationPage);
        hierarchy.add(new PlaceRequest(NameTokens.publicationStructurePage));
        placeManager.revealPlaceHierarchy(hierarchy);
    }

    @Override
    public void goToPublicationVersion(String urn) {
        List<PlaceRequest> hierarchy = PlaceRequestUtils.getHierarchyUntilNameToken(placeManager, NameTokens.publicationsListPage);
        hierarchy.add(PlaceRequestUtils.buildRelativePublicationPlaceRequest(urn));
        placeManager.revealPlaceHierarchy(hierarchy);
    }
}
