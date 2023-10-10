DROP TABLE IF EXISTS users, requests, items, bookings, comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name varchar(32) NOT NULL,
    email varchar(32) NOT NULL,
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(128),
    user_id BIGINT NOT NULL,
    created timestamp,
    CONSTRAINT fk_requests_to_requestor FOREIGN KEY(user_id) REFERENCES users(id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256),
    description VARCHAR(512),
    available varchar(32),
    user_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_items_to_requests FOREIGN KEY(request_id) REFERENCES requests(id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start timestamp,
    finish timestamp,
    item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR,
    CONSTRAINT fk_booking_to_items FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT fk_booking_to_bookers FOREIGN KEY(user_id) REFERENCES users(id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(1024),
    item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_comments_to_item FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT fk_comments_to_author FOREIGN KEY(user_id) REFERENCES users(id)
);
