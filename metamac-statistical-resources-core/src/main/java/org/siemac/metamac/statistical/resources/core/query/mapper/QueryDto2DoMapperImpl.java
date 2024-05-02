package org.siemac.metamac.statistical.resources.core.query.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.exception.QueryVersionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@org.springframework.stereotype.Component("queryDto2DoMapper")
public class QueryDto2DoMapperImpl extends BaseDto2DoMapperImpl implements QueryDto2DoMapper {

    @Autowired
    private QueryVersionRepository       queryVersionRepository;

    @Autowired
    private DatasetVersionRepository     datasetVersionRepository;

    @Autowired
    private QuerySelectionItemRepository querySelectionItemRepository;

    @Autowired
    private CodeItemRepository           codeItemRepository;

    @Autowired
    @Qualifier("commonDto2DoMapper")
    private CommonDto2DoMapper           dto2DoMapper;

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

        return target;
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
        return target;
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
