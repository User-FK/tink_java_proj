--liquibase formatted sql

--changeset fyodor:1
CREATE TABLE chats(
    id INTEGER PRIMARY KEY
);

--changeset fyodor:2
CREATE TABLE links(
    id SERIAL PRIMARY KEY,
    url VARCHAR(2000) NOT NULL,
    chat_id INTEGER REFERENCES chats (id),
    last_update TIMESTAMP WITH TIME ZONE DEFAULT '1954-01-01',
    host_type VARCHAR(2000) NOT NULL DEFAULT 'default'
)

--changeset fyodor:3
CREATE TABLE stackoverflow_answers(
    answer_id INTEGER PRIMARY KEY,
    link_id INTEGER REFERENCES links (id) ON DELETE CASCADE
)

--changeset fyodor:4
CREATE TABLE pull_requests(
    id INTEGER PRIMARY KEY,
    link_id INTEGER REFERENCES links (id),
    state VARCHAR(30)
)