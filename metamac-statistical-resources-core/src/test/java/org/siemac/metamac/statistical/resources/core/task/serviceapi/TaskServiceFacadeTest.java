package org.siemac.metamac.statistical.resources.core.task.serviceapi;

import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskServiceFacadeTest extends StatisticalResourcesBaseTest implements TaskServiceFacadeTestBase {

    @Autowired
    protected TaskServiceFacade taskServiceFacade;

    @Override
    public void testExecuteImportationTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testMarkTaskAsFailed() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testMarkAllInProgressTaskToFailed() throws Exception {
        // Already checked without test
    }

    @Override
    public void testExecuteRecoveryImportationTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testExecuteDuplicationTask() throws Exception {
        // See integration test in DataManipulateTest
    }

    @Override
    public void testExecuteUpdateGeocoverageCacheTask() throws Exception {
        // No test
    }
    
    @Override
    public void testExecuteUpdateExternalGeocoverageCacheTask() throws Exception {
        // No test
    } 

    @Override
    public void testMarkTaskAsFinished() throws Exception {
        // No test
    }

    @Override
    public void testExecuteDatabaseImportationTask() throws Exception {
        // No test
    }

    @Override
    public void testExecuteDatabaseDatasetPollingTask() throws Exception {
        // No test
    }

    @Override
    public void testScheduleDatabaseDatasetPollingJob() throws Exception {
        // No test
    }

    @Override
    public void testExecuteGeographicCoverageCacheClearTask() throws Exception {
        // No test
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
        // TODO Auto-generated method stub
        
    }
}
