package org.siemac.metamac.statistical.resources.core.cache.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_05_BASIC_NAME;

import org.apache.avro.specific.SpecificRecordBase;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/stream-messaging-service-mockito.xml",
        "classpath:spring/statistical-resources/include/dataset-repository-mockito.xml", "classpath:spring/statistical-resources/include/rest-services-mockito.xml",
        "classpath:spring/statistical-resources/include/external-item-checker-mockito.xml", "classpath:spring/statistical-resources/include/task-mockito.xml",
        "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class ResourceCacheInvalidationServiceTest extends StatisticalResourcesBaseTest {

    private static final long TIMESTAMP = new DateTime(2020, 1, 15, 10, 30, 0, 0).getMillis();

    @Autowired
    private ResourceCacheInvalidationService resourceCacheInvalidationService;

    @Autowired
    private StreamMessagingService<String, SpecificRecordBase> messagingService;

    @Before
    public void onBeforeTest() throws Exception {
        messagingService = (StreamMessagingService<String, SpecificRecordBase>) (((org.springframework.aop.framework.Advised) messagingService).getTargetSource().getTarget());
        Mockito.reset(messagingService);
    }

    @Test
    @MetamacMock(DATASET_VERSION_01_BASIC_NAME)
    public void testUpdateResourceLastUpdate() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        LifeCycleStatisticalResource resource = datasetVersion.getLifeCycleStatisticalResource();

        resource.setPatch(3);
        markAsLastPublishedVersion(resource);

        resourceCacheInvalidationService.updateResourceLastUpdate(getServiceContextWithoutPrincipal(), resource, TIMESTAMP);

        assertResourceLastUpdateIsUpdated(resource);
        Mockito.verify(messagingService).sendMessage(resource);
    }

    @Test
    @MetamacMock(QUERY_VERSION_05_BASIC_NAME)
    public void testUpdateQueryVersionLastUpdate() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_05_BASIC_NAME);
        LifeCycleStatisticalResource resource = queryVersion.getLifeCycleStatisticalResource();

        resource.setPatch(3);
        markAsLastPublishedVersion(resource);

        resourceCacheInvalidationService.updateResourceLastUpdate(getServiceContextWithoutPrincipal(), resource, TIMESTAMP);

        assertResourceLastUpdateIsUpdated(resource);
        Mockito.verify(messagingService).sendMessage(resource);
    }

    @Test
    @MetamacMock(DATASET_VERSION_01_BASIC_NAME)
    public void testUpdateResourceLastUpdateDoesNotSendMessageWhenVersionIsSuperseded() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        LifeCycleStatisticalResource resource = datasetVersion.getLifeCycleStatisticalResource();

        resource.setPatch(3);
        markAsSupersededPublishedVersion(resource);

        resourceCacheInvalidationService.updateResourceLastUpdate(getServiceContextWithoutPrincipal(), resource, TIMESTAMP);

        assertResourceLastUpdateIsUpdated(resource);
        Mockito.verify(messagingService, Mockito.never()).sendMessage(resource);
    }

    @Test
    @MetamacMock(QUERY_VERSION_05_BASIC_NAME)
    public void testUpdateQueryVersionLastUpdateDoesNotSendMessageWhenVersionIsSuperseded() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_05_BASIC_NAME);
        LifeCycleStatisticalResource resource = queryVersion.getLifeCycleStatisticalResource();

        resource.setPatch(3);
        markAsSupersededPublishedVersion(resource);

        resourceCacheInvalidationService.updateResourceLastUpdate(getServiceContextWithoutPrincipal(), resource, TIMESTAMP);

        assertResourceLastUpdateIsUpdated(resource);
        Mockito.verify(messagingService, Mockito.never()).sendMessage(resource);
    }

    @Test
    @MetamacMock(DATASET_VERSION_01_BASIC_NAME)
    public void testUpdateResourceLastUpdateDoesNotSendMessageWhenVersionIsNotPublished() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        LifeCycleStatisticalResource resource = datasetVersion.getLifeCycleStatisticalResource();

        resource.setPatch(3);
        resource.setProcStatus(ProcStatusEnum.DRAFT);
        resource.setValidTo(null);

        resourceCacheInvalidationService.updateResourceLastUpdate(getServiceContextWithoutPrincipal(), resource, TIMESTAMP);

        assertResourceLastUpdateIsUpdated(resource);
        Mockito.verify(messagingService, Mockito.never()).sendMessage(resource);
    }

    private void markAsLastPublishedVersion(LifeCycleStatisticalResource resource) {
        resource.setProcStatus(ProcStatusEnum.PUBLISHED);
        resource.setValidTo(null);
    }

    private void markAsSupersededPublishedVersion(LifeCycleStatisticalResource resource) {
        resource.setProcStatus(ProcStatusEnum.PUBLISHED);
        resource.setValidTo(new DateTime(2019, 6, 1, 0, 0, 0, 0));
    }

    private void assertResourceLastUpdateIsUpdated(LifeCycleStatisticalResource resource) {
        assertEquals(new DateTime(TIMESTAMP), resource.getLastUpdated());
        assertEquals("system", resource.getLastUpdatedBy());
        assertEquals(Integer.valueOf(4), resource.getPatch());
    }
}
