package org.siemac.metamac.statistical.resources.web.shared.publication;

import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateGeoCacheRelatedResource {

    @In(1)
    String              urnResource;

    @Out(2)
    @Optional
    MetamacWebException notificationException;
}
