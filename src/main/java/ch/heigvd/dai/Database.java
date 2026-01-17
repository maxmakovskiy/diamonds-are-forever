package ch.heigvd.dai;

import org.jdbi.v3.core.Jdbi;

/**
 * Database wrapper
 * that uses Singleton pattern
 * <a href="https://refactoring.guru/design-patterns/singleton">details</a>
 * */
public class Database {
    private static Database instance;

    final public Jdbi jdbi;

    private Database() {
        jdbi = Jdbi.create("jdbc:sqlite:mem:");
    }

    public static Database getInstance() {
        if (Database.instance != null) {
            return instance;
        }

        synchronized(Database.class) {
            if (Database.instance == null) {
                Database.instance = new Database();
            }
            return instance;
        }
    }

    private void initDb() {
        jdbi.useHandle(handle -> {
            handle.execute("PRAGMA foreign_keys = ON");

            handle.execute("DROP TABLE IF EXISTS currency");
            handle.execute("""
                CREATE TABLE currency (
                    code TEXT PRIMARY KEY,
                    name TEXT NOT NULL UNIQUE
                )"""
            );

            handle.execute("DROP TABLE IF EXISTS counterpart");
            handle.execute("""
                CREATE TABLE counterpart (
                    counterpart_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    phone_number TEXT,
                    address_short TEXT,
                    city TEXT,
                    postal_code TEXT,
                    country TEXT,
                    contact_email TEXT UNIQUE,
                    category TEXT NOT NULL,
                    is_active INTEGER NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)),
                    created_at TEXT NOT NULL DEFAULT (datetime('now')),
                    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
                    CHECK (updated_at >= created_at)
                )"""
            );


            handle.execute("DROP TABLE IF EXISTS user");
            handle.execute("""
                CREATE TABLE user (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    counterpart_id INTEGER NOT NULL,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL,
                    is_active INTEGER NOT NULL DEFAULT 1 CHECK (is_active IN (0,1)),
                    created_at TEXT NOT NULL DEFAULT (datetime('now')),
                    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
                    FOREIGN KEY (counterpart_id)
                        REFERENCES counterpart(counterpart_id)
                        ON DELETE CASCADE ON UPDATE CASCADE,
                    CHECK (updated_at >= created_at)
                )"""
            );

            handle.execute("DROP TABLE IF EXISTS item");
            handle.execute("""
                CREATE TABLE item (
                    lot_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    stock_name TEXT NOT NULL,
                    purchase_date TEXT NOT NULL DEFAULT (datetime('now')),
                    supplier_id INTEGER NOT NULL,
                    origin TEXT NOT NULL,
                    responsible_office_id INTEGER NOT NULL,
                    item_type TEXT NOT NULL,
                    created_at TEXT NOT NULL DEFAULT (datetime('now')),
                    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
                    is_available INTEGER NOT NULL DEFAULT 1 CHECK (is_available IN (0,1)),
                    FOREIGN KEY (supplier_id)
                        REFERENCES counterpart(counterpart_id)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (responsible_office_id)
                        REFERENCES counterpart(counterpart_id)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    CHECK (updated_at >= created_at)
                )"""
            );

            handle.execute("DROP TABLE IF EXISTS action");
            handle.execute("""
                CREATE TABLE action (
                    action_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    from_counterpart_id INTEGER,
                    to_counterpart_id INTEGER,
                    terms TEXT,
                    remarks TEXT,
                    category TEXT NOT NULL,
                    transfer_num TEXT NOT NULL,
                    transfer_date TEXT NOT NULL DEFAULT (date('now')),
                    user_id INTEGER,
                    lot_id INTEGER NOT NULL,
                    currency_code TEXT NOT NULL,
                    created_at TEXT NOT NULL DEFAULT (datetime('now')),
                    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
                    FOREIGN KEY (from_counterpart_id)
                        REFERENCES counterpart(counterpart_id)
                        ON DELETE SET NULL ON UPDATE CASCADE,
                    FOREIGN KEY (to_counterpart_id)
                        REFERENCES counterpart(counterpart_id)
                        ON DELETE SET NULL ON UPDATE CASCADE,
                    FOREIGN KEY (user_id)
                        REFERENCES user(user_id)
                        ON DELETE SET NULL ON UPDATE CASCADE,
                    FOREIGN KEY (lot_id)
                        REFERENCES item(lot_id)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (currency_code)
                        REFERENCES currency(code)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    CHECK (updated_at >= created_at),
                    CHECK (
                        from_counterpart_id IS NULL
                        OR to_counterpart_id IS NULL
                        OR from_counterpart_id != to_counterpart_id
                    )
                )"""
            );

            handle.execute("DROP TABLE IF EXISTS white_diamond");
            handle.execute("""
                            CREATE TABLE white_diamond (
                                lot_id INTEGER PRIMARY KEY,
                                weight_ct REAL NOT NULL CHECK (weight_ct > 0),
                                shape TEXT NOT NULL,
                                length REAL NOT NULL,
                                width REAL NOT NULL,
                                depth REAL NOT NULL,
                                white_scale TEXT NOT NULL,
                                clarity TEXT NOT NULL,
                                FOREIGN KEY (lot_id)
                                    REFERENCES loose_stone(lot_id)
                                    ON DELETE RESTRICT ON UPDATE CASCADE,
                                CHECK (length > 0 AND width > 0 AND depth > 0)
                            )
            """
            );


            handle.execute("DROP TABLE IF EXISTS colored_diamond");
            handle.execute("""
                            CREATE TABLE colored_diamond (
                                lot_id INTEGER PRIMARY KEY,
                                weight_ct REAL NOT NULL CHECK (weight_ct > 0),
                                shape TEXT NOT NULL,
                                length REAL NOT NULL,
                                width REAL NOT NULL,
                                depth REAL NOT NULL,
                                gem_type TEXT NOT NULL,
                                fancy_intensity TEXT NOT NULL,
                                fancy_overtone TEXT NOT NULL,
                                fancy_color TEXT NOT NULL,
                                clarity TEXT NOT NULL,
                                FOREIGN KEY (lot_id)
                                    REFERENCES loose_stone(lot_id)
                                    ON DELETE CASCADE ON UPDATE CASCADE,
                                CHECK (length > 0 AND width > 0 AND depth > 0)
                            )
            """
            );

            handle.execute("DROP TABLE IF EXISTS colored_gem_stone");
            handle.execute("""
                            CREATE TABLE colored_gem_stone (
                                lot_id INTEGER PRIMARY KEY,
                                weight_ct REAL NOT NULL CHECK (weight_ct > 0),
                                shape TEXT NOT NULL,
                                length REAL NOT NULL,
                                width REAL NOT NULL,
                                depth REAL NOT NULL,
                                gem_type TEXT NOT NULL,
                                gem_color TEXT NOT NULL,
                                treatment TEXT NOT NULL,
                                FOREIGN KEY (lot_id)
                                    REFERENCES loose_stone(lot_id)
                                    ON DELETE CASCADE ON UPDATE CASCADE,
                                CHECK (length > 0 AND width > 0 AND depth > 0)
                            )
            """
            );


        });
    }

}
