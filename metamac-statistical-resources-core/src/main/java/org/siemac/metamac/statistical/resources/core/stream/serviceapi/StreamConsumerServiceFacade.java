package org.siemac.metamac.statistical.resources.core.stream.serviceapi;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface StreamConsumerServiceFacade {

    public void updateGeographicCoverageExternalPublicationVariableElementsCache(ServiceContext ctx) throws MetamacException;

}
