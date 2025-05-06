package org.siemac.metamac.statistical.resources.core.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.conf.ConfigurationServiceImpl;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;

public class StatisticalResourcesConfigurationImpl extends ConfigurationServiceImpl implements StatisticalResourcesConfiguration {

    final String STATISTICAL_RESOURCES_EXTERNAL_DATASET_PUBLICATION_MESSAGES_GROUP        = "STATISTICAL_RESOURCES_EXTERNAL_DATASET_PUBLICATION_MESSAGES_GROUP";
    final String STATISTICAL_RESOURCES_CUSTOM_EXTERNAL_DATASET_PUBLICATION_MESSAGES_GROUP = "STATISTICAL_RESOURCES_CUSTOM_EXTERNAL_DATASET_PUBLICATION_MESSAGES_GROUP";

    @Override
    public Map<KeyDotEnum, String> retrieveDotCodeMapping() throws MetamacException {

        Map<KeyDotEnum, String> dotCodeMappingMap = new HashMap<StatisticalResourcesConfiguration.KeyDotEnum, String>(6);
        List<Object> dotCodeMappingList = retrievePropertyList(StatisticalResourcesConfigurationConstants.DOT_CODE_MAPPING);

        for (Object item : dotCodeMappingList) {
            String[] splitItem = ((String) item).split("=");
            try {
                KeyDotEnum key = KeyDotEnum.valueOf(splitItem[0].trim());
                dotCodeMappingMap.put(key, splitItem[1].trim());
            } catch (IllegalArgumentException e) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_INVALID)
                        .withMessageParameters(StatisticalResourcesConfigurationConstants.DOT_CODE_MAPPING).build();
            }
        }

        if (dotCodeMappingMap.size() != 6) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONFIGURATION_PROPERTY_INVALID)
                    .withMessageParameters(StatisticalResourcesConfigurationConstants.DOT_CODE_MAPPING).build();
        }

        return dotCodeMappingMap;
    }

    @Override
    public String retrieveHelpUrl() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.HELP_URL);
    }

    @Override
    public String retriveFilterColumnNameForDbDataImport() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.FILTER_COLUMN_NAME_FOR_DB_DATA_IMPORT);
    }

    @Override
    public String retriveCronExpressionForDbDataImport() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.CRON_EXPRESSION_FOR_DB_DATA_IMPORT);
    }

    @Override
    public String retrieveCronExpressionForGeograficCoverageCacheClear() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.CRON_EXPRESSION_FOR_GEOGRAPHIC_COVERAGE_CACHE_CLEAR);
    }

    @Override
    public boolean retriveDatabaseDatasetImportJobIsEnabled() {
        return environmentConfigurationProperties.getBoolean(StatisticalResourcesConfigurationConstants.DATABASE_DATASET_IMPORT_ENABLED, Boolean.FALSE);
    }

    @Override
    public String retrieveDbDataViewsRole() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.DB_DATA_VIEWS_ROLE);
    }

    @Override
    public String retrieveTwitterApiKey() throws MetamacException {
        try {
            return retrieveProperty(StatisticalResourcesConfigurationConstants.TWITTER_API_KEY);
        } catch (MetamacException e) {
            return null;
        }
    }

    @Override
    public String retrieveTwitterApiSecretKey() throws MetamacException {
        try {
            return retrieveProperty(StatisticalResourcesConfigurationConstants.TWITTER_API_SECRET_KEY);
        } catch (MetamacException e) {
            return null;
        }
    }

    @Override
    public String retrieveTwitterAccessToken() throws MetamacException {
        try {
            return retrieveProperty(StatisticalResourcesConfigurationConstants.TWITTER_ACCES_TOKEN);
        } catch (MetamacException e) {
            return null;
        }
    }

    @Override
    public boolean retrieveTwitterSentEnable() throws MetamacException {
        try {
            String property = retrieveProperty(StatisticalResourcesConfigurationConstants.TWITTER_SENT_ENABLE);
            return Boolean.valueOf(property);
        } catch (MetamacException e) {
            return false;
        }
    }

    @Override
    public String retrieveTwitterAccesTokenSecret() throws MetamacException {
        try {
            return retrieveProperty(StatisticalResourcesConfigurationConstants.TWITTER_ACCES_TOKEN_SECRET);
        } catch (MetamacException e) {
            return null;
        }
    }

    @Override
    public String retrieveKafkaExternalDatasetPublicationMessagesGroup() throws MetamacException {
        return STATISTICAL_RESOURCES_EXTERNAL_DATASET_PUBLICATION_MESSAGES_GROUP;
    }

    @Override
    public String retrieveKafkaCustomExternalDatasetPublicationMessagesGroup() throws MetamacException {
        return STATISTICAL_RESOURCES_CUSTOM_EXTERNAL_DATASET_PUBLICATION_MESSAGES_GROUP;
    }

    @Override
    public String retrieveCronExpressionForResendPublishedDatasetKafkaMessage() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.CRON_EXPRESSION_FOR_RESEND_DATASET_KAFKA_MESSAGE);
    }
}
