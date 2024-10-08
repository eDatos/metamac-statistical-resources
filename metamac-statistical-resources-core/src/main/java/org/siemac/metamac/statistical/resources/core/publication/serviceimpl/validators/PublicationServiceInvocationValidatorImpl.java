package org.siemac.metamac.statistical.resources.core.publication.serviceimpl.validators;

import java.net.URL;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.statistical.resources.core.base.validators.BaseInvocationValidator;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;

public class PublicationServiceInvocationValidatorImpl extends BaseInvocationValidator {

    // ------------------------------------------------------------------------
    // PUBLICATIONS
    // ------------------------------------------------------------------------

    public static void checkCreatePublicationVersion(PublicationVersion publicationVersion, ExternalItem statisticalOperation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkNewPublicationVersion(publicationVersion, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(statisticalOperation, ServiceExceptionParameters.STATISTICAL_OPERATION, exceptions);
    }

    public static void checkFindPublicationsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
        //NOTHING
    }

    public static void checkUpdatePublicationVersion(PublicationVersion publicationVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkExistingPublicationVersion(publicationVersion, exceptions);
    }

    public static void checkRetrievePublicationVersionByUrn(String publicationVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrn, ServiceExceptionParameters.PUBLICATION_VERSION_URN, exceptions);
    }

    public static void checkRetrieveLatestPublicationVersionByPublicationUrn(String publicationUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationUrn, ServiceExceptionParameters.PUBLICATION_URN, exceptions);
    }

    public static void checkRetrieveLatestPublishedPublicationVersionByPublicationUrn(String publicationUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationUrn, ServiceExceptionParameters.PUBLICATION_URN, exceptions);
    }

    public static void checkRetrievePublicationVersions(String publicationVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrn, ServiceExceptionParameters.PUBLICATION_VERSION_URN, exceptions);
    }

    public static void checkFindPublicationVersionsByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // NOTHING
    }

    public static void checkDeletePublicationVersion(String publicationVersionUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrn, ServiceExceptionParameters.PUBLICATION_VERSION_URN, exceptions);
    }

    public static void checkGetPublicationLastVersionPublished(String agencyId, String resourceId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(agencyId, ServiceExceptionParameters.PUBLICATION_VERSION__MAINTAINER__ID, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(resourceId, ServiceExceptionParameters.PUBLICATION_VERSION__CODE, exceptions);
    }

    public static void checkVersioningPublicationVersion(String publicationVersionUrnToCopy, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrnToCopy, ServiceExceptionParameters.PUBLICATION_VERSION_URN_TO_COPY, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(versionType, ServiceExceptionParameters.VERSION_TYPE, exceptions);
    }

    private static void checkNewPublicationVersion(PublicationVersion publicationVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersion, ServiceExceptionParameters.PUBLICATION_VERSION, exceptions);

        if (publicationVersion == null) {
            return;
        }

        checkNewSiemacMetadataStatisticalResource(publicationVersion.getSiemacMetadataStatisticalResource(), ServiceExceptionParameters.PUBLICATION_VERSION, exceptions);
        checkPublicationVersion(publicationVersion, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(publicationVersion.getPublication(), ServiceExceptionParameters.PUBLICATION_VERSION__PUBLICATION, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(publicationVersion.getId(), ServiceExceptionParameters.PUBLICATION_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(publicationVersion.getVersion(), ServiceExceptionParameters.PUBLICATION_VERSION__VERSION, exceptions);
    }

    public static void checkExistingPublicationVersion(PublicationVersion publicationVersion, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersion, ServiceExceptionParameters.PUBLICATION_VERSION, exceptions);

        if (publicationVersion == null) {
            return;
        }

        checkExistingSiemacMetadataStatisticalResource(publicationVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.PUBLICATION_VERSION,
                ServiceExceptionParameters.PUBLICATION_VERSION, exceptions);
        checkPublicationVersion(publicationVersion, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(publicationVersion.getPublication(), ServiceExceptionParameters.PUBLICATION_VERSION__PUBLICATION, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(publicationVersion.getId(), ServiceExceptionParameters.PUBLICATION_VERSION__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(publicationVersion.getVersion(), ServiceExceptionParameters.PUBLICATION_VERSION__VERSION, exceptions);
    }

    private static void checkPublicationVersion(PublicationVersion publicationVersion, List<MetamacExceptionItem> exceptions) {
    }

    // ------------------------------------------------------------------------
    // STRUCTURE
    // ------------------------------------------------------------------------

    public static void checkImportPublicationVersionStructure(String publicationVersionUrn, URL fileURL, String language, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrn, ServiceExceptionParameters.PUBLICATION_VERSION_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(fileURL, ServiceExceptionParameters.STRUCTURE_FILE_URL, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(language, ServiceExceptionParameters.STRUCTURE_FILE_LANGUAGE, exceptions);
    };

    // ------------------------------------------------------------------------
    // CHAPTERS
    // ------------------------------------------------------------------------

    public static void checkCreateChapter(String publicationVersionUrn, Chapter chapter, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrn, ServiceExceptionParameters.PUBLICATION_VERSION_URN, exceptions);
        checkNewChapter(chapter, exceptions);
    }

    public static void checkUpdateChapter(Chapter chapter, List<MetamacExceptionItem> exceptions) {
        checkExistingChapter(chapter, exceptions);
    }

    public static void checkUpdateChapterLocation(String chapterUrn, String parentChapterUrn, Long orderInLevel, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(chapterUrn, ServiceExceptionParameters.CHAPTER_URN, exceptions);
        // PARENT_CHAPTER_URN can be null if we want to move de chapter to the first level
        StatisticalResourcesValidationUtils.checkParameterRequired(orderInLevel, ServiceExceptionParameters.ORDER_IN_LEVEL, exceptions);
    }

    public static void checkRetrieveChapter(String chapterUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(chapterUrn, ServiceExceptionParameters.CHAPTER_URN, exceptions);
    }

    public static void checkDeleteChapter(String chapterUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(chapterUrn, ServiceExceptionParameters.CHAPTER_URN, exceptions);
    }

    private static void checkNewChapter(Chapter chapter, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(chapter, ServiceExceptionParameters.CHAPTER, exceptions);

        if (chapter == null) {
            return;
        }

        checkChapter(chapter, exceptions);
        checkNewNameableStatisticalResource(chapter.getNameableStatisticalResource(), ServiceExceptionParameters.CHAPTER, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(chapter.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL, exceptions);

        // Metadata that must be empty for new entities
        StatisticalResourcesValidationUtils.checkMetadataEmpty(chapter.getId(), ServiceExceptionParameters.CHAPTER__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataEmpty(chapter.getVersion(), ServiceExceptionParameters.CHAPTER__VERSION, exceptions);
    }

    private static void checkExistingChapter(Chapter chapter, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(chapter, ServiceExceptionParameters.CHAPTER, exceptions);

        if (chapter == null) {
            return;
        }

        checkChapter(chapter, exceptions);
        checkExistingNameableStatisticalResource(chapter.getNameableStatisticalResource(), TypeRelatedResourceEnum.CHAPTER, ServiceExceptionParameters.CHAPTER, exceptions);

        // Metadata that must be filled for existing entities
        StatisticalResourcesValidationUtils.checkMetadataRequired(chapter.getElementLevel(), ServiceExceptionParameters.CHAPTER, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(chapter.getId(), ServiceExceptionParameters.CHAPTER__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(chapter.getVersion(), ServiceExceptionParameters.CHAPTER__VERSION, exceptions);
    }

    private static void checkChapter(Chapter chapter, List<MetamacExceptionItem> exceptions) {
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    public static void checkCreateCube(String publicationVersionUrn, Cube cube, List<MetamacExceptionItem> exceptions) {
        if (cube.getUrl() == null) {
            StatisticalResourcesValidationUtils.checkParameterRequired(publicationVersionUrn, ServiceExceptionParameters.PUBLICATION_VERSION_URN, exceptions);
        }
        checkNewCube(cube, exceptions);

    }

    public static void checkUpdateCube(Cube cube, List<MetamacExceptionItem> exceptions) {
        checkExistingCube(cube, exceptions);
    }

    public static void checkUpdateCubeLocation(String cubeUrn, String parentChapterUrn, Long orderInLevel, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cubeUrn, ServiceExceptionParameters.CUBE_URN, exceptions);
        // PARENT_CHAPTER_URN can be null if we want to move de chapter to the first level
        StatisticalResourcesValidationUtils.checkParameterRequired(orderInLevel, ServiceExceptionParameters.ORDER_IN_LEVEL, exceptions);
    }

    public static void checkRetrieveCube(String cubeUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cubeUrn, ServiceExceptionParameters.CUBE_URN, exceptions);
    }

    public static void checkDeleteCube(String cubeUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cubeUrn, ServiceExceptionParameters.CUBE_URN, exceptions);
    }

    private static void checkNewCube(Cube cube, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cube, ServiceExceptionParameters.CUBE, exceptions);

        if (cube == null) {
            return;
        }

        checkCube(cube, exceptions);
        checkNewNameableStatisticalResource(cube.getNameableStatisticalResource(), ServiceExceptionParameters.CUBE, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL, exceptions);
    }

    private static void checkExistingCube(Cube cube, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(cube, ServiceExceptionParameters.CUBE, exceptions);

        if (cube == null) {
            return;
        }

        checkCube(cube, exceptions);
        checkExistingNameableStatisticalResource(cube.getNameableStatisticalResource(), TypeRelatedResourceEnum.CUBE, ServiceExceptionParameters.CUBE, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getElementLevel(), ServiceExceptionParameters.CUBE, exceptions);
    }

    private static void checkCube(Cube cube, List<MetamacExceptionItem> exceptions) {
        checkOnlyOneRelatedResourceType(cube, exceptions);

        if (cube.getDataset() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getDatasetUrn(), ServiceExceptionParameters.CUBE__DATASET__URN, exceptions);
        }

        if (cube.getQuery() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getQueryUrn(), ServiceExceptionParameters.CUBE__QUERY__URN, exceptions);
        }

        if (cube.getMultidataset() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getMultidatasetUrn(), ServiceExceptionParameters.CUBE__MULTIDATASET__URN, exceptions);
        }
        
        if (cube.getPublication() != null) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(cube.getPublicationUrn(), ServiceExceptionParameters.CUBE__PUBLICATION__URN, exceptions);
        }
        if (cube.getUrl() != null) {
            ValidationUtils.validateUrl(cube.getUrl(), ServiceExceptionParameters.CUBE_URL, exceptions);
        }

        StatisticalResourcesValidationUtils.checkMetadataEmpty(cube.getElementLevel().getChildren(), ServiceExceptionParameters.CUBE__CHILDREN, exceptions);
    }

    private static void checkOnlyOneRelatedResourceType(Cube cube, List<MetamacExceptionItem> exceptions) {
        if (cube.getUrl() != null) {
            return;
        }

        int numberOfRelatedResourceTypes = 0;

        if (cube.getDataset() != null) {
            numberOfRelatedResourceTypes++;
        }

        if (cube.getQuery() != null) {
            numberOfRelatedResourceTypes++;
        }

        if (cube.getMultidataset() != null) {
            numberOfRelatedResourceTypes++;
        }

        if (cube.getPublication() != null) {
            numberOfRelatedResourceTypes++;
        }

        // throw an exception if no related resources has been specified or more than one related resources has been specified
        if (numberOfRelatedResourceTypes == 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED,
                    ServiceExceptionParameters.CUBE__DATASET + " / " + ServiceExceptionParameters.CUBE__QUERY + " / " + ServiceExceptionParameters.CUBE__MULTIDATASET));
        } else if (numberOfRelatedResourceTypes > 1) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED,
                    ServiceExceptionParameters.CUBE__DATASET + " / " + ServiceExceptionParameters.CUBE__QUERY + " / " + ServiceExceptionParameters.CUBE__MULTIDATASET));
        }
    }

    public static void checkUpdateGeographicalCache(PublicationVersion publicationVersion, List<MetamacExceptionItem> exceptions) {
        checkExistingPublicationVersion(publicationVersion, exceptions);
    }

    public static void checkUpdateAllGeographicalCache(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }

    public static void checkUpdateAllExternalGeographicalCache(List<MetamacExceptionItem> exceptions) {
        // NOTHING
    }
}
