CREATE SCHEMA IF NOT EXISTS htsystem;
USE htsystem;

CREATE TABLE IF NOT EXISTS measurement (
    time TIMESTAMP NOT NULL,
    temp INT NOT NULL,
    hum INT NOT NULL,
    PRIMARY KEY(time)
);