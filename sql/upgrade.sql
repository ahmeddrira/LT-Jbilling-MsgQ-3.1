-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL

-- add the the balance fields
alter table customer add balance_type integer;
update customer set balance_type = 1;
alter table customer alter column balance_type set not null;
alter table customer add dynamic_balance DOUBLE PRECISION ;
alter table customer add credit_limit DOUBLE PRECISION ;
