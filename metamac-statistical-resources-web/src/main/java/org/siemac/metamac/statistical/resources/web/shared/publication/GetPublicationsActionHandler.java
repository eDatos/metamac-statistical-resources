package org.siemac.metamac.statistical.resources.web.shared.publication;

import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;

import com.gwtplatform.dispatch.shared.ActionException;

public class GetPublicationsActionHandler extends SecurityActionHandler<GetPublicationsAction, GetPublicationsResult> {

    public GetPublicationsActionHandler() {
        super(GetPublicationsAction.class);
    }

    @Override
    public GetPublicationsResult executeSecurityAction(GetPublicationsAction action) throws ActionException {
        // TODO Auto-generated method stub
        return null;
    }

}
