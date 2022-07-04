package org.siemac.metamac.statistical_resources.rest.external.v1_0.service;

import static org.siemac.metamac.rest.exception.utils.RestExceptionUtils.checkParameterNotWildcardAll;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.hasField;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.parseDimensionExpression;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestApiCommonUtils.parseFieldsParameter;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.manageException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Collection;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Collections;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Datasets;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Multidataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Multidatasets;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("statisticalResourcesRestExternalFacadeV10")
public class StatisticalResourcesRestExternalFacadeV10Impl implements StatisticalResourcesV1_0 {

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

    @Override
    public Datasets findDatasets(String query, String orderBy, String limit, String offset, List<String> lang) {
        return findDatasetsCommon(null, null, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Datasets findDatasets(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_AGENCY_ID, agencyID);
        return findDatasetsCommon(agencyID, null, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Datasets findDatasets(String agencyID, String resourceID, String query, String orderBy, String limit, String offset, List<String> lang) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_RESOURCE_ID, resourceID);
        return findDatasetsCommon(agencyID, resourceID, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Dataset retrieveDataset(String agencyID, String resourceID, String version, List<String> lang, String fields, String dim) {
        try {
            DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
            Map<String, List<String>> dimensions = parseDimensionExpression(dim);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Set<String> parsedFields = parseFieldsParameter(fields);
            return datasetsDo2RestMapper.toDataset(datasetVersion, dimensions, selectedLanguages, parsedFields);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Collections findCollections(String query, String orderBy, String limit, String offset, List<String> lang) {
        return findCollectionsCommon(null, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Collections findCollections(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_AGENCY_ID, agencyID);
        return findCollectionsCommon(agencyID, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Collection retrieveCollection(String agencyID, String resourceID, List<String> lang, String fields) {
        try {
            PublicationVersion publicationVersion = commonService.retrievePublicationVersion(agencyID, resourceID);

            boolean includeMetadata = !hasField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
            boolean includeData = !hasField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Collection collection = collectionsDo2RestMapper.toCollection(publicationVersion, selectedLanguages, includeMetadata, includeData);
            return collection;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Queries findQueries(String query, String orderBy, String limit, String offset, List<String> lang) {
        return findQueriesCommon(null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Queries findQueries(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang) {
        return findQueriesCommon(agencyID, query, orderBy, limit, offset, lang);
    }

    @Override
    public Query retrieveQuery(String agencyID, String resourceID, List<String> lang, String fields, String dim) {
        try {
            QueryVersion queryVersion = commonService.retrieveQueryVersion(agencyID, resourceID);
            Map<String, List<String>> dimensions = parseDimensionExpression(dim);

            boolean includeMetadata = !hasField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
            boolean includeData = !hasField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Query query = queriesDo2RestMapper.toQuery(queryVersion, dimensions, selectedLanguages, includeMetadata, includeData);
            return query;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Multidatasets findMultidatasets(String query, String orderBy, String limit, String offset, List<String> lang) {
        return findMultidatasetsCommon(null, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Multidatasets findMultidatasets(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang) {
        checkParameterNotWildcardAll(StatisticalResourcesRestExternalConstants.PARAMETER_AGENCY_ID, agencyID);
        return findMultidatasetsCommon(agencyID, null, query, orderBy, limit, offset, lang);
    }

    @Override
    public Multidataset retrieveMultidataset(String agencyID, String resourceID, List<String> lang, String fields) {
        try {
            MultidatasetVersion multidatasetVersion = commonService.retrieveMultidatasetVersion(agencyID, resourceID);

            boolean includeMetadata = !hasField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
            boolean includeData = !hasField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Multidataset multidataset = multidatasetsDo2RestMapper.toMultidataset(multidatasetVersion, selectedLanguages, includeMetadata, includeData);
            return multidataset;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Datasets findDatasetsCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            SculptorCriteria sculptorCriteria = datasetsRest2DoMapper.getDatasetCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<DatasetVersion> entitiesPagedResult = commonService.findDatasetVersions(agencyID, resourceID, version, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Datasets datasets = datasetsDo2RestMapper.toDatasets(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages);
            return datasets;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Collections findCollectionsCommon(String agencyID, String resourceID, String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            SculptorCriteria sculptorCriteria = collectionsRest2DoMapper.getCollectionCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<PublicationVersion> entitiesPagedResult = commonService.findPublicationVersions(agencyID, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Collections collections = collectionsDo2RestMapper.toCollections(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages);
            return collections;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Queries findQueriesCommon(String agencyID, String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            SculptorCriteria sculptorCriteria = queriesRest2DoMapper.getQueryCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<QueryVersion> entitiesPagedResult = commonService.findQueryVersions(agencyID, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Queries queries = queriesDo2RestMapper.toQueries(entitiesPagedResult, agencyID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages);
            return queries;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Multidatasets findMultidatasetsCommon(String agencyID, String resourceID, String query, String orderBy, String limit, String offset, List<String> lang) {
        try {
            SculptorCriteria sculptorCriteria = multidatasetsRest2DoMapper.getMultidatasetCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<MultidatasetVersion> entitiesPagedResult = commonService.findMultidatasetVersions(agencyID, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            List<String> selectedLanguages = languagesRequestedToEffectiveLanguages(lang);
            Multidatasets multidatasets = multidatasetsDo2RestMapper.toMultidatasets(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit(), selectedLanguages);
            return multidatasets;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

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

    private List<String> splitIfCommaSeparated(List<String> sources) {
        List<String> result = new ArrayList<String>();
        for (String source : sources) {
            String[] split = StringUtils.split(source, ",");
            result.addAll(Arrays.asList(split));
        }
        return result;
    }
}
