package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
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
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class UpdateResourceLastUpdateJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(UpdateResourceLastUpdateJob.class);

    public static final String RESOURCE_URN       = "resourceUrn";
    public static final String TIMESTAMP          = "timestamp";
    public static final String USER               = "user";
    public static final String TASK_NAME          = "taskName";
    public static final String SEND_NOTIFICATION  = "sendNotification";

    private TaskServiceFacade taskServiceFacade = null;

    public UpdateResourceLastUpdateJob() {
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

        String resourceUrn = data.getString(RESOURCE_URN);
        long timestamp = data.getLong(TIMESTAMP);
        String user = data.getString(USER);
        String taskName = data.getString(TASK_NAME);
        boolean sendNotification = data.getBoolean(SEND_NOTIFICATION);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");

        try {
            logger.info("UpdateResourceLastUpdateJob: {} starting at {}", jobKey, new Date());
            getTaskServiceFacade().executeUpdateResourceLastUpdateTask(serviceContext, taskName, resourceUrn, timestamp);
            logger.info("UpdateResourceLastUpdateJob: {} finished at {}", jobKey, new Date());
        } catch (MetamacException e) {
            logger.error("UpdateResourceLastUpdateJob: the last update job with key {} has failed", jobKey.getName(), e);
            if (sendNotification) {
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.UPDATE_OF_RESOURCE_LAST_UPDATE_CACHE_FAILED, e);
            }
            try {
                getTaskServiceFacade().markTaskAsFailed(serviceContext, taskName, resourceUrn, resourceUrn, e);
                logger.info("UpdateResourceLastUpdateJob: {} marked as error at {}", jobKey, new Date());
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_RESOURCE_LAST_UPDATE_JOB_ERROR, resourceUrn));
            } catch (MetamacException e1) {
                logger.error("UpdateResourceLastUpdateJob: the last update job with key {} has failed and it can't be marked as error", jobKey.getName(), e1);
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_RESOURCE_LAST_UPDATE_JOB_ERROR_AND_CANT_MARK_AS_ERROR, resourceUrn));
            }
        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
