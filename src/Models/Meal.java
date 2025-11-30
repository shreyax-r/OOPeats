package Models;

public class Meal implements Comparable<Meal> {
    private int mealId;
    private String mealName;
    private String mealType; // Healthy, Veg, Non-Veg, Vegan
    private double price;
    
    // Constructor Overloading
    public Meal() {}
    
    public Meal(String mealName, String mealType, double price) {
        this.mealName = mealName;
        this.mealType = mealType;
        this.price = price;
    }
    
    public Meal(int mealId, String mealName, String mealType, double price) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealType = mealType;
        this.price = price;
    }
    
    // Getters and Setters
    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }
    
    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }
    
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    // Comparable interface for sorting meals by price
    @Override
    public int compareTo(Meal other) {
        return Double.compare(this.price, other.price);
    }
    
    @Override
    public String toString() {
        return mealName + " (" + mealType + ") - Rs." + price;
    }
}
