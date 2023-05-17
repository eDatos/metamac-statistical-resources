package org.siemac.metamac.statistical.resources.core.dataset.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DimensionRepresentationMappingDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("datasetDo2DtoMapper")
public class DatasetDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements DatasetDo2DtoMapper {

    @Autowired
    private TaskService              taskService;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    // ---------------------------------------------------------------------------------------------------------
    // DATASOURCES
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public DatasourceDto datasourceDoToDto(Datasource source) throws MetamacException {
        if (source == null) {
            return null;
        }
        DatasourceDto target = new DatasourceDto();
        datasourceDoToDto(source, target);
        return target;
    }

    @Override
    public List<DatasourceDto> datasourceDoListToDtoList(List<Datasource> sources) throws MetamacException {
        List<DatasourceDto> targets = new ArrayList<DatasourceDto>();
        for (Datasource source : sources) {
            targets.add(datasourceDoToDto(source));
        }
        return targets;
    }

    private DatasourceDto datasourceDoToDto(Datasource source, DatasourceDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        identifiableStatisticalResourceDoToDto(source.getIdentifiableStatisticalResource(), target);

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Other
        target.setDatasetVersionUrn(source.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn());

        return target;
    }

    @Override
    public DimensionRepresentationMappingDto dimensionRepresentationMappingDoToDto(DimensionRepresentationMapping source) throws MetamacException {
        if (source == null) {
            return null;
        }

        DimensionRepresentationMappingDto target = new DimensionRepresentationMappingDto();
        target.setId(source.getId());
        target.setDatasourceFilename(source.getDatasourceFilename());
        target.setMapping(DatasetVersionUtils.dimensionRepresentationMapFromString(source.getMapping()));

        return target;
    }

    @Override
    public List<DimensionRepresentationMappingDto> dimensionRepresentationMappingDoToDtoList(List<DimensionRepresentationMapping> sources) throws MetamacException {
        List<DimensionRepresentationMappingDto> targets = new ArrayList<DimensionRepresentationMappingDto>();
        for (DimensionRepresentationMapping source : sources) {
            targets.add(dimensionRepresentationMappingDoToDto(source));
        }
        return targets;
    }

    // ---------------------------------------------------------------------------------------------------------
    // DATASETS
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public RelatedResourceDto datasetVersionDoToDatasetRelatedResourceDto(DatasetVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        datasetVersionDoToDatasetRelatedResourceDto(source, target);
        return target;
    }

    @Override
    public RelatedResourceDto datasetVersionDoToDatasetVersionRelatedResourceDto(DatasetVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        datasetVersionDoToDatasetVersionRelatedResourceDto(source, target);
        return target;
    }

    private RelatedResourceDto datasetVersionDoToDatasetRelatedResourceDto(DatasetVersion source, RelatedResourceDto target) {
        if (source == null) {
            return null;
        }

        // Identity
        target.setId(source.getDataset().getId());
        target.setVersion(source.getDataset().getVersion());

        // Type
        target.setType(TypeRelatedResourceEnum.DATASET);

        // Identifiable Fields
        target.setCode(source.getDataset().getIdentifiableStatisticalResource().getCode());
        target.setCodeNested(null);
        target.setUrn(source.getDataset().getIdentifiableStatisticalResource().getUrn());

        // Nameable Fields
        target.setTitle(internationalStringDoToDto(source.getSiemacMetadataStatisticalResource().getTitle()));

        return target;
    }

    private RelatedResourceDto datasetVersionDoToDatasetVersionRelatedResourceDto(DatasetVersion source, RelatedResourceDto target) {
        if (source == null) {
            return null;
        }

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Type
        target.setType(TypeRelatedResourceEnum.DATASET_VERSION);

        // Identifiable Fields
        target.setCode(source.getSiemacMetadataStatisticalResource().getCode());
        target.setCodeNested(null);
        target.setUrn(source.getSiemacMetadataStatisticalResource().getUrn());

        // Nameable Fields
        target.setTitle(internationalStringDoToDto(source.getSiemacMetadataStatisticalResource().getTitle()));

        return target;
    }

    // ---------------------------------------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public DatasetVersionBaseDto datasetVersionDoToBaseDto(ServiceContext ctx, DatasetVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        DatasetVersionBaseDto target = new DatasetVersionBaseDto();
        datasetVersionDoToBaseDto(ctx, source, target);
        return target;
    }

    private DatasetVersionBaseDto datasetVersionDoToBaseDto(ServiceContext ctx, DatasetVersion source, DatasetVersionBaseDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        siemacMetadataStatisticalResourceDoToBaseDto(source.getSiemacMetadataStatisticalResource(), target);

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        target.setRelatedDsd(externalItemDoToDto(source.getRelatedDsd()));
        target.setStatisticOfficiality(statisticOfficialityDo2Dto(source.getStatisticOfficiality()));
        target.setIsTaskInBackground(taskService.existsTaskForResource(ctx, source.getDataset().getIdentifiableStatisticalResource().getUrn()));

        return target;
    }

    @Override
    public DatasetVersionDto datasetVersionDoToDto(ServiceContext ctx, DatasetVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        DatasetVersionDto target = new DatasetVersionDto();
        datasetVersionDoToDto(ctx, source, target);
        return target;
    }

    private DatasetVersionDto datasetVersionDoToDto(ServiceContext ctx, DatasetVersion source, DatasetVersionDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy

        siemacMetadataStatisticalResourceDoToDto(source.getSiemacMetadataStatisticalResource(), target);

        // Siemac metadata that needs to be filled
        target.setIsReplacedByVersion(relatedResourceDoToDto(source.getLifeCycleStatisticalResource().getIsReplacedByVersion()));

        target.setIsReplacedBy(relatedResourceDoToDto(source.getSiemacMetadataStatisticalResource().getIsReplacedBy()));
        target.getIsPartOf().clear();
        target.getIsPartOf().addAll(relatedResourceResultCollectionToDtoCollection(datasetVersionRepository.retrieveIsPartOf(source)));

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        target.setDatasetRepositoryId(source.getDatasetRepositoryId());

        target.getGeographicGranularities().clear();
        target.getGeographicGranularities().addAll(externalItemDoCollectionToDtoCollection(source.getGeographicGranularities()));

        target.getTemporalGranularities().clear();
        target.getTemporalGranularities().addAll(externalItemDoCollectionToDtoCollection(source.getTemporalGranularities()));

        target.getStatisticalUnit().clear();
        target.getStatisticalUnit().addAll(externalItemDoCollectionToDtoCollection(source.getStatisticalUnit()));

        target.setDateStart(source.getDateStart());
        target.setDateEnd(source.getDateEnd());

        target.setRelatedDsd(externalItemDoToDto(source.getRelatedDsd()));

        target.setFormatExtentDimensions(source.getFormatExtentDimensions());
        target.setFormatExtentObservations(source.getFormatExtentObservations());

        target.setDateNextUpdate(source.getDateNextUpdate());
        target.setUpdateFrequency(externalItemDoToDto(source.getUpdateFrequency()));
        target.setStatisticOfficiality(statisticOfficialityDo2Dto(source.getStatisticOfficiality()));
        target.setBibliographicCitation(internationalStringDoToDto(source.getBibliographicCitation()));

        List<RelatedResourceResult> isRequiredBy = datasetVersionRepository.retrieveIsRequiredBy(source);
        target.getIsRequiredBy().clear();
        target.getIsRequiredBy().addAll(relatedResourceResultCollectionToDtoCollection(isRequiredBy));

        target.setIsTaskInBackground(taskService.existsTaskForResource(ctx, source.getDataset().getIdentifiableStatisticalResource().getUrn()) 
                || taskService.existsTaskImportAttributes(ctx, source.getDataset().getIdentifiableStatisticalResource().getUrn()));

        target.setKeepAllData(source.isKeepAllData());

        target.setDataSourceType(source.getDataSourceType());
        target.setDateLastTimeDataImport(dateDoToDto(source.getDateLastTimeDataImport()));

        target.setViewCode(source.getDataset().getViewCode());

        return target;
    }

    // ---------------------------------------------------------------------------------------------------------
    // STATISTIC OFFICIALITY
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public StatisticOfficialityDto statisticOfficialityDo2Dto(StatisticOfficiality source) {
        if (source == null) {
            return null;
        }

        StatisticOfficialityDto target = new StatisticOfficialityDto();

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Other
        target.setIdentifier(source.getIdentifier());
        target.setDescription(internationalStringDoToDto(source.getDescription()));

        return target;
    }

    @Override
    public List<StatisticOfficialityDto> statisticOfficialityDoList2DtoList(List<StatisticOfficiality> sources) throws MetamacException {
        List<StatisticOfficialityDto> targets = new ArrayList<StatisticOfficialityDto>();
        for (StatisticOfficiality source : sources) {
            targets.add(statisticOfficialityDo2Dto(source));
        }
        return targets;
    }

    // ---------------------------------------------------------------------------------------------------------
    // CODE ITEM
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public CodeItemDto codeDimensionDoToCodeItemDto(CodeDimension source) throws MetamacException {
        if (source == null) {
            return null;
        }
        CodeItemDto dto = new CodeItemDto();
        dto.setCode(source.getIdentifier());
        dto.setTitle(source.getTitle());
        return dto;
    }

    // ---------------------------------------------------------------------------------------------------------
    // CODE DIMENSION
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public List<CodeItemDto> codeDimensionDoListToCodeItemDtoList(List<CodeDimension> sources) throws MetamacException {
        List<CodeItemDto> targets = new ArrayList<CodeItemDto>();
        for (CodeDimension source : sources) {
            targets.add(codeDimensionDoToCodeItemDto(source));
        }
        return targets;
    }

    // ---------------------------------------------------------------------------------------------------------
    // CATEGORISATIONS
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public CategorisationDto categorisationDoToDto(Categorisation source) throws MetamacException {
        if (source == null) {
            return null;
        }
        CategorisationDto target = new CategorisationDto();
        categorisationDoToDto(source, target);
        return target;
    }

    @Override
    public List<CategorisationDto> categorisationDoListToDtoList(List<Categorisation> sources) throws MetamacException {
        List<CategorisationDto> targets = new ArrayList<CategorisationDto>();
        for (Categorisation source : sources) {
            targets.add(categorisationDoToDto(source));
        }
        return targets;
    }

    private CategorisationDto categorisationDoToDto(Categorisation source, CategorisationDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        versionableStatisticalResourceDoToDto(source.getVersionableStatisticalResource(), target);

        // Following metadata can be overrided by categorisation. Otherwise, they are copied from dataset
        target.setValidFrom(dateDoToDto(source.getValidFromEffective()));
        target.setValidTo(dateDoToDto(source.getValidToEffective()));

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Other
        target.setDatasetVersion(lifecycleStatisticalResourceDoToRelatedResourceDto(source.getDatasetVersion().getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION));
        target.setCategory(externalItemDoToDto(source.getCategory()));
        target.setMaintainer(externalItemDoToDto(source.getMaintainer()));

        return target;
    }
}
