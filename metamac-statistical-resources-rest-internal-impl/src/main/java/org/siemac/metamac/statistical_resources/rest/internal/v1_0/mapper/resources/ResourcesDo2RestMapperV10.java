package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.resources;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Resources;

public interface ResourcesDo2RestMapperV10 {

    public Resources toResources(PagedResult<GeoCacheByRelatedResource> sources, String query, String orderBy, Integer limit, List<String> selectedLanguages,
            List<String> cacheResourcesUrnWithSelectedCriteria) throws RestException;
}
