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

    final String STATISTICAL_RESOURCES_JAXI_MESSAGES_GROUP = "STATISTICAL_RESOURCES_JAXI_MESSAGES_GROUP";
    
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
    public String retrieveDocsPath() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.DOCS_PATH);
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
    public boolean retriveDatabaseDatasetImportJobIsEnabled() {
        return environmentConfigurationProperties.getBoolean(StatisticalResourcesConfigurationConstants.DATABASE_DATASET_IMPORT_ENABLED, Boolean.FALSE);
    }

    @Override
    public String retrieveDbDataViewsRole() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.DB_DATA_VIEWS_ROLE);
    }
    
    @Override
    public String retrieveKafkaExternalPublicationsTopicName() throws MetamacException {
        return retrieveProperty(StatisticalResourcesConfigurationConstants.EXTERNAL_PUBLICATIONS_TOPIC_NAME);
    }
    
    @Override
    public Boolean retrieveKafkaExternalPublicationsTopicIsEnabled() throws MetamacException {
        return retrievePropertyBoolean(StatisticalResourcesConfigurationConstants.EXTERNAL_PUBLICATIONS_TOPIC_IS_ENABLED);
    }
    
    @Override
    public String retrieveKafkaJaxiMessagesGroup() throws MetamacException {
        return STATISTICAL_RESOURCES_JAXI_MESSAGES_GROUP; 
    }
}
