package org.siemac.metamac.statistical.resources.core.geocache.repositoryimpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for GeoCacheByRelatedResource
 */
@Repository("geoCacheByRelatedResourceRepository")
public class GeoCacheByRelatedResourceRepositoryImpl extends GeoCacheByRelatedResourceRepositoryBase {

    private static Logger logger = LoggerFactory.getLogger(GeoCacheByRelatedResourceRepositoryImpl.class);

    public GeoCacheByRelatedResourceRepositoryImpl() {
    }

    @Override
    public void deleteAll() {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("deleteAll not implemented");

    }

    @Override
    public void disabledByResourceVersionUrn(String resourceVersionUrn) {
        Session session = (Session) getEntityManager().getDelegate();
        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    executeSqlStatement(connection, "UPDATE tb_geo_cache_related_resource g set is_activated=false WHERE  g.urn='" + resourceVersionUrn + "'");
                }
            });
        } catch (Exception e) {
            logger.error("Error disabling entries by urn from geographic related cache -> urn {} ", resourceVersionUrn, e);
        }

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
}
