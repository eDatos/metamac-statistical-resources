package org.siemac.metamac.statistical.resources.web.client.widgets;

import org.siemac.metamac.statistical.resources.web.client.operation.view.handlers.OperationListUiHandlers;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;

public class OperationsValuesSearchSectionStack extends OperationSearchSectionStack {
    private OperationListUiHandlers handlers;

    protected void retrieveResources() {
        getUiHandlers().retrieveOperations(0, CommonWebConstants.MAIN_LIST_MAX_RESULTS, getDataConfigurationWebCriteria());
    }

    private OperationListUiHandlers getUiHandlers() {
        return handlers;
    }

    public void setUiHandlers(OperationListUiHandlers handlers) {
        this.handlers = handlers;
    }
}
