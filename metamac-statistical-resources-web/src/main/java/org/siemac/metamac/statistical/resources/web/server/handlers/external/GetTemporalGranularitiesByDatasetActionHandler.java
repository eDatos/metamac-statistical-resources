package org.siemac.metamac.statistical.resources.web.server.handlers.external;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetTemporalGranularitiesByDatasetActionHandler extends SecurityActionHandler<GetTemporalGranularitiesByDatasetAction, GetTemporalGranularitiesByDatasetResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public GetTemporalGranularitiesByDatasetActionHandler() {
        super(GetTemporalGranularitiesByDatasetAction.class);
    }

    @Override
    public GetTemporalGranularitiesByDatasetResult executeSecurityAction(GetTemporalGranularitiesByDatasetAction action) throws ActionException {
        try {
            List<ExternalItemDto> externalItemsDto =  statisticalResourcesServiceFacade.retrieveExternalItemsByDatasetUrn(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetUrn());
            GetTemporalGranularitiesByDatasetResult temporalGranularitiesByDatasetResult = new GetTemporalGranularitiesByDatasetResult(externalItemsDto, 0,
                    externalItemsDto.size());
            return temporalGranularitiesByDatasetResult;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
