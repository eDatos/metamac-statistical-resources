package org.siemac.metamac.statistical.resources.web.server.handlers.external;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDataProviderPaginatedListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDataProviderPaginatedListActionHandler extends SecurityActionHandler<GetDataProviderPaginatedListAction, GetDataProviderPaginatedListResult> {

    @Autowired
    private SrmRestInternalFacade srmRestInternalFacade;

    public GetDataProviderPaginatedListActionHandler() {
        super(GetDataProviderPaginatedListAction.class);
    }

    @Override
    public GetDataProviderPaginatedListResult executeSecurityAction(GetDataProviderPaginatedListAction action) throws ActionException {
        ExternalItemsResult result = srmRestInternalFacade.findOrganisations(action.getFirstResult(), action.getMaxResults(), action.getCriteria(), TypeExternalArtefactsEnum.DATA_PROVIDER);
        return new GetDataProviderPaginatedListResult(result.getExternalItemDtos(), result.getFirstResult(), result.getTotalResults());
    }
}
