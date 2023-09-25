package org.siemac.metamac.statistical.resources.core.query.repositoryimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_16_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_ONE_QUERY_DRAFT_LINKED_TO_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_24_SIMPLE_WITH_TWO_VERSIONS_WITH_QUERY_LINKED_TO_DATASET_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_06_FOR_QUERIES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_01_SIMPLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_11_SINGLE_VERSION_NOT_VISIBLE_USED_IN_PUBLICATIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_12_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_WITH_SINGLE_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_01_WITH_SELECTION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_02_BASIC_ORDERED_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_03_BASIC_ORDERED_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_08_BASIC_DISCONTINUED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_11_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_21_FOR_QUERY_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME;

import java.util.Arrays;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts;
import org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateSystemException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class QueryVersionRepositoryTest extends StatisticalResourcesBaseTest implements QueryVersionRepositoryTestBase {

    @Autowired
    protected QueryVersionRepository queryVersionRepository;

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testRetrieveByUrn() throws MetamacException {
        QueryVersion actual = queryVersionRepository.retrieveByUrn(queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME), actual);
    }

    @Test
    public void testRetrieveByUrnNotFound() throws MetamacException {
        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, URN_NOT_EXISTS));

        queryVersionRepository.retrieveByUrn(URN_NOT_EXISTS);
    }

    // It's necessary mark test with rollback = false because the expected error is a database constraint so, if we make don't make commit the error doesn't appear
    @Test
    @Rollback(false)
    @MetamacMock({DATASET_VERSION_06_FOR_QUERIES_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateQueryErrorQuerySelectionMustHaveDimensionUnique() throws Exception {
        thrown.expect(HibernateSystemException.class);

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        QueryVersion queryVersion = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME), true);

        // Set attributes that normally sets THE service and can not be null in database
        queryVersion.setStatus(QueryStatusEnum.ACTIVE);
        queryVersion.setType(QueryTypeEnum.FIXED);
        queryVersion.setQuery(query);

        String queryCode = queryVersion.getLifeCycleStatisticalResource().getCode();
        String queryVersionNumber = StatisticalResourcesMockFactory.INIT_VERSION;
        String queryAgency = queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        queryVersion.getLifeCycleStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(new String[]{queryAgency}, queryCode, queryVersionNumber));

        // QuerySelectionItem 01
        QuerySelectionItem querySelectionItem01 = new QuerySelectionItem();
        querySelectionItem01.setQueryVersion(queryVersion);

        CodeItem codeItem011 = new CodeItem();
        codeItem011.setCode("CODE_01");
        codeItem011.setTitle("Code 01");
        codeItem011.setQuerySelectionItem(querySelectionItem01);

        CodeItem codeItem012 = new CodeItem();
        codeItem012.setCode("CODE_02");
        codeItem012.setTitle("Code 02");
        codeItem012.setQuerySelectionItem(querySelectionItem01);

        querySelectionItem01.setDimension("DIMENSION_01");
        querySelectionItem01.addCode(codeItem011);
        querySelectionItem01.addCode(codeItem012);

        // QuerySelectionItem 02
        QuerySelectionItem querySelectionItem02 = new QuerySelectionItem();
        querySelectionItem02.setQueryVersion(queryVersion);

        CodeItem codeItem021 = new CodeItem();
        codeItem021.setCode("CODE_03");
        codeItem021.setTitle("Code 03");
        codeItem021.setQuerySelectionItem(querySelectionItem02);

        CodeItem codeItem022 = new CodeItem();
        codeItem022.setCode("CODE_04");
        codeItem022.setTitle("Code 04");
        codeItem022.setQuerySelectionItem(querySelectionItem02);

        querySelectionItem02.setDimension("DIMENSION_01");
        querySelectionItem02.addCode(codeItem021);
        querySelectionItem02.addCode(codeItem022);

        // Add selections
        queryVersion.addSelection(querySelectionItem01);
        queryVersion.addSelection(querySelectionItem02);

        // Save
        queryVersionRepository.save(queryVersion);
    }

    // It's necessary mark test with rollback = false because the expected error is a database constraint so, if we make don't make commit the error doesn't appear
    @Test
    @Rollback(false)
    @MetamacMock({DATASET_VERSION_06_FOR_QUERIES_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateQueryErrorQuerySelectionItemMustHaveDimensionCodeUnique() throws Exception {
        thrown.expect(HibernateSystemException.class);

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        QueryVersion queryVersion = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME), true);

        // Set attributes that normally sets de service and can not be null in database
        queryVersion.setStatus(QueryStatusEnum.ACTIVE);
        queryVersion.setType(QueryTypeEnum.FIXED);
        queryVersion.setQuery(query);

        String queryCode = queryVersion.getLifeCycleStatisticalResource().getCode();
        String queryVersionNumber = StatisticalResourcesMockFactory.INIT_VERSION;
        String queryAgency = queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested();
        queryVersion.getLifeCycleStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(new String[]{queryAgency}, queryCode, queryVersionNumber));

        // QuerySelectionItem 01
        QuerySelectionItem querySelectionItem01 = new QuerySelectionItem();
        querySelectionItem01.setQueryVersion(queryVersion);

        CodeItem codeItem011 = new CodeItem();
        codeItem011.setCode("CODE_01");
        codeItem011.setTitle("Code 01");
        codeItem011.setQuerySelectionItem(querySelectionItem01);

        CodeItem codeItem012 = new CodeItem();
        codeItem012.setCode("CODE_01");
        codeItem012.setTitle("Code 01 B");
        codeItem012.setQuerySelectionItem(querySelectionItem01);

        querySelectionItem01.setDimension("DIMESNION_01");
        querySelectionItem01.addCode(codeItem011);
        querySelectionItem01.addCode(codeItem012);

        // QuerySelectionItem 02
        QuerySelectionItem querySelectionItem02 = new QuerySelectionItem();
        querySelectionItem02.setQueryVersion(queryVersion);

        CodeItem codeItem021 = new CodeItem();
        codeItem021.setCode("CODE_01");
        codeItem021.setTitle("Code 01");
        codeItem021.setQuerySelectionItem(querySelectionItem02);

        CodeItem codeItem022 = new CodeItem();
        codeItem022.setCode("CODE_02");
        codeItem022.setTitle("Code 02");
        codeItem022.setQuerySelectionItem(querySelectionItem02);

        querySelectionItem02.setDimension("DIMESNION_02");
        querySelectionItem02.addCode(codeItem021);
        querySelectionItem02.addCode(codeItem022);

        // Add selections
        queryVersion.addSelection(querySelectionItem01);
        queryVersion.addSelection(querySelectionItem02);

        // Save
        queryVersionRepository.save(queryVersion);
    }

    @Override
    @Test
    @MetamacMock({QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLastVersion() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME);
        QueryVersion actual = queryVersionRepository.retrieveLastVersion(queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME, QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME})
    public void testRetrieveLastVersionWithAllVersionsPublished() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05_NAME);
        QueryVersion actual = queryVersionRepository.retrieveLastVersion(queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_02_BASIC_WITH_GENERATED_VERSION_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME, QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME,
            QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME})
    public void testRetrieveLastVersionWithLatestVersionNoVisible() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME);
        QueryVersion actual = queryVersionRepository.retrieveLastVersion(queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLastPublishedVersion() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_21_FOR_QUERY_03_NAME);
        QueryVersion actual = queryVersionRepository.retrieveLastPublishedVersion(queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLastPublishedVersionQueryForExternalAPI() throws Exception {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_21_FOR_QUERY_03_NAME);

        // @formatter:off
        DateTime now = new DateTime();
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class)
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
            .and()
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().validFrom()).lessThanOrEqual(now)
            .and()
                .lbrace()
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().validTo()).greaterThan(now)
                .or()
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().validTo()).isNull()
                .rbrace()
            .distinctRoot().build();
        // @formatter:on

        List<QueryVersion> result = queryVersionRepository.findByCondition(conditions);
        assertTrue(result.size() == 1);
        assertEquals(result.iterator().next().getLifeCycleStatisticalResource().getUrn(), expected.getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveRelatedUrnQueryForExternalAPI() throws Exception {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_21_FOR_QUERY_03_NAME);

        // @formatter:off
        String expectedDatasetVersionUrn = expected.getDataset().getVersions().get(0).getSiemacMetadataStatisticalResource().getUrn();

        DateTime now = new DateTime();
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class)
          .lbrace()
              .withProperty(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().urn()).eq(expectedDatasetVersionUrn)
              .or()
              .withProperty(QueryVersionProperties.fixedDatasetVersion().siemacMetadataStatisticalResource().urn()).eq(expectedDatasetVersionUrn)
          .rbrace()
          .and()
          .lbrace()
              .lbrace()
                  .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().validTo()).greaterThan(now)
                  .or()
                  .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().validTo()).isNull()
              .rbrace()
              .and()
              .lbrace()
                  .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().validFrom()).lessThanOrEqual(now)
                  .and()
                  .withProperty(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
              .rbrace()
          .rbrace()
          .distinctRoot().build();
        // @formatter:on

        List<QueryVersion> result = queryVersionRepository.findByCondition(conditions);
        assertTrue(result.size() == 1);
        assertEquals(result.iterator().next().getLifeCycleStatisticalResource().getUrn(), expected.getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock({QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLastPublishedVersionWithAllVersionsPublished() throws Exception {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_26_V3_PUBLISHED_FOR_QUERY_05_NAME);
        QueryVersion actual = queryVersionRepository.retrieveLastPublishedVersion(queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_05_WITH_MULTIPLE_PUBLISHED_VERSIONS_NAME, QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME, QUERY_VERSION_11_DRAFT_NAME})
    public void testRetrieveLastPublishedVersionWithoutVersionsPublished() throws Exception {
        String queryUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME).getQuery().getIdentifiableStatisticalResource().getUrn();
        QueryVersion expected = null;
        QueryVersion actual = queryVersionRepository.retrieveLastPublishedVersion(queryUrn);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @Override
    @MetamacMock({QUERY_VERSION_08_BASIC_DISCONTINUED_NAME, DATASET_VERSION_03_FOR_DATASET_03_NAME, QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME})
    public void testFindLinkedToFixedDatasetVersion() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME);

        QueryVersion query01 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_08_BASIC_DISCONTINUED_NAME);

        List<QueryVersion> queryVersions = queryVersionRepository.findLinkedToFixedDatasetVersion(datasetVersion.getId());

        QueryAsserts.assertEqualsQueryVersionCollection(Arrays.asList(query01), queryVersions);
    }

    @Test
    @Override
    @MetamacMock({DATASET_24_SIMPLE_WITH_TWO_VERSIONS_WITH_QUERY_LINKED_TO_DATASET_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME, DATASET_VERSION_03_FOR_DATASET_03_NAME,
            QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME})
    public void testFindLinkedToDataset() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_24_SIMPLE_WITH_TWO_VERSIONS_WITH_QUERY_LINKED_TO_DATASET_NAME);

        QueryVersion query01 = queryVersionMockFactory.retrieveMock(QueryVersionMockFactory.QUERY_VERSION_36_LINKED_TO_DATASET_NAME);

        List<QueryVersion> queryVersions = queryVersionRepository.findLinkedToDataset(dataset.getId());

        QueryAsserts.assertEqualsQueryVersionCollection(Arrays.asList(query01), queryVersions);
    }

    @Test
    @Override
    @MetamacMock({DATASET_16_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_ONE_QUERY_DRAFT_LINKED_TO_VERSION_NAME})
    public void testFindQueriesPublishedLinkedToDataset() throws Exception {
        Dataset dataset = datasetMockFactory
                .retrieveMock(DATASET_16_WITH_PUBLISHED_AND_DRAFT_VERSIONS_WITH_THREE_QUERIES_DRAFT_NOT_VISIBLE_AND_PUBLISHED_LINKED_TO_DATASET_AND_ONE_QUERY_DRAFT_LINKED_TO_VERSION_NAME);

        List<QueryVersion> queryVersions = queryVersionRepository.findQueriesPublishedLinkedToDataset(dataset.getId());

        Assert.assertTrue(queryVersions.size() == 2);
    }

    @Test
    @Override
    @MetamacMock(QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME)
    // Query DRAFT only version
    public void testRetrieveIsPartOf() throws Exception {
        Query query = this.queryMockFactory.retrieveMock(QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME);

        PublicationVersion pubVersionDraftMultiVersion = this.publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME);
        PublicationVersion pubVersionDraftSingleVersion = this.publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME);

        QueryVersion draftQuery = query.getVersions().get(0);

        List<RelatedResourceResult> pubs = this.queryVersionRepository.retrieveIsPartOf(draftQuery);
        Assert.assertEquals(2, pubs.size());
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(Arrays.asList(pubVersionDraftMultiVersion, pubVersionDraftSingleVersion), pubs);
    }

    @Test
    @MetamacMock(QUERY_11_SINGLE_VERSION_NOT_VISIBLE_USED_IN_PUBLICATIONS_NAME)
    public void testRetrieveIsPartOfSingleVersionQueryPublishedNotVisible() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_11_SINGLE_VERSION_NOT_VISIBLE_USED_IN_PUBLICATIONS_NAME);

        PublicationVersion pubVersionDraftMultiVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_63_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_11_NAME);
        PublicationVersion pubVersionDraftSingleVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_64_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME);
        PublicationVersion pubVersionNotVisibleSingleVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_65_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION__LINKED_TO_QUERY_11_NAME);
        PublicationVersion pubVersionNotVisibleMultiVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_66_PUBLISHED_MULTI_VERSION_V01__LINKED_TO_QUERY_11_NAME);

        QueryVersion draftQuery = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersionDraftMultiVersion, pubVersionDraftSingleVersion, pubVersionNotVisibleSingleVersion, pubVersionNotVisibleMultiVersion);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOf(draftQuery);
        Assert.assertEquals(4, ispartOf.size());
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Test
    @MetamacMock(QUERY_12_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_WITH_SINGLE_VERSIONS_NAME)
    public void testRetrieveIsPartOfSingleVersionQueryPublishedLinkedInSingleVersionPublications() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_12_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_WITH_SINGLE_VERSIONS_NAME);

        PublicationVersion pubVersionDraft = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_68_DRAFT_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME);
        PublicationVersion pubVersionNotVisible = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_69_PUBLISHED_NOT_VISIBLE_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME);
        PublicationVersion pubVersionPublished = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_70_PUBLISHED_SINGLE_VERSION_LINKED_TO_QUERY_12_NAME);

        QueryVersion publishedQuery = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersionDraft, pubVersionNotVisible, pubVersionPublished);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOf(publishedQuery);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Test
    @MetamacMock(QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS_NAME)
    public void testRetrieveIsPartOfSingleVersionQueryPublishedLinkedInPublicationsToLastVersions() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS_NAME);

        PublicationVersion pubVersionDraft = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_71_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_DRAFT_LINKED_TO_QUERY_13_NAME);
        PublicationVersion pubVersionNotVisible = publicationVersionMockFactory
                .retrieveMock(PUBLICATION_VERSION_75_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_LAST_LINKED_TO_QUERY_13_NAME);
        PublicationVersion pubVersionPublished = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME);

        QueryVersion draftQuery = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersionDraft, pubVersionNotVisible, pubVersionPublished);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOf(draftQuery);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Test
    @MetamacMock(QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS_NAME)
    public void testRetrieveIsPartOfSingleVersionQueryPublishedLinkedInPublicationsToPreviousVersions() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS_NAME);

        PublicationVersion pubVersion01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME);
        PublicationVersion pubVersion02 = publicationVersionMockFactory
                .retrieveMock(PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME);

        QueryVersion queryVersion = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersion01, pubVersion02);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOf(queryVersion);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Test
    @MetamacMock(QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS_NAME)
    public void testRetrieveIsPartOfSingleVersionQueryPublishedLinkedInPublicationsToBoth() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS_NAME);

        PublicationVersion pubVersion01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME);
        PublicationVersion pubVersion02 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_74_DRAFT_V02_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME);
        PublicationVersion pubVersion03 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME);
        PublicationVersion pubVersion04 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_78_NOT_VISIBLE_V02_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME);
        PublicationVersion pubVersion06 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME);

        QueryVersion queryVersion = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersion01, pubVersion02, pubVersion03, pubVersion04, pubVersion06);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOf(queryVersion);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Override
    @Test
    // Query published
    @MetamacMock(QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS_NAME)
    public void testRetrieveIsPartOfOnlyLastPublished() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_15_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_BOTH_VERSIONS_NAME);

        PublicationVersion pubVersion01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_73_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__BOTH_LINKED_TO_QUERY_15_NAME);
        PublicationVersion pubVersion02 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_77_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__BOTH_LINKED_TO_QUERY_15_NAME);
        PublicationVersion pubVersion03 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_82_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__BOTH_LINKED_TO_QUERY_15_NAME);

        QueryVersion queryVersion = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersion01, pubVersion02, pubVersion03);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOfOnlyLastPublished(queryVersion);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Test
    @MetamacMock(QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS_NAME)
    public void testRetrieveIsPartOfOnlyLastPublishedQueryLinkedToLatestVersions() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_13_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_LAST_VERSIONS_NAME);

        PublicationVersion pubVersion01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_79_LAST_VERSION_V02_IN_PUB_WITH_TWO_PUBLISHED__ONLY_LAST_LINKED_TO_QUERY_13_NAME);

        QueryVersion queryVersion = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersion01);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOfOnlyLastPublished(queryVersion);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

    @Test
    @MetamacMock(QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS_NAME)
    public void testRetrieveIsPartOfOnlyLastPublishedQueryLinkedToPreviousVersions() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_14_SINGLE_VERSION_PUBLISHED_USED_IN_PUBLICATIONS_LINK_ONLY_PREVIOUS_VERSIONS_NAME);

        PublicationVersion pubVersion01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_72_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_DRAFT__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME);
        PublicationVersion pubVersion02 = publicationVersionMockFactory
                .retrieveMock(PUBLICATION_VERSION_76_PUBLISHED_V01_IN_PUB_WITH_PUBLISHED_AND_NOT_VISIBLE__ONLY_PUBLISHED_LINKED_TO_QUERY_14_NAME);

        QueryVersion queryVersion = query.getVersions().get(0);

        List<PublicationVersion> expectedIsPartOf = Arrays.asList(pubVersion01, pubVersion02);

        List<RelatedResourceResult> ispartOf = queryVersionRepository.retrieveIsPartOfOnlyLastPublished(queryVersion);
        CommonAsserts.assertEqualsRelatedResourceResultCollectionToPublicationVersionCollection(expectedIsPartOf, ispartOf);
    }

}
