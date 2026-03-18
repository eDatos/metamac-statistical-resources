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
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoResources;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateExternalGeocoverageCacheJob implements Job {

    private static Logger      logger            = LoggerFactory.getLogger(UpdateExternalGeocoverageCacheJob.class);

    public static final String USER              = "user";
    public static final String TASK_NAME         = "taskName";
    public static final String RESOURCE_TYPE     = "resourceType";
    public static final String SEND_NOTIFICATION = "sendNotification";

    private TaskServiceFacade  taskServiceFacade = null;

    /**
     * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
     */
    public UpdateExternalGeocoverageCacheJob() {
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
        JobDataMap data = context.getJobDetail().getJobDataMap();

        String user = data.getString(USER);
        String taskName = data.getString(TASK_NAME);
        TaskInfoResources taskInfoResource = setDataIntoTask(data);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "statistical-resources-core");

        try {
            logger.info("UpdateExternalGeocoverageCacheJob: {} starting at {}", jobKey, new Date());

            getTaskServiceFacade().executeUpdateExternalGeocoverageCacheTask(serviceContext, taskName, taskInfoResource);
            logger.info("UpdateExternalGeocoverageCacheJob: {} finished at {}", jobKey, new Date());

        } catch (MetamacException e) {
            logger.error("UpdateExternalGeocoverageCacheJob: the cache update job with key " + jobKey.getName() + " has failed", e);

            try {
                getTaskServiceFacade().markTaskAsFinished(serviceContext, taskName);
                logger.info("UpdateExternalGeocoverageCacheJob: {} marked as error at {}", jobKey, new Date());
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_RESOURCE_JOB_FROM_EXTERNAL_PUBLICATION_ERROR));
            } catch (MetamacException e1) {
                logger.error("UpdateExternalGeocoverageCacheJob: the cache update job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_RESOURCE_FROM_EXTERNAL_PUBLICATION_JOB_ERROR_AND_CANT_MARK_AS_ERROR,
                        taskInfoResource.getResourceType()));
            }
        }
    }

    private TaskInfoResources setDataIntoTask(JobDataMap data) {

        String resourceType = data.getString(RESOURCE_TYPE);

        TaskInfoResources taskInfoResource = new TaskInfoResources();
        taskInfoResource.setResourceType(resourceType);

        return taskInfoResource;
    }
}
