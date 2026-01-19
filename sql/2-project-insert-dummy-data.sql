-- Dummy data was generated with a help of ChatGPT

SET search_path TO diamonds_are_forever;

BEGIN;

INSERT INTO currency (code, name) VALUES
('USD', 'United States Dollar'),
('HKD', 'Hong Kong Dollar'),
('CHF', 'Swiss Franc'),
('EUR', 'Euro'),
('NTD', 'New Taiwan Dollar');


-- Offices (Internal)
INSERT INTO counterpart (counterpartId, name, phoneNumber, addressShort, city, postalCode, country, email, isActive, category) VALUES
(1, 'New York Office', '+1-212-555-0101', '580 5th Ave', 'New York', '10036', 'USA', 'ny@gemcompany.com', TRUE, 'Office'),
(2, 'Hong Kong Office', '+852-2345-6789', '88 Queensway', 'Hong Kong', '999077', 'Hong Kong', 'hk@gemcompany.com', TRUE, 'Office'),
(3, 'Geneva Office', '+41-44-555-0102', 'Bahnhofstrasse 45', 'Geneva', '1201', 'Switzerland', 'geneva@gemcompany.com', TRUE, 'Office'),
(4, 'Tokyo Office', '+81-3-5555-1234', '1-2-3 Ginza', 'Tokyo', '104-0061', 'Japan', 'tokyo@gemcompany.com', TRUE, 'Office');

-- Diamond Suppliers
INSERT INTO counterpart (counterpartId, name, phoneNumber, addressShort, city, postalCode, country, email, isActive, category) VALUES
(5, 'Antwerp Diamond Exchange', '+32-3-234-5678', 'Pelikaanstraat 62', 'Antwerp', '2018', 'Belgium', 'info@antwerpdiamonds.be', TRUE, 'Supplier'),
(6, 'Mumbai Diamond House', '+91-22-2345-6789', 'Opera House', 'Mumbai', '400004', 'India', 'sales@mumbaidia.in', TRUE, 'Supplier'),
(7, 'Tel Aviv Gem Co', '+972-3-517-8888', 'Ramat Gan', 'Tel Aviv', '5252100', 'Israel', 'contact@telavivgems.il', TRUE, 'Supplier'),
(8, 'Botswana Diamond Corp', '+267-318-0000', 'Plot 64518', 'Gaborone', '00000', 'Botswana', 'export@bwdiamonds.bw', TRUE, 'Supplier');

-- Colored Gem Suppliers
INSERT INTO counterpart (counterpartId, name, phoneNumber, addressShort, city, postalCode, country, email, isActive, category) VALUES
(9, 'Bangkok Ruby Traders', '+66-2-234-5678', '919 Silom Road', 'Bangkok', '10500', 'Thailand', 'sales@bangkokruby.th', TRUE, 'Supplier'),
(10, 'Colombian Emerald Source', '+57-1-234-5678', 'Carrera 7', 'Bogota', '110111', 'Colombia', 'info@colombiaemeralds.co', TRUE, 'Supplier'),
(11, 'Kashmir Sapphire Ltd', '+91-194-245-6789', 'Residency Road', 'Srinagar', '190001', 'India', 'contact@kashmirsapphire.in', TRUE, 'Supplier'),
(12, 'Myanmar Ruby Export', '+95-1-234-567', 'Merchant Street', 'Yangon', '11182', 'Myanmar', 'export@myanmarruby.mm', TRUE, 'Supplier');

-- Retail Clients
INSERT INTO counterpart (counterpartId, name, phoneNumber, addressShort, city, postalCode, country, email, isActive, category) VALUES
(13, 'Tiffany & Partners', '+1-212-755-8000', '727 5th Avenue', 'New York', '10022', 'USA', 'wholesale@tiffanypartners.com', TRUE, 'Client'),
(14, 'Cartier Geneva SA', '+41-22-818-1010', 'Rue du Rhone 35', 'Geneva', '1204', 'Switzerland', 'orders@cartiergeneva.ch', TRUE, 'Client'),
(15, 'Van Cleef Tokyo', '+81-3-5561-8888', '2-10-1 Ginza', 'Tokyo', '104-0061', 'Japan', 'purchasing@vancleeftokyo.jp', TRUE, 'Client'),
(16, 'Bulgari Hong Kong', '+852-2524-6888', 'Canton Road 3', 'Hong Kong', '999077', 'Hong Kong', 'sales@bulgarihk.com', TRUE, 'Client');

-- Certification Labs
INSERT INTO counterpart (counterpartId, name, phoneNumber, addressShort, city, postalCode, country, email, isActive, category) VALUES
(17, 'GIA Laboratory', '+1-760-603-4500', '5345 Armada Drive', 'Carlsbad', '92008', 'USA', 'lab@gia.edu', TRUE, 'Lab'),
(18, 'IGI International', '+32-3-201-0581', 'Schupstraat 1', 'Antwerp', '2018', 'Belgium', 'info@igi.org', TRUE, 'Lab'),
(19, 'HRD Antwerp', '+32-3-222-0511', 'Hoveniersstraat 22', 'Antwerp', '2018', 'Belgium', 'info@hrdantwerp.com', TRUE, 'Lab'),
(20, 'AGS Laboratories', '+1-702-255-6757', '3309 Juanita Street', 'Las Vegas', '89102', 'USA', 'lab@ags.org', TRUE, 'Lab');

-- Processing Manufacturers
INSERT INTO counterpart (counterpartId, name, phoneNumber, addressShort, city, postalCode, country, email, isActive, category) VALUES
(21, 'Surat Diamond Cutting', '+91-261-234-5678', 'Mini Bazaar', 'Surat', '395002', 'India', 'factory@suratcutting.in', TRUE, 'Manufacturer'),
(22, 'Antwerp Precision Cut', '+32-3-232-8899', 'Lange Herentalsestraat', 'Antwerp', '2018', 'Belgium', 'production@antwerpcut.be', TRUE, 'Manufacturer'),
(23, 'Bangkok Gem Processing', '+66-2-234-9999', 'Jewelry Trade Center', 'Bangkok', '10500', 'Thailand', 'service@bangkokprocess.th', TRUE, 'Manufacturer');


INSERT INTO employee (employeeId, counterpartId, firstName, lastName, email, role, isActive) VALUES
-- NY Office Staff
(1, 1, 'John', 'Smith', 'john.smith@example.com', 'Chief', TRUE),
(2, 1, 'Sarah', 'Johnson', 'sarah.johnson@example.com', 'Admin', TRUE),
(3, 1, 'Michael', 'Williams', 'michael.williams@example.com', 'Sales', TRUE),
(4, 1, 'Emily', 'Brown', 'emily.brown@example.com', 'Accountant', TRUE),
-- HK Office Staff
(5, 2, 'David', 'Chen', 'david.chen@example.com', 'Chief', TRUE),
(6, 2, 'Lisa', 'Wong', 'lisa.wong@example.com', 'Admin', TRUE),
(7, 2, 'Kevin', 'Lee', 'kevin.lee@example.com', 'Sales', TRUE),
-- Geneva Office Staff
(8, 3, 'Hans', 'Mueller', 'hans.mueller@example.com', 'Chief', TRUE),
(9, 3, 'Anna', 'Schmidt', 'anna.schmidt@example.com', 'Sales', TRUE),
(10, 3, 'Thomas', 'Weber', 'thomas.weber@example.comm', 'Accountant', TRUE),
-- Tokyo Office Staff
(11, 4, 'Yuki', 'Tanaka', 'yuki.tanaka@example.com', 'Chief', TRUE),
(12, 4, 'Sakura', 'Yamamoto', 'sakura.yamamoto@example.com', 'Sales', TRUE);


-- White Diamonds (15 items)
INSERT INTO item (lotId, stockName, purchaseDate, origin, type) VALUES
(1, 'WD-2024-001', '2024-01-15 10:00:00+00', 'South Africa', 'white diamond'),
(2, 'WD-2024-002', '2024-01-20 14:30:00+00', 'Botswana', 'white diamond'),
(3, 'WD-2024-003', '2024-02-05 09:15:00+00', 'India', 'white diamond'),
(4, 'WD-2024-004', '2024-02-10 11:45:00+00', 'Russia', 'white diamond'),
(5, 'WD-2024-005', '2024-02-15 13:20:00+00', 'Botswana', 'white diamond');

-- Colored Diamonds (10 items)
INSERT INTO item (lotId, stockName, purchaseDate, origin, type) VALUES
(6, 'CD-2024-001', '2024-01-25 11:00:00+00', 'South Africa', 'colored diamond'),
(7, 'CD-2024-002', '2024-02-12 15:30:00+00', 'Australia', 'colored diamond'),
(8, 'CD-2024-003', '2024-03-08 10:45:00+00', 'India', 'colored diamond'),
(9, 'CD-2024-004', '2024-03-22 14:00:00+00', 'South Africa', 'colored diamond'),
(10, 'CD-2024-005', '2024-04-15 09:20:00+00', 'Botswana', 'colored diamond');

-- Rubies (8 items)
INSERT INTO item (lotId, stockName, purchaseDate, origin, type) VALUES
(11, 'RB-2024-001', '2024-01-18 09:30:00+00', 'Thailand', 'colored gemstone'),
(12, 'RB-2024-002', '2024-02-08 14:45:00+00', 'Myanmar', 'colored gemstone'),
(13, 'RB-2024-003', '2024-03-12 10:20:00+00', 'Thailand', 'colored gemstone');

-- Sapphires (8 items)
INSERT INTO item (lotId, stockName, purchaseDate, origin, type) VALUES
(14, 'SP-2024-001', '2024-01-22 10:15:00+00', 'Kashmir', 'colored gemstone'),
(15, 'SP-2024-002', '2024-02-14 15:30:00+00', 'Sri Lanka', 'colored gemstone'),
(16, 'SP-2024-003', '2024-03-05 09:45:00+00', 'Kashmir', 'colored gemstone');

-- Emeralds (6 items)
INSERT INTO item (lotId, stockName, purchaseDate, origin, type) VALUES
(17, 'EM-2024-001', '2024-02-01 11:20:00+00', 'Colombia', 'colored gemstone'),
(18, 'EM-2024-002', '2024-03-15 14:40:00+00', 'Colombia', 'colored gemstone'),
(19, 'EM-2024-003', '2024-04-08 09:55:00+00', 'Zambia', 'colored gemstone');



INSERT INTO white_diamond (lotId, weightCt, shape, length, width, depth,
                           whiteScale, clarity) VALUES
(1, 1.52, 'Brilliant Cut', 7.45, 7.42, 4.58, 'F', 'VS1'),
(2, 2.03, 'Brilliant Cut', 8.15, 8.12, 5.02, 'D', 'VVS2'),
(3, 0.75, 'Princess', 5.32, 5.29, 3.88, 'G', 'VS2'),
(4, 3.21, 'Emerald Cut', 10.12, 8.05, 5.67, 'E', 'VVS1'),
(5, 1.08, 'Brilliant Cut', 6.58, 6.55, 4.05, 'H', 'VS1');


INSERT INTO colored_diamond (lotId, weightCt, shape, length, width, depth,
                             gemType, fancyIntensity, fancyOvertone,
                             fancyColor, clarity) VALUES
(6, 1.85, 'Brilliant Cut', 7.92, 7.89, 4.88, 'Diamond', 'Fancy', 'None', 'Yellow', 'VS1'),
(7, 2.34, 'Radiant Cut', 8.45, 7.58, 4.92, 'Diamond', 'Fancy intense', 'None', 'Yellow', 'VVS2'),
(8, 0.88, 'Princess', 5.68, 5.65, 3.95, 'Diamond', 'Fancy light', 'None', 'Blue', 'VS2'),
(9, 1.52, 'Oval', 8.15, 6.32, 4.25, 'Diamond', 'Fancy', 'Brownish', 'Yellow', 'VS1'),
(10, 3.15, 'Brilliant Cut', 9.85, 9.82, 6.08, 'Diamond', 'Fansy Vivid', 'None', 'Yellow', 'VVS1');

-- Rubies
INSERT INTO colored_gem_stone (lotId, weightCt, shape, length, width, depth,
                               gemType, gemColor, treatment) VALUES
(11, 2.45, 'Oval', 8.85, 6.75, 4.52, 'Ruby', 'Red', 'heated'),
(12, 3.82, 'Oval', 10.25, 8.15, 5.35, 'Ruby', 'Pigeon blood', 'No heat'),
(13, 1.68, 'Oval', 7.95, 6.05, 4.08, 'Ruby', 'Red', 'heated');

-- Sapphires
INSERT INTO colored_gem_stone (lotId, weightCt, shape, length, width, depth,
                               gemType, gemColor, treatment) VALUES
(14, 3.15, 'Oval', 9.75, 7.85, 5.12, 'Sapphire', 'Royal Blue', 'No heat'),
(15, 2.28, 'Oval', 8.65, 6.95, 4.55, 'Sapphire', 'Blue', 'heated'),
(16, 4.52, 'Oval', 11.15, 9.05, 5.98,  'Sapphire', 'Royal Blue', 'No heat');

-- Emeralds
INSERT INTO colored_gem_stone (lotId, weightCt, shape, length, width, depth,
                               gemType, gemColor, treatment) VALUES
(17, 2.85, 'Emerald Cut', 9.25, 7.15, 5.05, 'Emerald', 'Green', 'Minor Oil'),
(18, 3.95, 'Emerald Cut', 10.65, 8.25, 5.85, 'Emerald', 'Green', 'No oil'),
(19, 1.75, 'Emerald Cut', 7.85, 6.05, 4.35, 'Emerald', 'Green', 'Oiled');

INSERT INTO action (fromCounterpartId, toCounterpartId, terms, 
                    category, lotId, employeeId, price, currencyCode, shipNum, shipDate) VALUES
-- Purchases from diamond suppliers
(5, 1, 'Payment: 30 days net', 'purchase',  1, 1, 13244, 'USD', 'PO-2024-0001', '2024-01-15'),
(6, 2, 'Payment: 60 days net', 'purchase',  2, 5, 22441, 'USD', 'PO-2024-0002', '2024-01-25'),
(9, 2, 'Payment: 45 days net', 'purchase',  3, 5, 4000, 'USD', 'PO-2024-0002', '2024-01-25'),
(11, 2, 'Payment: 30 days net', 'purchase', 4, 5, 4151, 'USD', 'PO-2024-0002', '2024-01-25'),
(10, 1, 'Payment: 60 days net', 'purchase', 5, 1, 9555, 'USD', 'PO-2024-0001', '2024-01-15'),
-- colored diamonds
(5, 1, 'Payment: 30 days net', 'purchase', 6, 1, 18000, 'USD', 'PO-2024-0003', '2024-01-18'),
(5, 1, 'Payment: 10 days net', 'purchase', 7, 1, 22550, 'USD', 'PO-2024-0003', '2024-01-18'),
(7, 2, 'Payment: 20 days net', 'purchase', 8, 5, 50155, 'USD', 'PO-2024-0004', '2024-01-13'),
(8, 3, 'Payment: 10 days net', 'purchase', 9, 8, 19811, 'USD', 'PO-2024-0005', '2024-01-11'),
(8, 3, 'Payment: 10 days net', 'purchase', 10, 8, 12555, 'USD', 'PO-2024-0005', '2024-01-11'),
-- rubies
(10, 4, 'Payment: 30 days net', 'purchase', 11, 11, 3000, 'USD', 'PO-2024-0006', '2024-01-09'),
(11, 4, 'Payment: 30 days net', 'purchase', 12, 11, 4000, 'USD', 'PO-2024-0006', '2024-01-09'),
(9, 1, 'Payment: 30 days net', 'purchase', 13, 1, 1300, 'USD', 'PO-2024-0007', '2024-01-14'),
-- Sapphires
(9, 2, 'Payment: 60 days net', 'purchase', 14, 5, 4100, 'USD', 'PO-2024-0008', '2024-01-18'),
(9, 3, 'Payment: 30 days net', 'purchase', 15, 8, 2800, 'USD', 'PO-2024-0009', '2024-01-18'),
(10, 1, 'Payment: 45 days net', 'purchase', 16, 1, 4300, 'USD', 'PO-2024-0010', '2024-01-21'),
-- Emeralds
(12, 3, 'Payment: 60 days net', 'purchase', 17, 8, 3700, 'USD', 'PO-2024-0011', '2024-01-20'),
(12, 3, 'Payment: 30 days net', 'purchase', 18, 8, 2200, 'USD', 'PO-2024-0011', '2024-01-20'),
(11, 4, 'Payment: 60 days net', 'purchase', 19, 11, 3600, 'USD', 'PO-2024-0012', '2024-01-22');


-- Update sequences after manual insertions
SELECT pg_catalog.setval('diamonds_are_forever.counterpart_counterpartId_seq', 26, true);
SELECT pg_catalog.setval('diamonds_are_forever.employee_employeeId_seq', 12, true);
SELECT pg_catalog.setval('diamonds_are_forever.item_lotId_seq', 19, true);

-- ROLLBACK;
COMMIT;

-- Display summary
-- SELECT COUNT(*) AS total_items FROM item;
-- SELECT COUNT(*) AS total_actions FROM action;

