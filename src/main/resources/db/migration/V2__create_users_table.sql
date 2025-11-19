-- V2__create_users_table.sql
-- Postgres migration: create sequence and users table
CREATE SEQUENCE IF NOT EXISTS users_seq START 1;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

