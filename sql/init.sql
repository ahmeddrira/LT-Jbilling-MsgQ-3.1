--
-- PostgreSQL database dump
--

SET client_encoding = 'SQL_ASCII';
SET check_function_bodies = false;

SET SESSION AUTHORIZATION 'postgres';

--
-- TOC entry 2 (OID 0)
-- Name: jbilling; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE jbilling WITH TEMPLATE = template0 ENCODING = 'SQL_ASCII';


\connect jbilling postgres

SET client_encoding = 'SQL_ASCII';
SET check_function_bodies = false;

--
-- TOC entry 4 (OID 2200)
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--
create user jbilling_user password 'jbilling';
grant all on database jbilling to jbilling_user;

REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


SET SESSION AUTHORIZATION 'jbilling_user';

SET search_path = public, pg_catalog;

--
-- TOC entry 5 (OID 4131028)
-- Name: entity; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE entity (
    id integer NOT NULL,
    external_id character varying(20),
    description character varying(100) NOT NULL,
    create_datetime timestamp without time zone NOT NULL,
    language_id integer NOT NULL,
    currency_id integer NOT NULL
);


--
-- TOC entry 6 (OID 4131030)
-- Name: base_user; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE base_user (
    id integer NOT NULL,
    entity_id integer,
    "password" character varying(20),
    deleted smallint DEFAULT 0 NOT NULL,
    language_id integer,
    status_id integer,
    currency_id integer,
    create_datetime timestamp without time zone NOT NULL,
    last_status_change timestamp without time zone,
    last_login timestamp without time zone,
    user_name character varying(50)
);
ALTER TABLE ONLY base_user ALTER COLUMN entity_id SET STATISTICS 100;


--
-- TOC entry 7 (OID 4131033)
-- Name: user_status; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE user_status (
    id integer NOT NULL,
    can_login smallint NOT NULL
);


--
-- TOC entry 8 (OID 4131035)
-- Name: customer; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE customer (
    id integer NOT NULL,
    user_id integer,
    partner_id integer,
    referral_fee_paid smallint,
    invoice_delivery_method_id integer NOT NULL,
    notes character varying(1000),
    auto_payment_type integer,
    due_date_unit_id integer,
    due_date_value integer,
    df_fm smallint,
    parent_id integer,
    is_parent smallint,
    exclude_aging smallint DEFAULT 0 NOT NULL
);


--
-- TOC entry 9 (OID 4131038)
-- Name: partner; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE partner (
    id integer NOT NULL,
    user_id integer,
    balance double precision NOT NULL,
    total_payments double precision NOT NULL,
    total_refunds double precision NOT NULL,
    total_payouts double precision NOT NULL,
    percentage_rate double precision,
    referral_fee double precision,
    fee_currency_id integer,
    one_time smallint NOT NULL,
    period_unit_id integer NOT NULL,
    period_value integer NOT NULL,
    next_payout_date date NOT NULL,
    due_payout double precision,
    automatic_process smallint NOT NULL,
    related_clerk integer
);


--
-- TOC entry 10 (OID 4131040)
-- Name: partner_payout; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE partner_payout (
    id integer NOT NULL,
    starting_date date NOT NULL,
    ending_date date NOT NULL,
    payments_amount double precision NOT NULL,
    refunds_amount double precision NOT NULL,
    balance_left double precision NOT NULL,
    payment_id integer,
    partner_id integer
);


--
-- TOC entry 11 (OID 4131042)
-- Name: item; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE item (
    id integer NOT NULL,
    internal_number character varying(50),
    entity_id integer,
    percentage double precision,
    price_manual smallint NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL
);


--
-- TOC entry 12 (OID 4131045)
-- Name: item_type_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE item_type_map (
    item_id integer,
    type_id integer
);


--
-- TOC entry 13 (OID 4131047)
-- Name: item_price; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE item_price (
    id integer NOT NULL,
    item_id integer,
    currency_id integer,
    price double precision NOT NULL
);


--
-- TOC entry 14 (OID 4131049)
-- Name: item_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE item_type (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    description character varying(100),
    order_line_type_id integer NOT NULL
);


--
-- TOC entry 15 (OID 4131051)
-- Name: item_user_price; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE item_user_price (
    id integer NOT NULL,
    item_id integer,
    user_id integer,
    price double precision NOT NULL,
    currency_id integer NOT NULL
);


--
-- TOC entry 16 (OID 4131053)
-- Name: promotion; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE promotion (
    id integer NOT NULL,
    item_id integer,
    code character varying(50) NOT NULL,
    notes character varying(200),
    once smallint NOT NULL,
    since date,
    "until" date
);


--
-- TOC entry 17 (OID 4131055)
-- Name: promotion_user_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE promotion_user_map (
    user_id integer,
    promotion_id integer
);


--
-- TOC entry 18 (OID 4131057)
-- Name: purchase_order; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE purchase_order (
    id integer NOT NULL,
    user_id integer,
    period_id integer,
    billing_type_id integer NOT NULL,
    active_since date,
    active_until date,
    create_datetime timestamp without time zone NOT NULL,
    next_billable_day timestamp without time zone,
    created_by integer NOT NULL,
    status_id integer NOT NULL,
    currency_id integer NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL,
    "notify" smallint,
    last_notified timestamp without time zone,
    notification_step integer,
    due_date_unit_id integer,
    due_date_value integer,
    df_fm smallint,
    anticipate_periods integer,
    own_invoice smallint,
    notes character varying(200),
    notes_in_invoice smallint
);


--
-- TOC entry 19 (OID 4131060)
-- Name: order_billing_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE order_billing_type (
    id integer NOT NULL
);


--
-- TOC entry 20 (OID 4131062)
-- Name: order_status; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE order_status (
    id integer NOT NULL
);


--
-- TOC entry 21 (OID 4131064)
-- Name: order_line; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE order_line (
    id integer NOT NULL,
    order_id integer,
    item_id integer,
    type_id integer,
    amount double precision NOT NULL,
    quantity integer,
    price double precision,
    item_price smallint,
    create_datetime timestamp without time zone NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL,
    description character varying(1000)
);


--
-- TOC entry 22 (OID 4131067)
-- Name: order_period; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE order_period (
    id integer NOT NULL,
    entity_id integer,
    value integer,
    unit_id integer
);


--
-- TOC entry 23 (OID 4131069)
-- Name: period_unit; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE period_unit (
    id integer NOT NULL
);


--
-- TOC entry 24 (OID 4131071)
-- Name: order_line_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE order_line_type (
    id integer NOT NULL,
    editable smallint NOT NULL
);


--
-- TOC entry 25 (OID 4131073)
-- Name: pluggable_task; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE pluggable_task (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    type_id integer NOT NULL,
    processing_order integer NOT NULL
);


--
-- TOC entry 26 (OID 4131075)
-- Name: pluggable_task_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE pluggable_task_type (
    id integer NOT NULL,
    category_id integer NOT NULL,
    class_name character varying(200) NOT NULL,
    min_parameters integer NOT NULL
);


--
-- TOC entry 27 (OID 4131077)
-- Name: pluggable_task_type_category; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE pluggable_task_type_category (
    id integer NOT NULL,
    description character varying(30) NOT NULL,
    interface_name character varying(200) NOT NULL
);


--
-- TOC entry 28 (OID 4131079)
-- Name: pluggable_task_parameter; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE pluggable_task_parameter (
    id integer NOT NULL,
    task_id integer NOT NULL,
    name character varying(50) NOT NULL,
    int_value integer,
    str_value character varying(200),
    float_value double precision
);


--
-- TOC entry 29 (OID 4131081)
-- Name: invoice; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE invoice (
    id integer NOT NULL,
    create_datetime timestamp without time zone NOT NULL,
    billing_process_id integer,
    user_id integer,
    delegated_invoice_id integer,
    due_date date NOT NULL,
    total double precision NOT NULL,
    payment_attempts integer DEFAULT 0 NOT NULL,
    to_process smallint DEFAULT 1 NOT NULL,
    balance double precision,
    carried_balance double precision NOT NULL,
    in_process_payment smallint DEFAULT 1 NOT NULL,
    is_review integer NOT NULL,
    currency_id integer NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL,
    paper_invoice_batch_id integer,
    customer_notes character varying(1000),
    public_number character varying(40),
    last_reminder timestamp without time zone,
    overdue_step integer,
    create_timestamp timestamp without time zone NOT NULL
);


--
-- TOC entry 30 (OID 4131087)
-- Name: invoice_line; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE invoice_line (
    id integer NOT NULL,
    invoice_id integer,
    type_id integer,
    amount double precision NOT NULL,
    quantity integer,
    price double precision,
    deleted smallint DEFAULT 0 NOT NULL,
    item_id integer,
    description character varying(1000),
    source_user_id integer
);


--
-- TOC entry 31 (OID 4131090)
-- Name: invoice_line_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE invoice_line_type (
    id integer NOT NULL,
    description character varying(50) NOT NULL,
    order_position integer NOT NULL
);


--
-- TOC entry 32 (OID 4131092)
-- Name: invoice_delivery_method; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE invoice_delivery_method (
    id integer NOT NULL
);


--
-- TOC entry 33 (OID 4131094)
-- Name: entity_delivery_method_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE entity_delivery_method_map (
    method_id integer,
    entity_id integer
);


--
-- TOC entry 34 (OID 4131096)
-- Name: order_process; Type: TABLE; Schema: public; Owner: jbilling_user
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
    origin integer
);


--
-- TOC entry 35 (OID 4131098)
-- Name: billing_process; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE billing_process (
    id integer NOT NULL,
    entity_id integer NOT NULL,
    billing_date date NOT NULL,
    period_unit_id integer NOT NULL,
    period_value integer NOT NULL,
    is_review integer NOT NULL,
    paper_invoice_batch_id integer,
    retries_to_do integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 36 (OID 4131101)
-- Name: billing_process_run; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE billing_process_run (
    id integer NOT NULL,
    process_id integer,
    run_date date NOT NULL,
    started timestamp without time zone NOT NULL,
    finished timestamp without time zone,
    invoices_generated integer
);


--
-- TOC entry 37 (OID 4131103)
-- Name: billing_process_run_total; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE billing_process_run_total (
    id integer NOT NULL,
    process_run_id integer,
    currency_id integer NOT NULL,
    total_invoiced double precision,
    total_paid double precision,
    total_not_paid double precision
);


--
-- TOC entry 38 (OID 4131105)
-- Name: billing_process_run_total_pm; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE billing_process_run_total_pm (
    id integer NOT NULL,
    process_run_total_id integer,
    payment_method_id integer,
    total double precision NOT NULL
);


--
-- TOC entry 39 (OID 4131107)
-- Name: billing_process_configuration; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE billing_process_configuration (
    id integer NOT NULL,
    entity_id integer,
    next_run_date date NOT NULL,
    generate_report smallint NOT NULL,
    retries integer,
    days_for_retry integer,
    days_for_report integer,
    review_status integer NOT NULL,
    period_unit_id integer NOT NULL,
    period_value integer NOT NULL,
    due_date_unit_id integer NOT NULL,
    due_date_value integer NOT NULL,
    df_fm smallint,
    only_recurring smallint DEFAULT 1 NOT NULL,
    invoice_date_process smallint NOT NULL,
    auto_payment smallint DEFAULT 0 NOT NULL,
    maximum_periods integer DEFAULT 1 NOT NULL,
    auto_payment_application integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 40 (OID 4131113)
-- Name: paper_invoice_batch; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE paper_invoice_batch (
    id integer NOT NULL,
    total_invoices integer NOT NULL,
    delivery_date date,
    is_self_managed smallint NOT NULL
);


--
-- TOC entry 41 (OID 4131115)
-- Name: payment; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE payment (
    id integer NOT NULL,
    user_id integer,
    attempt integer,
    result_id integer,
    amount double precision NOT NULL,
    create_datetime timestamp without time zone NOT NULL,
    payment_date date,
    method_id integer NOT NULL,
    credit_card_id integer,
    deleted smallint DEFAULT 0 NOT NULL,
    is_refund smallint DEFAULT 0 NOT NULL,
    payment_id integer,
    currency_id integer NOT NULL,
    payout_id integer,
    ach_id integer,
    balance double precision
);


--
-- TOC entry 42 (OID 4131119)
-- Name: payment_info_cheque; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE payment_info_cheque (
    id integer NOT NULL,
    payment_id integer,
    bank character varying(50),
    cheque_number character varying(50),
    cheque_date date
);


--
-- TOC entry 43 (OID 4131121)
-- Name: payment_authorization; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE payment_authorization (
    id integer NOT NULL,
    payment_id integer,
    processor character varying(20) NOT NULL,
    code1 character varying(20) NOT NULL,
    code2 character varying(20),
    code3 character varying(20),
    approval_code character varying(20),
    avs character varying(20),
    transaction_id character varying(20),
    md5 character varying(100),
    card_code character varying(100),
    create_datetime date NOT NULL
);


--
-- TOC entry 44 (OID 4131123)
-- Name: credit_card; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE credit_card (
    id integer NOT NULL,
    cc_number character varying(30) NOT NULL,
    cc_expiry date NOT NULL,
    name character varying(50),
    cc_type integer NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL,
    security_code integer
);


--
-- TOC entry 45 (OID 4131126)
-- Name: user_credit_card_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE user_credit_card_map (
    user_id integer,
    credit_card_id integer
);


--
-- TOC entry 46 (OID 4131128)
-- Name: payment_result; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE payment_result (
    id integer NOT NULL
);


--
-- TOC entry 47 (OID 4131130)
-- Name: payment_invoice_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE payment_invoice_map (
    payment_id integer,
    invoice_id integer
);


--
-- TOC entry 48 (OID 4131132)
-- Name: payment_method; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE payment_method (
    id integer NOT NULL
);


--
-- TOC entry 49 (OID 4131134)
-- Name: entity_payment_method_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE entity_payment_method_map (
    entity_id integer,
    payment_method_id integer
);


--
-- TOC entry 50 (OID 4131136)
-- Name: contact; Type: TABLE; Schema: public; Owner: jbilling_user
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
    create_datetime timestamp without time zone NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL,
    notification_include smallint DEFAULT 1,
    user_id integer
);


--
-- TOC entry 51 (OID 4131140)
-- Name: contact_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE contact_map (
    id integer NOT NULL,
    contact_id integer,
    type_id integer NOT NULL,
    table_id integer NOT NULL,
    foreign_id integer NOT NULL
);
ALTER TABLE ONLY contact_map ALTER COLUMN foreign_id SET STATISTICS 100;


--
-- TOC entry 52 (OID 4131142)
-- Name: contact_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE contact_type (
    id integer NOT NULL,
    entity_id integer,
    is_primary smallint
);


--
-- TOC entry 53 (OID 4131144)
-- Name: jbilling_table; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE jbilling_table (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    next_id integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 54 (OID 4131147)
-- Name: jbilling_table_column; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE jbilling_table_column (
    table_id integer NOT NULL,
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


--
-- TOC entry 55 (OID 4131149)
-- Name: international_description; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE international_description (
    table_id integer NOT NULL,
    foreign_id integer NOT NULL,
    psudo_column character varying(20) NOT NULL,
    language_id integer NOT NULL,
    content character varying(5000) NOT NULL
);


--
-- TOC entry 56 (OID 4131154)
-- Name: language; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE "language" (
    id integer NOT NULL,
    code character varying(2) NOT NULL,
    description character varying(50) NOT NULL
);


--
-- TOC entry 57 (OID 4131156)
-- Name: event_log; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE event_log (
    id integer NOT NULL,
    entity_id integer,
    user_id integer,
    table_id integer,
    foreign_id integer NOT NULL,
    create_datetime timestamp without time zone NOT NULL,
    "level" integer NOT NULL,
    module_id integer NOT NULL,
    message_id integer NOT NULL,
    old_num integer,
    old_str character varying(1000),
    old_date timestamp without time zone
);


--
-- TOC entry 58 (OID 4131158)
-- Name: event_log_module; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE event_log_module (
    id integer NOT NULL
);


--
-- TOC entry 59 (OID 4131160)
-- Name: event_log_message; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE event_log_message (
    id integer NOT NULL
);


--
-- TOC entry 60 (OID 4131162)
-- Name: preference; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE preference (
    id integer NOT NULL,
    type_id integer,
    table_id integer NOT NULL,
    foreign_id integer NOT NULL,
    int_value integer,
    str_value character varying(200),
    float_value double precision
);


--
-- TOC entry 61 (OID 4131164)
-- Name: preference_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE preference_type (
    id integer NOT NULL,
    int_def_value integer,
    str_def_value character varying(200),
    float_def_value double precision
);


--
-- TOC entry 62 (OID 4131166)
-- Name: notification_message; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE notification_message (
    id integer NOT NULL,
    type_id integer,
    entity_id integer NOT NULL,
    language_id integer NOT NULL,
    use_flag smallint DEFAULT 1 NOT NULL
);


--
-- TOC entry 63 (OID 4131169)
-- Name: notification_message_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE notification_message_type (
    id integer NOT NULL,
    sections integer NOT NULL
);


--
-- TOC entry 64 (OID 4131171)
-- Name: notification_message_section; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE notification_message_section (
    id integer NOT NULL,
    message_id integer,
    section integer
);


--
-- TOC entry 65 (OID 4131173)
-- Name: notification_message_line; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE notification_message_line (
    id integer NOT NULL,
    message_section_id integer,
    content character varying(1000) NOT NULL
);


--
-- TOC entry 66 (OID 4131175)
-- Name: notification_message_archive; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE notification_message_archive (
    id integer NOT NULL,
    type_id integer,
    create_datetime timestamp without time zone NOT NULL,
    user_id integer,
    result_message character varying(200)
);


--
-- TOC entry 67 (OID 4131177)
-- Name: notification_message_archive_line; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE notification_message_archive_line (
    id integer NOT NULL,
    message_archive_id integer,
    section integer NOT NULL,
    content character varying(1000) NOT NULL
);


--
-- TOC entry 68 (OID 4131179)
-- Name: report; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE report (
    id integer NOT NULL,
    titlekey character varying(50),
    instructionskey character varying(50),
    tables character varying(1000) NOT NULL,
    where_str character varying(1000) NOT NULL,
    id_column smallint NOT NULL,
    link character varying(200)
);


--
-- TOC entry 69 (OID 4131184)
-- Name: report_user; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE report_user (
    id integer NOT NULL,
    user_id integer,
    report_id integer,
    create_datetime timestamp without time zone NOT NULL,
    title character varying(50)
);


--
-- TOC entry 70 (OID 4131186)
-- Name: report_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE report_type (
    id integer NOT NULL,
    showable smallint NOT NULL
);


--
-- TOC entry 71 (OID 4131188)
-- Name: report_type_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE report_type_map (
    report_id integer,
    type_id integer
);


--
-- TOC entry 72 (OID 4131190)
-- Name: report_entity_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE report_entity_map (
    entity_id integer,
    report_id integer
);


--
-- TOC entry 73 (OID 4131192)
-- Name: report_field; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE report_field (
    id integer NOT NULL,
    report_id integer,
    report_user_id integer,
    "position" integer NOT NULL,
    table_name character varying(50) NOT NULL,
    column_name character varying(50) NOT NULL,
    order_position integer,
    where_value character varying(50),
    title_key character varying(50),
    "function" character varying(10),
    is_grouped smallint NOT NULL,
    is_shown smallint NOT NULL,
    data_type character varying(10) NOT NULL,
    "operator" character varying(2),
    functionable smallint NOT NULL,
    selectable smallint NOT NULL,
    ordenable smallint NOT NULL,
    operatorable smallint NOT NULL,
    whereable smallint NOT NULL
);


--
-- TOC entry 74 (OID 4131194)
-- Name: permission; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE permission (
    id integer NOT NULL,
    type_id integer NOT NULL,
    foreign_id integer
);


--
-- TOC entry 75 (OID 4131196)
-- Name: permission_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE permission_type (
    id integer NOT NULL,
    description character varying(30) NOT NULL
);


--
-- TOC entry 76 (OID 4131198)
-- Name: role; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE role (
    id integer NOT NULL
);


--
-- TOC entry 77 (OID 4131200)
-- Name: permission_role_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE permission_role_map (
    permission_id integer,
    role_id integer
);


--
-- TOC entry 78 (OID 4131202)
-- Name: user_role_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE user_role_map (
    user_id integer,
    role_id integer
);


--
-- TOC entry 79 (OID 4131204)
-- Name: permission_user; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE permission_user (
    permission_id integer,
    user_id integer,
    is_grant smallint NOT NULL,
    id integer NOT NULL
);


--
-- TOC entry 80 (OID 4131206)
-- Name: menu_option; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE menu_option (
    id integer NOT NULL,
    link character varying(100) NOT NULL,
    "level" integer NOT NULL,
    parent_id integer
);


--
-- TOC entry 81 (OID 4131208)
-- Name: country; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE country (
    id integer NOT NULL,
    code character varying(2) NOT NULL
);


--
-- TOC entry 82 (OID 4131210)
-- Name: currency; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE currency (
    id integer NOT NULL,
    symbol character varying(10) NOT NULL,
    code character varying(3) NOT NULL,
    country_code character varying(2) NOT NULL
);


--
-- TOC entry 83 (OID 4131212)
-- Name: currency_entity_map; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE currency_entity_map (
    currency_id integer,
    entity_id integer
);


--
-- TOC entry 84 (OID 4131214)
-- Name: currency_exchange; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE currency_exchange (
    id integer NOT NULL,
    entity_id integer,
    currency_id integer,
    rate double precision NOT NULL,
    create_datetime timestamp without time zone NOT NULL
);


--
-- TOC entry 85 (OID 4131216)
-- Name: ageing_entity_step; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE ageing_entity_step (
    id integer NOT NULL,
    entity_id integer,
    status_id integer,
    days integer NOT NULL
);


--
-- TOC entry 86 (OID 4131218)
-- Name: ach; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE ach (
    id integer NOT NULL,
    user_id integer,
    aba_routing character varying(9) NOT NULL,
    bank_account character varying(20) NOT NULL,
    account_type integer NOT NULL,
    bank_name character varying(50) NOT NULL,
    account_name character varying(100) NOT NULL
);


--
-- TOC entry 87 (OID 4131220)
-- Name: contact_field_type; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE contact_field_type (
    id integer NOT NULL,
    entity_id integer,
    prompt_key character varying(50) NOT NULL,
    data_type character varying(10) NOT NULL,
    customer_readonly smallint
);


--
-- TOC entry 88 (OID 4131222)
-- Name: contact_field; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE contact_field (
    id integer NOT NULL,
    type_id integer,
    contact_id integer,
    content character varying(100) NOT NULL
);


--
-- TOC entry 89 (OID 4131224)
-- Name: list; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE list (
    id integer NOT NULL,
    legacy_name character varying(30),
    title_key character varying(100) NOT NULL,
    instr_key character varying(100)
);


--
-- TOC entry 90 (OID 4131226)
-- Name: list_entity; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE list_entity (
    id integer NOT NULL,
    list_id integer,
    entity_id integer NOT NULL,
    total_records integer NOT NULL,
    last_update date NOT NULL
);


--
-- TOC entry 91 (OID 4131228)
-- Name: list_field; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE list_field (
    id integer NOT NULL,
    list_id integer,
    title_key character varying(100) NOT NULL,
    column_name character varying(50) NOT NULL,
    ordenable smallint NOT NULL,
    searchable smallint NOT NULL,
    data_type character varying(10) NOT NULL
);


--
-- TOC entry 92 (OID 4131230)
-- Name: list_field_entity; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE list_field_entity (
    id integer NOT NULL,
    list_field_id integer,
    list_entity_id integer,
    min_value integer,
    max_value integer,
    min_str_value character varying(100),
    max_str_value character varying(100),
    min_date_value timestamp without time zone,
    max_date_value timestamp without time zone
);


--
-- TOC entry 93 (OID 4131232)
-- Name: partner_range; Type: TABLE; Schema: public; Owner: jbilling_user
--

CREATE TABLE partner_range (
    id integer NOT NULL,
    partner_id integer,
    percentage_rate double precision,
    referral_fee double precision,
    range_from integer NOT NULL,
    range_to integer NOT NULL
);


--
-- Data for TOC entry 222 (OID 4131028)
-- Name: entity; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 223 (OID 4131030)
-- Name: base_user; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 224 (OID 4131033)
-- Name: user_status; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO user_status (id, can_login) VALUES (1, 1);
INSERT INTO user_status (id, can_login) VALUES (2, 1);
INSERT INTO user_status (id, can_login) VALUES (3, 1);
INSERT INTO user_status (id, can_login) VALUES (4, 1);
INSERT INTO user_status (id, can_login) VALUES (5, 0);
INSERT INTO user_status (id, can_login) VALUES (6, 0);
INSERT INTO user_status (id, can_login) VALUES (7, 0);
INSERT INTO user_status (id, can_login) VALUES (8, 0);


--
-- Data for TOC entry 225 (OID 4131035)
-- Name: customer; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 226 (OID 4131038)
-- Name: partner; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 227 (OID 4131040)
-- Name: partner_payout; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 228 (OID 4131042)
-- Name: item; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 229 (OID 4131045)
-- Name: item_type_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 230 (OID 4131047)
-- Name: item_price; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 231 (OID 4131049)
-- Name: item_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 232 (OID 4131051)
-- Name: item_user_price; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 233 (OID 4131053)
-- Name: promotion; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 234 (OID 4131055)
-- Name: promotion_user_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 235 (OID 4131057)
-- Name: purchase_order; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 236 (OID 4131060)
-- Name: order_billing_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO order_billing_type (id) VALUES (1);
INSERT INTO order_billing_type (id) VALUES (2);


--
-- Data for TOC entry 237 (OID 4131062)
-- Name: order_status; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO order_status (id) VALUES (1);
INSERT INTO order_status (id) VALUES (2);
INSERT INTO order_status (id) VALUES (3);
INSERT INTO order_status (id) VALUES (4);


--
-- Data for TOC entry 238 (OID 4131064)
-- Name: order_line; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 239 (OID 4131067)
-- Name: order_period; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO order_period (id, entity_id, value, unit_id) VALUES (1, NULL, NULL, NULL);


--
-- Data for TOC entry 240 (OID 4131069)
-- Name: period_unit; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO period_unit (id) VALUES (1);
INSERT INTO period_unit (id) VALUES (2);
INSERT INTO period_unit (id) VALUES (3);
INSERT INTO period_unit (id) VALUES (4);


--
-- Data for TOC entry 241 (OID 4131071)
-- Name: order_line_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO order_line_type (id, editable) VALUES (1, 1);
INSERT INTO order_line_type (id, editable) VALUES (2, 0);
INSERT INTO order_line_type (id, editable) VALUES (3, 0);


--
-- Data for TOC entry 242 (OID 4131073)
-- Name: pluggable_task; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 243 (OID 4131075)
-- Name: pluggable_task_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (1, 1, 'com.sapienter.jbilling.server.pluggableTask.BasicLineTotalTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (13, 4, 'com.sapienter.jbilling.server.pluggableTask.CalculateDueDateDfFm', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (3, 4, 'com.sapienter.jbilling.server.pluggableTask.CalculateDueDate', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (4, 4, 'com.sapienter.jbilling.server.pluggableTask.BasicCompositionTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (5, 2, 'com.sapienter.jbilling.server.pluggableTask.BasicOrderFilterTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (6, 3, 'com.sapienter.jbilling.server.pluggableTask.BasicInvoiceFilterTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (7, 5, 'com.sapienter.jbilling.server.pluggableTask.BasicOrderPeriodTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (8, 6, 'com.sapienter.jbilling.server.pluggableTask.PaymentAuthorizeNetTask', 2);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (14, 3, 'com.sapienter.jbilling.server.pluggableTask.NoInvoiceFilterTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (10, 8, 'com.sapienter.jbilling.server.pluggableTask.BasicPaymentInfoTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (11, 6, 'com.sapienter.jbilling.server.pluggableTask.PaymentPartnerTestTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (12, 7, 'com.sapienter.jbilling.server.pluggableTask.PaperInvoiceNotificationTask', 1);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (2, 1, 'com.sapienter.jbilling.server.pluggableTask.GSTTaxTask', 2);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (15, 9, 'com.sapienter.jbilling.server.pluggableTask.BasicPenaltyTask', 1);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (16, 2, 'com.sapienter.jbilling.server.pluggableTask.OrderFilterAnticipatedTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (9, 7, 'com.sapienter.jbilling.server.pluggableTask.BasicEmailNotificationTask', 6);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (17, 5, 'com.sapienter.jbilling.server.pluggableTask.OrderPeriodAnticipateTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (18, 6, 'com.sapienter.jbilling.server.pluggableTask.PaymentBitMoversTask', 0);
INSERT INTO pluggable_task_type (id, category_id, class_name, min_parameters) VALUES (19, 6, 'com.sapienter.jbilling.server.pluggableTask.PaymentEmailAuthorizeNetTask', 1);


--
-- Data for TOC entry 244 (OID 4131077)
-- Name: pluggable_task_type_category; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (1, 'order processing task', 'com.sapienter.jbilling.server.pluggableTask.OrderProcessingTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (2, 'order_filter task', 'com.sapienter.jbilling.server.pluggableTask.OrderFilterTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (3, 'invoice filter task', 'com.sapienter.jbilling.server.pluggableTask.InvoiceFilterTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (4, 'invoice composition task', 'com.sapienter.jbilling.server.pluggableTask.InvoiceCompositionTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (5, 'order period calculation task', 'com.sapienter.jbilling.server.pluggableTask.OrderPeriodTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (6, 'payment processing task', 'com.sapienter.jbilling.server.pluggableTask.PaymentTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (7, 'notification task', 'com.sapienter.jbilling.server.pluggableTask.NotificationTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (8, 'payment information task', 'com.sapienter.jbilling.server.pluggableTask.PaymentInfoTask');
INSERT INTO pluggable_task_type_category (id, description, interface_name) VALUES (9, 'invoice overdue penalty', 'com.sapienter.jbilling.server.pluggableTask.PenaltyTask');


--
-- Data for TOC entry 245 (OID 4131079)
-- Name: pluggable_task_parameter; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 246 (OID 4131081)
-- Name: invoice; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 247 (OID 4131087)
-- Name: invoice_line; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 248 (OID 4131090)
-- Name: invoice_line_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO invoice_line_type (id, description, order_position) VALUES (5, 'sub account', 4);
INSERT INTO invoice_line_type (id, description, order_position) VALUES (3, 'due invoice', 1);
INSERT INTO invoice_line_type (id, description, order_position) VALUES (1, 'item', 2);
INSERT INTO invoice_line_type (id, description, order_position) VALUES (4, 'interests', 3);
INSERT INTO invoice_line_type (id, description, order_position) VALUES (2, 'tax', 5);


--
-- Data for TOC entry 249 (OID 4131092)
-- Name: invoice_delivery_method; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO invoice_delivery_method (id) VALUES (1);
INSERT INTO invoice_delivery_method (id) VALUES (2);
INSERT INTO invoice_delivery_method (id) VALUES (3);


--
-- Data for TOC entry 250 (OID 4131094)
-- Name: entity_delivery_method_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 251 (OID 4131096)
-- Name: order_process; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 252 (OID 4131098)
-- Name: billing_process; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 253 (OID 4131101)
-- Name: billing_process_run; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 254 (OID 4131103)
-- Name: billing_process_run_total; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 255 (OID 4131105)
-- Name: billing_process_run_total_pm; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 256 (OID 4131107)
-- Name: billing_process_configuration; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 257 (OID 4131113)
-- Name: paper_invoice_batch; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 258 (OID 4131115)
-- Name: payment; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 259 (OID 4131119)
-- Name: payment_info_cheque; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 260 (OID 4131121)
-- Name: payment_authorization; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 261 (OID 4131123)
-- Name: credit_card; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 262 (OID 4131126)
-- Name: user_credit_card_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 263 (OID 4131128)
-- Name: payment_result; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO payment_result (id) VALUES (1);
INSERT INTO payment_result (id) VALUES (2);
INSERT INTO payment_result (id) VALUES (3);
INSERT INTO payment_result (id) VALUES (4);


--
-- Data for TOC entry 264 (OID 4131130)
-- Name: payment_invoice_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 265 (OID 4131132)
-- Name: payment_method; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO payment_method (id) VALUES (1);
INSERT INTO payment_method (id) VALUES (2);
INSERT INTO payment_method (id) VALUES (3);
INSERT INTO payment_method (id) VALUES (4);
INSERT INTO payment_method (id) VALUES (5);
INSERT INTO payment_method (id) VALUES (6);
INSERT INTO payment_method (id) VALUES (7);
INSERT INTO payment_method (id) VALUES (8);


--
-- Data for TOC entry 266 (OID 4131134)
-- Name: entity_payment_method_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 267 (OID 4131136)
-- Name: contact; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 268 (OID 4131140)
-- Name: contact_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 269 (OID 4131142)
-- Name: contact_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO contact_type (id, entity_id, is_primary) VALUES (1, NULL, NULL);


--
-- Data for TOC entry 270 (OID 4131144)
-- Name: jbilling_table; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO jbilling_table (id, name, next_id) VALUES (8, 'entity_delivery_method_map', 4);
INSERT INTO jbilling_table (id, name, next_id) VALUES (62, 'user_role_map', 13);
INSERT INTO jbilling_table (id, name, next_id) VALUES (36, 'entity_payment_method_map', 26);
INSERT INTO jbilling_table (id, name, next_id) VALUES (72, 'report_entity_map', 113);
INSERT INTO jbilling_table (id, name, next_id) VALUES (68, 'currency_entity_map', 10);
INSERT INTO jbilling_table (id, name, next_id) VALUES (74, 'report_type_map', 19);
INSERT INTO jbilling_table (id, name, next_id) VALUES (45, 'user_credit_card_map', 5);
INSERT INTO jbilling_table (id, name, next_id) VALUES (61, 'permission_role_map', 279);
INSERT INTO jbilling_table (id, name, next_id) VALUES (29, 'contact_map', 6780);
INSERT INTO jbilling_table (id, name, next_id) VALUES (58, 'permission_type', 9);
INSERT INTO jbilling_table (id, name, next_id) VALUES (6, 'period_unit', 5);
INSERT INTO jbilling_table (id, name, next_id) VALUES (7, 'invoice_delivery_method', 4);
INSERT INTO jbilling_table (id, name, next_id) VALUES (9, 'user_status', 9);
INSERT INTO jbilling_table (id, name, next_id) VALUES (18, 'order_line_type', 4);
INSERT INTO jbilling_table (id, name, next_id) VALUES (19, 'order_billing_type', 3);
INSERT INTO jbilling_table (id, name, next_id) VALUES (63, 'menu_option', 92);
INSERT INTO jbilling_table (id, name, next_id) VALUES (20, 'order_status', 5);
INSERT INTO jbilling_table (id, name, next_id) VALUES (23, 'pluggable_task_type_category', 10);
INSERT INTO jbilling_table (id, name, next_id) VALUES (24, 'pluggable_task_type', 20);
INSERT INTO jbilling_table (id, name, next_id) VALUES (30, 'invoice_line_type', 6);
INSERT INTO jbilling_table (id, name, next_id) VALUES (4, 'currency', 11);
INSERT INTO jbilling_table (id, name, next_id) VALUES (73, 'report_type', 10);
INSERT INTO jbilling_table (id, name, next_id) VALUES (35, 'payment_method', 9);
INSERT INTO jbilling_table (id, name, next_id) VALUES (41, 'payment_result', 5);
INSERT INTO jbilling_table (id, name, next_id) VALUES (46, 'event_log_module', 10);
INSERT INTO jbilling_table (id, name, next_id) VALUES (47, 'event_log_message', 17);
INSERT INTO jbilling_table (id, name, next_id) VALUES (50, 'preference_type', 36);
INSERT INTO jbilling_table (id, name, next_id) VALUES (52, 'notification_message_type', 20);
INSERT INTO jbilling_table (id, name, next_id) VALUES (60, 'role', 6);
INSERT INTO jbilling_table (id, name, next_id) VALUES (64, 'country', 238);
INSERT INTO jbilling_table (id, name, next_id) VALUES (77, 'list_entity', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (59, 'permission', 135);
INSERT INTO jbilling_table (id, name, next_id) VALUES (78, 'list_field_entity', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (67, 'currency_exchange', 25);
INSERT INTO jbilling_table (id, name, next_id) VALUES (26, 'pluggable_task_parameter', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (34, 'billing_process_configuration', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (17, 'order_period', 2);
INSERT INTO jbilling_table (id, name, next_id) VALUES (0, 'report', 20);
INSERT INTO jbilling_table (id, name, next_id) VALUES (79, 'partner_range', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (15, 'item_price', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (11, 'partner', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (5, 'entity', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (28, 'contact_type', 2);
INSERT INTO jbilling_table (id, name, next_id) VALUES (16, 'item_user_price', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (65, 'promotion', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (25, 'pluggable_task', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (75, 'ach', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (43, 'payment_info_cheque', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (70, 'partner_payout', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (38, 'billing_process_run_total_pm', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (71, 'report_user', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (1, 'report_field', 1604);
INSERT INTO jbilling_table (id, name, next_id) VALUES (66, 'payment_authorization', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (32, 'billing_process', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (33, 'billing_process_run', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (37, 'billing_process_run_total', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (31, 'paper_invoice_batch', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (51, 'preference', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (53, 'notification_message', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (54, 'notification_message_section', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (55, 'notification_message_line', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (69, 'ageing_entity_step', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (13, 'item_type', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (14, 'item', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (48, 'event_log', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (21, 'purchase_order', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (22, 'order_line', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (39, 'invoice', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (40, 'invoice_line', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (49, 'order_process', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (42, 'payment', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (56, 'notification_message_archive', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (57, 'notification_message_archive_line', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (10, 'base_user', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (12, 'customer', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (27, 'contact', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (76, 'contact_field', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (44, 'credit_card', 1);
INSERT INTO jbilling_table (id, name, next_id) VALUES (3, 'language', 2);


--
-- Data for TOC entry 271 (OID 4131147)
-- Name: jbilling_table_column; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 1, 'titleKey');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 2, 'instructionsKey');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 3, 'tables');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 4, 'where_str');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 5, 'id_column');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (0, 6, 'link');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 1, 'report_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 2, 'report_user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 3, 'position');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 4, 'table_name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 5, 'column_name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 6, 'order_position');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 7, 'where_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 8, 'title_key');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 9, 'function');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 10, 'is_grouped');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 11, 'is_shown');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 12, 'data_type');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 13, 'operator');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 14, 'functionable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 15, 'selectable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 16, 'ordenable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 17, 'operatorable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (1, 18, 'whereable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (53, 2, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (53, 3, 'language_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (54, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (54, 1, 'message_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (3, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (3, 1, 'code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (3, 2, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (4, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (4, 1, 'symbol');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (4, 2, 'code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (4, 3, 'country_code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (5, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (5, 1, 'external_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (5, 2, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (5, 3, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (5, 4, 'language_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (5, 5, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (6, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (7, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (8, 0, 'method_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (8, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (9, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (9, 1, 'can_login');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 2, 'user_name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 3, 'password');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 4, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 5, 'status_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 6, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 7, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 8, 'last_status_change');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (10, 9, 'language_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 1, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 2, 'balance');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 3, 'total_payments');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 4, 'total_refunds');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 5, 'total_payouts');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 6, 'percentage_rate');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 7, 'referral_fee');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 8, 'fee_currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 9, 'one_time');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 10, 'period_unit_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 11, 'period_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 12, 'next_payout_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 13, 'due_payout');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 14, 'automatic_process');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (11, 15, 'related_clerk');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (12, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (12, 1, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (12, 2, 'partner_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (12, 3, 'referral_fee_paid');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (12, 4, 'invoice_delivery_method_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (13, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (13, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (14, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (14, 1, 'internal_number');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (14, 2, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (14, 3, 'percentage');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (14, 4, 'price_manual');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (14, 5, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (15, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (15, 1, 'item_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (15, 2, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (15, 3, 'price');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (16, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (16, 1, 'item_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (16, 2, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (16, 3, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (16, 4, 'price');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (17, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (17, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (17, 2, 'value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (17, 3, 'unit_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (18, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (18, 1, 'editable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (19, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (20, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 1, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 2, 'period_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 3, 'billing_type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 4, 'active_since');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 5, 'active_until');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 6, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 7, 'next_billable_day');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 8, 'created_by');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 9, 'status_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 10, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (21, 11, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 1, 'order_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 2, 'item_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 3, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 4, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 5, 'amount');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 6, 'quantity');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 7, 'price');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 8, 'item_price');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 9, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (22, 10, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (23, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (23, 1, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (23, 2, 'interface_name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (24, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (24, 1, 'category_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (24, 2, 'min_parameters');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (24, 3, 'class_name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (25, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (25, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (25, 2, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (25, 3, 'processing_order');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (26, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (26, 1, 'task_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (26, 2, 'name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (26, 3, 'int_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (26, 4, 'str_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (26, 5, 'float_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 1, 'ORGANIZATION_NAME');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 2, 'STREET_ADDRES1');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 3, 'STREET_ADDRES2');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 4, 'CITY');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 5, 'STATE_PROVINCE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 6, 'POSTAL_CODE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 7, 'COUNTRY_CODE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 8, 'LAST_NAME');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 9, 'FIRST_NAME');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 10, 'PERSON_INITIAL');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 11, 'PERSON_TITLE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 12, 'PHONE_COUNTRY_CODE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 13, 'PHONE_AREA_CODE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 14, 'PHONE_PHONE_NUMBER');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 15, 'FAX_COUNTRY_CODE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 16, 'FAX_AREA_CODE');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 17, 'FAX_PHONE_NUMBER');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 18, 'EMAIL');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 19, 'CREATE_DATETIME');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (27, 20, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (28, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (28, 1, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (29, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (29, 1, 'contact_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (29, 2, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (29, 3, 'table_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (29, 4, 'foreign_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (30, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (30, 1, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (31, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (31, 1, 'total_invoices');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (31, 2, 'delivery_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (31, 3, 'is_self_managed');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 2, 'billing_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 3, 'period_unit_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 4, 'period_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 5, 'is_review');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (32, 6, 'paper_invoice_batch_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (33, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (33, 1, 'process_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (33, 2, 'run_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (33, 3, 'started');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (33, 4, 'finished');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (33, 5, 'invoices_generated');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 2, 'next_run_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 3, 'generate_report');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 4, 'retries');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 5, 'days_for_retry');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 6, 'days_for_report');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 7, 'review_status');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 8, 'period_unit_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (34, 9, 'period_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (35, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (36, 0, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (36, 1, 'payment_method_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (37, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (37, 1, 'process_run_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (37, 2, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (37, 3, 'total_invoiced');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (37, 4, 'total_paid');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (37, 5, 'total_not_paid');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (38, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (38, 1, 'process_run_total_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (38, 2, 'payment_method_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (38, 3, 'total');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 1, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 2, 'billing_process_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 3, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 4, 'delegated_invoice_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 5, 'due_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 6, 'total');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 7, 'to_process');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 8, 'balance');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 9, 'is_review');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 10, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 11, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 12, 'carried_balance');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (39, 13, 'paper_invoice_batch_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 1, 'invoice_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 2, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 3, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 4, 'amount');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 5, 'quantity');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 6, 'price');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (40, 7, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (41, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 1, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 2, 'attempt');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 3, 'result_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 4, 'amount');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 5, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 6, 'payment_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 7, 'method_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 8, 'credit_card_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 9, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 10, 'is_refund');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 11, 'payment_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 12, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (42, 13, 'payout_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (43, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (43, 1, 'payment_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (43, 2, 'bank');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (43, 3, 'cheque_number');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (43, 4, 'cheque_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (44, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (44, 1, 'cc_number');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (44, 2, 'cc_expiry');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (44, 3, 'name');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (44, 4, 'cc_type');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (44, 5, 'deleted');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (45, 0, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (45, 1, 'credit_card_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (46, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (47, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 2, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 3, 'table_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 4, 'foreign_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 5, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 6, 'level');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 7, 'module_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 8, 'message_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 9, 'old_num');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 10, 'old_str');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (48, 11, 'old_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 1, 'order_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 2, 'invoice_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 3, 'billing_process_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 4, 'periods_included');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 5, 'period_start');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (49, 6, 'period_ends');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (50, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 1, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 2, 'table_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 3, 'foreign_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 4, 'int_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 5, 'str_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (51, 6, 'float_value');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (52, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (52, 1, 'sections');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (53, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (53, 1, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (54, 2, 'section');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (55, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (55, 1, 'message_section_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (55, 2, 'content');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (56, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (56, 1, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (56, 2, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (56, 3, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (56, 4, 'result_message');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (57, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (57, 1, 'message_archive_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (57, 2, 'content');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (58, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (58, 1, 'description');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (59, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (59, 1, 'type_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (59, 2, 'foreign_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (60, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (61, 0, 'permission_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (61, 1, 'role_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (62, 0, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (62, 1, 'role_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (63, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (63, 1, 'link');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (63, 2, 'level');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (63, 3, 'parent_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (64, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (64, 1, 'code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 1, 'item_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 2, 'code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 3, 'notes');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 4, 'once');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 5, 'since');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (65, 6, 'until');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 1, 'payment_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 2, 'processor');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 3, 'code1');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 4, 'code2');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 5, 'code3');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 6, 'approval_code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 7, 'avs');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 8, 'transaction_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 9, 'md5');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (66, 10, 'card_code');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (67, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (67, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (67, 2, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (67, 3, 'rate');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (67, 4, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (68, 0, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (68, 1, 'currency_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (69, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (69, 1, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (69, 2, 'status_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (69, 3, 'days');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 1, 'starting_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 2, 'ending_date');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 3, 'payments_amount');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 4, 'refunds_amount');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 5, 'balance_left');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 6, 'payment_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (70, 7, 'partner_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (71, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (71, 1, 'user_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (71, 2, 'report_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (71, 3, 'create_datetime');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (71, 4, 'title');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (72, 0, 'entity_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (72, 1, 'report_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (73, 0, 'id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (73, 1, 'showable');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (74, 0, 'report_id');
INSERT INTO jbilling_table_column (table_id, id, name) VALUES (74, 1, 'type_id');


--
-- Data for TOC entry 272 (OID 4131149)
-- Name: international_description; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 20, 'description', 1, 'Manual invoice deletion');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 9, 'description', 1, 'Invoice maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 16, 'description', 1, 'A purchase order as been manually applied to an invoice.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 17, 'description', 1, 'Payment (failed)');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 16, 'description', 1, 'Payment (successful)');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 7, 'display', 1, 'Create New User');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 76, 'display', 1, 'Numbering');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 111, 'description', 1, 'Menu Reports invoice details option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 111, 'title', 1, 'Menu Reports Invoice Details');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 112, 'description', 1, 'Menu Reports users option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 112, 'title', 1, 'Menu Reports Users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 8, 'description', 1, 'User');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 6, 'description', 1, 'Discovery');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 7, 'description', 1, 'Diners');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 109, 'title', 1, 'Menu Payment ACH');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 109, 'description', 1, 'Menu Payment ACH');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 75, 'display', 1, 'ACH');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 16, 'description', 1, 'Days before expiration for order notification 2');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 17, 'description', 1, 'Days before expiration for order notification 3');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 13, 'description', 1, 'Order about to expire. Step 1');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 14, 'description', 1, 'Order about to expire. Step 2');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 15, 'description', 1, 'Order about to expire. Step 3');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 91, 'display', 1, 'Periods');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 113, 'title', 1, 'Invoice delete');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 110, 'description', 1, 'Menu Invoice Numbering');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 110, 'title', 1, 'Menu Invoice Numbering');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 4, 'description', 1, 'AMEX');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 5, 'description', 1, 'ACH');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 114, 'title', 1, 'User edit links');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 14, 'description', 1, 'Include customer notes in invoice');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 115, 'title', 1, 'User create - inital left menu');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 18, 'description', 1, 'Invoice number prefix');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 234, 'description', 1, 'Wallis and Futuna');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 235, 'description', 1, 'Yemen');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 236, 'description', 1, 'Zambia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 237, 'description', 1, 'Zimbabwe');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 1, 'description', 1, 'Order');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 2, 'description', 1, 'Invoice');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 3, 'description', 1, 'Payment');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 4, 'description', 1, 'Refund');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 5, 'description', 1, 'Customer');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 6, 'description', 1, 'Partner');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 7, 'description', 1, 'Partner selected');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 15, 'description', 1, 'Days before expiration for order notification');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 21, 'description', 1, 'Use invoice reminders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 22, 'description', 1, 'Number of days after the invoice generation for the first reminder');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 23, 'description', 1, 'Number of days for next reminder');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 227, 'description', 1, 'Uzbekistan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 107, 'title', 1, 'Menu Notif Preferences');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 107, 'description', 1, 'Menu Notification preferences');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 74, 'display', 1, 'Preferences');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 108, 'title', 1, 'Order left menu options');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 108, 'description', 1, 'Order options: g.invoice,');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 147, 'description', 1, 'Nauru');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 148, 'description', 1, 'Nepal');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 149, 'description', 1, 'Netherlands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 150, 'description', 1, 'Netherlands Antilles');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 151, 'description', 1, 'New Caledonia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 152, 'description', 1, 'New Zealand');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 153, 'description', 1, 'Nicaragua');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 154, 'description', 1, 'Niger');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 155, 'description', 1, 'Nigeria');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 156, 'description', 1, 'Niue');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 157, 'description', 1, 'Norfolk Island');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 158, 'description', 1, 'North Korea');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 159, 'description', 1, 'Northern Mariana Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 160, 'description', 1, 'Norway');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 161, 'description', 1, 'Oman');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 162, 'description', 1, 'Pakistan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 163, 'description', 1, 'Palau');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 164, 'description', 1, 'Panama');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 165, 'description', 1, 'Papua New Guinea');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 166, 'description', 1, 'Paraguay');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 167, 'description', 1, 'Peru');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 168, 'description', 1, 'Philippines');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 169, 'description', 1, 'Pitcairn Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 170, 'description', 1, 'Poland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 171, 'description', 1, 'Portugal');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 172, 'description', 1, 'Puerto Rico');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 173, 'description', 1, 'Qatar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 174, 'description', 1, 'Reunion');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 175, 'description', 1, 'Romania');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 176, 'description', 1, 'Russia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 177, 'description', 1, 'Rwanda');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 178, 'description', 1, 'Samoa');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 179, 'description', 1, 'San Marino');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 180, 'description', 1, 'S�o Tom� and Pr�ncipe');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 181, 'description', 1, 'Saudi Arabia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 182, 'description', 1, 'Senegal');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 183, 'description', 1, 'Serbia and Montenegro');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 184, 'description', 1, 'Seychelles');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 185, 'description', 1, 'Sierra Leone');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 186, 'description', 1, 'Singapore');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 187, 'description', 1, 'Slovakia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 188, 'description', 1, 'Slovenia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 189, 'description', 1, 'Solomon Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 190, 'description', 1, 'Somalia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 191, 'description', 1, 'South Africa');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 192, 'description', 1, 'South Georgia and the South Sandwich Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 193, 'description', 1, 'Spain');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 194, 'description', 1, 'Sri Lanka');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 195, 'description', 1, 'St. Helena');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 196, 'description', 1, 'St. Kitts and Nevis');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 197, 'description', 1, 'St. Lucia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 198, 'description', 1, 'St. Pierre and Miquelon');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 199, 'description', 1, 'St. Vincent and the Grenadines');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 200, 'description', 1, 'Sudan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 201, 'description', 1, 'Suriname');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 202, 'description', 1, 'Svalbard and Jan Mayen');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 203, 'description', 1, 'Swaziland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 204, 'description', 1, 'Sweden');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 205, 'description', 1, 'Switzerland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 206, 'description', 1, 'Syria');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 207, 'description', 1, 'Taiwan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 208, 'description', 1, 'Tajikistan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 209, 'description', 1, 'Tanzania');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 210, 'description', 1, 'Thailand');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 211, 'description', 1, 'Togo');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 212, 'description', 1, 'Tokelau');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 213, 'description', 1, 'Tonga');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 214, 'description', 1, 'Trinidad and Tobago');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 215, 'description', 1, 'Tunisia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 216, 'description', 1, 'Turkey');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 217, 'description', 1, 'Turkmenistan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 218, 'description', 1, 'Turks and Caicos Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 219, 'description', 1, 'Tuvalu');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 220, 'description', 1, 'Uganda');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 221, 'description', 1, 'Ukraine');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 222, 'description', 1, 'United Arab Emirates');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 223, 'description', 1, 'United Kingdom');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 224, 'description', 1, 'United States');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 225, 'description', 1, 'United States Minor Outlying Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 226, 'description', 1, 'Uruguay');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 228, 'description', 1, 'Vanuatu');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 229, 'description', 1, 'Vatican City');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 230, 'description', 1, 'Venezuela');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 231, 'description', 1, 'Viet Nam');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 232, 'description', 1, 'Virgin Islands &#40;British&#41;');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 233, 'description', 1, 'Virgin Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 57, 'description', 1, 'Congo &#40;DRC&#41;');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 58, 'description', 1, 'Denmark');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 59, 'description', 1, 'Djibouti');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 60, 'description', 1, 'Dominica');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 61, 'description', 1, 'Dominican Republic');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 62, 'description', 1, 'East Timor');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 63, 'description', 1, 'Ecuador');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 64, 'description', 1, 'Egypt');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 65, 'description', 1, 'El Salvador');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 66, 'description', 1, 'Equatorial Guinea');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 67, 'description', 1, 'Eritrea');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 68, 'description', 1, 'Estonia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 69, 'description', 1, 'Ethiopia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 70, 'description', 1, 'Falkland Islands &#40;Islas Malvinas&#41;');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 71, 'description', 1, 'Faroe Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 72, 'description', 1, 'Fiji Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 73, 'description', 1, 'Finland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 74, 'description', 1, 'France');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 75, 'description', 1, 'French Guiana');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 76, 'description', 1, 'French Polynesia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 77, 'description', 1, 'French Southern and Antarctic Lands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 78, 'description', 1, 'Gabon');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 79, 'description', 1, 'Gambia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 80, 'description', 1, 'Georgia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 81, 'description', 1, 'Germany');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 82, 'description', 1, 'Ghana');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 83, 'description', 1, 'Gibraltar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 84, 'description', 1, 'Greece');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 85, 'description', 1, 'Greenland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 86, 'description', 1, 'Grenada');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 87, 'description', 1, 'Guadeloupe');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 88, 'description', 1, 'Guam');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 89, 'description', 1, 'Guatemala');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 90, 'description', 1, 'Guinea');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 91, 'description', 1, 'Guinea-Bissau');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 92, 'description', 1, 'Guyana');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 93, 'description', 1, 'Haiti');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 94, 'description', 1, 'Heard Island and McDonald Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 95, 'description', 1, 'Honduras');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 96, 'description', 1, 'Hong Kong SAR');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 97, 'description', 1, 'Hungary');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 98, 'description', 1, 'Iceland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 99, 'description', 1, 'India');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 100, 'description', 1, 'Indonesia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 101, 'description', 1, 'Iran');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 102, 'description', 1, 'Iraq');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 103, 'description', 1, 'Ireland');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 104, 'description', 1, 'Israel');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 105, 'description', 1, 'Italy');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 106, 'description', 1, 'Jamaica');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 107, 'description', 1, 'Japan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 108, 'description', 1, 'Jordan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 109, 'description', 1, 'Kazakhstan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 110, 'description', 1, 'Kenya');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 111, 'description', 1, 'Kiribati');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 112, 'description', 1, 'Korea');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 113, 'description', 1, 'Kuwait');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 114, 'description', 1, 'Kyrgyzstan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 115, 'description', 1, 'Laos');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 116, 'description', 1, 'Latvia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 117, 'description', 1, 'Lebanon');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 118, 'description', 1, 'Lesotho');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 119, 'description', 1, 'Liberia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 120, 'description', 1, 'Libya');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 121, 'description', 1, 'Liechtenstein');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 122, 'description', 1, 'Lithuania');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 123, 'description', 1, 'Luxembourg');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 124, 'description', 1, 'Macao SAR');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 125, 'description', 1, 'Macedonia, Former Yugoslav Republic of');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 126, 'description', 1, 'Madagascar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 127, 'description', 1, 'Malawi');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 128, 'description', 1, 'Malaysia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 129, 'description', 1, 'Maldives');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 130, 'description', 1, 'Mali');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 131, 'description', 1, 'Malta');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 132, 'description', 1, 'Marshall Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 133, 'description', 1, 'Martinique');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 134, 'description', 1, 'Mauritania');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 135, 'description', 1, 'Mauritius');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 136, 'description', 1, 'Mayotte');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 137, 'description', 1, 'Mexico');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 138, 'description', 1, 'Micronesia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 139, 'description', 1, 'Moldova');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 140, 'description', 1, 'Monaco');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 141, 'description', 1, 'Mongolia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 142, 'description', 1, 'Montserrat');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 143, 'description', 1, 'Morocco');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 144, 'description', 1, 'Mozambique');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 145, 'description', 1, 'Myanmar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 146, 'description', 1, 'Namibia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 37, 'display', 1, 'Process');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 38, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 39, 'display', 1, 'Configuration');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 40, 'display', 1, 'Latest');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 41, 'display', 1, 'Review');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 42, 'display', 1, 'Notification');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 43, 'display', 1, 'Compose');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 44, 'display', 1, 'Parameters');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 45, 'display', 1, 'Emails list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 46, 'display', 1, 'Customers');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 47, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 48, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 49, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 50, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 51, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 52, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 53, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 54, 'display', 1, 'New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 55, 'display', 1, 'Branding');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 56, 'display', 1, 'Currencies');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 57, 'display', 1, 'Ageing');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 58, 'display', 1, 'Create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 59, 'display', 1, 'Partners');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 60, 'display', 1, 'Customers');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 61, 'display', 1, 'New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 62, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 63, 'display', 1, 'Defaults');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 64, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 65, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 66, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 67, 'display', 1, 'New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 68, 'display', 1, 'Statement');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 69, 'display', 1, 'Latest');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 70, 'display', 1, 'Payouts');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 71, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 72, 'display', 1, 'Partners Due Payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 73, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 1, 'description', 1, 'Afghanistan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 2, 'description', 1, 'Albania');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 3, 'description', 1, 'Algeria');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 4, 'description', 1, 'American Samoa');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 5, 'description', 1, 'Andorra');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 6, 'description', 1, 'Angola');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 7, 'description', 1, 'Anguilla');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 8, 'description', 1, 'Antarctica');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 9, 'description', 1, 'Antigua and Barbuda');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 10, 'description', 1, 'Argentina');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 11, 'description', 1, 'Armenia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 12, 'description', 1, 'Aruba');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 13, 'description', 1, 'Australia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 14, 'description', 1, 'Austria');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 15, 'description', 1, 'Azerbaijan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 16, 'description', 1, 'Bahamas');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 17, 'description', 1, 'Bahrain');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 18, 'description', 1, 'Bangladesh');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 19, 'description', 1, 'Barbados');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 20, 'description', 1, 'Belarus');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 21, 'description', 1, 'Belgium');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 22, 'description', 1, 'Belize');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 23, 'description', 1, 'Benin');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 24, 'description', 1, 'Bermuda');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 25, 'description', 1, 'Bhutan');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 26, 'description', 1, 'Bolivia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 27, 'description', 1, 'Bosnia and Herzegovina');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 28, 'description', 1, 'Botswana');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 29, 'description', 1, 'Bouvet Island');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 30, 'description', 1, 'Brazil');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 31, 'description', 1, 'British Indian Ocean Territory');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 32, 'description', 1, 'Brunei');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 33, 'description', 1, 'Bulgaria');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 34, 'description', 1, 'Burkina Faso');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 35, 'description', 1, 'Burundi');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 36, 'description', 1, 'Cambodia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 37, 'description', 1, 'Cameroon');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 38, 'description', 1, 'Canada');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 39, 'description', 1, 'Cape Verde');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 40, 'description', 1, 'Cayman Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 41, 'description', 1, 'Central African Republic');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 42, 'description', 1, 'Chad');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 43, 'description', 1, 'Chile');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 44, 'description', 1, 'China');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 45, 'description', 1, 'Christmas Island');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 46, 'description', 1, 'Cocos &#40;Keeling&#41; Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 47, 'description', 1, 'Colombia');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 48, 'description', 1, 'Comoros');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 49, 'description', 1, 'Congo');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 50, 'description', 1, 'Cook Islands');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 51, 'description', 1, 'Costa Rica');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 52, 'description', 1, 'C�te d&#39;Ivoire');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 53, 'description', 1, 'Croatia &#40;Hrvatska&#41;');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 54, 'description', 1, 'Cuba');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 55, 'description', 1, 'Cyprus');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (64, 56, 'description', 1, 'Czech Republic');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 89, 'title', 1, 'Menu Partner - New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 90, 'description', 1, 'Menu Partner option - List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 90, 'title', 1, 'Menu Partner - List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 91, 'description', 1, 'Menu Partner option - Defaults');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 91, 'title', 1, 'Menu Partner - Defaults');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 92, 'description', 1, 'Menu Partner option - Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 92, 'title', 1, 'Menu Partner - Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 93, 'description', 1, 'Menu Customer option - Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 93, 'title', 1, 'Menu Customer - Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 94, 'description', 1, 'Menu Customer option - List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 94, 'title', 1, 'Menu Customer - List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 95, 'description', 1, 'Menu Customer option - New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 95, 'title', 1, 'Menu Customer - New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 96, 'description', 1, 'Menu Statement option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 96, 'title', 1, 'Menu Statement');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 97, 'description', 1, 'Menu Statement option - latest');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 97, 'title', 1, 'Menu Statement latest');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 98, 'description', 1, 'Menu Statement option - Payouts');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 98, 'title', 1, 'Menu Statement payouts');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 99, 'description', 1, 'Report Partners customers orders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 99, 'title', 1, 'Report partner ordres');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 100, 'description', 1, 'Report Partners customers payments');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 100, 'title', 1, 'Report partner payments');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 101, 'description', 1, 'Report partners customers refunds');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 101, 'title', 1, 'Report partner refunds');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 102, 'description', 1, 'Report General partners');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 102, 'title', 1, 'Report General partners');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 103, 'description', 1, 'Report General payouts');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 103, 'title', 1, 'Report General payouts');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 104, 'description', 1, 'Menu Reports parterns option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 1, 'description', 1, 'United States Dollar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 2, 'description', 1, 'Canadian Dollar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 3, 'description', 1, 'Euro');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 4, 'description', 1, 'Yen');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 5, 'description', 1, 'Pound Sterling');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 6, 'description', 1, 'Won');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 7, 'description', 1, 'Swiss Franc');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 8, 'description', 1, 'Swedish Krona');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (6, 1, 'description', 1, 'Month');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (6, 2, 'description', 1, 'Week');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (6, 3, 'description', 1, 'Day');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (6, 4, 'description', 1, 'Year');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (7, 1, 'description', 1, 'Email');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (7, 2, 'description', 1, 'Paper');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 1, 'description', 1, 'Active');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 2, 'description', 1, 'Overdue');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 3, 'description', 1, 'Overdue 2');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 4, 'description', 1, 'Overdue 3');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 5, 'description', 1, 'Suspended');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 6, 'description', 1, 'Suspended 2');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 7, 'description', 1, 'Suspended 3');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (9, 8, 'description', 1, 'Deleted');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 104, 'title', 1, 'Menu Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 105, 'description', 1, 'Menu Partner option - due payment');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 105, 'title', 1, 'Menu Partner - payable');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 106, 'description', 1, 'Menu Reports list parterns option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 106, 'title', 1, 'Menu Reports List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 1, 'description', 1, 'An internal user with all the permissions');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 1, 'title', 1, 'Internal');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 2, 'description', 1, 'The super user of an entity');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 2, 'title', 1, 'Super user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 3, 'description', 1, 'A billing clerk');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 3, 'title', 1, 'Clerk');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 4, 'description', 1, 'A partner that will bring customers');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 4, 'title', 1, 'Partner');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 5, 'description', 1, 'A customer that will query his/her account');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (60, 5, 'title', 1, 'Customer');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 1, 'display', 1, 'Orders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 2, 'display', 1, 'Payments');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 3, 'display', 1, 'Reports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (17, 1, 'description', 1, 'One time');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (18, 1, 'description', 1, 'Items');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (18, 2, 'description', 1, 'Tax');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (19, 1, 'description', 1, 'pre paid');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (19, 2, 'description', 1, 'post paid');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (20, 1, 'description', 1, 'Active');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (20, 2, 'description', 1, 'Finished');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (20, 3, 'description', 1, 'Suspended');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 1, 'description', 1, 'Takes the quantity and the price to calculate the totals for each line and the order total');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 1, 'title', 1, 'Basic calculation of totals');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 2, 'description', 1, 'Adds a line with a 7% and update the order total');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 2, 'title', 1, 'GST calculation');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 3, 'description', 1, 'Adds one month to the billing date for the due date');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 3, 'title', 1, 'Due date calculation');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 4, 'description', 1, 'Copies all the lines to generate the invoice');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 4, 'title', 1, 'Basic invoice generation');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 5, 'description', 1, 'Considers the active period and the last process');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 5, 'title', 1, 'Basic order filter');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 6, 'description', 1, 'Takes only those invoices with due date past the process date');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 6, 'title', 1, 'Basic invoice filter');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 7, 'description', 1, 'Most common logic to calculate the billable period of an order');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 7, 'title', 1, 'Basic period calculator');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 8, 'description', 1, 'Authorize.net payment process');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 8, 'title', 1, 'Authorize.net payment');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 9, 'description', 1, 'Simple email notification');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 9, 'title', 1, 'Simple email notification');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 10, 'description', 1, 'Gets a valid cc from the user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 10, 'title', 1, 'Simple payment instrument finder');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 11, 'description', 1, 'Test partner payout processor');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 11, 'title', 1, 'Test partner payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 12, 'description', 1, 'Paper invoice notification with JasperReports');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (24, 12, 'title', 1, 'Paper invoice notificaiton');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 1, 'description', 1, 'Cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 2, 'description', 1, 'Visa');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 3, 'description', 1, 'MasterCard');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (41, 1, 'description', 1, 'Successful');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (41, 2, 'description', 1, 'Failed');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (41, 3, 'description', 1, 'Processor unavailable');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (41, 4, 'description', 1, 'Entered');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 1, 'description', 1, 'Billing Process');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 2, 'description', 1, 'User maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 3, 'description', 1, 'Item maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 4, 'description', 1, 'Item type maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 5, 'description', 1, 'Item user price maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 6, 'description', 1, 'Promotion maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 7, 'description', 1, 'Order maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (46, 8, 'description', 1, 'Credit card maintenance');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 1, 'description', 1, 'A prepaid order has unbilled time before the billing process date');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 2, 'description', 1, 'Order has no active time at the date of process.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 3, 'description', 1, 'At least one complete period has to be billable.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 4, 'description', 1, 'Already billed for the current date.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 5, 'description', 1, 'This order had to be maked for exclusion in the last process.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 6, 'description', 1, 'Pre-paid order is being process after its expiration.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 7, 'description', 1, 'A row was marked as deleted.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 8, 'description', 1, 'A user password was changed.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 9, 'description', 1, 'A row was updated.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 4, 'display', 1, 'System');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 10, 'description', 1, 'Running a billing process, but a review is found unapproved.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 11, 'description', 1, 'Running a billing process, review is required but not present.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 12, 'description', 1, 'A user status was changed.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 13, 'description', 1, 'An order status was changed.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 14, 'description', 1, 'A user had to be aged, but there''s no more steps configured.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (47, 15, 'description', 1, 'A partner has a payout ready, but no payment instrument.');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 1, 'description', 1, 'Process payment with billing process');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 2, 'description', 1, 'URL of CSS file');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 3, 'description', 1, 'URL of logo graphic');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 4, 'description', 1, 'Grace period');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 5, 'description', 1, 'Partner percentage rate');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 6, 'description', 1, 'Partner referral fee');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 7, 'description', 1, 'Partner one time payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 8, 'description', 1, 'Partner period unit payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 9, 'description', 1, 'Partner period value payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 10, 'description', 1, 'Partner automatic payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 11, 'description', 1, 'User in charge of partners ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 12, 'description', 1, 'Partner fee currency');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 13, 'description', 1, 'Self delivery of paper invoices');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 1, 'description', 1, 'Invoice (email)');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 2, 'description', 1, 'User Reactivated');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 3, 'description', 1, 'User Overdue');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 4, 'description', 1, 'User Overdue 2');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 5, 'description', 1, 'User Overdue 3');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 6, 'description', 1, 'User Suspended');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 7, 'description', 1, 'User Suspended 2');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 8, 'description', 1, 'User Suspended 3');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 9, 'description', 1, 'User Deleted');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 10, 'description', 1, 'Payout Remainder');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 11, 'description', 1, 'Partner Payout');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 12, 'description', 1, 'Invoice (paper)');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 1, 'description', 1, 'Menu Order option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 1, 'title', 1, 'Menu Order option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 2, 'description', 1, 'Menu Payment option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 2, 'title', 1, 'Menu Payment option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 3, 'description', 1, 'Menu Report option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 3, 'title', 1, 'Menu Report option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 4, 'description', 1, 'Menu System option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 4, 'title', 1, 'Menu System option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 5, 'description', 1, 'Menu System-User option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 5, 'title', 1, 'Menu System-User option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 6, 'description', 1, 'Menu System-User-All option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 6, 'title', 1, 'Menu System-User-All option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 7, 'description', 1, 'Selection of user type when creating a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 7, 'title', 1, 'User type selection');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 8, 'description', 1, 'Can create root users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 8, 'title', 1, 'Can create root users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 9, 'description', 1, 'Can create clerk users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 9, 'title', 1, 'Can create clerk users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 10, 'description', 1, 'Can create partner users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 10, 'title', 1, 'Can create partner users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 11, 'description', 1, 'Can create customer users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 11, 'title', 1, 'Can create customer users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 12, 'description', 1, 'Can change entity when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 12, 'title', 1, 'Can change entity');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 13, 'description', 1, 'Can change type when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 13, 'title', 1, 'Can change type');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 14, 'description', 1, 'Can view type when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 14, 'title', 1, 'Can view type');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 15, 'description', 1, 'Can change username when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 15, 'title', 1, 'Can change username');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 16, 'description', 1, 'Can change password when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 16, 'title', 1, 'Can change password');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 17, 'description', 1, 'Can change langauge when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 17, 'title', 1, 'Can change langauge');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 18, 'description', 1, 'Can view language when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 18, 'title', 1, 'Can view language');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 19, 'description', 1, 'Menu System-User-Maintain option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 19, 'title', 1, 'Menu System-User-Maintain option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 20, 'description', 1, 'Can change user status when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 20, 'title', 1, 'Can change status');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 21, 'description', 1, 'Can view status when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 21, 'title', 1, 'Can view status');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 22, 'description', 1, 'Menu Account sub-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 22, 'title', 1, 'Menu Account sub-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 23, 'description', 1, 'Menu change password lm-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 23, 'title', 1, 'Menu change password lm-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 24, 'description', 1, 'Menu edit contact info lm-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 24, 'title', 1, 'Menu edit contact info lm-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 25, 'description', 1, 'Menu account option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 25, 'title', 1, 'Menu account option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 26, 'description', 1, 'Menu change password sub-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 26, 'title', 1, 'Menu change password sub-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 27, 'description', 1, 'Menu edit contact info sub-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 27, 'title', 1, 'Menu edit contact info sub-option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 28, 'description', 1, 'Menu logout option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 28, 'title', 1, 'Menu logout option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 29, 'description', 1, 'Menu items option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 29, 'title', 1, 'Menu items option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 30, 'description', 1, 'Menu Items - Types option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 30, 'title', 1, 'Menu Items - Types option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 31, 'description', 1, 'Menu Items - Create option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 31, 'title', 1, 'Menu Items - Create option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 32, 'description', 1, 'Menu Items - List option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 32, 'title', 1, 'Menu Items - List option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 33, 'description', 1, 'Menu Items - Types - Create option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 33, 'title', 1, 'Menu Items - Types - Create option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 34, 'description', 1, 'Can edit item fields (read-write)');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 34, 'title', 1, 'Can edit item fields');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 35, 'description', 1, 'Menu Items - Types - List option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 35, 'title', 1, 'Menu Items - Types - List option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 36, 'description', 1, 'Menu Promotion');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 36, 'title', 1, 'Menu Promotion');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 37, 'description', 1, 'Menu Promotion create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 37, 'title', 1, 'Menu Promotion create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 38, 'description', 1, 'Menu Promotion list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 38, 'title', 1, 'Menu Promotion list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 39, 'description', 1, 'Menu Payments new cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 39, 'title', 1, 'Menu Payments new cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 40, 'description', 1, 'Menu Payments new credit card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 40, 'title', 1, 'Menu Payments new cc');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 41, 'description', 1, 'Menu Payments list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 41, 'title', 1, 'Menu Payments list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 42, 'description', 1, 'Menu Order create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 42, 'title', 1, 'Menu Order create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 43, 'description', 1, 'Menu Order list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 43, 'title', 1, 'Menu Order list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 44, 'description', 1, 'Menu Credit Card edit');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 44, 'title', 1, 'Menu Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 45, 'description', 1, 'Menu Credit Card edit');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 45, 'title', 1, 'Menu Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 46, 'description', 1, 'Menu Refund option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 46, 'title', 1, 'Menu Refund option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 47, 'description', 1, 'Menu Refund option - cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 47, 'title', 1, 'Menu Refund option - cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 48, 'description', 1, 'Menu Refund option - credit card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 48, 'title', 1, 'Menu Refund option - cc');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 49, 'description', 1, 'Menu Refund option - list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 49, 'title', 1, 'Menu Refund list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 50, 'description', 1, 'Menu Invoice option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 50, 'title', 1, 'Menu Invoice');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 51, 'description', 1, 'Menu Invoice option - list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 51, 'title', 1, 'Menu Invoice list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 52, 'description', 1, 'Menu Process option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 52, 'title', 1, 'Menu Process option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 53, 'description', 1, 'Menu Process - list option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 53, 'title', 1, 'Menu Process list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 54, 'description', 1, 'Menu Process - edit configuration option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 54, 'title', 1, 'Menu Process - Configuration');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 55, 'description', 1, 'Menu Process - see latest option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 55, 'title', 1, 'Menu Process - Latest');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 56, 'description', 1, 'Menu Process - Review option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 56, 'title', 1, 'Menu Process review');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 57, 'description', 1, 'Menu Notification');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 57, 'title', 1, 'Menu Notification');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 58, 'description', 1, 'Menu Notification - Compose');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 58, 'title', 1, 'Menu Notification Compose');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 59, 'description', 1, 'Menu Notification - Parameters');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 59, 'title', 1, 'Menu Notificaiton Parameters');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 60, 'description', 1, 'Menu Notification - Emails list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 60, 'title', 1, 'Menu Notification Emails');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 61, 'description', 1, 'Menu Customers ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 61, 'title', 1, 'Menu Customers');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 62, 'description', 1, 'Menu Reports - Orders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 62, 'title', 1, 'Menu Reports Orders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 63, 'description', 1, 'Menu Reports - Invoice');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 63, 'title', 1, 'Menu Reports Invoice');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 64, 'description', 1, 'Menu Reports - Payment');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 64, 'title', 1, 'Menu Reports Payment');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 65, 'description', 1, 'Menu Reports - Refund');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 65, 'title', 1, 'Menu Reports Refund');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 66, 'description', 1, 'Menu Reports - Customer');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 66, 'title', 1, 'Menu Reports Customer');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 67, 'description', 1, 'Report General orders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 67, 'title', 1, 'Report General orders');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 68, 'description', 1, 'Report General invoices');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 68, 'title', 1, 'Report General invoices');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 69, 'description', 1, 'Report General payments');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 69, 'title', 1, 'Report General payments');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 70, 'description', 1, 'Report General order lines');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 70, 'title', 1, 'Report General order lines');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 71, 'description', 1, 'Report General Refunds ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 71, 'title', 1, 'Report General Refunds ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 72, 'description', 1, 'Report Total invoiced by date range ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 72, 'title', 1, 'Report Total invoiced ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 73, 'description', 1, 'Report Total payments by date range ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 73, 'title', 1, 'Report Total payments');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 74, 'description', 1, 'Report Total refunds by date range ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 74, 'title', 1, 'Report Total refunds');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 75, 'description', 1, 'Report Total ordered by date range ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 75, 'title', 1, 'Report Total ordered');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 76, 'description', 1, 'Invoices overdue ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 76, 'title', 1, 'Report Invoices overdue ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 77, 'description', 1, 'Menu Customer option - list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 77, 'title', 1, 'Menu Customer list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 78, 'description', 1, 'Menu Report option - list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 78, 'title', 1, 'Menu Report list');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 79, 'description', 1, 'Menu Customer option - new');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 79, 'title', 1, 'Menu Customer New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 80, 'description', 1, 'Menu System option - Branding');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 80, 'title', 1, 'Menu System - Branding');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 81, 'description', 1, 'Can change currency when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 81, 'title', 1, 'Can change langauge');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 82, 'description', 1, 'Can view currency when editing a user');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 82, 'title', 1, 'Can view language');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 83, 'description', 1, 'Menu System option - Currencies');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 83, 'title', 1, 'Menu System - Currencies');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 84, 'description', 1, 'Invoices carring a balance ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 84, 'title', 1, 'Report Invoices carring a balance ');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 85, 'description', 1, 'Menu System option - Ageing');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 85, 'title', 1, 'Menu System - Ageing');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 86, 'description', 1, 'Menu Users option - All create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 86, 'title', 1, 'Menu Users - All create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 87, 'description', 1, 'Menu Partner option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 87, 'title', 1, 'Menu Partner');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 88, 'description', 1, 'Menu Customer option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 88, 'title', 1, 'Menu Customer');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 89, 'description', 1, 'Menu Partner option - New');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 18, 'description', 1, 'Invoice Reminder');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 113, 'description', 1, 'Invoice delete left menu option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 114, 'description', 1, 'User edit links');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 115, 'description', 1, 'User create - inital left menu');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 77, 'display', 1, 'Edit ACH');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 116, 'description', 1, 'Menu edit ach option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (59, 116, 'title', 1, 'Menu edit ach option');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 5, 'display', 1, 'Users');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 8, 'display', 1, 'My Account');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 9, 'display', 1, 'Change Password');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 10, 'display', 1, 'Edit Contact Information');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 11, 'display', 1, 'Account');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 12, 'display', 1, 'Change Password');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 13, 'display', 1, 'Edit Contact Information');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 14, 'display', 1, 'OBSOLETED');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 15, 'display', 1, 'Items');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 16, 'display', 1, 'Types');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 17, 'display', 1, 'Create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 18, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 19, 'display', 1, 'Create Type');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 20, 'display', 1, 'List/Edit Types');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 21, 'display', 1, 'Promotions');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 22, 'display', 1, 'Create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 23, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 24, 'display', 1, 'Cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 25, 'display', 1, 'Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 26, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 27, 'display', 1, 'Create');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 28, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 29, 'display', 1, 'Edit Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 30, 'display', 1, 'Edit Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 31, 'display', 1, 'Refunds');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 32, 'display', 1, 'Cheque');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 33, 'display', 1, 'Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 34, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 35, 'display', 1, 'Invoices');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 36, 'display', 1, 'List');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 24, 'description', 1, 'Data Fattura Fine Mese');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 9, 'description', 1, 'Singapore Dollar');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (4, 10, 'description', 1, 'Malaysian Ringgit');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (50, 19, 'description', 1, 'Next invoice number');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 83, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 84, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 85, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 86, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 87, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 88, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 89, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (7, 3, 'description', 1, 'Email + Paper');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 79, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (35, 8, 'description', 1, 'PayPal');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 90, 'display', 1, 'PayPal');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 78, 'display', 1, 'Sub-accounts');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 6, 'display', 1, 'Staff');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (73, 9, 'description', 1, 'Item');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 82, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 81, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (63, 80, 'display', 1, 'Help');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (52, 19, 'description', 1, 'Update Credit Card');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (20, 4, 'description', 1, 'Suspended (auto)');
INSERT INTO international_description (table_id, foreign_id, psudo_column, language_id, content) VALUES (18, 3, 'description', 1, 'Penalty');


--
-- Data for TOC entry 273 (OID 4131154)
-- Name: language; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO "language" (id, code, description) VALUES (1, 'en', 'English');


--
-- Data for TOC entry 274 (OID 4131156)
-- Name: event_log; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 275 (OID 4131158)
-- Name: event_log_module; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO event_log_module (id) VALUES (1);
INSERT INTO event_log_module (id) VALUES (2);
INSERT INTO event_log_module (id) VALUES (3);
INSERT INTO event_log_module (id) VALUES (4);
INSERT INTO event_log_module (id) VALUES (5);
INSERT INTO event_log_module (id) VALUES (6);
INSERT INTO event_log_module (id) VALUES (7);
INSERT INTO event_log_module (id) VALUES (8);
INSERT INTO event_log_module (id) VALUES (9);


--
-- Data for TOC entry 276 (OID 4131160)
-- Name: event_log_message; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO event_log_message (id) VALUES (1);
INSERT INTO event_log_message (id) VALUES (2);
INSERT INTO event_log_message (id) VALUES (3);
INSERT INTO event_log_message (id) VALUES (4);
INSERT INTO event_log_message (id) VALUES (5);
INSERT INTO event_log_message (id) VALUES (6);
INSERT INTO event_log_message (id) VALUES (7);
INSERT INTO event_log_message (id) VALUES (8);
INSERT INTO event_log_message (id) VALUES (9);
INSERT INTO event_log_message (id) VALUES (10);
INSERT INTO event_log_message (id) VALUES (11);
INSERT INTO event_log_message (id) VALUES (12);
INSERT INTO event_log_message (id) VALUES (13);
INSERT INTO event_log_message (id) VALUES (14);
INSERT INTO event_log_message (id) VALUES (15);
INSERT INTO event_log_message (id) VALUES (16);


--
-- Data for TOC entry 277 (OID 4131162)
-- Name: preference; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 278 (OID 4131164)
-- Name: preference_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (1, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (25, 0, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (26, 20, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (4, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (5, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (6, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (7, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (8, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (9, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (10, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (11, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (12, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (13, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (14, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (15, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (16, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (17, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (18, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (27, 0, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (22, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (23, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (2, NULL, '/billing/css/jbilling.css', NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (3, NULL, '/billing/graphics/jb-log-small.jpg', NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (20, 1, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (24, 0, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (19, 1, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (21, 0, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (28, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (31, NULL, '2000-01-01', NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (30, NULL, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (29, NULL, 'https://www.paypal.com/en_US/i/btn/x-click-but6.gif', NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (32, 0, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (33, 0, NULL, NULL);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (34, NULL, NULL, 0);
INSERT INTO preference_type (id, int_def_value, str_def_value, float_def_value) VALUES (35, 0, NULL, NULL);


--
-- Data for TOC entry 279 (OID 4131166)
-- Name: notification_message; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 280 (OID 4131169)
-- Name: notification_message_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO notification_message_type (id, sections) VALUES (1, 2);
INSERT INTO notification_message_type (id, sections) VALUES (2, 2);
INSERT INTO notification_message_type (id, sections) VALUES (3, 2);
INSERT INTO notification_message_type (id, sections) VALUES (4, 2);
INSERT INTO notification_message_type (id, sections) VALUES (5, 2);
INSERT INTO notification_message_type (id, sections) VALUES (6, 2);
INSERT INTO notification_message_type (id, sections) VALUES (7, 2);
INSERT INTO notification_message_type (id, sections) VALUES (8, 2);
INSERT INTO notification_message_type (id, sections) VALUES (9, 2);
INSERT INTO notification_message_type (id, sections) VALUES (10, 2);
INSERT INTO notification_message_type (id, sections) VALUES (11, 2);
INSERT INTO notification_message_type (id, sections) VALUES (12, 2);
INSERT INTO notification_message_type (id, sections) VALUES (13, 2);
INSERT INTO notification_message_type (id, sections) VALUES (14, 2);
INSERT INTO notification_message_type (id, sections) VALUES (15, 2);
INSERT INTO notification_message_type (id, sections) VALUES (16, 2);
INSERT INTO notification_message_type (id, sections) VALUES (17, 2);
INSERT INTO notification_message_type (id, sections) VALUES (18, 2);
INSERT INTO notification_message_type (id, sections) VALUES (19, 2);


--
-- Data for TOC entry 281 (OID 4131171)
-- Name: notification_message_section; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 282 (OID 4131173)
-- Name: notification_message_line; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 283 (OID 4131175)
-- Name: notification_message_archive; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 284 (OID 4131177)
-- Name: notification_message_archive_line; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 285 (OID 4131179)
-- Name: report; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (1, 'report.general_orders.title', 'report.general_orders.instr', ' base_user, purchase_order, order_period, order_billing_type, international_description id, jbilling_table bt, international_description id2, jbilling_table bt2, contact_map cm, contact , jbilling_table bt3', ' bt3.name = ''base_user'' and bt3.id = cm.table_id and cm.foreign_id = base_user.id and contact.id = cm.contact_id and base_user.id = purchase_order.user_id and bt.name = ''order_period'' and bt2.name = ''order_billing_type'' and id.table_id = bt.id and id2.table_id = bt2.id and id.foreign_id = order_period.id and id2.foreign_id = order_billing_type.id and purchase_order.period_id = order_period.id and purchase_order.billing_type_id = order_billing_type.id and id.language_id = id2.language_id', 1, '/orderMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (2, 'report.general_invoices.title', 'report.general_invoices.instr', 'base_user, invoice, currency', 'base_user.id = invoice.user_id and invoice.currency_id = currency.id', 1, '/invoiceMaintain.do');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (3, 'report.general_payments.title', 'report.general_payments.instr', 'base_user, payment, payment_method, payment_result, international_description id, jbilling_table bt, international_description id2, jbilling_table bt2', 'base_user.id = payment.user_id and bt.name = ''payment_method'' and bt2.name = ''payment_result'' and id.table_id = bt.id and id2.table_id = bt2.id and id.foreign_id = payment_method.id and id2.foreign_id = payment_result.id and payment.method_id = payment_method.id and payment.result_id = payment_result.id and id.language_id = id2.language_id', 1, '/paymentMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (19, 'report.item.title', 'report.item.instr', 'item_type, item_type_map, entity, international_description itd, item left outer join item_price left outer join currency on item_price.currency_id = currency.id on item.id = item_price.item_id', 'deleted = 0 and item.id = item_type_map.item_id and item_type.id = item_type_map.type_id and entity.id = item.entity_id and itd.language_id = entity.language_id and itd.table_id = 14 and itd.foreign_id = item.id and itd.psudo_column = ''description''', 1, '/itemMaintain.do?action=setup&mode=item');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (5, 'report.general_refunds.title', 'report.general_refunds.instr', 'base_user, payment, payment_method, payment_result, international_description id, jbilling_table bt, international_description id2, jbilling_table bt2', 'base_user.id = payment.user_id and bt.name = ''payment_method'' and bt2.name = ''payment_result'' and id.table_id = bt.id and id2.table_id = bt2.id and id.foreign_id = payment_method.id and id2.foreign_id = payment_result.id and payment.method_id = payment_method.id and payment.result_id = payment_result.id and id.language_id = id2.language_id', 1, '/paymentMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (6, 'report.invoices_total.title', 'report.invoices_total.instr', 'base_user, invoice', 'base_user.id = invoice.user_id', 0, NULL);
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (7, 'report.payments_total.title', 'report.payments_total.instr', 'base_user, payment', 'base_user.id = payment.user_id and payment.result_id in (1,4) ', 0, NULL);
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (8, 'report.refunds_total.title', 'report.refunds_total.instr', 'base_user, payment', 'base_user.id = payment.user_id and payment.result_id in (1,4) ', 0, NULL);
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (9, 'report.orders_total.title', 'report.orders_total.instr', 'base_user, purchase_order, order_line', 'base_user.id = purchase_order.user_id and purchase_order.id = order_line.order_id and purchase_order.deleted = 0 and order_line.deleted = 0', 0, NULL);
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (10, 'report.customers_overdue.title', 'report.customers_overdue.instr', 'base_user, invoice', 'base_user.id = invoice.user_id and invoice.deleted = 0 and to_process = 1', 1, '/invoiceMaintain.do');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (11, 'report.customers_carring.title', 'report.customers_carring.instr', 'base_user, invoice, currency', 'base_user.id = invoice.user_id and invoice.currency_id = currency.id and invoice.deleted = 0 and to_process = 1', 1, '/invoiceMaintain.do');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (12, 'report.partners_orders.title', 'report.partners_orders.instr', 'base_user, purchase_order, order_period, international_description id, jbilling_table bt, customer ', 'base_user.id = purchase_order.user_id and bt.name = ''order_period'' and id.table_id = bt.id and base_user.id = customer.user_id and id.foreign_id = order_period.id and purchase_order.status_id = 1 and purchase_order.deleted = 0 and purchase_order.period_id = order_period.id ', 1, '/orderMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (13, 'report.partners_payments.title', 'report.partners_payments.instr', 'base_user, payment, payment_method, payment_result, international_description id, jbilling_table bt, customer , international_description id2, jbilling_table bt2', 'base_user.id = payment.user_id and payment.deleted = 0 and payment.is_refund = 0 and bt.name = ''payment_method'' and bt2.name = ''payment_result'' and base_user.id = customer.user_id and id.table_id = bt.id and id2.table_id = bt2.id and id.foreign_id = payment_method.id and id2.foreign_id = payment_result.id and payment.method_id = payment_method.id and payment.result_id = payment_result.id and id.language_id = id2.language_id', 1, '/paymentMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (14, 'report.partners_refunds.title', 'report.partners_refunds.instr', 'base_user, payment, payment_method, payment_result, international_description id, jbilling_table bt, customer , international_description id2, jbilling_table bt2', 'base_user.id = payment.user_id and payment.deleted = 0 and payment.is_refund = 1 and bt.name = ''payment_method'' and bt2.name = ''payment_result'' and base_user.id = customer.user_id and id.table_id = bt.id and id2.table_id = bt2.id and id.foreign_id = payment_method.id and id2.foreign_id = payment_result.id and payment.method_id = payment_method.id and payment.result_id = payment_result.id and id.language_id = id2.language_id', 1, '/paymentMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (16, 'report.payouts.title', 'report.payouts.instr', 'partner, partner_payout, base_user, payment, payment_result,international_description id, jbilling_table bt', 'partner_payout.partner_id = partner.id and base_user.id = partner.user_id and partner.id = partner_payout.partner_id and payment.id = partner_payout.payment_id and bt.name = ''payment_result'' and bt.id = id.table_id and payment_result.id = payment.result_id and payment_result.id = id.foreign_id and base_user.deleted = 0', 1, '/payout.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (17, 'report.invoice_line.title', 'report.invoice_line.instr', 'base_user, currency, invoice, invoice_line_type, invoice_line left outer join item on invoice_line.item_id = item.id', 'base_user.id = invoice.user_id and invoice.currency_id = currency.id and invoice.id = invoice_line.invoice_id and invoice_line.type_id = invoice_line_type.id and invoice_line.deleted = 0 and invoice.is_review = 0', 1, '/invoiceMaintain.do');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (18, 'report.user.title', 'report.user.instr', 'base_user, contact_map, contact, jbilling_table bt1, user_status, user_role_map, jbilling_table bt2, jbilling_table bt3, international_description it1, international_description it2, language, international_description it3, country, jbilling_table bt4 ', 'base_user.id = contact_map.foreign_id and bt1.name = ''base_user'' and bt1.id = contact_map.table_id and contact_map.contact_id = contact.id  and base_user.status_id = user_status.id and base_user.id = user_role_map.user_id and bt2.name = ''user_status'' and bt3.name = ''role'' and it1.table_id = bt2.id and it1.foreign_id = user_status.id and it1.psudo_column = ''description'' and it2.table_id = bt3.id and it2.foreign_id = user_role_map.role_id and it2.psudo_column = ''title'' and it2.language_id = it1.language_id and base_user.deleted = 0 and base_user.language_id = language.id and contact.country_code = country.code and country.id = it3.foreign_id and it3.language_id = it2.language_id and it3.table_id = bt4.id and bt4.name = ''country'' and it3.psudo_column = ''description''', 1, '/userMaintain.do?action=setup');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (4, 'report.general_order_line.title', 'report.general_order_line.instr', 'base_user, purchase_order, order_line, order_line_type,international_description id, jbilling_table bt, international_description id2, international_description id3, international_description id4', 'base_user.id = purchase_order.user_id and purchase_order.id = order_line.order_id and bt.name = ''order_line_type'' and id.table_id = bt.id and id.foreign_id = order_line_type.id and order_line.type_id = order_line_type.id and id2.table_id = 17 and id2.foreign_id = purchase_order.period_id and id2.language_id = id.language_id and id3.table_id = 19 and id3.foreign_id = purchase_order.billing_type_id and id3.language_id = id.language_id and id4.table_id = 20 and id4.foreign_id = purchase_order.status_id and id4.language_id = id.language_id', 1, '/orderMaintain.do?action=view');
INSERT INTO report (id, titlekey, instructionskey, tables, where_str, id_column, link) VALUES (15, 'report.partners.title', 'report.partners.instr', 'partner, base_user, period_unit pu, international_description id, jbilling_table bt ', 'id.table_id = bt.id and bt.name = ''period_unit'' and id.foreign_id = pu.id and partner.period_unit_id = pu.id and id.psudo_column = ''description'' and base_user.id = partner.user_id and base_user.deleted = 0', 1, '/partnerMaintain.do?action=view');


--
-- Data for TOC entry 286 (OID 4131184)
-- Name: report_user; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 287 (OID 4131186)
-- Name: report_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO report_type (id, showable) VALUES (1, 1);
INSERT INTO report_type (id, showable) VALUES (2, 1);
INSERT INTO report_type (id, showable) VALUES (3, 1);
INSERT INTO report_type (id, showable) VALUES (4, 1);
INSERT INTO report_type (id, showable) VALUES (5, 1);
INSERT INTO report_type (id, showable) VALUES (6, 1);
INSERT INTO report_type (id, showable) VALUES (7, 0);
INSERT INTO report_type (id, showable) VALUES (8, 1);
INSERT INTO report_type (id, showable) VALUES (9, 1);


--
-- Data for TOC entry 288 (OID 4131188)
-- Name: report_type_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO report_type_map (report_id, type_id) VALUES (1, 1);
INSERT INTO report_type_map (report_id, type_id) VALUES (2, 2);
INSERT INTO report_type_map (report_id, type_id) VALUES (3, 3);
INSERT INTO report_type_map (report_id, type_id) VALUES (4, 1);
INSERT INTO report_type_map (report_id, type_id) VALUES (5, 4);
INSERT INTO report_type_map (report_id, type_id) VALUES (6, 2);
INSERT INTO report_type_map (report_id, type_id) VALUES (7, 3);
INSERT INTO report_type_map (report_id, type_id) VALUES (8, 4);
INSERT INTO report_type_map (report_id, type_id) VALUES (9, 1);
INSERT INTO report_type_map (report_id, type_id) VALUES (10, 2);
INSERT INTO report_type_map (report_id, type_id) VALUES (10, 5);
INSERT INTO report_type_map (report_id, type_id) VALUES (11, 2);
INSERT INTO report_type_map (report_id, type_id) VALUES (11, 5);
INSERT INTO report_type_map (report_id, type_id) VALUES (12, 7);
INSERT INTO report_type_map (report_id, type_id) VALUES (13, 7);
INSERT INTO report_type_map (report_id, type_id) VALUES (14, 7);
INSERT INTO report_type_map (report_id, type_id) VALUES (15, 6);
INSERT INTO report_type_map (report_id, type_id) VALUES (16, 6);
INSERT INTO report_type_map (report_id, type_id) VALUES (17, 2);
INSERT INTO report_type_map (report_id, type_id) VALUES (18, 8);
INSERT INTO report_type_map (report_id, type_id) VALUES (19, 9);


--
-- Data for TOC entry 289 (OID 4131190)
-- Name: report_entity_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 290 (OID 4131192)
-- Name: report_field; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1, 1, NULL, 1, 'purchase_order', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (2, 1, NULL, 1, 'base_user', 'user_name', NULL, NULL, 'report.prompt.base_user.user_name', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (3, 1, NULL, 1, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (4, 1, NULL, 3, 'purchase_order', 'period_id', NULL, NULL, 'order.prompt.periodId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (5, 1, NULL, 4, 'purchase_order', 'billing_type_id', NULL, NULL, 'order.prompt.billingTypeId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (6, 1, NULL, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (7, 1, NULL, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (8, 1, NULL, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (9, 1, NULL, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (10, 1, NULL, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (11, 1, NULL, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (12, 1, NULL, 8, 'purchase_order', 'created_by', NULL, NULL, 'report.prompt.purchase_order.created_by', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (13, 1, NULL, 9, 'purchase_order', 'status_id', NULL, NULL, 'report.prompt.purchase_order.status', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (14, 1, NULL, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (15, 1, NULL, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (16, 1, NULL, 10, 'purchase_order', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (17, 1, NULL, 11, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (18, 1, NULL, 11, 'base_user', 'id', NULL, NULL, NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (19, 1, NULL, 12, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (20, 1, NULL, 3, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (21, 1, NULL, 4, 'id2', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (22, NULL, 1, 1, 'purchase_order', 'deleted', NULL, '0', NULL, NULL, 0, 1, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (23, 2, NULL, 1, 'invoice', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (289, 17, NULL, 1, 'invoice', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (25, 2, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (26, 2, NULL, 99, 'base_user', 'id', NULL, NULL, NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (27, 2, NULL, 4, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (28, 2, NULL, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (29, 2, NULL, 6, 'invoice', 'billing_process_id', NULL, NULL, 'process.external.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (30, 2, NULL, 7, 'invoice', 'delegated_invoice_id', NULL, NULL, 'invoice.delegated.prompt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (31, 2, NULL, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (32, 2, NULL, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (418, 4, NULL, 10, 'purchase_order', 'status_id', NULL, NULL, 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (34, 2, NULL, 11, 'invoice', 'total', NULL, NULL, 'invoice.total.prompt', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (35, 2, NULL, 12, 'invoice', 'payment_attempts', NULL, NULL, 'invoice.attempts.prompt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (36, 2, NULL, 13, 'invoice', 'to_process', NULL, NULL, 'invoice.is_payable', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (37, 2, NULL, 14, 'invoice', 'balance', NULL, NULL, 'invoice.balance.prompt', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (38, 2, NULL, 15, 'invoice', 'carried_balance', NULL, NULL, 'invoice.carriedBalance.prompt', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (39, 2, NULL, 16, 'invoice', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (40, 2, NULL, 17, 'invoice', 'is_review', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (41, 2, NULL, 18, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (42, 3, NULL, 1, 'payment', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (43, 3, NULL, 2, 'payment', 'id', NULL, NULL, 'payment.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (44, 3, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (45, 3, NULL, 11, 'base_user', 'id', NULL, NULL, NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (46, 3, NULL, 4, 'payment', 'attempt', NULL, NULL, 'payment.attempt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (47, 3, NULL, 5, 'payment', 'result_id', NULL, NULL, 'payment.resultId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (48, 3, NULL, 6, 'payment', 'amount', NULL, NULL, 'payment.amount', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (49, 3, NULL, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (50, 3, NULL, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (51, 3, NULL, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (52, 3, NULL, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (53, 3, NULL, 9, 'payment', 'method_id', NULL, NULL, 'payment.methodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (54, 3, NULL, 12, 'payment', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (55, 3, NULL, 12, 'payment', 'is_refund', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (195, 15, NULL, 18, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (196, 15, NULL, 19, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (58, 3, NULL, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (59, 3, NULL, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (60, 4, NULL, 0, 'purchase_order', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (61, 4, NULL, 1, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (62, 4, NULL, 2, 'purchase_order', 'id', NULL, NULL, 'order.external.prompt.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (63, 4, NULL, 3, 'order_line', 'item_id', NULL, NULL, 'item.prompt.number', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (64, 4, NULL, 4, 'order_line', 'type_id', NULL, NULL, 'order.line.prompt.typeid', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (65, 4, NULL, 5, 'id', 'content', NULL, NULL, 'order.line.prompt.type', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (66, 4, NULL, 6, 'order_line', 'description', NULL, NULL, 'order.line.prompt.description', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (67, 4, NULL, 7, 'order_line', 'amount', NULL, NULL, 'order.line.prompt.amount', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (68, 4, NULL, 8, 'order_line', 'quantity', NULL, NULL, 'order.line.prompt.quantity', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (69, 4, NULL, 9, 'order_line', 'price', NULL, NULL, 'order.line.prompt.price', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (70, 4, NULL, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (71, 4, NULL, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (72, 4, NULL, 0, 'purchase_order', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (73, 4, NULL, 0, 'order_line', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (74, 4, NULL, 0, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (75, 4, NULL, 1, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (76, 5, NULL, 1, 'payment', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (77, 5, NULL, 2, 'payment', 'id', NULL, NULL, 'payment.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (78, 5, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (79, 5, NULL, 11, 'base_user', 'id', NULL, NULL, NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (80, 5, NULL, 4, 'payment', 'attempt', NULL, NULL, 'payment.attempt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (81, 5, NULL, 5, 'payment', 'result_id', NULL, NULL, 'payment.resultId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (82, 5, NULL, 6, 'payment', 'amount', NULL, NULL, 'payment.amount', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (83, 5, NULL, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (84, 5, NULL, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (85, 5, NULL, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (86, 5, NULL, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (87, 5, NULL, 9, 'payment', 'method_id', NULL, NULL, 'payment.methodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (88, 5, NULL, 10, 'payment', 'payout_id', NULL, NULL, 'payout.external.prompt.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (89, 5, NULL, 12, 'payment', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (90, 5, NULL, 12, 'payment', 'is_refund', NULL, '1', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (197, 16, NULL, 1, 'partner_payout', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (198, 16, NULL, 2, 'partner', 'id', NULL, NULL, 'partner.external.prompt.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (93, 5, NULL, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (94, 5, NULL, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (95, 6, NULL, 1, 'invoice', 'total', NULL, NULL, 'invoice.total.prompt', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (96, 6, NULL, 1, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (97, 6, NULL, 2, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (98, 6, NULL, 0, 'invoice', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (99, 6, NULL, 0, 'invoice', 'is_review', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (100, 6, NULL, 0, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (101, 7, NULL, 1, 'payment', 'amount', NULL, NULL, 'payment.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (102, 7, NULL, 2, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (103, 7, NULL, 3, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (104, 7, NULL, 0, 'payment', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (105, 7, NULL, 0, 'payment', 'is_refund', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (106, 7, NULL, 14, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (107, 8, NULL, 1, 'payment', 'amount', NULL, NULL, 'payment.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (108, 8, NULL, 2, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (109, 8, NULL, 3, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (110, 8, NULL, 0, 'payment', 'deleted', NULL, '0', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (111, 8, NULL, 0, 'payment', 'is_refund', NULL, '1', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (112, 8, NULL, 14, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (113, 9, NULL, 1, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (114, 9, NULL, 2, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (115, 9, NULL, 1, 'order_line', 'amount', NULL, NULL, 'order.line.prompt.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (116, 9, NULL, 0, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (117, 10, NULL, 1, 'invoice', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (118, 10, NULL, 2, 'invoice', 'id', NULL, NULL, 'invoice.id.prompt', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (119, 10, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (120, 10, NULL, 4, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 1, 'date', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (121, 10, NULL, 5, 'invoice', 'total', NULL, NULL, 'invoice.total.prompt', NULL, 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (122, 10, NULL, 10, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (123, 10, NULL, 11, 'invoice', 'due_date', NULL, '?', 'invoice.due_date', NULL, 0, 1, 'date', '<', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (124, 11, NULL, 1, 'invoice', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (125, 11, NULL, 2, 'invoice', 'id', NULL, NULL, 'invoice.number', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (126, 11, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (127, 11, NULL, 4, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 1, 'date', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (128, 11, NULL, 5, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 1, 'string', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (129, 11, NULL, 6, 'invoice', 'total', NULL, NULL, 'invoice.total.prompt', NULL, 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (130, 11, NULL, 99, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (131, 11, NULL, 99, 'invoice', 'carried_balance', NULL, '0', 'invoice.due_date', NULL, 0, 1, 'float', '>', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (132, 12, NULL, 1, 'purchase_order', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (133, 12, NULL, 2, 'base_user', 'id', NULL, NULL, 'customer.prompt.id', NULL, 0, 1, 'integer', NULL, 0, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (134, 12, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'report.prompt.base_user.user_name', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (135, 12, NULL, 4, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (136, 12, NULL, 5, 'purchase_order', 'period_id', NULL, NULL, 'order.prompt.periodId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (137, 12, NULL, 6, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (138, 12, NULL, 7, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (139, 12, NULL, 8, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (140, 12, NULL, 9, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (141, 12, NULL, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (142, 12, NULL, 11, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (143, 12, NULL, 12, 'customer', 'partner_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (144, 12, NULL, 13, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (145, 12, NULL, 14, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (146, 13, NULL, 1, 'payment', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (147, 13, NULL, 2, 'payment', 'id', NULL, NULL, 'payment.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (419, 10, NULL, 2, 'invoice', 'public_number', NULL, NULL, 'invoice.number.prompt', NULL, 0, 1, 'string', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (149, 13, NULL, 4, 'base_user', 'id', NULL, NULL, 'customer.prompt.id', NULL, 0, 1, 'integer', NULL, 0, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (150, 13, NULL, 5, 'payment', 'attempt', NULL, NULL, 'payment.attempt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (151, 13, NULL, 6, 'payment', 'result_id', NULL, NULL, 'payment.resultId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (152, 13, NULL, 7, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (153, 13, NULL, 8, 'payment', 'amount', NULL, NULL, 'payment.amount', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (154, 13, NULL, 9, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (155, 13, NULL, 10, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (156, 13, NULL, 11, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (157, 13, NULL, 12, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (158, 13, NULL, 13, 'payment', 'method_id', NULL, NULL, 'payment.methodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (159, 13, NULL, 14, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (160, 13, NULL, 15, 'customer', 'partner_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (161, 13, NULL, 16, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (162, 14, NULL, 1, 'payment', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (163, 14, NULL, 2, 'payment', 'id', NULL, NULL, 'payment.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (164, 14, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (165, 14, NULL, 4, 'base_user', 'id', NULL, NULL, 'customer.prompt.id', NULL, 0, 1, 'integer', '=', 0, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (166, 14, NULL, 5, 'payment', 'attempt', NULL, NULL, 'payment.attempt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (167, 14, NULL, 6, 'payment', 'result_id', NULL, NULL, 'payment.resultId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (168, 14, NULL, 7, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (169, 14, NULL, 8, 'payment', 'amount', NULL, NULL, 'payment.amount', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (170, 14, NULL, 9, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (171, 14, NULL, 10, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (172, 14, NULL, 11, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (173, 14, NULL, 12, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (174, 14, NULL, 13, 'payment', 'method_id', NULL, NULL, 'payment.methodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (175, 14, NULL, 14, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (176, 14, NULL, 15, 'customer', 'partner_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (177, 14, NULL, 16, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (178, 15, NULL, 1, 'base_user', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (179, 15, NULL, 2, 'partner', 'id', NULL, NULL, 'partner.prompt.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (180, 15, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (181, 15, NULL, 4, 'partner', 'balance', NULL, NULL, 'partner.prompt.balance', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (182, 15, NULL, 5, 'partner', 'total_payments', NULL, NULL, 'partner.prompt.totalPayments', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (183, 15, NULL, 6, 'partner', 'total_refunds', NULL, NULL, 'partner.prompt.totalRefunds', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (184, 15, NULL, 7, 'partner', 'total_payouts', NULL, NULL, 'partner.prompt.totalPayouts', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (185, 15, NULL, 8, 'partner', 'percentage_rate', NULL, NULL, 'partner.prompt.rate', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (186, 15, NULL, 9, 'partner', 'referral_fee', NULL, NULL, 'partner.prompt.fee', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (187, 15, NULL, 10, 'partner', 'one_time', NULL, NULL, 'partner.prompt.onetime', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (188, 15, NULL, 11, 'partner', 'period_unit_id', NULL, NULL, 'partner.prompt.periodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (189, 15, NULL, 12, 'id', 'content', NULL, NULL, 'partner.prompt.period', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (190, 15, NULL, 13, 'partner', 'period_value', NULL, NULL, 'partner.prompt.periodValue', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (191, 15, NULL, 14, 'partner', 'next_payout_date', NULL, NULL, 'partner.prompt.nextPayout', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (192, 15, NULL, 15, 'partner', 'next_payout_date', NULL, NULL, 'partner.prompt.nextPayout', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (193, 15, NULL, 16, 'partner', 'automatic_process', NULL, NULL, 'partner.prompt.process', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (194, 15, NULL, 17, 'partner', 'related_clerk', NULL, NULL, 'partner.prompt.clerk', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (265, NULL, 35, 1, 'base_user', 'user_name', NULL, '', 'report.prompt.base_user.user_name', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (266, NULL, 35, 1, 'purchase_order', 'id', NULL, '', 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (267, NULL, 35, 1, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (268, NULL, 35, 1, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (199, 16, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (200, 16, NULL, 4, 'partner_payout', 'starting_date', NULL, NULL, 'payout.prompt.startDate', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (201, 16, NULL, 5, 'partner_payout', 'ending_date', NULL, NULL, 'payout.prompt.endDate', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (202, 16, NULL, 6, 'partner_payout', 'payments_amount', NULL, NULL, 'payout.prompt.payments', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (203, 16, NULL, 7, 'partner_payout', 'refunds_amount', NULL, NULL, 'payout.prompt.refunds', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (204, 16, NULL, 8, 'partner_payout', 'balance_left', NULL, NULL, 'payout.prompt.balance', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (205, 16, NULL, 9, 'payment', 'amount', NULL, NULL, 'payout.prompt.paid', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (206, 16, NULL, 10, 'payment', 'create_datetime', NULL, NULL, 'payout.prompt.date', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (207, 16, NULL, 11, 'payment', 'create_datetime', NULL, NULL, 'payout.prompt.date', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (208, 16, NULL, 12, 'payment', 'method_id', NULL, NULL, 'payment.methodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (209, 16, NULL, 13, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (210, 16, NULL, 14, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (211, 16, NULL, 15, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (269, NULL, 35, 1, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (270, NULL, 35, 1, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (271, NULL, 35, 3, 'purchase_order', 'period_id', NULL, '', 'order.prompt.periodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (272, NULL, 35, 3, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (273, NULL, 35, 4, 'purchase_order', 'billing_type_id', NULL, '', 'order.prompt.billingTypeId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (274, NULL, 35, 4, 'id2', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (275, NULL, 35, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (276, NULL, 35, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (277, NULL, 35, 6, 'purchase_order', 'active_until', 1, '2004-08-01', 'report.prompt.purchase_order.active_until', NULL, 0, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (278, NULL, 35, 6, 'purchase_order', 'active_until', NULL, '2004-09-01', 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (279, NULL, 35, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (280, NULL, 35, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (281, NULL, 35, 8, 'purchase_order', 'created_by', NULL, '', 'report.prompt.purchase_order.created_by', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (282, NULL, 35, 9, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (283, NULL, 35, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (284, NULL, 35, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (212, NULL, 2, 0, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (213, NULL, 2, 0, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (214, NULL, 2, 0, 'order_line', 'deleted', NULL, '0', 'report.prompt.order_line.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (215, NULL, 2, 0, 'base_user', 'entity_id', NULL, '277', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (216, NULL, 2, 1, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (217, NULL, 2, 1, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (218, NULL, 2, 2, 'purchase_order', 'id', NULL, '', 'order.external.prompt.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (219, NULL, 2, 3, 'order_line', 'item_id', NULL, '70', 'item.prompt.number', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (220, NULL, 2, 4, 'order_line', 'type_id', NULL, '', 'order.line.prompt.typeid', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (221, NULL, 2, 5, 'id', 'content', NULL, NULL, 'order.line.prompt.type', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (222, NULL, 2, 6, 'order_line', 'description', NULL, '', 'order.line.prompt.description', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (223, NULL, 2, 7, 'order_line', 'amount', NULL, '', 'order.line.prompt.amount', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (224, NULL, 2, 8, 'order_line', 'quantity', NULL, '', 'order.line.prompt.quantity', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (225, NULL, 2, 9, 'order_line', 'price', NULL, '', 'order.line.prompt.price', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (226, NULL, 2, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (227, NULL, 2, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (56, 3, NULL, 12, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (57, 3, NULL, 14, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (92, 5, NULL, 14, 'id', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (91, 5, NULL, 12, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (285, NULL, 35, 10, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (286, NULL, 35, 11, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (287, NULL, 35, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (288, NULL, 35, 12, 'id', 'language_id', NULL, '?', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (332, 17, NULL, 10, 'invoice', 'create_datetime', NULL, NULL, 'invoice.createDateTime.prompt', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (290, 17, NULL, 2, 'invoice', 'id', NULL, NULL, 'invoice.id.prompt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (291, 17, NULL, 2, 'invoice', 'public_number', NULL, NULL, 'invoice.number.prompt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (292, 17, NULL, 2, 'invoice_line', 'type_id', NULL, NULL, 'report.prompt.invoice_line.type_id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (293, 17, NULL, 3, 'invoice_line_type', 'description', NULL, NULL, 'report.prompt.invoice_line.type', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (294, 17, NULL, 4, 'invoice_line', 'item_id', NULL, NULL, 'report.prompt.invoice_line.item_id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (295, 17, NULL, 5, 'invoice_line', 'description', NULL, NULL, 'report.prompt.invoice_line.description', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (296, 17, NULL, 6, 'invoice_line', 'amount', NULL, NULL, 'report.prompt.invoice_line.amount', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (297, 17, NULL, 7, 'invoice_line', 'quantity', NULL, NULL, 'report.prompt.invoice_line.quantity', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (298, 17, NULL, 8, 'invoice_line', 'price', NULL, NULL, 'report.prompt.invoice_line.price', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (299, 17, NULL, 9, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (260, 1, NULL, 1, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (261, 1, NULL, 1, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (262, 1, NULL, 1, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (263, 1, NULL, 1, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (300, 17, NULL, 10, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (301, 18, NULL, 1, 'base_user', 'id', NULL, NULL, NULL, NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (302, 18, NULL, 2, 'base_user', 'id', NULL, NULL, 'user.prompt.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (303, 18, NULL, 3, 'base_user', 'user_name', NULL, NULL, 'user.prompt.username', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (304, 18, NULL, 4, 'base_user', 'language_id', NULL, NULL, 'report.prompt.user.languageCode', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (305, 18, NULL, 4, 'language', 'description', NULL, NULL, 'user.prompt.language', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (306, 18, NULL, 5, 'base_user', 'status_id', NULL, NULL, 'report.prompt.user.statusCode', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (307, 18, NULL, 6, 'it1', 'content', NULL, NULL, 'user.prompt.status', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (308, 18, NULL, 7, 'base_user', 'create_datetime', NULL, NULL, 'report.prompt.user.create', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (309, 18, NULL, 8, 'base_user', 'create_datetime', NULL, NULL, 'report.prompt.user.create', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (310, 18, NULL, 9, 'base_user', 'last_status_change', NULL, NULL, 'report.prompt.user.status_change', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (311, 18, NULL, 10, 'base_user', 'last_status_change', NULL, NULL, 'report.prompt.user.status_change', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (312, 18, NULL, 11, 'user_role_map', 'role_id', NULL, NULL, 'report.prompt.user.roleCode', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (313, 18, NULL, 12, 'it2', 'content', NULL, NULL, 'report.prompt.user.role', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (314, 18, NULL, 13, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (315, 18, NULL, 14, 'contact', 'street_addres1', NULL, NULL, 'contact.prompt.address1', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (316, 18, NULL, 15, 'contact', 'street_addres2', NULL, NULL, 'contact.prompt.address2', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (317, 18, NULL, 16, 'contact', 'city', NULL, NULL, 'contact.prompt.city', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (318, 18, NULL, 17, 'contact', 'state_province', NULL, NULL, 'contact.prompt.stateProvince', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (319, 18, NULL, 18, 'contact', 'postal_code', NULL, NULL, 'contact.prompt.postalCode', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (320, 18, NULL, 19, 'contact', 'country_code', NULL, NULL, 'report.prompt.user.countryCode', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (324, 18, NULL, 23, 'contact', 'phone_country_code', NULL, NULL, 'contact.prompt.phoneCountryCode', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (322, 18, NULL, 21, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (323, 18, NULL, 22, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (325, 18, NULL, 24, 'contact', 'phone_area_code', NULL, NULL, 'contact.prompt.phoneAreaCode', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (326, 18, NULL, 25, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (327, 18, NULL, 26, 'contact', 'email', NULL, NULL, 'contact.prompt.email', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (328, 18, NULL, 27, 'base_user', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (329, 18, NULL, 28, 'it1', 'language_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (321, 18, NULL, 20, 'it3', 'content', NULL, NULL, 'contact.prompt.countryCode', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (330, 1, NULL, 11, 'purchase_order', 'notify', NULL, NULL, 'report.prompt.purchase_order.notify', NULL, 0, 0, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (398, NULL, 72, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (24, 2, NULL, 2, 'invoice', 'id', NULL, NULL, 'invoice.id.prompt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (333, 17, NULL, 11, 'invoice', 'create_datetime', NULL, NULL, 'invoice.createDateTime.prompt', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (33, 2, NULL, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (334, 17, NULL, 12, 'invoice', 'user_id', NULL, NULL, 'invoice.userId.prompt', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (399, NULL, 72, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (400, NULL, 72, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (401, NULL, 72, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (402, NULL, 72, 4, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (403, NULL, 72, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (404, NULL, 72, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (405, NULL, 72, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (406, NULL, 72, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (407, NULL, 72, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (264, NULL, 35, 1, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (408, NULL, 72, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (409, NULL, 72, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (410, NULL, 72, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (411, NULL, 72, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (412, NULL, 72, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (413, NULL, 72, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (414, NULL, 72, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (415, NULL, 72, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (498, NULL, 105, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (499, NULL, 105, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (500, NULL, 105, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (501, NULL, 105, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (502, NULL, 105, 4, 'invoice', 'create_datetime', 1, '2004-09-01', 'invoice.create_date', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (503, NULL, 105, 5, 'invoice', 'create_datetime', NULL, '2004-09-12', 'invoice.create_date', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (504, NULL, 105, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (505, NULL, 105, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (506, NULL, 105, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 1, 1, 'date', '<=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (507, NULL, 105, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (508, NULL, 105, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 1, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (509, NULL, 105, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (510, NULL, 105, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (511, NULL, 105, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (512, NULL, 105, 14, 'invoice', 'balance', NULL, '0.00', 'invoice.balance.prompt', NULL, 1, 1, 'float', '>', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (513, NULL, 105, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (514, NULL, 105, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (515, NULL, 105, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (516, NULL, 105, 18, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (517, NULL, 105, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (416, NULL, 72, 18, 'base_user', 'entity_id', NULL, '303', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (417, NULL, 72, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1537, NULL, 289, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (331, 2, NULL, 2, 'invoice', 'public_number', NULL, NULL, 'invoice.number.prompt', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (420, 18, NULL, 29, 'base_user', 'last_login', NULL, NULL, 'user.prompt.lastLogin', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (421, 18, NULL, 29, 'base_user', 'last_login', NULL, NULL, 'user.prompt.lastLogin', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (422, NULL, 86, 1, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (423, NULL, 86, 2, 'base_user', 'id', NULL, '', 'user.prompt.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (424, NULL, 86, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (425, NULL, 86, 4, 'base_user', 'language_id', NULL, '', 'report.prompt.user.languageCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (426, NULL, 86, 4, 'language', 'description', NULL, NULL, 'user.prompt.language', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1538, NULL, 289, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1539, NULL, 289, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1540, NULL, 289, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1541, NULL, 289, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1542, NULL, 289, 14, 'invoice', 'balance', 1, '', 'invoice.balance.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1543, NULL, 289, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1544, NULL, 289, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1545, NULL, 289, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1546, NULL, 289, 18, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (427, NULL, 86, 5, 'base_user', 'status_id', NULL, '', 'report.prompt.user.statusCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (428, NULL, 86, 6, 'it1', 'content', NULL, NULL, 'user.prompt.status', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (429, NULL, 86, 7, 'base_user', 'create_datetime', NULL, NULL, 'report.prompt.user.create', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (430, NULL, 86, 8, 'base_user', 'create_datetime', NULL, NULL, 'report.prompt.user.create', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (431, NULL, 86, 9, 'base_user', 'last_status_change', NULL, NULL, 'report.prompt.user.status_change', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (432, NULL, 86, 10, 'base_user', 'last_status_change', NULL, NULL, 'report.prompt.user.status_change', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (433, NULL, 86, 11, 'user_role_map', 'role_id', NULL, '5', 'report.prompt.user.roleCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (434, NULL, 86, 12, 'it2', 'content', NULL, NULL, 'report.prompt.user.role', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (435, NULL, 86, 13, 'contact', 'organization_name', NULL, '', 'contact.prompt.organizationName', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (594, NULL, 138, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (595, NULL, 138, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (596, NULL, 138, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (597, NULL, 138, 12, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (598, NULL, 138, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (599, NULL, 138, 14, 'id', 'language_id', NULL, '?', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (562, NULL, 137, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (563, NULL, 137, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (564, NULL, 137, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (565, NULL, 137, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (566, NULL, 137, 4, 'invoice', 'create_datetime', 1, NULL, 'invoice.create_date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (567, NULL, 137, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (568, NULL, 137, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (569, NULL, 137, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (528, NULL, 120, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (529, NULL, 120, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (530, NULL, 120, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (531, NULL, 120, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (532, NULL, 120, 4, 'invoice', 'create_datetime', NULL, '2004-09-01', 'invoice.create_date', NULL, 0, 0, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (533, NULL, 120, 5, 'invoice', 'create_datetime', NULL, '2004-09-30', 'invoice.create_date', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (534, NULL, 120, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (535, NULL, 120, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (536, NULL, 120, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (537, NULL, 120, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (538, NULL, 120, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (539, NULL, 120, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (540, NULL, 120, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (541, NULL, 120, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (542, NULL, 120, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (543, NULL, 120, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (544, NULL, 120, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (545, NULL, 120, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (436, NULL, 86, 14, 'contact', 'street_addres1', NULL, '', 'contact.prompt.address1', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (437, NULL, 86, 15, 'contact', 'street_addres2', NULL, '', 'contact.prompt.address2', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (438, NULL, 86, 16, 'contact', 'city', NULL, '', 'contact.prompt.city', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (439, NULL, 86, 17, 'contact', 'state_province', NULL, '', 'contact.prompt.stateProvince', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (440, NULL, 86, 18, 'contact', 'postal_code', NULL, '', 'contact.prompt.postalCode', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (441, NULL, 86, 19, 'contact', 'country_code', NULL, '', 'report.prompt.user.countryCode', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (442, NULL, 86, 20, 'it3', 'content', NULL, NULL, 'contact.prompt.countryCode', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (443, NULL, 86, 21, 'contact', 'last_name', NULL, '', 'contact.prompt.lastName', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (444, NULL, 86, 22, 'contact', 'first_name', NULL, '', 'contact.prompt.firstName', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (445, NULL, 86, 23, 'contact', 'phone_country_code', NULL, '', 'contact.prompt.phoneCountryCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (446, NULL, 86, 24, 'contact', 'phone_area_code', NULL, '', 'contact.prompt.phoneAreaCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (447, NULL, 86, 25, 'contact', 'phone_phone_number', NULL, '', 'contact.prompt.phoneNumber', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (448, NULL, 86, 26, 'contact', 'email', NULL, '', 'contact.prompt.email', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (449, NULL, 86, 27, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (450, NULL, 86, 28, 'it1', 'language_id', NULL, '1', 'report.prompt.it1.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (451, NULL, 86, 29, 'base_user', 'last_login', 1, NULL, 'user.prompt.lastLogin', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (452, NULL, 86, 29, 'base_user', 'last_login', NULL, NULL, 'user.prompt.lastLogin', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1547, NULL, 289, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1568, NULL, 309, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1569, NULL, 309, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1570, NULL, 309, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1571, NULL, 309, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1572, NULL, 309, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1573, NULL, 309, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (546, NULL, 120, 18, 'base_user', 'entity_id', NULL, '277', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (547, NULL, 120, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (651, NULL, 156, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (652, NULL, 156, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (653, NULL, 156, 4, 'invoice', 'create_datetime', 1, NULL, 'invoice.create_date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (655, NULL, 156, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (656, NULL, 156, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (657, NULL, 156, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (658, NULL, 156, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (659, NULL, 156, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 1, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (660, NULL, 156, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (661, NULL, 156, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (662, NULL, 156, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (663, NULL, 156, 14, 'invoice', 'balance', NULL, '0.00', 'invoice.balance.prompt', NULL, 1, 1, 'float', '>', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (664, NULL, 156, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (665, NULL, 156, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (666, NULL, 156, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (667, NULL, 156, 18, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (668, NULL, 156, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (631, NULL, 155, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (632, NULL, 155, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (633, NULL, 155, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (634, NULL, 155, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (635, NULL, 155, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (636, NULL, 155, 6, 'payment', 'amount', NULL, '', 'payment.amount', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (637, NULL, 155, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (638, NULL, 155, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (639, NULL, 155, 8, 'payment', 'payment_date', NULL, '2004-9-1', 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (640, NULL, 155, 9, 'payment', 'payment_date', 1, '2004-10-1', 'payment.date', NULL, 1, 1, 'date', '<=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (641, NULL, 155, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (570, NULL, 137, 8, 'invoice', 'due_date', NULL, '2004-9-1', 'invoice.dueDate.prompt', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (571, NULL, 137, 9, 'invoice', 'due_date', NULL, '2004-9-15', 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (572, NULL, 137, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 1, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (573, NULL, 137, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (574, NULL, 137, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (575, NULL, 137, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (576, NULL, 137, 14, 'invoice', 'balance', NULL, '0.0', 'invoice.balance.prompt', NULL, 1, 1, 'float', '>', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (577, NULL, 137, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (578, NULL, 137, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (579, NULL, 137, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (580, NULL, 137, 18, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (581, NULL, 137, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (642, NULL, 155, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (643, NULL, 155, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (644, NULL, 155, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (645, NULL, 155, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (646, NULL, 155, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (647, NULL, 155, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (582, NULL, 138, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (583, NULL, 138, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (584, NULL, 138, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (585, NULL, 138, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (586, NULL, 138, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (587, NULL, 138, 6, 'payment', 'amount', NULL, '', 'payment.amount', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (588, NULL, 138, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (589, NULL, 138, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (590, NULL, 138, 8, 'payment', 'payment_date', NULL, '2004-9-1', 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (591, NULL, 138, 9, 'payment', 'payment_date', 1, '2004-9-15', 'payment.date', NULL, 1, 1, 'date', '<=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (592, NULL, 138, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', 'sum', 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (593, NULL, 138, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (921, NULL, 207, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (922, NULL, 207, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (923, NULL, 207, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (924, NULL, 207, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (925, NULL, 207, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (926, NULL, 207, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (927, NULL, 207, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (928, NULL, 207, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (929, NULL, 207, 12, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (930, NULL, 207, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (931, NULL, 207, 14, 'id', 'language_id', NULL, '?', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (875, NULL, 198, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (876, NULL, 198, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (877, NULL, 198, 8, 'purchase_order', 'created_by', NULL, '', 'report.prompt.purchase_order.created_by', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (878, NULL, 198, 9, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (879, NULL, 198, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (880, NULL, 198, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (881, NULL, 198, 10, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (882, NULL, 198, 11, 'base_user', 'entity_id', NULL, '306', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (884, NULL, 198, 11, 'purchase_order', 'notify', NULL, '', 'report.prompt.purchase_order.notify', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1021, NULL, 161, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1022, NULL, 161, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1023, NULL, 161, 6, 'payment', 'amount', NULL, '', 'payment.amount', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1024, NULL, 161, 8, 'payment', 'create_datetime', NULL, '2004-11-1', 'payment.createDate', NULL, 0, 0, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1025, NULL, 161, 8, 'payment', 'create_datetime', NULL, '2004-11-23', 'payment.createDate', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1026, NULL, 161, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1027, NULL, 161, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1028, NULL, 161, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (648, NULL, 155, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1029, NULL, 161, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1030, NULL, 161, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1031, NULL, 161, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1032, NULL, 161, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1033, NULL, 161, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1034, NULL, 161, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1035, NULL, 161, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1038, 18, NULL, 29, 'contact_map', 'type_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1116, 19, NULL, 1, 'item', 'id', NULL, NULL, 'item.prompt.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1117, 19, NULL, 10, 'item', 'entity_id', NULL, '?', NULL, NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1118, 19, NULL, 20, 'item', 'id', NULL, NULL, 'item.prompt.id', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1119, 19, NULL, 30, 'item', 'internal_number', NULL, NULL, 'item.prompt.internalNumber', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (885, NULL, 198, 12, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (902, NULL, 205, 0, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (903, NULL, 205, 0, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (904, NULL, 205, 1, 'payment', 'amount', NULL, NULL, 'payment.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (905, NULL, 205, 2, 'payment', 'payment_date', NULL, '2004-10-1', 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (906, NULL, 205, 3, 'payment', 'payment_date', NULL, '2005-10-1', 'payment.date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (817, NULL, 194, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1178, NULL, 199, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1179, NULL, 199, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1180, NULL, 199, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1181, NULL, 199, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1182, NULL, 199, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1183, NULL, 199, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (649, NULL, 156, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (650, NULL, 156, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (654, NULL, 156, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1184, NULL, 199, 8, 'payment', 'create_datetime', 1, '2004-12-1', 'payment.createDate', NULL, 0, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1185, NULL, 199, 8, 'payment', 'create_datetime', NULL, '2004-12-31', 'payment.createDate', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1120, 19, NULL, 35, 'itd', 'content', NULL, NULL, 'item.prompt.description', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1186, NULL, 199, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1187, NULL, 199, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1188, NULL, 199, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1189, NULL, 199, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1190, NULL, 199, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1191, NULL, 199, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1192, NULL, 199, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1193, NULL, 199, 12, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1194, NULL, 199, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1195, NULL, 199, 14, 'id', 'language_id', NULL, '?', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1121, 19, NULL, 40, 'item', 'percentage', NULL, NULL, 'item.prompt.pricePercentage', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1122, 19, NULL, 50, 'item', 'price_manual', NULL, NULL, 'item.prompt.priceManual', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (907, NULL, 205, 14, 'base_user', 'entity_id', NULL, '306', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (908, NULL, 206, 0, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (909, NULL, 206, 0, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (910, NULL, 206, 1, 'payment', 'amount', NULL, NULL, 'payment.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (911, NULL, 206, 2, 'payment', 'payment_date', NULL, '2004-10-1', 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (912, NULL, 206, 3, 'payment', 'payment_date', NULL, '2005-10-1', 'payment.date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (913, NULL, 206, 14, 'base_user', 'entity_id', NULL, '306', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (914, NULL, 207, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (915, NULL, 207, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (916, NULL, 207, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (917, NULL, 207, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (918, NULL, 207, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (919, NULL, 207, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (920, NULL, 207, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (854, NULL, 197, 0, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (855, NULL, 197, 0, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (856, NULL, 197, 0, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (857, NULL, 197, 1, 'invoice', 'total', NULL, NULL, 'invoice.total.prompt', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (858, NULL, 197, 1, 'invoice', 'create_datetime', NULL, '2004-10-01', 'invoice.create_date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (755, NULL, 190, 1, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (756, NULL, 190, 1, 'base_user', 'user_name', NULL, '', 'report.prompt.base_user.user_name', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (757, NULL, 190, 1, 'purchase_order', 'id', NULL, '', 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (758, NULL, 190, 1, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (759, NULL, 190, 1, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (760, NULL, 190, 1, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (761, NULL, 190, 1, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (762, NULL, 190, 3, 'purchase_order', 'period_id', NULL, '', 'order.prompt.periodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (763, NULL, 190, 3, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (764, NULL, 190, 4, 'purchase_order', 'billing_type_id', NULL, '', 'order.prompt.billingTypeId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (765, NULL, 190, 4, 'id2', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (766, NULL, 190, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (767, NULL, 190, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (768, NULL, 190, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (769, NULL, 190, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (770, NULL, 190, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (771, NULL, 190, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (772, NULL, 190, 8, 'purchase_order', 'created_by', NULL, '', 'report.prompt.purchase_order.created_by', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (773, NULL, 190, 9, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (774, NULL, 190, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (775, NULL, 190, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (776, NULL, 190, 10, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (777, NULL, 190, 11, 'base_user', 'entity_id', NULL, '306', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (778, NULL, 190, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (779, NULL, 190, 11, 'purchase_order', 'notify', NULL, '', 'report.prompt.purchase_order.notify', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (998, NULL, 160, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (780, NULL, 190, 12, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (859, NULL, 197, 2, 'invoice', 'create_datetime', NULL, '2005-10-01', 'invoice.create_date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (860, NULL, 198, 1, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (861, NULL, 198, 1, 'base_user', 'user_name', NULL, '', 'report.prompt.base_user.user_name', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (862, NULL, 198, 1, 'purchase_order', 'id', NULL, '', 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (781, NULL, 191, 0, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (782, NULL, 191, 1, 'purchase_order', 'create_datetime', NULL, '2004-10-15', 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (783, NULL, 191, 1, 'order_line', 'amount', NULL, NULL, 'order.line.prompt.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (784, NULL, 191, 2, 'purchase_order', 'create_datetime', NULL, '2005-10-15', 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (863, NULL, 198, 1, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (864, NULL, 198, 1, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (865, NULL, 198, 1, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (866, NULL, 198, 1, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (785, NULL, 192, 0, 'base_user', 'entity_id', NULL, '306', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (786, NULL, 192, 1, 'purchase_order', 'create_datetime', NULL, '2004-10-15', 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (787, NULL, 192, 1, 'order_line', 'amount', NULL, NULL, 'order.line.prompt.amount', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (788, NULL, 192, 2, 'purchase_order', 'create_datetime', NULL, '2005-10-15', 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (867, NULL, 198, 3, 'purchase_order', 'period_id', NULL, '', 'order.prompt.periodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (868, NULL, 198, 3, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (869, NULL, 198, 4, 'purchase_order', 'billing_type_id', NULL, '', 'order.prompt.billingTypeId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (870, NULL, 198, 4, 'id2', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (871, NULL, 198, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (872, NULL, 198, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (873, NULL, 198, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (874, NULL, 198, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (883, NULL, 198, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (806, NULL, 194, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (807, NULL, 194, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (808, NULL, 194, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (809, NULL, 194, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (810, NULL, 194, 4, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (811, NULL, 194, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (812, NULL, 194, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (813, NULL, 194, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (789, NULL, 193, 0, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (790, NULL, 193, 0, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (791, NULL, 193, 0, 'order_line', 'deleted', NULL, '0', 'report.prompt.order_line.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (792, NULL, 193, 0, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (793, NULL, 193, 1, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (794, NULL, 193, 1, 'id', 'language_id', NULL, '?', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (795, NULL, 193, 2, 'purchase_order', 'id', NULL, '', 'order.external.prompt.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (796, NULL, 193, 3, 'order_line', 'item_id', NULL, '', 'item.prompt.number', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (797, NULL, 193, 4, 'order_line', 'type_id', NULL, '', 'order.line.prompt.typeid', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (798, NULL, 193, 5, 'id', 'content', NULL, NULL, 'order.line.prompt.type', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (799, NULL, 193, 6, 'order_line', 'description', NULL, '', 'order.line.prompt.description', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (800, NULL, 193, 7, 'order_line', 'amount', NULL, '', 'order.line.prompt.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (801, NULL, 193, 8, 'order_line', 'quantity', NULL, '', 'order.line.prompt.quantity', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (802, NULL, 193, 9, 'order_line', 'price', NULL, '', 'order.line.prompt.price', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (803, NULL, 193, 10, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (804, NULL, 193, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (805, NULL, 193, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (814, NULL, 194, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (815, NULL, 194, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (816, NULL, 194, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (943, NULL, 157, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1157, 17, NULL, 4, 'item', 'internal_number', NULL, NULL, 'item.prompt.internalNumber', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (944, NULL, 157, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (945, NULL, 157, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (946, NULL, 157, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (947, NULL, 157, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (948, NULL, 157, 7, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (949, NULL, 157, 8, 'purchase_order', 'created_by', NULL, '', 'report.prompt.purchase_order.created_by', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (950, NULL, 157, 9, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (951, NULL, 157, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (952, NULL, 157, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (953, NULL, 157, 10, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (954, NULL, 157, 11, 'base_user', 'entity_id', NULL, '277', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (955, NULL, 157, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (956, NULL, 157, 11, 'purchase_order', 'notify', NULL, '', 'report.prompt.purchase_order.notify', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (957, NULL, 157, 12, 'id', 'language_id', NULL, '4', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (958, NULL, 158, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (959, NULL, 158, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (818, NULL, 194, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (819, NULL, 194, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (820, NULL, 194, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (821, NULL, 194, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (822, NULL, 194, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (823, NULL, 194, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (824, NULL, 194, 18, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (825, NULL, 194, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (932, NULL, 157, 1, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (933, NULL, 157, 1, 'base_user', 'user_name', NULL, '', 'report.prompt.base_user.user_name', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (934, NULL, 157, 1, 'purchase_order', 'id', NULL, '', 'report.prompt.purchase_order.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (935, NULL, 157, 1, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (936, NULL, 157, 1, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (937, NULL, 157, 1, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (938, NULL, 157, 1, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (939, NULL, 157, 3, 'purchase_order', 'period_id', NULL, '', 'order.prompt.periodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (940, NULL, 157, 3, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (941, NULL, 157, 4, 'purchase_order', 'billing_type_id', NULL, '', 'order.prompt.billingTypeId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (942, NULL, 157, 4, 'id2', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (826, NULL, 195, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (827, NULL, 195, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (828, NULL, 195, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (829, NULL, 195, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (830, NULL, 195, 4, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (831, NULL, 195, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (832, NULL, 195, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (833, NULL, 195, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (834, NULL, 195, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (835, NULL, 195, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (836, NULL, 195, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (837, NULL, 195, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (838, NULL, 195, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (839, NULL, 195, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (840, NULL, 195, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (841, NULL, 195, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (842, NULL, 195, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (843, NULL, 195, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (844, NULL, 195, 18, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (845, NULL, 195, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1068, NULL, 172, 1, 'contact', 'organization_name', NULL, NULL, 'contact.prompt.organizationName', NULL, 0, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1069, NULL, 172, 1, 'contact', 'first_name', NULL, NULL, 'contact.prompt.firstName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1070, NULL, 172, 1, 'contact', 'last_name', NULL, NULL, 'contact.prompt.lastName', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1071, NULL, 172, 1, 'contact', 'phone_phone_number', NULL, NULL, 'contact.prompt.phoneNumber', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1072, NULL, 172, 3, 'purchase_order', 'period_id', NULL, '', 'order.prompt.periodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1073, NULL, 172, 3, 'id', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1074, NULL, 172, 4, 'purchase_order', 'billing_type_id', NULL, '', 'order.prompt.billingTypeId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1075, NULL, 172, 4, 'id2', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1076, NULL, 172, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1077, NULL, 172, 5, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1078, NULL, 172, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1079, NULL, 172, 6, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1080, NULL, 172, 7, 'purchase_order', 'create_datetime', 1, '2004-10-1', 'report.prompt.purchase_order.create_datetime', NULL, 0, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1081, NULL, 172, 7, 'purchase_order', 'create_datetime', NULL, '2004-11-1', 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1082, NULL, 172, 8, 'purchase_order', 'created_by', NULL, '', 'report.prompt.purchase_order.created_by', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1083, NULL, 172, 9, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1084, NULL, 172, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1085, NULL, 172, 10, 'purchase_order', 'next_billable_day', NULL, NULL, 'order.prompt.nextBillableDay', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1086, NULL, 172, 10, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1087, NULL, 172, 11, 'base_user', 'entity_id', NULL, '303', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1088, NULL, 172, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1089, NULL, 172, 11, 'purchase_order', 'notify', NULL, '', 'report.prompt.purchase_order.notify', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1090, NULL, 172, 12, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1106, 4, NULL, 20, 'purchase_order', 'period_id', NULL, NULL, 'order.prompt.periodId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1107, 4, NULL, 30, 'purchase_order', 'status_id', NULL, NULL, 'report.prompt.purchase_order.status', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1108, 4, NULL, 40, 'purchase_order', 'billing_type_id', NULL, NULL, 'order.prompt.billingTypeId', NULL, 0, 1, 'integer', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1109, 4, NULL, 50, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1110, 4, NULL, 51, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1111, 4, NULL, 60, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 1, 'date', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1112, 4, NULL, 61, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', NULL, 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1113, 4, NULL, 21, 'id2', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1114, 4, NULL, 41, 'id3', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1115, 4, NULL, 31, 'id4', 'content', NULL, NULL, 'order.prompt.status', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1065, NULL, 172, 1, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1066, NULL, 172, 1, 'base_user', 'user_name', NULL, '', 'report.prompt.base_user.user_name', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1067, NULL, 172, 1, 'purchase_order', 'id', NULL, '', 'report.prompt.purchase_order.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1018, NULL, 161, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (960, NULL, 158, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (961, NULL, 158, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (962, NULL, 158, 4, 'invoice', 'create_datetime', 1, NULL, 'invoice.create_date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (963, NULL, 158, 5, 'invoice', 'create_datetime', NULL, NULL, 'invoice.create_date', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (964, NULL, 158, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (965, NULL, 158, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (966, NULL, 158, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (967, NULL, 158, 9, 'invoice', 'due_date', NULL, '2004-11-1', 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (968, NULL, 158, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 1, 1, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (969, NULL, 158, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (970, NULL, 158, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (971, NULL, 158, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (972, NULL, 158, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (973, NULL, 158, 15, 'invoice', 'carried_balance', NULL, '0.0', 'invoice.carriedBalance.prompt', NULL, 1, 1, 'float', '>', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (974, NULL, 158, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (975, NULL, 158, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (976, NULL, 158, 18, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (977, NULL, 158, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1019, NULL, 161, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1020, NULL, 161, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1226, NULL, 220, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1227, NULL, 220, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1228, NULL, 220, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1229, NULL, 220, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1230, NULL, 220, 5, 'payment', 'result_id', NULL, '2', 'payment.resultId', NULL, 0, 0, 'integer', '!=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1231, NULL, 220, 6, 'payment', 'amount', NULL, '', 'payment.amount', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1232, NULL, 220, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1233, NULL, 220, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1234, NULL, 220, 8, 'payment', 'payment_date', NULL, '2005-2-1', 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1235, NULL, 220, 9, 'payment', 'payment_date', NULL, '2005-3-1', 'payment.date', NULL, 0, 0, 'date', '<', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1236, NULL, 220, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1237, NULL, 220, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1238, NULL, 220, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1239, NULL, 220, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1240, NULL, 220, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1241, NULL, 220, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1242, NULL, 220, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1243, NULL, 220, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1208, NULL, 219, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1209, NULL, 219, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1210, NULL, 219, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1211, NULL, 219, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1212, NULL, 219, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1213, NULL, 219, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1214, NULL, 219, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1215, NULL, 219, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1216, NULL, 219, 8, 'payment', 'payment_date', NULL, '2005-01-01', 'payment.date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1217, NULL, 219, 9, 'payment', 'payment_date', 1, '2005-02-01', 'payment.date', NULL, 0, 1, 'date', '<', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1218, NULL, 219, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1219, NULL, 219, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1220, NULL, 219, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1221, NULL, 219, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1222, NULL, 219, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1223, NULL, 219, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (999, NULL, 160, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1000, NULL, 160, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1001, NULL, 160, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1002, NULL, 160, 4, 'invoice', 'create_datetime', NULL, '2004-11-1', 'invoice.create_date', NULL, 0, 0, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1003, NULL, 160, 5, 'invoice', 'create_datetime', NULL, '2004-11-23', 'invoice.create_date', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1004, NULL, 160, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1005, NULL, 160, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1006, NULL, 160, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1007, NULL, 160, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1008, NULL, 160, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1009, NULL, 160, 11, 'invoice', 'total', 1, '0', 'invoice.total.prompt', 'sum', 0, 1, 'float', '>', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1010, NULL, 160, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1011, NULL, 160, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1012, NULL, 160, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1013, NULL, 160, 15, 'invoice', 'carried_balance', NULL, '0', 'invoice.carriedBalance.prompt', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1014, NULL, 160, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1015, NULL, 160, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1016, NULL, 160, 18, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1017, NULL, 160, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1123, 19, NULL, 60, 'item_type', 'description', NULL, NULL, 'item.prompt.types', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1124, 19, NULL, 70, 'item_price', 'price', NULL, NULL, 'item.prompt.price', NULL, 0, 1, 'float', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1125, 19, NULL, 70, 'currency', 'code', NULL, NULL, 'item.prompt.currency', NULL, 0, 1, 'string', NULL, 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1224, NULL, 219, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1225, NULL, 219, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1248, NULL, 229, 0, 'purchase_order', 'id', NULL, NULL, 'report.prompt.purchase_order.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1318, NULL, 249, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1319, NULL, 249, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1320, NULL, 249, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1321, NULL, 249, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1322, NULL, 249, 5, 'payment', 'result_id', NULL, '1', 'payment.resultId', NULL, 0, 1, 'integer', '!=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1323, NULL, 249, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1324, NULL, 249, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1325, NULL, 249, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1326, NULL, 249, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1327, NULL, 249, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1328, NULL, 249, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1329, NULL, 249, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1330, NULL, 249, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1331, NULL, 249, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1332, NULL, 249, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1333, NULL, 249, 12, 'base_user', 'entity_id', NULL, '307', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1334, NULL, 249, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1335, NULL, 249, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1286, NULL, 239, 8, 'base_user', 'create_datetime', NULL, NULL, 'report.prompt.user.create', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1287, NULL, 239, 9, 'base_user', 'last_status_change', NULL, NULL, 'report.prompt.user.status_change', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1288, NULL, 239, 10, 'base_user', 'last_status_change', NULL, NULL, 'report.prompt.user.status_change', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1289, NULL, 239, 11, 'user_role_map', 'role_id', NULL, '', 'report.prompt.user.roleCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1290, NULL, 239, 12, 'it2', 'content', NULL, NULL, 'report.prompt.user.role', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1398, NULL, 269, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1249, NULL, 229, 0, 'purchase_order', 'deleted', NULL, '0', 'report.prompt.purchase_order.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1250, NULL, 229, 0, 'order_line', 'deleted', NULL, '0', 'report.prompt.order_line.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1251, NULL, 229, 0, 'base_user', 'entity_id', NULL, '307', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1252, NULL, 229, 1, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1253, NULL, 229, 1, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1254, NULL, 229, 2, 'purchase_order', 'id', NULL, '', 'order.external.prompt.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1255, NULL, 229, 3, 'order_line', 'item_id', NULL, '', 'item.prompt.number', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1256, NULL, 229, 4, 'order_line', 'type_id', NULL, '', 'order.line.prompt.typeid', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1257, NULL, 229, 5, 'id', 'content', NULL, NULL, 'order.line.prompt.type', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1258, NULL, 229, 6, 'order_line', 'description', NULL, '', 'order.line.prompt.description', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1259, NULL, 229, 7, 'order_line', 'amount', NULL, '69', 'order.line.prompt.amount', NULL, 0, 1, 'float', '!=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1260, NULL, 229, 8, 'order_line', 'quantity', NULL, '', 'order.line.prompt.quantity', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1261, NULL, 229, 9, 'order_line', 'price', NULL, '', 'order.line.prompt.price', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1262, NULL, 229, 10, 'purchase_order', 'status_id', NULL, '1', 'report.prompt.purchase_order.status', NULL, 0, 0, 'integer', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1263, NULL, 229, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1264, NULL, 229, 10, 'purchase_order', 'create_datetime', NULL, NULL, 'report.prompt.purchase_order.create_datetime', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1265, NULL, 229, 20, 'purchase_order', 'period_id', NULL, '', 'order.prompt.periodId', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1266, NULL, 229, 21, 'id2', 'content', NULL, NULL, 'order.prompt.period', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1267, NULL, 229, 30, 'purchase_order', 'status_id', NULL, '', 'report.prompt.purchase_order.status', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1268, NULL, 229, 31, 'id4', 'content', NULL, NULL, 'order.prompt.status', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1269, NULL, 229, 40, 'purchase_order', 'billing_type_id', NULL, '', 'order.prompt.billingTypeId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1270, NULL, 229, 41, 'id3', 'content', NULL, NULL, 'order.prompt.billingType', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1271, NULL, 229, 50, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1272, NULL, 229, 51, 'purchase_order', 'active_since', NULL, NULL, 'report.prompt.purchase_order.active_since', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1273, NULL, 229, 60, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1274, NULL, 229, 61, 'purchase_order', 'active_until', NULL, NULL, 'report.prompt.purchase_order.active_until', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1278, NULL, 239, 1, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1279, NULL, 239, 2, 'base_user', 'id', NULL, '', 'user.prompt.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1280, NULL, 239, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1281, NULL, 239, 4, 'base_user', 'language_id', NULL, '', 'report.prompt.user.languageCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1282, NULL, 239, 4, 'language', 'description', NULL, NULL, 'user.prompt.language', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1283, NULL, 239, 5, 'base_user', 'status_id', NULL, '', 'report.prompt.user.statusCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1284, NULL, 239, 6, 'it1', 'content', NULL, NULL, 'user.prompt.status', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1285, NULL, 239, 7, 'base_user', 'create_datetime', NULL, NULL, 'report.prompt.user.create', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1399, NULL, 269, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1400, NULL, 269, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1401, NULL, 269, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1403, NULL, 269, 12, 'base_user', 'entity_id', NULL, '303', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1404, NULL, 269, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1291, NULL, 239, 13, 'contact', 'organization_name', 1, '', 'contact.prompt.organizationName', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1292, NULL, 239, 14, 'contact', 'street_addres1', NULL, '', 'contact.prompt.address1', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1293, NULL, 239, 15, 'contact', 'street_addres2', NULL, '', 'contact.prompt.address2', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1294, NULL, 239, 16, 'contact', 'city', NULL, '', 'contact.prompt.city', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1295, NULL, 239, 17, 'contact', 'state_province', NULL, '', 'contact.prompt.stateProvince', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1296, NULL, 239, 18, 'contact', 'postal_code', NULL, '', 'contact.prompt.postalCode', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1297, NULL, 239, 19, 'contact', 'country_code', NULL, '', 'report.prompt.user.countryCode', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1298, NULL, 239, 20, 'it3', 'content', NULL, NULL, 'contact.prompt.countryCode', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1299, NULL, 239, 21, 'contact', 'last_name', NULL, '', 'contact.prompt.lastName', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1300, NULL, 239, 22, 'contact', 'first_name', NULL, '', 'contact.prompt.firstName', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1301, NULL, 239, 23, 'contact', 'phone_country_code', NULL, '', 'contact.prompt.phoneCountryCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1302, NULL, 239, 24, 'contact', 'phone_area_code', NULL, '', 'contact.prompt.phoneAreaCode', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1303, NULL, 239, 25, 'contact', 'phone_phone_number', NULL, '', 'contact.prompt.phoneNumber', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1304, NULL, 239, 26, 'contact', 'email', NULL, '', 'contact.prompt.email', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1305, NULL, 239, 27, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1306, NULL, 239, 28, 'it1', 'language_id', NULL, '1', 'report.prompt.it1.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1307, NULL, 239, 29, 'base_user', 'last_login', NULL, NULL, 'user.prompt.lastLogin', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1308, NULL, 239, 29, 'base_user', 'last_login', NULL, NULL, 'user.prompt.lastLogin', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1309, NULL, 239, 29, 'contact_map', 'type_id', NULL, '3', 'report.prompt.contact_map.type_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1405, NULL, 269, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1338, NULL, 259, 0, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1339, NULL, 259, 0, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1340, NULL, 259, 0, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1341, NULL, 259, 1, 'invoice', 'total', NULL, NULL, 'invoice.total.prompt', 'sum', 0, 1, 'float', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1342, NULL, 259, 1, 'invoice', 'create_datetime', NULL, '2004-01-01', 'invoice.create_date', NULL, 0, 0, 'date', '>=', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1343, NULL, 259, 2, 'invoice', 'create_datetime', NULL, '2005-01-01', 'invoice.create_date', NULL, 0, 0, 'date', '<', 0, 0, 0, 0, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1406, NULL, 270, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1407, NULL, 270, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1408, NULL, 270, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1409, NULL, 270, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1410, NULL, 270, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1411, NULL, 270, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1412, NULL, 270, 8, 'payment', 'create_datetime', NULL, '2005-1-1', 'payment.createDate', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1413, NULL, 270, 8, 'payment', 'create_datetime', NULL, '2005-2-1', 'payment.createDate', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1414, NULL, 270, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1415, NULL, 270, 9, 'payment', 'payment_date', 1, NULL, 'payment.date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1416, NULL, 270, 9, 'payment', 'method_id', 2, '1', 'payment.methodId', NULL, 1, 1, 'integer', '>', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1417, NULL, 270, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1418, NULL, 270, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1344, NULL, 260, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1345, NULL, 260, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1346, NULL, 260, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1347, NULL, 260, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1348, NULL, 260, 4, 'invoice', 'create_datetime', 1, '2005-1-1', 'invoice.create_date', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1349, NULL, 260, 5, 'invoice', 'create_datetime', NULL, '2005-1-31', 'invoice.create_date', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1350, NULL, 260, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1351, NULL, 260, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1352, NULL, 260, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1353, NULL, 260, 9, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1354, NULL, 260, 10, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 1, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1355, NULL, 260, 11, 'invoice', 'total', NULL, '', 'invoice.total.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1356, NULL, 260, 12, 'invoice', 'payment_attempts', NULL, '', 'invoice.attempts.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1357, NULL, 260, 13, 'invoice', 'to_process', NULL, '', 'invoice.is_payable', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1358, NULL, 260, 14, 'invoice', 'balance', NULL, '', 'invoice.balance.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1359, NULL, 260, 15, 'invoice', 'carried_balance', NULL, '', 'invoice.carriedBalance.prompt', 'sum', 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1360, NULL, 260, 16, 'invoice', 'deleted', NULL, '0', 'report.prompt.invoice.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1361, NULL, 260, 17, 'invoice', 'is_review', NULL, '0', 'report.prompt.invoice.is_review', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1362, NULL, 260, 18, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1363, NULL, 260, 99, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1419, NULL, 270, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1420, NULL, 270, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1421, NULL, 270, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1422, NULL, 270, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1423, NULL, 270, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1491, NULL, 281, 8, 'payment', 'create_datetime', NULL, '2005-2-1', 'payment.createDate', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1492, NULL, 281, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1493, NULL, 281, 9, 'payment', 'payment_date', 1, NULL, 'payment.date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1494, NULL, 281, 9, 'payment', 'method_id', 2, '4', 'payment.methodId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1495, NULL, 281, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1496, NULL, 281, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1497, NULL, 281, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1498, NULL, 281, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1499, NULL, 281, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1500, NULL, 281, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1501, NULL, 281, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1574, NULL, 309, 8, 'payment', 'create_datetime', NULL, '2005-1-1', 'payment.createDate', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1424, NULL, 271, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1425, NULL, 271, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1426, NULL, 271, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1427, NULL, 271, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1428, NULL, 271, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1429, NULL, 271, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1430, NULL, 271, 8, 'payment', 'create_datetime', NULL, '2005-04-12', 'payment.createDate', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1431, NULL, 271, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1432, NULL, 271, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1433, NULL, 271, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1434, NULL, 271, 9, 'payment', 'method_id', NULL, '', 'payment.methodId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1435, NULL, 271, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1436, NULL, 271, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1437, NULL, 271, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1438, NULL, 271, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1439, NULL, 271, 12, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1440, NULL, 271, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1441, NULL, 271, 14, 'id', 'language_id', NULL, '?', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1448, NULL, 279, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1449, NULL, 279, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1450, NULL, 279, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1451, NULL, 279, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1452, NULL, 279, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1453, NULL, 279, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1454, NULL, 279, 8, 'payment', 'create_datetime', NULL, '2005-1-1', 'payment.createDate', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1455, NULL, 279, 8, 'payment', 'create_datetime', NULL, '2005-2-1', 'payment.createDate', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1388, NULL, 269, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1389, NULL, 269, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1390, NULL, 269, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1391, NULL, 269, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1392, NULL, 269, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1393, NULL, 269, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1394, NULL, 269, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1395, NULL, 269, 8, 'payment', 'create_datetime', NULL, NULL, 'payment.createDate', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1396, NULL, 269, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1397, NULL, 269, 9, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1402, NULL, 269, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1456, NULL, 279, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1457, NULL, 279, 9, 'payment', 'payment_date', 1, NULL, 'payment.date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1458, NULL, 279, 9, 'payment', 'method_id', 2, '2', 'payment.methodId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1459, NULL, 279, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1460, NULL, 279, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1461, NULL, 279, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1462, NULL, 279, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1463, NULL, 279, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1464, NULL, 279, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1465, NULL, 279, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1575, NULL, 309, 8, 'payment', 'create_datetime', NULL, '2005-2-1', 'payment.createDate', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1576, NULL, 309, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1577, NULL, 309, 9, 'payment', 'payment_date', 1, NULL, 'payment.date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1578, NULL, 309, 9, 'payment', 'method_id', 2, '1', 'payment.methodId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1579, NULL, 309, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1580, NULL, 309, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1581, NULL, 309, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1582, NULL, 309, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1583, NULL, 309, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1584, NULL, 309, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1585, NULL, 309, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1528, NULL, 289, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1529, NULL, 289, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1466, NULL, 280, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1467, NULL, 280, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1468, NULL, 280, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1469, NULL, 280, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1470, NULL, 280, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1471, NULL, 280, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1472, NULL, 280, 8, 'payment', 'create_datetime', NULL, '2005-1-1', 'payment.createDate', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1473, NULL, 280, 8, 'payment', 'create_datetime', NULL, '2005-2-1', 'payment.createDate', NULL, 0, 0, 'date', '<', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1474, NULL, 280, 8, 'payment', 'payment_date', NULL, NULL, 'payment.date', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1475, NULL, 280, 9, 'payment', 'payment_date', 1, NULL, 'payment.date', NULL, 1, 1, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1476, NULL, 280, 9, 'payment', 'method_id', 2, '3', 'payment.methodId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1477, NULL, 280, 11, 'base_user', 'id', NULL, NULL, 'report.prompt.base_user.id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1478, NULL, 280, 12, 'payment', 'deleted', NULL, '0', 'report.prompt.payment.deleted', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1479, NULL, 280, 12, 'payment', 'is_refund', NULL, '0', 'report.prompt.payment.is_refund', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1480, NULL, 280, 12, 'id', 'content', NULL, NULL, 'payment.method', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1481, NULL, 280, 12, 'base_user', 'entity_id', NULL, '301', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1482, NULL, 280, 13, 'id2', 'content', NULL, NULL, 'payment.result', NULL, 1, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1483, NULL, 280, 14, 'id', 'language_id', NULL, '1', 'report.prompt.id.language_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1530, NULL, 289, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 0, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1531, NULL, 289, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1532, NULL, 289, 4, 'invoice', 'create_datetime', NULL, '2005-4-1', 'invoice.create_date', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1533, NULL, 289, 5, 'invoice', 'create_datetime', NULL, '2005-4-30', 'invoice.create_date', NULL, 0, 0, 'date', '<=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1534, NULL, 289, 6, 'invoice', 'billing_process_id', NULL, '', 'process.external.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1535, NULL, 289, 7, 'invoice', 'delegated_invoice_id', NULL, '', 'invoice.delegated.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1536, NULL, 289, 8, 'invoice', 'due_date', NULL, NULL, 'invoice.dueDate.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1484, NULL, 281, 1, 'payment', 'id', NULL, NULL, 'report.prompt.payment.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1485, NULL, 281, 2, 'payment', 'id', NULL, '', 'payment.id', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1486, NULL, 281, 3, 'base_user', 'user_name', NULL, '', 'user.prompt.username', NULL, 1, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1487, NULL, 281, 4, 'payment', 'attempt', NULL, '', 'payment.attempt', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1488, NULL, 281, 5, 'payment', 'result_id', NULL, '', 'payment.resultId', NULL, 1, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1489, NULL, 281, 6, 'payment', 'amount', NULL, '', 'payment.amount', NULL, 1, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1490, NULL, 281, 8, 'payment', 'create_datetime', NULL, '2005-1-1', 'payment.createDate', NULL, 1, 1, 'date', '>=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1588, NULL, 319, 1, 'invoice', 'id', NULL, NULL, 'report.prompt.invoice.id', NULL, 0, 1, 'integer', NULL, 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1589, NULL, 319, 2, 'invoice', 'id', NULL, '', 'invoice.id.prompt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1590, NULL, 319, 2, 'invoice', 'public_number', NULL, '', 'invoice.number.prompt', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1591, NULL, 319, 2, 'invoice_line', 'type_id', NULL, '', 'report.prompt.invoice_line.type_id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1592, NULL, 319, 3, 'invoice_line_type', 'description', NULL, NULL, 'report.prompt.invoice_line.type', NULL, 0, 1, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1593, NULL, 319, 4, 'invoice_line', 'item_id', NULL, '', 'report.prompt.invoice_line.item_id', NULL, 0, 1, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1594, NULL, 319, 4, 'item', 'internal_number', NULL, '', 'item.prompt.internalNumber', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1595, NULL, 319, 5, 'invoice_line', 'description', NULL, '', 'report.prompt.invoice_line.description', NULL, 0, 1, 'string', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1596, NULL, 319, 6, 'invoice_line', 'amount', NULL, '', 'report.prompt.invoice_line.amount', NULL, 0, 0, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1597, NULL, 319, 7, 'invoice_line', 'quantity', NULL, '', 'report.prompt.invoice_line.quantity', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1598, NULL, 319, 8, 'invoice_line', 'price', NULL, '', 'report.prompt.invoice_line.price', NULL, 0, 1, 'float', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1599, NULL, 319, 9, 'currency', 'symbol', NULL, NULL, 'currency.external.prompt.name', NULL, 0, 0, 'string', NULL, 1, 1, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1600, NULL, 319, 10, 'invoice', 'create_datetime', NULL, NULL, 'invoice.createDateTime.prompt', NULL, 0, 0, 'date', '=', 1, 1, 1, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1601, NULL, 319, 10, 'base_user', 'entity_id', NULL, '?', 'report.prompt.base_user.entity_id', NULL, 0, 0, 'integer', '=', 0, 0, 0, 0, 0);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1602, NULL, 319, 11, 'invoice', 'create_datetime', NULL, NULL, 'invoice.createDateTime.prompt', NULL, 0, 0, 'date', '=', 0, 0, 0, 1, 1);
INSERT INTO report_field (id, report_id, report_user_id, "position", table_name, column_name, order_position, where_value, title_key, "function", is_grouped, is_shown, data_type, "operator", functionable, selectable, ordenable, operatorable, whereable) VALUES (1603, NULL, 319, 12, 'invoice', 'user_id', NULL, '', 'invoice.userId.prompt', NULL, 0, 0, 'integer', '=', 1, 1, 1, 1, 1);


--
-- Data for TOC entry 291 (OID 4131194)
-- Name: permission; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO permission (id, type_id, foreign_id) VALUES (1, 1, 1);
INSERT INTO permission (id, type_id, foreign_id) VALUES (2, 1, 2);
INSERT INTO permission (id, type_id, foreign_id) VALUES (3, 1, 3);
INSERT INTO permission (id, type_id, foreign_id) VALUES (4, 1, 4);
INSERT INTO permission (id, type_id, foreign_id) VALUES (5, 1, 5);
INSERT INTO permission (id, type_id, foreign_id) VALUES (6, 1, 6);
INSERT INTO permission (id, type_id, foreign_id) VALUES (7, 2, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (8, 2, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (9, 2, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (10, 2, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (11, 2, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (12, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (13, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (14, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (15, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (16, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (17, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (18, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (19, 1, 7);
INSERT INTO permission (id, type_id, foreign_id) VALUES (20, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (21, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (22, 1, 8);
INSERT INTO permission (id, type_id, foreign_id) VALUES (23, 1, 9);
INSERT INTO permission (id, type_id, foreign_id) VALUES (24, 1, 10);
INSERT INTO permission (id, type_id, foreign_id) VALUES (25, 1, 11);
INSERT INTO permission (id, type_id, foreign_id) VALUES (26, 1, 12);
INSERT INTO permission (id, type_id, foreign_id) VALUES (27, 1, 13);
INSERT INTO permission (id, type_id, foreign_id) VALUES (28, 1, 14);
INSERT INTO permission (id, type_id, foreign_id) VALUES (29, 1, 15);
INSERT INTO permission (id, type_id, foreign_id) VALUES (30, 1, 16);
INSERT INTO permission (id, type_id, foreign_id) VALUES (31, 1, 17);
INSERT INTO permission (id, type_id, foreign_id) VALUES (32, 1, 18);
INSERT INTO permission (id, type_id, foreign_id) VALUES (33, 1, 19);
INSERT INTO permission (id, type_id, foreign_id) VALUES (34, 4, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (35, 1, 20);
INSERT INTO permission (id, type_id, foreign_id) VALUES (36, 1, 21);
INSERT INTO permission (id, type_id, foreign_id) VALUES (37, 1, 22);
INSERT INTO permission (id, type_id, foreign_id) VALUES (38, 1, 23);
INSERT INTO permission (id, type_id, foreign_id) VALUES (39, 1, 24);
INSERT INTO permission (id, type_id, foreign_id) VALUES (40, 1, 25);
INSERT INTO permission (id, type_id, foreign_id) VALUES (41, 1, 26);
INSERT INTO permission (id, type_id, foreign_id) VALUES (42, 1, 27);
INSERT INTO permission (id, type_id, foreign_id) VALUES (43, 1, 28);
INSERT INTO permission (id, type_id, foreign_id) VALUES (44, 1, 29);
INSERT INTO permission (id, type_id, foreign_id) VALUES (45, 1, 30);
INSERT INTO permission (id, type_id, foreign_id) VALUES (46, 1, 31);
INSERT INTO permission (id, type_id, foreign_id) VALUES (47, 1, 32);
INSERT INTO permission (id, type_id, foreign_id) VALUES (48, 1, 33);
INSERT INTO permission (id, type_id, foreign_id) VALUES (49, 1, 34);
INSERT INTO permission (id, type_id, foreign_id) VALUES (50, 1, 35);
INSERT INTO permission (id, type_id, foreign_id) VALUES (51, 1, 36);
INSERT INTO permission (id, type_id, foreign_id) VALUES (52, 1, 37);
INSERT INTO permission (id, type_id, foreign_id) VALUES (53, 1, 38);
INSERT INTO permission (id, type_id, foreign_id) VALUES (54, 1, 39);
INSERT INTO permission (id, type_id, foreign_id) VALUES (55, 1, 40);
INSERT INTO permission (id, type_id, foreign_id) VALUES (56, 1, 41);
INSERT INTO permission (id, type_id, foreign_id) VALUES (57, 1, 42);
INSERT INTO permission (id, type_id, foreign_id) VALUES (58, 1, 43);
INSERT INTO permission (id, type_id, foreign_id) VALUES (59, 1, 44);
INSERT INTO permission (id, type_id, foreign_id) VALUES (60, 1, 45);
INSERT INTO permission (id, type_id, foreign_id) VALUES (61, 1, 46);
INSERT INTO permission (id, type_id, foreign_id) VALUES (62, 1, 47);
INSERT INTO permission (id, type_id, foreign_id) VALUES (63, 1, 48);
INSERT INTO permission (id, type_id, foreign_id) VALUES (64, 1, 49);
INSERT INTO permission (id, type_id, foreign_id) VALUES (65, 1, 50);
INSERT INTO permission (id, type_id, foreign_id) VALUES (66, 1, 51);
INSERT INTO permission (id, type_id, foreign_id) VALUES (67, 5, 1);
INSERT INTO permission (id, type_id, foreign_id) VALUES (68, 5, 2);
INSERT INTO permission (id, type_id, foreign_id) VALUES (69, 5, 3);
INSERT INTO permission (id, type_id, foreign_id) VALUES (70, 5, 4);
INSERT INTO permission (id, type_id, foreign_id) VALUES (71, 5, 5);
INSERT INTO permission (id, type_id, foreign_id) VALUES (72, 5, 6);
INSERT INTO permission (id, type_id, foreign_id) VALUES (73, 5, 7);
INSERT INTO permission (id, type_id, foreign_id) VALUES (74, 5, 8);
INSERT INTO permission (id, type_id, foreign_id) VALUES (75, 5, 9);
INSERT INTO permission (id, type_id, foreign_id) VALUES (76, 5, 10);
INSERT INTO permission (id, type_id, foreign_id) VALUES (77, 1, 52);
INSERT INTO permission (id, type_id, foreign_id) VALUES (78, 1, 53);
INSERT INTO permission (id, type_id, foreign_id) VALUES (79, 1, 54);
INSERT INTO permission (id, type_id, foreign_id) VALUES (80, 1, 55);
INSERT INTO permission (id, type_id, foreign_id) VALUES (81, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (82, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (83, 1, 56);
INSERT INTO permission (id, type_id, foreign_id) VALUES (84, 5, 11);
INSERT INTO permission (id, type_id, foreign_id) VALUES (85, 1, 57);
INSERT INTO permission (id, type_id, foreign_id) VALUES (86, 1, 6);
INSERT INTO permission (id, type_id, foreign_id) VALUES (87, 1, 59);
INSERT INTO permission (id, type_id, foreign_id) VALUES (88, 1, 60);
INSERT INTO permission (id, type_id, foreign_id) VALUES (89, 1, 61);
INSERT INTO permission (id, type_id, foreign_id) VALUES (90, 1, 62);
INSERT INTO permission (id, type_id, foreign_id) VALUES (91, 1, 63);
INSERT INTO permission (id, type_id, foreign_id) VALUES (92, 1, 64);
INSERT INTO permission (id, type_id, foreign_id) VALUES (93, 1, 65);
INSERT INTO permission (id, type_id, foreign_id) VALUES (94, 1, 66);
INSERT INTO permission (id, type_id, foreign_id) VALUES (95, 1, 67);
INSERT INTO permission (id, type_id, foreign_id) VALUES (96, 1, 68);
INSERT INTO permission (id, type_id, foreign_id) VALUES (97, 1, 69);
INSERT INTO permission (id, type_id, foreign_id) VALUES (98, 1, 70);
INSERT INTO permission (id, type_id, foreign_id) VALUES (99, 5, 12);
INSERT INTO permission (id, type_id, foreign_id) VALUES (100, 5, 13);
INSERT INTO permission (id, type_id, foreign_id) VALUES (101, 5, 14);
INSERT INTO permission (id, type_id, foreign_id) VALUES (102, 5, 15);
INSERT INTO permission (id, type_id, foreign_id) VALUES (103, 5, 16);
INSERT INTO permission (id, type_id, foreign_id) VALUES (104, 1, 71);
INSERT INTO permission (id, type_id, foreign_id) VALUES (105, 1, 72);
INSERT INTO permission (id, type_id, foreign_id) VALUES (106, 1, 73);
INSERT INTO permission (id, type_id, foreign_id) VALUES (107, 1, 74);
INSERT INTO permission (id, type_id, foreign_id) VALUES (108, 6, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (109, 1, 75);
INSERT INTO permission (id, type_id, foreign_id) VALUES (110, 1, 76);
INSERT INTO permission (id, type_id, foreign_id) VALUES (111, 5, 17);
INSERT INTO permission (id, type_id, foreign_id) VALUES (112, 5, 18);
INSERT INTO permission (id, type_id, foreign_id) VALUES (113, 7, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (114, 3, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (115, 2, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (116, 1, 77);
INSERT INTO permission (id, type_id, foreign_id) VALUES (117, 1, 78);
INSERT INTO permission (id, type_id, foreign_id) VALUES (118, 1, 79);
INSERT INTO permission (id, type_id, foreign_id) VALUES (119, 1, 80);
INSERT INTO permission (id, type_id, foreign_id) VALUES (120, 8, NULL);
INSERT INTO permission (id, type_id, foreign_id) VALUES (122, 5, 19);
INSERT INTO permission (id, type_id, foreign_id) VALUES (123, 1, 82);
INSERT INTO permission (id, type_id, foreign_id) VALUES (124, 1, 81);
INSERT INTO permission (id, type_id, foreign_id) VALUES (128, 1, 86);
INSERT INTO permission (id, type_id, foreign_id) VALUES (125, 1, 83);
INSERT INTO permission (id, type_id, foreign_id) VALUES (129, 1, 87);
INSERT INTO permission (id, type_id, foreign_id) VALUES (126, 1, 85);
INSERT INTO permission (id, type_id, foreign_id) VALUES (127, 1, 84);
INSERT INTO permission (id, type_id, foreign_id) VALUES (130, 1, 88);
INSERT INTO permission (id, type_id, foreign_id) VALUES (131, 1, 89);
INSERT INTO permission (id, type_id, foreign_id) VALUES (132, 1, 90);
INSERT INTO permission (id, type_id, foreign_id) VALUES (133, 1, 91);
INSERT INTO permission (id, type_id, foreign_id) VALUES (134, 3, NULL);


--
-- Data for TOC entry 292 (OID 4131196)
-- Name: permission_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO permission_type (id, description) VALUES (1, 'Menu option');
INSERT INTO permission_type (id, description) VALUES (2, 'User creation');
INSERT INTO permission_type (id, description) VALUES (3, 'User edition');
INSERT INTO permission_type (id, description) VALUES (4, 'Item edition');
INSERT INTO permission_type (id, description) VALUES (5, 'Reports');
INSERT INTO permission_type (id, description) VALUES (6, 'Orders');
INSERT INTO permission_type (id, description) VALUES (7, 'Inovices');
INSERT INTO permission_type (id, description) VALUES (8, 'Web Services');


--
-- Data for TOC entry 293 (OID 4131198)
-- Name: role; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO role (id) VALUES (1);
INSERT INTO role (id) VALUES (2);
INSERT INTO role (id) VALUES (3);
INSERT INTO role (id) VALUES (4);
INSERT INTO role (id) VALUES (5);


--
-- Data for TOC entry 294 (OID 4131200)
-- Name: permission_role_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO permission_role_map (permission_id, role_id) VALUES (1, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (2, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (3, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (4, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (80, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (83, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (85, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (5, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (87, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (89, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (105, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (88, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (94, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (95, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (6, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (86, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (19, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (22, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (23, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (29, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (30, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (31, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (32, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (33, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (35, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (36, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (37, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (38, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (39, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (42, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (44, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (46, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (47, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (48, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (49, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (52, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (53, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (54, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (55, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (56, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (57, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (58, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (59, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (60, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (62, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (63, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (64, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (65, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (78, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (67, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (68, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (69, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (70, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (71, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (73, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (74, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (75, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (76, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (84, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (99, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (100, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (101, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (102, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (103, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (7, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (8, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (9, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (10, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (12, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (13, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (15, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (16, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (17, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (20, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (81, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (34, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (1, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (2, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (3, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (4, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (80, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (83, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (85, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (5, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (87, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (89, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (105, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (88, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (94, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (95, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (6, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (86, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (19, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (22, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (23, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (29, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (30, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (31, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (32, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (33, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (35, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (36, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (37, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (38, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (39, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (42, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (44, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (46, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (47, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (48, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (49, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (52, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (53, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (54, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (55, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (56, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (57, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (58, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (59, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (60, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (62, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (63, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (64, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (65, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (67, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (68, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (69, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (70, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (71, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (73, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (74, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (75, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (76, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (84, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (99, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (100, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (101, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (102, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (103, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (78, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (7, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (9, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (10, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (13, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (47, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (16, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (17, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (20, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (81, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (34, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (1, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (2, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (3, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (4, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (5, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (87, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (89, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (90, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (91, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (92, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (105, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (88, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (93, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (94, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (95, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (6, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (86, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (19, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (22, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (23, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (29, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (32, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (39, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (42, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (44, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (46, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (109, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (48, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (49, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (52, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (53, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (55, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (56, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (62, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (63, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (64, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (65, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (67, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (68, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (69, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (71, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (73, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (74, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (75, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (76, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (84, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (78, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (7, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (10, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (14, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (16, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (17, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (20, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (82, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (1, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (2, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (104, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (106, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (4, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (96, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (97, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (98, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (22, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (23, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (24, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (44, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (46, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (7, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (11, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (18, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (21, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (82, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (61, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (77, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (79, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (99, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (100, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (101, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (1, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (2, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (109, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (109, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (25, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (26, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (27, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (40, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (41, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (43, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (45, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (46, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (50, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (51, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (107, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (107, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (108, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (108, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (108, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (108, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (109, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (109, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (110, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (110, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (111, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (112, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (111, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (112, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (111, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (112, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (113, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (113, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (113, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (114, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (114, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (114, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (115, 1);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (115, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (115, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (70, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (72, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (114, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (99, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (100, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (101, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (102, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (103, 3);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (116, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (42, 4);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (117, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (118, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (119, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (122, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (123, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (124, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (125, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (125, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (126, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (127, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (128, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (129, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (130, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (131, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (132, 5);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (133, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (134, 2);
INSERT INTO permission_role_map (permission_id, role_id) VALUES (134, 3);


--
-- Data for TOC entry 295 (OID 4131202)
-- Name: user_role_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 296 (OID 4131204)
-- Name: permission_user; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 297 (OID 4131206)
-- Name: menu_option; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO menu_option (id, link, "level", parent_id) VALUES (1, '/order/list.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (2, '/payment/list.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (3, '/report/list.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (4, '/userAccount.do', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (77, '/achMaintain.do?action=setup&mode=ach', 2, 11);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (59, '/user/listPartner.jsp', 2, 5);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (78, '/user/listSubAccounts.jsp?own=yes', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (8, '/userAccount.do', 2, 4);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (9, '/user/edit.jsp', 3, 8);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (10, '/user/editContact.jsp', 3, 8);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (11, '/userAccount.do', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (12, '/user/edit.jsp', 2, 11);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (13, '/user/editContact.jsp', 2, 11);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (14, '/logout.do', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (15, '/item/list.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (16, '/item/listType.jsp', 2, 15);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (17, '/item/create.jsp?create=yes', 2, 15);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (18, '/item/list.jsp', 2, 15);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (19, '/item/createType.jsp?create=yes', 3, 16);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (20, '/item/listType.jsp', 3, 16);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (21, '/item/promotionList.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (22, '/item/promotionCreate.jsp?create=yes', 2, 21);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (23, '/item/promotionList.jsp', 2, 21);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (24, '/payment/customerSelect.jsp?create=yes&cheque=yes', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (25, '/payment/customerSelect.jsp?create=yes&cc=yes', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (26, '/payment/list.jsp', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (27, '/order/newOrder.jsp', 2, 1);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (28, '/order/list.jsp', 2, 1);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (29, '/creditCardMaintain.do?action=setup&mode=creditCard', 3, 8);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (30, '/creditCardMaintain.do?action=setup&mode=creditCard', 2, 11);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (31, '/payment/listRefund.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (32, '/payment/customerSelect.jsp?create=yes&cheque=yes&refund=yes', 2, 31);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (33, '/payment/customerSelect.jsp?create=yes&cc=yes&refund=yes', 2, 31);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (34, '/payment/listRefund.jsp', 2, 31);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (35, '/invoice/list.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (36, '/invoice/list.jsp', 2, 35);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (37, '/processMaintain.do?action=view&latest=yes', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (38, '/process/list.jsp', 2, 37);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (39, '/processConfigurationMaintain.do?action=setup&mode=configuration', 2, 37);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (40, '/processMaintain.do?action=view&latest=yes', 2, 37);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (41, '/processMaintain.do?action=review', 2, 37);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (42, '/notification/listTypes.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (43, '/notification/listTypes.jsp', 2, 42);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (44, '/parameterMaintain.do?action=setup&mode=parameter&type=notification', 2, 42);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (45, '/notification/emails.jsp', 2, 42);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (46, '/user/list.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (47, '/reportList.do?type=1', 2, 1);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (48, '/reportList.do?type=2', 2, 35);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (49, '/reportList.do?type=3', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (50, '/reportList.do?type=4', 2, 31);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (51, '/reportList.do?type=5', 2, 46);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (52, '/user/list.jsp', 2, 46);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (53, '/report/list.jsp', 2, 3);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (54, '/user/create.jsp?create=yes&customer=yes&frompartner=yes', 2, 46);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (55, '/brandingMaintain.do?action=setup&mode=branding', 2, 4);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (56, '/currencyMaintain.do?action=setup', 2, 4);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (57, '/ageingMaintain.do?action=setup', 2, 4);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (58, '/user/create.jsp?create=yes', 3, 6);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (79, 'HELP|page=items|anchor=', 2, 15);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (60, '/user/list.jsp', 2, 5);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (61, '/user/create.jsp?create=yes&partner=yes', 3, 59);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (62, '/user/listPartner.jsp', 3, 59);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (63, '/partnerDefaults.do?action=setup&mode=partnerDefault', 3, 59);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (64, '/reportList.do?type=6', 3, 59);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (65, '/reportList.do?type=5', 3, 60);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (66, '/user/list.jsp', 3, 60);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (67, '/user/create.jsp?create=yes&customer=yes', 3, 60);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (68, '/partnerMaintain.do?action=view&self=yes', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (69, '/partnerMaintain.do?action=view&self=yes', 2, 68);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (70, '/user/payoutList.jsp', 2, 68);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (71, '/reportList.do?type=7', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (72, '/reportTrigger.do?mode=partner&id=15', 3, 59);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (73, '/reportList.do?type=7', 2, 71);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (74, '/notificationPreference.do?action=setup&mode=notificationPreference', 2, 42);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (75, '/payment/customerSelect.jsp?create=yes&ach=yes', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (76, '/numberingMaintain.do?action=setup&mode=invoiceNumbering', 2, 35);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (5, '/user/maintain.jsp', 1, NULL);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (6, '/user/maintain.jsp', 2, 5);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (7, '/user/create.jsp?create=yes', 3, 6);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (80, 'HELP|page=reports|anchor=', 2, 3);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (82, 'HELP|page=users|anchor=', 2, 5);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (83, 'HELP|page=orders|anchor=', 2, 1);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (81, 'HELP|page=process|anchor=', 2, 37);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (84, 'HELP|page=notifications|anchor=', 2, 42);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (85, 'HELP|page=system|anchor=', 2, 4);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (86, 'HELP|page=payments|anchor=', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (87, 'HELP|page=invoices|anchor=', 2, 35);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (88, 'HELP|page=promotions|anchor=', 2, 21);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (89, 'HELP|page=payments|anchor=refunds', 2, 31);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (90, '/payment/customerSelect.jsp?create=yes&paypal=yes', 2, 2);
INSERT INTO menu_option (id, link, "level", parent_id) VALUES (91, '/orderPeriod.do?action=setup', 2, 1);


--
-- Data for TOC entry 298 (OID 4131208)
-- Name: country; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO country (id, code) VALUES (1, 'AF');
INSERT INTO country (id, code) VALUES (2, 'AL');
INSERT INTO country (id, code) VALUES (3, 'DZ');
INSERT INTO country (id, code) VALUES (4, 'AS');
INSERT INTO country (id, code) VALUES (5, 'AD');
INSERT INTO country (id, code) VALUES (6, 'AO');
INSERT INTO country (id, code) VALUES (7, 'AI');
INSERT INTO country (id, code) VALUES (8, 'AQ');
INSERT INTO country (id, code) VALUES (9, 'AG');
INSERT INTO country (id, code) VALUES (10, 'AR');
INSERT INTO country (id, code) VALUES (11, 'AM');
INSERT INTO country (id, code) VALUES (12, 'AW');
INSERT INTO country (id, code) VALUES (13, 'AU');
INSERT INTO country (id, code) VALUES (14, 'AT');
INSERT INTO country (id, code) VALUES (15, 'AZ');
INSERT INTO country (id, code) VALUES (16, 'BS');
INSERT INTO country (id, code) VALUES (17, 'BH');
INSERT INTO country (id, code) VALUES (18, 'BD');
INSERT INTO country (id, code) VALUES (19, 'BB');
INSERT INTO country (id, code) VALUES (20, 'BY');
INSERT INTO country (id, code) VALUES (21, 'BE');
INSERT INTO country (id, code) VALUES (22, 'BZ');
INSERT INTO country (id, code) VALUES (23, 'BJ');
INSERT INTO country (id, code) VALUES (24, 'BM');
INSERT INTO country (id, code) VALUES (25, 'BT');
INSERT INTO country (id, code) VALUES (26, 'BO');
INSERT INTO country (id, code) VALUES (27, 'BA');
INSERT INTO country (id, code) VALUES (28, 'BW');
INSERT INTO country (id, code) VALUES (29, 'BV');
INSERT INTO country (id, code) VALUES (30, 'BR');
INSERT INTO country (id, code) VALUES (31, 'IO');
INSERT INTO country (id, code) VALUES (32, 'BN');
INSERT INTO country (id, code) VALUES (33, 'BG');
INSERT INTO country (id, code) VALUES (34, 'BF');
INSERT INTO country (id, code) VALUES (35, 'BI');
INSERT INTO country (id, code) VALUES (36, 'KH');
INSERT INTO country (id, code) VALUES (37, 'CM');
INSERT INTO country (id, code) VALUES (38, 'CA');
INSERT INTO country (id, code) VALUES (39, 'CV');
INSERT INTO country (id, code) VALUES (40, 'KY');
INSERT INTO country (id, code) VALUES (41, 'CF');
INSERT INTO country (id, code) VALUES (42, 'TD');
INSERT INTO country (id, code) VALUES (43, 'CL');
INSERT INTO country (id, code) VALUES (44, 'CN');
INSERT INTO country (id, code) VALUES (45, 'CX');
INSERT INTO country (id, code) VALUES (46, 'CC');
INSERT INTO country (id, code) VALUES (47, 'CO');
INSERT INTO country (id, code) VALUES (48, 'KM');
INSERT INTO country (id, code) VALUES (49, 'CG');
INSERT INTO country (id, code) VALUES (50, 'CK');
INSERT INTO country (id, code) VALUES (51, 'CR');
INSERT INTO country (id, code) VALUES (52, 'CI');
INSERT INTO country (id, code) VALUES (53, 'HR');
INSERT INTO country (id, code) VALUES (54, 'CU');
INSERT INTO country (id, code) VALUES (55, 'CY');
INSERT INTO country (id, code) VALUES (56, 'CZ');
INSERT INTO country (id, code) VALUES (57, 'CD');
INSERT INTO country (id, code) VALUES (58, 'DK');
INSERT INTO country (id, code) VALUES (59, 'DJ');
INSERT INTO country (id, code) VALUES (60, 'DM');
INSERT INTO country (id, code) VALUES (61, 'DO');
INSERT INTO country (id, code) VALUES (62, 'TP');
INSERT INTO country (id, code) VALUES (63, 'EC');
INSERT INTO country (id, code) VALUES (64, 'EG');
INSERT INTO country (id, code) VALUES (65, 'SV');
INSERT INTO country (id, code) VALUES (66, 'GQ');
INSERT INTO country (id, code) VALUES (67, 'ER');
INSERT INTO country (id, code) VALUES (68, 'EE');
INSERT INTO country (id, code) VALUES (69, 'ET');
INSERT INTO country (id, code) VALUES (70, 'FK');
INSERT INTO country (id, code) VALUES (71, 'FO');
INSERT INTO country (id, code) VALUES (72, 'FJ');
INSERT INTO country (id, code) VALUES (73, 'FI');
INSERT INTO country (id, code) VALUES (74, 'FR');
INSERT INTO country (id, code) VALUES (75, 'GF');
INSERT INTO country (id, code) VALUES (76, 'PF');
INSERT INTO country (id, code) VALUES (77, 'TF');
INSERT INTO country (id, code) VALUES (78, 'GA');
INSERT INTO country (id, code) VALUES (79, 'GM');
INSERT INTO country (id, code) VALUES (80, 'GE');
INSERT INTO country (id, code) VALUES (81, 'DE');
INSERT INTO country (id, code) VALUES (82, 'GH');
INSERT INTO country (id, code) VALUES (83, 'GI');
INSERT INTO country (id, code) VALUES (84, 'GR');
INSERT INTO country (id, code) VALUES (85, 'GL');
INSERT INTO country (id, code) VALUES (86, 'GD');
INSERT INTO country (id, code) VALUES (87, 'GP');
INSERT INTO country (id, code) VALUES (88, 'GU');
INSERT INTO country (id, code) VALUES (89, 'GT');
INSERT INTO country (id, code) VALUES (90, 'GN');
INSERT INTO country (id, code) VALUES (91, 'GW');
INSERT INTO country (id, code) VALUES (92, 'GY');
INSERT INTO country (id, code) VALUES (93, 'HT');
INSERT INTO country (id, code) VALUES (94, 'HM');
INSERT INTO country (id, code) VALUES (95, 'HN');
INSERT INTO country (id, code) VALUES (96, 'HK');
INSERT INTO country (id, code) VALUES (97, 'HU');
INSERT INTO country (id, code) VALUES (98, 'IS');
INSERT INTO country (id, code) VALUES (99, 'IN');
INSERT INTO country (id, code) VALUES (100, 'ID');
INSERT INTO country (id, code) VALUES (101, 'IR');
INSERT INTO country (id, code) VALUES (102, 'IQ');
INSERT INTO country (id, code) VALUES (103, 'IE');
INSERT INTO country (id, code) VALUES (104, 'IL');
INSERT INTO country (id, code) VALUES (105, 'IT');
INSERT INTO country (id, code) VALUES (106, 'JM');
INSERT INTO country (id, code) VALUES (107, 'JP');
INSERT INTO country (id, code) VALUES (108, 'JO');
INSERT INTO country (id, code) VALUES (109, 'KZ');
INSERT INTO country (id, code) VALUES (110, 'KE');
INSERT INTO country (id, code) VALUES (111, 'KI');
INSERT INTO country (id, code) VALUES (112, 'KR');
INSERT INTO country (id, code) VALUES (113, 'KW');
INSERT INTO country (id, code) VALUES (114, 'KG');
INSERT INTO country (id, code) VALUES (115, 'LA');
INSERT INTO country (id, code) VALUES (116, 'LV');
INSERT INTO country (id, code) VALUES (117, 'LB');
INSERT INTO country (id, code) VALUES (118, 'LS');
INSERT INTO country (id, code) VALUES (119, 'LR');
INSERT INTO country (id, code) VALUES (120, 'LY');
INSERT INTO country (id, code) VALUES (121, 'LI');
INSERT INTO country (id, code) VALUES (122, 'LT');
INSERT INTO country (id, code) VALUES (123, 'LU');
INSERT INTO country (id, code) VALUES (124, 'MO');
INSERT INTO country (id, code) VALUES (125, 'MK');
INSERT INTO country (id, code) VALUES (126, 'MG');
INSERT INTO country (id, code) VALUES (127, 'MW');
INSERT INTO country (id, code) VALUES (128, 'MY');
INSERT INTO country (id, code) VALUES (129, 'MV');
INSERT INTO country (id, code) VALUES (130, 'ML');
INSERT INTO country (id, code) VALUES (131, 'MT');
INSERT INTO country (id, code) VALUES (132, 'MH');
INSERT INTO country (id, code) VALUES (133, 'MQ');
INSERT INTO country (id, code) VALUES (134, 'MR');
INSERT INTO country (id, code) VALUES (135, 'MU');
INSERT INTO country (id, code) VALUES (136, 'YT');
INSERT INTO country (id, code) VALUES (137, 'MX');
INSERT INTO country (id, code) VALUES (138, 'FM');
INSERT INTO country (id, code) VALUES (139, 'MD');
INSERT INTO country (id, code) VALUES (140, 'MC');
INSERT INTO country (id, code) VALUES (141, 'MN');
INSERT INTO country (id, code) VALUES (142, 'MS');
INSERT INTO country (id, code) VALUES (143, 'MA');
INSERT INTO country (id, code) VALUES (144, 'MZ');
INSERT INTO country (id, code) VALUES (145, 'MM');
INSERT INTO country (id, code) VALUES (146, 'NA');
INSERT INTO country (id, code) VALUES (147, 'NR');
INSERT INTO country (id, code) VALUES (148, 'NP');
INSERT INTO country (id, code) VALUES (149, 'NL');
INSERT INTO country (id, code) VALUES (150, 'AN');
INSERT INTO country (id, code) VALUES (151, 'NC');
INSERT INTO country (id, code) VALUES (152, 'NZ');
INSERT INTO country (id, code) VALUES (153, 'NI');
INSERT INTO country (id, code) VALUES (154, 'NE');
INSERT INTO country (id, code) VALUES (155, 'NG');
INSERT INTO country (id, code) VALUES (156, 'NU');
INSERT INTO country (id, code) VALUES (157, 'NF');
INSERT INTO country (id, code) VALUES (158, 'KP');
INSERT INTO country (id, code) VALUES (159, 'MP');
INSERT INTO country (id, code) VALUES (160, 'NO');
INSERT INTO country (id, code) VALUES (161, 'OM');
INSERT INTO country (id, code) VALUES (162, 'PK');
INSERT INTO country (id, code) VALUES (163, 'PW');
INSERT INTO country (id, code) VALUES (164, 'PA');
INSERT INTO country (id, code) VALUES (165, 'PG');
INSERT INTO country (id, code) VALUES (166, 'PY');
INSERT INTO country (id, code) VALUES (167, 'PE');
INSERT INTO country (id, code) VALUES (168, 'PH');
INSERT INTO country (id, code) VALUES (169, 'PN');
INSERT INTO country (id, code) VALUES (170, 'PL');
INSERT INTO country (id, code) VALUES (171, 'PT');
INSERT INTO country (id, code) VALUES (172, 'PR');
INSERT INTO country (id, code) VALUES (173, 'QA');
INSERT INTO country (id, code) VALUES (174, 'RE');
INSERT INTO country (id, code) VALUES (175, 'RO');
INSERT INTO country (id, code) VALUES (176, 'RU');
INSERT INTO country (id, code) VALUES (177, 'RW');
INSERT INTO country (id, code) VALUES (178, 'WS');
INSERT INTO country (id, code) VALUES (179, 'SM');
INSERT INTO country (id, code) VALUES (180, 'ST');
INSERT INTO country (id, code) VALUES (181, 'SA');
INSERT INTO country (id, code) VALUES (182, 'SN');
INSERT INTO country (id, code) VALUES (183, 'YU');
INSERT INTO country (id, code) VALUES (184, 'SC');
INSERT INTO country (id, code) VALUES (185, 'SL');
INSERT INTO country (id, code) VALUES (186, 'SG');
INSERT INTO country (id, code) VALUES (187, 'SK');
INSERT INTO country (id, code) VALUES (188, 'SI');
INSERT INTO country (id, code) VALUES (189, 'SB');
INSERT INTO country (id, code) VALUES (190, 'SO');
INSERT INTO country (id, code) VALUES (191, 'ZA');
INSERT INTO country (id, code) VALUES (192, 'GS');
INSERT INTO country (id, code) VALUES (193, 'ES');
INSERT INTO country (id, code) VALUES (194, 'LK');
INSERT INTO country (id, code) VALUES (195, 'SH');
INSERT INTO country (id, code) VALUES (196, 'KN');
INSERT INTO country (id, code) VALUES (197, 'LC');
INSERT INTO country (id, code) VALUES (198, 'PM');
INSERT INTO country (id, code) VALUES (199, 'VC');
INSERT INTO country (id, code) VALUES (200, 'SD');
INSERT INTO country (id, code) VALUES (201, 'SR');
INSERT INTO country (id, code) VALUES (202, 'SJ');
INSERT INTO country (id, code) VALUES (203, 'SZ');
INSERT INTO country (id, code) VALUES (204, 'SE');
INSERT INTO country (id, code) VALUES (205, 'CH');
INSERT INTO country (id, code) VALUES (206, 'SY');
INSERT INTO country (id, code) VALUES (207, 'TW');
INSERT INTO country (id, code) VALUES (208, 'TJ');
INSERT INTO country (id, code) VALUES (209, 'TZ');
INSERT INTO country (id, code) VALUES (210, 'TH');
INSERT INTO country (id, code) VALUES (211, 'TG');
INSERT INTO country (id, code) VALUES (212, 'TK');
INSERT INTO country (id, code) VALUES (213, 'TO');
INSERT INTO country (id, code) VALUES (214, 'TT');
INSERT INTO country (id, code) VALUES (215, 'TN');
INSERT INTO country (id, code) VALUES (216, 'TR');
INSERT INTO country (id, code) VALUES (217, 'TM');
INSERT INTO country (id, code) VALUES (218, 'TC');
INSERT INTO country (id, code) VALUES (219, 'TV');
INSERT INTO country (id, code) VALUES (220, 'UG');
INSERT INTO country (id, code) VALUES (221, 'UA');
INSERT INTO country (id, code) VALUES (222, 'AE');
INSERT INTO country (id, code) VALUES (223, 'UK');
INSERT INTO country (id, code) VALUES (224, 'US');
INSERT INTO country (id, code) VALUES (225, 'UM');
INSERT INTO country (id, code) VALUES (226, 'UY');
INSERT INTO country (id, code) VALUES (227, 'UZ');
INSERT INTO country (id, code) VALUES (228, 'VU');
INSERT INTO country (id, code) VALUES (229, 'VA');
INSERT INTO country (id, code) VALUES (230, 'VE');
INSERT INTO country (id, code) VALUES (231, 'VN');
INSERT INTO country (id, code) VALUES (232, 'VG');
INSERT INTO country (id, code) VALUES (233, 'VI');
INSERT INTO country (id, code) VALUES (234, 'WF');
INSERT INTO country (id, code) VALUES (235, 'YE');
INSERT INTO country (id, code) VALUES (236, 'ZM');
INSERT INTO country (id, code) VALUES (237, 'ZW');


--
-- Data for TOC entry 299 (OID 4131210)
-- Name: currency; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO currency (id, symbol, code, country_code) VALUES (1, 'US$', 'USD', 'US');
INSERT INTO currency (id, symbol, code, country_code) VALUES (2, 'C$', 'CAD', 'CA');
INSERT INTO currency (id, symbol, code, country_code) VALUES (3, '&#8364;', 'EUR', 'EU');
INSERT INTO currency (id, symbol, code, country_code) VALUES (4, '&#165;', 'JPY', 'JP');
INSERT INTO currency (id, symbol, code, country_code) VALUES (5, '&#163;', 'GBP', 'UK');
INSERT INTO currency (id, symbol, code, country_code) VALUES (6, '&#8361;', 'KRW', 'KR');
INSERT INTO currency (id, symbol, code, country_code) VALUES (7, 'Sf', 'CHF', 'CH');
INSERT INTO currency (id, symbol, code, country_code) VALUES (8, 'SeK', 'SEK', 'SE');
INSERT INTO currency (id, symbol, code, country_code) VALUES (9, 'S$', 'SGD', 'SG');
INSERT INTO currency (id, symbol, code, country_code) VALUES (10, 'M$', 'MYR', 'MY');


--
-- Data for TOC entry 300 (OID 4131212)
-- Name: currency_entity_map; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 301 (OID 4131214)
-- Name: currency_exchange; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (1, 0, 2, 1.325, '2004-03-09 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (2, 0, 3, 0.81179999999999997, '2004-03-09 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (3, 0, 4, 111.40000000000001, '2004-03-09 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (4, 0, 5, 0.54790000000000005, '2004-03-09 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (5, 0, 6, 1171, '2004-03-09 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (6, 0, 7, 1.23, '2004-07-06 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (7, 0, 8, 7.4699999999999998, '2004-07-06 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (10, 0, 9, 1.6799999999999999, '2004-10-12 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (11, 0, 10, 3.7999999999999998, '2004-10-12 00:00:00');
INSERT INTO currency_exchange (id, entity_id, currency_id, rate, create_datetime) VALUES (24, 303, 9, 1.75, '2005-02-04 12:05:29.945');


--
-- Data for TOC entry 302 (OID 4131216)
-- Name: ageing_entity_step; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 303 (OID 4131218)
-- Name: ach; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 304 (OID 4131220)
-- Name: contact_field_type; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 305 (OID 4131222)
-- Name: contact_field; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 306 (OID 4131224)
-- Name: list; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO list (id, legacy_name, title_key, instr_key) VALUES (1, 'customerSimple', 'list.customers.title', NULL);
INSERT INTO list (id, legacy_name, title_key, instr_key) VALUES (2, 'invoiceGeneral', 'list.invoices.title', NULL);
INSERT INTO list (id, legacy_name, title_key, instr_key) VALUES (3, 'order', 'list.orders.title', NULL);
INSERT INTO list (id, legacy_name, title_key, instr_key) VALUES (4, 'payment', 'list.payments.title', NULL);


--
-- Data for TOC entry 307 (OID 4131226)
-- Name: list_entity; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 308 (OID 4131228)
-- Name: list_field; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--

INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (1, 1, 'user.prompt.id', 'c.id', 1, 1, 'integer');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (2, 1, 'contact.list.organization', 'a.organization_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (3, 1, 'customer.last_name', 'a.last_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (4, 1, 'customer.first_name', 'a.first_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (5, 1, 'user.prompt.username', 'c.user_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (6, 2, 'invoice.id.prompt', 'i.id', 0, 0, 'integer');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (7, 2, 'invoice.number', 'i.public_number', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (8, 2, 'user.prompt.username', 'bu.user_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (9, 2, 'invoice.id.prompt', 'i.id', 1, 1, 'integer');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (12, 2, '', 'c.symbol', 0, 0, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (13, 2, 'invoice.total', 'i.total', 0, 0, 'float');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (14, 2, 'invoice.balance', 'i.balance', 0, 0, 'float');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (15, 2, 'invoice.is_payable', 'i.to_process', 0, 0, 'integer');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (16, 3, 'order.prompt.id', 'po.id', 1, 1, 'integer');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (17, 3, 'user.prompt.username', 'bu.user_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (18, 3, 'contact.list.organization', 'c.organization_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (19, 3, 'order.prompt.createDate', 'po.create_datetime', 0, 1, 'date');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (20, 4, 'payment.id', 'p.id', 1, 1, 'integer');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (21, 4, 'user.prompt.username', 'u.user_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (22, 4, 'contact.list.organization', 'co.organization_name', 0, 1, 'string');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (23, 4, 'payment.date', 'p.create_datetime', 0, 1, 'date');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (10, 2, 'invoice.create_date', 'i.create_datetime', 0, 1, 'date');
INSERT INTO list_field (id, list_id, title_key, column_name, ordenable, searchable, data_type) VALUES (11, 2, 'contact.list.organization', 'co.organization_name', 0, 1, 'string');


--
-- Data for TOC entry 309 (OID 4131230)
-- Name: list_field_entity; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- Data for TOC entry 310 (OID 4131232)
-- Name: partner_range; Type: TABLE DATA; Schema: public; Owner: jbilling_user
--



--
-- TOC entry 98 (OID 4134534)
-- Name: customer_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX customer_i_2 ON customer USING btree (user_id);


--
-- TOC entry 100 (OID 4134535)
-- Name: partner_i_3; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX partner_i_3 ON partner USING btree (user_id);


--
-- TOC entry 102 (OID 4134536)
-- Name: partner_payout_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX partner_payout_i_2 ON partner_payout USING btree (partner_id);


--
-- TOC entry 108 (OID 4134537)
-- Name: item_user_price_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX item_user_price_i_2 ON item_user_price USING btree (user_id, item_id);


--
-- TOC entry 110 (OID 4134538)
-- Name: ix_promotion_code; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_promotion_code ON promotion USING btree (code);


--
-- TOC entry 112 (OID 4134539)
-- Name: promotion_user_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX promotion_user_map_i_2 ON promotion_user_map USING btree (user_id, promotion_id);


--
-- TOC entry 141 (OID 4134540)
-- Name: ix_uq_order_process_or_in; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_uq_order_process_or_in ON order_process USING btree (order_id, invoice_id);


--
-- TOC entry 140 (OID 4134541)
-- Name: ix_uq_order_process_or_bp; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_uq_order_process_or_bp ON order_process USING btree (order_id, billing_process_id);


--
-- TOC entry 152 (OID 4134542)
-- Name: payment_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX payment_i_2 ON payment USING btree (user_id, create_datetime);


--
-- TOC entry 157 (OID 4134543)
-- Name: user_credit_card_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX user_credit_card_map_i_2 ON user_credit_card_map USING btree (user_id, credit_card_id);


--
-- TOC entry 159 (OID 4134544)
-- Name: ix_uq_payment_invoice_map_pa_in; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_uq_payment_invoice_map_pa_in ON payment_invoice_map USING btree (payment_id, invoice_id);


--
-- TOC entry 161 (OID 4134545)
-- Name: ix_uq_entity_payment_method_map; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_uq_entity_payment_method_map ON entity_payment_method_map USING btree (entity_id, payment_method_id);


--
-- TOC entry 175 (OID 4134546)
-- Name: international_description_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX international_description_i_2 ON international_description USING btree (table_id, foreign_id, language_id);


--
-- TOC entry 193 (OID 4134547)
-- Name: report_entity_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX report_entity_map_i_2 ON report_entity_map USING btree (entity_id, report_id);


--
-- TOC entry 198 (OID 4134548)
-- Name: permission_role_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX permission_role_map_i_2 ON permission_role_map USING btree (permission_id, role_id);


--
-- TOC entry 199 (OID 4134549)
-- Name: user_role_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX user_role_map_i_2 ON user_role_map USING btree (user_id, role_id);


--
-- TOC entry 202 (OID 4134550)
-- Name: permission_user_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX permission_user_map_i_2 ON permission_user USING btree (permission_id, user_id);


--
-- TOC entry 206 (OID 4134551)
-- Name: currency_entity_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX currency_entity_map_i_2 ON currency_entity_map USING btree (currency_id, entity_id);


--
-- TOC entry 176 (OID 4134552)
-- Name: international_description_i_lan; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX international_description_i_lan ON international_description USING btree (language_id);


--
-- TOC entry 115 (OID 4134553)
-- Name: purchase_order_i_user; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX purchase_order_i_user ON purchase_order USING btree (user_id, deleted);


--
-- TOC entry 200 (OID 4134554)
-- Name: user_role_map_i_role; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX user_role_map_i_role ON user_role_map USING btree (role_id);


--
-- TOC entry 162 (OID 4134555)
-- Name: contact_i_del; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX contact_i_del ON contact USING btree (deleted);


--
-- TOC entry 209 (OID 4134556)
-- Name: ach_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ach_i_2 ON ach USING btree (user_id);


--
-- TOC entry 114 (OID 4134557)
-- Name: purchase_order_i_notif; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX purchase_order_i_notif ON purchase_order USING btree (active_until, notification_step);


--
-- TOC entry 172 (OID 4134558)
-- Name: jbilling_table_i_name; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE UNIQUE INDEX jbilling_table_i_name ON jbilling_table USING btree (name);


--
-- TOC entry 168 (OID 4134559)
-- Name: contact_map_i_2; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE UNIQUE INDEX contact_map_i_2 ON contact_map USING btree (contact_id);


--
-- TOC entry 139 (OID 4134560)
-- Name: ix_order_process_in; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_order_process_in ON order_process USING btree (invoice_id);


--
-- TOC entry 134 (OID 4134561)
-- Name: ix_invoice_user_id; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_user_id ON invoice USING btree (user_id, deleted);


--
-- TOC entry 129 (OID 4134562)
-- Name: ix_invoice_date; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_date ON invoice USING btree (create_datetime);


--
-- TOC entry 105 (OID 4134563)
-- Name: ix_item_ent; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_item_ent ON item USING btree (entity_id, internal_number);


--
-- TOC entry 119 (OID 4134564)
-- Name: ix_order_line_order_id; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_order_line_order_id ON order_line USING btree (order_id);


--
-- TOC entry 136 (OID 4134565)
-- Name: ix_invoice_line_invoice_id; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_line_invoice_id ON invoice_line USING btree (invoice_id);


--
-- TOC entry 216 (OID 4134566)
-- Name: ix_line_entity_uq1; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE UNIQUE INDEX ix_line_entity_uq1 ON list_entity USING btree (list_id, entity_id);


--
-- TOC entry 164 (OID 4134567)
-- Name: ix_contact_fname; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_contact_fname ON contact USING btree (first_name);


--
-- TOC entry 165 (OID 4134568)
-- Name: ix_contact_lname; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_contact_lname ON contact USING btree (last_name);


--
-- TOC entry 166 (OID 4134569)
-- Name: ix_contact_orgname; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_contact_orgname ON contact USING btree (organization_name);


--
-- TOC entry 113 (OID 4134570)
-- Name: ix_purchase_order_date; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_purchase_order_date ON purchase_order USING btree (user_id, create_datetime);


--
-- TOC entry 131 (OID 4134571)
-- Name: ix_invoice_number; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_number ON invoice USING btree (user_id, public_number);


--
-- TOC entry 130 (OID 4134572)
-- Name: ix_invoice_due_date; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_due_date ON invoice USING btree (user_id, due_date);


--
-- TOC entry 214 (OID 4134573)
-- Name: ix_contact_field_cid; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_contact_field_cid ON contact_field USING btree (contact_id);


--
-- TOC entry 212 (OID 4134574)
-- Name: ix_cf_type_entity; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_cf_type_entity ON contact_field_type USING btree (entity_id);


--
-- TOC entry 169 (OID 4134575)
-- Name: contact_map_i_3; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX contact_map_i_3 ON contact_map USING btree (table_id, foreign_id, type_id);


--
-- TOC entry 145 (OID 4134576)
-- Name: bp_run_process_ix; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX bp_run_process_ix ON billing_process_run USING btree (process_id);


--
-- TOC entry 147 (OID 4134577)
-- Name: bp_run_total_run_ix; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX bp_run_total_run_ix ON billing_process_run_total USING btree (process_run_id);


--
-- TOC entry 149 (OID 4134578)
-- Name: bp_pm_index_total; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX bp_pm_index_total ON billing_process_run_total_pm USING btree (process_run_total_id);


--
-- TOC entry 132 (OID 4134579)
-- Name: ix_invoice_process; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_process ON invoice USING btree (billing_process_id);


--
-- TOC entry 220 (OID 4134580)
-- Name: partner_range_p; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX partner_range_p ON partner_range USING btree (partner_id);


--
-- TOC entry 96 (OID 4134581)
-- Name: ix_base_user_un; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_base_user_un ON base_user USING btree (entity_id, user_name);


--
-- TOC entry 133 (OID 4134582)
-- Name: ix_invoice_ts; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE INDEX ix_invoice_ts ON invoice USING btree (create_timestamp, user_id);


--
-- TOC entry 167 (OID 4134583)
-- Name: ix_contact_user; Type: INDEX; Schema: public; Owner: jbilling_user
--

CREATE UNIQUE INDEX ix_contact_user ON contact USING btree (user_id);


--
-- TOC entry 94 (OID 4134584)
-- Name: entity_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT entity_pkey PRIMARY KEY (id);


--
-- TOC entry 95 (OID 4134586)
-- Name: base_user_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_pkey PRIMARY KEY (id);


--
-- TOC entry 97 (OID 4134588)
-- Name: user_status_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY user_status
    ADD CONSTRAINT user_status_pkey PRIMARY KEY (id);


--
-- TOC entry 99 (OID 4134590)
-- Name: customer_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- TOC entry 101 (OID 4134592)
-- Name: partner_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_pkey PRIMARY KEY (id);


--
-- TOC entry 103 (OID 4134594)
-- Name: partner_payout_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner_payout
    ADD CONSTRAINT partner_payout_pkey PRIMARY KEY (id);


--
-- TOC entry 104 (OID 4134596)
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- TOC entry 106 (OID 4134598)
-- Name: item_price_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_price
    ADD CONSTRAINT item_price_pkey PRIMARY KEY (id);


--
-- TOC entry 107 (OID 4134600)
-- Name: item_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_type
    ADD CONSTRAINT item_type_pkey PRIMARY KEY (id);


--
-- TOC entry 109 (OID 4134602)
-- Name: item_user_price_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_user_price
    ADD CONSTRAINT item_user_price_pkey PRIMARY KEY (id);


--
-- TOC entry 111 (OID 4134604)
-- Name: promotion_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY promotion
    ADD CONSTRAINT promotion_pkey PRIMARY KEY (id);


--
-- TOC entry 116 (OID 4134606)
-- Name: purchase_order_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_pkey PRIMARY KEY (id);


--
-- TOC entry 117 (OID 4134608)
-- Name: order_billing_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_billing_type
    ADD CONSTRAINT order_billing_type_pkey PRIMARY KEY (id);


--
-- TOC entry 118 (OID 4134610)
-- Name: order_status_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_status
    ADD CONSTRAINT order_status_pkey PRIMARY KEY (id);


--
-- TOC entry 120 (OID 4134612)
-- Name: order_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_pkey PRIMARY KEY (id);


--
-- TOC entry 121 (OID 4134614)
-- Name: order_period_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_period
    ADD CONSTRAINT order_period_pkey PRIMARY KEY (id);


--
-- TOC entry 122 (OID 4134616)
-- Name: period_unit_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY period_unit
    ADD CONSTRAINT period_unit_pkey PRIMARY KEY (id);


--
-- TOC entry 123 (OID 4134618)
-- Name: order_line_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_line_type
    ADD CONSTRAINT order_line_type_pkey PRIMARY KEY (id);


--
-- TOC entry 124 (OID 4134620)
-- Name: pluggable_task_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task
    ADD CONSTRAINT pluggable_task_pkey PRIMARY KEY (id);


--
-- TOC entry 125 (OID 4134622)
-- Name: pluggable_task_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task_type
    ADD CONSTRAINT pluggable_task_type_pkey PRIMARY KEY (id);


--
-- TOC entry 126 (OID 4134624)
-- Name: pluggable_task_type_category_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task_type_category
    ADD CONSTRAINT pluggable_task_type_category_pkey PRIMARY KEY (id);


--
-- TOC entry 127 (OID 4134626)
-- Name: pluggable_task_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task_parameter
    ADD CONSTRAINT pluggable_task_parameter_pkey PRIMARY KEY (id);


--
-- TOC entry 128 (OID 4134628)
-- Name: invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);


--
-- TOC entry 135 (OID 4134630)
-- Name: invoice_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_pkey PRIMARY KEY (id);


--
-- TOC entry 137 (OID 4134632)
-- Name: invoice_line_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice_line_type
    ADD CONSTRAINT invoice_line_type_pkey PRIMARY KEY (id);


--
-- TOC entry 138 (OID 4134634)
-- Name: invoice_delivery_method_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice_delivery_method
    ADD CONSTRAINT invoice_delivery_method_pkey PRIMARY KEY (id);


--
-- TOC entry 142 (OID 4134636)
-- Name: order_process_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_process
    ADD CONSTRAINT order_process_pkey PRIMARY KEY (id);


--
-- TOC entry 143 (OID 4134638)
-- Name: billing_process_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_pkey PRIMARY KEY (id);


--
-- TOC entry 144 (OID 4134640)
-- Name: billing_process_run_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run
    ADD CONSTRAINT billing_process_run_pkey PRIMARY KEY (id);


--
-- TOC entry 146 (OID 4134642)
-- Name: billing_process_run_total_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run_total
    ADD CONSTRAINT billing_process_run_total_pkey PRIMARY KEY (id);


--
-- TOC entry 148 (OID 4134644)
-- Name: billing_process_run_total_pm_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run_total_pm
    ADD CONSTRAINT billing_process_run_total_pm_pkey PRIMARY KEY (id);


--
-- TOC entry 150 (OID 4134646)
-- Name: billing_process_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_configuration
    ADD CONSTRAINT billing_process_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 151 (OID 4134648)
-- Name: paper_invoice_batch_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY paper_invoice_batch
    ADD CONSTRAINT paper_invoice_batch_pkey PRIMARY KEY (id);


--
-- TOC entry 153 (OID 4134650)
-- Name: payment_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- TOC entry 154 (OID 4134652)
-- Name: payment_info_cheque_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_info_cheque
    ADD CONSTRAINT payment_info_cheque_pkey PRIMARY KEY (id);


--
-- TOC entry 155 (OID 4134654)
-- Name: payment_authorization_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_authorization
    ADD CONSTRAINT payment_authorization_pkey PRIMARY KEY (id);


--
-- TOC entry 156 (OID 4134656)
-- Name: credit_card_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY credit_card
    ADD CONSTRAINT credit_card_pkey PRIMARY KEY (id);


--
-- TOC entry 158 (OID 4134658)
-- Name: payment_result_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_result
    ADD CONSTRAINT payment_result_pkey PRIMARY KEY (id);


--
-- TOC entry 160 (OID 4134660)
-- Name: payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (id);


--
-- TOC entry 163 (OID 4134662)
-- Name: contact_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- TOC entry 170 (OID 4134664)
-- Name: contact_map_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_pkey PRIMARY KEY (id);


--
-- TOC entry 171 (OID 4134666)
-- Name: contact_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_type
    ADD CONSTRAINT contact_type_pkey PRIMARY KEY (id);


--
-- TOC entry 173 (OID 4134668)
-- Name: jbilling_table_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY jbilling_table
    ADD CONSTRAINT jbilling_table_pkey PRIMARY KEY (id);


--
-- TOC entry 174 (OID 4134670)
-- Name: jbilling_table_column_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY jbilling_table_column
    ADD CONSTRAINT jbilling_table_column_pkey PRIMARY KEY (table_id, id);


--
-- TOC entry 177 (OID 4134672)
-- Name: international_description_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY international_description
    ADD CONSTRAINT international_description_pkey PRIMARY KEY (table_id, foreign_id, psudo_column, language_id);


--
-- TOC entry 178 (OID 4134674)
-- Name: language_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY "language"
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- TOC entry 179 (OID 4134676)
-- Name: event_log_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_pkey PRIMARY KEY (id);


--
-- TOC entry 180 (OID 4134678)
-- Name: event_log_module_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log_module
    ADD CONSTRAINT event_log_module_pkey PRIMARY KEY (id);


--
-- TOC entry 181 (OID 4134680)
-- Name: event_log_message_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log_message
    ADD CONSTRAINT event_log_message_pkey PRIMARY KEY (id);


--
-- TOC entry 182 (OID 4134682)
-- Name: preference_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY preference
    ADD CONSTRAINT preference_pkey PRIMARY KEY (id);


--
-- TOC entry 183 (OID 4134684)
-- Name: preference_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY preference_type
    ADD CONSTRAINT preference_type_pkey PRIMARY KEY (id);


--
-- TOC entry 184 (OID 4134686)
-- Name: notification_message_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_pkey PRIMARY KEY (id);


--
-- TOC entry 185 (OID 4134688)
-- Name: notification_message_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_type
    ADD CONSTRAINT notification_message_type_pkey PRIMARY KEY (id);


--
-- TOC entry 186 (OID 4134690)
-- Name: notification_message_section_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_section
    ADD CONSTRAINT notification_message_section_pkey PRIMARY KEY (id);


--
-- TOC entry 187 (OID 4134692)
-- Name: notification_message_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_line
    ADD CONSTRAINT notification_message_line_pkey PRIMARY KEY (id);


--
-- TOC entry 188 (OID 4134694)
-- Name: notification_message_archive_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_archive
    ADD CONSTRAINT notification_message_archive_pkey PRIMARY KEY (id);


--
-- TOC entry 189 (OID 4134696)
-- Name: notification_message_archive_line_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_archive_line
    ADD CONSTRAINT notification_message_archive_line_pkey PRIMARY KEY (id);


--
-- TOC entry 190 (OID 4134698)
-- Name: report_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_pkey PRIMARY KEY (id);


--
-- TOC entry 191 (OID 4134700)
-- Name: report_user_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_user
    ADD CONSTRAINT report_user_pkey PRIMARY KEY (id);


--
-- TOC entry 192 (OID 4134702)
-- Name: report_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_type
    ADD CONSTRAINT report_type_pkey PRIMARY KEY (id);


--
-- TOC entry 194 (OID 4134704)
-- Name: report_field_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_field
    ADD CONSTRAINT report_field_pkey PRIMARY KEY (id);


--
-- TOC entry 195 (OID 4134706)
-- Name: permission_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- TOC entry 196 (OID 4134708)
-- Name: permission_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission_type
    ADD CONSTRAINT permission_type_pkey PRIMARY KEY (id);


--
-- TOC entry 197 (OID 4134710)
-- Name: role_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 203 (OID 4134712)
-- Name: menu_option_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY menu_option
    ADD CONSTRAINT menu_option_pkey PRIMARY KEY (id);


--
-- TOC entry 204 (OID 4134714)
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- TOC entry 205 (OID 4134716)
-- Name: currency_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY currency
    ADD CONSTRAINT currency_pkey PRIMARY KEY (id);


--
-- TOC entry 207 (OID 4134718)
-- Name: currency_exchange_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY currency_exchange
    ADD CONSTRAINT currency_exchange_pkey PRIMARY KEY (id);


--
-- TOC entry 208 (OID 4134720)
-- Name: ageing_entity_step_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_pkey PRIMARY KEY (id);


--
-- TOC entry 210 (OID 4134722)
-- Name: ach_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY ach
    ADD CONSTRAINT ach_pkey PRIMARY KEY (id);


--
-- TOC entry 211 (OID 4134724)
-- Name: contact_field_type_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_field_type
    ADD CONSTRAINT contact_field_type_pkey PRIMARY KEY (id);


--
-- TOC entry 213 (OID 4134726)
-- Name: contact_field_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_field
    ADD CONSTRAINT contact_field_pkey PRIMARY KEY (id);


--
-- TOC entry 201 (OID 4134728)
-- Name: p_key; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission_user
    ADD CONSTRAINT p_key PRIMARY KEY (id);


--
-- TOC entry 215 (OID 4134730)
-- Name: list_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list
    ADD CONSTRAINT list_pkey PRIMARY KEY (id);


--
-- TOC entry 217 (OID 4134732)
-- Name: list_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_entity
    ADD CONSTRAINT list_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 218 (OID 4134734)
-- Name: list_field_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_field
    ADD CONSTRAINT list_field_pkey PRIMARY KEY (id);


--
-- TOC entry 219 (OID 4134736)
-- Name: list_field_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_field_entity
    ADD CONSTRAINT list_field_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 221 (OID 4134738)
-- Name: partner_range_pkey; Type: CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner_range
    ADD CONSTRAINT partner_range_pkey PRIMARY KEY (id);


--
-- TOC entry 311 (OID 4134740)
-- Name: entity_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT entity_fk_1 FOREIGN KEY (language_id) REFERENCES "language"(id);


--
-- TOC entry 312 (OID 4134744)
-- Name: entity_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT entity_fk_2 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 313 (OID 4134748)
-- Name: base_user_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_1 FOREIGN KEY (language_id) REFERENCES "language"(id);


--
-- TOC entry 314 (OID 4134752)
-- Name: base_user_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 315 (OID 4134756)
-- Name: base_user_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_3 FOREIGN KEY (status_id) REFERENCES user_status(id);


--
-- TOC entry 316 (OID 4134760)
-- Name: base_user_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY base_user
    ADD CONSTRAINT base_user_fk_4 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 317 (OID 4134764)
-- Name: customer_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 318 (OID 4134768)
-- Name: customer_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_fk_2 FOREIGN KEY (partner_id) REFERENCES partner(id);


--
-- TOC entry 319 (OID 4134772)
-- Name: customer_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_fk_3 FOREIGN KEY (invoice_delivery_method_id) REFERENCES invoice_delivery_method(id);


--
-- TOC entry 320 (OID 4134776)
-- Name: partner_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 321 (OID 4134780)
-- Name: partner_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_2 FOREIGN KEY (related_clerk) REFERENCES base_user(id);


--
-- TOC entry 322 (OID 4134784)
-- Name: partner_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_3 FOREIGN KEY (fee_currency_id) REFERENCES currency(id);


--
-- TOC entry 323 (OID 4134788)
-- Name: partner_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_fk_4 FOREIGN KEY (period_unit_id) REFERENCES period_unit(id);


--
-- TOC entry 324 (OID 4134792)
-- Name: partner_payout_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner_payout
    ADD CONSTRAINT partner_payout_fk_1 FOREIGN KEY (partner_id) REFERENCES partner(id);


--
-- TOC entry 325 (OID 4134796)
-- Name: partner_payout_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY partner_payout
    ADD CONSTRAINT partner_payout_fk_2 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- TOC entry 326 (OID 4134800)
-- Name: item_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 327 (OID 4134804)
-- Name: item_type_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_type_map
    ADD CONSTRAINT item_type_map_fk_1 FOREIGN KEY (type_id) REFERENCES item_type(id);


--
-- TOC entry 328 (OID 4134808)
-- Name: item_type_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_type_map
    ADD CONSTRAINT item_type_map_fk_2 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- TOC entry 329 (OID 4134812)
-- Name: item_price_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_price
    ADD CONSTRAINT item_price_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- TOC entry 330 (OID 4134816)
-- Name: item_price_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_price
    ADD CONSTRAINT item_price_fk_2 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 331 (OID 4134820)
-- Name: item_type_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_type
    ADD CONSTRAINT item_type_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 332 (OID 4134824)
-- Name: item_user_price_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_user_price
    ADD CONSTRAINT item_user_price_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- TOC entry 333 (OID 4134828)
-- Name: item_user_price_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_user_price
    ADD CONSTRAINT item_user_price_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 334 (OID 4134832)
-- Name: item_user_price_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY item_user_price
    ADD CONSTRAINT item_user_price_fk_3 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 335 (OID 4134836)
-- Name: promotion_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY promotion
    ADD CONSTRAINT promotion_fk_1 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- TOC entry 336 (OID 4134840)
-- Name: promotion_user_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY promotion_user_map
    ADD CONSTRAINT promotion_user_map_fk_1 FOREIGN KEY (promotion_id) REFERENCES promotion(id);


--
-- TOC entry 337 (OID 4134844)
-- Name: promotion_user_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY promotion_user_map
    ADD CONSTRAINT promotion_user_map_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 338 (OID 4134848)
-- Name: purchase_order_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_1 FOREIGN KEY (created_by) REFERENCES base_user(id);


--
-- TOC entry 339 (OID 4134852)
-- Name: purchase_order_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 340 (OID 4134856)
-- Name: purchase_order_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_3 FOREIGN KEY (period_id) REFERENCES order_period(id);


--
-- TOC entry 341 (OID 4134860)
-- Name: purchase_order_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_4 FOREIGN KEY (billing_type_id) REFERENCES order_billing_type(id);


--
-- TOC entry 342 (OID 4134864)
-- Name: purchase_order_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_5 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 343 (OID 4134868)
-- Name: purchase_order_fk_6; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY purchase_order
    ADD CONSTRAINT purchase_order_fk_6 FOREIGN KEY (status_id) REFERENCES order_status(id);


--
-- TOC entry 344 (OID 4134872)
-- Name: order_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_fk_1 FOREIGN KEY (order_id) REFERENCES purchase_order(id);


--
-- TOC entry 345 (OID 4134876)
-- Name: order_line_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_fk_2 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- TOC entry 346 (OID 4134880)
-- Name: order_line_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_line
    ADD CONSTRAINT order_line_fk_3 FOREIGN KEY (type_id) REFERENCES order_line_type(id);


--
-- TOC entry 347 (OID 4134884)
-- Name: order_period_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_period
    ADD CONSTRAINT order_period_fk_1 FOREIGN KEY (unit_id) REFERENCES period_unit(id);


--
-- TOC entry 348 (OID 4134888)
-- Name: order_period_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_period
    ADD CONSTRAINT order_period_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 349 (OID 4134892)
-- Name: pluggable_task_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task
    ADD CONSTRAINT pluggable_task_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 350 (OID 4134896)
-- Name: pluggable_task_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task
    ADD CONSTRAINT pluggable_task_fk_2 FOREIGN KEY (type_id) REFERENCES pluggable_task_type(id);


--
-- TOC entry 351 (OID 4134900)
-- Name: pluggable_task_type_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task_type
    ADD CONSTRAINT pluggable_task_type_fk_1 FOREIGN KEY (category_id) REFERENCES pluggable_task_type_category(id);


--
-- TOC entry 352 (OID 4134904)
-- Name: pluggable_task_parameter_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY pluggable_task_parameter
    ADD CONSTRAINT pluggable_task_parameter_fk_1 FOREIGN KEY (task_id) REFERENCES pluggable_task(id);


--
-- TOC entry 353 (OID 4134908)
-- Name: invoice_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_1 FOREIGN KEY (paper_invoice_batch_id) REFERENCES paper_invoice_batch(id);


--
-- TOC entry 354 (OID 4134912)
-- Name: invoice_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_2 FOREIGN KEY (billing_process_id) REFERENCES billing_process(id);


--
-- TOC entry 355 (OID 4134916)
-- Name: invoice_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_3 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 356 (OID 4134920)
-- Name: invoice_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_4 FOREIGN KEY (delegated_invoice_id) REFERENCES invoice(id);


--
-- TOC entry 357 (OID 4134924)
-- Name: invoice_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice
    ADD CONSTRAINT invoice_fk_5 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 358 (OID 4134928)
-- Name: invoice_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_fk_1 FOREIGN KEY (invoice_id) REFERENCES invoice(id);


--
-- TOC entry 359 (OID 4134932)
-- Name: invoice_line_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT invoice_line_fk_2 FOREIGN KEY (type_id) REFERENCES invoice_line_type(id);


--
-- TOC entry 361 (OID 4134936)
-- Name: entity_delivery_method_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity_delivery_method_map
    ADD CONSTRAINT entity_delivery_method_map_fk_1 FOREIGN KEY (method_id) REFERENCES invoice_delivery_method(id);


--
-- TOC entry 362 (OID 4134940)
-- Name: entity_delivery_method_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity_delivery_method_map
    ADD CONSTRAINT entity_delivery_method_map_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 363 (OID 4134944)
-- Name: order_process_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_process
    ADD CONSTRAINT order_process_fk_1 FOREIGN KEY (order_id) REFERENCES purchase_order(id);


--
-- TOC entry 364 (OID 4134948)
-- Name: order_process_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY order_process
    ADD CONSTRAINT order_process_fk_3 FOREIGN KEY (billing_process_id) REFERENCES billing_process(id);


--
-- TOC entry 365 (OID 4134952)
-- Name: billing_process_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_fk_1 FOREIGN KEY (paper_invoice_batch_id) REFERENCES paper_invoice_batch(id);


--
-- TOC entry 366 (OID 4134956)
-- Name: billing_process_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 367 (OID 4134960)
-- Name: billing_process_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process
    ADD CONSTRAINT billing_process_fk_3 FOREIGN KEY (period_unit_id) REFERENCES period_unit(id);


--
-- TOC entry 368 (OID 4134964)
-- Name: billing_process_run_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run
    ADD CONSTRAINT billing_process_run_fk_1 FOREIGN KEY (process_id) REFERENCES billing_process(id);


--
-- TOC entry 369 (OID 4134968)
-- Name: billing_process_run_total_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run_total
    ADD CONSTRAINT billing_process_run_total_fk_1 FOREIGN KEY (process_run_id) REFERENCES billing_process_run(id);


--
-- TOC entry 370 (OID 4134972)
-- Name: billing_process_run_total_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run_total
    ADD CONSTRAINT billing_process_run_total_fk_2 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 371 (OID 4134976)
-- Name: billing_process_run_total_pm_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_run_total_pm
    ADD CONSTRAINT billing_process_run_total_pm_fk_1 FOREIGN KEY (payment_method_id) REFERENCES payment_method(id);


--
-- TOC entry 372 (OID 4134980)
-- Name: billing_process_configuration_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_configuration
    ADD CONSTRAINT billing_process_configuration_fk_1 FOREIGN KEY (period_unit_id) REFERENCES period_unit(id);


--
-- TOC entry 373 (OID 4134984)
-- Name: billing_process_configuration_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY billing_process_configuration
    ADD CONSTRAINT billing_process_configuration_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 374 (OID 4134988)
-- Name: payment_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 375 (OID 4134992)
-- Name: payment_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_2 FOREIGN KEY (method_id) REFERENCES payment_method(id);


--
-- TOC entry 376 (OID 4134996)
-- Name: payment_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_3 FOREIGN KEY (result_id) REFERENCES payment_result(id);


--
-- TOC entry 377 (OID 4135000)
-- Name: payment_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_4 FOREIGN KEY (credit_card_id) REFERENCES credit_card(id);


--
-- TOC entry 378 (OID 4135004)
-- Name: payment_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_5 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- TOC entry 379 (OID 4135008)
-- Name: payment_fk_6; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_6 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 381 (OID 4135012)
-- Name: payment_info_cheque_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_info_cheque
    ADD CONSTRAINT payment_info_cheque_fk_1 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- TOC entry 382 (OID 4135016)
-- Name: payment_authorization_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_authorization
    ADD CONSTRAINT payment_authorization_fk_1 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- TOC entry 383 (OID 4135020)
-- Name: user_credit_card_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY user_credit_card_map
    ADD CONSTRAINT user_credit_card_map_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 385 (OID 4135024)
-- Name: payment_invoice_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_invoice_map
    ADD CONSTRAINT payment_invoice_map_fk_1 FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- TOC entry 386 (OID 4135028)
-- Name: payment_invoice_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment_invoice_map
    ADD CONSTRAINT payment_invoice_map_fk_2 FOREIGN KEY (invoice_id) REFERENCES invoice(id);


--
-- TOC entry 387 (OID 4135032)
-- Name: entity_payment_method_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity_payment_method_map
    ADD CONSTRAINT entity_payment_method_map_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 388 (OID 4135036)
-- Name: entity_payment_method_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY entity_payment_method_map
    ADD CONSTRAINT entity_payment_method_map_fk_2 FOREIGN KEY (payment_method_id) REFERENCES payment_method(id);


--
-- TOC entry 389 (OID 4135040)
-- Name: contact_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_fk_1 FOREIGN KEY (contact_id) REFERENCES contact(id);


--
-- TOC entry 390 (OID 4135044)
-- Name: contact_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_fk_2 FOREIGN KEY (type_id) REFERENCES contact_type(id);


--
-- TOC entry 391 (OID 4135048)
-- Name: contact_map_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_map
    ADD CONSTRAINT contact_map_fk_3 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- TOC entry 393 (OID 4135052)
-- Name: jbilling_table_column_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY jbilling_table_column
    ADD CONSTRAINT jbilling_table_column_fk_1 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- TOC entry 394 (OID 4135056)
-- Name: international_description_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY international_description
    ADD CONSTRAINT international_description_fk_1 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- TOC entry 395 (OID 4135060)
-- Name: international_description_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY international_description
    ADD CONSTRAINT international_description_fk_2 FOREIGN KEY (language_id) REFERENCES "language"(id);


--
-- TOC entry 396 (OID 4135064)
-- Name: event_log_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 397 (OID 4135068)
-- Name: event_log_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_2 FOREIGN KEY (module_id) REFERENCES event_log_module(id);


--
-- TOC entry 398 (OID 4135072)
-- Name: event_log_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_3 FOREIGN KEY (message_id) REFERENCES event_log_message(id);


--
-- TOC entry 399 (OID 4135076)
-- Name: event_log_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_4 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- TOC entry 400 (OID 4135080)
-- Name: event_log_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY event_log
    ADD CONSTRAINT event_log_fk_5 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 401 (OID 4135084)
-- Name: preference_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY preference
    ADD CONSTRAINT preference_fk_1 FOREIGN KEY (table_id) REFERENCES jbilling_table(id);


--
-- TOC entry 402 (OID 4135088)
-- Name: preference_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY preference
    ADD CONSTRAINT preference_fk_2 FOREIGN KEY (type_id) REFERENCES preference_type(id);


--
-- TOC entry 403 (OID 4135092)
-- Name: notification_message_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 404 (OID 4135096)
-- Name: notification_message_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_fk_2 FOREIGN KEY (type_id) REFERENCES notification_message_type(id);


--
-- TOC entry 405 (OID 4135100)
-- Name: notification_message_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message
    ADD CONSTRAINT notification_message_fk_3 FOREIGN KEY (language_id) REFERENCES "language"(id);


--
-- TOC entry 406 (OID 4135104)
-- Name: notification_message_section_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_section
    ADD CONSTRAINT notification_message_section_fk_1 FOREIGN KEY (message_id) REFERENCES notification_message(id);


--
-- TOC entry 407 (OID 4135108)
-- Name: notification_message_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_line
    ADD CONSTRAINT notification_message_line_fk_1 FOREIGN KEY (message_section_id) REFERENCES notification_message_section(id);


--
-- TOC entry 408 (OID 4135112)
-- Name: notification_message_archive_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_archive
    ADD CONSTRAINT notification_message_archive_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 409 (OID 4135116)
-- Name: notification_message_archive_line_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY notification_message_archive_line
    ADD CONSTRAINT notification_message_archive_line_fk_1 FOREIGN KEY (message_archive_id) REFERENCES notification_message_archive(id);


--
-- TOC entry 410 (OID 4135120)
-- Name: report_user_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_user
    ADD CONSTRAINT report_user_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 411 (OID 4135124)
-- Name: report_user_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_user
    ADD CONSTRAINT report_user_fk_2 FOREIGN KEY (report_id) REFERENCES report(id);


--
-- TOC entry 412 (OID 4135128)
-- Name: report_type_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_type_map
    ADD CONSTRAINT report_type_map_fk_1 FOREIGN KEY (report_id) REFERENCES report(id);


--
-- TOC entry 413 (OID 4135132)
-- Name: report_type_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_type_map
    ADD CONSTRAINT report_type_map_fk_2 FOREIGN KEY (type_id) REFERENCES report_type(id);


--
-- TOC entry 414 (OID 4135136)
-- Name: report_entity_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_entity_map
    ADD CONSTRAINT report_entity_map_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 415 (OID 4135140)
-- Name: report_entity_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_entity_map
    ADD CONSTRAINT report_entity_map_fk_2 FOREIGN KEY (report_id) REFERENCES report(id);


--
-- TOC entry 416 (OID 4135144)
-- Name: report_field_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY report_field
    ADD CONSTRAINT report_field_fk_1 FOREIGN KEY (report_id) REFERENCES report(id);


--
-- TOC entry 417 (OID 4135148)
-- Name: permission_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_fk_1 FOREIGN KEY (type_id) REFERENCES permission_type(id);


--
-- TOC entry 418 (OID 4135152)
-- Name: permission_role_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission_role_map
    ADD CONSTRAINT permission_role_map_fk_1 FOREIGN KEY (permission_id) REFERENCES permission(id);


--
-- TOC entry 419 (OID 4135156)
-- Name: permission_role_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission_role_map
    ADD CONSTRAINT permission_role_map_fk_2 FOREIGN KEY (role_id) REFERENCES role(id);


--
-- TOC entry 420 (OID 4135160)
-- Name: user_role_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY user_role_map
    ADD CONSTRAINT user_role_map_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 421 (OID 4135164)
-- Name: user_role_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY user_role_map
    ADD CONSTRAINT user_role_map_fk_2 FOREIGN KEY (role_id) REFERENCES role(id);


--
-- TOC entry 422 (OID 4135168)
-- Name: permission_user_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission_user
    ADD CONSTRAINT permission_user_map_fk_1 FOREIGN KEY (permission_id) REFERENCES permission(id);


--
-- TOC entry 423 (OID 4135172)
-- Name: permission_user_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY permission_user
    ADD CONSTRAINT permission_user_map_fk_2 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 424 (OID 4135176)
-- Name: menu_option_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY menu_option
    ADD CONSTRAINT menu_option_fk_1 FOREIGN KEY (parent_id) REFERENCES menu_option(id);


--
-- TOC entry 425 (OID 4135180)
-- Name: currency_entity_map_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY currency_entity_map
    ADD CONSTRAINT currency_entity_map_fk_1 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 426 (OID 4135184)
-- Name: currency_entity_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY currency_entity_map
    ADD CONSTRAINT currency_entity_map_fk_2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 427 (OID 4135188)
-- Name: currency_exchange_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY currency_exchange
    ADD CONSTRAINT currency_exchange_fk_1 FOREIGN KEY (currency_id) REFERENCES currency(id);


--
-- TOC entry 428 (OID 4135192)
-- Name: ageing_entity_step_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 429 (OID 4135196)
-- Name: ageing_entity_step_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY ageing_entity_step
    ADD CONSTRAINT ageing_entity_step_fk_2 FOREIGN KEY (status_id) REFERENCES user_status(id);


--
-- TOC entry 430 (OID 4135200)
-- Name: ach_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY ach
    ADD CONSTRAINT ach_fk_1 FOREIGN KEY (user_id) REFERENCES base_user(id);


--
-- TOC entry 380 (OID 4135204)
-- Name: payment_fk_7; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fk_7 FOREIGN KEY (ach_id) REFERENCES ach(id);


--
-- TOC entry 360 (OID 4135208)
-- Name: item_fk; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY invoice_line
    ADD CONSTRAINT item_fk FOREIGN KEY (item_id) REFERENCES item(id);


--
-- TOC entry 431 (OID 4135212)
-- Name: contact_field_type_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_field_type
    ADD CONSTRAINT contact_field_type_fk_1 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 432 (OID 4135216)
-- Name: contact_field_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_field
    ADD CONSTRAINT contact_field_fk_1 FOREIGN KEY (type_id) REFERENCES contact_field_type(id);


--
-- TOC entry 433 (OID 4135220)
-- Name: contact_field_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_field
    ADD CONSTRAINT contact_field_fk_2 FOREIGN KEY (contact_id) REFERENCES contact(id);


--
-- TOC entry 392 (OID 4135224)
-- Name: fk_entity_id; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY contact_type
    ADD CONSTRAINT fk_entity_id FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 434 (OID 4135228)
-- Name: list_entity_fk1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_entity
    ADD CONSTRAINT list_entity_fk1 FOREIGN KEY (list_id) REFERENCES list(id);


--
-- TOC entry 435 (OID 4135232)
-- Name: list_entity_fk2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_entity
    ADD CONSTRAINT list_entity_fk2 FOREIGN KEY (entity_id) REFERENCES entity(id);


--
-- TOC entry 436 (OID 4135236)
-- Name: list_field_fk1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_field
    ADD CONSTRAINT list_field_fk1 FOREIGN KEY (list_id) REFERENCES list(id);


--
-- TOC entry 437 (OID 4135240)
-- Name: list_field_entity_fk1; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_field_entity
    ADD CONSTRAINT list_field_entity_fk1 FOREIGN KEY (list_field_id) REFERENCES list_field(id);


--
-- TOC entry 438 (OID 4135244)
-- Name: list_field_entity_fk2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY list_field_entity
    ADD CONSTRAINT list_field_entity_fk2 FOREIGN KEY (list_entity_id) REFERENCES list_entity(id);


--
-- TOC entry 384 (OID 4135248)
-- Name: user_credit_card_map_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: jbilling_user
--

ALTER TABLE ONLY user_credit_card_map
    ADD CONSTRAINT user_credit_card_map_fk_2 FOREIGN KEY (credit_card_id) REFERENCES credit_card(id);


SET SESSION AUTHORIZATION 'postgres';

--
-- TOC entry 3 (OID 2200)
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


