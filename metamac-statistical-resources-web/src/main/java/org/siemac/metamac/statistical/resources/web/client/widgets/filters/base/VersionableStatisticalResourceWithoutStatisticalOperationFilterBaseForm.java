package org.siemac.metamac.statistical.resources.web.client.widgets.filters.base;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.filters.base.SimpleVersionableFilterBaseForm;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;
import org.siemac.metamac.web.common.client.widgets.filters.facets.OnlyLastVersionFacetFilter;

public abstract class VersionableStatisticalResourceWithoutStatisticalOperationFilterBaseForm<T extends VersionableStatisticalResourceWebCriteria> extends SimpleVersionableFilterBaseForm<T> {

    public VersionableStatisticalResourceWithoutStatisticalOperationFilterBaseForm() {
        super();
        onlyLastVersionFacet = new OnlyLastVersionFacetFilter();
        onlyLastVersionFacet.setColSpan(2);
        criteriaFacet.setColSpan(2);
    }

    // IMPORTANT: This method must be inherited if you change the WebCriteria in T
    @Override
    public T getSearchCriteria() {
        return super.getSearchCriteria();
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList(onlyLastVersionFacet, criteriaFacet);
    }
}
