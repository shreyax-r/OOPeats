package Database;

public class UserSession {
    public static int userId;
    public static String userName;
    public static String userRole;
    
    public static boolean isAdmin() {
        return "Admin".equals(userRole);
    }
    
    public static boolean isStudent() {
        return "Student".equals(userRole);
    }
}
