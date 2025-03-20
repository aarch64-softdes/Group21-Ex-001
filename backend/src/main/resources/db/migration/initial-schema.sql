CREATE TABLE addresses
(
    id       VARCHAR(255) NOT NULL,
    street   VARCHAR(255),
    ward     VARCHAR(255),
    district VARCHAR(255),
    country  VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE identities
(
    id          VARCHAR(255) NOT NULL,
    type        VARCHAR(255),
    number      VARCHAR(255),
    issued_by   VARCHAR(255),
    issued_date date,
    expiry_date date,
    CONSTRAINT pk_identities PRIMARY KEY (id)
);

CREATE TABLE students
(
    id                   VARCHAR(255) NOT NULL,
    student_id           VARCHAR(255),
    name                 VARCHAR(255) NOT NULL,
    dob                  date,
    gender               VARCHAR(255) NOT NULL,
    faculty              VARCHAR(255) NOT NULL,
    course               INTEGER,
    program              VARCHAR(255),
    email                VARCHAR(255) NOT NULL,
    phone                VARCHAR(10)  NOT NULL,
    status               VARCHAR(255) NOT NULL,
    permanent_address_id VARCHAR(255),
    temporary_address_id VARCHAR(255),
    mailing_address_id   VARCHAR(255),
    identity_id          VARCHAR(255),
    CONSTRAINT pk_students PRIMARY KEY (id)
);

ALTER TABLE students
    ADD CONSTRAINT uc_students_email UNIQUE (email);

ALTER TABLE students
    ADD CONSTRAINT uc_students_identity UNIQUE (identity_id);

ALTER TABLE students
    ADD CONSTRAINT uc_students_mailing_address UNIQUE (mailing_address_id);

ALTER TABLE students
    ADD CONSTRAINT uc_students_permanent_address UNIQUE (permanent_address_id);

ALTER TABLE students
    ADD CONSTRAINT uc_students_phone UNIQUE (phone);

ALTER TABLE students
    ADD CONSTRAINT uc_students_student UNIQUE (student_id);

ALTER TABLE students
    ADD CONSTRAINT uc_students_temporary_address UNIQUE (temporary_address_id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_IDENTITY FOREIGN KEY (identity_id) REFERENCES identities (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_MAILING_ADDRESS FOREIGN KEY (mailing_address_id) REFERENCES addresses (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_PERMANENT_ADDRESS FOREIGN KEY (permanent_address_id) REFERENCES addresses (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_TEMPORARY_ADDRESS FOREIGN KEY (temporary_address_id) REFERENCES addresses (id);