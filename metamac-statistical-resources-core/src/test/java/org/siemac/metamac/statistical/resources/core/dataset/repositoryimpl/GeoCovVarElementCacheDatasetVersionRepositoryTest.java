package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class GeoCovVarElementCacheDatasetVersionRepositoryTest
    extends AbstractDbUnitJpaTests
    implements GeoCovVarElementCacheDatasetVersionRepositoryTestBase {
    @Autowired
    protected GeoCovVarElementCacheDatasetVersionRepository geoCovVarElementCacheDatasetVersionRepository;

    @Test
    public void testRetrieveByDatasetVersionUrn() throws Exception {
        // TODO EDATOS-3770 implements this test.
    }
    
    @Test
    public void testDeleteAllByDatasetVersionUrn() throws Exception {
        // TODO EDATOS-3770 implements this test.
    }
}
