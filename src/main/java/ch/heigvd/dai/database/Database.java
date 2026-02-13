package ch.heigvd.dai.database;

import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

/**
 * Initialization-on-demand holder idiom of Signleton implementation Key Advantages &
 * Characteristics: - Lazy Initialization: The instance is not created until getInstance() is
 * called, saving resources if it's never used. - Thread Safety: The Java Language Specification
 * (JLS) guarantees that class loading is thread-safe. Because initialization occurs during class
 * loading, no explicit synchronized keywords are needed. - High Performance: Because
 * synchronization is only used once by the class loader, it avoids the overhead of double-checked
 * locking on every access. - Works in All Versions: It is compliant with all versions of Java. -
 * Best Practice: Considered the safest and most efficient way to implement the Singleton pattern in
 * Java
 */
public class Database {
  private static final String url;
  private static final HikariDataSource dataSource;
  public final Jdbi jdbi;

  static {
    url =
        "jdbc:postgresql://"
            + System.getenv("POSTGRES_HOST")
            + ":"
            + System.getenv("POSTGRES_PORT")
            + "/"
            + System.getenv("POSTGRES_DB");

    dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(System.getenv("POSTGRES_USER"));
    dataSource.setPassword(System.getenv("POSTGRES_PASSWORD"));
  }

  private Database() {
    jdbi =
        Jdbi.create(dataSource)
            .installPlugin(new PostgresPlugin())
            .installPlugin(new SqlObjectPlugin());
  }

  public static HikariDataSource getDataSource() {
    return dataSource;
  }

  public static Database getInstance() {
    return Holder.INSTANCE;
  }

  private static class Holder {
    public static final Database INSTANCE = new Database();
  }
}
