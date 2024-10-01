package org.siemac.metamac.statistical.resources.core.task.serviceimpl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnByDots;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForDatabaseImportationResource;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForDuplicationResource;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForImportationAttributes;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForImportationResource;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForRecoveryImportationAttributes;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForRecoveryImportationResource;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForUpdateExternalGeocoverageCache;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForUpdateGeoCacheRelatedResources;
import static org.siemac.metamac.statistical.resources.core.task.utils.JobUtil.createJobNameForUpdateGeocoverageCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ExceptionHelper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.constraint.api.ConstraintsService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.repository.api.DatabaseImportRepository;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetService;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.TaskStatusTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.geocache.serviceapi.CacheService;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.service.StatisticalOperationsRestInternalService;
import org.siemac.metamac.statistical.resources.core.invocation.utils.RestMapper;
import org.siemac.metamac.statistical.resources.core.io.mapper.MetamacSdmx2StatRepoMapper;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.AbstractImportDatasetJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.DatabaseDatasetPollingJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.DuplicationDatasetJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.GeographicCoverageCacheClearJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ImportAttributesJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ImportDatasetFromDatabaseJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ImportDatasetJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ManipulateCsvDataService;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ManipulatePxDataService;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ManipulateSdmx21DataCallbackImpl;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.RecoveryImportAttributesJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.RecoveryImportDatasetJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.ResendPublishedDatasetsKafkaMessageJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.UpdateExternalGeocoverageCacheJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.UpdateGeocoverageCacheJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.UpdateGeocoverageCacheRelatedResourcesJob;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.serviceapi.PublicationService;
import org.siemac.metamac.statistical.resources.core.stream.messages.mappers.InternationalStringDo2AvroMapper;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamConsumerServiceFacade;
import org.siemac.metamac.statistical.resources.core.task.domain.AlternativeEnumeratedRepresentation;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptor;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptorResult;
import org.siemac.metamac.statistical.resources.core.task.domain.Task;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskProperties;
import org.siemac.metamac.statistical.resources.core.task.exception.TaskNotFoundException;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.validators.TaskServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.task.utils.JobUtil;
import org.siemac.metamac.statistical.resources.core.utils.DatabaseDatasetImportUtils;
import org.siemac.metamac.statistical.resources.core.utils.DatasetImportUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesExternalItemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.arte.statistic.parser.csv.CsvWriter;
import com.arte.statistic.parser.csv.constants.CsvConstants;
import com.arte.statistic.parser.px.domain.PxModel;
import com.arte.statistic.parser.sdmx.v2_1.Sdmx21Parser;

import es.gobcan.istac.edatos.dataset.repository.domain.DatasetRepositoryExceptionCodeEnum;
import es.gobcan.istac.edatos.dataset.repository.dto.DatasetRepositoryDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.Mapping;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

/**
 * Implementation of TaskService.
 */
@Service("taskService")
public class TaskServiceImpl extends TaskServiceImplBase implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger                     logger                                        = LoggerFactory.getLogger(TaskServiceImpl.class);

    public static final String                SCHEDULER_INSTANCE_NAME                       = "StatisticalResourcesScheduler";
    public static final String                PREFIX_JOB_IMPORT_DATA                        = "job_importdata_";
    public static final String                PREFIX_JOB_DATABASE_IMPORT_DATA               = "job_databaseimportdata_";
    public static final String                PREFIX_JOB_RECOVERY_IMPORT_DATA               = "job_recoveryimportdata_";
    public static final String                PREFIX_JOB_DUPLICATION_DATA                   = "job_duplicationdata_";
    public static final String                PREFIX_JOB_UPDATE_GEOCOVERAGE_CACHE           = "job_update_geocoverage_cache_";
    public static final String                PREFIX_JOB_UPDATE_GEO_CACHE_RELATED_RESOURCES = "job_update_geo_cache_related_resources";
    public static final String                PREFIX_JOB_UPDATE_EXTERNAL_GEOCOVERAGE_CACHE  = "job_update_external_geocoverage_cache";
    public static final String                PREFIX_TRIGGER_IMPORT_DATA                    = "trigger_importdata_";
    public static final String                PREFIX_TRIGGER_RECOVERY_IMPORT_DATA           = "trigger_recoveryimportdata_";
    public static final String                GROUP_IMPORTATION                             = "importation";
    public static final String                GROUP_EXTERNAL_CACHE                          = "externalCacheUpdate";
    public static final String                PREFIX_JOB_IMPORT_ATTRIBUTES                  = "job_import_attributes_";
    public static final String                PREFIX_JOB_RECOVERY_IMPORT_ATTRIBUTES         = "job_recovery_import_attributes_";

    @Autowired
    private TaskServiceInvocationValidator    taskServiceInvocationValidator;

    @Autowired
    private MetamacSdmx2StatRepoMapper        metamac2StatRepoMapper;

    @Autowired
    private SrmRestInternalService            srmRestInternalService;

    @Autowired
    StatisticalOperationsRestInternalService  statisticalOperationsRestInternalService;

    @Autowired
    private DatasetRepositoriesServiceFacade  datasetRepositoriesServiceFacade;

    @Autowired
    private ManipulatePxDataService           manipulatePxDataService;

    @Autowired
    private ManipulateCsvDataService          manipulateCsvDataService;

    @Autowired
    private ConstraintsService                constraintsService;

    @Autowired
    private DatasetService                    datasetService;

    @Autowired
    private PublicationService                publicationService;

    @Autowired
    private LifecycleService<DatasetVersion>  datasetLifecycleService;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    @Autowired
    @Qualifier("txManager")
    private PlatformTransactionManager        platformTransactionManager;

    @Autowired
    private DatasetVersionRepository          datasetVersionRepository;

    @Autowired
    private DatabaseImportRepository          databaseImportRepository;

    @Autowired
    private RestMapper                        restMapper;

    @Autowired
    private NoticesRestInternalService        noticesRestInternalService;

    @Autowired
    StreamConsumerServiceFacade               streamConsumerServiceFacade;

    @Autowired
    CacheService                              cacheService;

    @Autowired
    @Qualifier("commonDto2DoMapper")
    private CommonDto2DoMapper                dto2DoMapper;

    private SchedulerFactory                  schedulerFactory                              = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (schedulerFactory != null) {
            return;
        }

        // Quartz Properties
        Properties quartzProps = new Properties();
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, "StatisticalResourcesScheduler");
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID, "statisticalResourcesScheduler001");
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
        quartzProps.put(StdSchedulerFactory.PROP_JOB_STORE_CLASS, "org.quartz.simpl.RAMJobStore");
        quartzProps.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        // EDATOS-3360: Se reduce el número de threads concurrentes en ejecución a 1 para evitar que el proceso de importación de tsv falle
        // provocando el error ORA-00060 / org.hibernate.exception.LockAcquisitionException
        quartzProps.put("org.quartz.threadPool.threadCount", "1");
        quartzProps.put("org.quartz.threadPool.threadPriority", "5");

        try {
            schedulerFactory = new StdSchedulerFactory(quartzProps);
            Scheduler sched = schedulerFactory.getScheduler();

            // Start now
            sched.start();
        } catch (SchedulerException e) {
            throw new IllegalStateException("An unexpected error has occurred during quartz initialization", e);
        }

    }

    @Override
    public void scheduleDatabaseDatasetPollingJob(ServiceContext ctx) {
        try {
            taskServiceInvocationValidator.checkScheduleDatabaseDatasetPollingJob(ctx);

            if (configurationService.retriveDatabaseDatasetImportJobIsEnabled()) {

                JobDetail job = newJob(DatabaseDatasetPollingJob.class).build();

                CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                        .withSchedule(CronScheduleBuilder.cronSchedule(configurationService.retriveCronExpressionForDbDataImport()).withMisfireHandlingInstructionDoNothing()).build();

                Scheduler sched = schedulerFactory.getScheduler();
                sched.scheduleJob(job, cronTrigger);

                logger.info("Database dataset polling job successfully scheduled at {} ", new Date());
            } else {
                logger.warn("Database dataset polling job is disabled. Check " + StatisticalResourcesConfigurationConstants.DATABASE_DATASET_IMPORT_ENABLED
                        + " property value in environment.xml file in case you want to enable it");
            }

        } catch (Exception e) {
            logger.error("An unexpected error has occurred scheduling database dataset polling job", e);
        }
    }

    @Override
    public void scheduleGeographicCoverageCacheClearJob(ServiceContext ctx) {
        try {
            taskServiceInvocationValidator.checkScheduleGeographicCoverageCacheClearJob(ctx);

            JobDetail job = newJob(GeographicCoverageCacheClearJob.class).build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(configurationService.retrieveCronExpressionForGeograficCoverageCacheClear()).withMisfireHandlingInstructionDoNothing()).build();

            Scheduler sched = schedulerFactory.getScheduler();
            sched.scheduleJob(job, cronTrigger);

            logger.info("Geographic coverage cache clear job successfully scheduled at {} ", new Date());

        } catch (Exception e) {
            logger.error("An unexpected error has occurred scheduling Geographic coverage cache clear job", e);
        }
    }

    @Override
    public synchronized String planifyImportationDataset(ServiceContext ctx, TaskInfoDataset taskInfoDataset) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyImportationDataset(ctx, taskInfoDataset);

        String datasetUrn = taskInfoDataset.getDatasetUrn();

        JobKey jobKey = createJobKey(ctx, datasetUrn);
        TriggerKey triggerKey = createTriggerKey(ctx, datasetUrn);
        String taskName = createTaskName(ctx, taskInfoDataset.getDatasetVersionId());

        Task task = null;
        try {
            // Save InputStream (TempFile)
            StringBuilder filePaths = new StringBuilder();
            StringBuilder fileNames = new StringBuilder();
            StringBuilder fileFormats = new StringBuilder();
            StringBuilder alternativeRepresentations = new StringBuilder();
            StringBuilder datasetVersionRationaleTypes = new StringBuilder();
            StringBuilder datasetVersionDataProvidersUrn = new StringBuilder();
            serializeFilePathsAndNames(taskInfoDataset, filePaths, fileNames, fileFormats);
            serializeAlternativeRepresentations(taskInfoDataset, alternativeRepresentations);
            serializeDatasetVersionRationaleTypes(taskInfoDataset, datasetVersionRationaleTypes);
            serializeDatasetVersionDataProvidersUrn(taskInfoDataset, datasetVersionDataProvidersUrn);
            checkExistTaskInResource(ctx, jobKey, datasetUrn);

            // Checking garbage
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Task.class).withProperty(TaskProperties.job()).eq(taskName).distinctRoot().build();
            PagedResult<Task> tasks = findTasksByCondition(ctx, conditions, PagingParameter.pageAccess(1, 1));
            if (!tasks.getValues().isEmpty()) {
                task = tasks.getValues().get(0);
            }
            if (task != null) {
                if (createJobKeyForImportationResource(datasetUrn).equals(jobKey)) {
                    TaskInfoDataset recoveryTaskInfo = new TaskInfoDataset();
                    recoveryTaskInfo.setDatasetVersionId(taskInfoDataset.getDatasetVersionId());
                    recoveryTaskInfo.setDatasetUrn(datasetUrn);
                    // It's not necessary notify to user because this method is not for application startup recovers
                    planifyRecoveryImportDataset(ctx, recoveryTaskInfo, Boolean.FALSE); // Perform a clean recovery
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_RECOVERY_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build(); // Error
                } else if (createJobKeyForDatabaseImportationResource(datasetUrn).equals(jobKey)) {
                    processRollbackDatabaseImportTask(ctx, task.getJob());
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_DATABASE_IMPORTATION_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
                }
            }

            JobDetail job = createJob(ctx, jobKey, taskName, filePaths, fileNames, fileFormats, alternativeRepresentations, datasetVersionRationaleTypes, datasetVersionDataProvidersUrn,
                    taskInfoDataset);

            // No existing Job
            Task newTask = new Task(taskName);
            newTask.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            newTask.setExtensionPoint(taskInfoDataset.getDatasetVersionId() + JobUtil.SERIALIZATION_SEPARATOR + fileNames.toString()); // DatasetId | filename0 | ... @| filenameN
            createTask(ctx, newTask);
            SimpleTrigger trigger = newTrigger().withIdentity(triggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

            // Scheduler an importation job
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            sched.scheduleJob(job, trigger);

        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        }

        return jobKey.getName();
    }

    @Override
    public synchronized String planifyImportationAttributes(ServiceContext ctx, TaskInfoDataset taskInfoDataset) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyImportationDataset(ctx, taskInfoDataset);
        String datasetUrn = taskInfoDataset.getDatasetUrn();

        try {
            JobKey jobKey = createJobKeyImportAttributes(datasetUrn);
            TriggerKey triggerKey = createTriggerAttributesKey(datasetUrn);
            String taskName = createTaskImportAttributesName(taskInfoDataset.getDatasetVersionId());
            // Save InputStream (TempFile)
            StringBuilder filePaths = new StringBuilder();
            StringBuilder fileNames = new StringBuilder();
            StringBuilder fileFormats = new StringBuilder();
            serializeFilePathsAndNames(taskInfoDataset, filePaths, fileNames, fileFormats);
            JobDetail job = createImportAttributesJob(ctx, jobKey, filePaths, fileNames, fileFormats, taskInfoDataset, taskName);
            checkExistTaskInResource(ctx, jobKey, datasetUrn);

            checkGarbage(datasetUrn, ctx, jobKey, taskInfoDataset, taskName);
            // No existing Job
            Task newTask = new Task(taskName);
            newTask.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            newTask.setExtensionPoint(taskInfoDataset.getDatasetVersionId() + JobUtil.SERIALIZATION_SEPARATOR + fileNames.toString()); // DatasetId | filename0 | ... @| filenameN
            createTask(ctx, newTask);
            SimpleTrigger trigger = newTrigger().withIdentity(triggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

            // Scheduler an importation job
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            sched.scheduleJob(job, trigger);
            return jobKey.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        }
    }

    private void checkGarbage(String datasetUrn, ServiceContext ctx, JobKey jobKey, TaskInfoDataset taskInfoDataset, String taskName) throws MetamacException {
        Task task = null;
        // Checking garbage
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Task.class).withProperty(TaskProperties.job()).eq(taskName).distinctRoot().build();
        PagedResult<Task> tasks = findTasksByCondition(ctx, conditions, PagingParameter.pageAccess(1, 1));
        if (!tasks.getValues().isEmpty()) {
            task = tasks.getValues().get(0);
        }
        if (task != null && createJobKeyForImportationAttributes(datasetUrn).equals(jobKey)) {
            TaskInfoDataset recoveryTaskInfo = new TaskInfoDataset();
            recoveryTaskInfo.setDatasetVersionId(taskInfoDataset.getDatasetVersionId());
            recoveryTaskInfo.setDatasetUrn(datasetUrn);
            // It's not necessary notify to user because this method is not for application startup recovers
            planifyRecoveryImportAttributes(ctx, recoveryTaskInfo, Boolean.FALSE); // Perform a clean recovery
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_RECOVERY_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build(); // Error
        }

    }

    private void processRollbackDatabaseImportTask(ServiceContext ctx, String jobKey) throws MetamacException {
        // IDEA METAMAC-2866 Database import recovery not defined yet, task is deleted in order to not generated side effects. This behavior will be temporary until it's studied if it's
        // necessary to create a recovery job.
        String datasetVersionUrn = extractDatasetVersionUrnFromDatabaseImportationDatasetJobKey(jobKey);
        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        getNoticesRestInternalService().createDatabaseBackgroundNotification(datasetVersion, ServiceNoticeAction.DATABASE_IMPORT_DATASET_JOB, ServiceNoticeMessage.DATABASE_IMPORT_DATASET_JOB_DETECTED,
                datasetVersionUrn);

        markTaskAsFinished(ctx, jobKey);
    }

    private String createTaskName(ServiceContext ctx, String datasetVersionId) {
        return (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx) ? createJobNameForDatabaseImportationResource(datasetVersionId) : createJobNameForImportationResource(datasetVersionId));
    }

    private String createTaskImportAttributesName(String datasetVersionId) {
        return createJobNameForImportationAttributes(datasetVersionId);
    }

    protected JobKey createJobKeyImportAttributes(String datasetUrn) {
        return createJobKeyForImportationAttributes(datasetUrn);
    }

    protected JobKey createJobKey(ServiceContext ctx, String datasetUrn) {
        return (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx) ? createJobKeyForDatabaseImportationResource(datasetUrn) : createJobKeyForImportationResource(datasetUrn));
    }

    protected TriggerKey createTriggerKey(ServiceContext ctx, String datasetUrn) {
        return (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx) ? createTriggerKeyForDbImportationDataset(datasetUrn) : createTriggerKeyForImportationDataset(datasetUrn));
    }

    protected TriggerKey createTriggerAttributesKey(String datasetUrn) {
        return createTriggerKeyForImportationAttributes(datasetUrn);
    }

    private JobDetail createJob(ServiceContext serviceContext, JobKey jobKey, String taskName, StringBuilder filePaths, StringBuilder fileNames, StringBuilder fileFormats,
            StringBuilder alternativeRepresentations, StringBuilder versionRationaleTypes, StringBuilder datasetVersionDataProvidersUrn, TaskInfoDataset taskInfoDataset) {
        // @formatter:off
        
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AbstractImportDatasetJob.DATASET_VERSION_RATIONALE, taskInfoDataset.getVersionRationale());
        JobBuilder jobBuilder = 
                newJob().withIdentity(jobKey)
                    .usingJobData(AbstractImportDatasetJob.FILE_PATHS, filePaths.toString())
                    .usingJobData(AbstractImportDatasetJob.FILE_FORMATS, fileFormats.toString())
                    .usingJobData(AbstractImportDatasetJob.FILE_NAMES, fileNames.toString())
                    .usingJobData(AbstractImportDatasetJob.ALTERNATIVE_REPRESENTATIONS, alternativeRepresentations.toString())
                    .usingJobData(AbstractImportDatasetJob.STORE_ALTERNATIVE_REPRESENTATIONS, taskInfoDataset.getStoreAlternativeRepresentations())
                    .usingJobData(AbstractImportDatasetJob.DATASET_URN, taskInfoDataset.getDatasetUrn())
                    .usingJobData(AbstractImportDatasetJob.DATA_STRUCTURE_URN, taskInfoDataset.getDataStructureUrn())
                    .usingJobData(AbstractImportDatasetJob.DATASET_VERSION_ID, taskInfoDataset.getDatasetVersionId())
                    .usingJobData(AbstractImportDatasetJob.DATASET_NEXT_VERSION, taskInfoDataset.getDatasetNextVersion())
                    .usingJobData(AbstractImportDatasetJob.DATASET_NEXT_VERSION_DATE, taskInfoDataset.getDatasetNextVersionDate())
                    .usingJobData(AbstractImportDatasetJob.DATASET_NEXT_UPDATE_DATE, taskInfoDataset.getDatasetNextUpdateDate())
                    .usingJobData(AbstractImportDatasetJob.DATASET_UPDATE_FREQUENCY, taskInfoDataset.getDatasetUpdateFrequency())
                    .usingJobData(AbstractImportDatasetJob.DATASET_VERSION_DATA_PROVIDERS_URN, datasetVersionDataProvidersUrn.toString())
                    .usingJobData(AbstractImportDatasetJob.DATASET_VERSION_RATIONALE_TYPES, versionRationaleTypes.toString())
                    .usingJobData(AbstractImportDatasetJob.DATASET_NEXT_PROC_STATUS, taskInfoDataset.getDatasetNextProcStatus())
                    .usingJobData(AbstractImportDatasetJob.DATASET_AUTOMATIC_LIFE_CICLE, taskInfoDataset.getDatasetAutomaticLifeCicle())
                    .usingJobData(AbstractImportDatasetJob.TASK_NAME, taskName)
                    .usingJobData(AbstractImportDatasetJob.USER, serviceContext.getUserId())
                    .usingJobData(AbstractImportDatasetJob.DATASET_CODE, taskInfoDataset.getDatasetVersionCode())
                    .usingJobData(jobDataMap);
        // @formatter:on

        if (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(serviceContext)) {
            DateTime dt = (DateTime) serviceContext.getProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_EXECUTION_DATE);

            // @formatter:off
            jobBuilder.ofType(ImportDatasetFromDatabaseJob.class)
                .usingJobData(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_FLAG, Boolean.TRUE)
                .usingJobData(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_EXECUTION_DATE, dt.getMillis())
                .usingJobData(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_DATASOURCE_IDENTIFIER, (String) serviceContext.getProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_DATASOURCE_IDENTIFIER))
                .usingJobData(ImportDatasetFromDatabaseJob.STATISTICAL_OPERATION_URN, taskInfoDataset.getStatisticalOperationUrn());
            // @formatter:on
        } else {
            jobBuilder.ofType(ImportDatasetJob.class);
        }

        return jobBuilder.requestRecovery().build();
    }

    private JobDetail createImportAttributesJob(ServiceContext serviceContext, JobKey jobKey, StringBuilder filePaths, StringBuilder fileNames, StringBuilder fileFormats,
            TaskInfoDataset taskInfoDataset, String taskName) {
        JobBuilder jobBuilder = newJob().withIdentity(jobKey).usingJobData(ImportAttributesJob.FILE_PATHS, filePaths.toString()).usingJobData(ImportAttributesJob.FILE_FORMATS, fileFormats.toString())
                .usingJobData(ImportAttributesJob.FILE_NAMES, fileNames.toString()).usingJobData(ImportAttributesJob.DATASET_URN, taskInfoDataset.getDatasetUrn())
                .usingJobData(ImportAttributesJob.DATASET_VERSION_ID, taskInfoDataset.getDatasetVersionId()).usingJobData(ImportAttributesJob.TASK_NAME, taskName)
                .usingJobData(AbstractImportDatasetJob.USER, serviceContext.getUserId()).usingJobData(AbstractImportDatasetJob.DATA_STRUCTURE_URN, taskInfoDataset.getDataStructureUrn());

        jobBuilder.ofType(ImportAttributesJob.class);

        return jobBuilder.requestRecovery().build();
    }

    @Override
    public synchronized String planifyRecoveryImportDataset(ServiceContext ctx, TaskInfoDataset taskInfoDataset, Boolean notifyToUser) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyRecoveryImportDataset(ctx, taskInfoDataset, notifyToUser);

        String datasetUrn = taskInfoDataset.getDatasetUrn();

        // Job keys
        JobKey recoveryImportJobKey = createJobKeyForRecoveryImportationResource(datasetUrn);
        TriggerKey recoveryImportTriggerKey = createTriggerKeyForRecoveryImportationDataset(datasetUrn);

        // Scheduler an importation job
        Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler

        // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
        // @formatter:off
        JobDetail recoveryImportJob = newJob(RecoveryImportDatasetJob.class)
                                        .withIdentity(recoveryImportJobKey)
                                        .usingJobData(RecoveryImportDatasetJob.DATASET_VERSION_ID, taskInfoDataset.getDatasetVersionId())
                                        .usingJobData(RecoveryImportDatasetJob.USER, ctx.getUserId())
                                        .usingJobData(RecoveryImportDatasetJob.NOTIFY_TO_USER, notifyToUser)
                                        .usingJobData(RecoveryImportDatasetJob.DATASET_URN, datasetUrn)
                                        .requestRecovery()
                                        .build();
        // @formatter:on

        SimpleTrigger recoveryImportTrigger = newTrigger().withIdentity(recoveryImportTriggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

        try {
            sched.scheduleJob(recoveryImportJob, recoveryImportTrigger);
        } catch (SchedulerException e) {
            logger.error("PlannifyRecoveryImportDataset: the recovery importation with key " + recoveryImportJobKey.getName() + " has failed", e);
        }

        return recoveryImportJobKey.getName();
    }

    @Override
    public String planifyRecoveryImportAttributes(ServiceContext ctx, TaskInfoDataset taskInfoDataset, Boolean notifyToUser) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyRecoveryImportDataset(ctx, taskInfoDataset, notifyToUser);

        String datasetUrn = taskInfoDataset.getDatasetUrn();

        // Job keys
        JobKey recoveryImportJobKey = createJobKeyForRecoveryImportationAttributes(datasetUrn);
        TriggerKey recoveryImportTriggerKey = createTriggerKeyForRecoveryImportationAttributes(datasetUrn);

        // Scheduler an importation job
        Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
        // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
        // @formatter:off
        JobDetail recoveryImportJob = newJob(RecoveryImportAttributesJob.class)
                                        .withIdentity(recoveryImportJobKey)
                                        .usingJobData(RecoveryImportAttributesJob.DATASET_VERSION_ID, taskInfoDataset.getDatasetVersionId())
                                        .usingJobData(RecoveryImportAttributesJob.USER, ctx.getUserId())
                                        .usingJobData(RecoveryImportAttributesJob.NOTIFY_TO_USER, notifyToUser)
                                        .requestRecovery()
                                        .build();
        // @formatter:on

        SimpleTrigger recoveryImportTrigger = newTrigger().withIdentity(recoveryImportTriggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

        try {
            sched.scheduleJob(recoveryImportJob, recoveryImportTrigger);
        } catch (SchedulerException e) {
            logger.error("PlannifyRecoveryImportDataset: the recovery importation with key " + recoveryImportJobKey.getName() + " has failed", e);
        }

        return recoveryImportJobKey.getName();
    }

    @Override
    public synchronized String planifyDuplicationDataset(ServiceContext ctx, TaskInfoDataset taskInfoDataset, String newDatasetId, List<Mapping> datasourcesMapping) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyDuplicationDataset(ctx, taskInfoDataset, newDatasetId, datasourcesMapping);

        String datasetUrn = taskInfoDataset.getDatasetUrn();
        String taskName = createJobNameForDuplicationResource(taskInfoDataset.getDatasetVersionId());

        // Job keys
        JobKey duplicationJobKey = createJobKeyForDuplicationResource(datasetUrn);
        TriggerKey duplicationTriggerKey = createTriggerKeyForDuplicationDataset(datasetUrn);

        try {
            checkExistTaskInResource(ctx, duplicationJobKey, datasetUrn);

            // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
            HashMap<String, List<Mapping>> jobDataMap = new HashMap<String, List<Mapping>>();
            jobDataMap.put(DuplicationDatasetJob.DATASOURCE_MAPPINGS, datasourcesMapping);
            // @formatter:off
            JobDetail duplicationImportJob = newJob(DuplicationDatasetJob.class)
                    .withIdentity(duplicationJobKey)
                    .usingJobData(DuplicationDatasetJob.DATASET_VERSION_ID, taskInfoDataset.getDatasetVersionId())
                    .usingJobData(DuplicationDatasetJob.USER, ctx.getUserId())
                    .usingJobData(DuplicationDatasetJob.NEW_DATASET_VERSION_ID, newDatasetId)
                    .usingJobData(DuplicationDatasetJob.DATASET_URN, datasetUrn)
                    .usingJobData(DuplicationDatasetJob.TASK_NAME, taskName)
                    .usingJobData(new JobDataMap(jobDataMap))
                    .requestRecovery()
                    .build();
            // @formatter:on

            Task task = new Task(taskName);
            task.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            task.setExtensionPoint(newDatasetId + JobUtil.SERIALIZATION_SEPARATOR + taskInfoDataset.getDatasetVersionId());
            createTask(ctx, task);

            if (!DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx) && !DatasetImportUtils.isDatasetImportJob(ctx)) {
                SimpleTrigger duplicationImportTrigger = newTrigger().withIdentity(duplicationTriggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

                try {
                    // Scheduler a duplication job
                    Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
                    sched.scheduleJob(duplicationImportJob, duplicationImportTrigger);
                } catch (SchedulerException e) {
                    logger.error("PlannifyRecoveryImportDataset: the recovery importation with key " + duplicationJobKey.getName() + " has failed", e);
                }
            } else {
                processDuplicationTask(ctx, taskName, taskInfoDataset, newDatasetId, datasourcesMapping);
            }
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        }

        return duplicationJobKey.getName();
    }

    @Override
    public String planifyUpdateGeocoverageCache(ServiceContext ctx, TaskInfoDataset taskInfoDataset, boolean sendNotification) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyUpdateGeocoverageCache(ctx, taskInfoDataset, sendNotification);

        String datasetUrn = taskInfoDataset.getDatasetUrn();
        String datasetVersionUrn = taskInfoDataset.getDatasetVersionId();
        String taskName = createJobNameForUpdateGeocoverageCache(datasetVersionUrn);

        // Job keys
        JobKey jobKey = createJobKeyForUpdateGeocoverageCacheResource(datasetUrn);
        TriggerKey triggerKey = createTriggerKeyForUpdateGeocoverageCache(datasetUrn);

        try {
            checkExistTaskInResource(ctx, jobKey, datasetUrn);

            // @formatter:off
            JobDetail job = newJob(UpdateGeocoverageCacheJob.class)
                    .withIdentity(jobKey)
                    .usingJobData(UpdateGeocoverageCacheJob.DATASET_VERSION_ID, datasetVersionUrn)
                    .usingJobData(UpdateGeocoverageCacheJob.USER, ctx.getUserId())
                    .usingJobData(UpdateGeocoverageCacheJob.DATASET_URN, datasetUrn)
                    .usingJobData(UpdateGeocoverageCacheJob.TASK_NAME, taskName)
                    .usingJobData(UpdateGeocoverageCacheJob.SEND_NOTIFICATION, sendNotification)
                    .requestRecovery()
                    .build();
            // @formatter:on

            Task task = new Task(taskName);
            task.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            task.setExtensionPoint(datasetUrn);
            createTask(ctx, task);

            SimpleTrigger trigger = newTrigger().withIdentity(triggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

            try {
                // Scheduler a duplication job
                Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
                sched.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                logger.error("PlanifyUpdateGeocoverageCache: the job with key " + jobKey.getName() + " has failed", e);
            }
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }

        return jobKey.getName();
    }

    @Override
    public String planifyUpdateExternalGeocoverageCache(ServiceContext ctx, TaskInfoDataset taskInfoDataset) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyUpdateExternalGeocoverageCache(ctx, taskInfoDataset);

        String taskName = createJobNameForUpdateExternalGeocoverageCache();

        // Job keys
        JobKey jobKey = createJobKeyForUpdateExternalGeocoverageCacheResource();
        TriggerKey triggerKey = createTriggerKeyForUpdateExternalGeocoverageCache();

        try {
            checkExistTaskForUpdateExternalGeocoverageCacheResourceInResource(ctx, jobKey);

            // @formatter:off
            JobDetail job = newJob(UpdateExternalGeocoverageCacheJob.class)
                    .withIdentity(jobKey)
                    .usingJobData(UpdateExternalGeocoverageCacheJob.USER, ctx.getUserId())
                    .usingJobData(UpdateExternalGeocoverageCacheJob.TASK_NAME, taskName)
                    .requestRecovery()
                    .build();
            // @formatter:on

            Task task = new Task(taskName);
            task.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            task.setExtensionPoint(taskName);
            createTask(ctx, task);
            SimpleTrigger trigger = newTrigger().withIdentity(triggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withPriority(10).withSchedule(simpleSchedule()).build();

            try {
                // Scheduler a duplication job
                Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
                sched.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                logger.error("PlanifyUpdateExternalGeocoverageCache: the job with key " + jobKey.getName() + " has failed", e);
            }
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }

        return jobKey.getName();
    }

    private void checkExistTaskInResource(ServiceContext ctx, JobKey jobKey, String resourceUrn) throws MetamacException {
        checkSameJobNotExists(jobKey);

        checkExistRecoveryImportationTaskInResource(ctx, resourceUrn);

        if (!createJobKeyForImportationResource(resourceUrn).equals(jobKey)) {
            checkExistImportationTaskInResource(ctx, resourceUrn);
        }

        if (!createJobKeyForDatabaseImportationResource(resourceUrn).equals(jobKey)) {
            checkExistDatabaseImportationTaskInResource(ctx, resourceUrn);
        }

        if (!createJobKeyForDuplicationResource(resourceUrn).equals(jobKey)) {
            checkExistDuplicationTaskInResource(ctx, resourceUrn);
        }

        if (!createJobKeyForUpdateGeocoverageCacheResource(resourceUrn).equals(jobKey)) {
            checkExistUpdateGeocoverageCacheResource(ctx, resourceUrn);
        }

        if (!createJobKeyForUpdateGeoCacheRelatedResources(resourceUrn).equals(jobKey)) {
            checkExistUpdateGeoCacheRelatedResources(ctx, resourceUrn);
        }
    }

    private void checkExistTaskForUpdateExternalGeocoverageCacheResourceInResource(ServiceContext ctx, JobKey jobKey) throws MetamacException {
        checkSameJobNotExists(jobKey);

        if (!createJobKeyForUpdateExternalGeocoverageCacheResource().equals(jobKey)) {
            checkExistUpdateExternalGeocoverageCacheResource(ctx);
        }
    }

    private void checkExistDuplicationTaskInResource(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (existDuplicationTaskInResource(ctx, datasetUrn)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_DUPLICATION_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkExistUpdateGeocoverageCacheResource(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (existUpdateGeocoverageCacheTaskInResource(ctx, datasetUrn)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_UPDATE_GEOCOVERAGE_CACHE_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkExistUpdateGeoCacheRelatedResources(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (existUpdateGeoCacheRelatedResourcesTaskInResource(ctx, datasetUrn)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_UPDATE_GEO_CACHE_RELATED_RESOURCES_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkExistUpdateExternalGeocoverageCacheResource(ServiceContext ctx) throws MetamacException {
        if (existUpdateExternalGeocoverageCacheTaskInResource(ctx)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_UPDATE_EXTERNAL_GEOCOVERAGE_CACHE_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkExistRecoveryImportationTaskInResource(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (existRecoveryImportationTaskInResource(ctx, datasetUrn)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_RECOVERY_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkExistDatabaseImportationTaskInResource(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (existDatabaseImportationTaskInResource(ctx, datasetUrn)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_DATABASE_IMPORTATION_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkExistImportationTaskInResource(ServiceContext ctx, String datasetUrn) throws MetamacException {
        if (existImportationTaskInResource(ctx, datasetUrn)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_IMPORTATION_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private void checkSameJobNotExists(JobKey jobKey) throws MetamacException {
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            if (sched.checkExists(jobKey)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR_MAX_CURRENT_JOBS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public void processImportationTask(ServiceContext ctx, String importationJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkProcessImportationTask(ctx, importationJobKey, taskInfoDataset);
        if (Boolean.TRUE.equals(taskInfoDataset.getDatasetAutomaticLifeCicle())) {
            processCommonImportationTask(ctx, importationJobKey, taskInfoDataset);
        } else {
            processDatasetInImportTask(ctx, importationJobKey, taskInfoDataset);
            markDatabaseImportTaskAsFinished(ctx, importationJobKey);
        }
    }

    private void processDatasetInImportTask(ServiceContext ctx, String importationJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        Task task = retrieveTaskByJob(ctx, importationJobKey);

        try {
            processDatasets(ctx, taskInfoDataset, getCreatedDataSourceDate(ctx, task.getCreatedDate()));
        } catch (Exception e) {
            // Convert parser exception to metamac exception
            MetamacException throwableMetamacException = null;
            if (e instanceof MetamacException) {
                throwableMetamacException = (MetamacException) e;
            } else {
                throwableMetamacException = MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e))
                        .build();
            }
            throw throwableMetamacException;
        }
        // this function process the import files and add them to the datasets as new observations. After this task the function ******"markDatabaseImportTaskAsFinished"******* must be always called
        // if the process finished correctly.
        // if markDatabaseImportTaskAsFinished is not called, the task is pending in the database and the observations introduced in this execution will be deleted the next time the server is
        // restarted by an automatic background process.
    }

    private DateTime getCreatedDataSourceDate(ServiceContext ctx, DateTime createdDateTask) {
        return DatasetImportUtils.isDatasetImportJob(ctx) ? new DateTime() : createdDateTask;
    }

    private ProcStatusEnum getNextProcStatus(ServiceContext ctx, TaskInfoDataset taskInfoDataset) {
        if (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx)) {
            return ProcStatusEnum.PUBLISHED;
        }
        return taskInfoDataset.getDatasetNextProcStatus() != null ? ProcStatusEnum.valueOf(taskInfoDataset.getDatasetNextProcStatus()) : null;
    }

    private void processCommonImportationTask(ServiceContext ctx, String importationJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        String datasetVersionUrn = taskInfoDataset.getDatasetVersionId();
        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
        ProcStatusEnum procNextStatus = getNextProcStatus(ctx, taskInfoDataset);

        if (ProcStatusEnum.PUBLISHED.equals(datasetVersion.getLifeCycleStatisticalResource().getEffectiveProcStatus())) {
            datasetVersionUrn = versioningDatasetVersion(ctx, taskInfoDataset.getDatasetVersionId());

            // After versioning, it's necessary to update the task info because there is a new version of the dataset. The importation task should be applied to this.
            updateImportationTaskInfo(datasetVersionUrn, taskInfoDataset);

            executeImportationTask(ctx, importationJobKey, taskInfoDataset);

            updateMetadataDatasetVersion(ctx, datasetVersionUrn, taskInfoDataset);

            sendDatasetVersionToProductionValidation(ctx, datasetVersionUrn, procNextStatus);

            sendDatasetVersionToDiffusionValidation(ctx, datasetVersionUrn, procNextStatus);

            publishDatasetVersion(ctx, datasetVersionUrn, procNextStatus);

        } else {
            executeImportationTask(ctx, importationJobKey, taskInfoDataset);
        }

        markDatabaseImportTaskAsFinished(ctx, importationJobKey);

        sendNotification(ctx, datasetVersionUrn);

        updateGeographicCoverageVariableElementsCache(ctx, datasetVersionUrn, procNextStatus);
    }

    private void sendNotification(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        if (!DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx)) {

            logger.debug("sendNotification dataset in zip import with automatic life cicle {}", datasetVersionUrn);
            getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

                @Override
                protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                    DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
                    if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(datasetVersion.getLifeCycleStatisticalResource().getProcStatus())
                            || ProcStatusEnum.DIFFUSION_VALIDATION.equals(datasetVersion.getLifeCycleStatisticalResource().getProcStatus())
                            || ProcStatusEnum.PUBLISHED.equals(datasetVersion.getLifeCycleStatisticalResource().getProcStatus())) {
                        noticesRestInternalService.createLifeCycleNotification(ctx, datasetVersion.getLifeCycleStatisticalResource().getProcStatus(), datasetVersion);
                    }
                    return null;
                }
            });

        }
    }

    @Override
    public void processDatabaseImportationTask(ServiceContext ctx, String databaseImportationJobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        taskServiceInvocationValidator.checkProcessDatabaseImportationTask(ctx, databaseImportationJobKey, taskInfoDataset);
        processCommonImportationTask(ctx, databaseImportationJobKey, taskInfoDataset);

    }

    private String versioningDatasetVersion(ServiceContext ctx, String datasetVersionUrn) {
        logger.debug("Versioning dataset {}", datasetVersionUrn);

        return getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<String>() {

            @Override
            protected String doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                DatasetVersion datasetVersion = datasetLifecycleService.versioning(ctx, datasetVersionUrn, VersionTypeEnum.MINOR);
                return datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            }
        });
    }

    private void updateImportationTaskInfo(String datasetVersionUrn, TaskInfoDataset taskInfoDataset) {
        logger.debug("Updating task with the new dataset {}", datasetVersionUrn);
        taskInfoDataset.setDatasetVersionId(datasetVersionUrn);
    }

    private void executeImportationTask(ServiceContext ctx, String importationJobKey, TaskInfoDataset taskInfoDataset) {
        logger.debug("Execute importation dataset task {}", taskInfoDataset.getDatasetVersionId());

        getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

            @Override
            protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                processDatasetInImportTask(ctx, importationJobKey, taskInfoDataset);
                return null;
            }
        });
    }

    private void updateMetadataDatasetVersion(ServiceContext ctx, String datasetVersionUrn, TaskInfoDataset taskInfoDataset) {
        logger.debug("Updating required metada for dataset {}", datasetVersionUrn);

        getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

            @Override
            protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                // Retrieve dataset again to get it updated after importation task
                DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

                if (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx)) {
                    DatabaseDatasetImportUtils.setRequiredMetadataForDatabaseDatasetImportation(datasetVersion);
                } else {
                    DatasetImportUtils.setRequiredMetadataForDatasetImportation(datasetVersion, taskInfoDataset, srmRestInternalService, dto2DoMapper);
                }

                // It's necessary to save the new metadata of the dataset before continuing transiting it through the life cycle
                datasetService.updateDatasetVersion(ctx, datasetVersion);

                return null;
            }
        });
    }

    private void sendDatasetVersionToProductionValidation(ServiceContext ctx, String datasetVersionUrn, ProcStatusEnum procNextStatus) {
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procNextStatus) || ProcStatusEnum.DIFFUSION_VALIDATION.equals(procNextStatus) || ProcStatusEnum.PUBLISHED.equals(procNextStatus)) {

            logger.debug("Sending to production validation dataset {}", datasetVersionUrn);

            getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

                @Override
                protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                    datasetLifecycleService.sendToProductionValidation(ctx, datasetVersionUrn);
                    return null;
                }
            });
        }
    }

    private void sendDatasetVersionToDiffusionValidation(ServiceContext ctx, String datasetVersionUrn, ProcStatusEnum procNextStatus) {
        if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procNextStatus) || ProcStatusEnum.PUBLISHED.equals(procNextStatus)) {

            logger.debug("Sending to difussion validation dataset {}", datasetVersionUrn);

            getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

                @Override
                protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                    datasetLifecycleService.sendToDiffusionValidation(ctx, datasetVersionUrn);
                    return null;
                }
            });
        }
    }

    private void publishDatasetVersion(ServiceContext ctx, String datasetVersionUrn, ProcStatusEnum procNextStatus) {
        if (ProcStatusEnum.PUBLISHED.equals(procNextStatus)) {
            logger.debug("Publishing dataset {}", datasetVersionUrn);

            getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

                @Override
                protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                    datasetLifecycleService.sendToPublished(ctx, datasetVersionUrn);
                    return null;
                }
            });
        }
    }

    private void updateGeographicCoverageVariableElementsCache(ServiceContext ctx, String datasetVersionUrn, ProcStatusEnum procNextStatus) {
        if (ProcStatusEnum.PUBLISHED.equals(procNextStatus) && (DatasetImportUtils.isDatasetImportJob(ctx) || DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx))) {
            logger.debug("updateGeographicCoverageVariableElementsCache dataset in zip import or database import {}", datasetVersionUrn);

            getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

                @Override
                protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                    DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
                    if (ProcStatusEnum.PUBLISHED.equals(datasetVersion.getSiemacMetadataStatisticalResource().getProcStatus())) {
                        datasetService.updateGeographicCoverageVariableElementsCache(ctx, datasetVersion);
                    }
                    return null;
                }
            });
        }
    }

    private void markDatabaseImportTaskAsFinished(ServiceContext ctx, String databaseImportationJobKey) {
        logger.debug("Marking databaset task as finished {}", databaseImportationJobKey);

        getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

            @Override
            protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                markTaskAsFinished(ctx, databaseImportationJobKey);
                return null;
            }
        });
    }

    private TransactionTemplate getTransactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate;
    }

    @Override
    public void processRollbackImportationTask(ServiceContext ctx, String recoveryJobKey, TaskInfoDataset taskInfoDataset) {
        Task task = null;
        try {
            task = retrieveTaskByJob(ctx, createJobKeyForImportationResource(taskInfoDataset.getDatasetVersionId()).getName());

            DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, taskInfoDataset.getDatasetVersionId());

            if (ProcStatusEnum.PUBLISHED.equals(datasetVersion.getLifeCycleStatisticalResource().getProcStatus())) {
                // Delete failed entry
                logger.info("Rollback importation task not executed because the dataset is published. Urn: {}", taskInfoDataset.getDatasetVersionId());
                deleteFailedTask(task);

                getNoticesRestInternalService().createDatabaseImportSuccessBackgroundNotification(datasetVersion, ServiceNoticeAction.DATABASE_IMPORT_DATASET_RECOVERY_JOB_ROLLBACK_PUBLISHED_DATASET,
                        ServiceNoticeMessage.DATABASE_IMPORT_DATASET_RECOVERY_JOB_ROLLBACK_PUBLISHED_DATASET_ALERT, taskInfoDataset.getDatasetVersionId(), MetamacRolesEnum.ADMINISTRADOR);

                return;

            }

            String fileNames = task.getExtensionPoint();
            String[] names = fileNames.split("\\" + JobUtil.SERIALIZATION_SEPARATOR);
            for (int i = 1; i < names.length; i++) {
                String dataSourceId = Datasource.generateDataSourceId(names[i], task.getCreatedDate());

                InternationalStringDto internationalStringDto = new InternationalStringDto();
                LocalisedStringDto localisedStringDto = new LocalisedStringDto();
                localisedStringDto.setLabel(dataSourceId);
                localisedStringDto.setLocale(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
                internationalStringDto.addText(localisedStringDto);

                logger.info("Rollback importation task is trying to delete observations by attribute instance value. Dataset = {}, Datasource = {}",
                        new Object[]{taskInfoDataset.getDatasetVersionId(), dataSourceId});

                datasetRepositoriesServiceFacade.deleteObservationsByAttributeInstanceValue(taskInfoDataset.getDatasetVersionId(), StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID,
                        internationalStringDto);
            }

            deleteFailedTask(task);
        } catch (ApplicationException e) {
            if (task != null && DatasetRepositoryExceptionCodeEnum.DATASET_NOT_EXISTS.name().equals(e.getErrorCode())) {
                getTaskRepository().delete(task);
            }
            logger.error("Error while perform a recovery in dataset", e);
        } catch (Exception e) {
            logger.error("Error while perform a recovery in dataset", e);
        }

    }

    private void deleteFailedTask(Task task) {
        // Delete failed entry
        logger.info("Deleting failed task starting");
        getTaskRepository().delete(task);
        logger.info("Deleting failed task finished");
    }

    @Override
    public void processRollbackImportationAttributesTask(ServiceContext ctx, String recoveryJobKey, TaskInfoDataset taskInfoDataset) {
        Task task = null;
        try {
            task = retrieveTaskByJob(ctx, createJobKeyForImportationAttributes(taskInfoDataset.getDatasetVersionId()).getName());
            // Delete failed entry
            logger.info("Deleting failed task starting");
            getTaskRepository().delete(task);
            logger.info("Deleting failed task finished");
        } catch (Exception e) {
            getTaskRepository().delete(task);
            logger.error("Error while perform a recovery in dataset", e);
        }

    }

    @Override
    public void processDuplicationTask(ServiceContext ctx, String duplicationJobKey, TaskInfoDataset taskInfoDataset, String newDatasetId, List<Mapping> datasourceMappings) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkProcessDuplicationTask(ctx, duplicationJobKey, taskInfoDataset, newDatasetId, datasourceMappings);

        try {
            datasetRepositoriesServiceFacade.duplicateDatasetRepository(taskInfoDataset.getDatasetVersionId(), newDatasetId, datasourceMappings);

            DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, taskInfoDataset.getDatasetVersionId());
            datasetService.manageDatabaseView(ctx, newDatasetId, datasetVersion);

        } catch (Exception e) {
            // Convert parser exception to metamac exception
            MetamacException throwableMetamacException = null;
            if (e instanceof MetamacException) {
                throwableMetamacException = (MetamacException) e;
            } else {
                throwableMetamacException = MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e))
                        .build();
            }
            throw throwableMetamacException;
        }

        markTaskAsFinished(ctx, duplicationJobKey); // Finish the importation
    }

    private String getCodelistFromCodeUrn(String urn) {
        if (urn == null) {
            return null;
        }

        // e.g.: urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_AREA_ES70_DS_20111120(01.000).ES70 is converted to
        // [urn:sdmx:org, sdmx, infomodel, codelist, Code=ISTAC:CL_AREA_ES70_DS_20111120(01.000), ES70]
        // The use of LinkedList is because Arrays.asList returns a fixed-size list
        List<String> splittedByDotUrn = new LinkedList<>(Arrays.asList(splitUrnByDots(urn)));

        int lastElementIndex = splittedByDotUrn.size() - 1;
        if (lastElementIndex >= 0) {
            // remove the element corresponding to the code id
            splittedByDotUrn.remove(lastElementIndex);
        }

        return String.join(".", splittedByDotUrn);
    }

    @Override
    public void processUpdateGeocoverageCacheTask(ServiceContext ctx, String jobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkProcessUpdateGeocoverageCacheTask(ctx, jobKey, taskInfoDataset);

        DatasetVersion datasetVersion = datasetVersionRepository.retrieveByUrn(taskInfoDataset.getDatasetVersionId());

        boolean isLastVersionPublished = datasetVersion.getSiemacMetadataStatisticalResource().getLastVersion();

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        logger.debug("Updating geocoverage cache for dataset {}", datasetVersionUrn);
        List<ExternalItem> geographicCoverage = datasetVersion.getGeographicCoverage();

        if (geographicCoverage.isEmpty()) {
            logger.debug("Dataset geographic coverage is empty. trying to recover information from spatial attribute");
            datasetService.updateGeographicCoverageFromSpatialAttribute(ctx, datasetVersion);
            if (geographicCoverage.isEmpty()) {
                logger.debug("Dataset geographic coverage is empty");
                return;
            }
        }

        String geographicCoverageCodelistUrn = getCodelistFromCodeUrn(geographicCoverage.get(0).getUrn());

        if (!isLastVersionPublished) {
            isLastVersionPublished = isLastVersionPublished(ctx, datasetVersionUrn);
        }

        cacheService.processUpdateGeoCacheResource(ctx, datasetVersion, geographicCoverageCodelistUrn, geographicCoverage, isLastVersionPublished);

        logger.debug("Processing geographic coverage to create the cache correctly finished");

        markTaskAsFinished(ctx, jobKey);
    }

    /*
     * if cache is manually updated, dataset can be in draft and this version is lastversion. For this case, it is necessary to calculate if this dataset is last published version
     */
    private boolean isLastVersionPublished(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        //
        String[] params = UrnUtils.splitUrnItemScheme(datasetVersionUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        DatasetVersion lastVersionDataset = datasetService.getDatasetLastVersionPublishedByDatasetUrn(ctx, agencyId, resourceId);
        return lastVersionDataset != null && lastVersionDataset.getSiemacMetadataStatisticalResource().getUrn().equals(datasetVersionUrn);
    }

    private void markTaskAsFinishedInTransaction(ServiceContext ctx, String jobKey) {
        logger.debug("Marking  task as finished {}", jobKey);

        getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<Object>() {

            @Override
            protected Object doInMetamacTransaction(TransactionStatus status) throws MetamacException {
                markTaskAsFinished(ctx, jobKey);
                return null;
            }
        });
    }

    @Override
    public void processUpdateExternalGeocoverageCacheTask(ServiceContext ctx, String jobKey, TaskInfoDataset taskInfoDataset) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkProcessUpdateExternalGeocoverageCacheTask(ctx, jobKey, taskInfoDataset);

        logger.debug("Updating geocoverage cache for external datasets (nonexistent in database)");

        streamConsumerServiceFacade.updateGeographicCoverageExternalPublicationVariableElementsCache(ctx);

        logger.debug("Processing geographic coverage for external datasets (nonexistent in database) to create the cache correctly finished");

        markTaskAsFinishedInTransaction(ctx, jobKey);
    }

    public es.ibestat.jaxi.stream.messages.ExternalItemAvro do2Avro(ExternalItem source) {
        es.ibestat.jaxi.stream.messages.ExternalItemAvro target = null;
        if (source != null) {
            try {
                target = es.ibestat.jaxi.stream.messages.ExternalItemAvro.newBuilder().setCode(source.getCode()).setCodeNested(source.getCodeNested())
                        .setTitle(InternationalStringDo2AvroMapper.do2Avro(source.getTitle())).setType(es.ibestat.jaxi.stream.messages.TypeExternalArtefactsEnumAvro.STATISTICAL_OPERATION)
                        .setUrn(source.getUrn()).build();
            } catch (Exception e) {
                logger.error("ERROR CREANDO MENSAJE JAXI PUBLICATION", e);
            }
        }
        return target;

    }

    private void processRollbackDuplicationTask(ServiceContext ctx, Task task) throws MetamacException {
        markTaskAsFinished(ctx, task.getJob());
    }

    private void processRollbackDuplicationTaskOnApplicationStartup(ServiceContext ctx, Task task) throws MetamacException {
        try {
            String[] datasets = task.getExtensionPoint().split("\\" + JobUtil.SERIALIZATION_SEPARATOR);

            DatasetRepositoryDto datasetRepository = datasetRepositoriesServiceFacade.retrieveDatasetRepository(datasets[0]);

            if (datasetRepository != null) {
                // If it exists, all is correct, the duplicate successfully finished but did not notice the application. So, we have to do it with a success message
                markTaskAsFinished(ctx, task.getJob());

                String datasetVersionId = datasets[1];
                String newDatasetVersionId = datasetRepository.getDatasetId();
                getNoticesRestInternalService().createSuccessBackgroundNotification(task.getCreatedBy(), ServiceNoticeAction.DUPLICATION_DATASET_JOB, ServiceNoticeMessage.DUPLICATION_DATASET_JOB_OK,
                        datasetVersionId, newDatasetVersionId);
            } else {
                // If not, send notification about the failure. No further action is necessary because there is no waste in the repository.
                markTaskAsFinished(ctx, task.getJob()); // Clear the error task
                MetamacException metamacException = MetamacExceptionBuilder.builder().withPrincipalException(ServiceExceptionType.TASKS_ERROR_SERVER_DOWN, task.getJob()).build();
                getNoticesRestInternalService().createErrorBackgroundNotification(task.getCreatedBy(), ServiceNoticeAction.CANCEL_IN_PROGRESS_TASKS_WHILE_SERVER_SHUTDOWN, metamacException);
            }
        } catch (ApplicationException e) {
            logger.error("Unable to connect to the repository", e);
        }
    }

    @Override
    public boolean existsTaskForResource(ServiceContext ctx, String resourceId) throws MetamacException {
        try {
            taskServiceInvocationValidator.checkExistsTaskForResource(ctx, resourceId);
            boolean a = existImportationTaskInResource(ctx, resourceId) || existRecoveryImportationTaskInResource(ctx, resourceId) || existDuplicationTaskInResource(ctx, resourceId)
                    || (existDatabaseImportationTaskInResource(ctx, resourceId)) || existUpdateGeocoverageCacheTaskInResource(ctx, resourceId);
            return a;
        } catch (Exception e) {
            logger.error("existsTaskForResource ----", e);
        }
        return true;
    }

    @Override
    public boolean existsTaskImportAttributes(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistImportationTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return sched.checkExists(createJobKeyForImportationAttributes(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existImportationTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistImportationTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return !DatasetImportUtils.isDatasetImportJob(ctx) && sched.checkExists(createJobKeyForImportationResource(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existRecoveryImportationTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistRecoveryImportationTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return sched.checkExists(createJobKeyForRecoveryImportationResource(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existDuplicationTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistDuplicationTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return sched.checkExists(createJobKeyForDuplicationResource(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existUpdateGeocoverageCacheTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistUpdateGeocoverageCacheTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return sched.checkExists(createJobKeyForUpdateGeocoverageCacheResource(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existUpdateGeoCacheRelatedResourcesTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistUpdateGeoCacheRelatedResourcesTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return sched.checkExists(createJobKeyForUpdateGeocoverageCacheResource(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existUpdateExternalGeocoverageCacheTaskInResource(ServiceContext ctx) throws MetamacException {
        taskServiceInvocationValidator.checkExistUpdateExternalGeocoverageCacheTaskInResource(ctx);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return sched.checkExists(createJobKeyForUpdateExternalGeocoverageCacheResource());
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public boolean existDatabaseImportationTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        taskServiceInvocationValidator.checkExistDatabaseImportationTaskInResource(ctx, resourceId);
        try {
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            return !DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(ctx) && sched.checkExists(createJobKeyForDatabaseImportationResource(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    @Override
    public Task createTask(ServiceContext ctx, Task task) throws MetamacException {
        // Save version
        task = getTaskRepository().save(task);
        return task;
    }

    @Override
    public Task updateTask(ServiceContext ctx, Task task) throws MetamacException {
        // Save version
        task = getTaskRepository().save(task);
        return task;
    }

    @Override
    public Task retrieveTaskByJob(ServiceContext ctx, String job) throws MetamacException {
        try {
            Task task = getTaskRepository().findByKey(job);
            return task;
        } catch (TaskNotFoundException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_NOT_FOUND).withMessageParameters(job).build();
        }
    }

    @Override
    public void markTaskAsFinished(ServiceContext ctx, String job) throws MetamacException {
        // Delete complete task
        Task task = retrieveTaskByJob(ctx, job);
        getTaskRepository().delete(task);
    }

    @Override
    public void markTasksAsFailedOnApplicationStartup(ServiceContext ctx, String jobKey) throws MetamacException {
        Task task = retrieveTaskByJob(ctx, jobKey);
        // Plannify a recovery job
        if (jobKey.startsWith(PREFIX_JOB_IMPORT_DATA)) {
            String datasetVersionUrn = extractDatasetVersionUrnFromImportationDatasetJobKey(jobKey);

            TaskInfoDataset recoveryTaskInfo = setDatasetDataToPlanifyRecovery(ctx, task, datasetVersionUrn);
            planifyRecoveryImportDataset(ctx, recoveryTaskInfo, Boolean.TRUE);
        } else if (jobKey.startsWith(PREFIX_JOB_DUPLICATION_DATA)) {
            processRollbackDuplicationTaskOnApplicationStartup(ctx, task);
        } else if (jobKey.startsWith(PREFIX_JOB_DATABASE_IMPORT_DATA)) {
            processRollbackDatabaseImportTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_UPDATE_GEOCOVERAGE_CACHE)) {
            processRollbackUpdateGeocoverageCacheTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_UPDATE_GEO_CACHE_RELATED_RESOURCES)) {
            processRollbackUpdateGeoCacheRelatedResourcesTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_UPDATE_EXTERNAL_GEOCOVERAGE_CACHE)) {
            processRollbackUpdateExternalPublicationGeocoverageCacheTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_IMPORT_ATTRIBUTES)) {
            String datasetVersionUrn = extractDatasetVersionUrnFromImportationAttributesJobKey(jobKey);
            TaskInfoDataset recoveryTaskInfo = setDatasetDataToPlanifyRecovery(ctx, task, datasetVersionUrn);
            planifyRecoveryImportAttributes(ctx, recoveryTaskInfo, Boolean.TRUE);
        }
    }

    private TaskInfoDataset setDatasetDataToPlanifyRecovery(ServiceContext ctx, Task task, String datasetVersionUrn) throws MetamacException {
        // Update
        task.setStatus(TaskStatusTypeEnum.FAILED);
        updateTask(ctx, task);

        String datasetUrn = retrieveDatasetUrn(ctx, datasetVersionUrn);

        TaskInfoDataset recoveryTaskInfo = new TaskInfoDataset();
        recoveryTaskInfo.setDatasetVersionId(datasetVersionUrn);
        recoveryTaskInfo.setDatasetUrn(datasetUrn);
        return recoveryTaskInfo;
    }

    private void processRollbackUpdateGeocoverageCacheTask(ServiceContext ctx, String jobKey) throws MetamacException {
        String datasetVersionUrn = extractDatasetVersionUrnFromUpdateGeocoverageCacheJobKey(jobKey);
        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
        getNoticesRestInternalService().createUpdateGeocoverageCacheNotification(datasetVersion, ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB,
                ServiceNoticeMessage.UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_ERROR, datasetVersionUrn);
        markTaskAsFinished(ctx, jobKey);
    }

    private void processRollbackUpdateGeoCacheRelatedResourcesTask(ServiceContext ctx, String jobKey) throws MetamacException {
        String urn = extractUrnFromUpdateGeoCacheRelatedResourceJobKey(jobKey);
        getNoticesRestInternalService().createErrorUpdateGeocoverageCacheBackgroundNotification(urn, ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB,
                ServiceNoticeMessage.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR, urn);
        markTaskAsFinished(ctx, jobKey);
    }

    private void processRollbackUpdateExternalPublicationGeocoverageCacheTask(ServiceContext ctx, String jobKey) throws MetamacException {

        getNoticesRestInternalService().createExternalPublicationUpdateErrorBackgroundNotification(ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_ERROR);

        markTaskAsFinished(ctx, jobKey);
    }

    private String retrieveDatasetUrn(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
        return datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();
    }

    @Override
    public void markTaskAsFailed(ServiceContext ctx, String jobKey, String datasetVersionId, String datasetUrn) throws MetamacException {
        Task task = retrieveTaskByJob(ctx, jobKey);
        // Plannify a recovery job
        if (jobKey.startsWith(PREFIX_JOB_IMPORT_DATA)) {
            TaskInfoDataset recoveryTaskInfo = setTaskInfoToPlanifyRecovery(ctx, datasetVersionId, datasetUrn, task);
            planifyRecoveryImportDataset(ctx, recoveryTaskInfo, Boolean.FALSE);
        } else if (jobKey.startsWith(PREFIX_JOB_DUPLICATION_DATA)) {
            processRollbackDuplicationTask(ctx, task);
        } else if (jobKey.startsWith(PREFIX_JOB_DATABASE_IMPORT_DATA)) {
            processRollbackDatabaseImportTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_UPDATE_GEOCOVERAGE_CACHE)) {
            processRollbackUpdateGeocoverageCacheTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_UPDATE_GEO_CACHE_RELATED_RESOURCES)) {
            processRollbackUpdateGeoCacheRelatedResourcesTask(ctx, task.getJob());
        } else if (jobKey.startsWith(PREFIX_JOB_IMPORT_ATTRIBUTES)) {
            TaskInfoDataset recoveryTaskInfo = setTaskInfoToPlanifyRecovery(ctx, datasetVersionId, datasetUrn, task);
            planifyRecoveryImportAttributes(ctx, recoveryTaskInfo, Boolean.FALSE);
        }
    }

    private TaskInfoDataset setTaskInfoToPlanifyRecovery(ServiceContext ctx, String datasetVersionId, String datasetUrn, Task task) throws MetamacException {
        // Update
        task.setStatus(TaskStatusTypeEnum.FAILED);
        updateTask(ctx, task);

        TaskInfoDataset recoveryTaskInfo = new TaskInfoDataset();
        recoveryTaskInfo.setDatasetVersionId(datasetVersionId);
        recoveryTaskInfo.setDatasetUrn(datasetUrn);
        return recoveryTaskInfo;
    }

    @Override
    public PagedResult<Task> findTasksByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(Task.class).distinctRoot().build();
        }
        PagedResult<Task> pagedResult = getTaskRepository().findByCondition(conditions, pagingParameter);
        return pagedResult;
    }

    /****************************************************************
     * PRIVATES
     ****************************************************************/

    private JobKey createJobKeyForImportationResource(String resourceId) {
        return new JobKey(createJobNameForImportationResource(resourceId), GROUP_IMPORTATION);
    }

    private JobKey createJobKeyForImportationAttributes(String resourceId) {
        return new JobKey(createJobNameForImportationAttributes(resourceId), GROUP_IMPORTATION);
    }

    private JobKey createJobKeyForDatabaseImportationResource(String resourceId) {
        return new JobKey(createJobNameForDatabaseImportationResource(resourceId), GROUP_IMPORTATION);
    }

    private JobKey createJobKeyForRecoveryImportationResource(String resourceId) {
        return new JobKey(createJobNameForRecoveryImportationResource(resourceId), GROUP_IMPORTATION);
    }

    private JobKey createJobKeyForRecoveryImportationAttributes(String resourceId) {
        return new JobKey(createJobNameForRecoveryImportationAttributes(resourceId), GROUP_IMPORTATION);
    }

    private JobKey createJobKeyForDuplicationResource(String resourceId) {
        return new JobKey(createJobNameForDuplicationResource(resourceId), GROUP_IMPORTATION);
    }

    private JobKey createJobKeyForUpdateGeocoverageCacheResource(String resourceId) {
        return new JobKey(createJobNameForUpdateGeocoverageCache(resourceId));
    }

    private JobKey createJobKeyForUpdateGeoCacheRelatedResources(String resourceId) {
        return new JobKey(createJobNameForUpdateGeoCacheRelatedResources(resourceId));
    }

    private JobKey createJobKeyForUpdateExternalGeocoverageCacheResource() {
        return new JobKey(createJobNameForUpdateExternalGeocoverageCache());
    }

    private TriggerKey createTriggerKeyForImportationDataset(String datasetId) {
        return new TriggerKey(createJobNameForImportationResource(datasetId), GROUP_IMPORTATION);
    }

    private TriggerKey createTriggerKeyForImportationAttributes(String datasetId) {
        return new TriggerKey(createJobNameForImportationAttributes(datasetId), GROUP_IMPORTATION);
    }

    private TriggerKey createTriggerKeyForDbImportationDataset(String datasetId) {
        return new TriggerKey(createJobNameForDatabaseImportationResource(datasetId), GROUP_IMPORTATION);
    }

    private TriggerKey createTriggerKeyForRecoveryImportationDataset(String datasetId) {
        return new TriggerKey(createJobNameForRecoveryImportationResource(datasetId), GROUP_IMPORTATION);
    }

    private TriggerKey createTriggerKeyForRecoveryImportationAttributes(String datasetId) {
        return new TriggerKey(createJobNameForRecoveryImportationAttributes(datasetId), GROUP_IMPORTATION);
    }

    private TriggerKey createTriggerKeyForDuplicationDataset(String datasetId) {
        return new TriggerKey(createJobNameForDuplicationResource(datasetId), GROUP_IMPORTATION);
    }

    private TriggerKey createTriggerKeyForUpdateGeocoverageCache(String datasetId) {
        return new TriggerKey(createJobNameForUpdateGeocoverageCache(datasetId));
    }

    private TriggerKey createTriggerKeyForUpdateGeoCacheRelatedResources(String datasetId) {
        return new TriggerKey(createJobNameForUpdateGeoCacheRelatedResources(datasetId));
    }

    private TriggerKey createTriggerKeyForUpdateExternalGeocoverageCache() {
        return new TriggerKey(createJobNameForUpdateExternalGeocoverageCache(), GROUP_EXTERNAL_CACHE);
    }

    private String extractDatasetVersionUrnFromImportationDatasetJobKey(String jobKeyName) {
        return extractResourceVersionUrnFromJobKey(jobKeyName, PREFIX_JOB_IMPORT_DATA);
    }

    private String extractDatasetVersionUrnFromImportationAttributesJobKey(String jobKeyName) {
        return extractResourceVersionUrnFromJobKey(jobKeyName, PREFIX_JOB_IMPORT_ATTRIBUTES);
    }

    private String extractDatasetVersionUrnFromDatabaseImportationDatasetJobKey(String jobKeyName) {
        return extractResourceVersionUrnFromJobKey(jobKeyName, PREFIX_JOB_DATABASE_IMPORT_DATA);
    }

    private String extractDatasetVersionUrnFromUpdateGeocoverageCacheJobKey(String jobKeyName) {
        return extractResourceVersionUrnFromJobKey(jobKeyName, PREFIX_JOB_UPDATE_GEOCOVERAGE_CACHE);
    }

    private String extractUrnFromUpdateGeoCacheRelatedResourceJobKey(String jobKeyName) {
        return extractResourceVersionUrnFromJobKey(jobKeyName, PREFIX_JOB_UPDATE_GEO_CACHE_RELATED_RESOURCES);
    }

    private String extractResourceVersionUrnFromJobKey(String jobKeyName, String prefixJob) {
        if (StringUtils.isEmpty(jobKeyName)) {
            return null;
        }
        return StringUtils.substringAfter(jobKeyName, prefixJob);
    }

    protected void serializeFilePathsAndNames(TaskInfoDataset taskInfoDataset, StringBuilder filePaths, StringBuilder fileNames, StringBuilder fileFormats) throws IOException, FileNotFoundException {
        for (FileDescriptor fileDescriptorDto : taskInfoDataset.getFiles()) {
            if (filePaths.length() > 0) {
                filePaths.append(JobUtil.SERIALIZATION_SEPARATOR);
            }
            filePaths.append(fileDescriptorDto.getFile().getAbsolutePath());

            if (fileNames.length() > 0) {
                fileNames.append(JobUtil.SERIALIZATION_SEPARATOR);
            }
            fileNames.append(fileDescriptorDto.getFileName());

            if (fileFormats.length() > 0) {
                fileFormats.append(JobUtil.SERIALIZATION_SEPARATOR);
            }
            fileFormats.append(fileDescriptorDto.getDatasetFileFormatEnum());
        }
    }

    protected void serializeAlternativeRepresentations(TaskInfoDataset taskInfoDataset, StringBuilder alternativeRepresentations) throws IOException, FileNotFoundException {
        for (AlternativeEnumeratedRepresentation alternativeEnumeratedRepresentation : taskInfoDataset.getAlternativeRepresentations()) {
            if (alternativeRepresentations.length() > 0) {
                alternativeRepresentations.append(JobUtil.SERIALIZATION_SEPARATOR);
            }
            alternativeRepresentations.append(alternativeEnumeratedRepresentation.getComponentId()).append(JobUtil.SERIALIZATION_PAIR_SEPARATOR).append(alternativeEnumeratedRepresentation.getUrn());
        }
    }

    protected void serializeDatasetVersionRationaleTypes(TaskInfoDataset taskInfoDataset, StringBuilder datasetVersionRationaleTypes) throws IOException, FileNotFoundException {
        for (String datasetVersionRationaleType : taskInfoDataset.getDatasetVersionRationaleTypes()) {
            if (datasetVersionRationaleTypes.length() > 0) {
                datasetVersionRationaleTypes.append(JobUtil.SERIALIZATION_SEPARATOR);
            }
            datasetVersionRationaleTypes.append(datasetVersionRationaleType);
        }
    }

    protected void serializeDatasetVersionDataProvidersUrn(TaskInfoDataset taskInfoDataset, StringBuilder datasetVersionDataProvidersUrn) throws IOException, FileNotFoundException {
        for (String datasetVersionDataProviderUrn : taskInfoDataset.getDatasetVersionDataProviderUrn()) {
            if (datasetVersionDataProvidersUrn.length() > 0) {
                datasetVersionDataProvidersUrn.append(JobUtil.SERIALIZATION_SEPARATOR);
            }
            datasetVersionDataProvidersUrn.append(datasetVersionDataProviderUrn);
        }
    }

    private void processDatasets(ServiceContext ctx, TaskInfoDataset taskInfoDataset, DateTime dateTime) throws Exception {
        DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(taskInfoDataset.getDataStructureUrn());

        if (BooleanUtils.isTrue(taskInfoDataset.getStoreAlternativeRepresentations())) {
            saveAlternativeEnumeratedRepresetation(ctx, taskInfoDataset);
        }

        // Fetch and marks as final the Dataset's content constraints
        List<ContentConstraint> calculateConstraints = calculateConstraints(ctx, taskInfoDataset.getDatasetVersionId());

        // Validator
        ValidateDataVersusDsd validateDataVersusDsd = new ValidateDataVersusDsd(ctx, dataStructure, srmRestInternalService, calculateConstraints, taskInfoDataset);

        // Callbacks
        ManipulateSdmx21DataCallbackImpl callback = null;

        List<FileDescriptorResult> filesResult = new ArrayList<FileDescriptorResult>(taskInfoDataset.getFiles().size());

        for (FileDescriptor fileDescriptor : taskInfoDataset.getFiles()) {

            validateDataVersusDsd.setCurrentFilename(fileDescriptor.getFileName());

            String dataSourceId = generateDataSourceId(ctx, fileDescriptor.getFileName(), dateTime);

            Date nextUpdate = null;
            if (DatasetFileFormatEnum.SDMX_2_1.equals(fileDescriptor.getDatasetFileFormatEnum())) {
                if (callback == null) {
                    // Create the callback in the first appearance of a SDMX dataset
                    callback = new ManipulateSdmx21DataCallbackImpl(dataStructure, srmRestInternalService, metamac2StatRepoMapper, datasetRepositoriesServiceFacade,
                            taskInfoDataset.getDatasetVersionId(), validateDataVersusDsd);
                }

                callback.setDataSourceID(dataSourceId);
                Sdmx21Parser.parseData(new FileInputStream(fileDescriptor.getFile()), callback); // Parse and import

            } else if (DatasetFileFormatEnum.PX.equals(fileDescriptor.getDatasetFileFormatEnum())) {
                PxModel pxModel = manipulatePxDataService.importPx(ctx, fileDescriptor.getFile(), dataStructure, taskInfoDataset.getDatasetVersionId(), dataSourceId, validateDataVersusDsd);
                nextUpdate = pxModel.getNextUpdate();
            } else if (DatasetFileFormatEnum.CSV.equals(fileDescriptor.getDatasetFileFormatEnum())) {
                manipulateCsvDataService.importCsv(ctx, fileDescriptor.getFile(), dataStructure, taskInfoDataset.getDatasetVersionId(), dataSourceId, validateDataVersusDsd);
            }

            FileDescriptorResult fileDescriptorResult = new FileDescriptorResult();
            fileDescriptorResult.setDatasetFileFormatEnum(fileDescriptor.getDatasetFileFormatEnum());
            fileDescriptorResult.setFile(fileDescriptor.getFile());
            fileDescriptorResult.setFileName(fileDescriptor.getFileName());
            fileDescriptorResult.setDatasourceId(dataSourceId);
            fileDescriptorResult.setNextUpdate(nextUpdate);
            fileDescriptorResult.setStoreDimensionRepresentationMapping(taskInfoDataset.getStoreAlternativeRepresentations());
            fileDescriptorResult.setDimensionRepresentationMapping(validateDataVersusDsd.getAlternativeSourceEnumerationRepresentationMap().get(fileDescriptor.getFileName()));

            filesResult.add(fileDescriptorResult);

            validateDataVersusDsd.setCurrentFilename(null);
        }

        // Callback
        getDatasetService().proccessDatasetFileImportationResult(ctx, taskInfoDataset.getDatasetVersionId(), filesResult);
    }

    private String generateDataSourceId(ServiceContext serviceContext, String fileName, DateTime dateTime) {
        return (DatabaseDatasetImportUtils.isDatabaseDatasetImportJob(serviceContext)
                ? (String) serviceContext.getProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_DATASOURCE_IDENTIFIER)
                : Datasource.generateDataSourceId(fileName, dateTime));
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }

    /**
     * Fetch Dataset's content constraints
     *
     * @param ctx
     * @param datasetVersionUrn
     * @return
     * @throws MetamacException
     */
    private List<ContentConstraint> calculateConstraints(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        List<ResourceInternal> contentConstraintsForArtefact = constraintsService.findContentConstraintsForArtefact(ctx, datasetVersionUrn);

        List<ContentConstraint> result = new LinkedList<ContentConstraint>();
        for (ResourceInternal resourceInternal : contentConstraintsForArtefact) {
            // This means, you can come Draft, but in our business, the validation against constraints involves that are at least as marked as final
            ContentConstraint contentConstraint = constraintsService.retrieveContentConstraintByUrn(ctx, resourceInternal.getUrn(), Boolean.TRUE);
            result.add(contentConstraint);
        }

        return result;
    }

    private void saveAlternativeEnumeratedRepresetation(ServiceContext ctx, TaskInfoDataset taskInfoDataset) throws MetamacException {
        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, taskInfoDataset.getDatasetVersionId());
        for (FileDescriptor fileDescriptor : taskInfoDataset.getFiles()) {
            Map<String, String> mappings = alternativeEnumeratedRepresentationToMap(taskInfoDataset.getAlternativeRepresentations());
            datasetService.saveDimensionRepresentationMapping(ctx, datasetVersion.getDataset(), fileDescriptor.getFileName(), mappings);
        }
    }

    private Map<String, String> alternativeEnumeratedRepresentationToMap(List<AlternativeEnumeratedRepresentation> alternativeEnumeratedRepresentations) {
        Map<String, String> mappings = new LinkedHashMap<String, String>();
        for (AlternativeEnumeratedRepresentation representation : alternativeEnumeratedRepresentations) {
            mappings.put(representation.getComponentId(), representation.getUrn());
        }
        return mappings;
    }

    @Override
    public void processDatabaseDatasetPollingTask(ServiceContext ctx) throws MetamacException {
        taskServiceInvocationValidator.checkProcessDatabaseDatasetPollingTask(ctx);

        DateTime executionDate = new DateTime();

        List<DatasetVersion> datasetsVersions = retrieveDatabaseDatasets(ctx);

        if (!CollectionUtils.isEmpty(datasetsVersions)) {
            for (DatasetVersion datasetVersion : datasetsVersions) {
                if (!CollectionUtils.isEmpty(datasetVersion.getDatasources())) {
                    updateDataFromDatasources(ctx, executionDate, datasetVersion);
                } else {
                    logger.debug("There are no datasources configured yet for dataset {}", datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
                }
            }

        } else {
            logger.debug("There are no database datasets configured yet");
        }
    }

    @Override
    public void processGeographicCoverageCacheClearTask(ServiceContext ctx) throws MetamacException {
        taskServiceInvocationValidator.checkProcessGeographicCoverageCacheClearTask(ctx);

        DateTime executionDate = new DateTime();

        logger.info("Execution start - delete all disabled entries from geographic coverage cache at : {} ", executionDate);

        cacheService.deleteDisabledCacheEntries(ctx);

        executionDate = new DateTime();

        logger.info("Execution end - delete all disabled entries from geographic coverage cache at : {} ", executionDate);

    }

    private List<DatasetVersion> retrieveDatabaseDatasets(ServiceContext ctx) throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class)
                .withProperty(DatasetVersionProperties.dataSourceType()).eq(DataSourceTypeEnum.DATABASE)
                .and()
                .withProperty(DatasetVersionProperties.datasources()).isNotEmpty()
                .and()
                .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE)
                .distinctRoot().build();
        // @formatter:off

        List<DatasetVersion> datasetsVersion = datasetVersionRepository.findByCondition(conditions);
        return filterDatasetsWithTaskInProgress(ctx, datasetsVersion);
    }
    
    private List<DatasetVersion> filterDatasetsWithTaskInProgress(ServiceContext ctx, List<DatasetVersion> datasetsVersion) throws MetamacException {
        List<DatasetVersion> dsv = new ArrayList<>();
        if (datasetsVersion != null) {
            for (DatasetVersion datasetVersion : datasetsVersion) {
                if (!existsTaskForResource(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn())) {
                    dsv.add(datasetVersion);
                }
            }
        }
        return dsv;
    }
    
    private void updateDataFromDatasources(ServiceContext ctx, DateTime executionDate, DatasetVersion datasetVersion) {
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        for (Datasource datasource : datasetVersion.getDatasources()) {
            try {
                // Get table name from datasource and check if it exists
                String tableName = datasource.getSourceName();

                checkTableExists(tableName, datasetVersionUrn);

                // Get dimensions, attributes and measure columns name from dsd, get filter column name from configuration in common metadata and check if they exists
                DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());

                List<String> dimensionsColumnsName = getDimensionsColumnsName(dataStructure);

                List<String> attributesColumnsName = getAttributesColumnsName(dataStructure);

                String measureColumnName = getMeasureColumnName(dataStructure);

                String filterColumnName = getFilterColumnName();

                checkTableHasRequiredColumns(tableName, dimensionsColumnsName, attributesColumnsName, measureColumnName, filterColumnName, datasetVersionUrn);

                List<String> columnsName = getAllQueryColumns(dimensionsColumnsName, attributesColumnsName, measureColumnName);

                // Get de filter column value from last data import
                DateTime filterColumnValue = getFilterColumnValue(datasetVersion);

                // IDEA METAMAC-2866 A possible improve could be to do a paginated query to get the observations and write them to file progressively to avoid memory problems derivated from having a
                // huge number of observations in memory
                List<String[]> observations = getObservations(tableName, columnsName, filterColumnName, filterColumnValue);

                if (!observations.isEmpty()) {
                    File csvFile = generateCsvFile(tableName, columnsName, observations);

                    List<URL> fileUrls = new ArrayList<>();
                    fileUrls.add(csvFile.toURI().toURL());

                    logger.info("Planning a database import for dataset {} generated file: {} ", datasetVersionUrn, csvFile.getName());

                    setContextPropertiesForDbImportJob(ctx, executionDate, datasource);
                    importDatabaseDatasourcesInDatasetVersion(ctx, datasetVersionUrn, fileUrls, new HashMap<>(), Boolean.FALSE);

                    logger.info("Planned a database import for dataset {} generated file: {} ", datasetVersionUrn, csvFile.getName());
                    sendDatabaseImportationSuccessNotification(datasetVersion, tableName, MetamacRolesEnum.ADMINISTRADOR, MetamacRolesEnum.JEFE_PRODUCCION, MetamacRolesEnum.TECNICO_PRODUCCION, MetamacRolesEnum.TECNICO_APOYO_PRODUCCION);
                } else {
                    logger.debug("There are no new observations in table {} for dataset {}", tableName, datasetVersionUrn);
                }
            } catch (MetamacException e) {
                logger.error("An MetamacException error has occurred trying to do a database import for dataset {}", datasetVersionUrn, e);
                sendDatabaseImportationErrorNotification(ctx, datasetVersionUrn, e, MetamacRolesEnum.ADMINISTRADOR, MetamacRolesEnum.JEFE_PRODUCCION, MetamacRolesEnum.TECNICO_PRODUCCION, MetamacRolesEnum.TECNICO_APOYO_PRODUCCION);
            } catch (Exception e) {
                logger.error("An unexpected error has occurred trying to do a database import for dataset {}", datasetVersionUrn, e);
            }
        }
    }

    private void checkTableExists(String tableName, String datasetVersionUrn) throws MetamacException {
        if (!databaseImportRepository.checkTableExists(tableName)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TABLE_NOT_EXIST).withMessageParameters(tableName, datasetVersionUrn).build();
        }
    }
    
    private List<String> getDimensionsColumnsName(DataStructure dataStructure) {
        List<String> dimensionsColumnName = new ArrayList<>();

        for (DimensionBase dimensionBase : dataStructure.getDataStructureComponents().getDimensions().getDimensions()) {
            dimensionsColumnName.add(dimensionBase.getId());
        }

        return dimensionsColumnName;
    }
    
    private List<String> getAttributesColumnsName(DataStructure dataStructure) throws MetamacException {
        List<String> attibutesColumnName = new ArrayList<>();

        for (AttributeBase attributeBase : dataStructure.getDataStructureComponents().getAttributes().getAttributes()) {
            DsdAttribute dsdAttribute = new DsdAttribute((Attribute) attributeBase);
            if (dsdAttribute.isAttributeAtObservationLevel()) {
                attibutesColumnName.add(attributeBase.getId());
            }
        }

        return attibutesColumnName;
    }
    
    private String getMeasureColumnName(DataStructure dataStructure) {
        return dataStructure.getDataStructureComponents().getMeasure().getPrimaryMeasure().getId();
    }
    
    private String getFilterColumnName() throws MetamacException {
        return configurationService.retriveFilterColumnNameForDbDataImport();
    }
    
    private List<String> getAllQueryColumns(List<String> dimensionsColumnsName, List<String> attributesColumnsName, String measureColumnName) {
        List<String> queryColumns = new ArrayList<>();

        queryColumns.addAll(dimensionsColumnsName);
        queryColumns.add(measureColumnName);
        queryColumns.addAll(attributesColumnsName);

        return queryColumns;
    }
    
    private void checkTableHasRequiredColumns(String tableName, List<String> dimensionsColumnsName, List<String> attributesColumnsName, String measureColumnName, String filterColumnName,
            String datasetVersionUrn) throws MetamacException {
        checkTableHasDimensionColumns(tableName, dimensionsColumnsName, datasetVersionUrn);
        checkTableHasAttibuteColumns(tableName, attributesColumnsName, datasetVersionUrn);
        checkTableHasObservationColumn(tableName, measureColumnName, datasetVersionUrn);
        checkTableHasFilterColumn(tableName, filterColumnName, datasetVersionUrn);
    }
    
    private void checkTableHasDimensionColumns(String tableName, List<String> columnsName, String datasetVersionUrn) throws MetamacException {
        checkTableHasColumns(ServiceExceptionType.DIMENSION_COLUMN_NOT_EXIST, datasetVersionUrn, tableName, columnsName);
    }
    
    private void checkTableHasAttibuteColumns(String tableName, List<String> attributesColumnsName, String datasetVersionUrn) throws MetamacException {
        checkTableHasColumns(ServiceExceptionType.ATTRIBUTE_COLUMN_NOT_EXIST, datasetVersionUrn, tableName, attributesColumnsName);
    }
    
    private void checkTableHasObservationColumn(String tableName, String observationColumnName, String datasetVersionUrn) throws MetamacException {
        checkTableHasColumns(ServiceExceptionType.OBS_COLUMN_NOT_EXIST, datasetVersionUrn, tableName, Arrays.asList(observationColumnName));
    }
    
    private void checkTableHasFilterColumn(String tableName, String filterColumn, String datasetVersionUrn) throws MetamacException {
        checkTableHasColumns(ServiceExceptionType.FILTER_COLUMN_NOT_EXIST, datasetVersionUrn, tableName, Arrays.asList(filterColumn));
    }
    
    private void checkTableHasColumns(CommonServiceExceptionType commonServiceExceptionType, String datasetVersionUrn, String tableName, List<String> columnsName) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();

        for (String columnName : columnsName) {
            if (!databaseImportRepository.checkTableHasColumn(tableName, columnName)) {
                exceptionItems.add(new MetamacExceptionItem(commonServiceExceptionType, tableName, datasetVersionUrn, columnName));
            }
        }

        if (!exceptionItems.isEmpty()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
    }
    
    private DateTime getFilterColumnValue(DatasetVersion datasetVersion) {
        return datasetVersion.getDateLastTimeDataImport();
    }
    
    private List<String[]> getObservations(String tableName, List<String> columnsName, String filterColumnName, DateTime filterColumnValue) throws MetamacException {
        return databaseImportRepository.getObservations(tableName, columnsName, filterColumnName, filterColumnValue);
    }
    
    private File generateCsvFile(String tableName, List<String> columnsName, List<String[]> observations) throws MetamacException {
        return writeTempCsvFile(createTempCsvFile(tableName), observations, columnsName);
    }
    
    private File createTempCsvFile(String tableName) throws MetamacException {
        try {
            return File.createTempFile("dbImport_" + tableName + "_", ".csv");
        } catch (IOException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.IMPORTATION_CSV_FILE_ERROR).withMessageParameters(ExceptionHelper.excMessage(e)).build();
        }
    }
    
    private File writeTempCsvFile(File file, List<String[]> observations, List<String> columnsName) throws MetamacException {
        try (OutputStream os = new FileOutputStream(file); CsvWriter csvWriter = new CsvWriter(os, "UTF-8", CsvConstants.SEPARATOR_TAB)) {
            logger.debug("Temporary csv file created {}", file.getAbsolutePath());
            csvWriter.write(columnsName.toArray(new String[0]), observations);
            return file;
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.IMPORTATION_CSV_FILE_ERROR).withMessageParameters(ExceptionHelper.excMessage(e)).build();
        }
    }
    
    private void setContextPropertiesForDbImportJob(ServiceContext ctx, DateTime executionDate, Datasource datasource) {
        ctx.setProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_DATASOURCE_IDENTIFIER, datasource.getIdentifiableStatisticalResource().getCode());
        ctx.setProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_EXECUTION_DATE, executionDate);
        ctx.setProperty(ImportDatasetFromDatabaseJob.DATABASE_IMPORT_JOB_FLAG, Boolean.TRUE);
    }

    public void importDatabaseDatasourcesInDatasetVersion(ServiceContext ctx, String datasetVersionUrn, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping) throws MetamacException {
        datasetService.importDatabaseDatasourcesInDatasetVersion(ctx, datasetVersionUrn, fileUrls, dimensionRepresentationMapping, storeDimensionRepresentationMapping);
    }

    private void sendDatabaseImportationSuccessNotification(DatasetVersion datasetVersion, String dataTable, MetamacRolesEnum... roles) {
        getNoticesRestInternalService().createDatabaseImportSuccessBackgroundNotification(datasetVersion, ServiceNoticeAction.DATABASE_IMPORT_DATASET_JOB, ServiceNoticeMessage.IMPORT_DATASET_DATABASE_JOB_OK, dataTable, roles);
    }

    private void sendDatabaseImportationErrorNotification(ServiceContext ctx, String datasetVersionUrn, MetamacException metamacException,MetamacRolesEnum... roles) {
        try {
            taskServiceInvocationValidator.checkSendDatabaseImportationErrorNotification(ctx, datasetVersionUrn, metamacException);

            DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
            getNoticesRestInternalService().createDatabaseImportErrorBackgroundNotification(datasetVersion, ServiceNoticeAction.DATABASE_IMPORT_DATASET_JOB, metamacException, roles);
        } catch (MetamacException e) {
            // If an error occurred sending the notification, it must be logged but it mustn't be threw to avoid generate more additional noise to the previous error
            logger.error("Error sending database importation error notification:", e);
        }        
    }

    @Override
    public void sendDatabaseImportationErrorNotification(ServiceContext ctx, String datasetVersionUrn, MetamacException metamacException) {
        try {
            taskServiceInvocationValidator.checkSendDatabaseImportationErrorNotification(ctx, datasetVersionUrn, metamacException);

            DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
            getNoticesRestInternalService().createDatabaseImportErrorBackgroundNotification(datasetVersion, ServiceNoticeAction.DATABASE_IMPORT_DATASET_JOB, metamacException);
        } catch (MetamacException e) {
            // If an error occurred sending the notification, it must be logged but it mustn't be threw to avoid generate more additional noise to the previous error
            logger.error("Error sending database importation error notification:", e);
        }        
    }
    
    abstract class MetamacExceptionTransactionCallback<T> implements TransactionCallback<T> {

        @Override
        public final T doInTransaction(TransactionStatus status) {
            try {
                return doInMetamacTransaction(status);
            } catch (MetamacException e) {
                throw new RuntimeException("Error in transactional method", e);
            }
        }

        protected abstract T doInMetamacTransaction(TransactionStatus status) throws MetamacException;
    }

    @Override
    public void importAttributesInDatasetVersion(ServiceContext ctx, String dataVersionUrn, TaskInfoDataset taskInfoDataset) throws MetamacException {
        try {
            taskServiceInvocationValidator.checkImportAttributesInDatasetVersion(ctx, dataVersionUrn, taskInfoDataset);
            DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(taskInfoDataset.getDataStructureUrn());
            List<String> idsDimensions = getDimensionsColumnsName(dataStructure);
            Map<String, List<CodeDimension>> codeDimensions = getCodeDimensions(ctx, idsDimensions, dataVersionUrn);
            List<DsdAttribute> dsdAttributes = DsdProcessor.getAttributes(dataStructure);
            Map<String, List<ExternalItemDto>> externalItemsAttributesId = getExternalItemsFromSrm(dsdAttributes);
            List<String> languages = configurationService.retrieveLanguages();
            for (FileDescriptor fileDescriptor : taskInfoDataset.getFiles()) {
                manipulateCsvDataService.importCsvAttributes(fileDescriptor.getFile(), dataStructure, codeDimensions, externalItemsAttributesId, ctx, dataVersionUrn, languages);
            }
        } catch(MetamacException e) {
            throw e;
        } catch (Exception e) {
            throw  MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e))
                    .build();
        }
    }

    private Map<String, List<CodeDimension>> getCodeDimensions(ServiceContext ctx, List<String> idsDimensions, String datasetVersionUrn) throws MetamacException {
        Map<String, List<CodeDimension>> codesDimensions = new HashMap<>();
        for (String idDimension : idsDimensions) {
            List<CodeDimension> codes = datasetService.filterCoverageForDatasetVersionDimension(ctx, datasetVersionUrn,
                    idDimension, null);
            codesDimensions.put(idDimension, codes);
        }
        return codesDimensions;
    }

    private Map<String, List<ExternalItemDto>> getExternalItemsFromSrm(List<DsdAttribute> dsdAttributes) throws MetamacException {
        Map<String, List<ExternalItemDto>> codes = new HashMap<>();
        for (DsdAttribute dsdAttribute : dsdAttributes) {
            if (!dsdAttribute.isAttributeAtObservationLevel()) {
                codes.put(dsdAttribute.getComponentId(), StatisticalResourcesExternalItemUtils.buildExternalItemDtoFromCodes(srmRestInternalService.findCodes(dsdAttribute.getCodelistRepresentationUrn(), 0, null, "")));
            }
        }
        return codes;
    }
    
    @Override
    public void processResendKafkaDatasetMessageTask(ServiceContext ctx) throws MetamacException {
        
        taskServiceInvocationValidator.checkProcessResendKafkaDatasetMessageTask(ctx); 

        datasetLifecycleService.resendDatasetStreamMessage(ctx);
        
    }
    
    @Override
    public void scheduleResendKafkaDatasetMessageJob(ServiceContext ctx) {
        try {
            taskServiceInvocationValidator.checkScheduleResendKafkaDatasetMessageJob(ctx); 

            JobDetail job = newJob(ResendPublishedDatasetsKafkaMessageJob.class).build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(configurationService.retrieveCronExpressionForResendPublishedDatasetKafkaMessage()).withMisfireHandlingInstructionDoNothing()).build();

            
            CronExpression cronEx = new CronExpression(cronTrigger.getCronExpression());

            if (cronEx.getNextValidTimeAfter(new Date()) == null) {
                logger.info("ATENTION!! Cron scheduler for resend all published last version dataset kafka messages  is before actual date. For this reason the job has been aborted and it will not never executed ");
                return;
            }
            
            Scheduler sched = schedulerFactory.getScheduler();
            sched.scheduleJob(job, cronTrigger);

            logger.info("resend all published last version dataset kafka messages at {} ", new Date());

        } catch (Exception e) {
            logger.error("An unexpected error has occurred scheduling resend all published last version dataset kafka messages job", e);
        }
    }

    @Override
    public String planifyUpdateGeographicalCacheRelatedResource(ServiceContext ctx, TaskInfoResources taskInfoResources, boolean sendNotification) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkPlanifyUpdateGeographicalCacheRelatedResource(ctx, taskInfoResources, sendNotification);

        String resourceUrn = taskInfoResources.getUrn();
        String resourceVersionUrn = taskInfoResources.getVersionId();
        String taskName = createJobNameForUpdateGeoCacheRelatedResources(resourceVersionUrn);

        // Job keys
        JobKey jobKey = createJobKeyForUpdateGeoCacheRelatedResources(resourceUrn);
        TriggerKey triggerKey = createTriggerKeyForUpdateGeoCacheRelatedResources(resourceUrn);

        try {
            checkExistTaskInResource(ctx, jobKey, resourceUrn);

            // @formatter:off
            JobDetail job = newJob(UpdateGeocoverageCacheRelatedResourcesJob.class)
                    .withIdentity(jobKey)
                    .usingJobData(UpdateGeocoverageCacheRelatedResourcesJob.RESOURCE_VERSION_ID, resourceVersionUrn)
                    .usingJobData(UpdateGeocoverageCacheRelatedResourcesJob.USER, ctx.getUserId())
                    .usingJobData(UpdateGeocoverageCacheRelatedResourcesJob.RESOURCE_URN, resourceUrn)
                    .usingJobData(UpdateGeocoverageCacheRelatedResourcesJob.RESOURCE_TYPE, taskInfoResources.getResourceType())
                    .usingJobData(UpdateGeocoverageCacheRelatedResourcesJob.TASK_NAME, taskName)
                    .usingJobData(UpdateGeocoverageCacheRelatedResourcesJob.SEND_NOTIFICATION, sendNotification)
                    .requestRecovery()
                    .build();
            // @formatter:on

            Task task = new Task(taskName);
            task.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            task.setExtensionPoint(resourceUrn);
            createTask(ctx, task);

            SimpleTrigger trigger = newTrigger().withIdentity(triggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();
            scheduleUpdateGeographicalCacheRelatedResourceJob(jobKey, job, trigger);

        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }

        return jobKey.getName();
    }

    private void scheduleUpdateGeographicalCacheRelatedResourceJob(JobKey jobKey, JobDetail job, SimpleTrigger trigger) {
        try {
            // Scheduler a duplication job
            Scheduler sched = SchedulerRepository.getInstance().lookup(SCHEDULER_INSTANCE_NAME); // get a reference to a scheduler
            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.error("planifyUpdateGeographicalCacheRelatedResource for related resources: the job with key " + jobKey.getName() + " has failed", e);
        }
    }

    @Override
    public void processUpdateGeographicalCacheRelatedResourceTask(ServiceContext ctx, String jobKey, TaskInfoResources taskInfoResource) throws MetamacException {
        // Validation
        taskServiceInvocationValidator.checkProcessUpdateGeographicalCacheRelatedResourceTask(ctx, jobKey, taskInfoResource);

        logger.debug("START - Processing updating geographic cache related resource task");

        StatisticalResourceTypeEnum resourceType = StatisticalResourceTypeEnum.valueOf(taskInfoResource.getResourceType());

        if (StatisticalResourceTypeEnum.COLLECTION.equals(resourceType)) {

            processGeoCacheRelatedCollection(ctx, taskInfoResource);

        } else if (StatisticalResourceTypeEnum.QUERY.equals(resourceType)) {
            processGeoCacheRelatedCollection(ctx, taskInfoResource);
        }

        logger.debug("FINISHED - Processing updating geographic cache related resource task correctlY");

        markTaskAsFinished(ctx, jobKey);

    }

    private void processGeoCacheRelatedCollection(ServiceContext ctx, TaskInfoResources taskInfoResource) throws MetamacException {
        logger.debug("> START Subprocess - Processing updating geographic cache related resource task - collections {}", taskInfoResource.getVersionId());
        PublicationVersion publicationVersion = publicationService.retrievePublicationVersionByUrn(ctx, taskInfoResource.getVersionId());

        boolean isLastVersionPublished = isPublicationLastVersionPublished(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getUrn());

        // only it is necessary to save in cache last version of related resources.
        if (isLastVersionPublished) {

            cacheService.processGeoCacheRelatedCollection(ctx, publicationVersion, isLastVersionPublished, taskInfoResource.getUrn());

        } else {
            logger.info(
                    "> check is resource last version. The result was FALSE and the resource it  will not inserted in cache - Processing updating geographic cache related resource task - collections {}",
                    taskInfoResource.getVersionId());
        }

        logger.debug("> END Subprocess - Processing updating geographic cache related resource task - collections {}", taskInfoResource.getVersionId());
    }

    /*
     * if cache is manually updated, dataset can be in draft and this version is lastversion. For this case, it is necessary to calculate if this dataset is last published version
     */
    private boolean isPublicationLastVersionPublished(ServiceContext ctx, String publicationVersionUrn) throws MetamacException {
        //
        String[] params = UrnUtils.splitUrnItemScheme(publicationVersionUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        PublicationVersion lastVersionPublication = publicationService.getPublicationLastVersionPublished(ctx, agencyId, resourceId);
        return lastVersionPublication != null && lastVersionPublication.getSiemacMetadataStatisticalResource().getUrn().equals(publicationVersionUrn);
    }
}
