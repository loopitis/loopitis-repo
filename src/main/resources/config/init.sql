CREATE SCHEMA IF NOT EXISTS notifier;

CREATE TABLE IF NOT EXISTS notifier.requests
(
    i_id bigserial,
    e_id character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    return_url character varying(255) COLLATE pg_catalog."default",
    notify_status character varying(255) COLLATE pg_catalog."default",
    delay bigint,
    "interval" bigint,
    occurances integer,
    done integer,
    payload text COLLATE pg_catalog."default",
    request_time timestamp without time zone,
    first_exec_time timestamp without time zone,
    callbacktype character varying(7) COLLATE pg_catalog."default",
    CONSTRAINT requests_pkey PRIMARY KEY (i_id)
);

CREATE TABLE IF NOT EXISTS notifier.executions
(
    i_id integer bigserial,
    e_id character varying(255) COLLATE pg_catalog."default",
    e_request_id character varying(255) COLLATE pg_catalog."default",
    time_executed timestamp without time zone,
    status_code integer,
    comment text COLLATE pg_catalog."default",
    CONSTRAINT executions_pkey PRIMARY KEY (i_id)
)

