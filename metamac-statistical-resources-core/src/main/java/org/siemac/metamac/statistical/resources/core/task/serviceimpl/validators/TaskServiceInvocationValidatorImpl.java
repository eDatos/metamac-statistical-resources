package org.siemac.metamac.statistical.resources.core.task.serviceimpl.validators;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.Task;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesValidationUtils;

import es.gobcan.istac.edatos.dataset.repository.dto.Mapping;

public class TaskServiceInvocationValidatorImpl {

    public static void checkPlanifyImportationDataset(TaskInfoDataset taskInfoDataset, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset, ServiceExceptionParameters.TASK_INFO_DATASET, exceptions);

        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getFiles(), ServiceExceptionParameters.TASK_INFO_DATASET_FILES, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDataStructureUrn(), ServiceExceptionParameters.TASK_INFO_DATASET_DSD_URN, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDatasetVersionId(), ServiceExceptionParameters.TASK_INFO_DATASET_DATASET_VERSION_ID, exceptions);
        checkImportDataSourcesInStatisticalOperationWithAutomaticLifeCicle(taskInfoDataset, exceptions);

        for (FileDescriptor fileDescriptorDto : taskInfoDataset.getFiles()) {
            StatisticalResourcesValidationUtils.checkMetadataRequired(fileDescriptorDto.getFile(), ServiceExceptionParameters.FILE_DESCRIPTOR_INPUT_MESSAGE, exceptions);
            StatisticalResourcesValidationUtils.checkMetadataRequired(fileDescriptorDto.getFileName(), ServiceExceptionParameters.FILE_DESCRIPTOR_FILE_NAME, exceptions);
            StatisticalResourcesValidationUtils.checkMetadataRequired(fileDescriptorDto.getDatasetFileFormatEnum(), ServiceExceptionParameters.FILE_DESCRIPTOR_FORMAT, exceptions);
        }
    }

    private static void checkImportDataSourcesInStatisticalOperationWithAutomaticLifeCicle(TaskInfoDataset taskInfoDataset, List<MetamacExceptionItem> exceptions) {
        if (Boolean.TRUE.equals(taskInfoDataset.getDatasetAutomaticLifeCicle())) {
            StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset.getDatasetVersionRationaleTypes(), ServiceExceptionParameters.DATASET_VERSION_RATIONALE_TYPES, exceptions);
            StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset.getDatasetNextVersion(), ServiceExceptionParameters.DATASET_NEXT_VERSION, exceptions);
            if (NextVersionTypeEnum.SCHEDULED_UPDATE.equals(NextVersionTypeEnum.valueOf(taskInfoDataset.getDatasetNextVersion()))) {
                StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset.getDatasetNextVersionDate(), ServiceExceptionParameters.DATASET_DATE_NEXT_VERSION, exceptions);
                StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset.getDatasetNextUpdateDate(), ServiceExceptionParameters.DATASET_DATE_NEXT_UPDATE, exceptions);

            }
            StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset.getDatasetNextProcStatus(), ServiceExceptionParameters.DATASET_NEXT_PROC_STATUS, exceptions);
            
        }
    }
    
    public static void checkPlanifyRecoveryImportDataset(TaskInfoDataset taskInfoDataset, Boolean notifyToUser, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset, ServiceExceptionParameters.TASK_INFO_DATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDatasetVersionId(), ServiceExceptionParameters.TASK_INFO_DATASET_DATASET_VERSION_ID, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(notifyToUser, ServiceExceptionParameters.NOTIFY_TO_USER, exceptions);
    }

    public static void checkPlanifyDuplicationDataset(TaskInfoDataset taskInfoDataset, String newDatasetId, List<Mapping> mappings, List<MetamacExceptionItem> exceptions) {
        checkPlanifyDuplicationDataset(taskInfoDataset, newDatasetId, exceptions);
    }

    public static void checkPlanifyDuplicationDataset(TaskInfoDataset taskInfoDataset, String newDatasetId, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset, ServiceExceptionParameters.TASK_INFO_DATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDatasetVersionId(), ServiceExceptionParameters.TASK_INFO_DATASET_DATASET_VERSION_ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDatasetUrn(), ServiceExceptionParameters.DATASET_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(newDatasetId, ServiceExceptionParameters.TASK_INFO_DATASET_NEW_DATASET_VERSION_ID, exceptions);
    }

    public static void checkProcessImportationTask(String importationJobKey, TaskInfoDataset taskInfoDataset, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(importationJobKey, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
        checkPlanifyImportationDataset(taskInfoDataset, exceptions);
    }

    public static void checkProcessRollbackImportationTask(String recoveryJobKey, TaskInfoDataset taskInfoDataset, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(recoveryJobKey, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset, ServiceExceptionParameters.TASK_INFO_DATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDatasetVersionId(), ServiceExceptionParameters.TASK_INFO_DATASET_DATASET_VERSION_ID, exceptions);
    }

    public static void checkProcessDuplicationTask(String duplicationJobKey, TaskInfoDataset taskInfoDataset, String newDatasetId, List<Mapping> datasourceMappings,
            List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(duplicationJobKey, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(newDatasetId, ServiceExceptionParameters.TASK_INFO_DATASET_NEW_DATASET_VERSION_ID, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(taskInfoDataset, ServiceExceptionParameters.TASK_INFO_DATASET, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(taskInfoDataset.getDatasetVersionId(), ServiceExceptionParameters.TASK_INFO_DATASET_DATASET_VERSION_ID, exceptions);
    }

    public static void checkExistsTaskForResource(String resourceId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(resourceId, ServiceExceptionParameters.TASK_INFO_RESOURCE_ID, exceptions);
    }

    public static void checkExistImportationTaskInResource(String resourceId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(resourceId, ServiceExceptionParameters.TASK_INFO_RESOURCE_ID, exceptions);
    }

    public static void checkExistRecoveryImportationTaskInResource(String resourceId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(resourceId, ServiceExceptionParameters.TASK_INFO_RESOURCE_ID, exceptions);
    }

    public static void checkExistDuplicationTaskInResource(String resourceId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(resourceId, ServiceExceptionParameters.TASK_INFO_RESOURCE_ID, exceptions);
    }

    public static void checkMarkTaskAsFinished(String job, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(job, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
    }

    public static void checkMarkTaskAsFailed(String job, String datasetIdVersion, String datasetUrn, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(job, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetIdVersion, ServiceExceptionParameters.TASK_INFO_DATASET_DATASET_VERSION_ID, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetUrn, ServiceExceptionParameters.DATASET_URN, exceptions);
    }

    public static void checkMarkTasksAsFailedOnApplicationStartup(String job, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(job, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
    }

    public static void checkCreateTask(Task task, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(task, ServiceExceptionParameters.TASK, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(task.getId(), ServiceExceptionParameters.TASK__JOB, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(task.getId(), ServiceExceptionParameters.TASK__STATUS, exceptions);
    }

    public static void checkRetrieveTaskByJob(String job, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(job, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
    }

    public static void checkUpdateTask(Task task, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(task, ServiceExceptionParameters.TASK, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(task.getId(), ServiceExceptionParameters.TASK__ID, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(task.getId(), ServiceExceptionParameters.TASK__JOB, exceptions);
        StatisticalResourcesValidationUtils.checkMetadataRequired(task.getId(), ServiceExceptionParameters.TASK__STATUS, exceptions);
    }

    public static void checkFindTasksByCondition(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) {
    }

    public static void checkExistDatabaseImportationTaskInResource(String resourceId, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(resourceId, ServiceExceptionParameters.TASK_INFO_RESOURCE_ID, exceptions);
    }

    public static void checkProcessDatabaseImportationTask(String databaseImportationJobKey, TaskInfoDataset taskInfoDataset, List<MetamacExceptionItem> exceptions) throws MetamacException {
        StatisticalResourcesValidationUtils.checkParameterRequired(databaseImportationJobKey, ServiceExceptionParameters.TASK_DATASET_JOB_KEY, exceptions);
        checkPlanifyImportationDataset(taskInfoDataset, exceptions);
    }

    public static void checkProcessDatabaseDatasetPollingTask(List<MetamacExceptionItem> exceptions) {
        // NOTHING TO DO HERE
    }

    public static void checkScheduleDatabaseDatasetPollingJob(List<MetamacExceptionItem> exceptions) {
        // NOTHING TO DO HERE
    }

    public static void checkSendDatabaseImportationErrorNotification(String datasetVersionUrn, MetamacException metamacException, List<MetamacExceptionItem> exceptions) {
        StatisticalResourcesValidationUtils.checkParameterRequired(datasetVersionUrn, ServiceExceptionParameters.DATASET_VERSION_URN, exceptions);
        StatisticalResourcesValidationUtils.checkParameterRequired(metamacException, ServiceExceptionParameters.METAMAC_EXCEPTION, exceptions);
    }
}
