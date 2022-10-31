package org.siemac.metamac.statistical.resources.core.utils.mocks;

import java.util.Map;

import org.siemac.metamac.common.test.mock.ConfigurationServiceMockImpl;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;

public class StatisticalResourcesConfigurationMockImpl extends ConfigurationServiceMockImpl implements StatisticalResourcesConfiguration {

    @Override
    public Map<KeyDotEnum, String> retrieveDotCodeMapping() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }

    @Override
    public String retrieveDocsPath() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }

    @Override
    public String retrieveHelpUrl() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }

    @Override
    public String retriveFilterColumnNameForDbDataImport() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }

    @Override
    public String retriveCronExpressionForDbDataImport() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }

    @Override
    public boolean retriveDatabaseDatasetImportJobIsEnabled() {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }

    @Override
    public String retrieveDbDataViewsRole() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }
    
    @Override
    public String retrieveKafkaJaxiMessagesGroup() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }
    
    @Override
    public String retrieveKafkaCustomJaxiMessagesGroup() throws MetamacException {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }
    
    @Override
    public String retrieveKafkaExternalPublicationsTopicName() {
        throw new UnsupportedOperationException("Not implemented. Not necessary for mocking purposes");
    }
}
