package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateTerritoriesCacheAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateTerritoriesCacheResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateTerritoriesCacheActionHandler extends SecurityActionHandler<UpdateTerritoriesCacheAction, UpdateTerritoriesCacheResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public UpdateTerritoriesCacheActionHandler() {
        super(UpdateTerritoriesCacheAction.class);
    }

    @Override
    public UpdateTerritoriesCacheResult executeSecurityAction(UpdateTerritoriesCacheAction action) throws ActionException {
        try {
            statisticalResourcesServiceFacade.updateTerritoriesCache(ServiceContextHolder.getCurrentServiceContext());
            return new UpdateTerritoriesCacheResult.Builder().build();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
