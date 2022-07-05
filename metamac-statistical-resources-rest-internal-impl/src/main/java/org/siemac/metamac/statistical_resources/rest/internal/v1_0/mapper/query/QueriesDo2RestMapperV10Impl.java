package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.query;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceQueryUrn;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.isDateAfterNowSetNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryMetadata;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourcesInternal;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;
import org.siemac.metamac.statistical_resources.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueriesDo2RestMapperV10Impl implements QueriesDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10   commonDo2RestMapper;

    @Autowired
    private DatasetsDo2RestMapperV10 datasetsDo2RestMapper;

    @Autowired
    private QueryVersionRepository   queryVersionRepository;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    private static final Logger      logger = LoggerFactory.getLogger(QueriesDo2RestMapperV10Impl.class);

    @Override
    public Queries toQueries(PagedResult<QueryVersion> sources, String agencyID, String query, String orderBy, Integer limit, List<String> selectedLanguages) {

        Queries targets = new Queries();
        targets.setKind(StatisticalResourcesRestInternalConstants.KIND_QUERIES);

        // Pagination
        String baseLink = toQueriesLink(agencyID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        // Values
        for (QueryVersion source : sources.getValues()) {
            ResourceInternal target = toResource(source, selectedLanguages);
            targets.getQueries().add(target);
        }
        return targets;
    }

    @Override
    public Query toQuery(QueryVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, boolean includeMetadata, boolean includeData) throws Exception {
        if (source == null) {
            return null;
        }
        Query target = new Query();
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_QUERY);
        target.setId(source.getLifeCycleStatisticalResource().getCode());
        target.setUrn(toQueryUrn(source));
        target.setSelfLink(toQuerySelfLink(source));
        target.setManagementAppLink(toQueryVersionManagementApplicationLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toQueryParentLink(source));
        target.setChildLinks(toQueryChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        DsdProcessorResult dsdProcessorResult = null;
        DatasetVersion relatedDatasetEffective = null;
        if (includeMetadata || includeData) {
            relatedDatasetEffective = getQueryRelatedDatasetVersionEffective(source);
            dsdProcessorResult = commonDo2RestMapper.processDataStructure(relatedDatasetEffective.getRelatedDsd().getUrn());
        }
        if (includeMetadata) {
            target.setMetadata(toQueryMetadata(source, relatedDatasetEffective, dsdProcessorResult, selectedLanguages));
        }
        if (includeData) {
            target.setData(toQueryData(source, relatedDatasetEffective, dsdProcessorResult, selectedDimensions, selectedLanguages));
        }
        return target;
    }

    private DatasetVersion getQueryRelatedDatasetVersionEffective(QueryVersion source) throws MetamacException {
        if (source.getFixedDatasetVersion() != null) {
            return source.getFixedDatasetVersion();
        } else {
            if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
                return datasetVersionRepository.retrieveLastVersion(source.getDataset().getIdentifiableStatisticalResource().getUrn());
            } else {
                return datasetVersionRepository.retrieveLastPublishedVersion(source.getDataset().getIdentifiableStatisticalResource().getUrn());
            }
        }
    }

    @Override
    public ResourceInternal toResource(QueryVersion source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        target.setId(source.getLifeCycleStatisticalResource().getCode());
        target.setUrn(toQueryUrn(source));
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_QUERY);
        target.setSelfLink(toQuerySelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getTitle(), selectedLanguages));
        target.setManagementAppLink(toQueryVersionManagementApplicationLink(source));

        return target;
    }

    @Override
    public ResourceInternal toResource(RelatedResourceResult source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.QUERY_VERSION.equals(source.getType())) {
            throw buildRestException("RelatedResource unsupported: " + source.getType());
        }

        ResourceInternal target = new ResourceInternal();
        target.setId(source.getCode());
        target.setUrn(toQueryUrn(source.getMaintainerNestedCode(), source.getCode()));
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_QUERY);
        target.setSelfLink(toQuerySelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setManagementAppLink(toQueryVersionManagementApplicationLink(source));

        return target;
    }

    @Override
    public ResourceLink toQuerySelfLink(LifeCycleStatisticalResourceDto source) {
        String agencyID = source.getMaintainer().getCodeNested();
        String resourceID = source.getCode();
        return toQuerySelfLink(agencyID, resourceID);
    }

    @Override
    public ResourceLink toQuerySelfLink(LifeCycleStatisticalResourceBaseDto source) {
        String agencyID = source.getMaintainerCodeNested();
        String resourceID = source.getCode();
        return toQuerySelfLink(agencyID, resourceID);
    }

    private QueryMetadata toQueryMetadata(QueryVersion source, DatasetVersion datasetVersion, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        QueryMetadata target = new QueryMetadata();

        Map<String, List<String>> effectiveDimensionValuesToDataByDimension = calculateEffectiveDimensionValuesToQuery(source, datasetVersion);

        target.setRelatedDsd(commonDo2RestMapper.toDataStructureDefinition(datasetVersion.getRelatedDsd(), dsdProcessorResult.getDataStructure(), selectedLanguages));
        target.setDimensions(commonDo2RestMapper.toDimensions(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, effectiveDimensionValuesToDataByDimension,
                selectedLanguages, null));
        target.setAttributes(commonDo2RestMapper.toAttributes(datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages));

        ResourceInternal relatedDataset = null;
        if (source.getDataset() != null) {
            relatedDataset = datasetsDo2RestMapper.toResourceAsLatest(datasetVersion, selectedLanguages);
        } else {
            relatedDataset = datasetsDo2RestMapper.toResource(datasetVersion, selectedLanguages);
        }
        target.setRelatedDataset(relatedDataset);
        target.setStatus(toQueryStatus(source.getStatus()));
        target.setType(toQueryType(source.getType()));
        target.setLatestDataNumber(source.getLatestDataNumber());
        target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getLifeCycleStatisticalResource().getStatisticalOperation(), selectedLanguages));
        target.setMaintainer(commonDo2RestMapper.toResourceExternalItemSrm(source.getLifeCycleStatisticalResource().getMaintainer(), selectedLanguages));
        target.setValidFrom(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getValidFrom()));
        target.setValidTo(commonDo2RestMapper.toDate(isDateAfterNowSetNull(source.getLifeCycleStatisticalResource().getValidTo())));
        target.setRequires(datasetsDo2RestMapper.toResource(datasetVersion, selectedLanguages));
        target.setIsPartOf(toQueryIsPartOf(source, selectedLanguages));
        target.setNextVersion(commonDo2RestMapper.toNextVersionType(source.getLifeCycleStatisticalResource().getNextVersion(), selectedLanguages));
        target.setNextVersionDate(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getNextVersionDate()));
        target.setProcStatus(commonDo2RestMapper.toProcStatusType(source.getLifeCycleStatisticalResource().getProcStatus(), selectedLanguages));
        target.setCreationDate(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getCreationDate()));
        target.setCreationUser(source.getLifeCycleStatisticalResource().getCreationUser());
        target.setProductionValidationDate(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getCreationDate()));
        target.setProductionValidationUser(source.getLifeCycleStatisticalResource().getPublicationUser());
        target.setDiffusionValidationDate(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getLifeCycleStatisticalResource().getDiffusionValidationUser());
        target.setRejectValidationDate(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getRejectValidationDate()));
        target.setRejectValidationUser(source.getLifeCycleStatisticalResource().getRejectValidationUser());
        target.setPublicationDate(commonDo2RestMapper.toDate(source.getLifeCycleStatisticalResource().getPublicationDate()));
        target.setPublicationUser(source.getLifeCycleStatisticalResource().getPublicationUser());

        return target;
    }

    private ResourcesInternal toQueryIsPartOf(QueryVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsPartOf = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            relatedResourceIsPartOf = queryVersionRepository.retrieveIsPartOf(source);
        } else {
            relatedResourceIsPartOf = queryVersionRepository.retrieveIsPartOfOnlyLastPublished(source);
        }

        if (CollectionUtils.isEmpty(relatedResourceIsPartOf)) {
            return null;
        }
        ResourcesInternal targets = new ResourcesInternal();
        for (RelatedResourceResult relatedResourceResult : relatedResourceIsPartOf) {
            targets.getResources().add(commonDo2RestMapper.toResource(relatedResourceResult, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    private Data toQueryData(QueryVersion source, DatasetVersion datasetVersion, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages)
            throws Exception {
        if (source == null) {
            return null;
        }
        Map<String, List<String>> effectiveQueryDimensionValuesToDataByDimension = calculateEffectiveDimensionValuesToQuery(source, datasetVersion);
        Map<String, List<String>> effectiveSelectionValues = calculateEffectiveSelectionValues(selectedDimensions, effectiveQueryDimensionValuesToDataByDimension);
        Map<String, List<String>> effectiveDimensionValuesToDataByDimension = StatisticalResourcesRestImplCommonUtils.filterDimensions(effectiveQueryDimensionValuesToDataByDimension,
                effectiveSelectionValues);
        return commonDo2RestMapper.toData(datasetVersion, dsdProcessorResult, effectiveDimensionValuesToDataByDimension, selectedLanguages);
    }

    private ResourceLink toQueryParentLink(QueryVersion source) {
        return toQueriesSelfLink(null, null);
    }

    private ChildLinks toQueryChildLinks(QueryVersion source) {
        // nothing
        return null;
    }

    private ResourceLink toQueriesSelfLink(String agencyID, String resourceID) {
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_QUERIES, toQueriesLink(agencyID, resourceID));
    }

    private String toQueriesLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestInternalConstants.LINK_SUBPATH_QUERIES;
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
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_QUERY, link);
    }

    private String toQueryLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestInternalConstants.LINK_SUBPATH_QUERIES;
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

    private org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryStatus toQueryStatus(QueryStatusEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case ACTIVE:
                return org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryStatus.ACTIVE;
            case DISCONTINUED:
                return org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryStatus.DISCONTINUED;
            default:
                logger.error("QueryStatusEnum unsupported: " + source);
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryType toQueryType(QueryTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case FIXED:
                return org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryType.FIXED;
            case AUTOINCREMENTAL:
                return org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryType.AUTOINCREMENTAL;
            case LATEST_DATA:
                return org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.QueryType.LATEST_DATA;
            default:
                throw buildRestException("QueryTypeEnum unsupported: " + source);
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
        QueryTypeEnum type = source.getType();
        if (QueryTypeEnum.FIXED.equals(type)) {
            // return exactly
            return selectionCodes;
        } else if (QueryTypeEnum.AUTOINCREMENTAL.equals(type)) {
            List<String> effectiveDimensionValues = new ArrayList<String>();

            List<String> sortedSelectionCodes = SdmxTimeUtils.sortTimeList(selectionCodes);
            List<String> sortedTemporalCoverageCodes = SdmxTimeUtils.sortTimeList(temporalCoverageCodes);

            String latestSelectionCode = sortedSelectionCodes.get(sortedSelectionCodes.size() - 1);
            int indexLatestSelectionCode = sortedTemporalCoverageCodes.indexOf(latestSelectionCode);

            effectiveDimensionValues.addAll(selectionCodes);
            if (indexLatestSelectionCode >= 0) {
                // add codes added after lastest selected code
                List<String> temporalCodesAddedAfterLatestSelectedCodeString = sortedTemporalCoverageCodes.subList(indexLatestSelectionCode, sortedTemporalCoverageCodes.size());

                for (String code : temporalCodesAddedAfterLatestSelectedCodeString) {
                    if (!effectiveDimensionValues.contains(code)) {
                        effectiveDimensionValues.add(code);
                    }
                }
            }

            // We reverse the array to restore the order after the sortTimeList invocation
            Collections.reverse(effectiveDimensionValues);

            return effectiveDimensionValues;
        } else if (QueryTypeEnum.LATEST_DATA.equals(type)) {
            // return N data
            int codeLastIndexToReturn = -1;
            if (temporalCoverageCodes.size() < source.getLatestDataNumber()) {
                codeLastIndexToReturn = temporalCoverageCodes.size(); // there is not N data, so return all
            } else {
                codeLastIndexToReturn = source.getLatestDataNumber();
            }
            return temporalCoverageCodes.subList(0, codeLastIndexToReturn);
        } else {
            throw buildRestException("QueryTypeEnum unsupported: " + source);
        }
    }

    private RestException buildRestException(String message) {
        logger.error(message);
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
        return new RestException(exception, Status.INTERNAL_SERVER_ERROR);
    }

    private String toQueryVersionManagementApplicationLink(QueryVersion source) {
        return commonDo2RestMapper.getInternalWebApplicationNavigation().buildQueryVersionUrl(source);
    }

    private String toQueryVersionManagementApplicationLink(RelatedResourceResult source) {
        return commonDo2RestMapper.getInternalWebApplicationNavigation().buildQueryVersionUrl(source);
    }
}