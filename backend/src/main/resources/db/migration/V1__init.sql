CREATE TYPE user_role AS ENUM ('user', 'admin');
CREATE TYPE reaction_type AS ENUM ('like');

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_picture_link TEXT NULL,
    role user_role NOT NULL
);
CREATE TABLE posts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    creation_timestamp TIMESTAMP NOT NULL,
    deletion_timestamp TIMESTAMP NULL,
    text_content TEXT NOT NULL,
    image_link TEXT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id)
);
CREATE TABLE comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    creation_timestamp TIMESTAMP NOT NULL,
    deletion_timestamp TIMESTAMP NULL,
    text_content VARCHAR(200) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id),
    post_id BIGINT NOT NULL REFERENCES posts (id)
);
CREATE TABLE reactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reaction reaction_type NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id),
    comment_id BIGINT NULL REFERENCES comments (id),
    post_id BIGINT NULL REFERENCES posts (id),
    UNIQUE(user_id, post_id),
    UNIQUE(user_id, comment_id)
);
