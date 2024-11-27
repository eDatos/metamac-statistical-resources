package org.siemac.metamac.statistical_resources.rest.external.service;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;

public interface StatisticalResourcesRestExternalCommonService {

    public PagedResult<DatasetVersion> findDatasetVersions(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public DatasetVersion retrieveDatasetVersion(String agencyID, String resourceID, String version, String granularity);
    public ContentConstraint retrieveDatasetConstraintVersion(String datasetUrn);
    public Boolean checkDatasetVersion(String agencyID, String resourceID, String version);
    public DatasetVersion retrieveDatasetLastPublishedVersionByUrn(String urn);

    public PagedResult<PublicationVersion> findPublicationVersions(String agencyID, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public PublicationVersion retrievePublicationVersion(String agencyID, String resourceID);

    public PagedResult<QueryVersion> findQueryVersions(String agencyID, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public QueryVersion retrieveQueryVersion(String agencyID, String resourceID);

    public PagedResult<MultidatasetVersion> findMultidatasetVersions(String agencyID, List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);
    public MultidatasetVersion retrieveMultidatasetVersion(String agencyID, String resourceID);

    public PagedResult<GeoCovVarElementCacheDatasetVersion> findResources(List<ConditionalCriteria> conditionalCriteria, PagingParameter pagingParameter);

}
