package org.siemac.metamac.statistical.resources.core.common.utils;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;

public class PortalWebCoreUtils {
    
    private PortalWebCoreUtils() {
        //NOTHING
    }
    
    private static final String PAGE_DATA_RESOURCE                  = "data.html";
    private static final String URL_SEPARATOR                       = "/";
    private static final String URL_QUERY_SEPARATOR                 = "?";
    private static final String URL_QUERY_EQUALS                    = "=";
    private static final String URL_QUERY_AND                       = "&";
    private static final String URL_QUERY_PARAMETER_RESOURCEID      = "resourceId";
    private static final String URL_QUERY_PARAMETER_AGENCYID        = "agencyId";
    private static final String URL_QUERY_PARAMETER_VERSION         = "version";
    private static final String URL_QUERY_PARAMETER_RESOURCETYPE    = "resourceType";
    private static final String URL_QUERY_RESOURCE_TYPE_DATASET     = "dataset";

    public static String buildDatasetVersionUrl(DatasetVersion datasetVersion, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(datasetVersion, StatisticalResourceTypeEnum.DATASET));

        return builder.toString();
    }

    private static String buildEndpointUrl(String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder().append(urlBase);

        if (!StringUtils.isBlank(urlBase) && !urlBase.endsWith(URL_SEPARATOR)) {
            builder.append(URL_SEPARATOR);
        }
        return builder.toString();
    }

    private static String buildQueryParametersForVersionableResource(DatasetVersion datasetVersion, StatisticalResourceTypeEnum type)
            throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();

        if (datasetVersion.getSiemacMetadataStatisticalResource() != null) {
            String maintainerCode = datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer() != null ? datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCode() : null;
            String code = datasetVersion.getSiemacMetadataStatisticalResource().getCode();
            return buildQueryParametersForVersionableResource(maintainerCode, code, datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic(), type);
        }

        return builder.toString();
    }

    private static String buildQueryParametersForVersionableResource(String maintainerCode, String code, String version, StatisticalResourceTypeEnum type) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();

        String parametersForNotVersionableResource = buildQueryParametersForNotVersionableResource(maintainerCode, code, type);
        builder.append(parametersForNotVersionableResource);
        builder.append(URL_QUERY_AND);
        builder.append(URL_QUERY_PARAMETER_VERSION);
        builder.append(URL_QUERY_EQUALS);
        builder.append(version);

        return builder.toString();

    }

    private static String buildQueryParametersForNotVersionableResource(String maintainerCode, String code, StatisticalResourceTypeEnum type) {
        StringBuilder builder = new StringBuilder();


        builder.append(URL_QUERY_PARAMETER_RESOURCETYPE);
        builder.append(URL_QUERY_EQUALS);
        builder.append(URL_QUERY_RESOURCE_TYPE_DATASET);
        builder.append(URL_QUERY_AND);
        builder.append(URL_QUERY_PARAMETER_AGENCYID);
        builder.append(URL_QUERY_EQUALS);
        builder.append(maintainerCode);
        builder.append(URL_QUERY_AND);
        builder.append(URL_QUERY_PARAMETER_RESOURCEID);
        builder.append(URL_QUERY_EQUALS);
        builder.append(code);
        return builder.toString();
    }
}
