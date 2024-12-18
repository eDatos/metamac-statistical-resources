package org.siemac.metamac.statistical.resources.core.geocache.repositoryimpl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for GeoCacheResource
 */
@Repository("geoCacheResourceRepository")
public class GeoCacheResourceRepositoryImpl extends GeoCacheResourceRepositoryBase {

    private static Logger   logger             = LoggerFactory.getLogger(GeoCacheResourceRepositoryImpl.class);
    public static final int MAX_SIZE_IN_CLAUSE = 5000;

    public GeoCacheResourceRepositoryImpl() {
        // without impl
    }

    @Override
    public void deleteAll() {

        Set<String> geoCacheResourceIds = new HashSet<>();
        Set<String> variableElementId = new HashSet<>();
        Set<String> internationalStrings = new HashSet<>();

        List<Object> disabledElements = findNoActivatedElements();

        for (Object row : disabledElements) {

            Object[] cols = (Object[]) row;

            geoCacheResourceIds.add(getStringFromBigInteger((BigInteger) cols[0]));

            if (cols[1] != null) {
                variableElementId.add(getStringFromBigInteger((BigInteger) cols[1]));
            }

            if (cols[2] != null) {
                internationalStrings.add(getStringFromBigInteger((BigInteger) cols[2]));
            }

            if (cols[3] != null) {
                internationalStrings.add(getStringFromBigInteger((BigInteger) cols[3]));
            }
        }

        Session session = (Session) getEntityManager().getDelegate();
        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_GEO_CACHE_RESOURCE_BY_RELATED_RESOURCE WHERE geo_cache_resource_fk IN ", geoCacheResourceIds,
                            "TB_GEO_CACHE_RESOURCE_BY_RELATED_RESOURCE");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_TERRITORIES_BY_GEO_CACHE_RESOURCE WHERE geo_cache_resource_fk IN ", geoCacheResourceIds,
                            "TB_TERRITORIES_BY_GEO_CACHE_RESOURCE");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_GEO_CACHE_RESOURCE WHERE id IN ", geoCacheResourceIds, "TB_GEO_CACHE_RESOURCE");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_EXTERNAL_ITEMS WHERE id IN ", variableElementId, "TB_EXTERNAL_ITEMS");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_LOCALISED_STRINGS WHERE international_string_fk IN ", internationalStrings, "TB_LOCALISED_STRINGS");

                    executeSqlSentenceWithIn(connection, "DELETE FROM TB_INTERNATIONAL_STRINGS WHERE id IN ", internationalStrings, "TB_INTERNATIONAL_STRINGS");

                }
            });
        } catch (Exception e) {
            logger.error("Error deleting all disabled entries from geographic coverage cache", e);
        }

    }

    @Override
    public void disabledByResourceVersionUrn(String resourceVersionUrn) {
        Session session = (Session) getEntityManager().getDelegate();
        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    executeSqlStatement(connection, "UPDATE tb_geo_cache_resource g set is_activated=false WHERE  g.urn='" + resourceVersionUrn + "'");
                }
            });
        } catch (Exception e) {
            logger.error("Error disabling entries by urn from geographic coverage cache -> urn {} ", resourceVersionUrn, e);
        }

    }

    @Override
    public GeoCacheResource retrieveByResourceVersionUrn(String resourceVersionUrn) throws MetamacException {
        return retrieveByResourceVersionUrn(resourceVersionUrn, true);
    }

    @Override
    public GeoCacheResource retrieveByResourceVersionUrn(String resourceVersionUrn, boolean filterOnlyActivated) throws MetamacException {
        List<ConditionalCriteria> conditions = null;
        if (filterOnlyActivated) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.urn()).eq(resourceVersionUrn).and()
                    .withProperty(GeoCacheResourceProperties.isActivated()).eq(Boolean.TRUE).orderBy(GeoCacheResourceProperties.code()).ascending().build();
        } else {
            conditions = ConditionalCriteriaBuilder.criteriaFor(GeoCacheResource.class).withProperty(GeoCacheResourceProperties.urn()).eq(resourceVersionUrn).build();
        }

        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        PagedResult<GeoCacheResource> result = findByCondition(conditions, paging);

        if (result.getRowCount() == 0) {
            throw new MetamacException(ServiceExceptionType.GEO_CACHE_RESOURCE_NOT_FOUND, resourceVersionUrn);
        }
        return result.getValues().get(0);
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

    private String fillSqlSentenceWithIn(String sqlSentence, Set<String> partialParametersIn) {
        return sqlSentence + "(" + String.join(", ", partialParametersIn) + ")";
    }

    // multidatasets do not have territories so the join must be "left"
    private List<Object> findNoActivatedElements() {

        //@formatter:off
          Query query = getEntityManager().createNativeQuery(
                  "SELECT a.id, t.variable_element_fk, a.title_fk, b.title_fk as variable_element_title_fk "
                + "FROM tb_geo_cache_resource a "
                + "LEFT JOIN tb_territories_by_geo_cache_resource t ON t.geo_cache_resource_fk = a.id "
                + "LEFT JOIN tb_external_items b ON t.variable_element_fk = b.id "
                + "WHERE  a.IS_ACTIVATED = false");
          //@formatter:on
        return query.getResultList();
    }

    private String getStringFromBigInteger(BigInteger id) {
        return id.toString();
    }
}
