package org.siemac.metamac.statistical.resources.core.geocache.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCovVarElementCacheDatasetVersionFactory.GEO_COV_VAR_ELEMENT_CACHE_01;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceRepository;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.GeoCovVarElementCacheDatasetVersionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class CacheServiceTest extends StatisticalResourcesBaseTest implements CacheServiceTestBase {

    @Autowired
    protected CacheService             cacheService;

    @Autowired
    private GeoCacheResourceRepository geoCacheResourceRepository;

    // TODO EDATOS-4587 VER TESTS

    @Test
    public void testRetrieveQueryVersionByUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveQueryVersionByUrn not implemented");
    }

    @Test
    public void testFindQueryVersionByUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindQueryVersionByUrn not implemented");
    }

    @Override
    public void testUpdateAllGeoCacheResourcesByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testDisabledResourceByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testDeleteDisabledCacheEntries() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testUpdateGeoCacheExternalResource() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testUpdateGeoCacheResource() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testUpdateGeoCacheByRelatedResource() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testUpdateAllGeoCacheRelatedResourcesByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testDisabledRelatedResourceByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testDeleteDisabledRelatedResourceCacheEntries() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testRetrieveGeoCacheResourceByUrn() throws Exception {
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
    @MetamacMock(GEO_COV_VAR_ELEMENT_CACHE_01)
    public void testFindResourcesByCondition() throws Exception {
        GeoCacheResource actual = geoCovVarElementCacheDatasetVersionFactory.retrieveMock(GEO_COV_VAR_ELEMENT_CACHE_01);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.territories().variableElement().code())
                .eq(GeoCovVarElementCacheDatasetVersionFactory.VARIABLE_ELEMENT_01).orderBy(GeoCacheResourceProperties.code()).ascending().build();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<GeoCacheResource> resourcesPagedResult = cacheService.findResourcesByCondition(getServiceContextWithoutPrincipal(), conditions, pagingParameter);
        assertEquals(1, resourcesPagedResult.getTotalRows());
        assertEquals(actual.getUrn(), resourcesPagedResult.getValues().get(0).getUrn());

    }

    @Override
    public void testFindGeoRelatedResourcesByCondition() throws Exception {
        // TODO Auto-generated method stub

    }

}
