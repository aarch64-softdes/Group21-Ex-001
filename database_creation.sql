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

-- Create addresses table
CREATE TABLE addresses (
    id VARCHAR(255) NOT NULL,
    street VARCHAR(255),
    ward VARCHAR(255),
    district VARCHAR(255),
    country VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
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
    phone VARCHAR(10) NOT NULL,
    status_id INTEGER NOT NULL,
    permanent_address_id VARCHAR(255),
    temporary_address_id VARCHAR(255),
    mailing_address_id VARCHAR(255),
    identity_id VARCHAR(255),
    CONSTRAINT pk_students PRIMARY KEY (id)
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
('ID001', 'Chip_Card', '123456789012', 'Ministry of Public Security', '2018-05-10', '2028-05-10', true, 'Vietnam', 'Citizen Identity Card with Chip'),
('ID004', 'Chip_Card', '234567891023', 'Ministry of Public Security', '2021-01-05', '2031-01-05', true, 'Vietnam', NULL),
('ID006', 'Chip_Card', '345678912034', 'Ministry of Public Security', '2019-07-14', '2029-07-14', true, 'Vietnam', NULL),
('ID009', 'Chip_Card', '456789123045', 'Ministry of Public Security', '2021-06-17', '2031-06-17', true, 'Vietnam', NULL),

-- Identity_Card (CMND) examples - 9 digits
('ID003', 'Identity_Card', '456789123', 'Police Department', '2020-03-22', '2025-03-22', false, 'Vietnam', 'Old ID card'),
('ID005', 'Identity_Card', '567891234', 'Police Department', '2017-11-30', '2027-11-30', false, 'Vietnam', NULL),
('ID007', 'Identity_Card', '678912345', 'Police Department', '2022-09-08', '2027-09-08', false, 'Vietnam', NULL),

-- Passport examples - 2 uppercase letters followed by 7 digits
('ID002', 'Passport', 'AB1234567', 'Immigration Department', '2019-08-15', '2029-08-15', false, 'Vietnam', 'International travel document'),
('ID008', 'Passport', 'CD2345678', 'Immigration Department', '2020-04-25', '2030-04-25', false, 'Vietnam', NULL),
('ID010', 'Passport', 'EF3456789', 'Immigration Department', '2018-12-03', '2028-12-03', false, 'Vietnam', 'Diplomatic passport');

-- Insert addresses (converting the single address into three address types)
INSERT INTO addresses (id, street, ward, district, country)
VALUES
-- Permanent addresses
('PERM001', '123 Main Street', NULL, 'City', 'Country'),
('PERM002', '456 Oak Avenue', NULL, 'Town', 'Country'),
('PERM003', '789 Pine Road', NULL, 'Village', 'Country'),
('PERM004', '321 Maple Lane', NULL, 'County', 'Country'),
('PERM005', '654 Elm Street', NULL, 'District', 'Country'),
('PERM006', '987 Cedar Avenue', NULL, 'Region', 'Country'),
('PERM007', '246 Birch Boulevard', NULL, 'Zone', 'Country'),
('PERM008', '135 Spruce Drive', NULL, 'Area', 'Country'),
('PERM009', '864 Willow Way', NULL, 'Territory', 'Country'),
('PERM010', '753 Aspen Place', NULL, 'Sector', 'Country'),

-- Temporary addresses (using same as permanent for demo purposes)
('TEMP001', '123 Main Street', NULL, 'City', 'Country'),
('TEMP002', '456 Oak Avenue', NULL, 'Town', 'Country'),
('TEMP003', '789 Pine Road', NULL, 'Village', 'Country'),
('TEMP004', '321 Maple Lane', NULL, 'County', 'Country'),
('TEMP005', '654 Elm Street', NULL, 'District', 'Country'),
('TEMP006', '987 Cedar Avenue', NULL, 'Region', 'Country'),
('TEMP007', '246 Birch Boulevard', NULL, 'Zone', 'Country'),
('TEMP008', '135 Spruce Drive', NULL, 'Area', 'Country'),
('TEMP009', '864 Willow Way', NULL, 'Territory', 'Country'),
('TEMP010', '753 Aspen Place', NULL, 'Sector', 'Country'),

-- Mailing addresses (using same as permanent for demo purposes)
('MAIL001', '123 Main Street', NULL, 'City', 'Country'),
('MAIL002', '456 Oak Avenue', NULL, 'Town', 'Country'),
('MAIL003', '789 Pine Road', NULL, 'Village', 'Country'),
('MAIL004', '321 Maple Lane', NULL, 'County', 'Country'),
('MAIL005', '654 Elm Street', NULL, 'District', 'Country'),
('MAIL006', '987 Cedar Avenue', NULL, 'Region', 'Country'),
('MAIL007', '246 Birch Boulevard', NULL, 'Zone', 'Country'),
('MAIL008', '135 Spruce Drive', NULL, 'Area', 'Country'),
('MAIL009', '864 Willow Way', NULL, 'Territory', 'Country'),
('MAIL010', '753 Aspen Place', NULL, 'Sector', 'Country');

-- Seed student data with relationships to addresses, faculties, programs, and statuses
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
    'john.smith@example.com',
    '0123456789',
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
    'emily.johnson@example.com',
    '0987654321',
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
    'michael.brown@example.com',
    '0567891234',
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
    'sarah.davis@example.com',
    '0345678912',
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
    'david.wilson@example.com',
    '0678912345',
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
    'jennifer.taylor@example.com',
    '0234567891',
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
    'james.anderson@example.com',
    '0891234567',
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
    'linda.martinez@example.com',
    '0456789123',
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
    'robert.thompson@example.com',
    '0789123456',
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
    'elizabeth.garcia@example.com',
    '0912345678',
    1, -- studying
    'PERM010',
    'TEMP010',
    'MAIL010',
    'ID010'
);