DROP DATABASE IF EXISTS rit_collab;
CREATE DATABASE rit_collab;

DROP TABLE IF EXISTS account;
CREATE TABLE account(
	account_id INT PRIMARY KEY,
    username VARCHAR(50),
    pass_hash CHAR(64),
    account_type ENUM('Faculty','Student','Public')
);

DROP TABLE IF EXISTS faculty;
CREATE TABLE faculty(
	faculty_id INT PRIMARY KEY,
    account_id INT,
    fname VARCHAR(50),
    lname VARCHAR(50),
    email VARCHAR(225),
    building INT,
    office_number VARCHAR(10),
    cell_phone INT,
    slack VARCHAR(50),
    college_id INT,
    office_hours VARCHAR(225),
    calendar_link VARCHAR(225),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

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

-- abstract stuff

DROP TABLE IF EXISTS faculty_abstract;
CREATE TABLE faculty_abstract(
	abstract_id INT PRIMARY KEY,
    faculty_id INT PRIMARY KEY,
    FOREIGN KEY (intrest_id) REFERENCES intrest(intrest_id),
    FOREIGN KEY (faculty_id) REFERENCES factulty(faculty_id)
);

DROP TABLE IF EXISTS abstract;
CREATE TABLE abstract(
	abstract_id INT PRIMARY KEY,
    title VARCHAR(200),
    abstract_type ENUM('book','speaking'),
    abstract_content TEXT
);

-- intrests stuff

DROP TABLE IF EXISTS faculty_intrest;
CREATE TABLE faculty_intrest(
	faculty_id INT PRIMARY KEY,
    intrest_id INT PRIMARY KEY,
	FOREIGN KEY (intrest_id) REFERENCES intrest(intrest_id),
	FOREIGN KEY (faculty_id) REFERENCES factulty(faculty_id)


);

DROP TABLE IF EXISTS guest_intrest;
CREATE TABLE guest_intrest(
	guest_id INT PRIMARY KEY,
    intrest_id INT PRIMARY KEY,
    FOREIGN KEY (guest_id) REFERENCES guest(guest_id),
	FOREIGN KEY (intrest_id) REFERENCES intrest(intrest_id)
);

DROP TABLE IF EXISTS student_intrest;
CREATE TABLE student_intrest(
	student_id INT PRIMARY KEY, 
    intrest_id INT PRIMARY KEY,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
	FOREIGN KEY (intrest_id) REFERENCES intrest(intrest_id)
);

DROP TABLE IF EXISTS intrest;
CREATE TABLE intrest(
	intrest_id INT PRIMARY KEY,
    intrest_word VARCHAR(40)
);

-- major information 

DROP TABLE IF EXISTS major;
CREATE TABLE major(
	major_id INT PRIMARY KEY,
    major_name VARCHAR(64)
);

DROP TABLE IF EXISTS college_major;
CREATE TABLE college_major(
	college_id VARCHAR(20) PRIMARY KEY,
    major_id INT PRIMARY KEY,
    FOREIGN KEY (college_id) REFERENCES collegename_lookup(college_id),
	FOREIGN KEY (major_id) REFERENCES major(major_id)
);

DROP TABLE IF EXISTS student_major;
CREATE TABLE student_major(
	studnet_id VARCHAR(20) PRIMARY KEY,
    major_id INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
	FOREIGN KEY (major_id) REFERENCES major(major_id)
);

-- look up table 

DROP TABLE IF EXISTS collegeName_lookup;
CREATE TABLE collegeName_lookup(
	college_id VARCHAR(6) PRIMARY KEY,
    college_name VARCHAR(70)
);



