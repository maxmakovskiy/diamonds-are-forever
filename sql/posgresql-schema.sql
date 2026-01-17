-- DROP SCHEMA IF EXISTS diamonds_are_forever CASCADE;
CREATE SCHEMA IF NOT EXISTS diamonds_are_forever;

SET search_path TO diamonds_are_forever;

BEGIN;

--ENUM TYPE

CREATE TYPE code AS ENUM ('USD', 'HKD', 'CHF', 'EUR', 'NTD');
CREATE TYPE category AS ENUM ('Supplier', 'Client', 'Office',
    'Lab', 'Manufacturer');
CREATE TYPE role AS ENUM ('Chief', 'Admin', 'Sales', 'Accountant');
CREATE TYPE shape AS ENUM ('Brilliant Cut', 'Pear Shape', 'Radiant Cut',
    'Heart Shape', 'Emerald Cut', 'Baquette', 'Briolette', 'Kite',
    'Marquise', 'Oval', 'Princess', 'Trillion');
CREATE TYPE clarity AS ENUM ('I1', 'I2', 'VS', 'VS1', 'VS2', 'VVS',
    'VVS1', 'VVS2','FL', 'IF');
CREATE TYPE gem_type AS ENUM ('Sapphire', 'Emerald', 'Ruby', 'Diamond');
CREATE TYPE fancy_intensity AS ENUM ('Faint', 'Very Light', 'Light',
    'Fancy light', 'Fancy','Fansy Vivid',
    'Fancy intense', 'Fancy Deep', 'Fansy Dark');
CREATE TYPE fancy_color AS ENUM ('Red', 'Orange', 'Yellow',
    'Green', 'Blue', 'Violet', 'Gray');
CREATE TYPE jewelry_type AS ENUM ('Earrings', 'Necklace', 'Ring',
    'Brooch', 'Bracelet');
CREATE TYPE metal_type AS ENUM ('PT900', 'PT950', '18k white gold',
    '14k white gold', '18k white/yellow gold',
    '18k rose gold', '18k white gold + PT');
CREATE TYPE update_type_enum AS ENUM ('Insert', 'Update', 'Delete');
CREATE TYPE action_role_type AS ENUM ('Creator', 'Approver',
    'Processor', 'Reviewer');
CREATE TYPE lab_purpose AS ENUM ('Certify', 'Re-certify');
CREATE TYPE processing_type AS ENUM ('Remove oil', 'Recut');
CREATE TYPE payment_status AS ENUM ('Partial paid', 'Unpaid', 'Paid');
CREATE TYPE treatment AS ENUM ('No heat', 'heated', 'No oil',
    'Minor Oil', 'Oiled');
CREATE TYPE gem_color AS ENUM ('Red', 'Blue', 'Green',
    'Pigeon blood', 'Royal Blue');
CREATE TYPE white_scale AS ENUM ( 'D', 'E', 'F', 'G', 'H',
    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
CREATE TYPE transfer_category AS ENUM ('purchase', 'memo in',
    'return memo in', 'transfer to lab', 'return from lab',
    'transfer to factory', 'return from factory', 'transfer to office',
    'memo out', 'return memo out', 'sale');
CREATE TYPE item_category AS ENUM ('white diamond', 'colored diamond',
    'colored gemstone', 'jewelry');


--Create tables

CREATE TABLE currency
(
   code code PRIMARY KEY,
   name TEXT NOT NULL UNIQUE
);

CREATE TABLE counterpart
(
   counterpart_id SERIAL PRIMARY KEY,
   name           TEXT UNIQUE                            NOT NULL,
   phone_number   TEXT,
   address_short  TEXT,
   city           TEXT,
   postal_code    TEXT,
   country        TEXT,
   contact_email  TEXT UNIQUE,
   category       category                               NOT NULL,
   is_active      BOOLEAN                                NOT NULL DEFAULT TRUE,
   created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   updated_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   CONSTRAINT valid_update_time CHECK (updated_at >= created_at)
);


CREATE TABLE user 
(
   user_id SERIAL PRIMARY KEY,
   counterpart_id INTEGER                                NOT NULL,
   first_name     TEXT                                   NOT NULL,
   last_name      TEXT                                   NOT NULL,
   email          TEXT UNIQUE                            NOT NULL,
   password_hash  TEXT                                   NOT NULL,
   role           role                                   NOT NULL,
   is_active      BOOLEAN                                NOT NULL DEFAULT TRUE,
   created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   updated_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   FOREIGN KEY (counterpart_id) REFERENCES counterpart (counterpart_id)
      ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT valid_update_time CHECK (updated_at >= created_at)
);


CREATE TABLE item
(
   lot_id                SERIAL PRIMARY KEY,
   stock_name            TEXT                                   NOT NULL,
   purchase_date         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   supplier_id           INTEGER                                NOT NULL,
   origin                TEXT                                   NOT NULL,
   responsible_office_id INTEGER                                NOT NULL,
   item_type             item_category                          NOT NULL,
   created_at            TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   updated_at            TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   is_available          BOOLEAN                                NOT NULL DEFAULT TRUE,
   FOREIGN KEY (responsible_office_id) REFERENCES counterpart (counterpart_id)
      ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY (supplier_id) REFERENCES counterpart (counterpart_id)
      ON DELETE RESTRICT ON UPDATE CASCADE,
   CONSTRAINT valid_item_update CHECK (updated_at >= created_at)
);


CREATE TABLE action
(
   action_id           SERIAL PRIMARY KEY,
   from_counterpart_id INTEGER,
   to_counterpart_id   INTEGER,
   terms               TEXT,
   remarks             TEXT,
   category            transfer_category NOT NULL,
   transfer_num        TEXT NOT NULL,
   transfer_date       DATE DEFAULT CURRENT_DATE,
   user_id             INTEGER NOT NULL,
   lot_id              INTEGER NOT NULL,
   currency_code       code NOT NULL,
   created_at          TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   updated_at          TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   FOREIGN KEY (from_counterpart_id) REFERENCES counterpart (counterpart_id)
      ON DELETE SET NULL ON UPDATE CASCADE,
   FOREIGN KEY (to_counterpart_id) REFERENCES counterpart (counterpart_id)
      ON DELETE SET NULL ON UPDATE CASCADE,
   FOREIGN KEY (user_id) REFERENCES user (user_id)
      ON DELETE SET NULL ON UPDATE CASCADE,
   FOREIGN KEY (lot_id) REFERENCES item (lot_id)
      ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY (currency_code) REFERENCES currency (code)
      ON DELETE RESTRICT ON UPDATE CASCADE,
   CONSTRAINT valid_update_time CHECK (updated_at >= created_at),
   CONSTRAINT different_counterparts CHECK (from_counterpart_id != to_counterpart_id)
);



-- loose_stone (**lot_id**, weight_ct, length, width, depth)
--    loose_stone.lot_id references item.lot_id
CREATE TABLE loose_stone
(
   lot_id    INTEGER PRIMARY KEY,
   weight_ct DECIMAL(5, 2) NOT NULL,
   shape     shape         NOT NULL,
   length    DECIMAL(4, 2) NOT NULL,
   width     DECIMAL(4, 2) NOT NULL,
   depth     DECIMAL(4, 2) NOT NULL,
   FOREIGN KEY (lot_id) REFERENCES item (lot_id)
      ON DELETE RESTRICT ON UPDATE CASCADE,
   CONSTRAINT positive_weight CHECK (weight_ct > 0),
   CONSTRAINT positive_dimensions CHECK (length > 0 AND width > 0 AND depth > 0)
);


-- white_diamond (**lot_id**, white_level, shape, clarity)
--     white_diamond.lot_id references loose_stone.lot_id

CREATE TABLE white_diamond
(
   lot_id      INTEGER PRIMARY KEY,
   white_scale white_scale NOT NULL,
   clarity     clarity     NOT NULL,
   FOREIGN KEY (lot_id) REFERENCES loose_stone (lot_id)
      ON DELETE RESTRICT ON UPDATE CASCADE
);

-- colored_diamond (**lot_id**, gem_type, fancy_intensity, fancy_overton, fancy_color, shape, clarity)
--     colored_diamond.lot_id references loose_stone.lot_id


CREATE TABLE colored_diamond
(
   lot_id          INTEGER PRIMARY KEY,
   gem_type        gem_type        NOT NULL,
   fancy_intensity fancy_intensity NOT NULL,
   fancy_overtone  TEXT            NOT NULL,
   fancy_color     fancy_color     NOT NULL,
   clarity         clarity         NOT NULL,
   FOREIGN KEY (lot_id) REFERENCES loose_stone (lot_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);

-- colored_gem_stone (**lot_id**, gem_type, shape, color, treatment, origin)
--     colored_gem_stone.lot_id references loose_stone.lot_id


CREATE TABLE colored_gem_stone
(
   lot_id    INTEGER PRIMARY KEY,
   gem_type  gem_type  NOT NULL,
   gem_color gem_color NOT NULL,
   treatment treatment NOT NULL,
   FOREIGN KEY (lot_id) REFERENCES loose_stone (lot_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);

-- jewerly (**lot_id**, jew_type, gross_weight_gr, metal_type, metal_weight_gr,
--     total_center_stone_qty, total_center_stone_weight_ct, centered_stone_type,
--    total_side_stone_qty, total_side_stone_weight_ct, side_stone_type)
CREATE TABLE jewelry
(
   lot_id                       INTEGER PRIMARY KEY,
   jewelry_type                 jewelry_type  NOT NULL,
   gross_weight_gr              DECIMAL(5, 2) NOT NULL,
   metal_type                   metal_type    NOT NULL,
   metal_weight_gr              DECIMAL(5, 2) NOT NULL,
   total_center_stone_qty       INTEGER       NOT NULL,
   total_center_stone_weight_ct DECIMAL(5, 2) NOT NULL,
   centered_stone_type          TEXT          NOT NULL,
   total_side_stone_qty         INTEGER       NOT NULL,
   total_side_stone_weight_ct   DECIMAL(5, 2) NOT NULL,
   side_stone_type              TEXT          NOT NULL,
   FOREIGN KEY (lot_id) REFERENCES item (lot_id)
      ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT positive_weights CHECK (gross_weight_gr > 0 AND metal_weight_gr > 0),
   CONSTRAINT metal_weight_check CHECK (metal_weight_gr <= gross_weight_gr)

);

END;
-- ROLLBACK;

