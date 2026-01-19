# Banking System

A comprehensive desktop banking application built with Spring Boot and Java Swing, featuring secure authentication, role-based access control, and essential banking operations.

## Overview

The Banking System is a professional desktop application that enables multiple user roles to perform various banking operations. It provides a secure, user-friendly interface for Account Opening Officers, Tellers, and Customers to manage accounts, process deposits/withdrawals, and handle fund transfers.

## Features

- **User Authentication & Registration**: Secure login and user registration with password validation
- **Role-Based Access Control (RBAC)**: Three user roles with specific permissions:
  - Account Opening Officer (AOO)
  - Teller
  - Customer
- **Account Management**: Create and view customer accounts with automatic account number generation
- **Banking Operations**:
  - Deposits to accounts
  - Withdrawals from accounts
  - Fund transfers between accounts
- **Professional GUI**: Modern Java Swing interface with consistent styling and user-friendly dialogs
- **Secure Database**: MySQL-backed persistent storage with HikariCP connection pooling
- **Input Validation**: Comprehensive validation for all user inputs
- **Transaction Processing**: Secure transaction handling with proper error management

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 3.4.0 |
| Language | Java 17 |
| UI Framework | Java Swing |
| Database | MySQL 8.4 |
| Connection Pool | HikariCP |
| Build Tool | Maven |
| Architecture | 3-Tier MVC (Presentation, Business Logic, Data Access) |

## Prerequisites

Before running this application, ensure you have:

- **Java 17** or higher installed
- **Maven 3.6+** for build management
- **MySQL 8.4** or compatible version running
- **Git** for version control (optional)

### Installation Steps

1. **Install Java 17**
   - Download from [java.com](https://www.java.com) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java17)
   - Verify installation: `java -version`

2. **Install Maven**
   - Download from [maven.apache.org](https://maven.apache.org/download.cgi)
   - Add Maven to your system PATH
   - Verify installation: `mvn -version`

3. **Install & Start MySQL**
   - Download from [mysql.com](https://www.mysql.com/downloads/)
   - Default credentials: `root` / `password`
   - Ensure MySQL is running on `localhost:3306`

## Setup & Configuration

### 1. Database Configuration

Update the database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### 2. Build the Project

```bash
# Clean and build without running tests
mvn clean package -DskipTests -q

# Or build with test execution
mvn clean package
```

### 3. Run the Application

```bash
# Navigate to project directory
cd bankingsystem

# Run the JAR file
java -jar target/bankingsystem-0.0.1-SNAPSHOT.jar
```

The application will start and display the login screen.

## Usage Guide

### Initial Setup

1. **First Launch**: The application creates required database tables automatically
2. **Create an Account**: Click "Register" to create a new user account
3. **Select Role**: Choose appropriate role (AOO, Teller, or Customer)
4. **Login**: Use your credentials to access the system

### User Roles & Permissions

#### Account Opening Officer (AOO)
- Create new customer accounts
- View account details
- Access dashboard with system overview

#### Teller
- View customer accounts
- Process deposits
- Process withdrawals
- Process transfers between accounts

#### Customer
- View personal account information
- Limited transaction viewing

### GUI Components

| Component | Purpose |
|-----------|---------|
| Login | User authentication |
| Register | New user account creation |
| Dashboard | Main hub and role-based access control |
| CreateAccount | New account setup (AOO only) |
| Deposit | Process deposit transactions |
| Withdraw | Process withdrawal transactions |
| Transfer | Handle fund transfers |
| ViewAccount | Search and view account details |
| AccountOperations | Core transaction management |



## Testing

### Running Tests

```bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=BankingsystemApplicationTests

# Run with coverage report
mvn test jacoco:report
```

### Test Cases

| Test ID | Test Case | Status |
|---------|-----------|--------|
| TC001 | User Login | Pass |
| TC002 | User Registration | Pass |
| TC003 | Account Creation | Pass |
| TC004 | Deposit Transaction | Pass |
| TC005 | Withdrawal Transaction | Pass |
| TC006 | Fund Transfer | Pass |



## Team Contributions

### Iddy Chesire - Backend Development

**Database & Infrastructure**
- MySQL database design and schema creation
- HikariCP connection pool configuration
- Database initialization and table creation

**Backend Services**
- BankService implementation (500+ lines)
  - Account operations and transaction processing
  - Balance management and validation
  - Fund transfer logic
- UserService implementation (200+ lines)
  - User authentication and registration
  - Password validation
  - Role management

**Security & Repositories**
- Role-Based Access Control (RBAC) implementation
- UserRepository for user data persistence
- AccountRepository for account data persistence
- Exception handling and error management

### Jeremiah Nzai - Frontend Development

**GUI Architecture & Design**
- Modern Java Swing interface
- Professional color scheme (blues, greens, reds)
- Consistent styling across all components

**GUI Components (2,500+ lines)**
- LoginGUI.java (180 lines) - Authentication interface
- RegisterGUI.java (250 lines) - User registration
- DashboardGUI.java (350 lines) - Main application hub
- DepositGUI.java, WithdrawGUI.java, TransferGUI.java - Transaction dialogs
- ViewAccountGUI.java - Account search and details
- CreateAccountGUI.java - New account creation
- AccountOperationsGUI.java - Core operations management

**UI Enhancements**
- Professional text-based interface (no emojis)
- JOptionPane dialogs for user feedback
- Hover effects and interactive styling
- Card-based layouts for better UX
- Optimized dialog sizes for usability
- Segoe UI font throughout

## Building & Deployment

### Quick Start

```bash
# 1. Navigate to project directory
cd bankingsystem

# 2. Build the project
mvn clean package -DskipTests -q

# 3. Run the application
java -jar target/bankingsystem-0.0.1-SNAPSHOT.jar
```

### Maven Commands

```bash
# Clean the project
mvn clean

# Compile the source code
mvn compile

# Run tests
mvn test

# Build JAR file
mvn package

# Skip tests during build
mvn package -DskipTests

# Install dependencies
mvn install

# Run the application directly (requires GUI support)
mvn spring-boot:run
```

## Troubleshooting

### Application Won't Start

**Issue**: "Connection refused" error
- **Solution**: Ensure MySQL is running and accessible on localhost:3306

**Issue**: Database table doesn't exist
- **Solution**: Check application.properties `spring.jpa.hibernate.ddl-auto=create-drop` is set

**Issue**: Port already in use
- **Solution**: Change port in application.properties: `server.port=8081`

### Login Issues

**Issue**: Cannot login with correct credentials
- **Solution**: Ensure database connection is working; check MySQL logs

**Issue**: "User not found"
- **Solution**: Register a new account first using the Register button

### Database Issues

**Issue**: "No tables in database"
- **Solution**: Restart the application; ensure ddl-auto is set to create-drop

**Issue**: Connection pool exhausted
- **Solution**: Increase connection pool size in application.properties

## Future Enhancements

- Mobile application support
- REST API for third-party integration
- Transaction history and analytics
- Advanced reporting features
- Multi-currency support
- Enhanced security (2FA, encryption)
- Audit logging and compliance tracking
- Real-time notifications



## License

This project is developed for educational purposes as part of the Advanced Programming course at BIT (Business Information Technology).

## Support & Contact

For issues, questions, or contributions, please contact the development team:
- **Backend**: Iddy Chesire
- **Frontend**: Jeremiah Nzai

## Changelog

### Version 1.0.0
- Initial release
- Core banking operations implemented
- Role-based access control
- Professional GUI interface
- Comprehensive testing suite

---

**Last Updated**: January 2026
**Status**: Production Ready
