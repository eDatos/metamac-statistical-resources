package org.siemac.metamac.statistical.resources.web.server.listener;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class JobsSchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger     logger = LoggerFactory.getLogger(JobsSchedulerListener.class);

    @Autowired
    private TaskServiceFacade taskServiceFacade;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.debug("Scheduling jobs...");
        schedulingDatabaseDatasetPollingJob();
        schedulingGeographicCoverageCacheClearJob();
        schedulingResendKafkaDatasetMessageJob();
        markAllInProgressTaskToFailed();
    }

    private void schedulingDatabaseDatasetPollingJob() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");
        taskServiceFacade.scheduleDatabaseDatasetPollingJob(ctx);
    }

    private void schedulingGeographicCoverageCacheClearJob() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");
        taskServiceFacade.scheduleGeographicCoverageCacheClearJob(ctx);
    }

    private void schedulingResendKafkaDatasetMessageJob() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");
        taskServiceFacade.scheduleResendKafkaDatasetMessageJob(ctx);
    }

    private void markAllInProgressTaskToFailed() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");
        try {
            logger.info("Launching markAllInProgressTaskToFailed in order to recover all failed and in progress tasks");
            taskServiceFacade.markAllInProgressTaskToFailed(ctx);
        } catch (MetamacException e) {
            logger.error("Impossible to mark old jobs with running state to failed state.", e);
        }
    }
}
