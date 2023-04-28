package org.siemac.metamac.statistical.resources.core.utils.shared;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;

public class MetamacPortalWebUtils {

    private static final String PAGE_DATA_RESOURCE                  = "data.html";
    private static final String PAGE_COLLECTION_RESOURCE            = "collection.html";
    private static final String URL_SEPARATOR                       = "/";
    private static final String URL_QUERY_SEPARATOR                 = "?";
    private static final String URL_QUERY_EQUALS                    = "=";
    private static final String URL_QUERY_AND                       = "&";
    private static final String URL_QUERY_PARAMETER_RESOURCEID      = "resourceId";
    private static final Object URL_QUERY_COLON                     = ":";
    private static final String URL_QUERY_PARAMETER_AGENCYID        = "agencyId";
    private static final String URL_QUERY_PARAMETER_VERSION         = "version";
    private static final String URL_QUERY_PARAMETER_RESOURCETYPE    = "resourceType";

    private static final String URL_QUERY_PARAMETER_MULTIDATASET_ID = "multidatasetId";

    private static final String URL_QUERY_RESOURCE_TYPE_DATASET     = "dataset";
    private static final String URL_QUERY_RESOURCE_TYPE_QUERY       = "query";
    private static final String URL_QUERY_RESOURCE_TYPE_COLLECTION  = "collection";

    public static String buildDatasetVersionUrl(DatasetVersionDto datasetVersionDto, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(datasetVersionDto, StatisticalResourceTypeEnum.DATASET));

        return builder.toString();
    }

    public static String buildDatasetVersionUrl(String maintainerCode, String code, String version, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(maintainerCode, code, version, StatisticalResourceTypeEnum.DATASET));

        return builder.toString();
    }

    public static String buildQueryVersionUrl(String maintainerCode, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForNotVersionableResource(maintainerCode, code, StatisticalResourceTypeEnum.QUERY));

        return builder.toString();
    }

    public static String buildMultidatasetVersionUrl(String maintainerCode, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForMultidataset(maintainerCode, code));

        return builder.toString();
    }

    public static String buildQueryVersionUrl(QueryVersionDto queryVersionDto, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForNotVersionableResource(queryVersionDto, StatisticalResourceTypeEnum.QUERY));

        return builder.toString();
    }

    public static String buildPublicationVersionUrl(PublicationVersionDto publicationVersionDto, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_COLLECTION_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForNotVersionableResource(publicationVersionDto, StatisticalResourceTypeEnum.COLLECTION));

        return builder.toString();
    }

    public static String buildPublicationVersionUrl(String maintainerCode, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_COLLECTION_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForNotVersionableResource(maintainerCode, code, StatisticalResourceTypeEnum.COLLECTION));

        return builder.toString();
    }

    public static String buildMultidatasetVersionUrl(MultidatasetVersionDto multidatasetVersionDto, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(PAGE_DATA_RESOURCE);
        builder.append(URL_QUERY_SEPARATOR);
        builder.append(buildQueryParametersForMultidataset(multidatasetVersionDto));

        return builder.toString();
    }

    private static String buildEndpointUrl(String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder().append(urlBase);

        if (!StringUtils.isBlank(urlBase) && !urlBase.endsWith(URL_SEPARATOR)) {
            builder.append(URL_SEPARATOR);
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

    private static String buildQueryParametersForVersionableResource(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto, StatisticalResourceTypeEnum type)
            throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();

        if (lifeCycleStatisticalResourceDto != null) {
            String maintainerCode = lifeCycleStatisticalResourceDto.getMaintainer() != null ? lifeCycleStatisticalResourceDto.getMaintainer().getCode() : null;
            String code = lifeCycleStatisticalResourceDto.getCode();
            return buildQueryParametersForVersionableResource(maintainerCode, code, lifeCycleStatisticalResourceDto.getVersionLogic(), type);
        }

        return builder.toString();
    }

    private static String buildQueryParametersForNotVersionableResource(String maintainerCode, String code, StatisticalResourceTypeEnum type) {
        StringBuilder builder = new StringBuilder();

        String resourceType = determinateResourceType(type);

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
        return builder.toString();
    }

    private static String buildQueryParametersForNotVersionableResource(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto, StatisticalResourceTypeEnum type)
            throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();

        if (lifeCycleStatisticalResourceDto != null) {

            String maintainerCode = lifeCycleStatisticalResourceDto.getMaintainer() != null ? lifeCycleStatisticalResourceDto.getMaintainer().getCode() : null;
            String code = lifeCycleStatisticalResourceDto.getCode();
            return buildQueryParametersForNotVersionableResource(maintainerCode, code, type);
        }
        return builder.toString();
    }

    private static String buildQueryParametersForMultidataset(String maintainerCode, String code) {
        StringBuilder builder = new StringBuilder();

        builder.append(URL_QUERY_PARAMETER_MULTIDATASET_ID);
        builder.append(URL_QUERY_EQUALS);
        builder.append(maintainerCode);
        builder.append(URL_QUERY_COLON);
        builder.append(code);

        return builder.toString();
    }

    private static String buildQueryParametersForMultidataset(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        StringBuilder builder = new StringBuilder();

        if (lifeCycleStatisticalResourceDto != null) {
            String maintainerCode = lifeCycleStatisticalResourceDto.getMaintainer() != null ? lifeCycleStatisticalResourceDto.getMaintainer().getCode() : null;
            String code = lifeCycleStatisticalResourceDto.getCode();

            return buildQueryParametersForMultidataset(maintainerCode, code);
        }
        return builder.toString();
    }

    private static String determinateResourceType(StatisticalResourceTypeEnum type) throws IllegalArgumentException {
        switch (type) {
            case QUERY:
                return URL_QUERY_RESOURCE_TYPE_QUERY;
            case DATASET:
                return URL_QUERY_RESOURCE_TYPE_DATASET;
            case COLLECTION:
                return URL_QUERY_RESOURCE_TYPE_COLLECTION;
            default:
                throw new IllegalArgumentException("StatisticalResourceTypeEnum " + type + " not valid.");
        }
    }
}
