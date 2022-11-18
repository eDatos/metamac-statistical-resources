package org.siemac.metamac.statistical.resources.core.utils;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;

public class MetamacPortalUtils {

    private static final String PAGE_DATA_RESOURCE                  = "data.html";
    private static final String URL_SEPARATOR                       = "/";
    private static final String URL_SINGLEPAGE_SEPARATOR            = "#";
    private static final String URL_QUERY_SEPARATOR                 = "?";
    private static final String URL_QUERY_EQUALS                    = "=";
    private static final String URL_QUERY_AND                       = "&";
    private static final String URL_QUERY_PARAMETER_RESOURCEID      = "resourceId";
    private static final String URL_QUERY_PARAMETER_AGENCYID        = "agencyId";
    private static final String URL_QUERY_PARAMETER_VERSION         = "version";
    private static final String URL_QUERY_PARAMETER_RESOURCETYPE    = "resourceType";
    private static final String URL_QUERY_RESOURCE_TYPE_DATASET     = "dataset";

    private MetamacPortalUtils() {
        
    }
    
    private static String buildEndpointUrl(StatisticalResourcesConfiguration configurationService) throws MetamacException {
        String urlBase = configurationService.retrievePortalExternalWebApplicationUrlVisualizer();
        StringBuilder builder = new StringBuilder().append(urlBase);

        if (!StringUtils.isBlank(urlBase) && !urlBase.endsWith(URL_SEPARATOR)) {
            builder.append(URL_SEPARATOR);
        }
        return builder.toString();
    }

    private static String buildQueryParametersForNotVersionableResource(LifeCycleStatisticalResource lifeCycleStatisticalResource) {
        StringBuilder builder = new StringBuilder();

        if (lifeCycleStatisticalResource != null) {

            String maintainerCode = lifeCycleStatisticalResource.getMaintainer() != null ? lifeCycleStatisticalResource.getMaintainer().getCode() : null;
            String code = lifeCycleStatisticalResource.getCode();
            String resourceType = URL_QUERY_RESOURCE_TYPE_DATASET;

            builder.append(URL_QUERY_PARAMETER_RESOURCETYPE);
            builder.append(URL_QUERY_EQUALS);
            builder.append(resourceType);
            builder.append(URL_QUERY_AND);
            builder.append(URL_QUERY_PARAMETER_AGENCYID);
            builder.append(URL_QUERY_EQUALS);
            builder.append(maintainerCode);
            builder.append(URL_QUERY_AND);
            builder.append(URL_QUERY_PARAMETER_RESOURCEID);
            builder.append(URL_QUERY_EQUALS);
            builder.append(code);
        }
        return builder.toString();
    }

    private static String buildQueryParametersForVersionableResource(LifeCycleStatisticalResource lifeCycleStatisticalResource) throws MetamacException {
        StringBuilder builder = new StringBuilder();

        if (lifeCycleStatisticalResource != null) {
            String parametersForNotVersionableResource = buildQueryParametersForNotVersionableResource(lifeCycleStatisticalResource);
            builder.append(parametersForNotVersionableResource);
            builder.append(URL_QUERY_AND);
            builder.append(URL_QUERY_PARAMETER_VERSION);
            builder.append(URL_QUERY_EQUALS);
            builder.append(lifeCycleStatisticalResource.getVersionLogic());
        }

        return builder.toString();

    }

    public static String buildDatasetVersionUrl(DatasetVersion datasetVersion, StatisticalResourcesConfiguration configurationService) throws MetamacException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(configurationService));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(datasetVersion.getLifeCycleStatisticalResource()));
        builder.append(URL_SINGLEPAGE_SEPARATOR);

        return builder.toString();
    }

}
