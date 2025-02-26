package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset;

import static org.siemac.metamac.core.common.util.rest.RequestUtil.containsField;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.isTemporalDimension;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.Items;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DatasetMetadata;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.DimensionsFilter;
import org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources.ExternalRestObjectsMapper;
import org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ConstraintDimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Datasets;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.HtmlLinkUtil;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.jsonstat.CommonDo2JsonStatRestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetsDo2RestMapperV10Impl implements DatasetsDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10            commonDo2RestMapper;

    @Autowired
    private CommonDo2JsonStatRestMapperV10    commonDo2JsonStatRestMapper;

    @Autowired
    private DatasetVersionRepository          datasetVersionRepository;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    private static final Logger               logger = LoggerFactory.getLogger(DatasetsDo2RestMapperV10.class);

    @Override
    public JsonStatData toJsonStatDataset(DatasetVersion source, Map<String, List<String>> selectedDimensions, String selectedLanguage, Set<String> parsedFields, String granularity) throws Exception {
        if (source == null) {
            return null;
        }

        List<String> selectedLanguages = Collections.singletonList(selectedLanguage);

        DsdProcessorResult dsdProcessorResult = commonDo2RestMapper.processDataStructure(source.getRelatedDsd().getUrn());
        Map<String, List<String>> granularities = commonDo2RestMapper.parseParamExpression(granularity);
        DimensionsFilter dimensionsFilter = commonDo2RestMapper.getDimensionsFilter(granularities, dsdProcessorResult);
        Dimensions dimensions = commonDo2RestMapper.toDimensions(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, null, selectedLanguages, parsedFields, dimensionsFilter);
        Data data = toDatasetData(source, dsdProcessorResult, selectedDimensions, selectedLanguages, dimensions);

        Attributes attributes = commonDo2RestMapper.toAttributes(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages);

        // ********************************************
        // ***** See https://json-stat.org/full/ ******
        // ********************************************

        JsonStatData target = new JsonStatData();

        target.setVersion(CommonDo2JsonStatRestMapperV10.JSON_STAT_VERSION);
        target.setClazz(CommonDo2JsonStatRestMapperV10.JSON_STAT_CLASS);
        target.addAllValues(commonDo2JsonStatRestMapper.toJsonStatDatasetValues(data));
        target.setDimension(commonDo2JsonStatRestMapper.toJsonStatDatasetDimensions(dimensions, data.getDimensions(), dsdProcessorResult, attributes, data.getAttributes(), selectedLanguage));
        target.setRole(commonDo2JsonStatRestMapper.toJsonStatRoles(dsdProcessorResult));
        target.setId(commonDo2JsonStatRestMapper.getJsonStatId(data, dsdProcessorResult));
        target.setSize(commonDo2JsonStatRestMapper.toJsonStatSize(data, dsdProcessorResult, attributes));
        target.setLabel(commonDo2JsonStatRestMapper.toI18nValue(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguage));
        target.setUpdated(source.getSiemacMetadataStatisticalResource().getLastUpdate().toString());
        target.setExtension(commonDo2JsonStatRestMapper.toJsonStatExtension(source, dimensions, selectedLanguage));
        target.setNote(commonDo2JsonStatRestMapper.toJsonStatNote(source, data, dimensions, attributes, dsdProcessorResult, selectedLanguage));

        return target;
    }

    @Override
    public Datasets toDatasets(PagedResult<DatasetVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            Set<String> parsedFields) throws MetamacException {

        Datasets targets = new Datasets();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_DATASETS);

        // Pagination
        String baseLink = toDatasetsLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        // Values
        for (DatasetVersion source : sources.getValues()) {
            ResourceWithStatisticalOperation target = toResource(source, selectedLanguages, parsedFields, externalRestObjectsMapper);
            targets.getDatasets().add(target);
        }
        return targets;
    }

    @Override
    public Dataset toDataset(DatasetVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> fields, String granularity) throws Exception {
        if (source == null) {
            return null;
        }
        Dataset target = new Dataset();
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_DATASET);
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        target.setSelfLink(toDatasetSelfLink(source, false));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toDatasetParentLink(source));
        target.setChildLinks(toDatasetChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.DATASET, source.getLifeCycleStatisticalResource(), configurationService, false));
        DsdProcessorResult dsdProcessorResult = null;
        Dimensions dimensions = null;
        DimensionsFilter dimensionsFilter = new DimensionsFilter();
        boolean includeMetadata = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
        boolean includeData = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
        Map<String, List<String>> granularities = commonDo2RestMapper.parseParamExpression(granularity);
        if (includeMetadata || includeData) {
            dsdProcessorResult = commonDo2RestMapper.processDataStructure(source.getRelatedDsd().getUrn());
            dimensionsFilter = commonDo2RestMapper.getDimensionsFilter(granularities, dsdProcessorResult);
        }

        if (includeMetadata || (includeData && (dimensionsFilter.getTemporalDimensionValuesIds() != null && !dimensionsFilter.getTemporalDimensionValuesIds().isEmpty())
                || (dimensionsFilter.getGeographicDimensionValuesIds() != null && !dimensionsFilter.getGeographicDimensionValuesIds().isEmpty()))) {

            dimensions = commonDo2RestMapper.toDimensions(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, null, selectedLanguages, fields, dimensionsFilter);
        }

        if (includeMetadata) {
            boolean includeConstraint = containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_DATASET_CONSTRAINTS);
            ConstraintDimensionRepresentations constraintDimensionRepresentations = null;
            if (includeConstraint) {
                constraintDimensionRepresentations = commonDo2RestMapper.processDatasetConstraint(source.getSiemacMetadataStatisticalResource().getUrn());
            }

            target.setMetadata(toDatasetMetadata(source, dsdProcessorResult, constraintDimensionRepresentations, selectedLanguages, dimensions));

        }
        if (includeData) {
            target.setData(toDatasetData(source, dsdProcessorResult, selectedDimensions, selectedLanguages, dimensions));
        }
        boolean includeKeywords = containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_KEYWORDS);
        if (includeKeywords) {
            target.setKeywords(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getKeywords(), selectedLanguages));
        }

        return target;
    }

    public Data toDatasetData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages) throws Exception {
        return toDatasetData(source, dsdProcessorResult, dimensionValuesSelected, selectedLanguages, null);
    }

    public Data toDatasetData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages, Dimensions dimensions)
            throws Exception {

        if (source == null) {
            return null;
        }
        DimensionsFilter dimensionsFilter = commonDo2RestMapper.getDimensionFilter(dimensions);
        Map<String, List<String>> effectiveSelectionValues = calculateEffectiveDimensionValuesToDataset(dimensionValuesSelected, source);
        return commonDo2RestMapper.toData(source, dsdProcessorResult, effectiveSelectionValues, selectedLanguages, dimensionsFilter);
    }

    public Map<String, List<String>> calculateEffectiveDimensionValuesToDataset(Map<String, List<String>> selectedDimensions, DatasetVersion datasetVersion) {
        Map<String, List<String>> dimensionValuesSelected = new HashMap<String, List<String>>(selectedDimensions.size());
        for (Entry<String, List<String>> selectedDimension : selectedDimensions.entrySet()) {
            String dimensionId = selectedDimension.getKey();
            List<String> selectedValues = selectedDimension.getValue();
            if (isTemporalDimension(dimensionId)) {
                List<String> temporalCoverageValues = commonDo2RestMapper.temporalCoverageToString(datasetVersion.getTemporalCoverage());
                List<String> effectiveValues = StatisticalResourcesRestImplCommonUtils.calculateEffectiveTemporalSelectionValues(temporalCoverageValues, selectedValues);
                dimensionValuesSelected.put(dimensionId, effectiveValues);
            } else {
                dimensionValuesSelected.put(dimensionId, selectedValues);
            }
        }
        return dimensionValuesSelected;
    }

    @Override
    public ResourceWithStatisticalOperation toResource(DatasetVersion source, List<String> selectedLanguages, Set<String> parsedFields, ExternalRestObjectsMapper externalRestObjectsMapper)
            throws MetamacException {
        return toResource(source, false, selectedLanguages, parsedFields, externalRestObjectsMapper);
    }

    @Override
    public ResourceStatisticalResourceBase toResourceAsLatest(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        return toResource(source, true, selectedLanguages, null, new ExternalRestObjectsMapper());
    }

    @Override
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.DATASET_VERSION.equals(source.getType())) {
            logger.error("RelatedResource unsupported: " + source.getType());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_DATASET);
        target.setSelfLink(toDatasetSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setVisualizerHtmlLink(toVisualizerHtmlLink(source));
        return target;
    }

    private String toVisualizerHtmlLink(RelatedResourceResult source) throws MetamacException {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        String version = source.getVersion();
        return HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.DATASET, agencyID, resourceID, version, configurationService, false);
    }

    private ResourceWithStatisticalOperation toResource(DatasetVersion source, boolean asLatest, List<String> selectedLanguages, Set<String> parsedFields,
            ExternalRestObjectsMapper externalRestObjectsMapper) throws MetamacException {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_DATASET);
        target.setSelfLink(toDatasetSelfLink(source, asLatest));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        boolean includeStatisticalOperation = containsField(parsedFields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_STATISTICAL_OPERATION);
        if (includeStatisticalOperation) {
            target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getSiemacMetadataStatisticalResource().getStatisticalOperation(), selectedLanguages,
                    externalRestObjectsMapper));
        }
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.DATASET, source.getLifeCycleStatisticalResource(), configurationService, asLatest));
        return target;
    }

    private DatasetMetadata toDatasetMetadata(DatasetVersion source, DsdProcessorResult dsdProcessorResult, ConstraintDimensionRepresentations constraintDimensionRepresentations,
            List<String> selectedLanguages, Dimensions dimensions) throws MetamacException {
        if (source == null) {
            return null;
        }
        DatasetMetadata target = new DatasetMetadata();
        target.setRelatedDsd(commonDo2RestMapper.toDataStructureDefinition(source.getRelatedDsd(), dsdProcessorResult.getDataStructure(), selectedLanguages, source.getHeadingDimensions(),
                source.getStubDimensions()));
        target.setDimensions(dimensions);
        target.setAttributes(commonDo2RestMapper.toAttributes(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages));
        target.setConstraints(constraintDimensionRepresentations);
        target.setGeographicCoverages(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getGeographicCoverage(), selectedLanguages));
        target.setTemporalCoverages(toTemporalCoverages(source.getTemporalCoverage(), selectedLanguages));
        target.setMeasureCoverages(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getMeasureCoverage(), selectedLanguages));
        target.setGeographicGranularities(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getGeographicGranularities(), selectedLanguages));
        target.setTemporalGranularities(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getTemporalGranularities(), selectedLanguages));
        target.setDateStart(commonDo2RestMapper.toSdmxObservationalTimePeriod(source.getDateStart(), selectedLanguages));
        target.setDateEnd(commonDo2RestMapper.toSdmxObservationalTimePeriod(source.getDateEnd(), selectedLanguages));
        target.setStatisticalUnit(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getStatisticalUnit(), selectedLanguages));
        target.setSubjectAreas(toDatasetSubjectAreas(source, selectedLanguages));
        target.setFormatExtentObservations(source.getFormatExtentObservations());
        target.setFormatExtentDimensions(source.getFormatExtentDimensions());
        target.setFormatExtentTableSize(source.getFormatExtentTableSize());
        target.setDateNextUpdate(commonDo2RestMapper.toSdmxObservationalTimePeriod(source.getDateNextUpdate(), selectedLanguages));
        target.setUpdateFrequency(commonDo2RestMapper.toResourceExternalItemSrm(source.getUpdateFrequency(), selectedLanguages));
        target.setStatisticOfficiality(toStatisticOfficiality(source.getStatisticOfficiality(), selectedLanguages));
        target.setBibliographicCitation(toBibliographicCitation(source, source.getBibliographicCitation(), selectedLanguages));
        target.setIsRequiredBy(toDatasetIsRequiredBy(source, selectedLanguages));
        target.setReplacesVersion(toDatasetReplacesVersion(source, selectedLanguages));
        target.setIsReplacedByVersion(toDatasetIsReplacedByVersion(source, selectedLanguages));
        target.setReplaces(toDatasetReplaces(source, selectedLanguages));
        target.setIsReplacedBy(toDatasetIsReplacedBy(source, selectedLanguages));
        target.setIsPartOf(toDatasetIsPartOf(source, selectedLanguages));
        target.setKeepAllData(source.isKeepAllData());

        // StatisticalResource and other
        commonDo2RestMapper.toMetadataStatisticalResource(source.getSiemacMetadataStatisticalResource(), target, selectedLanguages);
        return target;
    }

    private ResourcesStatisticalResourceBase toDatasetIsRequiredBy(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsRequiredBy = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceIsRequiredBy = datasetVersionRepository.retrieveIsRequiredBy(source);
        } else {
            relatedResourceIsRequiredBy = datasetVersionRepository.retrieveIsRequiredByOnlyLastPublished(source);
        }

        if (CollectionUtils.isEmpty(relatedResourceIsRequiredBy)) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (RelatedResourceResult relatedResourceResult : relatedResourceIsRequiredBy) {
            targets.getResources().add(commonDo2RestMapper.toResource(relatedResourceResult, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    private ResourceStatisticalResourceBase toDatasetReplacesVersion(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResource replacesVersion = source.getSiemacMetadataStatisticalResource().getReplacesVersion();
        return commonDo2RestMapper.toResource(replacesVersion, selectedLanguages);
    }

    private ResourceStatisticalResourceBase toDatasetIsReplacedByVersion(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesByVersion = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesByVersion = datasetVersionRepository.retrieveIsReplacedByVersion(source);
        } else {
            relatedResourceReplacesByVersion = datasetVersionRepository.retrieveIsReplacedByVersionOnlyIfPublished(source);
        }
        return toResource(relatedResourceReplacesByVersion, selectedLanguages);
    }

    private ResourceStatisticalResourceBase toDatasetReplaces(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        // Contrary to the internal API, there is no need to check if the replaced resource is published. The "replaces" metadata is always filled with a published dataset.
        RelatedResource replaces = source.getSiemacMetadataStatisticalResource().getReplaces();
        return commonDo2RestMapper.toResource(replaces, selectedLanguages);
    }

    private ResourceStatisticalResourceBase toDatasetIsReplacedBy(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesBy = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesBy = datasetVersionRepository.retrieveIsReplacedBy(source);
        } else {
            relatedResourceReplacesBy = datasetVersionRepository.retrieveIsReplacedByOnlyIfPublished(source);
        }
        return toResource(relatedResourceReplacesBy, selectedLanguages);
    }

    private ResourcesStatisticalResourceBase toDatasetIsPartOf(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsPartOf = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceIsPartOf = datasetVersionRepository.retrieveIsPartOf(source);
        } else {
            relatedResourceIsPartOf = datasetVersionRepository.retrieveIsPartOfOnlyLastPublished(source);
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

    private Items toTemporalCoverages(List<TemporalCode> sources, List<String> selectedLanguages) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        Items targets = new Items();
        for (TemporalCode source : sources) {
            targets.getItems().add(toTemporalCoverage(source, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getItems().size()));
        return targets;
    }

    private Item toTemporalCoverage(TemporalCode source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getIdentifier());
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        return target;
    }

    private ResourcesStatisticalResourceBase toDatasetSubjectAreas(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        if (CollectionUtils.isEmpty(source.getCategorisations())) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (Categorisation categorisation : source.getCategorisations()) {
            targets.getResources().add(commonDo2RestMapper.toResourceExternalItemSrm(categorisation.getCategory(), selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    private Item toStatisticOfficiality(StatisticOfficiality source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getIdentifier());
        target.setName(commonDo2RestMapper.toInternationalString(source.getDescription(), selectedLanguages));
        return target;
    }

    private ResourceLink toDatasetParentLink(DatasetVersion source) {
        return toDatasetsSelfLink(null, null, null);
    }

    private ChildLinks toDatasetChildLinks(DatasetVersion source) {
        // nothing
        return null;
    }

    private ResourceLink toDatasetsSelfLink(String agencyID, String resourceID, String version) {
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_DATASETS, toDatasetsLink(agencyID, resourceID, version));
    }

    private String toDatasetsLink(String agencyID, String resourceID, String version) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_DATASETS;
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    private ResourceLink toDatasetSelfLink(DatasetVersion source, boolean asLatest) {
        String agencyID = source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        String resourceID = source.getLifeCycleStatisticalResource().getCode();
        String version = null;
        if (asLatest) {
            version = StatisticalResourcesRestExternalConstants.WILDCARD_LATEST;
        } else {
            version = source.getLifeCycleStatisticalResource().getVersionLogic();
        }
        return toDatasetSelfLink(agencyID, resourceID, version);
    }

    private ResourceLink toDatasetSelfLink(RelatedResourceResult source) {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        String version = source.getVersion();
        return toDatasetSelfLink(agencyID, resourceID, version);
    }

    private ResourceLink toDatasetSelfLink(String agencyID, String resourceID, String version) {
        String link = toDatasetLink(agencyID, resourceID, version);
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_DATASET, link);
    }

    private String toDatasetLink(String agencyID, String resourceID, String version) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_DATASETS;
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    private String toDatasetLink(DatasetVersion source) throws MetamacException {
        String agencyID = source.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested();
        String resourceID = source.getSiemacMetadataStatisticalResource().getCode();
        String version = source.getSiemacMetadataStatisticalResource().getVersionLogic();

        return toDatasetLink(agencyID, resourceID, version);
    }

    private InternationalString toBibliographicCitation(DatasetVersion datasetVersion, org.siemac.metamac.statistical.resources.core.common.domain.InternationalString sources,
            List<String> selectedLanguages) throws MetamacException {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString source : sources.getTexts()) {
            if (selectedLanguages.contains(source.getLocale())) {
                LocalisedString target = new LocalisedString();
                target.setLang(source.getLocale());
                target.setValue(source.getLabel().replace(StatisticalResourcesConstants.BIBLIOGRAPHIC_CITATION_URI_TOKEN, toDatasetLink(datasetVersion)));
                targets.getTexts().add(target);
            }
        }
        return targets;
    }

}