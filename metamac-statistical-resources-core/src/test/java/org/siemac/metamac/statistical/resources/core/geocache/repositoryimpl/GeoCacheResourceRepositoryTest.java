package org.siemac.metamac.statistical.resources.core.geocache.repositoryimpl;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class GeoCacheResourceRepositoryTest extends AbstractDbUnitJpaTests implements GeoCacheResourceRepositoryTestBase {

    @Autowired
    protected GeoCacheResourceRepository geoCacheResourceRepository;

    // TODO EDATOS-4587 VER ESTOS TESTS

    @Test
    public void testDeleteAll() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteAll not implemented");
    }

    @Test
    public void testDisabledByResourceVersionUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testDisabledByResourceVersionUrn not implemented");
    }

    @Test
    public void testRetrieveByResourceVersionUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveByResourceVersionUrn not implemented");
    }
}
