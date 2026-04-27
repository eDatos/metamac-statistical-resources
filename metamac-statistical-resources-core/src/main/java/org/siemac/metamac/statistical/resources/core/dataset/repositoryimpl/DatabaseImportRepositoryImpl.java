package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.dataset.repository.api.DatabaseImportRepository;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository(DatabaseImportRepository.BEAN_ID)
@Lazy
public class DatabaseImportRepositoryImpl implements DatabaseImportRepository {

    private static Logger logger = LoggerFactory.getLogger(DatabaseImportRepositoryImpl.class);

    private JdbcTemplate  jdbcTemplate;

    @Autowired
    @Lazy
    @Qualifier("dataSourceDatabaseImportRepository")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<String[]> getObservations(String tableName, List<String> columnsName, String filterColumnName, DateTime filterValue) throws MetamacException {
        checkGetObservations(tableName, columnsName, filterColumnName, filterValue);

        String sqlQuery = buildQuery(tableName, columnsName, filterColumnName, filterValue);
        logger.debug(sqlQuery);

        try {
            return jdbcTemplate.query(sqlQuery, new ObservationMapper(columnsName));
        } catch (DataAccessException e) {
            logger.error("Error accessing database for table: {}", tableName, e);
            MetamacException exception = new MetamacException();
            exception.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.DB_IMPORT_DATASET_JOB_ERROR));
            throw exception;
        }
    }

    @Override
    public boolean checkTableExists(String tableName) {
        try {
            return StringUtils.isNotBlank(tableName) && jdbcTemplate.execute(new ConnectionCallback<Boolean>() {

                @Override
                public Boolean doInConnection(Connection con) throws SQLException {
                    boolean found = Boolean.FALSE;

                    DatabaseMetaData databaseMetaData = con.getMetaData();

                    ResultSet resultSet = databaseMetaData.getTables(null, null, getNonCaseSensitiveIdentifier(databaseMetaData, tableName), null);

                    while (resultSet.next() && !found) {
                        found = StringUtils.equalsIgnoreCase(resultSet.getString("TABLE_NAME"), tableName);
                    }
                    return found;
                }
            });
        } catch (DataAccessException e) {
            logger.error("Error checking if table exists: {}", tableName, e);
            return false;
        }
    }

    @Override
    public boolean checkTableHasColumn(String tableName, String columnName) {
        try {
            return StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(columnName) && jdbcTemplate.execute(new ConnectionCallback<Boolean>() {

                @Override
                public Boolean doInConnection(Connection con) throws SQLException {
                    boolean found = Boolean.FALSE;

                    DatabaseMetaData databaseMetaData = con.getMetaData();

                    ResultSet resultSet = databaseMetaData.getColumns(null, null, getNonCaseSensitiveIdentifier(databaseMetaData, tableName), getNonCaseSensitiveIdentifier(databaseMetaData, columnName));

                    while (resultSet.next() && !found) {
                        found = StringUtils.equalsIgnoreCase(resultSet.getString("COLUMN_NAME"), columnName);
                    }

                    return found;
                }
            });
        } catch (DataAccessException e) {
            logger.error("Error checking if table has column: {}", tableName + " - " + columnName, e);
            return false;
        }
    }

    private void checkGetObservations(String tableName, List<String> columnsName, String filterColumnName, DateTime filterValue) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();

        checkTableName(tableName, exceptionItems);
        checkColumnsName(columnsName, exceptionItems);
        checkFilterColumnName(filterColumnName, filterValue, exceptionItems);

        if (!exceptionItems.isEmpty()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }

    }

    private void checkTableName(String tableName, List<MetamacExceptionItem> exceptionItems) {
        if (StringUtils.isBlank(tableName)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.TABLE_NAME));
        }
    }

    private void checkColumnsName(List<String> columnsName, List<MetamacExceptionItem> exceptionItems) {
        if (CollectionUtils.isEmpty(columnsName)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.COLUMNS_NAME));
        } else {
            for (String columnName : columnsName) {
                if (StringUtils.isBlank(columnName)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.COLUMNS_NAME));
                }
            }
        }
    }

    private void checkFilterColumnName(String filterColumnName, DateTime filterValue, List<MetamacExceptionItem> exceptionItems) {
        if ((filterValue != null) && StringUtils.isBlank(filterColumnName)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.FILTER_COLUMN_NAME));
        }
    }

    private String buildQuery(String tableName, List<String> columnsName, String filterColumnName, DateTime filterValue) {
        StringBuilder statement = new StringBuilder("SELECT ").append(getColumnsNameAsString(columnsName)).append(" FROM ").append(tableName);

        if (filterValue != null) {
            statement.append(" WHERE ").append(filterColumnName).append(" >= ").append("TO_TIMESTAMP('").append(getFilterValueAsString(filterValue)).append("', 'DD-MM-YYYY HH24:MI:SS')");
        }

        return statement.toString();
    }

    private String getColumnsNameAsString(List<String> columnsName) {
        return StringUtils.join(columnsName, ", ");
    }

    private String getFilterValueAsString(DateTime filterValue) {
        return filterValue.toString("dd-MM-yyyy HH:mm:ss");
    }

    private String getNonCaseSensitiveIdentifier(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        String checkedTableName = tableName;

        if (!databaseMetaData.storesMixedCaseIdentifiers()) {
            if (databaseMetaData.storesUpperCaseIdentifiers()) {
                checkedTableName = tableName.toUpperCase();
            } else if (databaseMetaData.storesLowerCaseIdentifiers()) {
                checkedTableName = tableName.toLowerCase();
            }
        }
        return checkedTableName;
    }

    private class ObservationMapper implements RowMapper<String[]> {

        private List<String> columnsName;

        public ObservationMapper(List<String> columnsName) {
            this.columnsName = columnsName;
        }

        @Override
        public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<String> observation = new ArrayList<>();
            for (String s : columnsName) {
                observation.add(rs.getString(s));
            }
            return observation.toArray(new String[0]);
        }
    }
}
