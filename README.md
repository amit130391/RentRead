# RentRead
RESTful API service using Spring Boot to manage an online book rental system while using MySQL to persist the data.The service uses Basic Auth.

## Postman Collection

You can import the Postman collection for this project using the following link:

[Postman Collection](https://elements.getpostman.com/redirect?entityId=30015848-9991c801-b66f-4113-b7f7-e9c8367d6ac9&entityType=collection)

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contact](#contact)

## Features
- **User Registration**: Users can register and manage their accounts.
- **Book Management**: Admins can create, update, and delete books. Users can only view the list of available books.
- **Book Renting**: Users can rent available books.
- **Book Returning**: Users can return rented books.

## Technologies Used
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- RESTful API
- MySQL

## Installation
### Prerequisites
- Java Development Kit (JDK) 11 or higher
- MySql
- Gradle
- Git

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/amit130391/RentRead.git
   cd RentRead
2. Configure MySql:
   Ensure MySql is running on your local machine or update the connection details in application.properties if using a remote MySql instance.
3. Build and run the application:
   ./gradlew clean build
   ./gradlew bootRun

## Usage
1. The application runs on http://localhost:8080.
2. Use the API endpoints to manage user,books and rental service.
3. Use Postman or any other API client to interact with the endpoints.

## API Endpoints
### User Endpoints
1. GET /admin/users - Retrieve a list of all user, only admin can view all the users.
2. GET user/me - Retrieve the details of an authenticated user.
3. POST /register - Registers a new user.
### Book Endpoints
1. GET /books - Retrieve a list of all books.
2. POST /admin/book - Save a new book in the DB. Only admin can save a new book.
3. PUT /admin/book/{bookId}?status={Status} - Updates the given book. Only admin is allowed to update.
4. DELETE /book/{bookId} - Deletes a book with the given ID.
### Rental Endpoints
1. POST /user/me/books/{bookId}/rent - Rent a book with the given Id for the authenticated user. Shows required error if the book is already rented or is unvailable or user has already rented 2 books. 
2. POST /user/me/books/{bookId}/return - Return a book with the given Id for the authenticated user. Shows required error if the book is not rented by the user or if there is no such book.
   
## Contact
If you have any questions or suggestions, feel free to contact me:

    Name: Amit Sharma
    Email: amit130391@gmail.com
    GitHub: amit130391 

