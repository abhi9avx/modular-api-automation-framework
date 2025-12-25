# Modular API Automation Framework

## 📌 Overview
This is a **production-ready, modular API Automation Framework** built using **Java** and **RestAssured**. It is designed to be scalable, maintainable, and readable, following industry best practices and standard design patterns.

The framework completely decouples the **Test Layer** (Validation) from the **Logic Layer** (API Calls) and **Data Layer** (POJOs), ensuring that changes in API endpoints or payloads usually require updates in only one place.

---

## 🛠️ Tech Stack & Key Tools
*   **Language**: Java 17+
*   **API Library**: [RestAssured](https://rest-assured.io/) - For making HTTP requests and validating responses.
*   **Test Runner**: [TestNG](https://testng.org/) - For execution control, parallel testing, and reporting hooks.
*   **Build Tool**: [Gradle](https://gradle.org/) - For dependency management and build automation.
*   **Reporting**: [Allure Reports](https://allurereport.org/) - For visualization of test results.
*   **Boilerplate Reduction**: [Lombok](https://projectlombok.org/) (`@Data`, `@Builder`) - To reduce code noise (getters/setters).
*   **JSON Parsing**: [Jackson](https://github.com/FasterXML/jackson) (`ObjectMapper`) - For Serialization/Deserialization.
*   **Logging**: [SLF4J](https://www.slf4j.org/) + [Logback](https://logback.qos.ch/) - For structured logging.

---

## 📂 Project Structure & Deep Dive
This project follows a strict **Layered Modular Architecture**. Each package has a specific responsibility.

```text
src
├── main
│   └── java
│       └── com.abhinav.framework
│           ├── client       # [Logic Layer] Wraps RestAssured. Handles low-level HTTP calls.
│           ├── config       # [Config Layer] Environment variables and endpoint constants.
│           ├── controller   # [Business Layer] Logic bridge. Prepares requests for specific features.
│           ├── dto          # [Data Layer] POJOs for JSON Serialization/Deserialization.
│           ├── enums        # [Constants] Type-safe constant values (e.g., User Roles).
│           └── utils        # [Helpers] Generic utilities (JSON parsing, Random data, Logging).
└── test
    └── java
        └── com.abhinav.tests # [Test Layer] Contains ONLY validation logic (@Test).
    └── resources
        └── testng.xml        # [Execution Control] Defines Test Suites and Parallelism.
```

### 🔹 1. `dto` (Data Transfer Objects)
*   **Key Files**: `UserRequest.java`, `UserResponse.java`
*   **What**: Simple Java classes (POJOs) that represent the JSON Body of Requests and Responses.
*   **Why**: standardizes data format. Instead of `map.put("name", "john")`, we use `UserRequest.builder().name("john").build()`.
*   **Role**: **Jackson** converts these Java Objects to JSON string automatically before sending the request.

### 🔹 2. `client` (API Client Layer)
*   **Key Files**: `ApiClient.java`, `UserClient.java`
*   **What**: The "Engine Room". It bridges our code with the external **RestAssured** library.
*   **Role**:
    *   `ApiClient` contains generic static methods (`get`, `post`, `put`) that take a URL and Headers.
    *   It abstracts the library-specific syntax (`given().when().then()`) away from the rest of the framework.

### 🔹 3. `controller` (Business Logic Layer)
*   **Key Files**: `UserController.java`, `PostController.java`
*   **What**: The "Brain". It orchestrates *how* a specific feature should work.
*   **Role**:
    *   It knows the specific **Endpoint URL** (from `EndpointConfig`).
    *   It adds specific **Headers** (like `Content-Type`).
    *   It calls the `ApiClient` to execute the request.

### 🔹 4. `config` (Configuration Layer)
*   **Key Files**: `EnvironmentConfig.java`, `EndpointConfig.java`
*   **What**: Central storage for configuration.
*   **Role**:
    *   `EnvironmentConfig` reads `application.properties` to find out if we are running on **QA**, **DEV**, or **PROD**.
    *   `EndpointConfig` stores static strings like `/users` to prevent typos.

### 🔹 5. `test` (Test Layer)
*   **Key Files**: `BaseTest.java`, `CreateUserApiTest.java`
*   **What**: The actual validation scripts.
*   **Role**:
    *   **`BaseTest.java`**: The Parent class. It runs `@BeforeSuite` setups (like initializing logs). All test classes extend this.
    *   **`testng.xml`**: The Control Center. It tells TestNG which tests to run and if they should run in **parallel** (simultaneously).

---

## 🏗️ Design Patterns Used
This is a critical section for your interviews. Be ready to explain *where* these are used.

1.  **Builder Pattern**: Used in DTOs (`UserRequest.builder()...`) for clean object creation.
2.  **Singleton Pattern**: Used in `EnvironmentConfig` to load properties only once.
3.  **Facade Pattern**: The `Controller` acts as a facade, hiding the complexity of the `Client` and `RestAssured` from the properties.
4.  **POJO/DTO Pattern**: Separation of Data (DTO) from Logic (Controller).

---

## 🎓 Ready: Q&A (Deep Dive)

### 🔹 TestNG
**Q1: Why do we use TestNG over JUnit?**
> **Answer**: TestNG offers advanced features critical for API automation, such as:
> *   **Parallel Execution**: Ability to run tests in parallel threads (`thread-count="3"` in `testng.xml`) to save time.
> *   **Prioritization**: `@Test(priority=1)` ensures orderly execution.
> *   **Grouping**: We can group tests (`@Test(groups="smoke")`) and run only specific sets.
> *   **Data Providers**: Easy way to run the same test with multiple data sets (Data-Driven Testing).

**Q2: What is the purpose of `testng.xml`?**
> **Answer**: It is the configuration file for the test runner. It allows us to:
> *   Define **Test Suites** and **Test Names**.
> *   Configure **Parallel Execution** (Classes vs Methods).
> *   Include/Exclude specific packages or classes.
> *   Pass parameters (like URL or Env) to the tests at runtime.

**Q3: How does `BaseTest` work?**
> **Answer**: `BaseTest` is a parent class that holds common setup and teardown logic. It uses annotations like `@BeforeSuite` to run code once before any test starts (e.g., setting up Loggers or Reports). All test classes extend `BaseTest` so they inherit this setup automatically without duplicating code.

### 🔹 Gradle
**Q4: Why Gradle instead of Maven?**
> **Answer**:
> *   **Performance**: Gradle is significantly faster due to its incremental build features and build cache.
> *   **Flexibility**: Gradle uses a Groovy/Kotlin DSL which is more powerful than Maven's XML structure.
> *   **Dependency Management**: Gradle handles transitive dependencies better and allows concise configuration.

**Q5: What commands do you use typically?**
> *   `./gradlew clean build`: Deletes old artifacts and builds the project fresh.
> *   `./gradlew test`: Runs all tests.
> *   `./gradlew allureServe`: Generates and serves the Allure HTML report.

### 🔹 Framework Design
**Q6: How do you handle Thread Safety in Parallel Execution?**
> **Answer**: Since we run tests in parallel (configured in `testng.xml`), we must ensure our shared resources are thread-safe.
> *   **RestAssured** is thread-safe by design.
> *   Our **DTOs** are instantiated per test method (inside the `@Test` method), so they are confined to their own thread stack and don't interfere with each other.
> *   **Loggers** are static but are inherently thread-safe in SLF4J.

**Q7: Explain the flow of a single test (e.g., Create User).**
> **Answer**:
> 1.  **Test Layer**: `CreateUserApiTest` generates data (`UserRequest`) and calls `userController.createUser(request)`.
> 2.  **Controller Layer**: `UserController` constructs the full URL (Base URL + `/users`) and calls `ApiClient.post()`.
> 3.  **Client Layer**: `ApiClient` uses `RestAssured` to send the HTTP POST request.
> 4.  **Return Path**: The Response bubbles back up: Client -> Controller -> Test.
> 5.  **Validation**: The Test Layer deserializes the JSON response into `UserResponse` and asserts the values.

**Q8: How do you manage secrets (API Keys, Passwords)?**
> **Answer**: In a local setup, they might be in `application.properties`. In a CI/CD pipeline (Jenkins/GitHub Actions), we pass them as **Environment Variables** (`System.getenv("API_KEY")`) so they are never hardcoded in the git repository.
