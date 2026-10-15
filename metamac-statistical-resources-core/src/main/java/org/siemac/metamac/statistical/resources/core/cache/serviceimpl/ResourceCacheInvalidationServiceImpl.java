package org.siemac.metamac.statistical.resources.core.cache.serviceimpl;

import java.util.HashSet;
import java.util.Set;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.cache.serviceapi.ResourceCacheInvalidationService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingService;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceCacheInvalidationServiceImpl implements ResourceCacheInvalidationService {

    private static final Logger                                LOGGER = LoggerFactory.getLogger(ResourceCacheInvalidationServiceImpl.class);

    @Autowired
    private StreamMessagingService<String, SpecificRecordBase> messagingService;

    @Autowired
    private TaskService                                        taskService;

    @Autowired
    private StatisticalResourceRepository                      statisticalResourceRepository;

    @Autowired
    private DatasetVersionRepository                           datasetVersionRepository;

    @Autowired
    private QueryVersionRepository                             queryVersionRepository;

    @Override
    public void updateResourceLastUpdate(ServiceContext ctx, LifeCycleStatisticalResource resource, long timestamp) throws MetamacException {
        DateTime dateTime = new DateTime(timestamp);
        resource.setLastUpdated(dateTime);
        resource.setLastUpdatedBy("system");
        resource.setPatch(resource.getPatch() + 1);
        statisticalResourceRepository.save(resource);

        if (resource.isLastPublishedVersion()) {
            messagingService.sendMessage(resource);
        } else {
            LOGGER.info("Skipping stream message for resource {} because it is not the last published version", resource.getUrn());
        }
    }

    @Override
    public void updateDatasetVersionsLastUpdateByDsd(ServiceContext ctx, String dsdUrn, long timestamp) {
        LOGGER.info("Updating resources by DSD {}", dsdUrn);
        if (dsdUrn == null || dsdUrn.trim().isEmpty()) {
            return;
        }

        // TODO EDATOS-5431 El procesamiento solo de recursos externos está pendiente de ser revisado
        // dado que en el futuro cachearemos también la API interna y los recursos en otros estados
        Set<String> affectedUrns = new HashSet<>(datasetVersionRepository.findPublishedUrnsByRelatedDsdUrn(dsdUrn));
        affectedUrns.addAll(queryVersionRepository.findPublishedUrnsLinkedToPublishedDatasetVersionsByRelatedDsdUrn(dsdUrn));
        planifyAffectedResourcesLastUpdate(ctx, dsdUrn, affectedUrns, timestamp);
    }

    @Override
    public void updateDatasetVersionsLastUpdateByOperation(ServiceContext ctx, String operationUrn, long timestamp) {
        LOGGER.info("Updating resources by operation {}", operationUrn);
        if (operationUrn == null || operationUrn.trim().isEmpty()) {
            return;
        }

        // TODO EDATOS-5431 El procesamiento solo de recursos externos está pendiente de ser revisado
        // dado que en el futuro cachearemos también la API interna y los recursos en otros estados
        Set<String> affectedUrns = new HashSet<>(datasetVersionRepository.findPublishedUrnsByStatisticalOperationUrn(operationUrn));
        affectedUrns.addAll(queryVersionRepository.findPublishedUrnsLinkedToPublishedDatasetVersionsByStatisticalOperationUrn(operationUrn));
        planifyAffectedResourcesLastUpdate(ctx, operationUrn, affectedUrns, timestamp);
    }

    private void planifyAffectedResourcesLastUpdate(ServiceContext ctx, String sourceUrn, Set<String> affectedUrns, long timestamp) {
        if (affectedUrns.isEmpty()) {
            LOGGER.info("No last update jobs were planified for {} because there are no resources affected", sourceUrn);
            return;
        }

        LOGGER.info("Planning last update job for {} affected resources by {}", affectedUrns.size(), sourceUrn);
        for (String urn : affectedUrns) {
            try {
                taskService.planifyUpdateResourceLastUpdate(ctx, urn, timestamp, true);
            } catch (MetamacException e) {
                LOGGER.error("Failed to planify last update job for resource {}", urn, e);
            }
        }
    }
}
