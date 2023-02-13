package org.siemac.metamac.statistical.resources.core.publication.repositoryimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertRelaxedEqualsCube;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CubeMockFactory.CUBE_03_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_22_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_23_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_09_C1_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.CubeRepository;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class CubeRepositoryTest extends StatisticalResourcesBaseTest implements CubeRepositoryTestBase {

    @Autowired
    private CubeRepository cubeRepository;

    @Override
    @Test
    @MetamacMock({CUBE_01_BASIC_NAME, CUBE_02_BASIC_NAME, CUBE_03_BASIC_NAME})
    public void testRetrieveCubeByUrn() throws Exception {
        Cube expected = cubeMockFactory.retrieveMock(CUBE_01_BASIC_NAME);
        Cube actual = cubeRepository.retrieveCubeByUrn(expected.getNameableStatisticalResource().getUrn());
        assertRelaxedEqualsCube(expected, actual);
    }

    @Test
    @MetamacMock({CUBE_01_BASIC_NAME, CUBE_02_BASIC_NAME, CUBE_03_BASIC_NAME})
    public void testRetrieveChapterByUrnErrorNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CUBE_NOT_FOUND, URN_NOT_EXISTS));
        cubeRepository.retrieveCubeByUrn(URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testExistAnyCubeInPublication() throws Exception {
        boolean result = cubeRepository.existAnyCubeInPublication(
                publicationMockFactory.retrieveMock(PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME).getIdentifiableStatisticalResource().getCode(),
                StatisticalResourcesMockFactory.INIT_VERSION);
        assertTrue(result);
    }

    @Test
    @MetamacMock({PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testExistAnyCubeInPublicationReturnFalse() throws Exception {
        boolean result = cubeRepository.existAnyCubeInPublication(
                publicationMockFactory.retrieveMock(PUBLICATION_04_STRUCTURED_WITH_2_PUBLICATION_VERSIONS_NAME).getIdentifiableStatisticalResource().getCode(),
                StatisticalResourcesMockFactory.SECOND_VERSION);
        assertFalse(result);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME})
    public void testFindDatasetsLinkedWithPublicationVersion() throws Exception {
        List<String> result = cubeRepository.findDatasetsLinkedWithPublicationVersion(
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(2, result.size());
        assertTrue(result.contains(datasetMockFactory.retrieveMock(DATASET_23_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME).getIdentifiableStatisticalResource().getUrn()));
        assertTrue(result.contains(datasetMockFactory.retrieveMock(DATASET_22_C1_SIMPLE_LINKED_TO_PUB_VERSION_17_NAME).getIdentifiableStatisticalResource().getUrn()));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testFindDatasetsLinkedWithPublicationVersionWithoutDatasets() throws Exception {
        List<String> result = cubeRepository.findDatasetsLinkedWithPublicationVersion(
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(0, result.size());
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME})
    public void testFindQueriesLinkedWithPublicationVersion() throws Exception {
        List<String> result = cubeRepository.findQueriesLinkedWithPublicationVersion(
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(1, result.size());
        assertTrue(result.contains(queryMockFactory.retrieveMock(QUERY_09_C1_SINGLE_VERSION_USED_IN_PUB_VERSION_17_NAME).getIdentifiableStatisticalResource().getUrn()));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testFindQueriesLinkedWithPublicationVersionWithoutQueries() throws Exception {
        List<String> result = cubeRepository.findQueriesLinkedWithPublicationVersion(
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_C1_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(0, result.size());
    }

}
