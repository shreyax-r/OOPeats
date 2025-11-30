# Quick Setup Guide for Windows

## Step-by-Step Installation

### 1. Install Java JDK 11+

1. Download JDK from: https://www.oracle.com/java/technologies/downloads/
2. Install it (default location: `C:\Program Files\Java\jdk-11`)
3. Verify installation:
   ```cmd
   java -version
   javac -version
   ```

### 2. Install MySQL Server

1. Download MySQL Installer from: https://dev.mysql.com/downloads/installer/
2. Run the installer
3. Choose **Developer Default** or **Server only**
4. During installation:
   - Set root password to: `Shreya2006`
   - Remember to start MySQL Server as a Windows service
5. Verify installation:
   ```cmd
   mysql --version
   ```

### 3. Setup Database

**Option A: Using MySQL Command Line**
```cmd
mysql -u root -p
```
Enter password: `Shreya2006`

Then in MySQL:
```sql
source C:/Users/rshre/OOPeats/database/meal_reservation_db.sql
```

**Option B: Using MySQL Workbench**
1. Open MySQL Workbench
2. Connect to localhost (root, password: Shreya2006)
3. File → Open SQL Script → Select `database/meal_reservation_db.sql`
4. Execute the script (⚡ icon)

### 4. Compile the Project

Open Command Prompt in the project folder:
```cmd
cd C:\Users\rshre\OOPeats
```

**Simple compilation (if supported):**
```cmd
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out -sourcepath src src\**\*.java
```

**Manual compilation (if above fails):**
```cmd
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Database\*.java
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Exceptions\*.java
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Utils\*.java
javac -cp "lib\mysql-connector-j-9.5.0.jar;out" -d out src\Models\*.java
REM Compile Login and Home together to resolve circular dependency
javac -cp "lib\mysql-connector-j-9.5.0.jar;out" -d out src\Login\*.java src\Home\*.java
```

### 5. Run the Application

```cmd
java -cp "out;lib\mysql-connector-j-9.5.0.jar" Login.Login
```

## Quick Test

1. The login window should appear
2. Try logging in with:
   - Username: `admin`
   - Password: `admin@123`

## Common Issues

### "MySQL not found"
- Start MySQL service: `net start MySQL80`
- Or: Services → MySQL80 → Start

### "Class not found"
- Make sure you compiled all files
- Check that `out` folder contains `.class` files

### "Connection refused"
- Verify MySQL is running
- Check password in `DatabaseManager.java` is `Shreya2006`
- Verify database `meal_reservation_db` exists

### "Access denied"
- Check MySQL root password matches `Shreya2006`
- Or update password in `src/Database/DatabaseManager.java`

## Creating a Batch File (Optional)

Create `run.bat` in the project root:
```batch
@echo off
echo Starting OOPeats...
java -cp "out;lib\mysql-connector-j-9.5.0.jar" Login.Login
pause
```

Double-click `run.bat` to start the application!

