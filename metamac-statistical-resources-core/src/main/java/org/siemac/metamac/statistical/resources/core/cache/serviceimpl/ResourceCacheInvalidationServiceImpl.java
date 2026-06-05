package org.siemac.metamac.statistical.resources.core.cache.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.cache.serviceapi.ResourceCacheInvalidationService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingService;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceCacheInvalidationServiceImpl implements ResourceCacheInvalidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceCacheInvalidationServiceImpl.class);

    @Autowired
    private StreamMessagingService<String, SpecificRecordBase> messagingService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StatisticalResourceRepository statisticalResourceRepository;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    @Autowired
    private QueryVersionRepository queryVersionRepository;

    @Override
    public void updateResourceLastUpdate(ServiceContext ctx, LifeCycleStatisticalResource resource, long timestamp) throws MetamacException {
        DateTime dateTime = new DateTime(timestamp);
        resource.setLastUpdated(dateTime);
        resource.setLastUpdatedBy("system");
        resource.setPatch(resource.getPatch() + 1);
        statisticalResourceRepository.save(resource);
        messagingService.sendMessage(resource);
    }

    @Override
    public void updateDatasetVersionsLastUpdateByDsd(ServiceContext ctx, String dsdUrn, long timestamp) {
        LOGGER.info("Updating resources by DSD {}", dsdUrn);
        if (dsdUrn == null || dsdUrn.trim().isEmpty()) {
            return;
        }

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class)
                                                                         .withProperty(DatasetVersionProperties.relatedDsd().urn()).eq(dsdUrn)
                                                                         .distinctRoot()
                                                                         .build();

        List<DatasetVersion> affectedDatasets = datasetVersionRepository.findByCondition(conditions);
        planifyAffectedResourcesLastUpdate(ctx, affectedDatasets, timestamp);
    }

    @Override
    public void updateDatasetVersionsLastUpdateByOperation(ServiceContext ctx, String operationUrn, long timestamp) {
        LOGGER.info("Updating resources by operation {}", operationUrn);
        if (operationUrn == null || operationUrn.trim().isEmpty()) {
            return;
        }

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class)
            .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().statisticalOperation().urn()).eq(operationUrn)
            .distinctRoot()
            .build();

        List<DatasetVersion> affectedDatasets = datasetVersionRepository.findByCondition(conditions);
        planifyAffectedResourcesLastUpdate(ctx, affectedDatasets, timestamp);
    }

    private void planifyAffectedResourcesLastUpdate(ServiceContext ctx, List<DatasetVersion> affectedDatasets, long timestamp) {
        Set<String> affectedUrns = new HashSet<String>();
        for (DatasetVersion dataset : affectedDatasets) {
            affectedUrns.add(dataset.getSiemacMetadataStatisticalResource().getUrn());
            String datasetUrn = dataset.getDataset().getIdentifiableStatisticalResource().getUrn();
            String datasetVersionUrn = dataset.getSiemacMetadataStatisticalResource().getUrn();
            List<String> queryUrns = getAffectedQueryUrns(datasetUrn, datasetVersionUrn);
            affectedUrns.addAll(queryUrns);
        }

        LOGGER.info("Planning last update job for {} affected resources", affectedUrns.size());
        for (String urn : affectedUrns) {
            try {
                taskService.planifyUpdateResourceLastUpdate(ctx, urn, timestamp, true);
            } catch (MetamacException e) {
                LOGGER.error("Failed to planify last update job for resource {}", urn, e);
            }
        }
    }

    private List<String> getAffectedQueryUrns(String datasetUrn, String datasetVersionUrn) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class)
                                                                         .withProperty(QueryVersionProperties.dataset().identifiableStatisticalResource().urn()).eq(datasetUrn)
                                                                         .or()
                                                                         .withProperty(QueryVersionProperties.fixedDatasetVersion().siemacMetadataStatisticalResource().urn()).eq(datasetVersionUrn)
                                                                         .distinctRoot()
                                                                         .build();

        List<QueryVersion> affectedQueries = queryVersionRepository.findByCondition(conditions);
        List<String> urns = new ArrayList<>();
        for (QueryVersion query : affectedQueries) {
            urns.add(query.getLifeCycleStatisticalResource().getUrn());
        }
        return urns;
    }
}
