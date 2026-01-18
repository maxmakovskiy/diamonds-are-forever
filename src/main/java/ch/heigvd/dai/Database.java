package ch.heigvd.dai;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

/**
 * Database wrapper that uses Singleton pattern <a
 * href="https://refactoring.guru/design-patterns/singleton">details</a>
 */
public class Database {
    private static Database instance;

    public final Jdbi jdbi;

    private Database() {
        jdbi =
                Jdbi.create("jdbc:postgresql://postgresql:5432/diamonds", "diamonds", "diamonds")
                        .installPlugin(new PostgresPlugin())
                        .installPlugin(new SqlObjectPlugin());
    }

    public static Database getInstance() {
        if (Database.instance != null) {
            return instance;
        }

        synchronized (Database.class) {
            if (Database.instance == null) {
                Database.instance = new Database();
            }
            return instance;
        }
    }
}
