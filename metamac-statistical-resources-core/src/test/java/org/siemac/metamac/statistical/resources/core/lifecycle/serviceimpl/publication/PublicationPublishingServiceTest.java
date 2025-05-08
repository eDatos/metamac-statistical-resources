package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.publication;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.LifecycleAsserts.assertNotNullAutomaticallyFilledMetadataSiemacSendToPublished;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.ChapterMockFactory.CHAPTER_05_EMPTY_IN_PUBLICATION_VERSION_89_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_08_EMPTY_IN_PUBLICATION_VERSION_90_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_25_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_26_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_27_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_28_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_08_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_09_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_10_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_11_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_84_PUBLISHED_FOR_PUBLICATION_07_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_PUBLICATION_07_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_88_WITH_NO_CUBES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_89_WITH_EMPTY_CHAPTER_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_90_WITH_EMPTY_CUBE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_16_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_17_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_18_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_19_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesMockRestBaseTest;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleService;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.TaskMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.asserts.BaseAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/stream-messaging-service-facade-mockito.xml",
        "classpath:spring/statistical-resources/include/dataset-repository-mockito.xml", "classpath:spring/statistical-resources/include/task-mockito.xml",
        "classpath:spring/statistical-resources/include/apis-locator-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class PublicationPublishingServiceTest extends StatisticalResourcesMockRestBaseTest {

    private final PublicationVersionMockFactory  publicationVersionMockFactory = PublicationVersionMockFactory.getInstance();

    @Autowired
    @Qualifier("publicationLifecycleService")
    private LifecycleService<PublicationVersion> publicationVersionLifecycleService;

    @Autowired
    private PublicationVersionRepository         publicationVersionRepository;

    @Autowired
    private TaskService                          taskService;

    @Override
    @Before
    public void setUp() throws MetamacException {
        super.setUp();
        mockAllTaskInProgressForDatasetVersion(false);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME)
    public void testPublishPublicationVersionFirstVersion() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);

        publicationVersion = publicationVersionRepository.retrieveByUrn(publicationVersionUrn);

        assertPublishingPublicationVersion(publicationVersion, null);
    }

    @Test
    @MetamacMock(PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME)
    public void testPublishPublicationVersionWithPreviousVersion() throws Exception {
        PublicationVersion previousVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_84_PUBLISHED_FOR_PUBLICATION_07_NAME);
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_85_PREPARED_TO_PUBLISH_WITH_PREVIOUS_VERSION_EXTERNAL_ITEM_FULL_FOR_PUBLICATION_07_NAME);

        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);

        publicationVersion = publicationVersionRepository.retrieveByUrn(publicationVersionUrn);
        previousVersion = publicationVersionRepository.retrieveByUrn(previousVersion.getSiemacMetadataStatisticalResource().getUrn());

        assertPublishingPublicationVersion(publicationVersion, previousVersion);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME)
    public void testPublishPublicationVersionCheckExternalItemsNotPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_83_PREPARED_TO_PUBLISH_ONLY_VERSION_EXTERNAL_ITEM_FULL_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsNotPublished(siemacResource);
        // No external items in Publication

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        exceptionItems.addAll(getExceptionItemsForExternalItemNotPublishedSiemac(siemacResource, ServiceExceptionParameters.PUBLICATION_VERSION));

        expectedMetamacException(new MetamacException(exceptionItems));

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED_NAME)
    public void testPublishPublicationVersionCheckStructureElementsNotPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_86_WITH_ELEMENT_LEVELS_NOT_PUBLISHED_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        List<String> datasetsNotPublished = getDatasetsMocksUrns(DATASET_25_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME, DATASET_26_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME,
                DATASET_27_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, DATASET_28_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME);

        // Datasets errors
        for (String datasetUrn : datasetsNotPublished) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_DATASET, datasetUrn));
        }

        // Queries
        List<String> queryiesNotPublished = getQueryMocksUrns(QUERY_16_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME, QUERY_17_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME,
                QUERY_18_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, QUERY_19_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME);

        for (String queryUrn : queryiesNotPublished) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_QUERY, queryUrn));
        }

        // Multidataset errors
        List<String> multidatasetsNotPublished = getMultidatasetMocksUrns(MULTIDATASET_08_DRAFT_USED_IN_PUBLICATION_VERSION_86_NAME,
                MULTIDATASET_09_PRODUCTION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME, MULTIDATASET_10_DIFFUSION_VALIDATION_USED_IN_PUBLICATION_VERSION_86_NAME,
                MULTIDATASET_11_VALIDATION_REJECTED_USED_IN_PUBLICATION_VERSION_86_NAME);

        for (String multidatasetUrn : multidatasetsNotPublished) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_LINKED_TO_NOT_PUBLISHED_MULTIDATASET, multidatasetUrn));
        }

        expectedMetamacException(new MetamacException(exceptionItems));

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED_NAME)
    public void testPublishPublicationVersionCheckRelatedResourcesNotPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_87_WITH_RELATED_RESOURCES_NOT_PUBLISHED_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        String prefix = ServiceExceptionParameters.PUBLICATION_VERSION;

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        exceptionItems.add(buildRelatedResourceNotPublishedException(siemacResource.getReplaces(), prefix, "replaces"));

        expectedMetamacException(new MetamacException(exceptionItems));

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_88_WITH_NO_CUBES_NAME)
    public void testPublishPublicationVersionCheckAtLeastOneCube() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_88_WITH_NO_CUBES_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_MUST_HAVE_AT_LEAST_ONE_CUBE));

        expectedMetamacException(new MetamacException(exceptionItems));

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_89_WITH_EMPTY_CHAPTER_NAME)
    public void testPublishPublicationVersionCheckNoEmptyChapters() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_89_WITH_EMPTY_CHAPTER_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        String chapterUrn = getChapterMockUrn(CHAPTER_05_EMPTY_IN_PUBLICATION_VERSION_89_NAME);
        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_CHAPTER_MUST_HAVE_AT_LEAST_ONE_CUBE, chapterUrn));

        expectedMetamacException(new MetamacException(exceptionItems));

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_90_WITH_EMPTY_CUBE_NAME)
    public void testPublishPublicationVersionCheckNoEmptyCubes() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_90_WITH_EMPTY_CUBE_NAME);
        SiemacMetadataStatisticalResource siemacResource = publicationVersion.getSiemacMetadataStatisticalResource();
        String publicationVersionUrn = siemacResource.getUrn();

        mockSiemacExternalItemsPublished(siemacResource);

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        String cubeUrn = getCubeMockUrn(CUBE_08_EMPTY_IN_PUBLICATION_VERSION_90_NAME);
        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_CUBE_MUST_LINK_TO_DATASET_QUERY_MULTIDATASET_PUBLICATION_OR_URL, cubeUrn));

        expectedMetamacException(new MetamacException(exceptionItems));

        publicationVersionLifecycleService.sendToPublished(getServiceContextAdministrador(), publicationVersionUrn);
    }

    private void assertPublishingPublicationVersion(PublicationVersion current, PublicationVersion previous) throws MetamacException {
        assertNotNullAutomaticallyFilledMetadataSiemacSendToPublished(current, previous);

        Integer formatExtentResources = current.getChildrenAllLevels().size();
        assertEquals(formatExtentResources, current.getFormatExtentResources());

        List<RelatedResource> expectedHasPart = buildExpectedHasPart(current);
        BaseAsserts.assertEqualsRelatedResourceCollection(expectedHasPart, current.getHasPart());
    }

    // -------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------

    private void mockAllTaskInProgressForDatasetVersion(boolean status) throws MetamacException {
        TaskMockUtils.mockAllTaskInProgressForDatasetVersion(taskService, status);
    }

    private List<RelatedResource> buildExpectedHasPart(PublicationVersion current) {
        Map<String, Cube> uniqueResourceCubes = new HashMap<String, Cube>();
        for (ElementLevel level : current.getChildrenAllLevels()) {
            if (level.isCube()) {
                Cube cube = level.getCube();
                String urn = null;
                if (cube.getDataset() != null) {
                    urn = cube.getDatasetUrn();
                } else if (cube.getQuery() != null) {
                    urn = cube.getQueryUrn();
                } else if (cube.getMultidataset() != null) {
                    urn = cube.getMultidatasetUrn();
                } else if (cube.getPublication() != null) {
                    urn = cube.getPublicationUrn();
                }
                uniqueResourceCubes.put(urn, cube);
            }
        }

        List<RelatedResource> resources = new ArrayList<RelatedResource>();
        for (String urn : uniqueResourceCubes.keySet()) {
            Cube cube = uniqueResourceCubes.get(urn);
            if (cube.getDataset() != null) {
                resources.add(StatisticalResourcesDoMocks.mockDatasetRelated(cube.getDataset()));
            } else if (cube.getQuery() != null) {
                resources.add(StatisticalResourcesDoMocks.mockQueryRelated(cube.getQuery()));
            } else if (cube.getMultidataset() != null) {
                resources.add(StatisticalResourcesDoMocks.mockMultidatasetRelated(cube.getMultidataset()));
            } else if (cube.getPublication() != null) {
                resources.add(StatisticalResourcesDoMocks.mockPublicationRelated(cube.getPublication()));
            }
        }
        return resources;
    }
}
