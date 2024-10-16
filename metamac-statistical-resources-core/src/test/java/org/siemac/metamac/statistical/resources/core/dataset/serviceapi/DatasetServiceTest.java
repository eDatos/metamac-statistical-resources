package org.siemac.metamac.statistical.resources.core.dataset.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts.assertEqualsInternationalString;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsCategorisation;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersionCollection;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersionInGroup;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersionNotChecksDataset;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDimensionRepresentationMapping;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_01_DATASET_VERSION_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_01_DATASET_VERSION_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_02_DATASET_VERSION_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_02_DATASET_VERSION_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_03_DATASET_VERSION_02_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_MAINTAINER_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory.CATEGORISATION_SEQUENCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_01_FOR_UPDATE_MULTIPLE;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_02_FOR_UPDATE_MULTIPLE;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_03_FOR_DATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_05_FOR_DATASET_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_09_OPER_0001_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_101_TO_DELETE_WITH_PREVIOUS_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_114_DATABASE_DATASET_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_115_DATABASE_TYPE_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_119_DRAFT_DATABASE_DATASET_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_120_VALIDATION_REJECTED_DATABASE_DATASET_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_30_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_68_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_AND_USER_MODIFIED_DATE_NEXT_UPDATE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_79_NO_PUB_REPLACES_DATASET_80_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_80_NO_PUB_IS_REPLACED_BY_DATASET_79_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_85_LAST_VERSION_NOT_PUBLISHED__IS_PART_OF_PUBLICATIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DimensionRepresentationMappingMockFactory.DIMENSION_REPRESENTATION_MAPPING_01_DATASET_01_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_100_DRAFT_SINGLE_VERSION__LINKED_TO_DATASET_85_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_07_SIMPLE_MULTI_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_40_TO_PUBLISH_WITH_DATASET_VERSION_NOT_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_41_TO_PUBLISH_WITH_DATASET_WITH_NO_PUBLISHED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_42_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_COMPATIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_43_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_INCOMPATIBLE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticOfficialityMockFactory.STATISTIC_OFFICIALITY_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticOfficialityMockFactory.STATISTIC_OFFICIALITY_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks.mockCodeDimensionsWithIdentifiers;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.constants.ProcStatusForActionsConstants;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CategorisationProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DataSourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.task.domain.DatasetFileFormatEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.task.domain.FileDescriptorResult;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.DataMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.DatabaseDatasetImportUtils;
import org.siemac.metamac.statistical.resources.core.utils.DatasetLifecycleTestUtils;
import org.siemac.metamac.statistical.resources.core.utils.TaskMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.asserts.BaseAsserts;
import org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.CategorisationMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/dataset-repository-mockito.xml", "classpath:spring/statistical-resources/include/task-mockito.xml",
        "classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional

public class DatasetServiceTest extends StatisticalResourcesBaseTest implements DatasetServiceTestBase {

    @Autowired
    private DatasetService                   datasetService;

    @Autowired
    private DatasetVersionRepository         datasetVersionRepository;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private SrmRestInternalService           srmRestInternalService;

    @Autowired
    private TaskService                      taskService;

    @Before
    public void setUp() throws Exception {
        // Remove cglib from mock
        datasetRepositoriesServiceFacade = (DatasetRepositoriesServiceFacade) (((org.springframework.aop.framework.Advised) datasetRepositoriesServiceFacade).getTargetSource().getTarget());
        Mockito.reset(datasetRepositoriesServiceFacade);
        Mockito.reset(srmRestInternalService);
        mockAllTaskInProgressForResource(false);
    }

    @After
    public void after() {
        Mockito.validateMockitoUsage();
    }

    // ------------------------------------------------------------------------
    // DATASETS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock(DATASET_01_BASIC_NAME)
    public void testFindDatasetsByCondition() throws Exception {
        Dataset result = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME);

        // Find by code
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Dataset.class).withProperty(DatasetProperties.identifiableStatisticalResource().code())
                .eq(result.getIdentifiableStatisticalResource().getCode()).build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<Dataset> datasetsPagedResult = datasetService.findDatasetsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
        assertEquals(1, datasetsPagedResult.getTotalRows());
        assertEquals(result.getIdentifiableStatisticalResource().getUrn(), datasetsPagedResult.getValues().get(0).getIdentifiableStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock({DATASET_01_BASIC_NAME, DATASET_02_BASIC_WITH_GENERATED_VERSION_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testFindDatasetsByConditionWithoutConditions() throws Exception {
        // Find by code
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<Dataset> datasetsPagedResult = datasetService.findDatasetsByCondition(getServiceContextWithoutPrincipal(), null, pagingParameter);
        assertEquals(3, datasetsPagedResult.getTotalRows());
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateDatasetVersion() throws Exception {

        DatasetVersion expected = notPersistedDoMocks.mockDatasetVersion();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        mockDsdAndCreateDatasetRepository(expected, statisticalOperation);

        DatasetVersion actual = datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        String operationCode = actual.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(operationCode + "_000001", actual.getSiemacMetadataStatisticalResource().getCode());
        assertEquals(buildDatasetUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 1, StatisticalResourcesMockFactory.INIT_VERSION),
                actual.getSiemacMetadataStatisticalResource().getUrn());
        assertEqualsDatasetVersionNotChecksDataset(expected, actual);
        assertNotNull(actual.getSiemacMetadataStatisticalResource().getCreatedDate());
        assertNotNull(actual.getSiemacMetadataStatisticalResource().getCreatedBy());
        assertTrue(actual.getSiemacMetadataStatisticalResource().getCreatedDate().equals(actual.getSiemacMetadataStatisticalResource().getCreationDate()));
        assertTrue(actual.getSiemacMetadataStatisticalResource().getCreatedBy().equals(actual.getSiemacMetadataStatisticalResource().getCreationUser()));
        assertNotNull(actual.getDatasetRepositoryId());
        assertNotNull(actual.getDataset().getViewCode());
        assertEquals(DatasetVersionUtils.generateViewCode(actual.getSiemacMetadataStatisticalResource().getCode()), actual.getDataset().getViewCode());
    }

    @Test
    @MetamacMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME)
    public void testCreateDatasetVersionSameOperationExistingDataset() throws Exception {
        DatasetVersion datasetVersionOper1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);
        String operationCode = datasetVersionOper1.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        DatasetVersion expected = notPersistedDoMocks.mockDatasetVersion();

        mockDsdAndCreateDatasetRepository(expected, statisticalOperation);

        DatasetVersion actual = datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(operationCode + "_000004", actual.getSiemacMetadataStatisticalResource().getCode());
        assertEquals(buildDatasetUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 4, StatisticalResourcesMockFactory.INIT_VERSION),
                actual.getSiemacMetadataStatisticalResource().getUrn());

        assertEqualsDatasetVersionNotChecksDataset(expected, actual);
    }

    @Test
    @MetamacMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME)
    public void testCreateDatasetVersionSameOperationExistingDatasetConsecutiveCode() throws Exception {
        DatasetVersion datasetVersionOper1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_09_OPER_0001_CODE_000003_NAME);
        String operationCode = datasetVersionOper1.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        {
            ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
            DatasetVersion expected = notPersistedDoMocks.mockDatasetVersion();
            mockDsdAndCreateDatasetRepository(expected, statisticalOperation);
            DatasetVersion actual = datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
            assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
            assertEquals(operationCode + "_000004", actual.getSiemacMetadataStatisticalResource().getCode());
            assertEquals(buildDatasetUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 4, StatisticalResourcesMockFactory.INIT_VERSION),
                    actual.getSiemacMetadataStatisticalResource().getUrn());
            assertEqualsDatasetVersionNotChecksDataset(expected, actual);
        }
        {
            ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
            DatasetVersion expected = notPersistedDoMocks.mockDatasetVersion();
            mockDsdAndCreateDatasetRepository(expected, statisticalOperation);
            DatasetVersion actual = datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
            assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
            assertEquals(operationCode + "_000005", actual.getSiemacMetadataStatisticalResource().getCode());
            assertEquals(buildDatasetUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 5, StatisticalResourcesMockFactory.INIT_VERSION),
                    actual.getSiemacMetadataStatisticalResource().getUrn());
            assertEqualsDatasetVersionNotChecksDataset(expected, actual);
        }
    }

    @Test
    @MetamacMock(DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME)
    public void testCreateDatasetVersionMaxCode() throws Exception {
        DatasetVersion datasetVersionOper1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_12_OPER_0002_MAX_CODE_NAME);
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.DATASET_MAX_REACHED_IN_OPERATION, datasetVersionOper1.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn()));

        String operationCode = datasetVersionOper1.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), notPersistedDoMocks.mockDatasetVersion(), statisticalOperation);
    }

    @Test
    public void testCreateDatasetVersionErrorParameterDatasetRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.DATASET_VERSION));
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), null, statisticalOperation);
    }

    @Test
    public void testCreateDatasetVersionErrorParameterStatisticalOperationRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.STATISTICAL_OPERATION));

        DatasetVersion expected = notPersistedDoMocks.mockDatasetVersion();
        datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), expected, null);
    }

    @Test
    public void testCreateDatasetVersionErrorMetadataSiemacStatisticalResourceRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATASET_VERSION));

        DatasetVersion expected = notPersistedDoMocks.mockDatasetVersionWithNullableSiemacStatisticalResource();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        datasetService.createDatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
    }

    private void mockDsdAndCreateDatasetRepository(DatasetVersion expected, ExternalItem statisticalOperation) throws Exception, ApplicationException {
        String urn = buildDatasetUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), statisticalOperation.getCode(), 1, StatisticalResourcesMockFactory.INIT_VERSION);
        DataMockUtils.mockDsdAndCreateDatasetRepository(datasetRepositoriesServiceFacade, srmRestInternalService, urn);
    }

    @Override
    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testUpdateDatasetVersion() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        expected.getSiemacMetadataStatisticalResource().setDescription(notPersistedDoMocks.mockInternationalString());

        DatasetVersion actual = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsDatasetVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME)
    public void testUpdateDatasetVersionInDraft() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME);

        InternationalString expectedTitle = notPersistedDoMocks.mockInternationalString();
        expected.getSiemacMetadataStatisticalResource().setTitle(expectedTitle);

        InternationalString actualTitle = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected).getSiemacMetadataStatisticalResource().getTitle();
        assertEqualsInternationalString(expectedTitle, actualTitle);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testUpdateDatasetVersionInProductionValidation() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME);

        InternationalString expectedTitle = notPersistedDoMocks.mockInternationalString();
        expected.getSiemacMetadataStatisticalResource().setTitle(expectedTitle);

        InternationalString actualTitle = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected).getSiemacMetadataStatisticalResource().getTitle();
        assertEqualsInternationalString(expectedTitle, actualTitle);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testUpdateDatasetVersionInDiffusionValidation() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);

        InternationalString expectedTitle = notPersistedDoMocks.mockInternationalString();
        expected.getSiemacMetadataStatisticalResource().setTitle(expectedTitle);

        InternationalString actualTitle = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected).getSiemacMetadataStatisticalResource().getTitle();
        assertEqualsInternationalString(expectedTitle, actualTitle);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME)
    public void testUpdateDatasetVersionInValidationRejected() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);

        InternationalString expectedTitle = notPersistedDoMocks.mockInternationalString();
        expected.getSiemacMetadataStatisticalResource().setTitle(expectedTitle);

        InternationalString actualTitle = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected).getSiemacMetadataStatisticalResource().getTitle();
        assertEqualsInternationalString(expectedTitle, actualTitle);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME)
    public void testUpdateDatasetVersionInPublished() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);

        InternationalString expectedTitle = notPersistedDoMocks.mockInternationalString();
        expected.getSiemacMetadataStatisticalResource().setTitle(expectedTitle);

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, expected.getSiemacMetadataStatisticalResource().getUrn(),
                ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_RESOURCE));
        datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected).getSiemacMetadataStatisticalResource().getTitle();
    }

    @Test
    @MetamacMock(QUERY_07_SIMPLE_MULTI_VERSION_NAME)
    public void testUpdateDatasetVersionChangeDsdWithQueriesError() throws Exception {
        DatasetVersion updatedDataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME);
        updatedDataset.setRelatedDsdChanged(true);
        updatedDataset.setRelatedDsd(generateOtherDsdVersion(updatedDataset.getRelatedDsd()));

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_CHANGE_DSD_SOME_QUERIES_EXIST, updatedDataset.getSiemacMetadataStatisticalResource().getUrn()));
        datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), updatedDataset);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testUpdateDatasetVersionImportationTaskInProgress() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        String datasetUrn = expected.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);

        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        expected.getSiemacMetadataStatisticalResource().setDescription(notPersistedDoMocks.mockInternationalString());

        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), expected);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testUpdateDatasetVersionErrorFinal() throws Exception {
        DatasetVersion finalDataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME);
        String datasetVersionUrn = finalDataset.getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, datasetVersionUrn, "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), finalDataset);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testUpdateDatasetVersionErrorIncorrectCode() throws Exception {
        DatasetVersion dataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME);

        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATASET_VERSION__CODE));

        dataset.getSiemacMetadataStatisticalResource().setCode("@12345");
        datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), dataset);
    }

    @Test
    @MetamacMock({DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_NAME})
    public void testUpdateDatasetVersionChangeDsdClearDataRelatedMetadata() throws Exception {
        DatasetVersion dataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_NAME);

        ExternalItem newDsd = StatisticalResourcesDoMocks.mockDsdExternalItem();
        dataset.setRelatedDsd(newDsd);
        dataset.setRelatedDsdChanged(true);
        mockDsdAndCreateDatasetRepository(dataset, dataset.getSiemacMetadataStatisticalResource().getStatisticalOperation());

        DateTime oldLastUpdate = dataset.getSiemacMetadataStatisticalResource().getLastUpdate();

        DatasetVersion updatedDataset = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), dataset);

        assertEquals(newDsd.getUrn(), updatedDataset.getRelatedDsd().getUrn());
        assertEquals(0, updatedDataset.getDatasources().size());
        assertEquals(0, updatedDataset.getDimensionsCoverage().size());
        assertEquals(0, updatedDataset.getGeographicCoverage().size());
        assertEquals(0, updatedDataset.getTemporalCoverage().size());
        assertEquals(0, updatedDataset.getMeasureCoverage().size());
        assertNull(updatedDataset.getDateStart());
        assertNull(updatedDataset.getDateEnd());
        assertNotNull(updatedDataset.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertTrue(updatedDataset.getSiemacMetadataStatisticalResource().getLastUpdate().isAfter(oldLastUpdate));
        assertNull(updatedDataset.getFormatExtentDimensions());
        assertNull(updatedDataset.getFormatExtentTableSize());
        assertNull(updatedDataset.getFormatExtentObservations());
        assertTrue(BooleanUtils.isNotTrue(updatedDataset.getUserModifiedDateNextUpdate()));
        assertNull(updatedDataset.getDateNextUpdate());
        assertNotNull(updatedDataset.getDatasetRepositoryId());
        assertNull(updatedDataset.getDateLastTimeDataImport());
    }

    @Test
    @MetamacMock({DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_NAME})
    public void testUpdateDatasetVersionNoChangeDsdClearDataRelatedMetadata() throws Exception {
        DatasetVersion dataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_67_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_NAME);

        ExternalItem oldDsd = dataset.getRelatedDsd();
        dataset.setRelatedDsdChanged(false);

        DateTime oldLastUpdate = dataset.getSiemacMetadataStatisticalResource().getLastUpdate();

        DatasetVersion updatedDataset = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), dataset);

        assertEquals(oldDsd.getUrn(), updatedDataset.getRelatedDsd().getUrn());
        assertEquals(1, updatedDataset.getDatasources().size());
        assertEquals(10, updatedDataset.getDimensionsCoverage().size());
        assertEquals(4, updatedDataset.getGeographicCoverage().size());
        assertEquals(3, updatedDataset.getTemporalCoverage().size());
        assertEquals(3, updatedDataset.getMeasureCoverage().size());
        assertNotNull(updatedDataset.getDateStart());
        assertNotNull(updatedDataset.getDateEnd());
        assertNotNull(updatedDataset.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedDataset.getSiemacMetadataStatisticalResource().getLastUpdate().isAfter(oldLastUpdate));
        assertEquals(Integer.valueOf(3), updatedDataset.getFormatExtentDimensions());
        assertEquals(Long.valueOf(36L), updatedDataset.getFormatExtentTableSize());
        assertEquals(Long.valueOf(36L), updatedDataset.getFormatExtentObservations());
        assertTrue(BooleanUtils.isNotTrue(updatedDataset.getUserModifiedDateNextUpdate()));
        assertNotNull(updatedDataset.getDateNextUpdate());
        assertNotNull(updatedDataset.getDatasetRepositoryId());
        assertNotNull(updatedDataset.getDateLastTimeDataImport());
    }

    @Test
    @MetamacMock({DATASET_VERSION_68_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_AND_USER_MODIFIED_DATE_NEXT_UPDATE_NAME})
    public void testUpdateDatasetVersionChangeDsdClearDataRelatedMetadataKeepUserDateNextUpdate() throws Exception {
        DatasetVersion dataset = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_68_WITH_DATASOURCES_AND_COMPUTED_FIELDS_FILLED_AND_USER_MODIFIED_DATE_NEXT_UPDATE_NAME);

        ExternalItem newDsd = StatisticalResourcesDoMocks.mockDsdExternalItem();
        dataset.setRelatedDsd(newDsd);
        dataset.setRelatedDsdChanged(true);
        mockDsdAndCreateDatasetRepository(dataset, dataset.getSiemacMetadataStatisticalResource().getStatisticalOperation());

        DateTime oldLastUpdate = dataset.getSiemacMetadataStatisticalResource().getLastUpdate();
        String oldNextDateUpdate = dataset.getDateNextUpdate();

        DatasetVersion updatedDataset = datasetService.updateDatasetVersion(getServiceContextWithoutPrincipal(), dataset);

        assertEquals(newDsd.getUrn(), updatedDataset.getRelatedDsd().getUrn());
        assertEquals(0, updatedDataset.getDatasources().size());
        assertEquals(0, updatedDataset.getDimensionsCoverage().size());
        assertEquals(0, updatedDataset.getGeographicCoverage().size());
        assertEquals(0, updatedDataset.getTemporalCoverage().size());
        assertEquals(0, updatedDataset.getMeasureCoverage().size());
        assertNull(updatedDataset.getDateStart());
        assertNull(updatedDataset.getDateEnd());
        assertNotNull(updatedDataset.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertTrue(updatedDataset.getSiemacMetadataStatisticalResource().getLastUpdate().isAfter(oldLastUpdate));
        assertNull(updatedDataset.getFormatExtentDimensions());
        assertNull(updatedDataset.getFormatExtentTableSize());
        assertNull(updatedDataset.getFormatExtentObservations());
        assertTrue(BooleanUtils.isTrue(updatedDataset.getUserModifiedDateNextUpdate()));
        Assert.assertEquals(oldNextDateUpdate, updatedDataset.getDateNextUpdate());
        assertNotNull(updatedDataset.getDatasetRepositoryId());
        assertNull(updatedDataset.getDateLastTimeDataImport());
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasetVersionByUrn() throws Exception {
        String urn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME), actual);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveLatestDatasetVersionByDatasetUrn() throws Exception {
        String urn = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        DatasetVersion actual = datasetService.retrieveLatestDatasetVersionByDatasetUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveLatestDatasetVersionByDatasetUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.DATASET_URN));
        datasetService.retrieveLatestDatasetVersionByDatasetUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveLatestPublishedDatasetVersionByDatasetUrn() throws Exception {
        String urn = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        DatasetVersion actual = datasetService.retrieveLatestPublishedDatasetVersionByDatasetUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME), actual);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveLatestPublishedDatasetVersionByDatasetUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.DATASET_URN));
        datasetService.retrieveLatestPublishedDatasetVersionByDatasetUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasetVersionByUrnV2() throws Exception {
        String urn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsDatasetVersion(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    public void testRetrieveDatasetVersionByUrnErrorParameterRequiered() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.DATASET_VERSION_URN));

        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasetVersionByUrnErrorNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_01_BASIC_NAME, DATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testRetrieveDatasetVersions() throws Exception {
        String urn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        List<DatasetVersion> actual = datasetService.retrieveDatasetVersions(getServiceContextWithoutPrincipal(), urn);

        assertEquals(2, actual.size());
        assertEqualsDatasetVersionCollection(datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME).getVersions(), actual);
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_01_BASIC_NAME, DATASET_VERSION_01_BASIC_NAME})
    public void testFindDatasetVersionsByCondition() throws Exception {
        // Find by version number
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class).withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().versionLogic())
                .eq(StatisticalResourcesMockFactory.SECOND_VERSION).orderBy(DatasetVersionProperties.siemacMetadataStatisticalResource().id()).ascending().build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<DatasetVersion> datasetVersionPagedResult = datasetService.findDatasetVersionsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
        assertEquals(1, datasetVersionPagedResult.getTotalRows());
        assertEquals(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                datasetVersionPagedResult.getValues().get(0).getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_01_BASIC_NAME, DATASET_VERSION_01_BASIC_NAME})
    public void testDeleteDatasetVersion() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn));

        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME)
    public void testDeleteDatasetVersionProcStatusDraft() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn));
        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME)
    public void testDeleteDatasetVersionProcStatusValidationRejected() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn));
        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testDeleteDatasetVersionProcStatusProductionValidation() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME));

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testDeleteDatasetVersionProcStatusDiffusionValidation() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME));

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME)
    public void testDeleteDatasetVersionProcStatusPublished() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME));

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME)
    public void testDeleteDatasetVersionMultiVersionClearDatasetRepository() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME));

        DatasetVersion datasetVersion = datasetVersionRepository.retrieveByUrn(urn);
        String datasetRepositoryId = datasetVersion.getDatasetRepositoryId();
        assertNotNull(datasetRepositoryId);

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

        Mockito.verify(datasetRepositoriesServiceFacade).deleteDatasetRepository(datasetRepositoryId);
    }

    @Test
    @MetamacMock(DATASET_04_FULL_FILLED_WITH_1_DATASET_VERSIONS_NAME)
    public void testDeleteDatasetVersionAndDatasetClearDatasetRepository() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_05_FOR_DATASET_04_NAME));

        DatasetVersion datasetVersion = datasetVersionRepository.retrieveByUrn(urn);
        String datasetRepositoryId = datasetVersion.getDatasetRepositoryId();
        assertNotNull(datasetRepositoryId);

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

        Mockito.verify(datasetRepositoriesServiceFacade).deleteDatasetRepository(datasetRepositoryId);
    }

    @Test
    @MetamacMock({DATASET_VERSION_79_NO_PUB_REPLACES_DATASET_80_NAME, DATASET_VERSION_80_NO_PUB_IS_REPLACED_BY_DATASET_79_NAME})
    public void testDeleteDatasetVersionReplacedByOther() throws Exception {
        String datasetReplacesUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_79_NO_PUB_REPLACES_DATASET_80_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String urn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_80_NO_PUB_IS_REPLACED_BY_DATASET_79_NAME).getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REPLACED_BY_OTHER_RESOURCE, datasetReplacesUrn);
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

    }

    @Test
    @MetamacMock({DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME})
    public void testDeleteDatasetVersionReplacesOther() throws Exception {
        String datasetReplacesUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_77_NO_PUB_REPLACES_DATASET_78_NAME).getSiemacMetadataStatisticalResource().getUrn();

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), datasetReplacesUrn);
    }

    @Test
    @MetamacMock({DATASET_VERSION_101_TO_DELETE_WITH_PREVIOUS_VERSION_NAME})
    public void testDeleteDatasetVersionReplacesVersion() throws Exception {
        String datasetReplacesUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_101_TO_DELETE_WITH_PREVIOUS_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), datasetReplacesUrn);
    }

    @Test
    @MetamacMock({DATASET_VERSION_85_LAST_VERSION_NOT_PUBLISHED__IS_PART_OF_PUBLICATIONS_NAME, PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME,
            PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME, PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME})
    public void testDeleteDatasetVersionIsPartOf() throws Exception {
        PublicationVersion publicationVersion01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_43_DRAFT_HAS_PART_DATASET_VERSION_85_FIRST_LEVEL_NAME);
        PublicationVersion publicationVersion02 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_44_DRAFT_HAS_PART_DATASET_VERSION_85_NO_FIRST_LEVEL_NAME);
        PublicationVersion publicationVersion03 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_45_DRAFT_HAS_PART_DATASET_VERSION_85_MULTI_CUBE_NAME);
        MultidatasetVersion multidatasetVersion01 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_100_DRAFT_SINGLE_VERSION__LINKED_TO_DATASET_85_NAME);

        List<String> publicationsUrn = Arrays.asList(publicationVersion01.getSiemacMetadataStatisticalResource().getUrn(), publicationVersion02.getSiemacMetadataStatisticalResource().getUrn(),
                publicationVersion03.getSiemacMetadataStatisticalResource().getUrn(), multidatasetVersion01.getSiemacMetadataStatisticalResource().getUrn());
        Collections.sort(publicationsUrn);

        String urn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_85_LAST_VERSION_NOT_PUBLISHED__IS_PART_OF_PUBLICATIONS_NAME).getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_PART_OF_OTHER_RESOURCES, StringUtils.join(publicationsUrn, ", "));
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

    }

    @Test
    @MetamacMock({QUERY_VERSION_40_TO_PUBLISH_WITH_DATASET_VERSION_NOT_PUBLISHED_NAME})
    public void testDeleteDatasetVersionIsRequiredBy() throws Exception {
        QueryVersion queryVersion01 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_40_TO_PUBLISH_WITH_DATASET_VERSION_NOT_PUBLISHED_NAME);

        String urn = queryVersion01.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES,
                StringUtils.join(Arrays.asList(queryVersion01.getLifeCycleStatisticalResource().getUrn()), ", "));
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

    }

    @Test
    @MetamacMock({QUERY_VERSION_41_TO_PUBLISH_WITH_DATASET_WITH_NO_PUBLISHED_VERSION_NAME})
    public void testDeleteDatasetVersionIsRequiredByQueryLinkedToDataset() throws Exception {
        QueryVersion queryVersion01 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_41_TO_PUBLISH_WITH_DATASET_WITH_NO_PUBLISHED_VERSION_NAME);

        String urn = queryVersion01.getDataset().getVersions().get(0).getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES,
                StringUtils.join(Arrays.asList(queryVersion01.getLifeCycleStatisticalResource().getUrn()), ", "));
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

    }

    @Test
    @MetamacMock(QUERY_VERSION_42_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_COMPATIBLE_NAME)
    public void testDeleteDatasetVersionIsRequiredByQueryLinkedToDatasetPreviousPublishedVersionCompatible() throws Exception {
        QueryVersion queryVersion01 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_42_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_COMPATIBLE_NAME);

        String urn = queryVersion01.getDataset().getVersions().get(1).getSiemacMetadataStatisticalResource().getUrn();

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn));

        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(QUERY_VERSION_43_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_INCOMPATIBLE_NAME)
    public void testDeleteDatasetVersionIsRequiredByQueryLinkedToDatasetPreviousPublishedVersionInCompatible() throws Exception {
        QueryVersion queryVersion01 = queryVersionMockFactory.retrieveMock(QUERY_VERSION_43_TO_PUBLISH_WITH_DATASET_WITH_LAST_VERSION_NOT_PUBLISHED_PREVIOUS_INCOMPATIBLE_NAME);

        String urn = queryVersion01.getDataset().getVersions().get(1).getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.DATASET_VERSION_IS_REQUIRED_BY_OTHER_RESOURCES_LAST_VERSION_INCOMPATIBLE,
                queryVersion01.getLifeCycleStatisticalResource().getUrn());
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_01_BASIC_NAME, DATASET_VERSION_01_BASIC_NAME})
    public void testDeleteDatasetVersionImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, DATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testDeleteDatasetVersionWithTwoVersions() throws Exception {
        String urnV1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String urnV2 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_04_FOR_DATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urnV2));

        // Delete dataset version
        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urnV2);

        // Validation
        DatasetVersion datasetVersionV1 = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urnV1);

        // is replaced_by_version
        assertNull(datasetVersionV1.getSiemacMetadataStatisticalResource().getIsReplacedByVersion());
        assertTrue(datasetVersionV1.getSiemacMetadataStatisticalResource().getLastVersion());

        datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urnV2);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testDeleteDatasetVersionErrorNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testDeleteDatasetVersionErrorNoLastVersion() throws Exception {
        String urnV1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_03_FOR_DATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urnV1, "DRAFT, VALIDATION_REJECTED"));

        datasetService.deleteDatasetVersion(getServiceContextWithoutPrincipal(), urnV1);
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersion() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicle() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicleFieldNextVersionRequired() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();
        basicVersionableStatisticalResourceDto.setNextVersion(null);

        try {
            datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
            fail("parameter next version required");
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new Serializable[]{ServiceExceptionParameters.DATASET_NEXT_VERSION}, e.getExceptionItems().get(0));
        }
    }

    @Test
    @MetamacMock(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME)
    public void testImportDatasourcesInDatasetVersionPublishedWithAutomaticLifeCicle() throws Exception {
        String urn = getResourceUrn(datasetVersionMockFactory.retrieveMock(DATASET_VERSION_61_PUBLISHED_INITIAL_VERSION_NAME));

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();

        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicleFieldDateNextVersionRequired() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();
        basicVersionableStatisticalResourceDto.setNextVersionDate(null);

        try {
            datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
            fail("parameter date next version required");
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new Serializable[]{ServiceExceptionParameters.DATASET_DATE_NEXT_VERSION}, e.getExceptionItems().get(0));
        }
    }

    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicleFieldDateNextProcStatusRequired() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();
        basicVersionableStatisticalResourceDto.setNextProcStatus(null);

        try {
            datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
            fail("parameter date next proc status required");
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new Serializable[]{ServiceExceptionParameters.DATASET_NEXT_PROC_STATUS}, e.getExceptionItems().get(0));
        }
    }

    @Test
    @MetamacMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME)
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicleFieldProcStatusProductionInvalid() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_20_PRODUCTION_VALIDATION_READY_FOR_DIFFUSION_VALIDATION_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                .withMessageParameters(urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_IMPORT_ZIP_DATASOURCES).build());

        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testImportDatasourcesInDatasetVersionWithAutomaticLifeCicleFieldProcStatusDifussionInvalid() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = getMockBasicVersionableStatisticalResourceDto();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                .withMessageParameters(urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_IMPORT_ZIP_DATASOURCES).build());

        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, basicVersionableStatisticalResourceDto);
    }

    @Test
    @MetamacMock(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME)
    public void testImportDatasourcesInDatasetVersionProcStatusDraft() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_57_DRAFT_INITIAL_VERSION_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, new HashMap<String, String>(), false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME)
    public void testImportDatasourcesInDatasetVersionProcStatusValidationRejected() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, new HashMap<String, String>(), false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testImportDatasourcesInDatasetVersionProcStatusProductionValidation() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_IMPORT_DATASOURCES_IN_DATASET_VERSION));
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, new HashMap<String, String>(), false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testImportDatasourcesInDatasetVersionProcStatusDiffusionValidation() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_IMPORT_DATASOURCES_IN_DATASET_VERSION));
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, new HashMap<String, String>(), false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME)
    public void testImportDatasourcesInDatasetVersionProcStatusPublished() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_IMPORT_DATASOURCES_IN_DATASET_VERSION));
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, new HashMap<String, String>(), false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME)
    public void testImportDatasourcesInDatasetVersionImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn, urls, mappings, false, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock({DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME, DATASET_VERSION_30_WITH_DATASOURCE_NAME})
    public void testImportDatasourcesInDatasetVersionFileAlreadyAssignedInOtherDataset() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_29_WITHOUT_DATASOURCES_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.INVALID_FILE_FOR_DATASET_VERSION, "datasource_06.px", urn))));

        List<URL> urls = Arrays.asList(new File("datasource_06.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false, new BasicVersionableStatisticalResourceDto());
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME, DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME})
    public void testImportDatasourcesInStatisticalOperation() throws Exception {
        DatasetVersion datasetVersion37 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME);
        String fileForDatasetVersion37 = datasetVersion37.getDatasources().get(0).getSourceName();
        DatasetVersion datasetVersion38 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME);
        String fileForDatasetVersion38 = datasetVersion38.getDatasources().get(0).getSourceName();

        String statisticalOperationCode = datasetVersion37.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        List<URL> urls = Arrays.asList(buildURLForFile(fileForDatasetVersion37), buildURLForFile(fileForDatasetVersion38));
        datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock({DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME, DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME})
    public void testImportDatasourcesInStatisticalOperationImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion37 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME);

        String fileForDatasetVersion37 = datasetVersion37.getDatasources().get(0).getSourceName();
        DatasetVersion datasetVersion38 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME);
        String fileForDatasetVersion38 = datasetVersion38.getDatasources().get(0).getSourceName();

        String datasetUrn37 = datasetVersion37.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn37, true);
        MetamacException expectedException = new MetamacException(ServiceExceptionType.IMPORTATION_DATASET_VERSION_ERROR, datasetVersion37.getSiemacMetadataStatisticalResource().getUrn());
        expectedException.getExceptionItems().get(0).setExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn37)));

        expectedMetamacException(expectedException);

        String statisticalOperationCode = datasetVersion37.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        List<URL> urls = Arrays.asList(buildURLForFile(fileForDatasetVersion37), buildURLForFile(fileForDatasetVersion38));
        throw datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());
    }

    @Test
    @MetamacMock({DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME, DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME})
    public void testImportDatasourcesInStatisticalOperationNotLinkedFile() throws Exception {
        DatasetVersion datasetVersion37 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_37_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME);
        DatasetVersion datasetVersion38 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_38_WITH_SINGLE_DATASOURCE_IN_OPERATION_0001_NAME);
        String fileForDatasetVersion38 = datasetVersion38.getDatasources().get(0).getSourceName();

        String statisticalOperationCode = datasetVersion37.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        expectedMetamacException(new MetamacException(ServiceExceptionType.FILE_NOT_LINKED_TO_ANY_DATASET_IN_STATISTICAL_OPERATION, "not_exist.px", statisticalOperationCode));

        List<URL> urls = Arrays.asList(buildURLForFile("not_exist.px"), buildURLForFile(fileForDatasetVersion38));
        throw datasetService.importDatasourcesInStatisticalOperation(getServiceContextWithoutPrincipal(), statisticalOperationCode, urls, new BasicVersionableStatisticalResourceDto());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA_NAME)
    public void testProccessDatasetFileImportationResult() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA_NAME).getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensions();

        FileDescriptorResult fileDescriptor = new FileDescriptorResult();
        fileDescriptor.setDatasourceId("file.csv_" + new Date().toString());
        fileDescriptor.setFileName("file.csv");
        fileDescriptor.setDatasetFileFormatEnum(DatasetFileFormatEnum.CSV);

        List<FileDescriptorResult> fileDescriptors = new ArrayList<FileDescriptorResult>();
        fileDescriptors.add(fileDescriptor);

        datasetService.proccessDatasetFileImportationResult(getServiceContextAdministrador(), datasetVersionUrn, fileDescriptors);

        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);
        assertNotNull(datasetVersion.getDatasetRepositoryId());

        List<Datasource> datasources = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);
        assertEquals(1, datasources.size());
        assertEquals(fileDescriptor.getDatasourceId(), datasources.get(0).getIdentifiableStatisticalResource().getCode());
        assertEquals(fileDescriptor.getFileName(), datasources.get(0).getSourceName());
        assertNull(datasources.get(0).getDateNextUpdate());
    }

    @Test
    @MetamacMock(DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA_NAME)
    public void testProccessDatasetFileImportationResultPxDatasource() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_28_WITHOUT_DATASOURCES_IMPORTING_DATA_NAME).getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensions();

        FileDescriptorResult fileDescriptor = new FileDescriptorResult();
        fileDescriptor.setDatasourceId("file.px_" + new Date().toString());
        fileDescriptor.setFileName("file.px");
        fileDescriptor.setDatasetFileFormatEnum(DatasetFileFormatEnum.PX);
        fileDescriptor.setNextUpdate(new Date());

        List<FileDescriptorResult> fileDescriptors = new ArrayList<FileDescriptorResult>();
        fileDescriptors.add(fileDescriptor);

        datasetService.proccessDatasetFileImportationResult(getServiceContextAdministrador(), datasetVersionUrn, fileDescriptors);

        List<Datasource> datasources = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);
        assertEquals(1, datasources.size());
        assertEquals(fileDescriptor.getDatasourceId(), datasources.get(0).getIdentifiableStatisticalResource().getCode());
        BaseAsserts.assertEqualsDate(datasources.get(0).getDateNextUpdate(), fileDescriptor.getNextUpdate());
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testRetrieveCoverageForDatasetVersionDimension() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        {
            List<CodeDimension> codeDimensions = datasetService.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim-none");
            Assert.assertEquals(0, codeDimensions.size());
        }
        {
            List<CodeDimension> codeDimensions = datasetService.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim1");
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(mockCodeDimensionsWithIdentifiers(datasetVersion, "dim1", "code-d1-1", "code-d1-2"), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = datasetService.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim2");
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(mockCodeDimensionsWithIdentifiers(datasetVersion, "dim2", "code-d2-1", "code-d2-2"), codeDimensions);
        }
        {
            List<CodeDimension> codeDimensions = datasetService.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim3");
            DatasetsAsserts.assertEqualsCodeDimensionsCollection(mockCodeDimensionsWithIdentifiers(datasetVersion, "dim3", "code-d3-1"), codeDimensions);
        }
    }

    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testRetrieveCoverageForDatasetVersionDimensionImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        {
            List<CodeDimension> codeDimensions = datasetService.retrieveCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "dim-none");
            Assert.assertEquals(0, codeDimensions.size());
        }
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME)
    public void testFilterCoverageForDatasetVersionDimension() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        {
            List<CodeDimension> codeDimensions = datasetService.filterCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "TIME_PERIOD", "Enero");
            Assert.assertEquals(1, codeDimensions.size());
        }
    }

    @Test
    @MetamacMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME)
    public void testFilterCoverageForDatasetVersionDimensionImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_47_WITH_COVERAGE_FILLED_WITH_TITLES_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        {
            List<CodeDimension> codeDimensions = datasetService.filterCoverageForDatasetVersionDimension(getServiceContextAdministrador(), datasetVersionUrn, "TIME_PERIOD", "Enero");
            Assert.assertEquals(1, codeDimensions.size());
        }
    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testRetrieveDatasetVersionDimensionsIds() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<String> dimensionIds = datasetService.retrieveDatasetVersionDimensionsIds(getServiceContextAdministrador(), datasetVersionUrn);
        assertEquals(dimensionIds, Arrays.asList("dim1", "dim2", "dim3"));
    }

    @Test
    @MetamacMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME)
    public void testRetrieveDatasetVersionDimensionsIdsImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_27_WITH_COVERAGE_FILLED_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        List<String> dimensionIds = datasetService.retrieveDatasetVersionDimensionsIds(getServiceContextAdministrador(), datasetVersionUrn);
        assertEquals(dimensionIds, Arrays.asList("dim1", "dim2", "dim3"));
    }

    @Override
    @Test
    @MetamacMock({STATISTIC_OFFICIALITY_01_BASIC_NAME, STATISTIC_OFFICIALITY_02_BASIC_NAME})
    public void testFindStatisticOfficialities() throws Exception {
        List<StatisticOfficiality> officialities = datasetService.findStatisticOfficialities(getServiceContextAdministrador());
        assertEquals(2, officialities.size());
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersion() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(10);
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);

        List<Datasource> datasources = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);

        assertTrue(CollectionUtils.isNotEmpty(datasources));
        assertEquals(1, datasources.size());
        assertNotNull(datasources.get(0).getSourceName());
        assertEquals(tableName, datasources.get(0).getSourceName());
    }

    @Test
    @MetamacMock({DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionTableNameMaxLengthError() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(DatabaseDatasetImportUtils.TABLENAME_MAX_LENGTH_PERMITTED + 1);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_TABLENAME_LENGTH).withMessageParameters(tableName.length(), tableName,
                datasetVersionUrn, DatabaseDatasetImportUtils.TABLENAME_MIN_LENGTH_PERMITTED, DatabaseDatasetImportUtils.TABLENAME_MAX_LENGTH_PERMITTED).build());

        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);
    }

    @Test
    @MetamacMock({DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionTableNameFormatError() throws Exception {
        String tableName = RandomStringUtils.randomNumeric(10);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_TABLENAME_FORMAT).withMessageParameters(tableName, datasetVersionUrn).build());

        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);
    }

    @Test
    @MetamacMock({DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionTaskInProgressError() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(10);

        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);
    }

    @Test
    @MetamacMock({DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionProducctionValidationDatasetCantImportDatasourceError() throws Exception {
        testCreateDatabaseDatasourceInDatasetVersionCantImportDatasourceError(DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionDifussionValidationDatasetCantImportDatasourceError() throws Exception {
        testCreateDatabaseDatasourceInDatasetVersionCantImportDatasourceError(DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionPublishedDatasetCantImportDatasourceError() throws Exception {
        testCreateDatabaseDatasourceInDatasetVersionCantImportDatasourceError(DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME);
    }

    private void testCreateDatabaseDatasourceInDatasetVersionCantImportDatasourceError(String mockName) throws Exception, MetamacException {
        String tableName = RandomStringUtils.randomAlphabetic(10);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(mockName).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                .withMessageParameters(datasetVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_IMPORT_DATASOURCES_IN_DATASET_VERSION).build());

        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);
    }

    @Test
    @MetamacMock({DATASET_VERSION_119_DRAFT_DATABASE_DATASET_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionDraftDataset() throws Exception {
        testCreateDatabaseDatasourceInDatasetVersionCanImportDatasource(DATASET_VERSION_119_DRAFT_DATABASE_DATASET_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_120_VALIDATION_REJECTED_DATABASE_DATASET_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionValidationRejectedDataset() throws Exception {
        testCreateDatabaseDatasourceInDatasetVersionCanImportDatasource(DATASET_VERSION_120_VALIDATION_REJECTED_DATABASE_DATASET_NAME);
    }

    private void testCreateDatabaseDatasourceInDatasetVersionCanImportDatasource(String mockName) throws Exception, MetamacException {
        String tableName = RandomStringUtils.randomAlphabetic(10);
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(mockName).getSiemacMetadataStatisticalResource().getUrn();

        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);

        List<Datasource> datasources = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);

        assertTrue(CollectionUtils.isNotEmpty(datasources));
        assertEquals(1, datasources.size());
        assertNotNull(datasources.get(0).getSourceName());
        assertEquals(tableName, datasources.get(0).getSourceName());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionInvalidDataSourceTypeError() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(10);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_DATA_SOURCE_TYPE_FOR_DATASET_DATA_IMPORTATION)
                .withMessageParameters(DataSourceTypeEnum.FILE, DataSourceTypeEnum.DATABASE).build());

        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);
    }

    @Test
    @MetamacMock({DATASET_VERSION_114_DATABASE_DATASET_WITH_DATASOURCE_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionExistsDatasourceForDatasetError() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(10);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_114_DATABASE_DATASET_WITH_DATASOURCE_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_MORE_THAN_ONE_DATASOURCE_FOR_DATABASE_IMPORTATION_ERROR).build());

        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);
    }

    @Test
    @MetamacMock({DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME, DATASET_VERSION_115_DATABASE_TYPE_BASIC_NAME})
    public void testCreateDatabaseDatasourceInDatasetVersionDuplicatedSourceName() throws Exception {
        String tableName = RandomStringUtils.randomAlphabetic(10);
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn, tableName);

        List<Datasource> datasources = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), datasetVersionUrn);

        assertTrue(CollectionUtils.isNotEmpty(datasources));
        assertEquals(1, datasources.size());
        assertNotNull(datasources.get(0).getSourceName());
        assertEquals(tableName, datasources.get(0).getSourceName());

        String newDatasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_115_DATABASE_TYPE_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();
        datasetService.createDatabaseDatasourceInDatasetVersion(getServiceContextAdministrador(), newDatasetVersionUrn, tableName);

        List<Datasource> newDatasetVersiondatasources = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextAdministrador(), newDatasetVersionUrn);

        assertTrue(CollectionUtils.isNotEmpty(newDatasetVersiondatasources));
        assertEquals(1, newDatasetVersiondatasources.size());
        assertNotNull(newDatasetVersiondatasources.get(0).getSourceName());
        assertEquals(tableName, newDatasetVersiondatasources.get(0).getSourceName());
        assertEquals(datasources.get(0).getSourceName(), newDatasetVersiondatasources.get(0).getSourceName());

    }

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME)
    public void testImportDatabaseDatasourcesInDatasetVersion() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_113_DATABASE_TYPE_BASIC_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatabaseDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false);
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testImportDatabaseDatasourcesInDatasetVersionInvalidDataSourceTypeError() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.INVALID_DATA_SOURCE_TYPE_FOR_DATASET_DATA_IMPORTATION)
                .withMessageParameters(DataSourceTypeEnum.FILE, DataSourceTypeEnum.DATABASE).build());

        datasetService.importDatabaseDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn, urls, mappings, false);
    }

    @Test
    @MetamacMock(DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET_NAME)
    public void testImportDatabaseDatasourcesInDatasetVersionProductionValidationDatabaseDataset() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_116_PRODUCTION_VALIDATION_DATABASE_DATASET_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatabaseDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false);
    }

    @Test
    @MetamacMock(DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET_NAME)
    public void testImportDatabaseDatasourcesInDatasetVersionDiffusionValidationDatabaseDataset() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_117_DIFUSSION_VALIDATION_DATASET_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatabaseDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false);
    }

    @Test
    @MetamacMock(DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME)
    public void testImportDatabaseDatasourcesInDatasetVersionPublishedDatabaseDataset() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        HashMap<String, String> mappings = new HashMap<String, String>();
        datasetService.importDatabaseDatasourcesInDatasetVersion(getServiceContextWithoutPrincipal(), urn, urls, mappings, false);
    }

    // ------------------------------------------------------------------------
    // DATASOURCES are tested in DatasetServiceDatasourceManagementTest.java
    // ------------------------------------------------------------------------

    @Override
    public void testCreateDatasource() throws Exception {
        // In DatasetServiceDatasourceManagementTest.java
    }

    @Override
    public void testUpdateDatasource() throws Exception {
        // In DatasetServiceDatasourceManagementTest.java
    }

    @Override
    public void testRetrieveDatasourceByUrn() throws Exception {
        // In DatasetServiceDatasourceManagementTest.java
    }

    @Override
    public void testDeleteDatasource() throws Exception {
        // In DatasetServiceDatasourceManagementTest.java
    }

    @Override
    public void testDeleteDatasourcesNotUsed() throws Exception {
        // In DatasetServiceDatasourceManagementTest.java
    }

    @Override
    public void testRetrieveDatasourcesByDatasetVersion() throws Exception {
        // In DatasetServiceDatasourceManagementTest.java
    }

    // ------------------------------------------------------------------------
    // ATTRIBUTES
    // ------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateAttributeInstance() throws Exception {
        // TODO: Implement (METAMAC-2143)

    }

    @Override
    @Test
    public void testUpdateAttributeInstance() throws Exception {
        // TODO: Implement (METAMAC-2143)

    }

    @Override
    @Test
    public void testDeleteAttributeInstance() throws Exception {
        // TODO: Implement (METAMAC-2143)

    }

    @Override
    @Test
    public void testRetrieveAttributeInstances() throws Exception {
        // TODO: Implement (METAMAC-2143)

    }

    @Override
    @Test
    public void testRetrieveCoverageForDatasetVersionAttribute() throws Exception {
        // TODO: Implement (METAMAC-2143)

    }

    // ------------------------------------------------------------------------
    // CATEGORISATIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, CATEGORISATION_SEQUENCE_NAME, CATEGORISATION_MAINTAINER_NAME})
    public void testInitializeCategorisationMetadataForCreation() throws Exception {
        String categoryCode = "category01";
        String maintainerCode = "agency01"; // nested = ISTAC.agency01
        Categorisation categorisation = notPersistedDoMocks.mockCategorisation(maintainerCode, categoryCode);

        assertNull(categorisation.getVersionableStatisticalResource().getCode());
        assertNull(categorisation.getVersionableStatisticalResource().getVersionLogic());
        assertNull(categorisation.getVersionableStatisticalResource().getUrn());
        assertNull(categorisation.getVersionableStatisticalResource().getTitle());

        datasetService.initializeCategorisationMetadataForCreation(getServiceContextAdministrador(), categorisation);

        // Validation
        assertEquals("cat_data_101", categorisation.getVersionableStatisticalResource().getCode());
        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, categorisation.getVersionableStatisticalResource().getVersionLogic());
        assertEquals(buildCategorisationUrn(categorisation.getMaintainer().getCodeNested(), "cat_data_101", StatisticalResourcesMockFactory.INIT_VERSION),
                categorisation.getVersionableStatisticalResource().getUrn());
        assertEquals("Categoría cat_data_101", categorisation.getVersionableStatisticalResource().getTitle().getLocalisedLabel("es"));
        assertEquals("Category cat_data_101", categorisation.getVersionableStatisticalResource().getTitle().getLocalisedLabel("en"));
        assertEquals("Categoria cat_data_101", categorisation.getVersionableStatisticalResource().getTitle().getLocalisedLabel("pt"));
    }

    @Override
    public void testCreateCategorisation() throws Exception {
        // Tested in other testCreateCategorisation* methods
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, CATEGORISATION_SEQUENCE_NAME, CATEGORISATION_MAINTAINER_NAME})
    public void testCreateCategorisationDatasetNotPublished() throws Exception {

        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        String categoryCode = "category01";
        String maintainerCode = "agency01"; // nested = ISTAC.agency01
        Categorisation expected = notPersistedDoMocks.mockCategorisation(maintainerCode, categoryCode);

        Categorisation actual = datasetService.createCategorisation(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), expected);

        // Validate
        assertEqualsCategorisation(expected, actual);
        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getVersionableStatisticalResource().getVersionLogic());
        assertEquals("cat_data_101", actual.getVersionableStatisticalResource().getCode());
        assertEquals(buildCategorisationUrn(expected.getMaintainer().getCodeNested(), "cat_data_101", StatisticalResourcesMockFactory.INIT_VERSION),
                actual.getVersionableStatisticalResource().getUrn());
        assertEquals("Categoría cat_data_101", actual.getVersionableStatisticalResource().getTitle().getLocalisedLabel("es"));
        assertNotNull(actual.getVersionableStatisticalResource().getCreatedDate());
        assertNotNull(actual.getVersionableStatisticalResource().getCreatedBy());
        assertNull(actual.getVersionableStatisticalResource().getValidFrom());
        assertNull(actual.getVersionableStatisticalResource().getValidTo());
        assertNull(actual.getValidFromEffective());
        assertNull(actual.getValidToEffective());
    }

    @Test
    @MetamacMock({DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME, CATEGORISATION_SEQUENCE_NAME, CATEGORISATION_MAINTAINER_NAME})
    public void testCreateCategorisationDatasetAlreadyPublished() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME);
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String categoryCode = "category01";
        String maintainerCode = "agency01"; // nested = ISTAC.agency01
        Categorisation expected = notPersistedDoMocks.mockCategorisation(maintainerCode, categoryCode);

        mockFindPublishedCategory(expected.getCategory().getUrn());
        mockFindPublishedAgency(expected.getMaintainer().getUrn());
        Categorisation actual = datasetService.createCategorisation(getServiceContextAdministrador(), datasetVersionUrn, expected);

        // Validate
        assertEqualsCategorisation(expected, actual);
        MetamacAsserts.assertEqualsDay(new DateTime(), actual.getVersionableStatisticalResource().getValidFrom());
        assertNull(actual.getVersionableStatisticalResource().getValidTo());
        assertFalse(datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom().equals(actual.getValidFromEffective()));
    }

    @Test
    @MetamacMock({DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME, CATEGORISATION_SEQUENCE_NAME, CATEGORISATION_MAINTAINER_NAME})
    public void testCreateCategorisationDatasetAlreadyPublishedErrorCategoryNotPublished() throws Exception {

        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME);
        Categorisation categorisation = notPersistedDoMocks.mockCategorisation("agency01", "category01");
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockFindNotPublishedCategory(categorisation.getCategory().getUrn());
        mockFindPublishedAgency(categorisation.getMaintainer().getUrn());

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.EXTERNAL_ITEM_NOT_PUBLISHED, ServiceExceptionParameters.DATASET_VERSION__CATEGORISATIONS, categorisation.getCategory().getUrn()));
        datasetService.createCategorisation(getServiceContextAdministrador(), datasetVersionUrn, categorisation);
    }

    @Test
    @MetamacMock({DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME, CATEGORISATION_SEQUENCE_NAME, CATEGORISATION_MAINTAINER_NAME})
    public void testCreateCategorisationDatasetAlreadyPublishedErrorMaintainerNotPublished() throws Exception {

        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_22_V1_PUBLISHED_FOR_DATASET_05_NAME);
        Categorisation categorisation = notPersistedDoMocks.mockCategorisation("agency01", "category01");
        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockFindPublishedCategory(categorisation.getCategory().getUrn());
        mockFindNotPublishedAgency(categorisation.getMaintainer().getUrn());

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.EXTERNAL_ITEM_NOT_PUBLISHED, ServiceExceptionParameters.DATASET_VERSION__CATEGORISATIONS, categorisation.getMaintainer().getUrn()));
        datasetService.createCategorisation(getServiceContextAdministrador(), datasetVersionUrn, categorisation);
    }

    @Test
    public void testCheckCategorisationValidFromEffective() throws Exception {
        DatasetVersion datasetVersion = new DatasetVersion();
        datasetVersion.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());

        Categorisation categorisation = new Categorisation();
        categorisation.setVersionableStatisticalResource(new VersionableStatisticalResource());
        categorisation.setDatasetVersion(datasetVersion);

        assertNull(datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom());
        assertNull(datasetVersion.getSiemacMetadataStatisticalResource().getValidTo());
        assertNull(categorisation.getVersionableStatisticalResource().getValidFrom());
        assertNull(categorisation.getVersionableStatisticalResource().getValidTo());
        assertNull(categorisation.getValidFromEffective());
        assertNull(categorisation.getValidToEffective());

        datasetVersion.getSiemacMetadataStatisticalResource().setValidFrom(new DateTime());
        MetamacAsserts.assertEqualsDay(new DateTime(), datasetVersion.getSiemacMetadataStatisticalResource().getValidFrom());
        assertNull(datasetVersion.getSiemacMetadataStatisticalResource().getValidTo());
        assertNull(categorisation.getVersionableStatisticalResource().getValidFrom()); // inherited from dataset
        assertNull(categorisation.getVersionableStatisticalResource().getValidTo());
        MetamacAsserts.assertEqualsDay(new DateTime(), categorisation.getValidFromEffective());
        assertNull(categorisation.getValidToEffective());
    }

    @Override
    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_01_NAME})
    public void testRetrieveCategorisationByUrn() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        Categorisation actual = datasetService.retrieveCategorisationByUrn(getServiceContextWithoutPrincipal(), urn);

        assertEqualsCategorisation(expected, actual);
    }

    @Test
    public void testRetrieveCategorisationByUrnErrorNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CATEGORISATION_NOT_FOUND, URN_NOT_EXISTS));
        datasetService.retrieveCategorisationByUrn(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME, CATEGORISATION_01_DATASET_VERSION_01_NAME, CATEGORISATION_02_DATASET_VERSION_01_NAME,
            CATEGORISATION_01_DATASET_VERSION_02_NAME, CATEGORISATION_02_DATASET_VERSION_02_NAME, CATEGORISATION_03_DATASET_VERSION_02_NAME})
    public void testRetrieveCategorisationsByDatasetVersion() throws Exception {
        DatasetVersion datasetVersion1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersion datasetVersion2 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_02_BASIC_NAME);
        Categorisation categorisation1Dataset1 = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        Categorisation categorisation2Dataset1 = categorisationMockFactory.retrieveMock(CATEGORISATION_02_DATASET_VERSION_01_NAME);
        Categorisation categorisation1Dataset2 = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_02_NAME);
        Categorisation categorisation2Dataset2 = categorisationMockFactory.retrieveMock(CATEGORISATION_02_DATASET_VERSION_02_NAME);
        Categorisation categorisation3Dataset2 = categorisationMockFactory.retrieveMock(CATEGORISATION_03_DATASET_VERSION_02_NAME);

        {
            List<Categorisation> categorisations = datasetService.retrieveCategorisationsByDatasetVersion(getServiceContextWithoutPrincipal(),
                    datasetVersion1.getSiemacMetadataStatisticalResource().getUrn());
            assertEquals(2, categorisations.size());
            assertEquals(categorisation1Dataset1.getVersionableStatisticalResource().getUrn(), categorisations.get(0).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation2Dataset1.getVersionableStatisticalResource().getUrn(), categorisations.get(1).getVersionableStatisticalResource().getUrn());
        }
        {
            List<Categorisation> categorisations = datasetService.retrieveCategorisationsByDatasetVersion(getServiceContextWithoutPrincipal(),
                    datasetVersion2.getSiemacMetadataStatisticalResource().getUrn());
            assertEquals(3, categorisations.size());
            assertEquals(categorisation1Dataset2.getVersionableStatisticalResource().getUrn(), categorisations.get(0).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation2Dataset2.getVersionableStatisticalResource().getUrn(), categorisations.get(1).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation3Dataset2.getVersionableStatisticalResource().getUrn(), categorisations.get(2).getVersionableStatisticalResource().getUrn());
        }
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME, CATEGORISATION_01_DATASET_VERSION_01_NAME, CATEGORISATION_02_DATASET_VERSION_01_NAME,
            CATEGORISATION_01_DATASET_VERSION_02_NAME, CATEGORISATION_02_DATASET_VERSION_02_NAME, CATEGORISATION_03_DATASET_VERSION_02_NAME})
    public void testFindCategorisationsByCondition() throws Exception {
        DatasetVersion datasetVersion1 = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        Categorisation categorisation1Dataset1 = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        Categorisation categorisation2Dataset1 = categorisationMockFactory.retrieveMock(CATEGORISATION_02_DATASET_VERSION_01_NAME);
        Categorisation categorisation1Dataset2 = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_02_NAME);
        Categorisation categorisation2Dataset2 = categorisationMockFactory.retrieveMock(CATEGORISATION_02_DATASET_VERSION_02_NAME);
        Categorisation categorisation3Dataset2 = categorisationMockFactory.retrieveMock(CATEGORISATION_03_DATASET_VERSION_02_NAME);

        {
            // Find all
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Categorisation.class).orderBy(CategorisationProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = null;
            PagedResult<Categorisation> categorisationPagedResult = datasetService.findCategorisationsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
            assertEquals(5, categorisationPagedResult.getTotalRows());
            assertEquals(categorisation1Dataset1.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(0).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation2Dataset1.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(1).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation1Dataset2.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(2).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation2Dataset2.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(3).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation3Dataset2.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(4).getVersionableStatisticalResource().getUrn());
        }
        {
            // Find by dataset

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Categorisation.class)
                    .withProperty(CategorisationProperties.datasetVersion().siemacMetadataStatisticalResource().urn()).eq(datasetVersion1.getSiemacMetadataStatisticalResource().getUrn())
                    .orderBy(CategorisationProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<Categorisation> categorisationPagedResult = datasetService.findCategorisationsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
            assertEquals(2, categorisationPagedResult.getTotalRows());
            assertEquals(categorisation1Dataset1.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(0).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation2Dataset1.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(1).getVersionableStatisticalResource().getUrn());
        }
        {
            // Find by category

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Categorisation.class).withProperty(CategorisationProperties.category().code()).eq("category01")
                    .orderBy(CategorisationProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<Categorisation> categorisationPagedResult = datasetService.findCategorisationsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
            assertEquals(2, categorisationPagedResult.getTotalRows());
            assertEquals(categorisation1Dataset1.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(0).getVersionableStatisticalResource().getUrn());
            assertEquals(categorisation1Dataset2.getVersionableStatisticalResource().getUrn(), categorisationPagedResult.getValues().get(1).getVersionableStatisticalResource().getUrn());
        }
    }

    @Override
    public void testUpdateGeographicCoverageFromSpatialAttribute() throws Exception {
        // no test
    }

    @Override
    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_01_NAME})
    public void testDeleteCategorisation() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();

        // Delete
        datasetService.deleteCategorisation(getServiceContextWithoutPrincipal(), urn);

        expectedMetamacException(new MetamacException(ServiceExceptionType.CATEGORISATION_NOT_FOUND, urn));
        datasetService.retrieveCategorisationByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME})
    public void testDeleteCategorisationErrorDatasetPublished() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, expected.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));
        datasetService.deleteCategorisation(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    public void testDeleteCategorisationErrorNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CATEGORISATION_NOT_FOUND, URN_NOT_EXISTS));
        datasetService.deleteCategorisation(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME})
    public void testEndCategorisationValidity() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        assertNull(expected.getVersionableStatisticalResource().getValidTo());
        assertNull(expected.getValidToEffective());

        Categorisation actual = datasetService.endCategorisationValidity(getServiceContextWithoutPrincipal(), urn, null);
        assertNotNull(actual.getVersionableStatisticalResource().getValidTo());
        assertEquals(actual.getVersionableStatisticalResource().getValidTo(), actual.getValidToEffective());
    }

    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME})
    public void testEndCategorisationValidityWithValidTo() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        assertNull(expected.getVersionableStatisticalResource().getValidTo());
        assertNull(expected.getValidToEffective());

        DateTime validTo = new DateTime().plusDays(3);
        Categorisation actual = datasetService.endCategorisationValidity(getServiceContextWithoutPrincipal(), urn, validTo);
        MetamacAsserts.assertEqualsDate(validTo, actual.getVersionableStatisticalResource().getValidTo());
        assertEquals(actual.getVersionableStatisticalResource().getValidTo(), actual.getValidToEffective());
    }

    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_01_NAME})
    public void testEndCategorisationValidityErrorValidityNotStarted() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_01_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CATEGORISATION_CANT_END_VALIDITY_WITHOUT_VALIDITY_STARTED, urn));
        datasetService.endCategorisationValidity(getServiceContextWithoutPrincipal(), urn, null);
    }

    @Test
    @MetamacMock({CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME})
    public void testEndCategorisationValidityErrorEndBeforeStart() throws Exception {
        Categorisation expected = categorisationMockFactory.retrieveMock(CATEGORISATION_01_DATASET_VERSION_03_PUBLISHED_NAME);
        String urn = expected.getVersionableStatisticalResource().getUrn();
        assertNull(expected.getVersionableStatisticalResource().getValidTo());
        assertNull(expected.getValidToEffective());

        expectedMetamacException(new MetamacException(ServiceExceptionType.CATEGORISATION_CANT_END_VALIDITY_BEFORE_VALIDITY_STARTED, urn));

        DateTime validTo = expected.getValidFromEffective().minusSeconds(1);
        datasetService.endCategorisationValidity(getServiceContextWithoutPrincipal(), urn, validTo);
    }

    // ------------------------------------------------------------------------
    // DIMENSION REPRESENTATION MAPPING
    // ------------------------------------------------------------------------

    @Test
    @MetamacMock({DATASET_01_BASIC_NAME})
    @Override
    public void testSaveDimensionRepresentationMapping() throws Exception {

        Map<String, String> mapping = new LinkedHashMap<String, String>();
        mapping.put("DIMENSION01", "urn:codelist");
        mapping.put("DIMENSION02", "urn:conceptScheme");
        mapping.put("DIMENSION03", "urn:codelist");

        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME);

        DimensionRepresentationMapping expected = notPersistedDoMocks.mockDimensionRepresentationMapping(dataset, "filename", mapping);

        DimensionRepresentationMapping actual = datasetService.saveDimensionRepresentationMapping(getServiceContextWithoutPrincipal(), dataset, "filename", mapping);

        assertEqualsDimensionRepresentationMapping(expected, actual);
    }

    @Test
    @Override
    @MetamacMock({DIMENSION_REPRESENTATION_MAPPING_01_DATASET_01_NAME})
    public void testRetrieveDimensionRepresentationMapping() throws Exception {
        DimensionRepresentationMapping expected = dimensionRepresentationMappingMockFactory.retrieveMock(DIMENSION_REPRESENTATION_MAPPING_01_DATASET_01_NAME);
        DimensionRepresentationMapping actual = datasetService.retrieveDimensionRepresentationMapping(getServiceContextAdministrador(),
                expected.getDataset().getIdentifiableStatisticalResource().getUrn(), expected.getDatasourceFilename());
        assertEqualsDimensionRepresentationMapping(expected, actual);
    }

    @Override
    public void testManageDatabaseView() throws Exception {
        // Not tested
    }

    // ------------------------------------------------------------------------
    // PRIVATE UTILS
    // ------------------------------------------------------------------------

    private static String buildCategorisationUrn(String maintainerCode, String code, String versionNumber) {
        StringBuilder strBuilder = new StringBuilder("urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=");
        strBuilder.append(maintainerCode).append(":").append(code).append("(").append(versionNumber).append(")");
        return strBuilder.toString();
    }

    private URL buildURLForFile(String filename) throws Exception {
        return new URL("file", null, filename);
    }

    private ExternalItem generateOtherDsdVersion(ExternalItem relatedDsd) {
        ExternalItem dsd = StatisticalResourcesDoMocks.mockDsdExternalItem();
        dsd.setCode(relatedDsd.getCode());
        dsd.setUrn(StatisticalResourcesDoMocks.mockDsdUrn(relatedDsd.getCode(), "02.035"));
        assertFalse(dsd.getUrn().equals(relatedDsd.getUrn()));
        return dsd;
    }

    private void mockDsdAndDataRepositorySimpleDimensions() throws Exception {
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
    }

    private void mockTaskInProgressForResource(String datasetUrn, boolean status) throws MetamacException {
        TaskMockUtils.mockTaskInProgressForDatasetVersion(taskService, datasetUrn, status);
    }

    private void mockAllTaskInProgressForResource(boolean status) throws MetamacException {
        TaskMockUtils.mockAllTaskInProgressForDatasetVersion(taskService, status);
    }

    private void mockFindPublishedCategory(String urn) throws MetamacException {
        Mockito.when(srmRestInternalService.findCategoriesAsUrnsList(anyString())).thenReturn(Arrays.asList(urn));
    }

    private void mockFindNotPublishedCategory(String urn) throws MetamacException {
        Mockito.when(srmRestInternalService.findCategoriesAsUrnsList(anyString())).thenReturn(new ArrayList<String>());
    }

    private void mockFindPublishedAgency(String urn) throws MetamacException {
        Mockito.when(srmRestInternalService.findOrganisationsAsUrnsList(anyString())).thenReturn(Arrays.asList(urn));
    }

    private void mockFindNotPublishedAgency(String urn) throws MetamacException {
        Mockito.when(srmRestInternalService.findOrganisationsAsUrnsList(anyString())).thenReturn(new ArrayList<String>());
    }

    private BasicVersionableStatisticalResourceDto getMockBasicVersionableStatisticalResourceDto() {
        BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto = new BasicVersionableStatisticalResourceDto();
        basicVersionableStatisticalResourceDto.setAutomaticLifeCicle(true);
        List<String> versionRationaleTypes = new ArrayList<>();
        versionRationaleTypes.add(VersionRationaleTypeEnum.MINOR_DATA_UPDATE.getName());
        basicVersionableStatisticalResourceDto.setVersionRationaleTypes(versionRationaleTypes);
        basicVersionableStatisticalResourceDto.setNextVersion(NextVersionTypeEnum.SCHEDULED_UPDATE.getName());
        basicVersionableStatisticalResourceDto.setNextProcStatus(ProcStatusEnum.PUBLISHED.getName());
        basicVersionableStatisticalResourceDto.setNextVersionDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().toDate()));
        basicVersionableStatisticalResourceDto.setNextUpdateDate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime().toDate()));

        return basicVersionableStatisticalResourceDto;
    }

    @Override
    public void testImportAttributesFromFile() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME);
        List<URL> urls = Arrays.asList(new File("prueba.px").toURI().toURL());
        datasetService.importAttributesFromFile(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), urls);
    }

    @Override
    @Test
    public void testRetrievePublishedLastVersionDatasets() throws Exception {
        // NOTHING TO DO
    }

    private static final String GEOCODELIST_TEST_URN = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=TEST:codelist-01(1.0)";

    @Override
    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testCalculateTableSize() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        String urn = buildDatasetUrn(datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), statisticalOperation.getCode(), 1,
                StatisticalResourcesMockFactory.INIT_VERSION);
        DataMockUtils.mockDsdAndCreateDatasetRepository(datasetRepositoriesServiceFacade, srmRestInternalService, urn);

        Map<String, List<String>> coverageMap = new HashMap<String, List<String>>();
        coverageMap.put("GEO_DIM", Arrays.asList("code-01", "code-02", "code-03"));
        coverageMap.put("TIME_PERIOD", Arrays.asList("2010", "2011", "2012"));
        coverageMap.put("MEAS_DIM", Arrays.asList("concept-01", "concept-02", "concept-03"));
        Mockito.when(datasetRepositoriesServiceFacade.findCodeDimensions(Mockito.anyString())).thenReturn(coverageMap);

        Long tableSize = datasetService.calculateTableSize(getServiceContextAdministrador(), datasetVersion);
        assertEquals(Long.valueOf(27), tableSize);
    }

    @Override
    public void testGetDatasetLastVersionPublishedByDatasetUrn() throws Exception {
        // NOTHING TO DO
    }

    @Override
    @Test
    @MetamacMock({DATASET_VERSION_01_FOR_UPDATE_MULTIPLE, DATASET_VERSION_02_FOR_UPDATE_MULTIPLE})
    public void testUpdateDatasetVersionInGroup() throws Exception {
        // datasetVersion to change
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_FOR_UPDATE_MULTIPLE);
        String urn = expected.getSiemacMetadataStatisticalResource().getUrn();

        // metadata changes
        DatasetVersion metadataToChange = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_02_FOR_UPDATE_MULTIPLE);
        DatasetLifecycleTestUtils.fillGranularitiesAlternative(metadataToChange);
        metadataToChange.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_1", "category01", metadataToChange));
        metadataToChange.addCategorisation(CategorisationMockFactory.createCategorisation("cat_data_2", "category02", metadataToChange));

        // to change all permitted metadata
        datasetService.updateDatasetVersionInGroup(getServiceContextWithoutPrincipal(), metadataToChange, urn);
        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsDatasetVersionInGroup(metadataToChange, actual);

    }

    @Test
    @MetamacMock(DATASET_VERSION_01_FOR_UPDATE_MULTIPLE)
    public void testUpdateDatasetVersionInGroupNotPremittedMetadata() throws Exception {
        DatasetVersion expected = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_FOR_UPDATE_MULTIPLE);
        DatasetVersion metadataToChange = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_FOR_UPDATE_MULTIPLE);
        DatasetVersionMockFactory.changeDatasetVersionBasicMetadataNotPermitted(metadataToChange);

        String urn = expected.getSiemacMetadataStatisticalResource().getUrn();

        datasetService.updateDatasetVersionInGroup(getServiceContextWithoutPrincipal(), metadataToChange, urn);
        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsDatasetVersion(expected, actual);
    }

    @Override
    @Test
    public void testExportDatasourcesTsv() throws Exception {
        // NOTHING TO DO
    }

    @Override
    public void testCopyDatasetVersion() throws Exception {
        // NOTHING TO DO
    }

    public void testUpdateGeographicCoverageVariableElementsCache() throws Exception {
        // NOTHING TO DO

    }

    @Override
    public void testUpdateAllGeographicCoverageVariableElementsCache() throws Exception {
        // NOTHING TO DO

    }

    @Override
    public void testUpdateGeographicCoverageExternalPublicationCache() throws Exception {
        // NOTHING TO DO
    }
    
    @Override
    @Test
    @MetamacMock(DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME)
    public void testExportAttributesTsv() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_118_PUBLISHED_DATABASE_DATASET_NAME);
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        // Set a valid datasetRepositoryId (override mock value if needed)
        datasetVersion.setDatasetRepositoryId(urn);
        datasetVersionRepository.save(datasetVersion);

        // Mock DSD and dataset repository with attributes
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsWithObservationAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);

        String fileName = datasetService.exportAttributesTsv(getServiceContextWithoutPrincipal(), urn);

        assertNotNull("El nombre del archivo exportado no debe ser null", fileName);
        assertFalse("El nombre del archivo exportado no debe estar vacío", fileName.isEmpty());
        assertTrue("El nombre del archivo debe terminar en .tsv", fileName.endsWith(".tsv"));

        // Verify the file was created
        File exportedFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        assertTrue("El archivo exportado debe existir", exportedFile.exists());
        assertTrue("El archivo exportado debe tener contenido", exportedFile.length() > 0);

        // Clean up
        exportedFile.delete();
    }
}
