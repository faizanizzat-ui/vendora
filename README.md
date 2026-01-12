# E-Commerce Management System

## Project Information

**Subject:** Object Oriented Programming  
**Semester:** 3rd Semester  
**Department:** Computer Science and Software Engineering  
**Institution:** COMSATS University Islamabad, Abbottabad Campus  
**Instructor:** Sir Ahmed Ali  
**Project Type:** Semester Project

---

## Team Members

| Name             | Registration Number 
| ---------------- | ------------------- 
| Faizan izzat     | FA24-BSE-121       
| Ali Hasnat       |       
| Tanveer Hasssan  |        

---

## Project Overview

The **E-Commerce Management System** is a comprehensive Java-based application that simulates an online shopping platform with both customer and administrative functionalities. This project demonstrates core Object-Oriented Programming (OOP) concepts including:

- **Inheritance** - User class hierarchy (Customer, Admin)
- **Polymorphism** - Interface implementation and method overriding
- **Encapsulation** - Private fields with public getters/setters
- **Abstraction** - Abstract User class and Manageable interface
- **File I/O** - Persistent data storage for users, products, and transactions
- **GUI Design** - Modern Glassmorphism UI with Java Swing

### What the Project Does

The system provides two main user interfaces:

#### 1. Customer Interface

- Browse available products with real-time stock information
- Add products to shopping cart
- View cart with total price calculation
- Checkout and complete purchases
- View personal transaction history
- User registration and secure login

#### 2. Admin Interface

- Manage product inventory (Add/Remove products)
- Update product stock and pricing
- View all registered users
- Remove users from the system
- View complete payment history
- Track user purchase history
- Monitor all system transactions

### Key Features

- **Modern Glassmorphism UI** - Sleek dark-themed interface with frosted glass effects
- **Secure Authentication** - User login system with role-based access control
- **Data Persistence** - Automatic save/load functionality using file storage
- **Shopping Cart** - Full cart management with quantity adjustments
- **Transaction Tracking** - Complete order history with timestamps
- **Real-time Updates** - Live product availability and pricing
- **Input Validation** - Robust error handling and user input validation
- **Responsive Design** - Clean and intuitive user interface

---

## Technologies Used

- **Language:** Java (JDK 8 or higher)
- **GUI Framework:** Java Swing
- **Graphics:** Java AWT (Advanced Window Toolkit)
- **Data Storage:** File I/O (Text-based storage)
- **Design Pattern:** MVC-inspired architecture

---

## Project Structure

```
ECommerce_Management_System/
│
├── src/
│   ├── ECommerceApp.java      # Main application with core classes
│   ├── ECommerceGUI.java      # GUI implementation with Swing
│   ├── ProductCart.java       # (Additional classes if present)
│   ├── Transaction.java       # (Transaction handling)
│   └── UserClasses.java       # (User-related classes)
│
├── bin/                       # Compiled .class files
├── lib/                       # External libraries (if any)
├── data/                      # Data files (auto-generated)
│   ├── users.txt             # User credentials and info
│   ├── products.txt          # Product inventory
│   └── transactions.txt      # Transaction history
│
└── README.md                 # Project documentation
```

---

## How to Run the Project

### Prerequisites

- Java Development Kit (JDK) 8 or higher installed
- Java Runtime Environment (JRE) configured
- Terminal/Command Prompt access

### Compilation Steps

1. **Open Terminal/Command Prompt**

2. **Navigate to Project Directory**

   ```bash
   cd "C:\Users\user\OneDrive - Higher Education Commission\Desktop\ECommerce_Management_System"
   ```

3. **Compile the Source Files**

   ```bash
   javac -d bin src\*.java
   ```

4. **Run the Application**
   ```bash
   java -cp bin ECommerceApp
   ```

### Alternative: Using IDE

#### For IntelliJ IDEA:

1. Open the project folder
2. Right-click on `ECommerceApp.java`
3. Select "Run 'ECommerceApp.main()'"

#### For Eclipse:

1. Import as existing Java project
2. Right-click on `ECommerceApp.java`
3. Select "Run As" → "Java Application"

#### For VS Code:

1. Open the project folder
2. Install "Extension Pack for Java"
3. Open `ECommerceApp.java`
4. Click "Run" above the main method

---

## Default Login Credentials

### Admin Account

- **Username:** `admin`
- **Password:** `admin123`

### Customer Account (Create via Registration)

- Click "Register" on the welcome screen
- Fill in your details to create a new customer account

---

## Usage Guide

### For Customers:

1. **Register/Login**

   - Launch the application
   - Click "Register" to create a new account or "Login" with existing credentials

2. **Browse Products**

   - View all available products with prices and stock information
   - Products are displayed in a clean table format

3. **Shopping**

   - Enter Product ID and quantity
   - Click "Add to Cart"
   - View cart contents and total price
   - Click "Checkout" to complete purchase

4. **View History**
   - Check your past transactions and purchases

### For Administrators:

1. **Login**

   - Use admin credentials to access admin panel

2. **Product Management**

   - Add new products with ID, name, price, and stock
   - Remove products from inventory
   - Update product information

3. **User Management**

   - View all registered users
   - Remove users if necessary

4. **Reports**
   - View complete payment history
   - Track user purchase patterns
   - Monitor system transactions

---

## Features Showcase

### Object-Oriented Concepts Implemented

1. **Classes & Objects**

   - User, Customer, Admin
   - Product, Cart, CartItem
   - Transaction, EcommerceSystem

2. **Inheritance**

   - User (Abstract) → Customer, Admin

3. **Polymorphism**

   - Method overriding (display(), getType())
   - Interface implementation (Manageable)

4. **Encapsulation**

   - Private fields with public accessor methods
   - Protected data with controlled access

5. **Abstraction**

   - Abstract User class
   - Manageable interface

6. **Collections Framework**

   - ArrayList for dynamic data storage
   - HashMap for efficient lookups

7. **Exception Handling**

   - Try-catch blocks for robust error management
   - Input validation and error recovery

8. **File I/O**
   - Persistent data storage
   - Reading and writing user/product/transaction data

---

## Data Persistence

The system automatically creates and manages three data files:

- **users.txt** - Stores user credentials and types
- **products.txt** - Maintains product inventory
- **transactions.txt** - Records all completed transactions

Data is automatically loaded on startup and saved on exit.

---

## Future Enhancements

- Database integration (MySQL/PostgreSQL)
- Payment gateway integration
- Product categories and search functionality
- Order status tracking (Processing, Shipped, Delivered)
- Product ratings and reviews
- Email notifications
- Advanced reporting and analytics
- Multi-currency support

---

## Troubleshooting

### Common Issues:

1. **"Class not found" error**

   - Ensure you're in the correct directory
   - Check that files are compiled in the `bin` folder

2. **GUI not displaying properly**

   - Update to latest JDK version
   - Check system compatibility with Swing

3. **Data not persisting**
   - Ensure write permissions in project directory
   - Check if data folder exists

---

## License

This project is developed as an academic assignment for educational purposes at COMSATS University Islamabad, Abbottabad Campus.

---

## Contact Information

For queries or support regarding this project, please contact:

- **Group Leader:** Ibadat Ullah (FA24-BSE-116)
- **Instructor:** Sir Nauman Khan
- **Department:** Computer Science and Software Engineering
- **Institution:** COMSATS University Islamabad, Abbottabad Campus

---

## Acknowledgments

Special thanks to **Sir Nauman Khan** for guidance and support throughout the development of this project, and to all team members for their contributions and dedication.

---

**Note:** This is a semester project demonstrating Object-Oriented Programming concepts learned during the 3rd semester at COMSATS University Islamabad, Abbottabad Campus.
