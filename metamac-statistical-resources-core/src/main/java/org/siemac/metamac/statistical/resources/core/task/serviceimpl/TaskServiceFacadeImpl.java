package org.siemac.metamac.statistical.resources.core.task.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.TaskStatusTypeEnum;
import org.siemac.metamac.statistical.resources.core.task.domain.Task;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.security.DatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskProperties;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.dataset.repository.dto.Mapping;

/**
 * Implementation of TaskServiceFacade.
 */
@Service("taskServiceFacade")
public class TaskServiceFacadeImpl extends TaskServiceFacadeImplBase {

    private static Logger logger = LoggerFactory.getLogger(TaskServiceFacadeImpl.class);

    @Autowired
    private TaskService   taskservice;

    public TaskServiceFacadeImpl() {
    }

    @Override
    public void executeImportationTask(ServiceContext ctx, String importationJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskservice.processImportationTask(ctx, importationJobKey, taskInfoDataset);
    }

    @Override
    public void executeDatabaseImportationTask(ServiceContext ctx, String databaseImportationJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskservice.processDatabaseImportationTask(ctx, databaseImportationJobKey, taskInfoDataset);
    }

    @Override
    public void executeRecoveryImportationTask(ServiceContext ctx, String recoveryJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskservice.processRollbackImportationTask(ctx, recoveryJobKey, taskInfoDataset);
    }

    @Override
    public void executeDuplicationTask(ServiceContext ctx, String duplicationJobKey, TaskInfoDataset taskInfoDataset, String newDatasetId, List<Mapping> datasourceMappings) throws MetamacException {
        taskservice.processDuplicationTask(ctx, duplicationJobKey, taskInfoDataset, newDatasetId, datasourceMappings);
    }

    @Override
    public void executeUpdateGeocoverageCacheTask(ServiceContext ctx, String jobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskservice.processUpdateGeocoverageCacheTask(ctx, jobKey, taskInfoDataset);
    }

    @Override
    public void executeUpdateGeographicalCacheRelatedResourceTask(ServiceContext ctx, String jobKey, TaskInfoResources taskInfoResource) throws MetamacException {
        taskservice.processUpdateGeographicalCacheRelatedResourceTask(ctx, jobKey, taskInfoResource);
    }

    @Override
    public void executeUpdateExternalGeocoverageCacheTask(ServiceContext ctx, String jobKey, TaskInfoResources taskInfoResource) throws MetamacException {
        taskservice.processUpdateExternalGeocoverageCacheTask(ctx, jobKey, taskInfoResource);
    }

    @Override
    public void executeDatabaseDatasetPollingTask(ServiceContext ctx) throws MetamacException {
        taskservice.processDatabaseDatasetPollingTask(ctx);
    }

    @Override
    public void executeGeographicCoverageCacheClearTask(ServiceContext ctx) throws MetamacException {
        taskservice.processGeographicCoverageCacheClearTask(ctx);
    }

    @Override
    public void markTaskAsFailed(ServiceContext ctx, String job, String datasetVersionId, String datasetUrn, Exception exception) throws MetamacException {
        taskservice.markTaskAsFailed(ctx, job, datasetVersionId, datasetUrn);
    }

    @Override
    public void markTaskAsFinished(ServiceContext ctx, String job) throws MetamacException {
        taskservice.markTaskAsFinished(ctx, job);
    }

    @Override
    // This method is only used on application startup
    public void markAllInProgressTaskToFailed(ServiceContext ctx) throws MetamacException {
        // Mark as failed current IN_PROGRESS states, if are available and also FAILED in order to retry the recovery process
        List<ConditionalCriteria> conditionList = ConditionalCriteriaBuilder.criteriaFor(Task.class).withProperty(TaskProperties.status()).eq(TaskStatusTypeEnum.IN_PROGRESS).or()
                .withProperty(TaskProperties.status()).eq(TaskStatusTypeEnum.FAILED).build();
        PagedResult<Task> pagedResult = taskservice.findTasksByCondition(ctx, conditionList, PagingParameter.noLimits());

        for (Task task : pagedResult.getValues()) {
            logger.info("Recovering task " + task.getJob() + " of the user " + task.getCreatedBy());
            taskservice.markTasksAsFailedOnApplicationStartup(ctx, task.getJob());
        }
    }

    @Override
    public void scheduleDatabaseDatasetPollingJob(ServiceContext ctx) {
        taskservice.scheduleDatabaseDatasetPollingJob(ctx);

    }

    @Override
    public void scheduleGeographicCoverageCacheClearJob(ServiceContext ctx) {
        taskservice.scheduleGeographicCoverageCacheClearJob(ctx);

    }

    @Override
    public void sendDatabaseImportationErrorNotification(ServiceContext ctx, String datasetVersionUrn, MetamacException metamacException) {
        taskservice.sendDatabaseImportationErrorNotification(ctx, datasetVersionUrn, metamacException);
    }

    @Override
    public void importAttributesInDatasetVersion(ServiceContext ctx, String dataVersionUrn, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskservice.importAttributesInDatasetVersion(ctx, dataVersionUrn, taskInfoDataset);
    }

    @Override
    public void executeRecoveryImportationAttributes(ServiceContext ctx, String recoveryJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskservice.processRollbackImportationAttributesTask(ctx, recoveryJobKey, taskInfoDataset);
    }

    @Override
    public void executeResendPublishedDatasetsKafkaMessageTask(ServiceContext ctx) throws MetamacException {
        taskservice.processResendKafkaDatasetMessageTask(ctx);
    }

    @Override
    public void scheduleResendKafkaDatasetMessageJob(ServiceContext ctx) {
        taskservice.scheduleResendKafkaDatasetMessageJob(ctx);
    }

    @Override
    public void executeReloadKafkaTopicDatasetTask(ServiceContext ctx) throws MetamacException {
        taskservice.processReloadKafkaTopicDatasetTask(ctx);
    }

    @Override
    public void executeReloadKafkaTopicPublicationTask(ServiceContext ctx) throws MetamacException {
        taskservice.processReloadKafkaTopicPublicationTask(ctx);
    }

    @Override
    public void executeReloadKafkaTopicQueryTask(ServiceContext ctx) throws MetamacException {
        taskservice.processReloadKafkaTopicQueryTask(ctx);
    }

    @Override
    public void scheduleReloadTopicDatasetJob(ServiceContext ctx) throws MetamacException {
        DatasetsSecurityUtils.canResendAllKafkaMessages(ctx);
        taskservice.scheduleReloadTopicDatasetJob(ctx);
    }

    @Override
    public void scheduleReloadTopicPublicationJob(ServiceContext ctx) throws MetamacException {
        DatasetsSecurityUtils.canResendAllKafkaMessages(ctx);
        taskservice.scheduleReloadTopicPublicationJob(ctx);
    }

    @Override
    public void scheduleReloadTopicQueryJob(ServiceContext ctx) throws MetamacException {
        DatasetsSecurityUtils.canResendAllKafkaMessages(ctx);
        taskservice.scheduleReloadTopicQueryJob(ctx);
    }

    @Override
    public boolean existsGeoCacheTasksByTaskName(ServiceContext ctx, List<String> taskNames) throws MetamacException {
        return taskservice.existsGeoCacheTasksByTaskName(ctx, taskNames);
    }

    @Override
    public void executeRecoveryGeographicalCacheTask(ServiceContext ctx, String recoveryJobKey, TaskInfoResources taskInfoResource) throws MetamacException {
        taskservice.processRetryGeographicalCacheTask(ctx, recoveryJobKey, taskInfoResource);
    }

    @Override
    public void executeUpdateResourceBusinessLastUpdateTask(ServiceContext ctx, String taskName, String resourceUrn, String resourceRootUrn, String resourceType, long timestamp) throws MetamacException {
        taskservice.processUpdateResourceBusinessLastUpdate(ctx, taskName, resourceUrn, resourceRootUrn, resourceType, timestamp);
    }

    @Override
    public void executeUpdateResourceLastUpdateTask(ServiceContext ctx, String taskName, String resourceUrn, long timestamp) throws MetamacException {
        taskservice.processUpdateResourceLastUpdateTask(ctx, taskName, resourceUrn, timestamp);
    }

}
