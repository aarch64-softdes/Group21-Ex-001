-- Create identities table
CREATE TABLE identities (
    id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    issued_by VARCHAR(255) NOT NULL,
    issued_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    has_chip BOOLEAN,
    country VARCHAR(255),
    notes VARCHAR(255),
    CONSTRAINT pk_identities PRIMARY KEY (id)
);

CREATE TABLE addresses (
    id VARCHAR(255) NOT NULL,
    street VARCHAR(255),
    ward VARCHAR(255),
    district VARCHAR(255),
    province VARCHAR(255),
    country VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE settings (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    details TEXT,
    CONSTRAINT pk_settings PRIMARY KEY (id),
    CONSTRAINT uc_settings_name UNIQUE (name)
);

-- Create faculties table
CREATE TABLE faculties (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL UNIQUE,
    deleted_at DATE
);

-- Create programs table
CREATE TABLE programs (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL UNIQUE,
    deleted_at DATE
);

-- Create statuses table
CREATE TABLE statuses (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL UNIQUE,
    deleted_at DATE
);

-- Create students table
CREATE TABLE students (
    id VARCHAR(255) NOT NULL,
    student_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    dob DATE,
    gender VARCHAR(255) NOT NULL,
    faculty_id INTEGER NOT NULL,
    course INTEGER,
    program_id INTEGER NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    status_id INTEGER NOT NULL,
    permanent_address_id VARCHAR(255),
    temporary_address_id VARCHAR(255),
    mailing_address_id VARCHAR(255),
    identity_id VARCHAR(255),
    CONSTRAINT pk_students PRIMARY KEY (id)
);

CREATE TABLE status_transitions (
    from_status_id INTEGER NOT NULL,
    to_status_id INTEGER NOT NULL,
    CONSTRAINT pk_status_transitions PRIMARY KEY (from_status_id, to_status_id),
    CONSTRAINT fk_status_transitions_from_status FOREIGN KEY (from_status_id) REFERENCES statuses (id),
    CONSTRAINT fk_status_transitions_to_status FOREIGN KEY (to_status_id) REFERENCES statuses (id)
);

-- Add constraints to students table
ALTER TABLE students
    ADD CONSTRAINT uc_students_email UNIQUE (email);

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

-- Add foreign key constraints
ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_FACULTY FOREIGN KEY (faculty_id) REFERENCES faculties (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_PROGRAM FOREIGN KEY (program_id) REFERENCES programs (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_STATUS FOREIGN KEY (status_id) REFERENCES statuses (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_MAILING_ADDRESS FOREIGN KEY (mailing_address_id) REFERENCES addresses (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_PERMANENT_ADDRESS FOREIGN KEY (permanent_address_id) REFERENCES addresses (id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_TEMPORARY_ADDRESS FOREIGN KEY (temporary_address_id) REFERENCES addresses (id);
    
ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_IDENTITY FOREIGN KEY (identity_id) REFERENCES identities (id);
    
ALTER TABLE students
    ADD CONSTRAINT uc_students_identity UNIQUE (identity_id);

-- Insert settings records
INSERT INTO settings (id, name, details) VALUES
('d5a2c7e1-3b4f-48ae-90d5-8f6e7a9c1b2d', 'phonenumber', '["VN"]'),
('f8e7d6c5-b4a3-42d1-90e8-7f6d5c4b3a2e', 'email', '@student.hcmus.edu.vn');

-- Seed faculties data based on the existing student data (uppercase)
INSERT INTO faculties (name) VALUES
('Faculty of Business English'),
('Faculty of Law'),
('Faculty of Japanese'),
('Faculty of French');

-- Seed programs data based on the existing student data (uppercase)
INSERT INTO programs (name) VALUES
('Business Administration'),
('Criminal Justice'),
('Japanese Literature'),
('French Studies'),
('International Business'),
('Japanese Culture'),
('Corporate Law'),
('French Language'),
('Japanese Economics'),
('Business Communication');

-- Seed statuses data based on the existing student data (uppercase)
INSERT INTO statuses (name) VALUES
('Studying'),
('Graduated'),
('Suspended'),
('Dropped');

-- Insert sample identity data
INSERT INTO identities (id, type, number, issued_by, issued_date, expiry_date, has_chip, country, notes)
VALUES
-- Chip_Card (CCCD) examples - 12 digits
('ID001', 'Chip Card', '123456789012', 'Ministry of Public Security', '2018-05-10', '2028-05-10', true, 'Vietnam', 'Citizen Identity Card with Chip'),
('ID004', 'Chip Card', '234567891023', 'Ministry of Public Security', '2021-01-05', '2031-01-05', true, 'Vietnam', NULL),
('ID006', 'Chip Card', '345678912034', 'Ministry of Public Security', '2019-07-14', '2029-07-14', true, 'Vietnam', NULL),
('ID009', 'Chip Card', '456789123045', 'Ministry of Public Security', '2021-06-17', '2031-06-17', true, 'Vietnam', NULL),

-- Identity_Card (CMND) examples - 9 digits
('ID003', 'Identity Card', '456789123', 'Police Department', '2020-03-22', '2025-03-22', false, 'Vietnam', 'Old ID card'),
('ID005', 'Identity Card', '567891234', 'Police Department', '2017-11-30', '2027-11-30', false, 'Vietnam', NULL),
('ID007', 'Identity Card', '678912345', 'Police Department', '2022-09-08', '2027-09-08', false, 'Vietnam', NULL),

-- Passport examples - 2 uppercase letters followed by 7 digits
('ID002', 'Passport', 'AB1234567', 'Immigration Department', '2019-08-15', '2029-08-15', false, 'Vietnam', 'International travel document'),
('ID008', 'Passport', 'CD2345678', 'Immigration Department', '2020-04-25', '2030-04-25', false, 'Vietnam', NULL),
('ID010', 'Passport', 'EF3456789', 'Immigration Department', '2018-12-03', '2028-12-03', false, 'Vietnam', 'Diplomatic passport');

-- Insert addresses with Ward and Province data
INSERT INTO addresses (id, street, ward, district, province, country)
VALUES
-- Permanent addresses
('PERM001', '123 Main Street', 'Thanh Cong', 'Ba Dinh', 'Hanoi', 'Vietnam'),
('PERM002', '456 Oak Avenue', 'Ben Nghe', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('PERM003', '789 Pine Road', 'Hai Chau', 'Hai Chau', 'Da Nang', 'Vietnam'),
('PERM004', '321 Maple Lane', 'Thac Gian', 'Thanh Khe', 'Da Nang', 'Vietnam'),
('PERM005', '654 Elm Street', 'Phuoc My', 'Son Tra', 'Da Nang', 'Vietnam'),
('PERM006', '987 Cedar Avenue', 'Tan Dinh', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('PERM007', '246 Birch Boulevard', 'Truc Bach', 'Ba Dinh', 'Hanoi', 'Vietnam'),
('PERM008', '135 Spruce Drive', 'Pham Ngu Lao', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('PERM009', '864 Willow Way', 'Cau Giay', 'Cau Giay', 'Hanoi', 'Vietnam'),
('PERM010', '753 Aspen Place', 'Vinh Trung', 'Thanh Khe', 'Da Nang', 'Vietnam'),

-- Temporary addresses
('TEMP001', '123 Main Street', 'Thanh Cong', 'Ba Dinh', 'Hanoi', 'Vietnam'),
('TEMP002', '456 Oak Avenue', 'Ben Nghe', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('TEMP003', '789 Pine Road', 'An Hai Bac', 'Son Tra', 'Da Nang', 'Vietnam'),
('TEMP004', '321 Maple Lane', 'Thao Dien', 'District 2', 'Ho Chi Minh City', 'Vietnam'),
('TEMP005', '654 Elm Street', 'Lang Ha', 'Dong Da', 'Hanoi', 'Vietnam'),
('TEMP006', '987 Cedar Avenue', 'Tan Dinh', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('TEMP007', '246 Birch Boulevard', 'Nguyen Thai Binh', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('TEMP008', '135 Spruce Drive', 'Pham Ngu Lao', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('TEMP009', '864 Willow Way', 'Linh Trung', 'Thu Duc', 'Ho Chi Minh City', 'Vietnam'),
('TEMP010', '753 Aspen Place', 'My An', 'Ngu Hanh Son', 'Da Nang', 'Vietnam'),

-- Mailing addresses
('MAIL001', '123 Main Street', 'Thanh Cong', 'Ba Dinh', 'Hanoi', 'Vietnam'),
('MAIL002', '456 Oak Avenue', 'Ben Nghe', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('MAIL003', '789 Pine Road', 'Hai Chau', 'Hai Chau', 'Da Nang', 'Vietnam'),
('MAIL004', '321 Maple Lane', 'Thao Dien', 'District 2', 'Ho Chi Minh City', 'Vietnam'),
('MAIL005', '654 Elm Street', 'Lang Ha', 'Dong Da', 'Hanoi', 'Vietnam'),
('MAIL006', '987 Cedar Avenue', 'Tan Dinh', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('MAIL007', '246 Birch Boulevard', 'Truc Bach', 'Ba Dinh', 'Hanoi', 'Vietnam'),
('MAIL008', '135 Spruce Drive', 'Pham Ngu Lao', 'District 1', 'Ho Chi Minh City', 'Vietnam'),
('MAIL009', '864 Willow Way', 'Cau Giay', 'Cau Giay', 'Hanoi', 'Vietnam'),
('MAIL010', '753 Aspen Place', 'My An', 'Ngu Hanh Son', 'Da Nang', 'Vietnam');


-- From 'Studying' (1) status
INSERT INTO status_transitions (from_status_id, to_status_id) VALUES
(1, 2), -- Studying -> Graduated
(1, 3), -- Studying -> Suspended
(1, 4); -- Studying -> Dropped

-- From 'Suspended' (3) status
INSERT INTO status_transitions (from_status_id, to_status_id) VALUES
(3, 1), -- Suspended -> Studying (reinstatement)
(3, 4); -- Suspended -> Dropped

INSERT INTO students (
    id,
    student_id, 
    name, 
    dob, 
    gender, 
    faculty_id, 
    course, 
    program_id, 
    email, 
    phone, 
    status_id,
    permanent_address_id,
    temporary_address_id,
    mailing_address_id,
    identity_id
) VALUES 
-- Student 1
(
    'ST001_ID',
    'ST001',
    'John Smith',
    '2000-05-15',
    'Male',
    1, -- Faculty of Business English
    3,
    1, -- Business Administration
    'john.smith@student.hcmus.edu.vn',
    '+84903456789',
    1, -- Studying
    'PERM001',
    'TEMP001',
    'MAIL001',
    'ID001'
),
-- Student 2
(
    'ST002_ID',
    'ST002',
    'Emily Johnson',
    '2001-08-22',
    'Female',
    2, -- Faculty of Law
    2,
    2, -- Criminal Justice
    'emily.johnson@student.hcmus.edu.vn',
    '+84987654321',
    1, -- studying
    'PERM002',
    'TEMP002',
    'MAIL002',
    'ID002'
),
-- Student 3
(
    'ST003_ID',
    'ST003',
    'Michael Brown',
    '1999-03-10',
    'Male',
    3, -- Faculty of Japanese
    4,
    3, -- Japanese Literature
    'michael.brown@student.hcmus.edu.vn',
    '+84367891234',
    2, -- Graduated
    'PERM003',
    'TEMP003',
    'MAIL003',
    'ID003'
),
-- Student 4
(
    'ST004_ID',
    'ST004',
    'Sarah Davis',
    '2002-01-30',
    'Female',
    4, -- Faculty of French
    1,
    4, -- French Studies
    'sarah.davis@student.hcmus.edu.vn',
    '+84345678912',
    1, -- studying
    'PERM004',
    'TEMP004',
    'MAIL004',
    'ID004'
),
-- Student 5
(
    'ST005_ID',
    'ST005',
    'David Wilson',
    '2000-11-05',
    'Male',
    1, -- Faculty of Business English
    3,
    5, -- International Business
    'david.wilson@student.hcmus.edu.vn',
    '+84778912345',
    3, -- Suspended
    'PERM005',
    'TEMP005',
    'MAIL005',
    'ID005'
),
-- Student 6
(
    'ST006_ID',
    'ST006',
    'Jennifer Taylor',
    '2001-07-19',
    'Female',
    3, -- Faculty of Japanese
    2,
    6, -- Japanese Culture
    'jennifer.taylor@student.hcmus.edu.vn',
    '+84934567891',
    1, -- studying
    'PERM006',
    'TEMP006',
    'MAIL006',
    'ID006'
),
-- Student 7
(
    'ST007_ID',
    'ST007',
    'James Anderson',
    '1999-09-25',
    'Male',
    2, -- Faculty of Law
    4,
    7, -- Corporate Law
    'james.anderson@student.hcmus.edu.vn',
    '+84791234567',
    1, -- studying
    'PERM007',
    'TEMP007',
    'MAIL007',
    'ID007'
),
-- Student 8
(
    'ST008_ID',
    'ST008',
    'Linda Martinez',
    '2002-04-12',
    'Female',
    4, -- Faculty of French
    1,
    8, -- French Language
    'linda.martinez@student.hcmus.edu.vn',
    '+84796789123',
    4, -- Dropped
    'PERM008',
    'TEMP008',
    'MAIL008',
    'ID008'
),
-- Student 9
(
    'ST009_ID',
    'ST009',
    'Robert Thompson',
    '2000-06-28',
    'Male',
    3, -- Faculty of Japanese
    3,
    9, -- Japanese Economics
    'robert.thompson@student.hcmus.edu.vn',
    '+84789123456',
    1, -- studying
    'PERM009',
    'TEMP009',
    'MAIL009',
    'ID009'
),
-- Student 10
(
    'ST010_ID',
    'ST010',
    'Elizabeth Garcia',
    '2001-12-07',
    'Female',
    1, -- Faculty of Business English
    2,
    10, -- Business Communication
    'elizabeth.garcia@student.hcmus.edu.vn',
    '+84912345678',
    1, -- studying
    'PERM010',
    'TEMP010',
    'MAIL010',
    'ID010'
);