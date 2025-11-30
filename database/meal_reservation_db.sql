-- Drop existing database if exists (for clean setup)
DROP DATABASE IF EXISTS meal_reservation_db;

-- Create Database
CREATE DATABASE meal_reservation_db;
USE meal_reservation_db;

-- Table 1: User
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('Student', 'Admin') DEFAULT 'Student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 2: Meal
CREATE TABLE meal (
    meal_id INT PRIMARY KEY AUTO_INCREMENT,
    meal_name VARCHAR(100) NOT NULL,
    meal_type ENUM('Healthy', 'Veg', 'Non-Veg', 'Vegan') NOT NULL,
    meal_time ENUM('Breakfast', 'Lunch', 'Dinner', 'Snacks') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 3: Meal Selection
CREATE TABLE meal_selection (
    selection_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    meal_id INT NOT NULL,
    selection_date DATE NOT NULL,
    meal_time ENUM('Breakfast', 'Lunch', 'Dinner', 'Snacks') NOT NULL,
    status ENUM('Booked', 'Cancelled') DEFAULT 'Booked',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (meal_id) REFERENCES meal(meal_id) ON DELETE CASCADE,
    UNIQUE KEY unique_booking (user_id, selection_date, meal_time)
);

-- Breakfast Meals
INSERT INTO meal (meal_name, meal_type, meal_time, price) VALUES
('Scrambled Eggs & Toast', 'Non-Veg', 'Breakfast', 80.00),
('Oatmeal with Fruits', 'Healthy', 'Breakfast', 70.00),
('Poha', 'Veg', 'Breakfast', 60.00),
('Vegan Smoothie Bowl', 'Vegan', 'Breakfast', 75.00);

-- Lunch Meals
INSERT INTO meal (meal_name, meal_type, meal_time, price) VALUES
('Chicken Biryani', 'Non-Veg', 'Lunch', 200.00),
('Dal Tadka Combo', 'Veg', 'Lunch', 100.00),
('Grilled Chicken Salad', 'Healthy', 'Lunch', 150.00),
('Vegan Buddha Bowl', 'Vegan', 'Lunch', 140.00);

-- Dinner Meals
INSERT INTO meal (meal_name, meal_type, meal_time, price) VALUES
('Mutton Curry', 'Non-Veg', 'Dinner', 220.00),
('Paneer Butter Masala', 'Veg', 'Dinner', 120.00),
('Fish Curry Rice', 'Non-Veg', 'Dinner', 180.00),
('Tofu Stir Fry', 'Vegan', 'Dinner', 160.00);

-- Snacks
INSERT INTO meal (meal_name, meal_type, meal_time, price) VALUES
('Samosa (2 pcs)', 'Veg', 'Snacks', 40.00),
('Chicken Wings', 'Non-Veg', 'Snacks', 120.00),
('Fruit Salad', 'Healthy', 'Snacks', 80.00),
('Vegan Burger', 'Vegan', 'Snacks', 145.00);


-- View all users
SELECT * FROM user;

-- View all meals grouped by meal time
SELECT meal_time, meal_name, meal_type, price FROM meal ORDER BY meal_time, meal_type;

-- View all bookings with details
SELECT
    ms.selection_id,
    u.name AS student_name,
    m.meal_name,
    m.meal_type,
    m.meal_time,
    m.price,
    ms.selection_date,
    ms.status
FROM meal_selection ms
INNER JOIN user u ON ms.user_id = u.user_id
INNER JOIN meal m ON ms.meal_id = m.meal_id
ORDER BY ms.selection_date DESC, m.meal_time;

-- Count meals by type
SELECT meal_type, COUNT(*) as count
FROM meal
GROUP BY meal_type;

-- Total bookings per student
SELECT
    u.name,
    COUNT(ms.selection_id) as total_bookings,
    SUM(CASE WHEN ms.status = 'Booked' THEN m.price ELSE 0 END) as total_spent
FROM user u
         LEFT JOIN meal_selection ms ON u.user_id = ms.user_id
         LEFT JOIN meal m ON ms.meal_id = m.meal_id
WHERE u.role = 'Student'
GROUP BY u.user_id, u.name;
