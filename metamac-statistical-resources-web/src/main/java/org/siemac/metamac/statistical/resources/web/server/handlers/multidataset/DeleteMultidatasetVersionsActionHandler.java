package org.siemac.metamac.statistical.resources.web.server.handlers.multidataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.DeleteMultidatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.DeleteMultidatasetVersionsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteMultidatasetVersionsActionHandler extends SecurityActionHandler<DeleteMultidatasetVersionsAction, DeleteMultidatasetVersionsResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public DeleteMultidatasetVersionsActionHandler() {
        super(DeleteMultidatasetVersionsAction.class);
    }

    @Override
    public DeleteMultidatasetVersionsResult executeSecurityAction(DeleteMultidatasetVersionsAction action) throws ActionException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        for (String urn : action.getUrns()) {
            try {
                statisticalResourcesServiceFacade.deleteMultidatasetVersion(ServiceContextHolder.getCurrentServiceContext(), urn);
            } catch (MetamacException e) {
                exceptionItems.addAll(e.getExceptionItems());
            }
        }
        if (!exceptionItems.isEmpty()) {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(exceptionItems));
        }
        return new DeleteMultidatasetVersionsResult();
    }
}
