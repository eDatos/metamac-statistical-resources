package org.siemac.metamac.statistical.resources.core.publication.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetRepository;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.ChapterRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.CubeRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.exception.PublicationVersionNotFoundException;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("publicationDto2DoMapper")
public class PublicationDto2DoMapperImpl extends BaseDto2DoMapperImpl implements PublicationDto2DoMapper {

    @Autowired
    private PublicationVersionRepository publicationVersionRepository;

    @Autowired
    private ChapterRepository            chapterRepository;

    @Autowired
    private CubeRepository               cubeRepository;

    @Autowired
    private QueryRepository              queryRepository;

    @Autowired
    private DatasetRepository            datasetRepository;

    @Autowired
    private MultidatasetRepository       multidatasetRepository;

    @Autowired
    private PublicationRepository        publicationRepository;

    // --------------------------------------------------------------------------------------
    // PUBLICATION VERSION
    // --------------------------------------------------------------------------------------

    @Override
    public void checkOptimisticLocking(PublicationVersionBaseDto source) throws MetamacException {
        if (source != null && source.getId() != null) {
            try {
                PublicationVersion target = publicationVersionRepository.findById(source.getId());
                if (target.getId() != null) {
                    OptimisticLockingUtils.checkVersion(target.getSiemacMetadataStatisticalResource().getVersion(), source.getOptimisticLockingVersion());
                }
            } catch (PublicationVersionNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }
    }

    @Override
    public PublicationVersion publicationVersionDtoToDo(PublicationVersionDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        PublicationVersion target = null;
        if (source.getId() == null) {
            target = new PublicationVersion();
            target.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        } else {
            try {
                target = publicationVersionRepository.findById(source.getId());
            } catch (PublicationVersionNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        publicationVersionDtoToDo(source, target);

        return target;
    }

    private PublicationVersion publicationVersionDtoToDo(PublicationVersionDto source, PublicationVersion target) throws MetamacException {
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.PUBLICATION_VERSION).build();
        }

        // Check replaces before setting fields
        checkCanPublicationReplacesOtherPublication(source);

        // Hierarchy
        siemacMetadataStatisticalResourceDtoToDo(source, target.getSiemacMetadataStatisticalResource(), ServiceExceptionParameters.PUBLICATION_VERSION);

        // Other
        // We don't copy formatExtentResources because it is a calculated metadata

        return target;
    }

    protected void checkCanPublicationReplacesOtherPublication(PublicationVersionDto source) throws MetamacException {
        if (source.getReplaces() != null) {
            String currentUrn = source.getUrn();
            RelatedResource resourceReplaced = relatedResourceDtoToDo(source.getReplaces(), null, ServiceExceptionParameters.PUBLICATION_VERSION__REPLACES);
            if (currentUrn.equals(resourceReplaced.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_CANT_REPLACE_ITSELF).withMessageParameters(currentUrn).build();
            } else {
                RelatedResource resourceAlreadyReplacing = resourceReplaced.getPublicationVersion().getSiemacMetadataStatisticalResource().getIsReplacedBy();
                if (resourceAlreadyReplacing != null && !resourceAlreadyReplacing.getPublicationVersion().getSiemacMetadataStatisticalResource().getUrn().equals(source.getUrn())) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PUBLICATION_VERSION_ALREADY_BEEN_REPLACED_BY_OTHER_PUBLICATION_VERSION)
                            .withMessageParameters(currentUrn, source.getReplaces().getUrn()).build();
                }
            }
        }
    }

    // --------------------------------------------------------------------------------------
    // CHAPTER
    // --------------------------------------------------------------------------------------

    @Override
    public Chapter chapterDtoToDo(ChapterDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        Chapter target = null;
        if (source.getUrn() == null) {
            target = new Chapter();

            target.setElementLevel(new ElementLevel());
            target.setNameableStatisticalResource(new NameableStatisticalResource());
            target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
            target.getElementLevel().setCube(null);
            target.getElementLevel().setChapter(target);
        } else {
            target = chapterRepository.retrieveChapterByUrn(source.getUrn());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            // Modified in specific operation
            ValidationUtils.checkMetadataUnmodifiable(source.getParentChapterUrn(), target.getElementLevel().getParentUrn(), ServiceExceptionParameters.CHAPTER__PARENT, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getOrderInLevel(), target.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getCode(), target.getNameableStatisticalResource().getCode(), ServiceExceptionParameters.CHAPTER__CODE, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getUrn(), target.getNameableStatisticalResource().getUrn(), ServiceExceptionParameters.CHAPTER__URN, exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        chapterDtoToDo(source, target);

        return target;
    }

    private Chapter chapterDtoToDo(ChapterDto source, Chapter target) throws MetamacException {
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CHAPTER);
        }

        // Hierarchy
        nameableStatisticalResourceDtoToDo(source, target.getNameableStatisticalResource(), ServiceExceptionParameters.CHAPTER);

        // Related entities
        if (source.getParentChapterUrn() != null) {
            Chapter parentChapter = chapterRepository.retrieveChapterByUrn(source.getParentChapterUrn());
            target.getElementLevel().setParent(parentChapter.getElementLevel());
        }
 
        return target;
    }

    // --------------------------------------------------------------------------------------
    // CUBE
    // --------------------------------------------------------------------------------------

    @Override
    public Cube cubeDtoToDo(CubeDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        Cube target = null;
        if (source.getUrn() == null) {
            target = new Cube();

            target.setElementLevel(new ElementLevel());
            target.setNameableStatisticalResource(new NameableStatisticalResource());
            target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
            target.getElementLevel().setCube(target);
            target.getElementLevel().setChapter(null);
        } else {
            target = cubeRepository.retrieveCubeByUrn(source.getUrn());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            // Modified in specific operation
            ValidationUtils.checkMetadataUnmodifiable(source.getParentChapterUrn(), target.getElementLevel().getParentUrn(), ServiceExceptionParameters.CUBE__PARENT, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getOrderInLevel(), target.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getCode(), target.getNameableStatisticalResource().getCode(), ServiceExceptionParameters.CUBE__CODE, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getUrn(), target.getNameableStatisticalResource().getUrn(), ServiceExceptionParameters.CUBE__URN, exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        cubeDtoToDo(source, target);

        return target;
    }

    private Cube cubeDtoToDo(CubeDto source, Cube target) throws MetamacException {
        // Hierarchy
        nameableStatisticalResourceDtoToDo(source, target.getNameableStatisticalResource(), ServiceExceptionParameters.CUBE);

        // Related entities
        if (source.getParentChapterUrn() != null) {
            Chapter parentChapter = chapterRepository.retrieveChapterByUrn(source.getParentChapterUrn());
            target.getElementLevel().setParent(parentChapter.getElementLevel());
        } else {
            target.getElementLevel().setParent(null);
        }

        if (source.getDatasetUrn() != null) {
            Dataset dataset = datasetRepository.retrieveByUrn(source.getDatasetUrn());
            target.setDataset(dataset);
        } else {
            target.setDataset(null);
        }

        if (source.getQueryUrn() != null) {
            Query query = queryRepository.retrieveByUrn(source.getQueryUrn());
            target.setQuery(query);
        } else {
            target.setQuery(null);
        }

        if (source.getMultidatasetUrn() != null) {
            Multidataset multidataset = multidatasetRepository.retrieveByUrn(source.getMultidatasetUrn());
            target.setMultidataset(multidataset);
        } else {
            target.setMultidataset(null);
        }

        if (source.getCollectionUrn() != null) {
            Publication publication = publicationRepository.retrieveByUrn(source.getCollectionUrn());
            target.setPublication(publication);
        } else {
            target.setPublication(null);
        }

        target.setUrl(source.getUrl());
               
        return target;

    }
}
