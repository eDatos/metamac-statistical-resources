package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.enums.StatisticalResourcesToolStripAdminManagementButtonEnum;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.statistical.resources.web.client.view.handlers.MainPageUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.GeographicalCacheUpdateOptionsWindow;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.WarningWindow;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class StatisticalResourcesAdminMenu extends ToolStrip {

    private MainPageUiHandlers                   uiHandlers;
    private GeographicalCacheUpdateOptionsWindow geographicalCacheUpdateOptionsWindow;

    public StatisticalResourcesAdminMenu() {
        super();
        setWidth100();
        setAlign(Alignment.LEFT);

        createCacheUpdateOptionsWindow();

        CustomToolStripButton updateGeographicCoverageVariableElementsCacheButton = new CustomToolStripButton(getConstants().updateGeographicCoverageVariableElementsCache(),
                GlobalResources.RESOURCE.reload().getURL());
        updateGeographicCoverageVariableElementsCacheButton.setID(StatisticalResourcesToolStripAdminManagementButtonEnum.UPDATE_GEOCOV_VARELEM_CACHE.getValue());
        updateGeographicCoverageVariableElementsCacheButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                geographicalCacheUpdateOptionsWindow.show();
            }
        });

        if (DatasetClientSecurityUtils.canUpdateAllGeographicCoverageVariableElementsCache()) {
            addButton(updateGeographicCoverageVariableElementsCacheButton);
        }

    }

    private void createCacheUpdateOptionsWindow() {
        geographicalCacheUpdateOptionsWindow = new GeographicalCacheUpdateOptionsWindow(getConstants().updateGeographicCoverageVariableElementsCache(),
                getConstants().updateGeographicalCacheResourcesOptionsMessage());
        geographicalCacheUpdateOptionsWindow.setVisible(false);

        createCacheUpdateOptionsClickAction();
    }

    private void createCacheUpdateOptionsClickAction() {
        geographicalCacheUpdateOptionsWindow.getUpdateCacheButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (geographicalCacheUpdateOptionsWindow.validate()) {

                    final WarningWindow warningWindow = new WarningWindow(getConstants().warning(), getConstants().updateGeographicCoverageVariableElementsCacheWarning());
                    warningWindow.setCancelable(true);

                    warningWindow.getAcceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                            updateGeographicCoverageVariableElementsCache(geographicalCacheUpdateOptionsWindow.getStatisticalResourcesSelectedOptions(false),
                                    geographicalCacheUpdateOptionsWindow.getStatisticalResourcesSelectedOptions(true));
                            geographicalCacheUpdateOptionsWindow.hide();
                        }
                    });

                } else {

                    final WarningWindow warningWindow = new WarningWindow(getConstants().warning(), getConstants().updateGeoCacheSelectedWindowWarning());
                    warningWindow.setCancelable(false);

                    warningWindow.getAcceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                            warningWindow.markForDestroy();
                        }
                    });
                }
            }
        });

    }

    public void setUiHandlers(MainPageUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public MainPageUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private void updateGeographicCoverageVariableElementsCache(List<StatisticalResourceTypeEnum> resourcesToUpdate, List<StatisticalResourceTypeEnum> externalResourcesToUpdate) {
        getUiHandlers().updateGeographicCoverageVariableElementsCache(resourcesToUpdate, externalResourcesToUpdate);
    }
}
