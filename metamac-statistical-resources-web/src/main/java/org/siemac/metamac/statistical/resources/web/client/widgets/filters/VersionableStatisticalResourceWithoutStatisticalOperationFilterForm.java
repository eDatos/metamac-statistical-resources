package org.siemac.metamac.statistical.resources.web.client.widgets.filters;

import org.siemac.metamac.statistical.resources.web.client.widgets.filters.base.VersionableStatisticalResourceWithoutStatisticalOperationFilterBaseForm;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;

public class VersionableStatisticalResourceWithoutStatisticalOperationFilterForm
        extends
            VersionableStatisticalResourceWithoutStatisticalOperationFilterBaseForm<VersionableStatisticalResourceWebCriteria> {

    public VersionableStatisticalResourceWithoutStatisticalOperationFilterForm() {
        super();
    }

    @Override
    protected VersionableStatisticalResourceWebCriteria buildEmptySearchCriteria() {
        return new VersionableStatisticalResourceWebCriteria();
    }
}
