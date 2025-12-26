# Modular API Automation Framework

## 📌 Overview
This is a **production-ready, modular API Automation Framework** built using **Java** and **RestAssured**. It is designed to be scalable, maintainable, and readable, following industry best practices and standard design patterns.

The framework completely decouples the **Test Layer** (Validation) from the **Logic Layer** (API Calls) and **Data Layer** (POJOs), ensuring that changes in API endpoints or payloads usually require updates in only one place.

---

## 🛠️ Tech Stack & Key Tools
### 1. RestAssured (The Core Engine)
*   **What is it?**: A Java DSL (Domain Specific Language) library used specifically for testing RESTful Web Services. It acts like a headless Postman.
*   **Why use it?**:
    *   **Simplicity**: It uses a BDD (Behavior Driven Development) syntax: `given().when().then()`, making tests easy to read.
    *   **Power**: It handles complex HTTP requests, Authentication (OAuth, Basic), and allows detailed validation of Headers, Cookies, and Body.
    *   **Integration**: Seamlessly integrates with Java and Hamcrest matchers.

### 2. TestNG (The Test Runner)
*   **What is it?**: A testing framework inspired by JUnit but designed for more complex integration and end-to-end testing scenarios.
*   **Why use it?**:
    *   **Control**: It allows us to define **Test Suites** via `testng.xml` to pick and choose which tests to run.
    *   **Parallel Execution**: Critical for saving time. It can run multiple test classes or methods simultaneously.
    *   **Annotations**: Powerful hierarchy (`@BeforeSuite`, `@BeforeClass`, `@BeforeMethod`) to manage setup and teardown effectively.
    *   **Data Providers**: Enables running the same test logic with different data sets (Data-Driven Testing).

### 3. Gradle (The Build Tool)
*   **What is it?**: A modern build automation tool that manages project dependencies and lifecycle.
*   **Why use it?**:
    *   **Dependency Management**: We just list libraries (like `rest-assured:5.3.0`) in `build.gradle`, and Gradle automatically downloads them.
    *   **Performance**: It uses an incremental build cache, making it significantly faster than Maven.
    *   **Flexibility**: The configuration is written in Groovy/Kotlin, allowing for programmable build logic (e.g., passing Environment variables like `-Denv=qa`).

### 4. Jackson (The JSON Processor)
*   **What is it?**: A high-performance JSON processor for Java.
*   **Why use it?**:
    *   **Serialization**: Converts our Java Objects (DTOs) into JSON format to send in API requests.
    *   **Deserialization**: Converts the API's JSON response back into Java Objects so we can easily validate fields like `response.getId()`.
    *   **Automation**: It works behind the scenes with RestAssured to map data automatically, eliminating manual string parsing.

### 5. Lombok (The Boilerplate Killer)
*   **What is it?**: A Java library that automatically plugs into your editor and build tools.
*   **Why use it?**:
    *   **Clean Code**: Java requires a lot of "boilerplate" code like Getters, Setters, Constructors, and `toString()` methods. Lombok generates these at compile time with simple annotations (`@Data`, `@Builder`).
    *   **Readability**: Reduces a 50-line POJO class to just 5 lines, keeping the codebase clean and focused on data.

### 6. Allure Reports (The Visualization)
*   **What is it?**: An open-source framework designed to create flexible and interactive test execution reports.
*   **Why use it?**:
    *   **Insights**: It provides a beautiful HTML dashboard showing Pass/Fail statistics, execution time, and trends.
    *   **Debugging**: Allows attaching logs, request/response bodies, and stack traces directly to the failed test step.
    *   **History**: Can show test stability over time when integrated with CI servers like Jenkins.

### 7. SLF4J + Logback (The Logging)
*   **What is it?**: Simple Logging Facade for Java (SLF4J) acts as an interface, while Logback is the implementation.
*   **Why use it?**:
    *   **Structured Logs**: Instead of `System.out.println`, it provides formatted logs with timestamps, thread names, and severity levels (INFO, ERROR, DEBUG).
    *   **Performance**: It is asynchronous and optimized for speed, preventing logging from slowing down tests.

---

## � Getting Started

### Prerequisites
Before running the project, ensure you have the following installed:
*   **Java JDK 17+**: Verify with `java -version`
*   **Gradle**: (Optional, as we use the Gradle Wrapper included)

### 📥 Installation
1.  **Fork** the repository (Top right corner on GitHub).
2.  **Clone** your forked repository:
    ```bash
    git clone https://github.com/YOUR_USERNAME/modular-api-automation-framework.git
    cd modular-api-automation-framework
    ```

### 🏃‍♂️ How to Run
This project uses the **Gradle Wrapper**, so you don't need to manually install Gradle.

#### 1. Build the Project
Downloads dependencies and compiles the code.
```bash
./gradlew clean build -x test
```

#### 2. Run All Tests
Executes all tests configured in `src/test/resources/testng.xml`.
```bash
./gradlew clean test
```

#### 3. Run Specific Tests
To run a specific test class:
```bash
./gradlew clean test --tests "com.abhinav.tests.CreateUserApiTest"
```

#### 4. Run on Different Environments
You can switch environments (QA, DEV) using the system property `-Denv`.
```bash
./gradlew clean test -Denv=qa   # Runs on QA environment
./gradlew clean test -Denv=dev  # Runs on DEV environment
```

#### 5. Generate & View Allure Reports
After execution, generate a local Allure web report to visualize results.
```bash
./gradlew allureServe
```
*(Note: Press `Ctrl + C` to stop the server)*

#### 6. Viewing Reports from CI (GitHub Actions)
If you download the `allure-report` artifact from GitHub Actions, you **cannot** simply open `index.html` in your browser due to security restrictions.

**Method 1: Use Allure CLI (Recommended)**
```bash
allure open path/to/unzipped/allure-report
```

**Method 2: Use Python Simple Server**
```bash
cd path/to/unzipped/allure-report
python3 -m http.server 8000
# Open http://localhost:8000 in your browser
```

---

## �📂 Project Structure & Deep Dive
This project follows a strict **Layered Modular Architecture**. Each package has a specific responsibility.

```text
.
├── build.gradle      # [Build] Root build configuration
├── settings.gradle   # [Settings] Project name and modules
├── src
│   ├── main
│   │   └── java
│   │       └── com.abhinav.framework
│   │           ├── client       # [Logic Layer] Wraps RestAssured.
│   │           ├── config       # [Config Layer] Env variables & constants.
│   │           ├── controller   # [Business Layer] Logic bridge.
│   │           ├── dto          # [Data Layer] POJOs for JSON.
│   │           ├── enums        # [Constants] Type-safe values.
│   │           └── utils        # [Helpers] Generic utilities.
│   └── test
│       ├── java
│       │   └── com.abhinav.tests # [Test Layer] Validation logic.
│       └── resources
│           └── testng.xml        # [Execution Control] Test Suites.
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

## 🔔 Telegram Notifications
The framework sends automatic build status updates to Telegram.

### Setup Secrets
To enable this, add the following to **Settings > Secrets and variables > Actions**:
1. `TELEGRAM_BOT_TOKEN`: Your Bot Token (from BotFather).
2. `TELEGRAM_CHAT_ID`: The User or Channel ID to receive messages.

---

## 🎓 Interview Prep

For a deep dive into potential Interview Questions & Answers related to this framework, check out:
👉 **[INTERVIEW_QA.md](./INTERVIEW_QA.md)**

It covers:
- TestNG vs JUnit
- Thread Safety & Parallel Execution
- Framework Design Patterns
- Advanced RestAssured features

