package org.siemac.metamac.statistical.resources.web.external;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;

public class DeprecationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (requestIsDeprecated(request)) {
            response.addHeader("Deprecation", "true");
            response.addHeader("Link", String.format("<%s>; rel=\"deprecation\"", getApiBaseUrl()));
        }
        chain.doFilter(request, response);
    }

    private boolean requestIsDeprecated(HttpServletRequest request) {
        // EDATOS-3732 - dim is deprecated in favour of representation
        String dim = request.getParameter("dim");
        if (StringUtils.isNotEmpty(dim)) {
            return true;
        }

        return false;
    }

    private ConfigurationService configurationService = null;

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
    public void destroy() {
    }

}
