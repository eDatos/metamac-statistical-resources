package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.collection;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionUrn;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.containsField;

import java.math.BigInteger;
import java.util.ArrayList;
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
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Collection;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.CollectionMetadata;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.utils.PublicationsUtils;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources.ExternalRestObjectsMapper;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Chapter;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CollectionData;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CollectionNode;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CollectionNodes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Collections;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Table;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;
import org.siemac.metamac.statistical_resources.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.internal.service.utils.HtmlLinkUtil;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.multidataset.MultidatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.query.QueriesDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CollectionsDo2RestMapperV10Impl implements CollectionsDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10            commonDo2RestMapper;

    @Autowired
    private DatasetsDo2RestMapperV10          datasetsDo2RestMapper;

    @Autowired
    private DatasetVersionRepository          datasetVersionRepository;

    @Autowired
    private QueryVersionRepository            queryVersionRepository;

    @Autowired
    private MultidatasetVersionRepository     multidatasetVersionRepository;

    @Autowired
    private QueriesDo2RestMapperV10           queriesDo2RestMapper;

    @Autowired
    private MultidatasetsDo2RestMapperV10     multidatasetsDo2RestMapper;

    @Autowired
    private PublicationVersionRepository      publicationVersionRepository;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    private static final Logger               logger = LoggerFactory.getLogger(CollectionsDo2RestMapperV10.class);

    @Override
    public Collections toCollections(PagedResult<PublicationVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            Set<String> parsedFields) throws MetamacException {

        Collections targets = new Collections();
        targets.setKind(StatisticalResourcesRestInternalConstants.KIND_COLLECTIONS);

        // Pagination
        String baseLink = toCollectionsLink(agencyID);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        // Values
        for (PublicationVersion source : sources.getValues()) {
            ResourceWithStatisticalOperation target = toResource(source, selectedLanguages, parsedFields, externalRestObjectsMapper);
            targets.getCollections().add(target);
        }
        return targets;
    }

    @Override
    public Collection toCollection(PublicationVersion source, List<String> selectedLanguages, Set<String> fields) throws Exception {
        if (source == null) {
            return null;
        }
        Collection target = new Collection();
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_COLLECTION);
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(toCollectionUrn(source));
        target.setSelfLink(toCollectionSelfLink(source));
        target.setManagementAppLink(toPublicationVersionManagementApplicationLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toCollectionParentLink(source));
        target.setChildLinks(toCollectionChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.COLLECTION, source.getLifeCycleStatisticalResource(), configurationService, false));

        boolean includeMetadata = !containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_EXCLUDE_METADATA);
        boolean includeData = !containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_EXCLUDE_DATA);
        if (includeMetadata) {
            target.setMetadata(toCollectionMetadata(source, selectedLanguages));
        }
        if (includeData) {
            target.setData(toCollectionData(source, selectedLanguages));
        }
        boolean includeKeywords = containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_INCLUDE_KEYWORDS);
        if (includeKeywords) {
            target.setKeywords(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getKeywords(), selectedLanguages));
        }
        return target;
    }

    @Override
    public ResourceWithStatisticalOperation toResource(PublicationVersion source, List<String> selectedLanguages, Set<String> parsedFields, ExternalRestObjectsMapper externalRestObjectsMapper)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(toCollectionUrn(source));
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_COLLECTION);
        target.setSelfLink(toCollectionSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setManagementAppLink(toPublicationVersionManagementApplicationLink(source));
        boolean includeStatisticalOperation = containsField(parsedFields, StatisticalResourcesRestInternalConstants.FIELD_INCLUDE_STATISTICAL_OPERATION);
        if (includeStatisticalOperation) {
            target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getSiemacMetadataStatisticalResource().getStatisticalOperation(), selectedLanguages,
                    externalRestObjectsMapper));
        }
        boolean includeDescription = containsField(parsedFields, StatisticalResourcesRestInternalConstants.FIELD_INCLUDE_DESCRIPTION);
        if (includeDescription) {
            target.setDescription(commonDo2RestMapper.toInternationalString(source.getLifeCycleStatisticalResource().getDescription(),selectedLanguages));
        }
        target.setVisualizerHtmlLink(HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.COLLECTION, source.getLifeCycleStatisticalResource(), configurationService, false));
        target.setNewnessUntilDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getNewnessUntilDate()));
        target.setFeaturedUntilDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getFeaturedUntilDate()));

        return target;
    }

    @Override
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.PUBLICATION_VERSION.equals(source.getType())) {
            logger.error("RelatedResource unsupported: " + source.getType());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setId(source.getCode());
        target.setUrn(toCollectionUrn(source.getMaintainerNestedCode(), source.getCode()));
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_COLLECTION);
        target.setSelfLink(toCollectionSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        target.setVisualizerHtmlLink(toVisualizerHtmlLink(source));
        target.setManagementAppLink(toPublicationVersionManagementApplicationLink(source));
        return target;
    }

    private String toVisualizerHtmlLink(RelatedResourceResult source) throws MetamacException {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        return HtmlLinkUtil.getVisualizerHtmlLink(StatisticalResourceTypeEnum.COLLECTION, agencyID, resourceID, null, configurationService, false);
    }

    @Override
    public ResourceLink toCollectionSelfLink(LifeCycleStatisticalResourceDto source) {
        String agencyID = source.getMaintainer().getCodeNested();
        String resourceID = source.getCode();
        return toCollectionSelfLink(agencyID, resourceID);
    }

    @Override
    public ResourceLink toCollectionSelfLink(LifeCycleStatisticalResourceBaseDto source) {
        String agencyID = source.getMaintainerCodeNested();
        String resourceID = source.getCode();
        return toCollectionSelfLink(agencyID, resourceID);
    }

    private CollectionMetadata toCollectionMetadata(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        CollectionMetadata target = new CollectionMetadata();
        target.setFormatExtentResources(source.getFormatExtentResources());
        target.setReplaces(toCollectionReplaces(source, selectedLanguages));
        target.setIsReplacedBy(toCollectionIsReplacedBy(source, selectedLanguages));
        target.setHasPart(toCollectionHasPart(source, selectedLanguages));
        commonDo2RestMapper.toMetadataStatisticalResource(source.getSiemacMetadataStatisticalResource(), target, selectedLanguages);

        target.setConformsToInternal(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getConformsToInternal(), selectedLanguages));
        target.setProcStatus(commonDo2RestMapper.toProcStatusType(source.getSiemacMetadataStatisticalResource().getProcStatus(), selectedLanguages));
        target.setCreationDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getCreationDate()));
        target.setCreationUser(source.getSiemacMetadataStatisticalResource().getCreationUser());
        target.setProductionValidationDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getCreationDate()));
        target.setProductionValidationUser(source.getSiemacMetadataStatisticalResource().getPublicationUser());
        target.setDiffusionValidationDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getSiemacMetadataStatisticalResource().getDiffusionValidationUser());
        target.setRejectValidationDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getRejectValidationDate()));
        target.setRejectValidationUser(source.getSiemacMetadataStatisticalResource().getRejectValidationUser());
        target.setPublicationDate(commonDo2RestMapper.toDate(source.getSiemacMetadataStatisticalResource().getPublicationDate()));
        target.setPublicationUser(source.getSiemacMetadataStatisticalResource().getPublicationUser());
        target.setNextVersionDate(commonDo2RestMapper.toSdmxObservationalTimePeriod(source.getSiemacMetadataStatisticalResource().getNextVersionDate(), selectedLanguages));

        return target;
    }

    private ResourceStatisticalResourceBase toCollectionReplaces(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResource replaces = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            replaces = source.getSiemacMetadataStatisticalResource().getReplaces();
        } else {
            replaces = source.getSiemacMetadataStatisticalResource().getReplaces();
            // Check if the publication used in "replaces" is published
            if (replaces != null) {
                String urn = replaces.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn();
                try {
                    publicationVersionRepository.retrieveByUrnPublished(urn);
                } catch (MetamacException e) {
                    if (!e.getExceptionItems().isEmpty() && e.getExceptionItems().iterator().next().getCode().equals(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND.getCode())) {
                        return null;
                    }
                }
            }
        }
        return commonDo2RestMapper.toResource(replaces, selectedLanguages);
    }

    private ResourceStatisticalResourceBase toCollectionIsReplacedBy(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesBy = null;

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesBy = publicationVersionRepository.retrieveIsReplacedBy(source);
        } else {
            relatedResourceReplacesBy = publicationVersionRepository.retrieveIsReplacedByOnlyLastPublished(source);
        }
        return toResource(relatedResourceReplacesBy, selectedLanguages);
    }

    private ResourcesStatisticalResourceBase toCollectionHasPart(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResource> hasPart = null;
        List<String> urls = new ArrayList<String>();

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            hasPart = PublicationsUtils.computeHasPart(source); // Is necessary to calculate the has part
        } else {
            hasPart = source.getHasPart(); // All has part resources are already published
        }

        urls = PublicationsUtils.computeUrlsHasPart(source);
        ResourcesStatisticalResourceBase resources = new ResourcesStatisticalResourceBase();
        ResourcesStatisticalResourceBase hasPartResources = commonDo2RestMapper.toResources(hasPart, selectedLanguages);
        if (hasPartResources != null) {
            resources.getResources().addAll(hasPartResources.getResources());
        }
        ResourcesStatisticalResourceBase urlResources = commonDo2RestMapper.toUrlResources(urls);
        if (urlResources != null) {
            resources.getResources().addAll(urlResources.getResources());
        }
        resources.setTotal(BigInteger.valueOf(resources.getResources().size()));
        return resources;
    }

    private CollectionData toCollectionData(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        CollectionData target = new CollectionData();
        target.setNodes(toCollectionNodes(source.getChildrenFirstLevel(), selectedLanguages));
        return target;
    }

    private CollectionNode toCollectionNode(ElementLevel source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        CollectionNode target = null;
        if (source.isChapter()) {
            target = toCollectionNode(source.getChapter(), selectedLanguages);
        } else if (source.isCube()) {
            target = toCollectionNode(source.getCube(), selectedLanguages);
        }
        return target;
    }

    private Chapter toCollectionNode(org.siemac.metamac.statistical.resources.core.publication.domain.Chapter source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        Chapter target = new Chapter();
        target.setName(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getDescription(), selectedLanguages));

        target.setNodes(toCollectionNodes(source.getElementLevel().getChildren(), selectedLanguages));
        return target;
    }

    private Table toCollectionNode(Cube source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        Table target = new Table();
        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        target.setName(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getDescription(), selectedLanguages));

        if (StatisticalResourcesRestInternalConstants.IS_INTERNAL_API) {
            if (source.getDataset() != null) {
                DatasetVersion dataset = datasetVersionRepository.retrieveLastVersion(source.getDatasetUrn());
                target.setDataset(datasetsDo2RestMapper.toResourceAsLatest(dataset, selectedLanguages));
            } else if (source.getQuery() != null) {
                QueryVersion query = queryVersionRepository.retrieveLastVersion(source.getQueryUrn());
                target.setQuery(queriesDo2RestMapper.toResource(query, selectedLanguages, null, externalRestObjectsMapper));
            } else if (source.getMultidataset() != null) {
                MultidatasetVersion multidatasetVersion = multidatasetVersionRepository.retrieveLastVersion(source.getMultidatasetUrn());
                target.setMultidataset(multidatasetsDo2RestMapper.toResource(multidatasetVersion, selectedLanguages, null, externalRestObjectsMapper));
            } else if (source.getPublication() != null) {
                PublicationVersion publicationVersion = publicationVersionRepository.retrieveLastVersion(source.getPublicationUrn());
                target.setPublication(toResource(publicationVersion, selectedLanguages, null, externalRestObjectsMapper));
            } else if (source.getUrl() != null) {
                target.setUrl(toResource(source.getUrl(), selectedLanguages));
            }
        } else {
            if (source.getDataset() != null) {
                DatasetVersion dataset = datasetVersionRepository.retrieveLastPublishedVersion(source.getDatasetUrn());
                target.setDataset(datasetsDo2RestMapper.toResourceAsLatest(dataset, selectedLanguages));
            } else if (source.getQuery() != null) {
                QueryVersion query = queryVersionRepository.retrieveLastPublishedVersion(source.getQueryUrn());
                target.setQuery(queriesDo2RestMapper.toResource(query, selectedLanguages, null, externalRestObjectsMapper));
            } else if (source.getMultidataset() != null) {
                MultidatasetVersion multidatasetVersion = multidatasetVersionRepository.retrieveLastPublishedVersion(source.getMultidatasetUrn());
                target.setMultidataset(multidatasetsDo2RestMapper.toResource(multidatasetVersion, selectedLanguages, null, externalRestObjectsMapper));
            } else if (source.getPublication() != null) {
                PublicationVersion publicationVersion = publicationVersionRepository.retrieveLastPublishedVersion(source.getPublicationUrn());
                target.setPublication(toResource(publicationVersion, selectedLanguages, null, externalRestObjectsMapper));
            } else if (source.getUrl() != null) {
                target.setUrl(toResource(source.getUrl(), selectedLanguages));
            }
        }

        return target;
    }

    @Override
    public ResourceStatisticalResourceBase toResource(String url, List<String> selectedLanguages) {
        if (url == null) {
            return null;
        }
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setVisualizerHtmlLink(url);
        target.setKind(StatisticalResourcesRestInternalConstants.KIND_URL);
        return target;
    }

    private CollectionNodes toCollectionNodes(List<ElementLevel> sources, List<String> selectedLanguages) throws MetamacException {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        CollectionNodes target = new CollectionNodes();
        for (ElementLevel source : sources) {
            target.getNodes().add(toCollectionNode(source, selectedLanguages));
        }
        return target;
    }

    private ResourceLink toCollectionParentLink(PublicationVersion source) {
        return toCollectionsSelfLink(null);
    }

    private ChildLinks toCollectionChildLinks(PublicationVersion source) {
        // nothing
        return null;
    }

    private ResourceLink toCollectionsSelfLink(String agencyID) {
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_COLLECTIONS, toCollectionsLink(agencyID));
    }

    private String toCollectionsLink(String agencyID) {
        String resourceSubpath = StatisticalResourcesRestInternalConstants.LINK_SUBPATH_COLLECTIONS;
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, null, null);
    }

    private ResourceLink toCollectionSelfLink(PublicationVersion source) {
        String agencyID = source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        String resourceID = source.getLifeCycleStatisticalResource().getCode();
        return toCollectionSelfLink(agencyID, resourceID);
    }

    private ResourceLink toCollectionSelfLink(RelatedResourceResult source) {
        String agencyID = source.getMaintainerNestedCode();
        String resourceID = source.getCode();
        return toCollectionSelfLink(agencyID, resourceID);
    }

    private ResourceLink toCollectionSelfLink(String agencyID, String resourceID) {
        String link = toCollectionLink(agencyID, resourceID);
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestInternalConstants.KIND_COLLECTION, link);
    }

    private String toCollectionLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestInternalConstants.LINK_SUBPATH_COLLECTIONS;
        String version = null; // do not return version
        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    /**
     * Retrieve urn to API, without version
     */
    private String toCollectionUrn(PublicationVersion source) {
        return toCollectionUrn(source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested(), source.getLifeCycleStatisticalResource().getCode());
    }
    private String toCollectionUrn(String maintainerNestedCode, String code) {
        return generateSiemacStatisticalResourceCollectionUrn(new String[]{maintainerNestedCode}, code); // global urn without version
    }

    private String toPublicationVersionManagementApplicationLink(PublicationVersion source) {
        return commonDo2RestMapper.getInternalWebApplicationNavigation().buildPublicationVersionUrl(source);
    }

    private String toPublicationVersionManagementApplicationLink(RelatedResourceResult source) {
        return commonDo2RestMapper.getInternalWebApplicationNavigation().buildRelatedResourcePublicationVersionUrl(source);
    }
}