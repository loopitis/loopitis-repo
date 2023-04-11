CREATE SCHEMA IF NOT EXISTS notifier;

CREATE TABLE IF NOT EXISTS notifier.requests (
    i_id SERIAL PRIMARY KEY,
    e_id VARCHAR(255),
    name VARCHAR(255),
    status VARCHAR(255),
    return_url VARCHAR(255),
    delay BIGINT,
    interval BIGINT,
    occurances INTEGER,
    done INTEGER,
    payload TEXT
);

CREATE TABLE IF NOT EXISTS notifier.executions (
    i_id SERIAL PRIMARY KEY,
    e_id VARCHAR(255),
    e_request_id VARCHAR(255),
    time_executed TIMESTAMP,
    status_code INTEGER,
    comment TEXT
);
