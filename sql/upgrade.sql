-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL

-- PaymentRouterTask has become PaymentRouterCCFTask
UPDATE pluggable_task_type 
SET class_name = 'com.sapienter.jbilling.server.payment.tasks.PaymentRouterCCFTask' 
WHERE class_name = 'com.sapienter.jbilling.server.pluggableTask.PaymentRouterTask';

-- New generic status tables
DROP TABLE order_status CASCADE;

DROP TABLE user_status CASCADE;

DROP TABLE subscriber_status CASCADE;

CREATE TABLE generic_status_type
(
    id VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE generic_status
(
    id INTEGER NOT NULL,
    dtype VARCHAR(20) NOT NULL,
    status_value INTEGER NOT NULL,
    can_login INT2,
    PRIMARY KEY (id)
);

update base_user set subscriber_status = 9 where subscriber_status = 1;
update base_user set subscriber_status = 10 where subscriber_status = 2;
update base_user set subscriber_status = 11 where subscriber_status = 3;
update base_user set subscriber_status = 12 where subscriber_status = 4;
update base_user set subscriber_status = 13 where subscriber_status = 5;
update base_user set subscriber_status = 14 where subscriber_status = 6;
update base_user set subscriber_status = 15 where subscriber_status = 7;

update purchase_order set status_id = 16 where status_id = 1;
update purchase_order set status_id = 17 where status_id = 2;
update purchase_order set status_id = 18 where status_id = 3;
update purchase_order set status_id = 19 where status_id = 4;

INSERT INTO generic_status_type VALUES('order_line_provisioning_status');
INSERT INTO generic_status_type VALUES('order_status');
INSERT INTO generic_status_type VALUES('subscriber_status');
INSERT INTO generic_status_type VALUES('user_status');
INSERT INTO generic_status VALUES(1,'user_status',1,1);
INSERT INTO generic_status VALUES(2,'user_status',2,1);
INSERT INTO generic_status VALUES(3,'user_status',3,1);
INSERT INTO generic_status VALUES(4,'user_status',4,1);
INSERT INTO generic_status VALUES(5,'user_status',5,0);
INSERT INTO generic_status VALUES(6,'user_status',6,0);
INSERT INTO generic_status VALUES(7,'user_status',7,0);
INSERT INTO generic_status VALUES(8,'user_status',8,0);
INSERT INTO generic_status VALUES(9,'subscriber_status',1,NULL);
INSERT INTO generic_status VALUES(10,'subscriber_status',2,NULL);
INSERT INTO generic_status VALUES(11,'subscriber_status',3,NULL);
INSERT INTO generic_status VALUES(12,'subscriber_status',4,NULL);
INSERT INTO generic_status VALUES(13,'subscriber_status',5,NULL);
INSERT INTO generic_status VALUES(14,'subscriber_status',6,NULL);
INSERT INTO generic_status VALUES(15,'subscriber_status',7,NULL);
INSERT INTO generic_status VALUES(16,'order_status',1,NULL);
INSERT INTO generic_status VALUES(17,'order_status',2,NULL);
INSERT INTO generic_status VALUES(18,'order_status',3,NULL);
INSERT INTO generic_status VALUES(19,'order_status',4,NULL);
INSERT INTO generic_status VALUES(20,'order_line_provisioning_status',1,NULL);
INSERT INTO generic_status VALUES(21,'order_line_provisioning_status',2,NULL);
INSERT INTO generic_status VALUES(22,'order_line_provisioning_status',3,NULL);
INSERT INTO generic_status VALUES(23,'order_line_provisioning_status',4,NULL);
INSERT INTO generic_status VALUES(24,'order_line_provisioning_status',5,NULL);
INSERT INTO generic_status VALUES(25,'order_line_provisioning_status',6,NULL);

ALTER TABLE ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_FK_1 FOREIGN KEY (status_id)
    REFERENCES generic_status (id);

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_1 FOREIGN KEY (status_id)
    REFERENCES generic_status (id);

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_2 FOREIGN KEY (subscriber_status)
    REFERENCES generic_status (id);

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_6 FOREIGN KEY (status_id)
    REFERENCES generic_status (id);

ALTER TABLE generic_status
    ADD CONSTRAINT generic_status_FK_1 FOREIGN KEY (dtype)
    REFERENCES generic_status_type (id);

INSERT INTO jbilling_table (id,name,next_id)
    VALUES (86,'mediation_record_line',1);

INSERT INTO jbilling_table (id,name,next_id)
    VALUES (87,'generic_status',26);

INSERT INTO jbilling_table (id,name,next_id)
    VALUES (88,'order_line_provisioning_status',1);

INSERT INTO international_description (table_id,foreign_id,psudo_column,language_id,content)
    VALUES (88,1,'description',1,'Active');

INSERT INTO international_description (table_id,foreign_id,psudo_column,language_id,content)
    VALUES (88,2,'description',1,'Inactive');

INSERT INTO international_description (table_id,foreign_id,psudo_column,language_id,content)
    VALUES (88,3,'description',1,'Pending Active');

INSERT INTO international_description (table_id,foreign_id,psudo_column,language_id,content)
    VALUES (88,4,'description',1,'Pending Inactive');

INSERT INTO international_description (table_id,foreign_id,psudo_column,language_id,content)
    VALUES (88,5,'description',1,'Failed');

INSERT INTO international_description (table_id,foreign_id,psudo_column,language_id,content)
    VALUES (88,6,'description',1,'Unavailable');

INSERT INTO PREFERENCE_TYPE VALUES(48,1,NULL,NULL);
