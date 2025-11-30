# OOPeats - Meal Reservation System

A comprehensive Java-based meal reservation system for college canteen management with a modern Swing GUI. This application allows students to book meals (Breakfast, Lunch, Dinner, Snacks) up to 7 days in advance, while admins can manage meals and view all bookings.

## 🎯 Features

### For Students:
- **User Authentication**: Secure login with password hashing (PBKDF2)
- **Meal Booking**: Book meals for Breakfast, Lunch, Dinner, or Snacks
- **Date Selection**: Calendar picker for selecting booking dates
- **Advance Booking**: Book meals up to 7 days in advance
- **One Meal Per Time**: Enforced limit of one meal per meal time per day
- **View Bookings**: See all your bookings with sorting capabilities
- **Cancel Bookings**: Cancel any booked meal
- **Monthly Expense Tracking**: View total monthly expenses
- **Theme Support**: Light/Dark mode toggle

### For Admins:
- **Admin Authentication**: Special admin key required for account creation
- **Meal Management**: Add and delete meals with meal time specification
- **View All Bookings**: See all student bookings with filtering options
- **Statistics**: View total booking counts
- **Theme Support**: Light/Dark mode toggle

### General Features:
- **Responsive Design**: Window resizing with scrollable content
- **Modern UI**: Beautiful color scheme with gradient effects
- **Table Sorting**: Numeric sorting for IDs and prices
- **Date Validation**: Prevents past date bookings and enforces 7-day limit
- **Password Security**: PBKDF2 password hashing with salt

## 📋 Prerequisites

Before running this application, ensure you have:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version` and `javac -version`

2. **MySQL Server 8.0 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Install MySQL Server and MySQL Workbench (optional)

3. **Git** (for cloning the repository)
   - Download from: https://git-scm.com/downloads

## 🚀 Getting Started from Git

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd OOPeats
```

### Step 2: Install MySQL and Set Password

1. Install MySQL Server
2. During installation, set the root password to: `Shreya2006`
   - **Note**: If you use a different password, update it in `src/Database/DatabaseManager.java`

### Step 3: Setup Database

**Option A: Using MySQL Command Line**

1. Open Command Prompt (Windows) or Terminal (Mac/Linux)
2. Login to MySQL:
   ```bash
   mysql -u root -p
   ```
   Enter password: `Shreya2006`

3. Run the database setup script:
   ```sql
   source database/meal_reservation_db.sql
   ```
   
   Or on Windows with full path:
   ```sql
   source C:/path/to/OOPeats/database/meal_reservation_db.sql
   ```

**Option B: Using MySQL Workbench**

1. Open MySQL Workbench
2. Connect to localhost (root, password: Shreya2006)
3. File → Open SQL Script → Select `database/meal_reservation_db.sql`
4. Execute the script (⚡ icon or Ctrl+Shift+Enter)

**Verify Database Creation:**
```sql
USE meal_reservation_db;
SHOW TABLES;
```

You should see: `user`, `meal`, and `meal_selection` tables.

### Step 4: Update Database Password (If Different)

If you used a different MySQL password, edit `src/Database/DatabaseManager.java`:

```java
private static final String DB_PASSWORD = "YourPasswordHere";
```

### Step 5: Compile the Project

**On Windows (Command Prompt):**

```cmd
cd path\to\OOPeats
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Database\*.java
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Exceptions\*.java
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Utils\*.java
javac -cp "lib\mysql-connector-j-9.5.0.jar;out" -d out src\Models\*.java
REM Compile Login and Home together to resolve circular dependency
javac -cp "lib\mysql-connector-j-9.5.0.jar;out" -d out src\Login\*.java src\Home\*.java
```

**On Mac/Linux (Terminal):**

```bash
cd path/to/OOPeats
javac -cp "lib/mysql-connector-j-9.5.0.jar" -d out src/Database/*.java
javac -cp "lib/mysql-connector-j-9.5.0.jar" -d out src/Exceptions/*.java
javac -cp "lib/mysql-connector-j-9.5.0.jar" -d out src/Utils/*.java
javac -cp "lib/mysql-connector-j-9.5.0.jar:out" -d out src/Models/*.java
# Compile Login and Home together to resolve circular dependency
javac -cp "lib/mysql-connector-j-9.5.0.jar:out" -d out src/Login/*.java src/Home/*.java
```

### Step 6: Run the Application

**On Windows:**

```cmd
java -cp "out;lib\mysql-connector-j-9.5.0.jar" Login.Login
```

**On Mac/Linux:**

```bash
java -cp "out:lib/mysql-connector-j-9.5.0.jar" Login.Login
```

**Or use the provided batch file (Windows):**

```cmd
run.bat
```

## 🏗️ Project Structure

```
OOPeats/
├── database/
│   └── meal_reservation_db.sql          # Database schema and sample data
├── lib/
│   ├── mysql-connector-j-9.5.0.jar      # MySQL JDBC driver
│   └── jcalendar-1.4 (1).jar            # Date picker library
├── src/
│   ├── Database/
│   │   ├── DatabaseManager.java         # Database connection management
│   │   └── UserSession.java             # Session management
│   ├── Exceptions/
│   │   └── PasswordException.java       # Custom password exception
│   ├── Home/
│   │   └── HomePage.java                # Main dashboard (Student/Admin)
│   ├── Login/
│   │   ├── Login.java                   # Login window
│   │   └── SignUp.java                  # Registration window
│   ├── Models/
│   │   ├── User.java                    # Abstract user class
│   │   ├── Student.java                 # Student model
│   │   ├── Admin.java                   # Admin model
│   │   ├── Meal.java                    # Meal model
│   │   └── MealSelection.java           # Booking model
│   └── Utils/
│       ├── ThemeManager.java            # Theme management (Light/Dark)
│       ├── PasswordUtils.java           # Password hashing utilities
│       ├── DatePicker.java              # Date picker utility
│       └── HashPassword.java            # Password hashing tool
├── out/                                  # Compiled class files (auto-generated, not in Git)
├── target/                               # Maven build output (auto-generated, not in Git)
├── pom.xml                               # Maven configuration
├── run.bat                               # Windows run script
├── .gitignore                            # Git ignore file
└── README.md                             # This file
```

**Important Notes:**
- **`src/` folder**: Contains source code (`.java` files) - **KEEP THIS**, it's your actual code
- **`out/` folder**: Contains compiled bytecode (`.class` files) - **Can be deleted**, it's auto-generated when you compile
- The `out/` folder is excluded from Git (via `.gitignore`) since it can be regenerated
- The application uses direct SQL queries in `HomePage.java` instead of a DAO (Data Access Object) pattern


**Admin Key for Signup:** `ADMIN2025`

## 🎨 Features in Detail

### Theme System
- **Light Mode**: Orange accent colors with light background
- **Dark Mode**: Purple/pink/blue palette with black background
- Toggle button in top-right corner

### Booking System
- Students can book 4 meals per day (one each for Breakfast, Lunch, Dinner, Snacks)
- Bookings can be made up to 7 days in advance
- Date validation prevents past dates
- Duplicate booking prevention per meal time

### Table Features
- Sortable columns (numeric sorting for IDs and prices)
- Filter options for meal types and booking status
- Responsive table sizing

### Security
- Password hashing using PBKDF2 with salt
- Admin key protection for admin account creation
- SQL injection prevention with prepared statements

## 🛠️ Troubleshooting

### Database Connection Issues

**Error: "Access denied for user 'root'@'localhost'"**
- Check MySQL password in `DatabaseManager.java`
- Verify MySQL service is running: `net start MySQL80` (Windows)

**Error: "Unknown database 'meal_reservation_db'"**
- Run the database setup script: `database/meal_reservation_db.sql`

### Compilation Issues

**Error: "package does not exist"**
- Ensure you're compiling in the correct order (Database → Exceptions → Utils → Models → Login → Home)
- Check that classpath includes the MySQL connector JAR

**Error: "cannot find symbol"**
- Make sure all files are in the correct package directories
- Verify all imports are correct

### Runtime Issues

**Application doesn't start**
- Check MySQL is running
- Verify database connection settings
- Check console for error messages

## 📝 Development Notes

### Object-Oriented Programming Concepts Used:
- **Inheritance**: `User` (abstract) → `Student`, `Admin`
- **Interfaces**: `Authenticable`, `Comparable`
- **Method Overloading**: Multiple `selectMeal()` methods in `Student`
- **Abstract Classes**: `User` class with abstract `displayDashboard()`
- **Encapsulation**: Private fields with getters/setters
- **Polymorphism**: Different implementations of `displayDashboard()`

### Design Patterns:
- **Singleton Pattern**: `DatabaseManager` for connection management
- **Session Pattern**: `UserSession` for user state management

## 📄 License

This project is created for educational purposes as part of an Object-Oriented Programming course.

## 👥 Contributing

This is an academic project. For improvements or bug fixes, please create an issue or submit a pull request.

## 📧 Support

For issues or questions:
1. Check the Troubleshooting section
2. Review the database setup
3. Verify all prerequisites are installed correctly

---

**Happy Meal Booking! 🍽️**
