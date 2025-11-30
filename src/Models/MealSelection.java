package Models;

public class MealSelection {
    private int selectionId;
    private int userId;
    private int mealId;
    private String selectionDate;
    private String status; // Booked, Cancelled
    
    public MealSelection() {}
    
    public MealSelection(int userId, int mealId) {
        this.userId = userId;
        this.mealId = mealId;
        this.status = "Booked";
    }
    
    // Getters and Setters
    public int getSelectionId() { return selectionId; }
    public void setSelectionId(int selectionId) { this.selectionId = selectionId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }
    
    public String getSelectionDate() { return selectionDate; }
    public void setSelectionDate(String selectionDate) { this.selectionDate = selectionDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
