CREATE TABLE addresses
(
    id       VARCHAR(255) NOT NULL,
    street   VARCHAR(255),
    ward     VARCHAR(255),
    district VARCHAR(255),
    country  VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);