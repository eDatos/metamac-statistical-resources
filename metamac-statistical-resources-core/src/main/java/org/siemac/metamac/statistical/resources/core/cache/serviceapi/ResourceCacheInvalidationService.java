package org.siemac.metamac.statistical.resources.core.cache.serviceapi;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;

public interface ResourceCacheInvalidationService {

    void updateResourceLastUpdate(ServiceContext ctx, LifeCycleStatisticalResource resource, long timestamp) throws MetamacException;

    void updateDatasetVersionsLastUpdateByDsd(ServiceContext ctx, String dsdUrn, long timestamp);

    void updateDatasetVersionsLastUpdateByOperation(ServiceContext ctx, String operationUrn, long timestamp);
}