CREATE TABLE words (
                       id BIGSERIAL PRIMARY KEY,
                       sequence_number INTEGER NOT NULL UNIQUE,
                       word VARCHAR(100) NOT NULL,
                       translation VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT chk_words_sequence_number
                           CHECK (sequence_number > 0)
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       tg_user_id BIGINT NOT NULL UNIQUE,
                       username VARCHAR(255),
                       first_name VARCHAR(255),
                       started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE collocations (
                        id BIGSERIAL PRIMARY KEY,
                        word_id BIGINT NOT NULL REFERENCES words(id)
                            ON DELETE CASCADE,
                        text VARCHAR(255) NOT NULL,
                        translation VARCHAR(255) NOT NULL,
                        sort_order INTEGER NOT NULL DEFAULT 1,

                        CONSTRAINT chk_collocations_sort_order
                            CHECK (sort_order > 0),

                        CONSTRAINT uq_collocations_word_order
                            UNIQUE (word_id, sort_order)
);

CREATE TABLE phrases (
                         id BIGSERIAL PRIMARY KEY,
                         word_id BIGINT NOT NULL REFERENCES words(id)
                             ON DELETE CASCADE,
                         text VARCHAR(500) NOT NULL,
                         translation VARCHAR(500) NOT NULL,
                         sort_order INTEGER NOT NULL DEFAULT 1,

                         CONSTRAINT chk_phrases_sort_order
                             CHECK (sort_order > 0),

                         CONSTRAINT uq_phrases_word_order
                             UNIQUE (word_id, sort_order)
);

CREATE TABLE images (
                        id BIGSERIAL PRIMARY KEY,
                        word_id BIGINT NOT NULL REFERENCES words(id)
                            ON DELETE CASCADE,
                        image_url TEXT NOT NULL,
                        description VARCHAR(255),
                        sort_order INTEGER NOT NULL DEFAULT 1,

                        CONSTRAINT chk_images_sort_order
                            CHECK (sort_order > 0),

                        CONSTRAINT uq_images_word_order
                            UNIQUE (word_id, sort_order)
);

CREATE TABLE user_progress (
                        user_id BIGINT PRIMARY KEY REFERENCES users(id)
                                ON DELETE CASCADE,
                        last_sequence_number INTEGER NOT NULL DEFAULT 0,
                        last_sent_at TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT chk_progress_sequence_number
                            CHECK (last_sequence_number >= 0)
);

CREATE TABLE word_deliveries (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        word_id BIGINT NOT NULL REFERENCES words(id) ON DELETE RESTRICT,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        status VARCHAR(20) NOT NULL DEFAULT 'pending'
                            CHECK (status IN ('pending', 'sent', 'failed')),
                        error_message TEXT,

                        CONSTRAINT uq_delivery_user_word
                            UNIQUE (user_id, word_id)
);

CREATE INDEX idx_collocations_word_id
    ON collocations(word_id);

CREATE INDEX idx_phrases_word_id
    ON phrases(word_id);

CREATE INDEX idx_images_word_id
    ON images(word_id);

CREATE INDEX idx_deliveries_user_created_at
    ON word_deliveries(user_id, created_at DESC);