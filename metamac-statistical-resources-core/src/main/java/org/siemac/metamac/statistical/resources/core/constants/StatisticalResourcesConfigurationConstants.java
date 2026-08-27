package org.siemac.metamac.statistical.resources.core.constants;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;

public class StatisticalResourcesConfigurationConstants extends ConfigurationConstants {

    // PROPERTIES SPECIFIED IN THE DATA DIRECTORY

    // Configuration

    public static final String HELP_URL                                                    = "metamac.statistical_resources.help.url";
    public static final String DOT_CODE_MAPPING                                            = "metamac.statistical_resources.dot_code_mapping";
    public static final String FILTER_COLUMN_NAME_FOR_DB_DATA_IMPORT                       = "metamac.statistical_resources.data_import.filter_column_name";
    public static final String CRON_EXPRESSION_FOR_DB_DATA_IMPORT                          = "metamac.statistical_resources.data_import.cron_expression";
    public static final String DATABASE_DATASET_IMPORT_ENABLED                             = "environment.metamac.statistical_resources.data_import.enabled";
    public static final String DB_DATA_VIEWS_ROLE                                          = "metamac.statistical_resources.bbbd.data_views_role";
    public static final String CRON_EXPRESSION_FOR_GEOGRAPHIC_COVERAGE_CACHE_CLEAR         = "metamac.statistical_resources.geografic_coverage_cache_clear.cron_expression";
    public static final String CRON_EXPRESSION_FOR_RESEND_DATASET_KAFKA_MESSAGE            = "metamac.statistical_resources.resend_dataset_kafka_message.cron_expression";
    public static final String QUARTZ_TRIGGER_DELAY_FOR_GEOGRAPHIC_COVERAGE_CACHE          = "metamac.statistical_resources.geo_cache_update.quartz_scheduler";
    public static final String QUARTZ_TRIGGER_DELAY_FOR_RECOVERY_GEOGRAPHIC_COVERAGE_CACHE = "metamac.statistical_resources.geo_cache_recovery.quartz_scheduler";
    public static final String QUARTZ_TRIGGER_DELAY_FOR_RECOVERY_UPDATE_DATES             = "metamac.statistical_resources.update_dates.quartz_scheduler";

    // DataSources

    public static final String DB_URL                                                      = "metamac.statistical_resources.db.url";
    public static final String DB_USERNAME                                                 = "metamac.statistical_resources.db.username";
    public static final String DB_PASSWORD                                                 = "metamac.statistical_resources.db.password";
    public static final String DB_DRIVER_NAME                                              = "metamac.statistical_resources.db.driver_name";

    public static final String DB_REPOSITORY_URL                                           = "metamac.statistical_resources.repo.db.url";
    public static final String DB_REPOSITORY_USERNAME                                      = "metamac.statistical_resources.repo.db.username";
    public static final String DB_REPOSITORY_PASSWORD                                      = "metamac.statistical_resources.repo.db.password";
    public static final String DB_REPOSITORY_DRIVER_NAME                                   = "metamac.statistical_resources.repo.db.driver_name";

    public static final String DB_DATA_IMPORT_URL                                          = "metamac.statistical_resources.data_import.db.url";
    public static final String DB_DATA_IMPORT_USERNAME                                     = "metamac.statistical_resources.data_import.db.username";
    public static final String DB_DATA_IMPORT_PASSWORD                                     = "metamac.statistical_resources.data_import.db.password";                       // NOSONAR
    public static final String DB_DATA_IMPORT_DRIVER_NAME                                  = "metamac.statistical_resources.data_import.db.driver_name";

    public static final String DB_DRIVER_NAME_POSTGRESQL                                   = "org.postgresql.Driver";

    // X variables
    public static final String TWITTER_ACCES_TOKEN                                 = "metamac.statistical_resources.twitter.accessToken";
    public static final String TWITTER_ACCES_TOKEN_SECRET                          = "metamac.statistical_resources.twitter.accessTokenSecret";
    public static final String TWITTER_API_KEY                                     = "metamac.statistical_resources.twitter.apiKey";
    public static final String TWITTER_API_SECRET_KEY                              = "metamac.statistical_resources.twitter.apiSecretKey";
    public static final String TWITTER_SENT_ENABLE                                 = "metamac.statistical_resources.twitter.enabled";

    // if it is only necessary to reload jaxi messages for e-catalogo it is better to disabled this consumer to avoid bad performance.
    public static final String DISABLED_JAXI_PUBLICATIONS_CONSUMER                 = "metamac.statistical_resources.kafka.jaxi_publication_consumer_disabled";

}
