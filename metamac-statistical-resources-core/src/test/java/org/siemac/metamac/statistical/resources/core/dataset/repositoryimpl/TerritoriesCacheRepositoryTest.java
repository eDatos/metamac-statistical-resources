package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TerritoriesCacheRepositoryTest extends AbstractDbUnitJpaTests
    implements TerritoriesCacheRepositoryTestBase {
    @Autowired
    protected TerritoriesCacheRepository territoriesCacheRepository;

    @Test
    public void testRetrieveByTerritoryId() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveByTerritoryId not implemented");
    }
}
