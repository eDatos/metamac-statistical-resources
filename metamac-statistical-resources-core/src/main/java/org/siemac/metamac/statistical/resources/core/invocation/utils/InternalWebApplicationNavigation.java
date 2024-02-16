package org.siemac.metamac.statistical.resources.core.invocation.utils;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.navigation.shared.NameTokens;
import org.siemac.metamac.statistical.resources.navigation.shared.PlaceRequestParams;
import org.springframework.web.util.UriTemplate;

public class InternalWebApplicationNavigation {

    private final String      SEPARATOR                  = "/";
    private final String      PATH_STATISTICAL_RESOURCES = "#" + NameTokens.operationsListPage;
    private final String      OPERATION_ID_PARAMETER     = "operationParam";
    private final String      PUBLICATION_ID_PARAMETER   = "publicationParam";
    private final String      DATASET_ID_PARAMETER       = "datasetParam";
    private final String      QUERY_ID_PARAMETER         = "queryParam";
    private final String      MULTIDATASET_ID_PARAMETER  = "multidatasetParam";

    private final UriTemplate publicationVersionTemplate;
    private final UriTemplate datasetVersionTemplate;
    private final UriTemplate queryTemplate;
    private final UriTemplate multidatasetVersionTemplate;

    public InternalWebApplicationNavigation(String webApplicationPath) {
        // Publication
        // @formatter:off
        publicationVersionTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STATISTICAL_RESOURCES
                + SEPARATOR + NameTokens.operationPage + ";" + PlaceRequestParams.operationParam + "=" + "{" + OPERATION_ID_PARAMETER + "}"
                + SEPARATOR + NameTokens.publicationsListPage + SEPARATOR + NameTokens.publicationPage + ";" + PlaceRequestParams.publicationParam + "=" + "{" + PUBLICATION_ID_PARAMETER + "}"
                );

        datasetVersionTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STATISTICAL_RESOURCES
                + SEPARATOR + NameTokens.operationPage + ";" + PlaceRequestParams.operationParam + "=" + "{" + OPERATION_ID_PARAMETER + "}"
                + SEPARATOR + NameTokens.datasetsListPage + SEPARATOR + NameTokens.datasetPage + ";" + PlaceRequestParams.datasetParam + "=" + "{" + DATASET_ID_PARAMETER + "}"
                );

        queryTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STATISTICAL_RESOURCES
                + SEPARATOR + NameTokens.operationPage + ";" + PlaceRequestParams.operationParam + "=" + "{" + OPERATION_ID_PARAMETER + "}"
                + SEPARATOR + NameTokens.queriesListPage + SEPARATOR + NameTokens.queryPage + ";" + PlaceRequestParams.queryParam + "=" + "{" + QUERY_ID_PARAMETER + "}"
                );

        multidatasetVersionTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STATISTICAL_RESOURCES
                + SEPARATOR + NameTokens.operationPage + ";" + PlaceRequestParams.operationParam + "=" + "{" + OPERATION_ID_PARAMETER + "}"
                + SEPARATOR + NameTokens.multidatasetsListPage + SEPARATOR + NameTokens.multidatasetPage + ";" + PlaceRequestParams.multidatasetParam + "=" + "{" + MULTIDATASET_ID_PARAMETER + "}"
                );
        // @formatter:on
    }

    public String buildResourceUrl(IdentifiableStatisticalResourceDto source, StatisticalResourceTypeEnum type) {
        switch (type) {
            case DATASET:
                return buildDatasetVersionUrl(source.getStatisticalOperation().getCode(), source.getUrn());
            case COLLECTION:
                return buildPublicationVersionUrl(source.getStatisticalOperation().getCode(), source.getUrn());
            case QUERY:
                return buildQueryVersionUrl(source.getStatisticalOperation().getCode(), source.getUrn());
            case MULTIDATASET:
                return buildMultidatasetVersionUrl(source.getStatisticalOperation().getCode(), source.getUrn());
            default:
                throw new RuntimeException("Invalid value for statistical resource type " + type);
        }
    }

    //
    // PUBLICATIONS
    //

    public String buildPublicationVersionUrl(PublicationVersion source) {
        return buildPublicationVersionUrl(source.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), source.getLifeCycleStatisticalResource().getUrn());
    }

    public String buildRelatedResourcePublicationVersionUrl(RelatedResourceResult source) {
        return buildPublicationVersionUrl(source.getStatisticalOperationCode(), source.getUrn());
    }

    public String buildPublicationVersionUrl(String statisticalOperationCode, String publicationVersionUrn) {
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(OPERATION_ID_PARAMETER, statisticalOperationCode);
        parameters.put(PUBLICATION_ID_PARAMETER, UrnUtils.removePrefix(publicationVersionUrn));
        return publicationVersionTemplate.expand(parameters).toString();
    }

    //
    // DATASETS
    //

    public String buildDatasetVersionUrl(DatasetVersion source) {
        return buildDatasetVersionUrl(source.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), source.getLifeCycleStatisticalResource().getUrn());
    }

    public String buildDatasetVersionUrl(RelatedResourceResult source) {
        return buildDatasetVersionUrl(source.getStatisticalOperationCode(), source.getUrn());
    }

    public String buildDatasetVersionUrl(String statisticalOperationCode, String datasetVersionUrn) {
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(OPERATION_ID_PARAMETER, statisticalOperationCode);
        parameters.put(DATASET_ID_PARAMETER, UrnUtils.removePrefix(datasetVersionUrn));
        return datasetVersionTemplate.expand(parameters).toString();
    }

    //
    // QUERY
    //

    public String buildQueryVersionUrl(QueryVersion source) {
        return buildQueryVersionUrl(source.getLifeCycleStatisticalResource().getStatisticalOperation().getCode(), source.getLifeCycleStatisticalResource().getUrn());
    }

    public String buildQueryVersionUrl(RelatedResourceResult source) {
        return buildQueryVersionUrl(source.getStatisticalOperationCode(), source.getUrn());
    }

    public String buildQueryVersionUrl(String statisticalOperationCode, String queryVersionUrn) {
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(OPERATION_ID_PARAMETER, statisticalOperationCode);
        parameters.put(QUERY_ID_PARAMETER, UrnUtils.removePrefix(queryVersionUrn));
        return queryTemplate.expand(parameters).toString();
    }

    // MULTIDATASET

    public String buildMultidatasetVersionUrl(MultidatasetVersion source) {
        return buildMultidatasetVersionUrl(source.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), source.getLifeCycleStatisticalResource().getUrn());
    }

    public String buildRelatedResourceMultidatasetVersionUrl(RelatedResourceResult source) {
        return buildMultidatasetVersionUrl(source.getStatisticalOperationCode(), source.getUrn());
    }

    public String buildMultidatasetVersionUrl(String statisticalOperationCode, String multidatasetVersionUrn) {
        Map<String, String> parameters = new HashMap<String, String>(2);
        parameters.put(OPERATION_ID_PARAMETER, statisticalOperationCode);
        parameters.put(MULTIDATASET_ID_PARAMETER, UrnUtils.removePrefix(multidatasetVersionUrn));
        return multidatasetVersionTemplate.expand(parameters).toString();
    }
}