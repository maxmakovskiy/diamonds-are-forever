DROP SCHEMA IF EXISTS diamonds_are_forever CASCADE;
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


CREATE TABLE currency
(
   code code PRIMARY KEY,
   name TEXT NOT NULL UNIQUE
);

CREATE TABLE counterpart
(
   counterpartId SERIAL PRIMARY KEY,
   name          TEXT UNIQUE                            NOT NULL,
   phoneNumber   TEXT,
   addressShort  TEXT,
   city          TEXT,
   postalCode    TEXT,
   country       TEXT,
   email         TEXT UNIQUE,
   isActive      BOOLEAN                                NOT NULL DEFAULT TRUE,
   category      category NOT NULL,
   createdAt     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   updatedAt     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   CONSTRAINT valid_update_time CHECK (updatedAt >= createdAt)
);


CREATE TABLE employee
(
    employeeId        SERIAL PRIMARY KEY,
    counterpartId INT NOT NULL,
    firstName     TEXT NOT NULL,
    lastName      TEXT NOT NULL,
    email         TEXT UNIQUE NOT NULL,
    role          role NOT NULL,
    isActive      BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (counterpartId) REFERENCES counterpart (counterpartId)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE item
(
   lotId                SERIAL PRIMARY KEY,
   stockName            TEXT                                   NOT NULL,
   purchaseDate         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   origin               TEXT                                   NOT NULL,
   type                 item_category                          NOT NULL,
   createdAt            TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   updatedAt            TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
   CONSTRAINT valid_item_update CHECK (updatedAt >= createdAt)
);

CREATE TABLE action
(
    actionId          SERIAL PRIMARY KEY,
    fromCounterpartId INTEGER,
    toCounterpartId   INTEGER,
    terms             TEXT,
    category          transfer_category NOT NULL,
    shipNum           TEXT NOT NULL,
    shipDate          DATE DEFAULT CURRENT_DATE,
    lotId             INT NOT NULL,
    employeeId            INT NOT NULL,
    price             DECIMAL(15,2),
    currencyCode      code NOT NULL,
    createdAt         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updatedAt         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    FOREIGN KEY (fromCounterpartId) REFERENCES counterpart (counterpartId)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (toCounterpartId) REFERENCES counterpart (counterpartId)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT valid_update_time CHECK (updatedAt >= createdAt),
    FOREIGN KEY (lotId) REFERENCES item (lotId)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (employeeId) REFERENCES employee (employeeId)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (currencyCode) REFERENCES currency (code)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT non_negative_price CHECK (price >= 0)
);


CREATE TABLE white_diamond
(
   lotId     INTEGER PRIMARY KEY,
   weightCt  DECIMAL(5, 2) NOT NULL,
   shape     shape         NOT NULL,
   length    DECIMAL(4, 2) NOT NULL,
   width     DECIMAL(4, 2) NOT NULL,
   depth     DECIMAL(4, 2) NOT NULL,
   whiteScale white_scale NOT NULL,
   clarity    clarity     NOT NULL,
   FOREIGN KEY (lotId) REFERENCES item (lotId)
      ON DELETE RESTRICT ON UPDATE CASCADE,
       CONSTRAINT positive_weight CHECK (weightCt > 0),
   CONSTRAINT positive_dimensions CHECK (length > 0 AND width > 0 AND depth > 0)
);


CREATE TABLE colored_diamond
(
   lotId    INTEGER PRIMARY KEY,
   weightCt DECIMAL(5, 2) NOT NULL,
   shape     shape         NOT NULL,
   length    DECIMAL(4, 2) NOT NULL,
   width     DECIMAL(4, 2) NOT NULL,
   depth     DECIMAL(4, 2) NOT NULL,
   gemType       gem_type        NOT NULL,
   fancyIntensity fancy_intensity NOT NULL,
   fancyOvertone  TEXT            NOT NULL,
   fancyColor     fancy_color     NOT NULL,
   clarity        clarity         NOT NULL,
   FOREIGN KEY (lotId) REFERENCES item (lotId)
      ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE colored_gem_stone
(
   lotId    INTEGER PRIMARY KEY,
   weightCt DECIMAL(5, 2) NOT NULL,
   shape     shape         NOT NULL,
   length    DECIMAL(4, 2) NOT NULL,
   width     DECIMAL(4, 2) NOT NULL,
   depth     DECIMAL(4, 2) NOT NULL,
   gemType  gem_type  NOT NULL,
   gemColor gem_color NOT NULL,
   treatment treatment NOT NULL,
   FOREIGN KEY (lotId) REFERENCES item (lotId)
      ON DELETE CASCADE ON UPDATE CASCADE
);

COMMIT;

