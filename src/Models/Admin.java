package Models;

import javax.swing.JOptionPane;

public class Admin extends User {
    
    public Admin() {
        super();
        this.role = "Admin";
    }
    
    public Admin(String name, String username, String password) {
        super(name, username, password);
        this.role = "Admin";
    }
    
    @Override
    public void displayDashboard() {
        JOptionPane.showMessageDialog(null, "Welcome " + name + " to Admin Dashboard!");
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    // Admin specific methods
    public void addMeal(Meal meal) {
        JOptionPane.showMessageDialog(null, "Meal added: " + meal.getMealName());
    }
    
    public void removeMeal(int mealId) {
        JOptionPane.showMessageDialog(null, "Meal removed with ID: " + mealId);
    }
}
