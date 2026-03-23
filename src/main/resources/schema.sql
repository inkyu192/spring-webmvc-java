SET
FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS translation;
DROP TABLE IF EXISTS curation_product;
DROP TABLE IF EXISTS curation;
DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS accommodation;
DROP TABLE IF EXISTS transport;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS permission_menu;
DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS user_permission;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS user_company;
DROP TABLE IF EXISTS delivery_address;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS user_credential;
DROP TABLE IF EXISTS user_oauth;
DROP TABLE IF EXISTS users;

SET
FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    phone         VARCHAR(255) NOT NULL UNIQUE,
    gender        VARCHAR(50)  NOT NULL,
    birthday      DATE         NOT NULL,
    profile_image VARCHAR(500),
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE user_credential
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL UNIQUE,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    verified_at DATETIME(6),
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_user_credential_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE user_oauth
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    oauth_provider VARCHAR(50)  NOT NULL,
    oauth_user_id  VARCHAR(255) NOT NULL,
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_user_oauth_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE company
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    business_number VARCHAR(255) NOT NULL UNIQUE,
    address         VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(255) NOT NULL,
    created_at      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE role
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE permission
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE menu
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    translation_code VARCHAR(255) NOT NULL,
    path             VARCHAR(255),
    sort_order       BIGINT,
    parent_id        BIGINT,
    created_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_menu_parent FOREIGN KEY (parent_id) REFERENCES menu (id)
);

CREATE TABLE user_role
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE user_permission
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    CONSTRAINT fk_user_permission_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_permission_permission FOREIGN KEY (permission_id) REFERENCES permission (id)
);

CREATE TABLE user_company
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    CONSTRAINT fk_user_company_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_company_company FOREIGN KEY (company_id) REFERENCES company (id)
);

CREATE TABLE delivery_address
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    name            VARCHAR(100) NOT NULL,
    recipient_name  VARCHAR(100) NOT NULL,
    recipient_phone VARCHAR(20)  NOT NULL,
    postal_code     VARCHAR(10)  NOT NULL,
    address         VARCHAR(255) NOT NULL,
    address_detail  VARCHAR(255) NOT NULL,
    is_default      BIT(1)       NOT NULL DEFAULT 0,
    created_at      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_delivery_address_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE role_permission
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission (id)
);

CREATE TABLE permission_menu
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    permission_id BIGINT NOT NULL,
    menu_id       BIGINT NOT NULL,
    CONSTRAINT fk_permission_menu_permission FOREIGN KEY (permission_id) REFERENCES permission (id),
    CONSTRAINT fk_permission_menu_menu FOREIGN KEY (menu_id) REFERENCES menu (id)
);

CREATE TABLE product
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    category           VARCHAR(255) NOT NULL,
    status             VARCHAR(255) NOT NULL,
    name               VARCHAR(255) NOT NULL,
    description        TEXT         NOT NULL,
    price              BIGINT       NOT NULL,
    quantity           BIGINT       NOT NULL,
    exposure_attribute JSON,
    created_by         BIGINT,
    updated_by         BIGINT,
    created_at         DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at         DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE transport
(
    product_id         BIGINT PRIMARY KEY,
    departure_location VARCHAR(255) NOT NULL,
    arrival_location   VARCHAR(255) NOT NULL,
    departure_time     DATETIME(6)  NOT NULL,
    arrival_time       DATETIME(6)  NOT NULL,
    CONSTRAINT fk_transport_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE accommodation
(
    product_id     BIGINT PRIMARY KEY,
    place          VARCHAR(255) NOT NULL,
    check_in_time  DATETIME(6)  NOT NULL,
    check_out_time DATETIME(6)  NOT NULL,
    CONSTRAINT fk_accommodation_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE orders
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    ordered_at DATETIME(6)  NOT NULL,
    status     VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_price BIGINT NOT NULL,
    quantity    BIGINT NOT NULL,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    CONSTRAINT fk_order_product_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE curation
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    category   VARCHAR(50)  NOT NULL,
    is_exposed BIT(1)       NOT NULL,
    sort_order BIGINT       NOT NULL,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE curation_product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    sort_order  BIGINT,
    curation_id BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    CONSTRAINT fk_curation_product_curation FOREIGN KEY (curation_id) REFERENCES curation (id),
    CONSTRAINT fk_curation_product_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE translation
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    code       VARCHAR(255) NOT NULL,
    locale     VARCHAR(10)  NOT NULL,
    message    VARCHAR(500) NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_translation_code_locale (code, locale)
);
