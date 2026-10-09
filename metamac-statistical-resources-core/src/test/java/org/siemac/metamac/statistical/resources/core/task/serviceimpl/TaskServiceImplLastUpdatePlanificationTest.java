package org.siemac.metamac.statistical.resources.core.task.serviceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.SchedulerRepository;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.task.domain.Task;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskProperties;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskRepository;
import org.siemac.metamac.statistical.resources.core.task.exception.TaskNotFoundException;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

// Not @Transactional: every test drives its own transactions to observe commit and rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
public class TaskServiceImplLastUpdatePlanificationTest extends StatisticalResourcesBaseTest {

    private static final String        RESOURCE_URN = "urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=TEST:LAST_UPDATE_PLANIFICATION_TEST(01.000)";
    private static final String        TASK_NAME    = TaskServiceImpl.PREFIX_JOB_UPDATE_RESOURCE_LAST_UPDATE + RESOURCE_URN;
    private static final JobKey        JOB_KEY      = new JobKey(TASK_NAME);

    @Autowired
    private TaskService                taskService;

    @Autowired
    private TaskRepository             taskRepository;

    @Autowired
    @Qualifier("txManager")
    private PlatformTransactionManager txManager;

    @After
    public void cleanUp() throws Exception {
        // Must run before the 10 s trigger fires, or UpdateResourceLastUpdateJob would run against the fake resource
        scheduler().deleteJob(JOB_KEY);
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    taskRepository.delete(taskRepository.findByKey(TASK_NAME));
                } catch (TaskNotFoundException e) {
                    // Nothing to clean
                }
            }
        });
    }

    @Test
    public void testPlanifyTwiceInSameTransactionCreatesOneTask() throws Exception {
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                planify();
                planify();
                assertEquals(1, countTasks());
                status.setRollbackOnly();
            }
        });
    }

    @Test
    public void testPlanifyRollbackSchedulesNoJobAndReleasesKey() throws Exception {
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                planify();
                status.setRollbackOnly();
            }
        });
        assertFalse(scheduler().checkExists(JOB_KEY));

        planifyInCommittedTransaction();
        assertTrue(scheduler().checkExists(JOB_KEY));
    }

    @Test
    public void testPlanifyCommitSchedulesJob() throws Exception {
        planifyInCommittedTransaction();
        assertTrue(scheduler().checkExists(JOB_KEY));
    }

    private void planifyInCommittedTransaction() {
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                planify();
            }
        });
    }

    private void planify() {
        try {
            taskService.planifyUpdateResourceLastUpdate(getServiceContextAdministrador(), RESOURCE_URN, System.currentTimeMillis(), false);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private int countTasks() {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Task.class).withProperty(TaskProperties.job()).eq(TASK_NAME).build();
        return taskRepository.findByCondition(conditions, PagingParameter.noLimits()).getValues().size();
    }

    private Scheduler scheduler() throws Exception {
        return SchedulerRepository.getInstance().lookup(TaskServiceImpl.SCHEDULER_INSTANCE_NAME);
    }
}
