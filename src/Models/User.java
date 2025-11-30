package Models;

// Interface for authentication
interface Authenticable {
    boolean authenticate(String username, String password);
}

// Abstract User class
public abstract class User implements Authenticable, Comparable<User> {
    protected int userId;
    protected String name;
    protected String username;
    protected String password;
    protected String role;
    
    // Constructor Overloading
    public User() {}
    
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    // Abstract method
    public abstract void displayDashboard();
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    // Comparable interface implementation
    @Override
    public int compareTo(User other) {
        String a = (this.username == null) ? "" : this.username;
        String b = (other == null || other.username == null) ? "" : other.username;
        return a.compareTo(b);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
