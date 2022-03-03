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

    public String retrieveDocsPath() throws MetamacException;

    public String retriveFilterColumnNameForDbDataImport() throws MetamacException;

    public String retriveCronExpressionForDbDataImport() throws MetamacException;

    public boolean retriveDatabaseDatasetImportJobIsEnabled();

    /**
     * Checks if Database is Oracle
     */
    public Boolean isDatabaseOracle() throws MetamacException;

    /**
     * Checks if Database is PostgreSQL
     */
    public Boolean isDatabasePostgreSQL() throws MetamacException;

    public String retrieveDbDataViewsRole() throws MetamacException;
}
