// PresentationLayer.java
// Author: GROUP2 - Lastname, Lastname, Lastname
// Date: April 13, 2026

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PresentationLayer {
    
    private static DataLayer db;
    private static DataLayer.Account account;
    private static DataLayer.Faculty faculty;
    private static DataLayer.Student student;
    
    public static void main(String[] args) {
          // Connect to database - ONE combined dialog
          JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
          panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
          
          JTextField userField = new JTextField( 15);
          JPasswordField passField = new JPasswordField(15);
          
          panel.add(new JLabel("DB Username:"));
          panel.add(userField);
          panel.add(Box.createVerticalStrut(5));
          panel.add(new JLabel("DB Password:"));
          panel.add(passField);
          
          int result = JOptionPane.showConfirmDialog(null, panel, "Database Connection", 
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
          
          if (result != JOptionPane.OK_OPTION) System.exit(0);
          
          String user = userField.getText().trim();
          String pass = new String(passField.getPassword());
          
          // Create DataLayer instance (constructor attempts connection)
          db = new DataLayer("rit_collab", user, pass);
          
          // Check if connection was successful using isConnected()
          if (!db.isConnected()) {
              JOptionPane.showMessageDialog(null, "Connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
              System.exit(0);
          }
          JOptionPane.showMessageDialog(null, "Connected to database!", "Success", JOptionPane.INFORMATION_MESSAGE);
          
          // Main menu
          showMainMenu();
      }
    
    // ==================== VERTICAL MENU HELPER ====================
    private static int showVerticalMenu(String title, String message, String[] options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel msgLabel = new JLabel(message);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(msgLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[options.length];
        
        for (int i = 0; i < options.length; i++) {
            radioButtons[i] = new JRadioButton(options[i]);
            radioButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonGroup.add(radioButtons[i]);
            panel.add(radioButtons[i]);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        radioButtons[0].setSelected(true);
        
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        
        final int[] selectedIndex = {-1};
        
        okButton.addActionListener(e -> {
            for (int i = 0; i < radioButtons.length; i++) {
                if (radioButtons[i].isSelected()) {
                    selectedIndex[0] = i;
                    break;
                }
            }
            Window win = SwingUtilities.getWindowAncestor(panel);
            win.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(panel);
            win.dispose();
        });
        
        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{});
        JDialog dialog = optionPane.createDialog(title);
        dialog.setModal(true);
        dialog.setVisible(true);
        
        return selectedIndex[0];
    }
    
    // ==================== MAIN MENU ====================
    private static void showMainMenu() {
        while (true) {
            String[] options = {"Login", "Register", "Exit"};
            int choice = showVerticalMenu("Main Menu", "RIT Collaboration System", options);
            
            if (choice == 0) login();
            else if (choice == 1) register();
            else break;
        }
        db.disconnect();
    }
    
    // ==================== LOGIN ====================
    private static void login() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
        
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        
        try {
            account = db.login(username, password);
            if (account == null) {
                JOptionPane.showMessageDialog(null, "Invalid login!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (account.accountType.equals("Faculty")) {
                faculty = db.getFacultyByAccount(account.accountId);
                showFacultyMenu();
            } else if (account.accountType.equals("Student")) {
                student = db.getStudentByAccount(account.accountId);
                showStudentMenu();
            } else {
                guestMenu();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // ==================== REGISTER ====================
    private static void register() {

        try {
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== FACULTY MENU (VERTICAL) ====================
    private static void showFacultyMenu() {
        
    }
    
    private static void showSearchStudentMenu() {
        
    }
    
    private static void addAbstract() throws IOException {
       
        
        try {
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void viewAbstracts() {
        try {
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void updateInterests() {
        
        try {
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void searchStudentByName() {

        
        try {
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void searchStudentByInterest() {
       
        
        try {
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void matchStudents() {
        try {
         
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== STUDENT MENU (VERTICAL) ====================
    private static void showStudentMenu() {
        
    }
    
    private static void updateTopics() {
       
        
        try {
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void matchFaculty() {
        try {
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== GUEST MENU ====================
    private static void guestMenu() {
       
    }
}
  