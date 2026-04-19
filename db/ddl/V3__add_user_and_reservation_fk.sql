-- ============================================================
--  V3 - user table + reservation.user_id FK
--  Adds application users (Kakao OAuth for now) and links
--  reservations to an optional authenticated user.
-- ============================================================

-- ------------------------------------------------------------
--  app_user : authenticated application users
-- ------------------------------------------------------------
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    email              VARCHAR(150),
    nickname           VARCHAR(80)  NOT NULL,
    phone              VARCHAR(30),
    role               VARCHAR(20)  NOT NULL,
    provider           VARCHAR(20)  NOT NULL,
    provider_id        VARCHAR(100) NOT NULL,
    profile_image_url  VARCHAR(500),
    created_at         TIMESTAMP    NOT NULL,
    updated_at         TIMESTAMP    NOT NULL,
    CONSTRAINT pk_app_user PRIMARY KEY (id),
    CONSTRAINT uk_app_user_provider UNIQUE (provider, provider_id),
    CONSTRAINT ck_app_user_role     CHECK (role IN ('USER', 'ADMIN')),
    CONSTRAINT ck_app_user_provider CHECK (provider IN ('KAKAO'))
);

CREATE INDEX idx_app_user_email ON app_user (email);

-- ------------------------------------------------------------
--  reservation : add user_id FK (nullable = guest reservations allowed)
-- ------------------------------------------------------------
ALTER TABLE reservation
    ADD COLUMN user_id BIGINT;

ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES app_user (id);

CREATE INDEX idx_reservation_user_id ON reservation (user_id);
