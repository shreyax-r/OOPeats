package Login;

import javax.swing.*;
import java.sql.*;
import Database.DatabaseManager;
import Database.UserSession;
import Utils.PasswordUtils;
import Utils.ThemeManager;
import Home.HomePage;
import java.awt.*;

public class Login extends JFrame {
    
    // Components
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel messageLabel;
    
    public Login() {
        initComponents();
    }
    
    private void initComponents() {
        // Frame setup
        setTitle("OOPeats - Meal Reservation System");
        setSize(900, 600);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());
        applyTheme();
        
        // Decorative header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 900, 180);
        headerPanel.setBackground(ThemeManager.getPrimary());
        headerPanel.setLayout(null);
        add(headerPanel);
        
        // Title
        titleLabel = new JLabel("OOPeats");
        titleLabel.setBounds(0, 30, 900, 50);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 42));
        titleLabel.setForeground(java.awt.Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("College Canteen Management System");
        subtitleLabel.setBounds(0, 85, 900, 30);
        subtitleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        subtitleLabel.setForeground(java.awt.Color.WHITE);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(subtitleLabel);
        
        // Login form panel (white card)
        JPanel formPanel = new JPanel();
        formPanel.setBounds(250, 220, 400, 320);
        formPanel.setBackground(ThemeManager.getPanel());
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        // Don't add directly, will be added to contentPanel
        
        // Username
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(30, 30, 340, 25);
        usernameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        usernameLabel.setForeground(ThemeManager.getText());
        formPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(30, 55, 340, 40);
        usernameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(usernameField);
        
        // Password
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 110, 340, 25);
        passwordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        passwordLabel.setForeground(ThemeManager.getText());
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 135, 340, 40);
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(passwordField);
        
        // Message label (for errors)
        messageLabel = new JLabel("");
        messageLabel.setBounds(30, 185, 340, 20);
        messageLabel.setForeground(ThemeManager.getRed());
        messageLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        formPanel.add(messageLabel);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(30, 220, 165, 45);
        loginButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        loginButton.setBackground(ThemeManager.getPrimary());
        loginButton.setForeground(java.awt.Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.addActionListener(evt -> loginButtonActionPerformed(evt));
        formPanel.add(loginButton);
        
        // Signup button
        signupButton = new JButton("Sign Up");
        signupButton.setBounds(205, 220, 165, 45);
        signupButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        signupButton.setBackground(ThemeManager.getSecondary());
        signupButton.setForeground(java.awt.Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setBorderPainted(false);
        signupButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signupButton.addActionListener(evt -> signupButtonActionPerformed(evt));
        formPanel.add(signupButton);
        
        // Info label
        JLabel infoLabel = new JLabel("New here? Create an account to get started!");
        infoLabel.setBounds(30, 275, 340, 20);
        infoLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 11));
        infoLabel.setForeground(ThemeManager.getTextLight());
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(infoLabel);
        
        // Wrap content in scroll pane for responsiveness
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(ThemeManager.getBackground());
        contentPanel.setPreferredSize(new java.awt.Dimension(900, 600));
        
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
                contentPanel.setPreferredSize(new java.awt.Dimension(Math.max(width, 900), Math.max(height, 600)));
                contentPanel.revalidate();
            }
        });
        
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void applyTheme() {
        getContentPane().setBackground(ThemeManager.getBackground());
    }
    
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }
        
        try {
            // Connect to database
            DatabaseManager.connect();
            
            // Query user by username then verify hashed password
            String query = "SELECT * FROM user WHERE username = ?";
            PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String stored = rs.getString("password");
                boolean ok = false;
                try {
                    ok = PasswordUtils.verifyPassword(password, stored);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error verifying password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                if (ok) {
                    // Store session data
                    UserSession.userId = rs.getInt("user_id");
                    UserSession.userName = rs.getString("name");
                    UserSession.userRole = rs.getString("role");

                    JOptionPane.showMessageDialog(this, "Welcome " + UserSession.userName + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

                    HomePage homePage = new HomePage();
                    homePage.setVisible(true);
                    this.dispose();
                } else {
                    messageLabel.setText("Invalid username or password!");
                }
            } else {
                messageLabel.setText("Invalid username or password!");
            }

            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Database Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SignUp signupPage = new SignUp();
        signupPage.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
