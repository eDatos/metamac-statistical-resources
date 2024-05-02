package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportDatasourcesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.ExportDatasourcesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportDatasourcesActionHandler extends SecurityActionHandler<ExportDatasourcesAction, ExportDatasourcesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public ExportDatasourcesActionHandler() {
        super(ExportDatasourcesAction.class);
    }

    @Override
    public ExportDatasourcesResult executeSecurityAction(ExportDatasourcesAction action) throws ActionException {
        try {
            String fileName = statisticalResourcesServiceFacade.exportDatasourcesTsv(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn());
            return new ExportDatasourcesResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
