package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;
import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.joda.time.DateTime;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for GeoCovVarElementCacheDatasetVersion
 */
@Repository("geoCovVarElementCacheDatasetVersionRepository")
public class GeoCovVarElementCacheDatasetVersionRepositoryImpl
    extends GeoCovVarElementCacheDatasetVersionRepositoryBase {
    
    private static Logger                     logger                              = LoggerFactory.getLogger(GeoCovVarElementCacheDatasetVersionRepositoryImpl.class);
    
    public static final int MAX_SIZE_IN_CLAUSE = 5000;

    public GeoCovVarElementCacheDatasetVersionRepositoryImpl() {
    }

    public List<GeoCovVarElementCacheDatasetVersion> retrieveByDatasetVersionUrn(String datasetVersionUrn) {

        List<ConditionalCriteria> condition = criteriaFor(GeoCovVarElementCacheDatasetVersion.class).withProperty(GeoCovVarElementCacheDatasetVersionProperties.urn()).eq(datasetVersionUrn)
                .distinctRoot().build();
        condition.add(
                ConditionalCriteriaBuilder.criteriaFor(GeoCovVarElementCacheDatasetVersion.class).withProperty(GeoCovVarElementCacheDatasetVersionProperties.isActivated()).eq(true).buildSingle());

        return findByCondition(condition);

    }
  
    public void disabledByDatasetVersionUrn(String datasetVersionUrn) {
        Session session = (Session) getEntityManager().getDelegate();
        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    executeSqlStatement(connection, "UPDATE tb_geocov_varelem_cache_datasets_versions g set is_activated=false WHERE  g.urn='" + datasetVersionUrn + "'");
                }
            });
        } catch (Exception e) {
            logger.error("Error disabling entries by urn from geographic coverage cache -> urn {} ", datasetVersionUrn,  e);
        }
    }
    
    public List<GeoCovVarElementCacheDatasetVersion> findNoActivatedElements() {

        List<ConditionalCriteria> condition = criteriaFor(GeoCovVarElementCacheDatasetVersion.class).withProperty(GeoCovVarElementCacheDatasetVersionProperties.isActivated()).eq(false)
                .distinctRoot().build();

        return findByCondition(condition);
      }

    public void deleteAll() {

        Set<String> geoCovVarElementCacheDatasetVersionId = new HashSet<>();
        Set<String> variableElementId = new HashSet<>();
        Set<String> internationalStrings = new HashSet<>();
        
        List<GeoCovVarElementCacheDatasetVersion> disabledElements = findNoActivatedElements();

        for (GeoCovVarElementCacheDatasetVersion row : disabledElements) {
         
            geoCovVarElementCacheDatasetVersionId.add(String.valueOf(row.getId()));

            variableElementId.add(String.valueOf(row.getVariableElement().getId()));

            internationalStrings.add(String.valueOf(row.getVariableElement().getTitle().getId()));
            internationalStrings.add(String.valueOf(row.getTitle().getId()));
            internationalStrings.add(String.valueOf(row.getOperationTitle().getId()));
        }

        Session session = (Session) getEntityManager().getDelegate();
        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_GEOCOV_VARELEM_CACHE_DATASETS_VERSIONS WHERE id IN ", geoCovVarElementCacheDatasetVersionId, "TB_GEOCOV_VARELEM_CACHE_DATASETS_VERSIONS");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_EXTERNAL_ITEMS WHERE id IN ", variableElementId, "TB_EXTERNAL_ITEMS");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_LOCALISED_STRINGS WHERE international_string_fk IN ", internationalStrings, "TB_LOCALISED_STRINGS");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_INTERNATIONAL_STRINGS WHERE id IN ", internationalStrings, "TB_INTERNATIONAL_STRINGS");
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting all disabled entries from geographic coverage cache", e);
        }
    }
    

    private void executeSqlSentenceWithIn(Connection connection, String sqlSentence, Set<String> parametersIn, String tableAudit) throws SQLException {
        logger.info("Execution start - delete  <" + tableAudit + "> all disabled entries from geographic coverage cache at : {} ", new DateTime());
        
        Set<String> partialParametersIn = new HashSet<>();
        
        for (String parameterIn : parametersIn) {
            partialParametersIn.add(parameterIn);
            if (partialParametersIn.size() > MAX_SIZE_IN_CLAUSE) {
                executeSqlStatement(connection, fillSqlSentenceWithIn(sqlSentence, partialParametersIn));
                partialParametersIn.clear();
            }
        }
        
        if (!partialParametersIn.isEmpty()) {
            executeSqlStatement(connection, fillSqlSentenceWithIn(sqlSentence, partialParametersIn));
        }
        
        logger.info("Execution end - delete  <" + tableAudit + "> all disabled entries from geographic coverage cache at : {} ", new DateTime());
        
    }
    
    private void executeSqlStatement(Connection connection, String sb) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sb);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    
    private String fillSqlSentenceWithIn(String sqlSentence, Set<String> partialParametersIn) {
        return sqlSentence + "(" +  String.join(", ", partialParametersIn) + ")";
    }
}
