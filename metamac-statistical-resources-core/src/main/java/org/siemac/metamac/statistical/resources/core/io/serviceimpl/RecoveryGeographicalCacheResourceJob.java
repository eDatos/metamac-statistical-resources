package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
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
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecoveryGeographicalCacheResourceJob implements Job {

    private static Logger      logger              = LoggerFactory.getLogger(RecoveryGeographicalCacheResourceJob.class);

    public static final String USER                = "user";
    public static final String RESOURCE_VERSION_ID = "versionId";
    public static final String RESOURCE_URN        = "urn";
    public static final String RESOURCE_TYPE       = "resourceType";
    public static final String SEND_NOTIFICATION   = "sendNotification";

    private TaskServiceFacade  taskServiceFacade   = null;

    /**
     * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
     */
    public RecoveryGeographicalCacheResourceJob() {
        // without impl
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

        // Parameters
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String user = data.getString(USER);
        boolean sendNotification = data.getBoolean(SEND_NOTIFICATION);
        TaskInfoResources taskInfoResource = setDataIntoTask(data);

        // Execution
        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");

        try {

            if (logger.isDebugEnabled()) {
                logger.info(String.format("RecoveryGeographicalCacheResourceJob: %s starting at %tc", jobKey, new Date()));
            }

            getTaskServiceFacade().executeRecoveryGeographicalCacheTask(serviceContext, jobKey.getName(), taskInfoResource);

            if (logger.isDebugEnabled()) {
                logger.info(String.format("RecoveryGeographicalCacheResourceJob: %s  finished at %tc", jobKey, new Date()));
            }

        } catch (Exception e) {
            logger.error("RecoveryGeographicalCacheResourceJob: the geographical cache updating resource with key " + jobKey.getName() + " has failed", e);
        } finally {
            if (sendNotification) {
                MetamacException metamacException = MetamacExceptionBuilder.builder().withPrincipalException(ServiceExceptionType.TASKS_ERROR_SERVER_DOWN, jobKey).build();
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.CANCEL_IN_PROGRESS_TASKS_WHILE_SERVER_SHUTDOWN, metamacException);
            }
        }
    }

    private TaskInfoResources setDataIntoTask(JobDataMap data) {

        String resourceVersionId = data.getString(RESOURCE_VERSION_ID);
        String resourceUrn = data.getString(RESOURCE_URN);
        String resourceType = data.getString(RESOURCE_TYPE);

        TaskInfoResources taskInfoResource = new TaskInfoResources();
        taskInfoResource.setVersionId(resourceVersionId);
        taskInfoResource.setUrn(resourceUrn);
        taskInfoResource.setResourceType(resourceType);

        return taskInfoResource;
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
