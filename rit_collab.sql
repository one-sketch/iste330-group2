DROP DATABASE IF EXISTS rit_collab;
CREATE DATABASE rit_collab;
USE rit_collab;

-- Drop in reverse FK order
-- (delete dependent tables first to avoid foreign key errors)
DROP TABLE IF EXISTS Guest_Interest;
DROP TABLE IF EXISTS Student_Interest;
DROP TABLE IF EXISTS Faculty_interest;
DROP TABLE IF EXISTS Faculty_Abstract;
DROP TABLE IF EXISTS student_major;
DROP TABLE IF EXISTS College_Major;
DROP TABLE IF EXISTS abstract;
DROP TABLE IF EXISTS Interest;
DROP TABLE IF EXISTS major;
DROP TABLE IF EXISTS CollegeName_Lookup;
DROP TABLE IF EXISTS Guest;
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Faculty;
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    account_id   INT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    pass_hash    CHAR(64)     NOT NULL,
    account_type ENUM('Faculty','Student','Public') NOT NULL
);

-- Insert statement for account table sample data with SHA-256 hashes
-- SHA-256('of password') 
INSERT INTO Account (account_id, username, pass_hash, account_type) VALUES
    (1, 'jdoe', 'e98fa0ea8c4cd3339be2c100653a54fa74eb4fdc29bf068728f0c0089f3ec7f2', 'Faculty'),
    (2, 'asmith', '7d68b8db8f2668594d540704cc44a3df7ccf1b8b534b97380371755bef02a45b', 'Student'),
    (3, 'guestuser', '153a255160109ee7a0981b89a9e52ad6d1a2dc4f29ac318ab6121465a04b0d1c', 'Public'),
    (4, 'bwayne', '3ecf871dfc53c5474d4d8471e0b25825ce3550e907e87df1b519414dfd7d2c71', 'Faculty'),
    (5, 'ckent', 'e4760427c21cd06f24ad9c94da906ff659b1e1429d573fefdc3b7343bbb0e843', 'Student'),
    (6, 'dprince', '30ec70e85442080e1bc825fba723dc2062a4518b0c87fe74973106a6910f26e1', 'Public'),
    (7, 'pparker', 'f574a5175b6d8de2f26042e630d4b00fd52665f517d9268fd3b4fdff6c4afe0f', 'Student'),
    (8, 'tstark', '708febff5aa70306c2baf17b53e7a3cbb5eda1541b261204359d08e35414e182', 'Faculty'),
    (9, 'srogers', '0e8e62cc9ac652873167dfdf0c10ce030c5793e5231ef62b8f4c739aec1a7d95', 'Public'),
    (10, 'nromanoff', '857e31928350703a3e43dd5d2dc3c32485cec04d039fdfc8aad6120dfee8fe88', 'Student'),
    (11, 'clark.kent', '27d5233ddd8968526735078ca9efa95ec03a22ea942420df0841f78ca9d9c272', 'Public'),
    (12, 'diana.prince', '56171552c8637a1296046a5d22f8d1e18de17b91b67bd9e9142d640318d5e867', 'Public'),
    (13, 'tony.stark', 'c393bdcaa5476d137390c49e92983d516984923a5044ccff3fc488308bd5e711', 'Student'),
    (14, 'bruce.wayne', '9fa19f4258a76dd0b966644176a9d84ebd572b72a2e1e5d941dc47cb7579f59e', 'Student'),
    (15, 'natasha.romanoff', '223e13aecbb316f4c02f6c3c009bf4e72a175a147fb226680a9d33c9e6dff56d', 'Faculty'),
    (16, 'guest.user', '6cf629bd61ea5389f4ac832ab830903d1e0af478601f7e5a11a03932c3a344c8', 'Public'),
    (17, 'alice.smith', '7358f9e93ea640fc7809d6dd4271685a8bf6e853ab0306e14379c319ec50dcbf', 'Student'),
    (18, 'jack.bauer', '8b7628f4b6d4aa33f2e03ecc2a9d3cc7dad4841ffbd84f6ff3105d622d6baaf8', 'Student'),
    (19, 'steve.rogers', 'a3f804fc75309fe1f09c5f4409a9e01b5eb2b5abb3ae878c1c5166ee99b6d05c', 'Student'),
    (20, 'sam.wilson', '2001af5627ccb59abf03716673255e9903632d9f8c6b44c9b793b177d7f0a139', 'Faculty');

CREATE TABLE Faculty (
    faculty_id    INT PRIMARY KEY AUTO_INCREMENT,
    account_id    INT          NOT NULL,
    fname         VARCHAR(50)  NOT NULL,
    lname         VARCHAR(50)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    building      INT,
    office_number VARCHAR(10),
    cell_phone    VARCHAR(20),
    slack         VARCHAR(50),
    college_id    INT,
    office_hours  VARCHAR(255),
    calendar_link VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

-- Insert statement for faculty table sample data
INSERT INTO Faculty (faculty_id, account_id, fname, lname, email, 
    building, office_number, cell_phone, slack, college_id, office_hours, calendar_link) 
VALUES
    (1, 1, 'John', 'Doe', 'jdoe@rit.edu', 1, '101', '123-456-7890', 'jdoe', 1, '9:00 AM - 5:00 PM', 'https://calendar.rit.edu/jdoe'),
    (2, 4, 'Bruce', 'Wayne', 'bwayne@rit.edu', 2, '202', '234-567-8901', 'bwayne', 2, '10:00 AM - 6:00 PM', 'https://calendar.rit.edu/bwayne'),
    (3, 8, 'Tony', 'Stark', 'tstark@rit.edu', 3, '303', '345-678-9012', 'tstark', 3, '11:00 AM - 7:00 PM', 'https://calendar.rit.edu/tstark'),
    (4, 15, 'Natasha', 'Romanoff', 'nromanoff@rit.edu', 4, '404', '456-789-0123', 'nromanoff', 4, '12:00 PM - 8:00 PM', 'https://calendar.rit.edu/nromanoff'),
    (5, 20, 'Sam', 'Wilson', 'swilson@rit.edu', 5, '505', '567-890-1234', 'swilson', 5, '1:00 PM - 9:00 PM', 'https://calendar.rit.edu/swilson');

CREATE TABLE Student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT          NOT NULL,
    fname      VARCHAR(50)  NOT NULL,
    lname      VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20),
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

-- Insert statement for student table sample data
INSERT INTO Student (student_id, account_id, fname, lname, email, phone) VALUES
    (1, 2, 'Alice', 'Smith', 'asmith@rit.edu', '585-555-1234'),
    (2, 5, 'Clark', 'Kent', 'ckent@rit.edu', '585-555-5678'),
    (3, 7, 'Peter', 'Parker', 'pparker@rit.edu', '585-555-9012'),
    (4, 10, 'Natasha', 'Romanoff', 'nromanoff@rit.edu', '585-555-3456'),
    (5, 13, 'Tony', 'Stark', 'tstark@rit.edu', '585-555-7890'),
    (6, 14, 'Bruce', 'Wayne', 'bwayne@rit.edu', '585-555-1111'),
    (7, 17, 'Diana', 'Prince', 'dprince@rit.edu', '585-555-2222'),
    (8, 18, 'Jack', 'Bauer', 'jbauer@rit.edu', '585-555-3333'),
    (9, 19, 'Steve', 'Rogers', 'srogers@rit.edu', '585-555-4444');

CREATE TABLE Guest (
    guest_id     INT PRIMARY KEY AUTO_INCREMENT,
    account_id   INT          NOT NULL,
    fname        VARCHAR(50),
    lname        VARCHAR(50),
    company_name VARCHAR(100),
    email        VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

-- Insert statement for guest table sample data
INSERT INTO Guest (guest_id, account_id, fname, lname, company_name, email) VALUES
    (1, 3, 'Guest', 'User', 'N/A', 'guestuser@gmail.com'),
    (2, 6, 'D', 'Prince', 'Amazon Inc.', 'dprince@amazon.com'),
    (3, 9, 'Steve', 'Rogers', 'Shield Corp.', 'srogers@shield.com'),
    (4, 11, 'Clark', 'Kent', 'Daily Planet', 'ckent@dailyplanet.com'),
    (5, 12, 'Diana', 'Prince', 'Amazon Inc.', 'dprince@amazon.com'),
    (6, 16, 'Guest', 'User', 'N/A', 'guestuser2@gmail.com');

CREATE TABLE abstract (
    abstract_id      INT PRIMARY KEY AUTO_INCREMENT,
    title            VARCHAR(200),
    abstract_type    ENUM('book','speaking') NOT NULL,
    abstract_content TEXT
);

-- Insert statement for abstract table sample data
INSERT INTO abstract (abstract_id, title, abstract_type, abstract_content) VALUES
    (1, 'Introduction to Database Systems', 'book', 'This is a sample abstract for a book on database systems.'),
    (2, 'Advanced Topics in Software Engineering', 'speaking', 'This is a sample abstract for a speaking engagement on software engineering.'),
    (3, 'Data Science in Practice', 'book', 'This is a sample abstract for a book on data science.');

CREATE TABLE Faculty_Abstract (
    abstract_id INT NOT NULL,
    faculty_id  INT NOT NULL,
    PRIMARY KEY (abstract_id, faculty_id),
    FOREIGN KEY (abstract_id) REFERENCES abstract(abstract_id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id)  REFERENCES Faculty(faculty_id) ON DELETE CASCADE
);

-- Insert statement for faculty_abstract table sample data
INSERT INTO Faculty_Abstract (abstract_id, faculty_id) VALUES
    (1, 1),
    (2, 4),
    (3, 8),
    (1, 15),
    (2, 20);

CREATE TABLE Interest (
    interest_id   INT PRIMARY KEY AUTO_INCREMENT,
    interest_word VARCHAR(40) NOT NULL UNIQUE
);

-- Insert statement for interest table sample data
INSERT INTO Interest (interest_id, interest_word) VALUES
    (1, 'Artificial Intelligence'),
    (2, 'Machine Learning'),
    (3, 'Data Science'),
    (4, 'Cybersecurity'),
    (5, 'Software Engineering'),
    (6, 'Cloud Computing'),
    (7, 'Internet of Things'),
    (8, 'Blockchain'),
    (9, 'Virtual Reality'),
    (10, 'Augmented Reality'),
    (11, 'Human-Computer Interaction'),
    (12, 'Game Development'),
    (13, 'Sign Language Comprehension'),
    (14, 'Robotics'),
    (15, 'Quantum Computing'),
    (16, 'Bioinformatics'),
    (17, 'Entrepreneurship'),
    (18, 'Data Visualization'),
    (19, 'Edge Computing'),
    (20, '5G Technology');

CREATE TABLE Faculty_interest (
    faculty_id  INT NOT NULL,
    interest_id INT NOT NULL,
    PRIMARY KEY (faculty_id, interest_id),
    FOREIGN KEY (faculty_id)  REFERENCES Faculty(faculty_id) ON DELETE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES Interest(interest_id) ON DELETE CASCADE
);

-- Insert statement for faculty_interest table sample data
INSERT INTO Faculty_interest (faculty_id, interest_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
    (2, 6), (2, 7), (2, 8), (2, 9),
    (3, 10), (3, 11), (3, 2), (3, 13), (3, 14),
    (4, 11), (4, 12), (4, 13), (4, 14),
    (5, 15), (5, 16), (5, 17), (5, 18), (5, 19), (5, 20);

CREATE TABLE Student_Interest (
    student_id  INT NOT NULL,
    interest_id INT NOT NULL,
    PRIMARY KEY (student_id, interest_id),
    FOREIGN KEY (student_id)  REFERENCES Student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES Interest(interest_id) ON DELETE CASCADE
);

-- Insert statement for student_interest table sample data
INSERT INTO Student_Interest (student_id, interest_id) VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 4), (2, 5), (2, 6),
    (3, 7), (3, 8), (3, 9),
    (4, 10), (4, 11), (4, 12),
    (5, 13), (5, 14), (5, 15),
    (6, 16), (6, 17), (6, 18),
    (7, 19), (7, 20),
    (8, 1), (8, 20),
    (9, 2), (9, 19);

CREATE TABLE Guest_Interest (
    guest_id    INT NOT NULL,
    interest_id INT NOT NULL,
    PRIMARY KEY (guest_id, interest_id),
    FOREIGN KEY (guest_id)    REFERENCES Guest(guest_id) ON DELETE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES Interest(interest_id) ON DELETE CASCADE
);

-- Insert statement for guest_interest table sample data
INSERT INTO Guest_Interest (guest_id, interest_id) VALUES
    (1, 1), (1, 5), (1, 10),
    (2, 2), (2, 6), (2, 11),
    (3, 3), (3, 7), (3, 12),
    (4, 4), (4, 8), (4, 13),
    (5, 5), (5, 9), (5, 14),
    (6, 1), (6, 15), (6, 20);

-- Lookup Tables
CREATE TABLE CollegeName_Lookup (
    college_id   VARCHAR(6)  PRIMARY KEY,
    college_name VARCHAR(70) NOT NULL
);

-- Insert statement for collegeName_lookup table sample data
INSERT INTO CollegeName_Lookup (college_id, college_name) VALUES
    ('COS', 'College of Science'),
    ('GCCIS', 'Golisano College of Computing and Information Sciences'),
    ('NTID', 'National Technical Institute for the Deaf');

CREATE TABLE major (
    major_id   INT PRIMARY KEY AUTO_INCREMENT,
    major_name VARCHAR(64) NOT NULL
);

-- Insert statement for major table sample data
INSERT INTO major (major_id, major_name) VALUES
    (1, 'Computer Science'),
    (2, 'Information Technology'),
    (3, 'Software Engineering'),
    (4, 'Cybersecurity'),
    (5, 'Industrial Design'),
    (6, 'Computer Engineering'),
    (7, 'Business Management'),
    (8, 'Sign Language Interpretation'),
    (9, 'Game Design'),
    (10, 'Packaging Science');

CREATE TABLE College_Major (
    college_id VARCHAR(20) NOT NULL,
    major_id   INT         NOT NULL,
    PRIMARY KEY (college_id, major_id),
    FOREIGN KEY (college_id) REFERENCES CollegeName_Lookup(college_id) ON DELETE CASCADE,
    FOREIGN KEY (major_id) REFERENCES major(major_id) ON DELETE CASCADE
);

-- Insert statement for college_major table sample data
INSERT INTO College_Major (college_id, major_id) VALUES
    ('COS', 1),
    ('COS', 2),
    ('COS', 3),
    ('COS', 4),
    ('COS', 6),
    ('COS', 8),
    ('COS', 9),
    ('COS', 10),
    ('GCCIS', 1),
    ('GCCIS', 2),
    ('GCCIS', 3),
    ('GCCIS', 4),
    ('GCCIS', 6),
    ('GCCIS', 8),
    ('GCCIS', 9),
    ('GCCIS', 10),
    ('NTID', 8);

CREATE TABLE student_major (
    student_id INT NOT NULL,
    major_id   INT NOT NULL,
    PRIMARY KEY (student_id, major_id),
    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (major_id) REFERENCES major(major_id) ON DELETE CASCADE
);

-- Insert statement for student_major table sample data
INSERT INTO student_major (student_id, major_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6),
    (7, 7),
    (8, 8),
    (9, 9);