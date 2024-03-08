package org.siemac.metamac.statistical.resources.web.client.widgets.windows.search;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.web.client.widgets.filters.VersionableStatisticalResourceWithoutStatisticalOperationFilterForm;
import org.siemac.metamac.statistical.resources.web.client.widgets.filters.base.VersionableStatisticalResourceWithoutStatisticalOperationFilterBaseForm;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleRelatedResourceBasePaginatedWindow;

public class SearchMultipleStatisticalRelatedResourcePaginatedWindow extends SearchMultipleRelatedResourceBasePaginatedWindow<RelatedResourceDto, VersionableStatisticalResourceWebCriteria> {

    private VersionableStatisticalResourceWithoutStatisticalOperationFilterBaseForm<VersionableStatisticalResourceWebCriteria> filterForm;

    public SearchMultipleStatisticalRelatedResourcePaginatedWindow(String title, int maxResults, SearchPaginatedAction<VersionableStatisticalResourceWebCriteria> searchPaginatedAction) {
        super(title, maxResults, new VersionableStatisticalResourceWithoutStatisticalOperationFilterForm(), searchPaginatedAction);
        filterForm = (VersionableStatisticalResourceWithoutStatisticalOperationFilterForm) getFilterForm();
    }

    public VersionableStatisticalResourceWebCriteria getSearchCriteria() {
        return getFilterForm().getSearchCriteria();
    }

}
