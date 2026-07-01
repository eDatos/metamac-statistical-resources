package org.siemac.metamac.statistical.resources.core.dataset.repository.api;

import java.util.List;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface DatabaseImportRepository {

    public static final String BEAN_ID = "databaseImportRepository";

    public boolean checkTableExists(String tableName) throws MetamacException;
    public boolean checkTableHasColumn(String tableName, String columnName) throws MetamacException;

    public List<String[]> getObservations(String tableName, List<String> columnsName, String filterColumnName, DateTime filterValue) throws MetamacException;

}
