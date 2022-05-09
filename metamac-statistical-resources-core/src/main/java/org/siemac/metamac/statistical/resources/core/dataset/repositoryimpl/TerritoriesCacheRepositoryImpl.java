package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import org.siemac.metamac.statistical.resources.core.dataset.domain.TerritoriesCache;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for TerritoriesCache
 */
@Repository("territoriesCacheRepository")
public class TerritoriesCacheRepositoryImpl
    extends TerritoriesCacheRepositoryBase {
    public TerritoriesCacheRepositoryImpl() {
    }

    public TerritoriesCache retrieveByTerritoryId(String id) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "retrieveByTerritoryId not implemented");

    }
}
