// PresentationLayer.java
// Authors: GROUP 2
// Kylie Higgins
// Kaylie Chiodi
// Justen Jiang
// Alex Guan
// Jordan Ciferni
// Chris Donalds
 
// Date: April 15, 2026

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class PresentationLayer {


    private static Datalayer db;
    private static Datalayer.Account account;
    private static Datalayer.Faculty faculty;
    private static Datalayer.Student student;
    private static Datalayer.Guest guest;


    public static void main(String[] args) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField userField = new JTextField("root", 15);
        JPasswordField passField = new JPasswordField(15);
        panel.add(new JLabel("DB Username:")); panel.add(userField);
        panel.add(new JLabel("DB Password:")); panel.add(passField);


        if (JOptionPane.showConfirmDialog(null, panel, "Database Connection",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) System.exit(0);


        db = new Datalayer("rit_collab", userField.getText().trim(), new String(passField.getPassword()));
        if (!db.isConnected()) { error("Connection failed!"); System.exit(0); }
        info("Connected to database!");
        showMainMenu();
    }


    //  HELPERS 


    private static void info(String msg)  { JOptionPane.showMessageDialog(null, msg, "Info",    JOptionPane.INFORMATION_MESSAGE); }
    private static void error(String msg) { JOptionPane.showMessageDialog(null, msg, "Error",   JOptionPane.ERROR_MESSAGE); }
    private static void success(String msg){ JOptionPane.showMessageDialog(null, msg, "Success", JOptionPane.INFORMATION_MESSAGE); }


    private static int showVerticalMenu(String title, String message, String[] options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lbl = new JLabel(message);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));


        ButtonGroup bg = new ButtonGroup();
        JRadioButton[] btns = new JRadioButton[options.length];
        for (int i = 0; i < options.length; i++) {
            btns[i] = new JRadioButton(options[i]);
            btns[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            bg.add(btns[i]); panel.add(btns[i]);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        btns[0].setSelected(true);


        JButton ok = new JButton("OK"), cancel = new JButton("Cancel");
        JPanel btnRow = new JPanel(); btnRow.setLayout(new BoxLayout(btnRow, BoxLayout.X_AXIS));
        btnRow.add(ok); btnRow.add(Box.createRigidArea(new Dimension(10, 0))); btnRow.add(cancel);
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); panel.add(btnRow);


        final int[] sel = {-1};
        ok.addActionListener(e -> {
            for (int i = 0; i < btns.length; i++) if (btns[i].isSelected()) { sel[0] = i; break; }
            SwingUtilities.getWindowAncestor(panel).dispose();
        });
        cancel.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());


        JOptionPane op = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{});
        JDialog dlg = op.createDialog(title);
        dlg.setModal(true); dlg.setVisible(true);
        return sel[0];
    }


    private static void showScrollableText(String title, String text) {
        JTextArea ta = new JTextArea(text, 20, 60);
        ta.setEditable(false); ta.setFont(new Font("Monospaced", Font.PLAIN, 12)); ta.setCaretPosition(0);
        JOptionPane.showMessageDialog(null, new JScrollPane(ta), title, JOptionPane.PLAIN_MESSAGE);
    }


    /** Generic interest-list display for any user type. */
    private static <T> void viewInterests(List<T> interests, Function<T, String> word) {
        if (interests.isEmpty()) { info("No interests found."); return; }
        StringBuilder sb = new StringBuilder("YOUR INTERESTS\n\n");
        for (int i = 0; i < interests.size(); i++) sb.append((i+1)).append(". ").append(word.apply(interests.get(i))).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString(), "My Interests", JOptionPane.INFORMATION_MESSAGE);
    }


    /** Format a list of interests as comma-separated string. */
    private static <T> String joinInterests(List<T> list, Function<T,String> word) {
        return list.stream().map(word).collect(Collectors.joining(", "));
    }


    //  MAIN MENU 


    private static void showMainMenu() {
        String[] opts = {"Login", "Register", "Exit"};
        while (true) {
            int c = showVerticalMenu("Main Menu", "RIT Collaboration System", opts);
            if (c == 0) login(); else if (c == 1) register(); else break;
        }
        db.disconnect();
    }


    //  LOGIN 


    private static void login() {
        JPanel p = new JPanel(new GridLayout(0, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField uf = new JTextField(15); JPasswordField pf = new JPasswordField(15);
        p.add(new JLabel("Username:")); p.add(uf); p.add(new JLabel("Password:")); p.add(pf);
        if (JOptionPane.showConfirmDialog(null, p, "Login", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            account = db.login(uf.getText().trim(), new String(pf.getPassword()));
            if (account == null) { error("Invalid login!"); return; }
            switch (account.accountType) {
                case "Faculty": faculty = db.getFacultyByAccount(account.accountId);
                    if (faculty == null) { error("Faculty profile not found!"); return; } showFacultyMenu(); break;
                case "Student": student = db.getStudentByAccount(account.accountId);
                    if (student == null) { error("Student profile not found!"); return; } showStudentMenu(); break;
                default: guest = db.getGuestByAccount(account.accountId); showGuestMenu();
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    //  REGISTER 


    private static void register() {
        String[] types = {"Faculty", "Student", "Guest"};
        int tc = showVerticalMenu("Register", "Select account type:", types);
        if (tc < 0) return;
        JPanel p = new JPanel(new GridLayout(0, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField uf = new JTextField(15), fn = new JTextField(15), ln = new JTextField(15), em = new JTextField(15);
        JPasswordField pf = new JPasswordField(15);
        p.add(new JLabel("Username:")); p.add(uf);
        p.add(new JLabel("Password:")); p.add(pf);
        p.add(new JLabel("First Name:")); p.add(fn);
        p.add(new JLabel("Last Name:")); p.add(ln);
        p.add(new JLabel("Email:")); p.add(em);


        try {
            boolean ok = false;
            String title = "Register " + types[tc];
            if (tc == 0) {
                JTextField bld = new JTextField(15), off = new JTextField(15);
                p.add(new JLabel("Building:")); p.add(bld); p.add(new JLabel("Office:")); p.add(off);
                if (JOptionPane.showConfirmDialog(null, p, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                    ok = db.registerFaculty(uf.getText().trim(), new String(pf.getPassword()), fn.getText().trim(), ln.getText().trim(), em.getText().trim(), Integer.parseInt(bld.getText().trim()), off.getText().trim());
            } else if (tc == 1) {
                JTextField ph = new JTextField(15);
                p.add(new JLabel("Phone:")); p.add(ph);
                if (JOptionPane.showConfirmDialog(null, p, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                    ok = db.registerStudent(uf.getText().trim(), new String(pf.getPassword()), fn.getText().trim(), ln.getText().trim(), em.getText().trim(), ph.getText().trim());
            } else {
                JTextField co = new JTextField(15);
                p.add(new JLabel("Company:")); p.add(co);
                if (JOptionPane.showConfirmDialog(null, p, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                    ok = db.registerGuest(uf.getText().trim(), new String(pf.getPassword()), fn.getText().trim(), ln.getText().trim(), co.getText().trim(), em.getText().trim());
            }
            JOptionPane.showMessageDialog(null, ok ? "Registered!" : "Username exists!", "Result",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    //  SHARED ABSTRACT VIEWS 
    private static void seeAllAbstracts() {
    try {
        var list = db.getAllAbstracts();
        if (list.isEmpty()) { info("No abstracts found."); return; }
        StringBuilder sb = new StringBuilder("ALL FACULTY ABSTRACTS\n\n");
        for (var a : list) {
            sb.append("Title: ").append(a.title).append("\n");
            sb.append("Authors: ").append(a.authors).append("\n");  // ADD THIS LINE
            sb.append("Type: ").append(a.abstractType).append("\n");
            sb.append("Content:\n").append(a.abstractContent).append("\n");
            sb.append("---\n\n");
        }
        showScrollableText("All Abstracts", sb.toString());
    } catch (Exception e) { error("Error: " + e.getMessage()); }
}


    //  FACULTY MENU 
    private static void showFacultyMenu() {
        String[] opts = {"See All Faculty Abstracts","View My Abstracts","View My Interests","Add Abstract","Update Abstract","Delete Abstract","Update Interest","Delete Interest","Search Student By Name","Search by Interest/Abstract","Match by Interest","Logout"};
        while (true) {
            int c = showVerticalMenu("Faculty Menu", "Welcome " + faculty.getFullName(), opts);
            if      (c == 0)  seeAllAbstracts();
            else if (c == 1)  viewMyAbstracts();
            else if (c == 2)  viewMyInterests();
            else if (c == 3)  addAbstract();
            else if (c == 4)  updateAbstract();
            else if (c == 5)  deleteAbstract();
            else if (c == 6)  updateFacultyInterest();
            else if (c == 7)  deleteFacultyInterest();
            else if (c == 8)  searchStudentByName();
            else if (c == 9)  searchByInterestOrAbstract();
            else if (c == 10) matchByInterest();
            else break;
        }
    }


        private static void viewMyAbstracts() {
        try {
            var list = db.getAbstractsByFaculty(faculty.facultyId);
            if (list.isEmpty()) { info("No abstracts found."); return; }
            StringBuilder sb = new StringBuilder("YOUR ABSTRACTS\n\n");
            for (var a : list) {
                sb.append("Title: ").append(a.title).append("\n");
                sb.append("Authors: ").append(a.authors).append("\n");  // ADD THIS LINE
                sb.append("Type: ").append(a.abstractType).append("\n");
                sb.append("Content:\n").append(a.abstractContent).append("\n");
                sb.append("---\n\n");
            }
            showScrollableText("My Abstracts", sb.toString());
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void viewMyInterests() {
        try {
            var interests = db.getFacultyInterests(faculty.facultyId);
            viewInterests(interests, i -> i.interestWord);
            int count = interests.size();
            if (count < 3) {
                info("Faculty must have exactly 3 interests. You currently have " + count + ". Please add " + (3 - count) + " more.");
            } else if (count > 3) {
                info("Faculty must have exactly 3 interests. You currently have " + count + ". Please delete " + (count - 3) + ".");
            } else {
                info("Perfect! You have exactly 3 interests as required.");
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


        private static void addAbstract() {
            String[] inputMethods = {"Load from .txt file", "Type / paste manually"};
            int method = JOptionPane.showOptionDialog(null, "How do you want to provide the abstract text?", "Add Abstract",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, inputMethods, inputMethods[0]);
            if (method < 0) return;

            // --- Build shared panel ---
            JPanel p = new JPanel(new GridBagLayout());
            p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 3, 3, 3);
            gbc.anchor = GridBagConstraints.WEST;

            JTextField tf   = new JTextField(15);
            JTextField auth = new JTextField(15);
            JComboBox<String> type = new JComboBox<>(new String[]{"book", "speaking"});

            String[][] labelRows = {{"Title:", "Type:", "Authors:"}};
            Component[] fields   = {tf, type, auth};
            for (int i = 0; i < fields.length; i++) {
                gbc.gridx = 0; gbc.gridy = i; p.add(new JLabel(labelRows[0][i]), gbc);
                gbc.gridx = 1;                 p.add(fields[i], gbc);
            }

            JLabel hint = new JLabel("(comma separated: John Doe, Jane Smith)");
            hint.setFont(new Font("Arial", Font.ITALIC, 10));
            hint.setForeground(Color.GRAY);
            gbc.gridx = 1; gbc.gridy = 3;
            p.add(hint, gbc);

            // --- Method-specific input widget ---
            JTextField  pathField = (method == 0) ? new JTextField(15) : null;
            JTextArea   ta        = (method == 1) ? new JTextArea(5, 30) : null;

            gbc.gridx = 0; gbc.gridy = 4;
            if (method == 0) {
                p.add(new JLabel("File Path:"), gbc);
                gbc.gridx = 1; p.add(pathField, gbc);
            } else {
                ta.setWrapStyleWord(true); ta.setLineWrap(true);
                ta.setFont(new Font("Monospaced", Font.PLAIN, 11));
                JScrollPane sp = new JScrollPane(ta);
                sp.setPreferredSize(new Dimension(300, 100));
                p.add(new JLabel("Abstract Text:"), gbc);
                gbc.gridx = 1; p.add(sp, gbc);
            }

            if (JOptionPane.showConfirmDialog(null, p, "Add Abstract", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
            if (tf.getText().trim().isEmpty()) { error("Title required!"); return; }

            // --- Resolve content (unified) ---
            String content;
            try {
                content = (method == 0)
                    ? new String(Files.readAllBytes(Paths.get(pathField.getText().trim())))
                    : ta.getText();
            } catch (IOException e) { error("File not found!"); return; }

            if (content.trim().isEmpty()) { error("Abstract content cannot be empty!"); return; }
            if (!auth.getText().trim().isEmpty()) content = "Authors: " + auth.getText().trim() + "\n\n" + content;

            // --- Insert (unified) ---
            try {
                // In addAbstract() method, when calling insertAbstract:
                int id = db.insertAbstract(faculty.facultyId, tf.getText().trim(), auth.getText().trim(),  
                 (String)type.getSelectedItem(), content);
                success("Abstract added! ID: " + id);
            } catch (Exception e) { error("Error: " + e.getMessage()); }
        }


            private static void updateAbstract() {
            try {
                var list = db.getAbstractsByFaculty(faculty.facultyId);
                if (list.isEmpty()) { info("No abstracts to update."); return; }

                String[] titles = list.stream().map(a -> a.title).toArray(String[]::new);
                String sel = (String) JOptionPane.showInputDialog(null, "Select abstract to update:",
                    "Update Abstract", JOptionPane.PLAIN_MESSAGE, null, titles, titles[0]);
                if (sel == null) return;

                var abs = list.stream().filter(a -> a.title.equals(sel)).findFirst().get();

                // --- Build panel ---
                JPanel p = new JPanel(new GridBagLayout());
                p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(3, 3, 3, 3);
                gbc.anchor = GridBagConstraints.WEST;

                JTextField tf = new JTextField(abs.title, 15);
                JComboBox<String> typeBox = new JComboBox<>(new String[]{"book", "speaking"});
                typeBox.setSelectedItem(abs.abstractType);

                JTextArea ca = new JTextArea(abs.abstractContent, 5, 30);
                ca.setWrapStyleWord(true); ca.setLineWrap(true);
                ca.setFont(new Font("Monospaced", Font.PLAIN, 11));
                JScrollPane sp = new JScrollPane(ca);
                sp.setPreferredSize(new Dimension(300, 100));

                String[] labels = {"Title:", "Type:", "Content:"};
                Component[] fields = {tf, typeBox, sp};
                for (int i = 0; i < labels.length; i++) {
                    gbc.gridx = 0; gbc.gridy = i; p.add(new JLabel(labels[i]), gbc);
                    gbc.gridx = 1;               p.add(fields[i], gbc);
                }

                if (JOptionPane.showConfirmDialog(null, p, "Update Abstract", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    db.updateAbstract(abs.abstractId, tf.getText().trim(), (String) typeBox.getSelectedItem(), ca.getText());
                    success("Abstract updated!");
                }
            } catch (Exception e) { error("Error: " + e.getMessage()); }
        }

    private static void deleteAbstract() {
        try {
            var list = db.getAbstractsByFaculty(faculty.facultyId);
            if (list.isEmpty()) { info("No abstracts to delete."); return; }
            String[] titles = list.stream().map(a -> a.title).toArray(String[]::new);
            String sel = (String) JOptionPane.showInputDialog(null, "Select abstract to DELETE:", "Delete Abstract", JOptionPane.PLAIN_MESSAGE, null, titles, titles[0]);
            if (sel == null) return;
            var abs = list.stream().filter(a -> a.title.equals(sel)).findFirst().get();
            if (JOptionPane.showConfirmDialog(null, "Delete \"" + abs.title + "\"?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                db.deleteAbstract(faculty.facultyId, abs.abstractId); success("Abstract deleted!");
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


private static void updateFacultyInterest() {
    try {
        var current = db.getFacultyInterests(faculty.facultyId);
        int currentCount = current.size();
        List<String> words = current.stream().map(i -> i.interestWord).collect(Collectors.toList());
        
        String[] opts = {"Add New Interest", "Delete an Interest", "Cancel"};
        int choice = showVerticalMenu("Update Interest", "You have " + currentCount + "/3 interests. Faculty must have exactly 3.", opts);
        
        if (choice == 0) { // Add
            if (currentCount >= 3) {
                error("Faculty already has 3 interests! Delete one first.");
                return;
            }
            String newInterest = JOptionPane.showInputDialog(null, "Enter new interest (1-3 words):", "Add Interest", JOptionPane.QUESTION_MESSAGE);
            if (newInterest != null && !newInterest.trim().isEmpty()) {
                words.add(newInterest.trim());
                // Delete all existing first, then add the new list
                deleteAllFacultyInterests(faculty.facultyId);
                for (String word : words) {
                    List<String> single = new ArrayList<>();
                    single.add(word);
                    db.addFacultyInterest(faculty.facultyId, single);
                }
                success("Interest added! You now have " + words.size() + "/3 interests.");
                if (words.size() == 3) info("Perfect! You now have exactly 3 interests as required.");
            }
        } else if (choice == 1 && !current.isEmpty()) { // Delete
            String[] arr = words.toArray(new String[0]);
            String sel = (String) JOptionPane.showInputDialog(null, "Select interest to DELETE:", "Delete Interest", JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
            if (sel != null) {
                if (JOptionPane.showConfirmDialog(null, "Delete \"" + sel + "\"?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    words.remove(sel);
                    // Delete all existing first, then add the new list
                    deleteAllFacultyInterests(faculty.facultyId);
                    for (String word : words) {
                        List<String> single = new ArrayList<>();
                        single.add(word);
                        db.addFacultyInterest(faculty.facultyId, single);
                    }
                    success("Interest deleted! You now have " + words.size() + "/3 interests.");
                }
            }
        }
    } catch (Exception e) { error("Error: " + e.getMessage()); }
}

// Helper method to delete all faculty interests
private static void deleteAllFacultyInterests(int facultyId) throws Exception {
    var interests = db.getFacultyInterests(facultyId);
    for (var interest : interests) {
        db.deleteFacultyInterest(facultyId, interest.interestId);
    }
}

    private static void deleteFacultyInterest() {
        try {
            var interests = db.getFacultyInterests(faculty.facultyId);
            if (interests.isEmpty()) { info("No interests to delete."); return; }
            String[] arr = interests.stream().map(i -> i.interestWord).toArray(String[]::new);
            String sel = (String) JOptionPane.showInputDialog(null, "Select interest to DELETE:", "Delete Interest", JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
            if (sel == null) return;
            Optional<Datalayer.Interest> found = interests.stream().filter(i -> i.interestWord.equals(sel)).findFirst();
            if (!found.isPresent()) return;
            if (JOptionPane.showConfirmDialog(null, "Delete \"" + sel + "\"?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                db.deleteFacultyInterest(faculty.facultyId, found.get().interestId);
                int remaining = db.getFacultyInterests(faculty.facultyId).size();
                success("Interest deleted! You now have " + remaining + "/3 interests.");
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void searchStudentByName() {
        String name = JOptionPane.showInputDialog(null, "Enter student name:", "Search Student", JOptionPane.QUESTION_MESSAGE);
        if (name == null) return;
        try {
            var list = db.searchStudentsByName(name);
            if (list.isEmpty()) { info("No students found."); return; }
            StringBuilder sb = new StringBuilder("STUDENTS FOUND\n\n");
            list.forEach(s -> sb.append("Name: ").append(s.getFullName()).append("\nEmail: ").append(s.email).append("\nPhone: ").append(s.phone).append("\n---\n"));
            JOptionPane.showMessageDialog(null, sb.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void searchByInterestOrAbstract() {
        String kw = JOptionPane.showInputDialog(null, "Enter keyword:", "Search by Interest/Abstract", JOptionPane.QUESTION_MESSAGE);
        if (kw == null) return;
        try { showStudentResults("STUDENTS MATCHING '" + kw + "'", db.searchStudentsByInterest(kw), true); }
        catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void matchByInterest() {
        try { showStudentResults("STUDENTS MATCHING YOUR INTERESTS", db.matchStudentsByFacultyInterest(faculty.facultyId), true); }
        catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    //  STUDENT MENU 
    private static void showStudentMenu() {
        String[] opts = {"See All Faculty Abstracts","View My Interests","Update Interest","Delete Interest","Search Student By Name","Search All Interests","Search by Interest/Abstract","Match Faculty by Interest","Logout"};
        while (true) {
            int c = showVerticalMenu("Student Menu", "Welcome " + student.getFullName(), opts);
            if      (c == 0) seeAllAbstracts();
            else if (c == 1) { try { viewInterests(db.getStudentInterests(student.studentId), i -> i.interestWord); } catch (Exception e) { error(e.getMessage()); } }
            else if (c == 2) updateStudentInterest();
            else if (c == 3) deleteStudentInterest();
            else if (c == 4) searchStudentByName();
            else if (c == 5) info("Feature: View all available interests in the system.");
            else if (c == 6) studentSearchByKeyword();
            else if (c == 7) matchFacultyByInterest();
            else break;
        }
    }


    private static void updateStudentInterest() {
    try {
        var current = db.getStudentInterests(student.studentId);
        List<String> words = current.stream().map(i -> i.interestWord).collect(Collectors.toList());
        
        String[] opts = {"Add New Interest", "Select Existing to Update", "Cancel"};
        int choice = showVerticalMenu("Update Interest", "You have " + words.size() + " interests", opts);
        
        if (choice == 0) {
            String newInterest = JOptionPane.showInputDialog(null, "Enter new interest (1-3 words):", "Add Interest", JOptionPane.QUESTION_MESSAGE);
            if (newInterest != null && !newInterest.trim().isEmpty()) {
                words.add(newInterest.trim());
                // Delete all existing first, then add the new list
                deleteAllStudentInterests(student.studentId);
                for (String word : words) {
                    List<String> single = new ArrayList<>();
                    single.add(word);
                    db.addStudentInterest(student.studentId, single);
                }
                success("Interest added!");
            }
        } else if (choice == 1 && !current.isEmpty()) {
            String[] arr = words.toArray(new String[0]);
            String sel = (String) JOptionPane.showInputDialog(null, "Select interest to update:", "Update Interest", JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
            if (sel != null) {
                String nw = JOptionPane.showInputDialog(null, "Enter new interest word:", "Update Interest", JOptionPane.QUESTION_MESSAGE);
                if (nw != null && !nw.trim().isEmpty()) {
                    int index = words.indexOf(sel);
                    words.set(index, nw.trim());
                    // Delete all existing first, then add the new list
                    deleteAllStudentInterests(student.studentId);
                    for (String word : words) {
                        List<String> single = new ArrayList<>();
                        single.add(word);
                        db.addStudentInterest(student.studentId, single);
                    }
                    success("Interest updated!");
                }
            }
        }
    } catch (Exception e) { error("Error: " + e.getMessage()); }
}

private static void deleteAllStudentInterests(int studentId) throws Exception {
    var interests = db.getStudentInterests(studentId);
    for (var interest : interests) {
        db.deleteStudentInterest(studentId, interest.interestId);
    }
}


    private static void deleteStudentInterest() {
        try {
            var interests = db.getStudentInterests(student.studentId);
            if (interests.isEmpty()) { info("No interests to delete."); return; }
            String[] arr = interests.stream().map(i -> i.interestWord).toArray(String[]::new);
            String sel = (String) JOptionPane.showInputDialog(null, "Select interest to DELETE:", "Delete Interest", JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
            if (sel == null) return;
            Optional<Datalayer.Interest> found = interests.stream().filter(i -> i.interestWord.equals(sel)).findFirst();
            if (!found.isPresent()) return;
            if (JOptionPane.showConfirmDialog(null, "Delete \"" + sel + "\"?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                db.deleteStudentInterest(student.studentId, found.get().interestId);
                success("Interest deleted!");
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void studentSearchByKeyword() {
        String kw = JOptionPane.showInputDialog(null, "Enter keyword to search faculty:", "Search by Interest/Abstract", JOptionPane.QUESTION_MESSAGE);
        if (kw == null) return;
        try { showFacultyResults("FACULTY MATCHING '" + kw + "'", db.searchFacultyByKeyword(kw)); }
        catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void matchFacultyByInterest() {
        try { showFacultyResults("FACULTY MATCHING YOUR INTERESTS", db.matchFacultyByStudentInterest(student.studentId)); }
        catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    //  GUEST MENU 
    private static void showGuestMenu() {
        String[] opts = {"See All Faculty Abstracts","View My Interests","Update Interest","Delete Interest","Search by Interest/Abstract","Match Faculty by Interest","Match Student by Interest","Logout"};
        while (true) {
            int c = showVerticalMenu("Guest Menu", "Welcome " + guest.getDisplayName(), opts);
            if      (c == 0) seeAllAbstracts();
            else if (c == 1) viewGuestInterests();
            else if (c == 2) updateGuestInterest();
            else if (c == 3) deleteGuestInterest();
            else if (c == 4) guestSearchByKeyword();
            else if (c == 5) guestMatchFaculty();
            else if (c == 6) guestMatchStudent();
            else break;
        }
    }


    private static void viewGuestInterests() {
        try {
            var interests = db.getGuestInterests(guest.guestId);
            viewInterests(interests, i -> i.interestWord);
            int count = interests.size();
            if (count == 0) {
                info("Guests must have exactly 1 keyword. Please add one.");
            } else if (count == 1) {
                info("Perfect! You have exactly 1 keyword as required.");
            } else {
                info("Guests must have exactly 1 keyword. You currently have " + count + ". Please delete " + (count - 1) + ".");
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    // GUEST: Only 1 keyword allowed
    private static void updateGuestInterest() {
    try {
        var current = db.getGuestInterests(guest.guestId);
        int currentCount = current.size();
        
        if (currentCount >= 1) {
            error("Guests can only have 1 keyword. Delete existing before adding new.");
            return;
        }
        
        String newInterest = JOptionPane.showInputDialog(null, "Enter your keyword (1-3 words):", "Add Keyword", JOptionPane.QUESTION_MESSAGE);
        if (newInterest != null && !newInterest.trim().isEmpty()) {
            List<String> words = new ArrayList<>();
            words.add(newInterest.trim());
            // Delete all existing first, then add the new list
            deleteAllGuestInterests(guest.guestId);
            for (String word : words) {
                List<String> single = new ArrayList<>();
                single.add(word);
                db.addGuestInterest(guest.guestId, single);
            }
            success("Keyword added! You now have 1/1 keyword.");
        }
    } catch (Exception e) { error("Error: " + e.getMessage()); }
}

private static void deleteAllGuestInterests(int guestId) throws Exception {
    var interests = db.getGuestInterests(guestId);
    for (var interest : interests) {
        db.deleteGuestInterest(guestId, interest.interestId);
    }
}

    private static void deleteGuestInterest() {
        try {
            var interests = db.getGuestInterests(guest.guestId);
            if (interests.isEmpty()) { info("No keyword to delete."); return; }
            String[] arr = interests.stream().map(i -> i.interestWord).toArray(String[]::new);
            String sel = (String) JOptionPane.showInputDialog(null, "Select keyword to DELETE:", "Delete Keyword", JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
            if (sel == null) return;
            if (JOptionPane.showConfirmDialog(null, "Delete \"" + sel + "\"?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                db.deleteGuestInterest(guest.guestId, interests.get(0).interestId);
                success("Keyword deleted!");
            }
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void guestSearchByKeyword() {
        String kw = JOptionPane.showInputDialog(null, "Enter keyword to search:", "Search by Interest/Abstract", JOptionPane.QUESTION_MESSAGE);
        if (kw == null) return;
        try {
            StringBuilder sb = new StringBuilder("SEARCH RESULTS FOR: \"" + kw + "\"\n\n=== FACULTY RESULTS ===\n");
            var fac = db.searchFacultyByKeyword(kw);
            if (fac.isEmpty()) sb.append("No faculty found.\n");
            else fac.forEach(f -> appendFaculty(sb, f));
            sb.append("\n=== STUDENT RESULTS ===\n");
            var stu = db.searchStudentsByInterest(kw);
            if (stu.isEmpty()) sb.append("No students found.\n");
            else stu.forEach(s -> appendStudent(sb, s, true));
            showScrollableText("Search Results", sb.toString());
        } catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void guestMatchFaculty() {
        String kw = JOptionPane.showInputDialog(null, "Enter interest to match faculty:", "Match Faculty", JOptionPane.QUESTION_MESSAGE);
        if (kw == null) return;
        try { showFacultyResults("FACULTY MATCHING INTEREST: \"" + kw + "\"", db.searchFacultyByKeyword(kw)); }
        catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    private static void guestMatchStudent() {
        String kw = JOptionPane.showInputDialog(null, "Enter interest to match students:", "Match Student", JOptionPane.QUESTION_MESSAGE);
        if (kw == null) return;
        try { showStudentResults("STUDENTS MATCHING INTEREST: \"" + kw + "\"", db.searchStudentsByInterest(kw), true); }
        catch (Exception e) { error("Error: " + e.getMessage()); }
    }


    //  DISPLAY HELPERS 


    private static void appendFaculty(StringBuilder sb, Datalayer.Faculty f) {
        sb.append("Name: ").append(f.getFullName()).append("\nBuilding: ").append(f.building)
          .append("\nOffice: ").append(f.officeNumber).append("\nEmail: ").append(f.email).append("\n");
        try {
            var interests = db.getFacultyInterests(f.facultyId);
            if (!interests.isEmpty()) sb.append("Interests: ").append(joinInterests(interests, i -> i.interestWord)).append("\n");
        } catch (Exception ignored) {}
        sb.append("---\n");
    }


    private static void appendStudent(StringBuilder sb, Datalayer.Student s, boolean showInterests) {
        sb.append("Name: ").append(s.getFullName()).append("\nEmail: ").append(s.email).append("\nPhone: ").append(s.phone).append("\n");
        if (showInterests) try {
            var interests = db.getStudentInterests(s.studentId);
            if (!interests.isEmpty()) sb.append("Interests: ").append(joinInterests(interests, i -> i.interestWord)).append("\n");
        } catch (Exception ignored) {}
        sb.append("---\n");
    }


    private static void showFacultyResults(String header, List<Datalayer.Faculty> list) {
        if (list.isEmpty()) { info("No faculty found."); return; }
        StringBuilder sb = new StringBuilder(header + "\n\n");
        list.forEach(f -> appendFaculty(sb, f));
        JOptionPane.showMessageDialog(null, sb.toString(), header, JOptionPane.INFORMATION_MESSAGE);
    }


    private static void showStudentResults(String header, List<Datalayer.Student> list, boolean showInterests) {
        if (list.isEmpty()) { info("No students found."); return; }
        StringBuilder sb = new StringBuilder(header + "\n\n");
        list.forEach(s -> appendStudent(sb, s, showInterests));
        JOptionPane.showMessageDialog(null, sb.toString(), header, JOptionPane.INFORMATION_MESSAGE);
    }
}

