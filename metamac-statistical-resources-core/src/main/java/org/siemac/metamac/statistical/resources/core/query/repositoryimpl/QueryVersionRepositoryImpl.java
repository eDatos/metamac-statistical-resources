package org.siemac.metamac.statistical.resources.core.query.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils.getRelatedResourceResultsFromRows;

import java.util.List;

import javax.persistence.Query;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.utils.RepositoryUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for QueryVersion
 */
@Repository("queryVersionRepository")
public class QueryVersionRepositoryImpl extends QueryVersionRepositoryBase {

    public QueryVersionRepositoryImpl() {
    }

    @Override
    public QueryVersion retrieveByUrn(String urn) throws MetamacException {

        List<ConditionalCriteria> condition = criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.lifeCycleStatisticalResource().urn()).eq(urn).distinctRoot().build();

        List<QueryVersion> result = findByCondition(condition);

        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.QUERY_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one query version with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public QueryVersion retrieveLastVersion(String queryUrn) throws MetamacException {
        // Prepare criteria
        //@formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).
                withProperty(QueryVersionProperties.query().identifiableStatisticalResource().urn()).eq(queryUrn)
                .and()
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().lastVersion()).eq(Boolean.TRUE)
                .distinctRoot().build();

        //@formatter:on
        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        // Find
        PagedResult<QueryVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return. We have at least one queryVersion
        if (result.getRowCount() == 0) {
            throw new MetamacException(ServiceExceptionType.QUERY_LAST_VERSION_NOT_FOUND, queryUrn);
        }

        return result.getValues().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryVersion retrieveLastPublishedVersion(String queryUrn) throws MetamacException {
        // Prepare criteria
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.query().identifiableStatisticalResource().urn())
                .eq(queryUrn).and().withProperty(QueryVersionProperties.lifeCycleStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
                .orderBy(CriteriaUtils.getDatetimedLeafProperty(QueryVersionProperties.lifeCycleStatisticalResource().creationDate(), QueryVersion.class)).descending().distinctRoot().build();

        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        // Find
        PagedResult<QueryVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return
        if (result.getRowCount() != 0) {
            return result.getValues().get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<QueryVersion> findLinkedToFixedDatasetVersion(Long datasetVersionId) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.fixedDatasetVersion().id()).eq(datasetVersionId).build();
        return findByCondition(conditions);
    }

    @Override
    public List<QueryVersion> findLinkedToDataset(Long datasetId) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.dataset().id()).eq(datasetId).build();
        return findByCondition(conditions);
    }

    @Override
    public List<QueryVersion> findQueriesPublishedLinkedToDataset(Long datasetId) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.dataset().id()).eq(datasetId).and()
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED).and()
                .withProperty(QueryVersionProperties.lifeCycleStatisticalResource().lastVersion()).eq(Boolean.TRUE).build();
        return findByCondition(conditions);
    }

    @Override
    public List<RelatedResourceResult> retrieveIsPartOf(QueryVersion queryVersion) throws MetamacException {
        return retrieveIsPartOf(queryVersion, false);
    }

    @Override
    public List<RelatedResourceResult> retrieveIsPartOfOnlyLastPublished(QueryVersion queryVersion) throws MetamacException {
        return retrieveIsPartOf(queryVersion, true);
    }

    @SuppressWarnings("unchecked")
    private List<RelatedResourceResult> retrieveIsPartOf(QueryVersion queryVersion, boolean onlyLastPublished) throws MetamacException {
        //     @formatter:off
        Query query = getEntityManager().createNativeQuery(
            "SELECT  distinct stat.code, " +
            "        stat.urn, " +
            "        operation.code AS operCode, " +
            "        operation.urn AS operUrn, " +
            "        maintainer.code_nested AS code_nested, " +
            "        stat.version_logic, "+
            "        loc.locale, " +
            "        loc.label " +
            "FROM    tb_elements_levels elem INNER JOIN tb_cubes cubes " +
            "            on cubes.ID = elem.table_fk, " +
            "        tb_publications_versions pub INNER JOIN tb_stat_resources stat " +
            "            ON pub.siemac_resource_fk = stat.ID, " +
            "        tb_external_items operation, " +
            "        tb_external_items maintainer, " +
            "        tb_queries query, " +
            "        tb_queries_versions query_version INNER JOIN tb_stat_resources stat_query "+
            "            ON query_version.lifecycle_resource_fk = stat_query.ID, "+
            "        tb_localised_strings loc "+
            "WHERE       cubes.query_fk = query.ID "+
            "    AND     query_version.query_fk = query.ID " +
            "    AND     query_version.ID = :queryVersionFk " +
            "    AND     stat_query.last_version = 'true' " +
            "    AND     elem.publication_version_all_fk = pub.ID " +
            "    AND     stat.title_fk = loc.international_string_fk " +
            "    AND    " + buildLastPublishedVersionConditions(onlyLastPublished) +
            "    AND     operation.ID = stat.stat_operation_fk " +
            "    AND     maintainer.id = stat.maintainer_fk");

        //     @formatter:on
        query.setParameter("queryVersionFk", queryVersion.getId());
        query.setParameter("publishedProcStatus", ProcStatusEnum.PUBLISHED.name());
        query.setParameter("now", new DateTime().toDate());

        List<Object> rows = query.getResultList();
        List<RelatedResourceResult> resources = getRelatedResourceResultsFromRows(rows, TypeRelatedResourceEnum.PUBLICATION_VERSION);
        return resources;
    }

    private String buildLastPublishedVersionConditions(boolean onlyLastPublished) {
        StringBuilder lastVersionCondition = new StringBuilder(" ( ");
        if (!onlyLastPublished) {
            lastVersionCondition.append(" stat.last_version = 'true' ").append(" OR ");
        }
        lastVersionCondition.append(RepositoryUtils.isLastPublishedVersionConditions).append(" ) ");
        return lastVersionCondition.toString();
    }

}
