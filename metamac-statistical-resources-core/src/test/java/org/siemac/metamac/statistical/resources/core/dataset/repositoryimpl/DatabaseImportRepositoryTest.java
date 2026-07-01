package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dataset.repository.api.DatabaseImportRepository;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class DatabaseImportRepositoryTest extends StatisticalResourcesBaseTest {

    private static final String      TABLE_NAME_FOR_TESTING         = "TESTING_TABLE";
    private static final String      TABLE_NAME_WITH_DATA           = "TESTING_TABLE_2";
    private static final String      COLUMN_NAME_FOR_TESTING        = "TESTING_COLUMN_NAME";
    private static final String      FILTER_COLUMN_NAME_FOR_TESTING = "FILTER_COLUMN_NAME";
    public static final List<String> COLUMN_LIST                    = Collections.unmodifiableList(Arrays.asList(COLUMN_NAME_FOR_TESTING));

    // @formatter:off
    public static final List<String> INSERTS_LIST                   = Collections.unmodifiableList(Arrays.asList(
            "INSERT INTO " + TABLE_NAME_WITH_DATA + " (" + COLUMN_NAME_FOR_TESTING + ", " + FILTER_COLUMN_NAME_FOR_TESTING + ") VALUES ('" + MetamacMocks.mockString(20) + "', TO_TIMESTAMP('2019-01-26 08:30:16.664', 'YYYY-MM-DD HH24:MI:SS.FF'))", 
            "INSERT INTO " + TABLE_NAME_WITH_DATA + " (" + COLUMN_NAME_FOR_TESTING + ", " + FILTER_COLUMN_NAME_FOR_TESTING + ") VALUES ('" + MetamacMocks.mockString(20) + "', TO_TIMESTAMP('2019-01-27 08:30:16.664', 'YYYY-MM-DD HH24:MI:SS.FF'))", 
            "INSERT INTO " + TABLE_NAME_WITH_DATA + " (" + COLUMN_NAME_FOR_TESTING + ", " + FILTER_COLUMN_NAME_FOR_TESTING + ") VALUES ('" + MetamacMocks.mockString(20) + "', TO_TIMESTAMP('2019-01-28 08:30:16.664', 'YYYY-MM-DD HH24:MI:SS.FF'))", 
            "INSERT INTO " + TABLE_NAME_WITH_DATA + " (" + COLUMN_NAME_FOR_TESTING + ", " + FILTER_COLUMN_NAME_FOR_TESTING + ") VALUES ('" + MetamacMocks.mockString(20) + "', TO_TIMESTAMP('2019-01-29 12:00:00.999', 'YYYY-MM-DD HH24:MI:SS.FF'))"));
    // @formatter:on

    @Autowired
    DatabaseImportRepository         databaseImportRepository;

    private JdbcTemplate             jdbcTemplate;

    @Autowired
    @Qualifier("dataSourceDatabaseImportRepository")
    public void setDataSource(DataSource dataSource) throws SQLException {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void runBeforeTestMethod() {
        createTable(TABLE_NAME_FOR_TESTING);
        createTable(TABLE_NAME_WITH_DATA);
        populateTable();
    }

    @After
    public void runAfterTestMethod() {
        dropTable(TABLE_NAME_FOR_TESTING);
        dropTable(TABLE_NAME_WITH_DATA);
    }

    @Test
    public void testCheckTableExists() throws MetamacException {
        assertTableExists(TABLE_NAME_FOR_TESTING);
        assertTableExists(TABLE_NAME_FOR_TESTING.toLowerCase());
        assertTableExists(TABLE_NAME_FOR_TESTING.toUpperCase());
        assertTableExists(vowelsToUppercase(TABLE_NAME_FOR_TESTING));

        assertTableNotExists(EMPTY);
        assertTableNotExists(null);

        assertTableNotExists(toLeetSpeak(TABLE_NAME_FOR_TESTING));
        assertTableNotExists(halfWord(TABLE_NAME_FOR_TESTING));
        assertTableNotExists(addWhiteSpaces(TABLE_NAME_FOR_TESTING));
    }

    @Test
    public void testCheckTableHasColumns() throws MetamacException {
        assertTableHasColumn(TABLE_NAME_FOR_TESTING, COLUMN_NAME_FOR_TESTING);
        assertTableHasColumn(TABLE_NAME_FOR_TESTING, COLUMN_NAME_FOR_TESTING.toLowerCase());
        assertTableHasColumn(TABLE_NAME_FOR_TESTING, COLUMN_NAME_FOR_TESTING.toUpperCase());
        assertTableHasColumn(TABLE_NAME_FOR_TESTING, vowelsToUppercase(COLUMN_NAME_FOR_TESTING));

        assertTableHasColumn(TABLE_NAME_FOR_TESTING.toLowerCase(), COLUMN_NAME_FOR_TESTING);
        assertTableHasColumn(TABLE_NAME_FOR_TESTING.toUpperCase(), COLUMN_NAME_FOR_TESTING);
        assertTableHasColumn(vowelsToUppercase(TABLE_NAME_FOR_TESTING), COLUMN_NAME_FOR_TESTING);

        assertTableHasColumn(TABLE_NAME_FOR_TESTING.toLowerCase(), COLUMN_NAME_FOR_TESTING.toLowerCase());
        assertTableHasColumn(TABLE_NAME_FOR_TESTING.toUpperCase(), COLUMN_NAME_FOR_TESTING.toUpperCase());
        assertTableHasColumn(vowelsToUppercase(TABLE_NAME_FOR_TESTING), vowelsToUppercase(COLUMN_NAME_FOR_TESTING));

        assertTableHasNotColumn(TABLE_NAME_FOR_TESTING, null);
        assertTableHasNotColumn(null, COLUMN_NAME_FOR_TESTING);
        assertTableHasNotColumn(null, null);

        assertTableHasNotColumn(TABLE_NAME_FOR_TESTING, EMPTY);
        assertTableHasNotColumn(EMPTY, COLUMN_NAME_FOR_TESTING);
        assertTableHasNotColumn(EMPTY, EMPTY);

        assertTableHasNotColumn(TABLE_NAME_FOR_TESTING, toLeetSpeak(COLUMN_NAME_FOR_TESTING));
        assertTableHasNotColumn(toLeetSpeak(TABLE_NAME_FOR_TESTING), COLUMN_NAME_FOR_TESTING);
        assertTableHasNotColumn(toLeetSpeak(TABLE_NAME_FOR_TESTING), toLeetSpeak(COLUMN_NAME_FOR_TESTING));

        assertTableHasNotColumn(TABLE_NAME_FOR_TESTING, halfWord(COLUMN_NAME_FOR_TESTING));
        assertTableHasNotColumn(halfWord(TABLE_NAME_FOR_TESTING), COLUMN_NAME_FOR_TESTING);
        assertTableHasNotColumn(halfWord(TABLE_NAME_FOR_TESTING), halfWord(COLUMN_NAME_FOR_TESTING));

        assertTableHasNotColumn(TABLE_NAME_FOR_TESTING, addWhiteSpaces(COLUMN_NAME_FOR_TESTING));
        assertTableHasNotColumn(addWhiteSpaces(TABLE_NAME_FOR_TESTING), COLUMN_NAME_FOR_TESTING);
        assertTableHasNotColumn(addWhiteSpaces(TABLE_NAME_FOR_TESTING), addWhiteSpaces(COLUMN_NAME_FOR_TESTING));
    }

    @Test
    public void testGetObservationsTableHasNoObservations() throws MetamacException {
        assertTrue(getObservations(TABLE_NAME_FOR_TESTING, null).isEmpty());
    }

    @Test
    public void testGetObservationsTableHasObservationsAndFilterValueIsNotDefined() throws MetamacException {
        List<String[]> observations = getObservations(TABLE_NAME_WITH_DATA, null);
        assertFalse(observations.isEmpty());
        assertEquals(INSERTS_LIST.size(), observations.size());
    }

    @Test
    public void testGetObservationsFilterValueIsGreaterThanObservationsValue() throws MetamacException {
        assertTrue(getObservations(TABLE_NAME_WITH_DATA, new DateTime().plusDays(1)).isEmpty());
    }

    @Test
    public void testGetObservationsValueIsEqualFilterValue() throws MetamacException {
        List<String[]> observations = getObservations(TABLE_NAME_WITH_DATA, new DateTime(2019, 01, 29, 12, 0, 0, 999));
        assertFalse(observations.isEmpty());
        assertEquals(1, observations.size());
    }

    @Test
    public void testGetObservationsMultipleValuesAreReturned() throws MetamacException {
        List<String[]> observations = getObservations(TABLE_NAME_WITH_DATA, new DateTime(2019, 01, 27, 0, 0, 0, 0));
        assertFalse(observations.isEmpty());
        assertEquals(3, observations.size());
    }

    @Test
    public void testGetObservationsValuesTruncToSecond() throws MetamacException {
        assertTrue(getObservations(TABLE_NAME_WITH_DATA, new DateTime(2019, 01, 29, 12, 0, 1, 0)).isEmpty());
    }

    @Test
    public void testGetObservationsNullTableName() throws MetamacException {
        expectedMetamacException(
                MetamacExceptionBuilder.builder().withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.TABLE_NAME))).build());
        databaseImportRepository.getObservations(null, COLUMN_LIST, FILTER_COLUMN_NAME_FOR_TESTING, new DateTime());
    }

    @Test
    public void testGetObservationsBlankTableName() throws MetamacException {
        expectedMetamacException(
                MetamacExceptionBuilder.builder().withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.TABLE_NAME))).build());
        databaseImportRepository.getObservations("", COLUMN_LIST, FILTER_COLUMN_NAME_FOR_TESTING, new DateTime());
    }

    @Test
    public void testGetObservationsNullColumns() throws MetamacException {
        expectedMetamacException(MetamacExceptionBuilder.builder()
                .withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.COLUMNS_NAME))).build());
        databaseImportRepository.getObservations(TABLE_NAME_WITH_DATA, null, FILTER_COLUMN_NAME_FOR_TESTING, null);
    }

    @Test
    public void testGetObservationsEmptyColumns() throws MetamacException {
        expectedMetamacException(MetamacExceptionBuilder.builder()
                .withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.COLUMNS_NAME))).build());
        databaseImportRepository.getObservations(TABLE_NAME_WITH_DATA, Collections.emptyList(), FILTER_COLUMN_NAME_FOR_TESTING, null);
    }

    @Test
    public void testGetObservationsBlankColumns() throws MetamacException {
        expectedMetamacException(MetamacExceptionBuilder.builder()
                .withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.COLUMNS_NAME))).build());
        databaseImportRepository.getObservations(TABLE_NAME_WITH_DATA, Arrays.asList(""), FILTER_COLUMN_NAME_FOR_TESTING, null);
    }

    @Test
    public void testGetObservationsNullFilterColumn() throws MetamacException {
        expectedMetamacException(MetamacExceptionBuilder.builder()
                .withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.FILTER_COLUMN_NAME))).build());
        databaseImportRepository.getObservations(TABLE_NAME_WITH_DATA, COLUMN_LIST, null, new DateTime());
    }

    @Test
    public void testGetObservationsBlankFilterColumn() throws MetamacException {
        expectedMetamacException(MetamacExceptionBuilder.builder()
                .withExceptionItems(Arrays.asList(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.FILTER_COLUMN_NAME))).build());
        databaseImportRepository.getObservations(TABLE_NAME_WITH_DATA, COLUMN_LIST, "", new DateTime());
    }

    private void assertTableExists(String tableName) throws MetamacException {
        assertTrue(databaseImportRepository.checkTableExists(tableName));
    }

    private void assertTableNotExists(String tableName) throws MetamacException {
        assertFalse(databaseImportRepository.checkTableExists(tableName));
    }

    private void assertTableHasColumn(String tableName, String columnName) throws MetamacException {
        assertTrue(databaseImportRepository.checkTableHasColumn(tableName, columnName));
    }

    private void assertTableHasNotColumn(String tableName, String columnName) throws MetamacException {
        assertFalse(databaseImportRepository.checkTableHasColumn(tableName, columnName));
    }

    private List<String[]> getObservations(String tableName, DateTime filterValue) throws MetamacException {
        return databaseImportRepository.getObservations(tableName, COLUMN_LIST, FILTER_COLUMN_NAME_FOR_TESTING, filterValue);
    }

    private void createTable(String tableName) {
        dropTable(tableName);
        jdbcTemplate.execute("CREATE TABLE " + tableName + " (" + COLUMN_NAME_FOR_TESTING + " VARCHAR (50), " + FILTER_COLUMN_NAME_FOR_TESTING + " TIMESTAMP)");
    }

    // Drop the table and don't throw error if it doesn't exists
    private void dropTable(String tableName) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName + ";");
    }

    private void populateTable() {
        for (String insert : INSERTS_LIST) {
            jdbcTemplate.execute(insert);
        }
    }

    // Convert to leet speak. Example: input: "Hello world" output: "#3||0 W02|)"
    private String toLeetSpeak(String word) {
        char original[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char leet[] = {'4', '8', '(', ')', '3', '}', '6', '#', '!', ']', 'X', '|', 'M', 'N', '0', '9', 'Q', '2', 'Z', '7', 'M', 'V', 'W', 'X', 'J', 'Z'};
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            char tmp = word.charAt(i);
            boolean hasLeet = Boolean.FALSE;
            for (int j = 0; j < original.length && !hasLeet; j++) {
                if (tmp == original[j]) {
                    sb.append(leet[j]);
                    hasLeet = Boolean.TRUE;
                }
            }
            if (!hasLeet) {
                sb.append(tmp);
            }
        }
        return sb.toString();
    }

    // Example: input: "Hello world" output: "hEllO wOrld"
    private String vowelsToUppercase(String word) {
        StringBuilder sb = new StringBuilder();
        for (char ch : word.toCharArray()) {
            char t = Character.toUpperCase(ch);
            if (t == 'A' || t == 'E' || t == 'I' || t == 'O' || t == 'U') {
                sb.append(t);
            } else {
                sb.append(Character.toLowerCase(ch));
            }
        }
        return sb.toString();
    }

    // Example: input: "Hello world" output: "Hello"
    private String halfWord(String word) {
        return word.substring(0, (word.length() / 2));
    }

    // Example: input: "Hello world" output: " Hello world "
    private String addWhiteSpaces(String word) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 2; j++) {
            sb.append(" ");
        }
        sb.append(word);
        for (int j = 0; j < 2; j++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
