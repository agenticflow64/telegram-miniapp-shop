-- Flyway migration: initial schema for TMA SSU Web Shop
-- ===================================================================
-- Generated for PostgreSQL 16
-- Run with: flyway migrate

-- ========================= DOMAIN & EXTENSIONS =====================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================= TABLES ==============================

-- Roles -------------------------------------------------------------
CREATE TABLE role (
                      id              BIGSERIAL PRIMARY KEY,
                      role_name       VARCHAR(64) NOT NULL UNIQUE,
                      description     VARCHAR(255)
);

-- Users -------------------------------------------------------------
CREATE TABLE _user (
                       id                  BIGSERIAL PRIMARY KEY,
                       login               VARCHAR(64) UNIQUE,
                       password_hash       VARCHAR(255),
                       username_telegram   VARCHAR(64),
                       telegram_id         BIGINT      NOT NULL UNIQUE,
                       chat_id             BIGINT      NOT NULL,
                       phone_number        VARCHAR(32) UNIQUE,
                       first_name          VARCHAR(128),
                       last_name           VARCHAR(128),
                       email               VARCHAR(255) UNIQUE,
                       address             VARCHAR(255),
                       enabled             BOOLEAN     NOT NULL DEFAULT TRUE,
                       account_locked      BOOLEAN     NOT NULL DEFAULT FALSE,
                       created_at          TIMESTAMP   NOT NULL DEFAULT now(),
                       updated_at          TIMESTAMP,
                       CONSTRAINT uk_user_login_or_telegram UNIQUE (login, telegram_id)
);

-- user_roles join table --------------------------------------------
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL REFERENCES _user(id) ON DELETE CASCADE,
                           role_id BIGINT NOT NULL REFERENCES role(id)  ON DELETE CASCADE,
                           PRIMARY KEY (user_id, role_id)
);

-- Categories --------------------------------------------------------
CREATE TABLE category (
                          id              BIGSERIAL PRIMARY KEY,
                          category_name   VARCHAR(255) NOT NULL UNIQUE
);

-- Attributes --------------------------------------------------------
CREATE TABLE attribute (
                           id              BIGSERIAL PRIMARY KEY,
                           attribute_name  VARCHAR(255) NOT NULL,
                           category_id     BIGINT       NOT NULL REFERENCES category(id) ON DELETE RESTRICT,
                           CONSTRAINT uk_attribute UNIQUE (attribute_name, category_id)
);

-- Products ----------------------------------------------------------
CREATE TABLE product (
                         id                 BIGSERIAL PRIMARY KEY,
                         product_name       VARCHAR(255) NOT NULL,
                         product_price      NUMERIC(19,2) NOT NULL,
                         balance_in_stock   BIGINT        NOT NULL,
                         article_number     BIGINT        NOT NULL UNIQUE,
                         rating             NUMERIC(5,2)  DEFAULT 0,
                         description        TEXT,
                         number_of_orders   BIGINT        DEFAULT 0,
                         total_reviews      BIGINT        DEFAULT 0,
                         category_id        BIGINT        NOT NULL REFERENCES category(id) ON DELETE RESTRICT
);

-- Values (attribute values per product) ----------------------------
CREATE TABLE value (
                       id              BIGSERIAL PRIMARY KEY,
                       value           VARCHAR(255) NOT NULL,
                       attribute_id    BIGINT       NOT NULL REFERENCES attribute(id) ON DELETE CASCADE,
                       product_id      BIGINT       NOT NULL REFERENCES product(id)   ON DELETE CASCADE
);

-- Photos ------------------------------------------------------------
CREATE TABLE photo (
                       id              BIGSERIAL PRIMARY KEY,
                       object_key      VARCHAR(255) NOT NULL,
                       photo_url       TEXT         NOT NULL,
                       created_at      TIMESTAMP    NOT NULL DEFAULT now()
);

-- Junction product <-> photo ---------------------------------------
CREATE TABLE photo_product (
                               id              BIGSERIAL PRIMARY KEY,
                               index_number     INT          NOT NULL,
                               product_id       BIGINT       NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                               photo_id         BIGINT       NOT NULL REFERENCES photo(id)   ON DELETE CASCADE,
                               CONSTRAINT uk_photo_product UNIQUE (product_id, index_number)
);

-- Reviews -----------------------------------------------------------
CREATE TABLE review (
                        id              BIGSERIAL PRIMARY KEY,
                        rating          INT          NOT NULL CHECK (rating BETWEEN 1 AND 5),
                        review_text     TEXT         NOT NULL,
                        created_at      TIMESTAMP    NOT NULL DEFAULT now(),
                        product_id      BIGINT       NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                        _user_id        BIGINT       NOT NULL REFERENCES _user(id)   ON DELETE CASCADE
);

-- Review photos -----------------------------------------------------
CREATE TABLE photo_review (
                              id              BIGSERIAL PRIMARY KEY,
                              index_number     INT          NOT NULL,
                              photo_id         BIGINT       NOT NULL REFERENCES photo(id)   ON DELETE CASCADE,
                              review_id        BIGINT       NOT NULL REFERENCES review(id)  ON DELETE CASCADE,
                              CONSTRAINT uk_photo_review UNIQUE (review_id, index_number)
);

-- Cart items --------------------------------------------------------
CREATE TABLE cart_item (
                           id              BIGSERIAL PRIMARY KEY,
                           quantity        INT          NOT NULL,
                           add_date        TIMESTAMP    NOT NULL DEFAULT now(),
                           product_id      BIGINT       NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                           _user_id        BIGINT       NOT NULL REFERENCES _user(id)   ON DELETE CASCADE,
                           CONSTRAINT uk_cart_item UNIQUE (product_id, _user_id)
);

-- Wish list ---------------------------------------------------------
CREATE TABLE wish_list_item (
                                id              BIGSERIAL PRIMARY KEY,
                                add_date        TIMESTAMP    NOT NULL DEFAULT now(),
                                product_id      BIGINT       NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                                _user_id        BIGINT       NOT NULL REFERENCES _user(id)   ON DELETE CASCADE,
                                CONSTRAINT uk_wishlist UNIQUE (product_id, _user_id)
);

-- Orders ------------------------------------------------------------
CREATE TABLE orders (
                        id              BIGSERIAL PRIMARY KEY,
                        status          VARCHAR(32)  NOT NULL,
                        order_amount    NUMERIC(19,2) NOT NULL,
                        payment_method  VARCHAR(32)  NOT NULL,
                        order_address   VARCHAR(255) NOT NULL,
                        expected_date   TIMESTAMP    NOT NULL,
                        created_at      TIMESTAMP    NOT NULL DEFAULT now(),
                        user_id         BIGINT       NOT NULL REFERENCES _user(id)   ON DELETE CASCADE
);

-- Order products ----------------------------------------------------
CREATE TABLE order_product (
                               id              BIGSERIAL PRIMARY KEY,
                               quantity        BIGINT       NOT NULL,
                               amount          NUMERIC(19,2) NOT NULL,
                               product_id      BIGINT       NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                               orders_id       BIGINT       NOT NULL REFERENCES orders(id)  ON DELETE CASCADE
);

-- Tokens ------------------------------------------------------------
CREATE TABLE token (
                       id              BIGSERIAL PRIMARY KEY,
                       token           TEXT         NOT NULL UNIQUE,
                       token_type      VARCHAR(16)  NOT NULL,
                       revoked         BOOLEAN      NOT NULL DEFAULT FALSE,
                       expired         BOOLEAN      NOT NULL DEFAULT FALSE,
                       created_at      TIMESTAMP    NOT NULL DEFAULT now(),
                       user_id         BIGINT       NOT NULL REFERENCES _user(id) ON DELETE CASCADE
);

-- Indexes for search optimisation ----------------------------------
CREATE INDEX idx_product_name ON product(product_name);
CREATE INDEX idx_review_product ON review(product_id);
CREATE INDEX idx_cart_user ON cart_item(_user_id);
CREATE INDEX idx_wishlist_user ON wish_list_item(_user_id);

-- ========================== END SCHEMA ============================
