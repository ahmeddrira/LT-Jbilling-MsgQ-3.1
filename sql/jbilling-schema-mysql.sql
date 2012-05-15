
# -----------------------------------------------------------------------
# ach
# -----------------------------------------------------------------------
drop table if exists ach;

CREATE TABLE ach
(
    id INTEGER NOT NULL,
    user_id INTEGER,
    aba_routing VARCHAR(40) NOT NULL,
    bank_account VARCHAR(60) NOT NULL,
    account_type INTEGER NOT NULL,
    bank_name VARCHAR(50) NOT NULL,
    account_name VARCHAR(100) NOT NULL,
    gateway_key VARCHAR(100),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ach_i_2 (user_id));


# -----------------------------------------------------------------------
# ageing_entity_step
# -----------------------------------------------------------------------
drop table if exists ageing_entity_step;

CREATE TABLE ageing_entity_step
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    status_id INTEGER,
    days INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# base_user
# -----------------------------------------------------------------------
drop table if exists base_user;

CREATE TABLE base_user
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    password VARCHAR(40),
    deleted SMALLINT default 0 NOT NULL,
    language_id INTEGER,
    status_id INTEGER,
    subscriber_status INTEGER default 1,
    currency_id INTEGER,
    create_datetime TIMESTAMP NOT NULL,
    last_status_change TIMESTAMP,
    last_login TIMESTAMP,
    user_name VARCHAR(50),
    failed_attempts INTEGER default 0 NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    UNIQUE (entity_id, user_name, status_id),
    INDEX ix_base_user_un (entity_id, user_name));


# -----------------------------------------------------------------------
# billing_process
# -----------------------------------------------------------------------
drop table if exists billing_process;

CREATE TABLE billing_process
(
    id INTEGER NOT NULL,
    entity_id INTEGER NOT NULL,
    billing_date DATETIME NOT NULL,
    period_unit_id INTEGER NOT NULL,
    period_value INTEGER NOT NULL,
    is_review INTEGER NOT NULL,
    paper_invoice_batch_id INTEGER,
    retries_to_do INTEGER default 0 NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# billing_process_configuration
# -----------------------------------------------------------------------
drop table if exists billing_process_configuration;

CREATE TABLE billing_process_configuration
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    next_run_date DATETIME NOT NULL,
    generate_report SMALLINT NOT NULL,
    retries INTEGER,
    days_for_retry INTEGER,
    days_for_report INTEGER,
    review_status INTEGER NOT NULL,
    period_unit_id INTEGER NOT NULL,
    period_value INTEGER NOT NULL,
    due_date_unit_id INTEGER NOT NULL,
    due_date_value INTEGER NOT NULL,
    df_fm SMALLINT,
    only_recurring SMALLINT default 1 NOT NULL,
    invoice_date_process SMALLINT NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    auto_payment SMALLINT default 0 NOT NULL,
    maximum_periods INTEGER default 1 NOT NULL,
    auto_payment_application INTEGER default 0 NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# breadcrumb
# -----------------------------------------------------------------------
drop table if exists breadcrumb;

CREATE TABLE breadcrumb
(
    id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    controller VARCHAR(255) NOT NULL,
    action VARCHAR(255),
    name VARCHAR(255),
    object_id INTEGER,
    description VARCHAR(255),
    version INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# process_run
# -----------------------------------------------------------------------
drop table if exists process_run;

CREATE TABLE process_run
(
    id INTEGER NOT NULL,
    process_id INTEGER,
    run_date DATETIME NOT NULL,
    started TIMESTAMP NOT NULL,
    finished TIMESTAMP,
    payment_finished TIMESTAMP,
    invoices_generated INTEGER,
    OPTLOCK INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX bp_run_process_ix (process_id));


# -----------------------------------------------------------------------
# process_run_total
# -----------------------------------------------------------------------
drop table if exists process_run_total;

CREATE TABLE process_run_total
(
    id INTEGER NOT NULL,
    process_run_id INTEGER,
    currency_id INTEGER NOT NULL,
    total_invoiced DECIMAL(22,10),
    total_paid DECIMAL(22,10),
    total_not_paid DECIMAL(22,10),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX bp_run_total_run_ix (process_run_id));


# -----------------------------------------------------------------------
# process_run_total_pm
# -----------------------------------------------------------------------
drop table if exists process_run_total_pm;

CREATE TABLE process_run_total_pm
(
    id INTEGER NOT NULL,
    process_run_total_id INTEGER,
    payment_method_id INTEGER,
    total DECIMAL(22,10) NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX bp_pm_index_total (process_run_total_id));


# -----------------------------------------------------------------------
# process_run_user
# -----------------------------------------------------------------------
drop table if exists process_run_user;

CREATE TABLE process_run_user
(
    id INTEGER NOT NULL,
    process_run_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    status INTEGER NOT NULL,
    created DATETIME NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX bp_run_user_run_ix (process_run_id, user_id));


# -----------------------------------------------------------------------
# contact
# -----------------------------------------------------------------------
drop table if exists contact;

CREATE TABLE contact
(
    id INTEGER NOT NULL,
    organization_name VARCHAR(200),
    street_addres1 VARCHAR(100),
    street_addres2 VARCHAR(100),
    city VARCHAR(50),
    state_province VARCHAR(30),
    postal_code VARCHAR(15),
    country_code VARCHAR(2),
    last_name VARCHAR(30),
    first_name VARCHAR(30),
    person_initial VARCHAR(5),
    person_title VARCHAR(40),
    phone_country_code INTEGER,
    phone_area_code INTEGER,
    phone_phone_number VARCHAR(20),
    fax_country_code INTEGER,
    fax_area_code INTEGER,
    fax_phone_number VARCHAR(20),
    email VARCHAR(200),
    create_datetime TIMESTAMP NOT NULL,
    deleted SMALLINT default 0 NOT NULL,
    notification_include SMALLINT default 1,
    user_id INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_contact_fname (first_name),
    INDEX ix_contact_lname (last_name),
    INDEX ix_contact_orgname (organization_name),
    INDEX contact_i_del (deleted),
    INDEX ix_contact_fname_lname (first_name, last_name),
    INDEX ix_contact_address (street_addres1, city, postal_code, street_addres2, state_province, country_code),
    INDEX ix_contact_phone (phone_phone_number, phone_area_code, phone_country_code),
    INDEX ix_user (user_id));


# -----------------------------------------------------------------------
# contact_map
# -----------------------------------------------------------------------
drop table if exists contact_map;

CREATE TABLE contact_map
(
    id INTEGER NOT NULL,
    contact_id INTEGER,
    type_id INTEGER NOT NULL,
    table_id INTEGER NOT NULL,
    foreign_id INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX contact_map_i_3 (contact_id),
    INDEX contact_map_i_1 (table_id, foreign_id, type_id));


# -----------------------------------------------------------------------
# contact_type
# -----------------------------------------------------------------------
drop table if exists contact_type;

CREATE TABLE contact_type
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    is_primary SMALLINT,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# country
# -----------------------------------------------------------------------
drop table if exists country;

CREATE TABLE country
(
    id INTEGER NOT NULL,
    code VARCHAR(2) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# credit_card
# -----------------------------------------------------------------------
drop table if exists credit_card;

CREATE TABLE credit_card
(
    id INTEGER NOT NULL,
    cc_number VARCHAR(100) NOT NULL,
    cc_number_plain VARCHAR(20),
    cc_expiry DATETIME NOT NULL,
    name VARCHAR(150),
    cc_type INTEGER NOT NULL,
    deleted SMALLINT default 0 NOT NULL,
    gateway_key VARCHAR(100),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_cc_number (cc_number_plain),
    INDEX ix_cc_number_encrypted (cc_number));


# -----------------------------------------------------------------------
# currency
# -----------------------------------------------------------------------
drop table if exists currency;

CREATE TABLE currency
(
    id INTEGER NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    code VARCHAR(3) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# currency_entity_map
# -----------------------------------------------------------------------
drop table if exists currency_entity_map;

CREATE TABLE currency_entity_map
(
    currency_id INTEGER,
    entity_id INTEGER,
    INDEX currency_entity_map_i_2 (currency_id, entity_id));


# -----------------------------------------------------------------------
# currency_exchange
# -----------------------------------------------------------------------
drop table if exists currency_exchange;

CREATE TABLE currency_exchange
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    currency_id INTEGER,
    rate DECIMAL(22,10) NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    valid_since TIMESTAMP NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# customer
# -----------------------------------------------------------------------
drop table if exists customer;

CREATE TABLE customer
(
    id INTEGER NOT NULL,
    user_id INTEGER,
    partner_id INTEGER,
    referral_fee_paid SMALLINT,
    invoice_delivery_method_id INTEGER NOT NULL,
    notes VARCHAR(1000),
    auto_payment_type INTEGER,
    due_date_unit_id INTEGER,
    due_date_value INTEGER,
    df_fm SMALLINT,
    parent_id INTEGER,
    is_parent SMALLINT,
    exclude_aging SMALLINT default 0 NOT NULL,
    invoice_child SMALLINT,
    use_parent_pricing BIT NOT NULL,
    current_order_id INTEGER,
    balance_type INTEGER default 1 NOT NULL,
    dynamic_balance DECIMAL(22,10),
    credit_limit DECIMAL(22,10),
    auto_recharge DECIMAL(22,10),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX customer_i_2 (user_id));


# -----------------------------------------------------------------------
# entity
# -----------------------------------------------------------------------
drop table if exists entity;

CREATE TABLE entity
(
    id INTEGER NOT NULL,
    external_id VARCHAR(20),
    description VARCHAR(100) NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    language_id INTEGER NOT NULL,
    currency_id INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# entity_delivery_method_map
# -----------------------------------------------------------------------
drop table if exists entity_delivery_method_map;

CREATE TABLE entity_delivery_method_map
(
    method_id INTEGER,
    entity_id INTEGER);


# -----------------------------------------------------------------------
# entity_payment_method_map
# -----------------------------------------------------------------------
drop table if exists entity_payment_method_map;

CREATE TABLE entity_payment_method_map
(
    entity_id INTEGER,
    payment_method_id INTEGER);


# -----------------------------------------------------------------------
# event_log
# -----------------------------------------------------------------------
drop table if exists event_log;

CREATE TABLE event_log
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    user_id INTEGER,
    affected_user_id INTEGER,
    table_id INTEGER,
    foreign_id INTEGER NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    level_field INTEGER NOT NULL,
    module_id INTEGER NOT NULL,
    message_id INTEGER NOT NULL,
    old_num INTEGER,
    old_str VARCHAR(1000),
    old_date TIMESTAMP,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_el_main (module_id, message_id, create_datetime));


# -----------------------------------------------------------------------
# event_log_message
# -----------------------------------------------------------------------
drop table if exists event_log_message;

CREATE TABLE event_log_message
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# event_log_module
# -----------------------------------------------------------------------
drop table if exists event_log_module;

CREATE TABLE event_log_module
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# filter
# -----------------------------------------------------------------------
drop table if exists filter;

CREATE TABLE filter
(
    id INTEGER NOT NULL,
    filter_set_id INTEGER NOT NULL,
    type VARCHAR(255) NOT NULL,
    constraint_type VARCHAR(255) NOT NULL,
    field VARCHAR(255) NOT NULL,
    template VARCHAR(255) NOT NULL,
    visible BIT NOT NULL,
    integer_value INTEGER,
    string_value VARCHAR(255),
    start_date_value TIMESTAMP,
    end_date_value TIMESTAMP,
    boolean_value BIT,
    decimal_value DECIMAL(22,10),
    decimal_high_value DECIMAL(22,10),
    version INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# filter_set
# -----------------------------------------------------------------------
drop table if exists filter_set;

CREATE TABLE filter_set
(
    id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    user_id INTEGER NOT NULL,
    version INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# international_description
# -----------------------------------------------------------------------
drop table if exists international_description;

CREATE TABLE international_description
(
    table_id INTEGER NOT NULL,
    foreign_id INTEGER NOT NULL,
    psudo_column VARCHAR(20) NOT NULL,
    language_id INTEGER NOT NULL,
    content VARCHAR(4000) NOT NULL,
    PRIMARY KEY(table_id,foreign_id,psudo_column,language_id),
    INDEX international_description_i_2 (table_id, foreign_id, language_id),
    INDEX int_description_i_lan (language_id));


# -----------------------------------------------------------------------
# invoice
# -----------------------------------------------------------------------
drop table if exists invoice;

CREATE TABLE invoice
(
    id INTEGER NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    billing_process_id INTEGER,
    user_id INTEGER,
    status_id INTEGER NOT NULL,
    delegated_invoice_id INTEGER,
    due_date DATETIME NOT NULL,
    total DECIMAL(22,10) NOT NULL,
    payment_attempts INTEGER default 0 NOT NULL,
    balance DECIMAL(22,10),
    carried_balance DECIMAL(22,10) NOT NULL,
    in_process_payment SMALLINT default 1 NOT NULL,
    is_review INTEGER NOT NULL,
    currency_id INTEGER NOT NULL,
    deleted SMALLINT default 0 NOT NULL,
    paper_invoice_batch_id INTEGER,
    customer_notes VARCHAR(1000),
    public_number VARCHAR(40),
    last_reminder DATETIME,
    overdue_step INTEGER,
    create_timestamp TIMESTAMP NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_invoice_user_id (user_id, deleted),
    INDEX ix_invoice_date (create_datetime),
    INDEX ix_invoice_number (user_id, public_number),
    INDEX ix_invoice_due_date (user_id, due_date),
    INDEX ix_invoice_ts (create_timestamp, user_id),
    INDEX ix_invoice_process (billing_process_id));


# -----------------------------------------------------------------------
# invoice_delivery_method
# -----------------------------------------------------------------------
drop table if exists invoice_delivery_method;

CREATE TABLE invoice_delivery_method
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# invoice_line
# -----------------------------------------------------------------------
drop table if exists invoice_line;

CREATE TABLE invoice_line
(
    id INTEGER NOT NULL,
    invoice_id INTEGER,
    type_id INTEGER,
    amount DECIMAL(22,10) NOT NULL,
    quantity DECIMAL(22,10),
    price DECIMAL(22,10),
    deleted SMALLINT default 0 NOT NULL,
    item_id INTEGER,
    description VARCHAR(1000),
    source_user_id INTEGER,
    is_percentage SMALLINT default 0 NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_invoice_line_invoice_id (invoice_id));


# -----------------------------------------------------------------------
# invoice_line_type
# -----------------------------------------------------------------------
drop table if exists invoice_line_type;

CREATE TABLE invoice_line_type
(
    id INTEGER NOT NULL,
    description VARCHAR(50) NOT NULL,
    order_position INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# item
# -----------------------------------------------------------------------
drop table if exists item;

CREATE TABLE item
(
    id INTEGER NOT NULL,
    internal_number VARCHAR(50),
    entity_id INTEGER,
    percentage DECIMAL(22,10),
    deleted SMALLINT default 0 NOT NULL,
    has_decimals SMALLINT default 0 NOT NULL,
    gl_code VARCHAR(50),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_item_ent (entity_id, internal_number));


# -----------------------------------------------------------------------
# item_price_timeline
# -----------------------------------------------------------------------
drop table if exists item_price_timeline;

CREATE TABLE item_price_timeline
(
    item_id INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL,
    price_model_id INTEGER NOT NULL,
    PRIMARY KEY(item_id,start_date),
    UNIQUE (price_model_id));


# -----------------------------------------------------------------------
# item_type
# -----------------------------------------------------------------------
drop table if exists item_type;

CREATE TABLE item_type
(
    id INTEGER NOT NULL,
    entity_id INTEGER NOT NULL,
    description VARCHAR(100),
    internal BIT NOT NULL,
    order_line_type_id INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# item_type_map
# -----------------------------------------------------------------------
drop table if exists item_type_map;

CREATE TABLE item_type_map
(
    item_id INTEGER,
    type_id INTEGER);


# -----------------------------------------------------------------------
# item_type_exclude_map
# -----------------------------------------------------------------------
drop table if exists item_type_exclude_map;

CREATE TABLE item_type_exclude_map
(
    item_id INTEGER,
    type_id INTEGER);


# -----------------------------------------------------------------------
# price_model
# -----------------------------------------------------------------------
drop table if exists price_model;

CREATE TABLE price_model
(
    id INTEGER NOT NULL,
    strategy_type VARCHAR(40) NOT NULL,
    rate DECIMAL(22,10),
    currency_id INTEGER,
    next_model_id INTEGER,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# price_model_attribute
# -----------------------------------------------------------------------
drop table if exists price_model_attribute;

CREATE TABLE price_model_attribute
(
    price_model_id INTEGER NOT NULL,
    attribute_name VARCHAR(255) NOT NULL,
    attribute_value VARCHAR(255),
    PRIMARY KEY(price_model_id,attribute_name));


# -----------------------------------------------------------------------
# plan
# -----------------------------------------------------------------------
drop table if exists plan;

CREATE TABLE plan
(
    id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    period_id INTEGER NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# plan_item
# -----------------------------------------------------------------------
drop table if exists plan_item;

CREATE TABLE plan_item
(
    id INTEGER NOT NULL,
    plan_id INTEGER,
    item_id INTEGER NOT NULL,
    plan_item_bundle_id INTEGER,
    precedence INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX plan_item_precedence_i (precedence),
    INDEX plan_item_item_id_i (item_id));


# -----------------------------------------------------------------------
# plan_item_price_timeline
# -----------------------------------------------------------------------
drop table if exists plan_item_price_timeline;

CREATE TABLE plan_item_price_timeline
(
    plan_item_id INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL,
    price_model_id INTEGER NOT NULL,
    PRIMARY KEY(plan_item_id,start_date),
    UNIQUE (price_model_id));


# -----------------------------------------------------------------------
# plan_item_bundle
# -----------------------------------------------------------------------
drop table if exists plan_item_bundle;

CREATE TABLE plan_item_bundle
(
    id INTEGER NOT NULL,
    quantity DECIMAL(22,10) NOT NULL,
    period_id INTEGER NOT NULL,
    target_customer VARCHAR(20) NOT NULL,
    add_if_exists BIT NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# customer_price
# -----------------------------------------------------------------------
drop table if exists customer_price;

CREATE TABLE customer_price
(
    plan_item_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    PRIMARY KEY(plan_item_id,user_id));


# -----------------------------------------------------------------------
# rate_card
# -----------------------------------------------------------------------
drop table if exists rate_card;

CREATE TABLE rate_card
(
    id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    table_name VARCHAR(255) NOT NULL,
    entity_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    UNIQUE (table_name));


# -----------------------------------------------------------------------
# jbilling_table
# -----------------------------------------------------------------------
drop table if exists jbilling_table;

CREATE TABLE jbilling_table
(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# jbilling_seqs
# -----------------------------------------------------------------------
drop table if exists jbilling_seqs;

CREATE TABLE jbilling_seqs
(
    name VARCHAR(50) NOT NULL,
    next_id INTEGER default 0 NOT NULL);


# -----------------------------------------------------------------------
# language
# -----------------------------------------------------------------------
drop table if exists language;

CREATE TABLE language
(
    id INTEGER NOT NULL,
    code VARCHAR(2) NOT NULL,
    description VARCHAR(50) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_message
# -----------------------------------------------------------------------
drop table if exists notification_message;

CREATE TABLE notification_message
(
    id INTEGER NOT NULL,
    type_id INTEGER,
    entity_id INTEGER NOT NULL,
    language_id INTEGER NOT NULL,
    use_flag SMALLINT default 1 NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_message_arch
# -----------------------------------------------------------------------
drop table if exists notification_message_arch;

CREATE TABLE notification_message_arch
(
    id INTEGER NOT NULL,
    type_id INTEGER,
    create_datetime TIMESTAMP NOT NULL,
    user_id INTEGER,
    result_message VARCHAR(200),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_message_arch_line
# -----------------------------------------------------------------------
drop table if exists notification_message_arch_line;

CREATE TABLE notification_message_arch_line
(
    id INTEGER NOT NULL,
    message_archive_id INTEGER,
    section INTEGER NOT NULL,
    content VARCHAR(1000) NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_message_line
# -----------------------------------------------------------------------
drop table if exists notification_message_line;

CREATE TABLE notification_message_line
(
    id INTEGER NOT NULL,
    message_section_id INTEGER,
    content VARCHAR(1000) NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_message_section
# -----------------------------------------------------------------------
drop table if exists notification_message_section;

CREATE TABLE notification_message_section
(
    id INTEGER NOT NULL,
    message_id INTEGER,
    section INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_message_type
# -----------------------------------------------------------------------
drop table if exists notification_message_type;

CREATE TABLE notification_message_type
(
    id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# notification_category
# -----------------------------------------------------------------------
drop table if exists notification_category;

CREATE TABLE notification_category
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# order_billing_type
# -----------------------------------------------------------------------
drop table if exists order_billing_type;

CREATE TABLE order_billing_type
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# order_line
# -----------------------------------------------------------------------
drop table if exists order_line;

CREATE TABLE order_line
(
    id INTEGER NOT NULL,
    order_id INTEGER,
    item_id INTEGER,
    type_id INTEGER,
    amount DECIMAL(22,10) NOT NULL,
    quantity DECIMAL(22,10),
    price DECIMAL(22,10),
    item_price SMALLINT,
    create_datetime TIMESTAMP NOT NULL,
    deleted SMALLINT default 0 NOT NULL,
    use_item BIT NOT NULL,
    description VARCHAR(1000),
    provisioning_status INTEGER,
    provisioning_request_id VARCHAR(50),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_order_line_order_id (order_id),
    INDEX ix_order_line_item_id (item_id));


# -----------------------------------------------------------------------
# order_line_type
# -----------------------------------------------------------------------
drop table if exists order_line_type;

CREATE TABLE order_line_type
(
    id INTEGER NOT NULL,
    editable SMALLINT NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# order_period
# -----------------------------------------------------------------------
drop table if exists order_period;

CREATE TABLE order_period
(
    id INTEGER NOT NULL,
    entity_id INTEGER,
    value INTEGER,
    unit_id INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# order_process
# -----------------------------------------------------------------------
drop table if exists order_process;

CREATE TABLE order_process
(
    id INTEGER NOT NULL,
    order_id INTEGER,
    invoice_id INTEGER,
    billing_process_id INTEGER,
    periods_included INTEGER,
    period_start DATETIME,
    period_end DATETIME,
    is_review INTEGER NOT NULL,
    origin INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_uq_order_process_or_in (order_id, invoice_id),
    INDEX ix_uq_order_process_or_bp (order_id, billing_process_id),
    INDEX ix_order_process_in (invoice_id));


# -----------------------------------------------------------------------
# paper_invoice_batch
# -----------------------------------------------------------------------
drop table if exists paper_invoice_batch;

CREATE TABLE paper_invoice_batch
(
    id INTEGER NOT NULL,
    total_invoices INTEGER NOT NULL,
    delivery_date DATETIME,
    is_self_managed SMALLINT NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# partner
# -----------------------------------------------------------------------
drop table if exists partner;

CREATE TABLE partner
(
    id INTEGER NOT NULL,
    user_id INTEGER,
    balance DECIMAL(22,10) NOT NULL,
    total_payments DECIMAL(22,10) NOT NULL,
    total_refunds DECIMAL(22,10) NOT NULL,
    total_payouts DECIMAL(22,10) NOT NULL,
    percentage_rate DECIMAL(22,10),
    referral_fee DECIMAL(22,10),
    fee_currency_id INTEGER,
    one_time SMALLINT NOT NULL,
    period_unit_id INTEGER NOT NULL,
    period_value INTEGER NOT NULL,
    next_payout_date DATETIME NOT NULL,
    due_payout DECIMAL(22,10),
    automatic_process SMALLINT NOT NULL,
    related_clerk INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX partner_i_3 (user_id));


# -----------------------------------------------------------------------
# partner_payout
# -----------------------------------------------------------------------
drop table if exists partner_payout;

CREATE TABLE partner_payout
(
    id INTEGER NOT NULL,
    starting_date DATETIME NOT NULL,
    ending_date DATETIME NOT NULL,
    payments_amount DECIMAL(22,10) NOT NULL,
    refunds_amount DECIMAL(22,10) NOT NULL,
    balance_left DECIMAL(22,10) NOT NULL,
    payment_id INTEGER,
    partner_id INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX partner_payout_i_2 (partner_id));


# -----------------------------------------------------------------------
# partner_range
# -----------------------------------------------------------------------
drop table if exists partner_range;

CREATE TABLE partner_range
(
    id INTEGER NOT NULL,
    partner_id INTEGER,
    percentage_rate DECIMAL(22,10),
    referral_fee DECIMAL(22,10),
    range_from INTEGER NOT NULL,
    range_to INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX partner_range_p (partner_id));


# -----------------------------------------------------------------------
# payment
# -----------------------------------------------------------------------
drop table if exists payment;

CREATE TABLE payment
(
    id INTEGER NOT NULL,
    user_id INTEGER,
    attempt INTEGER,
    result_id INTEGER,
    amount DECIMAL(22,10) NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    update_datetime TIMESTAMP,
    payment_date DATETIME,
    method_id INTEGER NOT NULL,
    credit_card_id INTEGER,
    deleted SMALLINT default 0 NOT NULL,
    is_refund SMALLINT default 0 NOT NULL,
    is_preauth SMALLINT default 0 NOT NULL,
    payment_id INTEGER,
    currency_id INTEGER NOT NULL,
    payout_id INTEGER,
    ach_id INTEGER,
    balance DECIMAL(22,10),
    payment_period INTEGER,
    payment_notes VARCHAR(500),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX payment_i_2 (user_id, create_datetime),
    INDEX payment_i_3 (user_id, balance));


# -----------------------------------------------------------------------
# payment_authorization
# -----------------------------------------------------------------------
drop table if exists payment_authorization;

CREATE TABLE payment_authorization
(
    id INTEGER NOT NULL,
    payment_id INTEGER,
    processor VARCHAR(40) NOT NULL,
    code1 VARCHAR(40) NOT NULL,
    code2 VARCHAR(40),
    code3 VARCHAR(40),
    approval_code VARCHAR(20),
    avs VARCHAR(20),
    transaction_id VARCHAR(40),
    md5 VARCHAR(100),
    card_code VARCHAR(100),
    create_datetime DATETIME NOT NULL,
    response_message VARCHAR(200),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX create_datetime (create_datetime),
    INDEX transaction_id (transaction_id),
    INDEX ix_pa_payment (payment_id));


# -----------------------------------------------------------------------
# payment_info_cheque
# -----------------------------------------------------------------------
drop table if exists payment_info_cheque;

CREATE TABLE payment_info_cheque
(
    id INTEGER NOT NULL,
    payment_id INTEGER,
    bank VARCHAR(50),
    cheque_number VARCHAR(50),
    cheque_date DATETIME,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# payment_invoice
# -----------------------------------------------------------------------
drop table if exists payment_invoice;

CREATE TABLE payment_invoice
(
    id INTEGER NOT NULL,
    payment_id INTEGER,
    invoice_id INTEGER,
    amount DECIMAL(22,10),
    create_datetime TIMESTAMP NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_uq_payment_inv_map_pa_in (payment_id, invoice_id));


# -----------------------------------------------------------------------
# payment_method
# -----------------------------------------------------------------------
drop table if exists payment_method;

CREATE TABLE payment_method
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# payment_result
# -----------------------------------------------------------------------
drop table if exists payment_result;

CREATE TABLE payment_result
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# period_unit
# -----------------------------------------------------------------------
drop table if exists period_unit;

CREATE TABLE period_unit
(
    id INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# enumeration
# -----------------------------------------------------------------------
drop table if exists enumeration;

CREATE TABLE enumeration
(
    id INTEGER NOT NULL,
    name VARCHAR(50),
    value VARCHAR(50),
    entity_id INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# enumeration_values
# -----------------------------------------------------------------------
drop table if exists enumeration_values;

CREATE TABLE enumeration_values
(
    id INTEGER NOT NULL,
    enumeration_id INTEGER,
    value VARCHAR(50),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# permission
# -----------------------------------------------------------------------
drop table if exists permission;

CREATE TABLE permission
(
    id INTEGER NOT NULL,
    type_id INTEGER NOT NULL,
    foreign_id INTEGER,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# permission_role_map
# -----------------------------------------------------------------------
drop table if exists permission_role_map;

CREATE TABLE permission_role_map
(
    permission_id INTEGER,
    role_id INTEGER,
    INDEX permission_role_map_i_2 (permission_id, role_id));


# -----------------------------------------------------------------------
# permission_type
# -----------------------------------------------------------------------
drop table if exists permission_type;

CREATE TABLE permission_type
(
    id INTEGER NOT NULL,
    description VARCHAR(30) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# permission_user
# -----------------------------------------------------------------------
drop table if exists permission_user;

CREATE TABLE permission_user
(
    permission_id INTEGER,
    user_id INTEGER,
    is_grant SMALLINT NOT NULL,
    id INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX permission_user_map_i_2 (permission_id, user_id));


# -----------------------------------------------------------------------
# pluggable_task
# -----------------------------------------------------------------------
drop table if exists pluggable_task;

CREATE TABLE pluggable_task
(
    id INTEGER NOT NULL,
    entity_id INTEGER NOT NULL,
    type_id INTEGER,
    processing_order INTEGER NOT NULL,
    notes VARCHAR(1000),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# pluggable_task_parameter
# -----------------------------------------------------------------------
drop table if exists pluggable_task_parameter;

CREATE TABLE pluggable_task_parameter
(
    id INTEGER NOT NULL,
    task_id INTEGER,
    name VARCHAR(50) NOT NULL,
    int_value INTEGER,
    str_value VARCHAR(500),
    float_value DECIMAL(22,10),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# pluggable_task_type
# -----------------------------------------------------------------------
drop table if exists pluggable_task_type;

CREATE TABLE pluggable_task_type
(
    id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    class_name VARCHAR(200) NOT NULL,
    min_parameters INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# pluggable_task_type_category
# -----------------------------------------------------------------------
drop table if exists pluggable_task_type_category;

CREATE TABLE pluggable_task_type_category
(
    id INTEGER NOT NULL,
    interface_name VARCHAR(200) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# preference
# -----------------------------------------------------------------------
drop table if exists preference;

CREATE TABLE preference
(
    id INTEGER NOT NULL,
    type_id INTEGER,
    table_id INTEGER NOT NULL,
    foreign_id INTEGER NOT NULL,
    value VARCHAR(200),
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# preference_type
# -----------------------------------------------------------------------
drop table if exists preference_type;

CREATE TABLE preference_type
(
    id INTEGER NOT NULL,
    def_value VARCHAR(200),
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# promotion
# -----------------------------------------------------------------------
drop table if exists promotion;

CREATE TABLE promotion
(
    id INTEGER NOT NULL,
    item_id INTEGER,
    code VARCHAR(50) NOT NULL,
    notes VARCHAR(200),
    once SMALLINT NOT NULL,
    since DATETIME,
    until DATETIME,
    PRIMARY KEY(id),
    INDEX ix_promotion_code (code));


# -----------------------------------------------------------------------
# purchase_order
# -----------------------------------------------------------------------
drop table if exists purchase_order;

CREATE TABLE purchase_order
(
    id INTEGER NOT NULL,
    user_id INTEGER,
    period_id INTEGER,
    billing_type_id INTEGER NOT NULL,
    active_since DATETIME,
    active_until DATETIME,
    cycle_start DATETIME,
    create_datetime TIMESTAMP NOT NULL,
    next_billable_day DATETIME,
    created_by INTEGER,
    status_id INTEGER NOT NULL,
    currency_id INTEGER NOT NULL,
    deleted SMALLINT default 0 NOT NULL,
    notify SMALLINT,
    last_notified TIMESTAMP,
    notification_step INTEGER,
    due_date_unit_id INTEGER,
    due_date_value INTEGER,
    df_fm SMALLINT,
    anticipate_periods INTEGER,
    own_invoice SMALLINT,
    notes VARCHAR(200),
    notes_in_invoice SMALLINT,
    is_current SMALLINT,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX purchase_order_i_user (user_id, deleted),
    INDEX purchase_order_i_notif (active_until, notification_step),
    INDEX ix_purchase_order_date (user_id, create_datetime));


# -----------------------------------------------------------------------
# recent_item
# -----------------------------------------------------------------------
drop table if exists recent_item;

CREATE TABLE recent_item
(
    id INTEGER NOT NULL,
    type VARCHAR(255) NOT NULL,
    object_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    version INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# report
# -----------------------------------------------------------------------
drop table if exists report;

CREATE TABLE report
(
    id INTEGER NOT NULL,
    type_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# report_type
# -----------------------------------------------------------------------
drop table if exists report_type;

CREATE TABLE report_type
(
    id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# report_parameter
# -----------------------------------------------------------------------
drop table if exists report_parameter;

CREATE TABLE report_parameter
(
    id INTEGER NOT NULL,
    report_id INTEGER NOT NULL,
    dtype VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# entity_report_map
# -----------------------------------------------------------------------
drop table if exists entity_report_map;

CREATE TABLE entity_report_map
(
    report_id INTEGER NOT NULL,
    entity_id INTEGER NOT NULL,
    PRIMARY KEY(report_id,entity_id));


# -----------------------------------------------------------------------
# role
# -----------------------------------------------------------------------
drop table if exists role;

CREATE TABLE role
(
    id INTEGER NOT NULL,
    role_type_id INTEGER,
    entity_id INTEGER,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# user_credit_card_map
# -----------------------------------------------------------------------
drop table if exists user_credit_card_map;

CREATE TABLE user_credit_card_map
(
    user_id INTEGER,
    credit_card_id INTEGER,
    INDEX user_credit_card_map_i_2 (user_id, credit_card_id));


# -----------------------------------------------------------------------
# user_role_map
# -----------------------------------------------------------------------
drop table if exists user_role_map;

CREATE TABLE user_role_map
(
    user_id INTEGER,
    role_id INTEGER,
    INDEX user_role_map_i_2 (user_id, role_id),
    INDEX user_role_map_i_role (role_id));


# -----------------------------------------------------------------------
# mediation_cfg
# -----------------------------------------------------------------------
drop table if exists mediation_cfg;

CREATE TABLE mediation_cfg
(
    id INTEGER NOT NULL,
    entity_id INTEGER NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    name VARCHAR(50) NOT NULL,
    order_value INTEGER NOT NULL,
    pluggable_task_id INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# mediation_process
# -----------------------------------------------------------------------
drop table if exists mediation_process;

CREATE TABLE mediation_process
(
    id INTEGER NOT NULL,
    configuration_id INTEGER NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP,
    orders_affected INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# mediation_order_map
# -----------------------------------------------------------------------
drop table if exists mediation_order_map;

CREATE TABLE mediation_order_map
(
    mediation_process_id INTEGER NOT NULL,
    order_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# mediation_record
# -----------------------------------------------------------------------
drop table if exists mediation_record;

CREATE TABLE mediation_record
(
    id INTEGER NOT NULL,
    id_key VARCHAR(100) NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    mediation_process_id INTEGER,
    status_id INTEGER NOT NULL,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX mediation_record_i (id, status_id));


# -----------------------------------------------------------------------
# mediation_record_line
# -----------------------------------------------------------------------
drop table if exists mediation_record_line;

CREATE TABLE mediation_record_line
(
    id INTEGER NOT NULL,
    mediation_record_id INTEGER NOT NULL,
    order_line_id INTEGER NOT NULL,
    event_date TIMESTAMP NOT NULL,
    amount DECIMAL(22,10) NOT NULL,
    quantity DECIMAL(22,10) NOT NULL,
    description VARCHAR(200),
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_mrl_order_line (order_line_id));


# -----------------------------------------------------------------------
# blacklist
# -----------------------------------------------------------------------
drop table if exists blacklist;

CREATE TABLE blacklist
(
    id INTEGER NOT NULL,
    entity_id INTEGER NOT NULL,
    create_datetime TIMESTAMP NOT NULL,
    type INTEGER NOT NULL,
    source INTEGER NOT NULL,
    credit_card INTEGER,
    credit_card_id INTEGER,
    contact_id INTEGER,
    user_id INTEGER,
    meta_field_value_id INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id),
    INDEX ix_blacklist_user_type (user_id, type),
    INDEX ix_blacklist_entity_type (entity_id, type));


# -----------------------------------------------------------------------
# generic_status_type
# -----------------------------------------------------------------------
drop table if exists generic_status_type;

CREATE TABLE generic_status_type
(
    id VARCHAR(40) NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# generic_status
# -----------------------------------------------------------------------
drop table if exists generic_status;

CREATE TABLE generic_status
(
    id INTEGER NOT NULL,
    dtype VARCHAR(40) NOT NULL,
    status_value INTEGER NOT NULL,
    can_login SMALLINT,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# shortcut
# -----------------------------------------------------------------------
drop table if exists shortcut;

CREATE TABLE shortcut
(
    id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    controller VARCHAR(255) NOT NULL,
    action VARCHAR(255),
    name VARCHAR(255),
    object_id INTEGER,
    version INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# meta_field_name
# -----------------------------------------------------------------------
drop table if exists meta_field_name;

CREATE TABLE meta_field_name
(
    id INTEGER NOT NULL,
    name VARCHAR(100),
    entity_id INTEGER,
    entity_type VARCHAR(25) NOT NULL,
    data_type VARCHAR(25) NOT NULL,
    is_disabled BIT,
    is_mandatory BIT,
    display_order INTEGER,
    default_value_id INTEGER,
    OPTLOCK INTEGER NOT NULL,
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# meta_field_value
# -----------------------------------------------------------------------
drop table if exists meta_field_value;

CREATE TABLE meta_field_value
(
    id INTEGER NOT NULL,
    meta_field_name_id INTEGER NOT NULL,
    dtype VARCHAR(10) NOT NULL,
    boolean_value BIT,
    date_value TIMESTAMP,
    decimal_value DECIMAL(22,10),
    integer_value INTEGER,
    string_value VARCHAR(1000),
    PRIMARY KEY(id));


# -----------------------------------------------------------------------
# customer_meta_field_map
# -----------------------------------------------------------------------
drop table if exists customer_meta_field_map;

CREATE TABLE customer_meta_field_map
(
    customer_id INTEGER NOT NULL,
    meta_field_value_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# partner_meta_field_map
# -----------------------------------------------------------------------
drop table if exists partner_meta_field_map;

CREATE TABLE partner_meta_field_map
(
    partner_id INTEGER NOT NULL,
    meta_field_value_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# order_meta_field_map
# -----------------------------------------------------------------------
drop table if exists order_meta_field_map;

CREATE TABLE order_meta_field_map
(
    order_id INTEGER NOT NULL,
    meta_field_value_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# item_meta_field_map
# -----------------------------------------------------------------------
drop table if exists item_meta_field_map;

CREATE TABLE item_meta_field_map
(
    item_id INTEGER NOT NULL,
    meta_field_value_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# invoice_meta_field_map
# -----------------------------------------------------------------------
drop table if exists invoice_meta_field_map;

CREATE TABLE invoice_meta_field_map
(
    invoice_id INTEGER NOT NULL,
    meta_field_value_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# payment_meta_field_map
# -----------------------------------------------------------------------
drop table if exists payment_meta_field_map;

CREATE TABLE payment_meta_field_map
(
    payment_id INTEGER NOT NULL,
    meta_field_value_id INTEGER NOT NULL);


# -----------------------------------------------------------------------
# list_meta_field_values
# -----------------------------------------------------------------------
drop table if exists list_meta_field_values;

CREATE TABLE list_meta_field_values
(
    meta_field_value_id INTEGER NOT NULL,
    list_value VARCHAR(255));

ALTER TABLE ach
    ADD CONSTRAINT ach_FK_1
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_FK_1
    FOREIGN KEY (status_id)
    REFERENCES generic_status (id)
;

ALTER TABLE ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_1
    FOREIGN KEY (status_id)
    REFERENCES generic_status (id)
;

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_2
    FOREIGN KEY (subscriber_status)
    REFERENCES generic_status (id)
;

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_3
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_4
    FOREIGN KEY (language_id)
    REFERENCES language (id)
;

ALTER TABLE base_user
    ADD CONSTRAINT base_user_FK_5
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE billing_process
    ADD CONSTRAINT billing_process_FK_1
    FOREIGN KEY (period_unit_id)
    REFERENCES period_unit (id)
;

ALTER TABLE billing_process
    ADD CONSTRAINT billing_process_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE billing_process
    ADD CONSTRAINT billing_process_FK_3
    FOREIGN KEY (paper_invoice_batch_id)
    REFERENCES paper_invoice_batch (id)
;

ALTER TABLE billing_process_configuration
    ADD CONSTRAINT billing_process_configuration_FK_1
    FOREIGN KEY (period_unit_id)
    REFERENCES period_unit (id)
;

ALTER TABLE billing_process_configuration
    ADD CONSTRAINT billing_process_configuration_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE process_run
    ADD CONSTRAINT process_run_FK_1
    FOREIGN KEY (process_id)
    REFERENCES billing_process (id)
;

ALTER TABLE process_run
    ADD CONSTRAINT process_run_FK_2
    FOREIGN KEY (status_id)
    REFERENCES generic_status (id)
;

ALTER TABLE process_run_total
    ADD CONSTRAINT process_run_total_FK_1
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE process_run_total
    ADD CONSTRAINT process_run_total_FK_2
    FOREIGN KEY (process_run_id)
    REFERENCES process_run (id)
;

ALTER TABLE process_run_total_pm
    ADD CONSTRAINT process_run_total_pm_FK_1
    FOREIGN KEY (payment_method_id)
    REFERENCES payment_method (id)
;

ALTER TABLE process_run_user
    ADD CONSTRAINT process_run_user_FK_1
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE process_run_user
    ADD CONSTRAINT process_run_user_FK_2
    FOREIGN KEY (process_run_id)
    REFERENCES process_run (id)
;

ALTER TABLE contact_map
    ADD CONSTRAINT contact_map_FK_1
    FOREIGN KEY (table_id)
    REFERENCES jbilling_table (id)
;

ALTER TABLE contact_map
    ADD CONSTRAINT contact_map_FK_2
    FOREIGN KEY (type_id)
    REFERENCES contact_type (id)
;

ALTER TABLE contact_map
    ADD CONSTRAINT contact_map_FK_3
    FOREIGN KEY (contact_id)
    REFERENCES contact (id)
;

ALTER TABLE contact_type
    ADD CONSTRAINT contact_type_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE currency_entity_map
    ADD CONSTRAINT currency_entity_map_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE currency_entity_map
    ADD CONSTRAINT currency_entity_map_FK_2
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE currency_exchange
    ADD CONSTRAINT currency_exchange_FK_1
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE customer
    ADD CONSTRAINT customer_FK_1
    FOREIGN KEY (invoice_delivery_method_id)
    REFERENCES invoice_delivery_method (id)
;

ALTER TABLE customer
    ADD CONSTRAINT customer_FK_2
    FOREIGN KEY (partner_id)
    REFERENCES partner (id)
;

ALTER TABLE customer
    ADD CONSTRAINT customer_FK_3
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE entity
    ADD CONSTRAINT entity_FK_1
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE entity
    ADD CONSTRAINT entity_FK_2
    FOREIGN KEY (language_id)
    REFERENCES language (id)
;

ALTER TABLE entity_delivery_method_map
    ADD CONSTRAINT entity_delivery_method_map_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE entity_delivery_method_map
    ADD CONSTRAINT entity_delivery_method_map_FK_2
    FOREIGN KEY (method_id)
    REFERENCES invoice_delivery_method (id)
;

ALTER TABLE entity_payment_method_map
    ADD CONSTRAINT entity_payment_method_map_FK_1
    FOREIGN KEY (payment_method_id)
    REFERENCES payment_method (id)
;

ALTER TABLE entity_payment_method_map
    ADD CONSTRAINT entity_payment_method_map_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE event_log
    ADD CONSTRAINT event_log_FK_1
    FOREIGN KEY (module_id)
    REFERENCES event_log_module (id)
;

ALTER TABLE event_log
    ADD CONSTRAINT event_log_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE event_log
    ADD CONSTRAINT event_log_FK_3
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE event_log
    ADD CONSTRAINT event_log_FK_4
    FOREIGN KEY (affected_user_id)
    REFERENCES base_user (id)
;

ALTER TABLE event_log
    ADD CONSTRAINT event_log_FK_5
    FOREIGN KEY (table_id)
    REFERENCES jbilling_table (id)
;

ALTER TABLE event_log
    ADD CONSTRAINT event_log_FK_6
    FOREIGN KEY (message_id)
    REFERENCES event_log_message (id)
;

ALTER TABLE filter
    ADD CONSTRAINT filter_FK_1
    FOREIGN KEY (filter_set_id)
    REFERENCES filter_set (id)
;

ALTER TABLE international_description
    ADD CONSTRAINT international_description_FK_1
    FOREIGN KEY (language_id)
    REFERENCES language (id)
;

ALTER TABLE international_description
    ADD CONSTRAINT international_description_FK_2
    FOREIGN KEY (table_id)
    REFERENCES jbilling_table (id)
;

ALTER TABLE invoice
    ADD CONSTRAINT invoice_FK_1
    FOREIGN KEY (billing_process_id)
    REFERENCES billing_process (id)
;

ALTER TABLE invoice
    ADD CONSTRAINT invoice_FK_2
    FOREIGN KEY (paper_invoice_batch_id)
    REFERENCES paper_invoice_batch (id)
;

ALTER TABLE invoice
    ADD CONSTRAINT invoice_FK_3
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE invoice
    ADD CONSTRAINT invoice_FK_4
    FOREIGN KEY (delegated_invoice_id)
    REFERENCES invoice (id)
;

ALTER TABLE invoice
    ADD CONSTRAINT invoice_FK_5
    FOREIGN KEY (status_id)
    REFERENCES generic_status (id)
;

ALTER TABLE invoice_line
    ADD CONSTRAINT invoice_line_FK_1
    FOREIGN KEY (invoice_id)
    REFERENCES invoice (id)
;

ALTER TABLE invoice_line
    ADD CONSTRAINT invoice_line_FK_2
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE invoice_line
    ADD CONSTRAINT invoice_line_FK_3
    FOREIGN KEY (type_id)
    REFERENCES invoice_line_type (id)
;

ALTER TABLE item
    ADD CONSTRAINT item_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE item_price_timeline
    ADD CONSTRAINT item_price_timeline_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE item_price_timeline
    ADD CONSTRAINT item_price_timeline_FK_2
    FOREIGN KEY (price_model_id)
    REFERENCES price_model (id)
;

ALTER TABLE item_type
    ADD CONSTRAINT item_type_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE item_type_map
    ADD CONSTRAINT item_type_map_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE item_type_map
    ADD CONSTRAINT item_type_map_FK_2
    FOREIGN KEY (type_id)
    REFERENCES item_type (id)
;

ALTER TABLE item_type_exclude_map
    ADD CONSTRAINT item_type_exclude_map_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE item_type_exclude_map
    ADD CONSTRAINT item_type_exclude_map_FK_2
    FOREIGN KEY (type_id)
    REFERENCES item_type (id)
;

ALTER TABLE price_model
    ADD CONSTRAINT price_model_FK_1
    FOREIGN KEY (next_model_id)
    REFERENCES price_model (id)
;

ALTER TABLE price_model_attribute
    ADD CONSTRAINT price_model_attribute_FK_1
    FOREIGN KEY (price_model_id)
    REFERENCES price_model (id)
;

ALTER TABLE plan
    ADD CONSTRAINT plan_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE plan
    ADD CONSTRAINT plan_FK_2
    FOREIGN KEY (period_id)
    REFERENCES order_period (id)
;

ALTER TABLE plan_item
    ADD CONSTRAINT plan_item_FK_1
    FOREIGN KEY (plan_id)
    REFERENCES plan (id)
;

ALTER TABLE plan_item
    ADD CONSTRAINT plan_item_FK_2
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE plan_item
    ADD CONSTRAINT plan_item_FK_3
    FOREIGN KEY (plan_item_bundle_id)
    REFERENCES plan_item_bundle (id)
;

ALTER TABLE plan_item_price_timeline
    ADD CONSTRAINT plan_item_price_timeline_FK_1
    FOREIGN KEY (plan_item_id)
    REFERENCES plan_item (id)
;

ALTER TABLE plan_item_price_timeline
    ADD CONSTRAINT plan_item_price_timeline_FK_2
    FOREIGN KEY (price_model_id)
    REFERENCES price_model (id)
;

ALTER TABLE plan_item_bundle
    ADD CONSTRAINT plan_item_bundle_FK_1
    FOREIGN KEY (period_id)
    REFERENCES order_period (id)
;

ALTER TABLE customer_price
    ADD CONSTRAINT customer_price_FK_1
    FOREIGN KEY (plan_item_id)
    REFERENCES plan_item (id)
;

ALTER TABLE customer_price
    ADD CONSTRAINT customer_price_FK_2
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE rate_card
    ADD CONSTRAINT rate_card_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE notification_message
    ADD CONSTRAINT notification_message_FK_1
    FOREIGN KEY (language_id)
    REFERENCES language (id)
;

ALTER TABLE notification_message
    ADD CONSTRAINT notification_message_FK_2
    FOREIGN KEY (type_id)
    REFERENCES notification_message_type (id)
;

ALTER TABLE notification_message
    ADD CONSTRAINT notification_message_FK_3
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE notification_message_arch_line
    ADD CONSTRAINT notif_mess_arch_line_FK_1
    FOREIGN KEY (message_archive_id)
    REFERENCES notification_message_arch (id)
;

ALTER TABLE notification_message_line
    ADD CONSTRAINT notification_message_line_FK_1
    FOREIGN KEY (message_section_id)
    REFERENCES notification_message_section (id)
;

ALTER TABLE notification_message_section
    ADD CONSTRAINT notification_message_section_FK_1
    FOREIGN KEY (message_id)
    REFERENCES notification_message (id)
;

ALTER TABLE notification_message_type
    ADD CONSTRAINT notification_message_type_FK_1
    FOREIGN KEY (category_id)
    REFERENCES notification_category (id)
;

ALTER TABLE order_line
    ADD CONSTRAINT order_line_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE order_line
    ADD CONSTRAINT order_line_FK_2
    FOREIGN KEY (order_id)
    REFERENCES purchase_order (id)
;

ALTER TABLE order_line
    ADD CONSTRAINT order_line_FK_3
    FOREIGN KEY (type_id)
    REFERENCES order_line_type (id)
;

ALTER TABLE order_period
    ADD CONSTRAINT order_period_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE order_period
    ADD CONSTRAINT order_period_FK_2
    FOREIGN KEY (unit_id)
    REFERENCES period_unit (id)
;

ALTER TABLE order_process
    ADD CONSTRAINT order_process_FK_1
    FOREIGN KEY (order_id)
    REFERENCES purchase_order (id)
;

ALTER TABLE partner
    ADD CONSTRAINT partner_FK_1
    FOREIGN KEY (period_unit_id)
    REFERENCES period_unit (id)
;

ALTER TABLE partner
    ADD CONSTRAINT partner_FK_2
    FOREIGN KEY (fee_currency_id)
    REFERENCES currency (id)
;

ALTER TABLE partner
    ADD CONSTRAINT partner_FK_3
    FOREIGN KEY (related_clerk)
    REFERENCES base_user (id)
;

ALTER TABLE partner
    ADD CONSTRAINT partner_FK_4
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE partner_payout
    ADD CONSTRAINT partner_payout_FK_1
    FOREIGN KEY (partner_id)
    REFERENCES partner (id)
;

ALTER TABLE payment
    ADD CONSTRAINT payment_FK_1
    FOREIGN KEY (ach_id)
    REFERENCES ach (id)
;

ALTER TABLE payment
    ADD CONSTRAINT payment_FK_2
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE payment
    ADD CONSTRAINT payment_FK_3
    FOREIGN KEY (payment_id)
    REFERENCES payment (id)
;

ALTER TABLE payment
    ADD CONSTRAINT payment_FK_4
    FOREIGN KEY (credit_card_id)
    REFERENCES credit_card (id)
;

ALTER TABLE payment
    ADD CONSTRAINT payment_FK_5
    FOREIGN KEY (result_id)
    REFERENCES payment_result (id)
;

ALTER TABLE payment
    ADD CONSTRAINT payment_FK_6
    FOREIGN KEY (method_id)
    REFERENCES payment_method (id)
;

ALTER TABLE payment_authorization
    ADD CONSTRAINT payment_authorization_FK_1
    FOREIGN KEY (payment_id)
    REFERENCES payment (id)
;

ALTER TABLE payment_info_cheque
    ADD CONSTRAINT payment_info_cheque_FK_1
    FOREIGN KEY (payment_id)
    REFERENCES payment (id)
;

ALTER TABLE payment_invoice
    ADD CONSTRAINT payment_invoice_FK_1
    FOREIGN KEY (invoice_id)
    REFERENCES invoice (id)
;

ALTER TABLE payment_invoice
    ADD CONSTRAINT payment_invoice_FK_2
    FOREIGN KEY (payment_id)
    REFERENCES payment (id)
;

ALTER TABLE enumeration
    ADD CONSTRAINT enumeration_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE enumeration_values
    ADD CONSTRAINT enumeration_values_FK_1
    FOREIGN KEY (enumeration_id)
    REFERENCES enumeration (id)
;

ALTER TABLE permission
    ADD CONSTRAINT permission_FK_1
    FOREIGN KEY (type_id)
    REFERENCES permission_type (id)
;

ALTER TABLE permission_role_map
    ADD CONSTRAINT permission_role_map_FK_1
    FOREIGN KEY (role_id)
    REFERENCES role (id)
;

ALTER TABLE permission_role_map
    ADD CONSTRAINT permission_role_map_FK_2
    FOREIGN KEY (permission_id)
    REFERENCES permission (id)
;

ALTER TABLE permission_user
    ADD CONSTRAINT permission_user_FK_1
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE permission_user
    ADD CONSTRAINT permission_user_FK_2
    FOREIGN KEY (permission_id)
    REFERENCES permission (id)
;

ALTER TABLE pluggable_task
    ADD CONSTRAINT pluggable_task_FK_1
    FOREIGN KEY (type_id)
    REFERENCES pluggable_task_type (id)
;

ALTER TABLE pluggable_task
    ADD CONSTRAINT pluggable_task_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE pluggable_task_parameter
    ADD CONSTRAINT pluggable_task_parameter_FK_1
    FOREIGN KEY (task_id)
    REFERENCES pluggable_task (id)
;

ALTER TABLE pluggable_task_type
    ADD CONSTRAINT pluggable_task_type_FK_1
    FOREIGN KEY (category_id)
    REFERENCES pluggable_task_type_category (id)
;

ALTER TABLE preference
    ADD CONSTRAINT preference_FK_1
    FOREIGN KEY (type_id)
    REFERENCES preference_type (id)
;

ALTER TABLE preference
    ADD CONSTRAINT preference_FK_2
    FOREIGN KEY (table_id)
    REFERENCES jbilling_table (id)
;

ALTER TABLE promotion
    ADD CONSTRAINT promotion_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_1
    FOREIGN KEY (currency_id)
    REFERENCES currency (id)
;

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_2
    FOREIGN KEY (billing_type_id)
    REFERENCES order_billing_type (id)
;

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_3
    FOREIGN KEY (period_id)
    REFERENCES order_period (id)
;

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_4
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_5
    FOREIGN KEY (created_by)
    REFERENCES base_user (id)
;

ALTER TABLE purchase_order
    ADD CONSTRAINT purchase_order_FK_6
    FOREIGN KEY (status_id)
    REFERENCES generic_status (id)
;

ALTER TABLE report
    ADD CONSTRAINT report_FK_1
    FOREIGN KEY (type_id)
    REFERENCES report_type (id)
;

ALTER TABLE report_parameter
    ADD CONSTRAINT report_parameter_FK_1
    FOREIGN KEY (report_id)
    REFERENCES report (id)
;

ALTER TABLE entity_report_map
    ADD CONSTRAINT entity_report_map_FK_1
    FOREIGN KEY (report_id)
    REFERENCES report (id)
;

ALTER TABLE entity_report_map
    ADD CONSTRAINT entity_report_map_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE role
    ADD CONSTRAINT role_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE user_role_map
    ADD CONSTRAINT user_role_map_FK_1
    FOREIGN KEY (role_id)
    REFERENCES role (id)
;

ALTER TABLE user_role_map
    ADD CONSTRAINT user_role_map_FK_2
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE mediation_cfg
    ADD CONSTRAINT mediation_cfg_FK_1
    FOREIGN KEY (pluggable_task_id)
    REFERENCES pluggable_task (id)
;

ALTER TABLE mediation_process
    ADD CONSTRAINT mediation_process_FK_1
    FOREIGN KEY (configuration_id)
    REFERENCES mediation_cfg (id)
;

ALTER TABLE mediation_order_map
    ADD CONSTRAINT mediation_order_map_FK_1
    FOREIGN KEY (mediation_process_id)
    REFERENCES mediation_process (id)
;

ALTER TABLE mediation_order_map
    ADD CONSTRAINT mediation_order_map_FK_2
    FOREIGN KEY (order_id)
    REFERENCES purchase_order (id)
;

ALTER TABLE mediation_record
    ADD CONSTRAINT mediation_record_FK_1
    FOREIGN KEY (mediation_process_id)
    REFERENCES mediation_process (id)
;

ALTER TABLE mediation_record
    ADD CONSTRAINT mediation_record_FK_2
    FOREIGN KEY (status_id)
    REFERENCES generic_status (id)
;

ALTER TABLE mediation_record_line
    ADD CONSTRAINT mediation_record_line_FK_1
    FOREIGN KEY (mediation_record_id)
    REFERENCES mediation_record (id)
;

ALTER TABLE mediation_record_line
    ADD CONSTRAINT mediation_record_line_FK_2
    FOREIGN KEY (order_line_id)
    REFERENCES order_line (id)
;

ALTER TABLE blacklist
    ADD CONSTRAINT blacklist_FK_1
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE blacklist
    ADD CONSTRAINT blacklist_FK_2
    FOREIGN KEY (user_id)
    REFERENCES base_user (id)
;

ALTER TABLE blacklist
    ADD CONSTRAINT blacklist_FK_3
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE generic_status
    ADD CONSTRAINT generic_status_FK_1
    FOREIGN KEY (dtype)
    REFERENCES generic_status_type (id)
;

ALTER TABLE meta_field_name
    ADD CONSTRAINT meta_field_name_FK_1
    FOREIGN KEY (default_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE meta_field_name
    ADD CONSTRAINT meta_field_name_FK_2
    FOREIGN KEY (entity_id)
    REFERENCES entity (id)
;

ALTER TABLE meta_field_value
    ADD CONSTRAINT meta_field_value_FK_1
    FOREIGN KEY (meta_field_name_id)
    REFERENCES meta_field_name (id)
;

ALTER TABLE customer_meta_field_map
    ADD CONSTRAINT customer_meta_field_map_FK_1
    FOREIGN KEY (customer_id)
    REFERENCES customer (id)
;

ALTER TABLE customer_meta_field_map
    ADD CONSTRAINT customer_meta_field_map_FK_2
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE partner_meta_field_map
    ADD CONSTRAINT partner_meta_field_map_FK_1
    FOREIGN KEY (partner_id)
    REFERENCES partner (id)
;

ALTER TABLE partner_meta_field_map
    ADD CONSTRAINT partner_meta_field_map_FK_2
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE order_meta_field_map
    ADD CONSTRAINT order_meta_field_map_FK_1
    FOREIGN KEY (order_id)
    REFERENCES purchase_order (id)
;

ALTER TABLE order_meta_field_map
    ADD CONSTRAINT order_meta_field_map_FK_2
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE item_meta_field_map
    ADD CONSTRAINT item_meta_field_map_FK_1
    FOREIGN KEY (item_id)
    REFERENCES item (id)
;

ALTER TABLE item_meta_field_map
    ADD CONSTRAINT item_meta_field_map_FK_2
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE invoice_meta_field_map
    ADD CONSTRAINT invoice_meta_field_map_FK_1
    FOREIGN KEY (invoice_id)
    REFERENCES invoice (id)
;

ALTER TABLE invoice_meta_field_map
    ADD CONSTRAINT invoice_meta_field_map_FK_2
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE payment_meta_field_map
    ADD CONSTRAINT payment_meta_field_map_FK_1
    FOREIGN KEY (payment_id)
    REFERENCES payment (id)
;

ALTER TABLE payment_meta_field_map
    ADD CONSTRAINT payment_meta_field_map_FK_2
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

ALTER TABLE list_meta_field_values
    ADD CONSTRAINT list_meta_field_values_FK_1
    FOREIGN KEY (meta_field_value_id)
    REFERENCES meta_field_value (id)
;

