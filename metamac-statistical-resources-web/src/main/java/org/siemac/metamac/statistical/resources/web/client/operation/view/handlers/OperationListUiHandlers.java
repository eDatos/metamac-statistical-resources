package org.siemac.metamac.statistical.resources.web.client.operation.view.handlers;



import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.gwtplatform.mvp.client.UiHandlers;

public interface OperationListUiHandlers extends UiHandlers {

    public void retrieveOperations(int firstResult, int maxResults, MetamacWebCriteria criteria);
    public void goToOperation(String urn);
}
