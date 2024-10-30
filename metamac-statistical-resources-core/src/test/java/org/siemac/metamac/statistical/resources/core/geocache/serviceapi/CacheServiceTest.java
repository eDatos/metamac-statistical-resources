package org.siemac.metamac.statistical.resources.core.geocache.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheByRelatedResourceMockFactory.GEO_CACHE_BY_RELATED_RESOURCE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.mock.Mocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/task-mockito.xml", "classpath:spring/statistical-resources/include/rest-services-mockito.xml",
        "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class CacheServiceTest extends StatisticalResourcesBaseTest implements CacheServiceTestBase {

    @Autowired
    protected CacheService         cacheService;

    @Autowired
    private SrmRestInternalService srmRestInternalService;

    Codes                          geoCodes = Mocks.mock_CL_AREA_ES();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockRestService();
    }

    @After
    public void after() {
        Mockito.validateMockitoUsage();
    }

    public void mockRestService() throws MetamacException {
        // Codes of codelist with geographical values
        Mockito.doReturn(geoCodes).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently(Mockito.anyString());

    }

    // TODO EDATOS-4587 VER TESTS

    @Override
    public void testUpdateGeoCacheByRelatedResource() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testUpdateRelatedResourceByCacheResource() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testCreateRelatedResourceByCacheResourceByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    @Test
    @MetamacMock(GEO_CACHE_BY_RELATED_RESOURCE_01)
    public void testFindGeoRelatedResourcesByCondition() throws Exception {
        GeoCacheByRelatedResource expected = geoCacheByRelatedResourceFactory.retrieveMock(GEO_CACHE_BY_RELATED_RESOURCE_01);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn()).eq(expected.getUrn())
                .orderBy(GeoCacheByRelatedResourceProperties.code()).ascending().build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<GeoCacheByRelatedResource> resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
        assertEquals(1, resourcesPagedResult.getTotalRows());
        assertEquals(expected.getUrn(), resourcesPagedResult.getValues().get(0).getUrn());

    }

    @Override
    public void testDeleteRelatedResourceOldVersions() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testProcessGeoCacheRelatedCollection() throws Exception {
        // TODO Auto-generated method stub

    }

    //////////////////
    // GEO_CACHE_RESOURCE
    /////////////////

    @Override
    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_01)
    public void testRetrieveGeoCacheResourceByUrn() throws Exception {
        GeoCacheResource expected = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_01);

        GeoCacheResource actual = cacheService.retrieveGeoCacheResourceByUrn(getServiceContextWithoutPrincipal(), expected.getUrn());

        assertEquals(expected.getUrn(), actual.getUrn());

    }

    @Override
    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_01)
    public void testFindResourcesByCondition() throws Exception {
        GeoCacheResource actual = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_01);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.territories().code())
                .eq(GeoCacheResourceMockFactory.VARIABLE_ELEMENT_01).orderBy(GeoCacheResourceProperties.code()).ascending().build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<GeoCacheResource> resourcesPagedResult = cacheService.findResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
        assertEquals(1, resourcesPagedResult.getTotalRows());
        assertEquals(actual.getUrn(), resourcesPagedResult.getValues().get(0).getUrn());

    }

    @Test
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01)
    public void testFindQueryResourcesByCondition() throws Exception {
        GeoCacheResource actual = geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.territories().code())
                .eq(GeoCacheResourceMockFactory.VARIABLE_ELEMENT_01).orderBy(GeoCacheResourceProperties.code()).ascending().build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<GeoCacheResource> resourcesPagedResult = cacheService.findResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
        assertEquals(1, resourcesPagedResult.getTotalRows());
        assertEquals(actual.getUrn(), resourcesPagedResult.getValues().get(0).getUrn());

    }

    @Override
    @Test
    public void testProcessUpdateGeoCacheResource() throws Exception {
        DatasetVersion dv = DatasetVersionMockFactory.createDatasetVersionInStatusWithGeneratedDatasource(1, ProcStatusEnum.PUBLISHED);

        List<ExternalItem> territories = new ArrayList<>();

        // get two random geo codes.
        territories.add(StatisticalResourcesDoMocks.buildCodeExternalItemFromCodeResourceInternal(geoCodes.getCodes().get(0)));
        territories.add(StatisticalResourcesDoMocks.buildCodeExternalItemFromCodeResourceInternal(geoCodes.getCodes().get(1)));

        cacheService.processUpdateGeoCacheResource(getServiceContextAdministrador(), dv.getLifeCycleStatisticalResource(), dv.getSiemacMetadataStatisticalResource().getUrn(),
                StatisticalResourceTypeEnum.DATASET, territories, true);

        GeoCacheResource actual = cacheService.retrieveGeoCacheResourceByUrn(getServiceContextWithoutPrincipal(), dv.getSiemacMetadataStatisticalResource().getUrn());

        assertEquals(actual.getUrn(), dv.getSiemacMetadataStatisticalResource().getUrn());

        assertEquals(actual.getTerritories().size(), territories.size());
        assertEquals(actual.getTerritories().get(0).getUrn(), geoCodes.getCodes().get(0).getVariableElement().getUrn());
        assertEquals(actual.getTerritories().get(1).getUrn(), geoCodes.getCodes().get(1).getVariableElement().getUrn());

    }

    //////////////////
    // WITHOUT TEST
    /////////////////

    @Override
    public void testUpdateAllGeoCacheResourcesByUrn() throws Exception {
        // no test
    }

    @Override
    public void testDisabledResourceByUrn() throws Exception {
        // no test. Tested in GeoCacheResourceRepositoryTest.java
    }

    @Override
    public void testDeleteDisabledCacheEntries() throws Exception {
        // no test. Tested in GeoCacheResourceRepositoryTest.java
    }

    @Override
    public void testUpdateGeoCacheExternalResource() throws Exception {
        // no test

    }

    @Override
    public void testUpdateCollectionExternalPublicationCache() throws Exception {
        // no test

    }

    @Override
    public void testUpdateDatasetExternalPublicationCache() throws Exception {
        // no test

    }

    @Override
    public void testUpdateAllGeographicExternalCoverageVariableElementsCache() throws Exception {
        // no test

    }
}
