package org.siemac.metamac.statistical.resources.core.task.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class UpdateResourceBusinessLastUpdateJob implements Job {

    private static Logger     logger            = LoggerFactory.getLogger(UpdateResourceBusinessLastUpdateJob.class);

    public static final String RESOURCE_URN      = "resourceUrn";
    public static final String RESOURCE_ROOT_URN = "resourceRootUrn";
    public static final String RESOURCE_TYPE     = "resourceType";
    public static final String TIMESTAMP         = "timestamp";
    public static final String USER              = "user";
    public static final String TASK_NAME         = "taskName";
    public static final String SEND_NOTIFICATION = "sendNotification";

    private TaskServiceFacade taskServiceFacade = null;

    public UpdateResourceBusinessLastUpdateJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap data = context.getJobDetail().getJobDataMap();

        String user = data.getString(USER);
        String resourceUrn = data.getString(RESOURCE_URN);
        String resourceRootUrn = data.getString(RESOURCE_ROOT_URN);
        String resourceType = data.getString(RESOURCE_TYPE);
        long timestamp = data.getLong(TIMESTAMP);
        String taskName = data.getString(TASK_NAME);
        boolean sendNotification = data.containsKey(SEND_NOTIFICATION) && data.getBoolean(SEND_NOTIFICATION);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("UpdateResourceBusinessLastUpdateJob: {} starting at {}", jobKey, new Date());
            }

            getTaskServiceFacade().executeUpdateResourceBusinessLastUpdateTask(serviceContext, taskName, resourceUrn, resourceRootUrn, resourceType, timestamp);

            if (logger.isDebugEnabled()) {
                logger.debug("UpdateResourceBusinessLastUpdateJob: {} finished at {}", jobKey, new Date());
            }
        } catch (MetamacException e) {
            logger.error("UpdateResourceBusinessLastUpdateJob: task with key " + jobKey.getName() + " has failed", e);
            if (sendNotification) {
                getNoticesRestInternalService().createErrorBackgroundNotification(null, ServiceNoticeAction.BUSINESS_LAST_UPDATE_JOB_FAILED, e);
            }
            try {
                getTaskServiceFacade().markTaskAsFailed(serviceContext, taskName, resourceUrn, resourceRootUrn, e);
            } catch (Exception markError) {
                logger.error("UpdateResourceBusinessLastUpdateJob: could not mark task " + jobKey.getName() + " as failed", markError);
            }
        } catch (Exception e) {
            logger.error("UpdateResourceBusinessLastUpdateJob: task with key " + jobKey.getName() + " has failed", e);
            MetamacException metamacException = MetamacExceptionBuilder.builder().withPrincipalException(ServiceExceptionType.BUSINESS_LAST_UPDATE_JOB_ERROR, resourceUrn).withCause(e).build();
            if (sendNotification) {
                getNoticesRestInternalService().createErrorBackgroundNotification(null, ServiceNoticeAction.BUSINESS_LAST_UPDATE_JOB_FAILED, metamacException);
            }
            try {
                getTaskServiceFacade().markTaskAsFailed(serviceContext, taskName, resourceUrn, resourceRootUrn, metamacException);
            } catch (Exception markError) {
                logger.error("UpdateResourceBusinessLastUpdateJob: could not mark task " + jobKey.getName() + " as failed", markError);
            }
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }
        return taskServiceFacade;
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
