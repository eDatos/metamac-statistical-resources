package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportConsolidatedDatasourcesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportConsolidatedDatasourcesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportConsolidatedDatasourcesActionHandler extends SecurityActionHandler<ExportConsolidatedDatasourcesAction, ExportConsolidatedDatasourcesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public ExportConsolidatedDatasourcesActionHandler() {
        super(ExportConsolidatedDatasourcesAction.class);
    }

    @Override
    public ExportConsolidatedDatasourcesResult executeSecurityAction(ExportConsolidatedDatasourcesAction action) throws ActionException {
        try {
            String fileName = statisticalResourcesServiceFacade.exportDatasourcesTsv(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn());
            return new ExportConsolidatedDatasourcesResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
