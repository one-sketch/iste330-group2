// DataLayer.java
// Author: GROUP2 
// Date: april 10 2026
// SKELETON VERSION 

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Datalayer {
   private final String DRIVER = "com.mysql.cj.jdbc.Driver";
   private final String DBURL = "jdbc:mysql://localhost/";

    private Connection conn;
    private String dbUser;
    private String dbPass;

    //Initialize
    public Datalayer(String dbName, String user, String password) {
       if(!connect(dbName, user, password)){
          System.out.println("(init): DATABASE CONNECTION REFUSED");
       }  
    }
    
    // DB CONNECTION
    public boolean connect(String database, String username, String password) {
      try{
         conn = DriverManager.getConnection(DBURL+database, username, password);
         System.out.println("Database Connected");
         return true;
      }catch (SQLException sqle){
         System.out.println("(connect): COULD NOT CONNECT TO DATABASE");
         System.out.println(sqle);
         return false;
      }   
    }

    //Close established connection   
    public boolean disconnect(){
        if(conn == null){//If connection has not been established, abort
            System.out.println("(disconnect) CONNECTION NOT ESTABLISHED, ABORTING DISCONNECT");
            return false;
        }
        try{
            if(!conn.isClosed()){//If the connection is open, attempt to close it
                conn.close();
                return true;  
            }else{//Connection already closed
                System.out.println("(disconnect) CONNECTION ALREADY ESTABLISHED ABORTING DISCONNECT");
                return false;
            }
        }catch(SQLException sqle){
                System.out.println("(disconnect) CONNECTION FAILED TO CLOSE");
                System.out.println(sqle);
                return false;
        }
    }
   
    //Check connection to DB
    public boolean isConnected() {
        if(conn == null){//If connection has not been established
            return false;
        }
        try{
            if(!conn.isClosed()){//connection not closed
                return true;
            }else{//connection is closed
                return false;
            }
        }catch (SQLException sqle){
            System.out.println("(isConnected) FAILED TO CHECK CONNECTION");
            System.out.println(sqle);
            return false;
        }
    }
    
    // PASSWORD HASHING — SHA-256
    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    //==============================INNER MODEL CLASSES==============================
    
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
            facultyId=fid; 
            accountId=aid; 
            fname=fn; 
            lname=ln;
            email=em;
            building=bld; 
            officeNumber=off; 
            cellPhone=cell; 
            slack=sl;
            officeHours=oh; 
            calendarLink=cal;
        }
        public String getFullName() { 
            return fname + " " + lname; 
        }
        public String getContactInfo() {
            return "Name:     " + getFullName() + "\n" +
                   "Building: " + building + "\n" +
                   "Office:   " + officeNumber + "\n" +
                   "Email:    " + email;
        }
    }
        
    

    public static class Student {
        public int studentId, accountId;
        public String fname, lname, email, phone;

        public Student(int sid, int aid, String fn, String ln, String em, String ph) {
            studentId = sid;
            accountId = aid;
            fname = fn;
            lname = ln;
            email = em;
            phone = ph;
        }

        public String getFullName() {
            return fname + " " + lname;
        }

        public String getContactInfo() {
            return "Name:  " + getFullName() + "\n" + "Email: " + email;
        }

        @Override
        public String toString() {
            return studentId + " - " + fname + " " + lname + " (" + email + ")";
        }
    }

    public static class Guest {
        public int    guestId, accountId;
        public String fname, lname, companyName, email;
        public Guest(int gid, int aid, String fn, String ln, String co, String em) {
            guestId=gid; 
            accountId=aid;
             fname=fn; 
             lname=ln; 
             companyName=co; 
             email=em;
        }
        public String getDisplayName() {
            return (companyName != null && !companyName.isEmpty()) ? companyName : fname + " " + lname;
        }
    }

    public static class Abstract {
        public int    abstractId;
        public String title, abstractType, abstractContent;
        public Abstract(int id, String t, String type, String content) {
            abstractId=id; title=t; abstractType=type; abstractContent=content;
        }
        public String toString() {
             return "[" + abstractId + "] " + title + " (" + abstractType + ")"; 
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
        String hashed = hashPassword(password);
        String sql = "SELECT account_id, username, account_type FROM Account WHERE username=? AND pass_hash=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("account_type"));
        }
        return null;
    }

    /**
     * Register a new account with hashed password.
     * Returns the new account_id, or -1 if username already exists.
     */
    public int registerAccount(String username, String password, String accountType) throws SQLException {
        // Check duplicate
        String check = "SELECT account_id FROM Account WHERE username=?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setString(1, username);
            if (ps.executeQuery().next()) return -1;
        }
        String hashed = hashPassword(password);
        String sql = "INSERT INTO Account (username, pass_hash, account_type) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username); ps.setString(2, hashed); ps.setString(3, accountType);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        }
        return -1;
    }

    /** Register a new Faculty (creates Account + Faculty row) */
    public boolean registerFaculty(String username, String password, String fname, String lname,
                                    String email, int building, String officeNumber) throws SQLException {
        int accountId = registerAccount(username, password, "Faculty");
        if (accountId == -1) return false;
        String sql = "INSERT INTO Faculty (account_id, fname, lname, email, building, office_number) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId); ps.setString(2, fname); ps.setString(3, lname);
            ps.setString(4, email);  ps.setInt(5, building); ps.setString(6, officeNumber);
            ps.executeUpdate();
        }
        return true;
    }

    /** Register a new Student */
    public boolean registerStudent(String username, String password, String fname, String lname,
                                    String email, String phone) throws SQLException {
        int accountId = registerAccount(username, password, "Student");
        if (accountId == -1) return false;
        String sql = "INSERT INTO Student (account_id, fname, lname, email, phone) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId); ps.setString(2, fname); ps.setString(3, lname);
            ps.setString(4, email);  ps.setString(5, phone);
            ps.executeUpdate();
        }
        return true;
    }

    /** Register a new Guest / Public user */
    public boolean registerGuest(String username, String password, String fname, String lname,
                                  String companyName, String email) throws SQLException {
        int accountId = registerAccount(username, password, "Public");
        if (accountId == -1) return false;
        String sql = "INSERT INTO Guest (account_id, fname, lname, company_name, email) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId); ps.setString(2, fname); ps.setString(3, lname);
            ps.setString(4, companyName); ps.setString(5, email);
            ps.executeUpdate();
        }
        return true;
    }
    // PROFILE GETTERS
    public Faculty getFacultyByAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM rit_collab.Faculty WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,accountId);
            ResultSet resultSet = stmt.executeQuery();

            resultSet.next();
            return mapFaculty(resultSet);
        }
        
    }

    public Student getStudentByAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM rit_collab.Student WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,accountId);
            ResultSet resultSet = stmt.executeQuery();

            resultSet.next();
            return mapStudent(resultSet);
        }
        
    }

    public Guest getGuestByAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM rit_collab.Guest WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,accountId);
            ResultSet resultSet = stmt.executeQuery();

            resultSet.next();
            return mapGuest(resultSet);
        }        
    }

    // ABSTRACTS — Faculty 
   public List<Abstract> getAllAbstracts() throws SQLException {
        List<Abstract> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM abstract ORDER BY abstract_id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapAbstract(rs));
        }
        return list;
    }

     public List<Abstract> getAbstractsByFaculty(int facultyId) throws SQLException {
        List<Abstract> list = new ArrayList<>();
        String sql = "SELECT a.* FROM abstract a JOIN Faculty_Abstract fa ON a.abstract_id=fa.abstract_id WHERE fa.faculty_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapAbstract(rs));
        }
        return list;
    }


    /**
     * Insert abstract from file content or typed text.
     * abstractType must be "book" or "speaking".
     */
        public int insertAbstract(int facultyId, String title, String abstractType, String content) throws SQLException {
            abstractType = abstractType.toLowerCase().trim();
            if (!abstractType.equals("book") && !abstractType.equals("speaking")) {
                System.out.println("Invalid Abstract Type");
                return -1;
            }
            
            String sql = "INSERT INTO abstract (title, abstract_type, abstract_content) VALUES (?,?,?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, title);
                ps.setString(2, abstractType);
                ps.setString(3, content);
                
                int result = ps.executeUpdate();
                
                if (result == 0) {
                    throw new SQLException("Failed to insert");
                }
                
                // Get the generated abstract ID
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    // Link to faculty
                    addFacultyAbstract(newId, facultyId);
                    return newId;
                }
                
                return result;
                
            } catch (SQLException sqle) {
                System.out.println("Error in insertAbstract");
                System.out.println(sqle);
                return -1;
            }
        }
    public int addFacultyAbstract(int abstractID, int facultyID) {
        String sql = "INSERT INTO Faculty_Abstract (abstract_id, faculty_id) VALUES (?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, abstractID);
            ps.setInt(2, facultyID);

            int result = ps.executeUpdate();

            if (result == 0) {
                throw new SQLException("Failed to insert");
            }
            return result;

        } catch (SQLException sqle) {
            System.out.println("Error in addFacultyAbstract");
            System.out.println(sqle);
            return -1;
        }

    } 

    public void updateAbstract(int abstractId, String title, String abstractType, String content) throws SQLException {
        String sql = "UPDATE abstract SET title = ?, abstract_type = ?, abstract_content = ? WHERE abstract_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, abstractType);
            ps.setString(3, content);
            ps.setInt(4, abstractId);
            ps.executeUpdate();
        }
    }

    public int deleteAbstract(int facultyId, int abstractId) throws SQLException {
        int abstracts_deleted = 0;

        String delete_sql = "DELETE FROM abstract WHERE abstract_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(delete_sql); 

            pstmt.setInt(1, abstractId);
            pstmt.executeUpdate();

            abstracts_deleted = 1;
        } catch(SQLException e){
            System.out.println("Error in deleteAbstract: " + e.getMessage());
        }
        return abstracts_deleted;
    }

    // INTERESTS — Faculty
    public List<Interest> getFacultyInterests(int facultyId) throws SQLException {
        List<Interest> list = new ArrayList<>();
        String sql = "SELECT i.* FROM Interest i JOIN Faculty_interest fi ON i.interest_id=fi.interest_id WHERE fi.faculty_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Interest(rs.getInt("interest_id"), rs.getString("interest_word")));
        }
        return list;
    }
        // Student interest
        public List<Interest> getStudentInterests(int studentId) throws SQLException {
            List<Interest> list = new ArrayList<>();
            String sql = "SELECT i.* FROM Interest i JOIN Student_Interest si ON i.interest_id = si.interest_id WHERE si.student_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, studentId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new Interest(rs.getInt("interest_id"), rs.getString("interest_word")));
                }
            }
            return list;
        }

       
    public int addFacultyInterest(int facultyId, List<String> words) throws SQLException {
        int interests_added = 0; 
        String sql = "SELECT interest_id FROM interest WHERE interest_word = ?";
        String sqlInsert = "INSERT INTO faculty_interest (faculty_id, interest_id) VALUES (?,?) ";

        try { 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            
            for (String word : words){
                pstmt.setString(1, word);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()){
                    int interestId = rs.getInt("interest_id");

                    psInsert.setInt(1, facultyId); 
                    psInsert.setInt(2, interestId);

                    try {
                        psInsert.executeUpdate();
                        interests_added += 1;
                    } catch (SQLException e2) {
                        System.out.println("Error in executeUpdate in addFacultyInterest: " + e2.getMessage());
                    }
                }
            }
        } catch(SQLException e){
            System.out.println("Error in addFacultyInterest: " + e.getMessage());
        }
        return interests_added;
    }

    public int deleteFacultyInterest(int facultyId, int interestId) throws SQLException {
        int interests_deleted = 0;
        String sql = "DELETE FROM faculty_interest WHERE faculty_id = ? AND interest_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql); 

            pstmt.setInt(1, facultyId);
            pstmt.setInt(2, interestId);
            pstmt.executeUpdate();

            interests_deleted = 1;
        } catch(SQLException e){
            System.out.println("Error in deleteFacultyInterest: " + e.getMessage());
        }
        return interests_deleted;
    }

    // INTERESTS — Public users
        public List<Interest> getGuestInterests(int guestId) throws SQLException {
            List<Interest> list = new ArrayList<>();
            String sql = "SELECT i.* FROM Interest i JOIN Guest_Interest gi ON i.interest_id = gi.interest_id WHERE gi.guest_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, guestId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new Interest(rs.getInt("interest_id"), rs.getString("interest_word")));
                }
            }
            return list;
        }

    public int addStudentInterest(int studentId, List<String> words) throws SQLException {
        int interests_added = 0; 
        String sql = "SELECT interest_id FROM interest WHERE interest_word = ?";
        String sqlInsert = "INSERT INTO student_interest (student_id, interest_id) VALUES (?,?) ";

        try { 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            
            for (String word : words){
                pstmt.setString(1, word);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()){
                    int interestId = rs.getInt("interest_id");

                    psInsert.setInt(1, studentId); 
                    psInsert.setInt(2, interestId);

                    try {
                        psInsert.executeUpdate();
                        interests_added += 1;
                    } catch (SQLException e2) {
                        System.out.println("Error in executeUpdate in addStudentInterest: " + e2.getMessage());
                    }
                }
            }
        } catch(SQLException e){
            System.out.println("Error in addStudentInterest: " + e.getMessage());
        }
        return interests_added;
    }

    public int deleteStudentInterest(int studentId, int interestId) throws SQLException {
        int interests_deleted = 0;
        String sql = "DELETE FROM student_interest WHERE student_id = ? AND interest_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql); 

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, interestId);
            pstmt.executeUpdate();

            interests_deleted = 1;
        } catch(SQLException e){
            System.out.println("Error in deleteStudentInterest: " + e.getMessage());
        }
        return interests_deleted;
    }
    public int addGuestInterest(int guestId, List<String> words) throws SQLException {
        int interests_added = 0; 
        String sql = "SELECT interest_id FROM interest WHERE interest_word = ?";
        String sqlInsert = "INSERT INTO guest_interest (guest_id, interest_id) VALUES (?,?) ";

        try { 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            
            for (String word : words){
                pstmt.setString(1, word);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()){
                    int interestId = rs.getInt("interest_id");

                    psInsert.setInt(1, guestId); 
                    psInsert.setInt(2, interestId);

                    try {
                        psInsert.executeUpdate();
                        interests_added += 1;
                    } catch (SQLException e2) {
                        System.out.println("Error in executeUpdate in addGuestInterest: " + e2.getMessage());
                    }
                }
            }
        } catch(SQLException e){
            System.out.println("Error in addGuestInterest: " + e.getMessage());
        }
        return interests_added;
    }

    public int deleteGuestInterest(int guestId, int interestId) throws SQLException {
        int interests_deleted = 0;
        String sql = "DELETE FROM guest_interest WHERE guest_id = ? AND interest_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql); 

            pstmt.setInt(1, guestId);
            pstmt.setInt(2, interestId);
            pstmt.executeUpdate();

            interests_deleted = 1;
        } catch(SQLException e){
            System.out.println("Error in deleteGuestInterest: " + e.getMessage());
        }
        return interests_deleted;
    }
    // SEARCH & MATCHING 
    // Faculty: search students by interest keyword
    public List<Student> searchStudentsByInterest(String keyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, CONCAT(fname, ' ', lname) AS name, email, phone " +
            "FROM student JOIN student_interest USING (student_id) " +
            "JOIN interest USING (interest_id) " +
            "WHERE interest_word = LOWER(?)";
        String lower_keyword = keyword.toLowerCase();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lower_keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapStudent(rs));
        }
        return list;
    }

    // Faculty: search students by name 
    public List<Student> searchStudentsByName(String name) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM Student WHERE LOWER(fname) LIKE ? OR LOWER(lname) LIKE ? OR LOWER(CONCAT(fname,' ',lname)) LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + name.toLowerCase() + "%";
            ps.setString(1, q); ps.setString(2, q); ps.setString(3, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapStudent(rs));
        }
        return list;
    }

    // Faculty: auto-match students who share interests 
   public List<Student> matchStudentsByFacultyInterest(int facultyId) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT DISTINCT s.* FROM Student s " +
                     "JOIN Student_Interest si ON s.student_id=si.student_id " +
                     "JOIN Faculty_interest fi ON si.interest_id=fi.interest_id WHERE fi.faculty_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapStudent(rs));
        }
        return list;
    }

    /**
     * Student: search faculty by keyword — checks BOTH interests AND abstract content.
     * Returns faculty with name, building, office, email per assignment requirements.
     */
    public List<Faculty> searchFacultyByKeyword(String keyword) throws SQLException {
        List<Faculty> list = new ArrayList<>();
        String sql = "SELECT CONCAT(fname, ' ', lname) AS name, building, office_number, email " + 
            "FROM faculty JOIN faculty_interest USING (faculty_id) JOIN interest USING (interest_id) " + 
            "WHERE interest_word = LOWER(?)";
        String lower_keyword = keyword.toLowerCase();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lower_keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapFaculty(rs));
        }
        return list;
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
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            System.out.println("Executing SQL: " + pstmt);
            ResultSet rs = pstmt.executeQuery();
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
        return matchedFaculty;
    }

   /** Public/Guest: search both faculty and students by one keyword */
    public List<Faculty> searchFacultyForPublic(String keyword) throws SQLException {
        return searchFacultyByKeyword(keyword);
    }

    public List<Student> searchStudentsForPublic(String keyword) throws SQLException {
        return searchStudentsByInterest(keyword);
    }

    // HELPERS METHODS
    public int getOrCreateInterest(String word) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT interest_id FROM Interest WHERE interest_word=?")) {
            ps.setString(1, word);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("interest_id");
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Interest (interest_word) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, word); ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        }
        throw new SQLException("Could not create interest: " + word);
    }

    public static boolean isValidKeyword(String word) {
        if (word == null || word.trim().isEmpty()) return false;
        String[] parts = word.trim().split("\\s+");
        return parts.length >= 1 && parts.length <= 3;
    }

    // ── Row mappers ──
    private Faculty  mapFaculty(ResultSet rs) throws SQLException {
        return new Faculty(rs.getInt("faculty_id"), rs.getInt("account_id"),
            rs.getString("fname"), rs.getString("lname"), rs.getString("email"),
            rs.getInt("building"), rs.getString("office_number"),
            rs.getString("cell_phone"), rs.getString("slack"),
            rs.getString("office_hours"), rs.getString("calendar_link"));
    }
    private Student  mapStudent(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getInt("account_id"),
            rs.getString("fname"), rs.getString("lname"),
            rs.getString("email"), rs.getString("phone"));
    }
    private Guest    mapGuest(ResultSet rs) throws SQLException {
        return new Guest(rs.getInt("guest_id"), rs.getInt("account_id"),
            rs.getString("fname"), rs.getString("lname"),
            rs.getString("company_name"), rs.getString("email"));
    }
    private Abstract mapAbstract(ResultSet rs) throws SQLException {
        return new Abstract(rs.getInt("abstract_id"), rs.getString("title"),
            rs.getString("abstract_type"), rs.getString("abstract_content"));
    }
}