Liquibase Home is not set.
Liquibase Home: /home/aristokrates/jbilling/liquidbase-2.0.5
-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: descriptors/database/jbilling-schema.xml
-- Ran at: 11/07/12 3:03 PM
-- Against: jbilling@jdbc:postgresql://localhost:5432/jbilling_test
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITH TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

INSERT INTO databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE databasechangelog (ID VARCHAR(63) NOT NULL, AUTHOR VARCHAR(63) NOT NULL, FILENAME VARCHAR(200) NOT NULL, DATEEXECUTED TIMESTAMP WITH TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONSTRAINT PK_DATABASECHANGELOG PRIMARY KEY (ID, AUTHOR, FILENAME));

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-126::Emiliano Conde::(Checksum: 3:1fd8d77ec2f6825ef34e72f3f18d98ec)
ALTER TABLE ach ADD CONSTRAINT ach_fk_1 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-126', '2.0.5', '3:1fd8d77ec2f6825ef34e72f3f18d98ec', 1);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-127::Emiliano Conde::(Checksum: 3:e627b1462ec9fa48fe8c94a7158f9426)
ALTER TABLE ageing_entity_step ADD CONSTRAINT ageing_entity_step_fk_2 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-127', '2.0.5', '3:e627b1462ec9fa48fe8c94a7158f9426', 2);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-128::Emiliano Conde::(Checksum: 3:7b73f7f334c1c17af0a01f0140349a09)
ALTER TABLE base_user ADD CONSTRAINT base_user_fk_5 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-128', '2.0.5', '3:7b73f7f334c1c17af0a01f0140349a09', 3);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-129::Emiliano Conde::(Checksum: 3:3b7db9646e41a1bc0b86e364ad427119)
ALTER TABLE base_user ADD CONSTRAINT base_user_fk_3 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-129', '2.0.5', '3:3b7db9646e41a1bc0b86e364ad427119', 4);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-130::Emiliano Conde::(Checksum: 3:cbdd59b0175b5578ee710444e5b232f5)
ALTER TABLE base_user ADD CONSTRAINT base_user_fk_4 FOREIGN KEY (language_id) REFERENCES language (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-130', '2.0.5', '3:cbdd59b0175b5578ee710444e5b232f5', 5);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-131::Emiliano Conde::(Checksum: 3:d5d593a11353d3d98e54c5c4dabd0a05)
ALTER TABLE billing_process ADD CONSTRAINT billing_process_fk_2 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-131', '2.0.5', '3:d5d593a11353d3d98e54c5c4dabd0a05', 6);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-132::Emiliano Conde::(Checksum: 3:98b468d44e6fb46ca0e4680b96e8f3b1)
ALTER TABLE billing_process ADD CONSTRAINT billing_process_fk_3 FOREIGN KEY (paper_invoice_batch_id) REFERENCES paper_invoice_batch (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-132', '2.0.5', '3:98b468d44e6fb46ca0e4680b96e8f3b1', 7);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-133::Emiliano Conde::(Checksum: 3:5cee909a5d7ebb600f5024b1303efa8d)
ALTER TABLE billing_process ADD CONSTRAINT billing_process_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-133', '2.0.5', '3:5cee909a5d7ebb600f5024b1303efa8d', 8);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-134::Emiliano Conde::(Checksum: 3:c82e85c0c63e54e3a4426e617a80075a)
ALTER TABLE billing_process_configuration ADD CONSTRAINT billing_proc_configtn_fk_2 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-134', '2.0.5', '3:c82e85c0c63e54e3a4426e617a80075a', 9);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-135::Emiliano Conde::(Checksum: 3:1b60ee813784e281e3e35b86ab09c0a8)
ALTER TABLE billing_process_configuration ADD CONSTRAINT billing_proc_configtn_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-135', '2.0.5', '3:1b60ee813784e281e3e35b86ab09c0a8', 10);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-136::Emiliano Conde::(Checksum: 3:cae1e7104ce183c7d839340f6222541d)
ALTER TABLE blacklist ADD CONSTRAINT blacklist_fk_1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-136', '2.0.5', '3:cae1e7104ce183c7d839340f6222541d', 11);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-137::Emiliano Conde::(Checksum: 3:e79884389150c9fab7fb64618a350b1e)
ALTER TABLE blacklist ADD CONSTRAINT blacklist_fk_4 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-137', '2.0.5', '3:e79884389150c9fab7fb64618a350b1e', 12);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-138::Emiliano Conde::(Checksum: 3:a58175b0dac5de2550bae9d0a528ed06)
ALTER TABLE blacklist ADD CONSTRAINT blacklist_fk_2 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-138', '2.0.5', '3:a58175b0dac5de2550bae9d0a528ed06', 13);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-139::Emiliano Conde::(Checksum: 3:543abf849ceb37f4eaf06688bfecea2c)
ALTER TABLE contact_map ADD CONSTRAINT contact_map_fk_3 FOREIGN KEY (contact_id) REFERENCES contact (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-139', '2.0.5', '3:543abf849ceb37f4eaf06688bfecea2c', 14);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-140::Emiliano Conde::(Checksum: 3:decf7af7842f406a03d0cf048f4f4ae8)
ALTER TABLE contact_map ADD CONSTRAINT contact_map_fk_1 FOREIGN KEY (table_id) REFERENCES jbilling_table (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-140', '2.0.5', '3:decf7af7842f406a03d0cf048f4f4ae8', 15);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-141::Emiliano Conde::(Checksum: 3:9a710b784ab36b8968cdbfaebf9e975e)
ALTER TABLE contact_map ADD CONSTRAINT contact_map_fk_2 FOREIGN KEY (type_id) REFERENCES contact_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-141', '2.0.5', '3:9a710b784ab36b8968cdbfaebf9e975e', 16);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-142::Emiliano Conde::(Checksum: 3:4a7daa87875b4dd0d57f511229b60952)
ALTER TABLE contact_type ADD CONSTRAINT contact_type_fk_1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-142', '2.0.5', '3:4a7daa87875b4dd0d57f511229b60952', 17);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-143::Emiliano Conde::(Checksum: 3:f9aa8011b7dce8b332d84739bbcc99c8)
ALTER TABLE currency_entity_map ADD CONSTRAINT currency_entity_map_fk_2 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-143', '2.0.5', '3:f9aa8011b7dce8b332d84739bbcc99c8', 18);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-144::Emiliano Conde::(Checksum: 3:910a5a6aee89ebe7a733a06cf1142867)
ALTER TABLE currency_entity_map ADD CONSTRAINT currency_entity_map_fk_1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-144', '2.0.5', '3:910a5a6aee89ebe7a733a06cf1142867', 19);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-145::Emiliano Conde::(Checksum: 3:2de0a0ed919d7938ea216713312e543a)
ALTER TABLE currency_exchange ADD CONSTRAINT currency_exchange_fk_1 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-145', '2.0.5', '3:2de0a0ed919d7938ea216713312e543a', 20);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-146::Emiliano Conde::(Checksum: 3:096429fa1de682a99d89768798b5afbd)
ALTER TABLE customer ADD CONSTRAINT customer_fk_1 FOREIGN KEY (invoice_delivery_method_id) REFERENCES invoice_delivery_method (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-146', '2.0.5', '3:096429fa1de682a99d89768798b5afbd', 21);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-147::Emiliano Conde::(Checksum: 3:44f94241fda71bea2ede7f0038b15a21)
ALTER TABLE customer ADD CONSTRAINT customer_fk_2 FOREIGN KEY (partner_id) REFERENCES partner (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-147', '2.0.5', '3:44f94241fda71bea2ede7f0038b15a21', 22);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-148::Emiliano Conde::(Checksum: 3:6a71640663c7790b5feabf2b0847b2af)
ALTER TABLE customer ADD CONSTRAINT customer_fk_3 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-148', '2.0.5', '3:6a71640663c7790b5feabf2b0847b2af', 23);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-149::Emiliano Conde::(Checksum: 3:d7c1311626b441f3da9ff6d6aa330e98)
ALTER TABLE customer_meta_field_map ADD CONSTRAINT customer_meta_field_map_fk_1 FOREIGN KEY (customer_id) REFERENCES customer (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-149', '2.0.5', '3:d7c1311626b441f3da9ff6d6aa330e98', 24);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-150::Emiliano Conde::(Checksum: 3:bd78366bc6ad1381a6f85c21c40dadcd)
ALTER TABLE customer_meta_field_map ADD CONSTRAINT customer_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-150', '2.0.5', '3:bd78366bc6ad1381a6f85c21c40dadcd', 25);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-151::Emiliano Conde::(Checksum: 3:e446816be71a36ba499aef40389bb881)
ALTER TABLE customer_price ADD CONSTRAINT customer_price_plan_item_id_fk FOREIGN KEY (plan_item_id) REFERENCES plan_item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-151', '2.0.5', '3:e446816be71a36ba499aef40389bb881', 26);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-152::Emiliano Conde::(Checksum: 3:004fc6f9837b5251cafc763508aa75bf)
ALTER TABLE customer_price ADD CONSTRAINT customer_price_user_id_fk FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-152', '2.0.5', '3:004fc6f9837b5251cafc763508aa75bf', 27);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-153::Emiliano Conde::(Checksum: 3:d88c56e58f32801a4d33744da4bc112a)
ALTER TABLE entity ADD CONSTRAINT entity_fk_1 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-153', '2.0.5', '3:d88c56e58f32801a4d33744da4bc112a', 28);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-154::Emiliano Conde::(Checksum: 3:e8daeae151893ec6573b73f2245a0dc7)
ALTER TABLE entity ADD CONSTRAINT entity_fk_2 FOREIGN KEY (language_id) REFERENCES language (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-154', '2.0.5', '3:e8daeae151893ec6573b73f2245a0dc7', 29);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-155::Emiliano Conde::(Checksum: 3:e1c182342bfd7a50c593f04f3e5c9b1d)
ALTER TABLE entity_delivery_method_map ADD CONSTRAINT entity_delivry_methd_map_fk1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-155', '2.0.5', '3:e1c182342bfd7a50c593f04f3e5c9b1d', 30);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-156::Emiliano Conde::(Checksum: 3:a3caffb1ed8faed2ab394ec5370e0be9)
ALTER TABLE entity_delivery_method_map ADD CONSTRAINT entity_delivry_methd_map_fk2 FOREIGN KEY (method_id) REFERENCES invoice_delivery_method (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-156', '2.0.5', '3:a3caffb1ed8faed2ab394ec5370e0be9', 31);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-157::Emiliano Conde::(Checksum: 3:2a5da6a2366e95b82a631837358685d2)
ALTER TABLE entity_payment_method_map ADD CONSTRAINT entity_payment_method_map_fk_2 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-157', '2.0.5', '3:2a5da6a2366e95b82a631837358685d2', 32);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-158::Emiliano Conde::(Checksum: 3:135f19234cc9b2033a6a58bfaa738ebf)
ALTER TABLE entity_payment_method_map ADD CONSTRAINT entity_payment_method_map_fk_1 FOREIGN KEY (payment_method_id) REFERENCES payment_method (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-158', '2.0.5', '3:135f19234cc9b2033a6a58bfaa738ebf', 33);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-159::Emiliano Conde::(Checksum: 3:4c4c0d3bad01f418857f2239c148f8bf)
ALTER TABLE entity_report_map ADD CONSTRAINT report_map_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-159', '2.0.5', '3:4c4c0d3bad01f418857f2239c148f8bf', 34);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-160::Emiliano Conde::(Checksum: 3:161d162722f89c6b8990baa2b866b101)
ALTER TABLE entity_report_map ADD CONSTRAINT report_map_report_id_fk FOREIGN KEY (report_id) REFERENCES report (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-160', '2.0.5', '3:161d162722f89c6b8990baa2b866b101', 35);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-161::Emiliano Conde::(Checksum: 3:050dc222ebf928eb4ca382153058fc7a)
ALTER TABLE enumeration_values ADD CONSTRAINT enumeration_values_fk_1 FOREIGN KEY (enumeration_id) REFERENCES enumeration (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-161', '2.0.5', '3:050dc222ebf928eb4ca382153058fc7a', 36);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-162::Emiliano Conde::(Checksum: 3:0726716c5e83e11bf7536f3777da9912)
ALTER TABLE event_log ADD CONSTRAINT event_log_fk_6 FOREIGN KEY (affected_user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-162', '2.0.5', '3:0726716c5e83e11bf7536f3777da9912', 37);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-163::Emiliano Conde::(Checksum: 3:6ceb03f062b15f090c89c318c0a0446a)
ALTER TABLE event_log ADD CONSTRAINT event_log_fk_2 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-163', '2.0.5', '3:6ceb03f062b15f090c89c318c0a0446a', 38);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-164::Emiliano Conde::(Checksum: 3:8cb416a3198908287072b1982578f10c)
ALTER TABLE event_log ADD CONSTRAINT event_log_fk_5 FOREIGN KEY (message_id) REFERENCES event_log_message (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-164', '2.0.5', '3:8cb416a3198908287072b1982578f10c', 39);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-165::Emiliano Conde::(Checksum: 3:86d5544e78d2cfc0d48b65bf500def7b)
ALTER TABLE event_log ADD CONSTRAINT event_log_fk_1 FOREIGN KEY (module_id) REFERENCES event_log_module (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-165', '2.0.5', '3:86d5544e78d2cfc0d48b65bf500def7b', 40);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-166::Emiliano Conde::(Checksum: 3:7f92b7a790f11b27cfa9a5bc6ea3133c)
ALTER TABLE event_log ADD CONSTRAINT event_log_fk_4 FOREIGN KEY (table_id) REFERENCES jbilling_table (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-166', '2.0.5', '3:7f92b7a790f11b27cfa9a5bc6ea3133c', 41);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-167::Emiliano Conde::(Checksum: 3:492587d13bd9f4128f7c71365c4e515f)
ALTER TABLE event_log ADD CONSTRAINT event_log_fk_3 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-167', '2.0.5', '3:492587d13bd9f4128f7c71365c4e515f', 42);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-168::Emiliano Conde::(Checksum: 3:e624245340fe5cb417f30ff27633838e)
ALTER TABLE generic_status ADD CONSTRAINT generic_status_fk_1 FOREIGN KEY (dtype) REFERENCES generic_status_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-168', '2.0.5', '3:e624245340fe5cb417f30ff27633838e', 43);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-169::Emiliano Conde::(Checksum: 3:bdf9c4e8c6101b08177c09a8467dec56)
ALTER TABLE international_description ADD CONSTRAINT international_description_fk_1 FOREIGN KEY (language_id) REFERENCES language (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-169', '2.0.5', '3:bdf9c4e8c6101b08177c09a8467dec56', 44);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-170::Emiliano Conde::(Checksum: 3:1a4c196c15668472cccd5705be789361)
ALTER TABLE invoice ADD CONSTRAINT invoice_fk_1 FOREIGN KEY (billing_process_id) REFERENCES billing_process (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-170', '2.0.5', '3:1a4c196c15668472cccd5705be789361', 45);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-171::Emiliano Conde::(Checksum: 3:35e1d8538ae56373d2cbcc82a469ed7c)
ALTER TABLE invoice ADD CONSTRAINT invoice_fk_3 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-171', '2.0.5', '3:35e1d8538ae56373d2cbcc82a469ed7c', 46);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-172::Emiliano Conde::(Checksum: 3:f902167c3f74930c28cd3a46fdc26ce9)
ALTER TABLE invoice ADD CONSTRAINT invoice_fk_4 FOREIGN KEY (delegated_invoice_id) REFERENCES invoice (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-172', '2.0.5', '3:f902167c3f74930c28cd3a46fdc26ce9', 47);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-173::Emiliano Conde::(Checksum: 3:570e2fcfd4652630cb391ab947639e6f)
ALTER TABLE invoice ADD CONSTRAINT invoice_fk_2 FOREIGN KEY (paper_invoice_batch_id) REFERENCES paper_invoice_batch (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-173', '2.0.5', '3:570e2fcfd4652630cb391ab947639e6f', 48);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-174::Emiliano Conde::(Checksum: 3:28ac864e459c3bcb00fdcb07bbbb23e0)
ALTER TABLE invoice_line ADD CONSTRAINT invoice_line_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-174', '2.0.5', '3:28ac864e459c3bcb00fdcb07bbbb23e0', 49);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-175::Emiliano Conde::(Checksum: 3:e6609810566120d076d2f86784fb4af4)
ALTER TABLE invoice_line ADD CONSTRAINT invoice_line_fk_2 FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-175', '2.0.5', '3:e6609810566120d076d2f86784fb4af4', 50);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-176::Emiliano Conde::(Checksum: 3:c565a9d0b9197392f6b8864e26b993cb)
ALTER TABLE invoice_line ADD CONSTRAINT invoice_line_fk_3 FOREIGN KEY (type_id) REFERENCES invoice_line_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-176', '2.0.5', '3:c565a9d0b9197392f6b8864e26b993cb', 51);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-177::Emiliano Conde::(Checksum: 3:3ab0490358d6af9ad4c684900c4333ac)
ALTER TABLE invoice_meta_field_map ADD CONSTRAINT invoice_meta_field_map_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-177', '2.0.5', '3:3ab0490358d6af9ad4c684900c4333ac', 52);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-178::Emiliano Conde::(Checksum: 3:e63f0cde29ece99571b7ea1c27824fee)
ALTER TABLE invoice_meta_field_map ADD CONSTRAINT invoice_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-178', '2.0.5', '3:e63f0cde29ece99571b7ea1c27824fee', 53);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-179::Emiliano Conde::(Checksum: 3:030b915d7d6e43657c7c5bcab5fd23fc)
ALTER TABLE item ADD CONSTRAINT item_fk_1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-179', '2.0.5', '3:030b915d7d6e43657c7c5bcab5fd23fc', 54);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-180::Emiliano Conde::(Checksum: 3:5ead26f7fd7fb93840f40e5fc04f0046)
ALTER TABLE item_meta_field_map ADD CONSTRAINT item_meta_field_map_fk_1 FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-180', '2.0.5', '3:5ead26f7fd7fb93840f40e5fc04f0046', 55);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-181::Emiliano Conde::(Checksum: 3:b3a54a7f8a57429442ee9554ee78e283)
ALTER TABLE item_meta_field_map ADD CONSTRAINT item_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-181', '2.0.5', '3:b3a54a7f8a57429442ee9554ee78e283', 56);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-182::Emiliano Conde::(Checksum: 3:fbe6c5447b7b3a309d81810b512d368b)
ALTER TABLE item_price_timeline ADD CONSTRAINT item_pm_map_item_id_fk FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-182', '2.0.5', '3:fbe6c5447b7b3a309d81810b512d368b', 57);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-183::Emiliano Conde::(Checksum: 3:1dc93b214360cf43ea478ff14248c037)
ALTER TABLE item_price_timeline ADD CONSTRAINT item_pm_map_price_model_id_fk FOREIGN KEY (price_model_id) REFERENCES price_model (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-183', '2.0.5', '3:1dc93b214360cf43ea478ff14248c037', 58);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-184::Emiliano Conde::(Checksum: 3:d15085f0a3c6203ddfdd34f80f58c82c)
ALTER TABLE item_type ADD CONSTRAINT item_type_fk_1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-184', '2.0.5', '3:d15085f0a3c6203ddfdd34f80f58c82c', 59);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-185::Emiliano Conde::(Checksum: 3:cb7deebd5bdbba8c69a982b4999c62a3)
ALTER TABLE item_type_exclude_map ADD CONSTRAINT item_type_exclude_item_id_fk FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-185', '2.0.5', '3:cb7deebd5bdbba8c69a982b4999c62a3', 60);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-186::Emiliano Conde::(Checksum: 3:0a0b85871c59b51259e41147a0ba648c)
ALTER TABLE item_type_exclude_map ADD CONSTRAINT item_type_exclude_type_id_fk FOREIGN KEY (type_id) REFERENCES item_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-186', '2.0.5', '3:0a0b85871c59b51259e41147a0ba648c', 61);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-187::Emiliano Conde::(Checksum: 3:764682401f5b7917f367818b64bd6626)
ALTER TABLE item_type_map ADD CONSTRAINT item_type_map_fk_1 FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-187', '2.0.5', '3:764682401f5b7917f367818b64bd6626', 62);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-188::Emiliano Conde::(Checksum: 3:cd36ced5e84289f21ba26e63eafe5059)
ALTER TABLE item_type_map ADD CONSTRAINT item_type_map_fk_2 FOREIGN KEY (type_id) REFERENCES item_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-188', '2.0.5', '3:cd36ced5e84289f21ba26e63eafe5059', 63);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-189::Emiliano Conde::(Checksum: 3:83e4350dde7c24eea6f1395e69f625b1)
ALTER TABLE mediation_cfg ADD CONSTRAINT mediation_cfg_fk_1 FOREIGN KEY (pluggable_task_id) REFERENCES pluggable_task (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-189', '2.0.5', '3:83e4350dde7c24eea6f1395e69f625b1', 64);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-190::Emiliano Conde::(Checksum: 3:9bed5f03bfcfede738854277c9819c18)
ALTER TABLE mediation_order_map ADD CONSTRAINT mediation_order_map_fk_1 FOREIGN KEY (mediation_process_id) REFERENCES mediation_process (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-190', '2.0.5', '3:9bed5f03bfcfede738854277c9819c18', 65);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-191::Emiliano Conde::(Checksum: 3:a7a300725dc271da1641e00180ef5464)
ALTER TABLE mediation_order_map ADD CONSTRAINT mediation_order_map_fk_2 FOREIGN KEY (order_id) REFERENCES purchase_order (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-191', '2.0.5', '3:a7a300725dc271da1641e00180ef5464', 66);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-192::Emiliano Conde::(Checksum: 3:ea3c2b75d475ddd44a54bc86e471bd80)
ALTER TABLE mediation_process ADD CONSTRAINT mediation_process_fk_1 FOREIGN KEY (configuration_id) REFERENCES mediation_cfg (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-192', '2.0.5', '3:ea3c2b75d475ddd44a54bc86e471bd80', 67);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-193::Emiliano Conde::(Checksum: 3:44eee1ed52c6163b416f7d0ff867ddd5)
ALTER TABLE mediation_record ADD CONSTRAINT mediation_record_fk_1 FOREIGN KEY (mediation_process_id) REFERENCES mediation_process (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-193', '2.0.5', '3:44eee1ed52c6163b416f7d0ff867ddd5', 68);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-194::Emiliano Conde::(Checksum: 3:e98d694b52784429aaa508e3a4aa7764)
ALTER TABLE mediation_record ADD CONSTRAINT mediation_record_fk_2 FOREIGN KEY (status_id) REFERENCES generic_status (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-194', '2.0.5', '3:e98d694b52784429aaa508e3a4aa7764', 69);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-195::Emiliano Conde::(Checksum: 3:85f741a2cef2736be49cdd13d194c7eb)
ALTER TABLE mediation_record_line ADD CONSTRAINT mediation_record_line_fk_1 FOREIGN KEY (mediation_record_id) REFERENCES mediation_record (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-195', '2.0.5', '3:85f741a2cef2736be49cdd13d194c7eb', 70);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-196::Emiliano Conde::(Checksum: 3:9bde0cca6e27790d013ce5a5e5bbea86)
ALTER TABLE mediation_record_line ADD CONSTRAINT mediation_record_line_fk_2 FOREIGN KEY (order_line_id) REFERENCES order_line (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-196', '2.0.5', '3:9bde0cca6e27790d013ce5a5e5bbea86', 71);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-197::Emiliano Conde::(Checksum: 3:1a92920d0701c915fd24e503b74c5087)
ALTER TABLE meta_field_name ADD CONSTRAINT meta_field_name_fk_1 FOREIGN KEY (default_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-197', '2.0.5', '3:1a92920d0701c915fd24e503b74c5087', 72);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-198::Emiliano Conde::(Checksum: 3:08f23151872723ca01d5d5de9caaccb2)
ALTER TABLE meta_field_name ADD CONSTRAINT meta_field_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-198', '2.0.5', '3:08f23151872723ca01d5d5de9caaccb2', 73);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-199::Emiliano Conde::(Checksum: 3:78d470bd72d9284784f51f5dbc4b4495)
ALTER TABLE meta_field_value ADD CONSTRAINT meta_field_value_fk_1 FOREIGN KEY (meta_field_name_id) REFERENCES meta_field_name (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-199', '2.0.5', '3:78d470bd72d9284784f51f5dbc4b4495', 74);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-200::Emiliano Conde::(Checksum: 3:4ba2153ced6076ce2fa8bba8b220ad7c)
ALTER TABLE notification_message ADD CONSTRAINT notification_message_fk_3 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-200', '2.0.5', '3:4ba2153ced6076ce2fa8bba8b220ad7c', 75);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-201::Emiliano Conde::(Checksum: 3:8c580381298f27bdb21ae42dc2cb48d7)
ALTER TABLE notification_message ADD CONSTRAINT notification_message_fk_1 FOREIGN KEY (language_id) REFERENCES language (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-201', '2.0.5', '3:8c580381298f27bdb21ae42dc2cb48d7', 76);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-202::Emiliano Conde::(Checksum: 3:c8dcd666f2b3917f7e4938d734e45dd3)
ALTER TABLE notification_message ADD CONSTRAINT notification_message_fk_2 FOREIGN KEY (type_id) REFERENCES notification_message_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-202', '2.0.5', '3:c8dcd666f2b3917f7e4938d734e45dd3', 77);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-203::Emiliano Conde::(Checksum: 3:0b61ad2c1ff7b2c8d12e26b84526bb9a)
ALTER TABLE notification_message_arch_line ADD CONSTRAINT notif_mess_arch_line_fk_1 FOREIGN KEY (message_archive_id) REFERENCES notification_message_arch (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-203', '2.0.5', '3:0b61ad2c1ff7b2c8d12e26b84526bb9a', 78);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-204::Emiliano Conde::(Checksum: 3:ee2a1a0a8bc10789dd77eb88bf20d308)
ALTER TABLE notification_message_line ADD CONSTRAINT notification_message_line_fk_1 FOREIGN KEY (message_section_id) REFERENCES notification_message_section (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-204', '2.0.5', '3:ee2a1a0a8bc10789dd77eb88bf20d308', 79);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-205::Emiliano Conde::(Checksum: 3:ed428d3275f25dce236f5704a1cf3229)
ALTER TABLE notification_message_section ADD CONSTRAINT notification_msg_section_fk_1 FOREIGN KEY (message_id) REFERENCES notification_message (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-205', '2.0.5', '3:ed428d3275f25dce236f5704a1cf3229', 80);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-206::Emiliano Conde::(Checksum: 3:4d9e259484d9e573edbf4030dc0d9109)
ALTER TABLE notification_message_type ADD CONSTRAINT category_id_fk_1 FOREIGN KEY (category_id) REFERENCES notification_category (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-206', '2.0.5', '3:4d9e259484d9e573edbf4030dc0d9109', 81);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-207::Emiliano Conde::(Checksum: 3:c6e09b7ac266cdb2b9adcc23dcaa535f)
ALTER TABLE order_line ADD CONSTRAINT order_line_fk_1 FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-207', '2.0.5', '3:c6e09b7ac266cdb2b9adcc23dcaa535f', 82);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-208::Emiliano Conde::(Checksum: 3:9415bf87ccfc3af9e9493282efb4068c)
ALTER TABLE order_line ADD CONSTRAINT order_line_fk_2 FOREIGN KEY (order_id) REFERENCES purchase_order (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-208', '2.0.5', '3:9415bf87ccfc3af9e9493282efb4068c', 83);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-209::Emiliano Conde::(Checksum: 3:39709014ac5dbdb5222b6c2ee07799f5)
ALTER TABLE order_line ADD CONSTRAINT order_line_fk_3 FOREIGN KEY (type_id) REFERENCES order_line_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-209', '2.0.5', '3:39709014ac5dbdb5222b6c2ee07799f5', 84);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-210::Emiliano Conde::(Checksum: 3:840cd3eeaec02c8dd80ac7f1873e3e04)
ALTER TABLE order_meta_field_map ADD CONSTRAINT order_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-210', '2.0.5', '3:840cd3eeaec02c8dd80ac7f1873e3e04', 85);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-211::Emiliano Conde::(Checksum: 3:54a22c47cc50edd7361ee4a005865e37)
ALTER TABLE order_meta_field_map ADD CONSTRAINT order_meta_field_map_fk_1 FOREIGN KEY (order_id) REFERENCES purchase_order (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-211', '2.0.5', '3:54a22c47cc50edd7361ee4a005865e37', 86);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-212::Emiliano Conde::(Checksum: 3:5ebe3efb54b8eeb787b006c302e24674)
ALTER TABLE order_period ADD CONSTRAINT order_period_fk_1 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-212', '2.0.5', '3:5ebe3efb54b8eeb787b006c302e24674', 87);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-213::Emiliano Conde::(Checksum: 3:0f2f525220dd01b0fc2a1b74d80284fa)
ALTER TABLE order_period ADD CONSTRAINT order_period_fk_2 FOREIGN KEY (unit_id) REFERENCES period_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-213', '2.0.5', '3:0f2f525220dd01b0fc2a1b74d80284fa', 88);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-214::Emiliano Conde::(Checksum: 3:d318ad0cd4221e168cfb32c2b480b665)
ALTER TABLE order_process ADD CONSTRAINT order_process_fk_1 FOREIGN KEY (order_id) REFERENCES purchase_order (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-214', '2.0.5', '3:d318ad0cd4221e168cfb32c2b480b665', 89);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-215::Emiliano Conde::(Checksum: 3:dacdc3cc5f70e0cfd805f23f313f34a7)
ALTER TABLE partner ADD CONSTRAINT partner_fk_2 FOREIGN KEY (fee_currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-215', '2.0.5', '3:dacdc3cc5f70e0cfd805f23f313f34a7', 90);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-216::Emiliano Conde::(Checksum: 3:7e5566994dfd52b05b9a06873571694a)
ALTER TABLE partner ADD CONSTRAINT partner_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-216', '2.0.5', '3:7e5566994dfd52b05b9a06873571694a', 91);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-217::Emiliano Conde::(Checksum: 3:5d8170930a4e26019510bafad08f9331)
ALTER TABLE partner ADD CONSTRAINT partner_fk_3 FOREIGN KEY (related_clerk) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-217', '2.0.5', '3:5d8170930a4e26019510bafad08f9331', 92);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-218::Emiliano Conde::(Checksum: 3:ea62b586f139fa02dfccf2e5bed2d7bd)
ALTER TABLE partner ADD CONSTRAINT partner_fk_4 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-218', '2.0.5', '3:ea62b586f139fa02dfccf2e5bed2d7bd', 93);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-219::Emiliano Conde::(Checksum: 3:c34a1780ea2409952e266e40fcc206b1)
ALTER TABLE partner_meta_field_map ADD CONSTRAINT partner_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-219', '2.0.5', '3:c34a1780ea2409952e266e40fcc206b1', 94);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-220::Emiliano Conde::(Checksum: 3:81c979a8416e0dffd91c07e8cf06e40b)
ALTER TABLE partner_meta_field_map ADD CONSTRAINT partner_meta_field_map_fk_1 FOREIGN KEY (partner_id) REFERENCES partner (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-220', '2.0.5', '3:81c979a8416e0dffd91c07e8cf06e40b', 95);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-221::Emiliano Conde::(Checksum: 3:4d9f5bd3afad0bdd70e04308dc6618f2)
ALTER TABLE partner_payout ADD CONSTRAINT partner_payout_fk_1 FOREIGN KEY (partner_id) REFERENCES partner (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-221', '2.0.5', '3:4d9f5bd3afad0bdd70e04308dc6618f2', 96);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-222::Emiliano Conde::(Checksum: 3:2a2a2c3619b7e657a0e95fa60307c3a4)
ALTER TABLE payment ADD CONSTRAINT payment_fk_1 FOREIGN KEY (ach_id) REFERENCES ach (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-222', '2.0.5', '3:2a2a2c3619b7e657a0e95fa60307c3a4', 97);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-223::Emiliano Conde::(Checksum: 3:c661b7f690c0f8c45ff7a554d273c63c)
ALTER TABLE payment ADD CONSTRAINT payment_fk_4 FOREIGN KEY (credit_card_id) REFERENCES credit_card (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-223', '2.0.5', '3:c661b7f690c0f8c45ff7a554d273c63c', 98);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-224::Emiliano Conde::(Checksum: 3:68613117ece2749d057e684fef5bd86b)
ALTER TABLE payment ADD CONSTRAINT payment_fk_2 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-224', '2.0.5', '3:68613117ece2749d057e684fef5bd86b', 99);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-225::Emiliano Conde::(Checksum: 3:9f635e8d29d524b24bedbbbb91a701a4)
ALTER TABLE payment ADD CONSTRAINT payment_fk_6 FOREIGN KEY (method_id) REFERENCES payment_method (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-225', '2.0.5', '3:9f635e8d29d524b24bedbbbb91a701a4', 100);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-226::Emiliano Conde::(Checksum: 3:0679d13da00ada746d101238fabc82aa)
ALTER TABLE payment ADD CONSTRAINT payment_fk_3 FOREIGN KEY (payment_id) REFERENCES payment (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-226', '2.0.5', '3:0679d13da00ada746d101238fabc82aa', 101);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-227::Emiliano Conde::(Checksum: 3:0e3f4af38df3c9b09041ca204c499a56)
ALTER TABLE payment ADD CONSTRAINT payment_fk_5 FOREIGN KEY (result_id) REFERENCES payment_result (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-227', '2.0.5', '3:0e3f4af38df3c9b09041ca204c499a56', 102);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-228::Emiliano Conde::(Checksum: 3:9284c08e80c9db977b0d4a0a4cbc0ffa)
ALTER TABLE payment_authorization ADD CONSTRAINT payment_authorization_fk_1 FOREIGN KEY (payment_id) REFERENCES payment (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-228', '2.0.5', '3:9284c08e80c9db977b0d4a0a4cbc0ffa', 103);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-229::Emiliano Conde::(Checksum: 3:f46a8c3d4957f44e0792b138d576edc3)
ALTER TABLE payment_info_cheque ADD CONSTRAINT payment_info_cheque_fk_1 FOREIGN KEY (payment_id) REFERENCES payment (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-229', '2.0.5', '3:f46a8c3d4957f44e0792b138d576edc3', 104);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-230::Emiliano Conde::(Checksum: 3:92cedad2d06640d133d66de9e80dd1d6)
ALTER TABLE payment_invoice ADD CONSTRAINT payment_invoice_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-230', '2.0.5', '3:92cedad2d06640d133d66de9e80dd1d6', 105);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-231::Emiliano Conde::(Checksum: 3:d0b912f627117bb71fe4db82fb728fa9)
ALTER TABLE payment_invoice ADD CONSTRAINT payment_invoice_fk_2 FOREIGN KEY (payment_id) REFERENCES payment (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-231', '2.0.5', '3:d0b912f627117bb71fe4db82fb728fa9', 106);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-232::Emiliano Conde::(Checksum: 3:73fc7150111612f7bed856e5ea42f5bf)
ALTER TABLE payment_meta_field_map ADD CONSTRAINT payment_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-232', '2.0.5', '3:73fc7150111612f7bed856e5ea42f5bf', 107);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-233::Emiliano Conde::(Checksum: 3:e7e3b27d39fbe39811eeba0eee30fb8b)
ALTER TABLE payment_meta_field_map ADD CONSTRAINT payment_meta_field_map_fk_1 FOREIGN KEY (payment_id) REFERENCES payment (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-233', '2.0.5', '3:e7e3b27d39fbe39811eeba0eee30fb8b', 108);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-234::Emiliano Conde::(Checksum: 3:2927bea90b3f1dcc5c2176339342334d)
ALTER TABLE permission ADD CONSTRAINT permission_fk_1 FOREIGN KEY (type_id) REFERENCES permission_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-234', '2.0.5', '3:2927bea90b3f1dcc5c2176339342334d', 109);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-235::Emiliano Conde::(Checksum: 3:eee7b004987bd34a1482440cd87f3565)
ALTER TABLE permission_role_map ADD CONSTRAINT permission_role_map_fk_2 FOREIGN KEY (permission_id) REFERENCES permission (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-235', '2.0.5', '3:eee7b004987bd34a1482440cd87f3565', 110);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-236::Emiliano Conde::(Checksum: 3:310ea183e21f67f3c4aac295060157b3)
ALTER TABLE permission_role_map ADD CONSTRAINT permission_role_map_fk_1 FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-236', '2.0.5', '3:310ea183e21f67f3c4aac295060157b3', 111);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-237::Emiliano Conde::(Checksum: 3:0b2618c9bdbd6d2ec2c6d5620e35be08)
ALTER TABLE permission_user ADD CONSTRAINT permission_user_fk_2 FOREIGN KEY (permission_id) REFERENCES permission (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-237', '2.0.5', '3:0b2618c9bdbd6d2ec2c6d5620e35be08', 112);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-238::Emiliano Conde::(Checksum: 3:512a979c51dbee1bc7c34f8d9e795f6f)
ALTER TABLE permission_user ADD CONSTRAINT permission_user_fk_1 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-238', '2.0.5', '3:512a979c51dbee1bc7c34f8d9e795f6f', 113);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-239::Emiliano Conde::(Checksum: 3:1a37f520216d8c9bda1243d8c1b74f13)
ALTER TABLE plan ADD CONSTRAINT plan_item_id_fk FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-239', '2.0.5', '3:1a37f520216d8c9bda1243d8c1b74f13', 114);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-240::Emiliano Conde::(Checksum: 3:059c67efe046550f100a8d32e340454d)
ALTER TABLE plan ADD CONSTRAINT plan_period_id_fk FOREIGN KEY (period_id) REFERENCES order_period (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-240', '2.0.5', '3:059c67efe046550f100a8d32e340454d', 115);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-241::Emiliano Conde::(Checksum: 3:dd28b8f0e571ba5a9776c30472da5cac)
ALTER TABLE plan_item ADD CONSTRAINT plan_item_item_id_fk FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-241', '2.0.5', '3:dd28b8f0e571ba5a9776c30472da5cac', 116);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-242::Emiliano Conde::(Checksum: 3:9f3fba80779d29bee1ae4f4e04c646e1)
ALTER TABLE plan_item ADD CONSTRAINT plan_item_plan_id_fk FOREIGN KEY (plan_id) REFERENCES plan (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-242', '2.0.5', '3:9f3fba80779d29bee1ae4f4e04c646e1', 117);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-243::Emiliano Conde::(Checksum: 3:7026eaa4a49be9f1f37bbdbae3f9897c)
ALTER TABLE plan_item ADD CONSTRAINT plan_item_bundle_id_fk FOREIGN KEY (plan_item_bundle_id) REFERENCES plan_item_bundle (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-243', '2.0.5', '3:7026eaa4a49be9f1f37bbdbae3f9897c', 118);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-244::Emiliano Conde::(Checksum: 3:8fc01e9957eba47a34666897ebb69c22)
ALTER TABLE plan_item_bundle ADD CONSTRAINT plan_item_bundle_period_fk FOREIGN KEY (period_id) REFERENCES order_period (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-244', '2.0.5', '3:8fc01e9957eba47a34666897ebb69c22', 119);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-245::Emiliano Conde::(Checksum: 3:0ab0c68efb6ad73dc953db3d0b00aac0)
ALTER TABLE plan_item_price_timeline ADD CONSTRAINT plan_itm_timelin_plan_itm_fk FOREIGN KEY (plan_item_id) REFERENCES plan_item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-245', '2.0.5', '3:0ab0c68efb6ad73dc953db3d0b00aac0', 120);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-246::Emiliano Conde::(Checksum: 3:a6880991a7776b9a6010e796310ac73c)
ALTER TABLE plan_item_price_timeline ADD CONSTRAINT plnitmtimelnprc_mode_fk FOREIGN KEY (price_model_id) REFERENCES price_model (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-246', '2.0.5', '3:a6880991a7776b9a6010e796310ac73c', 121);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-247::Emiliano Conde::(Checksum: 3:24580bcb6180c4b853a6029c9263136f)
ALTER TABLE pluggable_task ADD CONSTRAINT pluggable_task_fk_2 FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-247', '2.0.5', '3:24580bcb6180c4b853a6029c9263136f', 122);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-248::Emiliano Conde::(Checksum: 3:baaf9634dd263a986ec94b1aa56b13a2)
ALTER TABLE pluggable_task ADD CONSTRAINT pluggable_task_fk_1 FOREIGN KEY (type_id) REFERENCES pluggable_task_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-248', '2.0.5', '3:baaf9634dd263a986ec94b1aa56b13a2', 123);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-249::Emiliano Conde::(Checksum: 3:fa674185e55a6b04ca95ce7ddb1fb530)
ALTER TABLE pluggable_task_parameter ADD CONSTRAINT pluggable_task_parameter_fk_1 FOREIGN KEY (task_id) REFERENCES pluggable_task (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-249', '2.0.5', '3:fa674185e55a6b04ca95ce7ddb1fb530', 124);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-250::Emiliano Conde::(Checksum: 3:2cea43cf8f22d2f819a7f481259a0e3f)
ALTER TABLE pluggable_task_type ADD CONSTRAINT pluggable_task_type_fk_1 FOREIGN KEY (category_id) REFERENCES pluggable_task_type_category (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-250', '2.0.5', '3:2cea43cf8f22d2f819a7f481259a0e3f', 125);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-251::Emiliano Conde::(Checksum: 3:b11e28e8207e971667b616f42c49aa9d)
ALTER TABLE preference ADD CONSTRAINT preference_fk_2 FOREIGN KEY (table_id) REFERENCES jbilling_table (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-251', '2.0.5', '3:b11e28e8207e971667b616f42c49aa9d', 126);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-252::Emiliano Conde::(Checksum: 3:b00cf35b627e45614f6371fba9dfb3cb)
ALTER TABLE preference ADD CONSTRAINT preference_fk_1 FOREIGN KEY (type_id) REFERENCES preference_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-252', '2.0.5', '3:b00cf35b627e45614f6371fba9dfb3cb', 127);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-253::Emiliano Conde::(Checksum: 3:5b782dad0c274b819c8864a840382a38)
ALTER TABLE price_model ADD CONSTRAINT price_model_currency_id_fk FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-253', '2.0.5', '3:5b782dad0c274b819c8864a840382a38', 128);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-254::Emiliano Conde::(Checksum: 3:a34eba6dba4aab6d181c0992ee4c933c)
ALTER TABLE price_model ADD CONSTRAINT price_model_next_id_fk FOREIGN KEY (next_model_id) REFERENCES price_model (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-254', '2.0.5', '3:a34eba6dba4aab6d181c0992ee4c933c', 129);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-255::Emiliano Conde::(Checksum: 3:7a9d6312c45ba076c6bb311dbd2bef49)
ALTER TABLE price_model_attribute ADD CONSTRAINT price_model_attr_model_id_fk FOREIGN KEY (price_model_id) REFERENCES price_model (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-255', '2.0.5', '3:7a9d6312c45ba076c6bb311dbd2bef49', 130);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-256::Emiliano Conde::(Checksum: 3:27a403b325d0af62eff968f16d30eba9)
ALTER TABLE process_run ADD CONSTRAINT process_run_fk_1 FOREIGN KEY (process_id) REFERENCES billing_process (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-256', '2.0.5', '3:27a403b325d0af62eff968f16d30eba9', 131);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-257::Emiliano Conde::(Checksum: 3:3d2e530ae6a5eaa0a3a4716d3091d28d)
ALTER TABLE process_run ADD CONSTRAINT process_run_fk_2 FOREIGN KEY (status_id) REFERENCES generic_status (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-257', '2.0.5', '3:3d2e530ae6a5eaa0a3a4716d3091d28d', 132);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-258::Emiliano Conde::(Checksum: 3:63c7f35602adbfc1752615559e2f4f3e)
ALTER TABLE process_run_total ADD CONSTRAINT process_run_total_fk_1 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-258', '2.0.5', '3:63c7f35602adbfc1752615559e2f4f3e', 133);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-259::Emiliano Conde::(Checksum: 3:959747f53479e966d0895b452ea45557)
ALTER TABLE process_run_total ADD CONSTRAINT process_run_total_fk_2 FOREIGN KEY (process_run_id) REFERENCES process_run (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-259', '2.0.5', '3:959747f53479e966d0895b452ea45557', 134);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-260::Emiliano Conde::(Checksum: 3:6c3d869afc5111db3ed791a1212c3d1f)
ALTER TABLE process_run_total_pm ADD CONSTRAINT process_run_total_pm_fk_1 FOREIGN KEY (payment_method_id) REFERENCES payment_method (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-260', '2.0.5', '3:6c3d869afc5111db3ed791a1212c3d1f', 135);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-261::Emiliano Conde::(Checksum: 3:1219e598fbb3e7d525c8f10d133b7ee7)
ALTER TABLE process_run_user ADD CONSTRAINT process_run_user_fk_1 FOREIGN KEY (process_run_id) REFERENCES process_run (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-261', '2.0.5', '3:1219e598fbb3e7d525c8f10d133b7ee7', 136);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-262::Emiliano Conde::(Checksum: 3:3e071fce532fdb875f74d5ba9ea4313c)
ALTER TABLE process_run_user ADD CONSTRAINT process_run_user_fk_2 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-262', '2.0.5', '3:3e071fce532fdb875f74d5ba9ea4313c', 137);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-263::Emiliano Conde::(Checksum: 3:1249eceb50835e41762801dac787b421)
ALTER TABLE promotion ADD CONSTRAINT promotion_fk_1 FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-263', '2.0.5', '3:1249eceb50835e41762801dac787b421', 138);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-264::Emiliano Conde::(Checksum: 3:ebdda1c11513a970d3ce866976c6d05e)
ALTER TABLE promotion_user_map ADD CONSTRAINT promotion_user_map_fk_2 FOREIGN KEY (promotion_id) REFERENCES promotion (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-264', '2.0.5', '3:ebdda1c11513a970d3ce866976c6d05e', 139);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-265::Emiliano Conde::(Checksum: 3:2291152213c4a893b5020c10421d22aa)
ALTER TABLE promotion_user_map ADD CONSTRAINT promotion_user_map_fk_1 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-265', '2.0.5', '3:2291152213c4a893b5020c10421d22aa', 140);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-266::Emiliano Conde::(Checksum: 3:787f1e373df0b4018cc6bd0c375ecb6f)
ALTER TABLE purchase_order ADD CONSTRAINT purchase_order_fk_2 FOREIGN KEY (billing_type_id) REFERENCES order_billing_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-266', '2.0.5', '3:787f1e373df0b4018cc6bd0c375ecb6f', 141);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-267::Emiliano Conde::(Checksum: 3:1855fa9484c44f3e8fbbadc27c098a71)
ALTER TABLE purchase_order ADD CONSTRAINT purchase_order_fk_5 FOREIGN KEY (created_by) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-267', '2.0.5', '3:1855fa9484c44f3e8fbbadc27c098a71', 142);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-268::Emiliano Conde::(Checksum: 3:99328f7bef410a4687af95a915281273)
ALTER TABLE purchase_order ADD CONSTRAINT purchase_order_fk_1 FOREIGN KEY (currency_id) REFERENCES currency (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-268', '2.0.5', '3:99328f7bef410a4687af95a915281273', 143);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-269::Emiliano Conde::(Checksum: 3:579d8a524213a381da27657bd93c88ef)
ALTER TABLE purchase_order ADD CONSTRAINT purchase_order_fk_3 FOREIGN KEY (period_id) REFERENCES order_period (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-269', '2.0.5', '3:579d8a524213a381da27657bd93c88ef', 144);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-270::Emiliano Conde::(Checksum: 3:f16cd236de594a70876866a74ec2c2f0)
ALTER TABLE purchase_order ADD CONSTRAINT purchase_order_fk_4 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-270', '2.0.5', '3:f16cd236de594a70876866a74ec2c2f0', 145);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-271::Emiliano Conde::(Checksum: 3:c216bc01c8c1ee5f155a9bd84d929752)
ALTER TABLE rate_card ADD CONSTRAINT rate_card_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-271', '2.0.5', '3:c216bc01c8c1ee5f155a9bd84d929752', 146);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-272::Emiliano Conde::(Checksum: 3:47c99643f6aae0e22e78fd7d614a6a23)
ALTER TABLE role ADD CONSTRAINT role_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-272', '2.0.5', '3:47c99643f6aae0e22e78fd7d614a6a23', 147);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-273::Emiliano Conde::(Checksum: 3:69bc455cccc553d3254f902f41c6d7bc)
ALTER TABLE user_role_map ADD CONSTRAINT user_role_map_fk_1 FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-273', '2.0.5', '3:69bc455cccc553d3254f902f41c6d7bc', 148);

-- Changeset descriptors/database/jbilling-schema.xml::1337623084753-274::Emiliano Conde::(Checksum: 3:fbcc167b561a87182ebd55db3b541eca)
ALTER TABLE user_role_map ADD CONSTRAINT user_role_map_fk_2 FOREIGN KEY (user_id) REFERENCES base_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Emiliano Conde', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'descriptors/database/jbilling-schema.xml', '1337623084753-274', '2.0.5', '3:fbcc167b561a87182ebd55db3b541eca', 149);

