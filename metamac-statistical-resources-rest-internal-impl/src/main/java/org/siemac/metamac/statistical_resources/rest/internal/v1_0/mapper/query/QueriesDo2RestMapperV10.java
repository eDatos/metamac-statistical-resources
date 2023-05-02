package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.JsonStatData;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Queries;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;

public interface QueriesDo2RestMapperV10 {

    public Queries toQueries(PagedResult<QueryVersion> sources, String agencyID, String query, String orderBy, Integer limit, List<String> selectedLanguages, Set<String> parsedFields)
            throws MetamacException;
    public Query toQuery(QueryVersion source, Map<String, List<String>> selectedDimensions, List<String> selectedLanguages, Set<String> fields) throws Exception;
    public ResourceLink toQuerySelfLink(LifeCycleStatisticalResourceDto source);
    public ResourceLink toQuerySelfLink(LifeCycleStatisticalResourceBaseDto source);
    public JsonStatData toJsonStatQuery(QueryVersion source, DatasetVersion datasetVersion, Map<String, List<String>> selectedDimensions, String selectedLanguage, Set<String> parsedFields)
            throws Exception;
    public ResourceStatisticalResourceBase toResource(QueryVersion source, List<String> selectedLanguages, Set<String> parsedFields) throws MetamacException;
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException;
}
