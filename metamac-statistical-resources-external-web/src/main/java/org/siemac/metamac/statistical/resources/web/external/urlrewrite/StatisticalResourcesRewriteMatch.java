package org.siemac.metamac.statistical.resources.web.external.urlrewrite;

import javax.servlet.ServletException;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.urlrewrite.AbstractRewriteMatch;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;

class StatisticalResourcesRewriteMatch extends AbstractRewriteMatch {

    private ConfigurationService configurationService = null;

    @Override
    protected String[] getAcceptedApiPrefixes() {
        return new String[]{"statistical-resources"};
    }

    @Override
    protected String getLatestApiVersion() {
        return StatisticalResourcesRestExternalConstants.API_VERSION_1_0;
    }

    @Override
    protected String getApiBaseUrl() throws ServletException {
        try {
            return getConfigurationService().retrieveStatisticalResourcesExternalApiUrlBase();
        } catch (MetamacException e) {
            throw new ServletException("Error retrieving configuration property of the external API URL base", e);
        }
    }

    private ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
        }
        return configurationService;
    }

    @Override
    protected String getApiVersionsListResource() {
        return "/index.jsp";
    }
}
