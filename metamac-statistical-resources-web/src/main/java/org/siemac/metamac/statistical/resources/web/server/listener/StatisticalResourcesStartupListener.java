package org.siemac.metamac.statistical.resources.web.server.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.siemac.metamac.web.common.server.listener.InternalApplicationStartupListener;

public class StatisticalResourcesStartupListener extends InternalApplicationStartupListener {

    private static final Log LOG = LogFactory.getLog(StatisticalResourcesStartupListener.class);

    @Override
    public void checkDatasourceProperties() {
        // DATASOURCE
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_DRIVER_NAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_URL);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_USERNAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_PASSWORD);

        // DATASOURCE REPOSITORY
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_DRIVER_NAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_URL);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_USERNAME);
        checkRequiredProperty(StatisticalResourcesConfigurationConstants.DB_REPOSITORY_PASSWORD);

        // DATASOURCE DB DATA IMPORT
        checkDataImportDatasourceProperties();
    }

    private void checkDataImportDatasourceProperties() {
        try {
            checkOptionalProperty(StatisticalResourcesConfigurationConstants.DB_DATA_IMPORT_DRIVER_NAME);
            checkOptionalProperty(StatisticalResourcesConfigurationConstants.DB_DATA_IMPORT_URL);
            checkOptionalProperty(StatisticalResourcesConfigurationConstants.DB_DATA_IMPORT_USERNAME);
            checkOptionalProperty(StatisticalResourcesConfigurationConstants.DB_DATA_IMPORT_PASSWORD);
        } catch (Exception e) {
            LOG.warn("Data import datasource properties not configured correctly, data import feature will be unavailable: " + e.getMessage());
        }
    }

    @Override
    public void checkWebApplicationsProperties() {
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_SRM_INTERNAL_WEB);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_COMMON_METADATA_INTERNAL_WEB);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_PORTAL_INTERNAL_WEB_VISUALIZER);
        checkRequiredProperty(ConfigurationConstants.WEB_APPLICATION_PORTAL_EXTERNAL_WEB_VISUALIZER);
    }

    @Override
    public void checkApiProperties() {
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_SRM_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_COMMON_METADATA_EXTERNAL_API);
        checkRequiredProperty(ConfigurationConstants.ENDPOINT_NOTICES_INTERNAL_API);
    }

    @Override
    public void checkOtherModuleProperties() {
        // Common
        checkOptionalDefaultCodelistTemporalGranularityUrn();
        checkOptionalDefaultCodelistGeographicalGranularityUrn();
        checkOptionalDefaultCodeLanguageUrn();
        checkOptionalDefaultCodelistLanguagesUrn();

        // Specific
        checkOptionalProperty(StatisticalResourcesConfigurationConstants.HELP_URL);
        checkOptionalProperty(StatisticalResourcesConfigurationConstants.FILTER_COLUMN_NAME_FOR_DB_DATA_IMPORT);
        checkOptionalProperty(StatisticalResourcesConfigurationConstants.CRON_EXPRESSION_FOR_DB_DATA_IMPORT);
        checkOptionalProperty(StatisticalResourcesConfigurationConstants.CRON_EXPRESSION_FOR_GEOGRAPHIC_COVERAGE_CACHE_CLEAR);
        checkOptionalProperty(StatisticalResourcesConfigurationConstants.CRON_EXPRESSION_FOR_RESEND_DATASET_KAFKA_MESSAGE);

        // Confluent && Kafka
        checkKafkaProperties();
    }

    protected void checkKafkaProperties() {
        checkRequiredProperty(ConfigurationConstants.KAFKA_BOOTSTRAP_SERVERS);
        checkRequiredProperty(ConfigurationConstants.KAFKA_SCHEMA_REGISTRY_URL);
    }

    @Override
    public String projectName() {
        return "statistical-resources-internal";
    }
}
