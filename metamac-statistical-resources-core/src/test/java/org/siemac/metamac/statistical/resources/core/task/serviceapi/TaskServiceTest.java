package org.siemac.metamac.statistical.resources.core.task.serviceapi;

import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskServiceTest extends StatisticalResourcesBaseTest implements TaskServiceTestBase {

    @Autowired
    protected TaskService taskService;

    @Override
    public void testPlanifyImportationDataset() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testProcessImportationTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testMarkTaskAsFinished() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testMarkTaskAsFailed() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testCreateTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testUpdateTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testRetrieveTaskByJob() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testFindTasksByCondition() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testPlanifyRecoveryImportDataset() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testProcessRollbackImportationTask() throws Exception {
        // Already checked without test
    }

    @Override
    public void testExistsTaskForResource() throws Exception {
        // See integration test in DataManipulateTest

    }

    @Override
    public void testExistImportationTaskInResource() throws Exception {
        // See integration test in DataManipulateTest

    }

    @Override
    public void testExistRecoveryImportationTaskInResource() throws Exception {
        // See integration test in DataManipulateTest

    }

    @Override
    public void testPlanifyDuplicationDataset() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testPlanifyUpdateGeocoverageCache() throws Exception {
        // No test
    }

    @Override
    public void testPlanifyUpdateExternalGeocoverageCache() throws Exception {
        // No test
    }

    @Override
    public void testProcessDuplicationTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testProcessUpdateGeocoverageCacheTask() throws Exception {
        // No test
    }

    @Override
    public void testProcessUpdateExternalGeocoverageCacheTask() throws Exception {
        // No test
    }

    @Override
    public void testExistDuplicationTaskInResource() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testExistUpdateGeocoverageCacheTaskInResource() throws Exception {
        // No test
    }

    @Override
    public void testExistUpdateExternalGeocoverageCacheTaskInResource() throws Exception {
        // No test
    }

    @Override
    public void testMarkTasksAsFailedOnApplicationStartup() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testExistDatabaseImportationTaskInResource() throws Exception {
        // No test
    }

    @Override
    public void testProcessDatabaseImportationTask() throws Exception {
        // No test
    }

    @Override
    public void testProcessDatabaseDatasetPollingTask() throws Exception {
        // No test
    }

    @Override
    public void testScheduleDatabaseDatasetPollingJob() throws Exception {
        // No test
    }

    @Override
    public void testProcessGeographicCoverageCacheClearTask() throws Exception {
        // Already checked without test
    }

    @Override
    public void testScheduleGeographicCoverageCacheClearJob() throws Exception {
        // No test
    }

    @Override
    public void testSendDatabaseImportationErrorNotification() throws Exception {
        // No test
    }

    @Override
    public void testImportAttributesInDatasetVersion() throws Exception {
        // No test

    }

    @Override
    public void testPlanifyImportationAttributes() throws Exception {
        // No test
    }

    @Override
    public void testPlanifyRecoveryImportAttributes() throws Exception {
        // No test
    }

    @Override
    public void testProcessRollbackImportationAttributesTask() {

    }

    @Override
    public void testExistsTaskImportAttributes() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testProcessResendKafkaDatasetMessageTask() throws Exception {
        // Already checked without test
    }

    @Override
    public void testScheduleResendKafkaDatasetMessageJob() throws Exception {
    }

    @Override
    public void testPlanifyUpdateGeographicalCacheRelatedResource() throws Exception {
        // No test
    }

    @Override
    public void testProcessUpdateGeographicalCacheRelatedResourceTask() throws Exception {
        // No test
    }

    @Override
    public void testExistUpdateGeoCacheRelatedResourcesTaskInResource() throws Exception {
        // No test
    }

    @Override
    public void testExistsGeoCacheTasksByTaskName() throws Exception {
        // No test
    }

    @Override
    public void testProcessRetryGeographicalCacheTask() throws Exception {
        // No test
    }

    @Override
    public void testProcessUpdateResourceLastUpdateTask() throws Exception {
        // No test
    }

    @Override
    public void testPlanifyUpdateResourceLastUpdate() throws Exception {
        // No test
    }
}
