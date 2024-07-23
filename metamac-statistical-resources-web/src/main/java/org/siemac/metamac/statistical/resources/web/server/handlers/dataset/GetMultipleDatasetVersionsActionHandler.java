package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetMultipleDatasetVersionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetMultipleDatasetVersionsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetMultipleDatasetVersionsActionHandler extends SecurityActionHandler<GetMultipleDatasetVersionsAction, GetMultipleDatasetVersionsResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    public GetMultipleDatasetVersionsActionHandler() {
        super(GetMultipleDatasetVersionsAction.class);
    }

    @Override
    public GetMultipleDatasetVersionsResult executeSecurityAction(GetMultipleDatasetVersionsAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        criteria.getOrdersBy().add(MetamacWebCriteriaUtils.buildMetamacCriteriaLastUpdatedOrder());

        // Criteria
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.buildMultipleDatasetVersionsCriteriaFromWebcriteria(action.getCriteria()));
        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<DatasetVersionBaseDto> result = statisticalResourcesServiceFacade.findDatasetsVersionsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetMultipleDatasetVersionsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
