package org.siemac.metamac.statistical.resources.web.server.utils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaRestriction;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaOrderEnum;
import org.siemac.metamac.statistical.resources.core.common.criteria.enums.StatisticalResourcesCriteriaPropertyEnum;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.LifeCycleStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.QueryVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.SiemacMetadataStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.base.HasDataCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.base.HasStatisticalOperationCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyLastVersionCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasSimpleCriteria;

public class MetamacWebCriteriaUtils {

    private MetamacWebCriteriaUtils() {
    }

    public static MetamacCriteriaRestriction buildMetamacCriteriaFromWebcriteria(MetamacWebCriteria webCriteria) {
        MetamacCriteriaConjunctionRestriction criteria = new MetamacCriteriaConjunctionRestriction();

        if (webCriteria instanceof HasSimpleCriteria) {
            addRestrictionIfExists(criteria, buildSimpleCriteria(webCriteria));
        }

        if (webCriteria instanceof HasStatisticalOperationCriteria) {
            addRestrictionIfExists(criteria, buildStatisticalOperationCriteria((HasStatisticalOperationCriteria) webCriteria));
        }

        if (webCriteria instanceof HasOnlyLastVersionCriteria) {
            addRestrictionIfExists(criteria, buildOnlyLastVersionCriteria((HasOnlyLastVersionCriteria) webCriteria));
        }

        if (webCriteria instanceof HasDataCriteria) {
            addRestrictionIfExists(criteria, buildMetamacCriteriaDatasetWithData((HasDataCriteria) webCriteria));
        }

        if (webCriteria instanceof VersionableStatisticalResourceWebCriteria) {
            VersionableStatisticalResourceWebCriteria versionableStatisticalResourceWebCriteria = (VersionableStatisticalResourceWebCriteria) webCriteria;
            addRestrictionIfExists(criteria, buildCodeCriteria(versionableStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildTitleCriteria(versionableStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildDescriptionCriteria(versionableStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildUrnCriteria(versionableStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildNextVersionTypeCriteria(versionableStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildNextVersionDateCriteria(versionableStatisticalResourceWebCriteria));
        }

        if (webCriteria instanceof LifeCycleStatisticalResourceWebCriteria) {
            LifeCycleStatisticalResourceWebCriteria lifeCycleStatisticalResourceWebCriteria = (LifeCycleStatisticalResourceWebCriteria) webCriteria;
            addRestrictionIfExists(criteria, buildProcStatusCriteria(lifeCycleStatisticalResourceWebCriteria));
        }

        if (webCriteria instanceof SiemacMetadataStatisticalResourceWebCriteria) {
            SiemacMetadataStatisticalResourceWebCriteria siemacMetadataStatisticalResourceWebCriteria = (SiemacMetadataStatisticalResourceWebCriteria) webCriteria;
            addRestrictionIfExists(criteria, buildTitleAlternativeCriteria(siemacMetadataStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildKeywordsCriteria(siemacMetadataStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildNewnessUntilDateCriteria(siemacMetadataStatisticalResourceWebCriteria));
            addRestrictionIfExists(criteria, buildPublicationStreamStatusCriteria(siemacMetadataStatisticalResourceWebCriteria));
        }

        if (webCriteria instanceof DatasetVersionWebCriteria) {
            DatasetVersionWebCriteria datasetVersionWebCriteria = (DatasetVersionWebCriteria) webCriteria;
            addRestrictionIfExists(criteria, buildGeographicGranularityCriteria(datasetVersionWebCriteria));
            addRestrictionIfExists(criteria, buildTemporalGranularityCriteria(datasetVersionWebCriteria));
            addRestrictionIfExists(criteria, buildDateStartCriteria(datasetVersionWebCriteria));
            addRestrictionIfExists(criteria, buildDateEndCriteria(datasetVersionWebCriteria));
            addRestrictionIfExists(criteria, buildDsdCriteria(datasetVersionWebCriteria));
            addRestrictionIfExists(criteria, buildDateNextUpdateCriteria(datasetVersionWebCriteria));
            addRestrictionIfExists(criteria, buildStatisticOfficialityCriteria(datasetVersionWebCriteria));
        }

        if (webCriteria instanceof QueryVersionWebCriteria) {
            QueryVersionWebCriteria queryVersionWebCriteria = (QueryVersionWebCriteria) webCriteria;
            addRestrictionIfExists(criteria, buildDatasetVersionCriteria(queryVersionWebCriteria));
            addRestrictionIfExists(criteria, buildQueryStatusCriteria(queryVersionWebCriteria));
            addRestrictionIfExists(criteria, buildQueryTypeCriteria(queryVersionWebCriteria));
        }

        return criteria;
    }

    public static MetamacCriteriaOrder buildMetamacCriteriaLastUpdatedOrder() {
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(StatisticalResourcesCriteriaOrderEnum.LAST_UPDATED.name());
        return order;
    }

    public static MetamacCriteriaRestriction buildMetamacCriteriaDatasetWithData(HasDataCriteria criteria) {
        if (BooleanUtils.isTrue(criteria.getHasData())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATA.name(), Boolean.TRUE, OperationType.EQ);
        } else if (BooleanUtils.isFalse(criteria.getHasData())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATA.name(), Boolean.FALSE, OperationType.EQ);
        }
        return null;
    }

    //
    // Private methods
    //

    private static void addRestrictionIfExists(MetamacCriteriaConjunctionRestriction criteria, MetamacCriteriaRestriction restriction) {
        if (restriction != null) {
            criteria.getRestrictions().add(restriction);
        }
    }

    private static MetamacCriteriaRestriction buildSimpleCriteria(HasSimpleCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getCriteria())) {
            MetamacCriteriaDisjunctionRestriction criteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            criteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            criteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
            criteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.TITLE.name(), criteria.getCriteria(), OperationType.ILIKE));
            return criteriaDisjuction;
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildStatisticalOperationCriteria(HasStatisticalOperationCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getStatisticalOperationUrn())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.STATISTICAL_OPERATION_URN.name(), criteria.getStatisticalOperationUrn(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildOnlyLastVersionCriteria(HasOnlyLastVersionCriteria criteria) {
        if (criteria.isOnlyLastVersion()) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.LAST_VERSION.name(), criteria.isOnlyLastVersion(), OperationType.EQ);
        } else if (criteria instanceof DatasetVersionWebCriteria) {
            // If the latest versions of the datasets are not searched, all versions of the dataset that save the observations and only the latest versions of the datasets that do not save the
            // observations will be searched
            // (KEEP_ALL_DATA == TRUE) OR (KEEP_ALL_DATA == FALSE AND LAST_VERSION == TRUE)
            MetamacCriteriaDisjunctionRestriction disjunctionRestriction = new MetamacCriteriaDisjunctionRestriction();
            disjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.KEEP_ALL_DATA.name(), Boolean.TRUE, OperationType.EQ));

            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.KEEP_ALL_DATA.name(), Boolean.FALSE, OperationType.EQ));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.LAST_VERSION.name(), Boolean.TRUE, OperationType.EQ));

            disjunctionRestriction.getRestrictions().add(conjunctionRestriction);

            return disjunctionRestriction;
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildCodeCriteria(VersionableStatisticalResourceWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getCode())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildTitleCriteria(VersionableStatisticalResourceWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getTitle())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.TITLE.name(), criteria.getTitle(), OperationType.ILIKE);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildUrnCriteria(VersionableStatisticalResourceWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getUrn())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildDescriptionCriteria(VersionableStatisticalResourceWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getDescription())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildProcStatusCriteria(LifeCycleStatisticalResourceWebCriteria criteria) {
        if (criteria.getProcStatus() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildTitleAlternativeCriteria(SiemacMetadataStatisticalResourceWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getTitleAlternative())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.TITLE_ALTERNATIVE.name(), criteria.getTitleAlternative(), OperationType.ILIKE);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildNextVersionTypeCriteria(VersionableStatisticalResourceWebCriteria criteria) {
        if (criteria.getNextVersionType() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION.name(), criteria.getNextVersionType(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildNextVersionDateCriteria(VersionableStatisticalResourceWebCriteria criteria) {
        if (criteria.getNextVersionDate() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.NEXT_VERSION_DATE.name(), criteria.getNextVersionDate(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildKeywordsCriteria(SiemacMetadataStatisticalResourceWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getKeywords())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.KEYWORDS.name(), criteria.getKeywords(), OperationType.ILIKE);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildNewnessUntilDateCriteria(SiemacMetadataStatisticalResourceWebCriteria criteria) {
        if (criteria.getNewnessUtilDate() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.NEWNESS_UNTIL_DATE.name(), criteria.getNewnessUtilDate(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildPublicationStreamStatusCriteria(SiemacMetadataStatisticalResourceWebCriteria criteria) {
        if (criteria.getPublicationStreamStatus() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.PUBLICATION_STREAM_STATUS.name(), criteria.getPublicationStreamStatus(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildGeographicGranularityCriteria(DatasetVersionWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getGeographicalGranularityUrn())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_GEOGRAPHIC_GRANULARITY_URN.name(), criteria.getGeographicalGranularityUrn(),
                    OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildTemporalGranularityCriteria(DatasetVersionWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getTemporalGranularityUrn())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_TEMPORAL_GRANULARITY_URN.name(), criteria.getTemporalGranularityUrn(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildDateStartCriteria(DatasetVersionWebCriteria criteria) {
        if (criteria.getDateStart() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_DATE_START.name(), criteria.getDateStart(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildDateEndCriteria(DatasetVersionWebCriteria criteria) {
        if (criteria.getDateEnd() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_DATE_END.name(), criteria.getDateEnd(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildDsdCriteria(DatasetVersionWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getDsdUrn())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_RELATED_DSD_URN.name(), criteria.getDsdUrn(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildDateNextUpdateCriteria(DatasetVersionWebCriteria criteria) {
        if (criteria.getDateNextUpdate() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_DATE_NEXT_UPDATE.name(), criteria.getDateNextUpdate(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildStatisticOfficialityCriteria(DatasetVersionWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getStatisticOfficialityIdentifier())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.DATASET_STATISTIC_OFFICIALITY_IDENTIFIER.name(), criteria.getStatisticOfficialityIdentifier(),
                    OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildDatasetVersionCriteria(QueryVersionWebCriteria criteria) {
        if (StringUtils.isNotBlank(criteria.getDatasetVersionUrn())) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.QUERY_RELATED_DATASET_URN.name(), criteria.getDatasetVersionUrn(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildQueryStatusCriteria(QueryVersionWebCriteria criteria) {
        if (criteria.getQueryStatus() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.QUERY_STATUS.name(), criteria.getQueryStatus(), OperationType.EQ);
        }
        return null;
    }

    private static MetamacCriteriaRestriction buildQueryTypeCriteria(QueryVersionWebCriteria criteria) {
        if (criteria.getQueryType() != null) {
            return new MetamacCriteriaPropertyRestriction(StatisticalResourcesCriteriaPropertyEnum.QUERY_TYPE.name(), criteria.getQueryType(), OperationType.EQ);
        }
        return null;
    }
}
