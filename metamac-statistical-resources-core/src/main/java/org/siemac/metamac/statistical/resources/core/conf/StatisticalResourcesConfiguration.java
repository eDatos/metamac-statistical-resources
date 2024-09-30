package org.siemac.metamac.statistical.resources.core.conf;

import java.util.Map;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface StatisticalResourcesConfiguration extends ConfigurationService {

    static enum KeyDotEnum {
        ONE_DOT, TWO_DOT, THREE_DOT, FOUR_DOT, FIVE_DOT, SIX_DOT;
    }

    public Map<KeyDotEnum, String> retrieveDotCodeMapping() throws MetamacException;

    public String retrieveHelpUrl() throws MetamacException;

    public String retriveFilterColumnNameForDbDataImport() throws MetamacException;

    public String retriveCronExpressionForDbDataImport() throws MetamacException;

    public String retrieveCronExpressionForGeograficCoverageCacheClear() throws MetamacException;

    public boolean retriveDatabaseDatasetImportJobIsEnabled();

    public String retrieveDbDataViewsRole() throws MetamacException;

    public String retrieveKafkaExternalDatasetPublicationMessagesGroup() throws MetamacException;

    public String retrieveKafkaCustomExternalDatasetPublicationMessagesGroup() throws MetamacException;

    public String retrieveCronExpressionForResendPublishedDatasetKafkaMessage() throws MetamacException;

    public String retrieveKafkaCustomCodelistPublicationMessagesGroup() throws MetamacException;

    public String retrieveKafkaCustomConceptSchemePublicationMessagesGroup() throws MetamacException;

    public String retrieveTwitterApiKey() throws MetamacException;

    public String retrieveTwitterApiSecretKey() throws MetamacException;

    public String retrieveTwitterAccessToken() throws MetamacException;

    public boolean retrieveTwitterSentEnable() throws MetamacException;

    public String retrieveTwitterAccesTokenSecret() throws MetamacException;
    public String retrieveKafkaExternalCollectionPublicationMessagesGroup() throws MetamacException;

}
