CREATE SCHEMA IF NOT EXISTS notifier;

CREATE TABLE IF NOT EXISTS notifier.requests
(
    i_id  bigserial,
    e_id character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    return_url character varying(255) COLLATE pg_catalog."default",
    delay bigint,
    "interval" bigint,
    occurances integer,
    done integer,
    payload text COLLATE pg_catalog."default",
	request_time timestamp, 
	first_exec_time timestamp,
    CONSTRAINT requests_pkey PRIMARY KEY (i_id)
)
;

CREATE TABLE IF NOT EXISTS notifier.executions (
    i_id SERIAL PRIMARY KEY,
    e_id VARCHAR(255),
    e_request_id VARCHAR(255),
    time_executed TIMESTAMP,
    status_code INTEGER,
    comment TEXT
);
