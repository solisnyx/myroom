-- ============================================================
--  myroom - Japanese izakaya reservation schema
--  Target: H2 (MySQL mode) / MySQL 8.x compatible
-- ============================================================

-- Drop in reverse dependency order (safe for re-runs on H2)
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS room;

-- ------------------------------------------------------------
--  room : private dining rooms (個室 / 座敷)
-- ------------------------------------------------------------
CREATE TABLE room (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    capacity    INT          NOT NULL,
    CONSTRAINT pk_room PRIMARY KEY (id)
);

-- ------------------------------------------------------------
--  menu : drinks (酒) and snacks (おつまみ)
-- ------------------------------------------------------------
CREATE TABLE menu (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name_ja         VARCHAR(200) NOT NULL,
    name_ko         VARCHAR(200),
    category        VARCHAR(20)  NOT NULL,
    price           INT          NOT NULL,
    description_ja  VARCHAR(500),
    description_ko  VARCHAR(500),
    available       BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_menu PRIMARY KEY (id),
    CONSTRAINT ck_menu_category CHECK (category IN ('DRINK', 'FOOD')),
    CONSTRAINT ck_menu_price    CHECK (price >= 0)
);

CREATE INDEX idx_menu_category  ON menu (category);
CREATE INDEX idx_menu_available ON menu (available);

-- ------------------------------------------------------------
--  reservation : customer bookings
-- ------------------------------------------------------------
CREATE TABLE reservation (
    id                  BIGINT        NOT NULL AUTO_INCREMENT,
    customer_name       VARCHAR(100)  NOT NULL,
    phone               VARCHAR(30)   NOT NULL,
    email               VARCHAR(150),
    party_size          INT           NOT NULL,
    reserved_at         TIMESTAMP     NOT NULL,
    notes               VARCHAR(1000),
    status              VARCHAR(20)   NOT NULL,
    preferred_language  VARCHAR(10)   NOT NULL,
    room_id             BIGINT,
    created_at          TIMESTAMP     NOT NULL,
    updated_at          TIMESTAMP     NOT NULL,
    CONSTRAINT pk_reservation PRIMARY KEY (id),
    CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room (id),
    CONSTRAINT ck_reservation_status   CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED')),
    CONSTRAINT ck_reservation_language CHECK (preferred_language IN ('JA', 'KO')),
    CONSTRAINT ck_reservation_party    CHECK (party_size >= 1)
);

CREATE INDEX idx_reservation_status      ON reservation (status);
CREATE INDEX idx_reservation_reserved_at ON reservation (reserved_at);
CREATE INDEX idx_reservation_room_id     ON reservation (room_id);
