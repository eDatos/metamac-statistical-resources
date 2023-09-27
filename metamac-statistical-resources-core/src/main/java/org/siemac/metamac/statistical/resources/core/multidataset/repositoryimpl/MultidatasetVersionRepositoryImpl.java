package org.siemac.metamac.statistical.resources.core.multidataset.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils.getRelatedResourceResultsFromSiemacResourceRows;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RepositoryUtils.isLastPublishedVersionConditions;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionProperties;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for MultidatasetVersion
 */
@Repository("multidatasetVersionRepository")
public class MultidatasetVersionRepositoryImpl extends MultidatasetVersionRepositoryBase {

    public MultidatasetVersionRepositoryImpl() {
    }

    @Override
    public MultidatasetVersion retrieveByUrn(String urn) throws MetamacException {

        // Prepare criteria
        List<ConditionalCriteria> condition = criteriaFor(MultidatasetVersion.class).withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().urn()).eq(urn).distinctRoot()
                .build();

        // Find
        List<MultidatasetVersion> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one multidataset with urn " + urn);
        }

        return result.get(0);

    }

    @Override
    public MultidatasetVersion retrieveByUrnPublished(String urn) throws MetamacException {
        // Prepare criteria
        // @formatter:off
        List<ConditionalCriteria> condition = criteriaFor(MultidatasetVersion.class)
            .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().urn()).eq(urn)
            .and()
            .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
            .distinctRoot().build();
        // @formatter:on

        // Find
        List<MultidatasetVersion> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one multidataset with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public MultidatasetVersion retrieveLastVersion(String multidatasetUrn) throws MetamacException {

        // Prepare criteria
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(MultidatasetVersion.class)
                .withProperty(MultidatasetVersionProperties.multidataset().identifiableStatisticalResource().urn()).eq(multidatasetUrn)
                .and()
                .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE)
                .distinctRoot().build();
        // @formatter:on

        // Find
        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        PagedResult<MultidatasetVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return. We have at least one multidatasetVersion
        if (result.getRowCount() == 0) {
            throw new MetamacException(ServiceExceptionType.MULTIDATASET_LAST_VERSION_NOT_FOUND, multidatasetUrn);
        }

        return result.getValues().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MultidatasetVersion retrieveLastPublishedVersion(String multidatasetUrn) {
        // Prepare criteria
        Date now = new DateTime().toDate();
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(MultidatasetVersion.class)
                .withProperty(MultidatasetVersionProperties.multidataset().identifiableStatisticalResource().urn()).eq(multidatasetUrn)
                .and()
                .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
                .and()
                    .lbrace()
                        .withProperty(CriteriaUtils.getDatetimeLeafPropertyEmbedded(MultidatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), MultidatasetVersion.class)).isNull()
                        .or()
                        .withProperty(CriteriaUtils.getDatetimeLeafPropertyEmbedded(MultidatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), MultidatasetVersion.class)).greaterThan(now)
                    .rbrace()
                .orderBy(MultidatasetVersionProperties.siemacMetadataStatisticalResource().validFrom()).descending()
                .build();
        // @formatter:on

        PagingParameter paging = PagingParameter.rowAccess(0, 1);

        // Find
        PagedResult<MultidatasetVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return
        if (result.getRowCount() > 0) {
            return result.getValues().get(result.getRowCount() - 1);
        } else {
            return null;
        }
    }

    @Override
    public MultidatasetVersion retrieveByVersion(Long statisticalResourceId, String versionLogic) throws MetamacException {

        // Prepare criteria
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(MultidatasetVersion.class).withProperty(MultidatasetVersionProperties.multidataset().id())
                .eq(statisticalResourceId).withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().versionLogic()).eq(versionLogic).distinctRoot().build();

        // Find
        List<MultidatasetVersion> result = findByCondition(conditions);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.MULTIDATASET_VERSION_NOT_FOUND, statisticalResourceId, versionLogic);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one multidataset with id " + statisticalResourceId + " and versionLogic " + versionLogic + " found");
        }
        return result.get(0);

    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByOnlyLastPublished(MultidatasetVersion multidatasetVersion) {
        MultidatasetVersion next = multidatasetVersion;
        MultidatasetVersion replacing = null;

        while (next != null && next.getLifeCycleStatisticalResource().getIsReplacedByVersion() != null) {
            next = next.getLifeCycleStatisticalResource().getIsReplacedByVersion().getMultidatasetVersion();
            if (next.getLifeCycleStatisticalResource().getProcStatus() == ProcStatusEnum.PUBLISHED) {
                replacing = next;
            }
        }
        return RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.MULTIDATASET_VERSION);
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByOnlyIfPublished(MultidatasetVersion multidatasetVersion) {
        RelatedResourceResult result = null;
        if (multidatasetVersion != null && multidatasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy() != null) {
            MultidatasetVersion replacing = multidatasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy().getMultidatasetVersion();
            if (ProcStatusEnum.PUBLISHED == replacing.getLifeCycleStatisticalResource().getProcStatus()) {
                result = RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.MULTIDATASET_VERSION);
            }
        }
        return result;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedBy(MultidatasetVersion multidatasetVersion) {
        RelatedResource replacingRelated = multidatasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        RelatedResourceResult replacing = null;
        if (replacingRelated != null) {
            replacing = RelatedResourceResultUtils.from(replacingRelated.getMultidatasetVersion(), TypeRelatedResourceEnum.MULTIDATASET_VERSION);
        }

        return replacing;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RelatedResourceResult> retrieveIsPartOf(MultidatasetVersion multidatasetVersion) throws MetamacException {
        //     @formatter:off
        Query query = getEntityManager().createNativeQuery(
                "SELECT  distinct stat.code, " +
                "        stat.urn, " +
                "        operation.code AS operCode, " +
                "        operation.urn AS operUrn, " +
                "        maintainer.code_nested AS code_nested, " +
                "        stat.version_logic, "+
                "        loc.locale, " +
                "        loc.label, " +
                "        stat.type " +
                "FROM    tb_elements_levels elem INNER JOIN tb_cubes cubes " +
                "            on cubes.ID = elem.table_fk, " +
                "        tb_publications_versions pub INNER JOIN tb_stat_resources stat " +
                "            ON pub.siemac_resource_fk = stat.ID, " +
                "        tb_external_items operation, " +
                "        tb_external_items maintainer, " +
                "        tb_multidatasets multidataset," +
                "        tb_multidatasets_versions multidataset_version INNER JOIN tb_stat_resources stat_multidataset "+
                "            ON multidataset_version.siemac_resource_fk = stat_multidataset.ID, "+
                "        tb_localised_strings loc "+
                "WHERE       cubes.multidataset_fk = multidataset.ID "+
                "    AND     multidataset_version.multidataset_fk = multidataset.ID " +
                "    AND     multidataset_version.ID = :multidatasetVersionFk " +
                "    AND     stat_multidataset.last_version = " + getBooleanValueForDatabase(true) +
                "    AND     elem.publication_version_all_fk = pub.ID " +
                "    AND     stat.title_fk = loc.international_string_fk " +
                "    AND     (stat.last_version = " + getBooleanValueForDatabase(true) +
                "           OR "+isLastPublishedVersionConditions+") " +
                "    AND     operation.ID = stat.stat_operation_fk " +
                "    AND     maintainer.id = stat.maintainer_fk");
        //     @formatter:on
        query.setParameter("multidatasetVersionFk", multidatasetVersion.getId());
        query.setParameter("publishedProcStatus", ProcStatusEnum.PUBLISHED.name());
        query.setParameter("now", new DateTime().toDate());

        List<Object> rows = query.getResultList();
        List<RelatedResourceResult> resources = getRelatedResourceResultsFromSiemacResourceRows(rows);
        return resources;
    }

    private String getBooleanValueForDatabase(boolean value) throws MetamacException {
        return value ? "true" : "false";
    }
}
