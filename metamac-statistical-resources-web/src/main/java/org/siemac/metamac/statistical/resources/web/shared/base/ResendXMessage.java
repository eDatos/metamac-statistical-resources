package org.siemac.metamac.statistical.resources.web.shared.base;

import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ResendXMessage {
    @In(1)
    LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto;

    @Out(1)
    LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceResultDto;

    @Optional
    @Out(2)
    MetamacWebException notificationException;
}
