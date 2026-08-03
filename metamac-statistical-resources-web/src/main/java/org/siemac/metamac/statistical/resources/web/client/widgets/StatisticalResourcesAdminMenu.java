package org.siemac.metamac.statistical.resources.web.client.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetClientSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.enums.StatisticalResourcesToolStripAdminManagementButtonEnum;
import org.siemac.metamac.statistical.resources.web.client.resources.GlobalResources;
import org.siemac.metamac.statistical.resources.web.client.view.handlers.MainPageUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.GeographicalCacheUpdateOptionsWindow;
import org.siemac.metamac.statistical.resources.web.client.widgets.windows.KafkaTopicReloadOptionsWindow;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.WarningWindow;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class StatisticalResourcesAdminMenu extends ToolStrip {

    private MainPageUiHandlers                   uiHandlers;
    private GeographicalCacheUpdateOptionsWindow geographicalCacheUpdateOptionsWindow;
    private KafkaTopicReloadOptionsWindow        kafkaTopicReloadOptionsWindow;

    public StatisticalResourcesAdminMenu() {
        super();
        setWidth100();
        setAlign(Alignment.LEFT);

        createCacheUpdateOptionsWindow();
        createKafkaTopicReloadOptionsWindow();

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

        CustomToolStripButton reloadKafkaTopicsButton = new CustomToolStripButton(getConstants().reloadKafkaTopics(), GlobalResources.RESOURCE.reload().getURL());
        reloadKafkaTopicsButton.setID(StatisticalResourcesToolStripAdminManagementButtonEnum.RELOAD_KAFKA_TOPICS.getValue());
        reloadKafkaTopicsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                kafkaTopicReloadOptionsWindow.show();
            }
        });

        if (DatasetClientSecurityUtils.canResendAllKafkaMessages()) {
            addButton(reloadKafkaTopicsButton);
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

    private void createKafkaTopicReloadOptionsWindow() {
        kafkaTopicReloadOptionsWindow = new KafkaTopicReloadOptionsWindow(getConstants().reloadKafkaTopics(), getConstants().reloadKafkaTopicsOptionsMessage());
        kafkaTopicReloadOptionsWindow.setVisible(false);

        createKafkaTopicReloadClickAction();
    }

    private void createKafkaTopicReloadClickAction() {
        kafkaTopicReloadOptionsWindow.getReloadButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (kafkaTopicReloadOptionsWindow.validate()) {

                    final WarningWindow warningWindow = new WarningWindow(getConstants().warning(), getConstants().reloadKafkaTopicsWarning());
                    warningWindow.setCancelable(true);

                    warningWindow.getAcceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                        @Override
                        public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                            reloadKafkaTopics(kafkaTopicReloadOptionsWindow.getSelectedResourceTypes());
                            kafkaTopicReloadOptionsWindow.hide();
                        }
                    });

                } else {

                    final WarningWindow warningWindow = new WarningWindow(getConstants().warning(), getConstants().reloadKafkaTopicsSelectedWindowWarning());
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

    private void reloadKafkaTopics(List<StatisticalResourceTypeEnum> resourceTypes) {
        getUiHandlers().reloadKafkaTopics(resourceTypes);
    }
}
