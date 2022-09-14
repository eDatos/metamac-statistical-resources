package org.siemac.metamac.statistical.resources.core.common.criteria.enums;

public enum StatisticalResourcesCriteriaPropertyEnum {

    // StatisticalReSource
    STATISTICAL_OPERATION_URN,

    // IdentifiableResource
    CODE,
    URN,

    // NameableResource
    TITLE,
    DESCRIPTION,

    // VersionableResource
    VERSION_RATIONALE_TYPE,
    NEXT_VERSION,
    NEXT_VERSION_DATE,

    // LifecyleResource
    PROC_STATUS,

    // Stream Status
    PUBLICATION_STREAM_STATUS,

    // SiemacResource
    TITLE_ALTERNATIVE,
    KEYWORDS,
    NEWNESS_UNTIL_DATE,

    // DatasetResource
    DATASET_GEOGRAPHIC_GRANULARITY_URN,
    DATASET_TEMPORAL_GRANULARITY_URN,
    DATASET_DATE_START,
    DATASET_DATE_END,
    DATASET_RELATED_DSD_URN,
    DATASET_DATE_NEXT_UPDATE,
    DATASET_STATISTIC_OFFICIALITY_IDENTIFIER,
    DATA,
    KEEP_ALL_DATA,

    // QueryResource
    QUERY_RELATED_DATASET_URN,
    QUERY_STATUS,
    QUERY_TYPE,

    // Others
    LAST_VERSION;

    public String value() {
        return name();
    }

    public static StatisticalResourcesCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}