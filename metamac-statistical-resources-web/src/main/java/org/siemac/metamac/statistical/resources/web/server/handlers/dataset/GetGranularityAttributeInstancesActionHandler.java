package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetGranularityAttributeInstancesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetGranularityAttributeInstancesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetGranularityAttributeInstancesActionHandler extends SecurityActionHandler<GetGranularityAttributeInstancesAction, GetGranularityAttributeInstancesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public GetGranularityAttributeInstancesActionHandler() {
        super(GetGranularityAttributeInstancesAction.class);
    }

    @Override
    public GetGranularityAttributeInstancesResult executeSecurityAction(GetGranularityAttributeInstancesAction action) throws ActionException {
        try {
            List<DsdGranularityAttributeInstanceDto> instances = statisticalResourcesServiceFacade.retrieveGranularityAttributeInstances(
                    ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn(), action.getAttributeId());
            return new GetGranularityAttributeInstancesResult(instances);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
