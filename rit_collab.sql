-- rit_collab.sql
-- Authors: GROUP 2
-- Kylie Higgins
-- Kaylie Chiodi
-- Justen Jiang 
-- Alex Guan
-- Jordan Ciferni
-- Chris Donalds
-- Date: April 22, 2026

DROP DATABASE IF EXISTS rit_collab;
CREATE DATABASE rit_collab;
USE rit_collab;

-- Drop in reverse FK order
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

-- Insert statement for account table sample data - NO DUPLICATES
INSERT INTO Account (account_id, username, pass_hash, account_type) VALUES
    -- Faculty (5 unique faculty)
    (1, 'jdoe', 'e98fa0ea8c4cd3339be2c100653a54fa74eb4fdc29bf068728f0c0089f3ec7f2', 'Faculty'),
    (2, 'bwayne', '3ecf871dfc53c5474d4d8471e0b25825ce3550e907e87df1b519414dfd7d2c71', 'Faculty'),
    (3, 'tstark', '708febff5aa70306c2baf17b53e7a3cbb5eda1541b261204359d08e35414e182', 'Faculty'),
    (4, 'nromanoff', '223e13aecbb316f4c02f6c3c009bf4e72a175a147fb226680a9d33c9e6dff56d', 'Faculty'),
    (5, 'swilson', '2001af5627ccb59abf03716673255e9903632d9f8c6b44c9b793b177d7f0a139', 'Faculty'),
    
    -- Students (7 unique students)
    (6, 'asmith', '7d68b8db8f2668594d540704cc44a3df7ccf1b8b534b97380371755bef02a45b', 'Student'),
    (7, 'ckent', 'e4760427c21cd06f24ad9c94da906ff659b1e1429d573fefdc3b7343bbb0e843', 'Student'),
    (8, 'pparker', 'f574a5175b6d8de2f26042e630d4b00fd52665f517d9268fd3b4fdff6c4afe0f', 'Student'),
    (9, 'kbrown', '7d68b8db8f2668594d540704cc44a3df7ccf1b8b534b97380371755bef02a45b', 'Student'),
    (10, 'dprince', '30ec70e85442080e1bc825fba723dc2062a4518b0c87fe74973106a6910f26e1', 'Student'),
    (11, 'jbauer', '8b7628f4b6d4aa33f2e03ecc2a9d3cc7dad4841ffbd84f6ff3105d622d6baaf8', 'Student'),
    (12, 'srogers', 'a3f804fc75309fe1f09c5f4409a9e01b5eb2b5abb3ae878c1c5166ee99b6d05c', 'Student'),
    
    -- Guests (5 unique corporations)
    (13, 'henrietta_library', '153a255160109ee7a0981b89a9e52ad6d1a2dc4f29ac318ab6121465a04b0d1c', 'Public'),
    (14, 'amazon_inc', '30ec70e85442080e1bc825fba723dc2062a4518b0c87fe74973106a6910f26e1', 'Public'),
    (15, 'shield_corp', '0e8e62cc9ac652873167dfdf0c10ce030c5793e5231ef62b8f4c739aec1a7d95', 'Public'),
    (16, 'daily_planet', '27d5233ddd8968526735078ca9efa95ec03a22ea942420df0841f78ca9d9c272', 'Public'),
    (17, 'wayne_enterprises', '2001af5627ccb59abf03716673255e9903632d9f8c6b44c9b793b177d7f0a139', 'Public');

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
    college_id    VARCHAR(10),
    office_hours  VARCHAR(255),
    calendar_link VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

-- Insert statement for faculty table sample data (5 unique faculty)
INSERT INTO Faculty (faculty_id, account_id, fname, lname, email, 
    building, office_number, cell_phone, slack, college_id, office_hours, calendar_link) 
VALUES
    (1, 1, 'John', 'Doe', 'jdoe@rit.edu', 70, 'GOL-2400', '585-555-1001', 'jdoe', 'GCCIS', '9:00 AM - 5:00 PM', 'https://calendar.rit.edu/jdoe'),
    (2, 2, 'Bruce', 'Wayne', 'bwayne@rit.edu', 70, 'GOL-2350', '585-555-1002', 'bwayne', 'GCCIS', '10:00 AM - 6:00 PM', 'https://calendar.rit.edu/bwayne'),
    (3, 3, 'Tony', 'Stark', 'tstark@rit.edu', 70, 'GOL-2310', '585-555-1003', 'tstark', 'GCCIS', '11:00 AM - 7:00 PM', 'https://calendar.rit.edu/tstark'),
    (4, 4, 'Natasha', 'Romanoff', 'nromanoff@rit.edu', 70, 'GOL-2405', '585-555-1004', 'nromanoff', 'GCCIS', '12:00 PM - 8:00 PM', 'https://calendar.rit.edu/nromanoff'),
    (5, 5, 'Sam', 'Wilson', 'swilson@rit.edu', 70, 'GOL-2420', '585-555-1005', 'swilson', 'GCCIS', '1:00 PM - 9:00 PM', 'https://calendar.rit.edu/swilson');

CREATE TABLE Student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT          NOT NULL,
    fname      VARCHAR(50)  NOT NULL,
    lname      VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20),
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

-- Insert statement for student table sample data (7 unique students)
INSERT INTO Student (student_id, account_id, fname, lname, email, phone) VALUES
    (1, 6, 'Alice', 'Smith', 'asmith@rit.edu', '585-555-1234'),
    (2, 7, 'Clark', 'Kent', 'ckent@rit.edu', '585-555-5678'),
    (3, 8, 'Peter', 'Parker', 'pparker@rit.edu', '585-555-9012'),
    (4, 9, 'Kyle', 'Brown', 'kbrown@rit.edu', '585-555-3456'),
    (5, 10, 'Diana', 'Prince', 'dprince@rit.edu', '585-555-2222'),
    (6, 11, 'Jack', 'Bauer', 'jbauer@rit.edu', '585-555-3333'),
    (7, 12, 'Steve', 'Rogers', 'srogers@rit.edu', '585-555-4444');

CREATE TABLE Guest (
    guest_id     INT PRIMARY KEY AUTO_INCREMENT,
    account_id   INT          NOT NULL,
    fname        VARCHAR(50),
    lname        VARCHAR(50),
    company_name VARCHAR(100),
    email        VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

-- Insert statement for guest table sample data (5 corporations - NO personal names)
INSERT INTO Guest (guest_id, account_id, fname, lname, company_name, email) VALUES
    (1, 13, NULL, NULL, 'Henrietta Public Library', 'contact@henriettalibrary.org'),
    (2, 14, NULL, NULL, 'Amazon Inc.', 'info@amazon.com'),
    (3, 15, NULL, NULL, 'Shield Corp.', 'contact@shield.com'),
    (4, 16, NULL, NULL, 'Daily Planet', 'news@dailyplanet.com'),
    (5, 17, NULL, NULL, 'Wayne Enterprises', 'info@wayneenterprises.com');

-- Abstract table with authors
CREATE TABLE abstract (
    abstract_id      INT PRIMARY KEY AUTO_INCREMENT,
    title            VARCHAR(200),
    authors          VARCHAR(255),
    abstract_type    ENUM('book','speaking') NOT NULL,
    abstract_content TEXT
);

-- Insert statement for abstract table sample data WITH authors
INSERT INTO abstract (abstract_id, title, authors, abstract_type, abstract_content) VALUES
    (1, 'Introduction to Database Systems', 'John Doe, Jane Smith', 'book', 
     'This book provides a comprehensive introduction to database systems, covering relational databases, SQL, and data modeling. Topics include normalization, transaction management, and database design principles.'),
    
    (2, 'Advanced Topics in Software Engineering', 'Bruce Wayne, Tony Stark', 'speaking', 
     'This speaking engagement explores advanced software engineering concepts including agile methodologies, DevOps practices, and software architecture patterns. The presentation includes case studies from industry leaders.'),
    
    (3, 'Data Science in Practice', 'Natasha Romanoff, Sam Wilson', 'book', 
     'This practical guide to data science covers data cleaning, exploratory data analysis, machine learning algorithms, and data visualization techniques. Real-world examples demonstrate how to derive insights from complex datasets.');

CREATE TABLE Faculty_Abstract (
    abstract_id INT NOT NULL,
    faculty_id  INT NOT NULL,
    PRIMARY KEY (abstract_id, faculty_id),
    FOREIGN KEY (abstract_id) REFERENCES abstract(abstract_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (faculty_id)  REFERENCES Faculty(faculty_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Insert statement for faculty_abstract table sample data
INSERT INTO Faculty_Abstract (abstract_id, faculty_id) VALUES
    (1, 1),  -- John Doe's abstract
    (2, 2),  -- Bruce Wayne's abstract
    (3, 3),  -- Tony Stark's abstract
    (1, 4),  -- Natasha Romanoff also has abstract 1
    (2, 5);  -- Sam Wilson also has abstract 2

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
    FOREIGN KEY (faculty_id)  REFERENCES Faculty(faculty_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES Interest(interest_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Insert statement for faculty_interest table sample data (EXACTLY 3 per faculty)
INSERT INTO Faculty_interest (faculty_id, interest_id) VALUES
    (1, 1), (1, 2), (1, 3),   -- John Doe: AI, ML, Data Science
    (2, 4), (2, 6), (2, 7),   -- Bruce Wayne: Cybersecurity, Cloud, IoT
    (3, 10), (3, 11), (3, 14), -- Tony Stark: AR, HCI, Robotics
    (4, 11), (4, 12), (4, 13), -- Natasha Romanoff: HCI, Game Dev, Sign Language
    (5, 15), (5, 16), (5, 17); -- Sam Wilson: Quantum, Bioinformatics, Entrepreneurship

CREATE TABLE Student_Interest (
    student_id  INT NOT NULL,
    interest_id INT NOT NULL,
    PRIMARY KEY (student_id, interest_id),
    FOREIGN KEY (student_id)  REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES Interest(interest_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Insert statement for student_interest table sample data
INSERT INTO Student_Interest (student_id, interest_id) VALUES
    (1, 1), (1, 2), (1, 3),   -- Alice Smith: AI, ML, Data Science
    (2, 4), (2, 5), (2, 6),   -- Clark Kent: Cybersecurity, Software Eng, Cloud
    (3, 7), (3, 8), (3, 9),   -- Peter Parker: IoT, Blockchain, VR
    (4, 10), (4, 11), (4, 12), -- Kyle Brown: AR, HCI, Game Dev
    (5, 13), (5, 14), (5, 15), -- Diana Prince: Sign Language, Robotics, Quantum
    (6, 16), (6, 17), (6, 18), -- Jack Bauer: Bioinformatics, Entrepreneurship, Data Viz
    (7, 19), (7, 20);          -- Steve Rogers: Edge Computing, 5G

CREATE TABLE Guest_Interest (
    guest_id    INT NOT NULL,
    interest_id INT NOT NULL,
    PRIMARY KEY (guest_id, interest_id),
    FOREIGN KEY (guest_id)    REFERENCES Guest(guest_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES Interest(interest_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Insert statement for guest_interest table sample data (1 keyword each - corporations only)
INSERT INTO Guest_Interest (guest_id, interest_id) VALUES
    (1, 1),   -- Henrietta Public Library: Artificial Intelligence
    (2, 2),   -- Amazon Inc.: Machine Learning
    (3, 3),   -- Shield Corp.: Data Science
    (4, 4),   -- Daily Planet: Cybersecurity
    (5, 5);   -- Wayne Enterprises: Software Engineering

-- Lookup Tables
CREATE TABLE CollegeName_Lookup (
    college_id   VARCHAR(10) PRIMARY KEY,
    college_name VARCHAR(100) NOT NULL
);

INSERT INTO CollegeName_Lookup (college_id, college_name) VALUES
    ('ART', 'College of Art and Design'),
    ('COE', 'College of Engineering'),
    ('ENGIN', 'College of Engineering Technology'),
    ('GCCIS', 'Golisano College of Computing and Information Sciences'),
    ('HEALTH', 'College of Health Sciences and Technology'),
    ('INDIV', 'College of Individualized Study'),
    ('LA', 'College of Liberal Arts'),
    ('NTID', 'National Technical Institute for the Deaf'),
    ('SAUND', 'Saunders College of Business'),
    ('SCI', 'College of Science'),
    ('SUST', 'College of Sustainability');

CREATE TABLE major (
    major_id   INT PRIMARY KEY AUTO_INCREMENT,
    major_name VARCHAR(64) NOT NULL
);

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
    college_id VARCHAR(10) NOT NULL,
    major_id   INT         NOT NULL,
    PRIMARY KEY (college_id, major_id),
    FOREIGN KEY (college_id) REFERENCES CollegeName_Lookup(college_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (major_id) REFERENCES major(major_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO College_Major (college_id, major_id) VALUES
    ('GCCIS', 1), ('GCCIS', 2), ('GCCIS', 3), ('GCCIS', 4), ('GCCIS', 6), ('GCCIS', 8), ('GCCIS', 9), ('GCCIS', 10),
    ('SCI', 1), ('SCI', 2), ('SCI', 3), ('SCI', 4), ('SCI', 6), ('SCI', 8), ('SCI', 9), ('SCI', 10),
    ('NTID', 8);

CREATE TABLE student_major (
    student_id INT NOT NULL,
    major_id   INT NOT NULL,
    PRIMARY KEY (student_id, major_id),
    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (major_id) REFERENCES major(major_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO student_major (student_id, major_id) VALUES
    (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7);