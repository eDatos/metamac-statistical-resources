package org.siemac.metamac.statistical.resources.core.task.serviceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.matchers.GroupMatcher;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.TaskStatusTypeEnum;
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
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallback;
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

    @Before
    public void pauseTriggers() throws Exception {
        // New triggers of a paused group are stored paused, so UpdateResourceLastUpdateJob never runs against the fake resource
        scheduler().pauseTriggers(GroupMatcher.triggerGroupEquals(Scheduler.DEFAULT_GROUP));
    }

    @After
    public void cleanUp() throws Exception {
        scheduler().deleteJob(JOB_KEY);
        scheduler().resumeTriggers(GroupMatcher.triggerGroupEquals(Scheduler.DEFAULT_GROUP));
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

    @Test
    public void testPlanifyReusesLeftoverTaskRow() throws Exception {
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Task leftover = new Task(TASK_NAME);
                leftover.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
                leftover.setExtensionPoint(RESOURCE_URN);
                taskRepository.save(leftover);
            }
        });

        planifyInCommittedTransaction();

        assertTrue(scheduler().checkExists(JOB_KEY));
        assertEquals(1, countTasksInTransaction());
    }

    @Test
    public void testPlanifyTaskCreationErrorSchedulesNoJobAndReleasesKey() throws Exception {
        // Exceeds the @Length of Task.job, so createTask fails before reaching the database
        final String tooLongUrn = RESOURCE_URN + StringUtils.repeat("X", 4000);
        try {
            new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    assertPlanifyFails(tooLongUrn);
                    // A key left claimed would make this second call skip silently as a duplicate
                    assertPlanifyFails(tooLongUrn);
                }
            });
        } catch (TransactionSystemException e) {
            // The entity manager marks the transaction as rollback-only on the validation error, so the commit fails
        }

        assertFalse(scheduler().checkExists(new JobKey(TaskServiceImpl.PREFIX_JOB_UPDATE_RESOURCE_LAST_UPDATE + tooLongUrn)));
    }

    @Test(timeout = 30000)
    public void testPlanifyWhileOtherTransactionPendingCommitSchedulesOneJob() throws Exception {
        planifyWhileOtherTransactionPending(false);

        assertTrue(scheduler().checkExists(JOB_KEY));
        assertEquals(1, countTasksInTransaction());
    }

    @Test(timeout = 30000)
    public void testPlanifyWhileOtherTransactionPendingRollbackSchedulesNoJob() throws Exception {
        planifyWhileOtherTransactionPending(true);

        assertFalse(scheduler().checkExists(JOB_KEY));
        assertEquals(0, countTasksInTransaction());
    }

    private void planifyWhileOtherTransactionPending(final boolean rollbackFirst) throws Exception {
        final CountDownLatch firstPlanified = new CountDownLatch(1);
        final CountDownLatch releaseFirst = new CountDownLatch(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> first = executor.submit(new Runnable() {

                @Override
                public void run() {
                    new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {

                        @Override
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            planify();
                            firstPlanified.countDown();
                            try {
                                // Bounded wait: without the pending key check the second insert blocks on UQ_TB_TASKS until this ends
                                releaseFirst.await(10, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            if (rollbackFirst) {
                                status.setRollbackOnly();
                            }
                        }
                    });
                }
            });
            assertTrue(firstPlanified.await(10, TimeUnit.SECONDS));

            planifyInCommittedTransaction();
            assertFalse(scheduler().checkExists(JOB_KEY));

            releaseFirst.countDown();
            first.get(10, TimeUnit.SECONDS);
        } finally {
            executor.shutdownNow();
        }
    }

    private void assertPlanifyFails(String resourceUrn) {
        try {
            taskService.planifyUpdateResourceLastUpdate(getServiceContextAdministrador(), resourceUrn, System.currentTimeMillis(), false);
            fail("Planification should fail when the task row cannot be created");
        } catch (MetamacException e) {
            // Expected
        }
    }

    private int countTasksInTransaction() {
        return new TransactionTemplate(txManager).execute(new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(TransactionStatus status) {
                return countTasks();
            }
        });
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
