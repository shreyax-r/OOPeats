@echo off
echo ========================================
echo    OOPeats - Meal Reservation System
echo ========================================
echo.

REM Check if compiled classes exist
if not exist "out\Login\Login.class" (
    echo Compiling project...
    echo.
    javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Database\*.java
    javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Exceptions\*.java
    javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out src\Utils\*.java
    javac -cp "lib\mysql-connector-j-9.5.0.jar;out" -d out src\Models\*.java
    REM Compile Login and Home together to resolve circular dependency
    javac -cp "lib\mysql-connector-j-9.5.0.jar;out" -d out src\Login\*.java src\Home\*.java
    echo.
    echo Compilation complete!
    echo.
)

echo Starting application...
echo.
java -cp "out;lib\mysql-connector-j-9.5.0.jar" Login.Login

if errorlevel 1 (
    echo.
    echo ========================================
    echo Error: Application failed to start
    echo ========================================
    echo.
    echo Troubleshooting:
    echo 1. Make sure MySQL Server is running
    echo 2. Verify database 'meal_reservation_db' exists
    echo 3. Check that lib\mysql-connector-j-9.5.0.jar exists
    echo.
    pause
)

