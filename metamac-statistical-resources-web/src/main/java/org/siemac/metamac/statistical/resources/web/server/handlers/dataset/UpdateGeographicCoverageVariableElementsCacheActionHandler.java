package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateGeographicCoverageVariableElementsCacheAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateGeographicCoverageVariableElementsCacheResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateGeographicCoverageVariableElementsCacheActionHandler
        extends
            SecurityActionHandler<UpdateGeographicCoverageVariableElementsCacheAction, UpdateGeographicCoverageVariableElementsCacheResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public UpdateGeographicCoverageVariableElementsCacheActionHandler() {
        super(UpdateGeographicCoverageVariableElementsCacheAction.class);
    }

    @Override
    public UpdateGeographicCoverageVariableElementsCacheResult executeSecurityAction(UpdateGeographicCoverageVariableElementsCacheAction action) throws ActionException {
        try {
            if (action.getDatasetVersionDto() != null) {
                statisticalResourcesServiceFacade.updateGeographicCoverageVariableElementsCache(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionDto().getUrn());
            } else {
                statisticalResourcesServiceFacade.updateAllGeographicCoverageVariableElementsCache(ServiceContextHolder.getCurrentServiceContext(), action.getResourcesToUpdate(),
                        action.getExternalResourcesToUpdate());
            }
            return new UpdateGeographicCoverageVariableElementsCacheResult.Builder().datasetVersionDto(action.getDatasetVersionDto()).build();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
