-- Create students table
CREATE TABLE students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id VARCHAR(255) UNIQUE,
    name VARCHAR(255) NOT NULL CHECK (name ~ '^[a-zA-Z\s]*$'),
    dob DATE,
    gender VARCHAR(10) NOT NULL,
    faculty VARCHAR(255) NOT NULL,
    course INTEGER,
    program VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    address TEXT,
    phone VARCHAR(10) NOT NULL UNIQUE CHECK (phone ~ '^0\d{9}$'),
    status VARCHAR(20) NOT NULL DEFAULT 'Studying'
);

-- Seed data
INSERT INTO students (
    student_id, 
    name, 
    dob, 
    gender, 
    faculty, 
    course, 
    program, 
    email, 
    address, 
    phone, 
    status
) VALUES 
-- Student 1
(
    'ST001',
    'John Smith',
    '2000-05-15',
    'Male',
    'Faculty of Business English',
    3,
    'Business Administration',
    'john.smith@example.com',
    '123 Main Street, City',
    '0123456789',
    'Studying'
),
-- Student 2
(
    'ST002',
    'Emily Johnson',
    '2001-08-22',
    'Female',
    'Faculty of Law',
    2,
    'Criminal Justice',
    'emily.johnson@example.com',
    '456 Oak Avenue, Town',
    '0987654321',
    'Studying'
),
-- Student 3
(
    'ST003',
    'Michael Brown',
    '1999-03-10',
    'Male',
    'Faculty of Japanese',
    4,
    'Japanese Literature',
    'michael.brown@example.com',
    '789 Pine Road, Village',
    '0567891234',
    'Graduated'
),
-- Student 4
(
    'ST004',
    'Sarah Davis',
    '2002-01-30',
    'Female',
    'Faculty of French',
    1,
    'French Studies',
    'sarah.davis@example.com',
    '321 Maple Lane, County',
    '0345678912',
    'Studying'
),
-- Student 5
(
    'ST005',
    'David Wilson',
    '2000-11-05',
    'Male',
    'Faculty of Business English',
    3,
    'International Business',
    'david.wilson@example.com',
    '654 Elm Street, District',
    '0678912345',
    'Suspended'
),
-- Student 6
(
    'ST006',
    'Jennifer Taylor',
    '2001-07-19',
    'Female',
    'Faculty of Japanese',
    2,
    'Japanese Culture',
    'jennifer.taylor@example.com',
    '987 Cedar Avenue, Region',
    '0234567891',
    'Studying'
),
-- Student 7
(
    'ST007',
    'James Anderson',
    '1999-09-25',
    'Male',
    'Faculty of Law',
    4,
    'Corporate Law',
    'james.anderson@example.com',
    '246 Birch Boulevard, Zone',
    '0891234567',
    'Studying'
),
-- Student 8
(
    'ST008',
    'Linda Martinez',
    '2002-04-12',
    'Female',
    'Faculty of French',
    1,
    'French Language',
    'linda.martinez@example.com',
    '135 Spruce Drive, Area',
    '0456789123',
    'Dropped'
),
-- Student 9
(
    'ST009',
    'Robert Thompson',
    '2000-06-28',
    'Male',
    'Faculty of Japanese',
    3,
    'Japanese Economics',
    'robert.thompson@example.com',
    '864 Willow Way, Territory',
    '0789123456',
    'Studying'
),
-- Student 10
(
    'ST010',
    'Elizabeth Garcia',
    '2001-12-07',
    'Female',
    'Faculty of Business English',
    2,
    'Business Communication',
    'elizabeth.garcia@example.com',
    '753 Aspen Place, Sector',
    '0912345678',
    'Studying'
);