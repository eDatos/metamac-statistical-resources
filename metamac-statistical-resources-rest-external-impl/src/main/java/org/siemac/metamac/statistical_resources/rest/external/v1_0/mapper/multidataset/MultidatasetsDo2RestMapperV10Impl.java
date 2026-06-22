package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.multidataset;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceMultidatasetUrn;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.containsField;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Multidataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.MultidatasetMetadata;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources.ExternalRestObjectsMapper;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MultidatasetData;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MultidatasetNodes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MultidatasetTable;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Multidatasets;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.HtmlLinkUtil;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query.QueriesDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultidatasetsDo2RestMapperV10Impl implements MultidatasetsDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10            commonDo2RestMapper;

    @Autowired
    private DatasetsDo2RestMapperV10          datasetsDo2RestMapper;

    @Autowired
    private DatasetVersionRepository          datasetVersionRepository;

    @Autowired
    private QueryVersionRepository            queryVersionRepository;

    @Autowired
    private QueriesDo2RestMapperV10           queriesDo2RestMapper;

    @Autowired
    private MultidatasetVersionRepository     multidatasetVersionRepository;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    private static final Logger               logger = LoggerFactory.getLogger(MultidatasetsDo2RestMapperV10.class);

    @Override
    public Multidatasets toMultidatasets(PagedResult<MultidatasetVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            Set<String> parsedFields) throws MetamacException {

        Multidatasets targets = new Multidatasets();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_MULTIDATASETS);

        // Pagination
        String baseLink = toMultidatasetsLink(agencyID);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        // Values
        for (MultidatasetVersion source : sources.getValues()) {
            ResourceWithStatisticalOperation target = toResource(source, selectedLanguages, parsedFields, externalRestObjectsMapper);
            targets.getMultidatasets().add(target);
        }
        return targets;
    }

    @Override
    public Multidataset toMultidataset(MultidatasetVersion source, List<String> selectedLanguages, Set<String> fields) throws Exception {
        if (source == null) {
            return null;
        }
        Multidataset target = new Multidataset();
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_MULTIDATASET);
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(toMultidatasetUrn(source));
        target.setSelfLink(toMultidatasetSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toMultidatasetParentLink(source));
        target.setChildLinks(toMultidatasetChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.MULTIDATASET, source.getLifeCycleStatisticalResource(), configurationService, false));
        boolean includeMetadata = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
        boolean includeData = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
        if (includeMetadata) {
            target.setMetadata(toMultidatasetMetadata(source, selectedLanguages));
        }
        if (includeData) {
            target.setData(toMultidatasetData(source, selectedLanguages));
        }
        boolean includeKeywords = containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_KEYWORDS);
        if (includeKeywords) {
            target.setKeywords(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getKeywords(), selectedLanguages));
        }
        return target;
    }

    @Override
    public ResourceWithStatisticalOperation toResource(MultidatasetVersion source, List<String> selectedLanguages, Set<String> parsedFields, ExternalRestObjectsMapper externalRestObjectsMapper)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(toMultidatasetUrn(source));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_MULTIDATASET);
        target.setSelfLink(toMultidatasetSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        boolean includeStatisticalOperation = containsField(parsedFields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_STATISTICAL_OPERATION);
        if (includeStatisticalOperation) {
            target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getSiemacMetadataStatisticalResource().getStatisticalOperation(), selectedLanguages,
                    externalRestObjectsMapper));
        }
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.MULTIDATASET, source.getLifeCycleStatisticalResource(), configurationService, false));
        target.setNewnessUntilDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getNewnessUntilDate()));
        target.setFeaturedUntilDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getFeaturedUntilDate()));

        return target;
    }

    @Override
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.MULTIDATASET_VERSION.equals(source.getType())) {
            logger.error("RelatedResource unsupported: " + source.getType());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setId(source.getCode());
        target.setUrn(toMultidatasetUrn(source.getMaintainerNestedCode(), source.getCode()));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_MULTIDATASET);
        target.setSelfLink(toMultidatasetSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setVisualizerHtmlLink(toVisualizerHtmlLink(source));
        return target;
    }

    private String toVisualizerHtmlLink(RelatedResourceResult source) throws MetamacException {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        return HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.MULTIDATASET, agencyID, resourceID, null, configurationService, false);
    }

    private MultidatasetMetadata toMultidatasetMetadata(MultidatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        MultidatasetMetadata target = new MultidatasetMetadata();
        target.setFormatExtentResources(source.getFormatExtentResources());
        target.setFilteringDimension(commonDo2RestMapper.toInternationalString(source.getFilteringDimension(), selectedLanguages));
        target.setReplaces(toMultidatasetReplaces(source, selectedLanguages));
        target.setIsReplacedBy(toMultidatasetIsReplacedBy(source, selectedLanguages));
        target.setIsPartOf(toMultidatasetIsPartOf(source, selectedLanguages));
        commonDo2RestMapper.toMetadataStatisticalResource(source.getSiemacMetadataStatisticalResource(), target, selectedLanguages);
        return target;
    }

    private ResourceStatisticalResourceBase toMultidatasetReplaces(MultidatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        // There is no need to check if the replaced resource is published. The "replaces" metadata is always filled with a published multidataset.
        RelatedResource replaces = source.getSiemacMetadataStatisticalResource().getReplaces();
        return commonDo2RestMapper.toResource(replaces, selectedLanguages);
    }

    private ResourceStatisticalResourceBase toMultidatasetIsReplacedBy(MultidatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesBy = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesBy = multidatasetVersionRepository.retrieveIsReplacedBy(source);
        } else {
            relatedResourceReplacesBy = multidatasetVersionRepository.retrieveIsReplacedByOnlyIfPublished(source);
        }
        return toResource(relatedResourceReplacesBy, selectedLanguages);
    }

    private ResourcesStatisticalResourceBase toMultidatasetIsPartOf(MultidatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResourceResult> relatedResourceIsPartOf = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceIsPartOf = multidatasetVersionRepository.retrieveIsPartOf(source);
        } else {
            relatedResourceIsPartOf = multidatasetVersionRepository.retrieveIsPartOfOnlyLastPublished(source);
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

    private MultidatasetData toMultidatasetData(MultidatasetVersion source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        MultidatasetData target = new MultidatasetData();
        target.setNodes(toMultidatasetNodes(source.getCubes(), selectedLanguages));
        return target;
    }

    private MultidatasetTable toMultidatasetNode(MultidatasetCube source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        MultidatasetTable target = new MultidatasetTable();
        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();
        target.setIdentifier(source.getIdentifier());
        target.setName(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getDescription(), selectedLanguages));

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            if (source.getDataset() != null) {
                DatasetVersion dataset = datasetVersionRepository.retrieveLastVersion(source.getDatasetUrn());
                target.setDataset(datasetsDo2RestMapper.toResourceAsLatest(dataset, selectedLanguages));
            } else if (source.getQuery() != null) {
                QueryVersion query = queryVersionRepository.retrieveLastVersion(source.getQueryUrn());
                target.setQuery(queriesDo2RestMapper.toResource(query, selectedLanguages, null, externalRestObjectsMapper));
            }
        } else {
            if (source.getDataset() != null) {
                DatasetVersion dataset = datasetVersionRepository.retrieveLastPublishedVersion(source.getDatasetUrn());
                target.setDataset(datasetsDo2RestMapper.toResourceAsLatest(dataset, selectedLanguages));
            } else if (source.getQuery() != null) {
                QueryVersion query = queryVersionRepository.retrieveLastPublishedVersion(source.getQueryUrn());
                target.setQuery(queriesDo2RestMapper.toResource(query, selectedLanguages, null, externalRestObjectsMapper));
            }
        }

        return target;
    }

    private MultidatasetNodes toMultidatasetNodes(List<MultidatasetCube> sources, List<String> selectedLanguages) throws MetamacException {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        MultidatasetNodes target = new MultidatasetNodes();
        for (MultidatasetCube source : sources) {
            target.getNodes().add(toMultidatasetNode(source, selectedLanguages));
        }
        return target;
    }

    private ResourceLink toMultidatasetParentLink(MultidatasetVersion source) {
        return toMultidatasetsSelfLink(null);
    }

    private ChildLinks toMultidatasetChildLinks(MultidatasetVersion source) {
        // nothing
        return null;
    }

    private ResourceLink toMultidatasetsSelfLink(String agencyID) {
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_MULTIDATASETS, toMultidatasetsLink(agencyID));
    }

    private String toMultidatasetsLink(String agencyID) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_MULTIDATASETS;
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, null, null);
    }

    private ResourceLink toMultidatasetSelfLink(MultidatasetVersion source) {
        String agencyID = source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        String resourceID = source.getLifeCycleStatisticalResource().getCode();
        return toMultidatasetSelfLink(agencyID, resourceID);
    }

    private ResourceLink toMultidatasetSelfLink(RelatedResourceResult source) {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        return toMultidatasetSelfLink(agencyID, resourceID);
    }

    private ResourceLink toMultidatasetSelfLink(String agencyID, String resourceID) {
        String link = toMultidatasetLink(agencyID, resourceID);
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_MULTIDATASET, link);
    }

    private String toMultidatasetLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_MULTIDATASETS;

        // do not return version
        String version = null;

        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    /**
     * Retrieve urn to API, without version
     */
    private String toMultidatasetUrn(MultidatasetVersion source) {
        return toMultidatasetUrn(source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested(), source.getLifeCycleStatisticalResource().getCode());
    }
    private String toMultidatasetUrn(String maintainerNestedCode, String code) {
        // global urn without version
        return generateSiemacStatisticalResourceMultidatasetUrn(new String[]{maintainerNestedCode}, code);
    }

}