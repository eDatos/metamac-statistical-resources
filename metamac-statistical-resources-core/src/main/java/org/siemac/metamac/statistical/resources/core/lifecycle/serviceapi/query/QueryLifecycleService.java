package org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.query;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface QueryLifecycleService {

    public static final String BEAN_ID = "queryLifecycleService";

    public void checkLinkedDatasetOrDatasetVersionPublishedBeforeQuery(ServiceContext ctx, QueryVersion resource) throws MetamacException;

    public void propagateLastUpdateForPublishedQuery(ServiceContext ctx, QueryVersion resource);

}
