package org.siemac.metamac.statistical.resources.core.dataset.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasource;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasourceCollection;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_50_WITH_DATASOURCE_FROM_PX_WITH_USER_NEXT_UPDATE_IN_ONE_MONTH_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_89_WITH_ONE_DATASOURCE_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_90_WITH_TWO_DATASOURCES_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasourceMockFactory.DATASOURCE_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasourceMockFactory.DATASOURCE_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_07_SIMPLE_MULTI_VERSION_NAME;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.DataMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.TaskMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/dataset-repository-mockito.xml", "classpath:spring/statistical-resources/include/task-mockito.xml",
        "classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class DatasetServiceDatasourceManagementTest extends StatisticalResourcesBaseTest {

    @Autowired
    private DatasetService                   datasetService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private SrmRestInternalService           srmRestInternalService;

    @Autowired
    private TaskService                      taskService;

    @Before
    public void setUp() throws MetamacException {
        Mockito.reset(datasetRepositoriesServiceFacade);
        mockAllTaskInProgressForResource(false);
    }

    @After
    public void after() {
        Mockito.validateMockitoUsage();
    }

    // ------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testCreateDatasource() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        // IMPORTATION TASK SET DATASETREPOSITORYID
        String datasetRepositoryId = StatisticalResourcesPersistedDoMocks.mockString(10);
        datasetVersion.setDatasetRepositoryId(datasetRepositoryId);
        expectedDatasetVersion.setDatasetRepositoryId(datasetRepositoryId);
        datasetVersion = datasetService.updateDatasetVersion(getServiceContextAdministrador(), datasetVersion);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();
        Datasource actual = datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);
        expected.setDatasetVersion(expectedDatasetVersion);

        // SET EXPECTED METADATA

        DataMockUtils.fillDatasetVersionWithCalculatedMetadataFromData(expectedDatasetVersion);

        assertEqualsDatasource(expected, actual);
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME, DATASET_VERSION_02_BASIC_NAME})
    public void testCreateDatasourceWithObservationAttributes() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);

        String datasetRepositoryId = StatisticalResourcesPersistedDoMocks.mockString(10);
        // IMPORTATION TASK SET DATASETREPOSITORYID
        expectedDatasetVersion.setDatasetRepositoryId(datasetRepositoryId);
        datasetVersion.setDatasetRepositoryId(datasetRepositoryId);
        datasetVersion = datasetService.updateDatasetVersion(getServiceContextAdministrador(), datasetVersion);

        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME).getSiemacMetadataStatisticalResource().getUrn();
        mockDsdAndDataRepositorySimpleDimensionsWithObservationAttributes();

        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();
        Datasource actual = datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);
        expected.setDatasetVersion(expectedDatasetVersion);

        // SET EXPECTED METADATA

        DataMockUtils.fillDatasetVersionWithCalculatedMetadataFromDataWithObservationAttributes(expectedDatasetVersion);

        assertEqualsDatasource(expected, actual);
    }

    @Test
    @MetamacMock(DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME)
    public void testCreateDatasourceDraft() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
        testCanAddDatasourceInCurrentProcStatus(DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testCreateDatasourceProductionValidation() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
        testCanNotAddDatasourceInCurrentProcStatus(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testCreateDatasourceDiffusionValidation() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
        testCanNotAddDatasourceInCurrentProcStatus(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME)
    public void testCreateDatasourceValidationRejected() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
        testCanAddDatasourceInCurrentProcStatus(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME)
    public void testCreateDatasourcePublished() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
        testCanNotAddDatasourceInCurrentProcStatus(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);
    }

    private void testCanNotAddDatasourceInCurrentProcStatus(String datasetVersionMockName) throws MetamacException {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATASOURCES, datasetVersionUrn));

        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);
        Assert.fail("Should have thrown Exception and should not created datasource");
    }

    private void testCanAddDatasourceInCurrentProcStatus(String datasetVersionMockName) throws MetamacException {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();

        Datasource actual = datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);
        assertNotNull(actual);
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testUpdateDateNextUpdateOnCreateDatasourceFromPxWithDateNextUpdateBuiltAutomatically() throws Exception {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        DateTime newNextUpdate = new DateTime().plusWeeks(1);
        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();
        expected.setDateNextUpdate(newNextUpdate);

        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);

        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        Assert.assertEquals(CoreCommonUtil.jodaDateTime2IsoDate(newNextUpdate.toDate()), actual.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testUpdateDateNextUpdateOnCreateDatasourceFromPxWithDateNextUpdateBuiltAutomaticallyNewDateIsAfter() throws Exception {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String oldNextUpdate = expectedDatasetVersion.getDateNextUpdate();
        DateTime newNextUpdate = new DateTime().plusYears(1);
        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();
        expected.setDateNextUpdate(newNextUpdate);

        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);

        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        Assert.assertEquals(oldNextUpdate, actual.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testUpdateDateNextUpdateOnCreateDatasourceFromPxWithDateNextUpdateBuiltAutomaticallyNewDateIsAfterButInThePast() throws Exception {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_49_WITH_DATASOURCE_FROM_PX_WITH_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String oldNextUpdate = expectedDatasetVersion.getDateNextUpdate();
        DateTime newNextUpdate = new DateTime().minusMonths(1);
        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();
        expected.setDateNextUpdate(newNextUpdate);

        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);

        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        Assert.assertEquals(oldNextUpdate, actual.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_50_WITH_DATASOURCE_FROM_PX_WITH_USER_NEXT_UPDATE_IN_ONE_MONTH_NAME})
    public void testUpdateDateNextUpdateOnCreateDatasourceFromPxWithUserDateNextUpdate() throws Exception {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_50_WITH_DATASOURCE_FROM_PX_WITH_USER_NEXT_UPDATE_IN_ONE_MONTH_NAME);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String oldNextUpdate = expectedDatasetVersion.getDateNextUpdate();
        DateTime nextUpdate = new DateTime().plusWeeks(1);
        Datasource expected = notPersistedDoMocks.mockDatasourceForPersist();
        expected.setDateNextUpdate(nextUpdate);

        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);

        DatasetVersion actual = datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersionUrn);

        // SET EXPECTED METADATA
        Assert.assertEquals(oldNextUpdate, actual.getDateNextUpdate());
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testCreateDatasourceErrorIdentifiableResourceRequired() throws Exception {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATASOURCE));

        Datasource expected = notPersistedDoMocks.mockDatasourceWithIdentifiableAndDatasetVersionNull(expectedDatasetVersion);
        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);
    }

    @Test
    @MetamacMock({DATASET_VERSION_01_BASIC_NAME})
    public void testCreateDatasoureErrorDatasetVersionMustBeEmpty() throws Exception {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_01_BASIC_NAME);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.DATASOURCE__DATASET_VERSION));

        Datasource template = new Datasource();
        template.setDatasetVersion(expectedDatasetVersion);
        Datasource expected = notPersistedDoMocks.mockDatasource(template);
        datasetService.createDatasource(getServiceContextWithoutPrincipal(), datasetVersionUrn, expected);
    }

    // ------------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------------

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testUpdateDatasource() throws Exception {
        Datasource expected = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME);

        expected.getIdentifiableStatisticalResource().setLastUpdatedBy("user");

        Datasource actual = datasetService.updateDatasource(getServiceContextWithoutPrincipal(), expected);
        assertEqualsDatasource(expected, actual);
    }

    @Test
    @MetamacMock(DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME)
    public void testUpdateDatasourceDraft() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanUpdateDatasourceInCurrentProcStatus(DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testUpdateDatasourceNotProductionValidation() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
        testCanNotUpdateDatasourceInCurrentProcStatus(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testUpdateDatasourceDiffusionValidtaion() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanNotUpdateDatasourceInCurrentProcStatus(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock({DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME, DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME})
    public void testUpdateDatasourceValidationRejected() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanUpdateDatasourceInCurrentProcStatus(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);
        testCanNotUpdateDatasourceInCurrentProcStatus(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME)
    public void testUpdateDatasourcePublished() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanNotUpdateDatasourceInCurrentProcStatus(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);
    }

    private void testCanNotUpdateDatasourceInCurrentProcStatus(String datasetVersionMockName) throws MetamacException {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Datasource expected = expectedDatasetVersion.getDatasources().get(0);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATASOURCES, datasetVersionUrn));

        datasetService.updateDatasource(getServiceContextWithoutPrincipal(), expected);
        Assert.fail("Should have thrown Exception and should not updated datasource");
    }

    private void testCanUpdateDatasourceInCurrentProcStatus(String datasetVersionMockName) throws MetamacException {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);

        Datasource expected = expectedDatasetVersion.getDatasources().get(0);

        Datasource actual = datasetService.updateDatasource(getServiceContextWithoutPrincipal(), expected);
        assertNotNull(actual);
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testUpdateDatasourceImportationTaskInProgress() throws Exception {
        Datasource expected = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME);
        DatasetVersion expectedDatasetVersion = expected.getDatasetVersion();
        String datasetUrn = expectedDatasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);

        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        expected.getIdentifiableStatisticalResource().setLastUpdatedBy("user");
        datasetService.updateDatasource(getServiceContextWithoutPrincipal(), expected);
    }

    // ------------------------------------------------------------------------
    // RETRIEVE
    // ------------------------------------------------------------------------

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testRetrieveDatasourceByUrn() throws Exception {
        Datasource actual = datasetService.retrieveDatasourceByUrn(getServiceContextWithoutPrincipal(),
                datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn());
        assertEqualsDatasource(datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME), actual);
    }

    @Test
    public void testRetrieveDatasourceByUrnParameterRequired() throws MetamacException {
        expectedMetamacException(new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.URN));

        datasetService.retrieveDatasourceByUrn(getServiceContextWithoutPrincipal(), EMPTY);
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testRetrieveDatasourceByUrnNotExists() throws MetamacException {
        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_NOT_FOUND, URN_NOT_EXISTS));

        datasetService.retrieveDatasourceByUrn(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS);
    }

    // ------------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------------

    @Test
    @MetamacMock(QUERY_07_SIMPLE_MULTI_VERSION_NAME)
    public void testDeleteDatasourceInDatasetVersionWithQueries() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_56_DRAFT_WITH_DATASOURCE_AND_QUERIES_NAME);

        Datasource expected = datasetVersion.getDatasources().get(0);

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String datasourceUrn = expected.getIdentifiableStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_IN_DATASET_VERSION_WITH_QUERIES_DELETE_ERROR, datasourceUrn));

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testDeleteDatasource() throws Exception {
        Datasource expected = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME);

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String datasourceUrn = expected.getIdentifiableStatisticalResource().getUrn();

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_NOT_FOUND, datasourceUrn));
        datasetService.retrieveDatasourceByUrn(getServiceContextWithoutPrincipal(), datasourceUrn);
    }

    // TODO EDATOS-3013 ADD TESTS FOR DELETEDATASOURCES NO USED

    @Test
    @MetamacMock(DATASET_VERSION_89_WITH_ONE_DATASOURCE_NAME)
    public void testDeleteDatasourceKeepDatasetRepository() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_89_WITH_ONE_DATASOURCE_NAME);

        mockDsdAndDataRepositoryEmpty();

        String datasourceUrn = datasetVersion.getDatasources().get(0).getIdentifiableStatisticalResource().getUrn();

        assertNotNull(datasetVersion.getDatasetRepositoryId());
        assertEquals(1, datasetVersion.getDatasources().size());

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);

        datasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextAdministrador(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        assertNotNull(datasetVersion.getDatasetRepositoryId());
        assertEquals(0, datasetVersion.getDatasources().size());
        assertEquals(0L, datasetVersion.getFormatExtentObservations().longValue());

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_NOT_FOUND, datasourceUrn));
        datasetService.retrieveDatasourceByUrn(getServiceContextWithoutPrincipal(), datasourceUrn);
    }

    @Test
    @MetamacMock(DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME)
    public void testDeleteDatasourceDraft() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanDeleteDatasourceInCurrentProcStatus(DATASET_VERSION_51_IN_DRAFT_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testDeleteDatasourceProductionValidation() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanNotDeleteDatasourceInCurrentProcStatus(DATASET_VERSION_52_IN_PRODUCTION_VALIDATION_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME)
    public void testDeleteDatasourceDiffusionValidation() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanNotDeleteDatasourceInCurrentProcStatus(DATASET_VERSION_53_IN_DIFFUSION_VALIDATION_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME)
    public void testDeleteDatasourceValidationRejected() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanDeleteDatasourceInCurrentProcStatus(DATASET_VERSION_54_IN_VALIDATION_REJECTED_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME)
    public void testDeleteDatasourcePublished() throws Exception {
        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        testCanNotDeleteDatasourceInCurrentProcStatus(DATASET_VERSION_55_PUBLISHED_WITH_DATASOURCE_NAME);
    }

    @Test
    @MetamacMock(DATASET_VERSION_90_WITH_TWO_DATASOURCES_NAME)
    public void testDeleteDatasourceErrorDeletingData() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_90_WITH_TWO_DATASOURCES_NAME);
        Datasource expected = datasetVersion.getDatasources().get(0);

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String datasourceUrn = expected.getIdentifiableStatisticalResource().getUrn();

        Mockito.doThrow(ApplicationException.class).when(datasetRepositoriesServiceFacade).deleteObservationsByAttributeInstanceValue(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(InternationalStringDto.class));

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_DATA_DELETE_ERROR, expected.getIdentifiableStatisticalResource().getCode()));

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testDeleteDatasourceImportationInProgress() throws Exception {
        Datasource expected = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME);
        DatasetVersion expectedDatasetVersion = expected.getDatasetVersion();
        String datasetUrn = expectedDatasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String datasourceUrn = expected.getIdentifiableStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testDeleteDatasourceUpdateDatasetVersionDataMetadata() throws Exception {
        Datasource expected = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME);
        DatasetVersion expectedDatasetVersion = expected.getDatasetVersion();
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositoryEmpty();

        String datasourceUrn = expected.getIdentifiableStatisticalResource().getUrn();

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);

        DatasetVersion actualDatasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), datasetVersionUrn);
        assertEquals(0, actualDatasetVersion.getDatasources().size());

        DataMockUtils.fillDatasetVersionWithEmptyCalculatedMetadataFromData(expectedDatasetVersion);

        assertEqualsDatasetVersion(expectedDatasetVersion, actualDatasetVersion);
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testDeleteDatasourceErrorNotExists() throws Exception {
        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASOURCE_NOT_FOUND, URN_NOT_EXISTS));

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), URN_NOT_EXISTS, Boolean.TRUE);
    }

    @Test
    @MetamacMock({DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME})
    public void testRetrieveDatasourcesByDatasetVersion() throws Exception {
        Dataset dataset = datasetMockFactory.retrieveMock(DATASET_03_BASIC_WITH_2_DATASET_VERSIONS_NAME);

        for (DatasetVersion datasetVersion : dataset.getVersions()) {
            String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
            List<Datasource> expected = datasetVersion.getDatasources();

            List<Datasource> actual = datasetService.retrieveDatasourcesByDatasetVersion(getServiceContextWithoutPrincipal(), datasetVersionUrn);
            assertEquals(expected.size(), actual.size());
            assertEqualsDatasourceCollection(expected, actual);
        }
    }

    @Test
    @MetamacMock({DATASOURCE_01_BASIC_NAME, DATASOURCE_02_BASIC_NAME})
    public void testUpdateLastUpdateDateOnDatasourceDeletion() throws Exception {
        Datasource datasource = datasourceMockFactory.retrieveMock(DATASOURCE_01_BASIC_NAME);
        DatasetVersion expectedDatasetVersion = datasource.getDatasetVersion();
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();

        String datasourceUrn = datasource.getIdentifiableStatisticalResource().getUrn();

        DateTime oldLastUpdate = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate();

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), datasourceUrn, Boolean.TRUE);

        // retrieve dataset version, lastupdate must have been changed
        DatasetVersion datasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), datasetVersionUrn);
        assertNotNull(datasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate());
        assertFalse(datasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().equals(oldLastUpdate));
    }

    private void testCanNotDeleteDatasourceInCurrentProcStatus(String datasetVersionMockName) throws MetamacException {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);
        String datasetVersionUrn = expectedDatasetVersion.getSiemacMetadataStatisticalResource().getUrn();

        Datasource expected = expectedDatasetVersion.getDatasources().get(0);

        expectedMetamacException(new MetamacException(ServiceExceptionType.DATASET_VERSION_CANT_ALTER_DATASOURCES, datasetVersionUrn));

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), expected.getIdentifiableStatisticalResource().getUrn(), Boolean.TRUE);

        fail("Should have thrown Exception and should not deleted datasource");
    }

    private void testCanDeleteDatasourceInCurrentProcStatus(String datasetVersionMockName) throws MetamacException {
        DatasetVersion expectedDatasetVersion = datasetVersionMockFactory.retrieveMock(datasetVersionMockName);

        Datasource expected = expectedDatasetVersion.getDatasources().get(0);

        datasetService.deleteDatasource(getServiceContextWithoutPrincipal(), expected.getIdentifiableStatisticalResource().getUrn(), Boolean.TRUE);
    }

    private void mockDsdAndDataRepositorySimpleDimensionsNoAttributes() throws Exception {
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
    }

    private void mockDsdAndDataRepositorySimpleDimensionsWithObservationAttributes() throws Exception {
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsWithObservationAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
    }

    private void mockDsdAndDataRepositoryEmpty() throws Exception {
        DataMockUtils.mockDsdAndDataRepositoryEmpty(datasetRepositoriesServiceFacade, srmRestInternalService);
    }

    private void mockTaskInProgressForResource(String datasetUrn, boolean status) throws MetamacException {
        TaskMockUtils.mockTaskInProgressForDatasetVersion(taskService, datasetUrn, status);
    }

    private void mockAllTaskInProgressForResource(boolean status) throws MetamacException {
        TaskMockUtils.mockAllTaskInProgressForDatasetVersion(taskService, status);
    }
}
