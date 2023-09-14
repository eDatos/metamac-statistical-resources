package org.siemac.metamac.statistical.resources.core.multidataset.repositoryimpl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_85_LAST_VERSION_NOT_PUBLISHED__IS_PART_OF_PUBLICATIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_02_C1_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_03_BASIC_WITH_2_C1_MULTIDATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_05_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_12_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class MultidatasetVersionRepositoryTest extends StatisticalResourcesBaseTest implements MultidatasetVersionRepositoryTestBase {

    @Autowired
    protected MultidatasetVersionRepository multidatasetVersionRepository;

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testRetrieveByUrn() throws Exception {
        MultidatasetVersion actual = multidatasetVersionRepository
                .retrieveByUrn(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME), actual);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME})
    public void testRetrieveByUrnPublished() throws Exception {
        MultidatasetVersion actual = multidatasetVersionRepository
                .retrieveByUrnPublished(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME).getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_30_V1_PUBLISHED_FOR_MULTIDATASET_06_NAME), actual);
    }

    @Test
    public void testRetrieveByUrnNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, URN_NOT_EXISTS));
        multidatasetVersionRepository.retrieveByUrn(URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLastVersion() throws Exception {
        MultidatasetVersion actual = multidatasetVersionRepository
                .retrieveLastVersion(multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn());
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME})
    public void testRetrieveLastVersionWithAllVersionsPublished() throws Exception {
        String multidatasetUrn = multidatasetMockFactory.retrieveMock(MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME);
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveLastVersion(multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_02_C1_BASIC_WITH_GENERATED_VERSION_NAME, MULTIDATASET_03_BASIC_WITH_2_C1_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_05_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME,
            MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME})
    public void testRetrieveLastVersionWithLatestVersionNoVisible() throws Exception {
        String multidatasetUrn = multidatasetMockFactory.retrieveMock(MULTIDATASET_06_C1_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_C1_NAME);
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveLastVersion(multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLastPublishedVersion() throws Exception {
        String multidatasetUrn = multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME);
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveLastPublishedVersion(multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLastPublishedVersionWithAllVersionsPublished() throws Exception {
        String multidatasetUrn = multidatasetMockFactory.retrieveMock(MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_29_V3_PUBLISHED_FOR_MULTIDATASET_05_NAME);
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveLastPublishedVersion(multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_VERSION_12_DRAFT_NAME})
    public void testRetrieveLastPublishedVersionWithoutVersionsPublished() throws Exception {
        String multidatasetUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_12_DRAFT_NAME).getMultidataset().getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = null;
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveLastPublishedVersion(multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_VERSION_12_DRAFT_NAME,
            MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME})
    public void testRetrieveLastPublishedVersionWithLatestVersionPublishedDateAfterNow() throws Exception {
        Multidataset multidataset = multidatasetMockFactory.retrieveMock(MULTIDATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        String multidatasetUrn = multidataset.getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion expected = multidataset.getVersions().get(multidataset.getVersions().size() - 1);
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveLastPublishedVersion(multidatasetUrn);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME, MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveByVersion() throws Exception {
        MultidatasetVersion actual = multidatasetVersionRepository.retrieveByVersion(multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getId(),
                multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME})
    public void testGetIsReplacedBy() throws Exception {
        MultidatasetVersion notVisibleMultidataset = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME);
        MultidatasetVersion publishedMultidataset = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME);

        RelatedResource resource = publishedMultidataset.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        assertNotNull(resource);
        assertEqualsMultidatasetVersion(notVisibleMultidataset, resource.getMultidatasetVersion());
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME})
    public void testRetrieveIsReplacedByOnlyLastPublished() throws Exception {
        MultidatasetVersion publishedMultidataset = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME);

        RelatedResource resource = publishedMultidataset.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        assertNotNull(resource);
        assertThat(resource.getMultidatasetVersion().getLifeCycleStatisticalResource().getProcStatus(), is(not(equalTo(ProcStatusEnum.PUBLISHED))));
    }

    @Override
    public void testRetrieveIsReplacedBy() throws Exception {

    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME})
    public void testRetrieveIsReplacedByOnlyIfPublished() throws Exception {
        MultidatasetVersion publishedMultidataset = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME);
        RelatedResource resource = publishedMultidataset.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        assertNotNull(resource);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME, PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME})
    public void testRetrieveIsPartOf() throws Exception {
        MultidatasetVersion datasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME);
        PublicationVersion publicationDraftFirstLevel = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME);
        PublicationVersion publicationDraftNoFirstLevel = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME);
        PublicationVersion publicationDraftMultiCube = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME);
        List<RelatedResourceResult> resources = multidatasetVersionRepository.retrieveIsPartOf(datasetVersion);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(Arrays.asList(publicationDraftFirstLevel, publicationDraftNoFirstLevel, publicationDraftMultiCube),
                resources);
    }
}
