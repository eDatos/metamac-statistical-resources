package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetProperties;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for Dataset
 */
@Repository("datasetRepository")
public class DatasetRepositoryImpl extends DatasetRepositoryBase {

    public DatasetRepositoryImpl() {
    }

    @Override
    public Dataset retrieveByUrn(String urn) throws MetamacException {
        // Prepare criteria
        List<ConditionalCriteria> condition = criteriaFor(Dataset.class).withProperty(DatasetProperties.identifiableStatisticalResource().urn()).eq(urn).distinctRoot().build();

        // Find
        List<Dataset> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one dataset with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public String findDatasetUrnLinkedToDatasourceSourceName(String sourceName) {
        // @formatter:off
        String query = "select ds " + 
                        "from Dataset ds " + 
                        "join ds.versions version " + 
                        "join version.datasources datasource " + 
                        "where lower(datasource.sourceName) = :sourceNameExpr";
        // @formatter:on
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sourceNameExpr", sourceName.toLowerCase());
        List<Dataset> result = findByQuery(query, parameters);

        if (result.isEmpty()) {
            return null;
        }
        return result.get(0).getIdentifiableStatisticalResource().getUrn();
    }
}
