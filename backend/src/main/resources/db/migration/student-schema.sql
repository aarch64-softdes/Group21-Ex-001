CREATE TABLE students
(
    id         VARCHAR(255) NOT NULL,
    student_id VARCHAR(255),
    name       VARCHAR(255) NOT NULL,
    dob        TIMESTAMP WITHOUT TIME ZONE,
    gender     VARCHAR(255) NOT NULL,
    faculty    VARCHAR(255) NOT NULL,
    course     INTEGER,
    program    VARCHAR(255),
    email      VARCHAR(255) NOT NULL,
    address    VARCHAR(255),
    phone      VARCHAR(10)  NOT NULL,
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_students PRIMARY KEY (id)
);

ALTER TABLE students
    ADD CONSTRAINT uc_students_email UNIQUE (email);

ALTER TABLE students
    ADD CONSTRAINT uc_students_phone UNIQUE (phone);

ALTER TABLE students
    ADD CONSTRAINT uc_students_student UNIQUE (student_id);