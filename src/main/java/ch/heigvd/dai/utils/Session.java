package ch.heigvd.dai.utils;

import javax.sql.DataSource;
import org.eclipse.jetty.server.session.*;

/* Taken from:
 * <a href="https://javalin.io/tutorials/jetty-session-handling">link</a>
 */
public class Session {
  public static SessionHandler sqlSessionHandler(DataSource ds) {
    SessionHandler sessionHandler = new SessionHandler();
    SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
    sessionCache.setSessionDataStore(jdbcDataStoreFactory(ds).getSessionDataStore(sessionHandler));
    sessionHandler.setSessionCache(sessionCache);
    sessionHandler.setHttpOnly(true);
    // NOTE:
    // make additional changes to your SessionHandler here
    return sessionHandler;
  }

  private static JDBCSessionDataStoreFactory jdbcDataStoreFactory(DataSource ds) {
    DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();
    databaseAdaptor.setDatasource(ds);
    JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
    jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);
    return jdbcSessionDataStoreFactory;
  }
}
