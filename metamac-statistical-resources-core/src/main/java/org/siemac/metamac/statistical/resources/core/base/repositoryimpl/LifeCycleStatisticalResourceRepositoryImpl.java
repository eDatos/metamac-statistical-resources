package org.siemac.metamac.statistical.resources.core.base.repositoryimpl;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResourceProperties;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

/**
 * Repository implementation for LifeCycleStatisticalResource
 */
@Repository("lifeCycleStatisticalResourceRepository")
public class LifeCycleStatisticalResourceRepositoryImpl extends LifeCycleStatisticalResourceRepositoryBase {
    private static final String LINE_BREAK = "\r\n";
    public LifeCycleStatisticalResourceRepositoryImpl() {
    }

    @Override
    public LifeCycleStatisticalResource retrieveByUrn(String urn) throws MetamacException {
        // Prepare criteria
        List<ConditionalCriteria> condition = criteriaFor(LifeCycleStatisticalResource.class).withProperty(LifeCycleStatisticalResourceProperties.urn()).eq(urn).distinctRoot().build();

        // Find
        List<LifeCycleStatisticalResource> result = findByCondition(condition);

        // Check for unique result and return
        if (result.isEmpty()) {
            throw new MetamacException(ServiceExceptionType.IDENTIFIABLE_STATISTICAL_RESOURCE_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one lifecycle resource with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public String findLastUsedCodeForResourceType(String operationUrn) {
        //@formatter:off
        //the query returns the maximum sequential number
        //It must be taken into account that there are codes that do not comply with the 'CODIGO_XXXXXX' format
        //Also see: SiemacMetadataStatisticalResourceRepositoryImpl
        
        String hql = "select max(substring(TSR.code, length(tsr.code) - 5, length(tsr.code))) " + LINE_BREAK +
                "from tb_queries_versions tqv " + LINE_BREAK +
                "join tb_stat_resources tsr on tsr.id = tqv.lifecycle_resource_fk " + LINE_BREAK + 
                "join tb_external_items tei on tei.id = tsr.stat_operation_fk " + LINE_BREAK +
                "where substring(TSR.code, length(tsr.code) - 5, length(tsr.code)) ~ \'^[0-9\\.]+$\' " + LINE_BREAK + 
                "and tei.urn = :operationUrn ";
        //@formatter:on

        Query query = getEntityManager().createNativeQuery(hql);
        query.setParameter("operationUrn", operationUrn);
        return (String) query.getSingleResult();

    }

}
