package Login;

import javax.swing.*;
import java.sql.*;
import Database.DatabaseManager;
import Exceptions.PasswordException;
import Utils.PasswordUtils;
import Utils.ThemeManager;
import java.util.regex.*;
import java.awt.*;

public class SignUp extends JFrame {
    
    // Components
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel confirmPasswordLabel;
    private JPasswordField confirmPasswordField;
    private JLabel roleLabel;
    private JComboBox<String> roleComboBox;
    private JLabel adminKeyLabel;
    private JPasswordField adminKeyField;
    private JButton signupButton;
    private JButton backButton;
    private JLabel messageLabel;
    
    public SignUp() {
        initComponents();
    }
    
    private void initComponents() {
        // Frame setup
        setTitle("OOPeats - Create Account");
        setSize(900, 750);
        setMinimumSize(new java.awt.Dimension(800, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());
        applyTheme();
        
        // Decorative header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 900, 150);
        headerPanel.setBackground(ThemeManager.getPrimary());
        headerPanel.setLayout(null);
        // Don't add directly, will be added to contentPanel
        
        // Title
        titleLabel = new JLabel("Create Your Account");
        titleLabel.setBounds(0, 20, 900, 50);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 36));
        titleLabel.setForeground(java.awt.Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Join OOPeats and start booking your meals!");
        subtitleLabel.setBounds(0, 75, 900, 25);
        subtitleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        subtitleLabel.setForeground(java.awt.Color.WHITE);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(subtitleLabel);
        
        // Form panel (white card)
        JPanel formPanel = new JPanel();
        formPanel.setBounds(200, 180, 500, 580);
        formPanel.setBackground(ThemeManager.getPanel());
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        // Don't add directly, will be added to contentPanel
        
        // Full Name
        nameLabel = new JLabel("Full Name");
        nameLabel.setBounds(30, 20, 440, 25);
        nameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        nameLabel.setForeground(ThemeManager.getText());
        formPanel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(30, 45, 440, 38);
        nameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(nameField);
        
        // Username
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(30, 95, 440, 25);
        usernameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        usernameLabel.setForeground(ThemeManager.getText());
        formPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(30, 120, 440, 38);
        usernameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(usernameField);
        
        // Password
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 170, 440, 25);
        passwordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        passwordLabel.setForeground(ThemeManager.getText());
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 195, 440, 38);
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(passwordField);
        
        // Confirm Password
        confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setBounds(30, 245, 440, 25);
        confirmPasswordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        confirmPasswordLabel.setForeground(ThemeManager.getText());
        formPanel.add(confirmPasswordLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(30, 270, 440, 38);
        confirmPasswordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(confirmPasswordField);
        
        // Role
        roleLabel = new JLabel("Role");
        roleLabel.setBounds(30, 320, 440, 25);
        roleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        roleLabel.setForeground(ThemeManager.getText());
        formPanel.add(roleLabel);
        
        roleComboBox = new JComboBox<>(new String[]{"Student", "Admin"});
        roleComboBox.setBounds(30, 345, 440, 38);
        roleComboBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        roleComboBox.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        roleComboBox.addActionListener(evt -> toggleAdminKeyField());
        formPanel.add(roleComboBox);
        
        // Password hint (always visible)
        JLabel hintLabel = new JLabel("Password must be 8+ characters with at least one special character");
        hintLabel.setBounds(30, 390, 440, 20);
        hintLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 11));
        hintLabel.setForeground(ThemeManager.getTextLight());
        formPanel.add(hintLabel);
        
        // Admin Key field (hidden by default, appears below hint)
        adminKeyLabel = new JLabel("Admin Key (Required for Admin):");
        adminKeyLabel.setBounds(30, 415, 440, 25);
        adminKeyLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        adminKeyLabel.setForeground(ThemeManager.getText());
        adminKeyLabel.setVisible(false);
        formPanel.add(adminKeyLabel);
        
        adminKeyField = new JPasswordField();
        adminKeyField.setBounds(30, 440, 440, 38);
        adminKeyField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        adminKeyField.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        adminKeyField.setVisible(false);
        formPanel.add(adminKeyField);
        
        // Message label (positioned after admin key field)
        messageLabel = new JLabel("");
        messageLabel.setBounds(30, 415, 440, 20);
        messageLabel.setForeground(ThemeManager.getRed());
        messageLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        formPanel.add(messageLabel);
        
        // Sign Up button (positioned dynamically)
        signupButton = new JButton("Create Account");
        signupButton.setBounds(30, 445, 215, 45);
        signupButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        signupButton.setBackground(ThemeManager.getSecondary());
        signupButton.setForeground(java.awt.Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setBorderPainted(false);
        signupButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signupButton.addActionListener(evt -> signupButtonActionPerformed(evt));
        formPanel.add(signupButton);
        
        // Back button (positioned dynamically)
        backButton = new JButton("Back to Login");
        backButton.setBounds(255, 445, 215, 45);
        backButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        backButton.setBackground(new java.awt.Color(158, 158, 158)); // Gray
        backButton.setForeground(java.awt.Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.addActionListener(evt -> backButtonActionPerformed(evt));
        formPanel.add(backButton);
        
        // Wrap content in scroll pane for responsiveness
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(ThemeManager.getBackground());
        contentPanel.setPreferredSize(new java.awt.Dimension(900, 750));
        
        // Add all components to content panel
        contentPanel.add(headerPanel);
        contentPanel.add(formPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, java.awt.BorderLayout.CENTER);
        
        // Add resize listener
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                contentPanel.setPreferredSize(new java.awt.Dimension(Math.max(width, 900), Math.max(height, 750)));
                contentPanel.revalidate();
            }
        });
        
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void applyTheme() {
        getContentPane().setBackground(ThemeManager.getBackground());
    }
    
    private void toggleAdminKeyField() {
        String role = (String) roleComboBox.getSelectedItem();
        boolean isAdmin = "Admin".equals(role);
        adminKeyLabel.setVisible(isAdmin);
        adminKeyField.setVisible(isAdmin);
        adminKeyField.setText(""); // Clear when hidden
        
        // Adjust message label and button positions based on admin key visibility
        if (isAdmin) {
            // Move message label and button down when admin key is visible
            messageLabel.setBounds(30, 485, 440, 20);
            signupButton.setBounds(30, 515, 215, 45);
            backButton.setBounds(255, 515, 215, 45);
        } else {
            // Normal positions when admin key is hidden
            messageLabel.setBounds(30, 415, 440, 20);
            signupButton.setBounds(30, 445, 215, 45);
            backButton.setBounds(255, 445, 215, 45);
        }
    }
    
    private void passwordRestrictions(String password) throws PasswordException {
        if (password.length() < 8) {
            throw new PasswordException("Password must be at least 8 characters long");
        }
        
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password);
        
        if (!matcher.find()) {
            throw new PasswordException("Password must contain at least one special character");
        }
    }
    
    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();
        String adminKey = new String(adminKeyField.getPassword()).trim();
        
        // Validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill all fields");
            return;
        }
        
        // Admin key validation
        if ("Admin".equals(role)) {
            if (adminKey.isEmpty()) {
                messageLabel.setText("Admin key is required for Admin accounts");
                return;
            }
            if (!"ADMIN2025".equals(adminKey)) {
                messageLabel.setText("Invalid admin key! Contact system administrator.");
                return;
            }
        }
        
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match!");
            return;
        }
        
        try {
            // Check password restrictions
            passwordRestrictions(password);
            
            // Connect to database
            DatabaseManager.connect();
            
            // Hash password (salted PBKDF2) and store as salt$hash
            String storedPassword;
            try {
                storedPassword = PasswordUtils.generateStrongPasswordHash(password);
            } catch (Exception ex) {
                messageLabel.setText("Error processing password");
                return;
            }
            
            // Check if username already exists
            String checkQuery = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement checkStmt = DatabaseManager.getConnection().prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        messageLabel.setText("Username already exists!");
                        return;
                    }
                }
            }

            // Insert new user
            String insertQuery = "INSERT INTO user(name, username, password, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(insertQuery)) {
                pstmt.setString(1, name);
                pstmt.setString(2, username);
                pstmt.setString(3, storedPassword);
                pstmt.setString(4, role);

                int rowsInserted = pstmt.executeUpdate();
                
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Account created successfully!\nPlease login now.", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Go back to login
                    Login loginPage = new Login();
                    loginPage.setVisible(true);
                    this.dispose();
                }
            }
            
        } catch (PasswordException ex) {
            messageLabel.setText(ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Login loginPage = new Login();
        loginPage.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new SignUp().setVisible(true);
        });
    }
}
