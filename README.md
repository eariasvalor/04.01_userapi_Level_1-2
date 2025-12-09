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

------------------------------------------------------------------------

## ⭐⭐⭐ Level 3 -- Refactoring to a Layered Architecture

After completing the basic API and in-memory data management, I
refactored the application to follow a **clean layered architecture**,
improving maintainability, separation of concerns, and adherence to
SOLID principles.

### 🎯 Why refactor?

In the previous version, `UserController` was responsible for:

-   Handling HTTP requests
-   Implementing business logic
-   Accessing and modifying data directly

This violated:

-   **S --- Single Responsibility Principle**
-   **D --- Dependency Inversion Principle**

The solution was to separate responsibilities into **three distinct
layers**:

-   **Controller** → receives HTTP requests and delegates work
-   **Service** → contains business logic and use cases
-   **Repository** → manages data access

------------------------------------------------------------------------

## 🧪 Step 1 --- Convert existing test to an integration test

Before starting the refactor, I converted the existing Web MVC test into
a **full integration test**, to ensure that after reorganising the code,
**everything still worked end-to-end**.

### ✔️ What changed

-   Renamed the test class: `UserIntegrationTest`
-   Removed `@WebMvcTest`
-   Added:

```{=html}
<!-- -->
```
    @SpringBootTest
    @AutoConfigureMockMvc

### 🎯 Purpose

This test now verifies that:

-   Controller + Service + Repository work correctly together

This gave me confidence to refactor safely.

------------------------------------------------------------------------

## 📦 Step 2 --- Repository Pattern

To decouple business logic from data storage, I introduced a
**Repository interface**.

### 🔧 Repository interface

``` java
public interface UserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(UUID id);
    List<User> searchByName(String name);
    boolean existsByEmail(String email);
}
```

### 💾 Implementation

I implemented the interface using an in-memory list:

``` java
@Repository
public class InMemoryUserRepository implements UserRepository {
    // internal list
}
```

This class now handles all access to user data.

🧪 Repository tests were added to ensure correct behaviour.

------------------------------------------------------------------------

## ⚙️ Step 3 --- Service Layer

Next, I introduced a **Service Layer** to host all business logic.

### ✔️ Interface

``` java
public interface UserService {
    User create(User user);
    List<User> getAll();
    User getById(UUID id);
    List<User> searchByName(String name);
}
```

### ✔️ Implementation

``` java
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }
}
```

The controller now delegates all logic to the service:

``` java
@GetMapping("/users")
public List<User> getUsers() {
    return userService.getAll();
}
```

### 🎯 Results

-   Controller is clean and focused
-   Logic is reusable and testable
-   Data access is encapsulated

------------------------------------------------------------------------

## 🧪 Step 4 --- Service Unit Tests (Mockito)

Once the business logic was isolated in the service layer, I wrote
**unit tests** using Mockito.

### 🎯 Use case: unique email validation

New business rule:

✔️ Before creating a user, check if the email already exists\
✔️ If it exists → throw `EmailAlreadyExistsException`\
✔️ If not → assign UUID and save

### 🧪 Test example (TDD)

``` java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        User user = new User (UUID.randomUUID(), "John Doe", "john@example.com");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals("Email already registered: john@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }
}

```

------------------------------------------------------------------------

## 📌 Conclusion

This task allowed me to practise the fundamentals of a REST API with
Spring Boot:

-   JSON request and response handling- Controller development-
    Automated testing with MockMvc- Running the application as an
    executable `.jar`- Refactoring into a clean layered architecture

✔️ The application executed correctly, and all tests passed.


