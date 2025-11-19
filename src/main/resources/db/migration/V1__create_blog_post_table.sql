-- V1__create_blog_post_table.sql
-- Postgres migration: create sequence and blog_posts table
CREATE SEQUENCE IF NOT EXISTS blog_posts_seq START 1;

CREATE TABLE IF NOT EXISTS blog_posts (
    id BIGINT PRIMARY KEY DEFAULT nextval('blog_posts_seq'),
    title VARCHAR(255) NOT NULL,
    content TEXT,
    author VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

