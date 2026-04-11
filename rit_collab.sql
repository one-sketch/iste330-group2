DROP DATABASE IF EXISTS rit_collab;
CREATE DATABASE rit_collab;

USE rit_collab;

DROP TABLE IF EXISTS account;
CREATE TABLE account(
	account_id INT PRIMARY KEY,
    username VARCHAR(50),
    pass_hash CHAR(64),
    account_type ENUM('Faculty','Student','Public')
);

-- Insert statement for account table sample data
INSERT INTO account (account_id, username, pass_hash, account_type) VALUES
    (1, 'jdoe', 'Maryland!1998', 'Faculty'),
    (2, 'asmith', 'Georgia#05.12', 'Student'),
    (3, 'guestuser', 'NewYork_2001', 'Public'),
    (4, 'bwayne', 'Maryland*03-22', 'Faculty'),
    (5, 'ckent', 'Georgia?1995', 'Student'),
    (6, 'dprince', 'NewYork^08.30', 'Public'),
    (7, 'pparker', 'Maryland&2004', 'Student'),
    (8, 'tstark', 'Georgia~11-15', 'Faculty'),
    (9, 'srogers', 'NewYork+1990', 'Public'),
    (10, 'nromanoff', 'Maryland$12.25', 'Student'),
    (11, 'clark.kent', 'Georgia%07-04', 'Public'),
    (12, 'diana.prince', 'NewYork@09.10', 'Public'),
    (13, 'tony.stark', 'Maryland!2008', 'Student'),
    (14, 'bruce.wayne', 'Georgia#01-01', 'Student'),
    (15, 'natasha.romanoff', 'NewYork_05.05', 'Faculty'),
    (16, 'guest.user', 'Maryland*06-15', 'Public'),
    (17, 'alice.smith', 'Georgia?1999', 'Student'),
    (18, 'jack.bauer', 'NewYork^10.20', 'Student'),
    (19, 'steve.rogers', 'Maryland&1918', 'Student'),
    (20, 'sam.wilson', 'Georgia~12-31', 'Faculty');



DROP TABLE IF EXISTS faculty;
CREATE TABLE faculty(
	faculty_id INT PRIMARY KEY,
    account_id INT,
    fname VARCHAR(50),
    lname VARCHAR(50),
    email VARCHAR(225),
    building INT,
    office_number VARCHAR(10),
    cell_phone VARCHAR(20),
    slack VARCHAR(50),
    college_id INT,
    office_hours VARCHAR(225),
    calendar_link VARCHAR(225),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

-- Insert statement for faculty table sample data
INSERT INTO faculty (faculty_id, account_id, fname, lname, email, 
    building, office_number, cell_phone, slack, college_id, office_hours, calendar_link) 
VALUES
    (1, 1, 'John', 'Doe', 'jdoe@rit.edu', 1, '101', '123-456-7890', 'jdoe', 1, '9:00 AM - 5:00 PM', 'https://calendar.rit.edu/jdoe'),
    (2, 4, 'Bruce', 'Wayne', 'bwayne@rit.edu', 2, '202', '234-567-8901', 'bwayne', 2, '10:00 AM - 6:00 PM', 'https://calendar.rit.edu/bwayne'),
    (3, 8, 'Tony', 'Stark', 'tstark@rit.edu', 3, '303', '345-678-9012', 'tstark', 3, '11:00 AM - 7:00 PM', 'https://calendar.rit.edu/tstark'),
    (4, 15, 'Natasha', 'Romanoff', 'nromanoff@rit.edu', 4, '404', '456-789-0123', 'nromanoff', 4, '12:00 PM - 8:00 PM', 'https://calendar.rit.edu/nromanoff'),
    (5, 20, 'Sam', 'Wilson', 'swilson@rit.edu', 5, '505', '567-890-1234', 'swilson', 5, '1:00 PM - 9:00 PM', 'https://calendar.rit.edu/swilson');

DROP TABLE IF EXISTS student;
CREATE TABLE student(
	student_id INT PRIMARY KEY,
    account_id INT,
    fname VARCHAR(50),
    lname VARCHAR(50),
    email VARCHAR(255),
    phone VARCHAR(20),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

-- Insert statement for student table sample data
INSERT INTO student (student_id, account_id, fname, lname, email, phone) VALUES
    (1, 2, 'Alice', 'Smith', 'asmith@rit.edu', '585-555-1234'),
    (2, 5, 'Clark', 'Kent', 'ckent@rit.edu', '585-555-5678'),
    (3, 7, 'Peter', 'Parker', 'pparker@rit.edu', '585-555-9012'),
    (4, 10, 'Natasha', 'Romanoff', 'nromanoff@rit.edu', '585-555-3456'),
    (5, 13, 'Tony', 'Stark', 'tstark@rit.edu', '585-555-7890'),
    (6, 14, 'Bruce', 'Wayne', 'bwayne@rit.edu', '585-555-1111'),
    (7, 17, 'Diana', 'Prince', 'dprince@rit.edu', '585-555-2222'),
    (8, 18, 'Jack', 'Bauer', 'jbauer@rit.edu', '585-555-3333'),
    (9, 19, 'Steve', 'Rogers', 'srogers@rit.edu', '585-555-4444');


DROP TABLE IF EXISTS guest;
CREATE TABLE guest (
	guest_id INT PRIMARY KEY,
    account_id INT,
    fname VARCHAR(50),
    lname VARCHAR(50),
    company_name VARCHAR(100),
    email VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

-- Insert statement for guest table sample data
INSERT INTO guest (guest_id, account_id, fname, lname, company_name, email) VALUES
    (1, 3, 'Guest', 'User', 'N/A', 'guestuser@gmail.com'),
    (2, 6, 'D', 'Prince', 'Amazon Inc.', 'dprince@amazon.com'),
    (3, 9, 'Steve', 'Rogers', 'Shield Corp.', 'srogers@shield.com'),
    (4, 11, 'Clark', 'Kent', 'Daily Planet', 'ckent@dailyplanet.com'),
    (5, 12, 'Diana', 'Prince', 'Amazon Inc.', 'dprince@amazon.com'),
    (6, 16, 'Guest', 'User', 'N/A', 'guestuser2@gmail.com');

-- abstract stuff

DROP TABLE IF EXISTS faculty_abstract;
CREATE TABLE faculty_abstract(
	abstract_id INT PRIMARY KEY,
    faculty_id INT PRIMARY KEY,
    FOREIGN KEY (intrest_id) REFERENCES intrest(intrest_id),
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id)
);

-- Insert statement for faculty_abstract table sample data*/
INSERT INTO faculty_abstract (abstract_id, faculty_id) VALUES
    (1, 1),
    (2, 4),
    (3, 8),
    (4, 15),
    (5, 20);


DROP TABLE IF EXISTS abstract;
CREATE TABLE abstract(
	abstract_id INT PRIMARY KEY,
    title VARCHAR(200),
    abstract_type ENUM('book','speaking'),
    abstract_content TEXT
);

-- Insert statement for abstract table sample data
INSERT INTO abstract (abstract_id, title, abstract_type, abstract_content) VALUES
    (1, 'Introduction to Database Systems', 'book', 'This is a sample abstract for a book on database systems.'),
    (2, 'Advanced Topics in Software Engineering', 'speaking', 'This is a sample abstract for a speaking engagement on software engineering.'),
    (3, 'Data Science in Practice', 'book', 'This is a sample abstract for a book on data science.');

-- interests stuff

DROP TABLE IF EXISTS faculty_interest;
CREATE TABLE faculty_interest(
	faculty_id INT PRIMARY KEY,
    interest_id INT PRIMARY KEY,
	FOREIGN KEY (interest_id) REFERENCES interest(interest_id),
	FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id)
);

-- Insert statement for faculty_interest table sample data
INSERT INTO faculty_interest (faculty_id, interest_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
    (2, 6), (2, 7), (2, 8), (2, 9),
    (3, 10), (3, 11), (3, 2), (3, 13), (3, 14),
    (4, 11),(4, 12),(4, 13),(4, 14),
    (5, 15),(5, 16),(5, 17),(5, 18),(5, 19),(5, 20);

DROP TABLE IF EXISTS guest_interest;
CREATE TABLE guest_interest(
	guest_id INT PRIMARY KEY,
    interest_id INT PRIMARY KEY,
    FOREIGN KEY (guest_id) REFERENCES guest(guest_id),
	FOREIGN KEY (interest_id) REFERENCES interest(interest_id)
);

-- Insert statement for guest_interest table sample data
INSERT INTO guest_interest (guest_id, interest_id) VALUES
    (1, 1), (1, 5), (1, 10),
    (2, 2), (2, 6), (2, 11),
    (3, 3), (3, 7), (3, 12),
    (4, 4), (4, 8), (4, 13),
    (5, 5), (5, 9), (5, 14),
    (6, 1), (6, 15), (6, 20);

DROP TABLE IF EXISTS student_interest;
CREATE TABLE student_interest(
	student_id INT PRIMARY KEY, 
    interest_id INT PRIMARY KEY,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
	FOREIGN KEY (interest_id) REFERENCES interest(interest_id)
);

-- Insert statement for student_interest table sample data
INSERT INTO student_interest (student_id, interest_id) VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 4), (2, 5), (2, 6),
    (3, 7), (3, 8), (3, 9),
    (4, 10), (4, 11), (4, 12),
    (5, 13), (5, 14), (5, 15),
    (6, 16), (6, 17), (6, 18),
    (7, 19), (7, 20),
    (8, 1), (8, 20),
    (9, 2), (9, 19);

DROP TABLE IF EXISTS interest;
CREATE TABLE interest(
	interest_id INT PRIMARY KEY,
    interest_word VARCHAR(40)
);

-- Insert statement for interest table sample data
INSERT INTO interest (interest_id, interest_word) VALUES
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

-- major information 

DROP TABLE IF EXISTS major;
CREATE TABLE major(
	major_id INT PRIMARY KEY,
    major_name VARCHAR(64)
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

DROP TABLE IF EXISTS college_major;
CREATE TABLE college_major(
	college_id VARCHAR(20) PRIMARY KEY,
    major_id INT PRIMARY KEY,
    FOREIGN KEY (college_id) REFERENCES collegename_lookup(college_id),
	FOREIGN KEY (major_id) REFERENCES major(major_id)
);

-- Insert statement for college_major table sample data
INSERT INTO college_major (college_id, major_id) VALUES
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

DROP TABLE IF EXISTS student_major;
CREATE TABLE student_major(
	student_id VARCHAR(20) PRIMARY KEY,
    major_id INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
	FOREIGN KEY (major_id) REFERENCES major(major_id)
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

-- look up table 

DROP TABLE IF EXISTS collegeName_lookup;
CREATE TABLE collegeName_lookup(
	college_id VARCHAR(6) PRIMARY KEY,
    college_name VARCHAR(70)
);

-- Insert statement for collegeName_lookup table sample data
INSERT INTO collegeName_lookup (college_id, college_name) VALUES
    ('COS', 'College of Science'),
    ('GCCIS', 'Golisano College of Computing and Information Sciences'),
    ('NTID', 'National Technical Institute for the Deaf');



