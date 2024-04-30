package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionCoverageAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionCoverageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDatasetDimensionCoverageActionHandler extends SecurityActionHandler<GetDatasetDimensionCoverageAction, GetDatasetDimensionCoverageResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public GetDatasetDimensionCoverageActionHandler() {
        super(GetDatasetDimensionCoverageAction.class);
    }

    @Override
    public GetDatasetDimensionCoverageResult executeSecurityAction(GetDatasetDimensionCoverageAction action) throws ActionException {
        try {
            List<CodeItemDto> codes = statisticalResourcesServiceFacade.filterCoverageForDatasetVersionDimension(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn(),
                    action.getDimensionId(), action.getCriteria() != null ? action.getCriteria().getCriteria() : null, action.getTemporalGranularitiesCodes());
            return new GetDatasetDimensionCoverageResult(codes);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
