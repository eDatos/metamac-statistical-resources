package org.siemac.metamac.statistical.resources.web.server.handlers.external;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderSchemesPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderSchemesPaginatedListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDataProviderSchemesPaginatedListActionHandler extends SecurityActionHandler<GetDataProviderSchemesPaginatedListAction, GetDataProviderSchemesPaginatedListResult> {

    @Autowired
    private SrmRestInternalFacade srmRestInternalFacade;

    public GetDataProviderSchemesPaginatedListActionHandler() {
        super(GetDataProviderSchemesPaginatedListAction.class);
    }

    @Override
    public GetDataProviderSchemesPaginatedListResult executeSecurityAction(GetDataProviderSchemesPaginatedListAction action) throws ActionException {
        ExternalItemsResult result = srmRestInternalFacade.findOrganisationSchemes(action.getFirstResult(), action.getMaxResults(), action.getCriteria(),
                TypeExternalArtefactsEnum.DATA_PROVIDER_SCHEME);
        return new GetDataProviderSchemesPaginatedListResult(result.getExternalItemDtos(), result.getFirstResult(), result.getTotalResults());
    }
}
