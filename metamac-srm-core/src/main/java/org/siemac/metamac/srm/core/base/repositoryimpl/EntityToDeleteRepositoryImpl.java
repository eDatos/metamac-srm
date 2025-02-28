package org.siemac.metamac.srm.core.base.repositoryimpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("entityToDeleteRepository")
public class EntityToDeleteRepositoryImpl extends EntityToDeleteRepositoryBase {

    private static Logger logger = LoggerFactory.getLogger(EntityToDeleteRepositoryImpl.class);

    public EntityToDeleteRepositoryImpl() {
    }

    @Override
    public void deleteEntitiesMarkedToDelete() throws MetamacException {

        Session session = (Session) getEntityManager().getDelegate();

        try {
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {

                    logger.info("deleteEntitiesMarkedToDelete starting at {}", new Date());

                    // Delete rows
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append("DELETE FROM ");
                        sb.append("TB_INTERNATIONAL_STRINGS  ");
                        sb.append("WHERE ID IN ");
                        sb.append(" (SELECT ID_TO_DELETE ");
                        sb.append("  FROM TB_ENTITIES_TO_DELETE ");
                        sb.append("  WHERE TABLE_NAME = '" + SrmConstants.TABLE_INTERNATIONAL_STRINGS + "')");
                        executeSqlStatement(connection, sb);
                    }
                    // Unmark rows to delete, clearing rows
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append("DELETE FROM ");
                        sb.append("TB_ENTITIES_TO_DELETE  ");
                        sb.append("WHERE TABLE_NAME = '" + SrmConstants.TABLE_INTERNATIONAL_STRINGS + "'");
                        executeSqlStatement(connection, sb);
                    }

                    logger.info("deleteEntitiesMarkedToDelete finished at {}", new Date());
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting rows marked to delete", e);
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Error deleting rows marked to delete: " + e.getMessage());
        }
    }

    private void executeSqlStatement(Connection connection, StringBuilder sb) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sb.toString());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
