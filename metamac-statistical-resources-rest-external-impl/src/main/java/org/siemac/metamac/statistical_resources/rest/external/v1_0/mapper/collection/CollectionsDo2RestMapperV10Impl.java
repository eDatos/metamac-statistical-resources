package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.collection;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionUrn;
import static org.siemac.metamac.core.common.util.rest.RequestUtil.containsField;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Chapter;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Collection;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CollectionData;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CollectionMetadata;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CollectionNode;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.CollectionNodes;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Collections;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Table;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.utils.PublicationsUtils;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceWithStatisticalOperation;
import org.siemac.metamac.statistical_resources.rest.external.StatisticalResourcesRestExternalConstants;
import org.siemac.metamac.statistical_resources.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base.CommonDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.multidataset.MultidatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query.QueriesDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CollectionsDo2RestMapperV10Impl implements CollectionsDo2RestMapperV10 {

    @Autowired
    private CommonDo2RestMapperV10        commonDo2RestMapper;

    @Autowired
    private DatasetsDo2RestMapperV10      datasetsDo2RestMapper;

    @Autowired
    private DatasetVersionRepository      datasetVersionRepository;

    @Autowired
    private QueryVersionRepository        queryVersionRepository;

    @Autowired
    private MultidatasetVersionRepository multidatasetVersionRepository;

    @Autowired
    private QueriesDo2RestMapperV10       queriesDo2RestMapper;

    @Autowired
    private MultidatasetsDo2RestMapperV10 multidatasetsDo2RestMapper;

    @Autowired
    private PublicationVersionRepository  publicationVersionRepository;

    private static final Logger           logger = LoggerFactory.getLogger(CollectionsDo2RestMapperV10.class);

    @Override
    public Collections toCollections(PagedResult<PublicationVersion> sources, String agencyID, String resourceID, String query, String orderBy, Integer limit, List<String> selectedLanguages,
        Set<String> parsedFields) {

        Collections targets = new Collections();
        targets.setKind(StatisticalResourcesRestExternalConstants.KIND_COLLECTIONS);

        // Pagination
        String baseLink = toCollectionsLink(agencyID);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        // Values
        for (PublicationVersion source : sources.getValues()) {
            ResourceWithStatisticalOperation target = toResource(source, selectedLanguages, parsedFields);
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
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_COLLECTION);
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(toCollectionUrn(source));
        target.setSelfLink(toCollectionSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getDescription(), selectedLanguages));
        target.setParentLink(toCollectionParentLink(source));
        target.setChildLinks(toCollectionChildLinks(source));
        target.setSelectedLanguages(commonDo2RestMapper.toLanguages(selectedLanguages));
        boolean includeMetadata = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_METADATA);
        boolean includeData = !containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_EXCLUDE_DATA);
        if (includeMetadata) {
            target.setMetadata(toCollectionMetadata(source, selectedLanguages));
        }
        if (includeData) {
            target.setData(toCollectionData(source, selectedLanguages));
        }
        boolean includeKeywords = containsField(fields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_KEYWORDS);
        if (includeKeywords) {
            target.setKeywords(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getKeywords(), selectedLanguages));
        }
        return target;
    }

    @Override
    public ResourceWithStatisticalOperation toResource(PublicationVersion source, List<String> selectedLanguages, Set<String> parsedFields) {
        if (source == null) {
            return null;
        }
        ResourceWithStatisticalOperation target = new ResourceWithStatisticalOperation();
        target.setId(source.getSiemacMetadataStatisticalResource().getCode());
        target.setUrn(toCollectionUrn(source));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_COLLECTION);
        target.setSelfLink(toCollectionSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getSiemacMetadataStatisticalResource().getTitle(), selectedLanguages));
        boolean includeStatisticalOperation = containsField(parsedFields, StatisticalResourcesRestExternalConstants.FIELD_INCLUDE_STATISTICAL_OPERATION);
        if (includeStatisticalOperation) {
            target.setStatisticalOperation(commonDo2RestMapper.toResourceExternalItemStatisticalOperations(source.getSiemacMetadataStatisticalResource().getStatisticalOperation(), selectedLanguages));
        }
        return target;
    }

    @Override
    public Resource toResource(RelatedResourceResult source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        if (!TypeRelatedResourceEnum.PUBLICATION_VERSION.equals(source.getType())) {
            logger.error("RelatedResource unsupported: " + source.getType());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        Resource target = new Resource();
        target.setId(source.getCode());
        target.setUrn(toCollectionUrn(source.getMaintainerNestedCode(), source.getCode()));
        target.setKind(StatisticalResourcesRestExternalConstants.KIND_COLLECTION);
        target.setSelfLink(toCollectionSelfLink(source));
        target.setName(commonDo2RestMapper.toInternationalString(source.getTitle(), selectedLanguages));
        return target;
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
        return target;
    }

    private Resource toCollectionReplaces(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        // There is no need to check if the replaced resource is published. The "replaces" metadata is always filled with a published collection.
        RelatedResource replaces = source.getSiemacMetadataStatisticalResource().getReplaces();;
        return commonDo2RestMapper.toResource(replaces, selectedLanguages);
    }

    private Resource toCollectionIsReplacedBy(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        RelatedResourceResult relatedResourceReplacesBy = null;

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            relatedResourceReplacesBy = publicationVersionRepository.retrieveIsReplacedBy(source);
        } else {
            relatedResourceReplacesBy = publicationVersionRepository.retrieveIsReplacedByOnlyIfPublished(source);
        }
        return toResource(relatedResourceReplacesBy, selectedLanguages);
    }

    private Resources toCollectionHasPart(PublicationVersion source, List<String> selectedLanguages) throws MetamacException {
        List<RelatedResource> hasPart = null;
        List<String> urls = new ArrayList<String>();
        
        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            // Is necessary to calculate the has part
            hasPart = PublicationsUtils.computeHasPart(source);
        } else {
            // All has part resources are already published
            hasPart = source.getHasPart();
        }
        urls = PublicationsUtils.computeUrlsHasPart(source);
        
        Resources resources = commonDo2RestMapper.toResources(hasPart, selectedLanguages);
        Resources urlResources = commonDo2RestMapper.toUrlResources(urls);
        if (!urlResources.getResources().isEmpty()) {
            resources.getResources().addAll(urlResources.getResources());  
        }
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
        target.setName(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getTitle(), selectedLanguages));
        target.setDescription(commonDo2RestMapper.toInternationalString(source.getNameableStatisticalResource().getDescription(), selectedLanguages));

        if (StatisticalResourcesRestExternalConstants.IS_INTERNAL_API) {
            if (source.getDataset() != null) {
                DatasetVersion dataset = datasetVersionRepository.retrieveLastVersion(source.getDatasetUrn());
                target.setDataset(datasetsDo2RestMapper.toResourceAsLatest(dataset, selectedLanguages));
            } else if (source.getQuery() != null) {
                QueryVersion query = queryVersionRepository.retrieveLastVersion(source.getQueryUrn());
                target.setQuery(queriesDo2RestMapper.toResource(query, selectedLanguages, null));
            } else if (source.getMultidataset() != null) {
                MultidatasetVersion multidatasetVersion = multidatasetVersionRepository.retrieveLastVersion(source.getMultidatasetUrn());
                target.setMultidataset(multidatasetsDo2RestMapper.toResource(multidatasetVersion, selectedLanguages, null));
            }
        } else {
            if (source.getDataset() != null) {
                DatasetVersion dataset = datasetVersionRepository.retrieveLastPublishedVersion(source.getDatasetUrn());
                target.setDataset(datasetsDo2RestMapper.toResourceAsLatest(dataset, selectedLanguages));
            } else if (source.getQuery() != null) {
                QueryVersion query = queryVersionRepository.retrieveLastPublishedVersion(source.getQueryUrn());
                target.setQuery(queriesDo2RestMapper.toResource(query, selectedLanguages, null));
            } else if (source.getMultidataset() != null) {
                MultidatasetVersion multidatasetVersion = multidatasetVersionRepository.retrieveLastPublishedVersion(source.getMultidatasetUrn());
                target.setMultidataset(multidatasetsDo2RestMapper.toResource(multidatasetVersion, selectedLanguages, null));
            }
        }

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
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_COLLECTIONS, toCollectionsLink(agencyID));
    }

    private String toCollectionsLink(String agencyID) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_COLLECTIONS;
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
        return commonDo2RestMapper.toResourceLink(StatisticalResourcesRestExternalConstants.KIND_COLLECTION, link);
    }

    private String toCollectionLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestExternalConstants.LINK_SUBPATH_COLLECTIONS;

        // do not return version
        String version = null;

        return commonDo2RestMapper.toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    /**
     * Retrieve urn to API, without version
     */
    private String toCollectionUrn(PublicationVersion source) {
        return toCollectionUrn(source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested(), source.getLifeCycleStatisticalResource().getCode());
    }
    private String toCollectionUrn(String maintainerNestedCode, String code) {
        // global urn without version
        return generateSiemacStatisticalResourceCollectionUrn(new String[]{maintainerNestedCode}, code);
    }

}