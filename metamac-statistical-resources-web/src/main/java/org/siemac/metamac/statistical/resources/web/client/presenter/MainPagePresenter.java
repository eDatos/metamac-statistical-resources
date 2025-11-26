package org.siemac.metamac.statistical.resources.web.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.client.enums.StatisticalResourcesToolStripButtonEnum;
import org.siemac.metamac.statistical.resources.web.client.enums.StatisticalResourcesToolStripLayoutEnum;
import org.siemac.metamac.statistical.resources.web.client.events.DeselectMenuButtonsEvent;
import org.siemac.metamac.statistical.resources.web.client.events.DeselectMenuButtonsEvent.DeselectMenuButtonHandler;
import org.siemac.metamac.statistical.resources.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SelectMenuButtonEvent.SelectMenuButtonHandler;
import org.siemac.metamac.statistical.resources.web.client.events.SelectMenuLayoutEvent;
import org.siemac.metamac.statistical.resources.web.client.events.SelectMenuLayoutEvent.SelectMenuLayoutHandler;
import org.siemac.metamac.statistical.resources.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.statistical.resources.web.client.view.handlers.MainPageUiHandlers;
import org.siemac.metamac.statistical.resources.web.shared.base.GetHelpUrlAction;
import org.siemac.metamac.statistical.resources.web.shared.base.GetHelpUrlResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateGeographicCoverageVariableElementsCacheAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateGeographicCoverageVariableElementsCacheResult;
import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent.ChangeWaitPopupVisibilityHandler;
import org.siemac.metamac.web.common.client.events.HideMessageEvent;
import org.siemac.metamac.web.common.client.events.HideMessageEvent.HideMessageHandler;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent.SetTitleHandler;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent.ShowMessageHandler;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.client.widgets.BreadCrumbsPanel;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.MasterHead;
import org.siemac.metamac.web.common.shared.CloseSessionAction;
import org.siemac.metamac.web.common.shared.CloseSessionResult;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.client.proxy.SetPlaceTitleHandler;

public class MainPagePresenter extends Presenter<MainPagePresenter.MainPageView, MainPagePresenter.MainPageProxy>
        implements
            MainPageUiHandlers,
            ShowMessageHandler,
            HideMessageHandler,
            SetTitleHandler,
            SelectMenuButtonHandler,
            SelectMenuLayoutHandler,
            DeselectMenuButtonHandler,
            ChangeWaitPopupVisibilityHandler {

    private static Logger       logger = Logger.getLogger(MainPagePresenter.class.getName());

    private final PlaceManager  placeManager;
    private final DispatchAsync dispatcher;

    private static MasterHead   masterHead;

    private String              helpUrl;

    @ProxyStandard
    @NameToken(NameTokens.mainPage)
    @NoGatekeeper
    public interface MainPageProxy extends Proxy<MainPagePresenter>, Place {

    }

    public interface MainPageView extends View, HasUiHandlers<MainPageUiHandlers> {

        MasterHead getMasterHead();

        BreadCrumbsPanel getBreadCrumbsPanel();
        void clearBreadcrumbs(int size, PlaceManager placeManager);
        void setBreadcrumb(int index, String title);
        void showMessage(Throwable throwable, String message, MessageTypeEnum type);
        void hideMessages();
        void setTitle(String title);

        void selectMenuButton(StatisticalResourcesToolStripButtonEnum resourceType);

        void selectMenuLayout(StatisticalResourcesToolStripLayoutEnum resourceType);

        void deselectMenuButtons();

        void showWaitPopup();
        void hideWaitPopup();

        InformationWindow showInformationMessage(String title, String message);
    }

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     * Is used to define a type to use in child presenters when you want to
     * include them inside this page.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContent = new Type<RevealContentHandler<?>>();

    @Inject
    public MainPagePresenter(EventBus eventBus, MainPageView view, MainPageProxy proxy, PlaceManager placeManager, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        MainPagePresenter.masterHead = getView().getMasterHead();
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
    }

    @Override
    protected void onReset() {
        super.onReset();
        updateBreadcrumbs();
    }

    @Override
    protected void onBind() {
        super.onBind();
        dispatcher.execute(new GetHelpUrlAction(), new WaitingAsyncCallbackHandlingError<GetHelpUrlResult>(this) {

            @Override
            public void onWaitSuccess(GetHelpUrlResult result) {
                helpUrl = result.getHelpUrl();
                masterHead.setHelpLinkVisibility(helpUrl);
            }
        });
    }

    private void updateBreadcrumbs() {
        int size = placeManager.getHierarchyDepth();
        getView().clearBreadcrumbs(size, placeManager);
        for (int i = 0; i < size; ++i) {
            final int index = i;
            placeManager.getTitle(i, new SetPlaceTitleHandler() {

                @Override
                public void onSetPlaceTitle(String title) {
                    getView().setBreadcrumb(index, title);
                }
            });
        }
    }

    @Override
    protected void revealInParent() {
        RevealRootContentEvent.fire(this, this);
    }

    @Override
    public void onNavigationPaneSectionHeaderClicked(String place) {
        if (place.length() != 0) {
            PlaceRequest placeRequest = new PlaceRequest(place);
            placeManager.revealPlace(placeRequest);
        }
    }

    @Override
    public void onNavigationPaneSectionClicked(String place) {
        if (place.length() != 0) {
            PlaceRequest placeRequest = new PlaceRequest(place);
            placeManager.revealPlace(placeRequest);
        }
    }

    public static MasterHead getMasterHead() {
        return masterHead;
    }

    @ProxyEvent
    @Override
    public void onShowMessage(ShowMessageEvent event) {
        getView().showMessage(event.getThrowable(), event.getMessage(), event.getMessageType());
    }

    @ProxyEvent
    @Override
    public void onHideMessage(HideMessageEvent event) {
        hideMessages();
    }

    @ProxyEvent
    @Override
    public void onSetTitle(SetTitleEvent event) {
        getView().setTitle(event.getTitle());
    }

    @ProxyEvent
    @Override
    public void onSelectMenuButton(SelectMenuButtonEvent event) {
        getView().selectMenuButton(event.getResourceType());
    }

    @ProxyEvent
    @Override
    public void onDeselectMenuButton(DeselectMenuButtonsEvent event) {
        getView().deselectMenuButtons();
    }

    @ProxyEvent
    @Override
    public void onChangeWaitPopupVisibility(ChangeWaitPopupVisibilityEvent event) {
        if (event.isShowPopup()) {
            getView().showWaitPopup();
        } else {
            getView().hideWaitPopup();
        }
    }

    @ProxyEvent
    @Override
    public void onSelectMenuLayout(SelectMenuLayoutEvent event) {
        getView().selectMenuLayout(event.getResourceType());
    }

    @Override
    public void closeSession() {
        dispatcher.execute(new CloseSessionAction(), new AsyncCallback<CloseSessionResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error closing session");
                ShowMessageEvent.fireErrorMessage(MainPagePresenter.this, caught);
            }
            @Override
            public void onSuccess(CloseSessionResult result) {
                Window.Location.assign(result.getLogoutPageUrl());
            }
        });
    }

    @Override
    public void openHelpUrl() {
        if (helpUrl != null) {
            Window.open(helpUrl, "_blank", "");
        }
    }

    @Override
    public void updateGeographicCoverageVariableElementsCache() {
        final InformationWindow informationWindow = getView().showInformationMessage(StatisticalResourcesWeb.getMessages().updateGeographicCoverageVariableElementsCache(),
                StatisticalResourcesWeb.getMessages().updateGeographicCoverageVariableElementsCacheInProgress());
        dispatcher.execute(new UpdateGeographicCoverageVariableElementsCacheAction.Builder().build(), new AsyncCallback<UpdateGeographicCoverageVariableElementsCacheResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.WARNING, "Could not update geographic coverage variable elements cache", caught);
                informationWindow.hide();
                ShowMessageEvent.fireErrorMessage(MainPagePresenter.this, caught);
            }

            @Override
            public void onSuccess(UpdateGeographicCoverageVariableElementsCacheResult result) {
                logger.log(Level.INFO, "Update of geographic coverage variable elements cache successful");
                ShowMessageEvent.fireSuccessMessage(MainPagePresenter.this, StatisticalResourcesWeb.getMessages().updateGeographicCoverageVariableElementsCacheSuccessful());
            }
        });
    }

    private void hideMessages() {
        getView().hideMessages();
    }

    @Override
    public void goToDatasets() {
        List<PlaceRequest> operationHierarchy = getHierarchyUntilNameToken(NameTokens.operationPage);
        operationHierarchy.add(PlaceRequestUtils.buildRelativeDatasetsPlaceRequest());
        placeManager.revealPlaceHierarchy(operationHierarchy);
    }

    @Override
    public void goToPublications() {
        List<PlaceRequest> operationHierarchy = getHierarchyUntilNameToken(NameTokens.operationPage);
        operationHierarchy.add(PlaceRequestUtils.buildRelativePublicationsPlaceRequest());
        placeManager.revealPlaceHierarchy(operationHierarchy);
    }

    @Override
    public void goToQueries() {
        List<PlaceRequest> operationHierarchy = getHierarchyUntilNameToken(NameTokens.operationPage);
        operationHierarchy.add(PlaceRequestUtils.buildRelativeQueriesPlaceRequest());
        placeManager.revealPlaceHierarchy(operationHierarchy);
    }

    @Override
    public void goToMultidatasets() {
        List<PlaceRequest> operationHierarchy = getHierarchyUntilNameToken(NameTokens.operationPage);
        operationHierarchy.add(PlaceRequestUtils.buildRelativeMultidatasetsPlaceRequest());
        placeManager.revealPlaceHierarchy(operationHierarchy);
    }

    protected List<PlaceRequest> getHierarchyUntilNameToken(String nameToken) {
        List<PlaceRequest> filteredHierarchy = new ArrayList<PlaceRequest>();
        List<PlaceRequest> hierarchy = placeManager.getCurrentPlaceHierarchy();
        boolean found = false;
        for (int i = 0; i < hierarchy.size() && !found; i++) {
            PlaceRequest placeReq = hierarchy.get(i);
            if (placeReq.matchesNameToken(nameToken)) {
                found = true;
            }
            filteredHierarchy.add(placeReq);
        }

        return filteredHierarchy;
    }
}
