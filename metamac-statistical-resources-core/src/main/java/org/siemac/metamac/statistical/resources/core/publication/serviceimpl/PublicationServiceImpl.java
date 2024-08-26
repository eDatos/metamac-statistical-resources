package org.siemac.metamac.statistical.resources.core.publication.serviceimpl;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionUrn;
import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceCollectionVersionUrn;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.base.components.SiemacStatisticalResourceGeneratedCode;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.utils.FillMetadataForCreateResourceUtils;
import org.siemac.metamac.statistical.resources.core.base.validators.ProcStatusValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetProperties;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetService;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.geocache.serviceapi.CacheService;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetProperties;
import org.siemac.metamac.statistical.resources.core.multidataset.serviceapi.MultidatasetService;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationProperties;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionProperties;
import org.siemac.metamac.statistical.resources.core.publication.serviceapi.validators.PublicationServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.publication.utils.ElementLevelComparator;
import org.siemac.metamac.statistical.resources.core.publication.utils.structure.Element;
import org.siemac.metamac.statistical.resources.core.publication.utils.structure.PublicationStructure;
import org.siemac.metamac.statistical.resources.core.publication.utils.structure.PublicationStructureTSVProcessor;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryProperties;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryService;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of PublicationService.
 */
@Service("publicationService")
public class PublicationServiceImpl extends PublicationServiceImplBase {

    @Autowired
    private IdentifiableStatisticalResourceRepository identifiableStatisticalResourceRepository;

    @Autowired
    private SiemacStatisticalResourceGeneratedCode    siemacStatisticalResourceGeneratedCode;

    @Autowired
    private PublicationServiceInvocationValidator     publicationServiceInvocationValidator;

    @Autowired
    private PublicationStructureTSVProcessor          publicationStructureTSVProcessor;

    @Autowired
    private DatasetService                            datasetService;

    @Autowired
    private QueryService                              queryService;

    @Autowired
    private MultidatasetService                       multidatasetService;

    @Autowired
    private RelatedResourceRepository                 relatedResourceRepository;

    @Autowired
    CacheService                                      cacheService;

    public PublicationServiceImpl() {
    }

    @Override
    public PublicationVersion createPublicationVersion(ServiceContext ctx, PublicationVersion publicationVersion, ExternalItem statisticalOperation) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkCreatePublicationVersion(ctx, publicationVersion, statisticalOperation);

        // Create publication
        Publication publication = new Publication();
        fillMetadataForCreatePublication(publication, statisticalOperation);

        // Fill metadata
        fillMetadataForCreatePublicationVersion(publicationVersion, statisticalOperation, ctx);

        // Add version to publication and Save version
        publicationVersion.setPublication(publication);
        assignCodeAndSavePublicationVersion(publication, publicationVersion);

        // Retrieve publicationVersion
        publicationVersion = getPublicationVersionRepository().retrieveByUrn(publicationVersion.getSiemacMetadataStatisticalResource().getUrn());

        return publicationVersion;
    }

    @Override
    public PublicationVersion updatePublicationVersion(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkUpdatePublicationVersion(ctx, publicationVersion);

        // Check status
        ProcStatusValidator.checkStatisticalResourceCanBeEdited(publicationVersion);

        // It's not necessary recreate URN because the code is assigned in the create service and never change.
        identifiableStatisticalResourceRepository.checkDuplicatedUrn(publicationVersion.getSiemacMetadataStatisticalResource());

        publicationVersion = getPublicationVersionRepository().save(publicationVersion);
        return publicationVersion;
    }

    @Override
    public PublicationVersion retrievePublicationVersionByUrn(ServiceContext ctx, String publicationVersionUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkRetrievePublicationVersionByUrn(ctx, publicationVersionUrn);

        // Retrieve
        return getPublicationVersionRepository().retrieveByUrn(publicationVersionUrn);
    }

    @Override
    public PublicationVersion retrieveLatestPublicationVersionByPublicationUrn(ServiceContext ctx, String publicationUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkRetrieveLatestPublicationVersionByPublicationUrn(ctx, publicationUrn);

        // Retrieve
        return getPublicationVersionRepository().retrieveLastVersion(publicationUrn);
    }

    @Override
    public PublicationVersion retrieveLatestPublishedPublicationVersionByPublicationUrn(ServiceContext ctx, String publicationUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkRetrieveLatestPublishedPublicationVersionByPublicationUrn(ctx, publicationUrn);

        // Retrieve
        return getPublicationVersionRepository().retrieveLastPublishedVersion(publicationUrn);
    }

    @Override
    public List<PublicationVersion> retrievePublicationVersions(ServiceContext ctx, String publicationVersionUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkRetrievePublicationVersions(ctx, publicationVersionUrn);

        // Retrieve
        List<PublicationVersion> publicationVersions = getPublicationVersionRepository().retrieveByUrn(publicationVersionUrn).getPublication().getVersions();

        return publicationVersions;
    }

    @Override
    public PagedResult<PublicationVersion> findPublicationVersionsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkFindPublicationVersionsByCondition(ctx, conditions, pagingParameter);

        // Find
        conditions = CriteriaUtils.initConditions(conditions, PublicationVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        PagedResult<PublicationVersion> publicationVersionPagedResult = getPublicationVersionRepository().findByCondition(conditions, pagingParameter);
        return publicationVersionPagedResult;
    }

    @Override
    public void deletePublicationVersion(ServiceContext ctx, String publicationVersionUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkDeletePublicationVersion(ctx, publicationVersionUrn);

        // Retrieve version to delete
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, publicationVersionUrn);

        // Check can be deleted
        ProcStatusValidator.checkStatisticalResourceCanBeDeleted(publicationVersion);

        checkCanPublicationVersionBeDeleted(publicationVersion);

        updateReplacedResourceIsReplacedByResource(publicationVersion);

        if (StatisticalResourcesVersionUtils.isInitialVersion(publicationVersion.getSiemacMetadataStatisticalResource().getVersionLogic())) {
            Publication publication = publicationVersion.getPublication();
            getPublicationRepository().delete(publication);
        } else {
            // Previous version
            updateReplacedVersionIsReplacedByVersion(publicationVersion);

            // Delete version
            Publication publication = publicationVersion.getPublication();
            publication.getVersions().remove(publicationVersion);
            getPublicationVersionRepository().delete(publicationVersion);
        }
    }

    private void updateReplacedVersionIsReplacedByVersion(PublicationVersion publicationVersion) {
        RelatedResource previousResource = publicationVersion.getSiemacMetadataStatisticalResource().getReplacesVersion();
        if (previousResource.getPublicationVersion() != null) {
            PublicationVersion previousVersion = previousResource.getPublicationVersion();
            previousVersion.getSiemacMetadataStatisticalResource().setLastVersion(true);
            RelatedResource isReplacedByVersion = previousVersion.getSiemacMetadataStatisticalResource().getIsReplacedByVersion();
            relatedResourceRepository.delete(isReplacedByVersion);
            previousVersion.getSiemacMetadataStatisticalResource().setIsReplacedByVersion(null);
            getPublicationVersionRepository().save(previousVersion);
        }
    }

    private void updateReplacedResourceIsReplacedByResource(PublicationVersion publicationVersion) {
        RelatedResource previousResource = publicationVersion.getSiemacMetadataStatisticalResource().getReplaces();
        if (previousResource != null && previousResource.getPublicationVersion() != null) {
            PublicationVersion previousVersion = previousResource.getPublicationVersion();
            RelatedResource isReplacedBy = previousVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
            relatedResourceRepository.delete(isReplacedBy);
            previousVersion.getSiemacMetadataStatisticalResource().setIsReplacedBy(null);
            getPublicationVersionRepository().save(previousVersion);
        }
    }

    private void checkCanPublicationVersionBeDeleted(PublicationVersion publicationVersion) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        RelatedResource resourcesIsReplacedBy = publicationVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        if (resourcesIsReplacedBy != null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_IS_REPLACED_BY_OTHER_RESOURCE,
                    resourcesIsReplacedBy.getPublicationVersion().getLifeCycleStatisticalResource().getUrn()));
        }

        if (exceptionItems.size() > 0) {
            MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_CANT_BE_DELETED, publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
            item.setExceptionItems(exceptionItems);
            throw new MetamacException(Arrays.asList(item));
        }
    }

    // ------------------------------------------------------------------------
    // STRUCTURE
    // ------------------------------------------------------------------------

    @Override
    public PublicationVersion importPublicationVersionStructure(ServiceContext ctx, String publicationVersionUrn, java.net.URL fileURL, String language) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkImportPublicationVersionStructure(ctx, publicationVersionUrn, fileURL, language);

        PublicationStructure publicationStructure;
        // Parse
        try {
            String encoding = StringUtils.isEmpty(System.getProperty("file.encoding")) ? "UTF-8" : System.getProperty("file.encoding");
            publicationStructure = publicationStructureTSVProcessor.parse(new File(URLDecoder.decode(fileURL.getPath(), encoding)));
        } catch (UnsupportedEncodingException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.FILE_ENCODING_ERROR).withMessageParameters(fileURL.getPath()).build();
        }

        // Save structure
        if (StringUtils.isNotBlank(publicationStructure.getPublicationTitle())) {
            updatePublicationTitle(ctx, publicationVersionUrn, language, publicationStructure.getPublicationTitle());
        }

        Long orderInLevel = 1L;
        for (Element element : publicationStructure.getElements()) {
            savePublicationStructureElement(ctx, publicationVersionUrn, element, language, orderInLevel, null);
            orderInLevel++;
        }
        return retrievePublicationVersionByUrn(ctx, publicationVersionUrn);
    };

    private void updatePublicationTitle(ServiceContext ctx, String publicationVersionUrn, String language, String newTitle) throws MetamacException {
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, publicationVersionUrn);
        InternationalString title = publicationVersion.getSiemacMetadataStatisticalResource().getTitle();
        if (title.getLocales().contains(language)) {
            for (LocalisedString localisedString : title.getTexts()) {
                if (language.equals(localisedString.getLocale())) {
                    localisedString.setLabel(newTitle);
                }
            }
        } else {
            title.addText(new LocalisedString(language, newTitle));
        }
        updatePublicationVersion(ctx, publicationVersion);
    }

    private void savePublicationStructureElement(ServiceContext ctx, String publicationVersionUrn, Element element, String language, Long orderInLevel, ElementLevel parent) throws MetamacException {

        ElementLevel currentElementLevel = null;

        if (TypeRelatedResourceEnum.CHAPTER.equals(element.getType())) {

            Chapter chapter = createChapter(ctx, publicationVersionUrn, element, language, orderInLevel, parent);
            currentElementLevel = chapter.getElementLevel();

        } else if (TypeRelatedResourceEnum.CUBE.equals(element.getType())) {

            Cube cube = createCube(ctx, publicationVersionUrn, element, language, orderInLevel, parent);
            currentElementLevel = cube.getElementLevel();
        }

        Long subElementOrderInLevel = 1L;
        for (Element subelement : element.getElements()) {
            savePublicationStructureElement(ctx, publicationVersionUrn, subelement, language, subElementOrderInLevel, currentElementLevel);
            subElementOrderInLevel++;
        }
    }

    private Chapter createChapter(ServiceContext ctx, String publicationVersionUrn, Element element, String language, Long orderInLevel, ElementLevel parent) throws MetamacException {
        Chapter chapter = new Chapter();
        chapter.setNameableStatisticalResource(new NameableStatisticalResource());
        chapter.getNameableStatisticalResource().setTitle(new InternationalString(language, element.getTitle()));
        chapter.setElementLevel(new ElementLevel());
        chapter.getElementLevel().setChapter(chapter);
        chapter.getElementLevel().setOrderInLevel(orderInLevel);
        chapter.getElementLevel().setParent(parent);
        return createChapter(ctx, publicationVersionUrn, chapter);
    }

    private Cube createCube(ServiceContext ctx, String publicationVersionUrn, Element element, String language, Long orderInLevel, ElementLevel parent) throws MetamacException {
        Cube cube = new Cube();
        cube.setNameableStatisticalResource(new NameableStatisticalResource());
        cube.getNameableStatisticalResource().setTitle(new InternationalString(language, element.getTitle()));
        cube.setElementLevel(new ElementLevel());
        cube.getElementLevel().setCube(cube);
        cube.getElementLevel().setOrderInLevel(orderInLevel);
        cube.getElementLevel().setParent(parent);
        addRelatedResourceToCube(ctx, cube, element);
        return createCube(ctx, publicationVersionUrn, cube);
    }

    private void addRelatedResourceToCube(ServiceContext ctx, Cube cube, Element element) throws MetamacException {
        StatisticalResourceTypeEnum type = element.getRelatedResourceType();
        if (StatisticalResourceTypeEnum.DATASET.equals(type)) {
            Dataset dataset = retrieveDatasetByCode(ctx, element.getRelatedResourceCode(), element.getLineNumber());
            cube.setDataset(dataset);
        } else if (StatisticalResourceTypeEnum.QUERY.equals(type)) {
            Query query = retrieveQueryByCode(ctx, element.getRelatedResourceCode(), element.getLineNumber());
            cube.setQuery(query);
        } else if (StatisticalResourceTypeEnum.MULTIDATASET.equals(type)) {
            Multidataset multidataset = retrieveMultidatasetByCode(ctx, element.getRelatedResourceCode(), element.getLineNumber());
            cube.setMultidataset(multidataset);
        } else if (StatisticalResourceTypeEnum.COLLECTION.equals(type)) {
            Publication publication = retrievePublicationByCode(ctx, element.getRelatedResourceCode(), element.getLineNumber());
            cube.setPublication(publication);
        } else if (StatisticalResourceTypeEnum.URL.equals(type)) {
            cube.setUrl(element.getRelatedResourceCode());
        }
    }

    private Multidataset retrieveMultidatasetByCode(ServiceContext ctx, String code, int lineNumber) throws MetamacException {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Multidataset.class).withProperty(MultidatasetProperties.identifiableStatisticalResource().code()).eq(code)
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1, true);
        PagedResult<Multidataset> multidatasets = multidatasetService.findMultidatasetsByCondition(ctx, conditions, pagingParameter);
        if (multidatasets.getTotalRows() > 0) {
            return multidatasets.getValues().get(0);
        }
        throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_MULTIDATASET)
                .withMessageParameters(lineNumber, code).build();
    }

    private Publication retrievePublicationByCode(ServiceContext ctx, String code, int lineNumber) throws MetamacException {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Publication.class).withProperty(PublicationProperties.identifiableStatisticalResource().code()).eq(code)
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1, true);
        PagedResult<Publication> publications = findPublicationsByCondition(ctx, conditions, pagingParameter);
        if (publications.getTotalRows() > 0) {
            return publications.getValues().get(0);
        }
        throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_MULTIDATASET)
                .withMessageParameters(lineNumber, code).build();
    }

    private Dataset retrieveDatasetByCode(ServiceContext ctx, String code, int lineNumber) throws MetamacException {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Dataset.class).withProperty(DatasetProperties.identifiableStatisticalResource().code()).eq(code).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1, true);
        PagedResult<Dataset> datasets = datasetService.findDatasetsByCondition(ctx, conditions, pagingParameter);
        if (datasets.getTotalRows() > 0) {
            return datasets.getValues().get(0);
        }
        throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_DATASET).withMessageParameters(lineNumber, code)
                .build();
    }

    private Query retrieveQueryByCode(ServiceContext ctx, String code, int lineNumber) throws MetamacException {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Query.class).withProperty(QueryProperties.identifiableStatisticalResource().code()).eq(code).build();
        List<Query> queries = queryService.findQueriesByCondition(ctx, conditions);
        if (queries.size() > 0) {
            return queries.get(0);
        }
        throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_QUERY).withMessageParameters(lineNumber, code)
                .build();
    }

    // ------------------------------------------------------------------------
    // CHAPTERS
    // ------------------------------------------------------------------------

    @Override
    public Chapter createChapter(ServiceContext ctx, String publicationVersionUrn, Chapter chapter) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkCreateChapter(ctx, publicationVersionUrn, chapter);
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, publicationVersionUrn);
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(publicationVersion);

        // Fill metadata for create chapter
        fillMetadataForCreateChapter(chapter, publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation());

        // Create element level
        ElementLevel elementLevel = createChapterElementLevel(ctx, publicationVersion, chapter);

        return elementLevel.getChapter();
    }

    private Chapter fillMetadataForCreateChapter(Chapter chapter, ExternalItem statisticalOperation) {
        FillMetadataForCreateResourceUtils.fillMetadataForCreateNameableResource(chapter.getNameableStatisticalResource(), statisticalOperation);
        chapter.fillCodeAndUrn();
        return chapter;
    }

    @Override
    public Chapter updateChapter(ServiceContext ctx, Chapter chapter) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkUpdateChapter(ctx, chapter);
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(publicationVersion);

        updateLastUpdateMetadata(publicationVersion);

        // Save
        return getChapterRepository().save(chapter);
    }

    @Override
    public Chapter updateChapterLocation(ServiceContext ctx, String chapterUrn, String parentChapterUrn, Long orderInLevel) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkUpdateChapterLocation(ctx, chapterUrn, parentChapterUrn, orderInLevel);

        // Update location
        ElementLevel elementLevel = retrieveChapter(ctx, chapterUrn).getElementLevel();
        elementLevel = updateElementLevelLocation(ctx, elementLevel, parentChapterUrn, orderInLevel);

        return elementLevel.getChapter();
    }

    @Override
    public Chapter retrieveChapter(ServiceContext ctx, String chapterUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkRetrieveChapter(ctx, chapterUrn);

        // Retrieve
        Chapter chapter = getChapterRepository().retrieveChapterByUrn(chapterUrn);
        return chapter;

    }

    @Override
    public void deleteChapter(ServiceContext ctx, String chapterUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkDeleteChapter(ctx, chapterUrn);

        // Retrieve
        ElementLevel elementLevel = retrieveChapter(ctx, chapterUrn).getElementLevel();

        // Delete
        deleteElementLevel(ctx, elementLevel);
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    @Override
    public Cube createCube(ServiceContext ctx, String publicationVersionUrn, Cube cube) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkCreateCube(ctx, publicationVersionUrn, cube);
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, publicationVersionUrn);
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(publicationVersion);

        // Fill metadata for create cube
        fillMetadataForCreateCube(cube, publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation());

        // Create element level
        ElementLevel elementLevel = createCubeElementLevel(ctx, publicationVersion, cube);

        return elementLevel.getCube();
    }

    private Cube fillMetadataForCreateCube(Cube cube, ExternalItem statisticalOperation) {
        FillMetadataForCreateResourceUtils.fillMetadataForCreateNameableResource(cube.getNameableStatisticalResource(), statisticalOperation);
        cube.fillCodeAndUrn();
        return cube;
    }

    @Override
    public Cube updateCube(ServiceContext ctx, Cube cube) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkUpdateCube(ctx, cube);
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(publicationVersion);

        updateLastUpdateMetadata(publicationVersion);

        // Save
        return getCubeRepository().save(cube);
    }

    @Override
    public Cube updateCubeLocation(ServiceContext ctx, String cubeUrn, String parentChapterUrn, Long orderInLevel) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkUpdateCubeLocation(ctx, cubeUrn, parentChapterUrn, orderInLevel);

        // Update location
        ElementLevel elementLevel = retrieveCube(ctx, cubeUrn).getElementLevel();
        elementLevel = updateElementLevelLocation(ctx, elementLevel, parentChapterUrn, orderInLevel);

        return elementLevel.getCube();
    }

    @Override
    public Cube retrieveCube(ServiceContext ctx, String cubeUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkRetrieveCube(ctx, cubeUrn);

        // Retrieve
        return getCubeRepository().retrieveCubeByUrn(cubeUrn);
    }

    @Override
    public void deleteCube(ServiceContext ctx, String cubeUrn) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkDeleteCube(ctx, cubeUrn);

        // Retrieve
        ElementLevel elementLevel = retrieveCube(ctx, cubeUrn).getElementLevel();

        // Delete
        deleteElementLevel(ctx, elementLevel);
    }

    @Override
    public void updateGeographicalCache(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        updateGeographicalCacheInJob(ctx, publicationVersion, true);
    }

    @Override
    public PublicationVersion getPublicationLastVersionPublished(ServiceContext ctx, String agencyId, String resourceId) throws MetamacException {
        PagingParameter paging = PagingParameter.rowAccess(0, 1, 1);
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(PublicationVersion.class)
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED).and()
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().maintainer().code()).eq(agencyId).and()
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().code()).eq(resourceId).and()
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().validTo()).isNull().distinctRoot().build();

        // @formatter:off

        PagedResult<PublicationVersion> result =  getPublicationVersionRepository().findByCondition(conditions, paging);
        
        if ( result.getValues() != null && !result.getValues().isEmpty() && result.getValues().size() == 1) {
            return result.getValues().get(0);
        }
        return null;
    }
    
    // ------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------

    private static void fillMetadataForCreatePublication(Publication publication, ExternalItem statisticalOperation) {
        publication.setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        FillMetadataForCreateResourceUtils.fillMetadataForCreateIdentifiableResource(publication.getIdentifiableStatisticalResource(), statisticalOperation);
    }

    private static void fillMetadataForCreatePublicationVersion(PublicationVersion publicationVersion, ExternalItem statisticalOperation, ServiceContext ctx) {
        FillMetadataForCreateResourceUtils.fillMetadataForCretateSiemacResource(publicationVersion.getSiemacMetadataStatisticalResource(), statisticalOperation, StatisticalResourceTypeEnum.COLLECTION,
                ctx);
    }

    private synchronized Publication assignCodeAndSavePublicationVersion(Publication publication, PublicationVersion publicationVersion) throws MetamacException {
        String code = siemacStatisticalResourceGeneratedCode.fillGeneratedCodeForCreateSiemacMetadataResource(publicationVersion.getSiemacMetadataStatisticalResource());
        String[] maintainer = new String[]{publicationVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};

        // Fill code and urn for root and version
        publication.getIdentifiableStatisticalResource().setCode(code);
        publication.getIdentifiableStatisticalResource().setUrn(generateSiemacStatisticalResourceCollectionUrn(maintainer, publication.getIdentifiableStatisticalResource().getCode()));

        publicationVersion.getSiemacMetadataStatisticalResource().setCode(code);
        publicationVersion.getSiemacMetadataStatisticalResource().setUrn(generateSiemacStatisticalResourceCollectionVersionUrn(maintainer,
                publicationVersion.getSiemacMetadataStatisticalResource().getCode(), publicationVersion.getSiemacMetadataStatisticalResource().getVersionLogic()));

        // Checks
        identifiableStatisticalResourceRepository.checkDuplicatedUrn(publicationVersion.getSiemacMetadataStatisticalResource());

        // Add version to publication
        publication.addVersion(publicationVersion);
        return getPublicationRepository().save(publicationVersion.getPublication());
    }

    private ElementLevel createChapterElementLevel(ServiceContext ctx, PublicationVersion publicationVersion, Chapter chapter) throws MetamacException {
        ElementLevel elementLevel = chapter.getElementLevel();
        elementLevel = createElementLevel(ctx, publicationVersion, elementLevel);
        return elementLevel;
    }

    private ElementLevel createCubeElementLevel(ServiceContext ctx, PublicationVersion publicationVersion, Cube cube) throws MetamacException {
        ElementLevel elementLevel = cube.getElementLevel();
        elementLevel = createElementLevel(ctx, publicationVersion, elementLevel);
        return elementLevel;
    }

    /**
     * Create element level: chapter or cube
     */
    private ElementLevel createElementLevel(ServiceContext ctx, PublicationVersion publicationVersion, ElementLevel elementLevel) throws MetamacException {
        if (elementLevel.getParent() == null) {
            elementLevel = addToFirstLevelInPublicationVersion(ctx, publicationVersion, elementLevel);
        } else {
            elementLevel = addToParentLevel(ctx, publicationVersion, elementLevel);
        }

        updateLastUpdateMetadata(publicationVersion);

        return elementLevel;
    }

    private ElementLevel addToFirstLevelInPublicationVersion(ServiceContext ctx, PublicationVersion publicationVersion, ElementLevel elementLevel) throws MetamacException {
        // Create element level
        elementLevel.setParent(null);
        elementLevel.setPublicationVersion(publicationVersion);
        elementLevel.setPublicationVersionFirstLevel(publicationVersion);
        elementLevel = getElementLevelRepository().save(elementLevel);

        // Update publicationVersion adding element
        publicationVersion.addChildrenFirstLevel(elementLevel);
        publicationVersion.addChildrenAllLevel(elementLevel);
        publicationVersion = getPublicationVersionRepository().save(publicationVersion);

        // Check order and update order of other elements in this level
        updatePublicationVersionElementsOrdersInLevelAddingElement(publicationVersion.getChildrenFirstLevel(), elementLevel);
        return elementLevel;
    }

    private ElementLevel addToParentLevel(ServiceContext ctx, PublicationVersion publicationVersion, ElementLevel elementLevel) throws MetamacException {
        ElementLevel elementLevelParent = elementLevel.getParent();

        // Check chapter parent belongs to publication version
        String publicationVersionUrnOfParentChapter = elementLevelParent.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn();
        if (!publicationVersion.getSiemacMetadataStatisticalResource().getUrn().equals(publicationVersionUrnOfParentChapter)) {
            throw new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, elementLevelParent.getChapter().getNameableStatisticalResource().getUrn(),
                    publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        }

        // Create element level in chapter
        elementLevel.setPublicationVersionFirstLevel(null);
        elementLevel.setPublicationVersion(publicationVersion);
        elementLevel.setParent(elementLevelParent);
        elementLevel = getElementLevelRepository().save(elementLevel);

        // Update parent level adding element
        elementLevelParent.addChildren(elementLevel);
        elementLevelParent = updateElementLevel(elementLevelParent);

        // Update order of other elements in this level
        updatePublicationVersionElementsOrdersInLevelAddingElement(elementLevelParent.getChildren(), elementLevel);

        // Update indicators system adding element to all children
        publicationVersion.addChildrenAllLevel(elementLevel);
        getPublicationVersionRepository().save(publicationVersion);

        return elementLevel;
    }

    private void updatePublicationVersionElementsOrdersInLevelAddingElement(List<ElementLevel> elementsAtLevel, ElementLevel elementToAdd) throws MetamacException {

        // Create a set with all possibles orders. At the end of this method, this set must be empty
        Set<Long> orders = new HashSet<>();
        for (int i = 1; i <= elementsAtLevel.size(); i++) {
            orders.add(Long.valueOf(i));
        }

        elementsAtLevel.sort(new ElementLevelComparator());

        // Update orders
        for (ElementLevel elementInLevel : elementsAtLevel) {
            // it is possible that element is already added to parent and order is already set
            if (elementInLevel.getElementId().equals(elementToAdd.getElementId()) && haveTheSameType(elementInLevel, elementToAdd)) {
                // nothing
            } else {
                // Update order
                if (elementInLevel.getOrderInLevel() >= elementToAdd.getOrderInLevel()) {
                    elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() + 1);
                    updateElementLevel(elementInLevel);
                }
            }

            boolean removed = orders.remove(elementInLevel.getOrderInLevel());
            if (!removed) {
                break; // order incorrect
            }
        }

        // Checks orders
        if (!orders.isEmpty()) {
            if (elementToAdd.isChapter()) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL);
            } else {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL);
            }
        }
    }

    private boolean haveTheSameType(ElementLevel firstElement, ElementLevel secondLevel) {
        return (firstElement.isChapter() && secondLevel.isChapter()) || (firstElement.isCube() && secondLevel.isCube());
    }

    private ElementLevel updateElementLevel(ElementLevel elementLevel) throws MetamacException {
        return getElementLevelRepository().save(elementLevel);
    }

    private ElementLevel updateElementLevelLocation(ServiceContext ctx, ElementLevel elementLevel, String targetParentChapterUrn, Long orderInLevel) throws MetamacException {
        // Check indicators system proc status
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, elementLevel.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(publicationVersion);

        // Change order
        Long orderInLevelBefore = elementLevel.getOrderInLevel();
        elementLevel.setOrderInLevel(orderInLevel);

        // Check if target parent is in publicationVersion
        if (targetParentChapterUrn != null) {
            Chapter targetParentChapter = retrieveChapter(ctx, targetParentChapterUrn);
            checkIfParentChapterIsInTheSamePublicationVersion(elementLevel, targetParentChapter);

            // Check target parent is not children of this chapter (only when element is a chapter)
            if (elementLevel.getChapter() != null) {
                checkChapterIsNotChildren(ctx, elementLevel.getChapter(), targetParentChapter);
            }
        }

        // Set actual parent
        String actualParentChapterUrn = elementLevel.getParentUrn();

        // Check if it's necessary update parent and order or only order
        if ((actualParentChapterUrn == null && targetParentChapterUrn != null) || (actualParentChapterUrn != null && targetParentChapterUrn == null)
                || (actualParentChapterUrn != null && targetParentChapterUrn != null && !targetParentChapterUrn.equals(actualParentChapterUrn))) {
            elementLevel = updateParentAndOrder(ctx, elementLevel, targetParentChapterUrn, publicationVersion, orderInLevelBefore);
        } else {
            elementLevel = updateOnlyOrder(ctx, elementLevel, orderInLevelBefore);
        }

        return elementLevel;
    }

    private ElementLevel updateOnlyOrder(ServiceContext ctx, ElementLevel elementLevel, Long orderInLevelBefore) throws MetamacException {
        // Same parent, only changes order. Check order is correct and update orders
        List<ElementLevel> elementsInLevel = elementLevel.getParent() != null ? elementLevel.getParent().getChildren() : elementLevel.getPublicationVersionFirstLevel().getChildrenFirstLevel();
        updatePublicationVersionElementsOrdersInLevelChangingOrder(ctx, elementsInLevel, elementLevel, orderInLevelBefore, elementLevel.getOrderInLevel());
        elementLevel = updateElementLevel(elementLevel);
        return elementLevel;
    }

    private ElementLevel updateParentAndOrder(ServiceContext ctx, ElementLevel elementLevel, String targetParentChapterUrn, PublicationVersion publicationVersion, Long orderInLevelBefore)
            throws MetamacException {
        ElementLevel parentActual = elementLevel.getParent() != null ? elementLevel.getParent() : null;
        ElementLevel parentTarget = targetParentChapterUrn != null ? retrieveChapter(ctx, targetParentChapterUrn).getElementLevel() : null;

        // Update actual parent dimension or indicators system version, removing dimension
        if (parentActual != null) {
            // Update order of other dimensions
            elementLevel.setParent(null);
            updatePublicationVersionElementsOrdersInLevelRemovingElement(ctx, parentActual.getChildren(), elementLevel, orderInLevelBefore);
        } else {
            // Update order of other dimensions
            elementLevel.setPublicationVersionFirstLevel(null);
            updatePublicationVersionElementsOrdersInLevelRemovingElement(ctx, publicationVersion.getChildrenFirstLevel(), elementLevel, orderInLevelBefore);
        }

        // Update target parent, adding dimension
        List<ElementLevel> elementsInLevel = null;
        if (parentTarget == null) {
            publicationVersion.addChildrenFirstLevel(elementLevel);
            publicationVersion = getPublicationVersionRepository().save(publicationVersion);
            elementsInLevel = publicationVersion.getChildrenFirstLevel();
        } else {
            parentTarget.addChildren(elementLevel);
            updateElementLevel(parentTarget);
            elementsInLevel = parentTarget.getChildren();
        }
        // Check order is correct and update orders
        updatePublicationVersionElementsOrdersInLevelAddingElement(elementsInLevel, elementLevel);

        // Update dimension, changing parent
        if (parentTarget == null) {
            elementLevel.setPublicationVersionFirstLevel(publicationVersion);
            elementLevel.setParent(null);
        } else {
            elementLevel.setPublicationVersionFirstLevel(null);
            elementLevel.setParent(parentTarget);
        }
        elementLevel = updateElementLevel(elementLevel);
        return elementLevel;
    }

    /**
     * We can not move a chapter to its child
     */
    private void checkChapterIsNotChildren(ServiceContext ctx, Chapter chapter, Chapter parentChapter) throws MetamacException {
        // Set parent
        ElementLevel chapterParentElementLevel = null;
        if (parentChapter.getElementLevel().getParent() != null) {
            chapterParentElementLevel = parentChapter.getElementLevel().getParent();
        }

        while (chapterParentElementLevel != null) {
            if (chapterParentElementLevel.isChapter() && chapterParentElementLevel.getChapter().getNameableStatisticalResource().getUrn().equals(chapter.getNameableStatisticalResource().getUrn())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__PARENT);
            }
            chapterParentElementLevel = chapterParentElementLevel.getParent();
        }
    }

    private void checkIfParentChapterIsInTheSamePublicationVersion(ElementLevel elementLevel, Chapter parentChapter) throws MetamacException {
        // Check if chapter is in the publicationVersion
        if (!elementLevel.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn()
                .equals(parentChapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn())) {
            throw new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentChapter.getNameableStatisticalResource().getUrn(),
                    elementLevel.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn());
        }
    }

    private void updatePublicationVersionElementsOrdersInLevelRemovingElement(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToRemove, Long orderBeforeUpdate)
            throws MetamacException {
        for (ElementLevel elementInLevel : elementsAtLevel) {
            if (elementInLevel.getElementId().equals(elementToRemove.getElementId())) {
                // nothing
            } else if (elementInLevel.getOrderInLevel() > orderBeforeUpdate) {
                elementInLevel.setOrderInLevel(elementInLevel.getOrderInLevel() - 1);
                updateElementLevel(elementInLevel);
            }
        }
    }

    private void updatePublicationVersionElementsOrdersInLevelChangingOrder(ServiceContext ctx, List<ElementLevel> elementsAtLevel, ElementLevel elementToChangeOrder, Long orderBeforeUpdate,
            Long orderAfterUpdate) throws MetamacException {

        // Checks orders
        if (orderAfterUpdate > elementsAtLevel.size()) {
            if (elementToChangeOrder.isChapter()) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL);
            } else {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL);
            }
        }

        // Update orders
        for (ElementLevel elementAtLevel : elementsAtLevel) {
            if (elementAtLevel.getElementId().equals(elementToChangeOrder.getElementId()) && haveTheSameType(elementAtLevel, elementToChangeOrder)) {
                continue;
            }
            if (orderAfterUpdate < orderBeforeUpdate) {
                if (elementAtLevel.getOrderInLevel() >= orderAfterUpdate && elementAtLevel.getOrderInLevel() < orderBeforeUpdate) {
                    elementAtLevel.setOrderInLevel(elementAtLevel.getOrderInLevel() + 1);
                    updateElementLevel(elementAtLevel);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (elementAtLevel.getOrderInLevel() > orderBeforeUpdate && elementAtLevel.getOrderInLevel() <= orderAfterUpdate) {
                    elementAtLevel.setOrderInLevel(elementAtLevel.getOrderInLevel() - 1);
                    updateElementLevel(elementAtLevel);
                }
            }
        }
    }

    private void deleteElementLevel(ServiceContext ctx, ElementLevel elementLevel) throws MetamacException {
        // Check indicators system proc status
        PublicationVersion publicationVersion = retrievePublicationVersionByUrn(ctx, elementLevel.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn());
        ProcStatusValidator.checkStatisticalResourceStructureCanBeEdited(publicationVersion);

        // Get order of the element that will be deleted
        Long orderInLevelOfDeletedElement = elementLevel.getOrderInLevel();

        // Delete
        getElementLevelRepository().delete(elementLevel);

        // Update publicationVersion
        publicationVersion = retrievePublicationVersionByUrn(ctx, elementLevel.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn());

        // Update orders of other elements in level
        List<ElementLevel> elementsAtLevel = null;
        if (elementLevel.getParent() == null) {
            elementsAtLevel = publicationVersion.getChildrenFirstLevel();
        } else {
            elementsAtLevel = elementLevel.getParent().getChildren();
        }

        updatePublicationVersionElementsOrdersInLevelRemovingElement(ctx, elementsAtLevel, elementLevel, orderInLevelOfDeletedElement);

        updateLastUpdateMetadata(publicationVersion);
    }

    private void updateLastUpdateMetadata(PublicationVersion publicationVersion) {
        publicationVersion.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime());
        getPublicationVersionRepository().save(publicationVersion);
    }
    
    @Override
    public PagedResult<Publication> findPublicationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validations
        publicationServiceInvocationValidator.checkFindPublicationVersionsByCondition(ctx, conditions, pagingParameter);

        conditions = CriteriaUtils.initConditions(conditions, PublicationVersion.class);
        pagingParameter = CriteriaUtils.initPagingParameter(pagingParameter);

        return getPublicationRepository().findByCondition(conditions, pagingParameter);
    }

    private void updateGeographicalCacheInJob(ServiceContext ctx, PublicationVersion publicationVersion, boolean sendNotification) throws MetamacException {
        ProcStatusValidator.checkStatisticalResourceStructureCanBeCached(publicationVersion);

        String publicationUrn = publicationVersion.getPublication().getIdentifiableStatisticalResource().getUrn();
        String publicationVersionUrn = publicationVersion.getSiemacMetadataStatisticalResource().getUrn();

        checkNotTasksInProgress(ctx, publicationVersionUrn);

        TaskInfoResources taskInfo = new TaskInfoResources();
        taskInfo.setVersionId(publicationVersionUrn);
        taskInfo.setUrn(publicationUrn);
        taskInfo.setResourceType(StatisticalResourceTypeEnum.COLLECTION.name());
        getTaskService().planifyUpdateGeographicalCacheRelatedResource(ctx, taskInfo, sendNotification);

    }

    private void checkNotTasksInProgress(ServiceContext ctx, String pulicationUrn) throws MetamacException {
        if (getTaskService().existsTaskForResource(ctx, pulicationUrn)) {
            throw new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, pulicationUrn);
        }
    }

}
