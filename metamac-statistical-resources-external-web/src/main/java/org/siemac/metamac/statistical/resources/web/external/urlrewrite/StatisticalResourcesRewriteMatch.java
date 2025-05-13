package org.siemac.metamac.statistical.resources.web.external.urlrewrite;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.siemac.edatos.core.common.constants.CoreCommonConstants.API_LATEST;
import static org.siemac.edatos.core.common.constants.CoreCommonConstants.URL_SEPARATOR;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;

class StatisticalResourcesRewriteMatch extends RewriteMatch {

    private ConfigurationService configurationService    = null;
    private Pattern              apiVersionsListPattern  = Pattern.compile(".*/apis/(" + StringUtils.join(getAcceptedApiPrefixes(), "|") + ")(/?)");
    private Pattern              apiUrlPattern           = Pattern.compile(".*/apis/(" + StringUtils.join(getAcceptedApiPrefixes(), "|") + ")/(v\\d+\\.\\d+|" + API_LATEST + ")(/(.*)?)?");
    private Pattern              swaggerResourcesPattern = Pattern.compile(".*/apis/(" + StringUtils.join(getAcceptedApiPrefixes(), "|") + ")(/swagger-ui/.*)");

    private String               currentApiPrefix        = null;
    private String               WADL_QUERY              = "_wadl";

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        Matcher apiListMatcher = apiVersionsListPattern.matcher(requestURI);
        boolean queryStringContainsWadlQuery = queryString != null && queryString.contains(WADL_QUERY);
        if (apiListMatcher.matches() && !queryStringContainsWadlQuery) {
            currentApiPrefix = apiListMatcher.group(1);
            ServletContext context = request.getSession().getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher(getApiVersionsListResource());
            requestDispatcher.forward(request, response);
            return true;
        }

        Matcher apiUrlmatcher = apiUrlPattern.matcher(requestURI);
        if (apiUrlmatcher.matches() && apiUrlmatcher.groupCount() > 3) {
            currentApiPrefix = apiUrlmatcher.group(1);
            String requestApiVersion = apiUrlmatcher.group(2);
            String requestPathAfterVersion = apiUrlmatcher.group(3);
            if (API_LATEST.equals(requestApiVersion)) {
                String internalBase = "/apis/statistical-resources/v1.0";
                String internalPath = internalBase + (requestPathAfterVersion != null ? requestPathAfterVersion : "/");

                String target = (queryString != null && !queryString.isEmpty())
                               ? internalPath + "?" + queryString
                               : internalPath;
                request.getRequestDispatcher(target).forward(request, response);
                return true;
            } else if (requestPathAfterVersion == null && requestURI.endsWith(requestApiVersion) && isBlank(queryString)) {
                String location = buildTargetLocation(requestApiVersion, requestPathAfterVersion, queryString);
                response.sendRedirect(location);
                return true;
            }
        }
        Matcher swaggerResourcesMatcher = swaggerResourcesPattern.matcher(requestURI);
        if (swaggerResourcesMatcher.matches() && apiUrlmatcher.groupCount() > 1) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(swaggerResourcesMatcher.group(2));
            requestDispatcher.forward(request, response);
            return true;
        }
        return false;
    }

    private String buildTargetLocation(String apiVersion, String requestPathAfterVersion, String queryString) throws ServletException {
        return getApiBaseUrl() + URL_SEPARATOR + apiVersion + (requestPathAfterVersion == null ? URL_SEPARATOR : requestPathAfterVersion) + (isBlank(queryString) ? EMPTY : "?" + queryString);
    }

    protected String[] getAcceptedApiPrefixes() {
        return new String[]{"statistical-resources"};
    }

    protected String getLatestApiVersion() {
        return StatisticalResourcesRestExternalConstants.API_VERSION_1_0;
    }

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

    protected String getApiVersionsListResource() {
        return "/index.jsp";
    }
}
