package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDatasourcesActionHandler extends SecurityActionHandler<DeleteDatasourcesAction, DeleteDatasourcesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public DeleteDatasourcesActionHandler() {
        super(DeleteDatasourcesAction.class);
    }

    @Override
    public DeleteDatasourcesResult executeSecurityAction(DeleteDatasourcesAction action) throws ActionException {
        int observationsDeleted = 0;
        List<MetamacExceptionItem> items = new ArrayList<MetamacExceptionItem>();
        for (String urn : action.getUrns()) {
            try {
                observationsDeleted += statisticalResourcesServiceFacade.deleteDatasource(ServiceContextHolder.getCurrentServiceContext(), urn, action.isDeleteAttributes());
            } catch (MetamacException e) {
                items.addAll(e.getExceptionItems());
            }
        }
        if (items.isEmpty()) {
            return new DeleteDatasourcesResult(observationsDeleted);
        } else {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(items));
        }
    }
}
