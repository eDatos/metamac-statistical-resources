package org.siemac.metamac.statistical.resources.core.query.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsQueryVersionCollection;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_30_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERIES_NOT_VISIBLE_BOTH_NOT_COMPATIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_06_FOR_QUERIES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_86_WITH_TEMPORAL_DIMENSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_87_WITH_NO_TEMPORAL_DIMENSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_99_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_01_SIMPLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_01_WITH_SELECTION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_02_BASIC_ORDERED_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_03_BASIC_ORDERED_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_04_BASIC_ORDERED_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_05_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_06_BASIC_ACTIVE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_08_BASIC_DISCONTINUED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_11_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_14_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_15_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_21_FOR_QUERY_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_27_V1_PUBLISHED_FOR_QUERY_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_29_CHECK_COMPAT_DATASET_86_OK_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_30_CHECK_COMPAT_DATASET_86_LESS_DIMENSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_31_CHECK_COMPAT_DATASET_86_MORE_DIMENSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_32_CHECK_COMPAT_DATASET_86_MORE_CODES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_34_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_AUTOINC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_35_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_LATEST_DATA_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_57_REPLACES_QUERY_58_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.buildSelectionItemWithDimensionAndCodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.constants.ProcStatusForActionsConstants;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResourceRepository;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetService;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryProperties;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItemRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class QueryServiceTest extends StatisticalResourcesBaseTest implements QueryServiceTestBase {

    @Autowired
    private QueryService                  queryService;

    @Autowired
    private DatasetService                datasetService;

    @Autowired
    private StatisticalResourceRepository statisticalResourceRepository;

    @Autowired
    private DatasetVersionRepository      datasetVersionRepository;

    @Autowired
    private QuerySelectionItemRepository  querySelectionItemRepository;

    @Autowired
    private QueryRepository               queryRepository;

    @Override
    @Test
    @MetamacMock(QUERY_01_SIMPLE_NAME)
    public void testFindQueriesByCondition() throws Exception {
        Query result = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);

        // Find by code
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Query.class).withProperty(QueryProperties.identifiableStatisticalResource().code())
                .eq(result.getIdentifiableStatisticalResource().getCode()).build();

        List<Query> queries = queryService.findQueriesByCondition(getServiceContextWithoutPrincipal(), conditions);
        assertEquals(1, queries.size());
        assertEquals(result.getIdentifiableStatisticalResource().getUrn(), queries.get(0).getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testRetrieveQueryVersionByUrn() throws MetamacException {
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(),
                (queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME)).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME), actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionByUrnDraftProcStatus() throws MetamacException {
        QueryVersion expectedQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), (expectedQueryVersion).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(expectedQueryVersion, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionByUrnProductionValidationProcStatus() throws MetamacException {
        QueryVersion expectedQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME);
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), (expectedQueryVersion).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(expectedQueryVersion, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionByUrnDiffusionValidationProcStatus() throws MetamacException {
        QueryVersion expectedQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME);
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), (expectedQueryVersion).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(expectedQueryVersion, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionByUrnValidationRejectedProcStatus() throws MetamacException {
        QueryVersion expectedQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME);
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), (expectedQueryVersion).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(expectedQueryVersion, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionByUrnPublishedNoVisibleProcStatus() throws MetamacException {
        QueryVersion expectedQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME);
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), (expectedQueryVersion).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(expectedQueryVersion, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionByUrnPublishedProcStatus() throws MetamacException {
        QueryVersion expectedQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        QueryVersion actual = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), (expectedQueryVersion).getLifeCycleStatisticalResource().getUrn());
        assertEqualsQueryVersion(expectedQueryVersion, actual);
    }

    @Test
    public void testRetrieveQueryVersionByUrnParameterRequired() throws MetamacException {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.URN));
        queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), EMPTY);
    }

    @Override
    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLatestQueryVersionByQueryUrn() throws Exception {
        String urn = queryMockFactory.retrieveMock(QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion actual = queryService.retrieveLatestQueryVersionByQueryUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsQueryVersion(queryVersionMockFactory.retrieveMock(QUERY_VERSION_22_FOR_QUERY_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLatestQueryVersionByQueryUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.QUERY_URN));
        queryService.retrieveLatestQueryVersionByQueryUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Override
    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLatestPublishedQueryVersionByQueryUrn() throws Exception {
        String urn = queryMockFactory.retrieveMock(QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        QueryVersion actual = queryService.retrieveLatestPublishedQueryVersionByQueryUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsQueryVersion(queryVersionMockFactory.retrieveMock(QUERY_VERSION_21_FOR_QUERY_03_NAME), actual);
    }

    @Test
    @MetamacMock({QUERY_03_BASIC_WITH_2_QUERY_VERSIONS_NAME})
    public void testRetrieveLatestPublishedQueryVersionByQueryUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.QUERY_URN));
        queryService.retrieveLatestPublishedQueryVersionByQueryUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({DATASET_30_LAST_VERSION_NOT_VISIBLE_WITH_PUBLICATION_AND_QUERIES_NOT_VISIBLE_BOTH_NOT_COMPATIBLE_NAME})
    public void testFindQueryVersionsByConditionDatasetURN() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).orderBy(QueryVersionProperties.lifeCycleStatisticalResource().code()).build();
            String datasetVersionFixedUrn = datasetVersionMockFactory.retrieveMock(DatasetVersionMockFactory.DATASET_VERSION_92_NOT_VISIBLE_FOR_DATASET_30_NAME).getLifeCycleStatisticalResource()
                    .getUrn();
            //@formatter:off
            conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class)
                .withProperty(QueryVersionProperties.fixedDatasetVersion().siemacMetadataStatisticalResource().urn()).eq(datasetVersionFixedUrn)
                .or()
                .lbrace()
                    .withProperty(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE)
                    .and()
                    .withProperty(QueryVersionProperties.dataset().versions().siemacMetadataStatisticalResource().urn()).eq(datasetVersionFixedUrn)
                .rbrace()
                .build();

            //@formatter:on
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, queriesPagedResult.getTotalRows());
        }
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME})
    public void testFindQueryVersionsByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).orderBy(QueryVersionProperties.lifeCycleStatisticalResource().code()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(3, queriesPagedResult.getTotalRows());
            int i = 0;
            assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getUrn(),
                    queriesPagedResult.getValues().get(i++).getLifeCycleStatisticalResource().getUrn());
            assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_03_BASIC_ORDERED_02_NAME)).getLifeCycleStatisticalResource().getUrn(),
                    queriesPagedResult.getValues().get(i++).getLifeCycleStatisticalResource().getUrn());
            assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_04_BASIC_ORDERED_03_NAME)).getLifeCycleStatisticalResource().getUrn(),
                    queriesPagedResult.getValues().get(i++).getLifeCycleStatisticalResource().getUrn());
        }

        // Find code
        {
            String code = (queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getCode();
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.lifeCycleStatisticalResource().code()).eq(code)
                    .build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(1, queriesPagedResult.getTotalRows());
            int i = 0;
            assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getUrn(),
                    queriesPagedResult.getValues().get(i++).getLifeCycleStatisticalResource().getUrn());
        }

        // Find URN
        {
            String urn = (queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getUrn();
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.lifeCycleStatisticalResource().urn()).eq(urn).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(1, queriesPagedResult.getTotalRows());
            int i = 0;
            assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getUrn(),
                    queriesPagedResult.getValues().get(i++).getLifeCycleStatisticalResource().getUrn());
        }

        // Find title
        {
            String titleQueryVersion = (queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getTitle().getLocalisedLabel("es");
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class)
                    .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().title().texts().label()).eq(titleQueryVersion).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(1, queriesPagedResult.getTotalRows());
            int i = 0;
            assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_02_BASIC_ORDERED_01_NAME)).getLifeCycleStatisticalResource().getUrn(),
                    queriesPagedResult.getValues().get(i++).getLifeCycleStatisticalResource().getUrn());
        }
    }

    @Test
    @MetamacMock(QUERY_VERSION_11_DRAFT_NAME)
    public void testFindQueryVersionsByConditionDraftProcStatus() throws Exception {
        PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), null, null);

        assertEquals(1, queriesPagedResult.getTotalRows());
        assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME)).getLifeCycleStatisticalResource().getUrn(),
                queriesPagedResult.getValues().iterator().next().getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME)
    public void testFindQueryVersionsByConditionProductionValidationProcStatus() throws Exception {
        PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), null, null);

        assertEquals(1, queriesPagedResult.getTotalRows());
        assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME)).getLifeCycleStatisticalResource().getUrn(),
                queriesPagedResult.getValues().iterator().next().getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME)
    public void testFindQueryVersionsByConditionDiffusionValidationProcStatus() throws Exception {
        PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), null, null);

        assertEquals(1, queriesPagedResult.getTotalRows());
        assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME)).getLifeCycleStatisticalResource().getUrn(),
                queriesPagedResult.getValues().iterator().next().getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME)
    public void testFindQueryVersionsByConditionValidationRejectedProcStatus() throws Exception {
        PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), null, null);

        assertEquals(1, queriesPagedResult.getTotalRows());
        assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME)).getLifeCycleStatisticalResource().getUrn(),
                queriesPagedResult.getValues().iterator().next().getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME)
    public void testFindQueryVersionsByConditionPublishedNoVisibleProcStatus() throws Exception {
        PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), null, null);

        assertEquals(2, queriesPagedResult.getTotalRows());
        QueryVersion queryV01 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME);
        QueryVersion queryV02 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_27_V1_PUBLISHED_FOR_QUERY_06_NAME);
        assertEqualsQueryVersionCollection(Arrays.asList(queryV01, queryV02), queriesPagedResult.getValues());
    }

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testFindQueryVersionsByConditionPublishedProcStatus() throws Exception {
        PagedResult<QueryVersion> queriesPagedResult = queryService.findQueryVersionsByCondition(getServiceContextAdministrador(), null, null);

        assertEquals(1, queriesPagedResult.getTotalRows());
        assertEquals((queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME)).getLifeCycleStatisticalResource().getUrn(),
                queriesPagedResult.getValues().iterator().next().getLifeCycleStatisticalResource().getUrn());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryVersion() throws Exception {
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME), true);
        QueryVersion actual = queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    @MetamacMock({DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME, DATASET_VERSION_03_FOR_DATASET_03_NAME})
    public void testCreateQueryVersionDiscontinued() throws Exception {
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME), false);
        QueryVersion actual = queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEqualsQueryVersion(expected, actual);
        assertEquals(QueryStatusEnum.DISCONTINUED, actual.getStatus());
    }

    @Test
    @MetamacMock({DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME, DATASET_VERSION_03_FOR_DATASET_03_NAME})
    public void testCreateQueryVersionActive() throws Exception {
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME), true);
        QueryVersion actual = queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEqualsQueryVersion(expected, actual);
        assertEquals(QueryStatusEnum.ACTIVE, actual.getStatus());
    }

    @Test
    @MetamacMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME)
    public void testCreateQueryVersionErrorMetadataProcStatusMustBeNull() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.QUERY_VERSION__PROC_STATUS));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME), true);
        expected.getLifeCycleStatisticalResource().setProcStatus(ProcStatusEnum.DRAFT);
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
    }

    @Test
    @MetamacMock({DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME, DATASET_VERSION_03_FOR_DATASET_03_NAME})
    public void testCreateQueryVersionErrorParameterStatisticalOperationRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.STATISTICAL_OPERATION));

        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME), true);
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, null);
    }

    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryWithSelection() throws Exception {
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME), true);
        QueryVersion actual = queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEqualsQueryVersion(expected, actual);
    }

    @Test
    public void testCreateQueryVersionErrorDatasetVersionRequired() throws Exception {
        String params = buildCommaSeparatedString(ServiceExceptionParameters.QUERY_VERSION__DATASET, ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION);
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_SOME_REQUIRED, params));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion query = notPersistedDoMocks.mockQueryVersionWithDatasetVersion(null, true);
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_X", "X"));
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), query, statisticalOperation);
    }

    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryVersionErrorSelectionRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__SELECTION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion query = notPersistedDoMocks.mockQueryVersionWithSelectionNull(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), query, statisticalOperation);
    }

    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryVersionWithSelectionNull() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__SELECTION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion expected = notPersistedDoMocks.mockQueryVersionWithSelectionNull(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
    }

    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryVersionErrorSelectionIncorrectDimensionNull() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.QUERY_VERSION__SELECTION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion query = notPersistedDoMocks.mockQueryVersionWithSelectionIncorrectDimensionNull(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), query, statisticalOperation);
    }

    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryVersionErrorTimeSelectionMustBeEmptyWhenTypeIsLatestData() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.QUERY_VERSION_SELECTION_TIME_PERIOD));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion query = notPersistedDoMocks.mockQueryVersionLatestDataWithTimeSelectionNotEmpty(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), query, statisticalOperation);
    }

    @Test
    @MetamacMock(DATASET_VERSION_06_FOR_QUERIES_NAME)
    public void testCreateQueryVersionErrorSelectionIncorrectCodesNull() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.QUERY_VERSION__SELECTION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        QueryVersion query = notPersistedDoMocks.mockQueryVersionWithSelectionIncorrectCodesNull(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        queryService.createQueryVersion(getServiceContextWithoutPrincipal(), query, statisticalOperation);
    }

    @Test
    @MetamacMock({DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME, DATASET_VERSION_03_FOR_DATASET_03_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testUpdateDatasetVersionQueryVersionChangesQueryStatusFromDiscontinuedToActiveUsingDatasetVersion() throws Exception {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_08_BASIC_DISCONTINUED_NAME);
        expected.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME));
        expected.setDataset(null);
        expected.getSelection().clear();
        expected.addSelection(buildSelectionItemWithDimensionAndCodes("DIM01", "C01", "C02"));
        expected.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2012", "2011"));

        QueryVersion actual = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), expected);
        assertEquals(QueryStatusEnum.ACTIVE, actual.getStatus());
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testUpdateDatasetVersionQueryVersionChangesQueryStatusFromDiscontinuedToActiveUsingDataset() throws Exception {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_08_BASIC_DISCONTINUED_NAME);
        expected.setDataset(datasetMockFactory.retrieveMock(DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME));
        expected.setFixedDatasetVersion(null);
        expected.getSelection().clear();
        expected.addSelection(buildSelectionItemWithDimensionAndCodes("DIM01", "C01", "C02"));
        expected.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2010"));

        QueryVersion actual = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), expected);
        assertEquals(QueryStatusEnum.ACTIVE, actual.getStatus());
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testUpdateDatasetVersionQueryVersionCantSpecifyDatasetAndDatasetVersion() throws Exception {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_08_BASIC_DISCONTINUED_NAME);
        expected.setDataset(datasetMockFactory.retrieveMock(DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME));
        expected.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME));
        String params = buildCommaSeparatedString(ServiceExceptionParameters.QUERY_VERSION__DATASET, ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION);

        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCOMPATIBLE, params));
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), expected);
    }

    @Override
    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testUpdateQueryVersion() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME)
    public void testUpdateQueryVersionLatestData() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME);
        query.setLatestDataNumber(Integer.valueOf(13));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock({QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME, QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME})
    public void testUpdateQueryVersionErrorDuplicatedUrn() throws Exception {
        String duplicatedCode = queryVersionMockFactory.retrieveMock(QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME).getLifeCycleStatisticalResource().getCode();
        String duplicatedUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME).getLifeCycleStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.IDENTIFIABLE_STATISTICAL_RESOURCE_URN_DUPLICATED, duplicatedUrn));

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME);
        query.getLifeCycleStatisticalResource().setCode(duplicatedCode);
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock({QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME, QUERY_VERSION_20_WITH_CODE_AND_URN_QUERY02_NAME})
    public void testUpdateQueryVersionChangeCodeUpdateUrn() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_19_WITH_CODE_AND_URN_QUERY01_NAME);
        String newCode = "newCode";

        queryVersion.getLifeCycleStatisticalResource().setCode(newCode);
        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), queryVersion);

        assertEquals(newCode, updatedQueryVersion.getLifeCycleStatisticalResource().getCode());

        String[] maintainerCodes = new String[]{updatedQueryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested()};
        assertEquals(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(maintainerCodes, newCode, StatisticalResourcesMockFactory.INIT_VERSION),
                updatedQueryVersion.getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME)
    public void testUpdateQueryVersionErrorLatestDataNumberNull() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER));

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME);
        query.setType(QueryTypeEnum.FIXED);
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM01", "C01", "C02"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM02", "C01"));
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME)
    public void testUpdateQueryVersionErrorLatestDataNumberRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER));

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME);
        query.setLatestDataNumber(null);
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME)
    public void testUpdateQueryVersionErrorLatestDataNumberIncorrectValue() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.QUERY_VERSION__LATEST_DATA_NUMBER));

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_10_ACTIVE_LATEST_DATA_5_NAME);
        query.setLatestDataNumber(Integer.valueOf(0));
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testUpdateQueryVersionErrorStatusRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.QUERY_VERSION__STATUS));

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        query.setStatus(null);
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock(QUERY_VERSION_01_WITH_SELECTION_NAME)
    public void testUpdateQueryVersionErrorDatasetVersionRequired() throws Exception {

        String params = buildCommaSeparatedString(ServiceExceptionParameters.QUERY_VERSION__DATASET, ServiceExceptionParameters.QUERY_VERSION__FIXED_DATASET_VERSION);
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_SOME_REQUIRED, params));

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_01_WITH_SELECTION_NAME);
        query.setFixedDatasetVersion(null);
        query.setDataset(null);
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testUpdateQueryVersionDraftProcStatus() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testUpdateQueryVersionProductionValidationProcStatus() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME);
        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testUpdateQueryVersionDiffusionValidationProcStatus() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME);
        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testUpdateQueryValidationRejectedProcStatus() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME);
        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME, QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testUpdateQueryVersionPublishedNoVisibleProcStatus() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME);

        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testUpdateQueryVersionPublishedProcStatus() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        query.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalString());

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);
    }

    @Test
    @MetamacMock({QUERY_VERSION_05_BASIC_NAME, DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testUpdateDatasetVersionQueryVersion() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_05_BASIC_NAME);
        query.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusDraft() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        query.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Test
    @MetamacMock({QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusProductionValidation() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME);
        query.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Test
    @MetamacMock({QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusDiffusionValidation() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME);
        query.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Test
    @MetamacMock({QUERY_VERSION_14_VALIDATION_REJECTED_NAME, DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusValidationRejected() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME);
        query.setFixedDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME));
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Test
    @MetamacMock({QUERY_VERSION_15_PUBLISHED_NAME, DATASET_VERSION_06_FOR_QUERIES_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusPublishedIncorrectDatasetVersion() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_06_FOR_QUERIES_NAME);
        assertEquals(ProcStatusEnum.DRAFT, datasetVersion.getSiemacMetadataStatisticalResource().getProcStatus());

        query.setDataset(null);
        query.setFixedDatasetVersion(datasetVersion);
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_VERSION_DATASET_VERSION_MUST_BE_PUBLISHED, datasetVersion.getSiemacMetadataStatisticalResource().getUrn()));
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock({QUERY_VERSION_15_PUBLISHED_NAME, DATASET_01_BASIC_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusPublishedIncorrectDataset() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME);
        assertEquals(1, dataset.getVersions().size());
        assertEquals(ProcStatusEnum.DRAFT, dataset.getVersions().get(0).getSiemacMetadataStatisticalResource().getProcStatus());

        query.setDataset(dataset);
        query.setFixedDatasetVersion(null);
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_01", "CODE_01"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("DIM_02", "CODE_12"));

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_VERSION_DATASET_WITH_NO_PUBLISHED_VERSION, dataset.getIdentifiableStatisticalResource().getUrn()));
        queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
    }

    @Test
    @MetamacMock({QUERY_VERSION_15_PUBLISHED_NAME, DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusPublishedCorrectDatasetVersion() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME);
        assertEquals(ProcStatusEnum.PUBLISHED, datasetVersion.getSiemacMetadataStatisticalResource().getProcStatus());

        query.setDataset(null);
        query.setFixedDatasetVersion(datasetVersion);
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2011"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("GEO_DIM", "ES"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("MEAS_DIM", "C01"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Test
    @MetamacMock({QUERY_VERSION_15_PUBLISHED_NAME, DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME})
    public void testUpdateDatasetVersionQueryVersionProcStatusPublishedCorrectDataset() throws Exception {
        int datasetVersionsBefore = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();

        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_06_WITH_MULTIPLE_PUBLISHED_VERSIONS_AND_LATEST_NO_VISIBLE_NAME);
        assertEquals(ProcStatusEnum.PUBLISHED, dataset.getVersions().get(0).getSiemacMetadataStatisticalResource().getProcStatus());

        query.setDataset(dataset);
        query.setFixedDatasetVersion(null);
        query.getSelection().clear();
        query.addSelection(buildSelectionItemWithDimensionAndCodes("TIME_PERIOD", "2011"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("GEO_DIM", "ES"));
        query.addSelection(buildSelectionItemWithDimensionAndCodes("MEAS_DIM", "C01"));

        QueryVersion updatedQueryVersion = queryService.updateQueryVersion(getServiceContextWithoutPrincipal(), query);
        assertEqualsQueryVersion(query, updatedQueryVersion);

        int datasetVersionsAfter = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), null, null).getValues().size();
        assertEquals(datasetVersionsBefore, datasetVersionsAfter);
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME})
    public void testRetrieveQueryVersions() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_02_BASIC_ORDERED_01_NAME, QUERY_VERSION_03_BASIC_ORDERED_02_NAME, QUERY_VERSION_04_BASIC_ORDERED_03_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertEqualsQueryVersionCollection(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME})
    public void testRetrieveQueryVersionsDraftProcStatus() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_11_DRAFT_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertEqualsQueryVersionCollection(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME})
    public void testRetrieveQueryVersionsProductionValidationProcStatus() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertEqualsQueryVersionCollection(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME})
    public void testRetrieveQueryVersionsDiffusionValidationProcStatus() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertEqualsQueryVersionCollection(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_14_VALIDATION_REJECTED_NAME})
    public void testRetrieveQueryVersionsValidationRejectedProcStatus() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_14_VALIDATION_REJECTED_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertEqualsQueryVersionCollection(expected, actual);
    }

    @Test
    @MetamacMock({QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME})
    public void testRetrieveQueryVersionsValidationPublishedNoVisibleProcStatus() throws Exception {
        QueryVersion expected = queryVersionMockFactory.retrieveMock(QUERY_VERSION_28_V2_PUBLISHED_NO_VISIBLE_FOR_QUERY_06_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertTrue(actual.contains(expected));
    }

    @Test
    @MetamacMock({QUERY_VERSION_15_PUBLISHED_NAME})
    public void testRetrieveQueryVersionsValidationPublishedProcStatus() throws Exception {
        List<QueryVersion> expected = queryVersionMockFactory.retrieveMocks(QUERY_VERSION_15_PUBLISHED_NAME);
        List<QueryVersion> actual = queryService.retrieveQueryVersions(getServiceContextWithoutPrincipal());
        assertEqualsQueryVersionCollection(expected, actual);
    }

    @Override
    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testDeleteQueryVersion() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        String urn = queryVersion.getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, urn));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);
        queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(QUERY_VERSION_11_DRAFT_NAME)
    public void testDeleteQueryVersionWithDatasetVersion() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        String urn = queryVersion.getLifeCycleStatisticalResource().getUrn();

        String datasetVersionUrn = queryVersion.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getUrn();

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);

        assertNotNull(datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn));

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, urn));
        queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME})
    public void testDeleteQueryVersionIsPartOf() throws Exception {
        QueryVersion queryVersion = queryMockFactory.retrieveMock(QUERY_10_SINGLE_VERSION_DRAFT_USED_IN_PUBLICATIONS_NAME).getVersions().get(0);
        String urn = queryVersion.getLifeCycleStatisticalResource().getUrn();

        String pubVersion01Urn = getResourceUrn(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_61_DRAFT_WITH_PREVIOUS_VERSION__LINKED_TO_QUERY_10_NAME));
        String pubVersion02Urn = getResourceUrn(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_62_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME));
        String mulVersion01Urn = getResourceUrn(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_99_DRAFT_SINGLE_VERSION__LINKED_TO_QUERY_10_NAME));

        List<String> resourceUrns = Arrays.asList(pubVersion01Urn, pubVersion02Urn, mulVersion01Urn);
        Collections.sort(resourceUrns);

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_IS_PART_OF_OTHER_RESOURCES, StringUtils.join(resourceUrns, ", "));
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);

    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testDeleteQueryVersionDeletingQueryRoot() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        String urn = queryVersion.getLifeCycleStatisticalResource().getUrn();
        String queryUrn = queryVersion.getQuery().getIdentifiableStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, queryUrn));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);
        queryRepository.retrieveByUrn(queryUrn);
    }

    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testDeleteQueryVersionErrorUrnRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.URN));
        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({QUERY_VERSION_01_WITH_SELECTION_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testDeleteQueryVersionErrorNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, URN_NOT_EXISTS));
        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_06_BASIC_ACTIVE_NAME, QUERY_VERSION_08_BASIC_DISCONTINUED_NAME})
    public void testDeleteQueryVersionAndCheckDependencies() throws Exception {
        QueryVersion query = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME);
        String urn = query.getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, urn));

        // Initial situation
        int lifeCycleStatisticalResourcesBefore = statisticalResourceRepository.findAll().size();
        int datasetVersionsBefore = datasetVersionRepository.findAll().size();
        int querySelectionItemsBefore = querySelectionItemRepository.findAll().size();

        // Expected result
        int lifeCycleStatisticalResourcesExpected = lifeCycleStatisticalResourcesBefore - 2; // -2 query version and root query should not be present
        int datasetVersionsExpected = datasetVersionsBefore;
        int querySelectionItemsExpected = querySelectionItemsBefore - query.getSelection().size();

        // Delete
        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);

        // Final situation
        int lifeCycleStatisticalResourcesAfter = statisticalResourceRepository.findAll().size();
        int datasetVersionsAfter = datasetVersionRepository.findAll().size();
        int querySelectionItemsAfter = querySelectionItemRepository.findAll().size();

        // Checks
        assertEquals(lifeCycleStatisticalResourcesExpected, lifeCycleStatisticalResourcesAfter);
        assertEquals(datasetVersionsExpected, datasetVersionsAfter);
        assertEquals(querySelectionItemsExpected, querySelectionItemsAfter);
        queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testDeleteQueryVersionErrorInvalidProcStatusProductionValidation() throws Exception {
        String urn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testDeleteQueryVersionErrorInvalidProcStatusDiffusionValidation() throws Exception {
        String urn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testDeleteQueryVersionErrorInvalidProcStatusValidationRejected() throws Exception {
        String urn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({QUERY_VERSION_11_DRAFT_NAME, QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME, QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME, QUERY_VERSION_14_VALIDATION_REJECTED_NAME,
            QUERY_VERSION_15_PUBLISHED_NAME})
    public void testDeleteQueryVersionErrorInvalidProcStatusPublished() throws Exception {
        String urn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME).getLifeCycleStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @Override
    @MetamacMock(DATASET_VERSION_86_WITH_TEMPORAL_DIMENSION_NAME)
    public void testCheckQueryCompatibility() throws Exception {
        DatasetVersion dataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_86_WITH_TEMPORAL_DIMENSION_NAME);

        QueryVersion queryOk = queryVersionMockFactory.retrieveMock(QUERY_VERSION_29_CHECK_COMPAT_DATASET_86_OK_NAME);
        assertTrue(queryService.checkQueryCompatibility(getServiceContextAdministrador(), queryOk, dataset));

        QueryVersion queryLessDimensions = queryVersionMockFactory.retrieveMock(QUERY_VERSION_30_CHECK_COMPAT_DATASET_86_LESS_DIMENSIONS_NAME);
        assertFalse(queryService.checkQueryCompatibility(getServiceContextAdministrador(), queryLessDimensions, dataset));

        QueryVersion queryMoreDimensions = queryVersionMockFactory.retrieveMock(QUERY_VERSION_31_CHECK_COMPAT_DATASET_86_MORE_DIMENSIONS_NAME);
        assertFalse(queryService.checkQueryCompatibility(getServiceContextAdministrador(), queryMoreDimensions, dataset));

        QueryVersion queryMoreCodes = queryVersionMockFactory.retrieveMock(QUERY_VERSION_32_CHECK_COMPAT_DATASET_86_MORE_CODES_NAME);
        assertFalse(queryService.checkQueryCompatibility(getServiceContextAdministrador(), queryMoreCodes, dataset));

    }

    @Test
    @MetamacMock(DATASET_VERSION_87_WITH_NO_TEMPORAL_DIMENSION_NAME)
    public void testCheckQueryCompatibilityQueryWithTemporalChecks() throws Exception {
        DatasetVersion dataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_87_WITH_NO_TEMPORAL_DIMENSION_NAME);

        QueryVersion queryInvalidTypeAutoInc = queryVersionMockFactory.retrieveMock(QUERY_VERSION_34_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_AUTOINC_NAME);
        assertFalse(queryService.checkQueryCompatibility(getServiceContextAdministrador(), queryInvalidTypeAutoInc, dataset));

        QueryVersion queryInvalidTypeLatest = queryVersionMockFactory.retrieveMock(QUERY_VERSION_35_CHECK_COMPAT_DATASET_87_INVALID_QUERY_TYPE_LATEST_DATA_NAME);
        assertFalse(queryService.checkQueryCompatibility(getServiceContextAdministrador(), queryInvalidTypeLatest, dataset));
    }

    @Test
    @MetamacMock(QUERY_VERSION_57_REPLACES_QUERY_58_NAME)
    public void testDeleteQueryVersionReplacedVersion() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_57_REPLACES_QUERY_58_NAME);
        String urn = queryVersion.getLifeCycleStatisticalResource().getUrn();

        String datasetVersionUrn = queryVersion.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getUrn();

        queryService.deleteQueryVersion(getServiceContextWithoutPrincipal(), urn);

        assertNotNull(datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn));

    }

    @Override
    public void testUpdateGeographicalCache() throws Exception {
        // no optimistic locking in this operation
    }

    @Override
    public void testFindPurposes() throws Exception {
        // no optimistic locking in this operation

    }

    @Override
    public void testUpdateAllGeographicalCache() throws Exception {
        // no optimistic locking in this operation
    }
}
