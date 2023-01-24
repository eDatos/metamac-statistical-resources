package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;
import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

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
                Query query = getEntityManager().createNativeQuery(fillSqlSentenceWithIn(sqlSentence, partialParametersIn));
                query.executeUpdate();
                partialParametersIn.clear();
            }
        }
        
        if (!partialParametersIn.isEmpty()) {
            Query query = getEntityManager().createNativeQuery(fillSqlSentenceWithIn(sqlSentence, partialParametersIn));
            query.executeUpdate();
        }
        
        logger.info("Execution end - delete  <" + tableAudit + "> all disabled entries from geographic coverage cache at : {} ", new DateTime());
        
    }
    
    private String fillSqlSentenceWithIn(String sqlSentence, Set<String> partialParametersIn) {
        return sqlSentence + "(" +  String.join(", ", partialParametersIn) + ")";
    }
}
