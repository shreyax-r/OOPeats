package Models;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import Database.DatabaseManager;

public class Student extends User {
    private List<MealSelection> mealSelections;
    private double totalExpense;
    
    // Constructor Overloading
    public Student() {
        super();
        this.role = "Student";
        this.mealSelections = new ArrayList<>();
        this.totalExpense = 0.0;
    }
    
    public Student(String name, String username, String password) {
        super(name, username, password);
        this.role = "Student";
        this.mealSelections = new ArrayList<>();
        this.totalExpense = 0.0;
    }
    
    public Student(int userId, String name, String username, String role) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.role = role;
        this.mealSelections = new ArrayList<>();
    }
    
    // Abstract method implementation from User class
    @Override
    public void displayDashboard() {
        JOptionPane.showMessageDialog(null, 
            "Welcome " + name + " to Student Dashboard!\n" +
            "Total Bookings: " + mealSelections.size() + "\n" +
            "Expense: Rs." + String.format("%.2f", totalExpense),
            "Student Dashboard",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Interface method implementation from Authenticable
    @Override
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    // Method Overloading - Different ways to select meals
    public void selectMeal(Meal meal) {
        MealSelection selection = new MealSelection(this.userId, meal.getMealId());
        mealSelections.add(selection);
        totalExpense += meal.getPrice();
    }
    
    public void selectMeal(int mealId, String date) {
        MealSelection selection = new MealSelection(this.userId, mealId);
        selection.setSelectionDate(date);
        mealSelections.add(selection);
    }
    
    public void selectMeal(Meal meal, String date, String status) {
        MealSelection selection = new MealSelection(this.userId, meal.getMealId());
        selection.setSelectionDate(date);
        selection.setStatus(status);
        mealSelections.add(selection);
        if ("Booked".equals(status)) {
            totalExpense += meal.getPrice();
        }
    }
    
    // Calculate  expense from database
    public double calculateExpense() {
        double total = 0.0;
        try {
            DatabaseManager.connect();
            String query = "SELECT SUM(m.price) as total " +
                          "FROM meal_selection ms " +
                          "INNER JOIN meal m ON ms.meal_id = m.meal_id " +
                          "WHERE ms.user_id = ? AND ms.status = 'Booked' " +
                          "AND MONTH(ms.selection_date) = MONTH(CURDATE()) " +
                          "AND YEAR(ms.selection_date) = YEAR(CURDATE())";
            
            PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query);
            pstmt.setInt(1, this.userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                total = rs.getDouble("total");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error calculating expense: " + e.getMessage());
        }
        
        this.totalExpense = total;
        return total;
    }
    
    // Cancel a meal booking
    public void cancelMeal(int selectionId) {
        try {
            DatabaseManager.connect();
            String updateQuery = "UPDATE meal_selection SET status = 'Cancelled' WHERE selection_id = ? AND user_id = ?";
            
            PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(updateQuery);
            pstmt.setInt(1, selectionId);
            pstmt.setInt(2, this.userId);
            
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Booking cancelled successfully!");
                // Recalculate  expense
                calculateExpense();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to cancel booking.");
            }
            
            pstmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    // Inner class for filtering meal selections by date range
    public class DateRangeFilter {
        private String startDate;
        private String endDate;
        
        public DateRangeFilter(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        public List<MealSelection> filterSelections() {
            List<MealSelection> filteredList = new ArrayList<>();
            for (MealSelection ms : mealSelections) {
                String selectionDate = ms.getSelectionDate();
                if (selectionDate.compareTo(startDate) >= 0 && 
                    selectionDate.compareTo(endDate) <= 0) {
                    filteredList.add(ms);
                }
            }
            return filteredList;
        }
    }
    
    // Getters and Setters
    public List<MealSelection> getMealSelections() {
        return mealSelections;
    }
    
    public void setMealSelections(List<MealSelection> mealSelections) {
        this.mealSelections = mealSelections;
    }
    
    public double getTotalExpense() {
        return totalExpense;
    }
    
    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }
    
    // toString method override
    @Override
    public String toString() {
        return "Student{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", totalBookings=" + mealSelections.size() +
                ", Expense=" + String.format("%.2f", totalExpense) +
                '}';
    }
}

