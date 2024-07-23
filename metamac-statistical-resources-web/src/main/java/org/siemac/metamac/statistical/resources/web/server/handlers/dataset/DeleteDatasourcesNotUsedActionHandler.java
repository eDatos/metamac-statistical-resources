package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesNotUsedAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesNotUsedResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDatasourcesNotUsedActionHandler extends SecurityActionHandler<DeleteDatasourcesNotUsedAction, DeleteDatasourcesNotUsedResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public DeleteDatasourcesNotUsedActionHandler() {
        super(DeleteDatasourcesNotUsedAction.class);
    }

    @Override
    public DeleteDatasourcesNotUsedResult executeSecurityAction(DeleteDatasourcesNotUsedAction action) throws ActionException {
        List<String> dataSourcesDeteted = new ArrayList<String>();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        try {
            dataSourcesDeteted = statisticalResourcesServiceFacade.deleteDatasourcesNotUsed(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetUrn(), action.isDeleteAttributes());
        } catch (MetamacException e) {
            exceptionItems.addAll(e.getExceptionItems());
        }

        if (!exceptionItems.isEmpty()) {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(exceptionItems));
        }

        return new DeleteDatasourcesNotUsedResult(dataSourcesDeteted);
    }
}
