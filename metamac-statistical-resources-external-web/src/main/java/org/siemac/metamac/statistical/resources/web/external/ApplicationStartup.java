package org.siemac.metamac.statistical.resources.web.external;

import javax.servlet.ServletContextEvent;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;
import org.siemac.metamac.core.common.util.WebUtils;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationStartup extends ApplicationStartupListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        try {
            WebUtils.setAppsBaseUrl(configurationService.retrieveAppsInternalWebApplicationUrlBase());
            WebUtils.setApiBaseURL(configurationService.retrieveStatisticalResourcesExternalApiUrlBase());

            WebUtils.setAppStyleHeaderUrl(configurationService.retrieveAppStyleHeaderUrl());
            WebUtils.setAppStyleFooterUrl(configurationService.retrieveAppStyleFooterUrl());
        } catch (MetamacException e) {
            log.error("Error retrieving application configuration", e);
        }
    }

    @Override
    public String projectName() {
        return "statistical-resources-external";
    }

    @Override
    public void checkApplicationProperties() throws MetamacException {
        // Datasource
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_URL);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_PASSWORD);

        // Datasource repository
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_DRIVER_NAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_URL);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_USERNAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_PASSWORD);

        // Api
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.ENDPOINT_STATISTICAL_RESOURCES_EXTERNAL_API);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.ENDPOINT_SRM_EXTERNAL_API);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.ENDPOINT_COMMON_METADATA_EXTERNAL_API);

        // Web application - for use with bibliographicalCitation
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.WEB_APPLICATION_PORTAL_EXTERNAL_WEB_VISUALIZER);

        // Misc
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.METAMAC_EDITION_LANGUAGES);
    }
}
