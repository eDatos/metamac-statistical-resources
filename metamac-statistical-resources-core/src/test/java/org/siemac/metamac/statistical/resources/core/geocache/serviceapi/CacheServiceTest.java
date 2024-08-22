package org.siemac.metamac.statistical.resources.core.geocache.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class CacheServiceTest extends AbstractDbUnitJpaTests implements CacheServiceTestBase {

    @Autowired
    protected CacheService cacheService;

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
}
