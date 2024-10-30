Delivery Management System
A Spring Boot-based web application to manage delivery orders efficiently, built for an Object-Oriented Programming course project. The system allows users to create, manage, and track delivery orders while enabling a distributor (administrator) to oversee and update order statuses in real-time.

Table of Contents
Features
Technologies Used
Project Structure
Setup and Installation
Usage
OOP Principles Applied
Future Enhancements
Features
User Features
User Registration and Login: Users can create accounts with basic information such as name, phone number, and password.
Order Creation: Users can create delivery orders by providing:
Recipient information (name, phone number, address)
Item details (name, weight, quantity, value)
Expected delivery date
Delivery service selection (Service A or B)
Order Management:
View sent and received orders in the /manage-order section.
Toggle between "Sent Orders" and "Received Orders" within the same page, displaying basic order details.
Click to view detailed information about each order.
Distributor (Admin) Features
Order Management: The distributor (specifically "GiaoHangCham") has exclusive access to manage all orders within the system.
Order Status Update: Only the distributor can update the status of orders in real-time, ensuring accurate tracking and visibility.
Technologies Used
Backend: Java with Spring Boot, Spring Security for authentication and authorization
Frontend: Thymeleaf for templating
Database: MySQL for data persistence
Build Tool: Maven
Project Structure
bash
Copy code
src/main/java/com/ndc/deliverymanagement
├── controller         # REST controllers for handling requests
│   ├── UserController.java
│   └── OrderController.java
├── model              # Domain models (entities)
│   ├── User.java
│   └── Order.java
├── repository         # Repository interfaces for database operations
│   ├── UserRepository.java
│   └── OrderRepository.java
└── service            # Service layer for business logic
    ├── UserService.java
    └── OrderService.java
Setup and Installation
Clone the Repository:

bash
Copy code
git clone https://github.com/your-username/delivery-management-system.git
cd delivery-management-system
Configure Database:

Set up a MySQL database.
Configure database connection details in src/main/resources/application.properties.
Build and Run:

bash
Copy code
mvn spring-boot:run
Access the Application: Open a browser and go to http://localhost:8080/.

Usage
Register and Login:

New users can register via the registration page.
Login to access order creation and management features.
Order Creation:

Go to /create-order to enter order details.
Submit the form to save the order in the database.
Manage Orders:

Go to /manage-order to view sent and received orders.
Distributor users can update the status of any order they manage.
OOP Principles Applied
Encapsulation: Business logic is encapsulated within service classes (UserService, OrderService), and data access is separated into repository interfaces.
Abstraction: Key entities (User, Order) represent core elements of the system, while controllers abstractly handle request-response logic.
Inheritance: If additional roles are required in the future, inheritance can be used to extend user functionality.
Polymorphism: Methods such as updateOrderStatus can exhibit polymorphic behavior, allowing flexibility in handling order updates.
Future Enhancements
Real-Time Notifications: Notify users of order status changes.
Enhanced Security: Implement more granular access control.
Detailed Logging: Track order status changes and other critical events.
