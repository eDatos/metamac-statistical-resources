package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;
import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersionProperties;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for GeoCovVarElementCacheDatasetVersion
 */
@Repository("geoCovVarElementCacheDatasetVersionRepository")
public class GeoCovVarElementCacheDatasetVersionRepositoryImpl
    extends GeoCovVarElementCacheDatasetVersionRepositoryBase {
    public GeoCovVarElementCacheDatasetVersionRepositoryImpl() {
    }

    public List<GeoCovVarElementCacheDatasetVersion> retrieveByDatasetVersionUrn(
        String datasetVersionUrn) {

        List<ConditionalCriteria> condition = criteriaFor(GeoCovVarElementCacheDatasetVersion.class).withProperty(GeoCovVarElementCacheDatasetVersionProperties.urn()).eq(datasetVersionUrn).distinctRoot().build();

        return findByCondition(condition);

    }

    public void deleteAllByDatasetVersionUrn(String datasetVersionUrn) {

        List<GeoCovVarElementCacheDatasetVersion> geoCovVarElementCacheDatasetVersions = retrieveByDatasetVersionUrn(datasetVersionUrn);
        for (GeoCovVarElementCacheDatasetVersion geoCovVarElementCacheDatasetVersion : geoCovVarElementCacheDatasetVersions) {
            delete(geoCovVarElementCacheDatasetVersion); 
        }
        this.getEntityManager().flush();
    }
}
