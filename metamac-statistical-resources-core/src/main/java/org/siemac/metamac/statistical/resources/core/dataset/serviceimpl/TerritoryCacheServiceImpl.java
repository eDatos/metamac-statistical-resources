package org.siemac.metamac.statistical.resources.core.dataset.serviceimpl;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TerritoriesCache;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of TerritoryCacheService.
 */
@Service("territoryCacheService")
public class TerritoryCacheServiceImpl extends TerritoryCacheServiceImplBase {
    public TerritoryCacheServiceImpl() {
    }

    public PagedResult<TerritoriesCache> findTerritoriesByCondition(
        ServiceContext ctx, List<ConditionalCriteria> conditions,
        PagingParameter pagingParameter) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findTerritoriesByCondition not implemented");

    }
}
