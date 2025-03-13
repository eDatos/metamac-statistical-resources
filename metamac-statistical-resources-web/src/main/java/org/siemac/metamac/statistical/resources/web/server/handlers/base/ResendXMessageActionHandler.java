package org.siemac.metamac.statistical.resources.web.server.handlers.base;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendXMessageAction;
import org.siemac.metamac.statistical.resources.web.shared.base.ResendXMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ResendXMessageActionHandler extends SecurityActionHandler<ResendXMessageAction, ResendXMessageResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public ResendXMessageActionHandler() {
        super(ResendXMessageAction.class);
    }

    @Override
    public ResendXMessageResult executeSecurityAction(ResendXMessageAction action) throws ActionException {
    try {
        LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto = action.getLifeCycleStatisticalResourceDto();
        DatasetVersionDto resendPublishedStreamMessage = statisticalResourcesServiceFacade.resendPublishedDatasetVersionXMessage(ServiceContextHolder.getCurrentServiceContext(),
                lifeCycleStatisticalResourceDto.getUrn());
        lifeCycleStatisticalResourceDto.setXStreamStatus(resendPublishedStreamMessage.getXStreamStatus());
        return null;
    } catch (MetamacException e) {
        throw WebExceptionUtils.createMetamacWebException(e);
    }
    }

}
