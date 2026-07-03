package org.siemac.metamac.statistical.resources.core.publication.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionProperties;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for PublicationVersion
 */
@Repository("publicationVersionRepository")
public class PublicationVersionRepositoryImpl extends PublicationVersionRepositoryBase {

    public PublicationVersionRepositoryImpl() {
    }

    @Override
    public PublicationVersion retrieveByUrn(String urn) throws MetamacException {

        // Prepare criteria
        List<ConditionalCriteria> condition = criteriaFor(PublicationVersion.class).withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().urn()).eq(urn).distinctRoot().build();

        // Find
        List<PublicationVersion> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one publication with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public PublicationVersion retrieveByUrnPublished(String urn) throws MetamacException {
        // Prepare criteria
        // @formatter:off
        List<ConditionalCriteria> condition = criteriaFor(PublicationVersion.class)
            .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().urn()).eq(urn)
            .and()
            .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
            .distinctRoot().build();
        // @formatter:on

        // Find
        List<PublicationVersion> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one publication with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public PublicationVersion retrieveLastVersion(String publicationUrn) throws MetamacException {

        // Prepare criteria
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(PublicationVersion.class)
                .withProperty(PublicationVersionProperties.publication().identifiableStatisticalResource().urn()).eq(publicationUrn)
                .and()
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE)
                .distinctRoot().build();
        // @formatter:on

        // Find
        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        PagedResult<PublicationVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return. We have at least one publicationVersion
        if (result.getRowCount() == 0) {
            throw new MetamacException(ServiceExceptionType.PUBLICATION_LAST_VERSION_NOT_FOUND, publicationUrn);
        }

        return result.getValues().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PublicationVersion retrieveLastPublishedVersion(String publicationUrn) throws MetamacException {
        // Prepare criteria
        Date now = new DateTime().toDate();
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(PublicationVersion.class)
                .withProperty(PublicationVersionProperties.publication().identifiableStatisticalResource().urn()).eq(publicationUrn)
                .and()
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
                .and()
                    .lbrace()
                        .withProperty(CriteriaUtils.getDatetimeLeafPropertyEmbedded(PublicationVersionProperties.siemacMetadataStatisticalResource().validTo(), PublicationVersion.class)).isNull()
                        .or()
                        .withProperty(CriteriaUtils.getDatetimeLeafPropertyEmbedded(PublicationVersionProperties.siemacMetadataStatisticalResource().validTo(), PublicationVersion.class)).greaterThan(now)
                    .rbrace()
                .orderBy(PublicationVersionProperties.siemacMetadataStatisticalResource().validFrom()).descending()
                .build();
        // @formatter:on

        PagingParameter paging = PagingParameter.rowAccess(0, 1);

        // Find
        PagedResult<PublicationVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return
        if (result.getRowCount() > 0) {
            return result.getValues().get(result.getRowCount() - 1);
        } else {
            return null;
        }
    }
    @Override
    public PublicationVersion retrieveByVersion(Long statisticalResourceId, String versionLogic) throws MetamacException {

        // Prepare criteria
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(PublicationVersion.class).withProperty(PublicationVersionProperties.publication().id()).eq(statisticalResourceId)
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().versionLogic()).eq(versionLogic).distinctRoot().build();

        // Find
        List<PublicationVersion> result = findByCondition(conditions);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_NOT_FOUND, statisticalResourceId, versionLogic);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one publication with id " + statisticalResourceId + " and versionLogic " + versionLogic + " found");
        }
        return result.get(0);
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByOnlyLastPublished(PublicationVersion publicationVersion) throws MetamacException {
        PublicationVersion next = publicationVersion;
        PublicationVersion replacing = null;

        while (next != null && next.getLifeCycleStatisticalResource().getIsReplacedByVersion() != null) {
            next = next.getLifeCycleStatisticalResource().getIsReplacedByVersion().getPublicationVersion();
            if (next.getLifeCycleStatisticalResource().getProcStatus() == ProcStatusEnum.PUBLISHED) {
                replacing = next;
            }
        }
        return RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.PUBLICATION_VERSION);
    }

    // TODO METAMAC-2718 Este método no parece estarse usando. Véase METAMAC-2578
    @Override
    public RelatedResourceResult retrieveIsReplacedByOnlyIfPublished(PublicationVersion publicationVersion) throws MetamacException {
        RelatedResourceResult result = null;
        if (publicationVersion != null && publicationVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy() != null) {
            PublicationVersion replacing = publicationVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy().getPublicationVersion();
            if (ProcStatusEnum.PUBLISHED == replacing.getLifeCycleStatisticalResource().getProcStatus()) {
                result = RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.PUBLICATION_VERSION);
            }
        }
        return result;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedBy(PublicationVersion publicationVersion) throws MetamacException {
        RelatedResource replacingRelated = publicationVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        RelatedResourceResult replacing = null;
        if (replacingRelated != null) {
            replacing = RelatedResourceResultUtils.from(replacingRelated.getPublicationVersion(), TypeRelatedResourceEnum.PUBLICATION_VERSION);
        }

        return replacing;
    }

    @Override
    public List<PublicationVersion> findPublishedContainingResource(String resourceRootUrn, StatisticalResourceTypeEnum containedResourceType) {
        Property<PublicationVersion> containedUrnProperty;
        switch (containedResourceType) {
            case DATASET:
                containedUrnProperty = PublicationVersionProperties.childrenAllLevels().cube().dataset().identifiableStatisticalResource().urn();
                break;
            case QUERY:
                containedUrnProperty = PublicationVersionProperties.childrenAllLevels().cube().query().identifiableStatisticalResource().urn();
                break;
            case MULTIDATASET:
                containedUrnProperty = PublicationVersionProperties.childrenAllLevels().cube().multidataset().identifiableStatisticalResource().urn();
                break;
            case COLLECTION:
                containedUrnProperty = PublicationVersionProperties.childrenAllLevels().cube().publication().identifiableStatisticalResource().urn();
                break;
            default:
                return Collections.emptyList();
        }

        List<ConditionalCriteria> conditions = criteriaFor(PublicationVersion.class)
                .withProperty(containedUrnProperty).eq(resourceRootUrn)
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().validTo()).isNull()
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
                .distinctRoot()
                .build();

        return findByCondition(conditions);
    }
}
