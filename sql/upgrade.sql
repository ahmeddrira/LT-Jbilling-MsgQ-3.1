-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL

-- add the the balance fields
alter table customer add balance_type integer;
update customer set balance_type = 1;
alter table customer alter column balance_type set not null;
alter table customer add dynamic_balance DOUBLE PRECISION ;
alter table customer add credit_limit DOUBLE PRECISION ;

-- for the GUI to support balance type
insert into jbilling_table values (89, 'balance_type', 0);
insert into international_description values (89, 1, 'description', 1, 'None');
insert into international_description values (89, 2, 'description', 1, 'Pre-paid balance');
insert into international_description values (89, 3, 'description', 1, 'Credit limit');

-- obsolete item user pricing
drop table item_user_price;
delete from jbilling_table where name = 'item_user_price';

-- dynamic balance changes
insert into event_log_message values (33);
insert into pluggable_task_type values (54, 17, 'com.sapienter.jbilling.server.user.balance.DynamicBalanceManagerTask',0);
-- check the correct id, ignore if this install does not care about pre-paid/credit limit balances.
insert into pluggable_task values (541, 1, 54, 1, 1);
