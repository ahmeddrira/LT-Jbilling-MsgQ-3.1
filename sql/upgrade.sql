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
