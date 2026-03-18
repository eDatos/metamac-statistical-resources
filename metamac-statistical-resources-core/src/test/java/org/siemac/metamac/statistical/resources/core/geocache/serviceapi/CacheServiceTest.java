package org.siemac.metamac.statistical.resources.core.geocache.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheByRelatedResourceMockFactory.GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_01_FOR_CACHE;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheByRelatedResourceMockFactory.GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_02_FOR_CACHE_PREVIOUS_VERSION;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheByRelatedResourceMockFactory.GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_08_BY_RELATED_RESOURCE;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_09_BY_RELATED_RESOURCE;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_10_BY_RELATED_RESOURCE_QUERY;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCacheResourceMockFactory.GEO_COV_VAR_ELEMENT_CACHE_QUERY_RESOURCE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_09_BASIC_FOR_CACHE_RESOURCE_01;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationMockFactory.PUBLICATION_10_BASIC_FOR_CACHE_RESOURCE_02;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_02;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_02_PREVIOUS_VERSION;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.asserts.GeoCacheAssertUtils;
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

    @Override
    @Test
    @MetamacMock({PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME, GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07})
    public void testFindGeoRelatedResourcesByCondition() throws Exception {

        GeoCacheByRelatedResource geoCacheByRelatedResource = geoCacheByRelatedResourceFactory.retrieveMock(GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class)
                .withProperty(GeoCacheByRelatedResourceProperties.relatedResources().territories().code()).eq(GeoCacheResourceMockFactory.VARIABLE_ELEMENT_01)
                .orderBy(GeoCacheByRelatedResourceProperties.code()).ascending().build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<GeoCacheByRelatedResource> resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(1, resourcesPagedResult.getTotalRows());

        GeoCacheByRelatedResource collectionResult = resourcesPagedResult.getValues().get(0);

        assertEquals(geoCacheByRelatedResource.getUrn(), collectionResult.getUrn());

        assertEquals(3, collectionResult.getRelatedResources().size());

        GeoCacheAssertUtils.assertEqualsArrayGeoCacheResource(collectionResult.getRelatedResources(), geoCacheByRelatedResource.getRelatedResources(), collectionResult.getUrn());

    }

    @Test
    @MetamacMock({PUBLICATION_07_WITH_TWO_VERSIONS_LAST_ONE_READY_TO_PUBLISHED_NAME, GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07})
    public void testFindGeoRelatedResourcesByUrn() throws Exception {

        GeoCacheByRelatedResource geoCacheByRelatedResource = geoCacheByRelatedResourceFactory.retrieveMock(GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_07);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn())
                .eq(geoCacheByRelatedResource.getUrn()).and().withProperty(GeoCacheByRelatedResourceProperties.isActivated()).eq(true).build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1, true);
        PagedResult<GeoCacheByRelatedResource> resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(1, resourcesPagedResult.getTotalRows());

        GeoCacheByRelatedResource collectionResult = resourcesPagedResult.getValues().get(0);

        assertEquals(geoCacheByRelatedResource.getUrn(), collectionResult.getUrn());
    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_09_BASIC_FOR_CACHE_RESOURCE_01, GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_01_FOR_CACHE})
    public void testDeleteRelatedResourceOldVersions() throws Exception {

        GeoCacheByRelatedResource geoCacheByRelatedResource = geoCacheByRelatedResourceFactory.retrieveMock(GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_01_FOR_CACHE);

        cacheService.deleteRelatedResourceOldVersions(getServiceContextAdministrador(), geoCacheByRelatedResource.getUrn());

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn())
                .eq(geoCacheByRelatedResource.getUrn()).and().withProperty(GeoCacheByRelatedResourceProperties.isActivated()).eq(true).build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1, true);
        PagedResult<GeoCacheByRelatedResource> resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(0, resourcesPagedResult.getTotalRows());

    }

    @Override
    @Test
    @MetamacMock({PUBLICATION_10_BASIC_FOR_CACHE_RESOURCE_02, PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_02_PREVIOUS_VERSION, PUBLICATION_VERSION_FOR_GEOGRAPHICAL_CACHE_02,
            GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_02_FOR_CACHE_PREVIOUS_VERSION, GEO_COV_VAR_ELEMENT_CACHE_08_BY_RELATED_RESOURCE, GEO_COV_VAR_ELEMENT_CACHE_09_BY_RELATED_RESOURCE,
            GEO_COV_VAR_ELEMENT_CACHE_10_BY_RELATED_RESOURCE_QUERY})
    public void testProcessGeoCacheRelatedCollection() throws Exception {
        Publication publication = publicationMockFactory.retrieveMock(PUBLICATION_10_BASIC_FOR_CACHE_RESOURCE_02);

        PublicationVersion publicationVersionPrevious = publication.getVersions().get(0);

        PublicationVersion publicationVersionLatest = publication.getVersions().get(1);

        GeoCacheByRelatedResource geoCacheByRelatedResource = geoCacheByRelatedResourceFactory.retrieveMock(GEO_CACHE_BY_RELATED_RESOURCE_03_COLLECTION_02_FOR_CACHE_PREVIOUS_VERSION);

        Set<GeoCacheResource> expectedGeoCacheResourceInLatestPublication = new HashSet<>();
        expectedGeoCacheResourceInLatestPublication.add(geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_08_BY_RELATED_RESOURCE));
        expectedGeoCacheResourceInLatestPublication.add(geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_09_BY_RELATED_RESOURCE));
        expectedGeoCacheResourceInLatestPublication.add(geoCacheResourceFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_10_BY_RELATED_RESOURCE_QUERY));

        // cache for previous version
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn())
                .eq(geoCacheByRelatedResource.getUrn()).and().withProperty(GeoCacheByRelatedResourceProperties.isActivated()).eq(true).build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1, true);
        PagedResult<GeoCacheByRelatedResource> resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(resourcesPagedResult.getValues().get(0).getUrn(), publicationVersionPrevious.getSiemacMetadataStatisticalResource().getUrn());

        // cache for latest version. Test that is not in cache
        conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn())
                .eq(publicationVersionLatest.getSiemacMetadataStatisticalResource().getUrn()).and().withProperty(GeoCacheByRelatedResourceProperties.isActivated()).eq(true).build();

        resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(0, resourcesPagedResult.getTotalRows());

        // insert lastest version collection in cache
        cacheService.processGeoCacheRelatedCollection(getServiceContextAdministrador(), publicationVersionLatest, true, publicationVersionLatest.getSiemacMetadataStatisticalResource().getUrn());

        // cache for latest version. Test that is in cache now
        conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheByRelatedResource.class).withProperty(GeoCacheByRelatedResourceProperties.urn())
                .eq(publicationVersionLatest.getSiemacMetadataStatisticalResource().getUrn()).and().withProperty(GeoCacheByRelatedResourceProperties.isActivated()).eq(true).build();

        resourcesPagedResult = cacheService.findGeoRelatedResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);

        assertEquals(1, resourcesPagedResult.getTotalRows());

        GeoCacheAssertUtils.assertEqualsArrayGeoCacheResource(resourcesPagedResult.getValues().get(0).getRelatedResources(), expectedGeoCacheResourceInLatestPublication,
                publicationVersionLatest.getSiemacMetadataStatisticalResource().getUrn());

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

    @Override
    public void testProcessGeoCacheRelatedMultidataset() throws Exception {
        // no test

    }
}
