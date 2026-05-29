package org.siemac.metamac.statistical.resources.web.server.handlers.external;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsWithoutVersionPaginatedListAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetDsdsWithoutVersionPaginatedListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdsWithoutVersionPaginatedListActionHandler extends SecurityActionHandler<GetDsdsWithoutVersionPaginatedListAction, GetDsdsWithoutVersionPaginatedListResult> {

    @Autowired
    private SrmRestInternalFacade srmRestInternalFacade;

    public GetDsdsWithoutVersionPaginatedListActionHandler() {
        super(GetDsdsWithoutVersionPaginatedListAction.class);
    }

    @Override
    public GetDsdsWithoutVersionPaginatedListResult executeSecurityAction(GetDsdsWithoutVersionPaginatedListAction action) throws ActionException {
        DsdWebCriteria dsdCriteria = new DsdWebCriteria(action.getCriteria().getCriteria());

        ExternalItemsResult result = srmRestInternalFacade.findDsds(action.getFirstResult(), action.getMaxResults(), dsdCriteria);

        for (ExternalItemDto item : result.getExternalItemDtos()) {
            item.setUrn(UrnUtils.removeVersion(item.getUrn()));
        }
        return new GetDsdsWithoutVersionPaginatedListResult(result.getExternalItemDtos(), result.getFirstResult(), result.getTotalResults());
    }
}
