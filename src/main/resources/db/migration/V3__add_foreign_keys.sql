-- V3__add_foreign_keys.sql
-- Add foreign key constraints to blog_posts table
-- to reference users table for author information

ALTER TABLE blog_posts
    ADD COLUMN author_id BIGINT;

ALTER TABLE blog_posts
    ADD CONSTRAINT fk_blog_posts_on_author
        FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE blog_posts
    DROP COLUMN author;