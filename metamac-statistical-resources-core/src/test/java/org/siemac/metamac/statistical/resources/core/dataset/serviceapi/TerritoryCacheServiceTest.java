package org.siemac.metamac.statistical.resources.core.dataset.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TerritoryCacheServiceTest extends AbstractDbUnitJpaTests
    implements TerritoryCacheServiceTestBase {
    @Autowired
    protected TerritoryCacheService territoryCacheService;

    @Test
    public void testFindTerritoriesByCondition() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindTerritoriesByCondition not implemented");
    }
}
