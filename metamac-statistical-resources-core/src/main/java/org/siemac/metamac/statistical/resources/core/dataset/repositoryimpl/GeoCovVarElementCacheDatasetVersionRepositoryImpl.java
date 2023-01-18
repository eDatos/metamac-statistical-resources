package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;
import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
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

      //@formatter:off
        Query query = getEntityManager().createNativeQuery("UPDATE tb_geocov_varelem_cache_datasets_versions g set is_activated=false WHERE  g.urn=:urn");
        //@formatter:on
        query.setParameter("urn", datasetVersionUrn).executeUpdate();
    }
    
    public List<Object> findNoActivatedElements() {

        //@formatter:off
          Query query = getEntityManager().createNativeQuery(
                  "SELECT a.id, a.variable_element_fk, a.title_fk, a.operation_title_fk, b.title_fk as variable_element_title_fk "
                + "FROM tb_geocov_varelem_cache_datasets_versions a "
                + "INNER JOIN tb_external_items b ON a.variable_element_fk = b.id "
                + "WHERE  a.IS_ACTIVATED = false");
          //@formatter:on
          return query.getResultList();
      }

    private String getStringFromBigInteger(BigInteger id) {
        return id.toString();
    }

    public void deleteAll() {

        Set<String> geoCovVarElementCacheDatasetVersionId = new HashSet<>();
        Set<String> variableElementId = new HashSet<>();
        Set<String> internationalStrings = new HashSet<>();
        
        List<Object> disabledElements = findNoActivatedElements();

        for (Object row : disabledElements) {

            Object[] cols = (Object[]) row;

            geoCovVarElementCacheDatasetVersionId.add(getStringFromBigInteger((BigInteger) cols[0]));

            variableElementId.add(getStringFromBigInteger((BigInteger) cols[1]));

            internationalStrings.add(getStringFromBigInteger((BigInteger) cols[2]));
            internationalStrings.add(getStringFromBigInteger((BigInteger) cols[3]));
            internationalStrings.add(getStringFromBigInteger((BigInteger) cols[4]));
        }

        executeSqlSentenceWithIn("DELETE FROM TB_GEOCOV_VARELEM_CACHE_DATASETS_VERSIONS WHERE id IN ", geoCovVarElementCacheDatasetVersionId, "TB_GEOCOV_VARELEM_CACHE_DATASETS_VERSIONS");
        
        executeSqlSentenceWithIn("DELETE FROM TB_EXTERNAL_ITEMS WHERE id IN ", variableElementId, "TB_EXTERNAL_ITEMS");
     
        executeSqlSentenceWithIn("DELETE FROM TB_LOCALISED_STRINGS WHERE international_string_fk IN ", internationalStrings, "TB_LOCALISED_STRINGS");
             
        executeSqlSentenceWithIn("DELETE FROM TB_INTERNATIONAL_STRINGS WHERE id IN ", internationalStrings, "TB_INTERNATIONAL_STRINGS");
 
    }
    

    private void executeSqlSentenceWithIn(String sqlSentence, Set<String> parametersIn, String tableAudit) {
        logger.info("Execution start - delete  <" + tableAudit + "> all disabled entries from geographic coverage cache at : {} ", new DateTime());
        
        Set<String> partialParametersIn = new HashSet<>();
        
        for (String parameterIn : parametersIn) {
            partialParametersIn.add(parameterIn);
            if (partialParametersIn.size() > MAX_SIZE_IN_CLAUSE) {
                Query query = getEntityManager().createNativeQuery(sqlSentence + "(" +  String.join(", ", partialParametersIn) + ")");
                query.executeUpdate();
                partialParametersIn.clear();
            }
        }
        
        if (!partialParametersIn.isEmpty()) {
            Query query = getEntityManager().createNativeQuery(sqlSentence + String.join(", ", partialParametersIn) + ")");
            query.executeUpdate();
        }
        
        logger.info("Execution end - delete  <" + tableAudit + "> all disabled entries from geographic coverage cache at : {} ", new DateTime());
        
    }
}
