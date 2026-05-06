package org.siemac.metamac.statistical.resources.web.shared.base;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateGeoCacheRelatedResource {

    @In(1)
    String                      urnResource;

    @In(2)
    StatisticalResourceTypeEnum resourceType;

    @Out(3)
    @Optional
    MetamacWebException         notificationException;
}
