package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.web.client.enums.StatisticalResourcesToolStripAdminManagementButtonEnum;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.statistical.resources.web.client.view.handlers.MainPageUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class StatisticalResourcesAdminMenu extends ToolStrip {

    private MainPageUiHandlers uiHandlers;

    public StatisticalResourcesAdminMenu() {
        super();
        setWidth100();
        setAlign(Alignment.LEFT);

        CustomToolStripButton updateTerritoriesCacheButton = new CustomToolStripButton(getConstants().updateTerritoriesCache(), GlobalResources.RESOURCE.reload().getURL());
        updateTerritoriesCacheButton.setID(StatisticalResourcesToolStripAdminManagementButtonEnum.UPDATE_TERRITORIES_CACHE.getValue());
        updateTerritoriesCacheButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final WarningWindow warningWindow = new WarningWindow(getConstants().updateTerritoriesCacheWarning());

                warningWindow.getAcceptButtomItem().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        warningWindow.destroy();
                        updateTerritoriesCache();
                    }
                });

                warningWindow.getCancelButtomItem().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        warningWindow.destroy();
                    }
                });
            }
        });

        addButton(updateTerritoriesCacheButton);
    }

    public void setUiHandlers(MainPageUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public MainPageUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private void updateTerritoriesCache() {
        getUiHandlers().updateTerritoriesCache();
    }
}
