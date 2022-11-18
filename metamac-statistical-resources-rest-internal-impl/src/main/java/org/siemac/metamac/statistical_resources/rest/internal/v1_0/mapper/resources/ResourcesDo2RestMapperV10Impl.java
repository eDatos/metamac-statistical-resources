package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.resources;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ItemBase;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Resources;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourcesDo2RestMapperV10Impl implements ResourcesDo2RestMapperV10 {

    // TODO EDATOS-3770 PONER ESTAS CONSTANTES EN API-COMMON
    public static String       KIND_RESOURCES                      = StatisticalResourcesRestInternalConstants.API_NAME + StatisticalResourcesRestInternalConstants.KIND_SEPARATOR + "resources";
    public static String       KIND_RESOURCE                        = StatisticalResourcesRestInternalConstants.API_NAME + StatisticalResourcesRestInternalConstants.KIND_SEPARATOR + "resource";
    public static String       LINK_SUBPATH_RESOURCES               = "resources";
    @Autowired
    private CommonDo2RestMapperV10   commonDo2RestMapper;

    private static final Logger      logger = LoggerFactory.getLogger(ResourcesDo2RestMapperV10.class);

    
    @Override
    public Resources toResources(PagedResult<GeoCovVarElementCacheDatasetVersion> sources, String query, String orderBy, Integer limit, List<String> selectedLanguages) {

        Resources targets = new Resources();
        targets.setKind(StatisticalResourcesRestInternalConstants.KIND_DATASETS);

        // Pagination
        String baseLink =  toResourcesLink(TypeExternalArtefactsEnum.DATASET.getName());
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        // Values
        for (GeoCovVarElementCacheDatasetVersion source : sources.getValues()) {
            Resource target = toResource(source, selectedLanguages);
            targets.getResources().add(target);
        }
        return targets;
    } 
     
    private Resource toResource(GeoCovVarElementCacheDatasetVersion source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setResourceID(toItemBase(source.getCode(), source.getUrn(), source.getTitle(), TypeExternalArtefactsEnum.DATASET, selectedLanguages ));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_DATASET);
        target.setSelfLink(toDatasetSelfLink(source.getUrn(), TypeExternalArtefactsEnum.DATASET.getName()));
        target.setVisualizerHtmlLink(source.getHtmlLink());
        target.setStatisticalOperation(toItemBase(source.getOperationCode(), source.getOperationUrn(), source.getOperationTitle(), TypeExternalArtefactsEnum.STATISTICAL_OPERATION, selectedLanguages ));
        //target.setManagementAppLink(toDatasetVersionManagementApplicationLink(source));

        return target;
    }
    
    private ItemBase toItemBase(String id, String urn, InternationalString name, TypeExternalArtefactsEnum type, List<String> selectedLanguages) {
        ItemBase target = new ItemBase();
        target.setId(id);
        target.setUrn(urn);
        target.setName(commonDo2RestMapper.toInternationalString(name, selectedLanguages));
        target.setType(type.getName());
        return target;
    }
    
    private String toResourcesLink(String typeResource) {
        String resourceSubpath = LINK_SUBPATH_RESOURCES +  StatisticalResourcesRestInternalConstants.KIND_SEPARATOR + typeResource.toLowerCase();
        return commonDo2RestMapper.toResourceLink(resourceSubpath, null, null, null);
    }
    
    private ResourceLink toDatasetSelfLink(String urn, String typeResource) {
        String[] params = UrnUtils.splitUrnItem(urn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];
        return toResourceSelfLink(agencyId, resourceId, version, typeResource);
    }
    
    private ResourceLink toResourceSelfLink(String agencyID, String resourceID, String version, String typeResource) {
        String link = toDatasetLink(agencyID, resourceID, version, typeResource);
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_DATASET, link);
    }
    
    private String toDatasetLink(String agencyID, String resourceID, String version, String typeResource) {
        String resourceSubpath = LINK_SUBPATH_RESOURCES + StatisticalResourcesRestInternalConstants.KIND_SEPARATOR + typeResource.toLowerCase();
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }
        
    /*
    
    
    private ItemBase toItemBase(GeoCovVarElementCacheDatasetVersion source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.getr
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_DATASET);
        target.setSelfLink(toDatasetSelfLink(source, asLatest));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setManagementAppLink(toDatasetVersionManagementApplicationLink(source));

        return target;
    }

    @Override
    public Dataset toResource(DatasetVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> fields) throws Exception {
        if (source == null) {
            return null;
        }
        Dataset target = new Dataset();
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_DATASET);
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        target.setSelfLink(toDatasetSelfLink(source, false));
        target.setManagementAppLink(toDatasetVersionManagementApplicationLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toDatasetParentLink(source));
        target.setChildLinks(toDatasetChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));

        DsdProcessorResult dsdProcessorResult = null;

        boolean includeMetadata = !containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_EXCLUDE_METADATA);
        boolean includeData = !containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_EXCLUDE_DATA);
        if (includeMetadata || includeData) {
            dsdProcessorResult = commonDo2RestMapper.processDataStructure(source.getRelatedDsd().getUrn());
        }
        if (includeMetadata) {
            target.setMetadata(toDatasetMetadata(source, dsdProcessorResult, selectedLanguages, fields));
        }
        if (includeData) {
            target.setData(toDatasetData(source, dsdProcessorResult, selectedDimensions, selectedLanguages));
        }
        boolean includeKeywords = containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_INCLUDE_KEYWORDS);
        if (includeKeywords) {
            target.setKeywords(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getKeywords(), selectedLanguages));
        }
        return target;
    }

    public Data toDatasetData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages) throws Exception {

        if (source == null) {
            return null;
        }
        Map<String, List<String>> effectiveSelectionValues = calculateEffectiveDimensionValuesToDataset(dimensionValuesSelected, source);
        return commonDo2RestMapper.toData(source, dsdProcessorResult, effectiveSelectionValues, selectedLanguages);
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
    public ResourceInternal toResource(DatasetVersion source, List<String> selectedLanguages) {
        return toResource(source, false, selectedLanguages);
    }

    @Override
    public ResourceInternal toResourceAsLatest(DatasetVersion source, List<String> selectedLanguages) {
        return toResource(source, true, selectedLanguages);
    }

    @Override
    public ResourceInternal toResource(RelatedResourceResult source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.DATASET_VERSION.equals(source.getType())) {
            logger.error("RelatedResource unsupported: " + source.getType());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        ResourceInternal target = new ResourceInternal();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_DATASET);
        target.setSelfLink(toDatasetSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setManagementAppLink(toDatasetVersionManagementApplicationLink(source));

        return target;
    }

    private ResourceInternal toResource(DatasetVersion source, boolean asLatest, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_DATASET);
        target.setSelfLink(toDatasetSelfLink(source, asLatest));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setManagementAppLink(toDatasetVersionManagementApplicationLink(source));

        return target;
    }

    private DatasetMetadata toDatasetMetadata(DatasetVersion source, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages, Set<String> fields) throws MetamacException {
        if (source == null) {
            return null;
        }
        DatasetMetadata target = new DatasetMetadata();
        target.setRelatedDsd(commonDo2RestMapper.toDataStructureDefinition(source.getRelatedDsd(), dsdProcessorResult.getDataStructure(), selectedLanguages));
        target.setDimensions(commonDo2RestMapper.toDimensions(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, null, selectedLanguages, fields));
        target.setAttributes(commonDo2RestMapper.toAttributes(source.getSiemacMetadataStatisticalResource().getUrn(), dsdProcessorResult, selectedLanguages));
        target.setGeographicCoverages(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getGeographicCoverage(), selectedLanguages));
        target.setTemporalCoverages(toTemporalCoverages(source.getTemporalCoverage(), selectedLanguages));
        target.setMeasureCoverages(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getMeasureCoverage(), selectedLanguages));
        target.setGeographicGranularities(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getGeographicGranularities(), selectedLanguages));
        target.setTemporalGranularities(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getTemporalGranularities(), selectedLanguages));
        target.setDateStart(commonDo2RestMapper.toDate(source.getDateStart()));
        target.setDateEnd(commonDo2RestMapper.toDate(source.getDateEnd()));
        target.setStatisticalUnit(commonDo2RestMapper.toResourcesExternalItemsSrm(source.getStatisticalUnit(), selectedLanguages));
        target.setSubjectAreas(toDatasetSubjectAreas(source, selectedLanguages));
        target.setFormatExtentObservations(source.getFormatExtentObservations());
        target.setFormatExtentDimensions(source.getFormatExtentDimensions());
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
   
    private ResourcesInternal toDatasetIsRequiredBy(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsRequiredBy = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            relatedResourceIsRequiredBy = datasetVersionRepository.retrieveIsRequiredBy(source);
        } else {
            relatedResourceIsRequiredBy = datasetVersionRepository.retrieveIsRequiredByOnlyLastPublished(source);
        }

        if (CollectionUtils.isEmpty(relatedResourceIsRequiredBy)) {
            return null;
        }
        ResourcesInternal targets = new ResourcesInternal();
        for (RelatedResourceResult relatedResourceResult : relatedResourceIsRequiredBy) {
            targets.getResources().add(commonDo2RestMapper.toResource(relatedResourceResult, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    private ResourceInternal toDatasetReplacesVersion(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResource replacesVersion = source.getSiemacMetadataStatisticalResource().getReplacesVersion();
        return commonDo2RestMapper.toResource(replacesVersion, selectedLanguages);
    }

    private ResourceInternal toDatasetIsReplacedByVersion(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesByVersion = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesByVersion = datasetVersionRepository.retrieveIsReplacedByVersion(source);
        } else {
            relatedResourceReplacesByVersion = datasetVersionRepository.retrieveIsReplacedByVersionOnlyLastPublished(source);
        }
        return toResource(relatedResourceReplacesByVersion, selectedLanguages);
    }

    private ResourceInternal toDatasetReplaces(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResource replaces = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            replaces = source.getSiemacMetadataStatisticalResource().getReplaces();
        } else {
            replaces = source.getSiemacMetadataStatisticalResource().getReplaces();
            if (replaces != null) {
                String urn = replaces.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn();
                try {
                    datasetVersionRepository.retrieveByUrnPublished(urn);
                } catch (MetamacException e) {
                    if (!e.getExceptionItems().isEmpty() && e.getExceptionItems().iterator().next().getCode().equals(ServiceExceptionType.DATASET_VERSION_NOT_FOUND.getCode())) {
                        return null;
                    }
                }
            }
        }

        return commonDo2RestMapper.toResource(replaces, selectedLanguages);
    }

    private ResourceInternal toDatasetIsReplacedBy(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesBy = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesBy = datasetVersionRepository.retrieveIsReplacedBy(source);
        } else {
            relatedResourceReplacesBy = datasetVersionRepository.retrieveIsReplacedByOnlyLastPublished(source);
        }
        return toResource(relatedResourceReplacesBy, selectedLanguages);
    }

    private ResourcesInternal toDatasetIsPartOf(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsPartOf = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            relatedResourceIsPartOf = datasetVersionRepository.retrieveIsPartOf(source);
        } else {
            relatedResourceIsPartOf = datasetVersionRepository.retrieveIsPartOfOnlyLastPublished(source);
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

    private ResourcesInternal toDatasetSubjectAreas(DatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        if (CollectionUtils.isEmpty(source.getCategorisations())) {
            return null;
        }
        ResourcesInternal targets = new ResourcesInternal();
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
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_DATASETS, toDatasetsLink(agencyID, resourceID, version));
    }

    private String toDatasetsLink(String agencyID, String resourceID, String version) {
        String resourceSubpath = StatisticalResourcesRestInternalConstants.LINK_SUBPATH_DATASETS;
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    private ResourceLink toDatasetSelfLink(DatasetVersion source, boolean asLatest) {
        String agencyID = source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        String resourceID = source.getLifeCycleStatisticalResource().getCode();
        String version = null;
        if (asLatest) {
            version = StatisticalResourcesRestInternalConstants.WILDCARD_LATEST;
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
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_DATASET, link);
    }

    private String toDatasetLink(String agencyID, String resourceID, String version) {
        String resourceSubpath = StatisticalResourcesRestInternalConstants.LINK_SUBPATH_DATASETS;
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

    @Override
    public ResourceLink toDatasetSelfLink(LifeCycleStatisticalResourceDto source) {
        String agencyID = source.getMaintainer().getCodeNested();
        String resourceID = source.getCode();
        String version = source.getVersionLogic();
        return toDatasetSelfLink(agencyID, resourceID, version);
    }

    @Override
    public ResourceLink toDatasetSelfLink(LifeCycleStatisticalResourceBaseDto source) {
        String agencyID = source.getMaintainerCodeNested();
        String resourceID = source.getCode();
        String version = source.getVersionLogic();
        return toDatasetSelfLink(agencyID, resourceID, version);
    }

    private String toDatasetVersionManagementApplicationLink(DatasetVersion source) {
        return commonDo2RestMapper.getInternalWebApplicationNavigation().buildDatasetVersionUrl(source);
    }

    private String toDatasetVersionManagementApplicationLink(RelatedResourceResult source) {
        return commonDo2RestMapper.getInternalWebApplicationNavigation().buildDatasetVersionUrl(source);
    }
        */
}