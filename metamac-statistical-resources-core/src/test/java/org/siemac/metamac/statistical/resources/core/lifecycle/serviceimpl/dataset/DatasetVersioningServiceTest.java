package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.dataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.BaseAsserts.assertEqualsVersioningSiemacMetadata;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts.assertEqualsExternalItem;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts.assertEqualsExternalItemCollection;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts.assertEqualsInternationalString;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_105_MAXIMUM_VERSION_REACHED;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_106_MAXIMUM_MINOR_VERSION_REACHED;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_21_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory.DATASET_VERSION_69_PUBLISHED_NO_ROOT_MAINTAINER_NAME;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetService;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleService;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskService;
import org.siemac.metamac.statistical.resources.core.utils.DataMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.siemac.metamac.statistical.resources.core.utils.TaskMockUtils;
import org.siemac.metamac.statistical.resources.core.utils.asserts.BaseAsserts;
import org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class DatasetVersioningServiceTest extends StatisticalResourcesBaseTest {

    private static final String              TEMPORAL_CODE_COMPARE_FIELD = "identifier";

    @Autowired
    @Qualifier("datasetLifecycleService")
    private LifecycleService<DatasetVersion> datasetVersionLifecycleService;

    @Autowired
    private DatasetService                   datasetService;

    @Autowired
    private TaskService                      taskService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private SrmRestInternalService           srmRestInternalService;

    @Before
    // Get reference to dataset repository without proxy (verify not working with proxy)
    public void fixInterceptorsMockito() throws Exception {
        datasetRepositoriesServiceFacade = (DatasetRepositoriesServiceFacade) ((Advised) datasetRepositoriesServiceFacade).getTargetSource().getTarget();

        mockDsdAndDataRepositorySimpleDimensionsNoAttributes();
    }

    @After
    public void validateUsage() {
        Mockito.validateMockitoUsage();
    }

    @Test
    @MetamacMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME)
    public void testVersioningDatasetVersion() throws Exception {
        String previousDatasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME).getSiemacMetadataStatisticalResource().getUrn();

        DatasetVersion newDatasetVersion = datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousDatasetVersionUrn, VersionTypeEnum.MINOR);
        DatasetVersion previousDatasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), previousDatasetVersionUrn);

        assertNotNull(newDatasetVersion);
        assertFalse(previousDatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic().equals(newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic()));
        checkNewDatasetVersionCreated(previousDatasetVersion, newDatasetVersion);

        assertTrue(CollectionUtils.isEmpty(newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes()));
        assertNull(newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationale());
    }

    @Test
    @MetamacMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME)
    public void testVersioningDatasetVersionCheckUrnIsCorrectForMinorChange() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME);

        // Necessary data for construct URN
        VersionTypeEnum versionType = VersionTypeEnum.MINOR;
        String datasetVersionCode = datasetVersion.getSiemacMetadataStatisticalResource().getCode();
        String[] maintainer = new String[]{datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};
        String versionBefore = datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic();

        // New dataset version
        DatasetVersion newDatasetVersion = datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), versionType);
        datasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Expected URN
        String versionAfter = StatisticalResourcesVersionUtils.createNextVersion(versionBefore, versionType).getValue();

        // Compare URNS
        String expectedUrn = GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(maintainer, datasetVersionCode, versionAfter);
        assertEquals(expectedUrn, newDatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME)
    public void testVersioningDatasetVersionCheckUrnIsCorrectForMayorChange() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME);

        // Necessary data for construct URN
        VersionTypeEnum versionType = VersionTypeEnum.MAJOR;
        String datasetVersionCode = datasetVersion.getSiemacMetadataStatisticalResource().getCode();
        String[] maintainer = new String[]{datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};
        String versionBefore = datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic();

        // New dataset version
        DatasetVersion newDatasetVersion = datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), versionType);
        datasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Expected URN
        String versionAfter = StatisticalResourcesVersionUtils.createNextVersion(versionBefore, versionType).getValue();

        // Compare URNS
        String expectedUrn = GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(maintainer, datasetVersionCode, versionAfter);
        assertEquals(expectedUrn, newDatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(DATASET_VERSION_69_PUBLISHED_NO_ROOT_MAINTAINER_NAME)
    public void testVersioningDatasetVersionCheckUrnIsCorrectForANonRootAgency() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_69_PUBLISHED_NO_ROOT_MAINTAINER_NAME);

        // Necessary data for construct URN
        VersionTypeEnum versionType = VersionTypeEnum.MINOR;
        String datasetVersionCode = datasetVersion.getSiemacMetadataStatisticalResource().getCode();
        String[] maintainer = new String[]{datasetVersion.getSiemacMetadataStatisticalResource().getMaintainer().getCodeNested()};
        String versionBefore = datasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic();

        // New dataset version
        DatasetVersion newDatasetVersion = datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), versionType);
        datasetVersion = datasetService.retrieveDatasetVersionByUrn(getServiceContextWithoutPrincipal(), datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Expected URN
        String versionAfter = StatisticalResourcesVersionUtils.createNextVersion(versionBefore, versionType).getValue();

        // Compare URNS
        String expectedUrn = GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(maintainer, datasetVersionCode, versionAfter);
        assertEquals(expectedUrn, newDatasetVersion.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME)
    public void testVersioningDatasetVersionErrorDraftProcStatus() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_16_DRAFT_READY_FOR_PRODUCTION_VALIDATION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, datasetVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(DATASET_VERSION_21_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME)
    public void testVersioningDatasetVersionErrorProductionValidationProcStatus() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_21_PRODUCTION_VALIDATION_READY_FOR_VALIDATION_REJECTED_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, datasetVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME)
    public void testVersioningDatasetVersionErrorDiffusionValidationProcStatus() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_64_DIFFUSION_VALIDATION_NOT_INITIAL_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, datasetVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME)
    public void testVersioningDatasetVersionErrorValidationRejectedProcStatus() throws Exception {
        String datasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_60_VALIDATION_REJECTED_INITIAL_VERSION_NAME).getSiemacMetadataStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, datasetVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME)
    public void testVersioningDatasetVersionImportationTaskInProgress() throws Exception {
        DatasetVersion datasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_14_OPER_03_CODE_01_PUBLISHED_NAME);

        String datasetVersionUrn = datasetVersion.getSiemacMetadataStatisticalResource().getUrn();
        String datasetUrn = datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn();

        mockTaskInProgressForResource(datasetUrn, true);
        expectedMetamacException(new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, datasetUrn));

        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), datasetVersionUrn, VersionTypeEnum.MINOR);
    }

    @Test
    @MetamacMock(DATASET_VERSION_105_MAXIMUM_VERSION_REACHED)
    public void testVersioningDatasetMinorVersionErrorMaximumVersionReached() throws Exception {
        String previousDatasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_105_MAXIMUM_VERSION_REACHED).getSiemacMetadataStatisticalResource().getUrn();

        VersionTypeEnum versionType = VersionTypeEnum.MINOR;

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(versionType, StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE).build());

        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousDatasetVersionUrn, versionType);
    }

    @Test
    @MetamacMock(DATASET_VERSION_105_MAXIMUM_VERSION_REACHED)
    public void testVersioningDatasetMajorVersionErrorMaximumVersionReached() throws Exception {
        String previousDatasetVersionUrn = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_105_MAXIMUM_VERSION_REACHED).getSiemacMetadataStatisticalResource().getUrn();

        VersionTypeEnum versionType = VersionTypeEnum.MAJOR;

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(versionType, StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE).build());

        datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousDatasetVersionUrn, versionType);
    }

    @Test
    @MetamacMock(DATASET_VERSION_106_MAXIMUM_MINOR_VERSION_REACHED)
    public void testVersioningDatasetMaximumMinorVersionReached() throws Exception {
        DatasetVersion previousDatasetVersion = datasetVersionMockFactory.retrieveMock(DATASET_VERSION_106_MAXIMUM_MINOR_VERSION_REACHED);

        DatasetVersion newDatasetVersion = datasetVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousDatasetVersion.getSiemacMetadataStatisticalResource().getUrn(),
                VersionTypeEnum.MINOR);

        assertEquals(StatisticalResourcesMockFactory.MAXIMUM_MINOR_VERSION_AVAILABLE, previousDatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic());
        assertEquals(StatisticalResourcesMockFactory.SECOND_VERSION, newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionLogic());

        assertTrue(CollectionUtils.isNotEmpty(newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes()));
        assertEquals(1, newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().size());
        assertEquals(VersionRationaleTypeEnum.MAJOR_OTHER, newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().get(0).getValue());
        assertEqualsInternationalString(getMinorChangeExpectedMajorVersionOccurredVersion(), newDatasetVersion.getSiemacMetadataStatisticalResource().getVersionRationale());
    }

    // -------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------

    private static void checkNewDatasetVersionCreated(DatasetVersion previous, DatasetVersion next) throws Exception {
        assertEqualsVersioningSiemacMetadata(previous.getSiemacMetadataStatisticalResource(), next.getSiemacMetadataStatisticalResource());
        checkInheritMetadata(previous, next);
        checkNotInheritMetadata(next);
        checkRelations(previous, next);
    }

    private static void checkRelations(DatasetVersion previous, DatasetVersion next) {
        // Dataset
        assertEquals(previous.getDataset().getId(), next.getDataset().getId());
        assertEquals(previous.getDataset().getIdentifiableStatisticalResource().getUrn(), next.getDataset().getIdentifiableStatisticalResource().getUrn());

        // Dataset repository Id
        assertNotSame(previous.getDatasetRepositoryId(), next.getDatasetRepositoryId());
        assertNotNull(next.getDatasetRepositoryId());

        // Datasources
        checkVersionedDatasourcesCollection(previous.getDatasources(), next.getDatasources());
    }

    private static void checkNotInheritMetadata(DatasetVersion next) {
        assertNotNull(next.getSiemacMetadataStatisticalResource().getUrn());
        assertEquals(next.getSiemacMetadataStatisticalResource().getUrn(), next.getDatasetRepositoryId());
        assertNull(next.getDateNextUpdate());
        assertNull(next.getBibliographicCitation());
        assertFalse(next.getUserModifiedDateNextUpdate());
    }

    private static void checkInheritMetadata(DatasetVersion previous, DatasetVersion next) throws Exception {
        checkVersionedCodeDimensionCollection(previous.getDimensionsCoverage(), next.getDimensionsCoverage());

        assertEqualsExternalItemCollection(previous.getGeographicCoverage(), next.getGeographicCoverage());
        StatisticalResourcesCollectionUtils.equalsCollectionByField(previous.getTemporalCoverage(), next.getTemporalCoverage(), TEMPORAL_CODE_COMPARE_FIELD, TEMPORAL_CODE_COMPARE_FIELD);
        assertEqualsExternalItemCollection(previous.getMeasureCoverage(), next.getMeasureCoverage());
        assertEqualsExternalItemCollection(previous.getGeographicGranularities(), next.getGeographicGranularities());
        assertEqualsExternalItemCollection(previous.getTemporalGranularities(), next.getTemporalGranularities());

        assertEqualsDate(previous.getDateStart(), next.getDateStart());
        assertEqualsDate(previous.getDateEnd(), next.getDateEnd());

        assertEqualsExternalItemCollection(previous.getStatisticalUnit(), next.getStatisticalUnit());

        assertEqualsExternalItem(previous.getRelatedDsd(), next.getRelatedDsd());

        assertEquals(previous.getFormatExtentObservations(), next.getFormatExtentObservations());
        assertEquals(previous.getFormatExtentDimensions(), next.getFormatExtentDimensions());

        assertEqualsExternalItem(previous.getUpdateFrequency(), next.getUpdateFrequency());
        DatasetsAsserts.assertEqualsStatisticOfficiality(previous.getStatisticOfficiality(), next.getStatisticOfficiality());
    }

    private static void checkVersionedCodeDimensionCollection(List<CodeDimension> expected, List<CodeDimension> actual) {
        if (expected != null) {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
            for (CodeDimension expectedItem : expected) {
                boolean match = false;
                for (CodeDimension actualItem : actual) {
                    try {
                        checkVersionedCodeDimension(expectedItem, actualItem);
                        match = true;
                    } catch (AssertionError e) {
                        continue;
                    }

                }

                if (!match) {
                    fail("Found elements in expected collection, which are not contained in actual collection. Element: " + expectedItem.getId());
                }
            }
        } else {
            assertNull(actual);
        }
    }

    public static void checkVersionedCodeDimension(CodeDimension expected, CodeDimension actual) {
        BaseAsserts.assertEqualsNullability(expected, actual);
        assertEquals(expected.getDsdComponentId(), actual.getDsdComponentId());
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertNotNull(expected.getDatasetVersion().getId());
        assertNotSame(expected.getDatasetVersion().getId(), actual.getDatasetVersion().getId());
    }

    public static void checkVersionedDatasourcesCollection(List<Datasource> expectedPrevious, List<Datasource> actualNew) {
        if (expectedPrevious != null) {
            assertNotNull(actualNew);
            assertEquals(expectedPrevious.size(), actualNew.size());
            for (Datasource expectedItem : expectedPrevious) {
                boolean match = false;
                for (Datasource actualItem : actualNew) {
                    try {
                        checkVersionedDatasource(expectedItem, actualItem);
                        match = true;
                    } catch (AssertionError e) {
                        continue;
                    }

                }

                if (!match) {
                    fail("Found elements in expected collection, which are not contained in actual collection. Element: " + expectedItem.getId());
                }
            }
        } else {
            assertNull(actualNew);
        }
    }

    private static void checkVersionedDatasource(Datasource expected, Datasource actualNewVersion) {
        assertEqualsVersionedIdentifiableDatasourceCodeIsDifferent(expected.getIdentifiableStatisticalResource(), actualNewVersion.getIdentifiableStatisticalResource());
        assertEquals(expected.getSourceName(), actualNewVersion.getSourceName());
        assertFalse(expected.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn().equals(actualNewVersion.getDatasetVersion().getSiemacMetadataStatisticalResource().getUrn()));
        assertNotNull(actualNewVersion.getIdentifiableStatisticalResource().getUrn());
    }

    public static void assertEqualsVersionedIdentifiableDatasourceCodeIsDifferent(IdentifiableStatisticalResource expected, IdentifiableStatisticalResource actual) {
        assertFalse(expected.getCode().equals(actual.getCode()));

        BaseAsserts.assertEqualsStatisticalResource(expected, actual);
    }

    private void mockTaskInProgressForResource(String datasetUrn, boolean status) throws MetamacException {
        TaskMockUtils.mockTaskInProgressForDatasetVersion(taskService, datasetUrn, status);
    }

    private void mockDsdAndDataRepositorySimpleDimensionsNoAttributes() throws Exception {
        DataMockUtils.mockDsdAndDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade, srmRestInternalService);
    }
}
