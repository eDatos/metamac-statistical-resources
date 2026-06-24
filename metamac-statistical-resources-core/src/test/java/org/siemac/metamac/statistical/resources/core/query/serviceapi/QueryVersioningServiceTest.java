package org.siemac.metamac.statistical.resources.core.query.serviceapi;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.CommonAsserts.assertEqualsInternationalString;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDataset;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.DatasetsAsserts.assertEqualsDatasetVersion;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.QueryAsserts.assertEqualsSelection;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_11_DRAFT_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_14_VALIDATION_REJECTED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_15_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_59_MAXIMUM_VERSION_REACHED;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryVersionMockFactory.QUERY_VERSION_60_MAXIMUM_MINOR_VERSION_REACHED;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleService;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesVersionUtils;
import org.siemac.metamac.statistical.resources.core.utils.asserts.BaseAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/dataset-repository-mockito.xml", "classpath:spring/statistical-resources/include/rest-services-mockito.xml",
        "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class QueryVersioningServiceTest extends StatisticalResourcesBaseTest {

    @Autowired
    @Qualifier("queryLifecycleService")
    private LifecycleService<QueryVersion> queryVersionLifecycleService;

    @Autowired
    private QueryService                   queryService;

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testVersioningQueryVersion() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);

        QueryVersion queryNewVersion = queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn(), VersionTypeEnum.MINOR);
        assertNotNull(queryNewVersion);
        assertFalse(queryVersion.getLifeCycleStatisticalResource().getVersionLogic().equals(queryNewVersion.getLifeCycleStatisticalResource().getVersionLogic()));
        checkNewQueryVersionCreated(queryVersion, queryNewVersion);

        assertTrue(CollectionUtils.isEmpty(queryNewVersion.getLifeCycleStatisticalResource().getVersionRationaleTypes()));
        assertNull(queryNewVersion.getLifeCycleStatisticalResource().getVersionRationale());
    }

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testVersioningQueryVersionCheckUrnIsCorrectForMinorChange() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);

        // Necessary data for construct URN
        VersionTypeEnum versionType = VersionTypeEnum.MINOR;
        String queryVersionCode = queryVersion.getLifeCycleStatisticalResource().getCode();
        String[] maintainer = new String[]{queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested()};
        String versionBefore = queryVersion.getLifeCycleStatisticalResource().getVersionLogic();

        // New query version
        QueryVersion newQueryVersion = queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn(), versionType);
        queryVersion = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Expected URN
        String versionAfter = StatisticalResourcesVersionUtils.createNextVersion(versionBefore, versionType).getValue();

        // Compare URNS
        String expectedUrn = GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(maintainer, queryVersionCode, versionAfter);
        assertEquals(expectedUrn, newQueryVersion.getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testVersioningQueryVersionCheckUrnIsCorrectForMayorChange() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);

        // Necessary data for construct URN
        VersionTypeEnum versionType = VersionTypeEnum.MAJOR;
        String queryVersionCode = queryVersion.getLifeCycleStatisticalResource().getCode();
        String[] maintainer = new String[]{queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested()};
        String versionBefore = queryVersion.getLifeCycleStatisticalResource().getVersionLogic();

        // New query version
        QueryVersion newQueryVersion = queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn(), versionType);
        queryVersion = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Expected URN
        String versionAfter = StatisticalResourcesVersionUtils.createNextVersion(versionBefore, versionType).getValue();

        // Compare URNS
        String expectedUrn = GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(maintainer, queryVersionCode, versionAfter);
        assertEquals(expectedUrn, newQueryVersion.getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_15_PUBLISHED_NAME)
    public void testVersioningQueryVersionCheckUrnIsCorrectForANonRootAgency() throws Exception {
        QueryVersion queryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_15_PUBLISHED_NAME);

        // Necessary data for construct URN
        VersionTypeEnum versionType = VersionTypeEnum.MINOR;
        String queryVersionCode = queryVersion.getLifeCycleStatisticalResource().getCode();
        String[] maintainer = new String[]{queryVersion.getLifeCycleStatisticalResource().getMaintainer().getCodeNested()};
        String versionBefore = queryVersion.getLifeCycleStatisticalResource().getVersionLogic();

        // New query version
        QueryVersion newQueryVersion = queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn(), versionType);
        queryVersion = queryService.retrieveQueryVersionByUrn(getServiceContextWithoutPrincipal(), queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Expected URN
        String versionAfter = StatisticalResourcesVersionUtils.createNextVersion(versionBefore, versionType).getValue();

        // Compare URNS
        String expectedUrn = GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(maintainer, queryVersionCode, versionAfter);
        assertEquals(expectedUrn, newQueryVersion.getLifeCycleStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock(QUERY_VERSION_11_DRAFT_NAME)
    public void testVersioningQueryVersionErrorDraftProcStatus() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_11_DRAFT_NAME).getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, queryVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME)
    public void testVersioningQueryVersionErrorProductionValidationProcStatus() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_12_PRODUCTION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, queryVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME)
    public void testVersioningQueryVersionErrorDiffusionValidationProcStatus() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_13_DIFFUSION_VALIDATION_NAME).getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, queryVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME)
    public void testVersioningQueryVersionErrorValidationRejectedProcStatus() throws Exception {
        String queryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_14_VALIDATION_REJECTED_NAME).getLifeCycleStatisticalResource().getUrn();

        expectedMetamacException(new MetamacException(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS, queryVersionUrn, ProcStatusEnum.PUBLISHED.getName()));
        queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), queryVersionUrn, VersionTypeEnum.MAJOR);
    }

    @Test
    @MetamacMock(QUERY_VERSION_59_MAXIMUM_VERSION_REACHED)
    public void testVersioningQueryMinorVersionErrorMaximumVersionReached() throws Exception {
        String previousQueryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_59_MAXIMUM_VERSION_REACHED).getLifeCycleStatisticalResource().getUrn();

        VersionTypeEnum versionType = VersionTypeEnum.MINOR;

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(versionType, StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE).build());

        queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousQueryVersionUrn, versionType);
    }

    @Test
    @MetamacMock(QUERY_VERSION_59_MAXIMUM_VERSION_REACHED)
    public void testVersioningQueryMajorVersionErrorMaximumVersionReached() throws Exception {
        String previousQueryVersionUrn = queryVersionMockFactory.retrieveMock(QUERY_VERSION_59_MAXIMUM_VERSION_REACHED).getLifeCycleStatisticalResource().getUrn();

        VersionTypeEnum versionType = VersionTypeEnum.MAJOR;

        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(versionType, StatisticalResourcesMockFactory.MAXIMUM_VERSION_AVAILABLE).build());

        queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousQueryVersionUrn, versionType);
    }

    @Test
    @MetamacMock(QUERY_VERSION_60_MAXIMUM_MINOR_VERSION_REACHED)
    public void testVersioningQueryMaximumMinorVersionReached() throws Exception {
        QueryVersion previousQueryVersion = queryVersionMockFactory.retrieveMock(QUERY_VERSION_60_MAXIMUM_MINOR_VERSION_REACHED);

        QueryVersion newQueryVersion = queryVersionLifecycleService.versioning(getServiceContextWithoutPrincipal(), previousQueryVersion.getLifeCycleStatisticalResource().getUrn(),
                VersionTypeEnum.MINOR);

        assertEquals(StatisticalResourcesMockFactory.MAXIMUM_MINOR_VERSION_AVAILABLE, previousQueryVersion.getLifeCycleStatisticalResource().getVersionLogic());
        assertEquals(StatisticalResourcesMockFactory.SECOND_VERSION, newQueryVersion.getLifeCycleStatisticalResource().getVersionLogic());

        assertTrue(CollectionUtils.isNotEmpty(newQueryVersion.getLifeCycleStatisticalResource().getVersionRationaleTypes()));
        assertEquals(1, newQueryVersion.getLifeCycleStatisticalResource().getVersionRationaleTypes().size());
        assertEquals(VersionRationaleTypeEnum.MAJOR_OTHER, newQueryVersion.getLifeCycleStatisticalResource().getVersionRationaleTypes().get(0).getValue());
        assertEqualsInternationalString(getMinorChangeExpectedMajorVersionOccurredVersion(), newQueryVersion.getLifeCycleStatisticalResource().getVersionRationale());
    }

    // -------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------

    private static void checkNewQueryVersionCreated(QueryVersion previous, QueryVersion next) throws MetamacException {
        BaseAsserts.assertEqualsVersioningLifecycle(previous.getLifeCycleStatisticalResource(), next.getLifeCycleStatisticalResource());

        // Non inherited fields

        // Inherited
        assertEquals(previous.getLatestDataNumber(), next.getLatestDataNumber());
        if (previous.getFixedDatasetVersion() != null) {
            assertEqualsDatasetVersion(previous.getFixedDatasetVersion(), next.getFixedDatasetVersion());
        } else if (previous.getDataset() != null) {
            assertEqualsDataset(previous.getDataset(), next.getDataset());
        }
        assertEquals(previous.getQuery().getId(), next.getQuery().getId());
        assertEquals(previous.getStatus(), next.getStatus());
        assertEquals(previous.getQueryType(), next.getQueryType());
        assertEqualsSelection(previous.getSelection(), next.getSelection());
    }
}
