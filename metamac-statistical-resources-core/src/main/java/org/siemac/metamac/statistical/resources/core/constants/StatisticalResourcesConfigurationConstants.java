package org.siemac.metamac.statistical.resources.core.constants;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;

public class StatisticalResourcesConfigurationConstants extends ConfigurationConstants {

    // PROPERTIES SPECIFIED IN THE DATA DIRECTORY

    // Configuration

    public static final String HELP_URL                              = "metamac.statistical_resources.help.url";
    public static final String DOCS_PATH                             = "metamac.data.docs.statistical_resources.path";
    public static final String DOT_CODE_MAPPING                      = "metamac.statistical_resources.dot_code_mapping";
    public static final String FILTER_COLUMN_NAME_FOR_DB_DATA_IMPORT = "metamac.statistical_resources.data_import.filter_column_name";
    public static final String CRON_EXPRESSION_FOR_DB_DATA_IMPORT    = "metamac.statistical_resources.data_import.cron_expression";
    public static final String DATABASE_DATASET_IMPORT_ENABLED       = "environment.metamac.statistical_resources.data_import.enabled";
    public static final String DB_DATA_VIEWS_ROLE                    = "metamac.statistical_resources.bbbd.data_views_role";
 
    // DataSources

    public static final String DB_URL                                = "metamac.statistical_resources.db.url";
    public static final String DB_USERNAME                           = "metamac.statistical_resources.db.username";
    public static final String DB_PASSWORD                           = "metamac.statistical_resources.db.password";
    public static final String DB_DIALECT                            = "metamac.statistical_resources.db.dialect";
    public static final String DB_DRIVER_NAME                        = "metamac.statistical_resources.db.driver_name";

    public static final String DB_REPOSITORY_URL                     = "metamac.statistical_resources.repo.db.url";
    public static final String DB_REPOSITORY_USERNAME                = "metamac.statistical_resources.repo.db.username";
    public static final String DB_REPOSITORY_PASSWORD                = "metamac.statistical_resources.repo.db.password";
    public static final String DB_REPOSITORY_DIALECT                 = "metamac.statistical_resources.repo.db.dialect";
    public static final String DB_REPOSITORY_DRIVER_NAME             = "metamac.statistical_resources.repo.db.driver_name";

    public static final String DB_DATA_IMPORT_URL                    = "metamac.statistical_resources.data_import.db.url";
    public static final String DB_DATA_IMPORT_USERNAME               = "metamac.statistical_resources.data_import.db.username";
    public static final String DB_DATA_IMPORT_PASSWORD               = "metamac.statistical_resources.data_import.db.password";        // NOSONAR
    public static final String DB_DATA_IMPORT_DRIVER_NAME            = "metamac.statistical_resources.data_import.db.driver_name";

    public static final String DB_DRIVER_NAME_POSTGRESQL             = "org.postgresql.Driver";

}
