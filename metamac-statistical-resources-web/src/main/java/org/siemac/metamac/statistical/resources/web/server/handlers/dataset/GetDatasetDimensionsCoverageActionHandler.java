package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsCoverageAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetDatasetDimensionsCoverageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDatasetDimensionsCoverageActionHandler extends SecurityActionHandler<GetDatasetDimensionsCoverageAction, GetDatasetDimensionsCoverageResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public GetDatasetDimensionsCoverageActionHandler() {
        super(GetDatasetDimensionsCoverageAction.class);
    }

    @Override
    public GetDatasetDimensionsCoverageResult executeSecurityAction(GetDatasetDimensionsCoverageAction action) throws ActionException {
        try {
            Map<String, List<CodeItemDto>> codesDimensions = new HashMap<String, List<CodeItemDto>>();
            for (String dimensionId : action.getDimensionsIds()) {
                List<CodeItemDto> codes = statisticalResourcesServiceFacade.filterCoverageForDatasetVersionDimension(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionUrn(),
                        dimensionId, action.getCriteria() != null ? action.getCriteria().getCriteria() : null, null);
                codesDimensions.put(dimensionId, codes);
            }
            return new GetDatasetDimensionsCoverageResult(codesDimensions);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
