package org.siemac.metamac.statistical.resources.web.shared.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.shared.RelatedResourceBaseUtils;

public class RelatedResourceUtils extends RelatedResourceBaseUtils {

    // -------------------------------------------------------------------------------------------------------------
    // IDENTIFIABLE RESOURCE
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getIdentifiableResourceDtoAsRelatedResourceDto(IdentifiableStatisticalResourceDto identifiableStatisticalResourceDto) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(identifiableStatisticalResourceDto.getCode());
        relatedResourceDto.setUrn(identifiableStatisticalResourceDto.getUrn());
        relatedResourceDto
                .setStatisticalOperationUrn(identifiableStatisticalResourceDto.getStatisticalOperation() != null ? identifiableStatisticalResourceDto.getStatisticalOperation().getUrn() : null);
        return relatedResourceDto;
    }

    public static DatasetVersionBaseDto getRelatedResourceDtoAsIdentifiableResourceDto(RelatedResourceDto relatedResourceDto) {
        DatasetVersionBaseDto datasetVersionBaseDto = new DatasetVersionBaseDto();
        datasetVersionBaseDto.setCode(relatedResourceDto.getCode());
        datasetVersionBaseDto.setUrn(relatedResourceDto.getUrn());
        ExternalItemDto externaItemDto = new ExternalItemDto();
        externaItemDto.setUrn(relatedResourceDto.getStatisticalOperationUrn());
        datasetVersionBaseDto.setStatisticalOperation(externaItemDto);
        return datasetVersionBaseDto;
    }

    // -------------------------------------------------------------------------------------------------------------
    // NAMEABLE RESOURCE
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getNameableResourceDtoAsRelatedResourceDto(NameableStatisticalResourceDto nameableStatisticalResourceDto) {
        RelatedResourceDto relatedResourceDto = getIdentifiableResourceDtoAsRelatedResourceDto(nameableStatisticalResourceDto);
        relatedResourceDto.setTitle(nameableStatisticalResourceDto.getTitle());
        return relatedResourceDto;
    }

    public static RelatedResourceDto getNameableResourceBaseDtoAsRelatedResourceDto(NameableStatisticalResourceBaseDto nameableStatisticalResourceBaseDto) {
        RelatedResourceDto relatedResourceDto = getIdentifiableResourceDtoAsRelatedResourceDto(nameableStatisticalResourceBaseDto);
        relatedResourceDto.setTitle(nameableStatisticalResourceBaseDto.getTitle());
        return relatedResourceDto;
    }

    public static DatasetVersionBaseDto getRelatedResourceDtoAsNameableResourceBaseDto(RelatedResourceDto relatedResourceDto) {
        DatasetVersionBaseDto datasetVersionBaseDto = getRelatedResourceDtoAsIdentifiableResourceDto(relatedResourceDto);
        datasetVersionBaseDto.setTitle(relatedResourceDto.getTitle());
        return datasetVersionBaseDto;
    }

    // -------------------------------------------------------------------------------------------------------------
    // PUBLICATIONS
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getPublicationVersionDtoAsRelatedResourceDto(PublicationVersionDto publicationDto) {
        RelatedResourceDto relatedResourceDto = getNameableResourceDtoAsRelatedResourceDto(publicationDto);
        relatedResourceDto.setType(TypeRelatedResourceEnum.PUBLICATION_VERSION);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getPublicationVersionBaseDtoAsRelatedResourceDto(PublicationVersionBaseDto publicationDto) {
        RelatedResourceDto relatedResourceDto = getNameableResourceBaseDtoAsRelatedResourceDto(publicationDto);
        relatedResourceDto.setType(TypeRelatedResourceEnum.PUBLICATION_VERSION);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getPublicationVersionDtosAsRelatedResourceDtos(List<PublicationVersionDto> publicationDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(publicationDtos.size());
        for (PublicationVersionDto publicationDto : publicationDtos) {
            relatedResourceDtos.add(getPublicationVersionDtoAsRelatedResourceDto(publicationDto));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getPublicationVersionBaseDtosAsRelatedResourceDtos(List<PublicationVersionBaseDto> publicationDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(publicationDtos.size());
        for (PublicationVersionBaseDto publicationDto : publicationDtos) {
            relatedResourceDtos.add(getPublicationVersionBaseDtoAsRelatedResourceDto(publicationDto));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // DATASETS
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getDatasetVersionDtoAsRelatedResourceDto(DatasetVersionDto DatasetVersionDto) {
        RelatedResourceDto relatedResourceDto = getNameableResourceDtoAsRelatedResourceDto(DatasetVersionDto);
        relatedResourceDto.setType(TypeRelatedResourceEnum.DATASET_VERSION);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getDatasetVersionBaseDtoAsRelatedResourceDto(DatasetVersionBaseDto datasetVersionBaseDto) {
        RelatedResourceDto relatedResourceDto = getNameableResourceBaseDtoAsRelatedResourceDto(datasetVersionBaseDto);
        relatedResourceDto.setType(TypeRelatedResourceEnum.DATASET_VERSION);

        relatedResourceDto.setLastVersion(datasetVersionBaseDto.getLastVersion());
        relatedResourceDto.setVersionLogic(datasetVersionBaseDto.getVersionLogic());

        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getDatasetVersionDtosAsRelatedResourceDtos(List<DatasetVersionDto> DatasetVersionDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(DatasetVersionDtos.size());
        for (DatasetVersionDto DatasetVersionDto : DatasetVersionDtos) {
            relatedResourceDtos.add(getDatasetVersionDtoAsRelatedResourceDto(DatasetVersionDto));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getDatasetVersionBaseDtosAsRelatedResourceDtos(List<DatasetVersionBaseDto> datasetVersionBaseDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(datasetVersionBaseDtos.size());
        for (DatasetVersionBaseDto datasetVersionBaseDto : datasetVersionBaseDtos) {
            relatedResourceDtos.add(getDatasetVersionBaseDtoAsRelatedResourceDto(datasetVersionBaseDto));
        }
        return relatedResourceDtos;
    }

    public static DatasetVersionBaseDto getRelatedResourceDtoAsDatasetVersionBaseDto(RelatedResourceDto relatedResourceDto, ProcStatusEnum status) {
        DatasetVersionBaseDto datasetVersionBaseDto = getRelatedResourceDtoAsNameableResourceBaseDto(relatedResourceDto);

        datasetVersionBaseDto.setLastVersion(relatedResourceDto.getLastVersion());
        datasetVersionBaseDto.setVersionLogic(relatedResourceDto.getVersionLogic());
        datasetVersionBaseDto.setProcStatus(status);

        return datasetVersionBaseDto;
    }

    public static List<DatasetVersionBaseDto> getRelatedResourceDtosAsDatasetVersionBaseDtos(List<RelatedResourceDto> relatedResourceDtos, ProcStatusEnum status) {

        if (relatedResourceDtos == null) {
            return new ArrayList<DatasetVersionBaseDto>();
        }

        List<DatasetVersionBaseDto> datasetVersionBaseDtos = new ArrayList<DatasetVersionBaseDto>(relatedResourceDtos.size());
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            datasetVersionBaseDtos.add(getRelatedResourceDtoAsDatasetVersionBaseDto(relatedResourceDto, status));
        }
        return datasetVersionBaseDtos;
    }

    public static String getRelatedResourceName(RelatedResourceDto relatedResourceDto) {
        return relatedResourceDto != null ? CommonWebUtils.getElementName(relatedResourceDto.getCode(), relatedResourceDto.getTitle()) : StringUtils.EMPTY;
    }

    public static RelatedResourceDto getRelatedResourceFromExternalItemDto(ExternalItemDto externalItem) {
        RelatedResourceDto relatedResource = new RelatedResourceDto();
        relatedResource.setId(externalItem.getId());
        relatedResource.setCode(externalItem.getCode());
        relatedResource.setTitle(externalItem.getTitle());
        relatedResource.setUrn(externalItem.getUrn());
        relatedResource.setUuid(externalItem.getUuid());
        relatedResource.setVersion(externalItem.getVersion());

        return relatedResource;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromExternalItemDtos(List<ExternalItemDto> externalItems) {
        List<RelatedResourceDto> relatedResources = new ArrayList<RelatedResourceDto>();
        if (externalItems != null) {
            for (ExternalItemDto externalItem : externalItems) {
                relatedResources.add(getRelatedResourceFromExternalItemDto(externalItem));
            }
        }
        return relatedResources;
    }

    // -------------------------------------------------------------------------------------------------------------
    // MULTIDATASETS
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getMultidatasetVersionDtoAsRelatedResourceDto(MultidatasetVersionDto multidatasetDto) {
        RelatedResourceDto relatedResourceDto = getNameableResourceDtoAsRelatedResourceDto(multidatasetDto);
        relatedResourceDto.setType(TypeRelatedResourceEnum.MULTIDATASET_VERSION);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getMultidatasetVersionBaseDtoAsRelatedResourceDto(MultidatasetVersionBaseDto multidatasetDto) {
        RelatedResourceDto relatedResourceDto = getNameableResourceBaseDtoAsRelatedResourceDto(multidatasetDto);
        relatedResourceDto.setType(TypeRelatedResourceEnum.MULTIDATASET_VERSION);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getMultidatasetVersionDtosAsRelatedResourceDtos(List<MultidatasetVersionDto> multidatasetDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(multidatasetDtos.size());
        for (MultidatasetVersionDto multidatasetDto : multidatasetDtos) {
            relatedResourceDtos.add(getMultidatasetVersionDtoAsRelatedResourceDto(multidatasetDto));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getMultidatasetVersionBaseDtosAsRelatedResourceDtos(List<MultidatasetVersionBaseDto> multidatasetDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(multidatasetDtos.size());
        for (MultidatasetVersionBaseDto multidatasetDto : multidatasetDtos) {
            relatedResourceDtos.add(getMultidatasetVersionBaseDtoAsRelatedResourceDto(multidatasetDto));
        }
        return relatedResourceDtos;
    }
}
