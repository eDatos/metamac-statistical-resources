package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceQueryUrn;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.containsField;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.isDateAfterNowSetNull;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.sortTimeListFromRecentToOldest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.QueryMetadata;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.DimensionsFilter;
import org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources.ExternalRestObjectsMapper;
import org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Queries;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.HtmlLinkUtil;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat.CommonDo2JsonStatRestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueriesDo2RestMapperV10Impl implements QueriesDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10            commonDo2RestMapper;

    @Autowired
    private DatasetsDo2RestMapperV10          datasetsDo2RestMapper;

    @Autowired
    private CommonDo2JsonStatRestMapperV10    commonDo2JsonStatRestMapper;

    @Autowired
    private QueryVersionRepository            queryVersionRepository;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    private static final Logger               logger = LoggerFactory.getLogger(QueriesDo2RestMapperV10Impl.class);

    @Override
    public Queries toQueries(PagedResult<QueryVersion> sources, String agencyID, String query, String orderBy, Integer limit, List<String> selectedLanguages, Set<String> parsedFields)
            throws MetamacException {

        Queries targets = new Queries();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_QUERIES);

        // Pagination
        String baseLink = toQueriesLink(agencyID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        // Values
        for (QueryVersion source : sources.getValues()) {
            ResourceWithStatisticalOperation target = toResource(source, selectedLanguages, parsedFields, externalRestObjectsMapper);
            targets.getQueries().add(target);
        }
        return targets;
    }

    @Override
    public Query toQuery(QueryVersion source, DatasetVersion relatedDataset, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> fields, String granularity)
            throws Exception {
        if (source == null) {
            return null;
        }
        Query target = new Query();
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_QUERY);
        target.setId(source.getLifeCycleStatisticalResource().getCode());
        target.setUrn(toQueryUrn(source));
        target.setSelfLink(toQuerySelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toQueryParentLink(source));
        target.setChildLinks(toQueryChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.QUERY, source.getLifeCycleStatisticalResource(), configurationService, false));
        DsdProcessorResult dsdProcessorResult = null;
        boolean includeMetadata = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
        boolean includeData = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
        boolean includeKeywords = containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_KEYWORDS);

        Dimensions dimensions = null;
        DimensionsFilter dimensionsFilter = new DimensionsFilter();
        Map<String, List<String>> granularities = commonDo2RestMapper.parseParamExpression(granularity);

        if (includeMetadata || includeData || includeKeywords) {
            dsdProcessorResult = commonDo2RestMapper.processDataStructure(relatedDataset.getRelatedDsd().getUrn());
            dimensionsFilter = commonDo2RestMapper.getDimensionsFilter(granularities, dsdProcessorResult);
            dimensionsFilter.setTemporalDimensionValuesQueriesIds(getTemporalGranularities(source.getTemporalGranularities()));
        }
        if (includeMetadata || (includeData && (dimensionsFilter.getTemporalDimensionValuesIds() != null && !dimensionsFilter.getTemporalDimensionValuesIds().isEmpty())
                || (dimensionsFilter.getGeographicDimensionValuesIds() != null && !(dimensionsFilter.getGeographicDimensionValuesIds().isEmpty())
                        || (source.getTemporalGranularities() != null && !source.getTemporalGranularities().isEmpty())))) {
            Map<String, List<String>> effectiveDimensionValuesToDataByDimension = calculateEffectiveDimensionValuesToQuery(source, relatedDataset);

            dimensions = commonDo2RestMapper.toDimensions(relatedDataset.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, effectiveDimensionValuesToDataByDimension,
                    selectedLanguages, null, dimensionsFilter);
        }
        if (includeMetadata) {
            target.setMetadata(toQueryMetadata(source, relatedDataset, dsdProcessorResult, selectedLanguages, dimensions));
        }
        if (includeData) {
            target.setData(toQueryData(source, relatedDataset, dsdProcessorResult, selectedDimensions, selectedLanguages, dimensions));
        }
        if (includeKeywords) {
            target.setKeywords(commonDo2RestMapper.toInternationalString(relatedDataset.getSiemacMetadataStatisticalResource().getKeywords(), selectedLanguages));
        }
        return target;
    }

    @Override
    public JsonStatData toJsonStatQuery(QueryVersion source, DatasetVersion datasetVersion, Map<String, List<String>> selectedDimensions, String selectedLanguage, Set<String> parsedFields,
            String granularity) throws Exception {
        if (source == null) {
            return null;
        }

        List<String> selectedLanguages = Collections.singletonList(selectedLanguage);

        DsdProcessorResult dsdProcessorResult = commonDo2RestMapper.processDataStructure(datasetVersion.getRelatedDsd().getUrn());
        Map<String, List<String>> granularities = commonDo2RestMapper.parseParamExpression(granularity);
        DimensionsFilter dimensionsFilter = commonDo2RestMapper.getDimensionsFilter(granularities, dsdProcessorResult);

        dimensionsFilter.setTemporalDimensionValuesQueriesIds(getTemporalGranularities(source.getTemporalGranularities()));
        Dimensions dimensions = commonDo2RestMapper.toDimensions(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult,
                calculateEffectiveDimensionValuesToQuery(source, datasetVersion), selectedLanguages, null, dimensionsFilter);
        Data data = toQueryData(source, datasetVersion, dsdProcessorResult, selectedDimensions, selectedLanguages, dimensions);
        Attributes attributes = commonDo2RestMapper.toAttributes(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages);

        // ********************************************
        // ***** See https://json-stat.org/full/ ******
        // ********************************************

        JsonStatData target = new JsonStatData();

        target.setVersion(commonDo2JsonStatRestMapper.JSON_STAT_VERSION);
        target.setClazz(commonDo2JsonStatRestMapper.JSON_STAT_CLASS);
        target.addAllValues(commonDo2JsonStatRestMapper.toJsonStatDatasetValues(data));
        target.setDimension(commonDo2JsonStatRestMapper.toJsonStatDatasetDimensions(dimensions, data.getDimensions(), dsdProcessorResult, attributes, data.getAttributes(), selectedLanguage));
        target.setRole(commonDo2JsonStatRestMapper.toJsonStatRoles(dsdProcessorResult));
        target.setId(commonDo2JsonStatRestMapper.getJsonStatId(data, dsdProcessorResult));
        target.setSize(commonDo2JsonStatRestMapper.toJsonStatSize(data, dsdProcessorResult, attributes));
        target.setLabel(commonDo2JsonStatRestMapper.toI18nValue(datasetVersion.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguage));
        target.setUpdated(datasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().toString());
        target.setExtension(commonDo2JsonStatRestMapper.toJsonStatExtension(datasetVersion, dimensions, selectedLanguage));
        target.setNote(commonDo2JsonStatRestMapper.toJsonStatNote(datasetVersion, data, dimensions, attributes, dsdProcessorResult, selectedLanguage));

        return target;
    }

    private List<String> getTemporalGranularities(List<ExternalItem> temporalGranularities) {
        if (temporalGranularities == null || temporalGranularities.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> temporalGranularitiesCodes = new ArrayList<>();
        for (ExternalItem temporalGranularity : temporalGranularities) {
            temporalGranularitiesCodes.add(temporalGranularity.getCode());
        }
        return temporalGranularitiesCodes;
    }

    @Override
    public ResourceWithStatisticalOperation toResource(QueryVersion source, List<String> selectedLanguages, Set<String> parsedFields, ExternalRestObjectsMapper externalRestObjectsMapper)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getLifeCycleStatisticalResource().getCode());
        target.setUrn(toQueryUrn(source));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_QUERY);
        target.setSelfLink(toQuerySelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getTitle(), selectedLanguages));
        boolean includeStatisticalOperation = containsField(parsedFields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_STATISTICAL_OPERATION);

        if (includeStatisticalOperation) {
            if (source.getStatus() == QueryStatusEnum.ACTIVE) {
                target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getDataset().getIdentifiableStatisticalResource().getStatisticalOperation(),
                        selectedLanguages, externalRestObjectsMapper));
            } else {
                target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(
                        source.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation(), selectedLanguages, externalRestObjectsMapper));
            }
        }
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.QUERY, source.getLifeCycleStatisticalResource(), configurationService, false));

        return target;
    }

    @Override
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.QUERY_VERSION.equals(source.getType())) {
            throw commonDo2RestMapper.buildRestException("RelatedResource unsupported: " + source.getType());
        }

        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setId(source.getCode());
        target.setUrn(toQueryUrn(source.getMaintainerNestedCode(), source.getCode()));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_QUERY);
        target.setSelfLink(toQuerySelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setVisualizerHtmlLink(toVisualizerHtmlLink(source));
        return target;
    }

    private String toVisualizerHtmlLink(RelatedResourceResult source) throws MetamacException {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        return HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.QUERY, agencyID, resourceID, null, configurationService, false);
    }

    private QueryMetadata toQueryMetadata(QueryVersion source, DatasetVersion datasetVersion, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages, Dimensions dimensions)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        QueryMetadata target = new QueryMetadata();
        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        target.setRelatedDsd(commonDo2RestMapper.toDataStructureDefinition(datasetVersion.getRelatedDsd(), dsdProcessorResult.getDataStructure(), selectedLanguages, source.getHeadingDimensions(),
                source.getStubDimensions()));
        target.setDimensions(dimensions);
        target.setAttributes(commonDo2RestMapper.toAttributes(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages));

        ResourceStatisticalResourceBase relatedDataset = null;
        if (source.getDataset() != null) {
            relatedDataset = datasetsDo2RestMapper.toResourceAsLatest(datasetVersion, selectedLanguages);
        } else {
            relatedDataset = datasetsDo2RestMapper.toResource(datasetVersion, selectedLanguages, null, externalRestObjectsMapper);
        }
        target.setRelatedDataset(relatedDataset);
        target.setStatus(toQueryStatus(source.getStatus()));
        target.setType(toQueryType(source.getType()));
        target.setLatestDataNumber(source.getLatestDataNumber());
        target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getLifeCycleStatisticalResource().getStatisticalOperation(), selectedLanguages,
                new ExternalRestObjectsMapper()));
        target.setMaintainer(commonDo2RestMapper.toResourceExternalItemSrm(source.getLifeCycleStatisticalResource().getMaintainer(), selectedLanguages));
        target.setValidFrom(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getValidFrom()));
        target.setValidTo(commonDo2RestMapper.toDate(isDateAfterNowSetNull(source.getLifeCycleStatisticalResource().getValidTo())));
        target.setRequires(datasetsDo2RestMapper.toResource(datasetVersion, selectedLanguages, null, externalRestObjectsMapper));
        target.setIsPartOf(toQueryIsPartOf(source, selectedLanguages));

        Dimension temporalDimension = getTemporalDimension(target.getDimensions());

        target.setDateStart(toDateStart(temporalDimension, selectedLanguages));
        target.setDateEnd(toDateEnd(temporalDimension, selectedLanguages));

        return target;
    }

    private ResourcesStatisticalResourceBase toQueryIsPartOf(QueryVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsPartOf = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceIsPartOf = queryVersionRepository.retrieveIsPartOf(source);
        } else {
            relatedResourceIsPartOf = queryVersionRepository.retrieveIsPartOfOnlyLastPublished(source);
        }

        if (CollectionUtils.isEmpty(relatedResourceIsPartOf)) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (RelatedResourceResult relatedResourceResult : relatedResourceIsPartOf) {
            targets.getResources().add(commonDo2RestMapper.toResource(relatedResourceResult, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    private InternationalString toDateStart(Dimension temporalDimension, List<String> selectedLanguages) throws MetamacException {
        if (temporalDimension != null) {
            List<NonEnumeratedDimensionValue> temporalDimensionValues = ((NonEnumeratedDimensionValues) temporalDimension.getDimensionValues()).getValues();
            if (temporalDimensionValues != null && temporalDimensionValues.size() > 0) {
                return commonDo2RestMapper.toSdmxObservationalTimePeriod(temporalDimensionValues.get(temporalDimensionValues.size() - 1).getId(), selectedLanguages);
            }
        }
        return null;
    }

    private InternationalString toDateEnd(Dimension temporalDimension, List<String> selectedLanguages) throws MetamacException {
        if (temporalDimension != null) {
            List<NonEnumeratedDimensionValue> temporalDimensionValues = ((NonEnumeratedDimensionValues) temporalDimension.getDimensionValues()).getValues();
            if (temporalDimensionValues != null && temporalDimensionValues.size() > 0) {
                return commonDo2RestMapper.toSdmxObservationalTimePeriod(temporalDimensionValues.get(0).getId(), selectedLanguages);
            }
        }
        return null;
    }

    private Dimension getTemporalDimension(Dimensions dimensions) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (DimensionType.TIME_DIMENSION.equals(dimension.getType())) {
                return dimension;
            }
        }
        return null;
    }

    public Data toQueryData(QueryVersion source, DatasetVersion datasetVersion, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages,
            Dimensions dimensions) throws Exception {
        if (source == null) {
            return null;
        }
        DimensionsFilter dimensionsFilter = commonDo2RestMapper.getDimensionFilter(dimensions);
        Map<String, List<String>> effectiveQueryDimensionValuesToDataByDimension = calculateEffectiveDimensionValuesToQuery(source, datasetVersion);
        Map<String, List<String>> effectiveSelectionValues = calculateEffectiveSelectionValues(selectedDimensions, effectiveQueryDimensionValuesToDataByDimension);
        Map<String, List<String>> effectiveDimensionValuesToDataByDimension = StatisticalResourcesRestImplCommonUtils.filterDimensions(effectiveQueryDimensionValuesToDataByDimension,
                effectiveSelectionValues);
        return commonDo2RestMapper.toData(datasetVersion, dsdProcessorResult, effectiveDimensionValuesToDataByDimension, selectedLanguages, dimensionsFilter);
    }

    private ResourceLink toQueryParentLink(QueryVersion source) {
        return toQueriesSelfLink(null, null);
    }

    private ChildLinks toQueryChildLinks(QueryVersion source) {
        // nothing
        return null;
    }

    private ResourceLink toQueriesSelfLink(String agencyID, String resourceID) {
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_QUERIES, toQueriesLink(agencyID, resourceID));
    }

    private String toQueriesLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_QUERIES;
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, null);
    }

    private ResourceLink toQuerySelfLink(QueryVersion source) {
        String agencyID = source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        String resourceID = source.getLifeCycleStatisticalResource().getCode();
        return toQuerySelfLink(agencyID, resourceID);
    }

    private ResourceLink toQuerySelfLink(RelatedResourceResult source) {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        return toQuerySelfLink(agencyID, resourceID);
    }

    private ResourceLink toQuerySelfLink(String agencyID, String resourceID) {
        String link = toQueryLink(agencyID, resourceID);
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_QUERY, link);
    }

    private String toQueryLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_QUERIES;
        String version = null; // do not return version
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    /**
     * Retrieve urn to API, without version
     */
    private String toQueryUrn(QueryVersion source) {
        return toQueryUrn(source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested(), source.getLifeCycleStatisticalResource().getCode());
    }
    private String toQueryUrn(String maintainerNestedCode, String code) {
        return generateSiemacStatisticalResourceQueryUrn(new String[]{maintainerNestedCode}, code); // global urn without version
    }

    private org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryStatus toQueryStatus(QueryStatusEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case ACTIVE:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryStatus.ACTIVE;
            case DISCONTINUED:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryStatus.DISCONTINUED;
            default:
                throw commonDo2RestMapper.buildRestException("QueryStatusEnum unsupported: " + source);

        }
    }

    private org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryType toQueryType(QueryTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case FIXED:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryType.FIXED;
            case AUTOINCREMENTAL:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryType.AUTOINCREMENTAL;
            case LATEST_DATA:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryType.LATEST_DATA;
            default:
                throw commonDo2RestMapper.buildRestException("QueryTypeEnum unsupported: " + source);
        }
    }

    // calculateEffectiveSelectionValues, calculateEffectiveDimensionValuesToQuery and calculateEffectiveDimensionValuesToDataset are similar, except that
    // - calculateEffectiveSelectionValues, applies the api selection with their special parameters to the previously queried results
    // - calculateEffectiveDimensionValuesToQuery, applies the query selection with their special parameters to the "whole" dataset (technicaly, only to the temporal coverage)
    // - calculateEffectiveDimensionValuesToDataset, applies the api selection with their special parameters to the "whole" dataset (technicaly, only to the temporal coverage)
    public Map<String, List<String>> calculateEffectiveSelectionValues(Map<String, List<String>> selectedDimensions, Map<String, List<String>> effectiveQueryDimensionValuesToDataByDimension) {
        Map<String, List<String>> effectiveDimensions = new HashMap<String, List<String>>(selectedDimensions.size());
        for (Entry<String, List<String>> selectedDimension : selectedDimensions.entrySet()) {
            String dimensionId = selectedDimension.getKey();
            List<String> selectedValues = selectedDimension.getValue();
            if (StatisticalResourcesRestImplCommonUtils.isTemporalDimension(dimensionId)) {
                List<String> temporalCoverageValues = effectiveQueryDimensionValuesToDataByDimension.get(StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID);
                List<String> effectiveValues = StatisticalResourcesRestImplCommonUtils.calculateEffectiveTemporalSelectionValues(temporalCoverageValues, selectedValues);
                effectiveDimensions.put(dimensionId, effectiveValues);
            } else {
                effectiveDimensions.put(dimensionId, selectedValues);
            }
        }
        return effectiveDimensions;
    }

    public Map<String, List<String>> calculateEffectiveDimensionValuesToQuery(QueryVersion source, DatasetVersion datasetVersion) {
        Map<String, List<String>> dimensionValuesSelected = new HashMap<String, List<String>>(source.getSelection().size());
        for (QuerySelectionItem selection : source.getSelection()) {
            String dimensionId = selection.getDimension();
            List<String> selectionCodes = commonDo2RestMapper.codeItemToString(selection.getCodes());
            if (StatisticalResourcesRestImplCommonUtils.isTemporalDimension(dimensionId)) {
                List<String> temporalCoverageCodes = commonDo2RestMapper.temporalCoverageToString(datasetVersion.getTemporalCoverage());
                List<String> dimensionValues = calculateEffectiveTemporalDimensionValuesToQuery(source, temporalCoverageCodes, selectionCodes);
                dimensionValuesSelected.put(dimensionId, dimensionValues);
            } else {
                dimensionValuesSelected.put(dimensionId, selectionCodes);
            }
        }
        return dimensionValuesSelected;
    }

    private List<String> calculateEffectiveTemporalDimensionValuesToQuery(QueryVersion source, List<String> temporalCoverageCodes, List<String> selectionCodes) {
        List<String> sortedTemporalCoverageCodes = sortTimeListFromRecentToOldest(temporalCoverageCodes);
        QueryTypeEnum type = source.getType();
        if (QueryTypeEnum.FIXED.equals(type)) {
            // We return exactly the selected codes, but first, we sort them so all three methods (FIXED, AUTOINCREMENTAL and LATEST_DATA) return the same order, equal to the coverage
            return sortTimeListFromRecentToOldest(selectionCodes);
        } else if (QueryTypeEnum.AUTOINCREMENTAL.equals(type)) {
            List<String> effectiveDimensionValues = new ArrayList<String>();
            List<String> sortedSelectionCodes = sortTimeListFromRecentToOldest(selectionCodes);

            String latestSelectionCode = sortedSelectionCodes.get(0);
            int indexLatestSelectionCode = sortedTemporalCoverageCodes.indexOf(latestSelectionCode);

            effectiveDimensionValues.addAll(selectionCodes);
            if (indexLatestSelectionCode >= 0) {
                // add codes added after lastest selected code
                List<String> temporalCodesAddedAfterLatestSelectedCodeString = sortedTemporalCoverageCodes.subList(0, indexLatestSelectionCode);
                effectiveDimensionValues.addAll(0, temporalCodesAddedAfterLatestSelectedCodeString);
            }

            return effectiveDimensionValues;
        } else if (QueryTypeEnum.LATEST_DATA.equals(type)) {
            // return N data
            int codeLastIndexToReturn = Math.min(sortedTemporalCoverageCodes.size(), source.getLatestDataNumber());
            return sortedTemporalCoverageCodes.subList(0, codeLastIndexToReturn);
        } else {
            throw commonDo2RestMapper.buildRestException("QueryTypeEnum unsupported: " + source);
        }
    }

}