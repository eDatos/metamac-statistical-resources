package org.siemac.metamac.statistical.resources.web.server.handlers.external;

import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryCode;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetAction;
import org.siemac.metamac.statistical.resources.web.shared.external.GetTemporalGranularitiesByDatasetResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetTemporalGranularitiesByDatasetActionHandler extends SecurityActionHandler<GetTemporalGranularitiesByDatasetAction, GetTemporalGranularitiesByDatasetResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private SrmRestInternalFacade             srmRestInternalFacade;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    public GetTemporalGranularitiesByDatasetActionHandler() {
        super(GetTemporalGranularitiesByDatasetAction.class);
    }

    @Override
    public GetTemporalGranularitiesByDatasetResult executeSecurityAction(GetTemporalGranularitiesByDatasetAction action) throws ActionException {
        GetTemporalGranularitiesByDatasetResult temporalGranularitiesByDatasetResult = null;
        try {
            String temporalGranularityCodelistUrn = configurationService.retrieveDefaultCodelistTemporalGranularityUrn();
            if (!StringUtils.isEmpty(temporalGranularityCodelistUrn)) {
                 ExternalItemsResult result = srmRestInternalFacade.findCodesInCodelist(temporalGranularityCodelistUrn, 0, null, action.getCriteria());
                List<ExternalItemDto> externalItemsDto =  statisticalResourcesServiceFacade.retrieveExternalItemsByDatasetUrn(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetUrn(), result.getExternalItemDtos());
                temporalGranularitiesByDatasetResult = new GetTemporalGranularitiesByDatasetResult(externalItemsDto, 0,
                        externalItemsDto.size());
            }
            return temporalGranularitiesByDatasetResult;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
