-- Translate from PosgreSQL by ChatGPT

PRAGMA foreign_keys = ON;
BEGIN;

-- ======================
-- Currency
-- ======================

CREATE TABLE currency (
    code TEXT PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

-- ======================
-- Counterpart
-- ======================

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
);

-- ======================
-- User
-- ======================

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
);

-- ======================
-- Item
-- ======================

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
);

-- ======================
-- Action
-- ======================

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
);

-- ======================
-- Loose stone
-- ======================

CREATE TABLE loose_stone (
    lot_id INTEGER PRIMARY KEY,
    weight_ct REAL NOT NULL CHECK (weight_ct > 0),
    shape TEXT NOT NULL,
    length REAL NOT NULL,
    width REAL NOT NULL,
    depth REAL NOT NULL,
    FOREIGN KEY (lot_id)
        REFERENCES item(lot_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CHECK (length > 0 AND width > 0 AND depth > 0)
);

-- ======================
-- White diamond
-- ======================

CREATE TABLE white_diamond (
    lot_id INTEGER PRIMARY KEY,
    white_scale TEXT NOT NULL,
    clarity TEXT NOT NULL,
    FOREIGN KEY (lot_id)
        REFERENCES loose_stone(lot_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ======================
-- Colored diamond
-- ======================

CREATE TABLE colored_diamond (
    lot_id INTEGER PRIMARY KEY,
    gem_type TEXT NOT NULL,
    fancy_intensity TEXT NOT NULL,
    fancy_overtone TEXT NOT NULL,
    fancy_color TEXT NOT NULL,
    clarity TEXT NOT NULL,
    FOREIGN KEY (lot_id)
        REFERENCES loose_stone(lot_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================
-- Colored gemstone
-- ======================

CREATE TABLE colored_gem_stone (
    lot_id INTEGER PRIMARY KEY,
    gem_type TEXT NOT NULL,
    gem_color TEXT NOT NULL,
    treatment TEXT NOT NULL,
    FOREIGN KEY (lot_id)
        REFERENCES loose_stone(lot_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================
-- Jewelry
-- ======================

CREATE TABLE jewelry (
    lot_id INTEGER PRIMARY KEY,
    jewelry_type TEXT NOT NULL,
    gross_weight_gr REAL NOT NULL CHECK (gross_weight_gr > 0),
    metal_type TEXT NOT NULL,
    metal_weight_gr REAL NOT NULL CHECK (metal_weight_gr > 0),
    total_center_stone_qty INTEGER NOT NULL,
    total_center_stone_weight_ct REAL NOT NULL,
    centered_stone_type TEXT NOT NULL,
    total_side_stone_qty INTEGER NOT NULL,
    total_side_stone_weight_ct REAL NOT NULL,
    side_stone_type TEXT NOT NULL,
    FOREIGN KEY (lot_id)
        REFERENCES item(lot_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (metal_weight_gr <= gross_weight_gr)
);

COMMIT;

