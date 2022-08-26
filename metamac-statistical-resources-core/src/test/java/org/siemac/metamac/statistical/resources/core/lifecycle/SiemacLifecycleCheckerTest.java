package org.siemac.metamac.statistical.resources.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.checker.ExternalItemChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.checker.RelatedResourceChecker;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;

import static org.junit.Assert.fail;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.statistical.resources.core.utils.LifecycleTestUtils.prepareToValidationRejectedFromProductionValidationSiemac;

/*
 * No spring context, we set the SUT (Software under test) dependencies with mocked objects. Unit testing style ;)
 */

@RunWith(MockitoJUnitRunner.class)
public class SiemacLifecycleCheckerTest extends StatisticalResourcesBaseTest {

    @InjectMocks
    private final SiemacLifecycleChecker   siemacLifecycleServiceImpl = new SiemacLifecycleChecker();

    @Mock
    private LifecycleChecker               lifecycleService;

    @Mock
    private ExternalItemChecker            externalItemChecker;

    @SuppressWarnings("unused")
    @Mock
    private RelatedResourceChecker         relatedResourceChecker;

    @Mock
    private LifecycleCommonMetadataChecker lifecycleCommonMetadataChecker;

    @After
    public void checkMockitoUsage() {
        validateMockitoUsage();
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Test
    public void testCheckSendToProductionValidationRequiredFields() throws Exception {
        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        HasSiemacMetadata mockedResource = mock(HasSiemacMetadata.class);
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());
        when(mockedResource.getSiemacMetadataStatisticalResource()).thenReturn(new SiemacMetadataStatisticalResource());

        siemacLifecycleServiceImpl.checkSendToProductionValidation(mockedResource, baseMetadata, exceptionItems);

        // NO specific checks for siemac
        Assert.assertEquals(0, exceptionItems.size());

        verify(lifecycleService, times(1)).checkSendToProductionValidation(any(HasLifecycle.class), anyString(), anyListOf(MetamacExceptionItem.class));
        verify(lifecycleCommonMetadataChecker, times(1)).checkSiemacCommonMetadata(any(HasSiemacMetadata.class), anyString(), anyListOf(MetamacExceptionItem.class));
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Test
    public void testCheckSendToDiffusionValidationRequiredFields() throws Exception {
        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        HasSiemacMetadata mockedResource = mock(HasSiemacMetadata.class);
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());
        when(mockedResource.getSiemacMetadataStatisticalResource()).thenReturn(new SiemacMetadataStatisticalResource());

        siemacLifecycleServiceImpl.checkSendToDiffusionValidation(mockedResource, baseMetadata, exceptionItems);

        // NO specific checks for siemac
        Assert.assertEquals(0, exceptionItems.size());

        verify(lifecycleService, times(1)).checkSendToDiffusionValidation(any(HasLifecycle.class), anyString(), anyListOf(MetamacExceptionItem.class));
        verify(lifecycleCommonMetadataChecker, times(1)).checkSiemacCommonMetadata(any(HasSiemacMetadata.class), anyString(), anyListOf(MetamacExceptionItem.class));
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Test
    public void testCheckSendToValidationRejectedRequiredFields() throws Exception {
        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        HasSiemacMetadata mockedResource = mock(HasSiemacMetadata.class);
        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(new LifeCycleStatisticalResource());
        when(mockedResource.getSiemacMetadataStatisticalResource()).thenReturn(new SiemacMetadataStatisticalResource());

        prepareToValidationRejectedFromProductionValidationSiemac(mockedResource);

        siemacLifecycleServiceImpl.checkSendToValidationRejected(mockedResource, baseMetadata, exceptionItems);

        // NO specific checks for siemac
        Assert.assertEquals(0, exceptionItems.size());

        verify(lifecycleService, times(1)).checkSendToValidationRejected(any(HasLifecycle.class), anyString(), anyListOf(MetamacExceptionItem.class));
        verify(lifecycleCommonMetadataChecker, times(1)).checkSiemacCommonMetadata(any(HasSiemacMetadata.class), anyString(), anyListOf(MetamacExceptionItem.class));
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Test
    public void testCheckSendToPublishedFirstVersion() throws Exception {
        String baseMetadata = ServiceExceptionSingleParameters.DATASET_VERSION;
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();

        HasSiemacMetadata mockedResource = mock(HasSiemacMetadata.class);

        SiemacMetadataStatisticalResource siemacMetadata = new SiemacMetadataStatisticalResource();

        fillSiemacWithExternalItemMocks(siemacMetadata);

        when(mockedResource.getLifeCycleStatisticalResource()).thenReturn(siemacMetadata);
        when(mockedResource.getSiemacMetadataStatisticalResource()).thenReturn(siemacMetadata);

        siemacLifecycleServiceImpl.checkSendToPublished(mockedResource, null, baseMetadata, exceptionItems);

        // NO specific checks for siemac
        Assert.assertEquals(0, exceptionItems.size());

        verifyExternalItemsSiemac(siemacMetadata);

        verify(lifecycleService, times(1)).checkSendToPublished(any(HasLifecycle.class), any(HasLifecycle.class), anyString(), anyListOf(MetamacExceptionItem.class));
        verify(lifecycleCommonMetadataChecker, times(1)).checkSiemacCommonMetadata(any(HasSiemacMetadata.class), anyString(), anyListOf(MetamacExceptionItem.class));
    }

    private void verifyExternalItemsSiemac(SiemacMetadataStatisticalResource siemacMetadata) throws MetamacException {
        verifyExternalItemExternallyPublished(siemacMetadata.getLanguage());
        verifyExternalItemsExternallyPublished(siemacMetadata.getLanguages());
        verifyExternalItemsExternallyPublished(siemacMetadata.getStatisticalOperationInstances());
        verifyExternalItemExternallyPublished(siemacMetadata.getCreator());
        verifyExternalItemsExternallyPublished(siemacMetadata.getDataProvider());
        verifyExternalItemsExternallyPublished(siemacMetadata.getContributor());
        verifyExternalItemsExternallyPublished(siemacMetadata.getPublisher());
        verifyExternalItemsExternallyPublished(siemacMetadata.getMediator());
    }

    private void verifyExternalItemExternallyPublished(ExternalItem item) throws MetamacException {
        verify(externalItemChecker).checkExternalItemsExternallyPublished(Mockito.eq(item), anyString(), Mockito.anyListOf(MetamacExceptionItem.class));
    }

    private void verifyExternalItemsExternallyPublished(List<ExternalItem> items) throws MetamacException {
        verify(externalItemChecker).checkExternalItemsExternallyPublished(Mockito.eq(items), anyString(), Mockito.anyListOf(MetamacExceptionItem.class));
    }

    private void fillSiemacWithExternalItemMocks(SiemacMetadataStatisticalResource siemacMetadata) {
        siemacMetadata.setLanguage(StatisticalResourcesDoMocks.mockCodeExternalItem("es"));

        siemacMetadata.addLanguage(StatisticalResourcesDoMocks.mockCodeExternalItem("es"));
        siemacMetadata.addLanguage(StatisticalResourcesDoMocks.mockCodeExternalItem("en"));

        siemacMetadata.addStatisticalOperationInstance(StatisticalResourcesDoMocks.mockStatisticalOperationInstanceExternalItem());
        siemacMetadata.addStatisticalOperationInstance(StatisticalResourcesDoMocks.mockStatisticalOperationInstanceExternalItem());

        siemacMetadata.setCreator(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());

        siemacMetadata.addDataProvider(StatisticalResourcesDoMocks.mockDataProviderExternalItem());
        siemacMetadata.addDataProvider(StatisticalResourcesDoMocks.mockDataProviderExternalItem());
        
        siemacMetadata.addContributor(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());
        siemacMetadata.addContributor(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());

        siemacMetadata.addPublisher(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());
        siemacMetadata.addPublisher(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());

        siemacMetadata.addPublisherContributor(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());
        siemacMetadata.addPublisherContributor(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());

        siemacMetadata.addMediator(StatisticalResourcesDoMocks.mockOrganizationUnitExternalItem());

        siemacMetadata.setCommonMetadata(StatisticalResourcesDoMocks.mockCommonConfigurationExternalItem());
    }

    @Ignore
    @Test
    public void testCheckExternalItemsPreviouslyPublished() throws Exception {
        // TODO: Implementar (METAMAC-2143)
        fail("not implemented");
    }

    @Ignore
    @Test
    public void testCheckRelatedResourcesPreviouslyPublished() throws Exception {
        // TODO: Implementar (METAMAC-2143)
        fail("not implemented");
    }
}
