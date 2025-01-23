package org.siemac.metamac.statistical.resources.core.query.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.domain.DimensionOrder;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.PurposeDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.Purpose;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("queryDo2DtoMapper")
public class QueryDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements QueryDo2DtoMapper {

    @Autowired
    private QueryVersionRepository   queryVersionRepository;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    @Autowired
    private SrmRestInternalService   srmRestInternalService;

    @Autowired
    private TaskService              taskService;

    // ---------------------------------------------------------------------------------------------------------
    // QUERY VERSION
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public RelatedResourceDto queryVersionDoToQueryRelatedResourceDto(QueryVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        queryVersionDoToQueryRelatedResourceDto(source, target);
        return target;
    }

    private RelatedResourceDto queryVersionDoToQueryRelatedResourceDto(QueryVersion source, RelatedResourceDto target) {
        if (source == null) {
            return null;
        }

        // Identity
        target.setId(source.getQuery().getId());
        target.setVersion(source.getQuery().getVersion());

        // Type
        target.setType(TypeRelatedResourceEnum.QUERY);

        // Identifiable Fields
        target.setCode(source.getQuery().getIdentifiableStatisticalResource().getCode());
        target.setCodeNested(null);
        target.setUrn(source.getQuery().getIdentifiableStatisticalResource().getUrn());

        // Nameable Fields
        target.setTitle(internationalStringDoToDto(source.getLifeCycleStatisticalResource().getTitle()));

        return target;
    }

    @Override
    public QueryVersionDto queryVersionDoToDto(QueryVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        QueryVersionDto target = new QueryVersionDto();
        queryVersionDoToDto(source, target);
        return target;
    }

    @Override
    public List<QueryVersionBaseDto> queryVersionDoListToDtoList(ServiceContext ctx, List<QueryVersion> sources) throws MetamacException {
        List<QueryVersionBaseDto> targets = new ArrayList<QueryVersionBaseDto>();
        for (QueryVersion source : sources) {
            targets.add(queryVersionDoToBaseDto(ctx, source));
        }
        return targets;
    }

    @Override
    public QueryVersionBaseDto queryVersionDoToBaseDto(ServiceContext ctx, QueryVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }
        QueryVersionBaseDto target = new QueryVersionBaseDto();
        queryVersionDoToBaseDto(ctx, source, target);
        return target;
    }

    private QueryVersionBaseDto queryVersionDoToBaseDto(ServiceContext ctx, QueryVersion source, QueryVersionBaseDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        lifeCycleStatisticalResourceDoToBaseDto(source.getLifeCycleStatisticalResource(), target);

        // DatasetVersion
        DatasetVersion datasetVersion = getCurrentDatasetVersionInQuery(source);
        if (datasetVersion != null) {
            target.setRelatedDatasetVersion(lifecycleStatisticalResourceDoToRelatedResourceDto(datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION));
        }

        // Status
        target.setStatus(source.getStatus());

        // Type
        target.setType(source.getType());

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());
        target.setXStreamStatus(source.getLifeCycleStatisticalResource().getXStreamStatus());

        target.setIsTaskInBackground(taskService.existsTaskForResource(ctx, source.getQuery().getIdentifiableStatisticalResource().getUrn()));

        return target;
    }
    private QueryVersionDto queryVersionDoToDto(QueryVersion source, QueryVersionDto target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Hierarchy
        lifeCycleStatisticalResourceDoToDto(source.getLifeCycleStatisticalResource(), target);

        // DatasetVersion
        DatasetVersion datasetVersion = getCurrentDatasetVersionInQuery(source);
        if (datasetVersion != null) {
            target.setRelatedDatasetVersion(lifecycleStatisticalResourceDoToRelatedResourceDto(datasetVersion.getSiemacMetadataStatisticalResource(), TypeRelatedResourceEnum.DATASET_VERSION));
        }

        // Status
        target.setStatus(source.getStatus());

        // Type
        target.setType(source.getType());

        // Latest data number
        target.setLatestDataNumber(source.getLatestDataNumber());

        // Selection
        target.setSelection(selectionDo2Dto(source.getSelection(), target));

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        target.setIsReplacedByVersion(relatedResourceDoToDto(source.getLifeCycleStatisticalResource().getIsReplacedByVersion()));

        List<RelatedResourceResult> isPartOf = queryVersionRepository.retrieveIsPartOf(source);
        target.getIsPartOf().clear();
        target.getIsPartOf().addAll(relatedResourceResultCollectionToDtoCollection(isPartOf));
        setHeadingAndStubDimension(target, source);
        target.setPurpose(purposeDo2Dto(source.getPurposes()));
        target.setXTemplate(source.getXTemplate());
        return target;
    }

    private void setHeadingAndStubDimension(QueryVersionDto target, QueryVersion source) throws MetamacException {
        String dsdUrn = "";
        if (source.getDataset() != null && source.getDataset().getVersions() != null && !source.getDataset().getVersions().isEmpty()) {
            dsdUrn = source.getDataset().getVersions().get(0).getRelatedDsd().getUrn();
        }

        if (dsdUrn.isEmpty() && source.getFixedDatasetVersion() != null && source.getFixedDatasetVersion().getRelatedDsd() != null) {
            dsdUrn = source.getFixedDatasetVersion().getRelatedDsd().getUrn();
        }

        DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
        if (source.getStubDimensions() != null && !source.getStubDimensions().isEmpty() || (source.getHeadingDimensions() != null && !source.getHeadingDimensions().isEmpty())) {
            target.getHeadingDimensions().addAll(getDatasetDimension(source.getHeadingDimensions(), dsd.getDataStructureComponents().getDimensions().getDimensions()));
            target.getStubDimensions().addAll(getDatasetDimension(source.getStubDimensions(), dsd.getDataStructureComponents().getDimensions().getDimensions()));
        } else {
            if (dsd != null && dsd.getStub() != null && dsd.getStub().getDimensions() != null) {
                target.getStubDimensions().addAll(getDsdDimensions(dsd, dsd.getStub().getDimensions()));
            }
            if (dsd != null && dsd.getHeading() != null && dsd.getHeading().getDimensions() != null) {
                target.getHeadingDimensions().addAll(getDsdDimensions(dsd, dsd.getHeading().getDimensions()));
            }
        }
    }

    private List<RelatedResourceDto> getDatasetDimension(List<DimensionOrder> dimensionsOrder, List<DimensionBase> dimensions) {
        List<RelatedResourceDto> relatedResources = new ArrayList<>();
        for (DimensionOrder dimensionOrder : dimensionsOrder) {
            RelatedResourceDto relatedResource = new RelatedResourceDto();
            DimensionBase dimensionBase = getDimensionByUrn(dimensionOrder.getUrnDimComponentFk(), dimensions);
            relatedResource.setId(dimensionOrder.getId());
            relatedResource.setCode(dimensionBase.getId());
            relatedResource.setUrn(dimensionOrder.getUrnDimComponentFk());
            relatedResources.add(relatedResource);
        }
        return relatedResources;
    }

    private List<RelatedResourceDto> getDsdDimensions(DataStructure dsd, List<String> dimensionsName) {
        List<RelatedResourceDto> dimensions = new ArrayList<>();
        List<DimensionBase> dimensionsBase = dsd.getDataStructureComponents().getDimensions().getDimensions();
        for (String dimensionName : dimensionsName) {
            DimensionBase dimensionBase = getDimensionByName(dimensionName, dimensionsBase);
            if (dimensionBase != null) {
                RelatedResourceDto target = new RelatedResourceDto();
                target.setCode(dimensionBase.getId());
                target.setUrn(dimensionBase.getUrn());
                dimensions.add(target);
            }
        }

        return dimensions;
    }

    private DimensionBase getDimensionByName(String dimensionName, List<DimensionBase> dimensions) {
        for (DimensionBase dimensionBase : dimensions) {
            if (dimensionBase.getId().equals(dimensionName)) {
                return dimensionBase;
            }
        }
        return null;
    }

    private DimensionBase getDimensionByUrn(String urn, List<DimensionBase> dimensions) {
        for (DimensionBase dimensionBase : dimensions) {
            if (dimensionBase.getUrn().equals(urn)) {
                return dimensionBase;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------
    // SELECTION
    // ---------------------------------------------------------------------------------------------------------

    private Map<String, List<CodeItemDto>> selectionDo2Dto(List<QuerySelectionItem> source, QueryVersionDto target) {
        Map<String, List<CodeItemDto>> result = new HashMap<String, List<CodeItemDto>>();
        for (QuerySelectionItem querySelectionItem : source) {
            List<CodeItemDto> codesResult = new ArrayList<CodeItemDto>();
            for (CodeItem codeItem : querySelectionItem.getCodes()) {
                codesResult.add(codeItemDo2Dto(codeItem));
            }

            result.put(querySelectionItem.getDimension(), codesResult);
        }
        return result;
    }

    private CodeItemDto codeItemDo2Dto(CodeItem source) {
        return new CodeItemDto(source.getCode(), source.getTitle());
    }

    private DatasetVersion getCurrentDatasetVersionInQuery(QueryVersion queryVersion) throws MetamacException {
        if (queryVersion.getFixedDatasetVersion() != null) {
            return queryVersion.getFixedDatasetVersion();
        } else if (queryVersion.getDataset() != null) {
            return datasetVersionRepository.retrieveLastVersion(queryVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        }
        return null;
    }

    @Override
    public List<PurposeDto> purposeDoListToDtoList(List<Purpose> sources) throws MetamacException {
        List<PurposeDto> targets = new ArrayList<>();
        
        for (Purpose source : sources) {
            targets.add(purposeDo2Dto(source));
        }
        return targets;
    }
    
    // ---------------------------------------------------------------------------------------------------------
    // STATISTIC OFFICIALITY
    // ---------------------------------------------------------------------------------------------------------

    @Override
    public PurposeDto purposeDo2Dto(Purpose source) {
        if (source == null) {
            return null;
        }

        PurposeDto target = new PurposeDto();

        // Identity
        target.setId(source.getId());
        target.setVersion(source.getVersion());

        // Other
        target.setIdentifier(source.getIdentifier());
        target.setDescription(internationalStringDoToDto(source.getDescription()));

        return target;
    }
}
