package org.siemac.metamac.statistical.resources.core.query.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.domain.DimensionOrder;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.PurposeDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.Purpose;
import org.siemac.metamac.statistical.resources.core.query.domain.PurposeRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.exception.PurposeNotFoundException;
import org.siemac.metamac.statistical.resources.core.query.exception.QueryVersionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import es.gobcan.istac.edatos.dataset.repository.dto.ConditionDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@org.springframework.stereotype.Component("queryDto2DoMapper")
public class QueryDto2DoMapperImpl extends BaseDto2DoMapperImpl implements QueryDto2DoMapper {

    @Autowired
    private QueryVersionRepository           queryVersionRepository;

    @Autowired
    private DatasetVersionRepository         datasetVersionRepository;

    @Autowired
    private QuerySelectionItemRepository     querySelectionItemRepository;

    @Autowired
    private CodeItemRepository               codeItemRepository;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private PurposeRepository                purposeRepository;

    @Autowired
    @Qualifier("commonDto2DoMapper")
    private CommonDto2DoMapper               dto2DoMapper;

    @Override
    public void checkOptimisticLocking(QueryVersionBaseDto source) throws MetamacException {
        if (source != null && source.getId() != null) {
            try {
                QueryVersion target = queryVersionRepository.findById(source.getId());
                if (target.getId() != null) {
                    OptimisticLockingUtils.checkVersion(target.getLifeCycleStatisticalResource().getVersion(), source.getOptimisticLockingVersion());
                }
            } catch (QueryVersionNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.QUERY_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }
    }

    @Override
    public QueryVersion queryVersionDtoToDo(QueryVersionDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        // If exists, retrieves existing entity. Otherwise, creates new entity.
        QueryVersion target = null;
        if (source.getId() == null) {
            target = new QueryVersion();
            target.setLifeCycleStatisticalResource(new LifeCycleStatisticalResource());
        } else {
            try {
                target = queryVersionRepository.findById(source.getId());
            } catch (QueryVersionNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.QUERY_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        queryVersionDtoToDo(source, target);
        try {
            checkPurpose(source, target);
        } catch (ApplicationException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withMessageParameters(source.getUrn()).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }

        return target;
    }

    private void checkPurpose(QueryVersionDto source, QueryVersion target) throws MetamacException, ApplicationException {
        if (source.getPurpose() != null) {
            List<ConditionDimensionDto> conditions = generateConditions(target.getSelection());
            DatasetVersion datasetVersion = getQueryRelatedDatasetVersionEffective(target);
            Map<String, ObservationExtendedDto> observations = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetVersion.getDatasetRepositoryId(), conditions);
            if (QueryTypeEnum.LATEST_DATA.equals(source.getType()) && source.getLatestDataNumber() > 1 && observations.size() > 1 && StatisticalResourcesConstants.SOCIAL_NETWORK_PURPOSE.equals(source.getPurpose().getIdentifier())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.QUERY_SOCIAL_NETWORK_NOT_UNIQUE_RESULT).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            if (StatisticalResourcesConstants.SOCIAL_NETWORK_PURPOSE.equals(source.getPurpose().getIdentifier()) && !QueryTypeEnum.LATEST_DATA.equals(source.getType())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.QUERY_SOCIAL_NETWORK_NOT_UNIQUE_RESULT).withMessageParameters(source.getUrn())
                .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }
    }

    private DatasetVersion getQueryRelatedDatasetVersionEffective(QueryVersion source) throws MetamacException {
        if (source.getFixedDatasetVersion() != null) {
            return source.getFixedDatasetVersion();
        } else {
            return datasetVersionRepository.retrieveLastVersion(source.getDataset().getIdentifiableStatisticalResource().getUrn());
        }
    }

    private List<ConditionDimensionDto> generateConditions(List<QuerySelectionItem> querySelectionItems) {
        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        for (QuerySelectionItem querySelectionItem : querySelectionItems) {
            ConditionDimensionDto conditionDimensionDto = new ConditionDimensionDto();
            conditionDimensionDto.setDimensionId(querySelectionItem.getDimension());
            for (CodeItem codeItem : querySelectionItem.getCodes()) {
                conditionDimensionDto.getCodesDimension().add(codeItem.getCode());
            }
            conditionDimensionDtos.add(conditionDimensionDto);
        }
        return conditionDimensionDtos;
    }

    private QueryVersion queryVersionDtoToDo(QueryVersionDto source, QueryVersion target) throws MetamacException {
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.QUERY).build();
        }

        // Hierarchy
        lifeCycleStatisticalResourceDtoToDo(source, target.getLifeCycleStatisticalResource(), ServiceExceptionParameters.QUERY_VERSION);

        // DatasetVersion
        if (source.getRelatedDatasetVersion() != null && source.getRelatedDatasetVersion().getUrn() != null) {
            DatasetVersion datasetVersionTarget = datasetVersionRepository.retrieveByUrn(source.getRelatedDatasetVersion().getUrn());
            DatasetVersion lastDatasetVersion = datasetVersionRepository.retrieveLastVersion(datasetVersionTarget.getDataset().getIdentifiableStatisticalResource().getUrn());

            if (datasetVersionTarget.getSiemacMetadataStatisticalResource().getUrn().equals(lastDatasetVersion.getSiemacMetadataStatisticalResource().getUrn())) {
                target.setDataset(datasetVersionTarget.getDataset());
                target.setFixedDatasetVersion(null);
            } else {
                target.setDataset(null);
                target.setFixedDatasetVersion(datasetVersionTarget);
            }
        }

        // Status
        // Not mapped. It's automatically managed.

        // Type
        target.setType(source.getType());

        // Latest Data Number
        target.setLatestDataNumber(source.getLatestDataNumber());

        dto2DoMapper.externalItemDtoCollectionToDoList(source.getTemporalGranularities(), target.getTemporalGranularities(), ServiceExceptionParameters.DATASET_VERSION__TEMPORAL_GRANULARITIES);
        // Selection
        List<QuerySelectionItem> targetItems = new ArrayList<QuerySelectionItem>(target.getSelection());
        targetItems = querySelectionDto2Do(source.getSelection(), targetItems, target, ServiceExceptionParameters.QUERY_VERSION__SELECTION);
        target.getSelection().clear();
        for (QuerySelectionItem item : targetItems) {
            target.addSelection(item);
        }
        target.getHeadingDimensions().addAll(getHeadingDimension(source.getHeadingDimensions(), target));
        target.getStubDimensions().addAll(getStubDimension(source.getStubDimensions(), target));
        target.setPurposes(purposeDtoToDo(source.getPurpose()));
        target.setXTemplate(internationalStringDtoToDo(source.getXTemplateDto(), target.getXTemplate(), ServiceExceptionParameters.QUERY_VERSION));
        return target;
    }

    private Purpose purposeDtoToDo(PurposeDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        try {
            return purposeRepository.findById(source.getId());
        } catch (PurposeNotFoundException e) {
            throw new MetamacException(ServiceExceptionType.PURPOSE_NOT_FOUND, source.getId());
        }
    }

    private List<DimensionOrder> getHeadingDimension(List<RelatedResourceDto> relatedResources, QueryVersion target) {
        target.getHeadingDimensions().clear();
        int count = 1;
        List<DimensionOrder> dimensionsOrder = new ArrayList<>();
        for (RelatedResourceDto relatedResource : relatedResources) {
            DimensionOrder dimensionOrder = new DimensionOrder();
            dimensionOrder.setDimOrder(count);
            dimensionOrder.setUrnDimComponentFk(relatedResource.getUrn());
            dimensionOrder.setQueryVersionHeading(target);
            dimensionsOrder.add(dimensionOrder);
            count++;
        }
        return dimensionsOrder;
    }

    private List<DimensionOrder> getStubDimension(List<RelatedResourceDto> relatedResources, QueryVersion target) {
        target.getStubDimensions().clear();
        int count = 1;
        List<DimensionOrder> dimensionsOrder = new ArrayList<>();
        for (RelatedResourceDto relatedResource : relatedResources) {
            DimensionOrder dimensionOrder = new DimensionOrder();
            dimensionOrder.setDimOrder(count);
            dimensionOrder.setUrnDimComponentFk(relatedResource.getUrn());
            dimensionOrder.setQueryVersionStub(target);
            dimensionsOrder.add(dimensionOrder);
            count++;
        }
        return dimensionsOrder;
    }

    private List<QuerySelectionItem> querySelectionDto2Do(Map<String, List<CodeItemDto>> source, List<QuerySelectionItem> target, QueryVersion queryTarget, String metadataName) {
        if (source.isEmpty()) {
            if (!target.isEmpty()) {
                // Delete old entities
                deleteQuerySelectionItemList(target);
            }
            return new ArrayList<QuerySelectionItem>();
        }

        if (target.isEmpty()) {
            target = new ArrayList<QuerySelectionItem>();
        }

        List<QuerySelectionItem> querySelectionItemEntities = querySelectionItemListDto2Do(source, target, queryTarget);
        target.clear();
        target.addAll(querySelectionItemEntities);

        return target;

    }

    private List<QuerySelectionItem> querySelectionItemListDto2Do(Map<String, List<CodeItemDto>> source, List<QuerySelectionItem> targets, QueryVersion queryTarget) {
        List<QuerySelectionItem> targetsBefore = targets;
        targets = new ArrayList<QuerySelectionItem>();

        for (Map.Entry<String, List<CodeItemDto>> sourceItem : source.entrySet()) {
            boolean existsBefore = false;
            for (QuerySelectionItem targetItem : targetsBefore) {
                if (sourceItem.getKey().equals(targetItem.getDimension())) {
                    targets.add(querySelectionItemDto2Do(sourceItem, targetItem, queryTarget));
                    existsBefore = true;
                    break;
                }
            }

            if (!existsBefore) {
                targets.add(querySelectionItemDto2Do(sourceItem, queryTarget));
            }
        }
        return targets;
    }

    private QuerySelectionItem querySelectionItemDto2Do(Entry<String, List<CodeItemDto>> sourceItem, QueryVersion queryTarget) {
        QuerySelectionItem target = new QuerySelectionItem();
        querySelectionItemDto2Do(sourceItem, target, queryTarget);
        return target;
    }

    private QuerySelectionItem querySelectionItemDto2Do(Entry<String, List<CodeItemDto>> sourceItem, QuerySelectionItem targetItem, QueryVersion queryTarget) {
        targetItem.setQueryVersion(queryTarget);
        targetItem.setDimension(sourceItem.getKey());

        // Set codes
        List<CodeItem> codeItemsBefore = targetItem.getCodes();

        // Clear codes add new ones
        for (CodeItem codeItem : codeItemsBefore) {
            codeItemRepository.delete(codeItem);
        }

        targetItem.getCodes().clear();

        for (CodeItemDto value : sourceItem.getValue()) {
            targetItem.addCode(codeItemDto2Do(value, targetItem));
        }

        return targetItem;
    }

    private CodeItem codeItemDto2Do(CodeItemDto value, QuerySelectionItem targetItem) {
        CodeItem target = new CodeItem();
        codeItemDto2Do(value, target, targetItem);
        return target;
    }

    private CodeItem codeItemDto2Do(CodeItemDto source, CodeItem target, QuerySelectionItem targetItem) {
        target.setCode(source.getCode());
        target.setTitle(source.getTitle());
        target.setQuerySelectionItem(targetItem);
        return target;
    }

    private void deleteQuerySelectionItemList(List<QuerySelectionItem> target) {
        for (QuerySelectionItem querySelectionItem : target) {
            querySelectionItemRepository.delete(querySelectionItem);
        }
    }
}
