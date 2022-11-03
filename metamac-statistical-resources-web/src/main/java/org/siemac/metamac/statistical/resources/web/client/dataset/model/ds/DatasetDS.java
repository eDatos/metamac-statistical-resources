package org.siemac.metamac.statistical.resources.web.client.dataset.model.ds;

import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;

public class DatasetDS extends SiemacMetadataDS {

    // IDENTIFIERS
    public static final String DATASET_REPOSITORY_ID      = "ds-rep-id";
    public static final String VIEW_CODE                  = "ds-view-code";

    // CONTENT DESCRIPTORS
    public static final String GEOGRAPHIC_COVERAGE        = "ds-geo-cov";
    public static final String TEMPORAL_COVERAGE          = "ds-tem-cov";
    public static final String GEOGRAPHIC_GRANULARITY     = "ds-geo-gra";
    public static final String TEMPORAL_GRANULARITY       = "ds-tem-gra";
    public static final String DATE_START                 = "ds-date-start";
    public static final String DATE_END                   = "ds-date-end";
    public static final String STATISTICAL_UNIT           = "ds-stat-unit";
    public static final String MEASURES                   = "ds-meas";

    // CLASS DESCRIPTORS
    public static final String FORMAT_EXTENT_OBSERVATIONS = "ds-for-ext-obs";
    public static final String FORMAT_EXTENT_DIMENSIONS   = "ds-for-ext-dim";

    // PUBLICATION DESCRIPTORS
    public static final String DATE_NEXT_UPDATE           = "ds-date-next-up";
    public static final String UPDATE_FRECUENCY           = "ds-up-freq";
    public static final String STATISTIC_OFFICIALITY      = "ds-sta-off";
    public static final String BIBLIOGRAPHIC_CITATION     = "ds-bib-cit";

    // PRODUCTION DESCRIPTORS
    public static final String RELATED_DSD                = "ds-dsd";
    public static final String RELATED_DSD_VIEW           = "ds-dsd-view";

    // RESOURCE RELATION DESCRIPTORS
    public static final String IS_REQUIRED_BY             = "ds-is-req-by";
    public static final String IS_PART_OF                 = "ds-is-part-of";

    // OTHERS
    public static final String KEEP_ALL_DATA              = "ds-keep-all-data";
    public static final String DATA_SOURCE_TYPE           = "ds-dataSourceType";
    public static final String DATE_LAST_TIME_DATA_IMPORT = "ds-dateLastTimeDataImport";
    public static final String MAP_CODELISTS              = "ds-mapcodelists";
}
