package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CopyDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.CopyDatasetResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyDatasetActionHandler extends SecurityActionHandler<CopyDatasetAction, CopyDatasetResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public CopyDatasetActionHandler() {
        super(CopyDatasetAction.class);
    }

    @Override
    public CopyDatasetResult executeSecurityAction(CopyDatasetAction action) throws ActionException {
        try {
            statisticalResourcesServiceFacade.copyDatasetVersion(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(), "");
            return null;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
