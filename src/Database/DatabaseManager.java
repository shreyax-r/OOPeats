package Database;

import java.sql.*;

public class DatabaseManager {
    private static Connection connection = null;
    
    // Database credentials - UPDATE THESE
    // Default values - can be overridden by environment variables
    // IMPORTANT: For production, use environment variables or a secure config file
    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/meal_reservation_db";
    private static final String DEFAULT_DB_USER = "root";
    private static final String DEFAULT_DB_PASSWORD = "Shreya2006";
    
    public static void connect() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Read from environment variables if provided
                String dbUrl = System.getenv("DB_URL");
                String dbUser = System.getenv("DB_USER");
                String dbPassword = System.getenv("DB_PASSWORD");

                if (dbUrl == null || dbUrl.isEmpty()) dbUrl = DEFAULT_DB_URL;
                if (dbUser == null || dbUser.isEmpty()) dbUser = DEFAULT_DB_USER;
                if (dbPassword == null) dbPassword = DEFAULT_DB_PASSWORD;

                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                System.out.println("✅ Database connected successfully to: " + dbUrl);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ MySQL Driver not found. Add mysql-connector-java.jar to classpath.", e);
        } catch (SQLException e) {
            throw new SQLException("❌ Database connection failed: " + e.getMessage(), e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }
    
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing database: " + e.getMessage());
        }
    }
}
