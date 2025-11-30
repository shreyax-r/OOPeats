package Home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import Database.DatabaseManager;
import Database.UserSession;
import Login.Login;
import Utils.ThemeManager;
import Utils.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.awt.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import java.util.Comparator;

public class HomePage extends JFrame {
    
    // Common components
    private JPanel headerPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JTabbedPane tabbedPane;
    
    // Student Tab Components - Book Meals
    private JTextField mealDateField; // format: yyyy-MM-dd
    private JButton datePickerButton;
    private JComboBox<String> mealTypeComboBox;
    private JComboBox<String> mealTimeComboBox; // Breakfast, Lunch, Dinner, Snacks
    private JTable availableMealsTable;
    private JButton bookMealButton;
    private JButton refreshMealsButton;
    private JButton themeToggleButton;
    
    // Student Tab Components - My Bookings
    private JTable myBookingsTable;
    private JButton cancelBookingButton;
    private JButton refreshBookingsButton;
    private JLabel totalExpenseLabel;
    
    // Admin Tab Components - Manage Meals
    private JTextField mealNameField;
    private JTextField mealPriceField;
    private JComboBox<String> adminMealTypeComboBox;
    private JComboBox<String> adminMealTimeComboBox; // Breakfast, Lunch, Dinner, Snacks
    private JButton addMealButton;
    private JTable allMealsTable;
    private JButton deleteMealButton;
    
    // Admin Tab Components - View Bookings
    private JTable allBookingsTable;
    private JButton refreshAllBookingsButton;
    private JLabel totalBookingsLabel;
    private JComboBox<String> filterRoleComboBox;
    
    public HomePage() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Frame setup
        setTitle("OOPeats - Dashboard");
        setSize(1200, 750);
        setMinimumSize(new java.awt.Dimension(1000, 600)); // Minimum window size
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());
        applyTheme();
        
        // Main container panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(ThemeManager.getBackground());
        
        // Header panel with gradient effect (fixed at top)
        headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1200, 80);
        headerPanel.setBackground(ThemeManager.getPrimary());
        headerPanel.setLayout(null);
        headerPanel.setPreferredSize(new java.awt.Dimension(1200, 80));
        mainPanel.add(headerPanel);
        
        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + UserSession.userName + " (" + UserSession.userRole + ")");
        welcomeLabel.setBounds(30, 15, 600, 50);
        welcomeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        welcomeLabel.setForeground(java.awt.Color.WHITE);
        headerPanel.add(welcomeLabel);
        
        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(1070, 20, 110, 40);
        
        themeToggleButton = new JButton(ThemeManager.getCurrentTheme() == ThemeManager.Theme.LIGHT ? "Dark Mode" : "Light Mode");
        themeToggleButton.setBounds(920, 20, 130, 40);
        themeToggleButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        // Purple for dark mode button, Orange for light mode button
        if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.LIGHT) {
            themeToggleButton.setBackground(new java.awt.Color(138, 43, 226)); // Purple
        } else {
            themeToggleButton.setBackground(new java.awt.Color(255, 140, 0)); // Orange
        }
        themeToggleButton.setForeground(java.awt.Color.WHITE);
        themeToggleButton.setFocusPainted(false);
        themeToggleButton.setBorderPainted(false);
        themeToggleButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        themeToggleButton.addActionListener(evt -> toggleTheme());
        headerPanel.add(themeToggleButton);
        logoutButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        logoutButton.setBackground(ThemeManager.getRed());
        logoutButton.setForeground(java.awt.Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButton.addActionListener(evt -> logoutButtonActionPerformed(evt));
        headerPanel.add(logoutButton);
        
        // Tabbed pane with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20, 100, 1160, 620);
        tabbedPane.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        tabbedPane.setBackground(ThemeManager.getBackground());
        
        // Create tabs based on role (null-safe checks)
        if ("Student".equals(UserSession.userRole)) {
            tabbedPane.addTab("Book Meals", createBookMealsPanel());
            tabbedPane.addTab("My Bookings", createMyBookingsPanel());
        } else if ("Admin".equals(UserSession.userRole)) {
            tabbedPane.addTab("Manage Meals", createManageMealsPanel());
            tabbedPane.addTab("View All Bookings", createViewBookingsPanel());
        }
        
        mainPanel.add(tabbedPane);
        
        // Make main panel scrollable and responsive
        mainPanel.setPreferredSize(new java.awt.Dimension(1200, 750));
        
        // Add component listener to adjust sizes on window resize
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                
                // Update header panel width
                headerPanel.setBounds(0, 0, width, 80);
                headerPanel.setPreferredSize(new java.awt.Dimension(width, 80));
                
                // Update logout button position
                logoutButton.setBounds(width - 150, 20, 120, 40);

                // Update theme toggle button position
                themeToggleButton.setBounds(width - 290, 20, 130, 40);

                
                // Update tabbed pane size
                tabbedPane.setBounds(20, 100, width - 40, height - 120);
                
                // Update main panel size
                mainPanel.setPreferredSize(new java.awt.Dimension(Math.max(width, 1200), Math.max(height, 750)));
                mainPanel.revalidate();
            }
        });
        
        // Wrap main panel in scroll pane
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(mainScrollPane, java.awt.BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
    }
    
    private void applyTheme() {
        getContentPane().setBackground(ThemeManager.getBackground());
    }
    
    private void toggleTheme() {
        ThemeManager.toggleTheme();
        applyTheme();
        
        // Update header panel colors
        headerPanel.setBackground(ThemeManager.getPrimary());
        // Update theme button color and icon: Purple for dark mode, Orange for light mode
        if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.LIGHT) {
            themeToggleButton.setBackground(new java.awt.Color(138, 43, 226)); // Purple
            themeToggleButton.setText("Dark Mode");
        } else {
            themeToggleButton.setBackground(new java.awt.Color(255, 140, 0)); // Orange
            themeToggleButton.setText("Light Mode");
        }
        themeToggleButton.setForeground(java.awt.Color.WHITE); // Ensure text stays white

        
        // Update tabbed pane background
        tabbedPane.setBackground(ThemeManager.getBackground());
        
        // Refresh all panels by removing and re-adding tabs
        int selectedIndex = tabbedPane.getSelectedIndex();
        tabbedPane.removeAll();
        
        if ("Student".equals(UserSession.userRole)) {
            tabbedPane.addTab("Book Meals", createBookMealsPanel());
            tabbedPane.addTab("My Bookings", createMyBookingsPanel());
        } else if ("Admin".equals(UserSession.userRole)) {
            tabbedPane.addTab("Manage Meals", createManageMealsPanel());
            tabbedPane.addTab("View All Bookings", createViewBookingsPanel());
        }
        
        // Restore selected tab
        if (selectedIndex >= 0 && selectedIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(selectedIndex);
        }
        
        // Reload data
        loadData();
        
        // Force repaint of all components
        headerPanel.repaint();
        tabbedPane.repaint();
        repaint();
        revalidate();
    }
    
    // ==================== STUDENT PANELS ====================
    
    private JPanel createBookMealsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ThemeManager.getAccent());
        
        // Title
        JLabel titleLabel = new JLabel("Book Your Meals");
        titleLabel.setBounds(30, 20, 400, 35);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(ThemeManager.getPrimary());
        panel.add(titleLabel);
        
        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setBounds(30, 70, 1100, 80);
        filterPanel.setBackground(ThemeManager.getAccent());
        filterPanel.setLayout(null);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.add(filterPanel);
        
        // Meal Time selector (Breakfast, Lunch, Dinner, Snacks)
        JLabel mealTimeLabel = new JLabel("Meal Time:");
        mealTimeLabel.setBounds(20, 10, 120, 30);
        mealTimeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        mealTimeLabel.setForeground(ThemeManager.getText());
        filterPanel.add(mealTimeLabel);
        
        mealTimeComboBox = new JComboBox<>(new String[]{"Breakfast", "Lunch", "Dinner", "Snacks"});
        mealTimeComboBox.setBounds(100, 8, 150, 34);
        mealTimeComboBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        mealTimeComboBox.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        mealTimeComboBox.addActionListener(evt -> loadAvailableMeals());
        filterPanel.add(mealTimeComboBox);
        
        // Date selector
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setBounds(300, 10, 120, 30);
        dateLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        dateLabel.setForeground(ThemeManager.getText());
        filterPanel.add(dateLabel);
        
        mealDateField = new JTextField();
        mealDateField.setBounds(390, 8, 180, 34);
        mealDateField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        mealDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        mealDateField.setToolTipText("Select date (up to 7 days in advance)");
        filterPanel.add(mealDateField);
        
        // Date picker button
        datePickerButton = new JButton("Pick Date");
        datePickerButton.setBounds(580, 8, 130, 34);
        datePickerButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        datePickerButton.setBackground(ThemeManager.getBlue());
        datePickerButton.setForeground(java.awt.Color.WHITE);
        datePickerButton.setFocusPainted(false);
        datePickerButton.setBorderPainted(false);
        datePickerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        datePickerButton.addActionListener(evt -> {
            LocalDate selectedDate = DatePicker.showDatePickerDialog(this, "Select Booking Date", LocalDate.now());
            if (selectedDate != null) {
                mealDateField.setText(selectedDate.toString());
            }
        });
        filterPanel.add(datePickerButton);
        
        // Meal type filter
        JLabel typeLabel = new JLabel("Meal Type:");
        typeLabel.setBounds(760, 10, 110, 30);
        typeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        typeLabel.setForeground(ThemeManager.getText());
        filterPanel.add(typeLabel);

        mealTypeComboBox = new JComboBox<>(new String[]{"All", "Healthy", "Veg", "Non-Veg", "Vegan"});
        mealTypeComboBox.setBounds(840, 8, 130, 34); 
        mealTypeComboBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        mealTypeComboBox.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        mealTypeComboBox.addActionListener(evt -> loadAvailableMeals());
        filterPanel.add(mealTypeComboBox);

        // Refresh button
        refreshMealsButton = new JButton("Refresh");
        refreshMealsButton.setBounds(1000, 8, 80, 34);
        refreshMealsButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        refreshMealsButton.setBackground(ThemeManager.getBlue());
        refreshMealsButton.setForeground(java.awt.Color.WHITE);
        refreshMealsButton.setFocusPainted(false);
        refreshMealsButton.setBorderPainted(false);
        refreshMealsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refreshMealsButton.addActionListener(evt -> loadAvailableMeals());
        filterPanel.add(refreshMealsButton);
        
        // Available meals table with sorting
        availableMealsTable = new JTable();
        availableMealsTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        availableMealsTable.setRowHeight(30);
        availableMealsTable.setSelectionBackground(ThemeManager.getBorder());
        availableMealsTable.setAutoCreateRowSorter(true);
        // setupNumericSorting will be called after model is set in loadAvailableMeals()
        JScrollPane scrollPane = new JScrollPane(availableMealsTable);
        scrollPane.setBounds(30, 160, 1100, 310);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(scrollPane);
        
        // Book button
        bookMealButton = new JButton("Book Selected Meal");
        bookMealButton.setBounds(450, 500, 260, 50);
        bookMealButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        bookMealButton.setBackground(ThemeManager.getSecondary());
        bookMealButton.setForeground(java.awt.Color.WHITE);
        bookMealButton.setFocusPainted(false);
        bookMealButton.setBorderPainted(false);
        bookMealButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bookMealButton.addActionListener(evt -> bookMealButtonActionPerformed(evt));
        panel.add(bookMealButton);
        
        return panel;
    }
    
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ThemeManager.getAccent());
        
        // Title
        JLabel titleLabel = new JLabel("My Meal Bookings");
        titleLabel.setBounds(30, 20, 400, 35);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(ThemeManager.getPrimary());
        panel.add(titleLabel);
        
        // Total expense label with card style
        JPanel expensePanel = new JPanel();
        expensePanel.setBounds(700, 15, 430, 50);
        expensePanel.setBackground(ThemeManager.getSecondary());
        expensePanel.setLayout(null);
        expensePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getSecondary().darker(), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        panel.add(expensePanel);
        
        totalExpenseLabel = new JLabel("Total Expense: Rs. 0.00");
        totalExpenseLabel.setBounds(10, 5, 410, 40);
        totalExpenseLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        totalExpenseLabel.setForeground(java.awt.Color.WHITE);
        expensePanel.add(totalExpenseLabel);
        
        // Refresh button
        refreshBookingsButton = new JButton("Refresh");
        refreshBookingsButton.setBounds(1015, 80, 110, 38);
        refreshBookingsButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        refreshBookingsButton.setBackground(ThemeManager.getBlue());
        refreshBookingsButton.setForeground(java.awt.Color.WHITE);
        refreshBookingsButton.setFocusPainted(false);
        refreshBookingsButton.setBorderPainted(false);
        refreshBookingsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refreshBookingsButton.addActionListener(evt -> loadMyBookings());
        panel.add(refreshBookingsButton);
        
        // My bookings table
        // My bookings table with sorting
        myBookingsTable = new JTable();
        myBookingsTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        myBookingsTable.setRowHeight(30);
        myBookingsTable.setSelectionBackground(ThemeManager.getBorder());
        myBookingsTable.setAutoCreateRowSorter(true);
        // setupNumericSorting will be called after model is set in loadMyBookings()
        JScrollPane scrollPane = new JScrollPane(myBookingsTable);
        scrollPane.setBounds(30, 130, 1100, 330);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(scrollPane);
        
        // Cancel button
        cancelBookingButton = new JButton("Cancel Selected Booking");
        cancelBookingButton.setBounds(420, 480, 280, 50);
        cancelBookingButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        cancelBookingButton.setBackground(ThemeManager.getRed());
        cancelBookingButton.setForeground(java.awt.Color.WHITE);
        cancelBookingButton.setFocusPainted(false);
        cancelBookingButton.setBorderPainted(false);
        cancelBookingButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelBookingButton.addActionListener(evt -> cancelBookingButtonActionPerformed(evt));
        panel.add(cancelBookingButton);
        
        return panel;
    }
    
    // ==================== ADMIN PANELS ====================
    
    private JPanel createManageMealsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ThemeManager.getAccent());
        
        // Title
        JLabel titleLabel = new JLabel("Manage Meals");
        titleLabel.setBounds(30, 20, 400, 35);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(ThemeManager.getPrimary());
        panel.add(titleLabel);
        
        // Add meal form panel
        JPanel formPanel = new JPanel();
        formPanel.setBounds(30, 70, 1100, 100);
        formPanel.setBackground(ThemeManager.getAccent());
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.add(formPanel);
        
        // Form title
        JLabel formTitle = new JLabel("Add New Meal:");
        formTitle.setBounds(20, 0, 200, 25);
        formTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        formTitle.setForeground(ThemeManager.getText());
        formPanel.add(formTitle);
        
        // Meal name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 35, 70, 25);
        nameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        nameLabel.setForeground(ThemeManager.getText());
        formPanel.add(nameLabel);
        
        mealNameField = new JTextField();
        mealNameField.setBounds(75, 32, 200, 32);
        mealNameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        mealNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(mealNameField);
        
        // Meal type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(315, 35, 50, 25);
        typeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        typeLabel.setForeground(ThemeManager.getText());
        formPanel.add(typeLabel);
        
        adminMealTypeComboBox = new JComboBox<>(new String[]{"Healthy", "Veg", "Non-Veg", "Vegan"});
        adminMealTypeComboBox.setBounds(370, 32, 140, 32);
        adminMealTypeComboBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        adminMealTypeComboBox.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        formPanel.add(adminMealTypeComboBox);
        
        // Price
        JLabel priceLabel = new JLabel("Price (Rs):");
        priceLabel.setBounds(540, 35, 80, 25);
        priceLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        priceLabel.setForeground(ThemeManager.getText());
        formPanel.add(priceLabel);
        
        mealPriceField = new JTextField();
        mealPriceField.setBounds(620, 32, 80, 32);
        mealPriceField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        mealPriceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(mealPriceField);
        
        // Meal Time (Breakfast, Lunch, Dinner, Snacks)
        JLabel mealTimeLabel = new JLabel("Meal Time:");
        mealTimeLabel.setBounds(735, 35, 120, 25);
        mealTimeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        mealTimeLabel.setForeground(ThemeManager.getText());
        formPanel.add(mealTimeLabel);
        
        adminMealTimeComboBox = new JComboBox<>(new String[]{"Breakfast", "Lunch", "Dinner", "Snacks"});
        adminMealTimeComboBox.setBounds(820, 32, 150, 32);
        adminMealTimeComboBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        adminMealTimeComboBox.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        formPanel.add(adminMealTimeComboBox);
        
        // Add button
        addMealButton = new JButton("Add Meal");
        addMealButton.setBounds(990, 32, 100, 32);
        addMealButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        addMealButton.setBackground(ThemeManager.getSecondary());
        addMealButton.setForeground(java.awt.Color.WHITE);
        addMealButton.setFocusPainted(false);
        addMealButton.setBorderPainted(false);
        addMealButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addMealButton.addActionListener(evt -> addMealButtonActionPerformed(evt));
        formPanel.add(addMealButton);
        
        // All meals table
        JLabel tableTitle = new JLabel("All Available Meals:");
        tableTitle.setBounds(30, 190, 300, 30);
        tableTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        tableTitle.setForeground(ThemeManager.getText());
        panel.add(tableTitle);
        
        // All meals table with sorting
        allMealsTable = new JTable();
        allMealsTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        allMealsTable.setRowHeight(30);
        allMealsTable.setSelectionBackground(ThemeManager.getBorder());
        allMealsTable.setAutoCreateRowSorter(true);
        // setupNumericSorting will be called after model is set in loadAllMeals()
        JScrollPane scrollPane = new JScrollPane(allMealsTable);
        scrollPane.setBounds(30, 230, 1100, 250);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(scrollPane);
        
        // Delete button
        deleteMealButton = new JButton("Delete Selected Meal");
        deleteMealButton.setBounds(450, 500, 260, 50);
        deleteMealButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        deleteMealButton.setBackground(ThemeManager.getRed());
        deleteMealButton.setForeground(java.awt.Color.WHITE);
        deleteMealButton.setFocusPainted(false);
        deleteMealButton.setBorderPainted(false);
        deleteMealButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteMealButton.addActionListener(evt -> deleteMealButtonActionPerformed(evt));
        panel.add(deleteMealButton);
        
        return panel;
    }
    
    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ThemeManager.getAccent());
        
        // Title
        JLabel titleLabel = new JLabel("All Student Bookings");
        titleLabel.setBounds(30, 20, 400, 35);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(ThemeManager.getPrimary());
        panel.add(titleLabel);
        
        // Total bookings label with card style
        JPanel statsPanel = new JPanel();
        statsPanel.setBounds(700, 15, 430, 50);
        statsPanel.setBackground(ThemeManager.getBlue());
        statsPanel.setLayout(null);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBlue().darker(), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        panel.add(statsPanel);
        
        totalBookingsLabel = new JLabel("Total Bookings: 0");
        totalBookingsLabel.setBounds(10, 5, 410, 40);
        totalBookingsLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        totalBookingsLabel.setForeground(java.awt.Color.WHITE);
        statsPanel.add(totalBookingsLabel);
        
        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setBounds(30, 75, 1100, 50);
        filterPanel.setBackground(ThemeManager.getAccent());
        filterPanel.setLayout(null);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        panel.add(filterPanel);
        
        // Filter
        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setBounds(20, 10, 150, 30);
        filterLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        filterLabel.setForeground(ThemeManager.getText());
        filterPanel.add(filterLabel);
        
        filterRoleComboBox = new JComboBox<>(new String[]{"All", "Booked", "Cancelled"});
        filterRoleComboBox.setBounds(170, 8, 180, 34);
        filterRoleComboBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        filterRoleComboBox.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 2));
        filterRoleComboBox.addActionListener(evt -> loadAllBookings());
        filterPanel.add(filterRoleComboBox);
        
        // Refresh button
        refreshAllBookingsButton = new JButton("Refresh");
        refreshAllBookingsButton.setBounds(950, 8, 150, 34);
        refreshAllBookingsButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        refreshAllBookingsButton.setBackground(ThemeManager.getBlue());
        refreshAllBookingsButton.setForeground(java.awt.Color.WHITE);
        refreshAllBookingsButton.setFocusPainted(false);
        refreshAllBookingsButton.setBorderPainted(false);
        refreshAllBookingsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refreshAllBookingsButton.addActionListener(evt -> loadAllBookings());
        filterPanel.add(refreshAllBookingsButton);
        
        // All bookings table with sorting
        allBookingsTable = new JTable();
        allBookingsTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        allBookingsTable.setRowHeight(30);
        allBookingsTable.setSelectionBackground(ThemeManager.getBorder());
        allBookingsTable.setAutoCreateRowSorter(true);
        setupNumericSorting(allBookingsTable, new int[]{4}); // Price column
        JScrollPane scrollPane = new JScrollPane(allBookingsTable);
        scrollPane.setBounds(30, 145, 1100, 415);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(scrollPane);
        
        return panel;
    }
    
    // ==================== DATA LOADING METHODS ====================
    
    private void loadData() {
        if (UserSession.userRole != null && UserSession.userRole.equals("Student")) {
            loadAvailableMeals();
            loadMyBookings();
        } else if (UserSession.userRole != null && UserSession.userRole.equals("Admin")) {
            loadAllMeals();
            loadAllBookings();
        }
    }
    
    private void loadAvailableMeals() {
        try {
            DatabaseManager.connect();
            if (mealTimeComboBox == null || mealTypeComboBox == null) {
                return; // Components not initialized yet
            }
            String mealType = (String) mealTypeComboBox.getSelectedItem();
            String mealTime = (String) mealTimeComboBox.getSelectedItem();

            String query;
            PreparedStatement pstmt;

            if ("All".equals(mealType)) {
                query = "SELECT * FROM meal WHERE meal_time = ? ORDER BY meal_type";
                pstmt = DatabaseManager.getConnection().prepareStatement(query);
                pstmt.setString(1, mealTime);
            } else {
                query = "SELECT * FROM meal WHERE meal_type = ? AND meal_time = ? ORDER BY meal_type";
                pstmt = DatabaseManager.getConnection().prepareStatement(query);
                pstmt.setString(1, mealType);
                pstmt.setString(2, mealTime);
            }

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Meal ID", "Meal Name", "Type", "Meal Time", "Price (Rs)"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table read-only
                }
            };

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("meal_id"),
                    rs.getString("meal_name"),
                    rs.getString("meal_type"),
                    rs.getString("meal_time"),
                    String.format("%.2f", rs.getDouble("price"))
                });
            }

            availableMealsTable.setModel(model);
            setupNumericSorting(availableMealsTable, new int[]{0, 4}); // Meal ID and Price columns
            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading meals: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMyBookings() {
        try {
            DatabaseManager.connect();

            String query = "SELECT ms.selection_id, m.meal_name, m.meal_type, m.meal_time, m.price, " +
                          "ms.selection_date, ms.status " +
                          "FROM meal_selection ms " +
                          "INNER JOIN meal m ON ms.meal_id = m.meal_id " +
                          "WHERE ms.user_id = ? ORDER BY ms.selection_date DESC, m.meal_time";

            PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query);
            pstmt.setInt(1, UserSession.userId);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Booking ID", "Meal Name", "Type", "Meal Time", "Price (Rs)", "Date", "Status"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            double totalExpense = 0.0;

            while (rs.next()) {
                double price = rs.getDouble("price");
                String status = rs.getString("status");

                model.addRow(new Object[]{
                    rs.getInt("selection_id"),
                    rs.getString("meal_name"),
                    rs.getString("meal_type"),
                    rs.getString("meal_time"),
                    String.format("%.2f", price),
                    rs.getDate("selection_date"),
                    status
                });

                if ("Booked".equals(status)) {
                    totalExpense += price;
                }
            }

            myBookingsTable.setModel(model);
            setupNumericSorting(myBookingsTable, new int[]{0, 4}); // Booking ID and Price columns
            totalExpenseLabel.setText("Total Expense: Rs. " + String.format("%.2f", totalExpense));

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading bookings: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadAllMeals() {
        try {
            DatabaseManager.connect();

            String query = "SELECT * FROM meal ORDER BY meal_time, meal_type";
            Statement stmt = DatabaseManager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Meal ID", "Meal Name", "Type", "Meal Time", "Price (Rs)"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("meal_id"),
                    rs.getString("meal_name"),
                    rs.getString("meal_type"),
                    rs.getString("meal_time"),
                    String.format("%.2f", rs.getDouble("price"))
                });
            }

            allMealsTable.setModel(model);
            setupNumericSorting(allMealsTable, new int[]{0, 4}); // Meal ID and Price columns
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading meals: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadAllBookings() {
        try {
            DatabaseManager.connect();
            String filter = (String) filterRoleComboBox.getSelectedItem();

            String query;
            PreparedStatement pstmt;

            if ("All".equals(filter)) {
                query = "SELECT u.name, m.meal_name, m.meal_type, m.meal_time, m.price, ms.selection_date, ms.status " +
                       "FROM meal_selection ms " +
                       "INNER JOIN user u ON ms.user_id = u.user_id " +
                       "INNER JOIN meal m ON ms.meal_id = m.meal_id " +
                       "ORDER BY ms.selection_date DESC, m.meal_time";
                pstmt = DatabaseManager.getConnection().prepareStatement(query);
            } else {
                query = "SELECT u.name, m.meal_name, m.meal_type, m.meal_time, m.price, ms.selection_date, ms.status " +
                       "FROM meal_selection ms " +
                       "INNER JOIN user u ON ms.user_id = u.user_id " +
                       "INNER JOIN meal m ON ms.meal_id = m.meal_id " +
                       "WHERE ms.status = ? " +
                       "ORDER BY ms.selection_date DESC, m.meal_time";
                pstmt = DatabaseManager.getConnection().prepareStatement(query);
                pstmt.setString(1, filter);
            }

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Student Name", "Meal Name", "Type", "Meal Time", "Price (Rs)", "Date", "Status"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            int totalBookings = 0;

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("meal_name"),
                    rs.getString("meal_type"),
                    rs.getString("meal_time"),
                    String.format("%.2f", rs.getDouble("price")),
                    rs.getDate("selection_date"),
                    rs.getString("status")
                });
                totalBookings++;
            }

            allBookingsTable.setModel(model);
            setupNumericSorting(allBookingsTable, new int[]{4}); // Price column
            totalBookingsLabel.setText("Total Bookings: " + totalBookings);

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading bookings: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== ACTION METHODS ====================
    
    private void bookMealButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = availableMealsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a meal to book.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String dateText = mealDateField.getText().trim();
        if (dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a date for booking (yyyy-MM-dd).", 
                "No Date Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int mealId = (int) availableMealsTable.getValueAt(selectedRow, 0);
            String mealName = (String) availableMealsTable.getValueAt(selectedRow, 1);
            
            // Get meal_time from database
            DatabaseManager.connect();
            String mealTimeQuery = "SELECT meal_time FROM meal WHERE meal_id = ?";
            PreparedStatement mealTimeStmt = DatabaseManager.getConnection().prepareStatement(mealTimeQuery);
            mealTimeStmt.setInt(1, mealId);
            ResultSet mealTimeRs = mealTimeStmt.executeQuery();
            
            if (!mealTimeRs.next()) {
                JOptionPane.showMessageDialog(this, "Error: Meal not found!", "Error", JOptionPane.ERROR_MESSAGE);
                mealTimeRs.close();
                mealTimeStmt.close();
                return;
            }
            
            String mealTime = mealTimeRs.getString("meal_time");
            mealTimeRs.close();
            mealTimeStmt.close();

            // parse and validate date (yyyy-MM-dd)
            String selectedDate;
            LocalDate bookingDate;
            try {
                bookingDate = LocalDate.parse(dateText);
                if (bookingDate.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "Please select today or a future date.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Check if booking is within 7 days
                long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), bookingDate);
                if (daysBetween > 7) {
                    JOptionPane.showMessageDialog(this, "You can only book meals up to 7 days in advance.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selectedDate = bookingDate.toString();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if user already booked a meal for this meal_time on this date
            String checkQuery = "SELECT * FROM meal_selection WHERE user_id = ? AND selection_date = ? AND meal_time = ? AND status = 'Booked'";
            PreparedStatement checkStmt = DatabaseManager.getConnection().prepareStatement(checkQuery);
            checkStmt.setInt(1, UserSession.userId);
            checkStmt.setString(2, selectedDate);
            checkStmt.setString(3, mealTime);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                JOptionPane.showMessageDialog(this, 
                    "You have already booked a " + mealTime + " meal for " + selectedDate + "!\nYou can only book one meal per meal time per day.", 
                    "Duplicate Booking", 
                    JOptionPane.WARNING_MESSAGE);
                checkRs.close();
                checkStmt.close();
                return;
            }

            // Insert booking with meal_time
            String insertQuery = "INSERT INTO meal_selection(user_id, meal_id, selection_date, meal_time, status) " +
                                "VALUES (?, ?, ?, ?, 'Booked')";

            PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(insertQuery);
            pstmt.setInt(1, UserSession.userId);
            pstmt.setInt(2, mealId);
            pstmt.setString(3, selectedDate);
            pstmt.setString(4, mealTime);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Meal '" + mealName + "' (" + mealTime + ") booked successfully for " + selectedDate + "!", 
                    "Booking Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadMyBookings();
            }

            checkRs.close();
            checkStmt.close();
            pstmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error booking meal: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = myBookingsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a booking to cancel.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = (String) myBookingsTable.getValueAt(selectedRow, 6); // Status is column 6
        if ("Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "This booking is already cancelled.", 
                "Already Cancelled", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel this booking?", 
            "Confirm Cancellation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int bookingId = (int) myBookingsTable.getValueAt(selectedRow, 0);
                
                DatabaseManager.connect();
                String updateQuery = "UPDATE meal_selection SET status = 'Cancelled' WHERE selection_id = ?";
                
                PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(updateQuery);
                pstmt.setInt(1, bookingId);
                
                int rowsUpdated = pstmt.executeUpdate();
                
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Booking cancelled successfully!", 
                        "Cancellation Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadMyBookings();
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error cancelling booking: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addMealButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String mealName = mealNameField.getText().trim();
        String mealType = (String) adminMealTypeComboBox.getSelectedItem();
        String priceText = mealPriceField.getText().trim();
        
        if (mealName.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in meal name and price.", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String mealTime = (String) adminMealTimeComboBox.getSelectedItem();
        if (mealTime == null || mealTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a meal time (Breakfast, Lunch, Dinner, or Snacks).", 
                "No Meal Time Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double price = Double.parseDouble(priceText);
            
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Price must be greater than zero.", 
                    "Invalid Price", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            DatabaseManager.connect();

            String insertQuery = "INSERT INTO meal(meal_name, meal_type, meal_time, price) " +
                                "VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(insertQuery);
            pstmt.setString(1, mealName);
            pstmt.setString(2, mealType);
            pstmt.setString(3, mealTime);
            pstmt.setDouble(4, price);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Meal '" + mealName + "' (" + mealTime + ") added successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                mealNameField.setText("");
                mealPriceField.setText("");

                loadAllMeals();
            }

            pstmt.close();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid price (numbers only).", 
                "Invalid Price Format", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error adding meal: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMealButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = allMealsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a meal to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this meal?\nAll associated bookings will also be deleted.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int mealId = (int) allMealsTable.getValueAt(selectedRow, 0);

                DatabaseManager.connect();
                String deleteQuery = "DELETE FROM meal WHERE meal_id = ?";

                PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(deleteQuery);
                pstmt.setInt(1, mealId);

                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Meal deleted successfully!", 
                        "Deletion Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadAllMeals();
                }

                pstmt.close();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting meal: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Setup numeric sorting for table columns
    private void setupNumericSorting(JTable table, int[] numericColumns) {
        TableModel model = table.getModel();
        if (model == null || model.getColumnCount() == 0) {
            return; // Model not set yet, skip
        }
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        
        for (int col : numericColumns) {
            // Check if column index is valid
            if (col >= 0 && col < model.getColumnCount()) {
                sorter.setComparator(col, new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        if (o1 == null && o2 == null) return 0;
                        if (o1 == null) return -1;
                        if (o2 == null) return 1;
                        
                        try {
                            // Try to parse as numbers
                            double d1 = Double.parseDouble(o1.toString().replaceAll("[^0-9.]", ""));
                            double d2 = Double.parseDouble(o2.toString().replaceAll("[^0-9.]", ""));
                            return Double.compare(d1, d2);
                        } catch (NumberFormatException e) {
                            // If not numeric, fall back to string comparison
                            return o1.toString().compareTo(o2.toString());
                        }
                    }
                });
            }
        }
        
        table.setRowSorter(sorter);
    }
    
    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Clear session
            UserSession.userId = 0;
            UserSession.userName = null;
            UserSession.userRole = null;
            
            // Go to login
            Login loginPage = new Login();
            loginPage.setVisible(true);
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}
