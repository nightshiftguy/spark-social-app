CREATE TABLE image_urls (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    creation_timestamp TIMESTAMP NOT NULL,
    public_id UUID UNIQUE NOT NULL,
    image_link TEXT UNIQUE NULL,
    post_id BIGINT NULL UNIQUE REFERENCES posts (id),
    owner_id BIGINT NOT NULL REFERENCES users (id)
);

ALTER TABLE posts DROP COLUMN image_link;