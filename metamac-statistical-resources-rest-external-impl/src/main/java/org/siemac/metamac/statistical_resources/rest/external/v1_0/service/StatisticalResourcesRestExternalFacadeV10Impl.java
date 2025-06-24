package org.siemac.metamac.statistical_resources.rest.external.v1_0.service;

import static org.siemac.metamac.rest.exception.utils.RestExceptionUtils.checkParameterNotWildcardAll;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.parseDimensionExpression;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.parseFieldsStatisticalResources;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.parseFieldsStatisticalResourcesListEndpoints;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.manageException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Collection;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Multidataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources_external.v1_0.domain.DimensionFilters;
import org.siemac.metamac.rest.statistical_resources_external.v1_0.domain.Exportation;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionProperties;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.ExportResourceAccessToPlainText;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.ResourceAccess;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Collections;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Datasets;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Multidatasets;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Queries;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.service.StatisticalResourcesRestExternalCommonService;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.collection.CollectionsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.collection.CollectionsRest2DoMapper;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset.DatasetsRest2DoMapper;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.multidataset.MultidatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.multidataset.MultidatasetsRest2DoMapper;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query.QueriesDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query.QueriesRest2DoMapper;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources.ResourcesDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources.ResourcesRest2DoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("statisticalResourcesRestExternalFacadeV10")
public class StatisticalResourcesRestExternalFacadeV10Impl implements StatisticalResourcesV1_0 {

    public static final String                            OPERATOR = "=";
    @Autowired
    private StatisticalResourcesRestExternalCommonService commonService;

    @Autowired
    private DatasetsDo2RestMapperV10                      datasetsDo2RestMapper;

    @Autowired
    private DatasetsRest2DoMapper                         datasetsRest2DoMapper;

    @Autowired
    private CollectionsDo2RestMapperV10                   collectionsDo2RestMapper;

    @Autowired
    private CollectionsRest2DoMapper                      collectionsRest2DoMapper;

    @Autowired
    private QueriesDo2RestMapperV10                       queriesDo2RestMapper;

    @Autowired
    private QueriesRest2DoMapper                          queriesRest2DoMapper;

    @Autowired
    private MultidatasetsDo2RestMapperV10                 multidatasetsDo2RestMapper;

    @Autowired
    private MultidatasetsRest2DoMapper                    multidatasetsRest2DoMapper;

    @Autowired
    private StatisticalResourcesConfiguration             configurationService;

    @Autowired
    private ResourcesRest2DoMapper                        resourcesRest2DoMapper;

    @Autowired
    private ResourcesDo2RestMapperV10                     resourcesDo2RestMapper;

    @Override
    public Datasets findDatasets(String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findDatasetsCommon(null, null, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Datasets findDatasets(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_AGENCY_ID, agencyID);
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findDatasetsCommon(agencyID, null, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Datasets findDatasets(String agencyID, String resourceID, String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_RESOURCE_ID, resourceID);
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findDatasetsCommon(agencyID, resourceID, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Dataset retrieveDataset(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String granularity) {
        try {
            DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
            Map<String, List<String>> dimensions = parseDimensionExpression(dim, representation);
            Set<String> parsedFields = parseFieldsStatisticalResources(fields);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            return datasetsDo2RestMapper.toDataset(datasetVersion, dimensions, selectedLanguages, parsedFields, granularity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public JsonStatData retrieveDatasetJsonStat(Exportation exportationBody, String agencyID, String resourceID, String version, List<String> lang, String fields, String granularity) {

        try {
            DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
            String selectedLanguage = languagesRequestedToEffectiveLanguageForJsonStat(datasetVersion, lang);

            // Parse body for dimensions
            String dimensionSelection = toStatisticalResourcesApiRepresentationParameter(exportationBody);

            Map<String, List<String>> dimensions = parseDimensionExpression(dimensionSelection, exportationBody.toString());

            Set<String> parsedFields = parseFieldsStatisticalResources(fields);

            return datasetsDo2RestMapper.toJsonStatDataset(datasetVersion, dimensions, selectedLanguage, parsedFields, granularity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonStatData retrieveDatasetJsonStat(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String granularity) {
        try {
            DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
            Map<String, List<String>> dimensions = parseDimensionExpression(dim, representation);
            String selectedLanguage = languagesRequestedToEffectiveLanguageForJsonStat(datasetVersion, lang);
            Set<String> parsedFields = parseFieldsStatisticalResources(fields);
            return datasetsDo2RestMapper.toJsonStatDataset(datasetVersion, dimensions, selectedLanguage, parsedFields, granularity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Response retrieveDatasetTSV(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String granularity) {
        return retrieveDatasetPlainText(agencyID, resourceID, version, lang, fields, dim, representation, "tsv", granularity);
    }

    @Override
    public Response retrieveDatasetCSV(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String granularity) {
        return retrieveDatasetPlainText(agencyID, resourceID, version, lang, fields, dim, representation, "csv", granularity);
    }

    @Override
    public Response retrieveDatasetXLSX(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String granularity) {
        return retrieveDatasetPlainText(agencyID, resourceID, version, lang, fields, dim, representation, "xlsx", granularity);
    }

    private Response retrieveDatasetPlainText(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String format,
            String granularity) {
        try {
            return createPlainTextResourceAccess(agencyID, resourceID, version, lang, fields, dim, representation, format, granularity);
        } catch (Exception e) {
            throw manageExceptionResponse(e);
        }
    }

    private static String toStatisticalResourcesApiRepresentationParameter(Exportation exportationBody) {
        if (exportationBody == null) {
            return null;
        }
        org.siemac.metamac.rest.statistical_resources_external.v1_0.domain.Selection datasetSelection = exportationBody.getSelection();
        if (datasetSelection == null || datasetSelection.getDimensions() == null || datasetSelection.getDimensions().getDimensions() == null) {
            return null;
        }
        List<org.siemac.metamac.rest.statistical_resources_external.v1_0.domain.SelectionDimension> dimensions = datasetSelection.getDimensions().getDimensions();

        StringBuilder sb = new StringBuilder();
        for (org.siemac.metamac.rest.statistical_resources_external.v1_0.domain.SelectionDimension dimension : dimensions) {
            sb.append(dimension.getDimensionId());
            sb.append("[");

            if (dimension.getDimensionFilters() != null) {
                DimensionFilters dimensionFilters = dimension.getDimensionFilters();
                if (dimensionFilters.getAfter() != null) {
                    sb.append("~after=").append(dimensionFilters.getAfter()).append("|");
                }
                if (dimensionFilters.getLast() != null) {
                    sb.append("~last=").append(dimensionFilters.getLast()).append("|");
                }
                if (dimensionFilters.getRange() != null) {
                    sb.append("~range=").append(dimensionFilters.getRange().getStart()).append(";").append(dimensionFilters.getRange().getEnd()).append("|");
                }
            }
            if (dimension.getDimensionValues() != null && dimension.getDimensionValues().getDimensionValues() != null && dimension.getDimensionValues().getDimensionValues().size() > 0) {
                sb.append(StringUtils.join(dimension.getDimensionValues().getDimensionValues(), "|"));
            }
            if ('|' == sb.charAt(sb.length() - 1)) {
                sb.deleteCharAt(sb.length() - 1); // delete last |
            }

            sb.append("]");
            sb.append(":");
        }
        if (':' == sb.charAt(sb.length() - 1)) {
            sb.deleteCharAt(sb.length() - 1); // delete last :
        }

        return sb.toString();
    }

    private Response createPlainTextResourceAccess(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim, String representation, String format,
            String granularity) throws Exception {

        Set<String> parsedFields = parseFieldsStatisticalResources(fields);
        checkParameterData(parsedFields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
        checkParameterData(parsedFields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
        Map<String, List<String>> dimensions = parseDimensionExpression(dim, representation);

        // Specific for dataset
        DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
        List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(datasetVersion, lang);
        Dataset dataset = datasetsDo2RestMapper.toDataset(datasetVersion, dimensions, selectedLanguages, parsedFields, granularity);
        ResourceAccess resourceAccess = ExportResourceAccessToPlainText.buildResourceAccessForDataset(dataset, selectedLanguages);
        String filename = StatisticalResourcesRestConstants.LINK_SUBPATH_DATASETS + "-" + agencyID + "_" + resourceID + "_" + version;
        final String urn = dataset.getUrn();

        ExportResourceAccessToPlainText.checkMaxRowsInXlsxFormat(resourceAccess, format, configurationService.retrieveMaxXlsxRows(), urn);
        return ExportResourceAccessToPlainText.buildResponseExportResourceAccessToPlainText(resourceAccess, filename, format);
    }

    @Override
    public Collections findCollections(String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findCollectionsCommon(null, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Collections findCollections(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_AGENCY_ID, agencyID);
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findCollectionsCommon(agencyID, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Collection retrieveCollection(String agencyID, String resourceID, List<String> lang, String fields) {
        try {
            PublicationVersion publicationVersion = commonService.retrievePublicationVersion(agencyID, resourceID);
            Set<String> parsedFields = parseFieldsStatisticalResources(fields);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Collection collection = collectionsDo2RestMapper.toCollection(publicationVersion, selectedLanguages, parsedFields);
            return collection;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findQueriesCommon(null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Queries findQueries(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findQueriesCommon(agencyID, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Query retrieveQuery(String agencyID, String resourceID, List<String> lang, String fields, String dim, String representation, String granularity) {
        try {
            QueryVersion queryVersion = commonService.retrieveQueryVersion(agencyID, resourceID);
            Map<String, List<String>> dimensions = parseDimensionExpression(dim, representation);
            Set<String> parsedFields = parseFieldsStatisticalResources(fields);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Query query = queriesDo2RestMapper.toQuery(queryVersion, dimensions, selectedLanguages, parsedFields, granularity);
            return query;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public JsonStatData retrieveJsonStatQuery(String agencyID, String resourceID, List<String> lang, String fields, String dim, String representation, String granularity) {
        try {
            QueryVersion queryVersion = commonService.retrieveQueryVersion(agencyID, resourceID);
            Map<String, List<String>> dimensions = parseDimensionExpression(dim, representation);
            Set<String> parsedFields = parseFieldsStatisticalResources(fields);
            DatasetVersion datasetVersion = commonService.retrieveDatasetLastPublishedVersionByUrn(queryVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
            String selectedLanguage = languagesRequestedToEffectiveLanguageForJsonStat(datasetVersion, lang);
            return queriesDo2RestMapper.toJsonStatQuery(queryVersion, datasetVersion, dimensions, selectedLanguage, parsedFields, granularity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Multidatasets findMultidatasets(String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findMultidatasetsCommon(null, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Multidatasets findMultidatasets(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang, String fields) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_AGENCY_ID, agencyID);
        Set<String> parsedFields = parseFieldsStatisticalResourcesListEndpoints(fields);
        return findMultidatasetsCommon(agencyID, null, query, orderBy, limit, offset, lang, parsedFields);
    }

    @Override
    public Multidataset retrieveMultidataset(String agencyID, String resourceID, List<String> lang, String fields) {
        try {
            MultidatasetVersion multidatasetVersion = commonService.retrieveMultidatasetVersion(agencyID, resourceID);
            Set<String> parsedFields = parseFieldsStatisticalResources(fields);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Multidataset multidataset = multidatasetsDo2RestMapper.toMultidataset(multidatasetVersion, selectedLanguages, parsedFields);
            return multidataset;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Resources findResources(String query, String orderBy, String limit, String offset, List<String> lang) {
        return findResourcesCommon(query, orderBy, limit, offset, lang);
    }

    private Resources findResourcesCommon(String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            SculptorCriteria sculptorCriteria = resourcesRest2DoMapper.getResourcesCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset, true);

            // Find
            PagedResult<GeoCovVarElementCacheDatasetVersion> entitiesPagedResult = commonService.findResources(sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            return resourcesDo2RestMapper.toResources(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Datasets findDatasetsCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, List<String> lang, Set<String> parsedFields) {
        try {
            SculptorCriteria sculptorCriteria = datasetsRest2DoMapper.getDatasetCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Add condition for specific or default locale
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            sculptorCriteria.setOrderByCaseCondition(DatasetVersion.class, DatasetVersionProperties.siemacMetadataStatisticalResource().title().texts().locale(), selectedLanguages, OPERATOR, orderBy);

            // Find
            PagedResult<DatasetVersion> entitiesPagedResult = commonService.findDatasetVersions(agencyID, resourceID, version, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return datasetsDo2RestMapper.toDatasets(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages, parsedFields);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Collections findCollectionsCommon(String agencyID, String resourceID, String query, String orderBy, String limit, String offset, List<String> lang, Set<String> parsedFields) {
        try {
            SculptorCriteria sculptorCriteria = collectionsRest2DoMapper.getCollectionCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Add condition for specific or default locale
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            sculptorCriteria.setOrderByCaseCondition(PublicationVersion.class, PublicationVersionProperties.siemacMetadataStatisticalResource().title().texts().locale(), selectedLanguages, OPERATOR,
                    orderBy);

            // Find
            PagedResult<PublicationVersion> entitiesPagedResult = commonService.findPublicationVersions(agencyID, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return collectionsDo2RestMapper.toCollections(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages, parsedFields);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Queries findQueriesCommon(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang, Set<String> parsedFields) {
        try {
            SculptorCriteria sculptorCriteria = queriesRest2DoMapper.getQueryCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Add condition for specific or default locale
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            sculptorCriteria.setOrderByCaseCondition(QueryVersion.class, QueryVersionProperties.lifeCycleStatisticalResource().title().texts().locale(), selectedLanguages, OPERATOR, orderBy);

            // Find
            PagedResult<QueryVersion> entitiesPagedResult = commonService.findQueryVersions(agencyID, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Queries queries = queriesDo2RestMapper.toQueries(entitiesPagedResult, agencyID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages, parsedFields);
            return queries;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Multidatasets findMultidatasetsCommon(String agencyID, String resourceID, String query, String orderBy, String limit, String offset, List<String> lang, Set<String> parsedFields) {
        try {
            SculptorCriteria sculptorCriteria = multidatasetsRest2DoMapper.getMultidatasetCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<MultidatasetVersion> entitiesPagedResult = commonService.findMultidatasetVersions(agencyID, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Multidatasets multidatasets = multidatasetsDo2RestMapper.toMultidatasets(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages,
                    parsedFields);
            return multidatasets;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    // if sources is not empty, the user has introduced the languages. Otherwise, the common-metadata languages will be returned. The default language in common-metadata will be returned always.
    private List<String> languagesRequestedToEffectiveLanguages(List<String> sources) throws MetamacException {

        List<String> targets = null;
        if (CollectionUtils.isEmpty(sources)) {
            // all languages in DATA
            targets = configurationService.retrieveLanguages();
        } else {
            List<String> split = splitIfCommaSeparated(sources);

            targets = new ArrayList<String>();
            if (!CollectionUtils.isEmpty(split)) {
                targets.addAll(split);
            }
            String languageDefault = configurationService.retrieveLanguageDefault();
            if (!targets.contains(languageDefault)) {
                targets.add(languageDefault);
            }
        }
        return targets;
    }

    // if sources is not empty, the user has introduced the languages. Otherwise, the dataset languages will be returned. The default language in common-metadata will be returned always.
    private List<String> languagesRequestedToEffectiveLanguages(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        List<String> targets = null;
        if (CollectionUtils.isEmpty(selectedLanguages)) {
            targets = new ArrayList<String>();
            for (ExternalItem lang : source.getSiemacMetadataStatisticalResource().getLanguages()) {
                targets.add(lang.getCode().toLowerCase());
            }
            String languageDefault = configurationService.retrieveLanguageDefault();
            if (!targets.contains(languageDefault)) {
                targets.add(languageDefault);
            }
        } else {
            return languagesRequestedToEffectiveLanguages(selectedLanguages);
        }

        return targets;
    }

    private String languagesRequestedToEffectiveLanguageForJsonStat(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        String defaultLang = configurationService.retrieveLanguageDefault().toLowerCase();
        if (!CollectionUtils.isEmpty(selectedLanguages)) {
            String firstSelectedLang = selectedLanguages.get(0).toLowerCase();
            for (ExternalItem lang : source.getSiemacMetadataStatisticalResource().getLanguages()) {
                String langCode = lang.getCode().toLowerCase();
                if (Objects.equals(firstSelectedLang, langCode)) {
                    return firstSelectedLang;
                }
            }
        }
        return defaultLang;
    }

    private List<String> splitIfCommaSeparated(List<String> sources) {
        List<String> result = new ArrayList<String>();
        for (String source : sources) {
            String[] split = StringUtils.split(source, ",");
            result.addAll(Arrays.asList(split));
        }
        return result;
    }
    /**
     * Throws response error, logging exception When the success response is tsv or csv, a response error must be xml because a response error in tsv or csv is not desirable.
     */
    private RestException manageExceptionResponse(Exception e) {
        RestException ex = manageException(e);

        return new RestException(ex.getException(), ex.getStatus(), "application/xml");
    }

    private void checkParameterData(Set<String> parsedFields, String field) {

        if (parsedFields.contains(field)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.PARAMETER_UNEXPECTED, field);
            throw new RestException(exception, Status.BAD_REQUEST);
        }
    }

    @Override
    public Response retrieveDatasetHead(String agencyID, String resourceID, String version) {
        if (!commonService.checkDatasetVersion(agencyID, resourceID, version)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
