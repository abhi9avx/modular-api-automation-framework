# Interview Q&A (Deep Dive)

This document contains a curated list of interview questions and answers relevant to this modular API automation framework.

## 📌 Framework & Tech Stack Questions

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

### 🔹 Advanced RestAssured
**Q9: What is the difference between RequestSpecification and ResponseSpecification?**
> **Answer**:
> *   **RequestSpecification**: Is an interface to specify how the request will look like. It allows grouping common path parameters, headers, and authentication to prevent redundancy.
> *   **ResponseSpecification**: Is an interface to define how a response must look like. Used to check common expectations (e.g., Status Code 200, Content-Type JSON) across multiple tests.

**Q10: How do Filters work in RestAssured?**
> **Answer**: Filters allow you to intercept and modify requests/responses before they are sent or received.
> *   **Usage**: Logging request/response details, custom authentication schemes, or error handling.
> *   **Common Implementation**: `RequestLoggingFilter` and `ResponseLoggingFilter` are widely used to print logs to the console or file.

**Q11: Explain Serialization and Deserialization in the context of this framework.**
> **Answer**:
> *   **Serialization (POJO -> JSON)**: Converting a Java Object (DTO) into a JSON string to be sent as the Request Body. Handled by Jackson's `ObjectMapper`.
> *   **Deserialization (JSON -> POJO)**: Converting the received JSON Response Body back into a Java Object (DTO) for easy assertion and validation.

**Q12: How would you perform JSON Schema Validation?**
> **Answer**: RestAssured integrates with the `json-schema-validator` module.
> *   We store the `.json` schema file in `src/test/resources`.
> *   In the test verification phase, we use `.body(matchesJsonSchemaInClasspath("schema.json"))` to ensure the response structure matches strict type definitions.

**Q13: How do you handle multiple Content-Types (e.g., Multipart, Form-UrlEncoded)?**
> **Answer**:
> *   **JSON**: `.contentType(ContentType.JSON)` with `.body(pojo)`.
> *   **Form-UrlEncoded**: `.contentType(ContentType.URLENC)` with `.formParam("key", "value")`.
> *   **Multipart (File Upload)**: `.multiPart("file", new File("path"))`. The framework's Client layer can be extended to accept a generic Object and detect the type.

### 🔹 Advanced TestNG & Execution
**Q14: What constitutes a "Flaky" test and how do you handle it?**
> **Answer**: A flaky test passes/fails inconsistently without code changes.
> *   **Causes**: Network latency, data synchronization issues, or shared state.
> *   **Solution**:
>     1.  Use `IRetryAnalyzer` in TestNG to automatically retry failed tests.
>     2.  Ensure tests are isolated (clean data setup/teardown).
>     3.  Avoid hardcoded delays (`Thread.sleep`); use Awaitility or polling.

**Q15: What is the role of Listeners in TestNG?**
> **Answer**: Listeners allow customizing TestNG's behavior. The most common is `ITestListener`.
> *   **Use Case**: Taking screenshots on failure, logging start/end of tests, or integrated with Allure Reports to attach logs when `onTestFailure` is triggered.

**Q16: Hard Assert vs Soft Assert?**
> **Answer**:
> *   **Hard Assert** (`Assert.assertEquals`): Stops the test execution immediately upon failure. Used when checking critical values (e.g., Status Code).
> *   **Soft Assert** (`SoftAssert`): Records the error but continues execution. Checked at the end via `.assertAll()`. Used for validating multiple fields in a large JSON body.

**Q17: How does `ThreadLocal` help in Parallel Execution?**
> **Answer**: `ThreadLocal` creates a separate instance of a variable for each thread.
> *   **Framework Usage**: If we were to store the `ExtentReport` test instance or a shared `WebDriver` (in UI automation), `ThreadLocal` ensures that Thread A doesn't over-write or read Thread B's data during parallel execution.

**Q18: How do you pass dynamic data to tests?**
> **Answer**: Using `@DataProvider`.
> *   It returns an `Object[][]` or `Iterator<Object[]>`.
> *   Linked to a test via `@Test(dataProvider = "name")`.
> *   Useful for testing the same endpoint with valid, invalid, and boundary value inputs.

### 🔹 Framework Architecture & Best Practices
**Q19: Explain the Single Responsibility Principle (SRP) in this framework.**
> **Answer**:
> *   **Controller**: Only constructs requests.
> *   **Client**: Only executes HTTP calls.
> *   **Tests**: Only validate results.
> *   **DTO**: Only holds data.
> *   **Config**: Only manages properties.
> *   No class does "too much", making it easy to debug and maintain.

**Q20: Why use Lombok? Are there downsides?**
> **Answer**:
> *   **Pros**: drastically reduces boilerplate (Getters, Setters, Builders, toString). Keeps POJOs clean.
> *   **Cons**: Requires IDE plugin. Can hide complexity (e.g., `@Data` generates all getters/setters/equals/hashCode, which might impact performance for massive objects or JPA entities if not careful).

**Q21: How do you handle Environment Switching (QA/Stage/Prod)?**
> **Answer**:
> *   We use a configuration loader (`EnvironmentConfig`).
> *   It reads a system property `-Denv=qa` passed from Gradle.
> *   Based on the string, it loads the corresponding `application-qa.properties` file to set Base URLs and Credentials.

**Q22: Use of Java Streams in API Testing?**
> **Answer**: Streams are powerful for filtering Response collections.
> *   *Example*: Verify that all users in the list have `active: true`.
> *   `List<User> users = response.as(User[].class);`
> *   `boolean allActive = Arrays.stream(users).allMatch(User::isActive);`

**Q23: How would you debug an issue where the API request works in Postman but fails in the Framework?**
> **Answer**:
> 1.  Enable full logging: `given().log().all()`.
> 2.  Compare the printed **cURL** or Headers/Body strictly with Postman's console.
> 3.  Check for concealed headers like `User-Agent` or hidden characters in the URL.
> 4.  Verify SSL handshake (sometimes certificates are ignored in Postman but required in Java).

**Q24: Why use a customized `ApiClient` wrapper instead of `RestAssured.given()` directly in tests?**
> **Answer**:
> *   **Reusability**: If we need to add a default Header (e.g., Auth Token) to EVERY request, we do it in one place (`ApiClient`).
> *   **Decoupling**: If we switch from RestAssured to HTTPClient in the future, we only rewrite the `ApiClient`, not hundreds of tests.

**Q25: What is the difference between `@BeforeTest`, `@BeforeClass`, and `@BeforeMethod`?**
> **Answer**:
> *   `@BeforeSuite`: Runs once before the entire suite (Global setup).
> *   `@BeforeTest`: Runs before the `<test>` tag in `testng.xml`.
> *   `@BeforeClass`: Runs once before the first test method in the current class.
> *   `@BeforeMethod`: Runs before **each** `@Test` method (e.g., resetting data).

**Q26: How do you integrate this with Jenkins/CI?**
> **Answer**:
> *   We create a specific "Job" or "Pipeline" in Jenkins.
> *   The pipeline pulls the code from Git.
> *   Executes `./gradlew test -Denv=qa`.
> *   Uses the **Allure Plugin** for Jenkins to visualize the generated `allure-results` folder.

**Q27: How do you handle negative scenarios?**
> **Answer**: By creating negative Tests (e.g., `CreateUser_InvalidEmail`).
> *   We expect a specific Error Status Code (400 Bad Request).
> *   We validate the **Error Message** in the response body using a specific `ErrorResponse` DTO.

**Q28: What is Dependency Injection and where could it be used here?**
> **Answer**:
> *   Currently, we may instance `UserController` directly.
> *   With **Dependency Injection (DI)** (like Guice or Spring), we would let the framework manage the lifecycle of the Controllers and Clients.
> *   This creates looser coupling and makes unit testing individual components (mocking) easier.

### 🔹 CI/CD & Notifications (GitHub Actions)
**Q29: Explain the workflow of GitHub Actions in this project.**
> **Answer**:
> 1.  **Trigger**: The workflow starts on `push` to the `main` branch.
> 2.  **Environment**: It spins up an `ubuntu-latest` virtual machine.
> 3.  **Setup**: Installs Java JDK 17 (using `actions/setup-java`).
> 4.  **Execution**: Runs `./gradlew clean test` to execute the TestNG suite.
> 5.  **Reporting**: Generates Allure reports (`./gradlew allureReport`).
> 6.  **Artifacts**: Uploads the raw results and HTML report to GitHub Actions Artifacts storage.
> 7.  **Publishing**: Pushes the generated HTML to the `gh-pages` branch to host the website.
> 8.  **Notification**: Sends a Telegram message with the status and link.

**Q30: Why did we need to grant specific "Write" permissions in the YAML file?**
> **Answer**:
> *   By default, the `GITHUB_TOKEN` is often Read-Only for security.
> *   To **publish** to GitHub Pages, the workflow needs to **push** code (the HTML files) to the `gh-pages` branch.
> *   We added `permissions: contents: write` to authorize this action.

**Q31: How does the Telegram Bot integration work securely?**
> **Answer**:
> *   We do **not** hardcode the Bot Token in the code.
> *   We use **GitHub Secrets** (`secrets.TELEGRAM_BOT_TOKEN`).
> *   In the workflow YAML, we access these secrets securely (`${{ secrets... }}`) and pass them to a `curl` command that hits the Telegram Bot API (`/sendMessage`).

**Q32: What happens if the tests fail? Does the report still generate?**
> **Answer**:
> *   Yes. We use the `if: always()` condition in the GitHub Actions steps.
> *   This ensures that even if the `./gradlew test` step fails (exit code 1), the subsequent steps (Generate Report, Upload Artifact, Send Notification) **still run**. This is crucial for debugging *why* it failed.

### 🔹 Docker & Containerization
**Q33: Why should we dockerize our automation framework?**
> **Answer**:
> *   **Consistency**: "It works on my machine" is solved. Docker ensures the tests run in the exact same environment (OS, Java version, Dependencies) everywhere.
> *   **Isolation**: No need to install Java or Gradle on the host machine (e.g., Jenkins agent). You only need Docker.
> *   **Scalability**: Easy to spin up multiple containers to run tests in parallel on a Grid or Kubernetes.

**Q34: Explain the `Dockerfile` used in this project.**
> **Answer**:
> *   `FROM gradle:8.5-jdk17`: Sets the base image. we start with a Linux machine that already has Gradle 8.5 and Java 17 installed.
> *   `WORKDIR /app`: Creates a working directory inside the container and switches to it.
> *   `COPY . .`: Copies all files from our local project folder into the container's `/app` folder.
> *   `CMD ["./gradlew", "clean", "test"]`: The default command to run when the container starts. It executes the tests.

**Q35: What is the purpose of `.dockerignore`?**
> **Answer**:
> *   It is similar to `.gitignore`. It tells Docker which files **NOT** to copy into the container during the build process.
> *   **Why?**: We exclude `build/`, `.gradle/`, and `node_modules` to keep the image lightweight and prevent overwriting the container's environment with local build artifacts.

**Q36: What is the difference between `CMD` and `RUN` in a Dockerfile?**
> **Answer**:
> *   `RUN`: Executes a command *during the build phase* (e.g., `RUN apt-get install git`). The result is committed to the image layer.
> *   `CMD`: Specifies the command to run *when the container starts* (e.g., `./gradlew test`). It is the runtime entry point.

**Q37: How do you integrate Docker with GitHub Actions?**
> **Answer**:
> *   We use a YAML workflow (like `docker-tests.yml`) that specifies `runs-on: ubuntu-latest`.
> *   Since GitHub-hosted runners have Docker pre-installed, we can simply run `docker build` and `docker run` commands in the `steps`.
> *   This verifies that our Docker image builds and functions correctly in a blank environment.

**Q38: Why do we need `RUN chmod +x ./gradlew` in the Dockerfile?**
> **Answer**:
> *   When files are copied from Windows or sometimes even Mac to the Linux container, the "execution" permission might be lost.
> *   If we don't run `chmod +x`, the container fails with "Permission denied" when trying to run `./gradlew`.

**Q39: What is the difference between running tests in "CI with Gradle" vs. "CI with Docker"?**
> **Answer**:
> *   **CI with Gradle**: The runner (GitHub VM) uses its own installed Java/Gradle. If the runner updates Java automatically, your tests might break due to version mismatch.
> *   **CI with Docker**: The runner only runs Docker. The tests run INSIDE the container, which has a **fixed** version of Java and Gradle (defined in `FROM gradle:8.5-jdk17`). This is more stable and reliable.

