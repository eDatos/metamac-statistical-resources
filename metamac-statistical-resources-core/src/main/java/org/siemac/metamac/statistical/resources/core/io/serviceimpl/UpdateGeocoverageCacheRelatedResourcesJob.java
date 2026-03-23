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
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateGeocoverageCacheRelatedResourcesJob implements Job {

    private static Logger      logger              = LoggerFactory.getLogger(UpdateGeocoverageCacheRelatedResourcesJob.class);

    public static final String USER                = "user";
    public static final String RESOURCE_VERSION_ID = "versionId";
    public static final String RESOURCE_URN        = "urn";
    public static final String RESOURCE_TYPE       = "resourceType";
    public static final String TASK_NAME           = "taskName";
    public static final String SEND_NOTIFICATION   = "sendNotification";

    private TaskServiceFacade  taskServiceFacade   = null;

    /**
     * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
     */
    public UpdateGeocoverageCacheRelatedResourcesJob() {
        // without explicit initializations
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

        String user = data.getString(USER);
        String taskName = data.getString(TASK_NAME);
        boolean sendNotification = data.getBoolean(SEND_NOTIFICATION);
        TaskInfoResources taskInfoResource = setDataIntoTask(data);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");

        try {
            logger.info("UpdateGeocoverageCacheJob for related resource: {} starting at {}", jobKey, new Date());

            getTaskServiceFacade().executeUpdateGeographicalCacheRelatedResourceTask(serviceContext, taskName, taskInfoResource);
            logger.info("UpdateGeocoverageCacheJob  for related resource: {} finished at {}", jobKey, new Date());

        } catch (MetamacException e) {
            manageExceptions(e, serviceContext, taskInfoResource, sendNotification, jobKey, user, taskName);
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

    private void manageExceptions(MetamacException e, ServiceContext ctx, TaskInfoResources taskInfoResource, boolean sendNotification, JobKey jobKey, String user, String taskName) {
        logger.error("UpdateGeocoverageCacheJob for related resource: the cache update job with key " + jobKey.getName() + " has failed", e);
        if (sendNotification) {
            getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.UPDATE_GEOGRAPHICAL_RELATED_CACHE_JOB, e);
        }

        try {
            getTaskServiceFacade().markTaskAsFailed(ctx, taskName, taskInfoResource.getVersionId(), taskInfoResource.getUrn(), e);
            logger.info("UpdateGeocoverageCacheJob: {} marked as error at {}", jobKey, new Date());
            e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_RELATED_CACHE_JOB_ERROR, taskInfoResource.getVersionId()));
        } catch (MetamacException e1) {
            logger.error("UpdateGeocoverageCacheJob: the cache update job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
            e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_RELATED_CACHE_JOB_ERROR_AND_CANT_MARK_AS_ERROR, taskInfoResource.getVersionId()));
        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
