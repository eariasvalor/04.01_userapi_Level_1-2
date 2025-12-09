# 📘 Task S4.01 -- Spring Boot REST API

## 🎯 Overview

This document summarises the work completed for **Task S4.01**.\
The objective was to create a basic REST API using **Spring Boot**,
return JSON responses, and apply testing and execution procedures in a
structured way.

------------------------------------------------------------------------

## 🧱 Project Setup

The project was generated from **start.spring.io** using:

-   🔧 Spring Boot 3.x\
-   ☕ Java 21\
-   📦 Maven\
-   📚 Dependencies: Spring Web and DevTools

The application port was configured in `application.properties`:

    server.port=9000

------------------------------------------------------------------------

## ⭐ Level 1 -- Health Check Endpoint

A first endpoint was created to confirm that the application started
correctly:

-   📁 Package: `controllers`
-   🧭 Controller: `HealthController`
-   🔌 Mapping: `GET /health`
-   📤 Response: `{ "status": "OK" }`

This endpoint was tested:

-   🌐 in the browser\
-   📮 with Postman\
-   🧪 using a basic test with MockMvc

------------------------------------------------------------------------

## 🚀 Running the Application

The application was packaged and executed as a `.jar`:

    mvn clean package
    java -jar target/userapi-0.0.1-SNAPSHOT.jar

✔️ The endpoint continued working correctly.

------------------------------------------------------------------------

## ⭐⭐ Level 2 -- User Management (In Memory)

A simple user management system was implemented **without a database**.\
Data was stored temporarily in a static list.

### Implemented Features

-   🧑‍💻 Model: `User { id, name, email }`
-   🔧 Endpoints:
    -   `GET /users` → list all users
    -   `POST /users` → create a new user (UUID generated)
    -   `GET /users/{id}` → retrieve by ID, return 404 if not found
    -   `GET /users?name=` → optional filtering by name

All endpoints were verified using Postman.

------------------------------------------------------------------------

## 🧪 Testing

Controller tests were created using:

-   🧪 `@WebMvcTest`
-   ⚙️ `MockMvc`
-   🔄 `ObjectMapper`

Verified behaviours included:

-   Correct listing of users
-   Creation of users with UUID
-   Correct retrieval by ID
-   404 when the ID did not exist
-   Correct filtering using `?name=` parameter

✔️ All tests passed successfully.

------------------------------------------------------------------------

## 📌 Conclusion

This task allowed me to practise the fundamentals of a REST API with
Spring Boot:

-   JSON request and response handling\
-   Controller development\
-   Automated testing with MockMvc\
-   Running the application as an executable `.jar`

✔️ The application executed correctly, and all tests passed.


