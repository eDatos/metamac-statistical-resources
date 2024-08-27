package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class GeoCovVarElementCacheDatasetVersionRepositoryTest extends AbstractDbUnitJpaTests implements GeoCovVarElementCacheDatasetVersionRepositoryTestBase {

    @Autowired
    protected GeoCovVarElementCacheDatasetVersionRepository geoCovVarElementCacheDatasetVersionRepository;

    @Test
    public void testDeleteAll() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteAll not implemented");
    }

    @Test
    public void testDisabledByDatasetVersionUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testDisabledByDatasetVersionUrn not implemented");
    }

    @Test
    public void testRetrieveByDatasetVersionUrn() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveByDatasetVersionUrn not implemented");
    }
}
