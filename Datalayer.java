// DataLayer.java
// Author: GROUP2 
// Date: april 10 2026
// SKELETON VERSION 

import java.sql.*;
//used for the sha 256 hasing
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Datalayer {

    private Connection conn;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public Datalayer(String dbName, String user, String password) {
        // TODO: Initialize connection parameters
    }

    // PASSWORD HASHING — SHA-256
    public static String hashPassword(String plainText) {
        // TODO:  do SHA-256 hashing
        return null;
    }
    // DB CONNECTION
    public boolean connect() {
        // TODO: connect database connection
        return false;
    }

    public void disconnect() {
        // TODO: close database connection
    }

    public boolean isConnected() {
        // TODO: check if connection is active
        return false;
    }

    // INNER MODEL CLASSES
    public static class Account {
        public int    accountId;
        public String username, accountType;
        
        public Account(int id, String user, String type) {
            this.accountId = id;
            this.username = user;
            this.accountType = type;
        }
    }

    public static class Faculty {
        public int    facultyId, accountId, building;
        public String fname, lname, email, officeNumber, cellPhone, slack, officeHours, calendarLink;
        
        public Faculty(int fid, int aid, String fn, String ln, String em,
                       int bld, String off, String cell, String sl, String oh, String cal) {
            // TODO: add fields
        }
        
        public String getFullName() {
            // TODO: Return full name
            return null;
        }
        
        public String getContactInfo() {
            // TODO: Return formatted contact info
            return null;
        }
    }

    public static class Student {
        public int studentId, accountId;
        public String fname, lname, email, phone;

        public Student(int sid, int aid, String fn, String ln, String em, String ph) {
            // TODO: add fields
        }

        public String getFullName() {
            // TODO: Return full name
            return null;
        }

        public String getContactInfo() {
            // TODO: Return formatted contact info
            return null;
        }

        @Override
        public String toString() {
            // TODO: Return string representation
            return null;
        }
    }

    public static class Guest {
        public int    guestId, accountId;
        public String fname, lname, companyName, email;
        
        public Guest(int gid, int aid, String fn, String ln, String co, String em) {
            // TODO: add fields
        }
        
        public String getDisplayName() {
            // TODO: Return display name
            return null;
        }
    }

    public static class Abstract {
        public int    abstractId;
        public String title, abstractType, abstractContent;
        
        public Abstract(int id, String t, String type, String content) {
            // TODO: add fields
        }
        
        public String toString() {
            // TODO: Return string representation
            return null;
        }
    }

    public static class Interest {
        public int    interestId;
        public String interestWord;
        
        public Interest(int id, String word) {
            this.interestId = id;
            this.interestWord = word;
        }
        
        public String toString() {
            return interestWord;
        }
    }

    // AUTH
    /**
     * login with sha-256 hashed password comparison.
     * returns the Account if credentials match, null otherwise.
     */
    public Account login(String username, String password) throws SQLException {
        // TODO: Implement login authentication
        return null;
    }

    /**
     * Register a new account with hashed password.
     * Returns the new account_id, or -1 if username already exists.
     */
    public int registerAccount(String username, String password, String accountType) throws SQLException {
        // TODO: Create new account
        return -1;
    }

    // Register a new Faculty (creates Account + Faculty row) 
    public boolean registerFaculty(String username, String password, String fname, String lname,
                                    String email, int building, String officeNumber) throws SQLException {
        // TODO: Register faculty member
        return false;
    }

    // Register a new Student 
    public boolean registerStudent(String username, String password, String fname, String lname,
                                    String email, String phone) throws SQLException {
        // TODO: Register student
        return false;
    }

    // Register a new Guest 
    // Public user
    public boolean registerGuest(String username, String password, String fname, String lname,
                                  String companyName, String email) throws SQLException {
        // TODO: Register guest
        return false;
    }

    // PROFILE GETTERS
    public Faculty getFacultyByAccount(int accountId) throws SQLException {
        // TODO: Retrieve faculty by account ID
        return null;
    }

    public Student getStudentByAccount(int accountId) throws SQLException {
        // TODO: Retrieve student by account ID
        return null;
    }

    public Guest getGuestByAccount(int accountId) throws SQLException {
        // TODO: Retrieve guest by account ID
        return null;
    }

    // ABSTRACTS — Faculty 
    public List<Abstract> getAllAbstracts() throws SQLException {
        // TODO: Retrieve all abstracts from all faculty
        return new ArrayList<>();
    }

    public List<Abstract> getAbstractsByFaculty(int facultyId) throws SQLException {
        // TODO: Retrieve abstracts for a specific faculty
        return new ArrayList<>();
    }

    /**
     * Insert abstract from file content or typed text.
     * abstractType must be "book" or "speaking".
     */
    public int insertAbstract(int facultyId, String title, String abstractType, String content) throws SQLException {
        // TODO: Insert new abstract
        return -1;
    }

    public void updateAbstract(int abstractId, String title, String abstractType, String content) throws SQLException {
        // TODO: Update existing abstract
    }

    public void deleteAbstract(int facultyId, int abstractId) throws SQLException {
        // TODO: Delete abstract
    }

    // INTERESTS — Faculty
    public List<Interest> getFacultyInterests(int facultyId) throws SQLException {
        // TODO: Retrieve faculty interests
        return new ArrayList<>();
    }

    public void setFacultyInterests(int facultyId, List<String> words) throws SQLException {
        // TODO: Set/replace faculty interests
    }

    public void deleteFacultyInterest(int facultyId, int interestId) throws SQLException {
        // TODO: Delete specific faculty interest
    }

    // INTERESTS — Student
    public List<Interest> getStudentInterests(int studentId) throws SQLException {
        // TODO: Retrieve student interests
        return new ArrayList<>();
    }

    public void setStudentInterests(int studentId, List<String> words) throws SQLException {
        // TODO: Set/replace student interests
    }

    public void deleteStudentInterest(int studentId, int interestId) throws SQLException {
        // TODO: Delete specific student interest
    }

    // SEARCH & MATCHING 
    // Faculty: search students by interest keyword
    public List<Student> searchStudentsByInterest(String keyword) throws SQLException {
        // TODO: Search students by interest
        return new ArrayList<>();
    }

    // Faculty: search students by name 
    public List<Student> searchStudentsByName(String name) throws SQLException {
        // TODO: Search students by name
        return new ArrayList<>();
    }

    // Faculty: auto-match students who share interests 
    public List<Student> matchStudentsByFacultyInterest(int facultyId) throws SQLException {
        // TODO: Match students with faculty interests
        return new ArrayList<>();
    }

    /**
     * Student: search faculty by keyword — checks BOTH interests AND abstract content.
     * Returns faculty with name, building, office, email per assignment requirements.
     */
    public List<Faculty> searchFacultyByKeyword(String keyword) throws SQLException {
        // TODO: Search faculty by keyword in interests and abstracts
        return new ArrayList<>();
    }

    // Student: auto-match faculty who share interests 
    public List<Faculty> matchFacultyByStudentInterest(int studentId) throws SQLException {
        List<Faculty> matchedFaculty = new ArrayList<>();

        String sql = " SELECT DISTINCT faculty.fname, faculty.lname, faculty.building, faculty.office_number, faculty.email" + 
            " FROM faculty" + 
            " JOIN faculty_interest USING (faculty_id)" + 
            " JOIN student_interest USING (interest_id)" + 
            " WHERE student_interest.student_id = ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            System.out.println("Executing SQL: " + stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int building = rs.getInt("building");
                String office = rs.getString("office_number");
                String email = rs.getString("email");
                matchedFaculty.add(new Faculty(-1, -1, fname, lname, email, building, office, null, null, null, null));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Public/Guest: search both faculty and students by one keyword 
    public List<Faculty> searchFacultyForPublic(String keyword) throws SQLException {
        // TODO: Search faculty for guest users
        return new ArrayList<>();
    }

    public List<Student> searchStudentsForPublic(String keyword) throws SQLException {
        // TODO: Search students for guest users
        return new ArrayList<>();
    }

    // HELPERS METHODS
    public int getOrCreateInterest(String word) throws SQLException {
        // TODO: Get existing interest ID or create new interest
        return -1;
    }

    // validate that a keyword is 1-3 words 
    public static boolean isValidKeyword(String word) {
        // TODO: Validate keyword length
        return false;
    }

  // Row mappers: convert SQL ResultSet rows into Java objects (Faculty, Student, Guest, Abstract)
    private Faculty mapFaculty(ResultSet rs) throws SQLException {
        // TODO: Map ResultSet to Faculty object
        return null;
    }
    
    private Student mapStudent(ResultSet rs) throws SQLException {
        // TODO: Map ResultSet to Student object
        return null;
    }
    
    private Guest mapGuest(ResultSet rs) throws SQLException {
        // TODO: Map ResultSet to Guest object
        return null;
    }
    
    private Abstract mapAbstract(ResultSet rs) throws SQLException {
        // TODO: Map ResultSet to Abstract object
        return null;
    }
}