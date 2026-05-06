package org.siemac.metamac.statistical.resources.core.multidataset.serviceapi;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetVersionCollection;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidatasetVersionNotChecksMultidataset;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertRelaxedEqualsMultidatasetCube;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_11_OPERATION_0002_CODE_MAX_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_12_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_16_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_92_IS_REPLACED_BY_MULTIDATASET_91_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetVersionMockFactory.MULTIDATASET_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_01_SIMPLE_NAME;

import java.util.Arrays;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.constants.ProcStatusForActionsConstants;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetProperties;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesNotPersistedDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class MultidatasetServiceTest extends StatisticalResourcesBaseTest implements MultidatasetServiceTestBase {

    @Autowired
    protected MultidatasetService            multidatasetService;

    @Autowired
    @Qualifier("txManager")
    private final PlatformTransactionManager transactionManager = null;

    // ------------------------------------------------------------------------
    // MULTIDATASETS
    // ------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateMultidatasetVersion() throws Exception {
        MultidatasetVersion expected = notPersistedDoMocks.mockMultidatasetVersion();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        MultidatasetVersion actual = multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        String operationCode = actual.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(operationCode + "_000001", actual.getSiemacMetadataStatisticalResource().getCode());
        assertEquals(buildMultidatasetVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 1,
                expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());

        assertEqualsMultidatasetVersionNotChecksMultidataset(expected, actual);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME)
    public void testCreateMultidatasetVersionInSameOperationThatAlreadyExistsMultidatasets() throws Exception {
        MultidatasetVersion multidatasetVersionOperation01 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        String operationCode = multidatasetVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        MultidatasetVersion expected = notPersistedDoMocks.mockMultidatasetVersion();

        MultidatasetVersion actual = multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(operationCode + "_000004", actual.getSiemacMetadataStatisticalResource().getCode());
        assertEquals(buildMultidatasetVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 4,
                expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());

        assertEqualsMultidatasetVersionNotChecksMultidataset(expected, actual);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME)
    public void testCreateConsecutivesMultidatasetVersionInSameOperationThatAlreadyExistsMultidatasets() throws Exception {
        MultidatasetVersion multidatasetVersionOperation01 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        String operationCode = multidatasetVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        {
            ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
            MultidatasetVersion expected = notPersistedDoMocks.mockMultidatasetVersion();
            MultidatasetVersion actual = multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);

            assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
            assertEquals(operationCode + "_000004", actual.getSiemacMetadataStatisticalResource().getCode());
            assertEquals(buildMultidatasetVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 4,
                    expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());
            assertEqualsMultidatasetVersionNotChecksMultidataset(expected, actual);
        }
        {
            ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
            MultidatasetVersion expected = notPersistedDoMocks.mockMultidatasetVersion();
            MultidatasetVersion actual = multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);

            assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
            assertEquals(operationCode + "_000005", actual.getSiemacMetadataStatisticalResource().getCode());
            assertEquals(buildMultidatasetVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 5,
                    expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());
            assertEqualsMultidatasetVersionNotChecksMultidataset(expected, actual);
        }
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_11_OPERATION_0002_CODE_MAX_NAME)
    public void testCreateMultidatasetVersionMaxCodeReached() throws Exception {
        MultidatasetVersion multidatasetVersionOperation01 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_11_OPERATION_0002_CODE_MAX_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_MAX_REACHED_IN_OPERATION,
                multidatasetVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn()));

        String operationCode = multidatasetVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), notPersistedDoMocks.mockMultidatasetVersion(), statisticalOperation);
    }

    @Test
    public void testCreateMultidatasetVersionErrorParameterMultidatasetRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_VERSION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), null, statisticalOperation);
    }

    @Test
    public void testCreateMultidatasetVersionErrorMetadataSiemacStatisticalResourceRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_VERSION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        MultidatasetVersion expected = notPersistedDoMocks.mockMultidatasetVersionWithNullableSiemacStatisticalResource();
        multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
    }

    @Test
    public void testCreateDatasetVersionErrorParameterStatisticalOperationRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.STATISTICAL_OPERATION));

        MultidatasetVersion expected = notPersistedDoMocks.mockMultidatasetVersion();
        multidatasetService.createMultidatasetVersion(getServiceContextWithoutPrincipal(), expected, null);
    }

    @Override
    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_01_BASIC_NAME, MULTIDATASET_VERSION_02_BASIC_NAME})
    public void testUpdateMultidatasetVersion() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        expected.getSiemacMetadataStatisticalResource().setDescription(notPersistedDoMocks.mockInternationalString());

        MultidatasetVersion actual = multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testUpdateMultidatasetVersionErrorFinal() throws Exception {
        MultidatasetVersion finalMultidataset = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, finalMultidataset.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), finalMultidataset);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testUpdateMultidatasetVersionErrorIncorrectCode() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MULTIDATASET_VERSION__CODE));

        MultidatasetVersion multidataset = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME);
        multidataset.getSiemacMetadataStatisticalResource().setCode("@12345");
        multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), multidataset);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testUpdateMultidatasetVersionDraftProcStatus() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_12_DRAFT_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        MultidatasetVersion actual = multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testUpdateMultidatasetVersionProductionValidationProcStatus() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        MultidatasetVersion actual = multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testUpdateMultidatasetVersionDiffusionValidationProcStatus() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        MultidatasetVersion actual = multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testUpdateMultidatasetVersionValidationRejectedProcStatus() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        MultidatasetVersion actual = multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsMultidatasetVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testUpdateMultidatasetVersionPublishedProcStatus() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, expected.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME)
    public void testUpdateMultidatasetVersionProcStatusPublishedNotVisible() throws Exception {
        MultidatasetVersion expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME);
        String urn = expected.getSiemacMetadataStatisticalResource().getUrn();
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_RESOURCE));
        multidatasetService.updateMultidatasetVersion(getServiceContextWithoutPrincipal(), expected);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveMultidatasetVersionByUrn() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersion actual = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME), actual);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLatestMultidatasetVersionByMultidatasetUrn() throws Exception {
        String urn = multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion actual = multidatasetService.retrieveLatestMultidatasetVersionByMultidatasetUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLatestMultidatasetVersionByMultidatasetUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_URN));
        multidatasetService.retrieveLatestMultidatasetVersionByMultidatasetUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLatestPublishedMultidatasetVersionByMultidatasetUrn() throws Exception {
        String urn = multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        MultidatasetVersion actual = multidatasetService.retrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME), actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveLatestPublishedMultidatasetVersionByMultidatasetUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_URN));
        multidatasetService.retrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveMultidatasetVersionByUrnV2() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetVersion actual = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsMultidatasetVersion(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    public void testRetrieveMultidatasetVersionByUrnErrorParameterRequiered() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_VERSION_URN));

        multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testRetrieveMultidatasetVersionByUrnErrorNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testRetrieveMultidatasetVersions() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        List<MultidatasetVersion> actual = multidatasetService.retrieveMultidatasetVersions(getServiceContextWithoutPrincipal(), urn);

        assertEquals(2, actual.size());
        assertEqualsMultidatasetVersionCollection(multidatasetMockFactory.retrieveMock(MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME).getVersions(), actual);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testFindMultidatasetVersionsByCondition() throws Exception {

        {
            // Find by version number
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(MultidatasetVersion.class)
                    .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().versionLogic()).eq(StatisticalResourcesMockFactory.SECOND_VERSION)
                    .orderBy(MultidatasetVersionProperties.siemacMetadataStatisticalResource().id()).ascending().build();

            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<MultidatasetVersion> multidatasetVersionPagedResult = multidatasetService.findMultidatasetVersionsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
            assertEquals(1, multidatasetVersionPagedResult.getTotalRows());
            assertEquals(multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                    multidatasetVersionPagedResult.getValues().get(0).getSiemacMetadataStatisticalResource().getUrn());
        }
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_VERSION_01_BASIC_NAME})
    public void testDeleteMultidatasetVersion() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, urn));

        // Delete multidataset version
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);

        // Validation
        multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_92_IS_REPLACED_BY_MULTIDATASET_91_NAME)
    public void testDeleteMultidatasetVersionIsReplacedBy() throws Exception {
        String urnReplaces = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_92_IS_REPLACED_BY_MULTIDATASET_91_NAME).getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.MULTIDATASET_VERSION_IS_REPLACED_BY_OTHER_RESOURCE, urnReplaces);
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete multidataset version
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92_NAME)
    public void testDeleteMultidatasetVersionReplaces() throws Exception {
        String urnReplaces = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_91_REPLACES_MULTIDATASET_92_NAME).getSiemacMetadataStatisticalResource().getUrn();

        // Delete multidataset version
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urnReplaces);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testDeleteMultidatasetVersionWithTwoVersions() throws Exception {
        String urnV1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String urnV2 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_04_FOR_MULTIDATASET_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        // Delete multidataset version
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urnV2);

        // Validation
        MultidatasetVersion multidatasetVersionV1 = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), urnV1);

        // is replaced_by_version null
        assertNull(multidatasetVersionV1.getLifeCycleStatisticalResource().getIsReplacedByVersion());
        // Now is last version
        assertTrue(multidatasetVersionV1.getSiemacMetadataStatisticalResource().getLastVersion());

        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, urnV2));
        multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextWithoutPrincipal(), urnV2);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testDeleteMultidatasetVersionErrorNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock({MULTIDATASET_03_BASIC_WITH_2_MULTIDATASET_VERSIONS_NAME})
    public void testDeleteMultidatasetVersionErrorNoLastVersion() throws Exception {
        String urnV1 = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_03_FOR_MULTIDATASET_03_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urnV1, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urnV1);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testDeleteMultidatasetVersionDraftProcStatus() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_12_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testDeleteMultidatasetVersionProductionValidationProcStatus() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testDeleteMultidatasetVersionDiffusionValidationProcStatus() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testDeleteMultidatasetVersionValidationRejectedProcStatus() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME,
            MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, MULTIDATASET_VERSION_16_PUBLISHED_NAME})
    public void testDeleteMultidatasetVersionPublishedProcStatus() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME)
    public void testDeleteMultidatasetVersionProcStatusPublishedNotVisible() throws Exception {
        String urn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));
        multidatasetService.deleteMultidatasetVersion(getServiceContextWithoutPrincipal(), urn);
    }

    private Object buildMultidatasetVersionUrn(String maintainerCode, String operationCode, int datasetSequentialId, String versionNumber) {
        StringBuilder strBuilder = new StringBuilder("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Multidataset=");
        strBuilder.append(maintainerCode).append(":").append(operationCode).append("_").append(String.format("%06d", datasetSequentialId)).append("(").append(versionNumber).append(")");
        return strBuilder.toString();
    }

    // ------------------------------------------------------------------------
    // MULTIDATASET_CUBES
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateMultidatasetCube() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        MultidatasetCube actual = multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);

        assertRelaxedEqualsMultidatasetCube(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testUpdateLastUpdateOnCreateMultidatasetCube() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        MultidatasetVersion multidatasetVersionOld = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String multidatasetVersionUrn = multidatasetVersionOld.getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        MultidatasetCube actual = multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);

        assertRelaxedEqualsMultidatasetCube(expected, actual);

        MultidatasetVersion updatedMultidatasetVersion = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(multidatasetVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateMultidatasetCubeDataset() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        MultidatasetCube actual = multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);

        assertRelaxedEqualsMultidatasetCube(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeQuery() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        MultidatasetCube actual = multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);

        assertRelaxedEqualsMultidatasetCube(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateMultidatasetCubeErrorParameterRequiredMultidatasetVersionUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_VERSION_URN));
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), null, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataUnexpectedDatasetOrQuery() throws Exception {
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.MULTIDATASET_CUBE__DATASET + " / " + ServiceExceptionParameters.MULTIDATASET_CUBE__QUERY));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        expected.setQuery(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataRequiredDatasetOrQuery() throws Exception {
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__DATASET + " / " + ServiceExceptionParameters.MULTIDATASET_CUBE__QUERY));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        expected.setQuery(null);
        expected.setDataset(null);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataRequiredDatasetUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__DATASET__URN));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        expected.getDataset().getIdentifiableStatisticalResource().setUrn(null);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataRequiredQueryUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__QUERY__URN));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        expected.getQuery().getIdentifiableStatisticalResource().setUrn(null);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMultidatasetVersionNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), URN_NOT_EXISTS, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataIncorrectOrderInLevelNegative() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        expected.setOrderInMultidataset(Long.valueOf(-1));

        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataIncorrectOrderInLevelMaxValue() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        expected.setOrderInMultidataset(Long.MAX_VALUE);

        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeErrorMetadataRequiredOrderInLevel() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET));

        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        expected.setOrderInMultidataset(null);

        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateMultidatasetCubeErrorMetadataRequiredIdentifier() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__IDENTIFIER));

        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(dataset);
        expected.setIdentifier(null);

        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateMultidatasetCubeErrorDuplicateIdentifier() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        MultidatasetCube multidatasetCubeExist = multidatasetVersion.getCubes().get(3);
        MultidatasetCube multidatasetCubeToCreate = notPersistedDoMocks.mockMultidatasetCube(dataset);

        multidatasetCubeToCreate.setIdentifier(multidatasetCubeExist.getIdentifier());

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MULTIDATASET_CUBE_DUPLICATE_IDENTIFIER)
                .withMessageParameters(multidatasetCubeExist.getIdentifier(), multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn()).build());

        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn(), multidatasetCubeToCreate);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_12_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeStatusMultidatasetVersionDraft() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_12_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeStatusMultidatasetVersionProductionValidation() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_13_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeStatusMultidatasetVersionDiffusionValidation() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_14_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeStatusMultidatasetVersionValidationRejected() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_15_VALIDATION_REJECTED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_16_PUBLISHED_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeStatusMultidatasetVersionPublished() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, multidatasetVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_MULTIDATASET_STRUCTURE));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateMultidatasetCubeStatusMultidatasetVersionPublishedNotVisible() throws Exception {
        String multidatasetVersionUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_MULTIDATASET_06_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, multidatasetVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_MULTIDATASET_STRUCTURE));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        MultidatasetCube expected = notPersistedDoMocks.mockMultidatasetCube(query);
        multidatasetService.createMultidatasetCube(getServiceContextAdministrador(), multidatasetVersionUrn, expected);
    }

    @SuppressWarnings("static-access")
    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateMultidatasetCube() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        MultidatasetCube actual = multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsMultidatasetCube(expected, actual);
        CommonAsserts.assertEqualsInternationalString(expected.getNameableStatisticalResource().getTitle(), actual.getNameableStatisticalResource().getTitle());
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateMultidatasetCubeErrorMetadataRequiredIdentifier() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__IDENTIFIER));

        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.setIdentifier(null);

        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testUpdateMultidatasetCubeErrorDuplicateIdentifier() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        MultidatasetCube multidatasetCubeExists = multidatasetVersion.getCubes().get(3);
        MultidatasetCube multidatasetCubeToUpdate = multidatasetVersion.getCubes().get(2);

        multidatasetCubeToUpdate.setIdentifier(multidatasetCubeExists.getIdentifier());

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MULTIDATASET_CUBE_DUPLICATE_IDENTIFIER)
                .withMessageParameters(multidatasetCubeExists.getIdentifier(), multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn()).build());

        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeToUpdate);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateLastUpdateOnUpdateMultidatasetCube() throws Exception {
        MultidatasetVersion multidatasetVersionOld = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        MultidatasetCube expected = multidatasetVersionOld.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        MultidatasetCube actual = multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsMultidatasetCube(expected, actual);
        CommonAsserts.assertEqualsInternationalString(expected.getNameableStatisticalResource().getTitle(), actual.getNameableStatisticalResource().getTitle());

        MultidatasetVersion updatedMultidatasetVersion = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(multidatasetVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateMultidatasetCubeErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE));
        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), null);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateMultidatasetCubeStatusMultidatasetVersionDraft() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME})
    public void testUpdateMultidatasetCubeStatusMultidatasetVersionProductionValidation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME);
        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME})
    public void testUpdateMultidatasetCubeStatusMultidatasetVersionDiffusionValidation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME);
        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME})
    public void testUpdateMultidatasetCubeStatusMultidatasetVersionValidationRejected() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME);
        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME})
    public void testUpdateMultidatasetCubeStatusMultidatasetVersionPublished() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        MultidatasetCube expected = multidatasetVersion.getCubes().get(3);
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        multidatasetService.updateMultidatasetCube(getServiceContextAdministrador(), expected);
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateMultidatasetCubeLocation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        MultidatasetCube multidatasetCube = multidatasetVersion.getCubes().get(3);
        MultidatasetCube updatedMultidatasetCube = multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(),
                Long.valueOf(1));

        assertEquals(Long.valueOf(4), multidatasetCube.getOrderInMultidataset());
        assertEquals(Long.valueOf(1), updatedMultidatasetCube.getOrderInMultidataset());
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateLastUpdateOnUpdateMultidatasetCubeLocation() throws Exception {
        MultidatasetVersion multidatasetVersionOld = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        MultidatasetCube multidatasetCube = multidatasetVersionOld.getCubes().get(3);
        MultidatasetCube updatedMultidatasetCube = multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(),
                Long.valueOf(1));

        assertEquals(Long.valueOf(4), multidatasetCube.getOrderInMultidataset());
        assertEquals(Long.valueOf(1), updatedMultidatasetCube.getOrderInMultidataset());

        MultidatasetVersion updatedMultidatasetVersion = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        MetamacAsserts.assertEqualsDate(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate(),
                multidatasetVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate());
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateMultidatasetCubeLocationStatusMultidatasetVersionDraft() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        MultidatasetCube multidatasetCube = multidatasetVersion.getCubes().get(3);
        multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(), Long.valueOf(1));
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME)
    public void testUpdateMultidatasetCubeLocationStatusMultidatasetVersionProductionValidation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME);

        MultidatasetCube multidatasetCube = multidatasetVersion.getCubes().get(3);
        multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(), Long.valueOf(1));
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME)
    public void testUpdateMultidatasetCubeLocationStatusMultidatasetVersionDiffusionValidation() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME);

        MultidatasetCube multidatasetCube = multidatasetVersion.getCubes().get(3);
        multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(), Long.valueOf(1));
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME)
    public void testUpdateMultidatasetCubeLocationStatusValidationRejected() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME);

        MultidatasetCube multidatasetCube = multidatasetVersion.getCubes().get(3);
        multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(), Long.valueOf(1));
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME)
    public void testUpdateMultidatasetCubeLocationStatusMultidatasetVersionPublished() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        MultidatasetCube multidatasetCube = multidatasetVersion.getCubes().get(3);
        multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCube.getNameableStatisticalResource().getUrn(), Long.valueOf(1));
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateMultidatasetCubeLocationErrorOrderIncorrect() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.MULTIDATASET_CUBE__ORDER_IN_MULTIDATASET));

        String multidatasetCubeUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getCubes().get(3).getNameableStatisticalResource().getUrn();
        multidatasetService.updateMultidatasetCubeLocation(getServiceContextAdministrador(), multidatasetCubeUrn, Long.MAX_VALUE);
    }

    @Override
    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME})
    public void testRetrieveMultidatasetCube() throws Exception {
        MultidatasetCube expected = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getCubes().get(3);
        MultidatasetCube actual = multidatasetService.retrieveMultidatasetCube(getServiceContextAdministrador(), expected.getNameableStatisticalResource().getUrn());
        assertRelaxedEqualsMultidatasetCube(expected, actual);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME})
    public void testRetrieveMultidatasetCubeErrorParameterRequiredMultidatasetCubeUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__URN));
        multidatasetService.retrieveMultidatasetCube(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME})
    public void testRetrieveMultidatasetCubeErrorMultidatasetCubeNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_CUBE_NOT_FOUND, URN_NOT_EXISTS));
        multidatasetService.retrieveMultidatasetCube(getServiceContextAdministrador(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteMultidatasetCube() throws Exception {
        String multidatasetCubeUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getCubes().get(3).getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_CUBE_NOT_FOUND, multidatasetCubeUrn));

        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
        multidatasetService.retrieveMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateLastUpdateOnDeleteMultidatasetCube() throws Exception {
        MultidatasetVersion multidatasetVersionOld = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String multidatasetCubeUrn = multidatasetVersionOld.getCubes().get(3).getNameableStatisticalResource().getUrn();

        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);

        MultidatasetVersion updatedMultidatasetVersion = multidatasetService.retrieveMultidatasetVersionByUrn(getServiceContextAdministrador(),
                multidatasetVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedMultidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(multidatasetVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteMultidatasetCubeErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.MULTIDATASET_CUBE__URN));
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteMultidatasetCubeErrorMultidatasetCubeNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.MULTIDATASET_CUBE_NOT_FOUND, URN_NOT_EXISTS));
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteMultidatasetCubeStatusMultidatasetVersionDraft() throws Exception {
        String multidatasetCubeUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getCubes().get(3).getNameableStatisticalResource().getUrn();
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME)
    public void testDeleteMultidatasetCubeStatusMultidatasetVersionProductionValidation() throws Exception {
        String multidatasetCubeUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME).getCubes().get(3)
                .getNameableStatisticalResource().getUrn();
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME)
    public void testDeleteMultidatasetCubeStatusMultidatasetVersionDiffusionValidation() throws Exception {
        String multidatasetCubeUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME).getCubes().get(3)
                .getNameableStatisticalResource().getUrn();
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME)
    public void testDeleteMultidatasetCubeStatusMultidatasetVersionValidationRejected() throws Exception {
        String multidatasetCubeUrn = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME).getCubes().get(3)
                .getNameableStatisticalResource().getUrn();
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
    }

    @Test
    @MetamacMock(MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME)
    public void testDeleteMultidatasetCubeStatusMultidatasetVersionPublished() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME);
        String multidatasetCubeUrn = multidatasetVersion.getCubes().get(3).getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));
        multidatasetService.deleteMultidatasetCube(getServiceContextAdministrador(), multidatasetCubeUrn);
    }

    @Test
    @MetamacMock({MULTIDATASET_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME})
    public void testDeleteMultidatasetVersionReplacesVersion() throws Exception {
        MultidatasetVersion multidatasetVersion = multidatasetVersionMockFactory.retrieveMock(MULTIDATASET_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME);

        multidatasetService.deleteMultidatasetVersion(getServiceContextAdministrador(), multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    @Test
    @MetamacMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME)
    public void testFindMultidatasetsByCondition() throws Exception {
        Multidataset result = multidatasetMockFactory.retrieveMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Multidataset.class).withProperty(MultidatasetProperties.identifiableStatisticalResource().code())
                .eq(result.getIdentifiableStatisticalResource().getCode()).build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        PagedResult<Multidataset> multidatasets = multidatasetService.findMultidatasetsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(1, multidatasets.getTotalRows());
        assertEquals(result.getIdentifiableStatisticalResource().getUrn(), multidatasets.getValues().get(0).getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    public void testUpdateGeographicalCache() throws Exception {
        // NOTHING TO DO Execution in job. it will be tested in CacheServiceTest.java

    }

    @Override
    public void testUpdateAllGeographicalCache() throws Exception {
        // NOTHING TO DO

    }
}
