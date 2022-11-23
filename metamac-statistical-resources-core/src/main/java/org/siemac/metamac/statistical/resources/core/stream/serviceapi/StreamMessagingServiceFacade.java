package org.siemac.metamac.statistical.resources.core.stream.serviceapi;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface StreamMessagingServiceFacade {

    public void sendNewVersionPublished(HasSiemacMetadata version) throws MetamacException;

    public void sendNewVersionPublished(QueryVersion version) throws MetamacException;
}
