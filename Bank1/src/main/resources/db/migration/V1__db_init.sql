CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS transaction_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS account_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS public.customer_seq START WITH 1 INCREMENT BY 5;

CREATE TABLE role
(
    role_id             BIGINT              NOT NULL,
    type                VARCHAR(20)         NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (role_id)
);

CREATE TABLE transaction
(
    transaction_id          BIGINT              NOT NULL,
    source_id               BIGINT              NOT NULL,
    target_id               BIGINT              NOT NULL,
    target_bank_id          VARCHAR(20)         NOT NULL,
    amount                  DECIMAL             NOT NULL,
    status                  VARCHAR(20)         NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (transaction_id)
);

CREATE TABLE account
(
    account_id              BIGINT              NOT NULL,
    customer_id             BIGINT              NOT NULL,
    balance                 DECIMAL             NOT NULL,
    status                  VARCHAR(20)         NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (account_id)
);

CREATE TABLE public."customer"
(
    customer_id             BIGINT              NOT NULL,
    customer_name           VARCHAR(100)        NOT NULL,
    customer_phone_number   VARCHAR(20)         NOT NULL,
    username                VARCHAR(20)         NOT NULL,
    password                VARCHAR(100)        NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (customer_id)
);

CREATE TABLE public.customer_role
(
    role_id                 BIGINT              NOT NULL,
    customer_id             BIGINT              NOT NULL,
    CONSTRAINT pk_customer_role PRIMARY KEY (role_id, customer_id)
);

ALTER TABLE account ALTER COLUMN account_id SET DEFAULT nextval('account_seq');

ALTER TABLE role
    ADD CONSTRAINT uc_role_type UNIQUE (type);

ALTER TABLE public."customer"
    ADD CONSTRAINT uc_customer_username UNIQUE (username);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_SOURCE_ACCOUNT FOREIGN KEY (source_id) REFERENCES account (account_id);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_TARGET_ACCOUNT FOREIGN KEY (target_id) REFERENCES account (account_id);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_USER FOREIGN KEY (customer_id) REFERENCES public."customer" (customer_id);

ALTER TABLE public.customer_role
    ADD CONSTRAINT fk_customer_role_on_role FOREIGN KEY (role_id) REFERENCES role (role_id);

ALTER TABLE public.customer_role
    ADD CONSTRAINT fk_customer_role_on_user FOREIGN KEY (customer_id) REFERENCES public."customer" (customer_id);