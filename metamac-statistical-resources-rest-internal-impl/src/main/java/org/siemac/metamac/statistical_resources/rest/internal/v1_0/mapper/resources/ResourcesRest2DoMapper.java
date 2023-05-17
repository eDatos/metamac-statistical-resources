package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.resources;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;

public interface ResourcesRest2DoMapper {

    public RestCriteria2SculptorCriteria<GeoCovVarElementCacheDatasetVersion> getResourcesCriteriaMapper();
}
