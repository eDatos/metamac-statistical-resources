package org.siemac.metamac.statistical.resources.core.publication.serviceapi;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.MultidatasetsAsserts.assertEqualsMultidataset;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersionCollection;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersionNotChecksPublication;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertFilledMetadataForChaptersInAllLevels;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertFilledMetadataForChaptersInFirstLevel;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertFilledMetadataForChaptersInNoFirstLevel;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertFilledMetadataForCubesInAllLevels;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertFilledMetadataForCubesInFirstLevel;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertFilledMetadataForCubesInNoFirstLevel;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertRelaxedEqualsChapter;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertRelaxedEqualsCube;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.MultidatasetMockFactory.MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_11_OPERATION_0002_CODE_MAX_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_12_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_16_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_92_IS_REPLACED_BY_PUBLICATION_91_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_01_SIMPLE_NAME;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.constants.ProcStatusForActionsConstants;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionProperties;
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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class PublicationServiceTest extends StatisticalResourcesBaseTest implements PublicationServiceTestBase {

    @Autowired
    private PublicationService               publicationService;

    @Autowired
    @Qualifier("txManager")
    private final PlatformTransactionManager transactionManager = null;

    // ------------------------------------------------------------------------
    // PUBLICATIONS
    // ------------------------------------------------------------------------

    @Override
    @Test
    public void testCreatePublicationVersion() throws Exception {
        PublicationVersion expected = notPersistedDoMocks.mockPublicationVersion();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();

        PublicationVersion actual = publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        String operationCode = actual.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(operationCode + "_000001", actual.getSiemacMetadataStatisticalResource().getCode());
        assertEquals(buildPublicationVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 1,
                expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());

        assertEqualsPublicationVersionNotChecksPublication(expected, actual);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME)
    public void testCreatePublicationVersionInSameOperationThatAlreadyExistsPublications() throws Exception {
        PublicationVersion publicationVersionOperation01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        String operationCode = publicationVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        PublicationVersion expected = notPersistedDoMocks.mockPublicationVersion();

        PublicationVersion actual = publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
        assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(operationCode + "_000004", actual.getSiemacMetadataStatisticalResource().getCode());
        assertEquals(buildPublicationVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 4,
                expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());

        assertEqualsPublicationVersionNotChecksPublication(expected, actual);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME)
    public void testCreateConsecutivesPublicationVersionInSameOperationThatAlreadyExistsPublications() throws Exception {
        PublicationVersion publicationVersionOperation01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_07_OPERATION_0001_CODE_000003_NAME);
        String operationCode = publicationVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        {
            ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
            PublicationVersion expected = notPersistedDoMocks.mockPublicationVersion();
            PublicationVersion actual = publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);

            assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
            assertEquals(operationCode + "_000004", actual.getSiemacMetadataStatisticalResource().getCode());
            assertEquals(buildPublicationVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 4,
                    expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());
            assertEqualsPublicationVersionNotChecksPublication(expected, actual);
        }
        {
            ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
            PublicationVersion expected = notPersistedDoMocks.mockPublicationVersion();
            PublicationVersion actual = publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);

            assertEquals(StatisticalResourcesMockFactory.INIT_VERSION, actual.getSiemacMetadataStatisticalResource().getVersionLogic());
            assertEquals(operationCode + "_000005", actual.getSiemacMetadataStatisticalResource().getCode());
            assertEquals(buildPublicationVersionUrn(expected.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested(), operationCode, 5,
                    expected.getSiemacMetadataStatisticalResource().getVersionLogic()), actual.getSiemacMetadataStatisticalResource().getUrn());
            assertEqualsPublicationVersionNotChecksPublication(expected, actual);
        }
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_11_OPERATION_0002_CODE_MAX_NAME)
    public void testCreatePublicationVersionMaxCodeReached() throws Exception {
        PublicationVersion publicationVersionOperation01 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_11_OPERATION_0002_CODE_MAX_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_MAX_REACHED_IN_OPERATION,
                publicationVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn()));

        String operationCode = publicationVersionOperation01.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem(operationCode);
        publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), notPersistedDoMocks.mockPublicationVersion(), statisticalOperation);
    }

    @Test
    public void testCreatePublicationVersionErrorParameterPublicationRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PUBLICATION_VERSION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), null, statisticalOperation);
    }

    @Test
    public void testCreatePublicationVersionErrorMetadataSiemacStatisticalResourceRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.PUBLICATION_VERSION));

        ExternalItem statisticalOperation = StatisticalResourcesNotPersistedDoMocks.mockStatisticalOperationExternalItem();
        PublicationVersion expected = notPersistedDoMocks.mockPublicationVersionWithNullableSiemacStatisticalResource();
        publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), expected, statisticalOperation);
    }

    @Test
    public void testCreateDatasetVersionErrorParameterStatisticalOperationRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.STATISTICAL_OPERATION));

        PublicationVersion expected = notPersistedDoMocks.mockPublicationVersion();
        publicationService.createPublicationVersion(getServiceContextWithoutPrincipal(), expected, null);
    }

    @Override
    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testUpdatePublicationVersion() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        expected.getSiemacMetadataStatisticalResource().setDescription(notPersistedDoMocks.mockInternationalString());

        PublicationVersion actual = publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsPublicationVersion(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testUpdatePublicationVersionErrorFinal() throws Exception {
        PublicationVersion finalPublication = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, finalPublication.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), finalPublication);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testUpdatePublicationVersionErrorIncorrectCode() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.PUBLICATION_VERSION__CODE));

        PublicationVersion publication = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME);
        publication.getSiemacMetadataStatisticalResource().setCode("@12345");
        publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), publication);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testUpdatePublicationVersionDraftProcStatus() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        PublicationVersion actual = publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsPublicationVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testUpdatePublicationVersionProductionValidationProcStatus() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        PublicationVersion actual = publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsPublicationVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testUpdatePublicationVersionDiffusionValidationProcStatus() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        PublicationVersion actual = publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsPublicationVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testUpdatePublicationVersionValidationRejectedProcStatus() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME);
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        PublicationVersion actual = publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
        assertEqualsPublicationVersion(expected, actual);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testUpdatePublicationVersionPublishedProcStatus() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_16_PUBLISHED_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, expected.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME)
    public void testUpdatePublicationVersionProcStatusPublishedNotVisible() throws Exception {
        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME);
        String urn = expected.getSiemacMetadataStatisticalResource().getUrn();
        expected.getSiemacMetadataStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_RESOURCE));
        publicationService.updatePublicationVersion(getServiceContextWithoutPrincipal(), expected);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrievePublicationVersionByUrn() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        PublicationVersion actual = publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsPublicationVersion(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME), actual);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrieveLatestPublicationVersionByPublicationUrn() throws Exception {
        String urn = publicationMockFactory.retrieveMock(PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        PublicationVersion actual = publicationService.retrieveLatestPublicationVersionByPublicationUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsPublicationVersion(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrieveLatestPublicationVersionByPublicationUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PUBLICATION_URN));
        publicationService.retrieveLatestPublicationVersionByPublicationUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrieveLatestPublishedPublicationVersionByPublicationUrn() throws Exception {
        String urn = publicationMockFactory.retrieveMock(PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME).getIdentifiableStatisticalResource().getUrn();
        PublicationVersion actual = publicationService.retrieveLatestPublishedPublicationVersionByPublicationUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsPublicationVersion(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME), actual);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrieveLatestPublishedPublicationVersionByPublicationUrnErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PUBLICATION_URN));
        publicationService.retrieveLatestPublishedPublicationVersionByPublicationUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrievePublicationVersionByUrnV2() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        PublicationVersion actual = publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), urn);
        assertEqualsPublicationVersion(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME), actual);
    }

    @Test
    public void testRetrievePublicationVersionByUrnErrorParameterRequiered() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PUBLICATION_VERSION_URN));

        publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), null);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testRetrievePublicationVersionByUrnErrorNotFound() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME, PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testRetrievePublicationVersions() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        List<PublicationVersion> actual = publicationService.retrievePublicationVersions(getServiceContextWithoutPrincipal(), urn);

        assertEquals(2, actual.size());
        assertEqualsPublicationVersionCollection(publicationMockFactory.retrieveMock(PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME).getVersions(), actual);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME, PUBLICATION_VERSION_01_BASIC_NAME})
    public void testFindPublicationVersionsByCondition() throws Exception {

        {
            // Find by version number
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(PublicationVersion.class)
                    .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().versionLogic()).eq(StatisticalResourcesMockFactory.SECOND_VERSION)
                    .orderBy(PublicationVersionProperties.siemacMetadataStatisticalResource().id()).ascending().build();

            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<PublicationVersion> publicationVersionPagedResult = publicationService.findPublicationVersionsByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
            assertEquals(1, publicationVersionPagedResult.getTotalRows());
            assertEquals(publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                    publicationVersionPagedResult.getValues().get(0).getSiemacMetadataStatisticalResource().getUrn());
        }
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME, PUBLICATION_VERSION_01_BASIC_NAME})
    public void testDeletePublicationVersion() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, urn));

        // Delete publication version
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);

        // Validation
        publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_92_IS_REPLACED_BY_PUBLICATION_91_NAME)
    public void testDeletePublicationVersionIsReplacedBy() throws Exception {
        String urnReplaces = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_92_IS_REPLACED_BY_PUBLICATION_91_NAME).getSiemacMetadataStatisticalResource().getUrn();

        MetamacExceptionItem itemRoot = new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_CANT_BE_DELETED, urn);
        MetamacExceptionItem item = new MetamacExceptionItem(ServiceExceptionType.PUBLICATION_VERSION_IS_REPLACED_BY_OTHER_RESOURCE, urnReplaces);
        itemRoot.setExceptionItems(Arrays.asList(item));

        expectedMetamacException(new MetamacException(Arrays.asList(itemRoot)));

        // Delete publication version
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92_NAME)
    public void testDeletePublicationVersionReplaces() throws Exception {
        String urnReplaces = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_91_REPLACES_PUBLICATION_92_NAME).getSiemacMetadataStatisticalResource().getUrn();

        // Delete publication version
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urnReplaces);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME, PUBLICATION_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testDeletePublicationVersionWithTwoVersions() throws Exception {
        String urnV1 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String urnV2 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_04_FOR_PUBLICATION_03_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        // Delete publication version
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urnV2);

        // Validation
        PublicationVersion publicationVersionV1 = publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), urnV1);

        // is replaced_by_version null
        assertNull(publicationVersionV1.getLifeCycleStatisticalResource().getIsReplacedByVersion());
        // Now is last version
        assertTrue(publicationVersionV1.getSiemacMetadataStatisticalResource().getLastVersion());

        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, urnV2));
        publicationService.retrievePublicationVersionByUrn(getServiceContextWithoutPrincipal(), urnV2);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testDeletePublicationVersionErrorNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock({PUBLICATION_03_BASIC_WITH_2_PUBLICATION_VERSIONS_NAME})
    public void testDeletePublicationVersionErrorNoLastVersion() throws Exception {
        String urnV1 = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_FOR_PUBLICATION_03_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urnV1, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urnV1);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testDeletePublicationVersionDraftProcStatus() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testDeletePublicationVersionProductionValidationProcStatus() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testDeletePublicationVersionDiffusionValidationProcStatus() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testDeletePublicationVersionValidationRejectedProcStatus() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME,
            PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, PUBLICATION_VERSION_16_PUBLISHED_NAME})
    public void testDeletePublicationVersionPublishedProcStatus() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));

        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME)
    public void testDeletePublicationVersionProcStatusPublishedNotVisible() throws Exception {
        String urn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, urn, ProcStatusForActionsConstants.PROC_STATUS_FOR_DELETE_RESOURCE));
        publicationService.deletePublicationVersion(getServiceContextWithoutPrincipal(), urn);
    }

    private Object buildPublicationVersionUrn(String maintainerCode, String operationCode, int datasetSequentialId, String versionNumber) {
        StringBuilder strBuilder = new StringBuilder("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=");
        strBuilder.append(maintainerCode).append(":").append(operationCode).append("_").append(String.format("%06d", datasetSequentialId)).append("(").append(versionNumber).append(")");
        return strBuilder.toString();
    }

    // ------------------------------------------------------------------------
    // CHAPTERS
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testCreateChapter() throws Exception {
        Chapter expected = notPersistedDoMocks.mockChapter();
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn(), expected);

        assertRelaxedEqualsChapter(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testUpdateLastUpdateOnCreateChapter() throws Exception {
        Chapter expected = notPersistedDoMocks.mockChapter();
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(), publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn(), expected);

        assertRelaxedEqualsChapter(expected, actual);

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterInFirstLevel() throws Exception {
        Chapter expected = notPersistedDoMocks.mockChapter();
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                expected);

        assertFilledMetadataForChaptersInAllLevels(actual);
        assertFilledMetadataForChaptersInFirstLevel(actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterInNoFirstLevel() throws Exception {
        Chapter expected = notPersistedDoMocks.mockChapterInParentElementLevel(
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(0));
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                expected);

        assertFilledMetadataForChaptersInAllLevels(actual);
        assertFilledMetadataForChaptersInNoFirstLevel(actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterWithOrderFirst() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        // Create chapter
        Chapter expected = notPersistedDoMocks.mockChapter();
        expected.getElementLevel().setOrderInLevel(Long.valueOf(1));
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                expected);

        // Finish transaction
        transactionManager.commit(status);

        // Retrieve children and check orderInLevel
        List<ElementLevel> actualChildren = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn())
                .getChildrenFirstLevel();

        assertEquals(actualChildren.get(0).getId(), actual.getElementLevel().getId());

        assertEquals(Long.valueOf(1), actualChildren.get(0).getOrderInLevel());
        assertEquals(Long.valueOf(2), actualChildren.get(1).getOrderInLevel());
        assertEquals(Long.valueOf(3), actualChildren.get(2).getOrderInLevel());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterWithOrderLast() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        // Create chapter
        Chapter expected = notPersistedDoMocks.mockChapter();
        expected.getElementLevel().setOrderInLevel(Long.valueOf(3));
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                expected);

        // Finish transaction
        transactionManager.commit(status);

        // Retrieve children and check orderInLevel
        List<ElementLevel> actualChildren = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn())
                .getChildrenFirstLevel();

        assertEquals(Long.valueOf(1), actualChildren.get(0).getOrderInLevel());
        assertEquals(Long.valueOf(2), actualChildren.get(1).getOrderInLevel());
        assertEquals(Long.valueOf(3), actualChildren.get(2).getOrderInLevel());

        assertEquals(actualChildren.get(2).getId(), actual.getElementLevel().getId());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterWithOrderMiddle() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        // Create chapter
        Chapter expected = notPersistedDoMocks.mockChapter();
        expected.getElementLevel().setOrderInLevel(Long.valueOf(2));
        Chapter actual = publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn(),
                expected);

        // Finish transaction
        transactionManager.commit(status);

        // Retrieve children and check orderInLevel
        List<ElementLevel> actualChildren = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn())
                .getChildrenFirstLevel();

        assertEquals(Long.valueOf(1), actualChildren.get(0).getOrderInLevel());
        assertEquals(Long.valueOf(2), actualChildren.get(1).getOrderInLevel());
        assertEquals(Long.valueOf(3), actualChildren.get(2).getOrderInLevel());

        assertEquals(actualChildren.get(1).getId(), actual.getElementLevel().getId());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testCreateChapterErrorParameterRequiredPublicationVersionUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PUBLICATION_VERSION_URN));
        publicationService.createChapter(getServiceContextAdministrador(), null, notPersistedDoMocks.mockChapter());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testCreateChapterErrorParameterRequiredChapter() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CHAPTER));
        publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn(), null);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testCreateChapterErrorMetadataIncorrectOrderInLevelNegative() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL));

        Chapter expected = notPersistedDoMocks.mockChapter();
        expected.getElementLevel().setOrderInLevel(Long.valueOf(-1));
        publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn(), expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testCreateChapterErrorMetadataIncorrectOrderInLevelMaxValue() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL));

        Chapter expected = notPersistedDoMocks.mockChapter();
        expected.getElementLevel().setOrderInLevel(Long.MAX_VALUE);
        publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn(), expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testCreateChapterErrorMetadataRequiredOrderInLevel() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL));

        Chapter expected = notPersistedDoMocks.mockChapter();
        expected.getElementLevel().setOrderInLevel(null);
        publicationService.createChapter(getServiceContextAdministrador(),
                publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn(), expected);
    }

    @Test
    public void testCreateChapterErrorPublicationVersionNotExists() throws Exception {
        String publicationVersionUrn = URN_NOT_EXISTS;
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, publicationVersionUrn));

        Chapter expected = notPersistedDoMocks.mockChapter();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME})
    public void testCreateChapterStatusPublicationVersionDraft() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, notPersistedDoMocks.mockChapter());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME})
    public void testCreateChapterStatusPublicationVersionProductionValidation() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, notPersistedDoMocks.mockChapter());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME})
    public void testCreateChapterStatusPublicationVersionDiffusionValidation() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, notPersistedDoMocks.mockChapter());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME})
    public void testCreateChapterStatusPublicationVersionValidationRejected() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, notPersistedDoMocks.mockChapter());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME})
    public void testCreateChapterStatusPublicationVersionPublished() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_PUBLICATION_STRUCTURE));

        Chapter expected = notPersistedDoMocks.mockChapter();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME)
    public void testCreateChapterStatusPublicationVersionPublishedNotVisible() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_PUBLICATION_STRUCTURE));

        Chapter expected = notPersistedDoMocks.mockChapter();
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterStatusPublicationErrorParentNotExists() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        Chapter parentChapter = notPersistedDoMocks.mockChapter();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentChapter.getNameableStatisticalResource().getUrn(), publicationVersionUrn));

        Chapter expected = notPersistedDoMocks.mockChapterInParentElementLevel(parentChapter.getElementLevel());
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testCreateChapterStatusPublicationErrorParentNotExistsInPublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        Chapter parentChapter = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getChildrenFirstLevel().get(0).getChapter();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentChapter.getNameableStatisticalResource().getUrn(), publicationVersionUrn));

        Chapter expected = notPersistedDoMocks.mockChapterInParentElementLevel(parentChapter.getElementLevel());
        publicationService.createChapter(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @SuppressWarnings("static-access")
    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateChapter() throws Exception {
        Chapter expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(0)
                .getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        Chapter actual = publicationService.updateChapter(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsChapter(expected, actual);
        CommonAsserts.assertEqualsInternationalString(expected.getNameableStatisticalResource().getTitle(), actual.getNameableStatisticalResource().getTitle());
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateLastUpdateOnUpdateChapter() throws Exception {
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME);
        Chapter expected = publicationVersionOld.getChildrenFirstLevel().get(0).getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        Chapter actual = publicationService.updateChapter(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsChapter(expected, actual);
        CommonAsserts.assertEqualsInternationalString(expected.getNameableStatisticalResource().getTitle(), actual.getNameableStatisticalResource().getTitle());

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateChapterErrorParameterRequiredChapter() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CHAPTER));
        publicationService.updateChapter(getServiceContextAdministrador(), null);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateChapterStatusDraft() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME);
        Chapter expected = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateChapter(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME})
    public void testUpdateChapterStatusProductionValidation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME);
        Chapter expected = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateChapter(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME})
    public void testUpdateChapterStatusDiffusionValidation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME);
        Chapter expected = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateChapter(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME})
    public void testUpdateChapterStatusValidationRejected() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME);
        Chapter expected = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateChapter(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME})
    public void testUpdateChapterStatusPublished() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_PUBLICATION_STRUCTURE));

        Chapter expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getChildrenFirstLevel().get(0).getChapter();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateChapter(getServiceContextAdministrador(), expected);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateChapterLocation() throws Exception {
        Chapter chapter = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(1)
                .getChapter();
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));

        assertEquals(Long.valueOf(2), chapter.getElementLevel().getOrderInLevel());
        assertEquals(Long.valueOf(1), updatedChapter.getElementLevel().getOrderInLevel());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateLastUpdateOnUpdateChapterLocation() throws Exception {
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME);
        Chapter chapter = publicationVersionOld.getChildrenFirstLevel().get(1).getChapter();
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));

        assertEquals(Long.valueOf(2), chapter.getElementLevel().getOrderInLevel());
        assertEquals(Long.valueOf(1), updatedChapter.getElementLevel().getOrderInLevel());

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        MetamacAsserts.assertEqualsDate(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate(), publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateChapterLocationStatusDraft() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(1)
                .getChapter().getNameableStatisticalResource().getUrn();
        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, null, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME})
    public void testUpdateChapterLocationStatusProductionValidation() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME).getChildrenFirstLevel().get(1).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, null, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME})
    public void testUpdateChapterLocationStatusDiffusionValidation() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME).getChildrenFirstLevel().get(1).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, null, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME})
    public void testUpdateChapterLocationStatusValidationRejected() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME).getChildrenFirstLevel().get(1).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, null, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME})
    public void testUpdateChapterLocationStatusPublished() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersionUrn, "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getChildrenFirstLevel().get(1).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, null, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationActualWithoutParentAndWithoutChildrenTargetWithParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(2).getChapter();
        Chapter parentChapter = publicationVersion.getChildrenFirstLevel().get(1).getChapter();
        assertNull(chapter.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());

        // Update location
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(),
                parentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(1));

        // Check updatedChapter
        assertNotNull(updatedChapter.getElementLevel().getParent());
        assertEquals(parentChapter.getNameableStatisticalResource().getUrn(), updatedChapter.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(1), updatedChapter.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(3, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationActualWithoutParentAndWithChildrenTargetWithParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(1).getChapter();
        Chapter parentChapter = publicationVersion.getChildrenFirstLevel().get(2).getChapter();
        assertNull(chapter.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());

        // Update location
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(),
                parentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(1));

        // Check updatedChapter
        assertNotNull(updatedChapter.getElementLevel().getParent());
        assertEquals(parentChapter.getNameableStatisticalResource().getUrn(), updatedChapter.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(1), updatedChapter.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(3, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getChildren().size());

        assertEquals(parentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(1).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(chapter.getNameableStatisticalResource().getUrn(),
                updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(chapter.getElementLevel().getChildren().get(0).getCube().getNameableStatisticalResource().getUrn(),
                updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getChildren().get(0).getCube().getNameableStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationActualWithParentTargetWithoutParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChapter();
        Chapter initParentChapter = publicationVersion.getChildrenFirstLevel().get(0).getChapter();

        assertNotNull(chapter.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        assertEquals(3, initParentChapter.getElementLevel().getChildren().size());

        // Update location
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));

        // Check updatedChapter
        assertNull(updatedChapter.getElementLevel().getParent());
        assertEquals(Long.valueOf(1), updatedChapter.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(5, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());

        assertEquals(chapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().size());

        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(1).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(2, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationChangingParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChapter();
        Chapter initParentChapter = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        Chapter parentChapter = publicationVersion.getChildrenFirstLevel().get(1).getChapter();

        assertNotNull(chapter.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        assertEquals(3, initParentChapter.getElementLevel().getChildren().size());
        assertEquals(1, parentChapter.getElementLevel().getChildren().size());

        // Update location
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(),
                parentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(1));

        // Check updatedChapter
        assertNotNull(updatedChapter.getElementLevel().getParent());
        assertEquals(parentChapter.getNameableStatisticalResource().getUrn(), updatedChapter.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(1), updatedChapter.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(4, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());

        assertEquals(chapter.getNameableStatisticalResource().getUrn(),
                updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(2, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());

        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(2, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationChangingOrderInSameParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChapter();
        Chapter initParentChapter = publicationVersion.getChildrenFirstLevel().get(0).getChapter();

        assertNotNull(chapter.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        assertEquals(3, initParentChapter.getElementLevel().getChildren().size());
        assertEquals(Long.valueOf(1), chapter.getElementLevel().getOrderInLevel());

        // Update location
        Chapter updatedChapter = publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(),
                initParentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(2));

        // Check updatedChapter
        assertNotNull(updatedChapter.getElementLevel().getParent());
        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedChapter.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(2), updatedChapter.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(4, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());

        assertEquals(chapter.getNameableStatisticalResource().getUrn(),
                updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().get(1).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().get(1).getChildren().size());

        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(3, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationErrorParentIsChild() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__PARENT));

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(0).getChapter();
        Chapter parentChapter = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChapter();

        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(), parentChapter.getNameableStatisticalResource().getUrn(),
                Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateChapterLocationErrorParentNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND, URN_NOT_EXISTS));

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Chapter chapter = publicationVersion.getChildrenFirstLevel().get(0).getChapter();

        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(), URN_NOT_EXISTS, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateChapterLocationErrorParentNotExistsInPublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String parentChapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(0)
                .getChapter().getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentChapterUrn, publicationVersionUrn));

        Chapter chapter = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(0).getChapter();

        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapter.getNameableStatisticalResource().getUrn(), parentChapterUrn, Long.valueOf(1));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateChapterLocationErrorOrderIncorrect() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CHAPTER__ORDER_IN_LEVEL));

        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(0).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.updateChapterLocation(getServiceContextAdministrador(), chapterUrn, null, Long.MAX_VALUE);
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME)
    public void testRetrieveChapter() throws Exception {
        Chapter expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getChildrenFirstLevel().get(0).getChapter();
        Chapter actual = publicationService.retrieveChapter(getServiceContextAdministrador(), expected.getNameableStatisticalResource().getUrn());
        assertRelaxedEqualsChapter(expected, actual);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME)
    public void testRetrieveChapterErrorParameterRequiredChapterUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CHAPTER_URN));
        publicationService.retrieveChapter(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME)
    public void testRetrieveChapterErrorNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND, URN_NOT_EXISTS));
        publicationService.retrieveChapter(getServiceContextAdministrador(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteChapter() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(2).getChapter()
                .getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND, chapterUrn));

        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);
        publicationService.retrieveChapter(getServiceContextAdministrador(), chapterUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateLastUpdateOnDeleteChapter() throws Exception {
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String chapterUrn = publicationVersionOld.getChildrenFirstLevel().get(2).getChapter().getNameableStatisticalResource().getUrn();

        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteFirstLevelChapterWithChildren() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String chapterUrn = publicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn();

        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());

        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);

        // Commit
        transactionManager.commit(status);

        // Checks
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(3, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(4, updatedPublicationVersion.getChildrenAllLevels().size());
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteNoFirstLevelChapterWithChildren() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String chapterUrn = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChapter().getNameableStatisticalResource().getUrn();

        assertEquals(3, publicationVersion.getChildrenAllLevels().get(0).getChildren().size());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);

        // Commit
        transactionManager.commit(status);

        // Checks
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(4, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(8, updatedPublicationVersion.getChildrenAllLevels().size());
        assertEquals(2, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().size());
    }

    @SuppressWarnings("unused")
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteChapterAndCheckOrder() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String chapterUrn = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChapter().getNameableStatisticalResource().getUrn();

        ElementLevel elementLevel_01 = publicationVersion.getChildrenFirstLevel().get(0);
        ElementLevel elementLevel_01_01 = elementLevel_01.getChildren().get(0);
        ElementLevel elementLevel_01_01_01 = elementLevel_01_01.getChildren().get(0);
        ElementLevel elementLevel_01_02 = elementLevel_01.getChildren().get(1);
        ElementLevel elementLevel_01_02_01 = elementLevel_01_02.getChildren().get(0);
        ElementLevel elementLevel_01_03 = elementLevel_01.getChildren().get(2);
        ElementLevel elementLevel_02 = publicationVersion.getChildrenFirstLevel().get(1);
        ElementLevel elementLevel_02_01 = elementLevel_02.getChildren().get(0);
        ElementLevel elementLevel_03 = publicationVersion.getChildrenFirstLevel().get(2);
        ElementLevel elementLevel_04 = publicationVersion.getChildrenFirstLevel().get(3);

        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);

        // Commit
        transactionManager.commit(status);

        // Check number of nodes in level
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(8, updatedPublicationVersion.getChildrenAllLevels().size());
        assertEquals(4, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(2, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().size());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getChildren().size());
        assertEquals(0, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().get(1).getChildren().size());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());
        assertEquals(0, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getChildren().size());
        assertEquals(0, updatedPublicationVersion.getChildrenFirstLevel().get(2).getChildren().size());
        assertEquals(0, updatedPublicationVersion.getChildrenFirstLevel().get(3).getChildren().size());

        // Check nodes URN
        ElementLevel updatedElementLevel_01 = updatedPublicationVersion.getChildrenFirstLevel().get(0);
        ElementLevel updatedElementLevel_01_01 = updatedElementLevel_01.getChildren().get(0);
        ElementLevel updatedElementLevel_01_01_01 = updatedElementLevel_01_01.getChildren().get(0);
        ElementLevel updatedElementLevel_01_02 = updatedElementLevel_01.getChildren().get(1);
        ElementLevel updatedElementLevel_02 = updatedPublicationVersion.getChildrenFirstLevel().get(1);
        ElementLevel updatedElementLevel_02_01 = updatedElementLevel_02.getChildren().get(0);
        ElementLevel updatedElementLevel_03 = updatedPublicationVersion.getChildrenFirstLevel().get(2);
        ElementLevel updatedElementLevel_04 = updatedPublicationVersion.getChildrenFirstLevel().get(3);

        assertEquals(elementLevel_01.getElementId(), updatedElementLevel_01.getElementId());
        assertEquals(elementLevel_01_02.getElementId(), updatedElementLevel_01_01.getElementId());
        assertEquals(elementLevel_01_02_01.getElementId(), updatedElementLevel_01_01_01.getElementId());
        assertEquals(elementLevel_01_03.getElementId(), updatedElementLevel_01_02.getElementId());
        assertEquals(elementLevel_02.getElementId(), updatedElementLevel_02.getElementId());
        assertEquals(elementLevel_02_01.getElementId(), updatedElementLevel_02_01.getElementId());
        assertEquals(elementLevel_03.getElementId(), updatedElementLevel_03.getElementId());
        assertEquals(elementLevel_04.getElementId(), updatedElementLevel_04.getElementId());
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteChapterErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CHAPTER_URN));
        publicationService.deleteChapter(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteChapterErrorChapterNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND, URN_NOT_EXISTS));
        publicationService.deleteChapter(getServiceContextAdministrador(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME)
    public void testDeleteChapterStatusDraft() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(0)
                .getChapter().getNameableStatisticalResource().getUrn();
        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME)
    public void testDeleteChapterStatusProductionValidation() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_19_WITH_STRUCTURE_PRODUCTION_VALIDATION_NAME).getChildrenFirstLevel().get(0).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME)
    public void testDeleteChapterStatusDiffusionValidation() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_20_WITH_STRUCTURE_DIFFUSION_VALIDATION_NAME).getChildrenFirstLevel().get(0).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME)
    public void testDeleteChapterStatusValidationRejected() throws Exception {
        String chapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_21_WITH_STRUCTURE_VALIDATION_REJECTED_NAME).getChildrenFirstLevel().get(0).getChapter()
                .getNameableStatisticalResource().getUrn();
        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME)
    public void testDeleteChapterStatusPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME);
        String chapterUrn = publicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));
        publicationService.deleteChapter(getServiceContextAdministrador(), chapterUrn);
    }

    // ------------------------------------------------------------------------
    // STRUCTURE
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testImportPublicationVersionStructure() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_QUERY, 5, "C00031A_000001"));

        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);

        String locale = "es";
        File file = loadTSVFile("publication_structure-3-levels.tsv");
        URL fileUrl = file.toURI().toURL();

        PublicationVersion actual = publicationService.importPublicationVersionStructure(getServiceContextAdministrador(), expected.getSiemacMetadataStatisticalResource().getUrn(), fileUrl, locale);
        assertEquals("Publication title", actual.getSiemacMetadataStatisticalResource().getTitle().getLocalisedLabel(locale));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME})
    public void testImportPublicationVersionStructureNonExistsMultidatasetError() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_STRUCTURE_IMPORTATION_CUBE_WITH_NONEXISTENT_MULTIDATASET, 3, "Y00010X_000003"));

        PublicationVersion expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);

        String locale = "es";
        File file = loadTSVFile("publication_structure-multidataset_non_exists_error.tsv");
        URL fileUrl = file.toURI().toURL();

        publicationService.importPublicationVersionStructure(getServiceContextAdministrador(), expected.getSiemacMetadataStatisticalResource().getUrn(), fileUrl, locale);
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateCube() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertRelaxedEqualsCube(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testUpdateLastUpdateOnCreateCube() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String publicationVersionUrn = publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertRelaxedEqualsCube(expected, actual);

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateCubeErrorMetadataUnexpectedChildren() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.CUBE__CHILDREN));

        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        expected.getElementLevel().getChildren().add(notPersistedDoMocks.mockChapter().getElementLevel());
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateCubeDataset() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertRelaxedEqualsCube(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeQuery() throws Exception {
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertRelaxedEqualsCube(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testCreateCubeMultidataset() throws Exception {
        Multidataset multidataset = multidatasetMockFactory.retrieveMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME);
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockMultidatasetCube(multidataset);
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertRelaxedEqualsCube(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION})
    public void testCreateCubeUrl() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockUrlCube("http://www.pruebas.com");
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertRelaxedEqualsCube(expected, actual);
    }
   
    @Test
    @MetamacMock({PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION})
    public void testCreateCubeUrlFormatError() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION).getSiemacMetadataStatisticalResource().getUrn();
        Cube expected = notPersistedDoMocks.mockUrlCube("://www.pruebas.com");
        try {
            publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
            fail("create cube url must start with http or https");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(CommonServiceExceptionType.METADATA_INVALID_URL.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }
    
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testCreateCubeErrorParameterRequiredPublicationVersionUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.PUBLICATION_VERSION_URN));
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        publicationService.createCube(getServiceContextAdministrador(), null, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataUnexpectedDatasetOrQuery() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED,
                ServiceExceptionParameters.CUBE__DATASET + " / " + ServiceExceptionParameters.CUBE__QUERY + " / " + ServiceExceptionParameters.CUBE__MULTIDATASET));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        expected.setQuery(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testCreateCubeErrorMetadataUnexpectedDatasetOrMultidataset() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED,
                ServiceExceptionParameters.CUBE__DATASET + " / " + ServiceExceptionParameters.CUBE__QUERY + " / " + ServiceExceptionParameters.CUBE__MULTIDATASET));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        expected.setMultidataset(multidatasetMockFactory.retrieveMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME));
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testCreateCubeErrorMetadataUnexpectedQueryOrMultidataset() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED,
                ServiceExceptionParameters.CUBE__DATASET + " / " + ServiceExceptionParameters.CUBE__QUERY + " / " + ServiceExceptionParameters.CUBE__MULTIDATASET));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.setMultidataset(multidatasetMockFactory.retrieveMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME));
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataRequiredDatasetOrQuery() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED,
                ServiceExceptionParameters.CUBE__DATASET + " / " + ServiceExceptionParameters.CUBE__QUERY + " / " + ServiceExceptionParameters.CUBE__MULTIDATASET));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        expected.setQuery(null);
        expected.setDataset(null);
        expected.setMultidataset(null);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataRequiredDatasetUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CUBE__DATASET__URN));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);
        Cube expected = notPersistedDoMocks.mockDatasetCube(dataset);
        expected.getDataset().getIdentifiableStatisticalResource().setUrn(null);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataRequiredQueryUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CUBE__QUERY__URN));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getQuery().getIdentifiableStatisticalResource().setUrn(null);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testCreateCubeErrorMetadataRequiredMultidatasetUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CUBE__MULTIDATASET__URN));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Multidataset multidataset = multidatasetMockFactory.retrieveMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME);
        Cube expected = notPersistedDoMocks.mockMultidatasetCube(multidataset);
        expected.getMultidataset().getIdentifiableStatisticalResource().setUrn(null);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorPublicationVersionNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, URN_NOT_EXISTS));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), URN_NOT_EXISTS, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeInFirstLevel() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);

        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(null);
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);

        assertFilledMetadataForCubesInAllLevels(actual);
        assertFilledMetadataForCubesInFirstLevel(actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeInNoFirstLevel() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);

        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(publicationVersion.getChildrenFirstLevel().get(2));
        expected.getElementLevel().setOrderInLevel(Long.valueOf(1));
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersion.getSiemacMetadataStatisticalResource().getUrn(), expected);

        assertFilledMetadataForCubesInAllLevels(actual);
        assertFilledMetadataForCubesInNoFirstLevel(actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeWithOrderFirst() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        // Create cube
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);

        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(publicationVersion.getChildrenFirstLevel().get(0));
        expected.getElementLevel().setOrderInLevel(Long.valueOf(1));
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersion.getSiemacMetadataStatisticalResource().getUrn(), expected);

        // Finish transaction
        transactionManager.commit(status);

        // Retrieve children and check orderInLevel
        List<ElementLevel> actualChildren = publicationService
                .retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                        publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn())
                .getChildrenFirstLevel().get(0).getChildren();

        assertEquals(actualChildren.get(0).getId(), actual.getElementLevel().getId());

        assertEquals(Long.valueOf(1), actualChildren.get(0).getOrderInLevel());
        assertEquals(Long.valueOf(2), actualChildren.get(1).getOrderInLevel());
        assertEquals(Long.valueOf(3), actualChildren.get(2).getOrderInLevel());
        assertEquals(Long.valueOf(4), actualChildren.get(3).getOrderInLevel());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeWithOrderLast() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        // Create cube
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);

        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(publicationVersion.getChildrenFirstLevel().get(0));
        expected.getElementLevel().setOrderInLevel(Long.valueOf(4));
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersion.getSiemacMetadataStatisticalResource().getUrn(), expected);

        // Finish transaction
        transactionManager.commit(status);

        // Retrieve children and check orderInLevel
        List<ElementLevel> actualChildren = publicationService
                .retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                        publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn())
                .getChildrenFirstLevel().get(0).getChildren();

        assertEquals(actualChildren.get(3).getId(), actual.getElementLevel().getId());

        assertEquals(Long.valueOf(1), actualChildren.get(0).getOrderInLevel());
        assertEquals(Long.valueOf(2), actualChildren.get(1).getOrderInLevel());
        assertEquals(Long.valueOf(3), actualChildren.get(2).getOrderInLevel());
        assertEquals(Long.valueOf(4), actualChildren.get(3).getOrderInLevel());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeWithOrderMiddle() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        // Create cube
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);

        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(publicationVersion.getChildrenFirstLevel().get(0));
        expected.getElementLevel().setOrderInLevel(Long.valueOf(2));
        Cube actual = publicationService.createCube(getServiceContextAdministrador(), publicationVersion.getSiemacMetadataStatisticalResource().getUrn(), expected);

        // Finish transaction
        transactionManager.commit(status);

        // Retrieve children and check orderInLevel
        List<ElementLevel> actualChildren = publicationService
                .retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                        publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn())
                .getChildrenFirstLevel().get(0).getChildren();

        assertEquals(actualChildren.get(1).getId(), actual.getElementLevel().getId());

        assertEquals(Long.valueOf(1), actualChildren.get(0).getOrderInLevel());
        assertEquals(Long.valueOf(2), actualChildren.get(1).getOrderInLevel());
        assertEquals(Long.valueOf(3), actualChildren.get(2).getOrderInLevel());
        assertEquals(Long.valueOf(4), actualChildren.get(3).getOrderInLevel());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataIncorrectOrderInLevelNegative() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setOrderInLevel(Long.valueOf(-1));

        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataIncorrectOrderInLevelMaxValue() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setOrderInLevel(Long.MAX_VALUE);

        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeErrorMetadataRequiredOrderInLevel() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL));

        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setOrderInLevel(null);

        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_12_DRAFT_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationVersionDraft() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_12_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationVersionProductionValidation() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_13_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationVersionDiffusionValidation() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_14_DIFFUSION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationVersionValidationRejected() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_15_VALIDATION_REJECTED_NAME).getSiemacMetadataStatisticalResource().getUrn();
        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_16_PUBLISHED_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationVersionPublished() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_16_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_PUBLICATION_STRUCTURE));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME, DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationVersionPublishedNotVisible() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_31_V2_PUBLISHED_NO_VISIBLE_FOR_PUBLICATION_06_NAME).getSiemacMetadataStatisticalResource()
                .getUrn();

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersionUrn, ProcStatusForActionsConstants.PROC_STATUS_FOR_EDIT_PUBLICATION_STRUCTURE));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationErrorParentNotExists() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        ElementLevel parentElementLevel = notPersistedDoMocks.mockChapter().getElementLevel();
        expectedMetamacException(
                new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentElementLevel.getChapter().getNameableStatisticalResource().getUrn(), publicationVersionUrn));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(parentElementLevel);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME, PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME, QUERY_01_SIMPLE_NAME})
    public void testCreateCubeStatusPublicationErrorParentNotExistsInPublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME)
                .getSiemacMetadataStatisticalResource().getUrn();
        ElementLevel parentElementLevel = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_17_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_NAME).getChildrenFirstLevel().get(0);

        expectedMetamacException(
                new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentElementLevel.getChapter().getNameableStatisticalResource().getUrn(), publicationVersionUrn));

        Query query = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME);
        Cube expected = notPersistedDoMocks.mockQueryCube(query);
        expected.getElementLevel().setParent(parentElementLevel);
        publicationService.createCube(getServiceContextAdministrador(), publicationVersionUrn, expected);
    }

    @SuppressWarnings("static-access")
    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCube() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        Cube actual = publicationService.updateCube(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsCube(expected, actual);
        CommonAsserts.assertEqualsInternationalString(expected.getNameableStatisticalResource().getTitle(), actual.getNameableStatisticalResource().getTitle());
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME})
    public void testUpdateCubeSetWithMultidataset() throws Exception {
        Multidataset expectedMultidataset = multidatasetMockFactory.retrieveMock(MULTIDATASET_02_BASIC_WITH_GENERATED_VERSION_NAME);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();

        assertNull(expected.getDataset());
        assertNotNull(expected.getQuery());
        assertNull(expected.getMultidataset());
        assertNull(expected.getUrl());
        assertNull(expected.getPublication());

        expected.setQuery(null);
        expected.setMultidataset(expectedMultidataset);

        Cube actual = publicationService.updateCube(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsCube(expected, actual);
        assertNull(actual.getDataset());
        assertNull(actual.getQuery());
        assertNull(actual.getUrl());
        assertNotNull(actual.getMultidataset());
        assertNull(actual.getPublication());
        assertEqualsMultidataset(expectedMultidataset, actual.getMultidataset());
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION})
    public void testUpdateCubeSetWithUrl() throws Exception {
        String expectedUrl = "http://www.pruebas.com";

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();

        assertNull(expected.getDataset());
        assertNotNull(expected.getQuery());
        assertNull(expected.getMultidataset());
        assertNull(expected.getUrl());
        assertNull(expected.getPublication());

        expected.setQuery(null);
        expected.setUrl(expectedUrl);

        Cube actual = publicationService.updateCube(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsCube(expected, actual);
        assertNull(actual.getDataset());
        assertNull(actual.getQuery());
        assertNull(actual.getMultidataset());
        assertNull(actual.getPublication());
        assertEquals(expectedUrl, actual.getUrl());
    }
    
    @Test
    @MetamacMock({PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION})
    public void testUpdateCubeUrlFormatError() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_102_WITH_COMPLEX_STRUCTURE_FOR_URL_PUBLICATION);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(4).getCube();
        try {
            expected.setUrl("www.pruebas.com");
            publicationService.updateCube(getServiceContextAdministrador(), expected);
            fail("update cube url must start with http or https");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(CommonServiceExceptionType.METADATA_INVALID_URL.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }
    
    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateLastUpdateOnUpdateCube() throws Exception {
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube expected = publicationVersionOld.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        Cube actual = publicationService.updateCube(getServiceContextAdministrador(), expected);

        assertRelaxedEqualsCube(expected, actual);
        CommonAsserts.assertEqualsInternationalString(expected.getNameableStatisticalResource().getTitle(), actual.getNameableStatisticalResource().getTitle());

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeErrorMetadataUnexpectedChildren() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.CUBE__CHILDREN));

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        expected.getElementLevel().getChildren().add(notPersistedDoMocks.mockChapter().getElementLevel());
        publicationService.updateCube(getServiceContextAdministrador(), expected);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CUBE));
        publicationService.updateCube(getServiceContextAdministrador(), null);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeStatusPublicationVersionDraft() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME})
    public void testUpdateCubeStatusPublicationVersionProductionValidation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME})
    public void testUpdateCubeStatusPublicationVersionDiffusionValidation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME})
    public void testUpdateCubeStatusPublicationVersionValidationRejected() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME);
        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());

        publicationService.updateCube(getServiceContextAdministrador(), expected);
    }

    @SuppressWarnings("static-access")
    @Test
    @MetamacMock({PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME})
    public void testUpdateCubeStatusPublicationVersionPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        Cube expected = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        expected.getNameableStatisticalResource().setTitle(notPersistedDoMocks.mockInternationalString());
        publicationService.updateCube(getServiceContextAdministrador(), expected);
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateCubeLocation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        Cube updatedCube = publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));

        assertEquals(Long.valueOf(4), cube.getElementLevel().getOrderInLevel());
        assertEquals(Long.valueOf(1), updatedCube.getElementLevel().getOrderInLevel());
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateLastUpdateOnUpdateCubeLocation() throws Exception {
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        Cube cube = publicationVersionOld.getChildrenFirstLevel().get(3).getCube();
        Cube updatedCube = publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));

        assertEquals(Long.valueOf(4), cube.getElementLevel().getOrderInLevel());
        assertEquals(Long.valueOf(1), updatedCube.getElementLevel().getOrderInLevel());

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        MetamacAsserts.assertEqualsDate(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate(), publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate());
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateCubeLocationStatusPublicationVersionDraft() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);

        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME)
    public void testUpdateCubeLocationStatusPublicationVersionProductionValidation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME);

        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME)
    public void testUpdateCubeLocationStatusPublicationVersionDiffusionValidation() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME);

        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME)
    public void testUpdateCubeLocationStatusValidationRejected() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME);

        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME)
    public void testUpdateCubeLocationStatusPublicationVersionPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME);
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));

        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeLocationActualWithoutParentTargetWithParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube cube = publicationVersion.getChildrenFirstLevel().get(3).getCube();
        Chapter parentChapter = publicationVersion.getChildrenFirstLevel().get(2).getChapter();
        assertNull(cube.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());

        // Update location
        Cube updatedCube = publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(),
                parentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(1));

        // Check updatedChapter
        assertNotNull(updatedCube.getElementLevel().getParent());
        assertEquals(parentChapter.getNameableStatisticalResource().getUrn(), updatedCube.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(1), updatedCube.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(3, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeLocationActualWithParentTargetWithoutParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube cube = publicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getCube();
        Chapter initParentChapter = publicationVersion.getChildrenFirstLevel().get(1).getChapter();

        assertNotNull(cube.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        assertEquals(1, initParentChapter.getElementLevel().getChildren().size());

        // Update location
        Cube updatedCube = publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(), null, Long.valueOf(5));

        // Check updatedChapter
        assertNull(updatedCube.getElementLevel().getParent());
        assertEquals(Long.valueOf(5), updatedCube.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(5, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());

        assertEquals(cube.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(4).getCube().getNameableStatisticalResource().getUrn());

        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(1).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(0, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeLocationChangingParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube cube = publicationVersion.getChildrenFirstLevel().get(1).getChildren().get(0).getCube();
        Chapter initParentChapter = publicationVersion.getChildrenFirstLevel().get(1).getChapter();
        Chapter parentChapter = publicationVersion.getChildrenFirstLevel().get(2).getChapter();

        assertNotNull(cube.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        assertEquals(1, initParentChapter.getElementLevel().getChildren().size());
        assertEquals(0, parentChapter.getElementLevel().getChildren().size());

        // Update location
        Cube updatedCube = publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(),
                parentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(1));

        // Check updatedChapter
        assertNotNull(updatedCube.getElementLevel().getParent());
        assertEquals(parentChapter.getNameableStatisticalResource().getUrn(), updatedCube.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(1), updatedCube.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(4, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());

        assertEquals(cube.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(2).getChildren().get(0).getCube().getNameableStatisticalResource().getUrn());

        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(1).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(0, updatedPublicationVersion.getChildrenFirstLevel().get(1).getChildren().size());
        assertEquals(1, updatedPublicationVersion.getChildrenFirstLevel().get(2).getChildren().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeLocationChangingOrderInSameParent() throws Exception {
        // Create transaction
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        Cube cube = publicationVersion.getChildrenFirstLevel().get(0).getChildren().get(2).getCube();
        Chapter initParentChapter = publicationVersion.getChildrenFirstLevel().get(0).getChapter();

        assertNotNull(cube.getElementLevel().getParent());
        assertEquals(4, publicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, publicationVersion.getChildrenAllLevels().size());
        assertEquals(3, initParentChapter.getElementLevel().getChildren().size());
        assertEquals(Long.valueOf(3), cube.getElementLevel().getOrderInLevel());

        // Update location
        Cube updatedCube = publicationService.updateCubeLocation(getServiceContextAdministrador(), cube.getNameableStatisticalResource().getUrn(),
                initParentChapter.getNameableStatisticalResource().getUrn(), Long.valueOf(1));

        // Check updatedChapter
        assertNotNull(updatedCube.getElementLevel().getParent());
        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedCube.getElementLevel().getParentUrn());
        assertEquals(Long.valueOf(1), updatedCube.getElementLevel().getOrderInLevel());

        // Commit
        transactionManager.commit(status);

        // Validate structure
        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(4, updatedPublicationVersion.getChildrenFirstLevel().size());
        assertEquals(10, updatedPublicationVersion.getChildrenAllLevels().size());

        assertEquals(cube.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().get(0).getCube().getNameableStatisticalResource().getUrn());

        assertEquals(initParentChapter.getNameableStatisticalResource().getUrn(), updatedPublicationVersion.getChildrenFirstLevel().get(0).getChapter().getNameableStatisticalResource().getUrn());
        assertEquals(3, updatedPublicationVersion.getChildrenFirstLevel().get(0).getChildren().size());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME})
    public void testUpdateCubeLocationErrorParentNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND, URN_NOT_EXISTS));

        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String cubeUrn = publicationVersion.getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource().getUrn();

        publicationService.updateCubeLocation(getServiceContextAdministrador(), cubeUrn, URN_NOT_EXISTS, Long.valueOf(1));
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME})
    public void testUpdateCubeLocationErrorParentNotExistsInPublicationVersion() throws Exception {
        String publicationVersionUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getSiemacMetadataStatisticalResource().getUrn();
        String parentChapterUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_18_WITH_STRUCTURE_FOR_PUBLICATION_VERSION_04_AND_LAST_VERSION_NAME).getChildrenFirstLevel().get(0)
                .getChapter().getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CHAPTER_NOT_FOUND_IN_PUBLICATION_VERSION, parentChapterUrn, publicationVersionUrn));

        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource()
                .getUrn();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cubeUrn, parentChapterUrn, Long.valueOf(1));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateCubeLocationErrorOrderIncorrect() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CUBE__ORDER_IN_LEVEL));

        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource()
                .getUrn();
        publicationService.updateCubeLocation(getServiceContextAdministrador(), cubeUrn, null, Long.MAX_VALUE);
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME})
    public void testRetrieveCube() throws Exception {
        Cube expected = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(3).getCube();
        Cube actual = publicationService.retrieveCube(getServiceContextAdministrador(), expected.getNameableStatisticalResource().getUrn());
        assertRelaxedEqualsCube(expected, actual);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME})
    public void testRetrieveCubeErrorParameterRequiredCubeUrn() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CUBE_URN));
        publicationService.retrieveCube(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME, QUERY_01_SIMPLE_NAME})
    public void testRetrieveCubeErrorCubeNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CUBE_NOT_FOUND, URN_NOT_EXISTS));
        publicationService.retrieveCube(getServiceContextAdministrador(), URN_NOT_EXISTS);
    }

    @Override
    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteCube() throws Exception {
        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource()
                .getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CUBE_NOT_FOUND, cubeUrn));

        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
        publicationService.retrieveCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testUpdateLastUpdateOnDeleteCube() throws Exception {
        PublicationVersion publicationVersionOld = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME);
        String cubeUrn = publicationVersionOld.getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource().getUrn();

        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);

        PublicationVersion updatedPublicationVersion = publicationService.retrievePublicationVersionByUrn(getServiceContextAdministrador(),
                publicationVersionOld.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(updatedPublicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(publicationVersionOld.getSiemacMetadataStatisticalResource().getLastUpdate()));
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteCubeNoFirstLevel() throws Exception {
        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(1).getChildren().get(0).getCube()
                .getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.CUBE_NOT_FOUND, cubeUrn));

        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
        publicationService.retrieveCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteCubeErrorParameterRequired() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.CUBE_URN));
        publicationService.deleteCube(getServiceContextAdministrador(), null);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteCubeErrorCubeNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.CUBE_NOT_FOUND, URN_NOT_EXISTS));
        publicationService.deleteCube(getServiceContextAdministrador(), URN_NOT_EXISTS);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME)
    public void testDeleteCubeStatusPublicationVersionDraft() throws Exception {
        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_22_WITH_COMPLEX_STRUCTURE_DRAFT_NAME).getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource()
                .getUrn();
        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME)
    public void testDeleteCubeStatusPublicationVersionProductionValidation() throws Exception {
        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_23_WITH_COMPLEX_STRUCTURE_PRODUCTION_VALIDATION_NAME).getChildrenFirstLevel().get(3).getCube()
                .getNameableStatisticalResource().getUrn();
        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME)
    public void testDeleteCubeStatusPublicationVersionDiffusionValidation() throws Exception {
        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_24_WITH_COMPLEX_STRUCTURE_DIFFUSION_VALIDATION_NAME).getChildrenFirstLevel().get(3).getCube()
                .getNameableStatisticalResource().getUrn();
        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME)
    public void testDeleteCubeStatusPublicationVersionValidationRejected() throws Exception {
        String cubeUrn = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_25_WITH_COMPLEX_STRUCTURE_VALIDATION_REJECTED_NAME).getChildrenFirstLevel().get(3).getCube()
                .getNameableStatisticalResource().getUrn();
        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock(PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME)
    public void testDeleteCubeStatusPublicationVersionPublished() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_26_WITH_COMPLEX_STRUCTURE_PUBLISHED_NAME);
        String cubeUrn = publicationVersion.getChildrenFirstLevel().get(3).getCube().getNameableStatisticalResource().getUrn();
        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, publicationVersion.getSiemacMetadataStatisticalResource().getUrn(),
                "DRAFT, VALIDATION_REJECTED, PRODUCTION_VALIDATION, DIFFUSION_VALIDATION"));
        publicationService.deleteCube(getServiceContextAdministrador(), cubeUrn);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME})
    public void testDeletePublicationVersionReplacesVersion() throws Exception {
        PublicationVersion publicationVersion = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_98_TO_DELETE_WITH_PREVIOUS_VERSION_NAME);

        publicationService.deletePublicationVersion(getServiceContextAdministrador(), publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Override
    public void testFindPublicationsByCondition() throws Exception {
        // TODO Auto-generated method stub
        
    }
}
