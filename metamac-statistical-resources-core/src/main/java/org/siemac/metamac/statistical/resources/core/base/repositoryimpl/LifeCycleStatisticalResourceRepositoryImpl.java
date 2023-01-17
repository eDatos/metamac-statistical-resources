package org.siemac.metamac.statistical.resources.core.base.repositoryimpl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for LifeCycleStatisticalResource
 */
@Repository("lifeCycleStatisticalResourceRepository")
public class LifeCycleStatisticalResourceRepositoryImpl extends LifeCycleStatisticalResourceRepositoryBase {
    private static final String LINE_BREAK = "\r\n";
    public LifeCycleStatisticalResourceRepositoryImpl() {
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
