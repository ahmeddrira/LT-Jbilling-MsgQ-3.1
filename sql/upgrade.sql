-- this script will upgrade a database schema from the latest jbilling release
-- to the code currently at the tip of the trunk.
-- It is tested on postgreSQL, but it is meant to be ANSI SQL

-- Edit payments
alter table payment add column update_datetime timestamp without time zone;
INSERT INTO event_log_module (id) VALUES (10);

-- Forgot password 
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 20, 'description', 1, 'Lost password');
INSERT INTO notification_message_type (id, sections) VALUES (20, 2);