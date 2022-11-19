package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Resources;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;

public interface ResourcesDo2RestMapperV10 {

    public Resources toResources(PagedResult<GeoCovVarElementCacheDatasetVersion> sources, String query, String orderBy, Integer limit, List<String> selectedLanguages);
}
