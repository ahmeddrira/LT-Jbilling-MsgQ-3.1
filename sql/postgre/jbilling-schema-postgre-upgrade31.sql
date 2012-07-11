Liquibase Home is not set.
Liquibase Home: /home/aristokrates/jbilling/liquidbase-2.0.5
-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: descriptors/database/jbilling-upgrade-3.1.xml
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

-- Changeset descriptors/database/jbilling-upgrade-3.1.xml::20120608-#2725-notification-category::Vikas Bodani::(Checksum: 3:7c8427517b405966bb33a71180c538c7)
INSERT INTO jbilling_seqs (name, next_id) VALUES ('notification_category', 2);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Vikas Bodani', '', NOW(), 'Insert Row', 'EXECUTED', 'descriptors/database/jbilling-upgrade-3.1.xml', '20120608-#2725-notification-category', '2.0.5', '3:7c8427517b405966bb33a71180c538c7', 1);

-- Changeset descriptors/database/jbilling-upgrade-3.1.xml::20120530-#2825-Fix-percentage-products-in-Plans::Juan Vidal::(Checksum: 3:6e46ed99c604b7c4fe0002e0496b2007)
INSERT INTO order_period (entity_id, id, optlock, unit_id, value) VALUES (NULL, 5, 1, NULL, NULL);

INSERT INTO international_description (content, foreign_id, language_id, psudo_column, table_id) VALUES ('All Orders', 5, 1, 'description', 17);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Juan Vidal', '', NOW(), 'Insert Row (x2)', 'EXECUTED', 'descriptors/database/jbilling-upgrade-3.1.xml', '20120530-#2825-Fix-percentage-products-in-Plans', '2.0.5', '3:6e46ed99c604b7c4fe0002e0496b2007', 2);

