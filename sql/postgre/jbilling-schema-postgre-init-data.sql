Liquibase Home is not set.
Liquibase Home: /home/aristokrates/jbilling/liquidbase-2.0.5
-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: descriptors/database/jbilling-init_data.xml
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

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-1::aristokrates (generated)::(Checksum: 3:a8fe0d7a4810fcbbc55b9483cc576a67)
INSERT INTO order_line_type (editable, id) VALUES (1, 1);

INSERT INTO order_line_type (editable, id) VALUES (0, 2);

INSERT INTO order_line_type (editable, id) VALUES (0, 3);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x3)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-1', '2.0.5', '3:a8fe0d7a4810fcbbc55b9483cc576a67', 1);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-2::aristokrates (generated)::(Checksum: 3:0c0bce21aa0f8522d834b5c9e2ada6b1)
INSERT INTO notification_category (id) VALUES (1);

INSERT INTO notification_category (id) VALUES (2);

INSERT INTO notification_category (id) VALUES (3);

INSERT INTO notification_category (id) VALUES (4);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x4)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-2', '2.0.5', '3:0c0bce21aa0f8522d834b5c9e2ada6b1', 2);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-3::aristokrates (generated)::(Checksum: 3:b81fd56f0d90e5c63ee3a61d75ec6ad8)
INSERT INTO order_period (entity_id, id, optlock, unit_id, value) VALUES (NULL, 1, 1, NULL, NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-3', '2.0.5', '3:b81fd56f0d90e5c63ee3a61d75ec6ad8', 3);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-4::aristokrates (generated)::(Checksum: 3:44601e8a75747cea4c8fa54b933cefa3)
INSERT INTO jbilling_seqs (name, next_id) VALUES ('enumeration', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('enumeration_values', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('entity_delivery_method_map', 4);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('contact_field_type', 10);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('user_role_map', 13);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('entity_payment_method_map', 26);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('currency_entity_map', 10);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('user_credit_card_map', 5);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('permission_role_map', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('permission_user', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('contact_map', 6780);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('permission_type', 9);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('period_unit', 5);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('invoice_delivery_method', 4);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('user_status', 9);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_line_type', 4);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_billing_type', 3);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_status', 5);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('pluggable_task_type_category', 22);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('pluggable_task_type', 91);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('invoice_line_type', 6);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('currency', 11);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('payment_method', 9);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('payment_result', 5);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('event_log_module', 10);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('event_log_message', 17);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('preference_type', 37);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_message_type', 20);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('role', 6);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('country', 238);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('permission', 120);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('currency_exchange', 25);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('pluggable_task_parameter', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('billing_process_configuration', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_period', 2);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('partner_range', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('item_price', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('partner', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('entity', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('contact_type', 2);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('promotion', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('pluggable_task', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('ach', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('payment_info_cheque', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('partner_payout', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('process_run_total_pm', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('payment_authorization', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('billing_process', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('process_run', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('process_run_total', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('paper_invoice_batch', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('preference', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_message', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_message_section', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_message_line', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('ageing_entity_step', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('item_type', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('item', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('event_log', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('purchase_order', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_line', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('invoice', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('invoice_line', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_process', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('payment', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_message_arch', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_message_arch_line', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('base_user', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('customer', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('contact', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('contact_field', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('credit_card', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('language', 2);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('payment_invoice', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('subscriber_status', 7);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('mediation_cfg', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('mediation_process', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('blacklist', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('mediation_record_line', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('generic_status', 26);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('order_line_provisioning_status', 1);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('balance_type', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('mediation_record', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('price_model', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('price_model_attribute', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('plan', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('plan_item', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('filter', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('filter_set', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('recent_item', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('breadcrumb', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('shortcut', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('report', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('report_type', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('report_parameter', 0);

INSERT INTO jbilling_seqs (name, next_id) VALUES ('plan_item_bundle', 0);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x96)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-4', '2.0.5', '3:44601e8a75747cea4c8fa54b933cefa3', 4);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-5::aristokrates (generated)::(Checksum: 3:e5aa7e078527beaa455af114e17e601c)
INSERT INTO report_type (id, name, optlock) VALUES (1, 'invoice', 0);

INSERT INTO report_type (id, name, optlock) VALUES (2, 'order', 0);

INSERT INTO report_type (id, name, optlock) VALUES (3, 'payment', 0);

INSERT INTO report_type (id, name, optlock) VALUES (4, 'user', 0);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x4)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-5', '2.0.5', '3:e5aa7e078527beaa455af114e17e601c', 5);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-6::aristokrates (generated)::(Checksum: 3:632731df70af1e93a7e00d13088e436e)
INSERT INTO permission_role_map (permission_id, role_id) VALUES (10, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (12, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (13, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (14, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (15, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (16, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (17, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (20, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (21, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (22, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (23, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (25, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (26, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (27, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (28, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (30, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (31, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (32, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (33, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (34, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (35, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (36, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (42, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (44, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (52, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (60, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (61, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (62, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (63, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (70, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (71, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (73, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (74, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (80, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (94, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (95, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (96, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (97, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (98, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (99, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (100, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (101, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (102, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (103, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (104, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (111, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (120, 2);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (10, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (12, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (13, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (14, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (15, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (17, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (20, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (21, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (22, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (23, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (28, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (30, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (31, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (32, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (33, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (34, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (36, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (42, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (52, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (60, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (61, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (62, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (63, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (70, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (71, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (74, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (94, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (95, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (96, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (97, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (98, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (100, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (101, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (102, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (103, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (104, 3);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (15, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (10, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (28, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (20, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (21, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (34, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (36, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (30, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (74, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 4);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (15, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (18, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (29, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (30, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (34, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (37, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (75, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 5);

INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 5);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x136)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-6', '2.0.5', '3:632731df70af1e93a7e00d13088e436e', 6);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-7::aristokrates (generated)::(Checksum: 3:0476af908663535d9ff61956506d0992)
INSERT INTO permission_type (description, id) VALUES ('Customer', 1);

INSERT INTO permission_type (description, id) VALUES ('Order', 2);

INSERT INTO permission_type (description, id) VALUES ('Payment', 3);

INSERT INTO permission_type (description, id) VALUES ('Product', 4);

INSERT INTO permission_type (description, id) VALUES ('Product Category', 5);

INSERT INTO permission_type (description, id) VALUES ('Plan', 6);

INSERT INTO permission_type (description, id) VALUES ('Invoice', 7);

INSERT INTO permission_type (description, id) VALUES ('Billing', 8);

INSERT INTO permission_type (description, id) VALUES ('Menu', 9);

INSERT INTO permission_type (description, id) VALUES ('Partner', 10);

INSERT INTO permission_type (description, id) VALUES ('User Switching', 11);

INSERT INTO permission_type (description, id) VALUES ('API', 12);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x12)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-7', '2.0.5', '3:0476af908663535d9ff61956506d0992', 7);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-8::aristokrates (generated)::(Checksum: 3:3bd6a854e739af711cbfda0d81ef9d32)
INSERT INTO generic_status_type (id) VALUES ('order_line_provisioning_status');

INSERT INTO generic_status_type (id) VALUES ('order_status');

INSERT INTO generic_status_type (id) VALUES ('subscriber_status');

INSERT INTO generic_status_type (id) VALUES ('user_status');

INSERT INTO generic_status_type (id) VALUES ('invoice_status');

INSERT INTO generic_status_type (id) VALUES ('mediation_record_status');

INSERT INTO generic_status_type (id) VALUES ('process_run_status');

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x7)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-8', '2.0.5', '3:3bd6a854e739af711cbfda0d81ef9d32', 8);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-9::aristokrates (generated)::(Checksum: 3:64548c0f50aa0ebcd2c19dc26b5cac66)
INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (1, 'com.sapienter.jbilling.server.pluggableTask.OrderProcessingTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (2, 'com.sapienter.jbilling.server.pluggableTask.OrderFilterTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (3, 'com.sapienter.jbilling.server.pluggableTask.InvoiceFilterTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (4, 'com.sapienter.jbilling.server.pluggableTask.InvoiceCompositionTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (5, 'com.sapienter.jbilling.server.pluggableTask.OrderPeriodTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (6, 'com.sapienter.jbilling.server.pluggableTask.PaymentTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (7, 'com.sapienter.jbilling.server.pluggableTask.NotificationTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (8, 'com.sapienter.jbilling.server.pluggableTask.PaymentInfoTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (9, 'com.sapienter.jbilling.server.pluggableTask.PenaltyTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (10, 'com.sapienter.jbilling.server.pluggableTask.ProcessorAlarm');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (11, 'com.sapienter.jbilling.server.user.tasks.ISubscriptionStatusManager');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (12, 'com.sapienter.jbilling.server.payment.tasks.IAsyncPaymentParameters');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (13, 'com.sapienter.jbilling.server.item.tasks.IItemPurchaseManager');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (14, 'com.sapienter.jbilling.server.item.tasks.IPricing');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (15, 'com.sapienter.jbilling.server.mediation.task.IMediationReader');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (16, 'com.sapienter.jbilling.server.mediation.task.IMediationProcess');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (17, 'com.sapienter.jbilling.server.system.event.task.IInternalEventsTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (18, 'com.sapienter.jbilling.server.provisioning.task.IExternalProvisioning');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (19, 'com.sapienter.jbilling.server.user.tasks.IValidatePurchaseTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (20, 'com.sapienter.jbilling.server.process.task.IBillingProcessFilterTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (21, 'com.sapienter.jbilling.server.mediation.task.IMediationErrorHandler');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (22, 'com.sapienter.jbilling.server.process.task.IScheduledTask');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (23, 'com.sapienter.jbilling.server.rule.task.IRulesGenerator');

INSERT INTO pluggable_task_type_category (id, interface_name) VALUES (24, 'com.sapienter.jbilling.server.process.task.IAgeingTask');

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x24)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-9', '2.0.5', '3:64548c0f50aa0ebcd2c19dc26b5cac66', 9);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-10::aristokrates (generated)::(Checksum: 3:5a6dcf17687a990e080a10004219c161)
INSERT INTO role (entity_id, id, role_type_id) VALUES (NULL, 2, 2);

INSERT INTO role (entity_id, id, role_type_id) VALUES (NULL, 3, 3);

INSERT INTO role (entity_id, id, role_type_id) VALUES (NULL, 4, 4);

INSERT INTO role (entity_id, id, role_type_id) VALUES (NULL, 5, 5);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x4)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-10', '2.0.5', '3:5a6dcf17687a990e080a10004219c161', 10);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-11::aristokrates (generated)::(Checksum: 3:8216dc7570e0e0ead77be60c3b379313)
INSERT INTO jbilling_table (id, name) VALUES (8, 'entity_delivery_method_map');

INSERT INTO jbilling_table (id, name) VALUES (99, 'contact_field_type');

INSERT INTO jbilling_table (id, name) VALUES (62, 'user_role_map');

INSERT INTO jbilling_table (id, name) VALUES (36, 'entity_payment_method_map');

INSERT INTO jbilling_table (id, name) VALUES (68, 'currency_entity_map');

INSERT INTO jbilling_table (id, name) VALUES (45, 'user_credit_card_map');

INSERT INTO jbilling_table (id, name) VALUES (61, 'permission_role_map');

INSERT INTO jbilling_table (id, name) VALUES (29, 'contact_map');

INSERT INTO jbilling_table (id, name) VALUES (58, 'permission_type');

INSERT INTO jbilling_table (id, name) VALUES (6, 'period_unit');

INSERT INTO jbilling_table (id, name) VALUES (7, 'invoice_delivery_method');

INSERT INTO jbilling_table (id, name) VALUES (9, 'user_status');

INSERT INTO jbilling_table (id, name) VALUES (18, 'order_line_type');

INSERT INTO jbilling_table (id, name) VALUES (19, 'order_billing_type');

INSERT INTO jbilling_table (id, name) VALUES (20, 'order_status');

INSERT INTO jbilling_table (id, name) VALUES (23, 'pluggable_task_type_category');

INSERT INTO jbilling_table (id, name) VALUES (24, 'pluggable_task_type');

INSERT INTO jbilling_table (id, name) VALUES (30, 'invoice_line_type');

INSERT INTO jbilling_table (id, name) VALUES (4, 'currency');

INSERT INTO jbilling_table (id, name) VALUES (35, 'payment_method');

INSERT INTO jbilling_table (id, name) VALUES (41, 'payment_result');

INSERT INTO jbilling_table (id, name) VALUES (46, 'event_log_module');

INSERT INTO jbilling_table (id, name) VALUES (47, 'event_log_message');

INSERT INTO jbilling_table (id, name) VALUES (50, 'preference_type');

INSERT INTO jbilling_table (id, name) VALUES (52, 'notification_message_type');

INSERT INTO jbilling_table (id, name) VALUES (60, 'role');

INSERT INTO jbilling_table (id, name) VALUES (64, 'country');

INSERT INTO jbilling_table (id, name) VALUES (59, 'permission');

INSERT INTO jbilling_table (id, name) VALUES (67, 'currency_exchange');

INSERT INTO jbilling_table (id, name) VALUES (26, 'pluggable_task_parameter');

INSERT INTO jbilling_table (id, name) VALUES (34, 'billing_process_configuration');

INSERT INTO jbilling_table (id, name) VALUES (17, 'order_period');

INSERT INTO jbilling_table (id, name) VALUES (79, 'partner_range');

INSERT INTO jbilling_table (id, name) VALUES (15, 'item_price');

INSERT INTO jbilling_table (id, name) VALUES (11, 'partner');

INSERT INTO jbilling_table (id, name) VALUES (5, 'entity');

INSERT INTO jbilling_table (id, name) VALUES (28, 'contact_type');

INSERT INTO jbilling_table (id, name) VALUES (65, 'promotion');

INSERT INTO jbilling_table (id, name) VALUES (25, 'pluggable_task');

INSERT INTO jbilling_table (id, name) VALUES (75, 'ach');

INSERT INTO jbilling_table (id, name) VALUES (43, 'payment_info_cheque');

INSERT INTO jbilling_table (id, name) VALUES (70, 'partner_payout');

INSERT INTO jbilling_table (id, name) VALUES (38, 'process_run_total_pm');

INSERT INTO jbilling_table (id, name) VALUES (66, 'payment_authorization');

INSERT INTO jbilling_table (id, name) VALUES (32, 'billing_process');

INSERT INTO jbilling_table (id, name) VALUES (33, 'process_run');

INSERT INTO jbilling_table (id, name) VALUES (37, 'process_run_total');

INSERT INTO jbilling_table (id, name) VALUES (31, 'paper_invoice_batch');

INSERT INTO jbilling_table (id, name) VALUES (51, 'preference');

INSERT INTO jbilling_table (id, name) VALUES (53, 'notification_message');

INSERT INTO jbilling_table (id, name) VALUES (54, 'notification_message_section');

INSERT INTO jbilling_table (id, name) VALUES (55, 'notification_message_line');

INSERT INTO jbilling_table (id, name) VALUES (69, 'ageing_entity_step');

INSERT INTO jbilling_table (id, name) VALUES (13, 'item_type');

INSERT INTO jbilling_table (id, name) VALUES (14, 'item');

INSERT INTO jbilling_table (id, name) VALUES (48, 'event_log');

INSERT INTO jbilling_table (id, name) VALUES (21, 'purchase_order');

INSERT INTO jbilling_table (id, name) VALUES (22, 'order_line');

INSERT INTO jbilling_table (id, name) VALUES (39, 'invoice');

INSERT INTO jbilling_table (id, name) VALUES (40, 'invoice_line');

INSERT INTO jbilling_table (id, name) VALUES (49, 'order_process');

INSERT INTO jbilling_table (id, name) VALUES (42, 'payment');

INSERT INTO jbilling_table (id, name) VALUES (56, 'notification_message_arch');

INSERT INTO jbilling_table (id, name) VALUES (57, 'notification_message_arch_line');

INSERT INTO jbilling_table (id, name) VALUES (10, 'base_user');

INSERT INTO jbilling_table (id, name) VALUES (12, 'customer');

INSERT INTO jbilling_table (id, name) VALUES (27, 'contact');

INSERT INTO jbilling_table (id, name) VALUES (76, 'contact_field');

INSERT INTO jbilling_table (id, name) VALUES (44, 'credit_card');

INSERT INTO jbilling_table (id, name) VALUES (3, 'language');

INSERT INTO jbilling_table (id, name) VALUES (80, 'payment_invoice');

INSERT INTO jbilling_table (id, name) VALUES (81, 'subscriber_status');

INSERT INTO jbilling_table (id, name) VALUES (82, 'mediation_cfg');

INSERT INTO jbilling_table (id, name) VALUES (83, 'mediation_process');

INSERT INTO jbilling_table (id, name) VALUES (85, 'blacklist');

INSERT INTO jbilling_table (id, name) VALUES (86, 'mediation_record_line');

INSERT INTO jbilling_table (id, name) VALUES (87, 'generic_status');

INSERT INTO jbilling_table (id, name) VALUES (88, 'order_line_provisioning_status');

INSERT INTO jbilling_table (id, name) VALUES (89, 'balance_type');

INSERT INTO jbilling_table (id, name) VALUES (90, 'invoice_status');

INSERT INTO jbilling_table (id, name) VALUES (91, 'mediation_record_status');

INSERT INTO jbilling_table (id, name) VALUES (92, 'process_run_status');

INSERT INTO jbilling_table (id, name) VALUES (95, 'plan');

INSERT INTO jbilling_table (id, name) VALUES (96, 'plan_item');

INSERT INTO jbilling_table (id, name) VALUES (97, 'customer_price');

INSERT INTO jbilling_table (id, name) VALUES (100, 'report');

INSERT INTO jbilling_table (id, name) VALUES (101, 'report_type');

INSERT INTO jbilling_table (id, name) VALUES (102, 'report_parameter');

INSERT INTO jbilling_table (id, name) VALUES (103, 'plan_item_bundle');

INSERT INTO jbilling_table (id, name) VALUES (104, 'notification_category');

INSERT INTO jbilling_table (id, name) VALUES (105, 'enumeration');

INSERT INTO jbilling_table (id, name) VALUES (106, 'enumeration_values');

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x92)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-11', '2.0.5', '3:8216dc7570e0e0ead77be60c3b379313', 11);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-12::aristokrates (generated)::(Checksum: 3:5d757f4f946900667ef7c3e933a8b3b6)
INSERT INTO order_billing_type (id) VALUES (1);

INSERT INTO order_billing_type (id) VALUES (2);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x2)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-12', '2.0.5', '3:5d757f4f946900667ef7c3e933a8b3b6', 12);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-13::aristokrates (generated)::(Checksum: 3:5ecb19369abc453c9f313fad045584b8)
INSERT INTO language (code, description, id) VALUES ('en', 'English', 1);

INSERT INTO language (code, description, id) VALUES ('pt', 'Portugu�s', 2);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x2)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-13', '2.0.5', '3:5ecb19369abc453c9f313fad045584b8', 13);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-14::aristokrates (generated)::(Checksum: 3:0674523b69860e045bdc999038247f87)
INSERT INTO country (code, id) VALUES ('AF', 1);

INSERT INTO country (code, id) VALUES ('AL', 2);

INSERT INTO country (code, id) VALUES ('DZ', 3);

INSERT INTO country (code, id) VALUES ('AS', 4);

INSERT INTO country (code, id) VALUES ('AD', 5);

INSERT INTO country (code, id) VALUES ('AO', 6);

INSERT INTO country (code, id) VALUES ('AI', 7);

INSERT INTO country (code, id) VALUES ('AQ', 8);

INSERT INTO country (code, id) VALUES ('AG', 9);

INSERT INTO country (code, id) VALUES ('AR', 10);

INSERT INTO country (code, id) VALUES ('AM', 11);

INSERT INTO country (code, id) VALUES ('AW', 12);

INSERT INTO country (code, id) VALUES ('AU', 13);

INSERT INTO country (code, id) VALUES ('AT', 14);

INSERT INTO country (code, id) VALUES ('AZ', 15);

INSERT INTO country (code, id) VALUES ('BS', 16);

INSERT INTO country (code, id) VALUES ('BH', 17);

INSERT INTO country (code, id) VALUES ('BD', 18);

INSERT INTO country (code, id) VALUES ('BB', 19);

INSERT INTO country (code, id) VALUES ('BY', 20);

INSERT INTO country (code, id) VALUES ('BE', 21);

INSERT INTO country (code, id) VALUES ('BZ', 22);

INSERT INTO country (code, id) VALUES ('BJ', 23);

INSERT INTO country (code, id) VALUES ('BM', 24);

INSERT INTO country (code, id) VALUES ('BT', 25);

INSERT INTO country (code, id) VALUES ('BO', 26);

INSERT INTO country (code, id) VALUES ('BA', 27);

INSERT INTO country (code, id) VALUES ('BW', 28);

INSERT INTO country (code, id) VALUES ('BV', 29);

INSERT INTO country (code, id) VALUES ('BR', 30);

INSERT INTO country (code, id) VALUES ('IO', 31);

INSERT INTO country (code, id) VALUES ('BN', 32);

INSERT INTO country (code, id) VALUES ('BG', 33);

INSERT INTO country (code, id) VALUES ('BF', 34);

INSERT INTO country (code, id) VALUES ('BI', 35);

INSERT INTO country (code, id) VALUES ('KH', 36);

INSERT INTO country (code, id) VALUES ('CM', 37);

INSERT INTO country (code, id) VALUES ('CA', 38);

INSERT INTO country (code, id) VALUES ('CV', 39);

INSERT INTO country (code, id) VALUES ('KY', 40);

INSERT INTO country (code, id) VALUES ('CF', 41);

INSERT INTO country (code, id) VALUES ('TD', 42);

INSERT INTO country (code, id) VALUES ('CL', 43);

INSERT INTO country (code, id) VALUES ('CN', 44);

INSERT INTO country (code, id) VALUES ('CX', 45);

INSERT INTO country (code, id) VALUES ('CC', 46);

INSERT INTO country (code, id) VALUES ('CO', 47);

INSERT INTO country (code, id) VALUES ('KM', 48);

INSERT INTO country (code, id) VALUES ('CG', 49);

INSERT INTO country (code, id) VALUES ('CK', 50);

INSERT INTO country (code, id) VALUES ('CR', 51);

INSERT INTO country (code, id) VALUES ('CI', 52);

INSERT INTO country (code, id) VALUES ('HR', 53);

INSERT INTO country (code, id) VALUES ('CU', 54);

INSERT INTO country (code, id) VALUES ('CY', 55);

INSERT INTO country (code, id) VALUES ('CZ', 56);

INSERT INTO country (code, id) VALUES ('CD', 57);

INSERT INTO country (code, id) VALUES ('DK', 58);

INSERT INTO country (code, id) VALUES ('DJ', 59);

INSERT INTO country (code, id) VALUES ('DM', 60);

INSERT INTO country (code, id) VALUES ('DO', 61);

INSERT INTO country (code, id) VALUES ('TP', 62);

INSERT INTO country (code, id) VALUES ('EC', 63);

INSERT INTO country (code, id) VALUES ('EG', 64);

INSERT INTO country (code, id) VALUES ('SV', 65);

INSERT INTO country (code, id) VALUES ('GQ', 66);

INSERT INTO country (code, id) VALUES ('ER', 67);

INSERT INTO country (code, id) VALUES ('EE', 68);

INSERT INTO country (code, id) VALUES ('ET', 69);

INSERT INTO country (code, id) VALUES ('FK', 70);

INSERT INTO country (code, id) VALUES ('FO', 71);

INSERT INTO country (code, id) VALUES ('FJ', 72);

INSERT INTO country (code, id) VALUES ('FI', 73);

INSERT INTO country (code, id) VALUES ('FR', 74);

INSERT INTO country (code, id) VALUES ('GF', 75);

INSERT INTO country (code, id) VALUES ('PF', 76);

INSERT INTO country (code, id) VALUES ('TF', 77);

INSERT INTO country (code, id) VALUES ('GA', 78);

INSERT INTO country (code, id) VALUES ('GM', 79);

INSERT INTO country (code, id) VALUES ('GE', 80);

INSERT INTO country (code, id) VALUES ('DE', 81);

INSERT INTO country (code, id) VALUES ('GH', 82);

INSERT INTO country (code, id) VALUES ('GI', 83);

INSERT INTO country (code, id) VALUES ('GR', 84);

INSERT INTO country (code, id) VALUES ('GL', 85);

INSERT INTO country (code, id) VALUES ('GD', 86);

INSERT INTO country (code, id) VALUES ('GP', 87);

INSERT INTO country (code, id) VALUES ('GU', 88);

INSERT INTO country (code, id) VALUES ('GT', 89);

INSERT INTO country (code, id) VALUES ('GN', 90);

INSERT INTO country (code, id) VALUES ('GW', 91);

INSERT INTO country (code, id) VALUES ('GY', 92);

INSERT INTO country (code, id) VALUES ('HT', 93);

INSERT INTO country (code, id) VALUES ('HM', 94);

INSERT INTO country (code, id) VALUES ('HN', 95);

INSERT INTO country (code, id) VALUES ('HK', 96);

INSERT INTO country (code, id) VALUES ('HU', 97);

INSERT INTO country (code, id) VALUES ('IS', 98);

INSERT INTO country (code, id) VALUES ('IN', 99);

INSERT INTO country (code, id) VALUES ('ID', 100);

INSERT INTO country (code, id) VALUES ('IR', 101);

INSERT INTO country (code, id) VALUES ('IQ', 102);

INSERT INTO country (code, id) VALUES ('IE', 103);

INSERT INTO country (code, id) VALUES ('IL', 104);

INSERT INTO country (code, id) VALUES ('IT', 105);

INSERT INTO country (code, id) VALUES ('JM', 106);

INSERT INTO country (code, id) VALUES ('JP', 107);

INSERT INTO country (code, id) VALUES ('JO', 108);

INSERT INTO country (code, id) VALUES ('KZ', 109);

INSERT INTO country (code, id) VALUES ('KE', 110);

INSERT INTO country (code, id) VALUES ('KI', 111);

INSERT INTO country (code, id) VALUES ('KR', 112);

INSERT INTO country (code, id) VALUES ('KW', 113);

INSERT INTO country (code, id) VALUES ('KG', 114);

INSERT INTO country (code, id) VALUES ('LA', 115);

INSERT INTO country (code, id) VALUES ('LV', 116);

INSERT INTO country (code, id) VALUES ('LB', 117);

INSERT INTO country (code, id) VALUES ('LS', 118);

INSERT INTO country (code, id) VALUES ('LR', 119);

INSERT INTO country (code, id) VALUES ('LY', 120);

INSERT INTO country (code, id) VALUES ('LI', 121);

INSERT INTO country (code, id) VALUES ('LT', 122);

INSERT INTO country (code, id) VALUES ('LU', 123);

INSERT INTO country (code, id) VALUES ('MO', 124);

INSERT INTO country (code, id) VALUES ('MK', 125);

INSERT INTO country (code, id) VALUES ('MG', 126);

INSERT INTO country (code, id) VALUES ('MW', 127);

INSERT INTO country (code, id) VALUES ('MY', 128);

INSERT INTO country (code, id) VALUES ('MV', 129);

INSERT INTO country (code, id) VALUES ('ML', 130);

INSERT INTO country (code, id) VALUES ('MT', 131);

INSERT INTO country (code, id) VALUES ('MH', 132);

INSERT INTO country (code, id) VALUES ('MQ', 133);

INSERT INTO country (code, id) VALUES ('MR', 134);

INSERT INTO country (code, id) VALUES ('MU', 135);

INSERT INTO country (code, id) VALUES ('YT', 136);

INSERT INTO country (code, id) VALUES ('MX', 137);

INSERT INTO country (code, id) VALUES ('FM', 138);

INSERT INTO country (code, id) VALUES ('MD', 139);

INSERT INTO country (code, id) VALUES ('MC', 140);

INSERT INTO country (code, id) VALUES ('MN', 141);

INSERT INTO country (code, id) VALUES ('MS', 142);

INSERT INTO country (code, id) VALUES ('MA', 143);

INSERT INTO country (code, id) VALUES ('MZ', 144);

INSERT INTO country (code, id) VALUES ('MM', 145);

INSERT INTO country (code, id) VALUES ('NA', 146);

INSERT INTO country (code, id) VALUES ('NR', 147);

INSERT INTO country (code, id) VALUES ('NP', 148);

INSERT INTO country (code, id) VALUES ('NL', 149);

INSERT INTO country (code, id) VALUES ('AN', 150);

INSERT INTO country (code, id) VALUES ('NC', 151);

INSERT INTO country (code, id) VALUES ('NZ', 152);

INSERT INTO country (code, id) VALUES ('NI', 153);

INSERT INTO country (code, id) VALUES ('NE', 154);

INSERT INTO country (code, id) VALUES ('NG', 155);

INSERT INTO country (code, id) VALUES ('NU', 156);

INSERT INTO country (code, id) VALUES ('NF', 157);

INSERT INTO country (code, id) VALUES ('KP', 158);

INSERT INTO country (code, id) VALUES ('MP', 159);

INSERT INTO country (code, id) VALUES ('NO', 160);

INSERT INTO country (code, id) VALUES ('OM', 161);

INSERT INTO country (code, id) VALUES ('PK', 162);

INSERT INTO country (code, id) VALUES ('PW', 163);

INSERT INTO country (code, id) VALUES ('PA', 164);

INSERT INTO country (code, id) VALUES ('PG', 165);

INSERT INTO country (code, id) VALUES ('PY', 166);

INSERT INTO country (code, id) VALUES ('PE', 167);

INSERT INTO country (code, id) VALUES ('PH', 168);

INSERT INTO country (code, id) VALUES ('PN', 169);

INSERT INTO country (code, id) VALUES ('PL', 170);

INSERT INTO country (code, id) VALUES ('PT', 171);

INSERT INTO country (code, id) VALUES ('PR', 172);

INSERT INTO country (code, id) VALUES ('QA', 173);

INSERT INTO country (code, id) VALUES ('RE', 174);

INSERT INTO country (code, id) VALUES ('RO', 175);

INSERT INTO country (code, id) VALUES ('RU', 176);

INSERT INTO country (code, id) VALUES ('RW', 177);

INSERT INTO country (code, id) VALUES ('WS', 178);

INSERT INTO country (code, id) VALUES ('SM', 179);

INSERT INTO country (code, id) VALUES ('ST', 180);

INSERT INTO country (code, id) VALUES ('SA', 181);

INSERT INTO country (code, id) VALUES ('SN', 182);

INSERT INTO country (code, id) VALUES ('YU', 183);

INSERT INTO country (code, id) VALUES ('SC', 184);

INSERT INTO country (code, id) VALUES ('SL', 185);

INSERT INTO country (code, id) VALUES ('SG', 186);

INSERT INTO country (code, id) VALUES ('SK', 187);

INSERT INTO country (code, id) VALUES ('SI', 188);

INSERT INTO country (code, id) VALUES ('SB', 189);

INSERT INTO country (code, id) VALUES ('SO', 190);

INSERT INTO country (code, id) VALUES ('ZA', 191);

INSERT INTO country (code, id) VALUES ('GS', 192);

INSERT INTO country (code, id) VALUES ('ES', 193);

INSERT INTO country (code, id) VALUES ('LK', 194);

INSERT INTO country (code, id) VALUES ('SH', 195);

INSERT INTO country (code, id) VALUES ('KN', 196);

INSERT INTO country (code, id) VALUES ('LC', 197);

INSERT INTO country (code, id) VALUES ('PM', 198);

INSERT INTO country (code, id) VALUES ('VC', 199);

INSERT INTO country (code, id) VALUES ('SD', 200);

INSERT INTO country (code, id) VALUES ('SR', 201);

INSERT INTO country (code, id) VALUES ('SJ', 202);

INSERT INTO country (code, id) VALUES ('SZ', 203);

INSERT INTO country (code, id) VALUES ('SE', 204);

INSERT INTO country (code, id) VALUES ('CH', 205);

INSERT INTO country (code, id) VALUES ('SY', 206);

INSERT INTO country (code, id) VALUES ('TW', 207);

INSERT INTO country (code, id) VALUES ('TJ', 208);

INSERT INTO country (code, id) VALUES ('TZ', 209);

INSERT INTO country (code, id) VALUES ('TH', 210);

INSERT INTO country (code, id) VALUES ('TG', 211);

INSERT INTO country (code, id) VALUES ('TK', 212);

INSERT INTO country (code, id) VALUES ('TO', 213);

INSERT INTO country (code, id) VALUES ('TT', 214);

INSERT INTO country (code, id) VALUES ('TN', 215);

INSERT INTO country (code, id) VALUES ('TR', 216);

INSERT INTO country (code, id) VALUES ('TM', 217);

INSERT INTO country (code, id) VALUES ('TC', 218);

INSERT INTO country (code, id) VALUES ('TV', 219);

INSERT INTO country (code, id) VALUES ('UG', 220);

INSERT INTO country (code, id) VALUES ('UA', 221);

INSERT INTO country (code, id) VALUES ('AE', 222);

INSERT INTO country (code, id) VALUES ('UK', 223);

INSERT INTO country (code, id) VALUES ('US', 224);

INSERT INTO country (code, id) VALUES ('UM', 225);

INSERT INTO country (code, id) VALUES ('UY', 226);

INSERT INTO country (code, id) VALUES ('UZ', 227);

INSERT INTO country (code, id) VALUES ('VU', 228);

INSERT INTO country (code, id) VALUES ('VA', 229);

INSERT INTO country (code, id) VALUES ('VE', 230);

INSERT INTO country (code, id) VALUES ('VN', 231);

INSERT INTO country (code, id) VALUES ('VG', 232);

INSERT INTO country (code, id) VALUES ('VI', 233);

INSERT INTO country (code, id) VALUES ('WF', 234);

INSERT INTO country (code, id) VALUES ('YE', 235);

INSERT INTO country (code, id) VALUES ('ZM', 236);

INSERT INTO country (code, id) VALUES ('ZW', 237);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x237)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-14', '2.0.5', '3:0674523b69860e045bdc999038247f87', 14);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-15::aristokrates (generated)::(Checksum: 3:41080d0ee6377d5b3e39c35975063c72)
INSERT INTO payment_result (id) VALUES (1);

INSERT INTO payment_result (id) VALUES (2);

INSERT INTO payment_result (id) VALUES (3);

INSERT INTO payment_result (id) VALUES (4);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x4)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-15', '2.0.5', '3:41080d0ee6377d5b3e39c35975063c72', 15);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-16::aristokrates (generated)::(Checksum: 3:c6de6582f9666e851acd2c665a7f75eb)
INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('USD', 'US', 1, 0, 'US$');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('CAD', 'CA', 2, 0, 'C$');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('EUR', 'EU', 3, 0, '&#8364;');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('JPY', 'JP', 4, 0, '&#165;');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('GBP', 'UK', 5, 0, '&#163;');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('KRW', 'KR', 6, 0, '&#8361;');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('CHF', 'CH', 7, 0, 'Sf');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('SEK', 'SE', 8, 0, 'SeK');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('SGD', 'SG', 9, 0, 'S$');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('MYR', 'MY', 10, 0, 'M$');

INSERT INTO currency (code, country_code, id, optlock, symbol) VALUES ('AUD', 'AU', 11, 0, '$');

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x11)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-16', '2.0.5', '3:c6de6582f9666e851acd2c665a7f75eb', 16);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-17::aristokrates (generated)::(Checksum: 3:0a357b23ac41451aa9dd819c9bd925ea)
INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 10, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 11, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 12, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 13, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 14, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 15, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 16, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 17, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 18, 1);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 20, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 21, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 22, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 23, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 24, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 25, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 26, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 27, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 28, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 29, 2);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 30, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 31, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 32, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 33, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 34, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 35, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 36, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 37, 3);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 40, 4);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 41, 4);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 42, 4);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 43, 4);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 44, 4);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 50, 5);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 51, 5);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 52, 5);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 60, 6);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 61, 6);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 62, 6);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 63, 6);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 70, 7);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 71, 7);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 72, 7);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 73, 7);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 74, 7);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 75, 7);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 80, 8);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 90, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 91, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 92, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 93, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 94, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 95, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 96, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 97, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 98, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 99, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 100, 9);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 101, 10);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 102, 10);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 103, 10);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 104, 10);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 110, 11);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 111, 11);

INSERT INTO permission (foreign_id, id, type_id) VALUES (NULL, 120, 12);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x64)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-17', '2.0.5', '3:0a357b23ac41451aa9dd819c9bd925ea', 17);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-18::aristokrates (generated)::(Checksum: 3:6ef118c7f7daf505a34f29ea55020ab0)
INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('total_invoiced.jasper', 1, 'total_invoiced', 0, 1);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('ageing_balance.jasper', 2, 'ageing_balance', 0, 1);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('product_subscribers.jasper', 3, 'product_subscribers', 0, 2);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('total_payments.jasper', 4, 'total_payments', 0, 3);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('user_signups.jasper', 5, 'user_signups', 0, 4);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('top_customers.jasper', 6, 'top_customers', 0, 4);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('accounts_receivable.jasper', 7, 'accounts_receivable', 0, 1);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('gl_detail.jasper', 8, 'gl_detail', 0, 1);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('gl_summary.jasper', 9, 'gl_summary', 0, 1);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('plan_history.jasper', 10, 'plan_history', 0, 4);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('total_invoiced_per_customer.jasper', 11, 'total_invoiced_per_customer', 0, 4);

INSERT INTO report (file_name, id, name, optlock, type_id) VALUES ('total_invoiced_per_customer_over_years.jasper', 12, 'total_invoiced_per_customer_over_years', 0, 4);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x12)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-18', '2.0.5', '3:6ef118c7f7daf505a34f29ea55020ab0', 18);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-19::aristokrates (generated)::(Checksum: 3:ab61c6136374f8e5adfa5180b523f71e)
INSERT INTO invoice_line_type (description, id, order_position) VALUES ('item recurring', 1, 2);

INSERT INTO invoice_line_type (description, id, order_position) VALUES ('tax', 2, 6);

INSERT INTO invoice_line_type (description, id, order_position) VALUES ('due invoice', 3, 1);

INSERT INTO invoice_line_type (description, id, order_position) VALUES ('interests', 4, 4);

INSERT INTO invoice_line_type (description, id, order_position) VALUES ('sub account', 5, 5);

INSERT INTO invoice_line_type (description, id, order_position) VALUES ('item one-time', 6, 3);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x6)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-19', '2.0.5', '3:ab61c6136374f8e5adfa5180b523f71e', 19);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-20::aristokrates (generated)::(Checksum: 3:010a452736e89ec382875d23705a5f0d)
INSERT INTO event_log_message (id) VALUES (1);

INSERT INTO event_log_message (id) VALUES (2);

INSERT INTO event_log_message (id) VALUES (3);

INSERT INTO event_log_message (id) VALUES (4);

INSERT INTO event_log_message (id) VALUES (5);

INSERT INTO event_log_message (id) VALUES (6);

INSERT INTO event_log_message (id) VALUES (7);

INSERT INTO event_log_message (id) VALUES (8);

INSERT INTO event_log_message (id) VALUES (9);

INSERT INTO event_log_message (id) VALUES (10);

INSERT INTO event_log_message (id) VALUES (11);

INSERT INTO event_log_message (id) VALUES (12);

INSERT INTO event_log_message (id) VALUES (13);

INSERT INTO event_log_message (id) VALUES (14);

INSERT INTO event_log_message (id) VALUES (15);

INSERT INTO event_log_message (id) VALUES (16);

INSERT INTO event_log_message (id) VALUES (17);

INSERT INTO event_log_message (id) VALUES (18);

INSERT INTO event_log_message (id) VALUES (19);

INSERT INTO event_log_message (id) VALUES (20);

INSERT INTO event_log_message (id) VALUES (21);

INSERT INTO event_log_message (id) VALUES (22);

INSERT INTO event_log_message (id) VALUES (23);

INSERT INTO event_log_message (id) VALUES (24);

INSERT INTO event_log_message (id) VALUES (25);

INSERT INTO event_log_message (id) VALUES (26);

INSERT INTO event_log_message (id) VALUES (27);

INSERT INTO event_log_message (id) VALUES (28);

INSERT INTO event_log_message (id) VALUES (29);

INSERT INTO event_log_message (id) VALUES (30);

INSERT INTO event_log_message (id) VALUES (31);

INSERT INTO event_log_message (id) VALUES (32);

INSERT INTO event_log_message (id) VALUES (33);

INSERT INTO event_log_message (id) VALUES (34);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x34)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-20', '2.0.5', '3:010a452736e89ec382875d23705a5f0d', 20);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-21::aristokrates (generated)::(Checksum: 3:dc80d16e778a70dec420fe9a5a481ec9)
INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (1, 'com.sapienter.jbilling.server.pluggableTask.BasicLineTotalTask', 1, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.pluggableTask.CalculateDueDateDfFm', 13, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.pluggableTask.CalculateDueDate', 3, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.pluggableTask.BasicCompositionTask', 4, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (2, 'com.sapienter.jbilling.server.pluggableTask.BasicOrderFilterTask', 5, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (3, 'com.sapienter.jbilling.server.pluggableTask.BasicInvoiceFilterTask', 6, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (5, 'com.sapienter.jbilling.server.pluggableTask.BasicOrderPeriodTask', 7, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.pluggableTask.PaymentAuthorizeNetTask', 8, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (3, 'com.sapienter.jbilling.server.pluggableTask.NoInvoiceFilterTask', 14, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (8, 'com.sapienter.jbilling.server.pluggableTask.BasicPaymentInfoTask', 10, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.pluggableTask.PaymentPartnerTestTask', 11, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (7, 'com.sapienter.jbilling.server.pluggableTask.PaperInvoiceNotificationTask', 12, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (1, 'com.sapienter.jbilling.server.pluggableTask.GSTTaxTask', 2, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (9, 'com.sapienter.jbilling.server.pluggableTask.BasicPenaltyTask', 15, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (2, 'com.sapienter.jbilling.server.pluggableTask.OrderFilterAnticipatedTask', 16, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (7, 'com.sapienter.jbilling.server.pluggableTask.BasicEmailNotificationTask', 9, 6);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (5, 'com.sapienter.jbilling.server.pluggableTask.OrderPeriodAnticipateTask', 17, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.pluggableTask.PaymentEmailAuthorizeNetTask', 19, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (10, 'com.sapienter.jbilling.server.pluggableTask.ProcessorEmailAlarmTask', 20, 3);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.pluggableTask.PaymentFakeTask', 21, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentRouterCCFTask', 22, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (11, 'com.sapienter.jbilling.server.user.tasks.BasicSubscriptionStatusManagerTask', 23, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.user.tasks.PaymentACHCommerceTask', 24, 5);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (12, 'com.sapienter.jbilling.server.payment.tasks.NoAsyncParameters', 25, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (12, 'com.sapienter.jbilling.server.payment.tasks.RouterAsyncParameters', 26, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (13, 'com.sapienter.jbilling.server.item.tasks.BasicItemManager', 28, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (13, 'com.sapienter.jbilling.server.item.tasks.RulesItemManager', 29, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (1, 'com.sapienter.jbilling.server.order.task.RulesLineTotalTask', 30, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (14, 'com.sapienter.jbilling.server.item.tasks.RulesPricingTask', 31, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (15, 'com.sapienter.jbilling.server.mediation.task.SeparatorFileReader', 32, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (16, 'com.sapienter.jbilling.server.mediation.task.RulesMediationTask', 33, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (15, 'com.sapienter.jbilling.server.mediation.task.FixedFileReader', 34, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (8, 'com.sapienter.jbilling.server.user.tasks.PaymentInfoNoValidateTask', 35, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (7, 'com.sapienter.jbilling.server.notification.task.TestNotificationTask', 36, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (5, 'com.sapienter.jbilling.server.process.task.ProRateOrderPeriodTask', 37, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.process.task.DailyProRateCompositionTask', 38, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentAtlasTask', 39, 5);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.order.task.RefundOnCancelTask', 40, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.order.task.CancellationFeeRulesTask', 41, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentFilterTask', 42, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.payment.blacklist.tasks.BlacklistUserStatusTask', 43, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (15, 'com.sapienter.jbilling.server.mediation.task.JDBCReader', 44, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (15, 'com.sapienter.jbilling.server.mediation.task.MySQLReader', 45, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.provisioning.task.ProvisioningCommandsRulesTask', 46, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (18, 'com.sapienter.jbilling.server.provisioning.task.TestExternalProvisioningTask', 47, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (18, 'com.sapienter.jbilling.server.provisioning.task.CAIProvisioningTask', 48, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentRouterCurrencyTask', 49, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (18, 'com.sapienter.jbilling.server.provisioning.task.MMSCProvisioningTask', 50, 5);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (3, 'com.sapienter.jbilling.server.invoice.task.NegativeBalanceInvoiceFilterTask', 51, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.invoice.task.FileInvoiceExportTask', 52, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.system.event.task.InternalEventsRulesTask', 53, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.user.balance.DynamicBalanceManagerTask', 54, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (19, 'com.sapienter.jbilling.server.user.tasks.UserBalanceValidatePurchaseTask', 55, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (19, 'com.sapienter.jbilling.server.user.tasks.RulesValidatePurchaseTask', 56, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentsGatewayTask', 57, 4);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.payment.tasks.SaveCreditCardExternallyTask', 58, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (13, 'com.sapienter.jbilling.server.order.task.RulesItemManager2', 59, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (1, 'com.sapienter.jbilling.server.order.task.RulesLineTotalTask2', 60, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (14, 'com.sapienter.jbilling.server.item.tasks.RulesPricingTask2', 61, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.payment.tasks.SaveCreditCardExternallyTask', 62, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.pluggableTask.PaymentFakeExternalStorage', 63, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayTask', 64, 3);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayExternalTask', 65, 3);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.user.tasks.AutoRechargeTask', 66, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentBeanstreamTask', 67, 3);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentSageTask', 68, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (20, 'com.sapienter.jbilling.server.process.task.BasicBillingProcessFilterTask', 69, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (20, 'com.sapienter.jbilling.server.process.task.BillableUsersBillingProcessFilterTask', 70, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (21, 'com.sapienter.jbilling.server.mediation.task.SaveToFileMediationErrorHandler', 71, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (21, 'com.sapienter.jbilling.server.mediation.task.SaveToJDBCMediationErrorHandler', 73, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentPaypalExternalTask', 75, 3);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentAuthorizeNetCIMTask', 76, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (6, 'com.sapienter.jbilling.server.payment.tasks.PaymentMethodRouterTask', 77, 4);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (23, 'com.sapienter.jbilling.server.rule.task.VelocityRulesGeneratorTask', 78, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (14, 'com.sapienter.jbilling.server.pricing.tasks.PriceModelPricingTask', 79, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (22, 'com.sapienter.jbilling.server.mediation.task.MediationProcessTask', 81, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (22, 'com.sapienter.jbilling.server.billing.task.BillingProcessTask', 82, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (22, 'com.sapienter.jbilling.server.process.task.ScpUploadTask', 83, 4);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.payment.tasks.SaveACHExternallyTask', 84, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (20, 'com.sapienter.jbilling.server.process.task.BillableUserOrdersBillingProcessFilterTask', 85, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.item.tasks.PlanChangesExternalTask', 86, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (24, 'com.sapienter.jbilling.server.process.task.BasicAgeingTask', 87, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (22, 'com.sapienter.jbilling.server.process.task.AgeingProcessTask', 88, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (24, 'com.sapienter.jbilling.server.process.task.BusinessDayAgeingTask', 89, 0);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.process.task.SimpleTaxCompositionTask', 90, 1);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.process.task.CountryTaxCompositionTask', 91, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (4, 'com.sapienter.jbilling.server.process.task.PaymentTermPenaltyTask', 92, 2);

INSERT INTO pluggable_task_type (category_id, class_name, id, min_parameters) VALUES (17, 'com.sapienter.jbilling.server.order.task.CancellationFeeTask', 93, 2);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x88)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-21', '2.0.5', '3:dc80d16e778a70dec420fe9a5a481ec9', 21);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-22::aristokrates (generated)::(Checksum: 3:7ac8f12a07c740b0ee3aff5cd1e2e463)
INSERT INTO payment_method (id) VALUES (1);

INSERT INTO payment_method (id) VALUES (2);

INSERT INTO payment_method (id) VALUES (3);

INSERT INTO payment_method (id) VALUES (4);

INSERT INTO payment_method (id) VALUES (5);

INSERT INTO payment_method (id) VALUES (6);

INSERT INTO payment_method (id) VALUES (7);

INSERT INTO payment_method (id) VALUES (8);

INSERT INTO payment_method (id) VALUES (9);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x9)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-22', '2.0.5', '3:7ac8f12a07c740b0ee3aff5cd1e2e463', 22);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-23::aristokrates (generated)::(Checksum: 3:337914241aa740552edef0a928809808)
INSERT INTO invoice_delivery_method (id) VALUES (1);

INSERT INTO invoice_delivery_method (id) VALUES (2);

INSERT INTO invoice_delivery_method (id) VALUES (3);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x3)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-23', '2.0.5', '3:337914241aa740552edef0a928809808', 23);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-24::aristokrates (generated)::(Checksum: 3:5601ed88c59920dd7066642e0d0c4f62)
INSERT INTO contact_type (entity_id, id, is_primary, optlock) VALUES (NULL, 1, NULL, 0);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-24', '2.0.5', '3:5601ed88c59920dd7066642e0d0c4f62', 24);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-25::aristokrates (generated)::(Checksum: 3:3b07bce826f6643ecef6f07fc125678b)
INSERT INTO period_unit (id) VALUES (1);

INSERT INTO period_unit (id) VALUES (2);

INSERT INTO period_unit (id) VALUES (3);

INSERT INTO period_unit (id) VALUES (4);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x4)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-25', '2.0.5', '3:3b07bce826f6643ecef6f07fc125678b', 25);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-26::aristokrates (generated)::(Checksum: 3:647469db1cb3e020b738d2b32bf9334f)
INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 1, 'start_date', 1);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 2, 'end_date', 1);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('integer', 3, 'period', 1);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('integer', 4, 'item_id', 3);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 5, 'start_date', 4);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 6, 'end_date', 4);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('integer', 7, 'period', 4);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 8, 'start_date', 5);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 9, 'end_date', 5);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('integer', 10, 'period', 5);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 11, 'start_date', 6);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 12, 'end_date', 6);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 13, 'date', 8);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 14, 'date', 9);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('integer', 15, 'plan_id', 10);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('string', 16, 'plan_code', 10);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('string', 17, 'plan_description', 10);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 18, 'start_date', 11);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('date', 19, 'end_date', 11);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('string', 20, 'start_year', 12);

INSERT INTO report_parameter (dtype, id, name, report_id) VALUES ('string', 21, 'end_year', 12);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x21)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-26', '2.0.5', '3:647469db1cb3e020b738d2b32bf9334f', 26);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-27::aristokrates (generated)::(Checksum: 3:38418b0137d9c1fa7206f8d813ad63d2)
INSERT INTO preference_type (def_value, id) VALUES (NULL, 4);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 5);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 6);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 7);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 8);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 9);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 10);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 11);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 12);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 13);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 14);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 15);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 16);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 17);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 18);

INSERT INTO preference_type (def_value, id) VALUES ('1', 19);

INSERT INTO preference_type (def_value, id) VALUES ('1', 20);

INSERT INTO preference_type (def_value, id) VALUES ('0', 21);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 22);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 23);

INSERT INTO preference_type (def_value, id) VALUES ('0', 24);

INSERT INTO preference_type (def_value, id) VALUES ('0', 25);

INSERT INTO preference_type (def_value, id) VALUES ('0', 27);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 28);

INSERT INTO preference_type (def_value, id) VALUES ('https://www.paypal.com/en_US/i/btn/x-click-but6.gif', 29);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 30);

INSERT INTO preference_type (def_value, id) VALUES ('2000-01-01', 31);

INSERT INTO preference_type (def_value, id) VALUES ('0', 32);

INSERT INTO preference_type (def_value, id) VALUES ('0', 33);

INSERT INTO preference_type (def_value, id) VALUES ('0', 35);

INSERT INTO preference_type (def_value, id) VALUES ('1', 36);

INSERT INTO preference_type (def_value, id) VALUES ('0', 37);

INSERT INTO preference_type (def_value, id) VALUES ('1', 38);

INSERT INTO preference_type (def_value, id) VALUES ('0', 39);

INSERT INTO preference_type (def_value, id) VALUES ('0', 40);

INSERT INTO preference_type (def_value, id) VALUES ('0', 41);

INSERT INTO preference_type (def_value, id) VALUES ('0', 42);

INSERT INTO preference_type (def_value, id) VALUES ('0', 43);

INSERT INTO preference_type (def_value, id) VALUES ('0', 44);

INSERT INTO preference_type (def_value, id) VALUES ('0', 45);

INSERT INTO preference_type (def_value, id) VALUES ('0', 46);

INSERT INTO preference_type (def_value, id) VALUES ('0', 47);

INSERT INTO preference_type (def_value, id) VALUES ('1', 48);

INSERT INTO preference_type (def_value, id) VALUES (NULL, 49);

INSERT INTO preference_type (def_value, id) VALUES ('2', 50);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x45)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-27', '2.0.5', '3:38418b0137d9c1fa7206f8d813ad63d2', 27);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-28::aristokrates (generated)::(Checksum: 3:9464d6112f741d8d783b0ec038b94be6)
INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-03-09 00:00:00.0', 2, 0, 1, 1, 1.325, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-03-09 00:00:00.0', 3, 0, 2, 1, 0.8118, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-03-09 00:00:00.0', 4, 0, 3, 1, 111.4, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-03-09 00:00:00.0', 5, 0, 4, 1, 0.5479, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-03-09 00:00:00.0', 6, 0, 5, 1, 1171, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-07-06 00:00:00.0', 7, 0, 6, 1, 1.23, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-07-06 00:00:00.0', 8, 0, 7, 1, 7.47, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-10-12 00:00:00.0', 9, 0, 10, 1, 1.68, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2004-10-12 00:00:00.0', 10, 0, 11, 1, 3.8, '1970-01-01');

INSERT INTO currency_exchange (create_datetime, currency_id, entity_id, id, optlock, rate, valid_since) VALUES ('2007-01-25 00:00:00.0', 11, 0, 12, 1, 1.288, '1970-01-01');

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x10)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-28', '2.0.5', '3:9464d6112f741d8d783b0ec038b94be6', 28);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-29::aristokrates (generated)::(Checksum: 3:ec8fd6367fa13f67236ef1aac6946e97)
INSERT INTO event_log_module (id) VALUES (1);

INSERT INTO event_log_module (id) VALUES (2);

INSERT INTO event_log_module (id) VALUES (3);

INSERT INTO event_log_module (id) VALUES (4);

INSERT INTO event_log_module (id) VALUES (5);

INSERT INTO event_log_module (id) VALUES (6);

INSERT INTO event_log_module (id) VALUES (7);

INSERT INTO event_log_module (id) VALUES (8);

INSERT INTO event_log_module (id) VALUES (9);

INSERT INTO event_log_module (id) VALUES (10);

INSERT INTO event_log_module (id) VALUES (11);

INSERT INTO event_log_module (id) VALUES (12);

INSERT INTO event_log_module (id) VALUES (13);

INSERT INTO event_log_module (id) VALUES (14);

INSERT INTO event_log_module (id) VALUES (15);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x15)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-29', '2.0.5', '3:ec8fd6367fa13f67236ef1aac6946e97', 29);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-30::aristokrates (generated)::(Checksum: 3:5f3b64c893d145f239d91b4df979badc)
INSERT INTO notification_message_type (category_id, id, optlock) VALUES (1, 1, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 2, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 3, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 4, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 5, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 6, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 7, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 8, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 9, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (3, 10, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (3, 11, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (1, 12, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (2, 13, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (2, 14, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (2, 15, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (3, 16, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (3, 17, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (1, 18, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 19, 1);

INSERT INTO notification_message_type (category_id, id, optlock) VALUES (4, 20, 1);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x20)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-30', '2.0.5', '3:5f3b64c893d145f239d91b4df979badc', 30);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-31::aristokrates (generated)::(Checksum: 3:e781d13a841f2894c40cb78705d52b0b)
INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (1, 'user_status', 1, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (1, 'user_status', 2, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (1, 'user_status', 3, 3);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (1, 'user_status', 4, 4);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (0, 'user_status', 5, 5);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (0, 'user_status', 6, 6);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (0, 'user_status', 7, 7);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (0, 'user_status', 8, 8);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 9, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 10, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 11, 3);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 12, 4);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 13, 5);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 14, 6);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'subscriber_status', 15, 7);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_status', 16, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_status', 17, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_status', 18, 3);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_status', 19, 4);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_line_provisioning_status', 20, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_line_provisioning_status', 21, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_line_provisioning_status', 22, 3);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_line_provisioning_status', 23, 4);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_line_provisioning_status', 24, 5);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'order_line_provisioning_status', 25, 6);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'invoice_status', 26, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'invoice_status', 27, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'invoice_status', 28, 3);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'mediation_record_status', 29, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'mediation_record_status', 30, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'mediation_record_status', 31, 3);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'mediation_record_status', 32, 4);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'process_run_status', 33, 1);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'process_run_status', 34, 2);

INSERT INTO generic_status (can_login, dtype, id, status_value) VALUES (NULL, 'process_run_status', 35, 3);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x35)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-31', '2.0.5', '3:e781d13a841f2894c40cb78705d52b0b', 31);

-- Changeset descriptors/database/jbilling-init_data.xml::1337972683141-32::aristokrates (generated)::(Checksum: 3:fbd64c2395e2fad0067fdd2d799dd6fb)
INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manual invoice deletion', 20, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice maintenance', 9, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pluggable tasks maintenance', 11, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A purchase order as been manually applied to an invoice.', 16, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment (failed)', 17, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment (successful)', 16, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Discovery', 6, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Diners', 7, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Days before expiration for order notification 2', 16, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Days before expiration for order notification 3', 17, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order about to expire. Step 1', 13, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order about to expire. Step 2', 14, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order about to expire. Step 3', 15, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('AMEX', 4, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('ACH', 5, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Include customer notes in invoice', 14, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice number prefix', 18, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Wallis and Futuna', 234, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Yemen', 235, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Zambia', 236, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Zimbabwe', 237, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Days before expiration for order notification', 15, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use invoice reminders', 21, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Number of days after the invoice generation for the first reminder', 22, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Number of days for next reminder', 23, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uzbekistan', 227, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nauru', 147, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nepal', 148, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Netherlands', 149, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Netherlands Antilles', 150, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('New Caledonia', 151, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('New Zealand', 152, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nicaragua', 153, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Niger', 154, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nigeria', 155, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Niue', 156, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Norfolk Island', 157, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('North Korea', 158, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Northern Mariana Islands', 159, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Norway', 160, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Oman', 161, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pakistan', 162, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Palau', 163, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Panama', 164, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Papua New Guinea', 165, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paraguay', 166, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Peru', 167, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Philippines', 168, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pitcairn Islands', 169, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Poland', 170, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Portugal', 171, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Puerto Rico', 172, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Qatar', 173, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Reunion', 174, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Romania', 175, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Russia', 176, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rwanda', 177, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Samoa', 178, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('San Marino', 179, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sao Tome and Principe', 180, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Saudi Arabia', 181, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Senegal', 182, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Serbia and Montenegro', 183, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Seychelles', 184, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sierra Leone', 185, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Singapore', 186, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Slovakia', 187, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Slovenia', 188, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Solomon Islands', 189, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Somalia', 190, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('South Africa', 191, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('South Georgia and the South Sandwich Islands', 192, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Spain', 193, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sri Lanka', 194, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('St. Helena', 195, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('St. Kitts and Nevis', 196, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('St. Lucia', 197, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('St. Pierre and Miquelon', 198, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('St. Vincent and the Grenadines', 199, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sudan', 200, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suriname', 201, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Svalbard and Jan Mayen', 202, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Swaziland', 203, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sweden', 204, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Switzerland', 205, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Syria', 206, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Taiwan', 207, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tajikistan', 208, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tanzania', 209, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Thailand', 210, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Togo', 211, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tokelau', 212, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tonga', 213, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Trinidad and Tobago', 214, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tunisia', 215, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Turkey', 216, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Turkmenistan', 217, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Turks and Caicos Islands', 218, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tuvalu', 219, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uganda', 220, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ukraine', 221, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('United Arab Emirates', 222, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('United Kingdom', 223, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('United States', 224, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('United States Minor Outlying Islands', 225, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uruguay', 226, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Vanuatu', 228, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Vatican City', 229, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Venezuela', 230, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Viet Nam', 231, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Virgin Islands - British', 232, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Virgin Islands', 233, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Congo - DRC', 57, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Denmark', 58, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Djibouti', 59, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dominica', 60, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dominican Republic', 61, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('East Timor', 62, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ecuador', 63, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Egypt', 64, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('El Salvador', 65, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Equatorial Guinea', 66, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Eritrea', 67, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Estonia', 68, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ethiopia', 69, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malvinas Islands', 70, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Faroe Islands', 71, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Fiji Islands', 72, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Finland', 73, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('France', 74, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('French Guiana', 75, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('French Polynesia', 76, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('French Southern and Antarctic Lands', 77, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gabon', 78, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gambia', 79, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Georgia', 80, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Germany', 81, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ghana', 82, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gibraltar', 83, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Greece', 84, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Greenland', 85, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Grenada', 86, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guadeloupe', 87, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guam', 88, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guatemala', 89, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guinea', 90, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guinea-Bissau', 91, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guyana', 92, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Haiti', 93, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Heard Island and McDonald Islands', 94, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Honduras', 95, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Hong Kong SAR', 96, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Hungary', 97, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Iceland', 98, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('India', 99, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Indonesia', 100, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Iran', 101, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Iraq', 102, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ireland', 103, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Israel', 104, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Italy', 105, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Jamaica', 106, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Japan', 107, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Jordan', 108, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kazakhstan', 109, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kenya', 110, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kiribati', 111, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Korea', 112, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kuwait', 113, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kyrgyzstan', 114, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Laos', 115, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Latvia', 116, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lebanon', 117, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lesotho', 118, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Liberia', 119, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Libya', 120, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Liechtenstein', 121, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lithuania', 122, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Luxembourg', 123, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Macao SAR', 124, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Macedonia, Former Yugoslav Republic of', 125, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Madagascar', 126, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malawi', 127, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malaysia', 128, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Maldives', 129, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mali', 130, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malta', 131, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Marshall Islands', 132, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Martinique', 133, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mauritania', 134, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mauritius', 135, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mayotte', 136, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mexico', 137, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Micronesia', 138, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Moldova', 139, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Monaco', 140, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mongolia', 141, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Montserrat', 142, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Morocco', 143, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mozambique', 144, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Myanmar', 145, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Namibia', 146, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Afghanistan', 1, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Albania', 2, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Algeria', 3, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('American Samoa', 4, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Andorra', 5, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Angola', 6, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Anguilla', 7, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Antarctica', 8, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Antigua and Barbuda', 9, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Argentina', 10, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Armenia', 11, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Aruba', 12, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Australia', 13, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Austria', 14, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Azerbaijan', 15, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bahamas', 16, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bahrain', 17, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bangladesh', 18, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Barbados', 19, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Belarus', 20, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Belgium', 21, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Belize', 22, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Benin', 23, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bermuda', 24, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bhutan', 25, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bolivia', 26, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bosnia and Herzegovina', 27, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Botswana', 28, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bouvet Island', 29, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Brazil', 30, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('British Indian Ocean Territory', 31, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Brunei', 32, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bulgaria', 33, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Burkina Faso', 34, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Burundi', 35, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cambodia', 36, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cameroon', 37, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Canada', 38, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cape Verde', 39, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cayman Islands', 40, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Central African Republic', 41, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Chad', 42, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Chile', 43, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('China', 44, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Christmas Island', 45, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cocos - Keeling Islands', 46, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Colombia', 47, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Comoros', 48, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Congo', 49, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cook Islands', 50, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Costa Rica', 51, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cote d Ivoire', 52, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Croatia', 53, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cuba', 54, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cyprus', 55, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Czech Republic', 56, 1, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('United States Dollar', 1, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Canadian Dollar', 2, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Euro', 3, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Yen', 4, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pound Sterling', 5, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Won', 6, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Swiss Franc', 7, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Swedish Krona', 8, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Month', 1, 1, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Week', 2, 1, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Day', 3, 1, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Year', 4, 1, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Email', 1, 1, 'description', 7);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paper', 2, 1, 'description', 7);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Active', 1, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Overdue', 2, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Overdue 2', 3, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Overdue 3', 4, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspended', 5, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspended 2', 6, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspended 3', 7, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Deleted', 8, 1, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Active', 1, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pending Unsubscription', 2, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Unsubscribed', 3, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pending Expiration', 4, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Expired', 5, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nonsubscriber', 6, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Discontinued', 7, 1, 'description', 81);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('An internal user with all the permissions', 1, 1, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Internal', 1, 1, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The super user of an entity', 2, 1, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Super user', 2, 1, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A billing clerk', 3, 1, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Clerk', 3, 1, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A partner that will bring customers', 4, 1, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner', 4, 1, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A customer that will query his/her account', 5, 1, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Customer', 5, 1, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('One time', 1, 1, 'description', 17);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Items', 1, 1, 'description', 18);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tax', 2, 1, 'description', 18);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('pre paid', 1, 1, 'description', 19);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('post paid', 2, 1, 'description', 19);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Active', 1, 1, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Finished', 2, 1, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspended', 3, 1, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cheque', 1, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Visa', 2, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('MasterCard', 3, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Successful', 1, 1, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Failed', 2, 1, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Processor unavailable', 3, 1, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Entered', 4, 1, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Billing Process', 1, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User maintenance', 2, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item maintenance', 3, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item type maintenance', 4, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item user price maintenance', 5, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Promotion maintenance', 6, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order maintenance', 7, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Credit card maintenance', 8, 1, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A prepaid order has unbilled time before the billing process date', 1, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order has no active time at the date of process.', 2, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('At least one complete period has to be billable.', 3, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Already billed for the current date.', 4, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This order had to be maked for exclusion in the last process.', 5, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pre-paid order is being process after its expiration.', 6, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A row was marked as deleted.', 7, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A user password was changed.', 8, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A row was updated.', 9, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Running a billing process, but a review is found unapproved.', 10, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Running a billing process, review is required but not present.', 11, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A user status was changed.', 12, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('An order status was changed.', 13, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A user had to be aged, but there''s no more steps configured.', 14, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A partner has a payout ready, but no payment instrument.', 15, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Process payment with billing process', 1, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('URL of CSS file', 2, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('URL of logo graphic', 3, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Grace period', 4, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner percentage rate', 5, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner referral fee', 6, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner one time payout', 7, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner period unit payout', 8, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner period value payout', 9, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner automatic payout', 10, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User in charge of partners ', 11, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner fee currency', 12, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Self delivery of paper invoices', 13, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice (email)', 1, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Reactivated', 2, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Overdue', 3, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Overdue 2', 4, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Overdue 3', 5, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Suspended', 6, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Suspended 2', 7, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Suspended 3', 8, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User Deleted', 9, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payout Remainder', 10, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner Payout', 11, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice (paper)', 12, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Data Fattura Fine Mese', 24, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Singapore Dollar', 9, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malaysian Ringgit', 10, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Australian Dollar', 11, 1, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Next invoice number', 19, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Email + Paper', 3, 1, 'description', 7);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('PayPal', 8, 1, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Update Credit Card', 19, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspended (auto)', 4, 1, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Penalty', 3, 1, 'description', 18);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lost password', 20, 1, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Active', 1, 1, 'description', 88);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Inactive', 2, 1, 'description', 88);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pending Active', 3, 1, 'description', 88);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pending Inactive', 4, 1, 'description', 88);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Failed', 5, 1, 'description', 88);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Unavailable', 6, 1, 'description', 88);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Elimina��o manual de facturas', 20, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manuten��o de facturas', 9, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manuten��o de tarefas de plug-ins', 11, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uma ordem de compra foi aplicada manualmente a uma factura.', 16, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment (sem sucesso)', 17, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment (com sucesso)', 16, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Discovery', 6, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Diners', 7, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dias antes da expira��o para notifica��o de ordens 2', 16, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dias antes da expira��o para notifica��o de ordens 3', 17, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ordem de compra a expirar. Passo 1', 13, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ordem de compra a expirar. Passo 2', 14, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ordem de compra a expirar. Passo 3', 15, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('AMEX', 4, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('CCA', 5, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Incluir notas do cliente na factura', 14, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('N�mero de prefixo da factura', 18, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Wallis and Futuna', 234, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Yemen', 235, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Z�mbia', 236, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Zimbabwe', 237, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dias antes da expira��o para notifica��o de ordens', 15, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Usar os lembretes de factura', 21, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('N�mero de dias ap�s a gera��o da factura para o primeiro lembrete', 22, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('N�mero de dias para o pr�ximo lembrete', 23, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uzbekist�o', 227, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nauru', 147, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nepal', 148, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Holanda', 149, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Antilhas Holandesas', 150, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nova Caled�nia', 151, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nova Zel�ndia', 152, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nicar�gua', 153, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Niger', 154, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nig�ria', 155, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Niue', 156, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Norfolk', 157, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Coreia do Norte', 158, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Mariana do Norte', 159, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Noruega', 160, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Oman', 161, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pakist�o', 162, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Palau', 163, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Panama', 164, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Papua Nova Guin�', 165, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paraguai', 166, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Per�', 167, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Filipinas', 168, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Pitcairn', 169, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pol�nia', 170, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Portugal', 171, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Porto Rico', 172, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Qatar', 173, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Reuni�o', 174, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rom�nia', 175, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('R�ssia', 176, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rwanda', 177, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Samoa', 178, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('S�o Marino', 179, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('S�o Tom� e Princepe', 180, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ar�bia Saudita', 181, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Senegal', 182, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('S�rvia e Montenegro', 183, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Seychelles', 184, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Serra Leoa', 185, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Singapure', 186, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Eslov�quia', 187, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Eslov�nia', 188, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Salom�o', 189, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Som�lia', 190, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('�frica do Sul', 191, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Georgia do Sul e Ilhas Sandwich South', 192, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Espanha', 193, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sri Lanka', 194, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sta. Helena', 195, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sta. Kitts e Nevis', 196, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sta. Lucia', 197, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sta. Pierre e Miquelon', 198, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sto. Vicente e Grenadines', 199, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sud�o', 200, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suriname', 201, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Svalbard e Jan Mayen', 202, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Swazil�ndia', 203, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Su�cia', 204, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Su��a', 205, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('S�ria', 206, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Taiwan', 207, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tajikist�o', 208, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tanz�nia', 209, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Thail�ndia', 210, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Togo', 211, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tokelau', 212, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tonga', 213, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Trinidade e Tobago', 214, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tun�sia', 215, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Turquia', 216, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Turkmenist�o', 217, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Turks e Caicos', 218, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Tuvalu', 219, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uganda', 220, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ucr�nia', 221, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Emiados �rabes Unidos', 222, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Reino Unido', 223, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Estados Unidos', 224, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Estados Unidos e Ilhas Menores Circundantes', 225, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uruguai', 226, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Vanuatu', 228, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cidade do Vaticano', 229, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Venezuela', 230, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Vietname', 231, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Virgens Brit�nicas', 232, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Virgens', 233, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rep�blica Democr�tica do Congo', 57, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dinamarca', 58, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Djibouti', 59, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dominica', 60, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rep�blica Dominicana', 61, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Timor Leste', 62, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ecuador', 63, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Egipto', 64, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('El Salvador', 65, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guin� Equatorial', 66, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Eritreia', 67, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Est�nia', 68, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Etiopia', 69, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Malvinas', 70, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Faro�', 71, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Fiji', 72, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Finl�ndia', 73, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Fran�a', 74, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guiana Francesa', 75, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Polin�sia Francesa', 76, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Terras Ant�rticas e do Sul Francesas', 77, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gab�o', 78, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('G�mbia', 79, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Georgia', 80, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Alemanha', 81, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gana', 82, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gibraltar', 83, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gr�cia', 84, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Gronel�ndia', 85, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Granada', 86, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guadalupe', 87, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guantanamo', 88, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guatemala', 89, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guin�', 90, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guin�-Bissau', 91, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Guiana', 92, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Haiti', 93, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Heard e McDonald', 94, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Honduras', 95, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Hong Kong SAR', 96, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Hungria', 97, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Isl�ndia', 98, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('�ndia', 99, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Indon�sia', 100, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ir�o', 101, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Iraque', 102, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Irlanda', 103, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Israel', 104, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('It�lia', 105, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Jamaica', 106, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Jap�o', 107, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Jord�nia', 108, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kazaquist�o', 109, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('K�nia', 110, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kiribati', 111, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Coreia', 112, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kuwait', 113, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Kirgist�o', 114, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Laos', 115, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Latvia', 116, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('L�bano', 117, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lesoto', 118, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lib�ria', 119, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('L�bia', 120, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Liechtenstein', 121, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Litu�nia', 122, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Luxemburgo', 123, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Macau SAR', 124, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Maced�nia, Antiga Rep�blica Jugoslava da', 125, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Madag�scar', 126, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malaui', 127, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mal�sia', 128, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Maldivas', 129, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mali', 130, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Malta', 131, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Marshall', 132, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Martinica', 133, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Maurit�nia', 134, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Maur�cias', 135, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Maiote', 136, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('M�xico', 137, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Micron�sia', 138, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Moldova', 139, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('M�naco', 140, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mong�lia', 141, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Monserrate', 142, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Marrocos', 143, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mo�ambique', 144, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mianmar', 145, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Nam�bia', 146, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Afganist�o', 1, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Alb�nia', 2, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Alg�ria', 3, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Samoa Americana', 4, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Andorra', 5, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Angola', 6, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Anguilha', 7, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ant�rtida', 8, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Antigua e Barbuda', 9, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Argentina', 10, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Arm�nia', 11, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Aruba', 12, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Austr�lia', 13, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('�ustria', 14, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Azerbeij�o', 15, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bahamas', 16, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bahrain', 17, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bangladesh', 18, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Barbados', 19, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Belar�ssia', 20, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('B�lgica', 21, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Belize', 22, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Benin', 23, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bermuda', 24, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('But�o', 25, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bol�via', 26, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bosnia e Herzegovina', 27, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Botswana', 28, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilha Bouvet', 29, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Brasil', 30, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Territ�rio Brit�nico do Oceano �ndico', 31, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Brunei', 32, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Bulg�ria', 33, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Burquina Faso', 34, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Burundi', 35, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cambodia', 36, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Camar�es', 37, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Canada', 38, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cabo Verde', 39, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Caim�o', 40, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rep�blica Centro Africana', 41, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Chade', 42, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Chile', 43, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('China', 44, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilha Natal', 45, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilha Cocos e Keeling', 46, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Col�mbia', 47, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Comoros', 48, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Congo', 49, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ilhas Cook', 50, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Costa Rica', 51, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Costa do Marfim', 52, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cro�cia', 53, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cuba', 54, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Chipre', 55, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rep�blica Checa', 56, 2, 'description', 64);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('D�lares Norte Americanos', 1, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('D�lares Canadianos', 2, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Euro', 3, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ien', 4, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Libras Estrelinas', 5, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Won', 6, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Franco Su��o', 7, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Coroa Sueca', 8, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('M�s', 1, 2, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Semana', 2, 2, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dia', 3, 2, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ano', 4, 2, 'description', 6);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Email', 1, 2, 'description', 7);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Papel', 2, 2, 'description', 7);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Activo', 1, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Em aberto', 2, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Em aberto 2', 3, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Em aberto 3', 4, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspensa', 5, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspensa 2', 6, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspensa 3', 7, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Eliminado', 8, 2, 'description', 9);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um utilizador interno com todas as permiss�es', 1, 2, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Interno', 1, 2, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('O super utilizador de uma entidade', 2, 2, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Super utilizador', 2, 2, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um operador de factura��o', 3, 2, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Operador', 3, 2, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um parceiro que vai angariar clientes', 4, 2, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro', 4, 2, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um cliente que vai fazer pesquisas na sua conta', 5, 2, 'description', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cliente', 5, 2, 'title', 60);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Vez �nica', 1, 2, 'description', 17);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Items', 1, 2, 'description', 18);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Imposto', 2, 2, 'description', 18);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pr� pago', 1, 2, 'description', 19);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('P�s pago', 2, 2, 'description', 19);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Activo', 1, 2, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Terminado', 2, 2, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspenso', 3, 2, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Cheque', 1, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Visa', 2, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('MasterCard', 3, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Com sucesso', 1, 2, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sem sucesso', 2, 2, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Processador indispon�vel', 3, 2, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Inserido', 4, 2, 'description', 41);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Processo de Factura��o', 1, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manuten��o de Utilizador', 2, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item de manuten��o', 3, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item tipo de manuten��o', 4, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item manuten��o de pre�o de utilizador', 5, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manuten��o de promo��o', 6, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manuten��o por ordem', 7, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Manuten��o de cart�o de cr�dito', 8, 2, 'description', 46);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uma ordem pr�-paga tem tempo n�o facturado anterior � data de factura��o', 1, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A ordem n�o tem nenhum per�odo activo � data de processamento.', 2, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pelo menos um per�odo completo tem de ser factur�vel.', 3, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('J� h� factura��o para o per�odo.', 4, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Esta ordem teve de ser marcada para exclus�o do �ltimo processo.', 5, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pre-paid order is being process after its expiration.', 6, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A linha marcada foi eliminada.', 7, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A senha de utilizador foi alterada.', 8, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Uma linha foi actualizada.', 9, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A correr um processo de factura��o, foi encontrada uma revis�o rejeitada.', 10, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A correr um processo de factura��o, uma � necess�ria mas n�o encontrada.', 11, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um status de utilizador foi alterado.', 12, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um status de uma ordem foi alterado.', 13, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um utilizador foi inserido no processo de antiguidade, mas n�o h� mais passos configurados.', 14, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Um parceiro tem um pagamento a receber, mas n�o tem instrumento de pagamento.', 15, 2, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Processar pagamento com processo de factura��o', 1, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('URL ou ficheiro CSS', 2, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('URL ou gr�fico de logotipo', 3, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Per�odo de gra�a', 4, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Percentagem do parceiro', 5, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Valor de refer�ncia do parceiro', 6, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro pagamento �nico', 7, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro unidade do per�odo de pagamento', 8, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro valor do per�odo de pagamento', 9, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro pagamento autom�tico', 10, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador respons�vel pelos parceiros', 11, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro moeda', 12, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Entrega pelo mesmo das facturas em papel', 13, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Factura (email)', 1, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Reactivado', 2, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Em Atraso', 3, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Em Atraso 2', 4, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Em Atraso 3', 5, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Suspenso', 6, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Suspenso 2', 7, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Suspenso 3', 8, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Utilizador Eliminado', 9, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pagamento Remascente', 10, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parceiro Pagamento', 11, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Factura (papel)', 12, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Data Factura Fim do M�s', 24, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('D�lar da Singapura', 9, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ringgit Malasiano', 10, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('D�lar Australiano', 11, 2, 'description', 4);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pr�ximo n�mero de factura', 19, 2, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Email + Papel', 3, 2, 'description', 7);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('PayPal', 8, 2, 'description', 35);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Actualizar Cart�o de Cr�dito', 19, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Suspender (auto)', 4, 2, 'description', 20);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Penalidade', 3, 2, 'description', 18);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Senha esquecida', 20, 2, 'description', 52);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('None', 1, 1, 'description', 89);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Pre-paid balance', 2, 1, 'description', 89);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Credit limit', 3, 1, 'description', 89);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Done and billable', 1, 1, 'description', 91);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Done and not billable', 2, 1, 'description', 91);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Error detected', 3, 1, 'description', 91);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Error declared', 4, 1, 'description', 91);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Running', 1, 1, 'description', 92);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Finished: successful', 2, 1, 'description', 92);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Finished: failed', 3, 1, 'description', 92);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Item management and order line total calculation', 1, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Billing process: order filters', 2, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Billing process: invoice filters', 3, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice presentation', 4, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Billing process: order periods calculation', 5, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment gateway integration', 6, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Notifications', 7, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment instrument selection', 8, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Penalties for overdue invoices', 9, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Alarms when a payment gateway is down', 10, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Subscription status manager', 11, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Parameters for asynchronous payment processing', 12, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Add one product to order', 13, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Product pricing', 14, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mediation Reader', 15, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mediation Processor', 16, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Generic internal events listener', 17, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('External provisioning processor', 18, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Purchase validation against pre-paid balance / credit limit', 19, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Billing process: customer selection', 20, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mediation Error Handler', 21, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Scheduled Plug-ins', 22, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Generators', 23, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ageing for customers with overdue invoices', 24, 1, 'description', 23);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default order totals', 1, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Calculates the order total and the total for each line, considering the item prices, the quantity and if the prices are percentage or not.', 1, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('VAT', 2, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Adds an additional line to the order with a percentage charge to represent the value added tax.', 2, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice due date', 3, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A very simple implementation that sets the due date of the invoice. The due date is calculated by just adding the period of time to the invoice date.', 3, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default invoice composition.', 4, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This task will copy all the lines on the orders and invoices to the new invoice, considering the periods involved for each order, but not the fractions of periods. It will not copy the lines that are taxes. The quantity and total of each line will be multiplied by the amount of periods.', 4, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Standard Order Filter', 5, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Decides if an order should be included in an invoice for a given billing process.  This is done by taking the billing process time span, the order period, the active since/until, etc.', 5, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Standard Invoice Filter', 6, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Always returns true, meaning that the overdue invoice will be carried over to a new invoice.', 6, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default Order Periods', 7, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Calculates the start and end period to be included in an invoice. This is done by taking the billing process time span, the order period, the active since/until, etc.', 7, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Authorize.net payment processor', 8, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Integration with the authorize.net payment gateway.', 8, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Standard Email Notification', 9, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Notifies a user by sending an email. It supports text and HTML emails', 9, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default payment information', 10, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Finds the information of a payment method available to a customer, given priority to credit card. In other words, it will return the credit car of a customer or the ACH information in that order.', 10, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Testing plug-in for partner payouts', 11, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Plug-in useful only for testing', 11, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('PDF invoice notification', 12, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Will generate a PDF version of an invoice.', 12, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('No invoice carry over', 14, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Returns always false, which makes jBilling to never carry over an invoice into another newer invoice.', 14, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default interest task', 15, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Will create a new order with a penalty item. The item is taken as a parameter to the task.', 15, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Anticipated order filter', 16, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Extends BasicOrderFilterTask, modifying the dates to make the order applicable a number of months before it would be by using the default filter.', 16, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Anticipate order periods.', 17, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Extends BasicOrderPeriodTask, modifying the dates to make the order applicable a number of months before itd be by using the default task.', 17, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Email & process authorize.net', 19, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Extends the standard authorize.net payment processor to also send an email to the company after processing the payment.', 19, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment gateway down alarm', 20, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sends an email to the billing administrator as an alarm when a payment gateway is down.', 20, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Test payment processor', 21, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A test payment processor implementation to be able to test jBillings functions without using a real payment gateway.', 21, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Router payment processor based on Custom Fields', 22, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Allows a customer to be assigned a specific payment gateway. It checks a custom contact field to identify the gateway and then delegates the actual payment processing to another plugin.', 22, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default subscription status manager', 23, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('It determines how a payment event affects the subscription status of a user, considering its present status and a state machine.', 23, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('ACH Commerce payment processor', 24, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Integration with the ACH commerce payment gateway.', 24, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Standard asynchronous parameters', 25, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A dummy task that does not add any parameters for asynchronous payment processing. This is the default.', 25, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Router asynchronous parameters', 26, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plug-in adds parameters for asynchronous payment processing to have one processing message bean per payment processor. It is used in combination with the router payment processor plug-ins.', 26, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Standard Item Manager', 28, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('It adds items to an order. If the item is already in the order, it only updates the quantity.', 28, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Item Manager', 29, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in. It will do what the basic item manager does (actually calling it); but then it will execute external rules as well. These external rules have full control on changing the order that is getting new items.', 29, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Line Total', 30, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in. It calculates the total for an order line (typically this is the price multiplied by the quantity); allowing for the execution of external rules.', 30, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Pricing', 31, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in. It gives a price to an item by executing external rules. You can then add logic externally for pricing. It is also integrated with the mediation process by having access to the mediation pricing data.', 31, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Separator file reader', 32, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a reader for the mediation process. It reads records from a text file whose fields are separated by a character (or string).', 32, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules mediation processor', 33, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in (see chapter 7). It takes an event record from the mediation process and executes external rules to translate the record into billing meaningful data. This is at the core of the mediation component, see the ?Telecom Guide? document for more information.', 33, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Fixed length file reader', 34, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a reader for the mediation process. It reads records from a text file whose fields have fixed positions,and the record has a fixed length.', 34, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment information without validation', 35, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is exactly the same as the standard payment information task, the only difference is that it does not validate if the credit card is expired. Use this plug-in only if you want to submit payment with expired credit cards.', 35, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Notification task for testing', 36, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plug-in is only used for testing purposes. Instead of sending an email (or other real notification); it simply stores the text to be sent in a file named emails_sent.txt.', 36, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order periods calculator with pro rating.', 37, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plugin takes into consideration the field cycle starts of orders to calculate fractional order periods.', 37, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice composition task with pro-rating (day as fraction)', 38, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('When creating an invoice from an order, this plug-in will pro-rate any fraction of a period taking a day as the smallest billable unit.', 38, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment process for the Intraanuity payment gateway', 39, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Integration with the Intraanuity payment gateway.', 39, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Automatic cancellation credit.', 40, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plug-in will create a new order with a negative price to reflect a credit when an order is canceled within a period that has been already invoiced.', 40, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Fees for early cancellation of a plan.', 41, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plug-in will use external rules to determine if an order that is being canceled should create a new order with a penalty fee. This is typically used for early cancels of a contract.', 41, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Blacklist filter payment processor.', 42, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Used for blocking payments from reaching real payment processors. Typically configured as first payment processor in the processing chain.', 42, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Blacklist user when their status becomes suspended or higher.', 43, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Causes users and their associated details (e.g., credit card number, phone number, etc.) to be blacklisted when their status becomes suspended or higher. ', 43, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('JDBC Mediation Reader.', 44, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a reader for the mediation process. It reads records from a JDBC database source.', 44, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('MySQL Mediation Reader.', 45, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a reader for the mediation process. It is an extension of the JDBC reader, allowing easy configuration of a MySQL database source.', 45, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Provisioning commands rules task.', 46, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Responds to order related events. Runs rules to generate commands to send via JMS messages to the external provisioning module.', 46, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Test external provisioning task.', 47, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plug-in is only used for testing purposes. It is a test external provisioning task for testing the provisioning modules.', 47, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('CAI external provisioning task.', 48, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('An external provisioning plug-in for communicating with the Ericsson Customer Administration Interface (CAI).', 48, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Currency Router payment processor', 49, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delegates the actual payment processing to another plug-in based on the currency of the payment.', 49, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('MMSC external provisioning task.', 50, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('An external provisioning plug-in for communicating with the TeliaSonera MMSC.', 50, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Filters out negative invoices for carry over.', 51, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This filter will only invoices with a positive balance to be carried over to the next invoice.', 51, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('File invoice exporter.', 52, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('It will generate a file with one line per invoice generated.', 52, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules caller on an event.', 53, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('It will call a package of rules when an internal event happens.', 53, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dynamic balance manager', 54, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('It will update the dynamic balance of a customer (pre-paid or credit limit) when events affecting the balance happen.', 54, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Balance validator based on the customer balance.', 55, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Used for real-time mediation, this plug-in will validate a call based on the current dynamic balance of a customer.', 55, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Balance validator based on rules.', 56, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Used for real-time mediation, this plug-in will validate a call based on a package or rules', 56, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment processor for Payments Gateway.', 57, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Integration with the Payments Gateway payment processor.', 57, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Credit cards are stored externally.', 58, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Saves the credit card information in the payment gateway, rather than the jBilling DB.', 58, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Item Manager 2', 59, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in compatible with the mediation module of jBilling 2.2.x. It will do what the basic item manager does (actually calling it); but then it will execute external rules as well. These external rules have full control on changing the order that is getting new items.', 59, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Line Total - 2', 60, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in, compatible with the mediation process of jBilling 2.2.x and later. It calculates the total for an order line (typically this is the price multiplied by the quantity); allowing for the execution of external rules.', 60, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Rules Pricing 2', 61, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This is a rules-based plug-in compatible with the mediation module of jBilling 2.2.x. It gives a price to an item by executing external rules. You can then add logic externally for pricing. It is also integrated with the mediation process by having access to the mediation pricing data.', 61, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Test payment processor for external storage.', 63, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A fake plug-in to test payments that would be stored externally.', 63, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('WorldPay integration', 64, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment processor plug-in to integrate with RBS WorldPay', 64, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('WorldPay integration with external storage', 65, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment processor plug-in to integrate with RBS WorldPay. It stores the credit card information (number, etc) in the gateway.', 65, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Auto recharge', 66, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Monitors the balance of a customer and upon reaching a limit, it requests a real-time payment', 66, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Beanstream gateway integration', 67, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment processor for integration with the Beanstream payment gateway', 67, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Sage payments gateway integration', 68, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment processor for integration with the Sage payment gateway', 68, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Standard billing process users filter', 69, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Called when the billing process runs to select which users to evaluate. This basic implementation simply returns every user not in suspended (or worse) status', 69, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Selective billing process users filter', 70, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Called when the billing process runs to select which users to evaluate. This only returns users with orders that have a next invoice date earlier than the billing process.', 70, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mediation file error handler', 71, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Event records with errors are saved to a file', 71, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mediation data base error handler', 73, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Event records with errors are saved to a database table', 73, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paypal integration with external storage', 75, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Submits payments to paypal as a payment gateway and stores credit card information in PayPal as well', 75, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Authorize.net integration with external storage', 76, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Submits payments to authorize.net as a payment gateway and stores credit card information in authorize.net as well', 76, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment method router payment processor', 77, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delegates the actual payment processing to another plug-in based on the payment method of the payment.', 77, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Dynamic rules generator', 78, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Generates rules dynamically based on a Velocity template.', 78, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Mediation Process Task', 79, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A scheduled task to execute the Mediation Process.', 79, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Billing Process Task', 80, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A scheduled task to execute the Billing Process.', 80, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Basic ageing', 87, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ages a user based on the number of days that the account is overdue.', 87, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ageing process task', 88, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A scheduled task to execute the Ageing Process.', 88, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Business day ageing', 89, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Ages a user based on the number of business days (excluding holidays) that the account is overdue.', 89, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Simple Tax Composition Task', 90, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A pluggable task of the type AbstractChargeTask to apply tax item to an Invoice with a facility of exempting an exempt item or an exemp customer.', 90, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Country Tax Invoice Composition Task', 91, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A pluggable task of the type AbstractChargeTask to apply tax item to the Invoice if the Partner''s country code is matching.', 91, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment Terms Penalty Task', 92, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A pluggable task of the type AbstractChargeTask to apply a Penalty to an Invoice having a due date beyond a configurable days period.', 92, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Fees for early cancellation of a subscription', 93, 1, 'title', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('This plug-in will create a new order with a fee if a recurring order is cancelled too early', 93, 1, 'description', 24);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The order line has been updated', 17, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The order next billing date has been changed', 18, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Last API call to get the the user subscription status transitions', 19, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User subscription status has changed', 20, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User account is now locked', 21, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The order main subscription flag was changed', 22, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('All the one-time orders the mediation found were in status finished', 23, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A valid payment method was not found. The payment request was cancelled', 24, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A new row has been created', 25, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('An invoiced order was cancelled, a credit order was created', 26, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A user id was added to the blacklist', 27, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A user id was removed from the blacklist', 28, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Posted a provisioning command using a UUID', 29, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A command was posted for provisioning', 30, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The provisioning status of an order line has changed', 31, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('User subscription status has NOT changed', 32, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The dynamic balance of a user has changed', 33, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The invoice if child flag has changed', 34, 1, 'description', 47);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice Reports', 1, 1, 'description', 101);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Order Reports', 2, 1, 'description', 101);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payment Reports', 3, 1, 'description', 101);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Customer Reports', 4, 1, 'description', 101);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Total amount invoiced grouped by period.', 1, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Detailed balance ageing report. Shows the age of outstanding customer balances.', 2, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Number of users subscribed to a specific product.', 3, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Total payment amount received grouped by period.', 4, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Number of customers created within a period.', 5, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Total revenue (sum of received payments) per customer.', 6, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Simple accounts receivable report showing current account balances.', 7, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('General ledger details of all invoiced charges for the given day.', 8, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('General ledger summary of all invoiced charges for the given day, grouped by item type.', 9, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Plan pricing history for all plan products and start dates.', 10, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Total invoiced per customer grouped by product category.', 11, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Total invoiced per customer over years grouped by year.', 12, 1, 'description', 100);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoices', 1, 1, 'description', 104);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Orders', 2, 1, 'description', 104);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Payments', 3, 1, 'description', 104);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Users', 4, 1, 'description', 104);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use overdue penalties (interest).', 25, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use order anticipation.', 27, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paypal account.', 28, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paypal button URL.', 29, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('URL for HTTP ageing callback.', 30, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use continuous invoice dates.', 31, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Attach PDF invoice to email notification.', 32, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Force one order per invoice.', 33, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Add order Id to invoice lines.', 35, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Allow customers to edit own contact information.', 36, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Hide (mask) credit card numbers.', 37, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Link ageing to customer subscriber status.', 38, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Lock-out user after failed login attempts.', 39, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Expire user passwords after days.', 40, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use main-subscription orders.', 41, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use pro-rating.', 42, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use payment blacklist.', 43, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Allow negative payments.', 44, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delay negative invoice payments.', 45, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Allow invoice without orders.', 46, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Last read mediation record id.', 47, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Use provisioning.', 48, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Automatic customer recharge threshold.', 49, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Invoice decimal rounding.', 50, 1, 'description', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Grace period in days before ageing a customer with an overdue invoice.', 4, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner default percentage commission rate. See the Partner section of the documentation.', 5, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner default flat fee to be paid as commission. See the Partner section of the documentation.', 6, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to enable one-time payment for partners. If set, partners will only get paid once per customer. See the Partner section of the documentation.', 7, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner default payout period unit. See the Partner section of the documentation.', 8, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner default payout period value. See the Partner section of the documentation.', 9, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to enable batch payment payouts using the billing process and the configured payment processor. See the Partner section of the documentation.', 10, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Partner default assigned clerk id. See the Partner section of the documentation.', 11, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Currency ID to use when paying partners. See the Partner section of the documentation.', 12, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to e-mail invoices as the billing company. ''0'' to deliver invoices as jBilling.', 13, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to show notes in invoices, ''0'' to disable.', 14, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Days before the orders ''active until'' date to send the 1st notification. Leave blank to disable.', 15, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Days before the orders ''active until'' date to send the 2nd notification. Leave blank to disable.', 16, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Days before the orders ''active until'' date to send the 3rd notification. Leave blank to disable.', 17, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Prefix value for generated invoice public numbers.', 18, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The current value for generated invoice public numbers. New invoices will be assigned a public number by incrementing this value.', 19, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow invoices to be deleted, ''0'' to disable.', 20, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow invoice reminder notifications, ''0'' to disable.', 21, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to enable, ''0'' to disable.', 24, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to enable the billing process to calculate interest on overdue payments, ''0'' to disable. Calculation of interest is handled by the selected penalty plug-in.', 25, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to use the ''OrderFilterAnticipateTask'' to invoice a number of months in advance, ''0'' to disable. Plug-in must be configured separately.', 27, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('PayPal account name.', 28, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('A URL where the graphic of the PayPal button resides. The button is displayed to customers when they are making a payment. The default is usually the best option, except when another language is needed.', 29, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('URL for the HTTP Callback to invoke when the ageing process changes a status of a user.', 30, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Default ''2000-01-01''. If this preference is used, the system will make sure that all your invoices have their dates in a incremental way. Any invoice with a greater ''ID'' will also have a greater (or equal) date. In other words, a new invoice can not have an earlier date than an existing (older) invoice. To use this preference, set it as a string with the date where to start.', 31, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to attach a PDF version of the invoice to all invoice notification e-mails. ''0'' to disable.', 32, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to show the ''include in separate invoice'' flag on an order. ''0'' to disable.', 33, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to include the ID of the order in the description text of the resulting invoice line. ''0'' to disable. This can help to easily track which exact orders is responsible for a line in an invoice, considering that many orders can be included in a single invoice.', 35, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow customers to edit their own contact information. ''0'' to disable.', 36, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to mask all credit card numbers. ''0'' to disable. When set, numbers are masked to all users, even administrators, and in all log files.', 37, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to change the subscription status of a user when the user ages. ''0'' to disable.', 38, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The number of retries to allow before locking the user account. A locked user account will have their password changed to the value of lockout_password in the jbilling.properties configuration file.', 39, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('If greater than zero, it represents the number of days that a password is valid. After those days, the password is expired and the user is forced to change it.', 40, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow the usage of the ''main subscription'' flag for orders This flag is read only by the mediation process when determining where to place charges coming from external events.', 41, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow the use of pro-rating to invoice fractions of a period. Shows the ''cycle'' attribute of an order. Note that you need to configure the corresponding plug-ins for this feature to be fully functional.', 42, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('If the payment blacklist feature is used, this is set to the id of the configuration of the PaymentFilterTask plug-in. See the Blacklist section of the documentation.', 43, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow negative payments. ''0'' to disable', 44, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to delay payment of negative invoice amounts, causing the balance to be carried over to the next invoice. Invoices that have had negative balances from other invoices transferred to them are allowed to immediately make a negative payment (credit) if needed. ''0'' to disable. Preference 44 & 46 are usually also enabled.', 45, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow invoices with negative balances to generate a new invoice that isn''t composed of any orders so that their balances will always get carried over to a new invoice for the credit to take place. ''0'' to disable. Preference 44 & 45 are usually also enabled.', 46, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('ID of the last record read by the mediation process. This is used to determine what records are ''new'' and need to be read.', 47, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Set to ''1'' to allow the use of provisioning. ''0'' to disable.', 48, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The threshold value for automatic payments. Pre-paid users with an automatic recharge value set will generate an automatic payment whenever the account balance falls below this threshold. Note that you need to configure the AutoRechargeTask plug-in for this feature to be fully functional.', 49, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('The number of decimal places to be shown on the invoice. Defaults to 2.', 50, 1, 'instruction', 50);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Paid', 1, 1, 'description', 90);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Unpaid', 2, 1, 'description', 90);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Carried', 3, 1, 'description', 90);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create customer', 10, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit customer', 11, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete customer', 12, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Inspect customer', 13, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Blacklist customer', 14, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View customer details', 15, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Download customer CSV', 16, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View all customers', 17, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View customer sub-accounts', 18, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create order', 20, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit order', 21, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete order', 22, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Generate invoice for order', 23, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View order details', 24, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Download order CSV', 25, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit line price', 26, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit line description', 27, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View all customers', 28, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View customer sub-accounts', 29, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create payment', 30, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit payment', 31, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete payment', 32, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Link payment to invoice', 33, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View payment details', 34, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Download payment CSV', 35, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View all customers', 36, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View customer sub-accounts', 37, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create product', 40, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit product', 41, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete product', 42, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View product details', 43, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Download payment CSV', 44, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create product category', 50, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit product category', 51, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete product category', 52, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create plan', 60, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit plan', 61, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete plan', 62, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View plan details', 63, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete invoice', 70, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Send invoice notification', 71, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View invoice details', 72, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Download invoice CSV', 73, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View all customers', 74, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View customer sub-accounts', 75, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Approve / Disapprove review', 80, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show customer menu', 90, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show invoices menu', 91, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show order menu', 92, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show payments & refunds menu', 93, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show billing menu', 94, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show mediation menu', 95, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show reports menu', 96, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show products menu', 97, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show plans menu', 98, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show configuration menu', 99, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Show partner menu', 100, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Create partner', 101, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Edit partner', 102, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Delete partner', 103, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('View partner details', 104, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Switch to sub-account', 110, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Switch to any user', 111, 1, 'description', 59);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('Web Service API access', 120, 1, 'description', 59);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aristokrates (generated)', '', NOW(), 'Insert Row (x1109)', 'EXECUTED', 'descriptors/database/jbilling-init_data.xml', '1337972683141-32', '2.0.5', '3:fbd64c2395e2fad0067fdd2d799dd6fb', 32);

