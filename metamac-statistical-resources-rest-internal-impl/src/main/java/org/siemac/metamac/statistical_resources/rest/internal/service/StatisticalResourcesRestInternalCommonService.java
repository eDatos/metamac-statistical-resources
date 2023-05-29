package org.siemac.metamac.statistical_resources.rest.internal.service;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface StatisticalResourcesRestInternalCommonService {

    public PagedResult<DatasetVersion> findDatasetVersions(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public DatasetVersion retrieveDatasetVersion(String agencyID, String resourceID, String version);
    public DatasetVersion retrieveDatasetLastVersionByUrn(String urn);

    public PagedResult<PublicationVersion> findPublicationVersions(String agencyID, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public PublicationVersion retrievePublicationVersion(String agencyID, String resourceID);

    public PagedResult<QueryVersion> findQueryVersions(String agencyID, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public QueryVersion retrieveQueryVersion(String agencyID, String resourceID);

    public MultidatasetVersion retrieveMultidatasetVersion(String agencyID, String resourceID);
    public PagedResult<MultidatasetVersion> findMultidatasetVersions(String agencyID, List<ConditionalCriteria> conditions, PagingParameter pagingParameter);
    
    public PagedResult<GeoCovVarElementCacheDatasetVersion> findResources(List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
}
