package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportAttributesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportAttributesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportAttributesActionHandler extends SecurityActionHandler<ExportAttributesAction, ExportAttributesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public ExportAttributesActionHandler() {
        super(ExportAttributesAction.class);
    }

    @Override
    public ExportAttributesResult executeSecurityAction(ExportAttributesAction action) throws ActionException {
        try {
            String fileName = statisticalResourcesServiceFacade.exportAttributesTsv(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn());
            return new ExportAttributesResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
