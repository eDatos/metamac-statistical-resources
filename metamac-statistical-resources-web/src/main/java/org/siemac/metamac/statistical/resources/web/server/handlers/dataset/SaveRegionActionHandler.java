package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveRegionAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveRegionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveRegionActionHandler extends SecurityActionHandler<SaveRegionAction, SaveRegionResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public SaveRegionActionHandler() {
        super(SaveRegionAction.class);
    }

    @Override
    public SaveRegionResult executeSecurityAction(SaveRegionAction action) throws ActionException {
        try {
            RegionValueDto region = null;
            if (action.getRegionToSave().getKeys().isEmpty() && action.getRegionToSave().getCode() != null) {
                // A region cannot be saved if it does not have keys. If the key list is empty, the region will be deleted. If the code of the region is null, the region has not been created yet.
                statisticalResourcesServiceFacade.deleteRegion(ServiceContextHolder.getCurrentServiceContext(), action.getContentConstraintUrn(), action.getRegionToSave().getCode());
            } else {
                region = statisticalResourcesServiceFacade.saveRegionForContentConstraint(ServiceContextHolder.getCurrentServiceContext(), action.getContentConstraintUrn(), action.getRegionToSave(),
                        action.getSelectedDimension(), action.getDatasetUrn());
            }
            return new SaveRegionResult(region);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
