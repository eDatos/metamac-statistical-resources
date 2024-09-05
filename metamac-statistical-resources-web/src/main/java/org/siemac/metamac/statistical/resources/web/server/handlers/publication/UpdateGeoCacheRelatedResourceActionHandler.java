package org.siemac.metamac.statistical.resources.web.server.handlers.publication;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdateGeoCacheRelatedResourceAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdateGeoCacheRelatedResourceResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateGeoCacheRelatedResourceActionHandler extends SecurityActionHandler<UpdateGeoCacheRelatedResourceAction, UpdateGeoCacheRelatedResourceResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public UpdateGeoCacheRelatedResourceActionHandler() {
        super(UpdateGeoCacheRelatedResourceAction.class);
    }
    @Override
    public UpdateGeoCacheRelatedResourceResult executeSecurityAction(UpdateGeoCacheRelatedResourceAction action) throws ActionException {
        try {
            if (action.getUrnResource() != null) {
                statisticalResourcesServiceFacade.updateGeoCacheRelatedResource(ServiceContextHolder.getCurrentServiceContext(), action.getUrnResource(), StatisticalResourceTypeEnum.COLLECTION);
            }
            return new UpdateGeoCacheRelatedResourceResult.Builder().build();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
