package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.query;

import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface QueriesDo2RestMapperV10 {

    public Queries toQueries(PagedResult<QueryVersion> sources, String agencyID, String query, String orderBy, Integer limit, List<String> selectedLanguages);
    public Query toQuery(QueryVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, boolean includeMetadata, boolean includeData) throws Exception;
    public Resource toResource(QueryVersion source, List<String> selectedLanguages);
    public Resource toResource(RelatedResourceResult source, List<String> selectedLanguages);
}
