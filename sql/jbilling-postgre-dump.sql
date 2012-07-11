--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: ach; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE ach (
    id integer NOT NULL,
    user_id integer,
    aba_routing character varying(40) NOT NULL,
    bank_account character varying(60) NOT NULL,
    account_type integer NOT NULL,
    bank_name character varying(50) NOT NULL,
    account_name character varying(100) NOT NULL,
    optlock integer NOT NULL,
    gateway_key character varying(100)
);


ALTER TABLE public.ach OWNER TO jbilling;

--
-- Name: ageing_entity_step; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE ageing_entity_step (
    id integer NOT NULL,
    entity_id integer,
    status_id integer,
    days integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.ageing_entity_step OWNER TO jbilling;

--
-- Name: base_user; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE base_user (
    id integer NOT NULL,
    entity_id integer,
    password character varying(40),
    deleted integer DEFAULT 0 NOT NULL,
    language_id integer,
    status_id integer,
    subscriber_status integer DEFAULT 1,
    currency_id integer,
    create_datetime timestamp with time zone NOT NULL,
    last_status_change timestamp with time zone,
    last_login timestamp with time zone,
    user_name character varying(50),
    failed_attempts integer DEFAULT 0 NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.base_user OWNER TO jbilling;

--
-- Name: billing_process; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE billing_process (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    billing_date date NOT NULL,
    period_unit_id integer NOT NULL,
    period_value integer NOT NULL,
    is_review integer NOT NULL,
    paper_invoice_batch_id integer,
    retries_to_do integer DEFAULT 0 NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.billing_process OWNER TO jbilling;

--
-- Name: billing_process_configuration; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE billing_process_configuration (
    id integer NOT NULL,
    entity_id integer,
    next_run_date date NOT NULL,
    generate_report integer NOT NULL,
    retries integer,
    days_for_retry integer,
    days_for_report integer,
    review_status integer NOT NULL,
    period_unit_id integer NOT NULL,
    period_value integer NOT NULL,
    due_date_unit_id integer NOT NULL,
    due_date_value integer NOT NULL,
    df_fm integer,
    only_recurring integer DEFAULT 1 NOT NULL,
    invoice_date_process integer NOT NULL,
    optlock integer NOT NULL,
    auto_payment integer DEFAULT 0 NOT NULL,
    maximum_periods integer DEFAULT 1 NOT NULL,
    auto_payment_application integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.billing_process_configuration OWNER TO jbilling;

--
-- Name: blacklist; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE blacklist (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    type integer NOT NULL,
    source integer NOT NULL,
    credit_card integer,
    credit_card_id integer,
    contact_id integer,
    user_id integer,
    optlock integer NOT NULL,
    meta_field_value_id integer
);


ALTER TABLE public.blacklist OWNER TO jbilling;

--
-- Name: breadcrumb; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE breadcrumb (
    id integer NOT NULL,
    user_id integer NOT NULL,
    controller character varying(255) NOT NULL,
    action character varying(255),
    name character varying(255),
    object_id integer,
    version integer NOT NULL,
    description character varying(255)
);


ALTER TABLE public.breadcrumb OWNER TO jbilling;

--
-- Name: cdrentries; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE cdrentries (
    id integer NOT NULL,
    accountcode character varying(20),
    src character varying(20),
    dst character varying(20),
    dcontext character varying(20),
    clid character varying(20),
    channel character varying(20),
    dstchannel character varying(20),
    lastapp character varying(20),
    lastdatat character varying(20),
    start_time timestamp with time zone,
    answer timestamp with time zone,
    end_time timestamp with time zone,
    duration integer,
    billsec integer,
    disposition character varying(20),
    amaflags character varying(20),
    userfield character varying(100),
    ts timestamp with time zone
);


ALTER TABLE public.cdrentries OWNER TO jbilling;

--
-- Name: contact; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE contact (
    id integer NOT NULL,
    organization_name character varying(200),
    street_addres1 character varying(100),
    street_addres2 character varying(100),
    city character varying(50),
    state_province character varying(30),
    postal_code character varying(15),
    country_code character varying(2),
    last_name character varying(30),
    first_name character varying(30),
    person_initial character varying(5),
    person_title character varying(40),
    phone_country_code integer,
    phone_area_code integer,
    phone_phone_number character varying(20),
    fax_country_code integer,
    fax_area_code integer,
    fax_phone_number character varying(20),
    email character varying(200),
    create_datetime timestamp with time zone NOT NULL,
    deleted integer DEFAULT 0 NOT NULL,
    notification_include integer DEFAULT 1,
    user_id integer,
    optlock integer NOT NULL
);


ALTER TABLE public.contact OWNER TO jbilling;

--
-- Name: contact_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE contact_map (
    id integer NOT NULL,
    contact_id integer,
    type_id integer NOT NULL,
    table_id integer NOT NULL,
    foreign_id integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.contact_map OWNER TO jbilling;

--
-- Name: contact_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE contact_type (
    id integer NOT NULL,
    entity_id integer,
    is_primary integer,
    optlock integer NOT NULL
);


ALTER TABLE public.contact_type OWNER TO jbilling;

--
-- Name: country; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE country (
    id integer NOT NULL,
    code character varying(2) NOT NULL
);


ALTER TABLE public.country OWNER TO jbilling;

--
-- Name: credit_card; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE credit_card (
    id integer NOT NULL,
    cc_number character varying(100) NOT NULL,
    cc_number_plain character varying(20),
    cc_expiry date NOT NULL,
    name character varying(150),
    cc_type integer NOT NULL,
    deleted integer DEFAULT 0 NOT NULL,
    gateway_key character varying(100),
    optlock integer NOT NULL
);


ALTER TABLE public.credit_card OWNER TO jbilling;

--
-- Name: currency; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE currency (
    id integer NOT NULL,
    symbol character varying(10) NOT NULL,
    code character varying(3) NOT NULL,
    country_code character varying(2) NOT NULL,
    optlock integer
);


ALTER TABLE public.currency OWNER TO jbilling;

--
-- Name: currency_entity_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE currency_entity_map (
    currency_id integer,
    entity_id integer
);


ALTER TABLE public.currency_entity_map OWNER TO jbilling;

--
-- Name: currency_exchange; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE currency_exchange (
    id integer NOT NULL,
    entity_id integer,
    currency_id integer,
    rate numeric(22,10) NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    optlock integer NOT NULL,
    valid_since date DEFAULT '1970-01-01'::date NOT NULL
);


ALTER TABLE public.currency_exchange OWNER TO jbilling;

--
-- Name: customer; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE customer (
    id integer NOT NULL,
    user_id integer,
    partner_id integer,
    referral_fee_paid integer,
    invoice_delivery_method_id integer NOT NULL,
    notes character varying(1000),
    auto_payment_type integer,
    due_date_unit_id integer,
    due_date_value integer,
    df_fm integer,
    parent_id integer,
    is_parent integer,
    exclude_aging integer DEFAULT 0 NOT NULL,
    invoice_child integer,
    current_order_id integer,
    optlock integer NOT NULL,
    balance_type integer NOT NULL,
    dynamic_balance numeric(22,10),
    credit_limit numeric(22,10),
    auto_recharge numeric(22,10),
    use_parent_pricing boolean NOT NULL
);


ALTER TABLE public.customer OWNER TO jbilling;

--
-- Name: customer_meta_field_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE customer_meta_field_map (
    customer_id integer NOT NULL,
    meta_field_value_id integer NOT NULL
);


ALTER TABLE public.customer_meta_field_map OWNER TO jbilling;

--
-- Name: customer_price; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE customer_price (
    plan_item_id integer NOT NULL,
    user_id integer NOT NULL,
    create_datetime timestamp with time zone NOT NULL
);


ALTER TABLE public.customer_price OWNER TO jbilling;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE databasechangelog (
    id character varying(63) NOT NULL,
    author character varying(63) NOT NULL,
    filename character varying(200) NOT NULL,
    dateexecuted timestamp with time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20)
);


ALTER TABLE public.databasechangelog OWNER TO jbilling;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp with time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO jbilling;

--
-- Name: entity; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE entity (
    id integer NOT NULL,
    external_id character varying(20),
    description character varying(100) NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    language_id integer NOT NULL,
    currency_id integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.entity OWNER TO jbilling;

--
-- Name: entity_delivery_method_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE entity_delivery_method_map (
    method_id integer,
    entity_id integer
);


ALTER TABLE public.entity_delivery_method_map OWNER TO jbilling;

--
-- Name: entity_payment_method_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE entity_payment_method_map (
    entity_id integer,
    payment_method_id integer
);


ALTER TABLE public.entity_payment_method_map OWNER TO jbilling;

--
-- Name: entity_report_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE entity_report_map (
    report_id integer NOT NULL,
    entity_id integer NOT NULL
);


ALTER TABLE public.entity_report_map OWNER TO jbilling;

--
-- Name: enumeration; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE enumeration (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    name character varying(50) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.enumeration OWNER TO jbilling;

--
-- Name: enumeration_values; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE enumeration_values (
    id integer NOT NULL,
    enumeration_id integer NOT NULL,
    value character varying(50) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.enumeration_values OWNER TO jbilling;

--
-- Name: event_log; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE event_log (
    id integer NOT NULL,
    entity_id integer,
    user_id integer,
    table_id integer,
    foreign_id integer NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    level_field integer NOT NULL,
    module_id integer NOT NULL,
    message_id integer NOT NULL,
    old_num integer,
    old_str character varying(1000),
    old_date timestamp with time zone,
    optlock integer NOT NULL,
    affected_user_id integer
);


ALTER TABLE public.event_log OWNER TO jbilling;

--
-- Name: event_log_message; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE event_log_message (
    id integer NOT NULL
);


ALTER TABLE public.event_log_message OWNER TO jbilling;

--
-- Name: event_log_module; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE event_log_module (
    id integer NOT NULL
);


ALTER TABLE public.event_log_module OWNER TO jbilling;

--
-- Name: filter; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE filter (
    id integer NOT NULL,
    filter_set_id integer NOT NULL,
    type character varying(255) NOT NULL,
    constraint_type character varying(255) NOT NULL,
    field character varying(255) NOT NULL,
    template character varying(255) NOT NULL,
    visible boolean NOT NULL,
    integer_value integer,
    string_value character varying(255),
    start_date_value timestamp with time zone,
    end_date_value timestamp with time zone,
    version integer NOT NULL,
    boolean_value boolean,
    decimal_value numeric(22,10),
    decimal_high_value numeric(22,10)
);


ALTER TABLE public.filter OWNER TO jbilling;

--
-- Name: filter_set; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE filter_set (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    user_id integer NOT NULL,
    version integer NOT NULL
);


ALTER TABLE public.filter_set OWNER TO jbilling;

--
-- Name: filter_set_filter; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE filter_set_filter (
    filter_set_filters_id integer,
    filter_id integer
);


ALTER TABLE public.filter_set_filter OWNER TO jbilling;

--
-- Name: generic_status; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE generic_status (
    id integer NOT NULL,
    dtype character varying(50) NOT NULL,
    status_value integer NOT NULL,
    can_login integer
);


ALTER TABLE public.generic_status OWNER TO jbilling;

--
-- Name: generic_status_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE generic_status_type (
    id character varying(50) NOT NULL
);


ALTER TABLE public.generic_status_type OWNER TO jbilling;

--
-- Name: international_description; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE international_description (
    table_id integer NOT NULL,
    foreign_id integer NOT NULL,
    psudo_column character varying(20) NOT NULL,
    language_id integer NOT NULL,
    content character varying(4000) NOT NULL
);


ALTER TABLE public.international_description OWNER TO jbilling;

--
-- Name: invoice; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE invoice (
    id integer NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    billing_process_id integer,
    user_id integer,
    delegated_invoice_id integer,
    due_date date NOT NULL,
    total numeric(22,10) NOT NULL,
    payment_attempts integer DEFAULT 0 NOT NULL,
    status_id integer DEFAULT 1 NOT NULL,
    balance numeric(22,10),
    carried_balance numeric(22,10) NOT NULL,
    in_process_payment integer DEFAULT 1 NOT NULL,
    is_review integer NOT NULL,
    currency_id integer NOT NULL,
    deleted integer DEFAULT 0 NOT NULL,
    paper_invoice_batch_id integer,
    customer_notes character varying(1000),
    public_number character varying(40),
    last_reminder date,
    overdue_step integer,
    create_timestamp timestamp with time zone NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.invoice OWNER TO jbilling;

--
-- Name: invoice_delivery_method; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE invoice_delivery_method (
    id integer NOT NULL
);


ALTER TABLE public.invoice_delivery_method OWNER TO jbilling;

--
-- Name: invoice_line; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE invoice_line (
    id integer NOT NULL,
    invoice_id integer,
    type_id integer,
    amount numeric(22,10) NOT NULL,
    quantity numeric(22,10),
    price numeric(22,10),
    deleted integer DEFAULT 0 NOT NULL,
    item_id integer,
    description character varying(1000),
    source_user_id integer,
    is_percentage integer DEFAULT 0 NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.invoice_line OWNER TO jbilling;

--
-- Name: invoice_line_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE invoice_line_type (
    id integer NOT NULL,
    description character varying(50) NOT NULL,
    order_position integer NOT NULL
);


ALTER TABLE public.invoice_line_type OWNER TO jbilling;

--
-- Name: invoice_meta_field_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE invoice_meta_field_map (
    invoice_id integer NOT NULL,
    meta_field_value_id integer NOT NULL
);


ALTER TABLE public.invoice_meta_field_map OWNER TO jbilling;

--
-- Name: item; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE item (
    id integer NOT NULL,
    internal_number character varying(50),
    entity_id integer,
    percentage numeric(22,10),
    deleted integer DEFAULT 0 NOT NULL,
    has_decimals integer DEFAULT 0 NOT NULL,
    optlock integer NOT NULL,
    gl_code character varying(50)
);


ALTER TABLE public.item OWNER TO jbilling;

--
-- Name: item_meta_field_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE item_meta_field_map (
    item_id integer NOT NULL,
    meta_field_value_id integer NOT NULL
);


ALTER TABLE public.item_meta_field_map OWNER TO jbilling;

--
-- Name: item_price_timeline; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE item_price_timeline (
    item_id integer NOT NULL,
    price_model_id integer NOT NULL,
    start_date date NOT NULL
);


ALTER TABLE public.item_price_timeline OWNER TO jbilling;

--
-- Name: item_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE item_type (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    description character varying(100),
    order_line_type_id integer NOT NULL,
    optlock integer NOT NULL,
    internal boolean NOT NULL
);


ALTER TABLE public.item_type OWNER TO jbilling;

--
-- Name: item_type_exclude_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE item_type_exclude_map (
    item_id integer NOT NULL,
    type_id integer NOT NULL
);


ALTER TABLE public.item_type_exclude_map OWNER TO jbilling;

--
-- Name: item_type_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE item_type_map (
    item_id integer,
    type_id integer
);


ALTER TABLE public.item_type_map OWNER TO jbilling;

--
-- Name: jbilling_seqs; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE jbilling_seqs (
    name character varying(255),
    next_id integer
);


ALTER TABLE public.jbilling_seqs OWNER TO jbilling;

--
-- Name: jbilling_table; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE jbilling_table (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.jbilling_table OWNER TO jbilling;

--
-- Name: language; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE language (
    id integer NOT NULL,
    code character varying(2) NOT NULL,
    description character varying(50) NOT NULL
);


ALTER TABLE public.language OWNER TO jbilling;

--
-- Name: list_meta_field_values; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE list_meta_field_values (
    meta_field_value_id integer NOT NULL,
    list_value character varying(255)
);


ALTER TABLE public.list_meta_field_values OWNER TO jbilling;

--
-- Name: mediation_cfg; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE mediation_cfg (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    name character varying(50) NOT NULL,
    order_value integer NOT NULL,
    pluggable_task_id integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.mediation_cfg OWNER TO jbilling;

--
-- Name: mediation_errors; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE mediation_errors (
    accountcode character varying(50) NOT NULL,
    src character varying(50),
    dst character varying(50),
    dcontext character varying(50),
    clid character varying(50),
    channel character varying(50),
    dstchannel character varying(50),
    lastapp character varying(50),
    lastdata character varying(50),
    start_time timestamp with time zone,
    answer timestamp with time zone,
    end_time timestamp with time zone,
    duration integer,
    billsec integer,
    disposition character varying(50),
    amaflags character varying(50),
    userfield character varying(50),
    error_message character varying(200),
    should_retry boolean
);


ALTER TABLE public.mediation_errors OWNER TO jbilling;

--
-- Name: mediation_order_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE mediation_order_map (
    mediation_process_id integer NOT NULL,
    order_id integer NOT NULL
);


ALTER TABLE public.mediation_order_map OWNER TO jbilling;

--
-- Name: mediation_process; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE mediation_process (
    id integer NOT NULL,
    configuration_id integer NOT NULL,
    start_datetime timestamp with time zone NOT NULL,
    end_datetime timestamp with time zone,
    orders_affected integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.mediation_process OWNER TO jbilling;

--
-- Name: mediation_record; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE mediation_record (
    id_key character varying(100) NOT NULL,
    start_datetime timestamp with time zone NOT NULL,
    mediation_process_id integer,
    optlock integer NOT NULL,
    status_id integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.mediation_record OWNER TO jbilling;

--
-- Name: mediation_record_line; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE mediation_record_line (
    id integer NOT NULL,
    order_line_id integer NOT NULL,
    event_date timestamp with time zone NOT NULL,
    amount numeric(22,10) NOT NULL,
    quantity numeric(22,10) NOT NULL,
    description character varying(200),
    optlock integer NOT NULL,
    mediation_record_id integer NOT NULL
);


ALTER TABLE public.mediation_record_line OWNER TO jbilling;

--
-- Name: meta_field_name; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE meta_field_name (
    id integer NOT NULL,
    name character varying(100),
    entity_type character varying(25) NOT NULL,
    data_type character varying(25) NOT NULL,
    is_disabled boolean,
    is_mandatory boolean,
    display_order integer,
    default_value_id integer,
    optlock integer NOT NULL,
    entity_id integer DEFAULT 1
);


ALTER TABLE public.meta_field_name OWNER TO jbilling;

--
-- Name: meta_field_value; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE meta_field_value (
    id integer NOT NULL,
    meta_field_name_id integer NOT NULL,
    dtype character varying(10) NOT NULL,
    boolean_value boolean,
    date_value timestamp with time zone,
    decimal_value numeric(22,10),
    integer_value integer,
    string_value character varying(1000)
);


ALTER TABLE public.meta_field_value OWNER TO jbilling;

--
-- Name: my_test; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE my_test (
    id integer NOT NULL,
    entity_id integer,
    status_id integer,
    days integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.my_test OWNER TO jbilling;

--
-- Name: notification_category; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_category (
    id integer NOT NULL
);


ALTER TABLE public.notification_category OWNER TO jbilling;

--
-- Name: notification_message; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_message (
    id integer NOT NULL,
    type_id integer,
    entity_id integer NOT NULL,
    language_id integer NOT NULL,
    use_flag integer DEFAULT 1 NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.notification_message OWNER TO jbilling;

--
-- Name: notification_message_arch; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_message_arch (
    id integer NOT NULL,
    type_id integer,
    create_datetime timestamp with time zone NOT NULL,
    user_id integer,
    result_message character varying(200),
    optlock integer NOT NULL
);


ALTER TABLE public.notification_message_arch OWNER TO jbilling;

--
-- Name: notification_message_arch_line; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_message_arch_line (
    id integer NOT NULL,
    message_archive_id integer,
    section integer NOT NULL,
    content character varying(1000) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.notification_message_arch_line OWNER TO jbilling;

--
-- Name: notification_message_line; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_message_line (
    id integer NOT NULL,
    message_section_id integer,
    content character varying(1000) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.notification_message_line OWNER TO jbilling;

--
-- Name: notification_message_section; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_message_section (
    id integer NOT NULL,
    message_id integer,
    section integer,
    optlock integer NOT NULL
);


ALTER TABLE public.notification_message_section OWNER TO jbilling;

--
-- Name: notification_message_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE notification_message_type (
    id integer NOT NULL,
    optlock integer NOT NULL,
    category_id integer
);


ALTER TABLE public.notification_message_type OWNER TO jbilling;

--
-- Name: order_billing_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE order_billing_type (
    id integer NOT NULL
);


ALTER TABLE public.order_billing_type OWNER TO jbilling;

--
-- Name: order_line; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE order_line (
    id integer NOT NULL,
    order_id integer,
    item_id integer,
    type_id integer,
    amount numeric(22,10) NOT NULL,
    quantity numeric(22,10),
    price numeric(22,10),
    item_price integer,
    create_datetime timestamp with time zone NOT NULL,
    deleted integer DEFAULT 0 NOT NULL,
    description character varying(1000),
    provisioning_status integer,
    provisioning_request_id character varying(50),
    optlock integer NOT NULL,
    use_item boolean NOT NULL
);


ALTER TABLE public.order_line OWNER TO jbilling;

--
-- Name: order_line_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE order_line_type (
    id integer NOT NULL,
    editable integer NOT NULL
);


ALTER TABLE public.order_line_type OWNER TO jbilling;

--
-- Name: order_meta_field_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE order_meta_field_map (
    order_id integer NOT NULL,
    meta_field_value_id integer NOT NULL
);


ALTER TABLE public.order_meta_field_map OWNER TO jbilling;

--
-- Name: order_period; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE order_period (
    id integer NOT NULL,
    entity_id integer,
    value integer,
    unit_id integer,
    optlock integer NOT NULL
);


ALTER TABLE public.order_period OWNER TO jbilling;

--
-- Name: order_process; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE order_process (
    id integer NOT NULL,
    order_id integer,
    invoice_id integer,
    billing_process_id integer,
    periods_included integer,
    period_start date,
    period_end date,
    is_review integer NOT NULL,
    origin integer,
    optlock integer NOT NULL
);


ALTER TABLE public.order_process OWNER TO jbilling;

--
-- Name: paper_invoice_batch; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE paper_invoice_batch (
    id integer NOT NULL,
    total_invoices integer NOT NULL,
    delivery_date date,
    is_self_managed integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.paper_invoice_batch OWNER TO jbilling;

--
-- Name: partner; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE partner (
    id integer NOT NULL,
    user_id integer,
    balance numeric(22,10) NOT NULL,
    total_payments numeric(22,10) NOT NULL,
    total_refunds numeric(22,10) NOT NULL,
    total_payouts numeric(22,10) NOT NULL,
    percentage_rate numeric(22,10),
    referral_fee numeric(22,10),
    fee_currency_id integer,
    one_time integer NOT NULL,
    period_unit_id integer NOT NULL,
    period_value integer NOT NULL,
    next_payout_date date NOT NULL,
    due_payout numeric(22,10),
    automatic_process integer NOT NULL,
    related_clerk integer,
    optlock integer NOT NULL
);


ALTER TABLE public.partner OWNER TO jbilling;

--
-- Name: partner_meta_field_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE partner_meta_field_map (
    partner_id integer NOT NULL,
    meta_field_value_id integer NOT NULL
);


ALTER TABLE public.partner_meta_field_map OWNER TO jbilling;

--
-- Name: partner_payout; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE partner_payout (
    id integer NOT NULL,
    starting_date date NOT NULL,
    ending_date date NOT NULL,
    payments_amount numeric(22,10) NOT NULL,
    refunds_amount numeric(22,10) NOT NULL,
    balance_left numeric(22,10) NOT NULL,
    payment_id integer,
    partner_id integer,
    optlock integer NOT NULL
);


ALTER TABLE public.partner_payout OWNER TO jbilling;

--
-- Name: partner_range; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE partner_range (
    id integer NOT NULL,
    partner_id integer,
    percentage_rate numeric(22,10),
    referral_fee numeric(22,10),
    range_from integer NOT NULL,
    range_to integer NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.partner_range OWNER TO jbilling;

--
-- Name: payment; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment (
    id integer NOT NULL,
    user_id integer,
    attempt integer,
    result_id integer,
    amount numeric(22,10) NOT NULL,
    create_datetime timestamp with time zone NOT NULL,
    update_datetime timestamp with time zone,
    payment_date date,
    method_id integer NOT NULL,
    credit_card_id integer,
    deleted integer DEFAULT 0 NOT NULL,
    is_refund integer DEFAULT 0 NOT NULL,
    is_preauth integer DEFAULT 0 NOT NULL,
    payment_id integer,
    currency_id integer NOT NULL,
    payout_id integer,
    ach_id integer,
    balance numeric(22,10),
    optlock integer NOT NULL,
    payment_period integer,
    payment_notes character varying(500)
);


ALTER TABLE public.payment OWNER TO jbilling;

--
-- Name: payment_authorization; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment_authorization (
    id integer NOT NULL,
    payment_id integer,
    processor character varying(40) NOT NULL,
    code1 character varying(40) NOT NULL,
    code2 character varying(40),
    code3 character varying(40),
    approval_code character varying(20),
    avs character varying(20),
    transaction_id character varying(40),
    md5 character varying(100),
    card_code character varying(100),
    create_datetime date NOT NULL,
    response_message character varying(200),
    optlock integer NOT NULL
);


ALTER TABLE public.payment_authorization OWNER TO jbilling;

--
-- Name: payment_info_cheque; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment_info_cheque (
    id integer NOT NULL,
    payment_id integer,
    bank character varying(50),
    cheque_number character varying(50),
    cheque_date date,
    optlock integer NOT NULL
);


ALTER TABLE public.payment_info_cheque OWNER TO jbilling;

--
-- Name: payment_invoice; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment_invoice (
    id integer NOT NULL,
    payment_id integer,
    invoice_id integer,
    amount numeric(22,10),
    create_datetime timestamp with time zone NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.payment_invoice OWNER TO jbilling;

--
-- Name: payment_meta_field_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment_meta_field_map (
    payment_id integer NOT NULL,
    meta_field_value_id integer NOT NULL
);


ALTER TABLE public.payment_meta_field_map OWNER TO jbilling;

--
-- Name: payment_method; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment_method (
    id integer NOT NULL
);


ALTER TABLE public.payment_method OWNER TO jbilling;

--
-- Name: payment_result; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE payment_result (
    id integer NOT NULL
);


ALTER TABLE public.payment_result OWNER TO jbilling;

--
-- Name: period_unit; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE period_unit (
    id integer NOT NULL
);


ALTER TABLE public.period_unit OWNER TO jbilling;

--
-- Name: permission; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE permission (
    id integer NOT NULL,
    type_id integer NOT NULL,
    foreign_id integer
);


ALTER TABLE public.permission OWNER TO jbilling;

--
-- Name: permission_role_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE permission_role_map (
    permission_id integer,
    role_id integer
);


ALTER TABLE public.permission_role_map OWNER TO jbilling;

--
-- Name: permission_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE permission_type (
    id integer NOT NULL,
    description character varying(30) NOT NULL
);


ALTER TABLE public.permission_type OWNER TO jbilling;

--
-- Name: permission_user; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE permission_user (
    permission_id integer,
    user_id integer,
    is_grant integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.permission_user OWNER TO jbilling;

--
-- Name: plan; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE plan (
    id integer NOT NULL,
    item_id integer NOT NULL,
    description character varying(255),
    period_id integer NOT NULL
);


ALTER TABLE public.plan OWNER TO jbilling;

--
-- Name: plan_item; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE plan_item (
    id integer NOT NULL,
    plan_id integer,
    item_id integer NOT NULL,
    precedence integer NOT NULL,
    plan_item_bundle_id integer
);


ALTER TABLE public.plan_item OWNER TO jbilling;

--
-- Name: plan_item_bundle; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE plan_item_bundle (
    id integer NOT NULL,
    quantity numeric(22,10) NOT NULL,
    period_id integer NOT NULL,
    target_customer character varying(20) NOT NULL,
    add_if_exists boolean NOT NULL
);


ALTER TABLE public.plan_item_bundle OWNER TO jbilling;

--
-- Name: plan_item_price_timeline; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE plan_item_price_timeline (
    plan_item_id integer NOT NULL,
    price_model_id integer NOT NULL,
    start_date date NOT NULL
);


ALTER TABLE public.plan_item_price_timeline OWNER TO jbilling;

--
-- Name: pluggable_task; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE pluggable_task (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    type_id integer,
    processing_order integer NOT NULL,
    optlock integer NOT NULL,
    notes character varying(1000)
);


ALTER TABLE public.pluggable_task OWNER TO jbilling;

--
-- Name: pluggable_task_parameter; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE pluggable_task_parameter (
    id integer NOT NULL,
    task_id integer,
    name character varying(50) NOT NULL,
    int_value integer,
    str_value character varying(500),
    float_value numeric(22,10),
    optlock integer NOT NULL
);


ALTER TABLE public.pluggable_task_parameter OWNER TO jbilling;

--
-- Name: pluggable_task_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE pluggable_task_type (
    id integer NOT NULL,
    category_id integer NOT NULL,
    class_name character varying(200) NOT NULL,
    min_parameters integer NOT NULL
);


ALTER TABLE public.pluggable_task_type OWNER TO jbilling;

--
-- Name: pluggable_task_type_category; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE pluggable_task_type_category (
    id integer NOT NULL,
    interface_name character varying(200) NOT NULL
);


ALTER TABLE public.pluggable_task_type_category OWNER TO jbilling;

--
-- Name: preference; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE preference (
    id integer NOT NULL,
    type_id integer,
    table_id integer NOT NULL,
    foreign_id integer NOT NULL,
    value character varying(200)
);


ALTER TABLE public.preference OWNER TO jbilling;

--
-- Name: preference_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE preference_type (
    id integer NOT NULL,
    def_value character varying(200)
);


ALTER TABLE public.preference_type OWNER TO jbilling;

--
-- Name: price_model; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE price_model (
    id integer NOT NULL,
    strategy_type character varying(40) NOT NULL,
    rate numeric(22,10),
    included_quantity integer,
    currency_id integer,
    next_model_id integer
);


ALTER TABLE public.price_model OWNER TO jbilling;

--
-- Name: price_model_attribute; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE price_model_attribute (
    price_model_id integer NOT NULL,
    attribute_name character varying(255) NOT NULL,
    attribute_value character varying(255)
);


ALTER TABLE public.price_model_attribute OWNER TO jbilling;

--
-- Name: process_run; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE process_run (
    id integer NOT NULL,
    process_id integer,
    run_date date NOT NULL,
    started timestamp with time zone NOT NULL,
    finished timestamp with time zone,
    payment_finished timestamp with time zone,
    invoices_generated integer,
    optlock integer NOT NULL,
    status_id integer NOT NULL
);


ALTER TABLE public.process_run OWNER TO jbilling;

--
-- Name: process_run_total; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE process_run_total (
    id integer NOT NULL,
    process_run_id integer,
    currency_id integer NOT NULL,
    total_invoiced numeric(22,10),
    total_paid numeric(22,10),
    total_not_paid numeric(22,10),
    optlock integer NOT NULL
);


ALTER TABLE public.process_run_total OWNER TO jbilling;

--
-- Name: process_run_total_pm; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE process_run_total_pm (
    id integer NOT NULL,
    process_run_total_id integer,
    payment_method_id integer,
    total numeric(22,10) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.process_run_total_pm OWNER TO jbilling;

--
-- Name: process_run_user; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE process_run_user (
    id integer NOT NULL,
    process_run_id integer NOT NULL,
    user_id integer NOT NULL,
    status integer NOT NULL,
    created timestamp with time zone NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.process_run_user OWNER TO jbilling;

--
-- Name: promotion; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE promotion (
    id integer NOT NULL,
    item_id integer,
    code character varying(50) NOT NULL,
    notes character varying(200),
    once integer NOT NULL,
    since date,
    until date
);


ALTER TABLE public.promotion OWNER TO jbilling;

--
-- Name: promotion_user_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE promotion_user_map (
    user_id integer,
    promotion_id integer
);


ALTER TABLE public.promotion_user_map OWNER TO jbilling;

--
-- Name: purchase_order; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE purchase_order (
    id integer NOT NULL,
    user_id integer,
    period_id integer,
    billing_type_id integer NOT NULL,
    active_since date,
    active_until date,
    cycle_start date,
    create_datetime timestamp with time zone NOT NULL,
    next_billable_day date,
    created_by integer,
    status_id integer NOT NULL,
    currency_id integer NOT NULL,
    deleted integer DEFAULT 0 NOT NULL,
    notify integer,
    last_notified timestamp with time zone,
    notification_step integer,
    due_date_unit_id integer,
    due_date_value integer,
    df_fm integer,
    anticipate_periods integer,
    own_invoice integer,
    notes character varying(200),
    notes_in_invoice integer,
    is_current integer,
    optlock integer NOT NULL
);


ALTER TABLE public.purchase_order OWNER TO jbilling;

--
-- Name: rate_card; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE rate_card (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    table_name character varying(255) NOT NULL,
    entity_id integer NOT NULL
);


ALTER TABLE public.rate_card OWNER TO jbilling;

--
-- Name: recent_item; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE recent_item (
    id integer NOT NULL,
    type character varying(255) NOT NULL,
    object_id integer NOT NULL,
    user_id integer NOT NULL,
    version integer NOT NULL
);


ALTER TABLE public.recent_item OWNER TO jbilling;

--
-- Name: report; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE report (
    id integer NOT NULL,
    type_id integer NOT NULL,
    name character varying(255) NOT NULL,
    file_name character varying(500) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.report OWNER TO jbilling;

--
-- Name: report_parameter; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE report_parameter (
    id integer NOT NULL,
    report_id integer NOT NULL,
    dtype character varying(10) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.report_parameter OWNER TO jbilling;

--
-- Name: report_type; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE report_type (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    optlock integer NOT NULL
);


ALTER TABLE public.report_type OWNER TO jbilling;

--
-- Name: role; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE role (
    id integer NOT NULL,
    entity_id integer,
    role_type_id integer
);


ALTER TABLE public.role OWNER TO jbilling;

--
-- Name: shortcut; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE shortcut (
    id integer NOT NULL,
    user_id integer NOT NULL,
    controller character varying(255) NOT NULL,
    action character varying(255),
    name character varying(255),
    object_id integer,
    version integer NOT NULL
);


ALTER TABLE public.shortcut OWNER TO jbilling;

--
-- Name: user_credit_card_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE user_credit_card_map (
    user_id integer,
    credit_card_id integer
);


ALTER TABLE public.user_credit_card_map OWNER TO jbilling;

--
-- Name: user_role_map; Type: TABLE; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE TABLE user_role_map (
    user_id integer,
    role_id integer
);


ALTER TABLE public.user_role_map OWNER TO jbilling;

--
-- Data for Name: ach; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY ach (id, user_id, aba_routing, bank_account, account_type, bank_name, account_name, optlock, gateway_key) FROM stdin;
\.


--
-- Data for Name: ageing_entity_step; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY ageing_entity_step (id, entity_id, status_id, days, optlock) FROM stdin;
\.


--
-- Data for Name: base_user; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY base_user (id, entity_id, password, deleted, language_id, status_id, subscriber_status, currency_id, create_datetime, last_status_change, last_login, user_name, failed_attempts, optlock) FROM stdin;
\.


--
-- Data for Name: billing_process; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY billing_process (id, entity_id, billing_date, period_unit_id, period_value, is_review, paper_invoice_batch_id, retries_to_do, optlock) FROM stdin;
\.


--
-- Data for Name: billing_process_configuration; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY billing_process_configuration (id, entity_id, next_run_date, generate_report, retries, days_for_retry, days_for_report, review_status, period_unit_id, period_value, due_date_unit_id, due_date_value, df_fm, only_recurring, invoice_date_process, optlock, auto_payment, maximum_periods, auto_payment_application) FROM stdin;
\.


--
-- Data for Name: blacklist; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY blacklist (id, entity_id, create_datetime, type, source, credit_card, credit_card_id, contact_id, user_id, optlock, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: breadcrumb; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY breadcrumb (id, user_id, controller, action, name, object_id, version, description) FROM stdin;
\.


--
-- Data for Name: cdrentries; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY cdrentries (id, accountcode, src, dst, dcontext, clid, channel, dstchannel, lastapp, lastdatat, start_time, answer, end_time, duration, billsec, disposition, amaflags, userfield, ts) FROM stdin;
\.


--
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY contact (id, organization_name, street_addres1, street_addres2, city, state_province, postal_code, country_code, last_name, first_name, person_initial, person_title, phone_country_code, phone_area_code, phone_phone_number, fax_country_code, fax_area_code, fax_phone_number, email, create_datetime, deleted, notification_include, user_id, optlock) FROM stdin;
\.


--
-- Data for Name: contact_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY contact_map (id, contact_id, type_id, table_id, foreign_id, optlock) FROM stdin;
\.


--
-- Data for Name: contact_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY contact_type (id, entity_id, is_primary, optlock) FROM stdin;
1	\N	\N	0
\.


--
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY country (id, code) FROM stdin;
1	AF
2	AL
3	DZ
4	AS
5	AD
6	AO
7	AI
8	AQ
9	AG
10	AR
11	AM
12	AW
13	AU
14	AT
15	AZ
16	BS
17	BH
18	BD
19	BB
20	BY
21	BE
22	BZ
23	BJ
24	BM
25	BT
26	BO
27	BA
28	BW
29	BV
30	BR
31	IO
32	BN
33	BG
34	BF
35	BI
36	KH
37	CM
38	CA
39	CV
40	KY
41	CF
42	TD
43	CL
44	CN
45	CX
46	CC
47	CO
48	KM
49	CG
50	CK
51	CR
52	CI
53	HR
54	CU
55	CY
56	CZ
57	CD
58	DK
59	DJ
60	DM
61	DO
62	TP
63	EC
64	EG
65	SV
66	GQ
67	ER
68	EE
69	ET
70	FK
71	FO
72	FJ
73	FI
74	FR
75	GF
76	PF
77	TF
78	GA
79	GM
80	GE
81	DE
82	GH
83	GI
84	GR
85	GL
86	GD
87	GP
88	GU
89	GT
90	GN
91	GW
92	GY
93	HT
94	HM
95	HN
96	HK
97	HU
98	IS
99	IN
100	ID
101	IR
102	IQ
103	IE
104	IL
105	IT
106	JM
107	JP
108	JO
109	KZ
110	KE
111	KI
112	KR
113	KW
114	KG
115	LA
116	LV
117	LB
118	LS
119	LR
120	LY
121	LI
122	LT
123	LU
124	MO
125	MK
126	MG
127	MW
128	MY
129	MV
130	ML
131	MT
132	MH
133	MQ
134	MR
135	MU
136	YT
137	MX
138	FM
139	MD
140	MC
141	MN
142	MS
143	MA
144	MZ
145	MM
146	NA
147	NR
148	NP
149	NL
150	AN
151	NC
152	NZ
153	NI
154	NE
155	NG
156	NU
157	NF
158	KP
159	MP
160	NO
161	OM
162	PK
163	PW
164	PA
165	PG
166	PY
167	PE
168	PH
169	PN
170	PL
171	PT
172	PR
173	QA
174	RE
175	RO
176	RU
177	RW
178	WS
179	SM
180	ST
181	SA
182	SN
183	YU
184	SC
185	SL
186	SG
187	SK
188	SI
189	SB
190	SO
191	ZA
192	GS
193	ES
194	LK
195	SH
196	KN
197	LC
198	PM
199	VC
200	SD
201	SR
202	SJ
203	SZ
204	SE
205	CH
206	SY
207	TW
208	TJ
209	TZ
210	TH
211	TG
212	TK
213	TO
214	TT
215	TN
216	TR
217	TM
218	TC
219	TV
220	UG
221	UA
222	AE
223	UK
224	US
225	UM
226	UY
227	UZ
228	VU
229	VA
230	VE
231	VN
232	VG
233	VI
234	WF
235	YE
236	ZM
237	ZW
\.


--
-- Data for Name: credit_card; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY credit_card (id, cc_number, cc_number_plain, cc_expiry, name, cc_type, deleted, gateway_key, optlock) FROM stdin;
\.


--
-- Data for Name: currency; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY currency (id, symbol, code, country_code, optlock) FROM stdin;
1	US$	USD	US	0
2	C$	CAD	CA	0
3	&#8364;	EUR	EU	0
4	&#165;	JPY	JP	0
5	&#163;	GBP	UK	0
6	&#8361;	KRW	KR	0
7	Sf	CHF	CH	0
8	SeK	SEK	SE	0
9	S$	SGD	SG	0
10	M$	MYR	MY	0
11	$	AUD	AU	0
\.


--
-- Data for Name: currency_entity_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY currency_entity_map (currency_id, entity_id) FROM stdin;
\.


--
-- Data for Name: currency_exchange; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY currency_exchange (id, entity_id, currency_id, rate, create_datetime, optlock, valid_since) FROM stdin;
1	0	2	1.3250000000	2004-03-09 00:00:00+01	1	1970-01-01
2	0	3	0.8118000000	2004-03-09 00:00:00+01	1	1970-01-01
3	0	4	111.4000000000	2004-03-09 00:00:00+01	1	1970-01-01
4	0	5	0.5479000000	2004-03-09 00:00:00+01	1	1970-01-01
5	0	6	1171.0000000000	2004-03-09 00:00:00+01	1	1970-01-01
6	0	7	1.2300000000	2004-07-06 00:00:00+02	1	1970-01-01
7	0	8	7.4700000000	2004-07-06 00:00:00+02	1	1970-01-01
10	0	9	1.6800000000	2004-10-12 00:00:00+02	1	1970-01-01
11	0	10	3.8000000000	2004-10-12 00:00:00+02	1	1970-01-01
12	0	11	1.2880000000	2007-01-25 00:00:00+01	1	1970-01-01
\.


--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY customer (id, user_id, partner_id, referral_fee_paid, invoice_delivery_method_id, notes, auto_payment_type, due_date_unit_id, due_date_value, df_fm, parent_id, is_parent, exclude_aging, invoice_child, current_order_id, optlock, balance_type, dynamic_balance, credit_limit, auto_recharge, use_parent_pricing) FROM stdin;
\.


--
-- Data for Name: customer_meta_field_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY customer_meta_field_map (customer_id, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: customer_price; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY customer_price (plan_item_id, user_id, create_datetime) FROM stdin;
\.


--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) FROM stdin;
1337623084753-1	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:01.595348+02	1	EXECUTED	3:03a5cebb218306ca9a12129773f33725	Create Table		\N	2.0.5
1337623084753-2	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:01.855286+02	2	EXECUTED	3:61010718ff66001bc8bc0984204773b2	Create Table		\N	2.0.5
1337623084753-3	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.113936+02	3	EXECUTED	3:be23a14b20287e6a3089c035dbccb540	Create Table		\N	2.0.5
1337623084753-4	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.225793+02	4	EXECUTED	3:3015d3a382608425fa527e6d4d9d8377	Create Table		\N	2.0.5
1337623084753-5	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.359575+02	5	EXECUTED	3:ecbd9979013788fbeaa1aa531d5e1dab	Create Table		\N	2.0.5
1337623084753-6	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.458093+02	6	EXECUTED	3:a0ee15f0a67452a7326fd72bc0b3e640	Create Table		\N	2.0.5
1337623084753-7	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.619817+02	7	EXECUTED	3:7bacba393473f05078e84d0f2a03e01c	Create Table		\N	2.0.5
1337623084753-8	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.744777+02	8	EXECUTED	3:a759acb302158b4582ae9d0c12ef6368	Create Table		\N	2.0.5
1337623084753-9	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.901787+02	9	EXECUTED	3:e81dd6f9d69a8785570b0883ec3841bc	Create Table		\N	2.0.5
1337623084753-10	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:02.997988+02	10	EXECUTED	3:e616cf3e970760f3434101e6613f17d2	Create Table		\N	2.0.5
1337623084753-11	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.086605+02	11	EXECUTED	3:5704c03ead72025fe1277d6a92e6d0e4	Create Table		\N	2.0.5
1337623084753-12	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.186534+02	12	EXECUTED	3:9413e0e10251d19fb5ab961542a3ca8a	Create Table		\N	2.0.5
1337623084753-13	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.277058+02	13	EXECUTED	3:25322730bee882bdef7faefe1be7c97e	Create Table		\N	2.0.5
1337623084753-14	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.36435+02	14	EXECUTED	3:8de11595ff6ac63a0a475960fe2b9f56	Create Table		\N	2.0.5
1337623084753-15	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.419697+02	15	EXECUTED	3:29be1c001eab2651732e5110cf2f91fb	Create Table		\N	2.0.5
1337623084753-16	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.549019+02	16	EXECUTED	3:bdefa07e71d75a44e99ba6e48f4759b9	Create Table		\N	2.0.5
1337623084753-17	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.690692+02	17	EXECUTED	3:1fc21a3fabd75ceec470ddb352cccde9	Create Table		\N	2.0.5
1337623084753-18	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.714012+02	18	EXECUTED	3:b70f842a159d1a3d57a2aaf569aaf0bc	Create Table		\N	2.0.5
1337623084753-19	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.747177+02	19	EXECUTED	3:85cab7e11717b5288f5bd7b0d71f530c	Create Table		\N	2.0.5
1337623084753-20	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.894236+02	20	EXECUTED	3:aaba85d3d572bb5e9ab71f71d9c9f442	Create Table		\N	2.0.5
1337623084753-21	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.937136+02	21	EXECUTED	3:63fe6c3fa46633b1b87f78a00046dbb1	Create Table		\N	2.0.5
1337623084753-22	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:03.981945+02	22	EXECUTED	3:c8b0895a0664de435a200437280573b8	Create Table		\N	2.0.5
1337623084753-23	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.025544+02	23	EXECUTED	3:bb0cd6312da79bee3852460c63d1d16d	Create Table		\N	2.0.5
1337623084753-24	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.115446+02	24	EXECUTED	3:dfc8d798f653c487abc7df8f8a548996	Create Table		\N	2.0.5
1337623084753-25	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.202584+02	25	EXECUTED	3:87fd317d006863eda0e39a3bae67398a	Create Table		\N	2.0.5
1337623084753-26	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.340241+02	26	EXECUTED	3:9c24aa3c98cca5b049094a72fb349c49	Create Table		\N	2.0.5
1337623084753-27	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.426104+02	27	EXECUTED	3:19dadb4f7cbc41339e8794de238fc8b8	Create Table		\N	2.0.5
1337623084753-28	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.50462+02	28	EXECUTED	3:81b2fedc300c362e461ce9cd477efb77	Create Table		\N	2.0.5
1337623084753-29	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.639664+02	29	EXECUTED	3:4cf065414588cb0d3491c30ff38e961f	Create Table		\N	2.0.5
1337623084753-30	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.716308+02	30	EXECUTED	3:aa18f8b1f6cd84e38b3ac7de4758b00e	Create Table		\N	2.0.5
1337623084753-31	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.758276+02	31	EXECUTED	3:071f231e5309df4a4e4893eca9ef61cb	Create Table		\N	2.0.5
1337623084753-32	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.849591+02	32	EXECUTED	3:25d3dae54944511d5ec57434f649d498	Create Table		\N	2.0.5
1337623084753-33	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:04.937461+02	33	EXECUTED	3:5817bbb216dc6fd92d0c53bf717e6139	Create Table		\N	2.0.5
1337623084753-34	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.014156+02	34	EXECUTED	3:cf1c7a6a7074cabcc12704563c790ac8	Create Table		\N	2.0.5
1337623084753-35	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.150961+02	35	EXECUTED	3:f87f8d65ffb8dfcdbf137b922e49e611	Create Table		\N	2.0.5
1337623084753-36	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.238322+02	36	EXECUTED	3:d10ae137a7080c63a229de563450e6c4	Create Table		\N	2.0.5
1337623084753-37	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.363554+02	37	EXECUTED	3:f4f5fac951c53bd7c7f64035249a685a	Create Table		\N	2.0.5
1337623084753-38	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.450432+02	38	EXECUTED	3:a1f1a3c56fc2e5db5e40ca33ed91af5e	Create Table		\N	2.0.5
1337623084753-39	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.495092+02	39	EXECUTED	3:3f117b32bc53c16c8c0e3c2611dae9c6	Create Table		\N	2.0.5
1337623084753-40	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.594605+02	40	EXECUTED	3:9f7995d199d0a2481e03b76735210da5	Create Table		\N	2.0.5
1337623084753-41	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.636871+02	41	EXECUTED	3:260a02edc37a7e8599153f3486fad042	Create Table		\N	2.0.5
1337623084753-42	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.673196+02	42	EXECUTED	3:0b63449b721d6022e196e05a5a2b84ac	Create Table		\N	2.0.5
1337623084753-43	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.762239+02	43	EXECUTED	3:43c35c8ae9cd2be200ce87089fb55b8e	Create Table		\N	2.0.5
1337623084753-44	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.792831+02	44	EXECUTED	3:eb43e169a44716c8d5f117d5192144e5	Create Table		\N	2.0.5
1337623084753-45	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.84233+02	45	EXECUTED	3:2b49257b83c3bad183f97ac0911949e0	Create Table		\N	2.0.5
1337623084753-46	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.884192+02	46	EXECUTED	3:3f8e6de1ab2e63517dfe331b3331ace5	Create Table		\N	2.0.5
1337623084753-47	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:05.973754+02	47	EXECUTED	3:78f2c53f455f067accdc418138c7914f	Create Table		\N	2.0.5
1337623084753-48	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.06156+02	48	EXECUTED	3:dd52dea64df9a983d0e8d9d72869c991	Create Table		\N	2.0.5
1337623084753-49	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.09654+02	49	EXECUTED	3:c06ade2dbf43b1f650150e61ea4e050d	Create Table		\N	2.0.5
1337623084753-50	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.182535+02	50	EXECUTED	3:0b317492fd25c5769c3807350bc1f9e2	Create Table		\N	2.0.5
1337623084753-51	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.331424+02	51	EXECUTED	3:44f27a94ae47fd3483c0e82ce8025557	Create Table		\N	2.0.5
1337623084753-52	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.372945+02	52	EXECUTED	3:667ad5bec78567b204d98820b77bbca2	Create Table		\N	2.0.5
1337623084753-53	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.443403+02	53	EXECUTED	3:6419b8c4b21a389acc596bbc2fd701e6	Create Table		\N	2.0.5
1337623084753-54	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.528964+02	54	EXECUTED	3:f1b15f90b965194bbd3f6cd84982b044	Create Table		\N	2.0.5
1337623084753-55	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.633223+02	55	EXECUTED	3:18652d480389ee254762e972d5e40356	Create Table		\N	2.0.5
1337623084753-56	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.721357+02	56	EXECUTED	3:3a3ad69e2159ec8b75d22dd47909c2ee	Create Table		\N	2.0.5
1337623084753-57	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.816394+02	57	EXECUTED	3:9ea9a904bbe7e306537bf5bd79cd7f21	Create Table		\N	2.0.5
1337623084753-58	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.898045+02	58	EXECUTED	3:92523b68b7b4436c1aefc31031e45560	Create Table		\N	2.0.5
1337623084753-59	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:06.976665+02	59	EXECUTED	3:0446ce732f6e25ba0eea15c735d637e8	Create Table		\N	2.0.5
1337623084753-60	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.076269+02	60	EXECUTED	3:7745e68e3208bb864ed8cc90090cead7	Create Table		\N	2.0.5
1337623084753-61	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.199393+02	61	EXECUTED	3:09405d6a904b56318ac3464e52750800	Create Table		\N	2.0.5
1337623084753-62	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.335591+02	62	EXECUTED	3:a614459dad50881980cc72feddbd7821	Create Table		\N	2.0.5
1337623084753-63	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.420371+02	63	EXECUTED	3:eb87b9f99f8ee815417b7914b53a2ebe	Create Table		\N	2.0.5
1337623084753-64	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.497328+02	64	EXECUTED	3:bcd9f1b3472d1e125863c0a48b687b09	Create Table		\N	2.0.5
1337623084753-65	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.585974+02	65	EXECUTED	3:037ecda8ced792a9e4f57dce8cf8003b	Create Table		\N	2.0.5
1337623084753-66	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.724166+02	66	EXECUTED	3:a16247e56fe6a6d7e1eab58d2cb47b5a	Create Table		\N	2.0.5
1337623084753-67	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.810538+02	67	EXECUTED	3:aacc643466d1a698c9aa2ea6f4f77dd2	Create Table		\N	2.0.5
1337623084753-68	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.844378+02	68	EXECUTED	3:4e038cbe59d25b201deb700d77c9a1fa	Create Table		\N	2.0.5
1337623084753-69	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:07.933608+02	69	EXECUTED	3:394bd9eadfadf19c6d11cdb84960aec4	Create Table		\N	2.0.5
1337623084753-70	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.025001+02	70	EXECUTED	3:d397324ce47bc6a3a1dba5141527bef6	Create Table		\N	2.0.5
1337623084753-71	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.121987+02	71	EXECUTED	3:d004537f815a71762d54a225c10b0815	Create Table		\N	2.0.5
1337623084753-72	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.211118+02	72	EXECUTED	3:a44fa8aa96bdeffdbfea7e663cfe81d7	Create Table		\N	2.0.5
1337623084753-73	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.243945+02	73	EXECUTED	3:b5c0298f92aa91cfc3a5a5984887ae24	Create Table		\N	2.0.5
1337623084753-74	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.322884+02	74	EXECUTED	3:a7fb3d6049e40bfb02749d23ead005ce	Create Table		\N	2.0.5
1337623084753-75	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.42254+02	75	EXECUTED	3:1c10aa7b89c7ae4529108b60b697fe0c	Create Table		\N	2.0.5
1337623084753-76	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.546186+02	76	EXECUTED	3:1b2a6bad533cc36f4dab198377193a91	Create Table		\N	2.0.5
1337623084753-77	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.668271+02	77	EXECUTED	3:27b7b088a6dff1eb5bf7ec497baae017	Create Table		\N	2.0.5
1337623084753-78	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.767159+02	78	EXECUTED	3:48ea020feafff3cf4c69b0817940f55a	Create Table		\N	2.0.5
1337623084753-79	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.833465+02	79	EXECUTED	3:e7a907970aa98a95fa48d026e671b7e8	Create Table		\N	2.0.5
1337623084753-80	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.875891+02	80	EXECUTED	3:26a71c52cf002cc4183488f80544357a	Create Table		\N	2.0.5
1337623084753-81	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:08.943399+02	81	EXECUTED	3:8cf4c000ba69d6c0645d342bc1a2e1f3	Create Table		\N	2.0.5
1337623084753-82	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.012594+02	82	EXECUTED	3:31c53d745c9ae834d3a5982cb0ecc36e	Create Table		\N	2.0.5
1337623084753-83	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.089891+02	83	EXECUTED	3:6ec06a1c6bfa20b890f5daa375b0e1b2	Create Table		\N	2.0.5
1337623084753-84	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.190982+02	84	EXECUTED	3:f283d52bac6269950fc8c5221b9cc6f3	Create Table		\N	2.0.5
1337623084753-85	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.233901+02	85	EXECUTED	3:e757ef4108739dee3752a5d78ac95581	Create Table		\N	2.0.5
1337623084753-86	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.312347+02	86	EXECUTED	3:d3b0ef35e6a199026d2564e5bcf6ab93	Create Table		\N	2.0.5
1337623084753-87	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.379389+02	87	EXECUTED	3:c13b509307c398cc485605151f9eeb5f	Create Table		\N	2.0.5
1337623084753-88	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.455434+02	88	EXECUTED	3:f1cef62b77417dfb5ff0d729867b42f3	Create Table		\N	2.0.5
1337623084753-89	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.533732+02	89	EXECUTED	3:e6b0d4b4ccc87d0d2ea523d9b012389d	Create Table		\N	2.0.5
1337623084753-90	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.624538+02	90	EXECUTED	3:554d41019d495e10bf22121cf84448b2	Create Table		\N	2.0.5
1337623084753-91	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.657336+02	91	EXECUTED	3:340bd59bcf1a21cc62e3e9c03127d9cc	Create Table		\N	2.0.5
1337623084753-92	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.771149+02	92	EXECUTED	3:830d3dc7c0504af3f8b3a82a1a868669	Create Table		\N	2.0.5
1337623084753-93	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.913167+02	93	EXECUTED	3:deb3d2f60cff688a3a5bf976128ee92a	Create Table		\N	2.0.5
1337623084753-94	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:09.980756+02	94	EXECUTED	3:68dfdb546e76551fc5a199986e17288a	Create Table		\N	2.0.5
1337623084753-95	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.069111+02	95	EXECUTED	3:2ee2b68dadc81b63a98c6782029fd840	Create Table		\N	2.0.5
1337623084753-96	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.14791+02	96	EXECUTED	3:701eaf6644200e19c53f6d2035e8ac40	Create Table		\N	2.0.5
1337623084753-97	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.225466+02	97	EXECUTED	3:f35a5ce3237a6daa0d64cb5a43eb978d	Create Table		\N	2.0.5
1337623084753-98	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.314271+02	98	EXECUTED	3:07331681165e6c9700e011738654b39e	Create Table		\N	2.0.5
1337623084753-99	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.392464+02	99	EXECUTED	3:d6937b3b4d0db6660e6b20984d4a879a	Create Table		\N	2.0.5
1337623084753-100	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.482328+02	100	EXECUTED	3:5a04786cdedeaedbd69323c599b8de4c	Create Table		\N	2.0.5
1337623084753-101	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.581855+02	101	EXECUTED	3:563c50f2027e2067b086a631e281dcf1	Create Table		\N	2.0.5
1337623084753-102	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.848458+02	102	EXECUTED	3:740ee4edf3dd254f31365c3744f42472	Create Table		\N	2.0.5
1337623084753-103	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:10.938172+02	103	EXECUTED	3:c6edfaa6aec554700a58d13c21067077	Create Table		\N	2.0.5
1337623084753-104	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.013288+02	104	EXECUTED	3:3c79b35239807af00798ef05239ae6ab	Create Table		\N	2.0.5
1337623084753-105	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.034784+02	105	EXECUTED	3:5654b932bf19149b687bdd8f738e7e11	Create Table		\N	2.0.5
1337623084753-106	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.11632+02	106	EXECUTED	3:f23a3a6a63604d660d26068ade95a61d	Create Table		\N	2.0.5
1337623084753-107	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.248726+02	107	EXECUTED	3:3e06accbf4c7c58a7cc57382ac671c9a	Create Table		\N	2.0.5
1337623084753-108	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.326822+02	108	EXECUTED	3:1bd466f4fbbf9c8ff15063f0bedf2dbb	Create Table		\N	2.0.5
1337623084753-109	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.437589+02	109	EXECUTED	3:c72f3177109bf4d9b91e16ef6287e7c6	Create Table		\N	2.0.5
1337623084753-110	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.516785+02	110	EXECUTED	3:d9747bd8e0615b490e0471414cbe9a4a	Create Table		\N	2.0.5
1337623084753-111	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.6046+02	111	EXECUTED	3:960bf1edbae8d73ab83d262114022b46	Create Table		\N	2.0.5
1337623084753-112	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.681994+02	112	EXECUTED	3:677275f6b064915e186d8a125559bc52	Create Table		\N	2.0.5
1337623084753-113	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.795701+02	113	EXECUTED	3:fa63bae43f3d2a52f2e68974dcdf6735	Create Table		\N	2.0.5
1337623084753-114	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.838298+02	114	EXECUTED	3:61516745ca6c886ddc4e3b487a503c86	Create Table		\N	2.0.5
1337623084753-115	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.869748+02	115	EXECUTED	3:f76b7790e10b96c1fad61703fa959ee2	Create Table		\N	2.0.5
1337623084753-116	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:11.938499+02	116	EXECUTED	3:ec96ad42bc0db24b7cab6a03d3821579	Add Primary Key		\N	2.0.5
1337623084753-117	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.016369+02	117	EXECUTED	3:a7aed4bdbee79d6489881f704d3186fa	Add Primary Key		\N	2.0.5
1337623084753-118	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.083426+02	118	EXECUTED	3:4d1af0f8e4e95747c01460f81b9c6ce9	Add Primary Key		\N	2.0.5
1337623084753-119	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.160208+02	119	EXECUTED	3:bd0613180ba8732a9668d0248c4aa43d	Add Primary Key		\N	2.0.5
1337623084753-120	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.238087+02	120	EXECUTED	3:9a19138dcd59484dfc2f5158b599fc2f	Add Primary Key		\N	2.0.5
1337623084753-121	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.31578+02	121	EXECUTED	3:d6cd77f86ef26e2844225a84e20f7a24	Add Primary Key		\N	2.0.5
1337623084753-122	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.394574+02	122	EXECUTED	3:effaf8269dd3b75d28908a9a40d6017f	Add Unique Constraint		\N	2.0.5
1337623084753-123	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.474776+02	123	EXECUTED	3:a08f5e8b8ddaf1535e8607f1753dd8b8	Add Unique Constraint		\N	2.0.5
1337623084753-124	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.561121+02	124	EXECUTED	3:a4b52a42d1ce658721e2702d180ed638	Add Unique Constraint		\N	2.0.5
1337623084753-125	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.637235+02	125	EXECUTED	3:f7a074ca3d7fe98ffeafccf5d66e8150	Add Unique Constraint		\N	2.0.5
1337623084753-275	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.728565+02	126	EXECUTED	3:994fe113170287422961bc1fb9d45507	Create Index		\N	2.0.5
1337623084753-276	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.816791+02	127	EXECUTED	3:11acac1e05cef75c5e907544ea1baacd	Create Index		\N	2.0.5
1337623084753-277	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.895372+02	128	EXECUTED	3:fdb78a392b0b104f8cb8d70fdb3a19c0	Create Index		\N	2.0.5
1337623084753-278	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:12.98397+02	129	EXECUTED	3:e305907bc0b4c1d35866d33840269052	Create Index		\N	2.0.5
1337623084753-279	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.073358+02	130	EXECUTED	3:9663b0e86f9a6d55df40b5b54eb642d2	Create Index		\N	2.0.5
1337623084753-280	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.161525+02	131	EXECUTED	3:b9cf7c8e7cf46e7b535a4f53a9436747	Create Index		\N	2.0.5
1337623084753-281	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.239239+02	132	EXECUTED	3:62dcefdaf0bed060812fe52b7e50b83a	Create Index		\N	2.0.5
1337623084753-282	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.306225+02	133	EXECUTED	3:db04e689be7296af4ad06cccf028b8c8	Create Index		\N	2.0.5
1337623084753-283	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.382222+02	134	EXECUTED	3:8bbfaf351cf5f3c4b603b03f91e9f276	Create Index		\N	2.0.5
1337623084753-284	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.461467+02	135	EXECUTED	3:5b7a28747a3669f600b59ac295780c23	Create Index		\N	2.0.5
1337623084753-285	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.526953+02	136	EXECUTED	3:d1ad1d09cb2e9e15ecd0446d6a3d6634	Create Index		\N	2.0.5
1337623084753-286	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.594734+02	137	EXECUTED	3:7a614fe0d45db7c2fbc40e8f21e92c09	Create Index		\N	2.0.5
1337623084753-287	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.672889+02	138	EXECUTED	3:30568c1277b72661cef1a9c9f80ea584	Create Index		\N	2.0.5
1337623084753-288	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.739862+02	139	EXECUTED	3:1f5c5c62cc17ee05830ddac3ab75b99e	Create Index		\N	2.0.5
1337623084753-289	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.816104+02	140	EXECUTED	3:c835a6cdd01d4a8dec41c41bc74f0154	Create Index		\N	2.0.5
1337623084753-290	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.894937+02	141	EXECUTED	3:c1862737100ef9e2bd3c5430e20e7fcd	Create Index		\N	2.0.5
1337623084753-291	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:13.960219+02	142	EXECUTED	3:12e28798e75fafcf467fd4d20e163784	Create Index		\N	2.0.5
1337623084753-292	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.039209+02	143	EXECUTED	3:f1a6eb1379add9e57cef1ee542606bf2	Create Index		\N	2.0.5
1337623084753-293	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.118679+02	144	EXECUTED	3:bdf18b2d1fea493dc90b6973056ad75b	Create Index		\N	2.0.5
1337623084753-294	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.204506+02	145	EXECUTED	3:61e9ba92bafa79920fdea08eaed87beb	Create Index		\N	2.0.5
1337623084753-295	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.283586+02	146	EXECUTED	3:4d65a812b5ac84dbfadd4f14c997b7a6	Create Index		\N	2.0.5
1337623084753-296	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.363973+02	147	EXECUTED	3:4730935dc2814d92df28dee2d0196550	Create Index		\N	2.0.5
1337623084753-297	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.450933+02	148	EXECUTED	3:54b654d2f867524384d138159f9d92ba	Create Index		\N	2.0.5
1337623084753-298	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.515165+02	149	EXECUTED	3:df729a792dda503f3c2c45c84c824a5b	Create Index		\N	2.0.5
1337623084753-299	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.582807+02	150	EXECUTED	3:fa73f52b1cc7c2ee5d057ec9c290e275	Create Index		\N	2.0.5
1337623084753-300	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.650842+02	151	EXECUTED	3:5fe4c4b54f5453ff70e8e80233850e51	Create Index		\N	2.0.5
1337623084753-301	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.728168+02	152	EXECUTED	3:1f2ddf2a617233b6566483b0a71266ce	Create Index		\N	2.0.5
1337623084753-302	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.828234+02	153	EXECUTED	3:975a0e98177c1f0427833dfa38e858fd	Create Index		\N	2.0.5
1337623084753-303	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.909601+02	154	EXECUTED	3:4ef5b799d8732781480c2b6a563e0032	Create Index		\N	2.0.5
1337623084753-304	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:14.98426+02	155	EXECUTED	3:ed0bfac0ead896882264ecc0287d3fc1	Create Index		\N	2.0.5
1337623084753-305	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.050696+02	156	EXECUTED	3:5ea836c4c9019198c3e670d240fa2c2b	Create Index		\N	2.0.5
1337623084753-306	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.118076+02	157	EXECUTED	3:70beb8a34d580537faf7cb748fb7a4c6	Create Index		\N	2.0.5
1337623084753-307	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.194304+02	158	EXECUTED	3:4743df91243f658c93b90bb4718176a1	Create Index		\N	2.0.5
1337623084753-308	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.273111+02	159	EXECUTED	3:20b4a6ede2ec61fa259fb3e14b9a0970	Create Index		\N	2.0.5
1337623084753-309	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.338887+02	160	EXECUTED	3:1be024bfad7dee8650b2732273d86cf0	Create Index		\N	2.0.5
1337623084753-310	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.417862+02	161	EXECUTED	3:f0e272d07b8f70dfdb39b0127a91dbfb	Create Index		\N	2.0.5
1337623084753-311	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.4943+02	162	EXECUTED	3:7b52b669e7adcd55dfcfdb4e16a1ac82	Create Index		\N	2.0.5
1337623084753-312	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.573623+02	163	EXECUTED	3:44584a64b6591ed0c30bfe5491b0d8e4	Create Index		\N	2.0.5
1337623084753-313	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.652384+02	164	EXECUTED	3:c84989904149c6b7bdc90868fef123e3	Create Index		\N	2.0.5
1337623084753-314	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.740408+02	165	EXECUTED	3:6c3479f39419bba2cfcd6b2243d468fc	Create Index		\N	2.0.5
1337623084753-315	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.839755+02	166	EXECUTED	3:b6e9972ebe555c99d13849f2699d3223	Create Index		\N	2.0.5
1337623084753-316	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:15.918278+02	167	EXECUTED	3:211b3e0749302cd31d23329bb3ab2601	Create Index		\N	2.0.5
1337623084753-317	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:16.007438+02	168	EXECUTED	3:1c5e0aa133911f3a6c25a776f4450d92	Create Index		\N	2.0.5
1337623084753-318	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:16.094984+02	169	EXECUTED	3:84c120a531df046bd8acb44032bf3bd9	Create Table		\N	2.0.5
1337972683141-1	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:27.14498+02	170	EXECUTED	3:a8fe0d7a4810fcbbc55b9483cc576a67	Insert Row (x3)		\N	2.0.5
1337972683141-2	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:27.216008+02	171	EXECUTED	3:0c0bce21aa0f8522d834b5c9e2ada6b1	Insert Row (x4)		\N	2.0.5
1337972683141-3	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:27.260387+02	172	EXECUTED	3:b81fd56f0d90e5c63ee3a61d75ec6ad8	Insert Row		\N	2.0.5
1337972683141-4	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:28.059339+02	173	EXECUTED	3:44601e8a75747cea4c8fa54b933cefa3	Insert Row (x96)		\N	2.0.5
1337972683141-5	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:28.212624+02	174	EXECUTED	3:e5aa7e078527beaa455af114e17e601c	Insert Row (x4)		\N	2.0.5
1337972683141-6	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:28.800915+02	175	EXECUTED	3:632731df70af1e93a7e00d13088e436e	Insert Row (x136)		\N	2.0.5
1337972683141-7	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:28.945141+02	176	EXECUTED	3:0476af908663535d9ff61956506d0992	Insert Row (x12)		\N	2.0.5
1337972683141-8	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:29.035505+02	177	EXECUTED	3:3bd6a854e739af711cbfda0d81ef9d32	Insert Row (x7)		\N	2.0.5
1337972683141-9	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:29.176282+02	178	EXECUTED	3:64548c0f50aa0ebcd2c19dc26b5cac66	Insert Row (x24)		\N	2.0.5
1337972683141-10	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:29.222178+02	179	EXECUTED	3:5a6dcf17687a990e080a10004219c161	Insert Row (x4)		\N	2.0.5
1337972683141-11	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:29.569797+02	180	EXECUTED	3:8216dc7570e0e0ead77be60c3b379313	Insert Row (x92)		\N	2.0.5
1337972683141-12	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:29.634993+02	181	EXECUTED	3:5d757f4f946900667ef7c3e933a8b3b6	Insert Row (x2)		\N	2.0.5
1337972683141-13	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:29.678742+02	182	EXECUTED	3:5ecb19369abc453c9f313fad045584b8	Insert Row (x2)		\N	2.0.5
1337972683141-14	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:30.445149+02	183	EXECUTED	3:0674523b69860e045bdc999038247f87	Insert Row (x237)		\N	2.0.5
1337972683141-15	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:30.510714+02	184	EXECUTED	3:41080d0ee6377d5b3e39c35975063c72	Insert Row (x4)		\N	2.0.5
1337972683141-16	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:30.572507+02	185	EXECUTED	3:c6de6582f9666e851acd2c665a7f75eb	Insert Row (x11)		\N	2.0.5
1337972683141-17	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:30.81447+02	186	EXECUTED	3:0a357b23ac41451aa9dd819c9bd925ea	Insert Row (x64)		\N	2.0.5
1337972683141-18	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:30.914508+02	187	EXECUTED	3:6ef118c7f7daf505a34f29ea55020ab0	Insert Row (x12)		\N	2.0.5
1337972683141-19	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:30.969897+02	188	EXECUTED	3:ab61c6136374f8e5adfa5180b523f71e	Insert Row (x6)		\N	2.0.5
1337972683141-20	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.083403+02	189	EXECUTED	3:010a452736e89ec382875d23705a5f0d	Insert Row (x34)		\N	2.0.5
1337972683141-21	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.392233+02	190	EXECUTED	3:dc80d16e778a70dec420fe9a5a481ec9	Insert Row (x88)		\N	2.0.5
1337972683141-22	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.482587+02	191	EXECUTED	3:7ac8f12a07c740b0ee3aff5cd1e2e463	Insert Row (x9)		\N	2.0.5
1337972683141-23	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.520876+02	192	EXECUTED	3:337914241aa740552edef0a928809808	Insert Row (x3)		\N	2.0.5
1337972683141-24	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.547565+02	193	EXECUTED	3:5601ed88c59920dd7066642e0d0c4f62	Insert Row		\N	2.0.5
1337972683141-25	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.589127+02	194	EXECUTED	3:3b07bce826f6643ecef6f07fc125678b	Insert Row (x4)		\N	2.0.5
1337972683141-26	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.675274+02	195	EXECUTED	3:647469db1cb3e020b738d2b32bf9334f	Insert Row (x21)		\N	2.0.5
1337972683141-27	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.818909+02	196	EXECUTED	3:38418b0137d9c1fa7206f8d813ad63d2	Insert Row (x45)		\N	2.0.5
1337972683141-28	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:31.995627+02	197	EXECUTED	3:9464d6112f741d8d783b0ec038b94be6	Insert Row (x10)		\N	2.0.5
1337972683141-29	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:32.091843+02	198	EXECUTED	3:ec8fd6367fa13f67236ef1aac6946e97	Insert Row (x15)		\N	2.0.5
1337972683141-30	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:32.195934+02	199	EXECUTED	3:5f3b64c893d145f239d91b4df979badc	Insert Row (x20)		\N	2.0.5
1337972683141-31	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:32.320298+02	200	EXECUTED	3:e781d13a841f2894c40cb78705d52b0b	Insert Row (x35)		\N	2.0.5
1337972683141-32	aristokrates (generated)	descriptors/database/jbilling-init_data.xml	2012-07-11 13:54:35.651473+02	201	EXECUTED	3:fbd64c2395e2fad0067fdd2d799dd6fb	Insert Row (x1109)		\N	2.0.5
1337623084753-126	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.315698+02	202	EXECUTED	3:1fd8d77ec2f6825ef34e72f3f18d98ec	Add Foreign Key Constraint		\N	2.0.5
1337623084753-127	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.391249+02	203	EXECUTED	3:e627b1462ec9fa48fe8c94a7158f9426	Add Foreign Key Constraint		\N	2.0.5
1337623084753-128	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.430132+02	204	EXECUTED	3:7b73f7f334c1c17af0a01f0140349a09	Add Foreign Key Constraint		\N	2.0.5
1337623084753-129	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.480356+02	205	EXECUTED	3:3b7db9646e41a1bc0b86e364ad427119	Add Foreign Key Constraint		\N	2.0.5
1337623084753-130	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.530007+02	206	EXECUTED	3:cbdd59b0175b5578ee710444e5b232f5	Add Foreign Key Constraint		\N	2.0.5
1337623084753-131	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.574851+02	207	EXECUTED	3:d5d593a11353d3d98e54c5c4dabd0a05	Add Foreign Key Constraint		\N	2.0.5
1337623084753-132	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.618934+02	208	EXECUTED	3:98b468d44e6fb46ca0e4680b96e8f3b1	Add Foreign Key Constraint		\N	2.0.5
1337623084753-133	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.663214+02	209	EXECUTED	3:5cee909a5d7ebb600f5024b1303efa8d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-134	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.705477+02	210	EXECUTED	3:c82e85c0c63e54e3a4426e617a80075a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-135	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.743738+02	211	EXECUTED	3:1b60ee813784e281e3e35b86ab09c0a8	Add Foreign Key Constraint		\N	2.0.5
1337623084753-136	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.7856+02	212	EXECUTED	3:cae1e7104ce183c7d839340f6222541d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-137	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.913145+02	213	EXECUTED	3:e79884389150c9fab7fb64618a350b1e	Add Foreign Key Constraint		\N	2.0.5
1337623084753-138	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:40.963455+02	214	EXECUTED	3:a58175b0dac5de2550bae9d0a528ed06	Add Foreign Key Constraint		\N	2.0.5
1337623084753-139	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.007647+02	215	EXECUTED	3:543abf849ceb37f4eaf06688bfecea2c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-140	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.052553+02	216	EXECUTED	3:decf7af7842f406a03d0cf048f4f4ae8	Add Foreign Key Constraint		\N	2.0.5
1337623084753-141	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.096232+02	217	EXECUTED	3:9a710b784ab36b8968cdbfaebf9e975e	Add Foreign Key Constraint		\N	2.0.5
1337623084753-142	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.139917+02	218	EXECUTED	3:4a7daa87875b4dd0d57f511229b60952	Add Foreign Key Constraint		\N	2.0.5
1337623084753-143	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.18777+02	219	EXECUTED	3:f9aa8011b7dce8b332d84739bbcc99c8	Add Foreign Key Constraint		\N	2.0.5
1337623084753-144	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.229312+02	220	EXECUTED	3:910a5a6aee89ebe7a733a06cf1142867	Add Foreign Key Constraint		\N	2.0.5
1337623084753-145	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.263608+02	221	EXECUTED	3:2de0a0ed919d7938ea216713312e543a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-146	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.305261+02	222	EXECUTED	3:096429fa1de682a99d89768798b5afbd	Add Foreign Key Constraint		\N	2.0.5
1337623084753-147	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.341744+02	223	EXECUTED	3:44f94241fda71bea2ede7f0038b15a21	Add Foreign Key Constraint		\N	2.0.5
1337623084753-148	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.386354+02	224	EXECUTED	3:6a71640663c7790b5feabf2b0847b2af	Add Foreign Key Constraint		\N	2.0.5
1337623084753-149	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.430143+02	225	EXECUTED	3:d7c1311626b441f3da9ff6d6aa330e98	Add Foreign Key Constraint		\N	2.0.5
1337623084753-150	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.464616+02	226	EXECUTED	3:bd78366bc6ad1381a6f85c21c40dadcd	Add Foreign Key Constraint		\N	2.0.5
1337623084753-151	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.510105+02	227	EXECUTED	3:e446816be71a36ba499aef40389bb881	Add Foreign Key Constraint		\N	2.0.5
1337623084753-152	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.55333+02	228	EXECUTED	3:004fc6f9837b5251cafc763508aa75bf	Add Foreign Key Constraint		\N	2.0.5
1337623084753-153	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.596996+02	229	EXECUTED	3:d88c56e58f32801a4d33744da4bc112a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-154	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.64116+02	230	EXECUTED	3:e8daeae151893ec6573b73f2245a0dc7	Add Foreign Key Constraint		\N	2.0.5
1337623084753-155	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.676483+02	231	EXECUTED	3:e1c182342bfd7a50c593f04f3e5c9b1d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-156	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.718918+02	232	EXECUTED	3:a3caffb1ed8faed2ab394ec5370e0be9	Add Foreign Key Constraint		\N	2.0.5
1337623084753-157	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.753316+02	233	EXECUTED	3:2a5da6a2366e95b82a631837358685d2	Add Foreign Key Constraint		\N	2.0.5
1337623084753-158	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.79497+02	234	EXECUTED	3:135f19234cc9b2033a6a58bfaa738ebf	Add Foreign Key Constraint		\N	2.0.5
1337623084753-159	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.830759+02	235	EXECUTED	3:4c4c0d3bad01f418857f2239c148f8bf	Add Foreign Key Constraint		\N	2.0.5
1337623084753-160	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.866529+02	236	EXECUTED	3:161d162722f89c6b8990baa2b866b101	Add Foreign Key Constraint		\N	2.0.5
1337623084753-161	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.910612+02	237	EXECUTED	3:050dc222ebf928eb4ca382153058fc7a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-162	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.953952+02	238	EXECUTED	3:0726716c5e83e11bf7536f3777da9912	Add Foreign Key Constraint		\N	2.0.5
1337623084753-163	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:41.9886+02	239	EXECUTED	3:6ceb03f062b15f090c89c318c0a0446a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-164	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.032458+02	240	EXECUTED	3:8cb416a3198908287072b1982578f10c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-165	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.065975+02	241	EXECUTED	3:86d5544e78d2cfc0d48b65bf500def7b	Add Foreign Key Constraint		\N	2.0.5
1337623084753-166	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.097535+02	242	EXECUTED	3:7f92b7a790f11b27cfa9a5bc6ea3133c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-167	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.130475+02	243	EXECUTED	3:492587d13bd9f4128f7c71365c4e515f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-168	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.165914+02	244	EXECUTED	3:e624245340fe5cb417f30ff27633838e	Add Foreign Key Constraint		\N	2.0.5
1337623084753-169	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.247038+02	245	EXECUTED	3:bdf9c4e8c6101b08177c09a8467dec56	Add Foreign Key Constraint		\N	2.0.5
1337623084753-170	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.28876+02	246	EXECUTED	3:1a4c196c15668472cccd5705be789361	Add Foreign Key Constraint		\N	2.0.5
1337623084753-171	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.331878+02	247	EXECUTED	3:35e1d8538ae56373d2cbcc82a469ed7c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-172	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.365501+02	248	EXECUTED	3:f902167c3f74930c28cd3a46fdc26ce9	Add Foreign Key Constraint		\N	2.0.5
1337623084753-173	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.398268+02	249	EXECUTED	3:570e2fcfd4652630cb391ab947639e6f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-174	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.430922+02	250	EXECUTED	3:28ac864e459c3bcb00fdcb07bbbb23e0	Add Foreign Key Constraint		\N	2.0.5
1337623084753-175	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.46992+02	251	EXECUTED	3:e6609810566120d076d2f86784fb4af4	Add Foreign Key Constraint		\N	2.0.5
1337623084753-176	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.511853+02	252	EXECUTED	3:c565a9d0b9197392f6b8864e26b993cb	Add Foreign Key Constraint		\N	2.0.5
1337623084753-177	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.55573+02	253	EXECUTED	3:3ab0490358d6af9ad4c684900c4333ac	Add Foreign Key Constraint		\N	2.0.5
1337623084753-178	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.598412+02	254	EXECUTED	3:e63f0cde29ece99571b7ea1c27824fee	Add Foreign Key Constraint		\N	2.0.5
1337623084753-179	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.623014+02	255	EXECUTED	3:030b915d7d6e43657c7c5bcab5fd23fc	Add Foreign Key Constraint		\N	2.0.5
1337623084753-180	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.664941+02	256	EXECUTED	3:5ead26f7fd7fb93840f40e5fc04f0046	Add Foreign Key Constraint		\N	2.0.5
1337623084753-181	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.700059+02	257	EXECUTED	3:b3a54a7f8a57429442ee9554ee78e283	Add Foreign Key Constraint		\N	2.0.5
1337623084753-182	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.734182+02	258	EXECUTED	3:fbe6c5447b7b3a309d81810b512d368b	Add Foreign Key Constraint		\N	2.0.5
1337623084753-183	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.778631+02	259	EXECUTED	3:1dc93b214360cf43ea478ff14248c037	Add Foreign Key Constraint		\N	2.0.5
1337623084753-184	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.823487+02	260	EXECUTED	3:d15085f0a3c6203ddfdd34f80f58c82c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-185	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.865771+02	261	EXECUTED	3:cb7deebd5bdbba8c69a982b4999c62a3	Add Foreign Key Constraint		\N	2.0.5
1337623084753-186	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.901304+02	262	EXECUTED	3:0a0b85871c59b51259e41147a0ba648c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-187	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.945089+02	263	EXECUTED	3:764682401f5b7917f367818b64bd6626	Add Foreign Key Constraint		\N	2.0.5
1337623084753-188	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:42.989498+02	264	EXECUTED	3:cd36ced5e84289f21ba26e63eafe5059	Add Foreign Key Constraint		\N	2.0.5
1337623084753-189	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.023843+02	265	EXECUTED	3:83e4350dde7c24eea6f1395e69f625b1	Add Foreign Key Constraint		\N	2.0.5
1337623084753-190	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.066656+02	266	EXECUTED	3:9bed5f03bfcfede738854277c9819c18	Add Foreign Key Constraint		\N	2.0.5
1337623084753-191	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.101592+02	267	EXECUTED	3:a7a300725dc271da1641e00180ef5464	Add Foreign Key Constraint		\N	2.0.5
1337623084753-192	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.158584+02	268	EXECUTED	3:ea3c2b75d475ddd44a54bc86e471bd80	Add Foreign Key Constraint		\N	2.0.5
1337623084753-193	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.201578+02	269	EXECUTED	3:44eee1ed52c6163b416f7d0ff867ddd5	Add Foreign Key Constraint		\N	2.0.5
1337623084753-194	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.243155+02	270	EXECUTED	3:e98d694b52784429aaa508e3a4aa7764	Add Foreign Key Constraint		\N	2.0.5
1337623084753-195	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.281817+02	271	EXECUTED	3:85f741a2cef2736be49cdd13d194c7eb	Add Foreign Key Constraint		\N	2.0.5
1337623084753-196	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.322137+02	272	EXECUTED	3:9bde0cca6e27790d013ce5a5e5bbea86	Add Foreign Key Constraint		\N	2.0.5
1337623084753-197	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.36111+02	273	EXECUTED	3:1a92920d0701c915fd24e503b74c5087	Add Foreign Key Constraint		\N	2.0.5
1337623084753-198	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.40145+02	274	EXECUTED	3:08f23151872723ca01d5d5de9caaccb2	Add Foreign Key Constraint		\N	2.0.5
1337623084753-199	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.434421+02	275	EXECUTED	3:78d470bd72d9284784f51f5dbc4b4495	Add Foreign Key Constraint		\N	2.0.5
1337623084753-200	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.468458+02	276	EXECUTED	3:4ba2153ced6076ce2fa8bba8b220ad7c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-201	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.501994+02	277	EXECUTED	3:8c580381298f27bdb21ae42dc2cb48d7	Add Foreign Key Constraint		\N	2.0.5
1337623084753-202	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.534242+02	278	EXECUTED	3:c8dcd666f2b3917f7e4938d734e45dd3	Add Foreign Key Constraint		\N	2.0.5
1337623084753-203	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.568893+02	279	EXECUTED	3:0b61ad2c1ff7b2c8d12e26b84526bb9a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-204	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.612807+02	280	EXECUTED	3:ee2a1a0a8bc10789dd77eb88bf20d308	Add Foreign Key Constraint		\N	2.0.5
1337623084753-205	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.647409+02	281	EXECUTED	3:ed428d3275f25dce236f5704a1cf3229	Add Foreign Key Constraint		\N	2.0.5
1337623084753-206	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.691983+02	282	EXECUTED	3:4d9e259484d9e573edbf4030dc0d9109	Add Foreign Key Constraint		\N	2.0.5
1337623084753-207	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.735283+02	283	EXECUTED	3:c6e09b7ac266cdb2b9adcc23dcaa535f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-208	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.772656+02	284	EXECUTED	3:9415bf87ccfc3af9e9493282efb4068c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-209	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.813312+02	285	EXECUTED	3:39709014ac5dbdb5222b6c2ee07799f5	Add Foreign Key Constraint		\N	2.0.5
1337623084753-210	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.847167+02	286	EXECUTED	3:840cd3eeaec02c8dd80ac7f1873e3e04	Add Foreign Key Constraint		\N	2.0.5
1337623084753-211	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.89123+02	287	EXECUTED	3:54a22c47cc50edd7361ee4a005865e37	Add Foreign Key Constraint		\N	2.0.5
1337623084753-212	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.925276+02	288	EXECUTED	3:5ebe3efb54b8eeb787b006c302e24674	Add Foreign Key Constraint		\N	2.0.5
1337623084753-213	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:43.969678+02	289	EXECUTED	3:0f2f525220dd01b0fc2a1b74d80284fa	Add Foreign Key Constraint		\N	2.0.5
1337623084753-214	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.013816+02	290	EXECUTED	3:d318ad0cd4221e168cfb32c2b480b665	Add Foreign Key Constraint		\N	2.0.5
1337623084753-215	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.044913+02	291	EXECUTED	3:dacdc3cc5f70e0cfd805f23f313f34a7	Add Foreign Key Constraint		\N	2.0.5
1337623084753-216	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.082522+02	292	EXECUTED	3:7e5566994dfd52b05b9a06873571694a	Add Foreign Key Constraint		\N	2.0.5
1337623084753-217	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.12649+02	293	EXECUTED	3:5d8170930a4e26019510bafad08f9331	Add Foreign Key Constraint		\N	2.0.5
1337623084753-218	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.169615+02	294	EXECUTED	3:ea62b586f139fa02dfccf2e5bed2d7bd	Add Foreign Key Constraint		\N	2.0.5
1337623084753-219	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.203595+02	295	EXECUTED	3:c34a1780ea2409952e266e40fcc206b1	Add Foreign Key Constraint		\N	2.0.5
1337623084753-220	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.25172+02	296	EXECUTED	3:81c979a8416e0dffd91c07e8cf06e40b	Add Foreign Key Constraint		\N	2.0.5
1337623084753-221	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.291878+02	297	EXECUTED	3:4d9f5bd3afad0bdd70e04308dc6618f2	Add Foreign Key Constraint		\N	2.0.5
1337623084753-222	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.325685+02	298	EXECUTED	3:2a2a2c3619b7e657a0e95fa60307c3a4	Add Foreign Key Constraint		\N	2.0.5
1337623084753-223	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.367547+02	299	EXECUTED	3:c661b7f690c0f8c45ff7a554d273c63c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-224	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.402538+02	300	EXECUTED	3:68613117ece2749d057e684fef5bd86b	Add Foreign Key Constraint		\N	2.0.5
1337623084753-225	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.43505+02	301	EXECUTED	3:9f635e8d29d524b24bedbbbb91a701a4	Add Foreign Key Constraint		\N	2.0.5
1337623084753-226	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.470422+02	302	EXECUTED	3:0679d13da00ada746d101238fabc82aa	Add Foreign Key Constraint		\N	2.0.5
1337623084753-227	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.50357+02	303	EXECUTED	3:0e3f4af38df3c9b09041ca204c499a56	Add Foreign Key Constraint		\N	2.0.5
1337623084753-228	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.547574+02	304	EXECUTED	3:9284c08e80c9db977b0d4a0a4cbc0ffa	Add Foreign Key Constraint		\N	2.0.5
1337623084753-229	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.582238+02	305	EXECUTED	3:f46a8c3d4957f44e0792b138d576edc3	Add Foreign Key Constraint		\N	2.0.5
1337623084753-230	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.623728+02	306	EXECUTED	3:92cedad2d06640d133d66de9e80dd1d6	Add Foreign Key Constraint		\N	2.0.5
1337623084753-231	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.661584+02	307	EXECUTED	3:d0b912f627117bb71fe4db82fb728fa9	Add Foreign Key Constraint		\N	2.0.5
1337623084753-232	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.704929+02	308	EXECUTED	3:73fc7150111612f7bed856e5ea42f5bf	Add Foreign Key Constraint		\N	2.0.5
1337623084753-233	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.747713+02	309	EXECUTED	3:e7e3b27d39fbe39811eeba0eee30fb8b	Add Foreign Key Constraint		\N	2.0.5
1337623084753-234	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.783631+02	310	EXECUTED	3:2927bea90b3f1dcc5c2176339342334d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-235	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.827386+02	311	EXECUTED	3:eee7b004987bd34a1482440cd87f3565	Add Foreign Key Constraint		\N	2.0.5
1337623084753-236	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.871961+02	312	EXECUTED	3:310ea183e21f67f3c4aac295060157b3	Add Foreign Key Constraint		\N	2.0.5
1337623084753-237	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.929227+02	313	EXECUTED	3:0b2618c9bdbd6d2ec2c6d5620e35be08	Add Foreign Key Constraint		\N	2.0.5
1337623084753-238	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:44.973776+02	314	EXECUTED	3:512a979c51dbee1bc7c34f8d9e795f6f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-239	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.016096+02	315	EXECUTED	3:1a37f520216d8c9bda1243d8c1b74f13	Add Foreign Key Constraint		\N	2.0.5
1337623084753-240	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.061113+02	316	EXECUTED	3:059c67efe046550f100a8d32e340454d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-241	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.115234+02	317	EXECUTED	3:dd28b8f0e571ba5a9776c30472da5cac	Add Foreign Key Constraint		\N	2.0.5
1337623084753-242	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.150131+02	318	EXECUTED	3:9f3fba80779d29bee1ae4f4e04c646e1	Add Foreign Key Constraint		\N	2.0.5
1337623084753-243	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.199845+02	319	EXECUTED	3:7026eaa4a49be9f1f37bbdbae3f9897c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-244	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.241629+02	320	EXECUTED	3:8fc01e9957eba47a34666897ebb69c22	Add Foreign Key Constraint		\N	2.0.5
1337623084753-245	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.284106+02	321	EXECUTED	3:0ab0c68efb6ad73dc953db3d0b00aac0	Add Foreign Key Constraint		\N	2.0.5
1337623084753-246	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.325135+02	322	EXECUTED	3:a6880991a7776b9a6010e796310ac73c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-247	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.360797+02	323	EXECUTED	3:24580bcb6180c4b853a6029c9263136f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-248	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.404964+02	324	EXECUTED	3:baaf9634dd263a986ec94b1aa56b13a2	Add Foreign Key Constraint		\N	2.0.5
1337623084753-249	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.439176+02	325	EXECUTED	3:fa674185e55a6b04ca95ce7ddb1fb530	Add Foreign Key Constraint		\N	2.0.5
1337623084753-250	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.486916+02	326	EXECUTED	3:2cea43cf8f22d2f819a7f481259a0e3f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-251	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.527581+02	327	EXECUTED	3:b11e28e8207e971667b616f42c49aa9d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-252	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.568793+02	328	EXECUTED	3:b00cf35b627e45614f6371fba9dfb3cb	Add Foreign Key Constraint		\N	2.0.5
1337623084753-253	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.610517+02	329	EXECUTED	3:5b782dad0c274b819c8864a840382a38	Add Foreign Key Constraint		\N	2.0.5
1337623084753-254	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.64916+02	330	EXECUTED	3:a34eba6dba4aab6d181c0992ee4c933c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-255	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.683426+02	331	EXECUTED	3:7a9d6312c45ba076c6bb311dbd2bef49	Add Foreign Key Constraint		\N	2.0.5
1337623084753-256	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.720392+02	332	EXECUTED	3:27a403b325d0af62eff968f16d30eba9	Add Foreign Key Constraint		\N	2.0.5
1337623084753-257	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.759136+02	333	EXECUTED	3:3d2e530ae6a5eaa0a3a4716d3091d28d	Add Foreign Key Constraint		\N	2.0.5
1337623084753-258	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.794264+02	334	EXECUTED	3:63c7f35602adbfc1752615559e2f4f3e	Add Foreign Key Constraint		\N	2.0.5
1337623084753-259	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.95507+02	335	EXECUTED	3:959747f53479e966d0895b452ea45557	Add Foreign Key Constraint		\N	2.0.5
1337623084753-260	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:45.996331+02	336	EXECUTED	3:6c3d869afc5111db3ed791a1212c3d1f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-261	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.037194+02	337	EXECUTED	3:1219e598fbb3e7d525c8f10d133b7ee7	Add Foreign Key Constraint		\N	2.0.5
1337623084753-262	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.070265+02	338	EXECUTED	3:3e071fce532fdb875f74d5ba9ea4313c	Add Foreign Key Constraint		\N	2.0.5
1337623084753-263	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.106643+02	339	EXECUTED	3:1249eceb50835e41762801dac787b421	Add Foreign Key Constraint		\N	2.0.5
1337623084753-264	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.150591+02	340	EXECUTED	3:ebdda1c11513a970d3ce866976c6d05e	Add Foreign Key Constraint		\N	2.0.5
1337623084753-265	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.184371+02	341	EXECUTED	3:2291152213c4a893b5020c10421d22aa	Add Foreign Key Constraint		\N	2.0.5
1337623084753-266	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.227097+02	342	EXECUTED	3:787f1e373df0b4018cc6bd0c375ecb6f	Add Foreign Key Constraint		\N	2.0.5
1337623084753-267	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.273727+02	343	EXECUTED	3:1855fa9484c44f3e8fbbadc27c098a71	Add Foreign Key Constraint		\N	2.0.5
1337623084753-268	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.320726+02	344	EXECUTED	3:99328f7bef410a4687af95a915281273	Add Foreign Key Constraint		\N	2.0.5
1337623084753-269	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.359652+02	345	EXECUTED	3:579d8a524213a381da27657bd93c88ef	Add Foreign Key Constraint		\N	2.0.5
1337623084753-270	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.394645+02	346	EXECUTED	3:f16cd236de594a70876866a74ec2c2f0	Add Foreign Key Constraint		\N	2.0.5
1337623084753-271	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.428381+02	347	EXECUTED	3:c216bc01c8c1ee5f155a9bd84d929752	Add Foreign Key Constraint		\N	2.0.5
1337623084753-272	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.463198+02	348	EXECUTED	3:47c99643f6aae0e22e78fd7d614a6a23	Add Foreign Key Constraint		\N	2.0.5
1337623084753-273	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.505678+02	349	EXECUTED	3:69bc455cccc553d3254f902f41c6d7bc	Add Foreign Key Constraint		\N	2.0.5
1337623084753-274	Emiliano Conde	descriptors/database/jbilling-schema.xml	2012-07-11 13:54:46.539179+02	350	EXECUTED	3:fbcc167b561a87182ebd55db3b541eca	Add Foreign Key Constraint		\N	2.0.5
20120608-#2725-notification-category	Vikas Bodani	descriptors/database/jbilling-upgrade-3.1.xml	2012-07-11 13:54:48.996434+02	351	EXECUTED	3:7c8427517b405966bb33a71180c538c7	Insert Row		\N	2.0.5
20120530-#2825-Fix-percentage-products-in-Plans	Juan Vidal	descriptors/database/jbilling-upgrade-3.1.xml	2012-07-11 13:54:49.093179+02	352	EXECUTED	3:6e46ed99c604b7c4fe0002e0496b2007	Insert Row (x2)		\N	2.0.5
\.


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- Data for Name: entity; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY entity (id, external_id, description, create_datetime, language_id, currency_id, optlock) FROM stdin;
\.


--
-- Data for Name: entity_delivery_method_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY entity_delivery_method_map (method_id, entity_id) FROM stdin;
\.


--
-- Data for Name: entity_payment_method_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY entity_payment_method_map (entity_id, payment_method_id) FROM stdin;
\.


--
-- Data for Name: entity_report_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY entity_report_map (report_id, entity_id) FROM stdin;
\.


--
-- Data for Name: enumeration; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY enumeration (id, entity_id, name, optlock) FROM stdin;
\.


--
-- Data for Name: enumeration_values; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY enumeration_values (id, enumeration_id, value, optlock) FROM stdin;
\.


--
-- Data for Name: event_log; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY event_log (id, entity_id, user_id, table_id, foreign_id, create_datetime, level_field, module_id, message_id, old_num, old_str, old_date, optlock, affected_user_id) FROM stdin;
\.


--
-- Data for Name: event_log_message; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY event_log_message (id) FROM stdin;
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
\.


--
-- Data for Name: event_log_module; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY event_log_module (id) FROM stdin;
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
\.


--
-- Data for Name: filter; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY filter (id, filter_set_id, type, constraint_type, field, template, visible, integer_value, string_value, start_date_value, end_date_value, version, boolean_value, decimal_value, decimal_high_value) FROM stdin;
\.


--
-- Data for Name: filter_set; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY filter_set (id, name, user_id, version) FROM stdin;
\.


--
-- Data for Name: filter_set_filter; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY filter_set_filter (filter_set_filters_id, filter_id) FROM stdin;
\.


--
-- Data for Name: generic_status; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY generic_status (id, dtype, status_value, can_login) FROM stdin;
1	user_status	1	1
2	user_status	2	1
3	user_status	3	1
4	user_status	4	1
5	user_status	5	0
6	user_status	6	0
7	user_status	7	0
8	user_status	8	0
9	subscriber_status	1	\N
10	subscriber_status	2	\N
11	subscriber_status	3	\N
12	subscriber_status	4	\N
13	subscriber_status	5	\N
14	subscriber_status	6	\N
15	subscriber_status	7	\N
16	order_status	1	\N
17	order_status	2	\N
18	order_status	3	\N
19	order_status	4	\N
20	order_line_provisioning_status	1	\N
21	order_line_provisioning_status	2	\N
22	order_line_provisioning_status	3	\N
23	order_line_provisioning_status	4	\N
24	order_line_provisioning_status	5	\N
25	order_line_provisioning_status	6	\N
26	invoice_status	1	\N
27	invoice_status	2	\N
28	invoice_status	3	\N
29	mediation_record_status	1	\N
30	mediation_record_status	2	\N
31	mediation_record_status	3	\N
32	mediation_record_status	4	\N
33	process_run_status	1	\N
34	process_run_status	2	\N
35	process_run_status	3	\N
\.


--
-- Data for Name: generic_status_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY generic_status_type (id) FROM stdin;
order_line_provisioning_status
order_status
subscriber_status
user_status
invoice_status
mediation_record_status
process_run_status
\.


--
-- Data for Name: international_description; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY international_description (table_id, foreign_id, psudo_column, language_id, content) FROM stdin;
50	20	description	1	Manual invoice deletion
46	9	description	1	Invoice maintenance
46	11	description	1	Pluggable tasks maintenance
47	16	description	1	A purchase order as been manually applied to an invoice.
52	17	description	1	Payment (failed)
52	16	description	1	Payment (successful)
35	6	description	1	Discovery
35	7	description	1	Diners
50	16	description	1	Days before expiration for order notification 2
50	17	description	1	Days before expiration for order notification 3
52	13	description	1	Order about to expire. Step 1
52	14	description	1	Order about to expire. Step 2
52	15	description	1	Order about to expire. Step 3
35	4	description	1	AMEX
35	5	description	1	ACH
50	14	description	1	Include customer notes in invoice
50	18	description	1	Invoice number prefix
64	234	description	1	Wallis and Futuna
64	235	description	1	Yemen
64	236	description	1	Zambia
64	237	description	1	Zimbabwe
50	15	description	1	Days before expiration for order notification
50	21	description	1	Use invoice reminders
50	22	description	1	Number of days after the invoice generation for the first reminder
50	23	description	1	Number of days for next reminder
64	227	description	1	Uzbekistan
64	147	description	1	Nauru
64	148	description	1	Nepal
64	149	description	1	Netherlands
64	150	description	1	Netherlands Antilles
64	151	description	1	New Caledonia
64	152	description	1	New Zealand
64	153	description	1	Nicaragua
64	154	description	1	Niger
64	155	description	1	Nigeria
64	156	description	1	Niue
64	157	description	1	Norfolk Island
64	158	description	1	North Korea
64	159	description	1	Northern Mariana Islands
64	160	description	1	Norway
64	161	description	1	Oman
64	162	description	1	Pakistan
64	163	description	1	Palau
64	164	description	1	Panama
64	165	description	1	Papua New Guinea
64	166	description	1	Paraguay
64	167	description	1	Peru
64	168	description	1	Philippines
64	169	description	1	Pitcairn Islands
64	170	description	1	Poland
64	171	description	1	Portugal
64	172	description	1	Puerto Rico
64	173	description	1	Qatar
64	174	description	1	Reunion
64	175	description	1	Romania
64	176	description	1	Russia
64	177	description	1	Rwanda
64	178	description	1	Samoa
64	179	description	1	San Marino
64	180	description	1	Sao Tome and Principe
64	181	description	1	Saudi Arabia
64	182	description	1	Senegal
64	183	description	1	Serbia and Montenegro
64	184	description	1	Seychelles
64	185	description	1	Sierra Leone
64	186	description	1	Singapore
64	187	description	1	Slovakia
64	188	description	1	Slovenia
64	189	description	1	Solomon Islands
64	190	description	1	Somalia
64	191	description	1	South Africa
64	192	description	1	South Georgia and the South Sandwich Islands
64	193	description	1	Spain
64	194	description	1	Sri Lanka
64	195	description	1	St. Helena
64	196	description	1	St. Kitts and Nevis
64	197	description	1	St. Lucia
64	198	description	1	St. Pierre and Miquelon
64	199	description	1	St. Vincent and the Grenadines
64	200	description	1	Sudan
64	201	description	1	Suriname
64	202	description	1	Svalbard and Jan Mayen
64	203	description	1	Swaziland
64	204	description	1	Sweden
64	205	description	1	Switzerland
64	206	description	1	Syria
64	207	description	1	Taiwan
64	208	description	1	Tajikistan
64	209	description	1	Tanzania
64	210	description	1	Thailand
64	211	description	1	Togo
64	212	description	1	Tokelau
64	213	description	1	Tonga
64	214	description	1	Trinidad and Tobago
64	215	description	1	Tunisia
64	216	description	1	Turkey
64	217	description	1	Turkmenistan
64	218	description	1	Turks and Caicos Islands
64	219	description	1	Tuvalu
64	220	description	1	Uganda
64	221	description	1	Ukraine
64	222	description	1	United Arab Emirates
64	223	description	1	United Kingdom
64	224	description	1	United States
64	225	description	1	United States Minor Outlying Islands
64	226	description	1	Uruguay
64	228	description	1	Vanuatu
64	229	description	1	Vatican City
64	230	description	1	Venezuela
64	231	description	1	Viet Nam
64	232	description	1	Virgin Islands - British
64	233	description	1	Virgin Islands
64	57	description	1	Congo - DRC
64	58	description	1	Denmark
64	59	description	1	Djibouti
64	60	description	1	Dominica
64	61	description	1	Dominican Republic
64	62	description	1	East Timor
64	63	description	1	Ecuador
64	64	description	1	Egypt
64	65	description	1	El Salvador
64	66	description	1	Equatorial Guinea
64	67	description	1	Eritrea
64	68	description	1	Estonia
64	69	description	1	Ethiopia
64	70	description	1	Malvinas Islands
64	71	description	1	Faroe Islands
64	72	description	1	Fiji Islands
64	73	description	1	Finland
64	74	description	1	France
64	75	description	1	French Guiana
64	76	description	1	French Polynesia
64	77	description	1	French Southern and Antarctic Lands
64	78	description	1	Gabon
64	79	description	1	Gambia
64	80	description	1	Georgia
64	81	description	1	Germany
64	82	description	1	Ghana
64	83	description	1	Gibraltar
64	84	description	1	Greece
64	85	description	1	Greenland
64	86	description	1	Grenada
64	87	description	1	Guadeloupe
64	88	description	1	Guam
64	89	description	1	Guatemala
64	90	description	1	Guinea
64	91	description	1	Guinea-Bissau
64	92	description	1	Guyana
64	93	description	1	Haiti
64	94	description	1	Heard Island and McDonald Islands
64	95	description	1	Honduras
64	96	description	1	Hong Kong SAR
64	97	description	1	Hungary
64	98	description	1	Iceland
64	99	description	1	India
64	100	description	1	Indonesia
64	101	description	1	Iran
64	102	description	1	Iraq
64	103	description	1	Ireland
64	104	description	1	Israel
64	105	description	1	Italy
64	106	description	1	Jamaica
64	107	description	1	Japan
64	108	description	1	Jordan
64	109	description	1	Kazakhstan
64	110	description	1	Kenya
64	111	description	1	Kiribati
64	112	description	1	Korea
64	113	description	1	Kuwait
64	114	description	1	Kyrgyzstan
64	115	description	1	Laos
64	116	description	1	Latvia
64	117	description	1	Lebanon
64	118	description	1	Lesotho
64	119	description	1	Liberia
64	120	description	1	Libya
64	121	description	1	Liechtenstein
64	122	description	1	Lithuania
64	123	description	1	Luxembourg
64	124	description	1	Macao SAR
64	125	description	1	Macedonia, Former Yugoslav Republic of
64	126	description	1	Madagascar
64	127	description	1	Malawi
64	128	description	1	Malaysia
64	129	description	1	Maldives
64	130	description	1	Mali
64	131	description	1	Malta
64	132	description	1	Marshall Islands
64	133	description	1	Martinique
64	134	description	1	Mauritania
64	135	description	1	Mauritius
64	136	description	1	Mayotte
64	137	description	1	Mexico
64	138	description	1	Micronesia
64	139	description	1	Moldova
64	140	description	1	Monaco
64	141	description	1	Mongolia
64	142	description	1	Montserrat
64	143	description	1	Morocco
64	144	description	1	Mozambique
64	145	description	1	Myanmar
64	146	description	1	Namibia
64	1	description	1	Afghanistan
64	2	description	1	Albania
64	3	description	1	Algeria
64	4	description	1	American Samoa
64	5	description	1	Andorra
64	6	description	1	Angola
64	7	description	1	Anguilla
64	8	description	1	Antarctica
64	9	description	1	Antigua and Barbuda
64	10	description	1	Argentina
64	11	description	1	Armenia
64	12	description	1	Aruba
64	13	description	1	Australia
64	14	description	1	Austria
64	15	description	1	Azerbaijan
64	16	description	1	Bahamas
64	17	description	1	Bahrain
64	18	description	1	Bangladesh
64	19	description	1	Barbados
64	20	description	1	Belarus
64	21	description	1	Belgium
64	22	description	1	Belize
64	23	description	1	Benin
64	24	description	1	Bermuda
64	25	description	1	Bhutan
64	26	description	1	Bolivia
64	27	description	1	Bosnia and Herzegovina
64	28	description	1	Botswana
64	29	description	1	Bouvet Island
64	30	description	1	Brazil
64	31	description	1	British Indian Ocean Territory
64	32	description	1	Brunei
64	33	description	1	Bulgaria
64	34	description	1	Burkina Faso
64	35	description	1	Burundi
64	36	description	1	Cambodia
64	37	description	1	Cameroon
64	38	description	1	Canada
64	39	description	1	Cape Verde
64	40	description	1	Cayman Islands
64	41	description	1	Central African Republic
64	42	description	1	Chad
64	43	description	1	Chile
64	44	description	1	China
64	45	description	1	Christmas Island
64	46	description	1	Cocos - Keeling Islands
64	47	description	1	Colombia
64	48	description	1	Comoros
64	49	description	1	Congo
64	50	description	1	Cook Islands
64	51	description	1	Costa Rica
64	52	description	1	Cote d Ivoire
64	53	description	1	Croatia
64	54	description	1	Cuba
64	55	description	1	Cyprus
64	56	description	1	Czech Republic
4	1	description	1	United States Dollar
4	2	description	1	Canadian Dollar
4	3	description	1	Euro
4	4	description	1	Yen
4	5	description	1	Pound Sterling
4	6	description	1	Won
4	7	description	1	Swiss Franc
4	8	description	1	Swedish Krona
6	1	description	1	Month
6	2	description	1	Week
6	3	description	1	Day
6	4	description	1	Year
7	1	description	1	Email
7	2	description	1	Paper
9	1	description	1	Active
9	2	description	1	Overdue
9	3	description	1	Overdue 2
9	4	description	1	Overdue 3
9	5	description	1	Suspended
9	6	description	1	Suspended 2
9	7	description	1	Suspended 3
9	8	description	1	Deleted
81	1	description	1	Active
81	2	description	1	Pending Unsubscription
81	3	description	1	Unsubscribed
81	4	description	1	Pending Expiration
81	5	description	1	Expired
81	6	description	1	Nonsubscriber
81	7	description	1	Discontinued
60	1	description	1	An internal user with all the permissions
60	1	title	1	Internal
60	2	description	1	The super user of an entity
60	2	title	1	Super user
60	3	description	1	A billing clerk
60	3	title	1	Clerk
60	4	description	1	A partner that will bring customers
60	4	title	1	Partner
60	5	description	1	A customer that will query his/her account
60	5	title	1	Customer
17	1	description	1	One time
18	1	description	1	Items
18	2	description	1	Tax
19	1	description	1	pre paid
19	2	description	1	post paid
20	1	description	1	Active
20	2	description	1	Finished
20	3	description	1	Suspended
35	1	description	1	Cheque
35	2	description	1	Visa
35	3	description	1	MasterCard
41	1	description	1	Successful
41	2	description	1	Failed
41	3	description	1	Processor unavailable
41	4	description	1	Entered
46	1	description	1	Billing Process
46	2	description	1	User maintenance
46	3	description	1	Item maintenance
46	4	description	1	Item type maintenance
46	5	description	1	Item user price maintenance
46	6	description	1	Promotion maintenance
46	7	description	1	Order maintenance
46	8	description	1	Credit card maintenance
47	1	description	1	A prepaid order has unbilled time before the billing process date
47	2	description	1	Order has no active time at the date of process.
47	3	description	1	At least one complete period has to be billable.
47	4	description	1	Already billed for the current date.
47	5	description	1	This order had to be maked for exclusion in the last process.
47	6	description	1	Pre-paid order is being process after its expiration.
47	7	description	1	A row was marked as deleted.
47	8	description	1	A user password was changed.
47	9	description	1	A row was updated.
47	10	description	1	Running a billing process, but a review is found unapproved.
47	11	description	1	Running a billing process, review is required but not present.
47	12	description	1	A user status was changed.
47	13	description	1	An order status was changed.
47	14	description	1	A user had to be aged, but there's no more steps configured.
47	15	description	1	A partner has a payout ready, but no payment instrument.
50	1	description	1	Process payment with billing process
50	2	description	1	URL of CSS file
50	3	description	1	URL of logo graphic
50	4	description	1	Grace period
50	5	description	1	Partner percentage rate
50	6	description	1	Partner referral fee
50	7	description	1	Partner one time payout
50	8	description	1	Partner period unit payout
50	9	description	1	Partner period value payout
50	10	description	1	Partner automatic payout
50	11	description	1	User in charge of partners 
50	12	description	1	Partner fee currency
50	13	description	1	Self delivery of paper invoices
52	1	description	1	Invoice (email)
52	2	description	1	User Reactivated
52	3	description	1	User Overdue
52	4	description	1	User Overdue 2
52	5	description	1	User Overdue 3
52	6	description	1	User Suspended
52	7	description	1	User Suspended 2
52	8	description	1	User Suspended 3
52	9	description	1	User Deleted
52	10	description	1	Payout Remainder
52	11	description	1	Partner Payout
52	12	description	1	Invoice (paper)
50	24	description	1	Data Fattura Fine Mese
4	9	description	1	Singapore Dollar
4	10	description	1	Malaysian Ringgit
4	11	description	1	Australian Dollar
50	19	description	1	Next invoice number
7	3	description	1	Email + Paper
35	8	description	1	PayPal
52	19	description	1	Update Credit Card
20	4	description	1	Suspended (auto)
18	3	description	1	Penalty
52	20	description	1	Lost password
88	1	description	1	Active
88	2	description	1	Inactive
88	3	description	1	Pending Active
88	4	description	1	Pending Inactive
88	5	description	1	Failed
88	6	description	1	Unavailable
50	20	description	2	Elimina��o manual de facturas
46	9	description	2	Manuten��o de facturas
46	11	description	2	Manuten��o de tarefas de plug-ins
47	16	description	2	Uma ordem de compra foi aplicada manualmente a uma factura.
52	17	description	2	Payment (sem sucesso)
52	16	description	2	Payment (com sucesso)
35	6	description	2	Discovery
35	7	description	2	Diners
50	16	description	2	Dias antes da expira��o para notifica��o de ordens 2
50	17	description	2	Dias antes da expira��o para notifica��o de ordens 3
52	13	description	2	Ordem de compra a expirar. Passo 1
52	14	description	2	Ordem de compra a expirar. Passo 2
52	15	description	2	Ordem de compra a expirar. Passo 3
35	4	description	2	AMEX
35	5	description	2	CCA
50	14	description	2	Incluir notas do cliente na factura
50	18	description	2	N�mero de prefixo da factura
64	234	description	2	Wallis and Futuna
64	235	description	2	Yemen
64	236	description	2	Z�mbia
64	237	description	2	Zimbabwe
50	15	description	2	Dias antes da expira��o para notifica��o de ordens
50	21	description	2	Usar os lembretes de factura
50	22	description	2	N�mero de dias ap�s a gera��o da factura para o primeiro lembrete
50	23	description	2	N�mero de dias para o pr�ximo lembrete
64	227	description	2	Uzbekist�o
64	147	description	2	Nauru
64	148	description	2	Nepal
64	149	description	2	Holanda
64	150	description	2	Antilhas Holandesas
64	151	description	2	Nova Caled�nia
64	152	description	2	Nova Zel�ndia
64	153	description	2	Nicar�gua
64	154	description	2	Niger
64	155	description	2	Nig�ria
64	156	description	2	Niue
64	157	description	2	Ilhas Norfolk
64	158	description	2	Coreia do Norte
64	159	description	2	Ilhas Mariana do Norte
64	160	description	2	Noruega
64	161	description	2	Oman
64	162	description	2	Pakist�o
64	163	description	2	Palau
64	164	description	2	Panama
64	165	description	2	Papua Nova Guin�
64	166	description	2	Paraguai
64	167	description	2	Per�
64	168	description	2	Filipinas
64	169	description	2	Ilhas Pitcairn
64	170	description	2	Pol�nia
64	171	description	2	Portugal
64	172	description	2	Porto Rico
64	173	description	2	Qatar
64	174	description	2	Reuni�o
64	175	description	2	Rom�nia
64	176	description	2	R�ssia
64	177	description	2	Rwanda
64	178	description	2	Samoa
64	179	description	2	S�o Marino
64	180	description	2	S�o Tom� e Princepe
64	181	description	2	Ar�bia Saudita
64	182	description	2	Senegal
64	183	description	2	S�rvia e Montenegro
64	184	description	2	Seychelles
64	185	description	2	Serra Leoa
64	186	description	2	Singapure
64	187	description	2	Eslov�quia
64	188	description	2	Eslov�nia
64	189	description	2	Ilhas Salom�o
64	190	description	2	Som�lia
64	191	description	2	�frica do Sul
64	192	description	2	Georgia do Sul e Ilhas Sandwich South
64	193	description	2	Espanha
64	194	description	2	Sri Lanka
64	195	description	2	Sta. Helena
64	196	description	2	Sta. Kitts e Nevis
64	197	description	2	Sta. Lucia
64	198	description	2	Sta. Pierre e Miquelon
64	199	description	2	Sto. Vicente e Grenadines
64	200	description	2	Sud�o
64	201	description	2	Suriname
64	202	description	2	Svalbard e Jan Mayen
64	203	description	2	Swazil�ndia
64	204	description	2	Su�cia
64	205	description	2	Su��a
64	206	description	2	S�ria
64	207	description	2	Taiwan
64	208	description	2	Tajikist�o
64	209	description	2	Tanz�nia
64	210	description	2	Thail�ndia
64	211	description	2	Togo
64	212	description	2	Tokelau
64	213	description	2	Tonga
64	214	description	2	Trinidade e Tobago
64	215	description	2	Tun�sia
64	216	description	2	Turquia
64	217	description	2	Turkmenist�o
64	218	description	2	Ilhas Turks e Caicos
64	219	description	2	Tuvalu
64	220	description	2	Uganda
64	221	description	2	Ucr�nia
64	222	description	2	Emiados �rabes Unidos
64	223	description	2	Reino Unido
64	224	description	2	Estados Unidos
64	225	description	2	Estados Unidos e Ilhas Menores Circundantes
64	226	description	2	Uruguai
64	228	description	2	Vanuatu
64	229	description	2	Cidade do Vaticano
64	230	description	2	Venezuela
64	231	description	2	Vietname
64	232	description	2	Ilhas Virgens Brit�nicas
64	233	description	2	Ilhas Virgens
64	57	description	2	Rep�blica Democr�tica do Congo
64	58	description	2	Dinamarca
64	59	description	2	Djibouti
64	60	description	2	Dominica
64	61	description	2	Rep�blica Dominicana
64	62	description	2	Timor Leste
64	63	description	2	Ecuador
64	64	description	2	Egipto
64	65	description	2	El Salvador
64	66	description	2	Guin� Equatorial
64	67	description	2	Eritreia
64	68	description	2	Est�nia
64	69	description	2	Etiopia
64	70	description	2	Ilhas Malvinas
64	71	description	2	Ilhas Faro�
64	72	description	2	Ilhas Fiji
64	73	description	2	Finl�ndia
64	74	description	2	Fran�a
64	75	description	2	Guiana Francesa
64	76	description	2	Polin�sia Francesa
64	77	description	2	Terras Ant�rticas e do Sul Francesas
64	78	description	2	Gab�o
64	79	description	2	G�mbia
64	80	description	2	Georgia
64	81	description	2	Alemanha
64	82	description	2	Gana
64	83	description	2	Gibraltar
64	84	description	2	Gr�cia
64	85	description	2	Gronel�ndia
64	86	description	2	Granada
64	87	description	2	Guadalupe
64	88	description	2	Guantanamo
64	89	description	2	Guatemala
64	90	description	2	Guin�
64	91	description	2	Guin�-Bissau
64	92	description	2	Guiana
64	93	description	2	Haiti
64	94	description	2	Ilhas Heard e McDonald
64	95	description	2	Honduras
64	96	description	2	Hong Kong SAR
64	97	description	2	Hungria
64	98	description	2	Isl�ndia
64	99	description	2	�ndia
64	100	description	2	Indon�sia
64	101	description	2	Ir�o
64	102	description	2	Iraque
64	103	description	2	Irlanda
64	104	description	2	Israel
64	105	description	2	It�lia
64	106	description	2	Jamaica
64	107	description	2	Jap�o
64	108	description	2	Jord�nia
64	109	description	2	Kazaquist�o
64	110	description	2	K�nia
64	111	description	2	Kiribati
64	112	description	2	Coreia
64	113	description	2	Kuwait
64	114	description	2	Kirgist�o
64	115	description	2	Laos
64	116	description	2	Latvia
64	117	description	2	L�bano
64	118	description	2	Lesoto
64	119	description	2	Lib�ria
64	120	description	2	L�bia
64	121	description	2	Liechtenstein
64	122	description	2	Litu�nia
64	123	description	2	Luxemburgo
64	124	description	2	Macau SAR
64	125	description	2	Maced�nia, Antiga Rep�blica Jugoslava da
64	126	description	2	Madag�scar
64	127	description	2	Malaui
64	128	description	2	Mal�sia
64	129	description	2	Maldivas
64	130	description	2	Mali
64	131	description	2	Malta
64	132	description	2	Ilhas Marshall
64	133	description	2	Martinica
64	134	description	2	Maurit�nia
64	135	description	2	Maur�cias
64	136	description	2	Maiote
64	137	description	2	M�xico
64	138	description	2	Micron�sia
64	139	description	2	Moldova
64	140	description	2	M�naco
64	141	description	2	Mong�lia
64	142	description	2	Monserrate
64	143	description	2	Marrocos
64	144	description	2	Mo�ambique
64	145	description	2	Mianmar
64	146	description	2	Nam�bia
64	1	description	2	Afganist�o
64	2	description	2	Alb�nia
64	3	description	2	Alg�ria
64	4	description	2	Samoa Americana
64	5	description	2	Andorra
64	6	description	2	Angola
64	7	description	2	Anguilha
64	8	description	2	Ant�rtida
64	9	description	2	Antigua e Barbuda
64	10	description	2	Argentina
64	11	description	2	Arm�nia
64	12	description	2	Aruba
64	13	description	2	Austr�lia
64	14	description	2	�ustria
64	15	description	2	Azerbeij�o
64	16	description	2	Bahamas
64	17	description	2	Bahrain
64	18	description	2	Bangladesh
64	19	description	2	Barbados
64	20	description	2	Belar�ssia
64	21	description	2	B�lgica
64	22	description	2	Belize
64	23	description	2	Benin
64	24	description	2	Bermuda
64	25	description	2	But�o
64	26	description	2	Bol�via
64	27	description	2	Bosnia e Herzegovina
64	28	description	2	Botswana
64	29	description	2	Ilha Bouvet
64	30	description	2	Brasil
64	31	description	2	Territ�rio Brit�nico do Oceano �ndico
64	32	description	2	Brunei
64	33	description	2	Bulg�ria
64	34	description	2	Burquina Faso
64	35	description	2	Burundi
64	36	description	2	Cambodia
64	37	description	2	Camar�es
64	38	description	2	Canada
64	39	description	2	Cabo Verde
64	40	description	2	Ilhas Caim�o
64	41	description	2	Rep�blica Centro Africana
64	42	description	2	Chade
64	43	description	2	Chile
64	44	description	2	China
64	45	description	2	Ilha Natal
64	46	description	2	Ilha Cocos e Keeling
64	47	description	2	Col�mbia
64	48	description	2	Comoros
64	49	description	2	Congo
64	50	description	2	Ilhas Cook
64	51	description	2	Costa Rica
64	52	description	2	Costa do Marfim
64	53	description	2	Cro�cia
64	54	description	2	Cuba
64	55	description	2	Chipre
64	56	description	2	Rep�blica Checa
4	1	description	2	D�lares Norte Americanos
4	2	description	2	D�lares Canadianos
4	3	description	2	Euro
4	4	description	2	Ien
4	5	description	2	Libras Estrelinas
4	6	description	2	Won
4	7	description	2	Franco Su��o
4	8	description	2	Coroa Sueca
6	1	description	2	M�s
6	2	description	2	Semana
6	3	description	2	Dia
6	4	description	2	Ano
7	1	description	2	Email
7	2	description	2	Papel
9	1	description	2	Activo
9	2	description	2	Em aberto
9	3	description	2	Em aberto 2
9	4	description	2	Em aberto 3
9	5	description	2	Suspensa
9	6	description	2	Suspensa 2
9	7	description	2	Suspensa 3
9	8	description	2	Eliminado
60	1	description	2	Um utilizador interno com todas as permiss�es
60	1	title	2	Interno
60	2	description	2	O super utilizador de uma entidade
60	2	title	2	Super utilizador
60	3	description	2	Um operador de factura��o
60	3	title	2	Operador
60	4	description	2	Um parceiro que vai angariar clientes
60	4	title	2	Parceiro
60	5	description	2	Um cliente que vai fazer pesquisas na sua conta
60	5	title	2	Cliente
17	1	description	2	Vez �nica
18	1	description	2	Items
18	2	description	2	Imposto
19	1	description	2	Pr� pago
19	2	description	2	P�s pago
20	1	description	2	Activo
20	2	description	2	Terminado
20	3	description	2	Suspenso
35	1	description	2	Cheque
35	2	description	2	Visa
35	3	description	2	MasterCard
41	1	description	2	Com sucesso
41	2	description	2	Sem sucesso
41	3	description	2	Processador indispon�vel
41	4	description	2	Inserido
46	1	description	2	Processo de Factura��o
46	2	description	2	Manuten��o de Utilizador
46	3	description	2	Item de manuten��o
46	4	description	2	Item tipo de manuten��o
46	5	description	2	Item manuten��o de pre�o de utilizador
46	6	description	2	Manuten��o de promo��o
46	7	description	2	Manuten��o por ordem
46	8	description	2	Manuten��o de cart�o de cr�dito
47	1	description	2	Uma ordem pr�-paga tem tempo n�o facturado anterior � data de factura��o
47	2	description	2	A ordem n�o tem nenhum per�odo activo � data de processamento.
47	3	description	2	Pelo menos um per�odo completo tem de ser factur�vel.
47	4	description	2	J� h� factura��o para o per�odo.
47	5	description	2	Esta ordem teve de ser marcada para exclus�o do �ltimo processo.
47	6	description	2	Pre-paid order is being process after its expiration.
47	7	description	2	A linha marcada foi eliminada.
47	8	description	2	A senha de utilizador foi alterada.
47	9	description	2	Uma linha foi actualizada.
47	10	description	2	A correr um processo de factura��o, foi encontrada uma revis�o rejeitada.
47	11	description	2	A correr um processo de factura��o, uma � necess�ria mas n�o encontrada.
47	12	description	2	Um status de utilizador foi alterado.
47	13	description	2	Um status de uma ordem foi alterado.
47	14	description	2	Um utilizador foi inserido no processo de antiguidade, mas n�o h� mais passos configurados.
47	15	description	2	Um parceiro tem um pagamento a receber, mas n�o tem instrumento de pagamento.
50	1	description	2	Processar pagamento com processo de factura��o
50	2	description	2	URL ou ficheiro CSS
50	3	description	2	URL ou gr�fico de logotipo
50	4	description	2	Per�odo de gra�a
50	5	description	2	Percentagem do parceiro
50	6	description	2	Valor de refer�ncia do parceiro
50	7	description	2	Parceiro pagamento �nico
50	8	description	2	Parceiro unidade do per�odo de pagamento
50	9	description	2	Parceiro valor do per�odo de pagamento
50	10	description	2	Parceiro pagamento autom�tico
50	11	description	2	Utilizador respons�vel pelos parceiros
50	12	description	2	Parceiro moeda
50	13	description	2	Entrega pelo mesmo das facturas em papel
52	1	description	2	Factura (email)
52	2	description	2	Utilizador Reactivado
52	3	description	2	Utilizador Em Atraso
52	4	description	2	Utilizador Em Atraso 2
52	5	description	2	Utilizador Em Atraso 3
52	6	description	2	Utilizador Suspenso
52	7	description	2	Utilizador Suspenso 2
52	8	description	2	Utilizador Suspenso 3
52	9	description	2	Utilizador Eliminado
52	10	description	2	Pagamento Remascente
52	11	description	2	Parceiro Pagamento
52	12	description	2	Factura (papel)
50	24	description	2	Data Factura Fim do M�s
4	9	description	2	D�lar da Singapura
4	10	description	2	Ringgit Malasiano
4	11	description	2	D�lar Australiano
50	19	description	2	Pr�ximo n�mero de factura
7	3	description	2	Email + Papel
35	8	description	2	PayPal
52	19	description	2	Actualizar Cart�o de Cr�dito
20	4	description	2	Suspender (auto)
18	3	description	2	Penalidade
52	20	description	2	Senha esquecida
89	1	description	1	None
89	2	description	1	Pre-paid balance
89	3	description	1	Credit limit
91	1	description	1	Done and billable
91	2	description	1	Done and not billable
91	3	description	1	Error detected
91	4	description	1	Error declared
92	1	description	1	Running
92	2	description	1	Finished: successful
92	3	description	1	Finished: failed
23	1	description	1	Item management and order line total calculation
23	2	description	1	Billing process: order filters
23	3	description	1	Billing process: invoice filters
23	4	description	1	Invoice presentation
23	5	description	1	Billing process: order periods calculation
23	6	description	1	Payment gateway integration
23	7	description	1	Notifications
23	8	description	1	Payment instrument selection
23	9	description	1	Penalties for overdue invoices
23	10	description	1	Alarms when a payment gateway is down
23	11	description	1	Subscription status manager
23	12	description	1	Parameters for asynchronous payment processing
23	13	description	1	Add one product to order
23	14	description	1	Product pricing
23	15	description	1	Mediation Reader
23	16	description	1	Mediation Processor
23	17	description	1	Generic internal events listener
23	18	description	1	External provisioning processor
23	19	description	1	Purchase validation against pre-paid balance / credit limit
23	20	description	1	Billing process: customer selection
23	21	description	1	Mediation Error Handler
23	22	description	1	Scheduled Plug-ins
23	23	description	1	Rules Generators
23	24	description	1	Ageing for customers with overdue invoices
24	1	title	1	Default order totals
24	1	description	1	Calculates the order total and the total for each line, considering the item prices, the quantity and if the prices are percentage or not.
24	2	title	1	VAT
24	2	description	1	Adds an additional line to the order with a percentage charge to represent the value added tax.
24	3	title	1	Invoice due date
24	3	description	1	A very simple implementation that sets the due date of the invoice. The due date is calculated by just adding the period of time to the invoice date.
24	4	title	1	Default invoice composition.
24	4	description	1	This task will copy all the lines on the orders and invoices to the new invoice, considering the periods involved for each order, but not the fractions of periods. It will not copy the lines that are taxes. The quantity and total of each line will be multiplied by the amount of periods.
24	5	title	1	Standard Order Filter
24	5	description	1	Decides if an order should be included in an invoice for a given billing process.  This is done by taking the billing process time span, the order period, the active since/until, etc.
24	6	title	1	Standard Invoice Filter
24	6	description	1	Always returns true, meaning that the overdue invoice will be carried over to a new invoice.
24	7	title	1	Default Order Periods
24	7	description	1	Calculates the start and end period to be included in an invoice. This is done by taking the billing process time span, the order period, the active since/until, etc.
24	8	title	1	Authorize.net payment processor
24	8	description	1	Integration with the authorize.net payment gateway.
24	9	title	1	Standard Email Notification
24	9	description	1	Notifies a user by sending an email. It supports text and HTML emails
24	10	title	1	Default payment information
24	10	description	1	Finds the information of a payment method available to a customer, given priority to credit card. In other words, it will return the credit car of a customer or the ACH information in that order.
24	11	title	1	Testing plug-in for partner payouts
24	11	description	1	Plug-in useful only for testing
24	12	title	1	PDF invoice notification
24	12	description	1	Will generate a PDF version of an invoice.
24	14	title	1	No invoice carry over
24	14	description	1	Returns always false, which makes jBilling to never carry over an invoice into another newer invoice.
24	15	title	1	Default interest task
24	15	description	1	Will create a new order with a penalty item. The item is taken as a parameter to the task.
24	16	title	1	Anticipated order filter
24	16	description	1	Extends BasicOrderFilterTask, modifying the dates to make the order applicable a number of months before it would be by using the default filter.
24	17	title	1	Anticipate order periods.
24	17	description	1	Extends BasicOrderPeriodTask, modifying the dates to make the order applicable a number of months before itd be by using the default task.
24	19	title	1	Email & process authorize.net
24	19	description	1	Extends the standard authorize.net payment processor to also send an email to the company after processing the payment.
24	20	title	1	Payment gateway down alarm
24	20	description	1	Sends an email to the billing administrator as an alarm when a payment gateway is down.
24	21	title	1	Test payment processor
24	21	description	1	A test payment processor implementation to be able to test jBillings functions without using a real payment gateway.
24	22	title	1	Router payment processor based on Custom Fields
24	22	description	1	Allows a customer to be assigned a specific payment gateway. It checks a custom contact field to identify the gateway and then delegates the actual payment processing to another plugin.
24	23	title	1	Default subscription status manager
24	23	description	1	It determines how a payment event affects the subscription status of a user, considering its present status and a state machine.
24	24	title	1	ACH Commerce payment processor
24	24	description	1	Integration with the ACH commerce payment gateway.
24	25	title	1	Standard asynchronous parameters
24	25	description	1	A dummy task that does not add any parameters for asynchronous payment processing. This is the default.
24	26	title	1	Router asynchronous parameters
24	26	description	1	This plug-in adds parameters for asynchronous payment processing to have one processing message bean per payment processor. It is used in combination with the router payment processor plug-ins.
24	28	title	1	Standard Item Manager
24	28	description	1	It adds items to an order. If the item is already in the order, it only updates the quantity.
24	29	title	1	Rules Item Manager
24	29	description	1	This is a rules-based plug-in. It will do what the basic item manager does (actually calling it); but then it will execute external rules as well. These external rules have full control on changing the order that is getting new items.
24	30	title	1	Rules Line Total
24	30	description	1	This is a rules-based plug-in. It calculates the total for an order line (typically this is the price multiplied by the quantity); allowing for the execution of external rules.
24	31	title	1	Rules Pricing
24	31	description	1	This is a rules-based plug-in. It gives a price to an item by executing external rules. You can then add logic externally for pricing. It is also integrated with the mediation process by having access to the mediation pricing data.
24	32	title	1	Separator file reader
24	32	description	1	This is a reader for the mediation process. It reads records from a text file whose fields are separated by a character (or string).
24	33	title	1	Rules mediation processor
24	33	description	1	This is a rules-based plug-in (see chapter 7). It takes an event record from the mediation process and executes external rules to translate the record into billing meaningful data. This is at the core of the mediation component, see the ?Telecom Guide? document for more information.
24	34	title	1	Fixed length file reader
24	34	description	1	This is a reader for the mediation process. It reads records from a text file whose fields have fixed positions,and the record has a fixed length.
24	35	title	1	Payment information without validation
24	35	description	1	This is exactly the same as the standard payment information task, the only difference is that it does not validate if the credit card is expired. Use this plug-in only if you want to submit payment with expired credit cards.
24	36	title	1	Notification task for testing
24	36	description	1	This plug-in is only used for testing purposes. Instead of sending an email (or other real notification); it simply stores the text to be sent in a file named emails_sent.txt.
24	37	title	1	Order periods calculator with pro rating.
24	37	description	1	This plugin takes into consideration the field cycle starts of orders to calculate fractional order periods.
24	38	title	1	Invoice composition task with pro-rating (day as fraction)
24	38	description	1	When creating an invoice from an order, this plug-in will pro-rate any fraction of a period taking a day as the smallest billable unit.
24	39	title	1	Payment process for the Intraanuity payment gateway
24	39	description	1	Integration with the Intraanuity payment gateway.
24	40	title	1	Automatic cancellation credit.
24	40	description	1	This plug-in will create a new order with a negative price to reflect a credit when an order is canceled within a period that has been already invoiced.
24	41	title	1	Fees for early cancellation of a plan.
24	41	description	1	This plug-in will use external rules to determine if an order that is being canceled should create a new order with a penalty fee. This is typically used for early cancels of a contract.
24	42	title	1	Blacklist filter payment processor.
24	42	description	1	Used for blocking payments from reaching real payment processors. Typically configured as first payment processor in the processing chain.
24	43	title	1	Blacklist user when their status becomes suspended or higher.
24	43	description	1	Causes users and their associated details (e.g., credit card number, phone number, etc.) to be blacklisted when their status becomes suspended or higher. 
24	44	title	1	JDBC Mediation Reader.
24	44	description	1	This is a reader for the mediation process. It reads records from a JDBC database source.
24	45	title	1	MySQL Mediation Reader.
24	45	description	1	This is a reader for the mediation process. It is an extension of the JDBC reader, allowing easy configuration of a MySQL database source.
24	46	title	1	Provisioning commands rules task.
24	46	description	1	Responds to order related events. Runs rules to generate commands to send via JMS messages to the external provisioning module.
24	47	title	1	Test external provisioning task.
24	47	description	1	This plug-in is only used for testing purposes. It is a test external provisioning task for testing the provisioning modules.
24	48	title	1	CAI external provisioning task.
24	48	description	1	An external provisioning plug-in for communicating with the Ericsson Customer Administration Interface (CAI).
24	49	title	1	Currency Router payment processor
24	49	description	1	Delegates the actual payment processing to another plug-in based on the currency of the payment.
24	50	title	1	MMSC external provisioning task.
24	50	description	1	An external provisioning plug-in for communicating with the TeliaSonera MMSC.
24	51	title	1	Filters out negative invoices for carry over.
24	51	description	1	This filter will only invoices with a positive balance to be carried over to the next invoice.
24	52	title	1	File invoice exporter.
24	52	description	1	It will generate a file with one line per invoice generated.
24	53	title	1	Rules caller on an event.
24	53	description	1	It will call a package of rules when an internal event happens.
24	54	title	1	Dynamic balance manager
24	54	description	1	It will update the dynamic balance of a customer (pre-paid or credit limit) when events affecting the balance happen.
24	55	title	1	Balance validator based on the customer balance.
24	55	description	1	Used for real-time mediation, this plug-in will validate a call based on the current dynamic balance of a customer.
24	56	title	1	Balance validator based on rules.
24	56	description	1	Used for real-time mediation, this plug-in will validate a call based on a package or rules
24	57	title	1	Payment processor for Payments Gateway.
24	57	description	1	Integration with the Payments Gateway payment processor.
24	58	title	1	Credit cards are stored externally.
24	58	description	1	Saves the credit card information in the payment gateway, rather than the jBilling DB.
24	59	title	1	Rules Item Manager 2
24	59	description	1	This is a rules-based plug-in compatible with the mediation module of jBilling 2.2.x. It will do what the basic item manager does (actually calling it); but then it will execute external rules as well. These external rules have full control on changing the order that is getting new items.
24	60	title	1	Rules Line Total - 2
24	60	description	1	This is a rules-based plug-in, compatible with the mediation process of jBilling 2.2.x and later. It calculates the total for an order line (typically this is the price multiplied by the quantity); allowing for the execution of external rules.
24	61	title	1	Rules Pricing 2
24	61	description	1	This is a rules-based plug-in compatible with the mediation module of jBilling 2.2.x. It gives a price to an item by executing external rules. You can then add logic externally for pricing. It is also integrated with the mediation process by having access to the mediation pricing data.
24	63	title	1	Test payment processor for external storage.
24	63	description	1	A fake plug-in to test payments that would be stored externally.
24	64	title	1	WorldPay integration
24	64	description	1	Payment processor plug-in to integrate with RBS WorldPay
24	65	title	1	WorldPay integration with external storage
24	65	description	1	Payment processor plug-in to integrate with RBS WorldPay. It stores the credit card information (number, etc) in the gateway.
24	66	title	1	Auto recharge
24	66	description	1	Monitors the balance of a customer and upon reaching a limit, it requests a real-time payment
24	67	title	1	Beanstream gateway integration
24	67	description	1	Payment processor for integration with the Beanstream payment gateway
24	68	title	1	Sage payments gateway integration
24	68	description	1	Payment processor for integration with the Sage payment gateway
24	69	title	1	Standard billing process users filter
24	69	description	1	Called when the billing process runs to select which users to evaluate. This basic implementation simply returns every user not in suspended (or worse) status
24	70	title	1	Selective billing process users filter
24	70	description	1	Called when the billing process runs to select which users to evaluate. This only returns users with orders that have a next invoice date earlier than the billing process.
24	71	title	1	Mediation file error handler
24	71	description	1	Event records with errors are saved to a file
24	73	title	1	Mediation data base error handler
24	73	description	1	Event records with errors are saved to a database table
24	75	title	1	Paypal integration with external storage
24	75	description	1	Submits payments to paypal as a payment gateway and stores credit card information in PayPal as well
24	76	title	1	Authorize.net integration with external storage
24	76	description	1	Submits payments to authorize.net as a payment gateway and stores credit card information in authorize.net as well
24	77	title	1	Payment method router payment processor
24	77	description	1	Delegates the actual payment processing to another plug-in based on the payment method of the payment.
24	78	title	1	Dynamic rules generator
24	78	description	1	Generates rules dynamically based on a Velocity template.
24	79	title	1	Mediation Process Task
24	79	description	1	A scheduled task to execute the Mediation Process.
24	80	title	1	Billing Process Task
24	80	description	1	A scheduled task to execute the Billing Process.
24	87	title	1	Basic ageing
24	87	description	1	Ages a user based on the number of days that the account is overdue.
24	88	title	1	Ageing process task
24	88	description	1	A scheduled task to execute the Ageing Process.
24	89	title	1	Business day ageing
24	89	description	1	Ages a user based on the number of business days (excluding holidays) that the account is overdue.
24	90	title	1	Simple Tax Composition Task
24	90	description	1	A pluggable task of the type AbstractChargeTask to apply tax item to an Invoice with a facility of exempting an exempt item or an exemp customer.
24	91	title	1	Country Tax Invoice Composition Task
24	91	description	1	A pluggable task of the type AbstractChargeTask to apply tax item to the Invoice if the Partner's country code is matching.
24	92	title	1	Payment Terms Penalty Task
24	92	description	1	A pluggable task of the type AbstractChargeTask to apply a Penalty to an Invoice having a due date beyond a configurable days period.
24	93	title	1	Fees for early cancellation of a subscription
24	93	description	1	This plug-in will create a new order with a fee if a recurring order is cancelled too early
47	17	description	1	The order line has been updated
47	18	description	1	The order next billing date has been changed
47	19	description	1	Last API call to get the the user subscription status transitions
47	20	description	1	User subscription status has changed
47	21	description	1	User account is now locked
47	22	description	1	The order main subscription flag was changed
47	23	description	1	All the one-time orders the mediation found were in status finished
47	24	description	1	A valid payment method was not found. The payment request was cancelled
47	25	description	1	A new row has been created
47	26	description	1	An invoiced order was cancelled, a credit order was created
47	27	description	1	A user id was added to the blacklist
47	28	description	1	A user id was removed from the blacklist
47	29	description	1	Posted a provisioning command using a UUID
47	30	description	1	A command was posted for provisioning
47	31	description	1	The provisioning status of an order line has changed
47	32	description	1	User subscription status has NOT changed
47	33	description	1	The dynamic balance of a user has changed
47	34	description	1	The invoice if child flag has changed
101	1	description	1	Invoice Reports
101	2	description	1	Order Reports
101	3	description	1	Payment Reports
101	4	description	1	Customer Reports
100	1	description	1	Total amount invoiced grouped by period.
100	2	description	1	Detailed balance ageing report. Shows the age of outstanding customer balances.
100	3	description	1	Number of users subscribed to a specific product.
100	4	description	1	Total payment amount received grouped by period.
100	5	description	1	Number of customers created within a period.
100	6	description	1	Total revenue (sum of received payments) per customer.
100	7	description	1	Simple accounts receivable report showing current account balances.
100	8	description	1	General ledger details of all invoiced charges for the given day.
100	9	description	1	General ledger summary of all invoiced charges for the given day, grouped by item type.
100	10	description	1	Plan pricing history for all plan products and start dates.
100	11	description	1	Total invoiced per customer grouped by product category.
100	12	description	1	Total invoiced per customer over years grouped by year.
104	1	description	1	Invoices
104	2	description	1	Orders
104	3	description	1	Payments
104	4	description	1	Users
50	25	description	1	Use overdue penalties (interest).
50	27	description	1	Use order anticipation.
50	28	description	1	Paypal account.
50	29	description	1	Paypal button URL.
50	30	description	1	URL for HTTP ageing callback.
50	31	description	1	Use continuous invoice dates.
50	32	description	1	Attach PDF invoice to email notification.
50	33	description	1	Force one order per invoice.
50	35	description	1	Add order Id to invoice lines.
50	36	description	1	Allow customers to edit own contact information.
50	37	description	1	Hide (mask) credit card numbers.
50	38	description	1	Link ageing to customer subscriber status.
50	39	description	1	Lock-out user after failed login attempts.
50	40	description	1	Expire user passwords after days.
50	41	description	1	Use main-subscription orders.
50	42	description	1	Use pro-rating.
50	43	description	1	Use payment blacklist.
50	44	description	1	Allow negative payments.
50	45	description	1	Delay negative invoice payments.
50	46	description	1	Allow invoice without orders.
50	47	description	1	Last read mediation record id.
50	48	description	1	Use provisioning.
50	49	description	1	Automatic customer recharge threshold.
50	50	description	1	Invoice decimal rounding.
50	4	instruction	1	Grace period in days before ageing a customer with an overdue invoice.
50	5	instruction	1	Partner default percentage commission rate. See the Partner section of the documentation.
50	6	instruction	1	Partner default flat fee to be paid as commission. See the Partner section of the documentation.
50	7	instruction	1	Set to '1' to enable one-time payment for partners. If set, partners will only get paid once per customer. See the Partner section of the documentation.
50	8	instruction	1	Partner default payout period unit. See the Partner section of the documentation.
50	9	instruction	1	Partner default payout period value. See the Partner section of the documentation.
50	10	instruction	1	Set to '1' to enable batch payment payouts using the billing process and the configured payment processor. See the Partner section of the documentation.
50	11	instruction	1	Partner default assigned clerk id. See the Partner section of the documentation.
50	12	instruction	1	Currency ID to use when paying partners. See the Partner section of the documentation.
50	13	instruction	1	Set to '1' to e-mail invoices as the billing company. '0' to deliver invoices as jBilling.
50	14	instruction	1	Set to '1' to show notes in invoices, '0' to disable.
50	15	instruction	1	Days before the orders 'active until' date to send the 1st notification. Leave blank to disable.
50	16	instruction	1	Days before the orders 'active until' date to send the 2nd notification. Leave blank to disable.
50	17	instruction	1	Days before the orders 'active until' date to send the 3rd notification. Leave blank to disable.
50	18	instruction	1	Prefix value for generated invoice public numbers.
50	19	instruction	1	The current value for generated invoice public numbers. New invoices will be assigned a public number by incrementing this value.
50	20	instruction	1	Set to '1' to allow invoices to be deleted, '0' to disable.
50	21	instruction	1	Set to '1' to allow invoice reminder notifications, '0' to disable.
50	24	instruction	1	Set to '1' to enable, '0' to disable.
50	25	instruction	1	Set to '1' to enable the billing process to calculate interest on overdue payments, '0' to disable. Calculation of interest is handled by the selected penalty plug-in.
50	27	instruction	1	Set to '1' to use the 'OrderFilterAnticipateTask' to invoice a number of months in advance, '0' to disable. Plug-in must be configured separately.
50	28	instruction	1	PayPal account name.
50	29	instruction	1	A URL where the graphic of the PayPal button resides. The button is displayed to customers when they are making a payment. The default is usually the best option, except when another language is needed.
50	30	instruction	1	URL for the HTTP Callback to invoke when the ageing process changes a status of a user.
50	31	instruction	1	Default '2000-01-01'. If this preference is used, the system will make sure that all your invoices have their dates in a incremental way. Any invoice with a greater 'ID' will also have a greater (or equal) date. In other words, a new invoice can not have an earlier date than an existing (older) invoice. To use this preference, set it as a string with the date where to start.
50	32	instruction	1	Set to '1' to attach a PDF version of the invoice to all invoice notification e-mails. '0' to disable.
50	33	instruction	1	Set to '1' to show the 'include in separate invoice' flag on an order. '0' to disable.
50	35	instruction	1	Set to '1' to include the ID of the order in the description text of the resulting invoice line. '0' to disable. This can help to easily track which exact orders is responsible for a line in an invoice, considering that many orders can be included in a single invoice.
50	36	instruction	1	Set to '1' to allow customers to edit their own contact information. '0' to disable.
50	37	instruction	1	Set to '1' to mask all credit card numbers. '0' to disable. When set, numbers are masked to all users, even administrators, and in all log files.
50	38	instruction	1	Set to '1' to change the subscription status of a user when the user ages. '0' to disable.
50	39	instruction	1	The number of retries to allow before locking the user account. A locked user account will have their password changed to the value of lockout_password in the jbilling.properties configuration file.
50	40	instruction	1	If greater than zero, it represents the number of days that a password is valid. After those days, the password is expired and the user is forced to change it.
50	41	instruction	1	Set to '1' to allow the usage of the 'main subscription' flag for orders This flag is read only by the mediation process when determining where to place charges coming from external events.
50	42	instruction	1	Set to '1' to allow the use of pro-rating to invoice fractions of a period. Shows the 'cycle' attribute of an order. Note that you need to configure the corresponding plug-ins for this feature to be fully functional.
50	43	instruction	1	If the payment blacklist feature is used, this is set to the id of the configuration of the PaymentFilterTask plug-in. See the Blacklist section of the documentation.
50	44	instruction	1	Set to '1' to allow negative payments. '0' to disable
50	45	instruction	1	Set to '1' to delay payment of negative invoice amounts, causing the balance to be carried over to the next invoice. Invoices that have had negative balances from other invoices transferred to them are allowed to immediately make a negative payment (credit) if needed. '0' to disable. Preference 44 & 46 are usually also enabled.
50	46	instruction	1	Set to '1' to allow invoices with negative balances to generate a new invoice that isn't composed of any orders so that their balances will always get carried over to a new invoice for the credit to take place. '0' to disable. Preference 44 & 45 are usually also enabled.
50	47	instruction	1	ID of the last record read by the mediation process. This is used to determine what records are 'new' and need to be read.
50	48	instruction	1	Set to '1' to allow the use of provisioning. '0' to disable.
50	49	instruction	1	The threshold value for automatic payments. Pre-paid users with an automatic recharge value set will generate an automatic payment whenever the account balance falls below this threshold. Note that you need to configure the AutoRechargeTask plug-in for this feature to be fully functional.
50	50	instruction	1	The number of decimal places to be shown on the invoice. Defaults to 2.
90	1	description	1	Paid
90	2	description	1	Unpaid
90	3	description	1	Carried
59	10	description	1	Create customer
59	11	description	1	Edit customer
59	12	description	1	Delete customer
59	13	description	1	Inspect customer
59	14	description	1	Blacklist customer
59	15	description	1	View customer details
59	16	description	1	Download customer CSV
59	17	description	1	View all customers
59	18	description	1	View customer sub-accounts
59	20	description	1	Create order
59	21	description	1	Edit order
59	22	description	1	Delete order
59	23	description	1	Generate invoice for order
59	24	description	1	View order details
59	25	description	1	Download order CSV
59	26	description	1	Edit line price
59	27	description	1	Edit line description
59	28	description	1	View all customers
59	29	description	1	View customer sub-accounts
59	30	description	1	Create payment
59	31	description	1	Edit payment
59	32	description	1	Delete payment
59	33	description	1	Link payment to invoice
59	34	description	1	View payment details
59	35	description	1	Download payment CSV
59	36	description	1	View all customers
59	37	description	1	View customer sub-accounts
59	40	description	1	Create product
59	41	description	1	Edit product
59	42	description	1	Delete product
59	43	description	1	View product details
59	44	description	1	Download payment CSV
59	50	description	1	Create product category
59	51	description	1	Edit product category
59	52	description	1	Delete product category
59	60	description	1	Create plan
59	61	description	1	Edit plan
59	62	description	1	Delete plan
59	63	description	1	View plan details
59	70	description	1	Delete invoice
59	71	description	1	Send invoice notification
59	72	description	1	View invoice details
59	73	description	1	Download invoice CSV
59	74	description	1	View all customers
59	75	description	1	View customer sub-accounts
59	80	description	1	Approve / Disapprove review
59	90	description	1	Show customer menu
59	91	description	1	Show invoices menu
59	92	description	1	Show order menu
59	93	description	1	Show payments & refunds menu
59	94	description	1	Show billing menu
59	95	description	1	Show mediation menu
59	96	description	1	Show reports menu
59	97	description	1	Show products menu
59	98	description	1	Show plans menu
59	99	description	1	Show configuration menu
59	100	description	1	Show partner menu
59	101	description	1	Create partner
59	102	description	1	Edit partner
59	103	description	1	Delete partner
59	104	description	1	View partner details
59	110	description	1	Switch to sub-account
59	111	description	1	Switch to any user
59	120	description	1	Web Service API access
17	5	description	1	All Orders
\.


--
-- Data for Name: invoice; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY invoice (id, create_datetime, billing_process_id, user_id, delegated_invoice_id, due_date, total, payment_attempts, status_id, balance, carried_balance, in_process_payment, is_review, currency_id, deleted, paper_invoice_batch_id, customer_notes, public_number, last_reminder, overdue_step, create_timestamp, optlock) FROM stdin;
\.


--
-- Data for Name: invoice_delivery_method; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY invoice_delivery_method (id) FROM stdin;
1
2
3
\.


--
-- Data for Name: invoice_line; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY invoice_line (id, invoice_id, type_id, amount, quantity, price, deleted, item_id, description, source_user_id, is_percentage, optlock) FROM stdin;
\.


--
-- Data for Name: invoice_line_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY invoice_line_type (id, description, order_position) FROM stdin;
1	item recurring	2
2	tax	6
3	due invoice	1
4	interests	4
5	sub account	5
6	item one-time	3
\.


--
-- Data for Name: invoice_meta_field_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY invoice_meta_field_map (invoice_id, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY item (id, internal_number, entity_id, percentage, deleted, has_decimals, optlock, gl_code) FROM stdin;
\.


--
-- Data for Name: item_meta_field_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY item_meta_field_map (item_id, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: item_price_timeline; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY item_price_timeline (item_id, price_model_id, start_date) FROM stdin;
\.


--
-- Data for Name: item_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY item_type (id, entity_id, description, order_line_type_id, optlock, internal) FROM stdin;
\.


--
-- Data for Name: item_type_exclude_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY item_type_exclude_map (item_id, type_id) FROM stdin;
\.


--
-- Data for Name: item_type_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY item_type_map (item_id, type_id) FROM stdin;
\.


--
-- Data for Name: jbilling_seqs; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY jbilling_seqs (name, next_id) FROM stdin;
enumeration	1
enumeration_values	1
entity_delivery_method_map	4
contact_field_type	10
user_role_map	13
entity_payment_method_map	26
currency_entity_map	10
user_credit_card_map	5
permission_role_map	1
permission_user	1
contact_map	6780
permission_type	9
period_unit	5
invoice_delivery_method	4
user_status	9
order_line_type	4
order_billing_type	3
order_status	5
pluggable_task_type_category	22
pluggable_task_type	91
invoice_line_type	6
currency	11
payment_method	9
payment_result	5
event_log_module	10
event_log_message	17
preference_type	37
notification_message_type	20
role	6
country	238
permission	120
currency_exchange	25
pluggable_task_parameter	1
billing_process_configuration	1
order_period	2
partner_range	1
item_price	1
partner	1
entity	1
contact_type	2
promotion	1
pluggable_task	1
ach	1
payment_info_cheque	1
partner_payout	1
process_run_total_pm	1
payment_authorization	1
billing_process	1
process_run	1
process_run_total	1
paper_invoice_batch	1
preference	1
notification_message	1
notification_message_section	1
notification_message_line	1
ageing_entity_step	1
item_type	1
item	1
event_log	1
purchase_order	1
order_line	1
invoice	1
invoice_line	1
order_process	1
payment	1
notification_message_arch	1
notification_message_arch_line	1
base_user	1
customer	1
contact	1
contact_field	1
credit_card	1
language	2
payment_invoice	1
subscriber_status	7
mediation_cfg	1
mediation_process	1
blacklist	1
mediation_record_line	1
generic_status	26
order_line_provisioning_status	1
balance_type	0
mediation_record	0
price_model	0
price_model_attribute	0
plan	0
plan_item	0
filter	0
filter_set	0
recent_item	0
breadcrumb	0
shortcut	0
report	0
report_type	0
report_parameter	0
plan_item_bundle	0
notification_category	2
\.


--
-- Data for Name: jbilling_table; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY jbilling_table (id, name) FROM stdin;
8	entity_delivery_method_map
99	contact_field_type
62	user_role_map
36	entity_payment_method_map
68	currency_entity_map
45	user_credit_card_map
61	permission_role_map
29	contact_map
58	permission_type
6	period_unit
7	invoice_delivery_method
9	user_status
18	order_line_type
19	order_billing_type
20	order_status
23	pluggable_task_type_category
24	pluggable_task_type
30	invoice_line_type
4	currency
35	payment_method
41	payment_result
46	event_log_module
47	event_log_message
50	preference_type
52	notification_message_type
60	role
64	country
59	permission
67	currency_exchange
26	pluggable_task_parameter
34	billing_process_configuration
17	order_period
79	partner_range
15	item_price
11	partner
5	entity
28	contact_type
65	promotion
25	pluggable_task
75	ach
43	payment_info_cheque
70	partner_payout
38	process_run_total_pm
66	payment_authorization
32	billing_process
33	process_run
37	process_run_total
31	paper_invoice_batch
51	preference
53	notification_message
54	notification_message_section
55	notification_message_line
69	ageing_entity_step
13	item_type
14	item
48	event_log
21	purchase_order
22	order_line
39	invoice
40	invoice_line
49	order_process
42	payment
56	notification_message_arch
57	notification_message_arch_line
10	base_user
12	customer
27	contact
76	contact_field
44	credit_card
3	language
80	payment_invoice
81	subscriber_status
82	mediation_cfg
83	mediation_process
85	blacklist
86	mediation_record_line
87	generic_status
88	order_line_provisioning_status
89	balance_type
90	invoice_status
91	mediation_record_status
92	process_run_status
95	plan
96	plan_item
97	customer_price
100	report
101	report_type
102	report_parameter
103	plan_item_bundle
104	notification_category
105	enumeration
106	enumeration_values
\.


--
-- Data for Name: language; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY language (id, code, description) FROM stdin;
1	en	English
2	pt	Portugu�s
\.


--
-- Data for Name: list_meta_field_values; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY list_meta_field_values (meta_field_value_id, list_value) FROM stdin;
\.


--
-- Data for Name: mediation_cfg; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY mediation_cfg (id, entity_id, create_datetime, name, order_value, pluggable_task_id, optlock) FROM stdin;
\.


--
-- Data for Name: mediation_errors; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY mediation_errors (accountcode, src, dst, dcontext, clid, channel, dstchannel, lastapp, lastdata, start_time, answer, end_time, duration, billsec, disposition, amaflags, userfield, error_message, should_retry) FROM stdin;
\.


--
-- Data for Name: mediation_order_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY mediation_order_map (mediation_process_id, order_id) FROM stdin;
\.


--
-- Data for Name: mediation_process; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY mediation_process (id, configuration_id, start_datetime, end_datetime, orders_affected, optlock) FROM stdin;
\.


--
-- Data for Name: mediation_record; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY mediation_record (id_key, start_datetime, mediation_process_id, optlock, status_id, id) FROM stdin;
\.


--
-- Data for Name: mediation_record_line; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY mediation_record_line (id, order_line_id, event_date, amount, quantity, description, optlock, mediation_record_id) FROM stdin;
\.


--
-- Data for Name: meta_field_name; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY meta_field_name (id, name, entity_type, data_type, is_disabled, is_mandatory, display_order, default_value_id, optlock, entity_id) FROM stdin;
\.


--
-- Data for Name: meta_field_value; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY meta_field_value (id, meta_field_name_id, dtype, boolean_value, date_value, decimal_value, integer_value, string_value) FROM stdin;
\.


--
-- Data for Name: my_test; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY my_test (id, entity_id, status_id, days, optlock) FROM stdin;
\.


--
-- Data for Name: notification_category; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_category (id) FROM stdin;
1
2
3
4
\.


--
-- Data for Name: notification_message; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_message (id, type_id, entity_id, language_id, use_flag, optlock) FROM stdin;
\.


--
-- Data for Name: notification_message_arch; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_message_arch (id, type_id, create_datetime, user_id, result_message, optlock) FROM stdin;
\.


--
-- Data for Name: notification_message_arch_line; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_message_arch_line (id, message_archive_id, section, content, optlock) FROM stdin;
\.


--
-- Data for Name: notification_message_line; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_message_line (id, message_section_id, content, optlock) FROM stdin;
\.


--
-- Data for Name: notification_message_section; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_message_section (id, message_id, section, optlock) FROM stdin;
\.


--
-- Data for Name: notification_message_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY notification_message_type (id, optlock, category_id) FROM stdin;
1	1	1
2	1	4
3	1	4
4	1	4
5	1	4
6	1	4
7	1	4
8	1	4
9	1	4
10	1	3
11	1	3
12	1	1
13	1	2
14	1	2
15	1	2
16	1	3
17	1	3
18	1	1
19	1	4
20	1	4
\.


--
-- Data for Name: order_billing_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY order_billing_type (id) FROM stdin;
1
2
\.


--
-- Data for Name: order_line; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY order_line (id, order_id, item_id, type_id, amount, quantity, price, item_price, create_datetime, deleted, description, provisioning_status, provisioning_request_id, optlock, use_item) FROM stdin;
\.


--
-- Data for Name: order_line_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY order_line_type (id, editable) FROM stdin;
1	1
2	0
3	0
\.


--
-- Data for Name: order_meta_field_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY order_meta_field_map (order_id, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: order_period; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY order_period (id, entity_id, value, unit_id, optlock) FROM stdin;
1	\N	\N	\N	1
5	\N	\N	\N	1
\.


--
-- Data for Name: order_process; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY order_process (id, order_id, invoice_id, billing_process_id, periods_included, period_start, period_end, is_review, origin, optlock) FROM stdin;
\.


--
-- Data for Name: paper_invoice_batch; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY paper_invoice_batch (id, total_invoices, delivery_date, is_self_managed, optlock) FROM stdin;
\.


--
-- Data for Name: partner; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY partner (id, user_id, balance, total_payments, total_refunds, total_payouts, percentage_rate, referral_fee, fee_currency_id, one_time, period_unit_id, period_value, next_payout_date, due_payout, automatic_process, related_clerk, optlock) FROM stdin;
\.


--
-- Data for Name: partner_meta_field_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY partner_meta_field_map (partner_id, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: partner_payout; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY partner_payout (id, starting_date, ending_date, payments_amount, refunds_amount, balance_left, payment_id, partner_id, optlock) FROM stdin;
\.


--
-- Data for Name: partner_range; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY partner_range (id, partner_id, percentage_rate, referral_fee, range_from, range_to, optlock) FROM stdin;
\.


--
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment (id, user_id, attempt, result_id, amount, create_datetime, update_datetime, payment_date, method_id, credit_card_id, deleted, is_refund, is_preauth, payment_id, currency_id, payout_id, ach_id, balance, optlock, payment_period, payment_notes) FROM stdin;
\.


--
-- Data for Name: payment_authorization; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment_authorization (id, payment_id, processor, code1, code2, code3, approval_code, avs, transaction_id, md5, card_code, create_datetime, response_message, optlock) FROM stdin;
\.


--
-- Data for Name: payment_info_cheque; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment_info_cheque (id, payment_id, bank, cheque_number, cheque_date, optlock) FROM stdin;
\.


--
-- Data for Name: payment_invoice; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment_invoice (id, payment_id, invoice_id, amount, create_datetime, optlock) FROM stdin;
\.


--
-- Data for Name: payment_meta_field_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment_meta_field_map (payment_id, meta_field_value_id) FROM stdin;
\.


--
-- Data for Name: payment_method; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment_method (id) FROM stdin;
1
2
3
4
5
6
7
8
9
\.


--
-- Data for Name: payment_result; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY payment_result (id) FROM stdin;
1
2
3
4
\.


--
-- Data for Name: period_unit; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY period_unit (id) FROM stdin;
1
2
3
4
\.


--
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY permission (id, type_id, foreign_id) FROM stdin;
10	1	\N
11	1	\N
12	1	\N
13	1	\N
14	1	\N
15	1	\N
16	1	\N
17	1	\N
18	1	\N
20	2	\N
21	2	\N
22	2	\N
23	2	\N
24	2	\N
25	2	\N
26	2	\N
27	2	\N
28	2	\N
29	2	\N
30	3	\N
31	3	\N
32	3	\N
33	3	\N
34	3	\N
35	3	\N
36	3	\N
37	3	\N
40	4	\N
41	4	\N
42	4	\N
43	4	\N
44	4	\N
50	5	\N
51	5	\N
52	5	\N
60	6	\N
61	6	\N
62	6	\N
63	6	\N
70	7	\N
71	7	\N
72	7	\N
73	7	\N
74	7	\N
75	7	\N
80	8	\N
90	9	\N
91	9	\N
92	9	\N
93	9	\N
94	9	\N
95	9	\N
96	9	\N
97	9	\N
98	9	\N
99	9	\N
100	9	\N
101	10	\N
102	10	\N
103	10	\N
104	10	\N
110	11	\N
111	11	\N
120	12	\N
\.


--
-- Data for Name: permission_role_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY permission_role_map (permission_id, role_id) FROM stdin;
10	2
11	2
12	2
13	2
14	2
15	2
16	2
17	2
20	2
21	2
22	2
23	2
24	2
25	2
26	2
27	2
28	2
30	2
31	2
32	2
33	2
34	2
35	2
36	2
40	2
41	2
42	2
43	2
44	2
50	2
51	2
52	2
60	2
61	2
62	2
63	2
70	2
71	2
72	2
73	2
74	2
80	2
90	2
91	2
92	2
93	2
94	2
95	2
96	2
97	2
98	2
99	2
100	2
101	2
102	2
103	2
104	2
111	2
120	2
10	3
11	3
12	3
13	3
14	3
15	3
17	3
20	3
21	3
22	3
23	3
24	3
28	3
30	3
31	3
32	3
33	3
34	3
36	3
40	3
41	3
42	3
43	3
50	3
51	3
52	3
60	3
61	3
62	3
63	3
70	3
71	3
72	3
74	3
90	3
91	3
92	3
93	3
94	3
95	3
96	3
97	3
98	3
100	3
101	3
102	3
103	3
104	3
15	4
10	4
11	4
24	4
28	4
20	4
21	4
34	4
36	4
30	4
72	4
74	4
90	4
91	4
92	4
93	4
15	5
18	5
24	5
29	5
30	5
34	5
37	5
72	5
75	5
90	5
91	5
92	5
93	5
\.


--
-- Data for Name: permission_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY permission_type (id, description) FROM stdin;
1	Customer
2	Order
3	Payment
4	Product
5	Product Category
6	Plan
7	Invoice
8	Billing
9	Menu
10	Partner
11	User Switching
12	API
\.


--
-- Data for Name: permission_user; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY permission_user (permission_id, user_id, is_grant, id) FROM stdin;
\.


--
-- Data for Name: plan; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY plan (id, item_id, description, period_id) FROM stdin;
\.


--
-- Data for Name: plan_item; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY plan_item (id, plan_id, item_id, precedence, plan_item_bundle_id) FROM stdin;
\.


--
-- Data for Name: plan_item_bundle; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY plan_item_bundle (id, quantity, period_id, target_customer, add_if_exists) FROM stdin;
\.


--
-- Data for Name: plan_item_price_timeline; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY plan_item_price_timeline (plan_item_id, price_model_id, start_date) FROM stdin;
\.


--
-- Data for Name: pluggable_task; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY pluggable_task (id, entity_id, type_id, processing_order, optlock, notes) FROM stdin;
\.


--
-- Data for Name: pluggable_task_parameter; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY pluggable_task_parameter (id, task_id, name, int_value, str_value, float_value, optlock) FROM stdin;
\.


--
-- Data for Name: pluggable_task_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY pluggable_task_type (id, category_id, class_name, min_parameters) FROM stdin;
1	1	com.sapienter.jbilling.server.pluggableTask.BasicLineTotalTask	0
13	4	com.sapienter.jbilling.server.pluggableTask.CalculateDueDateDfFm	0
3	4	com.sapienter.jbilling.server.pluggableTask.CalculateDueDate	0
4	4	com.sapienter.jbilling.server.pluggableTask.BasicCompositionTask	0
5	2	com.sapienter.jbilling.server.pluggableTask.BasicOrderFilterTask	0
6	3	com.sapienter.jbilling.server.pluggableTask.BasicInvoiceFilterTask	0
7	5	com.sapienter.jbilling.server.pluggableTask.BasicOrderPeriodTask	0
8	6	com.sapienter.jbilling.server.pluggableTask.PaymentAuthorizeNetTask	2
14	3	com.sapienter.jbilling.server.pluggableTask.NoInvoiceFilterTask	0
10	8	com.sapienter.jbilling.server.pluggableTask.BasicPaymentInfoTask	0
11	6	com.sapienter.jbilling.server.pluggableTask.PaymentPartnerTestTask	0
12	7	com.sapienter.jbilling.server.pluggableTask.PaperInvoiceNotificationTask	1
2	1	com.sapienter.jbilling.server.pluggableTask.GSTTaxTask	2
15	9	com.sapienter.jbilling.server.pluggableTask.BasicPenaltyTask	1
16	2	com.sapienter.jbilling.server.pluggableTask.OrderFilterAnticipatedTask	0
9	7	com.sapienter.jbilling.server.pluggableTask.BasicEmailNotificationTask	6
17	5	com.sapienter.jbilling.server.pluggableTask.OrderPeriodAnticipateTask	0
19	6	com.sapienter.jbilling.server.pluggableTask.PaymentEmailAuthorizeNetTask	1
20	10	com.sapienter.jbilling.server.pluggableTask.ProcessorEmailAlarmTask	3
21	6	com.sapienter.jbilling.server.pluggableTask.PaymentFakeTask	0
22	6	com.sapienter.jbilling.server.payment.tasks.PaymentRouterCCFTask	2
23	11	com.sapienter.jbilling.server.user.tasks.BasicSubscriptionStatusManagerTask	0
24	6	com.sapienter.jbilling.server.user.tasks.PaymentACHCommerceTask	5
25	12	com.sapienter.jbilling.server.payment.tasks.NoAsyncParameters	0
26	12	com.sapienter.jbilling.server.payment.tasks.RouterAsyncParameters	0
28	13	com.sapienter.jbilling.server.item.tasks.BasicItemManager	0
29	13	com.sapienter.jbilling.server.item.tasks.RulesItemManager	0
30	1	com.sapienter.jbilling.server.order.task.RulesLineTotalTask	0
31	14	com.sapienter.jbilling.server.item.tasks.RulesPricingTask	0
32	15	com.sapienter.jbilling.server.mediation.task.SeparatorFileReader	2
33	16	com.sapienter.jbilling.server.mediation.task.RulesMediationTask	0
34	15	com.sapienter.jbilling.server.mediation.task.FixedFileReader	2
35	8	com.sapienter.jbilling.server.user.tasks.PaymentInfoNoValidateTask	0
36	7	com.sapienter.jbilling.server.notification.task.TestNotificationTask	0
37	5	com.sapienter.jbilling.server.process.task.ProRateOrderPeriodTask	0
38	4	com.sapienter.jbilling.server.process.task.DailyProRateCompositionTask	0
39	6	com.sapienter.jbilling.server.payment.tasks.PaymentAtlasTask	5
40	17	com.sapienter.jbilling.server.order.task.RefundOnCancelTask	0
41	17	com.sapienter.jbilling.server.order.task.CancellationFeeRulesTask	1
42	6	com.sapienter.jbilling.server.payment.tasks.PaymentFilterTask	0
43	17	com.sapienter.jbilling.server.payment.blacklist.tasks.BlacklistUserStatusTask	0
44	15	com.sapienter.jbilling.server.mediation.task.JDBCReader	0
45	15	com.sapienter.jbilling.server.mediation.task.MySQLReader	0
46	17	com.sapienter.jbilling.server.provisioning.task.ProvisioningCommandsRulesTask	0
47	18	com.sapienter.jbilling.server.provisioning.task.TestExternalProvisioningTask	0
48	18	com.sapienter.jbilling.server.provisioning.task.CAIProvisioningTask	2
49	6	com.sapienter.jbilling.server.payment.tasks.PaymentRouterCurrencyTask	2
50	18	com.sapienter.jbilling.server.provisioning.task.MMSCProvisioningTask	5
51	3	com.sapienter.jbilling.server.invoice.task.NegativeBalanceInvoiceFilterTask	0
52	17	com.sapienter.jbilling.server.invoice.task.FileInvoiceExportTask	1
53	17	com.sapienter.jbilling.server.system.event.task.InternalEventsRulesTask	0
54	17	com.sapienter.jbilling.server.user.balance.DynamicBalanceManagerTask	0
55	19	com.sapienter.jbilling.server.user.tasks.UserBalanceValidatePurchaseTask	0
56	19	com.sapienter.jbilling.server.user.tasks.RulesValidatePurchaseTask	0
57	6	com.sapienter.jbilling.server.payment.tasks.PaymentsGatewayTask	4
58	17	com.sapienter.jbilling.server.payment.tasks.SaveCreditCardExternallyTask	1
59	13	com.sapienter.jbilling.server.order.task.RulesItemManager2	0
60	1	com.sapienter.jbilling.server.order.task.RulesLineTotalTask2	0
61	14	com.sapienter.jbilling.server.item.tasks.RulesPricingTask2	0
62	17	com.sapienter.jbilling.server.payment.tasks.SaveCreditCardExternallyTask	1
63	6	com.sapienter.jbilling.server.pluggableTask.PaymentFakeExternalStorage	0
64	6	com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayTask	3
65	6	com.sapienter.jbilling.server.payment.tasks.PaymentWorldPayExternalTask	3
66	17	com.sapienter.jbilling.server.user.tasks.AutoRechargeTask	0
67	6	com.sapienter.jbilling.server.payment.tasks.PaymentBeanstreamTask	3
68	6	com.sapienter.jbilling.server.payment.tasks.PaymentSageTask	2
69	20	com.sapienter.jbilling.server.process.task.BasicBillingProcessFilterTask	0
70	20	com.sapienter.jbilling.server.process.task.BillableUsersBillingProcessFilterTask	0
71	21	com.sapienter.jbilling.server.mediation.task.SaveToFileMediationErrorHandler	0
73	21	com.sapienter.jbilling.server.mediation.task.SaveToJDBCMediationErrorHandler	1
75	6	com.sapienter.jbilling.server.payment.tasks.PaymentPaypalExternalTask	3
76	6	com.sapienter.jbilling.server.payment.tasks.PaymentAuthorizeNetCIMTask	2
77	6	com.sapienter.jbilling.server.payment.tasks.PaymentMethodRouterTask	4
78	23	com.sapienter.jbilling.server.rule.task.VelocityRulesGeneratorTask	2
79	14	com.sapienter.jbilling.server.pricing.tasks.PriceModelPricingTask	0
81	22	com.sapienter.jbilling.server.mediation.task.MediationProcessTask	0
82	22	com.sapienter.jbilling.server.billing.task.BillingProcessTask	0
83	22	com.sapienter.jbilling.server.process.task.ScpUploadTask	4
84	17	com.sapienter.jbilling.server.payment.tasks.SaveACHExternallyTask	1
85	20	com.sapienter.jbilling.server.process.task.BillableUserOrdersBillingProcessFilterTask	0
86	17	com.sapienter.jbilling.server.item.tasks.PlanChangesExternalTask	0
87	24	com.sapienter.jbilling.server.process.task.BasicAgeingTask	0
88	22	com.sapienter.jbilling.server.process.task.AgeingProcessTask	0
89	24	com.sapienter.jbilling.server.process.task.BusinessDayAgeingTask	0
90	4	com.sapienter.jbilling.server.process.task.SimpleTaxCompositionTask	1
91	4	com.sapienter.jbilling.server.process.task.CountryTaxCompositionTask	2
92	4	com.sapienter.jbilling.server.process.task.PaymentTermPenaltyTask	2
93	17	com.sapienter.jbilling.server.order.task.CancellationFeeTask	2
\.


--
-- Data for Name: pluggable_task_type_category; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY pluggable_task_type_category (id, interface_name) FROM stdin;
1	com.sapienter.jbilling.server.pluggableTask.OrderProcessingTask
2	com.sapienter.jbilling.server.pluggableTask.OrderFilterTask
3	com.sapienter.jbilling.server.pluggableTask.InvoiceFilterTask
4	com.sapienter.jbilling.server.pluggableTask.InvoiceCompositionTask
5	com.sapienter.jbilling.server.pluggableTask.OrderPeriodTask
6	com.sapienter.jbilling.server.pluggableTask.PaymentTask
7	com.sapienter.jbilling.server.pluggableTask.NotificationTask
8	com.sapienter.jbilling.server.pluggableTask.PaymentInfoTask
9	com.sapienter.jbilling.server.pluggableTask.PenaltyTask
10	com.sapienter.jbilling.server.pluggableTask.ProcessorAlarm
11	com.sapienter.jbilling.server.user.tasks.ISubscriptionStatusManager
12	com.sapienter.jbilling.server.payment.tasks.IAsyncPaymentParameters
13	com.sapienter.jbilling.server.item.tasks.IItemPurchaseManager
14	com.sapienter.jbilling.server.item.tasks.IPricing
15	com.sapienter.jbilling.server.mediation.task.IMediationReader
16	com.sapienter.jbilling.server.mediation.task.IMediationProcess
17	com.sapienter.jbilling.server.system.event.task.IInternalEventsTask
18	com.sapienter.jbilling.server.provisioning.task.IExternalProvisioning
19	com.sapienter.jbilling.server.user.tasks.IValidatePurchaseTask
20	com.sapienter.jbilling.server.process.task.IBillingProcessFilterTask
21	com.sapienter.jbilling.server.mediation.task.IMediationErrorHandler
22	com.sapienter.jbilling.server.process.task.IScheduledTask
23	com.sapienter.jbilling.server.rule.task.IRulesGenerator
24	com.sapienter.jbilling.server.process.task.IAgeingTask
\.


--
-- Data for Name: preference; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY preference (id, type_id, table_id, foreign_id, value) FROM stdin;
\.


--
-- Data for Name: preference_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY preference_type (id, def_value) FROM stdin;
4	\N
5	\N
6	\N
7	\N
8	\N
9	\N
10	\N
11	\N
12	\N
13	\N
14	\N
15	\N
16	\N
17	\N
18	\N
19	1
20	1
21	0
22	\N
23	\N
24	0
25	0
27	0
28	\N
29	https://www.paypal.com/en_US/i/btn/x-click-but6.gif
30	\N
31	2000-01-01
32	0
33	0
35	0
36	1
37	0
38	1
39	0
40	0
41	0
42	0
43	0
44	0
45	0
46	0
47	0
48	1
49	\N
50	2
\.


--
-- Data for Name: price_model; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY price_model (id, strategy_type, rate, included_quantity, currency_id, next_model_id) FROM stdin;
\.


--
-- Data for Name: price_model_attribute; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY price_model_attribute (price_model_id, attribute_name, attribute_value) FROM stdin;
\.


--
-- Data for Name: process_run; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY process_run (id, process_id, run_date, started, finished, payment_finished, invoices_generated, optlock, status_id) FROM stdin;
\.


--
-- Data for Name: process_run_total; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY process_run_total (id, process_run_id, currency_id, total_invoiced, total_paid, total_not_paid, optlock) FROM stdin;
\.


--
-- Data for Name: process_run_total_pm; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY process_run_total_pm (id, process_run_total_id, payment_method_id, total, optlock) FROM stdin;
\.


--
-- Data for Name: process_run_user; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY process_run_user (id, process_run_id, user_id, status, created, optlock) FROM stdin;
\.


--
-- Data for Name: promotion; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY promotion (id, item_id, code, notes, once, since, until) FROM stdin;
\.


--
-- Data for Name: promotion_user_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY promotion_user_map (user_id, promotion_id) FROM stdin;
\.


--
-- Data for Name: purchase_order; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY purchase_order (id, user_id, period_id, billing_type_id, active_since, active_until, cycle_start, create_datetime, next_billable_day, created_by, status_id, currency_id, deleted, notify, last_notified, notification_step, due_date_unit_id, due_date_value, df_fm, anticipate_periods, own_invoice, notes, notes_in_invoice, is_current, optlock) FROM stdin;
\.


--
-- Data for Name: rate_card; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY rate_card (id, name, table_name, entity_id) FROM stdin;
\.


--
-- Data for Name: recent_item; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY recent_item (id, type, object_id, user_id, version) FROM stdin;
\.


--
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY report (id, type_id, name, file_name, optlock) FROM stdin;
1	1	total_invoiced	total_invoiced.jasper	0
2	1	ageing_balance	ageing_balance.jasper	0
3	2	product_subscribers	product_subscribers.jasper	0
4	3	total_payments	total_payments.jasper	0
5	4	user_signups	user_signups.jasper	0
6	4	top_customers	top_customers.jasper	0
7	1	accounts_receivable	accounts_receivable.jasper	0
8	1	gl_detail	gl_detail.jasper	0
9	1	gl_summary	gl_summary.jasper	0
10	4	plan_history	plan_history.jasper	0
11	4	total_invoiced_per_customer	total_invoiced_per_customer.jasper	0
12	4	total_invoiced_per_customer_over_years	total_invoiced_per_customer_over_years.jasper	0
\.


--
-- Data for Name: report_parameter; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY report_parameter (id, report_id, dtype, name) FROM stdin;
1	1	date	start_date
2	1	date	end_date
3	1	integer	period
4	3	integer	item_id
5	4	date	start_date
6	4	date	end_date
7	4	integer	period
8	5	date	start_date
9	5	date	end_date
10	5	integer	period
11	6	date	start_date
12	6	date	end_date
13	8	date	date
14	9	date	date
15	10	integer	plan_id
16	10	string	plan_code
17	10	string	plan_description
18	11	date	start_date
19	11	date	end_date
20	12	string	start_year
21	12	string	end_year
\.


--
-- Data for Name: report_type; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY report_type (id, name, optlock) FROM stdin;
1	invoice	0
2	order	0
3	payment	0
4	user	0
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY role (id, entity_id, role_type_id) FROM stdin;
2	\N	2
3	\N	3
4	\N	4
5	\N	5
\.


--
-- Data for Name: shortcut; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY shortcut (id, user_id, controller, action, name, object_id, version) FROM stdin;
\.


--
-- Data for Name: user_credit_card_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY user_credit_card_map (user_id, credit_card_id) FROM stdin;
\.


--
-- Data for Name: user_role_map; Type: TABLE DATA; Schema: public; Owner: jbilling
--

COPY user_role_map (user_id, role_id) FROM stdin;
\.


--
-- Name: ach_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY ach
    ADD CONSTRAINT ach_pkey PRIMARY KEY (id);


--
-- Name: ageing_entity_step_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_pkey PRIMARY KEY (id);


--
-- Name: base_user_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_pkey PRIMARY KEY (id);


--
-- Name: billing_process_config_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY billing_process_configuration
    ADD CONSTRAINT billing_process_config_pkey PRIMARY KEY (id);


--
-- Name: billing_process_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_pkey PRIMARY KEY (id);


--
-- Name: blacklist_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY blacklist
    ADD CONSTRAINT blacklist_pkey PRIMARY KEY (id);


--
-- Name: breadcrumb_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY breadcrumb
    ADD CONSTRAINT breadcrumb_pkey PRIMARY KEY (id);


--
-- Name: cdrentries_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY cdrentries
    ADD CONSTRAINT cdrentries_pkey PRIMARY KEY (id);


--
-- Name: contact_map_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_pkey PRIMARY KEY (id);


--
-- Name: contact_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- Name: contact_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY contact_type
    ADD CONSTRAINT contact_type_pkey PRIMARY KEY (id);


--
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- Name: credit_card_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY credit_card
    ADD CONSTRAINT credit_card_pkey PRIMARY KEY (id);


--
-- Name: currency_exchange_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY currency_exchange
    ADD CONSTRAINT currency_exchange_pkey PRIMARY KEY (id);


--
-- Name: currency_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY currency
    ADD CONSTRAINT currency_pkey PRIMARY KEY (id);


--
-- Name: customer_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- Name: customer_price_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY customer_price
    ADD CONSTRAINT customer_price_pkey PRIMARY KEY (plan_item_id, user_id);


--
-- Name: entity_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT entity_pkey PRIMARY KEY (id);


--
-- Name: enumeration_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY enumeration
    ADD CONSTRAINT enumeration_pkey PRIMARY KEY (id);


--
-- Name: enumeration_values_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY enumeration_values
    ADD CONSTRAINT enumeration_values_pkey PRIMARY KEY (id);


--
-- Name: event_log_message_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY event_log_message
    ADD CONSTRAINT event_log_message_pkey PRIMARY KEY (id);


--
-- Name: event_log_module_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY event_log_module
    ADD CONSTRAINT event_log_module_pkey PRIMARY KEY (id);


--
-- Name: event_log_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_pkey PRIMARY KEY (id);


--
-- Name: filter_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY filter
    ADD CONSTRAINT filter_pkey PRIMARY KEY (id);


--
-- Name: filter_set_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY filter_set
    ADD CONSTRAINT filter_set_pkey PRIMARY KEY (id);


--
-- Name: generic_status_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY generic_status
    ADD CONSTRAINT generic_status_pkey PRIMARY KEY (id);


--
-- Name: generic_status_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY generic_status_type
    ADD CONSTRAINT generic_status_type_pkey PRIMARY KEY (id);


--
-- Name: international_description_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY international_description
    ADD CONSTRAINT international_description_pkey PRIMARY KEY (table_id, foreign_id, psudo_column, language_id);


--
-- Name: invoice_delivery_method_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY invoice_delivery_method
    ADD CONSTRAINT invoice_delivery_method_pkey PRIMARY KEY (id);


--
-- Name: invoice_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_pkey PRIMARY KEY (id);


--
-- Name: invoice_line_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY invoice_line_type
    ADD CONSTRAINT invoice_line_type_pkey PRIMARY KEY (id);


--
-- Name: invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);


--
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- Name: item_price_timeline_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY item_price_timeline
    ADD CONSTRAINT item_price_timeline_pkey PRIMARY KEY (item_id, start_date);


--
-- Name: item_type_exclude_map_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY item_type_exclude_map
    ADD CONSTRAINT item_type_exclude_map_pkey PRIMARY KEY (item_id, type_id);


--
-- Name: item_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY item_type
    ADD CONSTRAINT item_type_pkey PRIMARY KEY (id);


--
-- Name: itmprctimelnprc_model_id_key; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY item_price_timeline
    ADD CONSTRAINT itmprctimelnprc_model_id_key UNIQUE (price_model_id);


--
-- Name: jbilling_table_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY jbilling_table
    ADD CONSTRAINT jbilling_table_pkey PRIMARY KEY (id);


--
-- Name: language_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- Name: mediation_cfg_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY mediation_cfg
    ADD CONSTRAINT mediation_cfg_pkey PRIMARY KEY (id);


--
-- Name: mediation_errors_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY mediation_errors
    ADD CONSTRAINT mediation_errors_pkey PRIMARY KEY (accountcode);


--
-- Name: mediation_process_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY mediation_process
    ADD CONSTRAINT mediation_process_pkey PRIMARY KEY (id);


--
-- Name: mediation_record_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY mediation_record_line
    ADD CONSTRAINT mediation_record_line_pkey PRIMARY KEY (id);


--
-- Name: mediation_record_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY mediation_record
    ADD CONSTRAINT mediation_record_pkey PRIMARY KEY (id);


--
-- Name: meta_field_name_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY meta_field_name
    ADD CONSTRAINT meta_field_name_pkey PRIMARY KEY (id);


--
-- Name: meta_field_value_id_key; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY meta_field_value
    ADD CONSTRAINT meta_field_value_id_key UNIQUE (id);


--
-- Name: my_test_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY my_test
    ADD CONSTRAINT my_test_pkey PRIMARY KEY (id);


--
-- Name: notification_category_pk; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_category
    ADD CONSTRAINT notification_category_pk PRIMARY KEY (id);


--
-- Name: notifictn_msg_arch_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_message_arch_line
    ADD CONSTRAINT notifictn_msg_arch_line_pkey PRIMARY KEY (id);


--
-- Name: notifictn_msg_arch_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_message_arch
    ADD CONSTRAINT notifictn_msg_arch_pkey PRIMARY KEY (id);


--
-- Name: notifictn_msg_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_message_line
    ADD CONSTRAINT notifictn_msg_line_pkey PRIMARY KEY (id);


--
-- Name: notifictn_msg_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notifictn_msg_pkey PRIMARY KEY (id);


--
-- Name: notifictn_msg_section_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_message_section
    ADD CONSTRAINT notifictn_msg_section_pkey PRIMARY KEY (id);


--
-- Name: notifictn_msg_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY notification_message_type
    ADD CONSTRAINT notifictn_msg_type_pkey PRIMARY KEY (id);


--
-- Name: order_billing_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY order_billing_type
    ADD CONSTRAINT order_billing_type_pkey PRIMARY KEY (id);


--
-- Name: order_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_pkey PRIMARY KEY (id);


--
-- Name: order_line_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY order_line_type
    ADD CONSTRAINT order_line_type_pkey PRIMARY KEY (id);


--
-- Name: order_period_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY order_period
    ADD CONSTRAINT order_period_pkey PRIMARY KEY (id);


--
-- Name: order_process_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY order_process
    ADD CONSTRAINT order_process_pkey PRIMARY KEY (id);


--
-- Name: paper_invoice_batch_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY paper_invoice_batch
    ADD CONSTRAINT paper_invoice_batch_pkey PRIMARY KEY (id);


--
-- Name: partner_payout_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY partner_payout
    ADD CONSTRAINT partner_payout_pkey PRIMARY KEY (id);


--
-- Name: partner_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_pkey PRIMARY KEY (id);


--
-- Name: partner_range_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY partner_range
    ADD CONSTRAINT partner_range_pkey PRIMARY KEY (id);


--
-- Name: payment_authorization_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY payment_authorization
    ADD CONSTRAINT payment_authorization_pkey PRIMARY KEY (id);


--
-- Name: payment_info_cheque_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY payment_info_cheque
    ADD CONSTRAINT payment_info_cheque_pkey PRIMARY KEY (id);


--
-- Name: payment_invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY payment_invoice
    ADD CONSTRAINT payment_invoice_pkey PRIMARY KEY (id);


--
-- Name: payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (id);


--
-- Name: payment_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- Name: payment_result_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY payment_result
    ADD CONSTRAINT payment_result_pkey PRIMARY KEY (id);


--
-- Name: period_unit_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY period_unit
    ADD CONSTRAINT period_unit_pkey PRIMARY KEY (id);


--
-- Name: permission_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- Name: permission_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY permission_type
    ADD CONSTRAINT permission_type_pkey PRIMARY KEY (id);


--
-- Name: permission_user_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY permission_user
    ADD CONSTRAINT permission_user_pkey PRIMARY KEY (id);


--
-- Name: pk_databasechangelog; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY databasechangelog
    ADD CONSTRAINT pk_databasechangelog PRIMARY KEY (id, author, filename);


--
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- Name: plan_item_bundle_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY plan_item_bundle
    ADD CONSTRAINT plan_item_bundle_pkey PRIMARY KEY (id);


--
-- Name: plan_item_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY plan_item
    ADD CONSTRAINT plan_item_pkey PRIMARY KEY (id);


--
-- Name: plan_item_price_timeline_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY plan_item_price_timeline
    ADD CONSTRAINT plan_item_price_timeline_pkey PRIMARY KEY (plan_item_id, start_date);


--
-- Name: plan_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT plan_pkey PRIMARY KEY (id);


--
-- Name: plnitmprctimelnprc_mdl_key; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY plan_item_price_timeline
    ADD CONSTRAINT plnitmprctimelnprc_mdl_key UNIQUE (price_model_id);


--
-- Name: pluggable_task_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY pluggable_task_parameter
    ADD CONSTRAINT pluggable_task_parameter_pkey PRIMARY KEY (id);


--
-- Name: pluggable_task_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY pluggable_task
    ADD CONSTRAINT pluggable_task_pkey PRIMARY KEY (id);


--
-- Name: pluggable_task_type_cat_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY pluggable_task_type_category
    ADD CONSTRAINT pluggable_task_type_cat_pkey PRIMARY KEY (id);


--
-- Name: pluggable_task_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY pluggable_task_type
    ADD CONSTRAINT pluggable_task_type_pkey PRIMARY KEY (id);


--
-- Name: preference_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY preference
    ADD CONSTRAINT preference_pkey PRIMARY KEY (id);


--
-- Name: preference_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY preference_type
    ADD CONSTRAINT preference_type_pkey PRIMARY KEY (id);


--
-- Name: price_model_attribute_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY price_model_attribute
    ADD CONSTRAINT price_model_attribute_pkey PRIMARY KEY (price_model_id, attribute_name);


--
-- Name: price_model_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY price_model
    ADD CONSTRAINT price_model_pkey PRIMARY KEY (id);


--
-- Name: process_run_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY process_run
    ADD CONSTRAINT process_run_pkey PRIMARY KEY (id);


--
-- Name: process_run_total_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY process_run_total
    ADD CONSTRAINT process_run_total_pkey PRIMARY KEY (id);


--
-- Name: process_run_total_pm_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY process_run_total_pm
    ADD CONSTRAINT process_run_total_pm_pkey PRIMARY KEY (id);


--
-- Name: process_run_user_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY process_run_user
    ADD CONSTRAINT process_run_user_pkey PRIMARY KEY (id);


--
-- Name: promotion_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY promotion
    ADD CONSTRAINT promotion_pkey PRIMARY KEY (id);


--
-- Name: purchase_order_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_pkey PRIMARY KEY (id);


--
-- Name: rate_card_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY rate_card
    ADD CONSTRAINT rate_card_pkey PRIMARY KEY (id);


--
-- Name: rate_card_table_name_key; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY rate_card
    ADD CONSTRAINT rate_card_table_name_key UNIQUE (table_name);


--
-- Name: recent_item_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY recent_item
    ADD CONSTRAINT recent_item_pkey PRIMARY KEY (id);


--
-- Name: report_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY report_parameter
    ADD CONSTRAINT report_parameter_pkey PRIMARY KEY (id);


--
-- Name: report_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_pkey PRIMARY KEY (id);


--
-- Name: report_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY report_type
    ADD CONSTRAINT report_type_pkey PRIMARY KEY (id);


--
-- Name: role_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: shortcut_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling; Tablespace: 
--

ALTER TABLE ONLY shortcut
    ADD CONSTRAINT shortcut_pkey PRIMARY KEY (id);


--
-- Name: bp_pm_index_total; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX bp_pm_index_total ON process_run_total_pm USING btree (process_run_total_id);


--
-- Name: contact_i_del; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX contact_i_del ON contact USING btree (deleted);


--
-- Name: contact_map_i_3; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX contact_map_i_3 ON contact_map USING btree (table_id, foreign_id, type_id);


--
-- Name: create_datetime; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX create_datetime ON payment_authorization USING btree (create_datetime);


--
-- Name: currency_entity_map_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX currency_entity_map_i_2 ON currency_entity_map USING btree (currency_id, entity_id);


--
-- Name: international_description_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX international_description_i_2 ON international_description USING btree (table_id, foreign_id, language_id);


--
-- Name: ix_base_user_un; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_base_user_un ON base_user USING btree (entity_id, user_name);


--
-- Name: ix_blacklist_entity_type; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_blacklist_entity_type ON blacklist USING btree (entity_id, type);


--
-- Name: ix_blacklist_user_type; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_blacklist_user_type ON blacklist USING btree (user_id, type);


--
-- Name: ix_cc_number; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_cc_number ON credit_card USING btree (cc_number_plain);


--
-- Name: ix_cc_number_encrypted; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_cc_number_encrypted ON credit_card USING btree (cc_number);


--
-- Name: ix_contact_address; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_contact_address ON contact USING btree (street_addres1, city, postal_code, street_addres2, state_province, country_code);


--
-- Name: ix_contact_fname; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_contact_fname ON contact USING btree (first_name);


--
-- Name: ix_contact_fname_lname; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_contact_fname_lname ON contact USING btree (first_name, last_name);


--
-- Name: ix_contact_lname; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_contact_lname ON contact USING btree (last_name);


--
-- Name: ix_contact_orgname; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_contact_orgname ON contact USING btree (organization_name);


--
-- Name: ix_contact_phone; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_contact_phone ON contact USING btree (phone_phone_number, phone_area_code, phone_country_code);


--
-- Name: ix_el_main; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_el_main ON event_log USING btree (module_id, message_id, create_datetime);


--
-- Name: ix_invoice_date; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_invoice_date ON invoice USING btree (create_datetime);


--
-- Name: ix_invoice_due_date; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_invoice_due_date ON invoice USING btree (user_id, due_date);


--
-- Name: ix_invoice_number; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_invoice_number ON invoice USING btree (user_id, public_number);


--
-- Name: ix_invoice_ts; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_invoice_ts ON invoice USING btree (create_timestamp, user_id);


--
-- Name: ix_invoice_user_id; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_invoice_user_id ON invoice USING btree (user_id, deleted);


--
-- Name: ix_item_ent; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_item_ent ON item USING btree (entity_id, internal_number);


--
-- Name: ix_order_process_in; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_order_process_in ON order_process USING btree (invoice_id);


--
-- Name: ix_promotion_code; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_promotion_code ON promotion USING btree (code);


--
-- Name: ix_purchase_order_date; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_purchase_order_date ON purchase_order USING btree (user_id, create_datetime);


--
-- Name: ix_uq_order_process_or_bp; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_uq_order_process_or_bp ON order_process USING btree (order_id, billing_process_id);


--
-- Name: ix_uq_order_process_or_in; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_uq_order_process_or_in ON order_process USING btree (order_id, invoice_id);


--
-- Name: ix_uq_payment_inv_map_pa_in; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX ix_uq_payment_inv_map_pa_in ON payment_invoice USING btree (payment_id, invoice_id);


--
-- Name: mediation_record_i; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX mediation_record_i ON mediation_record USING btree (id_key, status_id);


--
-- Name: partner_range_p; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX partner_range_p ON partner_range USING btree (partner_id);


--
-- Name: payment_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX payment_i_2 ON payment USING btree (user_id, create_datetime);


--
-- Name: payment_i_3; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX payment_i_3 ON payment USING btree (user_id, balance);


--
-- Name: permission_role_map_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX permission_role_map_i_2 ON permission_role_map USING btree (permission_id, role_id);


--
-- Name: permission_user_map_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX permission_user_map_i_2 ON permission_user USING btree (permission_id, user_id);


--
-- Name: plan_item_item_id_i; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX plan_item_item_id_i ON plan_item USING btree (precedence);


--
-- Name: promotion_user_map_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX promotion_user_map_i_2 ON promotion_user_map USING btree (user_id, promotion_id);


--
-- Name: purchase_order_i_notif; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX purchase_order_i_notif ON purchase_order USING btree (active_until, notification_step);


--
-- Name: purchase_order_i_user; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX purchase_order_i_user ON purchase_order USING btree (user_id, deleted);


--
-- Name: transaction_id; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX transaction_id ON payment_authorization USING btree (transaction_id);


--
-- Name: user_credit_card_map_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX user_credit_card_map_i_2 ON user_credit_card_map USING btree (user_id, credit_card_id);


--
-- Name: user_role_map_i_2; Type: INDEX; Schema: public; Owner: jbilling; Tablespace: 
--

CREATE INDEX user_role_map_i_2 ON user_role_map USING btree (user_id, role_id);


--
-- Name: ach_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY ach
    ADD CONSTRAINT ach_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: ageing_entity_step_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: base_user_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_3 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: base_user_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_4 FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: base_user_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_5 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: billing_proc_configtn_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY billing_process_configuration
    ADD CONSTRAINT billing_proc_configtn_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit(id);


--
-- Name: billing_proc_configtn_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY billing_process_configuration
    ADD CONSTRAINT billing_proc_configtn_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: billing_process_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit(id);


--
-- Name: billing_process_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: billing_process_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_fk_3 FOREIGN KEY (paper_invoice_batch_id) REFERENCES paper_invoice_batch(id);


--
-- Name: blacklist_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY blacklist
    ADD CONSTRAINT blacklist_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: blacklist_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY blacklist
    ADD CONSTRAINT blacklist_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: blacklist_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY blacklist
    ADD CONSTRAINT blacklist_fk_4 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: category_id_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message_type
    ADD CONSTRAINT category_id_fk_1 FOREIGN KEY (category_id) REFERENCES notification_category(id);


--
-- Name: contact_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_fk_1 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- Name: contact_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_fk_2 FOREIGN KEY (type_id) REFERENCES contact_type(id);


--
-- Name: contact_map_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_fk_3 FOREIGN KEY (contact_id) REFERENCES contact(id);


--
-- Name: contact_type_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY contact_type
    ADD CONSTRAINT contact_type_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: currency_entity_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY currency_entity_map
    ADD CONSTRAINT currency_entity_map_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: currency_entity_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY currency_entity_map
    ADD CONSTRAINT currency_entity_map_fk_2 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: currency_exchange_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY currency_exchange
    ADD CONSTRAINT currency_exchange_fk_1 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: customer_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_fk_1 FOREIGN KEY (invoice_delivery_method_id) REFERENCES invoice_delivery_method(id);


--
-- Name: customer_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_fk_2 FOREIGN KEY (partner_id) REFERENCES partner(id);


--
-- Name: customer_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_fk_3 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: customer_meta_field_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer_meta_field_map
    ADD CONSTRAINT customer_meta_field_map_fk_1 FOREIGN KEY (customer_id) REFERENCES customer(id);


--
-- Name: customer_meta_field_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer_meta_field_map
    ADD CONSTRAINT customer_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: customer_price_plan_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer_price
    ADD CONSTRAINT customer_price_plan_item_id_fk FOREIGN KEY (plan_item_id) REFERENCES plan_item(id);


--
-- Name: customer_price_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY customer_price
    ADD CONSTRAINT customer_price_user_id_fk FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: entity_delivry_methd_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity_delivery_method_map
    ADD CONSTRAINT entity_delivry_methd_map_fk1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: entity_delivry_methd_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity_delivery_method_map
    ADD CONSTRAINT entity_delivry_methd_map_fk2 FOREIGN KEY (method_id) REFERENCES invoice_delivery_method(id);


--
-- Name: entity_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT entity_fk_1 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: entity_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT entity_fk_2 FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: entity_payment_method_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity_payment_method_map
    ADD CONSTRAINT entity_payment_method_map_fk_1 FOREIGN KEY (payment_method_id) REFERENCES payment_method(id);


--
-- Name: entity_payment_method_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity_payment_method_map
    ADD CONSTRAINT entity_payment_method_map_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: enumeration_values_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY enumeration_values
    ADD CONSTRAINT enumeration_values_fk_1 FOREIGN KEY (enumeration_id) REFERENCES enumeration(id);


--
-- Name: event_log_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_1 FOREIGN KEY (module_id) REFERENCES event_log_module(id);


--
-- Name: event_log_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: event_log_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_3 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: event_log_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_4 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- Name: event_log_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_5 FOREIGN KEY (message_id) REFERENCES event_log_message(id);


--
-- Name: event_log_fk_6; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_6 FOREIGN KEY (affected_user_id) REFERENCES base_user(id);


--
-- Name: generic_status_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY generic_status
    ADD CONSTRAINT generic_status_fk_1 FOREIGN KEY (dtype) REFERENCES generic_status_type(id);


--
-- Name: international_description_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY international_description
    ADD CONSTRAINT international_description_fk_1 FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: invoice_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_1 FOREIGN KEY (billing_process_id) REFERENCES billing_process(id);


--
-- Name: invoice_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_2 FOREIGN KEY (paper_invoice_batch_id) REFERENCES paper_invoice_batch(id);


--
-- Name: invoice_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_3 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: invoice_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_4 FOREIGN KEY (delegated_invoice_id) REFERENCES invoice(id);


--
-- Name: invoice_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice(id);


--
-- Name: invoice_line_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_fk_2 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: invoice_line_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_fk_3 FOREIGN KEY (type_id) REFERENCES invoice_line_type(id);


--
-- Name: invoice_meta_field_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice_meta_field_map
    ADD CONSTRAINT invoice_meta_field_map_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice(id);


--
-- Name: invoice_meta_field_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY invoice_meta_field_map
    ADD CONSTRAINT invoice_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: item_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: item_meta_field_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_meta_field_map
    ADD CONSTRAINT item_meta_field_map_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: item_meta_field_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_meta_field_map
    ADD CONSTRAINT item_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: item_pm_map_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_price_timeline
    ADD CONSTRAINT item_pm_map_item_id_fk FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: item_pm_map_price_model_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_price_timeline
    ADD CONSTRAINT item_pm_map_price_model_id_fk FOREIGN KEY (price_model_id) REFERENCES price_model(id);


--
-- Name: item_type_exclude_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_type_exclude_map
    ADD CONSTRAINT item_type_exclude_item_id_fk FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: item_type_exclude_type_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_type_exclude_map
    ADD CONSTRAINT item_type_exclude_type_id_fk FOREIGN KEY (type_id) REFERENCES item_type(id);


--
-- Name: item_type_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_type
    ADD CONSTRAINT item_type_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: item_type_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_type_map
    ADD CONSTRAINT item_type_map_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: item_type_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY item_type_map
    ADD CONSTRAINT item_type_map_fk_2 FOREIGN KEY (type_id) REFERENCES item_type(id);


--
-- Name: mediation_cfg_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_cfg
    ADD CONSTRAINT mediation_cfg_fk_1 FOREIGN KEY (pluggable_task_id) REFERENCES pluggable_task(id);


--
-- Name: mediation_order_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_order_map
    ADD CONSTRAINT mediation_order_map_fk_1 FOREIGN KEY (mediation_process_id) REFERENCES mediation_process(id);


--
-- Name: mediation_order_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_order_map
    ADD CONSTRAINT mediation_order_map_fk_2 FOREIGN KEY (order_id) REFERENCES purchase_order(id);


--
-- Name: mediation_process_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_process
    ADD CONSTRAINT mediation_process_fk_1 FOREIGN KEY (configuration_id) REFERENCES mediation_cfg(id);


--
-- Name: mediation_record_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_record
    ADD CONSTRAINT mediation_record_fk_1 FOREIGN KEY (mediation_process_id) REFERENCES mediation_process(id);


--
-- Name: mediation_record_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_record
    ADD CONSTRAINT mediation_record_fk_2 FOREIGN KEY (status_id) REFERENCES generic_status(id);


--
-- Name: mediation_record_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_record_line
    ADD CONSTRAINT mediation_record_line_fk_1 FOREIGN KEY (mediation_record_id) REFERENCES mediation_record(id);


--
-- Name: mediation_record_line_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY mediation_record_line
    ADD CONSTRAINT mediation_record_line_fk_2 FOREIGN KEY (order_line_id) REFERENCES order_line(id);


--
-- Name: meta_field_entity_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY meta_field_name
    ADD CONSTRAINT meta_field_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: meta_field_name_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY meta_field_name
    ADD CONSTRAINT meta_field_name_fk_1 FOREIGN KEY (default_value_id) REFERENCES meta_field_value(id);


--
-- Name: meta_field_value_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY meta_field_value
    ADD CONSTRAINT meta_field_value_fk_1 FOREIGN KEY (meta_field_name_id) REFERENCES meta_field_name(id);


--
-- Name: notif_mess_arch_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message_arch_line
    ADD CONSTRAINT notif_mess_arch_line_fk_1 FOREIGN KEY (message_archive_id) REFERENCES notification_message_arch(id);


--
-- Name: notification_message_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_fk_1 FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: notification_message_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_fk_2 FOREIGN KEY (type_id) REFERENCES notification_message_type(id);


--
-- Name: notification_message_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_fk_3 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: notification_message_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message_line
    ADD CONSTRAINT notification_message_line_fk_1 FOREIGN KEY (message_section_id) REFERENCES notification_message_section(id);


--
-- Name: notification_msg_section_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY notification_message_section
    ADD CONSTRAINT notification_msg_section_fk_1 FOREIGN KEY (message_id) REFERENCES notification_message(id);


--
-- Name: order_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: order_line_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_fk_2 FOREIGN KEY (order_id) REFERENCES purchase_order(id);


--
-- Name: order_line_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_fk_3 FOREIGN KEY (type_id) REFERENCES order_line_type(id);


--
-- Name: order_meta_field_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_meta_field_map
    ADD CONSTRAINT order_meta_field_map_fk_1 FOREIGN KEY (order_id) REFERENCES purchase_order(id);


--
-- Name: order_meta_field_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_meta_field_map
    ADD CONSTRAINT order_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: order_period_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_period
    ADD CONSTRAINT order_period_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: order_period_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_period
    ADD CONSTRAINT order_period_fk_2 FOREIGN KEY (unit_id) REFERENCES period_unit(id);


--
-- Name: order_process_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY order_process
    ADD CONSTRAINT order_process_fk_1 FOREIGN KEY (order_id) REFERENCES purchase_order(id);


--
-- Name: partner_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit(id);


--
-- Name: partner_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_2 FOREIGN KEY (fee_currency_id) REFERENCES currency(id);


--
-- Name: partner_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_3 FOREIGN KEY (related_clerk) REFERENCES base_user(id);


--
-- Name: partner_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_4 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: partner_meta_field_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner_meta_field_map
    ADD CONSTRAINT partner_meta_field_map_fk_1 FOREIGN KEY (partner_id) REFERENCES partner(id);


--
-- Name: partner_meta_field_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner_meta_field_map
    ADD CONSTRAINT partner_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: partner_payout_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY partner_payout
    ADD CONSTRAINT partner_payout_fk_1 FOREIGN KEY (partner_id) REFERENCES partner(id);


--
-- Name: payment_authorization_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment_authorization
    ADD CONSTRAINT payment_authorization_fk_1 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- Name: payment_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_1 FOREIGN KEY (ach_id) REFERENCES ach(id);


--
-- Name: payment_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_2 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: payment_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_3 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- Name: payment_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_4 FOREIGN KEY (credit_card_id) REFERENCES credit_card(id);


--
-- Name: payment_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_5 FOREIGN KEY (result_id) REFERENCES payment_result(id);


--
-- Name: payment_fk_6; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_6 FOREIGN KEY (method_id) REFERENCES payment_method(id);


--
-- Name: payment_info_cheque_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment_info_cheque
    ADD CONSTRAINT payment_info_cheque_fk_1 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- Name: payment_invoice_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment_invoice
    ADD CONSTRAINT payment_invoice_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice(id);


--
-- Name: payment_invoice_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment_invoice
    ADD CONSTRAINT payment_invoice_fk_2 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- Name: payment_meta_field_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment_meta_field_map
    ADD CONSTRAINT payment_meta_field_map_fk_1 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- Name: payment_meta_field_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY payment_meta_field_map
    ADD CONSTRAINT payment_meta_field_map_fk_2 FOREIGN KEY (meta_field_value_id) REFERENCES meta_field_value(id);


--
-- Name: permission_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_fk_1 FOREIGN KEY (type_id) REFERENCES permission_type(id);


--
-- Name: permission_role_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY permission_role_map
    ADD CONSTRAINT permission_role_map_fk_1 FOREIGN KEY (role_id) REFERENCES role(id);


--
-- Name: permission_role_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY permission_role_map
    ADD CONSTRAINT permission_role_map_fk_2 FOREIGN KEY (permission_id) REFERENCES permission(id);


--
-- Name: permission_user_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY permission_user
    ADD CONSTRAINT permission_user_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: permission_user_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY permission_user
    ADD CONSTRAINT permission_user_fk_2 FOREIGN KEY (permission_id) REFERENCES permission(id);


--
-- Name: plan_item_bundle_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan_item
    ADD CONSTRAINT plan_item_bundle_id_fk FOREIGN KEY (plan_item_bundle_id) REFERENCES plan_item_bundle(id);


--
-- Name: plan_item_bundle_period_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan_item_bundle
    ADD CONSTRAINT plan_item_bundle_period_fk FOREIGN KEY (period_id) REFERENCES order_period(id);


--
-- Name: plan_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT plan_item_id_fk FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: plan_item_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan_item
    ADD CONSTRAINT plan_item_item_id_fk FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: plan_item_plan_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan_item
    ADD CONSTRAINT plan_item_plan_id_fk FOREIGN KEY (plan_id) REFERENCES plan(id);


--
-- Name: plan_itm_timelin_plan_itm_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan_item_price_timeline
    ADD CONSTRAINT plan_itm_timelin_plan_itm_fk FOREIGN KEY (plan_item_id) REFERENCES plan_item(id);


--
-- Name: plan_period_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT plan_period_id_fk FOREIGN KEY (period_id) REFERENCES order_period(id);


--
-- Name: plnitmtimelnprc_mode_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY plan_item_price_timeline
    ADD CONSTRAINT plnitmtimelnprc_mode_fk FOREIGN KEY (price_model_id) REFERENCES price_model(id);


--
-- Name: pluggable_task_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY pluggable_task
    ADD CONSTRAINT pluggable_task_fk_1 FOREIGN KEY (type_id) REFERENCES pluggable_task_type(id);


--
-- Name: pluggable_task_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY pluggable_task
    ADD CONSTRAINT pluggable_task_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: pluggable_task_parameter_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY pluggable_task_parameter
    ADD CONSTRAINT pluggable_task_parameter_fk_1 FOREIGN KEY (task_id) REFERENCES pluggable_task(id);


--
-- Name: pluggable_task_type_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY pluggable_task_type
    ADD CONSTRAINT pluggable_task_type_fk_1 FOREIGN KEY (category_id) REFERENCES pluggable_task_type_category(id);


--
-- Name: preference_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY preference
    ADD CONSTRAINT preference_fk_1 FOREIGN KEY (type_id) REFERENCES preference_type(id);


--
-- Name: preference_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY preference
    ADD CONSTRAINT preference_fk_2 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- Name: price_model_attr_model_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY price_model_attribute
    ADD CONSTRAINT price_model_attr_model_id_fk FOREIGN KEY (price_model_id) REFERENCES price_model(id);


--
-- Name: price_model_currency_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY price_model
    ADD CONSTRAINT price_model_currency_id_fk FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: price_model_next_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY price_model
    ADD CONSTRAINT price_model_next_id_fk FOREIGN KEY (next_model_id) REFERENCES price_model(id);


--
-- Name: process_run_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run
    ADD CONSTRAINT process_run_fk_1 FOREIGN KEY (process_id) REFERENCES billing_process(id);


--
-- Name: process_run_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run
    ADD CONSTRAINT process_run_fk_2 FOREIGN KEY (status_id) REFERENCES generic_status(id);


--
-- Name: process_run_total_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run_total
    ADD CONSTRAINT process_run_total_fk_1 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: process_run_total_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run_total
    ADD CONSTRAINT process_run_total_fk_2 FOREIGN KEY (process_run_id) REFERENCES process_run(id);


--
-- Name: process_run_total_pm_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run_total_pm
    ADD CONSTRAINT process_run_total_pm_fk_1 FOREIGN KEY (payment_method_id) REFERENCES payment_method(id);


--
-- Name: process_run_user_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run_user
    ADD CONSTRAINT process_run_user_fk_1 FOREIGN KEY (process_run_id) REFERENCES process_run(id);


--
-- Name: process_run_user_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY process_run_user
    ADD CONSTRAINT process_run_user_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: promotion_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY promotion
    ADD CONSTRAINT promotion_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: promotion_user_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY promotion_user_map
    ADD CONSTRAINT promotion_user_map_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: promotion_user_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY promotion_user_map
    ADD CONSTRAINT promotion_user_map_fk_2 FOREIGN KEY (promotion_id) REFERENCES promotion(id);


--
-- Name: purchase_order_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_1 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- Name: purchase_order_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_2 FOREIGN KEY (billing_type_id) REFERENCES order_billing_type(id);


--
-- Name: purchase_order_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_3 FOREIGN KEY (period_id) REFERENCES order_period(id);


--
-- Name: purchase_order_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_4 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: purchase_order_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_5 FOREIGN KEY (created_by) REFERENCES base_user(id);


--
-- Name: rate_card_entity_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY rate_card
    ADD CONSTRAINT rate_card_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: report_map_entity_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity_report_map
    ADD CONSTRAINT report_map_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: report_map_report_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY entity_report_map
    ADD CONSTRAINT report_map_report_id_fk FOREIGN KEY (report_id) REFERENCES report(id);


--
-- Name: role_entity_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_entity_id_fk FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- Name: user_role_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY user_role_map
    ADD CONSTRAINT user_role_map_fk_1 FOREIGN KEY (role_id) REFERENCES role(id);


--
-- Name: user_role_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling
--

ALTER TABLE ONLY user_role_map
    ADD CONSTRAINT user_role_map_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

