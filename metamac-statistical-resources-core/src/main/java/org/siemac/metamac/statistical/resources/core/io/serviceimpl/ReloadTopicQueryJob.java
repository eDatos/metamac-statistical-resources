package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class ReloadTopicQueryJob implements Job {

    private static Logger     logger            = LoggerFactory.getLogger(ReloadTopicQueryJob.class);

    private TaskServiceFacade taskServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("reload kafka topic for all published last version queries at {}", new Date());
            ServiceContext serviceContext = new ServiceContext("Metamac", context.getFireInstanceId(), "statistical-resources-core");
            getTaskServiceFacade().executeReloadKafkaTopicQueryTask(serviceContext);
            logger.debug("reload kafka topic for all published last version queries completed at {}", new Date());
        } catch (MetamacException e) {
            logger.error("An unexpected error has occurred during reload kafka topic for all published last version queries job execution", e);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

}
