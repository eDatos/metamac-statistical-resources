package org.siemac.metamac.statistical.resources.web.client.widgets.windows.search;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.web.client.widgets.filters.VersionableStatisticalResourceFilterForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.filters.base.VersionableStatisticalResourceFilterBaseForm;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleRelatedResourceBasePaginatedWindow;

public class SearchMultipleStatisticalRelatedResourcePaginatedWindow extends SearchMultipleRelatedResourceBasePaginatedWindow<RelatedResourceDto, VersionableStatisticalResourceWebCriteria> {

    private VersionableStatisticalResourceFilterBaseForm<VersionableStatisticalResourceWebCriteria> filterForm;

    public SearchMultipleStatisticalRelatedResourcePaginatedWindow(String title, int maxResults, SearchPaginatedAction<VersionableStatisticalResourceWebCriteria> searchPaginatedAction) {
        super(title, maxResults, new VersionableStatisticalResourceFilterForm(), searchPaginatedAction);
        filterForm = (VersionableStatisticalResourceFilterForm) getFilterForm();
    }

    public void setStatisticalOperations(List<ExternalItemDto> statisticalOperations) {
        filterForm.setStatisticalOperations(statisticalOperations);
    }

    public void setSelectedStatisticalOperation(ExternalItemDto statisticalOperation) {
        filterForm.setSelectedStatisticalOperation(statisticalOperation);
    }

    public VersionableStatisticalResourceWebCriteria getSearchCriteria() {
        return getFilterForm().getSearchCriteria();
    }

}
