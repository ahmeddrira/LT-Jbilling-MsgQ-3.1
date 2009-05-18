-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL

alter table item add optlock integer;
update item set optlock = 1;
alter table item_price add optlock integer;
update item_price set optlock = 1;
alter table item_type add optlock integer;
update item_type set optlock = 1;
alter table item_user_price add optlock integer;
update item_user_price set optlock = 1;
alter table list add optlock integer;
update list set optlock = 1;
alter table list_entity add optlock integer;
update list_entity set optlock = 1;
alter table list_field add optlock integer;
update list_field set optlock = 1;
alter table list_field_entity add optlock integer;
update list_field_entity set optlock = 1;
alter table order_line add provisioning_status integer;
alter table order_line add provisioning_request_id VARCHAR(50);
CREATE TABLE mediation_record_line
(
    id INTEGER NOT NULL,
    mediation_record_id VARCHAR(100) NOT NULL,
    order_line_id INTEGER NOT NULL,
    event_date TIMESTAMP NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    description VARCHAR(200),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX ix_mrl_order_line ON mediation_record_line (order_line_id);

ALTER TABLE mediation_record_line
    ADD CONSTRAINT mediation_record_line_FK_1 FOREIGN KEY (mediation_record_id)
    REFERENCES mediation_record (id_key)
;
ALTER TABLE mediation_record_line
    ADD CONSTRAINT mediation_record_line_FK_2 FOREIGN KEY (order_line_id)
    REFERENCES order_line (id)
;
INSERT INTO event_log_message (id) VALUES (29);
INSERT INTO event_log_message (id) VALUES (30);
INSERT INTO event_log_message (id) VALUES (31);
INSERT INTO event_log_module (id) VALUES (15);
INSERT INTO jbilling_table (id,name,next_id)
    VALUES (86,'mediation_record_line',1);

INSERT INTO pluggable_task_type (id,category_id,class_name,min_parameters)
    VALUES (44,15,'com.sapienter.jbilling.server.mediation.task.JDBCReader',0);
INSERT INTO pluggable_task_type (id,category_id,class_name,min_parameters)
    VALUES (45,15,'com.sapienter.jbilling.server.mediation.task.MySQLReader',0);
INSERT INTO pluggable_task_type (id,category_id,class_name,min_parameters)
    VALUES (46,17,'com.sapienter.jbilling.server.provisioning.task.ProvisioningCommandsRulesTask',0);

INSERT INTO preference_type (id,int_def_value) VALUES (44,0);
INSERT INTO preference_type (id,int_def_value) VALUES (45,0);
INSERT INTO preference_type (id,int_def_value) VALUES (46,0);
INSERT INTO preference_type (id,int_def_value) VALUES (47,0);

alter table notification_message add optlock integer;
update notification_message set optlock = 1;
alter table notification_message_arch add optlock integer;
update notification_message_arch set optlock = 1;
alter table notification_message_arch_line add optlock integer;
update notification_message_arch_line set optlock = 1;
alter table notification_message_line add optlock integer;
update notification_message_line set optlock = 1;
alter table notification_message_section add optlock integer;
update notification_message_section set optlock = 1;
alter table notification_message_type add optlock integer;
update notification_message_type set optlock = 1;

INSERT INTO event_log_message (id) VALUES (32);


alter table notification_message_type drop column sections;
alter table report add optlock integer;
update report set optlock = 1;
alter table report_field add optlock integer;
update report_field set optlock = 1;
alter table report_type add optlock integer;
update report_type set optlock = 1;
alter table report_user add optlock integer;
update report_user set optlock = 1;


alter table ach add optlock integer;
update ach set optlock = 1;
alter table ageing_entity_step add optlock integer;
update ageing_entity_step set optlock = 1;
alter table billing_process add optlock integer;
update billing_process set optlock = 1;
alter table billing_process_configuration add optlock integer;
update billing_process_configuration set optlock = 1;
alter table process_run add optlock integer;
update process_run set optlock = 1;
alter table process_run_total add optlock integer;
update process_run_total set optlock = 1;
alter table process_run_total_pm add optlock integer;
update process_run_total_pm set optlock = 1;
alter table invoice add optlock integer;
update invoice set optlock = 1;
alter table invoice_line add optlock integer;
update invoice_line set optlock = 1;
alter table payment add optlock integer;
update payment set optlock = 1;
alter table payment_authorization add optlock integer;
update payment_authorization set optlock = 1;
alter table payment_info_cheque add optlock integer;
update payment_info_cheque set optlock = 1;
alter table payment_invoice add optlock integer;
update payment_invoice set optlock = 1;
alter table paper_invoice_batch add optlock integer;
update paper_invoice_batch set optlock = 1;


-- PaymentRouterTask has become PaymentRouterCCFTask
UPDATE pluggable_task_type 
SET class_name = 'com.sapienter.jbilling.server.payment.tasks.PaymentRouterCCFTask' 
WHERE class_name = 'com.sapienter.jbilling.server.pluggableTask.PaymentRouterTask';

-- New generic status tables
alter table purchase_order drop constraint purchase_or....;
DROP TABLE order_status;

alter table base_user drop constraint base_user_...;
alter table ageing_entity_step drop constraint ageing_entity_step_...;
DROP TABLE user_status;

alter table base_user drop constraint base_user_...;
DROP TABLE subscriber_status;

CREATE TABLE generic_status_type
(
    id VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE generic_status
(
    id INTEGER NOT NULL,
    dtype VARCHAR(40) NOT NULL,
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
    ADD CONSTRAINT ageing_entity_step_FK_gs FOREIGN KEY (status_id)
    REFERENCES generic_status (id);

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_gs FOREIGN KEY (status_id)
    REFERENCES generic_status (id);

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_gs2 FOREIGN KEY (subscriber_status)
    REFERENCES generic_status (id);

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_gs FOREIGN KEY (status_id)
    REFERENCES generic_status (id);

ALTER TABLE generic_status
    ADD CONSTRAINT generic_status_FK_1 FOREIGN KEY (dtype)
    REFERENCES generic_status_type (id);

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

update notification_message_line set content = replace(content,'|','$');
update notification_message_line set content = replace(content,'$ ',' ');
update notification_message_line set content = replace(content,'$,',',');
update notification_message_line set content = replace(content,'$.','.');
update notification_message_line set content = replace(content,'$\r','\r');

