package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.resources;

import java.util.List;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheByRelatedResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;

public interface ResourcesRest2DoMapper {

    public RestCriteria2SculptorCriteria<GeoCacheResource> getResourcesCriteriaMapper();
    public RestCriteria2SculptorCriteria<GeoCacheByRelatedResource> getGeoCacheByRelatedResourceCriteriaMapper(List<String> complexResourcesId);
}
