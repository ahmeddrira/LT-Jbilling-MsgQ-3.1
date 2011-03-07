<<<<<<< HEAD
-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL
--
-- MySQL does not support many of the ANSI SQL statements used in this file to upgrade the
-- base schema. If you are using MySQL as your database, you will need to edit this file and
-- comment out the labeled 'postgresql' statements and un-comment the 'mysql' statements.

-- validatePurchase changes
insert into pluggable_task_type_category values (19, 'Validate Purchase', 'com.sapienter.jbilling.server.user.tasks.IValidatePurchaseTask');
insert into pluggable_task_type values (55, 19, 'com.sapienter.jbilling.server.user.tasks.UserBalanceValidatePurchaseTask', 0);
insert into pluggable_task_type values (56, 19, 'com.sapienter.jbilling.server.user.tasks.RulesValidatePurchaseTask', 0);

-- new event log column
alter table event_log add column affected_user_id integer;
alter table event_log add constraint "event_log_fk_6" foreign key (affected_user_id) references base_user(id); -- postgresql
-- alter table event_log add constraint event_log_fk_6 foreign key (affected_user_id) references base_user(id); -- mysql

-- new invoice status for invoice carry over
insert into generic_status_type values ('invoice_status');
insert into generic_status (id, dtype, status_value) values (26, 'invoice_status', 1); -- paid
insert into generic_status (id, dtype, status_value) values (27, 'invoice_status', 2); -- unpaid
insert into generic_status (id, dtype, status_value) values (28, 'invoice_status', 3); -- unpaid and carried over

insert into jbilling_table values (90, 'invoice_status', 4);
update jbilling_table set next_id = 29 where id = 87; -- generic_status

insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (90, 1, 'description', 1, 'Paid');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (90, 2, 'description', 1, 'Unpaid');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (90, 3, 'description', 1, 'Unpaid, balance carried to new invoice');

update report set tables_list = 'base_user, invoice, currency , international_description, generic_status', where_str = 'base_user.id = invoice.user_id and invoice.currency_id = currency.id and invoice.status_id = generic_status.id and generic_status.dtype = ''invoice_status'' and international_description.foreign_id = generic_status.status_value and international_description.table_id = 90' where id = 2;
update report_field set table_name = 'international_description', column_name = 'content' where report_id = 2 and column_name = 'to_process';
-- migration
alter table invoice add column status_id integer;

update invoice set status_id = 26 where to_process = 0 and delegated_invoice_id is null;     -- mark as paid
update invoice set status_id = 28 where to_process = 0 and delegated_invoice_id is not null; -- mark carried over
update invoice set status_id = 27 where to_process = 1;                                      -- mark as unpaid

-- balance adjustment will need to be handled manually. a carried invoices balance will not be zeroed, and should
-- be equal to the total of all invoice lines for that invoice. a quick script can easily be created to handle this
--
-- psudo-code:
--    for all invoices where status_id = 28
--      for each invoice_line of invoice,
--        add line total to sum
--      update invoice set balance = sum where invoice_id = ?

alter table invoice drop column to_process;

-- external credit card storage
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (58, 17, 'com.sapienter.jbilling.server.payment.tasks.SaveCreditCardExternallyTask', 1);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (59, 6, 'com.sapienter.jbilling.server.pluggableTask.PaymentFakeExternalStorage', 0);

insert into payment_method (id) values (9);
alter table credit_card drop column security_code;
alter table credit_card add column gateway_key varchar(100);
insert into international_description (table_id, foreign_id, psudo_column, language_id, content) values (35, 9, 'description', 1, 'Payment Gateway Key');

-- new rules pricing plug-in
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (60, 14, 'com.sapienter.jbilling.server.item.tasks.RulesPricingTask2', 0);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (61, 13, 'com.sapienter.jbilling.server.order.task.RulesItemManager2', 0);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (62, 1, 'com.sapienter.jbilling.server.order.task.RulesLineTotalTask2', 0);

-- new mediation process requires 3 rules packages, lengthen parameter str_value to accept multiple urls
alter table pluggable_task_parameter alter column str_value type varchar(500); -- postgresql
-- alter table pluggable_task_parameter modify str_value varchar(500); -- mysql

-- world pay task
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (63, 6, 'com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayTask', 3);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (64, 6, 'com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayExternalTask', 3);

-- automatic recharge task
alter table customer add column auto_recharge double precision;
insert into preference_type (id, int_def_value, str_def_value, float_def_value) values (49, null, null, null);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (65, 17, 'com.sapienter.jbilling.server.user.tasks.AutoRechargeTask', 0);

-- changes in mediation module
alter table mediation_record drop column end_datetime;

-- refactoring of the sequence generator table
create table jbilling_seqs (name VARCHAR(255), next_id integer);
insert into jbilling_seqs select name, next_id from jbilling_table;
alter table jbilling_table drop column next_id;

-- new payment fields
alter table payment add column payment_period integer;
alter table payment add column payment_notes VARCHAR(500);

-- New pluggable task category, type and task for the billing process
insert into pluggable_task_type_category values (20, 'BillingProcessFilterTask', 'com.sapienter.jbilling.server.process.task.IBillingProcessFilterTask');
insert into pluggable_task_type values (69, 20, 'com.sapienter.jbilling.server.process.task.BasicBillingProcessFilterTask',0);
insert into pluggable_task_type values (70, 20, 'com.sapienter.jbilling.server.process.task.BillableUsersBillingProcessFilterTask',0);

-- new mediation_record_status
insert into generic_status_type values ('mediation_record_status');

insert into generic_status (id, dtype, status_value) values (29, 'mediation_record_status', 1); -- done and billable
insert into generic_status (id, dtype, status_value) values (30, 'mediation_record_status', 2); -- done and not billable
insert into generic_status (id, dtype, status_value) values (31, 'mediation_record_status', 3); -- error detected
insert into generic_status (id, dtype, status_value) values (32, 'mediation_record_status', 4); -- error declared

insert into jbilling_table values (91, 'mediation_record_status');

insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (91, 1, 'description', 1, 'Done and billable');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (91, 2, 'description', 1, 'Done and not billable');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (91, 3, 'description', 1, 'Error detected');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (91, 4, 'description', 1, 'Error declared');

-- alter mediation_record table for adding status field with previous data update
alter table mediation_record add column status_id integer;
update mediation_record set status_id = 29; -- mark all records as done and billable
alter table mediation_record alter column status_id set NOT NULL; -- postgresql
-- alter table mediation_record modify status_id integer NOT NULL; -- mysql

alter table mediation_record ADD CONSTRAINT mediation_record_FK_2 FOREIGN KEY (status_id) REFERENCES generic_status (id);

create index mediation_record_i on mediation_record using btree (id_key, status_id); -- postgresql
-- alter table mediation_record add index mediation_record_i (id_key, status_id); -- mysql

-- mediation error handler plug-in
insert into pluggable_task_type_category (id, description, interface_name) values (21, 'Mediation Error Handler', 'com.sapienter.jbilling.server.mediation.task.IMediationErrorHandler');
insert into pluggable_task_type  (id, category_id, class_name, min_parameters) values (71, 21, 'com.sapienter.jbilling.server.mediation.task.SaveToFileMediationErrorHandler', 0);
insert into pluggable_task_type  (id, category_id, class_name, min_parameters) values (73, 21, 'com.sapienter.jbilling.server.mediation.task.SaveToJDBCMediationErrorHandler', 1);

-- scheduled task's
insert into pluggable_task_type_category(id, description, interface_name) values(22, 'Scheduled Tasks', 'com.sapienter.jbilling.server.process.task.IScheduledTask');

-- unique id for mediation_record
-- postgresql
alter table mediation_record add column id integer;
create temp sequence mediation_rec_seq;
update mediation_record set id = nextval('mediation_rec_seq');

alter table mediation_record_line drop constraint mediation_record_line_fk_1;
alter table mediation_record drop constraint mediation_record_pkey;
alter table mediation_record add constraint mediation_record_pkey primary key (id);

alter table mediation_record_line add column mediation_record_id_2 integer;
update mediation_record_line set mediation_record_id_2 = r.id from mediation_record r, mediation_record_line l where r.id_key = l.mediation_record_id;
alter table mediation_record_line drop column mediation_record_id;
alter table mediation_record_line rename column mediation_record_id_2 to mediation_record_id;
alter table mediation_record_line alter column mediation_record_id set NOT NULL;
alter table mediation_record_line add constraint mediation_record_line_fk_1 foreign key (mediation_record_id) references mediation_record(id);
 
-- mysql
-- CREATE TABLE mediation_record_2 (
--    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    id_key varchar(100) NOT NULL,
--    start_datetime timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
--    mediation_process_id int,
--    OPTLOCK int NOT NULL,
--    status_id int NOT NULL
-- );
-- insert into mediation_record_2 (id_key, start_datetime, mediation_process_id, status_id, OPTLOCK) (select id_key, start_datetime, mediation_process_id, status_id, OPTLOCK from mediation_record);
-- drop table mediation_record;
-- rename table mediation_record_2 to mediation_record;
-- alter table mediation_record_line modify column id integer;
--
-- alter table mediation_record_line add column mediation_record_id_2 integer;
-- update mediation_record_line as l, mediation_record as r set l.mediation_record_id_2 = r.id where r.id_key = l.mediation_record_id;
-- alter table mediation_record_line drop column mediation_record_id;
-- alter table mediation_record_line change mediation_record_id_2 mediation_record_id integer;
-- alter table mediation_record_line modify mediation_record_id integer NOT NULL;

-- insert new mediation_record next_id value into the jbilling_seqs table.
-- if you have no existing mediation_records, insert 0
insert into jbilling_seqs values ('mediation_record', (select round(max(id)/100)+1 from mediation_record));

-- authorize.net CIM payment task, may already exist in some versions of jbilling
update pluggable_task_type set min_parameters = 2 where class_name = 'com.sapienter.jbilling.server.payment.tasks.PaymentAuthorizeNetCIMTask';
insert into pluggable_task_type  (id, category_id, class_name, min_parameters) values (76, 6, 'com.sapienter.jbilling.server.payment.tasks.PaymentAuthorizeNetCIMTask', 2);

-- mediation and billing process scheduled task
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (81, 22, 'com.sapienter.jbilling.server.mediation.task.MediationProcessTask', 0);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (82, 22, 'com.sapienter.jbilling.server.billing.task.BillingProcessTask', 0);

-- Changed price, amount, quantity, rate, percentage fields to decimal
-- postgresql
alter table currency_exchange alter column rate type numeric(22, 10);
alter table customer alter column dynamic_balance type numeric(22, 10);
alter table customer alter column credit_limit type numeric(22, 10);
alter table customer alter column auto_recharge type numeric(22, 10);
alter table invoice alter column total type numeric(22, 10);
alter table invoice alter column balance type numeric(22, 10);
alter table invoice alter column carried_balance type numeric(22, 10);
alter table invoice_line alter column amount type numeric(22, 10);
alter table invoice_line alter column quantity type numeric(22, 10);
alter table invoice_line alter column price type numeric(22, 10);
alter table item alter column percentage type numeric(22, 10);
alter table item_price alter column price type numeric(22, 10);
alter table mediation_record_line alter column amount type numeric(22, 10);
alter table mediation_record_line alter column quantity type numeric(22, 10);
alter table order_line alter column amount type numeric(22, 10);
alter table order_line alter column quantity type numeric(22, 10);
alter table order_line alter column price type numeric(22, 10);
alter table partner alter column balance type numeric(22, 10);
alter table partner alter column total_payments type numeric(22, 10);
alter table partner alter column total_refunds type numeric(22, 10);
alter table partner alter column total_payouts type numeric(22, 10);
alter table partner alter column percentage_rate type numeric(22, 10);
alter table partner alter column referral_fee type numeric(22, 10);
alter table partner alter column due_payout type numeric(22, 10);
alter table partner_payout alter column payments_amount type numeric(22, 10);
alter table partner_payout alter column refunds_amount type numeric(22, 10);
alter table partner_payout alter column balance_left type numeric(22, 10);
alter table partner_range alter column percentage_rate type numeric(22, 10);
alter table partner_range alter column referral_fee type numeric(22, 10);
alter table payment alter column amount type numeric(22, 10);
alter table payment alter column balance type numeric(22, 10);
alter table payment_invoice alter column amount type numeric(22, 10);
alter table process_run_total alter column total_invoiced type numeric(22, 10);
alter table process_run_total alter column total_paid type numeric(22, 10);
alter table process_run_total alter column total_not_paid type numeric(22, 10);
alter table process_run_total_pm alter column total type numeric(22, 10);
alter table pluggable_task_parameter alter column float_value type numeric(22, 10);
alter table preference alter column float_value type numeric(22, 10);
alter table preference_type alter column float_def_value type numeric(22, 10);
-- mysql
-- alter table currency_exchange modify rate numeric(22, 10);
-- alter table customer modify dynamic_balance numeric(22, 10);
-- alter table customer modify credit_limit numeric(22, 10);
-- alter table customer modify auto_recharge numeric(22, 10);
-- alter table invoice modify total numeric(22, 10);
-- alter table invoice modify balance numeric(22, 10);
-- alter table invoice modify carried_balance numeric(22, 10);
-- alter table invoice_line modify amount numeric(22, 10);
-- alter table invoice_line modify quantity numeric(22, 10);
-- alter table invoice_line modify price numeric(22, 10);
-- alter table item modify percentage numeric(22, 10);
-- alter table item_price modify price numeric(22, 10);
-- alter table mediation_record_line modify amount numeric(22, 10);
-- alter table mediation_record_line modify quantity numeric(22, 10);
-- alter table order_line modify amount numeric(22, 10);
-- alter table order_line modify quantity numeric(22, 10);
-- alter table order_line modify price numeric(22, 10);
-- alter table partner modify balance numeric(22, 10);
-- alter table partner modify total_payments numeric(22, 10);
-- alter table partner modify total_refunds numeric(22, 10);
-- alter table partner modify total_payouts numeric(22, 10);
-- alter table partner modify percentage_rate numeric(22, 10);
-- alter table partner modify referral_fee numeric(22, 10);
-- alter table partner modify due_payout numeric(22, 10);
-- alter table partner_payout modify payments_amount numeric(22, 10);
-- alter table partner_payout modify refunds_amount numeric(22, 10);
-- alter table partner_payout modify balance_left numeric(22, 10);
-- alter table partner_range modify percentage_rate numeric(22, 10);
-- alter table partner_range modify referral_fee numeric(22, 10);
-- alter table payment modify amount numeric(22, 10);
-- alter table payment modify balance numeric(22, 10);
-- alter table payment_invoice modify amount numeric(22, 10);
-- alter table process_run_total modify total_invoiced numeric(22, 10);
-- alter table process_run_total modify total_paid numeric(22, 10);
-- alter table process_run_total modify total_not_paid numeric(22, 10);
-- alter table process_run_total_pm modify total numeric(22, 10);
-- alter table pluggable_task_parameter modify float_value numeric(22, 10);
-- alter table preference modify float_value numeric(22, 10);
-- alter table preference_type modify float_def_value numeric(22, 10);

alter table mediation_process alter column end_datetime type timestamp; -- postgresql
-- alter table mediation_process modify end_datetime timestamp null default null; -- mysql


alter table ach alter column aba_routing type character varying(40); -- postgresql
alter table ach alter column bank_account type character varying(60); -- postgresql
-- alter table ach modify aba_routing varchar(40); -- mysql
-- alter table ach modify bank_account varchar(60); -- mysql

-- new generate rules plug-in category
insert into pluggable_task_type_category values (23, 'Rules Generator', 'com.sapienter.jbilling.server.rule.task.IRulesGenerator');
insert into pluggable_task_type values (78, 23, 'com.sapienter.jbilling.server.rule.task.VelocityRulesGeneratorTask', 2);

-- new process run status
insert into generic_status_type values ('process_run_status');
insert into generic_status (id, dtype, status_value) values (33, 'process_run_status', 1); -- running
insert into generic_status (id, dtype, status_value) values (34, 'process_run_status', 2); -- finished: successful
insert into generic_status (id, dtype, status_value) values (35, 'process_run_status', 3); -- finished: failed

insert into jbilling_table values (92, 'process_run_status');

--update jbilling_table set next_id = 36 where id = 87; -- generic_status

insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (92, 1, 'description', 1, 'Running');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (92, 2, 'description', 1, 'Finished: successful');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)  values (92, 3, 'description', 1, 'Finished: failed');


alter table process_run add column status_id integer;

update process_run set status_id = 33 where finished is null;
update process_run set status_id = 34 where finished is not null;

alter table process_run alter column status_id set NOT NULL;
alter table process_run ADD CONSTRAINT process_run_FK_2 FOREIGN KEY (status_id) REFERENCES generic_status (id);

-- new table to store user status within process run
drop table if exists process_run_user;
create table process_run_user (
  id int PRIMARY KEY NOT NULL,
  process_run_id int NOT NULL,
  user_id int NOT NULL,
  status int NOT NULL,
  created timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  OPTLOCK int NOT NULL
);
alter table process_run_user ADD CONSTRAINT process_run_user_FK_1 FOREIGN KEY (process_run_id) REFERENCES process_run (id);
alter table process_run_user ADD CONSTRAINT process_run_user_FK_2 FOREIGN KEY (user_id) REFERENCES base_user (id);
ALTER TABLE payment_authorization ALTER COLUMN transaction_id TYPE character varying(40);
-- alter table payment_authorization modify transaction_id varchar(40); -- mysql

-- plans and pricing models
CREATE TABLE price_model (
    id integer NOT NULL,
    strategy_type varchar(20) NOT NULL,
    rate numeric(22,10) NOT NULL,
    included_quantity integer,
    currency_id integer NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE price_model ADD CONSTRAINT price_model_currency_id_FK FOREIGN KEY (currency_id) REFERENCES currency (id);

insert into jbilling_table (id, name) values (94, 'price_model');
insert into jbilling_seqs (name, next_id) values ('price_model', 1);

DROP TABLE IF EXISTS price_model_attribute;
CREATE TABLE price_model_attribute (
    price_model_id integer NOT NULL,
    attribute_name varchar(255) NOT NULL,
    attribute_value varchar(255),
    PRIMARY KEY (price_model_id, attribute_name)
);
ALTER TABLE price_model_attribute ADD CONSTRAINT price_model_attr_model_id_FK FOREIGN KEY (price_model_id) REFERENCES price_model (id);

insert into jbilling_table (id, name) values (95, 'price_model_attribute');

DROP TABLE IF EXISTS plan;
CREATE TABLE plan (
    id integer NOT NULL,
    item_id integer NOT NULL,
    description varchar(255),
    PRIMARY KEY (id)
);
ALTER TABLE plan ADD CONSTRAINT plan_item_id_FK FOREIGN KEY (item_id) REFERENCES item (id);

insert into jbilling_table (id, name) values (96, 'plan');
insert into jbilling_seqs (name, next_id) values ('plan', 1);

DROP TABLE IF EXISTS plan_item;
CREATE TABLE plan_item (
    id integer NOT NULL,
    plan_id integer,
    item_id integer NOT NULL,
    price_model_id integer NOT NULL,
    precedence integer NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX plan_item_precedence_i ON plan_item (item_id);
CREATE INDEX plan_item_item_id_i ON plan_item (precedence);
ALTER TABLE plan_item ADD CONSTRAINT plan_item_item_id_FK FOREIGN KEY (item_id) REFERENCES item (id);
ALTER TABLE plan_item ADD CONSTRAINT plan_item_plan_id_FK FOREIGN KEY (plan_id) REFERENCES plan (id);
ALTER TABLE plan_item ADD CONSTRAINT plan_item_price_model_id_FK FOREIGN KEY (price_model_id) REFERENCES price_model (id);

insert into jbilling_table (id, name) values (97, 'plan_item');
insert into jbilling_seqs (name, next_id) values ('plan_item', 1);

DROP TABLE IF EXISTS customer_price;
CREATE TABLE customer_price (
    plan_item_id integer NOT NULL,
    user_id integer NOT NULL,
    create_datetime timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (plan_item_id, user_id)
);
ALTER TABLE customer_price ADD CONSTRAINT customer_price_plan_item_id_FK FOREIGN KEY (plan_item_id) REFERENCES plan_item (id);
ALTER TABLE customer_price ADD CONSTRAINT customer_price_user_id_FK FOREIGN KEY (user_id) REFERENCES base_user (id);

insert into jbilling_table (id, name) values (98, 'customer_price');

-- migrate item price to default item price_model
-- change insert sub-query "where currency_id = 1" to your primary currency id
-- postgresql
ALTER TABLE item add column price_model_id integer;
ALTER TABLE price_model add column tmp_item_id integer;
insert into price_model (id, strategy_type, tmp_item_id, rate, currency_id) (select distinct on (item_id) id, 'METERED', item_id, price, currency_id from item_price where currency_id = 1);
update item i set price_model_id = m.id from price_model m where m.tmp_item_id = i.id
ALTER TABLE price_model drop column tmp_item_id;
-- mysql
-- ALTER TABLE item add column price_model_id integer;
-- ALTER TABLE price_model add column tmp_item_id integer;
-- insert into price_model (id, strategy_type, tmp_item_id, rate, currency_id) (select id, 'METERED', item_id, price, currency_id from item_price where currency_id = 1 group by item_id order by id);
-- update item, price_model set item.price_model_id = price_model.id where item.id = price_model.tmp_item_id;
-- ALTER TABLE price_model drop column tmp_item_id;

-- reset jbilling_seqs for price_model after inserting default item price_model
update jbilling_seqs set next_id = (select round(max(id)/100)+1 from price_model) where name = 'price_model';

-- drop legacy item_price table
DROP TABLE IF EXISTS item_price;
delete from jbilling_table where name = 'item_price';
delete from jbilling_seqs where name = 'item_price';

-- price model pricing plug-ins
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (79, 14, 'com.sapienter.jbilling.server.pricing.tasks.PriceModelPricingTask', 0);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (80, 14, 'com.sapienter.jbilling.server.pricing.tasks.TieredPriceModelPricingTask', 0);

-- external ACH storage plug-in
insert into pluggable_task_type  (id, category_id, class_name, min_parameters) values (84, 17, 'com.sapienter.jbilling.server.payment.tasks.SaveACHExternallyTask', 1);

-- Modified size of ACH records to allow encryption
alter table ach alter column aba_routing type character varying(40); -- postgresql
alter table ach alter column bank_account type character varying(60); -- postgresql
-- alter table ach modify aba_routing varchar(40); -- mysql
-- alter table ach modify bank_account varchar(60); -- mysql

-- payment authorization transaction id
ALTER TABLE payment_authorization ALTER COLUMN transaction_id TYPE character varying(40); -- postgresql
-- alter table payment_authorization modify transaction_id varchar(40); -- mysql

-- ach external storage gateway_key
alter table ach add column gateway_key varchar(100) default null;

-- one-time / recurring invoice line types
insert into invoice_line_type (id, description, order_position) values (6, 'item one-time', 3);
update invoice_line_type set description = 'item recurring' where id = 1;
update invoice_line_type set order_position = 4 where id = 4;
update invoice_line_type set order_position = 5 where id = 5;
update invoice_line_type set order_position = 6 where id = 2;

-- new billing process filter task 
insert into pluggable_task_type values (85, 20, 'com.sapienter.jbilling.server.process.task.BillableUserOrdersBillingProcessFilterTask', 0);

--Database changes required for Notifications Screen, gui branch
create table notification_category (
	id integer NOT NULL,
	CONSTRAINT notification_category_pk PRIMARY KEY(id)
);

ALTER TABLE notification_category OWNER TO jbilling;

insert into notification_category (id) values (1),(2),(3),(4);

insert into jbilling_table (id, name) values (93, 'notification_category');

insert into international_description (table_id, foreign_id, psudo_column, language_id, content) 
values (93, 1, 'description',1, 'Invoices'),(93, 2, 'description',1, 'Orders'),
(93, 3, 'description',1, 'Payments'),(93, 4, 'description',1, 'Users') 

--new column to store the notification category for this notification
ALTER table notification_message_type add column category_id integer;
ALTER table notification_message_type add constraint "category_id_fk_1" foreign key (category_id) references notification_category(id); --postgres
-- alter table notification_message_type add constraint category_id_fk_1 foreign key (category_id) references notification_category(id); -- mysql

update notification_message_type set category_id = 1 where id = 1;
update notification_message_type set category_id = 4 where id = 2;
update notification_message_type set category_id = 4 where id = 3;
update notification_message_type set category_id = 4 where id = 4;
update notification_message_type set category_id = 4 where id = 5;
update notification_message_type set category_id = 4 where id = 6;
update notification_message_type set category_id = 4 where id = 7;
update notification_message_type set category_id = 4 where id = 8;
update notification_message_type set category_id = 4 where id = 9;
update notification_message_type set category_id = 3 where id = 10;
update notification_message_type set category_id = 3 where id = 11;
update notification_message_type set category_id = 1 where id = 12;
update notification_message_type set category_id = 2 where id = 13;
update notification_message_type set category_id = 2 where id = 14;
update notification_message_type set category_id = 2 where id = 15;
update notification_message_type set category_id = 3 where id = 16;
update notification_message_type set category_id = 3 where id = 17;
update notification_message_type set category_id = 1 where id = 18;
update notification_message_type set category_id = 4 where id = 19;
update notification_message_type set category_id = 4 where id = 20;

-- plug-in categories now have a i18n description
alter table pluggable_task_type_category drop column description;
insert into international_description (table_id, foreign_id, psudo_column, language_id, content) values 
(23, 1, 'description',1, 'Item management and order line total calculation'),
(23, 2, 'description',1, 'Billing process: order filters'),
(23, 3, 'description',1, 'Billing process: invoice filters'),
(23, 4, 'description',1, 'Invoice presentation'),
(23, 5, 'description',1, 'Billing process: order periods calculation'),
(23, 6, 'description',1, 'Payment gateway integration'),
(23, 7, 'description',1, 'Notifications'),
(23, 8, 'description',1, 'Payment instrument selection'),
(23, 9, 'description',1, 'Penalties for overdue invoices'),
(23, 10, 'description',1, 'Alarms when a payment gateway is down'),
(23, 11, 'description',1, 'Subscription status manager'),
(23, 12, 'description',1, 'Parameters for asynchronous payment processing'),
(23, 13, 'description',1, 'Add one product to order'),
(23, 14, 'description',1, 'Product pricing'),
(23, 15, 'description',1, 'Mediation Reader'),
(23, 16, 'description',1, 'Mediation Processor'),
(23, 17, 'description',1, 'Generic internal events listener'),
(23, 18, 'description',1, 'External provisioning processor'),
(23, 19, 'description',1, 'Purchase validation against pre-paid balance / credit limit'),
(23, 20, 'description',1, 'Billing process: customer selection'),
(23, 21, 'description',1, 'Mediation Error Handler'),
(23, 22, 'description',1, 'Scheduled Plug-ins'),
(23, 23, 'description',1, 'Rules Generators');

ALTER table pluggable_task add column notes varchar(1000);

-- add descriptions to every plug-in type, so the new GUI can use them
delete from international_description where table_id = 24;
insert into international_description (table_id, foreign_id, psudo_column, language_id, content) values 
(24, 1, 'title',1, 'Default order totals'),
(24, 1, 'description',1, 'Calculates the order total and the total for each line, considering the item prices, the quantity and if the prices are percentage or not.'),
(24, 2, 'title',1, 'VAT'),
(24, 2, 'description',1, 'Adds an additional line to the order with a percentage charge to represent the value added tax.'),
(24, 3, 'title',1, 'Invoice due date'),
(24, 3, 'description',1, 'A very simple implementation that sets the due date of the invoice. The due date is calculated by just adding the period of time to the invoice date.'),
(24, 4, 'title',1, 'Default invoice composition.'),
(24, 4, 'description',1, 'This task will copy all the lines on the orders and invoices to the new invoice, considering the periods involved for each order, but not the fractions of periods. It will not copy the lines that are taxes. The quantity and total of each line will be multiplied by the amount of periods.'),
(24, 5, 'title',1, 'Standard Order Filter'),
(24, 5, 'description',1, 'Decides if an order should be included in an invoice for a given billing process.  This is done by taking the billing process time span, the order period, the active since/until, etc.'),
(24, 6, 'title',1, 'Standard Invoice Filter'),
(24, 6, 'description',1, 'Always returns true, meaning that the overdue invoice will be carried over to a new invoice.'),
(24, 7, 'title',1, 'Default Order Periods'),
(24, 7, 'description',1, 'Calculates the start and end period to be included in an invoice. This is done by taking the billing process time span, the order period, the active since/until, etc.'),
(24, 8, 'title',1, 'Authorize.net payment processor'),
(24, 8, 'description',1, 'Integration with the authorize.net payment gateway.'),
(24, 9, 'title',1, 'Standard Email Notification'),
(24, 9, 'description',1, 'Notifies a user by sending an email. It supports text and HTML emails'),
(24, 10, 'title',1, 'Default payment information'),
(24, 10, 'description',1, 'Finds the information of a payment method available to a customer, given priority to credit card. In other words, it will return the credit car of a customer or the ACH information in that order.'),
(24, 11, 'title',1, 'Testing plug-in for partner payouts'),
(24, 11, 'description',1, 'Plug-in useful only for testing'),
(24, 12, 'title',1, 'PDF invoice notification'),
(24, 12, 'description',1, 'Will generate a PDF version of an invoice.'),
(24, 14, 'title',1, 'No invoice carry over'),
(24, 14, 'description',1, 'Returns always false, which makes jBilling to never carry over an invoice into another newer invoice.'),
(24, 15, 'title',1, 'Default interest task'),
(24, 15, 'description',1, 'Will create a new order with a penalty item. The item is taken as a parameter to the task.'),
(24, 16, 'title',1, 'Anticipated order filter'),
(24, 16, 'description',1, 'Extends BasicOrderFilterTask, modifying the dates to make the order applicable a number of months before it would be by using the default filter.'),
(24, 17, 'title',1, 'Anticipate order periods.'),
(24, 17, 'description',1, 'Extends BasicOrderPeriodTask, modifying the dates to make the order applicable a number of months before itd be by using the default task.'),
(24, 19, 'title',1, 'Email & process authorize.net'),
(24, 19, 'description',1, 'Extends the standard authorize.net payment processor to also send an email to the company after processing the payment.'),
(24, 20, 'title',1, 'Payment gateway down alarm'),
(24, 20, 'description',1, 'Sends an email to the billing administrator as an alarm when a payment gateway is down.'),
(24, 21, 'title',1, 'Test payment processor'),
(24, 21, 'description',1, 'A test payment processor implementation to be able to test jBillings functions without using a real payment gateway.'),
(24, 22, 'title',1, 'Router payment processor based on Custom Fields'),
(24, 22, 'description',1, 'Allows a customer to be assigned a specific payment gateway. It checks a custom contact field to identify the gateway and then delegates the actual payment processing to another plugin.'),
(24, 23, 'title',1, 'Default subscription status manager'),
(24, 23, 'description',1, 'It determines how a payment event affects the subscription status of a user, considering its present status and a state machine.'),
(24, 24, 'title',1, 'ACH Commerce payment processor'),
(24, 24, 'description',1, 'Integration with the ACH commerce payment gateway.'),
(24, 25, 'title',1, 'Standard asynchronous parameters'),
(24, 25, 'description',1, 'A dummy task that does not add any parameters for asynchronous payment processing. This is the default.'),
(24, 26, 'title',1, 'Router asynchronous parameters'),
(24, 26, 'description',1, 'This plug-in adds parameters for asynchronous payment processing to have one processing message bean per payment processor. It is used in combination with the router payment processor plug-ins.'),

(24, 28, 'title',1, 'Standard Item Manager'),
(24, 28, 'description',1, 'It adds items to an order. If the item is already in the order, it only updates the quantity.'),
(24, 29, 'title',1, 'Rules Item Manager'),
(24, 29, 'description',1, 'This is a rules-based plug-in. It will do what the basic item manager does (actually calling it), but then it will execute external rules as well. These external rules have full control on changing the order that is getting new items.'),
(24, 30, 'title',1, 'Rules Line Total'),
(24, 30, 'description',1, 'This is a rules-based plug-in. It calculates the total for an order line (typically this is the price multiplied by the quantity), allowing for the execution of external rules.'),
(24, 31, 'title',1, 'Rules Pricing'),
(24, 31, 'description',1, 'This is a rules-based plug-in. It gives a price to an item by executing external rules. You can then add logic externally for pricing. It is also integrated with the mediation process by having access to the mediation pricing data.'),
(24, 32, 'title',1, 'Separator file reader'),
(24, 32, 'description',1, 'This is a reader for the mediation process. It reads records from a text file whose fields are separated by a character (or string).'),
(24, 33, 'title',1, 'Rules mediation processor'),
(24, 33, 'description',1, 'This is a rules-based plug-in (see chapter 7). It takes an event record from the mediation process and executes external rules to translate the record into billing meaningful data. This is at the core of the mediation component, see the “Telecom Guide” document for more information.'),
(24, 34, 'title',1, 'Fixed length file reader'),
(24, 34, 'description',1, 'This is a reader for the mediation process. It reads records from a text file whose fields have fixed positions,and the record has a fixed length.'),
(24, 35, 'title',1, 'Payment information without validation'),
(24, 35, 'description',1, 'This is exactly the same as the standard payment information task, the only difference is that it does not validate if the credit card is expired. Use this plug-in only if you want to submit payment with expired credit cards.'),
(24, 36, 'title',1, 'Notification task for testing'),
(24, 36, 'description',1, 'This plug-in is only used for testing purposes. Instead of sending an email (or other real notification), it simply stores the text to be sent in a file named emails_sent.txt.'),
(24, 37, 'title',1, 'Order periods calculator with pro rating.'),
(24, 37, 'description',1, 'This plugin takes into consideration the field cycle starts of orders to calculate fractional order periods.'),
(24, 38, 'title',1, 'Invoice composition task with pro-rating (day as fraction)'),
(24, 38, 'description',1, 'When creating an invoice from an order, this plug-in will pro-rate any fraction of a period taking a day as the smallest billable unit.'),
(24, 39, 'title',1, 'Payment process for the Intraanuity payment gateway'),
(24, 39, 'description',1, 'Integration with the Intraanuity payment gateway.'),
(24, 40, 'title',1, 'Automatic cancellation credit.'),
(24, 40, 'description',1, 'This plug-in will create a new order with a negative price to reflect a credit when an order is canceled within a period that has been already invoiced.'),
(24, 41, 'title',1, 'Fees for early cancellation of a plan.'),
(24, 41, 'description',1, 'This plug-in will use external rules to determine if an order that is being canceled should create a new order with a penalty fee. This is typically used for early cancels of a contract.'),
(24, 42, 'title',1, 'Blacklist filter payment processor.'),
(24, 42, 'description',1, 'Used for blocking payments from reaching real payment processors. Typically configured as first payment processor in the processing chain.'),
(24, 43, 'title',1, 'Blacklist user when their status becomes suspended or higher.'),
(24, 43, 'description',1, 'Causes users and their associated details (e.g., credit card number, phone number, etc.) to be blacklisted when their status becomes suspended or higher. '),
(24, 44, 'title',1, 'JDBC Mediation Reader.'),
(24, 44, 'description',1, 'This is a reader for the mediation process. It reads records from a JDBC database source.'),
(24, 45, 'title',1, 'MySQL Mediation Reader.'),
(24, 45, 'description',1, 'This is a reader for the mediation process. It is an extension of the JDBC reader, allowing easy configuration of a MySQL database source.'),
(24, 46, 'title',1, 'Provisioning commands rules task.'),
(24, 46, 'description',1, 'Responds to order related events. Runs rules to generate commands to send via JMS messages to the external provisioning module.'),
(24, 47, 'title',1, 'Test external provisioning task.'),
(24, 47, 'description',1, 'This plug-in is only used for testing purposes. It is a test external provisioning task for testing the provisioning modules.'),
(24, 48, 'title',1, 'CAI external provisioning task.'),
(24, 48, 'description',1, 'An external provisioning plug-in for communicating with the Ericsson Customer Administration Interface (CAI).'),
(24, 49, 'title',1, 'Currency Router payment processor'),
(24, 49, 'description',1, 'Delegates the actual payment processing to another plug-in based on the currency of the payment.'),
(24, 50, 'title',1, 'MMSC external provisioning task.'),
(24, 50, 'description',1, 'An external provisioning plug-in for communicating with the TeliaSonera MMSC.'),
(24, 51, 'title',1, 'Filters out negative invoices for carry over.'),
(24, 51, 'description',1, 'This filter will only invoices with a positive balance to be carried over to the next invoice.'),
(24, 52, 'title',1, 'File invoice exporter.'),
(24, 52, 'description',1, 'It will generate a file with one line per invoice generated.'),
(24, 53, 'title',1, 'Rules caller on an event.'),
(24, 53, 'description',1, 'It will call a package of rules when an internal event happens.'),
(24, 54, 'title',1, 'Dynamic balance manager'),
(24, 54, 'description',1, 'It will update the dynamic balance of a customer (pre-paid or credit limit) when events affecting the balance happen.'),
(24, 55, 'title',1, 'Balance validator based on the customer balance.'),
(24, 55, 'description',1, 'Used for real-time mediation, this plug-in will validate a call based on the current dynamic balance of a customer.'),
(24, 56, 'title',1, 'Balance validator based on rules.'),
(24, 56, 'description',1, 'Used for real-time mediation, this plug-in will validate a call based on a package or rules'),
(24, 57, 'title',1, 'Payment processor for Payments Gateway.'),
(24, 57, 'description',1, 'Integration with the Payments Gateway payment processor.'),
(24, 58, 'title',1, 'Credit cards are stored externally.'),
(24, 58, 'description',1, 'Saves the credit card information in the payment gateway, rather than the jBilling DB.'),
(24, 59, 'title',1, 'Rules Item Manager 2'),
(24, 59, 'description',1, 'This is a rules-based plug-in compatible with the mediation module of jBilling 2.2.x. It will do what the basic item manager does (actually calling it), but then it will execute external rules as well. These external rules have full control on changing the order that is getting new items.'),
(24, 60, 'title',1, 'Rules Line Total - 2'),
(24, 60, 'description',1, 'This is a rules-based plug-in, compatible with the mediation process of jBilling 2.2.x and later. It calculates the total for an order line (typically this is the price multiplied by the quantity), allowing for the execution of external rules.'),
(24, 61, 'title',1, 'Rules Pricing 2'),
(24, 61, 'description',1, 'This is a rules-based plug-in compatible with the mediation module of jBilling 2.2.x. It gives a price to an item by executing external rules. You can then add logic externally for pricing. It is also integrated with the mediation process by having access to the mediation pricing data.'),
(24, 63, 'title',1, 'Test payment processor for external storage.'),
(24, 63, 'description',1, 'A fake plug-in to test payments that would be stored externally.'),
(24, 64, 'title',1, 'WorldPay integration'),
(24, 64, 'description',1, 'Payment processor plug-in to integrate with RBS WorldPay'),
(24, 65, 'title',1, 'WorldPay integration with external storage'),
(24, 65, 'description',1, 'Payment processor plug-in to integrate with RBS WorldPay. It stores the credit card information (number, etc) in the gateway.'),
(24, 66, 'title',1, 'Auto recharge'),
(24, 66, 'description',1, 'Monitors the balance of a customer and upon reaching a limit, it requests a real-time payment'),
(24, 67, 'title',1, 'Beanstream gateway integration'),
(24, 67, 'description',1, 'Payment processor for integration with the Beanstream payment gateway'),
(24, 68, 'title',1, 'Sage payments gateway integration'),
(24, 68, 'description',1, 'Payment processor for integration with the Sage payment gateway'),
(24, 69, 'title',1, 'Standard billing process users filter'),
(24, 69, 'description',1, 'Called when the billing process runs to select which users to evaluate. This basic implementation simply returns every user not in suspended (or worse) status'),
(24, 70, 'title',1, 'Selective billing process users filter'),
(24, 70, 'description',1, 'Called when the billing process runs to select which users to evaluate. This only returns users with orders that have a next invoice date earlier than the billing process.'),
(24, 71, 'title',1, 'Mediation file error handler'),
(24, 71, 'description',1, 'Event records with errors are saved to a file'),
(24, 73, 'title',1, 'Mediation data base error handler'),
(24, 73, 'description',1, 'Event records with errors are saved to a database table'),
(24, 75, 'title',1, 'Paypal integration with external storage'),
(24, 75, 'description',1, 'Submits payments to paypal as a payment gateway and stores credit card information in PayPal as well'),
(24, 76, 'title',1, 'Authorize.net integration with external storage'),
(24, 76, 'description',1, 'Submits payments to authorize.net as a payment gateway and stores credit card information in authorize.net as well'),
(24, 77, 'title',1, 'Payment method router payment processor'),
(24, 77, 'description',1, 'Delegates the actual payment processing to another plug-in based on the payment method of the payment.'),
(24, 78, 'title',1, 'Dynamic rules generator'),
(24, 78, 'description',1, 'Generates rules dynamically based on a Velocity template.'),
(24, 79, 'title',1, 'Mediation Process Task'),
(24, 79, 'description',1, 'A scheduled task to execute the Mediation Process.'),
(24, 80, 'title',1, 'Billing Process Task'),
(24, 80, 'description',1, 'A scheduled task to execute the Billing Process.');

-- filter database tables
drop table if exists filter;
create table filter (
    id int not null,
    filter_set_id int not null,
    type varchar(255) not null,
    constraint_type varchar(255) not null,
    field varchar(255) not null,
    template varchar(255) not null,
    visible bool not null,
    integer_value int,
    string_value varchar(255),
    start_date_value timestamp,
    end_date_value timestamp,
    version int not null,
    primary key (id)
);

drop table if exists filter_set;
create table filter_set (
    id int not null,
    name varchar(255) not null,
    user_id int not null,
    version int not null,    
    primary key (id)
);

insert into jbilling_seqs (name, next_id) values ('filter', 1);
insert into jbilling_seqs (name, next_id) values ('filter_set', 1);

-- recent item tables
drop table if exists recent_item;
create table recent_item (
  id int not null,  
  type varchar(255) not null,
  object_id int not null,
  user_id int not null,
  version int not null,
  primary key (id)
);

insert into jbilling_seqs (name, next_id) values ('recent_item', 1);

-- breadcrumb tables
drop table if exists breadcrumb;
create table breadcrumb (
    id int not null,
    user_id int not null,
    controller varchar(255) not null,
    action varchar(255),
    name varchar(255),
    object_id int,
    version int not null,
    primary key (id)
);

insert into jbilling_seqs (name, next_id) values ('breadcrumb', 1);

-- contact type optlock
alter table contact_type add column OPTLOCK int null;
update contact_type set OPTLOCK = 0;
alter table contact_type alter column OPTLOCK set not null;

-- Orders should always have an active since date
update purchase_order set active_since = date(create_datetime) where active_since is null;

-- custom contact fields
--- table id entry
INSERT INTO jbilling_table(name, id) VALUES ('contact_field_type', 1+(select max(id) from jbilling_table ));

--- table id generator
INSERT INTO jbilling_seqs(name, next_id) VALUES ('contact_field_type', 10);

---optlock column
alter table contact_field_type  add column OPTLOCK int null;
update contact_field_type set OPTLOCK = 0; 
alter table contact_field_type alter column OPTLOCK set not null;

---Entries to bring the old contact field type descriptions from the properties file
---into the international_description table for i18n purposes. 
---The below query should be made once per properties file. In the query below:
---    1. 'content' value comes from the property value in the language properties file
---    2. 'foreign_id' is the row id of the contact_field_type' table
---    3. 'language_id' is the id column value of the language table for the corresponding language
---	   4. 'table_id' is the id columne value of the jbilling_table table where name = 'contact_field_type'

insert into international_description (table_id, foreign_id, psudo_column, language_id, content) 
values (99, 1, 'description', 1, 'Referral Fee'), (99, 2, 'description', 1, 'Payment Processor'), (99, 3, 'description', 1, 'IP Address');


-- internal item types
alter table item_type add column internal bool null;
update item_type set internal = false;
alter table item_type alter column internal set not null;

-- internal 'plans' category, add for each company
insert into item_type (id, entity_id, description, internal, order_line_type_id, optlock) values ((select max(id)+1 from item_type), 1, 'plans', true, 1, 0);

-- price model chaining
alter table price_model add column next_model_id int null;
alter table price_model add constraint price_model_next_id_FK foreign key (next_model_id) references price_model (id);

-- plan item bundled quantity
alter table plan_item add column bundled_quantity numeric(22,10) null;

-- nullable price model rate and currency
alter table price_model alter column rate drop not null;
alter table price_model alter column currency_id drop not null;

-- target period for plans and plan items
alter table plan add column period_id int;
update plan set period_id = (select min(id) from order_period where entity_id = 1);
alter table plan alter column period_id set not null;
alter table plan add constraint plan_period_id_FK foreign key (period_id) references order_period (id);

alter table plan_item add column period_id int;
alter table plan_item add constraint plan_item_period_id_FK foreign key (period_id) references order_period (id);

-- descriptions of messages for the audit log screens
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 20, 'description', 1, 'User subscription status has changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 32, 'description', 1, 'User subscription status has NOT changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 21, 'description', 1, 'User account is now locked');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 33, 'description', 1, 'The dynamic balance of a user has changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 34, 'description', 1, 'The invoice if child flag has changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 17, 'description', 1, 'The order line has been updated');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 18, 'description', 1, 'The order next billing date has been changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 22, 'description', 1, 'The order main subscription flag was changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 26, 'description', 1, 'An invoiced order was cancelled, a credit order was created');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 24, 'description', 1, 'A valid payment method was not found. The payment request was cancelled');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 23, 'description', 1, 'All the one-time orders the mediation found were in status finished');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 27, 'description', 1, 'A user id was added to the blacklist');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 28, 'description', 1, 'A user id was removed from the blacklist');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 29, 'description', 1, 'Posted a provisioning command using a UUID');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 30, 'description', 1, 'A command was posted for provisioning');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 31, 'description', 1, 'The provisioning status of an order line has changed');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 25, 'description', 1, 'A new row has been created');
insert into international_description (table_id, foreign_id, psudo_column, language_id, content)
values (47, 19, 'description', 1, 'Last API call to get the the user subscription status transitions');

-- lengthen the preference int value to allow for longer mediation "last read ID" values
-- alter table preference modify int_value int4 null default null; -- mysql
alter table preference alter int_value type int4; -- postgresql

--shortcut tables
CREATE TABLE shortcut
(
  id integer NOT NULL,
  user_id integer NOT NULL,
  controller character varying(255) NOT NULL,
  "action" character varying(255),
  "name" character varying(255),
  object_id integer,
  "version" integer NOT NULL,
  PRIMARY KEY (id)
);

insert into jbilling_seqs values ('shortcut', 1);

--gl code new field in item table
alter table item add column gl_code character varying (50);

-- drop item manual pricing flag
alter table item drop column price_manual;

-- drop legacy reporting tables
drop table report_field;
drop table report_type_map;
drop table report_type;
drop table report_user;
drop table report_entity_map;
drop table report;

delete from jbilling_seqs where name in ('report_field', 'report_type_map', 'report_type', 'report_user', 'report_entity_map', 'report');
delete from international_description where table_id in (
  select id from jbilling_table where name in ('report_field', 'report_type_map', 'report_type', 'report_user', 'report_entity_map', 'report')
)
delete from jbilling_table where name in ('report_field', 'report_type_map', 'report_type', 'report_user', 'report_entity_map', 'report');

-- new reports tables
create table report (
    id int NOT NULL,
    type_id int NOT NULL,
    name varchar(255) NOT NULL,
    file_name varchar(500) NOT NULL,
    OPTLOCK int NOT NULL
);
create table report_type (
    id int NOT NULL,
    name varchar(255) NOT NULL,
    OPTLOCK int NOT NULL
);
create table report_parameter (
    id int NOT NULL,
    name varchar(255) NOT NULL,
    dtype varchar(10) NOT NULL
);

insert into jbilling_table (id, name) values (100, 'report');
insert into jbilling_table (id, name) values (101, 'report_type');
insert into jbilling_table (id, name) values (102, 'report_parameter');

insert into jbilling_seqs (name, next_id) values ('report', 1);
insert into jbilling_seqs (name, next_id) values ('report_type', 1);
insert into jbilling_seqs (name, next_id) values ('report_parameter', 1);

