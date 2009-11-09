-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL

-- validatePurchase changes
insert into pluggable_task_type_category values (19, 'Validate Purchase', 'com.sapienter.jbilling.server.user.tasks.IValidatePurchaseTask');
insert into pluggable_task_type values (55, 19, 'com.sapienter.jbilling.server.user.tasks.UserBalanceValidatePurchaseTask', 0);
insert into pluggable_task_type values (56, 19, 'com.sapienter.jbilling.server.user.tasks.RulesValidatePurchaseTask', 0);

-- new event log column
alter table event_log add column affected_user_id integer;
alter table event_log add constraint "event_log_fk_6" foreign key (affected_user_id) references base_user(id);

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

update report set tables_list = 'base_user, invoice, currency , international_description, generic_status', where_str = 'base_user.id = invoice.user_id and invoice.currency_id = currency.id and invoice.status_id = generic_status.id and generic_status.dtype = ''invoice_status'' and international_description.foreign_id = generic_status.status_value and international_description.table_id = 90'
where id = 2;
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

-- new rules pricing plug-in
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (60, 14, 'com.sapienter.jbilling.server.item.tasks.RulesPricingTask2', 0);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (61, 13, 'com.sapienter.jbilling.server.order.task.RulesItemManager2', 0);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (62, 1, 'com.sapienter.jbilling.server.order.task.RulesLineTotalTask2', 0);

-- world pay task
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (63, 6, 'com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayTask', 3);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (64, 6, 'com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayExternalTask', 3);

-- automatic recharge task
alter table customer add column auto_recharge double precision;
insert into preference_type (id, int_def_value, str_def_value, float_def_value) values (49, null, null, null);
insert into pluggable_task_type (id, category_id, class_name, min_parameters) values (65, 17, 'com.sapienter.jbilling.server.user.tasks.AutoRechargeTask', 0);