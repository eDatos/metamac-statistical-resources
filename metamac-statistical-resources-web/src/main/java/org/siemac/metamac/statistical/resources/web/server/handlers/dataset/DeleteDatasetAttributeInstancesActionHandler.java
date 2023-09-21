package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetAttributeInstancesAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasetAttributeInstancesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDatasetAttributeInstancesActionHandler extends SecurityActionHandler<DeleteDatasetAttributeInstancesAction, DeleteDatasetAttributeInstancesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public DeleteDatasetAttributeInstancesActionHandler() {
        super(DeleteDatasetAttributeInstancesAction.class);
    }

    @Override
    public DeleteDatasetAttributeInstancesResult executeSecurityAction(DeleteDatasetAttributeInstancesAction action) throws ActionException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        for (String uuid : action.getUuids()) {
            try {
                statisticalResourcesServiceFacade.deleteAttributeInstance(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn(), uuid);
            } catch (MetamacException e) {
                exceptionItems.addAll(e.getExceptionItems());
            }
        }
        if (!exceptionItems.isEmpty()) {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(exceptionItems));
        }
        return new DeleteDatasetAttributeInstancesResult();
    }
}
