package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateGeocoverageCacheJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(UpdateGeocoverageCacheJob.class);

    public static final String USER = "user";
    public static final String DATASET_VERSION_ID = "datasetVersionId";
    public static final String DATASET_URN = "datasetUrn";
    public static final String TASK_NAME = "taskName";
    public static final String SEND_NOTIFICATION = "sendNotification";

    private TaskServiceFacade taskServiceFacade = null;

    /**
     * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
     */
    public UpdateGeocoverageCacheJob() {
    }

    public TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap data = context.getJobDetail().getJobDataMap();

        String datasetVersionId = data.getString(DATASET_VERSION_ID);
        String user = data.getString(USER);
        String datasetUrn = data.getString(DATASET_URN);
        String taskName = data.getString(TASK_NAME);
        boolean sendNotification = data.getBoolean(SEND_NOTIFICATION);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");

        try {
            logger.info("UpdateGeocoverageCacheJob: {} starting at {}", jobKey, new Date());

            TaskInfoDataset taskInfoDataset = new TaskInfoDataset();
            taskInfoDataset.setDatasetVersionId(datasetVersionId);

            getTaskServiceFacade().executeUpdateGeocoverageCacheTask(serviceContext, taskName, taskInfoDataset);
            logger.info("UpdateGeocoverageCacheJob: {} finished at {}", jobKey, new Date());

        } catch (MetamacException e) {
            logger.error("UpdateGeocoverageCacheJob: the cache update job with key " + jobKey.getName() + " has failed", e);
            if (sendNotification) {
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB, e);
            }

            try {
                getTaskServiceFacade().markTaskAsFailed(serviceContext, taskName, datasetVersionId, datasetUrn, e);
                logger.info("UpdateGeocoverageCacheJob: {} marked as error at {}", jobKey, new Date());
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_ERROR, datasetVersionId));
            } catch (MetamacException e1) {
                logger.error("UpdateGeocoverageCacheJob: the cache update job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_DATASET_JOB_ERROR_AND_CANT_MARK_AS_ERROR, datasetVersionId));
            }
        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
