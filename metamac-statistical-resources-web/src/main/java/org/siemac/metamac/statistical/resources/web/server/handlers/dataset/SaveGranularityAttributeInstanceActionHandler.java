package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveGranularityAttributeInstanceAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.SaveGranularityAttributeInstanceResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveGranularityAttributeInstanceActionHandler extends SecurityActionHandler<SaveGranularityAttributeInstanceAction, SaveGranularityAttributeInstanceResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public SaveGranularityAttributeInstanceActionHandler() {
        super(SaveGranularityAttributeInstanceAction.class);
    }

    @Override
    public SaveGranularityAttributeInstanceResult executeSecurityAction(SaveGranularityAttributeInstanceAction action) throws ActionException {
        try {
            DsdGranularityAttributeInstanceDto saved;
            if (action.getDsdGranularityAttributeInstanceDto().getUuid() == null) {
                saved = statisticalResourcesServiceFacade.createGranularityAttributeInstance(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn(),
                        action.getDsdGranularityAttributeInstanceDto());
            } else {
                saved = statisticalResourcesServiceFacade.updateGranularityAttributeInstance(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn(),
                        action.getDsdGranularityAttributeInstanceDto());
            }
            return new SaveGranularityAttributeInstanceResult(saved);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
