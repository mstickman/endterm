Library Management REST API

Project Overview
This project is a RESTful Library Management API developed using Java Spring Boot and PostgreSQL.
The purpose of the project is to demonstrate advanced Object-Oriented Programming principles, JDBC-based database access, layered architecture, and proper exception handling.

The API allows managing books of different types (EBook and PrintedBook) that belong to categories.
All data is stored in a relational database and accessed using JDBC.

Architecture Overview
The application follows a layered architecture:
Controller - Service - Repository - Database


Controller layer handles HTTP requests and responses (REST endpoints)
Service layer contains business logic and validation
Repository layer interacts with the database using JDBC
Database stores persistent data (PostgreSQL)

OOP Design
BookBase (abstract)
Common fields: id, name, author, price, category
Abstract method: getEntityType()
Concrete validation logic

Subclasses
EBook
Additional field: fileSizeMb

PrintedBook
Additional field: pages

Polymorphism is demonstrated by working with BookBase references while creating and processing different book types.

Interfaces and Design Patterns

Factory Pattern
BookFactory
Creates EBook or PrintedBook objects based on request data
Hides object creation logic from the service layer

Composition
Book - Category
Each book contains a Category object
Category is stored as a separate table and referenced using a foreign key

Database Design
<img width="298" height="74" alt="image" src="https://github.com/user-attachments/assets/22596e46-1a81-4b96-8899-9369c4911ea8" />
<img width="478" height="263" alt="image" src="https://github.com/user-attachments/assets/ebe15cf7-e510-45ab-8b09-398f862374da" />

Constraints
Primary keys for all tables
Foreign keys with referential integrity
Unique constraint on (name, author) to prevent duplicate books

REST API Endpoints

GET	{url}/api/books	Get all books
GET	{url}/api/books/{id}	Get book by ID
POST	{url}/api/books	Create new book
PUT	{url}/api/books/{id}	Update existing book
DELETE	{url}/api/books/{id}	Delete book

Exception Handling
InvalidInputException → 400 Bad Request
ResourceNotFoundException → 404 Not Found
DuplicateResourceException → 409 Conflict
DatabaseOperationException → 500 Internal Server Error

Error responses include:
HTTP status
error message
request path

How to Run the Project

Requirements: Java 17+, Maven, PostgreSQL

Create PostgreSQL database
Update application.properties with DB credentials
Run the application by intellij


API will be available at:
http://localhost:8080

Postman Demonstration
All API functionality was tested using Postman.

Reflection

During this project, I improved my understanding of: Layered backend architecture, JDBC and transaction management, OOP principles in real-world applications, REST API design and error handling, Debugging database and SQL-related issues

This project demonstrates how clean architecture and proper separation of concerns improve maintainability and scalability of backend systems.
