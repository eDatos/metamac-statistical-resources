package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodeDimension
 */
@Repository("codeDimensionRepository")
public class CodeDimensionRepositoryImpl extends CodeDimensionRepositoryBase {
    public CodeDimensionRepositoryImpl() {
    }
    
    @Override
    public List<CodeDimension> findCodesForDatasetVersionByDimensionId(long datasetVersionId, String dimensionId, String filter) throws MetamacException {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put("dimensionId", dimensionId);
        parameters.put("datasetVersionId", datasetVersionId);
        
        String filterSql = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(filter)) {
            parameters.put("filter", "%"+filter+"%");
            filterSql =" and (code.title like :filter or code.identifier like :filter)";
        }
        return findByQuery(
                "from CodeDimension code "+
                "where code.dsdComponentId = :dimensionId "+
                "and code.datasetVersion.id = :datasetVersionId" +
                filterSql
                , 
                parameters);
    }

    @Override
    public String findTitleForDatasetVersionByDimensionId(long datasetVersionId, String dimensionId, String filter) throws MetamacException {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put("dimensionId", dimensionId);
        parameters.put("datasetVersionId", datasetVersionId);

        String filterSql = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(filter)) {
            parameters.put("filter", "%"+filter+"%");
            filterSql =" and (code.identifier like :filter)";
        }
        return findByQuery(
                "from CodeDimension code "+
                        "where code.dsdComponentId = :dimensionId "+
                        "and code.datasetVersion.id = :datasetVersionId" +
                        filterSql
                ,
                parameters).get(0).getTitle();
    }
}
