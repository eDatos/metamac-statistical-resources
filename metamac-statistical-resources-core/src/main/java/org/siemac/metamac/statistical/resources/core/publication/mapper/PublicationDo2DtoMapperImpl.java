package org.siemac.metamac.statistical.resources.core.publication.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ElementLevelDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationStructureDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.ProcStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("publicationDo2DtoMapper")
public class PublicationDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements PublicationDo2DtoMapper {

    @Autowired
    private PublicationVersionRepository publicationVersionRepository;

    @Autowired
    private QueryVersionRepository queryVersionRepository;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;
    // ---------------------------------------------------------------------------------------------------------
    // PUBLICATIONS
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public RelatedResourceDto publicationVersionDoToPublicationRelatedResourceDto(PublicationVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        publicationVersionDoToPublicationRelatedResourceDto(source, target);
        return target;
    }

    private RelatedResourceDto publicationVersionDoToPublicationRelatedResourceDto(PublicationVersion source, RelatedResourceDto target) {
        if (source == null) {
            return null;
        }

        // Identity
        target.setId(source.getPublication().getId());
        target.setVersion(source.getPublication().getVersion());

        // Type
        target.setType(TypeRelatedResourceEnum.PUBLICATION);

        // Identifiable Fields
        target.setCode(source.getPublication().getIdentifiableStatisticalResource().getCode());
        target.setCodeNested(null);
        target.setUrn(source.getPublication().getIdentifiableStatisticalResource().getUrn());

        // Nameable Fields
        target.setTitle(internationalStringDoToDto(source.getSiemacMetadataStatisticalResource().getTitle()));

        return target;
    }

    // --------------------------------------------------------------------------------------
    // PUBLICATION VERSION
    // --------------------------------------------------------------------------------------

    @Override
    public PublicationVersionBaseDto publicationVersionDoToBaseDto(PublicationVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }

        PublicationVersionBaseDto target = new PublicationVersionBaseDto();
        publicationVersionDoToBaseDto(source, target);
        return target;
    }

    private PublicationVersionBaseDto publicationVersionDoToBaseDto(PublicationVersion source, PublicationVersionBaseDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        siemacMetadataStatisticalResourceDoToBaseDto(source.getSiemacMetadataStatisticalResource(), target);

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        return target;
    }

    @Override
    public PublicationVersionDto publicationVersionDoToDto(PublicationVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }

        PublicationVersionDto target = new PublicationVersionDto();
        publicationVersionDoToDto(source, target);
        return target;
    }

    @Override
    public List<PublicationVersionBaseDto> publicationVersionDoListToDtoList(List<PublicationVersion> sources) throws MetamacException {
        List<PublicationVersionBaseDto> targets = new ArrayList<PublicationVersionBaseDto>();
        for (PublicationVersion source : sources) {
            targets.add(publicationVersionDoToBaseDto(source));
        }
        return targets;
    }

    private PublicationVersionDto publicationVersionDoToDto(PublicationVersion source, PublicationVersionDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        siemacMetadataStatisticalResourceDoToDto(source.getSiemacMetadataStatisticalResource(), target);

        // Siemac metadata that needs to be filled
        target.setIsReplacedByVersion(relatedResourceDoToDto(source.getLifeCycleStatisticalResource().getIsReplacedByVersion()));
        target.setIsReplacedBy(relatedResourceDoToDto(source.getSiemacMetadataStatisticalResource().getIsReplacedBy()));

        target.getHasPart().clear();
        target.getHasPart().addAll(relatedResourceDoCollectionToDtoCollection(source.getHasPart()));

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Other publication version attributes
        if (ProcStatusEnumUtils.isInAnyProcStatus(source, ProcStatusEnum.PUBLISHED)) {
            target.setFormatExtentResources(source.getFormatExtentResources());
        } else {
            target.setFormatExtentResources(source.getChildrenAllLevels().size());
        }

        return target;
    }

    @Override
    public RelatedResourceDto publicationVersionDoToPublicationVersionRelatedResourceDto(PublicationVersion source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        publicationVersionDoToPublicationVersionRelatedResourceDto(source, target);
        return target;
    }

    private RelatedResourceDto publicationVersionDoToPublicationVersionRelatedResourceDto(PublicationVersion source, RelatedResourceDto target) {
        if (source == null) {
            return null;
        }

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Type
        target.setType(TypeRelatedResourceEnum.PUBLICATION_VERSION);

        // Identifiable Fields
        target.setCode(source.getSiemacMetadataStatisticalResource().getCode());
        target.setCodeNested(null);
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());

        // Nameable Fields
        target.setTitle(internationalStringDoToDto(source.getSiemacMetadataStatisticalResource().getTitle()));

        return target;
    }

    // --------------------------------------------------------------------------------------
    // PUBLICATION STRUCTURE
    // --------------------------------------------------------------------------------------

    @Override
    public PublicationStructureDto publicationVersionStructureDoToDto(PublicationVersion publicationVersion) throws MetamacException {
        if (publicationVersion == null) {
            return null;
        }

        PublicationStructureDto publicationStructureDto = new PublicationStructureDto();
        publicationStructureDto.setPublicationVersion(publicationVersionDoToBaseDto(publicationVersion));

        if (!publicationVersion.getChildrenFirstLevel().isEmpty()) {
            publicationStructureDto.getElements().addAll(elementsLevelDoListToDtoList(publicationVersion.getChildrenFirstLevel()));
        }

        return publicationStructureDto;
    }

    // --------------------------------------------------------------------------------------
    // CHAPTER
    // --------------------------------------------------------------------------------------

    @Override
    public ChapterDto chapterDoToDto(Chapter source) throws MetamacException {
        if (source == null) {
            return null;
        }

        ChapterDto target = new ChapterDto();
        chapterDoToDto(source, target);
        return target;
    }

    private ChapterDto chapterDoToDto(Chapter source, ChapterDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        nameableStatisticalResourceDoToDto(source.getNameableStatisticalResource(), target);

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Other
        target.setParentChapterUrn(source.getElementLevel().getParentUrn());
        target.setOrderInLevel(source.getElementLevel().getOrderInLevel());
        return target;
    }

    // --------------------------------------------------------------------------------------
    // CUBE
    // --------------------------------------------------------------------------------------
    @Override
    public CubeDto cubeDoToDto(Cube source) throws MetamacException {
        if (source == null) {
            return null;
        }

        CubeDto target = new CubeDto();
        cubeDoToDto(source, target);
        return target;
    }

    private CubeDto cubeDoToDto(Cube source, CubeDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        nameableStatisticalResourceDoToDto(source.getNameableStatisticalResource(), target);

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        //Query, Datasource an DSD
        if (source.getQueryUrn() !=null){
            Dataset queryDataset = queryVersionRepository.retrieveLastVersion(source.getQueryUrn()).getDataset();
            String datasetUrnInQuery =  queryDataset.getIdentifiableStatisticalResource().getUrn();
            target.setRelatedDatasetUrnInQuery(datasetUrnInQuery);
            target.setRelatedDSD(getRelatedDsdUrn(datasetUrnInQuery));

        }
        if(source.getDatasetUrn() !=null){
            Dataset dataset = datasetVersionRepository.retrieveLastVersion(source.getDatasetUrn()).getDataset();
            String datasetUrn =  dataset.getIdentifiableStatisticalResource().getUrn();
            target.setRelatedDSD(getRelatedDsdUrn(datasetUrn));
        }


        // Other
        target.setParentChapterUrn(source.getElementLevel().getParentUrn());
        target.setOrderInLevel(source.getElementLevel().getOrderInLevel());
        target.setQueryUrn(source.getQueryUrn());
        target.setDatasetUrn(source.getDatasetUrn());
        target.setMultidatasetUrn(source.getMultidatasetUrn());
        target.setCollectionUrn(source.getPublicationUrn());
        target.setUrl(source.getUrl());
        return target;
    }

    private String getRelatedDsdUrn(String datasetUrn) throws MetamacException {
        DatasetVersion datasetVersion = datasetVersionRepository.retrieveLastVersion(datasetUrn);
        ExternalItem externalItem = datasetVersion.getRelatedDsd();
        return externalItem.getUrn();
    }


    // --------------------------------------------------------------------------------------
    // ELEMENT LEVELS
    // --------------------------------------------------------------------------------------

    @Override
    public List<ElementLevelDto> elementsLevelDoListToDtoList(List<ElementLevel> sources) throws MetamacException {
        List<ElementLevelDto> targets = new ArrayList<ElementLevelDto>();
        for (ElementLevel source : sources) {
            targets.add(elementLevelDoToDto(source));
        }
        return targets;
    }

    private ElementLevelDto elementLevelDoToDto(ElementLevel source) throws MetamacException {
        ElementLevelDto target = new ElementLevelDto();
        if (source.getChapter() != null) {
            target.setChapter(chapterDoToDto(source.getChapter()));
        } else if (source.getCube() != null) {
            target.setCube(cubeDoToDto(source.getCube()));
        }

        for (ElementLevel child : source.getChildren()) {
            target.addSubelement(elementLevelDoToDto(child));
        }
        return target;
    }
}
