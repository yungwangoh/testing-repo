CREATE TABLE IF NOT EXISTS users (

    id BIGSERIAL PRIMARY KEY,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted BOOLEAN NOT NULL,
    role VARCHAR NOT NULL
);

CREATE TABLE threads (
     id       BIGSERIAL PRIMARY KEY,
     user_id         BIGINT NOT NULL,
     created_at      TIMESTAMPTZ NOT NULL,
     deleted BOOLEAN NOT NULL
);

CREATE TABLE chats (
      id     BIGSERIAL PRIMARY KEY,
      thread_id   BIGINT NOT NULL,
      question    VARCHAR NOT NULL,
      answer      VARCHAR NOT NULL,
      deleted     BOOLEAN NOT NULL,
      created_at  TIMESTAMPTZ NOT NULL
);