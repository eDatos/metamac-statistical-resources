package org.siemac.metamac.statistical.resources.web.shared.dataset;

import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateTerritoriesCache {

    @Out(1)
    @Optional
    MetamacWebException notificationException;
}
