package org.siemac.metamac.statistical.resources.core.geocache.repositoryimpl;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class GeoCacheByRelatedResourceRepositoryTest extends AbstractDbUnitJpaTests implements GeoCacheByRelatedResourceRepositoryTestBase {

    @Autowired
    protected GeoCacheByRelatedResourceRepository geoCacheByRelatedResourceRepository;

    // TODO EDATOS-4587

    @Test
    public void testDeleteAll() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteAll not implemented");
    }

    @Override
    public void testDisabledByResourceVersionUrn() throws Exception {
        // TODO Auto-generated method stub

    }
}
