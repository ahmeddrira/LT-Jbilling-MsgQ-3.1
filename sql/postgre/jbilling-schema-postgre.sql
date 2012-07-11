Liquibase Home is not set.
Liquibase Home: /home/aristokrates/jbilling/liquidbase-2.0.5
-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: descriptors/database/jbilling-schema.xml
-- Ran at: 11/07/12 3:02 PM
-- Against: jbilling@jdbc:postgresql://localhost:5432/jbilling_test
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITH TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

INSERT INTO databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE databasechangelog (ID VARCHAR(63) NOT NULL, AUTHOR VARCHAR(63) NOT NULL, FILENAME VARCHAR(200) NOT NULL, DATEEXECUTED TIMESTAMP WITH TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONSTRAINT PK_DATABASECHANGELOG PRIMARY KEY (ID, AUTHOR, FILENAME));

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-1::Emiliano Conde::(Checksum: 3:03a5cebb218306ca9a12129773f33725)
CREATE TABLE ach (id INT NOT NULL, user_id INT, aba_routing VARCHAR(40) NOT NULL, bank_account VARCHAR(60) NOT NULL, account_type INT NOT NULL, bank_name VARCHAR(50) NOT NULL, account_name VARCHAR(100) NOT NULL, optlock INT NOT NULL, gateway_key VARCHAR(100), CONSTRAINT ach_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-1', '2.0.5', '3:03a5cebb218306ca9a12129773f33725', 1);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-2::Emiliano Conde::(Checksum: 3:61010718ff66001bc8bc0984204773b2)
CREATE TABLE ageing_entity_step (id INT NOT NULL, entity_id INT, status_id INT, days INT NOT NULL, optlock INT NOT NULL, CONSTRAINT ageing_entity_step_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-2', '2.0.5', '3:61010718ff66001bc8bc0984204773b2', 2);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-3::Emiliano Conde::(Checksum: 3:be23a14b20287e6a3089c035dbccb540)
CREATE TABLE base_user (id INT NOT NULL, entity_id INT, password VARCHAR(40), deleted INT DEFAULT 0 NOT NULL, language_id INT, status_id INT, subscriber_status INT DEFAULT 1, currency_id INT, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, last_status_change TIMESTAMP WITH TIME ZONE, last_login TIMESTAMP WITH TIME ZONE, user_name VARCHAR(50), failed_attempts INT DEFAULT 0 NOT NULL, optlock INT NOT NULL, CONSTRAINT base_user_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-3', '2.0.5', '3:be23a14b20287e6a3089c035dbccb540', 3);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-4::Emiliano Conde::(Checksum: 3:3015d3a382608425fa527e6d4d9d8377)
CREATE TABLE billing_process (id INT NOT NULL, entity_id INT NOT NULL, billing_date DATE NOT NULL, period_unit_id INT NOT NULL, period_value INT NOT NULL, is_review INT NOT NULL, paper_invoice_batch_id INT, retries_to_do INT DEFAULT 0 NOT NULL, optlock INT NOT NULL, CONSTRAINT billing_process_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-4', '2.0.5', '3:3015d3a382608425fa527e6d4d9d8377', 4);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-5::Emiliano Conde::(Checksum: 3:ecbd9979013788fbeaa1aa531d5e1dab)
CREATE TABLE billing_process_configuration (id INT NOT NULL, entity_id INT, next_run_date DATE NOT NULL, generate_report INT NOT NULL, retries INT, days_for_retry INT, days_for_report INT, review_status INT NOT NULL, period_unit_id INT NOT NULL, period_value INT NOT NULL, due_date_unit_id INT NOT NULL, due_date_value INT NOT NULL, df_fm INT, only_recurring INT DEFAULT 1 NOT NULL, invoice_date_process INT NOT NULL, optlock INT NOT NULL, auto_payment INT DEFAULT 0 NOT NULL, maximum_periods INT DEFAULT 1 NOT NULL, auto_payment_application INT DEFAULT 0 NOT NULL, CONSTRAINT billing_process_config_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-5', '2.0.5', '3:ecbd9979013788fbeaa1aa531d5e1dab', 5);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-6::Emiliano Conde::(Checksum: 3:a0ee15f0a67452a7326fd72bc0b3e640)
CREATE TABLE blacklist (id INT NOT NULL, entity_id INT NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, type INT NOT NULL, source INT NOT NULL, credit_card INT, credit_card_id INT, contact_id INT, user_id INT, optlock INT NOT NULL, meta_field_value_id INT, CONSTRAINT blacklist_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-6', '2.0.5', '3:a0ee15f0a67452a7326fd72bc0b3e640', 6);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-7::Emiliano Conde::(Checksum: 3:7bacba393473f05078e84d0f2a03e01c)
CREATE TABLE breadcrumb (id INT NOT NULL, user_id INT NOT NULL, controller VARCHAR(255) NOT NULL, action VARCHAR(255), name VARCHAR(255), object_id INT, "version" INT NOT NULL, description VARCHAR(255), CONSTRAINT breadcrumb_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-7', '2.0.5', '3:7bacba393473f05078e84d0f2a03e01c', 7);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-8::Emiliano Conde::(Checksum: 3:a759acb302158b4582ae9d0c12ef6368)
CREATE TABLE cdrentries (id INT NOT NULL, accountcode VARCHAR(20), src VARCHAR(20), dst VARCHAR(20), dcontext VARCHAR(20), clid VARCHAR(20), channel VARCHAR(20), dstchannel VARCHAR(20), lastapp VARCHAR(20), lastdatat VARCHAR(20), start_time TIMESTAMP WITH TIME ZONE, answer TIMESTAMP WITH TIME ZONE, end_time TIMESTAMP WITH TIME ZONE, duration INT, billsec INT, disposition VARCHAR(20), amaflags VARCHAR(20), userfield VARCHAR(100), ts TIMESTAMP WITH TIME ZONE, CONSTRAINT cdrentries_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-8', '2.0.5', '3:a759acb302158b4582ae9d0c12ef6368', 8);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-9::Emiliano Conde::(Checksum: 3:e81dd6f9d69a8785570b0883ec3841bc)
CREATE TABLE contact (id INT NOT NULL, organization_name VARCHAR(200), street_addres1 VARCHAR(100), street_addres2 VARCHAR(100), city VARCHAR(50), state_province VARCHAR(30), postal_code VARCHAR(15), country_code VARCHAR(2), last_name VARCHAR(30), first_name VARCHAR(30), person_initial VARCHAR(5), person_title VARCHAR(40), phone_country_code INT, phone_area_code INT, phone_phone_number VARCHAR(20), fax_country_code INT, fax_area_code INT, fax_phone_number VARCHAR(20), email VARCHAR(200), create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, deleted INT DEFAULT 0 NOT NULL, notification_include INT DEFAULT 1, user_id INT, optlock INT NOT NULL, CONSTRAINT contact_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-9', '2.0.5', '3:e81dd6f9d69a8785570b0883ec3841bc', 9);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-10::Emiliano Conde::(Checksum: 3:e616cf3e970760f3434101e6613f17d2)
CREATE TABLE contact_map (id INT NOT NULL, contact_id INT, type_id INT NOT NULL, table_id INT NOT NULL, foreign_id INT NOT NULL, optlock INT NOT NULL, CONSTRAINT contact_map_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-10', '2.0.5', '3:e616cf3e970760f3434101e6613f17d2', 10);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-11::Emiliano Conde::(Checksum: 3:5704c03ead72025fe1277d6a92e6d0e4)
CREATE TABLE contact_type (id INT NOT NULL, entity_id INT, is_primary INT, optlock INT NOT NULL, CONSTRAINT contact_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-11', '2.0.5', '3:5704c03ead72025fe1277d6a92e6d0e4', 11);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-12::Emiliano Conde::(Checksum: 3:9413e0e10251d19fb5ab961542a3ca8a)
CREATE TABLE country (id INT NOT NULL, code VARCHAR(2) NOT NULL, CONSTRAINT country_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-12', '2.0.5', '3:9413e0e10251d19fb5ab961542a3ca8a', 12);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-13::Emiliano Conde::(Checksum: 3:25322730bee882bdef7faefe1be7c97e)
CREATE TABLE credit_card (id INT NOT NULL, cc_number VARCHAR(100) NOT NULL, cc_number_plain VARCHAR(20), cc_expiry DATE NOT NULL, name VARCHAR(150), cc_type INT NOT NULL, deleted INT DEFAULT 0 NOT NULL, gateway_key VARCHAR(100), optlock INT NOT NULL, CONSTRAINT credit_card_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-13', '2.0.5', '3:25322730bee882bdef7faefe1be7c97e', 13);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-14::Emiliano Conde::(Checksum: 3:8de11595ff6ac63a0a475960fe2b9f56)
CREATE TABLE currency (id INT NOT NULL, symbol VARCHAR(10) NOT NULL, code VARCHAR(3) NOT NULL, country_code VARCHAR(2) NOT NULL, optlock INT, CONSTRAINT currency_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-14', '2.0.5', '3:8de11595ff6ac63a0a475960fe2b9f56', 14);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-15::Emiliano Conde::(Checksum: 3:29be1c001eab2651732e5110cf2f91fb)
CREATE TABLE currency_entity_map (currency_id INT, entity_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-15', '2.0.5', '3:29be1c001eab2651732e5110cf2f91fb', 15);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-16::Emiliano Conde::(Checksum: 3:bdefa07e71d75a44e99ba6e48f4759b9)
CREATE TABLE currency_exchange (id INT NOT NULL, entity_id INT, currency_id INT, rate NUMERIC(22,10) NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, optlock INT NOT NULL, valid_since DATE DEFAULT '1970-01-01' NOT NULL, CONSTRAINT currency_exchange_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-16', '2.0.5', '3:bdefa07e71d75a44e99ba6e48f4759b9', 16);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-17::Emiliano Conde::(Checksum: 3:1fc21a3fabd75ceec470ddb352cccde9)
CREATE TABLE customer (id INT NOT NULL, user_id INT, partner_id INT, referral_fee_paid INT, invoice_delivery_method_id INT NOT NULL, notes VARCHAR(1000), auto_payment_type INT, due_date_unit_id INT, due_date_value INT, df_fm INT, parent_id INT, is_parent INT, exclude_aging INT DEFAULT 0 NOT NULL, invoice_child INT, current_order_id INT, optlock INT NOT NULL, balance_type INT NOT NULL, dynamic_balance NUMERIC(22,10), credit_limit NUMERIC(22,10), auto_recharge NUMERIC(22,10), use_parent_pricing BOOLEAN NOT NULL, CONSTRAINT customer_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-17', '2.0.5', '3:1fc21a3fabd75ceec470ddb352cccde9', 17);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-18::Emiliano Conde::(Checksum: 3:b70f842a159d1a3d57a2aaf569aaf0bc)
CREATE TABLE customer_meta_field_map (customer_id INT NOT NULL, meta_field_value_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-18', '2.0.5', '3:b70f842a159d1a3d57a2aaf569aaf0bc', 18);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-19::Emiliano Conde::(Checksum: 3:85cab7e11717b5288f5bd7b0d71f530c)
CREATE TABLE customer_price (plan_item_id INT NOT NULL, user_id INT NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-19', '2.0.5', '3:85cab7e11717b5288f5bd7b0d71f530c', 19);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-20::Emiliano Conde::(Checksum: 3:aaba85d3d572bb5e9ab71f71d9c9f442)
CREATE TABLE entity (id INT NOT NULL, external_id VARCHAR(20), description VARCHAR(100) NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, language_id INT NOT NULL, currency_id INT NOT NULL, optlock INT NOT NULL, CONSTRAINT entity_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-20', '2.0.5', '3:aaba85d3d572bb5e9ab71f71d9c9f442', 20);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-21::Emiliano Conde::(Checksum: 3:63fe6c3fa46633b1b87f78a00046dbb1)
CREATE TABLE entity_delivery_method_map (method_id INT, entity_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-21', '2.0.5', '3:63fe6c3fa46633b1b87f78a00046dbb1', 21);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-22::Emiliano Conde::(Checksum: 3:c8b0895a0664de435a200437280573b8)
CREATE TABLE entity_payment_method_map (entity_id INT, payment_method_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-22', '2.0.5', '3:c8b0895a0664de435a200437280573b8', 22);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-23::Emiliano Conde::(Checksum: 3:bb0cd6312da79bee3852460c63d1d16d)
CREATE TABLE entity_report_map (report_id INT NOT NULL, entity_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-23', '2.0.5', '3:bb0cd6312da79bee3852460c63d1d16d', 23);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-24::Emiliano Conde::(Checksum: 3:dfc8d798f653c487abc7df8f8a548996)
CREATE TABLE enumeration (id INT NOT NULL, entity_id INT NOT NULL, name VARCHAR(50) NOT NULL, optlock INT NOT NULL, CONSTRAINT enumeration_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-24', '2.0.5', '3:dfc8d798f653c487abc7df8f8a548996', 24);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-25::Emiliano Conde::(Checksum: 3:87fd317d006863eda0e39a3bae67398a)
CREATE TABLE enumeration_values (id INT NOT NULL, enumeration_id INT NOT NULL, value VARCHAR(50) NOT NULL, optlock INT NOT NULL, CONSTRAINT enumeration_values_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-25', '2.0.5', '3:87fd317d006863eda0e39a3bae67398a', 25);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-26::Emiliano Conde::(Checksum: 3:9c24aa3c98cca5b049094a72fb349c49)
CREATE TABLE event_log (id INT NOT NULL, entity_id INT, user_id INT, table_id INT, foreign_id INT NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, level_field INT NOT NULL, module_id INT NOT NULL, message_id INT NOT NULL, old_num INT, old_str VARCHAR(1000), old_date TIMESTAMP WITH TIME ZONE, optlock INT NOT NULL, affected_user_id INT, CONSTRAINT event_log_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-26', '2.0.5', '3:9c24aa3c98cca5b049094a72fb349c49', 26);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-27::Emiliano Conde::(Checksum: 3:19dadb4f7cbc41339e8794de238fc8b8)
CREATE TABLE event_log_message (id INT NOT NULL, CONSTRAINT event_log_message_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-27', '2.0.5', '3:19dadb4f7cbc41339e8794de238fc8b8', 27);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-28::Emiliano Conde::(Checksum: 3:81b2fedc300c362e461ce9cd477efb77)
CREATE TABLE event_log_module (id INT NOT NULL, CONSTRAINT event_log_module_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-28', '2.0.5', '3:81b2fedc300c362e461ce9cd477efb77', 28);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-29::Emiliano Conde::(Checksum: 3:4cf065414588cb0d3491c30ff38e961f)
CREATE TABLE filter (id INT NOT NULL, filter_set_id INT NOT NULL, type VARCHAR(255) NOT NULL, constraint_type VARCHAR(255) NOT NULL, field VARCHAR(255) NOT NULL, template VARCHAR(255) NOT NULL, visible BOOLEAN NOT NULL, integer_value INT, string_value VARCHAR(255), start_date_value TIMESTAMP WITH TIME ZONE, end_date_value TIMESTAMP WITH TIME ZONE, "version" INT NOT NULL, boolean_value BOOLEAN, decimal_value NUMERIC(22,10), decimal_high_value NUMERIC(22,10), CONSTRAINT filter_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-29', '2.0.5', '3:4cf065414588cb0d3491c30ff38e961f', 29);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-30::Emiliano Conde::(Checksum: 3:aa18f8b1f6cd84e38b3ac7de4758b00e)
CREATE TABLE filter_set (id INT NOT NULL, name VARCHAR(255) NOT NULL, user_id INT NOT NULL, "version" INT NOT NULL, CONSTRAINT filter_set_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-30', '2.0.5', '3:aa18f8b1f6cd84e38b3ac7de4758b00e', 30);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-31::Emiliano Conde::(Checksum: 3:071f231e5309df4a4e4893eca9ef61cb)
CREATE TABLE filter_set_filter (filter_set_filters_id INT, filter_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-31', '2.0.5', '3:071f231e5309df4a4e4893eca9ef61cb', 31);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-32::Emiliano Conde::(Checksum: 3:25d3dae54944511d5ec57434f649d498)
CREATE TABLE generic_status (id INT NOT NULL, dtype VARCHAR(50) NOT NULL, status_value INT NOT NULL, can_login INT, CONSTRAINT generic_status_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-32', '2.0.5', '3:25d3dae54944511d5ec57434f649d498', 32);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-33::Emiliano Conde::(Checksum: 3:5817bbb216dc6fd92d0c53bf717e6139)
CREATE TABLE generic_status_type (id VARCHAR(50) NOT NULL, CONSTRAINT generic_status_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-33', '2.0.5', '3:5817bbb216dc6fd92d0c53bf717e6139', 33);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-34::Emiliano Conde::(Checksum: 3:cf1c7a6a7074cabcc12704563c790ac8)
CREATE TABLE international_description (table_id INT NOT NULL, foreign_id INT NOT NULL, psudo_column VARCHAR(20) NOT NULL, language_id INT NOT NULL, content VARCHAR(4000) NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-34', '2.0.5', '3:cf1c7a6a7074cabcc12704563c790ac8', 34);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-35::Emiliano Conde::(Checksum: 3:f87f8d65ffb8dfcdbf137b922e49e611)
CREATE TABLE invoice (id INT NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, billing_process_id INT, user_id INT, delegated_invoice_id INT, due_date DATE NOT NULL, total NUMERIC(22,10) NOT NULL, payment_attempts INT DEFAULT 0 NOT NULL, status_id INT DEFAULT 1 NOT NULL, balance NUMERIC(22,10), carried_balance NUMERIC(22,10) NOT NULL, in_process_payment INT DEFAULT 1 NOT NULL, is_review INT NOT NULL, currency_id INT NOT NULL, deleted INT DEFAULT 0 NOT NULL, paper_invoice_batch_id INT, customer_notes VARCHAR(1000), public_number VARCHAR(40), last_reminder DATE, overdue_step INT, create_timestamp TIMESTAMP WITH TIME ZONE NOT NULL, optlock INT NOT NULL, CONSTRAINT invoice_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-35', '2.0.5', '3:f87f8d65ffb8dfcdbf137b922e49e611', 35);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-36::Emiliano Conde::(Checksum: 3:d10ae137a7080c63a229de563450e6c4)
CREATE TABLE invoice_delivery_method (id INT NOT NULL, CONSTRAINT invoice_delivery_method_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-36', '2.0.5', '3:d10ae137a7080c63a229de563450e6c4', 36);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-37::Emiliano Conde::(Checksum: 3:f4f5fac951c53bd7c7f64035249a685a)
CREATE TABLE invoice_line (id INT NOT NULL, invoice_id INT, type_id INT, amount NUMERIC(22,10) NOT NULL, quantity NUMERIC(22,10), price NUMERIC(22,10), deleted INT DEFAULT 0 NOT NULL, item_id INT, description VARCHAR(1000), source_user_id INT, is_percentage INT DEFAULT 0 NOT NULL, optlock INT NOT NULL, CONSTRAINT invoice_line_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-37', '2.0.5', '3:f4f5fac951c53bd7c7f64035249a685a', 37);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-38::Emiliano Conde::(Checksum: 3:a1f1a3c56fc2e5db5e40ca33ed91af5e)
CREATE TABLE invoice_line_type (id INT NOT NULL, description VARCHAR(50) NOT NULL, order_position INT NOT NULL, CONSTRAINT invoice_line_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-38', '2.0.5', '3:a1f1a3c56fc2e5db5e40ca33ed91af5e', 38);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-39::Emiliano Conde::(Checksum: 3:3f117b32bc53c16c8c0e3c2611dae9c6)
CREATE TABLE invoice_meta_field_map (invoice_id INT NOT NULL, meta_field_value_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-39', '2.0.5', '3:3f117b32bc53c16c8c0e3c2611dae9c6', 39);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-40::Emiliano Conde::(Checksum: 3:9f7995d199d0a2481e03b76735210da5)
CREATE TABLE item (id INT NOT NULL, internal_number VARCHAR(50), entity_id INT, percentage NUMERIC(22,10), deleted INT DEFAULT 0 NOT NULL, has_decimals INT DEFAULT 0 NOT NULL, optlock INT NOT NULL, gl_code VARCHAR(50), CONSTRAINT item_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-40', '2.0.5', '3:9f7995d199d0a2481e03b76735210da5', 40);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-41::Emiliano Conde::(Checksum: 3:260a02edc37a7e8599153f3486fad042)
CREATE TABLE item_meta_field_map (item_id INT NOT NULL, meta_field_value_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-41', '2.0.5', '3:260a02edc37a7e8599153f3486fad042', 41);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-42::Emiliano Conde::(Checksum: 3:0b63449b721d6022e196e05a5a2b84ac)
CREATE TABLE item_price_timeline (item_id INT NOT NULL, price_model_id INT NOT NULL, start_date DATE NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-42', '2.0.5', '3:0b63449b721d6022e196e05a5a2b84ac', 42);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-43::Emiliano Conde::(Checksum: 3:43c35c8ae9cd2be200ce87089fb55b8e)
CREATE TABLE item_type (id INT NOT NULL, entity_id INT NOT NULL, description VARCHAR(100), order_line_type_id INT NOT NULL, optlock INT NOT NULL, internal BOOLEAN NOT NULL, CONSTRAINT item_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-43', '2.0.5', '3:43c35c8ae9cd2be200ce87089fb55b8e', 43);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-44::Emiliano Conde::(Checksum: 3:eb43e169a44716c8d5f117d5192144e5)
CREATE TABLE item_type_exclude_map (item_id INT NOT NULL, type_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-44', '2.0.5', '3:eb43e169a44716c8d5f117d5192144e5', 44);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-45::Emiliano Conde::(Checksum: 3:2b49257b83c3bad183f97ac0911949e0)
CREATE TABLE item_type_map (item_id INT, type_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-45', '2.0.5', '3:2b49257b83c3bad183f97ac0911949e0', 45);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-46::Emiliano Conde::(Checksum: 3:3f8e6de1ab2e63517dfe331b3331ace5)
CREATE TABLE jbilling_seqs (name VARCHAR(255), next_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-46', '2.0.5', '3:3f8e6de1ab2e63517dfe331b3331ace5', 46);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-47::Emiliano Conde::(Checksum: 3:78f2c53f455f067accdc418138c7914f)
CREATE TABLE jbilling_table (id INT NOT NULL, name VARCHAR(50) NOT NULL, CONSTRAINT jbilling_table_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-47', '2.0.5', '3:78f2c53f455f067accdc418138c7914f', 47);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-48::Emiliano Conde::(Checksum: 3:dd52dea64df9a983d0e8d9d72869c991)
CREATE TABLE language (id INT NOT NULL, code VARCHAR(2) NOT NULL, description VARCHAR(50) NOT NULL, CONSTRAINT language_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-48', '2.0.5', '3:dd52dea64df9a983d0e8d9d72869c991', 48);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-49::Emiliano Conde::(Checksum: 3:c06ade2dbf43b1f650150e61ea4e050d)
CREATE TABLE list_meta_field_values (meta_field_value_id INT NOT NULL, list_value VARCHAR(255));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-49', '2.0.5', '3:c06ade2dbf43b1f650150e61ea4e050d', 49);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-50::Emiliano Conde::(Checksum: 3:0b317492fd25c5769c3807350bc1f9e2)
CREATE TABLE mediation_cfg (id INT NOT NULL, entity_id INT NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, name VARCHAR(50) NOT NULL, order_value INT NOT NULL, pluggable_task_id INT NOT NULL, optlock INT NOT NULL, CONSTRAINT mediation_cfg_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-50', '2.0.5', '3:0b317492fd25c5769c3807350bc1f9e2', 50);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-51::Emiliano Conde::(Checksum: 3:44f27a94ae47fd3483c0e82ce8025557)
CREATE TABLE mediation_errors (accountcode VARCHAR(50) NOT NULL, src VARCHAR(50), dst VARCHAR(50), dcontext VARCHAR(50), clid VARCHAR(50), channel VARCHAR(50), dstchannel VARCHAR(50), lastapp VARCHAR(50), lastdata VARCHAR(50), start_time TIMESTAMP WITH TIME ZONE, answer TIMESTAMP WITH TIME ZONE, end_time TIMESTAMP WITH TIME ZONE, duration INT, billsec INT, disposition VARCHAR(50), amaflags VARCHAR(50), userfield VARCHAR(50), error_message VARCHAR(200), should_retry BOOLEAN, CONSTRAINT mediation_errors_pkey PRIMARY KEY (accountcode));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-51', '2.0.5', '3:44f27a94ae47fd3483c0e82ce8025557', 51);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-52::Emiliano Conde::(Checksum: 3:667ad5bec78567b204d98820b77bbca2)
CREATE TABLE mediation_order_map (mediation_process_id INT NOT NULL, order_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-52', '2.0.5', '3:667ad5bec78567b204d98820b77bbca2', 52);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-53::Emiliano Conde::(Checksum: 3:6419b8c4b21a389acc596bbc2fd701e6)
CREATE TABLE mediation_process (id INT NOT NULL, configuration_id INT NOT NULL, start_datetime TIMESTAMP WITH TIME ZONE NOT NULL, end_datetime TIMESTAMP WITH TIME ZONE, orders_affected INT NOT NULL, optlock INT NOT NULL, CONSTRAINT mediation_process_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-53', '2.0.5', '3:6419b8c4b21a389acc596bbc2fd701e6', 53);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-54::Emiliano Conde::(Checksum: 3:f1b15f90b965194bbd3f6cd84982b044)
CREATE TABLE mediation_record (id_key VARCHAR(100) NOT NULL, start_datetime TIMESTAMP WITH TIME ZONE NOT NULL, mediation_process_id INT, optlock INT NOT NULL, status_id INT NOT NULL, id INT NOT NULL, CONSTRAINT mediation_record_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-54', '2.0.5', '3:f1b15f90b965194bbd3f6cd84982b044', 54);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-55::Emiliano Conde::(Checksum: 3:18652d480389ee254762e972d5e40356)
CREATE TABLE mediation_record_line (id INT NOT NULL, order_line_id INT NOT NULL, event_date TIMESTAMP WITH TIME ZONE NOT NULL, amount NUMERIC(22,10) NOT NULL, quantity NUMERIC(22,10) NOT NULL, description VARCHAR(200), optlock INT NOT NULL, mediation_record_id INT NOT NULL, CONSTRAINT mediation_record_line_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-55', '2.0.5', '3:18652d480389ee254762e972d5e40356', 55);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-56::Emiliano Conde::(Checksum: 3:3a3ad69e2159ec8b75d22dd47909c2ee)
CREATE TABLE meta_field_name (id INT NOT NULL, name VARCHAR(100), entity_type VARCHAR(25) NOT NULL, data_type VARCHAR(25) NOT NULL, is_disabled BOOLEAN, is_mandatory BOOLEAN, display_order INT, default_value_id INT, optlock INT NOT NULL, entity_id INT DEFAULT 1, CONSTRAINT meta_field_name_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-56', '2.0.5', '3:3a3ad69e2159ec8b75d22dd47909c2ee', 56);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-57::Emiliano Conde::(Checksum: 3:9ea9a904bbe7e306537bf5bd79cd7f21)
CREATE TABLE meta_field_value (id INT NOT NULL, meta_field_name_id INT NOT NULL, dtype VARCHAR(10) NOT NULL, boolean_value BOOLEAN, date_value TIMESTAMP WITH TIME ZONE, decimal_value NUMERIC(22,10), integer_value INT, string_value VARCHAR(1000));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-57', '2.0.5', '3:9ea9a904bbe7e306537bf5bd79cd7f21', 57);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-58::Emiliano Conde::(Checksum: 3:92523b68b7b4436c1aefc31031e45560)
CREATE TABLE notification_category (id INT NOT NULL, CONSTRAINT notification_category_pk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-58', '2.0.5', '3:92523b68b7b4436c1aefc31031e45560', 58);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-59::Emiliano Conde::(Checksum: 3:0446ce732f6e25ba0eea15c735d637e8)
CREATE TABLE notification_message (id INT NOT NULL, type_id INT, entity_id INT NOT NULL, language_id INT NOT NULL, use_flag INT DEFAULT 1 NOT NULL, optlock INT NOT NULL, CONSTRAINT notifictn_msg_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-59', '2.0.5', '3:0446ce732f6e25ba0eea15c735d637e8', 59);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-60::Emiliano Conde::(Checksum: 3:7745e68e3208bb864ed8cc90090cead7)
CREATE TABLE notification_message_arch (id INT NOT NULL, type_id INT, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, user_id INT, result_message VARCHAR(200), optlock INT NOT NULL, CONSTRAINT notifictn_msg_arch_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-60', '2.0.5', '3:7745e68e3208bb864ed8cc90090cead7', 60);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-61::Emiliano Conde::(Checksum: 3:09405d6a904b56318ac3464e52750800)
CREATE TABLE notification_message_arch_line (id INT NOT NULL, message_archive_id INT, section INT NOT NULL, content VARCHAR(1000) NOT NULL, optlock INT NOT NULL, CONSTRAINT notifictn_msg_arch_line_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-61', '2.0.5', '3:09405d6a904b56318ac3464e52750800', 61);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-62::Emiliano Conde::(Checksum: 3:a614459dad50881980cc72feddbd7821)
CREATE TABLE notification_message_line (id INT NOT NULL, message_section_id INT, content VARCHAR(1000) NOT NULL, optlock INT NOT NULL, CONSTRAINT notifictn_msg_line_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-62', '2.0.5', '3:a614459dad50881980cc72feddbd7821', 62);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-63::Emiliano Conde::(Checksum: 3:eb87b9f99f8ee815417b7914b53a2ebe)
CREATE TABLE notification_message_section (id INT NOT NULL, message_id INT, section INT, optlock INT NOT NULL, CONSTRAINT notifictn_msg_section_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-63', '2.0.5', '3:eb87b9f99f8ee815417b7914b53a2ebe', 63);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-64::Emiliano Conde::(Checksum: 3:bcd9f1b3472d1e125863c0a48b687b09)
CREATE TABLE notification_message_type (id INT NOT NULL, optlock INT NOT NULL, category_id INT, CONSTRAINT notifictn_msg_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-64', '2.0.5', '3:bcd9f1b3472d1e125863c0a48b687b09', 64);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-65::Emiliano Conde::(Checksum: 3:037ecda8ced792a9e4f57dce8cf8003b)
CREATE TABLE order_billing_type (id INT NOT NULL, CONSTRAINT order_billing_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-65', '2.0.5', '3:037ecda8ced792a9e4f57dce8cf8003b', 65);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-66::Emiliano Conde::(Checksum: 3:a16247e56fe6a6d7e1eab58d2cb47b5a)
CREATE TABLE order_line (id INT NOT NULL, order_id INT, item_id INT, type_id INT, amount NUMERIC(22,10) NOT NULL, quantity NUMERIC(22,10), price NUMERIC(22,10), item_price INT, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, deleted INT DEFAULT 0 NOT NULL, description VARCHAR(1000), provisioning_status INT, provisioning_request_id VARCHAR(50), optlock INT NOT NULL, use_item BOOLEAN NOT NULL, CONSTRAINT order_line_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-66', '2.0.5', '3:a16247e56fe6a6d7e1eab58d2cb47b5a', 66);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-67::Emiliano Conde::(Checksum: 3:aacc643466d1a698c9aa2ea6f4f77dd2)
CREATE TABLE order_line_type (id INT NOT NULL, editable INT NOT NULL, CONSTRAINT order_line_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-67', '2.0.5', '3:aacc643466d1a698c9aa2ea6f4f77dd2', 67);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-68::Emiliano Conde::(Checksum: 3:4e038cbe59d25b201deb700d77c9a1fa)
CREATE TABLE order_meta_field_map (order_id INT NOT NULL, meta_field_value_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-68', '2.0.5', '3:4e038cbe59d25b201deb700d77c9a1fa', 68);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-69::Emiliano Conde::(Checksum: 3:394bd9eadfadf19c6d11cdb84960aec4)
CREATE TABLE order_period (id INT NOT NULL, entity_id INT, value INT, unit_id INT, optlock INT NOT NULL, CONSTRAINT order_period_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-69', '2.0.5', '3:394bd9eadfadf19c6d11cdb84960aec4', 69);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-70::Emiliano Conde::(Checksum: 3:d397324ce47bc6a3a1dba5141527bef6)
CREATE TABLE order_process (id INT NOT NULL, order_id INT, invoice_id INT, billing_process_id INT, periods_included INT, period_start DATE, period_end DATE, is_review INT NOT NULL, origin INT, optlock INT NOT NULL, CONSTRAINT order_process_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-70', '2.0.5', '3:d397324ce47bc6a3a1dba5141527bef6', 70);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-71::Emiliano Conde::(Checksum: 3:d004537f815a71762d54a225c10b0815)
CREATE TABLE paper_invoice_batch (id INT NOT NULL, total_invoices INT NOT NULL, delivery_date DATE, is_self_managed INT NOT NULL, optlock INT NOT NULL, CONSTRAINT paper_invoice_batch_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-71', '2.0.5', '3:d004537f815a71762d54a225c10b0815', 71);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-72::Emiliano Conde::(Checksum: 3:a44fa8aa96bdeffdbfea7e663cfe81d7)
CREATE TABLE partner (id INT NOT NULL, user_id INT, balance NUMERIC(22,10) NOT NULL, total_payments NUMERIC(22,10) NOT NULL, total_refunds NUMERIC(22,10) NOT NULL, total_payouts NUMERIC(22,10) NOT NULL, percentage_rate NUMERIC(22,10), referral_fee NUMERIC(22,10), fee_currency_id INT, one_time INT NOT NULL, period_unit_id INT NOT NULL, period_value INT NOT NULL, next_payout_date DATE NOT NULL, due_payout NUMERIC(22,10), automatic_process INT NOT NULL, related_clerk INT, optlock INT NOT NULL, CONSTRAINT partner_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-72', '2.0.5', '3:a44fa8aa96bdeffdbfea7e663cfe81d7', 72);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-73::Emiliano Conde::(Checksum: 3:b5c0298f92aa91cfc3a5a5984887ae24)
CREATE TABLE partner_meta_field_map (partner_id INT NOT NULL, meta_field_value_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-73', '2.0.5', '3:b5c0298f92aa91cfc3a5a5984887ae24', 73);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-74::Emiliano Conde::(Checksum: 3:a7fb3d6049e40bfb02749d23ead005ce)
CREATE TABLE partner_payout (id INT NOT NULL, starting_date DATE NOT NULL, ending_date DATE NOT NULL, payments_amount NUMERIC(22,10) NOT NULL, refunds_amount NUMERIC(22,10) NOT NULL, balance_left NUMERIC(22,10) NOT NULL, payment_id INT, partner_id INT, optlock INT NOT NULL, CONSTRAINT partner_payout_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-74', '2.0.5', '3:a7fb3d6049e40bfb02749d23ead005ce', 74);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-75::Emiliano Conde::(Checksum: 3:1c10aa7b89c7ae4529108b60b697fe0c)
CREATE TABLE partner_range (id INT NOT NULL, partner_id INT, percentage_rate NUMERIC(22,10), referral_fee NUMERIC(22,10), range_from INT NOT NULL, range_to INT NOT NULL, optlock INT NOT NULL, CONSTRAINT partner_range_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-75', '2.0.5', '3:1c10aa7b89c7ae4529108b60b697fe0c', 75);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-76::Emiliano Conde::(Checksum: 3:1b2a6bad533cc36f4dab198377193a91)
CREATE TABLE payment (id INT NOT NULL, user_id INT, attempt INT, result_id INT, amount NUMERIC(22,10) NOT NULL, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, update_datetime TIMESTAMP WITH TIME ZONE, payment_date DATE, method_id INT NOT NULL, credit_card_id INT, deleted INT DEFAULT 0 NOT NULL, is_refund INT DEFAULT 0 NOT NULL, is_preauth INT DEFAULT 0 NOT NULL, payment_id INT, currency_id INT NOT NULL, payout_id INT, ach_id INT, balance NUMERIC(22,10), optlock INT NOT NULL, payment_period INT, payment_notes VARCHAR(500), CONSTRAINT payment_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-76', '2.0.5', '3:1b2a6bad533cc36f4dab198377193a91', 76);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-77::Emiliano Conde::(Checksum: 3:27b7b088a6dff1eb5bf7ec497baae017)
CREATE TABLE payment_authorization (id INT NOT NULL, payment_id INT, processor VARCHAR(40) NOT NULL, code1 VARCHAR(40) NOT NULL, code2 VARCHAR(40), code3 VARCHAR(40), approval_code VARCHAR(20), avs VARCHAR(20), transaction_id VARCHAR(40), md5 VARCHAR(100), card_code VARCHAR(100), create_datetime DATE NOT NULL, response_message VARCHAR(200), optlock INT NOT NULL, CONSTRAINT payment_authorization_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-77', '2.0.5', '3:27b7b088a6dff1eb5bf7ec497baae017', 77);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-78::Emiliano Conde::(Checksum: 3:48ea020feafff3cf4c69b0817940f55a)
CREATE TABLE payment_info_cheque (id INT NOT NULL, payment_id INT, bank VARCHAR(50), cheque_number VARCHAR(50), cheque_date DATE, optlock INT NOT NULL, CONSTRAINT payment_info_cheque_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-78', '2.0.5', '3:48ea020feafff3cf4c69b0817940f55a', 78);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-79::Emiliano Conde::(Checksum: 3:e7a907970aa98a95fa48d026e671b7e8)
CREATE TABLE payment_invoice (id INT NOT NULL, payment_id INT, invoice_id INT, amount NUMERIC(22,10), create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, optlock INT NOT NULL, CONSTRAINT payment_invoice_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-79', '2.0.5', '3:e7a907970aa98a95fa48d026e671b7e8', 79);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-80::Emiliano Conde::(Checksum: 3:26a71c52cf002cc4183488f80544357a)
CREATE TABLE payment_meta_field_map (payment_id INT NOT NULL, meta_field_value_id INT NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-80', '2.0.5', '3:26a71c52cf002cc4183488f80544357a', 80);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-81::Emiliano Conde::(Checksum: 3:8cf4c000ba69d6c0645d342bc1a2e1f3)
CREATE TABLE payment_method (id INT NOT NULL, CONSTRAINT payment_method_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-81', '2.0.5', '3:8cf4c000ba69d6c0645d342bc1a2e1f3', 81);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-82::Emiliano Conde::(Checksum: 3:31c53d745c9ae834d3a5982cb0ecc36e)
CREATE TABLE payment_result (id INT NOT NULL, CONSTRAINT payment_result_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-82', '2.0.5', '3:31c53d745c9ae834d3a5982cb0ecc36e', 82);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-83::Emiliano Conde::(Checksum: 3:6ec06a1c6bfa20b890f5daa375b0e1b2)
CREATE TABLE period_unit (id INT NOT NULL, CONSTRAINT period_unit_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-83', '2.0.5', '3:6ec06a1c6bfa20b890f5daa375b0e1b2', 83);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-84::Emiliano Conde::(Checksum: 3:f283d52bac6269950fc8c5221b9cc6f3)
CREATE TABLE permission (id INT NOT NULL, type_id INT NOT NULL, foreign_id INT, CONSTRAINT permission_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-84', '2.0.5', '3:f283d52bac6269950fc8c5221b9cc6f3', 84);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-85::Emiliano Conde::(Checksum: 3:e757ef4108739dee3752a5d78ac95581)
CREATE TABLE permission_role_map (permission_id INT, role_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-85', '2.0.5', '3:e757ef4108739dee3752a5d78ac95581', 85);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-86::Emiliano Conde::(Checksum: 3:d3b0ef35e6a199026d2564e5bcf6ab93)
CREATE TABLE permission_type (id INT NOT NULL, description VARCHAR(30) NOT NULL, CONSTRAINT permission_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-86', '2.0.5', '3:d3b0ef35e6a199026d2564e5bcf6ab93', 86);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-87::Emiliano Conde::(Checksum: 3:c13b509307c398cc485605151f9eeb5f)
CREATE TABLE permission_user (permission_id INT, user_id INT, is_grant INT NOT NULL, id INT NOT NULL, CONSTRAINT permission_user_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-87', '2.0.5', '3:c13b509307c398cc485605151f9eeb5f', 87);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-88::Emiliano Conde::(Checksum: 3:f1cef62b77417dfb5ff0d729867b42f3)
CREATE TABLE plan (id INT NOT NULL, item_id INT NOT NULL, description VARCHAR(255), period_id INT NOT NULL, CONSTRAINT plan_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-88', '2.0.5', '3:f1cef62b77417dfb5ff0d729867b42f3', 88);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-89::Emiliano Conde::(Checksum: 3:e6b0d4b4ccc87d0d2ea523d9b012389d)
CREATE TABLE plan_item (id INT NOT NULL, plan_id INT, item_id INT NOT NULL, precedence INT NOT NULL, plan_item_bundle_id INT, CONSTRAINT plan_item_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-89', '2.0.5', '3:e6b0d4b4ccc87d0d2ea523d9b012389d', 89);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-90::Emiliano Conde::(Checksum: 3:554d41019d495e10bf22121cf84448b2)
CREATE TABLE plan_item_bundle (id INT NOT NULL, quantity NUMERIC(22,10) NOT NULL, period_id INT NOT NULL, target_customer VARCHAR(20) NOT NULL, add_if_exists BOOLEAN NOT NULL, CONSTRAINT plan_item_bundle_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-90', '2.0.5', '3:554d41019d495e10bf22121cf84448b2', 90);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-91::Emiliano Conde::(Checksum: 3:340bd59bcf1a21cc62e3e9c03127d9cc)
CREATE TABLE plan_item_price_timeline (plan_item_id INT NOT NULL, price_model_id INT NOT NULL, start_date DATE NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-91', '2.0.5', '3:340bd59bcf1a21cc62e3e9c03127d9cc', 91);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-92::Emiliano Conde::(Checksum: 3:830d3dc7c0504af3f8b3a82a1a868669)
CREATE TABLE pluggable_task (id INT NOT NULL, entity_id INT NOT NULL, type_id INT, processing_order INT NOT NULL, optlock INT NOT NULL, notes VARCHAR(1000), CONSTRAINT pluggable_task_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-92', '2.0.5', '3:830d3dc7c0504af3f8b3a82a1a868669', 92);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-93::Emiliano Conde::(Checksum: 3:deb3d2f60cff688a3a5bf976128ee92a)
CREATE TABLE pluggable_task_parameter (id INT NOT NULL, task_id INT, name VARCHAR(50) NOT NULL, int_value INT, str_value VARCHAR(500), float_value NUMERIC(22,10), optlock INT NOT NULL, CONSTRAINT pluggable_task_parameter_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-93', '2.0.5', '3:deb3d2f60cff688a3a5bf976128ee92a', 93);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-94::Emiliano Conde::(Checksum: 3:68dfdb546e76551fc5a199986e17288a)
CREATE TABLE pluggable_task_type (id INT NOT NULL, category_id INT NOT NULL, class_name VARCHAR(200) NOT NULL, min_parameters INT NOT NULL, CONSTRAINT pluggable_task_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-94', '2.0.5', '3:68dfdb546e76551fc5a199986e17288a', 94);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-95::Emiliano Conde::(Checksum: 3:2ee2b68dadc81b63a98c6782029fd840)
CREATE TABLE pluggable_task_type_category (id INT NOT NULL, interface_name VARCHAR(200) NOT NULL, CONSTRAINT pluggable_task_type_cat_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-95', '2.0.5', '3:2ee2b68dadc81b63a98c6782029fd840', 95);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-96::Emiliano Conde::(Checksum: 3:701eaf6644200e19c53f6d2035e8ac40)
CREATE TABLE preference (id INT NOT NULL, type_id INT, table_id INT NOT NULL, foreign_id INT NOT NULL, value VARCHAR(200), CONSTRAINT preference_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-96', '2.0.5', '3:701eaf6644200e19c53f6d2035e8ac40', 96);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-97::Emiliano Conde::(Checksum: 3:f35a5ce3237a6daa0d64cb5a43eb978d)
CREATE TABLE preference_type (id INT NOT NULL, def_value VARCHAR(200), CONSTRAINT preference_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-97', '2.0.5', '3:f35a5ce3237a6daa0d64cb5a43eb978d', 97);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-98::Emiliano Conde::(Checksum: 3:07331681165e6c9700e011738654b39e)
CREATE TABLE price_model (id INT NOT NULL, strategy_type VARCHAR(40) NOT NULL, rate NUMERIC(22,10), included_quantity INT, currency_id INT, next_model_id INT, CONSTRAINT price_model_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-98', '2.0.5', '3:07331681165e6c9700e011738654b39e', 98);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-99::Emiliano Conde::(Checksum: 3:d6937b3b4d0db6660e6b20984d4a879a)
CREATE TABLE price_model_attribute (price_model_id INT NOT NULL, attribute_name VARCHAR(255) NOT NULL, attribute_value VARCHAR(255));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-99', '2.0.5', '3:d6937b3b4d0db6660e6b20984d4a879a', 99);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-100::Emiliano Conde::(Checksum: 3:5a04786cdedeaedbd69323c599b8de4c)
CREATE TABLE process_run (id INT NOT NULL, process_id INT, run_date DATE NOT NULL, started TIMESTAMP WITH TIME ZONE NOT NULL, finished TIMESTAMP WITH TIME ZONE, payment_finished TIMESTAMP WITH TIME ZONE, invoices_generated INT, optlock INT NOT NULL, status_id INT NOT NULL, CONSTRAINT process_run_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-100', '2.0.5', '3:5a04786cdedeaedbd69323c599b8de4c', 100);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-101::Emiliano Conde::(Checksum: 3:563c50f2027e2067b086a631e281dcf1)
CREATE TABLE process_run_total (id INT NOT NULL, process_run_id INT, currency_id INT NOT NULL, total_invoiced NUMERIC(22,10), total_paid NUMERIC(22,10), total_not_paid NUMERIC(22,10), optlock INT NOT NULL, CONSTRAINT process_run_total_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-101', '2.0.5', '3:563c50f2027e2067b086a631e281dcf1', 101);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-102::Emiliano Conde::(Checksum: 3:740ee4edf3dd254f31365c3744f42472)
CREATE TABLE process_run_total_pm (id INT NOT NULL, process_run_total_id INT, payment_method_id INT, total NUMERIC(22,10) NOT NULL, optlock INT NOT NULL, CONSTRAINT process_run_total_pm_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-102', '2.0.5', '3:740ee4edf3dd254f31365c3744f42472', 102);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-103::Emiliano Conde::(Checksum: 3:c6edfaa6aec554700a58d13c21067077)
CREATE TABLE process_run_user (id INT NOT NULL, process_run_id INT NOT NULL, user_id INT NOT NULL, status INT NOT NULL, created TIMESTAMP WITH TIME ZONE NOT NULL, optlock INT NOT NULL, CONSTRAINT process_run_user_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-103', '2.0.5', '3:c6edfaa6aec554700a58d13c21067077', 103);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-104::Emiliano Conde::(Checksum: 3:3c79b35239807af00798ef05239ae6ab)
CREATE TABLE promotion (id INT NOT NULL, item_id INT, code VARCHAR(50) NOT NULL, notes VARCHAR(200), once INT NOT NULL, since DATE, until DATE, CONSTRAINT promotion_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-104', '2.0.5', '3:3c79b35239807af00798ef05239ae6ab', 104);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-105::Emiliano Conde::(Checksum: 3:5654b932bf19149b687bdd8f738e7e11)
CREATE TABLE promotion_user_map (user_id INT, promotion_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-105', '2.0.5', '3:5654b932bf19149b687bdd8f738e7e11', 105);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-106::Emiliano Conde::(Checksum: 3:f23a3a6a63604d660d26068ade95a61d)
CREATE TABLE purchase_order (id INT NOT NULL, user_id INT, period_id INT, billing_type_id INT NOT NULL, active_since DATE, active_until DATE, cycle_start DATE, create_datetime TIMESTAMP WITH TIME ZONE NOT NULL, next_billable_day DATE, created_by INT, status_id INT NOT NULL, currency_id INT NOT NULL, deleted INT DEFAULT 0 NOT NULL, "notify" INT, last_notified TIMESTAMP WITH TIME ZONE, notification_step INT, due_date_unit_id INT, due_date_value INT, df_fm INT, anticipate_periods INT, own_invoice INT, notes VARCHAR(200), notes_in_invoice INT, is_current INT, optlock INT NOT NULL, CONSTRAINT purchase_order_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-106', '2.0.5', '3:f23a3a6a63604d660d26068ade95a61d', 106);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-107::Emiliano Conde::(Checksum: 3:3e06accbf4c7c58a7cc57382ac671c9a)
CREATE TABLE rate_card (id INT NOT NULL, name VARCHAR(255) NOT NULL, table_name VARCHAR(255) NOT NULL, entity_id INT NOT NULL, CONSTRAINT rate_card_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-107', '2.0.5', '3:3e06accbf4c7c58a7cc57382ac671c9a', 107);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-108::Emiliano Conde::(Checksum: 3:1bd466f4fbbf9c8ff15063f0bedf2dbb)
CREATE TABLE recent_item (id INT NOT NULL, type VARCHAR(255) NOT NULL, object_id INT NOT NULL, user_id INT NOT NULL, "version" INT NOT NULL, CONSTRAINT recent_item_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-108', '2.0.5', '3:1bd466f4fbbf9c8ff15063f0bedf2dbb', 108);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-109::Emiliano Conde::(Checksum: 3:c72f3177109bf4d9b91e16ef6287e7c6)
CREATE TABLE report (id INT NOT NULL, type_id INT NOT NULL, name VARCHAR(255) NOT NULL, file_name VARCHAR(500) NOT NULL, optlock INT NOT NULL, CONSTRAINT report_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-109', '2.0.5', '3:c72f3177109bf4d9b91e16ef6287e7c6', 109);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-110::Emiliano Conde::(Checksum: 3:d9747bd8e0615b490e0471414cbe9a4a)
CREATE TABLE report_parameter (id INT NOT NULL, report_id INT NOT NULL, dtype VARCHAR(10) NOT NULL, name VARCHAR(255) NOT NULL, CONSTRAINT report_parameter_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-110', '2.0.5', '3:d9747bd8e0615b490e0471414cbe9a4a', 110);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-111::Emiliano Conde::(Checksum: 3:960bf1edbae8d73ab83d262114022b46)
CREATE TABLE report_type (id INT NOT NULL, name VARCHAR(255) NOT NULL, optlock INT NOT NULL, CONSTRAINT report_type_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-111', '2.0.5', '3:960bf1edbae8d73ab83d262114022b46', 111);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-112::Emiliano Conde::(Checksum: 3:677275f6b064915e186d8a125559bc52)
CREATE TABLE role (id INT NOT NULL, entity_id INT, role_type_id INT, CONSTRAINT role_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-112', '2.0.5', '3:677275f6b064915e186d8a125559bc52', 112);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-113::Emiliano Conde::(Checksum: 3:fa63bae43f3d2a52f2e68974dcdf6735)
CREATE TABLE shortcut (id INT NOT NULL, user_id INT NOT NULL, controller VARCHAR(255) NOT NULL, action VARCHAR(255), name VARCHAR(255), object_id INT, "version" INT NOT NULL, CONSTRAINT shortcut_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-113', '2.0.5', '3:fa63bae43f3d2a52f2e68974dcdf6735', 113);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-114::Emiliano Conde::(Checksum: 3:61516745ca6c886ddc4e3b487a503c86)
CREATE TABLE user_credit_card_map (user_id INT, credit_card_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-114', '2.0.5', '3:61516745ca6c886ddc4e3b487a503c86', 114);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-115::Emiliano Conde::(Checksum: 3:f76b7790e10b96c1fad61703fa959ee2)
CREATE TABLE user_role_map (user_id INT, role_id INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-115', '2.0.5', '3:f76b7790e10b96c1fad61703fa959ee2', 115);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-116::Emiliano Conde::(Checksum: 3:ec96ad42bc0db24b7cab6a03d3821579)
ALTER TABLE customer_price ADD CONSTRAINT customer_price_pkey PRIMARY KEY (plan_item_id, user_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Primary Key', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-116', '2.0.5', '3:ec96ad42bc0db24b7cab6a03d3821579', 116);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-117::Emiliano Conde::(Checksum: 3:a7aed4bdbee79d6489881f704d3186fa)
ALTER TABLE international_description ADD CONSTRAINT international_description_pkey PRIMARY KEY (table_id, foreign_id, psudo_column, language_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Primary Key', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-117', '2.0.5', '3:a7aed4bdbee79d6489881f704d3186fa', 117);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-118::Emiliano Conde::(Checksum: 3:4d1af0f8e4e95747c01460f81b9c6ce9)
ALTER TABLE item_price_timeline ADD CONSTRAINT item_price_timeline_pkey PRIMARY KEY (item_id, start_date);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Primary Key', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-118', '2.0.5', '3:4d1af0f8e4e95747c01460f81b9c6ce9', 118);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-119::Emiliano Conde::(Checksum: 3:bd0613180ba8732a9668d0248c4aa43d)
ALTER TABLE item_type_exclude_map ADD CONSTRAINT item_type_exclude_map_pkey PRIMARY KEY (item_id, type_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Primary Key', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-119', '2.0.5', '3:bd0613180ba8732a9668d0248c4aa43d', 119);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-120::Emiliano Conde::(Checksum: 3:9a19138dcd59484dfc2f5158b599fc2f)
ALTER TABLE plan_item_price_timeline ADD CONSTRAINT plan_item_price_timeline_pkey PRIMARY KEY (plan_item_id, start_date);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Primary Key', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-120', '2.0.5', '3:9a19138dcd59484dfc2f5158b599fc2f', 120);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-121::Emiliano Conde::(Checksum: 3:d6cd77f86ef26e2844225a84e20f7a24)
ALTER TABLE price_model_attribute ADD CONSTRAINT price_model_attribute_pkey PRIMARY KEY (price_model_id, attribute_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Primary Key', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-121', '2.0.5', '3:d6cd77f86ef26e2844225a84e20f7a24', 121);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-122::Emiliano Conde::(Checksum: 3:effaf8269dd3b75d28908a9a40d6017f)
ALTER TABLE item_price_timeline ADD CONSTRAINT itmprctimelnprc_model_id_key UNIQUE (price_model_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Unique Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-122', '2.0.5', '3:effaf8269dd3b75d28908a9a40d6017f', 122);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-123::Emiliano Conde::(Checksum: 3:a08f5e8b8ddaf1535e8607f1753dd8b8)
ALTER TABLE meta_field_value ADD CONSTRAINT meta_field_value_id_key UNIQUE (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Unique Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-123', '2.0.5', '3:a08f5e8b8ddaf1535e8607f1753dd8b8', 123);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-124::Emiliano Conde::(Checksum: 3:a4b52a42d1ce658721e2702d180ed638)
ALTER TABLE plan_item_price_timeline ADD CONSTRAINT plnitmprctimelnprc_mdl_key UNIQUE (price_model_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Unique Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-124', '2.0.5', '3:a4b52a42d1ce658721e2702d180ed638', 124);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-125::Emiliano Conde::(Checksum: 3:f7a074ca3d7fe98ffeafccf5d66e8150)
ALTER TABLE rate_card ADD CONSTRAINT rate_card_table_name_key UNIQUE (table_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Unique Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-125', '2.0.5', '3:f7a074ca3d7fe98ffeafccf5d66e8150', 125);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-275::Emiliano Conde::(Checksum: 3:994fe113170287422961bc1fb9d45507)
CREATE INDEX ix_base_user_un ON base_user(entity_id, user_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-275', '2.0.5', '3:994fe113170287422961bc1fb9d45507', 126);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-276::Emiliano Conde::(Checksum: 3:11acac1e05cef75c5e907544ea1baacd)
CREATE INDEX ix_blacklist_entity_type ON blacklist(entity_id, type);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-276', '2.0.5', '3:11acac1e05cef75c5e907544ea1baacd', 127);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-277::Emiliano Conde::(Checksum: 3:fdb78a392b0b104f8cb8d70fdb3a19c0)
CREATE INDEX ix_blacklist_user_type ON blacklist(user_id, type);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-277', '2.0.5', '3:fdb78a392b0b104f8cb8d70fdb3a19c0', 128);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-278::Emiliano Conde::(Checksum: 3:e305907bc0b4c1d35866d33840269052)
CREATE INDEX contact_i_del ON contact(deleted);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-278', '2.0.5', '3:e305907bc0b4c1d35866d33840269052', 129);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-279::Emiliano Conde::(Checksum: 3:9663b0e86f9a6d55df40b5b54eb642d2)
CREATE INDEX ix_contact_address ON contact(street_addres1, city, postal_code, street_addres2, state_province, country_code);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-279', '2.0.5', '3:9663b0e86f9a6d55df40b5b54eb642d2', 130);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-280::Emiliano Conde::(Checksum: 3:b9cf7c8e7cf46e7b535a4f53a9436747)
CREATE INDEX ix_contact_fname ON contact(first_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-280', '2.0.5', '3:b9cf7c8e7cf46e7b535a4f53a9436747', 131);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-281::Emiliano Conde::(Checksum: 3:62dcefdaf0bed060812fe52b7e50b83a)
CREATE INDEX ix_contact_fname_lname ON contact(first_name, last_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-281', '2.0.5', '3:62dcefdaf0bed060812fe52b7e50b83a', 132);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-282::Emiliano Conde::(Checksum: 3:db04e689be7296af4ad06cccf028b8c8)
CREATE INDEX ix_contact_lname ON contact(last_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-282', '2.0.5', '3:db04e689be7296af4ad06cccf028b8c8', 133);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-283::Emiliano Conde::(Checksum: 3:8bbfaf351cf5f3c4b603b03f91e9f276)
CREATE INDEX ix_contact_orgname ON contact(organization_name);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-283', '2.0.5', '3:8bbfaf351cf5f3c4b603b03f91e9f276', 134);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-284::Emiliano Conde::(Checksum: 3:5b7a28747a3669f600b59ac295780c23)
CREATE INDEX ix_contact_phone ON contact(phone_phone_number, phone_area_code, phone_country_code);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-284', '2.0.5', '3:5b7a28747a3669f600b59ac295780c23', 135);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-285::Emiliano Conde::(Checksum: 3:d1ad1d09cb2e9e15ecd0446d6a3d6634)
CREATE INDEX contact_map_i_3 ON contact_map(table_id, foreign_id, type_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-285', '2.0.5', '3:d1ad1d09cb2e9e15ecd0446d6a3d6634', 136);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-286::Emiliano Conde::(Checksum: 3:7a614fe0d45db7c2fbc40e8f21e92c09)
CREATE INDEX ix_cc_number ON credit_card(cc_number_plain);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-286', '2.0.5', '3:7a614fe0d45db7c2fbc40e8f21e92c09', 137);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-287::Emiliano Conde::(Checksum: 3:30568c1277b72661cef1a9c9f80ea584)
CREATE INDEX ix_cc_number_encrypted ON credit_card(cc_number);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-287', '2.0.5', '3:30568c1277b72661cef1a9c9f80ea584', 138);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-288::Emiliano Conde::(Checksum: 3:1f5c5c62cc17ee05830ddac3ab75b99e)
CREATE INDEX currency_entity_map_i_2 ON currency_entity_map(currency_id, entity_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-288', '2.0.5', '3:1f5c5c62cc17ee05830ddac3ab75b99e', 139);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-289::Emiliano Conde::(Checksum: 3:c835a6cdd01d4a8dec41c41bc74f0154)
CREATE INDEX ix_el_main ON event_log(module_id, message_id, create_datetime);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-289', '2.0.5', '3:c835a6cdd01d4a8dec41c41bc74f0154', 140);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-290::Emiliano Conde::(Checksum: 3:c1862737100ef9e2bd3c5430e20e7fcd)
CREATE INDEX international_description_i_2 ON international_description(table_id, foreign_id, language_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-290', '2.0.5', '3:c1862737100ef9e2bd3c5430e20e7fcd', 141);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-291::Emiliano Conde::(Checksum: 3:12e28798e75fafcf467fd4d20e163784)
CREATE INDEX ix_invoice_date ON invoice(create_datetime);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-291', '2.0.5', '3:12e28798e75fafcf467fd4d20e163784', 142);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-292::Emiliano Conde::(Checksum: 3:f1a6eb1379add9e57cef1ee542606bf2)
CREATE INDEX ix_invoice_due_date ON invoice(user_id, due_date);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-292', '2.0.5', '3:f1a6eb1379add9e57cef1ee542606bf2', 143);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-293::Emiliano Conde::(Checksum: 3:bdf18b2d1fea493dc90b6973056ad75b)
CREATE INDEX ix_invoice_number ON invoice(user_id, public_number);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-293', '2.0.5', '3:bdf18b2d1fea493dc90b6973056ad75b', 144);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-294::Emiliano Conde::(Checksum: 3:61e9ba92bafa79920fdea08eaed87beb)
CREATE INDEX ix_invoice_ts ON invoice(create_timestamp, user_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-294', '2.0.5', '3:61e9ba92bafa79920fdea08eaed87beb', 145);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-295::Emiliano Conde::(Checksum: 3:4d65a812b5ac84dbfadd4f14c997b7a6)
CREATE INDEX ix_invoice_user_id ON invoice(user_id, deleted);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-295', '2.0.5', '3:4d65a812b5ac84dbfadd4f14c997b7a6', 146);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-296::Emiliano Conde::(Checksum: 3:4730935dc2814d92df28dee2d0196550)
CREATE INDEX ix_item_ent ON item(entity_id, internal_number);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-296', '2.0.5', '3:4730935dc2814d92df28dee2d0196550', 147);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-297::Emiliano Conde::(Checksum: 3:54b654d2f867524384d138159f9d92ba)
CREATE INDEX mediation_record_i ON mediation_record(id_key, status_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-297', '2.0.5', '3:54b654d2f867524384d138159f9d92ba', 148);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-298::Emiliano Conde::(Checksum: 3:df729a792dda503f3c2c45c84c824a5b)
CREATE INDEX ix_order_process_in ON order_process(invoice_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-298', '2.0.5', '3:df729a792dda503f3c2c45c84c824a5b', 149);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-299::Emiliano Conde::(Checksum: 3:fa73f52b1cc7c2ee5d057ec9c290e275)
CREATE INDEX ix_uq_order_process_or_bp ON order_process(order_id, billing_process_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-299', '2.0.5', '3:fa73f52b1cc7c2ee5d057ec9c290e275', 150);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-300::Emiliano Conde::(Checksum: 3:5fe4c4b54f5453ff70e8e80233850e51)
CREATE INDEX ix_uq_order_process_or_in ON order_process(order_id, invoice_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-300', '2.0.5', '3:5fe4c4b54f5453ff70e8e80233850e51', 151);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-301::Emiliano Conde::(Checksum: 3:1f2ddf2a617233b6566483b0a71266ce)
CREATE INDEX partner_range_p ON partner_range(partner_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-301', '2.0.5', '3:1f2ddf2a617233b6566483b0a71266ce', 152);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-302::Emiliano Conde::(Checksum: 3:975a0e98177c1f0427833dfa38e858fd)
CREATE INDEX payment_i_2 ON payment(user_id, create_datetime);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-302', '2.0.5', '3:975a0e98177c1f0427833dfa38e858fd', 153);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-303::Emiliano Conde::(Checksum: 3:4ef5b799d8732781480c2b6a563e0032)
CREATE INDEX payment_i_3 ON payment(user_id, balance);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-303', '2.0.5', '3:4ef5b799d8732781480c2b6a563e0032', 154);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-304::Emiliano Conde::(Checksum: 3:ed0bfac0ead896882264ecc0287d3fc1)
CREATE INDEX create_datetime ON payment_authorization(create_datetime);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-304', '2.0.5', '3:ed0bfac0ead896882264ecc0287d3fc1', 155);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-305::Emiliano Conde::(Checksum: 3:5ea836c4c9019198c3e670d240fa2c2b)
CREATE INDEX transaction_id ON payment_authorization(transaction_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-305', '2.0.5', '3:5ea836c4c9019198c3e670d240fa2c2b', 156);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-306::Emiliano Conde::(Checksum: 3:70beb8a34d580537faf7cb748fb7a4c6)
CREATE INDEX ix_uq_payment_inv_map_pa_in ON payment_invoice(payment_id, invoice_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-306', '2.0.5', '3:70beb8a34d580537faf7cb748fb7a4c6', 157);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-307::Emiliano Conde::(Checksum: 3:4743df91243f658c93b90bb4718176a1)
CREATE INDEX permission_role_map_i_2 ON permission_role_map(permission_id, role_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-307', '2.0.5', '3:4743df91243f658c93b90bb4718176a1', 158);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-308::Emiliano Conde::(Checksum: 3:20b4a6ede2ec61fa259fb3e14b9a0970)
CREATE INDEX permission_user_map_i_2 ON permission_user(permission_id, user_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-308', '2.0.5', '3:20b4a6ede2ec61fa259fb3e14b9a0970', 159);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-309::Emiliano Conde::(Checksum: 3:1be024bfad7dee8650b2732273d86cf0)
CREATE INDEX plan_item_item_id_i ON plan_item(precedence);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-309', '2.0.5', '3:1be024bfad7dee8650b2732273d86cf0', 160);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-310::Emiliano Conde::(Checksum: 3:f0e272d07b8f70dfdb39b0127a91dbfb)
CREATE INDEX bp_pm_index_total ON process_run_total_pm(process_run_total_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-310', '2.0.5', '3:f0e272d07b8f70dfdb39b0127a91dbfb', 161);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-311::Emiliano Conde::(Checksum: 3:7b52b669e7adcd55dfcfdb4e16a1ac82)
CREATE INDEX ix_promotion_code ON promotion(code);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-311', '2.0.5', '3:7b52b669e7adcd55dfcfdb4e16a1ac82', 162);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-312::Emiliano Conde::(Checksum: 3:44584a64b6591ed0c30bfe5491b0d8e4)
CREATE INDEX promotion_user_map_i_2 ON promotion_user_map(user_id, promotion_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-312', '2.0.5', '3:44584a64b6591ed0c30bfe5491b0d8e4', 163);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-313::Emiliano Conde::(Checksum: 3:c84989904149c6b7bdc90868fef123e3)
CREATE INDEX ix_purchase_order_date ON purchase_order(user_id, create_datetime);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-313', '2.0.5', '3:c84989904149c6b7bdc90868fef123e3', 164);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-314::Emiliano Conde::(Checksum: 3:6c3479f39419bba2cfcd6b2243d468fc)
CREATE INDEX purchase_order_i_notif ON purchase_order(active_until, notification_step);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-314', '2.0.5', '3:6c3479f39419bba2cfcd6b2243d468fc', 165);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-315::Emiliano Conde::(Checksum: 3:b6e9972ebe555c99d13849f2699d3223)
CREATE INDEX purchase_order_i_user ON purchase_order(user_id, deleted);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-315', '2.0.5', '3:b6e9972ebe555c99d13849f2699d3223', 166);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-316::Emiliano Conde::(Checksum: 3:211b3e0749302cd31d23329bb3ab2601)
CREATE INDEX user_credit_card_map_i_2 ON user_credit_card_map(user_id, credit_card_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-316', '2.0.5', '3:211b3e0749302cd31d23329bb3ab2601', 167);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-317::Emiliano Conde::(Checksum: 3:1c5e0aa133911f3a6c25a776f4450d92)
CREATE INDEX user_role_map_i_2 ON user_role_map(user_id, role_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Index', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-317', '2.0.5', '3:1c5e0aa133911f3a6c25a776f4450d92', 168);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-318::Emiliano Conde::(Checksum: 3:84c120a531df046bd8acb44032bf3bd9)
CREATE TABLE my_test (id INT NOT NULL, entity_id INT, status_id INT, days INT NOT NULL, optlock INT NOT NULL, CONSTRAINT my_test_pkey PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Create Table', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-318', '2.0.5', '3:84c120a531df046bd8acb44032bf3bd9', 169);

