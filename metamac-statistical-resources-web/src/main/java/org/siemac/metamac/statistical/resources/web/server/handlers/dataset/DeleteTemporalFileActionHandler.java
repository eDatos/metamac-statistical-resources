package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesNotUsedAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteDatasourcesNotUsedResult;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteTemporalFileAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.DeleteTemporalFileResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteTemporalFileActionHandler extends SecurityActionHandler<DeleteTemporalFileAction, DeleteTemporalFileResult>{
    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public DeleteTemporalFileActionHandler() {
        super(DeleteTemporalFileAction.class);
    }

    public DeleteTemporalFileActionHandler(Class<DeleteTemporalFileAction> actionType) {
        super(actionType);
    }

    @Override
    public DeleteTemporalFileResult executeSecurityAction(DeleteTemporalFileAction action) throws ActionException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        try {
            statisticalResourcesServiceFacade.deleteTemporalFile(null, action.getTemporalFile());
        } catch (MetamacException e) {
            exceptionItems.addAll(e.getExceptionItems());
        }
        
        if (!exceptionItems.isEmpty()) {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(exceptionItems));
        }
        return new DeleteTemporalFileResult();
    }
    
}
