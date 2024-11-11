CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS transaction_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS account_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS public.user_seq START WITH 1 INCREMENT BY 5;

CREATE TABLE role
(
    id   BIGINT      NOT NULL,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE transaction
(
    id               BIGINT                      NOT NULL,
    amount           DECIMAL                     NOT NULL,
    description      VARCHAR(50),
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    reference_number UUID                        NOT NULL,
    status           VARCHAR(20)                 NOT NULL,
    from_account_id   BIGINT                      NOT NULL,
    to_account_id     BIGINT                      NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

CREATE TABLE account
(
    id      BIGINT      NOT NULL,
    iban    VARCHAR(34) NOT NULL,
    name    VARCHAR(50) NOT NULL,
    balance DECIMAL     NOT NULL,
    user_id BIGINT      NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE public."user"
(
    id         BIGINT       NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(20)  NOT NULL,
    email      VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE public.user_role
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (role_id, user_id)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_type UNIQUE (type);

ALTER TABLE transaction
    ADD CONSTRAINT uc_transaction_referencenumber UNIQUE (reference_number);

ALTER TABLE account
    ADD CONSTRAINT uc_account_iban UNIQUE (iban);

ALTER TABLE public."user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE public."user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);

CREATE UNIQUE INDEX account_user_id_iban_key ON account (user_id, iban);

CREATE UNIQUE INDEX account_user_id_name_key ON account (user_id, name);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_FROM_ACCOUNT FOREIGN KEY (from_account_id) REFERENCES account (id);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_TO_ACCOUNT FOREIGN KEY (to_account_id) REFERENCES account (id);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (id);

ALTER TABLE public.user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE public.user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES public."user" (id);