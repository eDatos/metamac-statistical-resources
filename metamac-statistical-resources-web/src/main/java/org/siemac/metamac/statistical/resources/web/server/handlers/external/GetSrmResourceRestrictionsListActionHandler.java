package org.siemac.metamac.statistical.resources.web.server.handlers.external;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.external.GetSrmResourceRestrictionsListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetSrmResourceRestrictionsListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetSrmResourceRestrictionsListActionHandler extends SecurityActionHandler<GetSrmResourceRestrictionsListAction, GetSrmResourceRestrictionsListResult> {

    @Autowired
    private SrmRestInternalFacade srmRestInternalFacade;

    public GetSrmResourceRestrictionsListActionHandler() {
        super(GetSrmResourceRestrictionsListAction.class);
    }

    @Override
    public GetSrmResourceRestrictionsListResult executeSecurityAction(GetSrmResourceRestrictionsListAction action) throws ActionException {
        List<ExternalItemDto> result = srmRestInternalFacade.retrieveSrmResourceRestrictions(action.getCriteria());
        return new GetSrmResourceRestrictionsListResult(result);
    }
}
