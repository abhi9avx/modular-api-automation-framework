---
description: Always follow this rule for any API automation task.
---

## 🔥 ALWAYS FOLLOW THIS Rule: Modular API Automation Auto-Implementation Flow

You are working on the repository:
`https://github.com/abhi9avx/modular-api-automation-framework`

> [!IMPORTANT]
> This rule is the SOURCE OF TRUTH for all API automation tasks. You MUST follow every step in order whenever the user provides a cURL or asks for API test implementation.

---

## 🚀 Step 1: Branch Strategy
1. Pull the latest changes from `main`.
2. Create a new branch from `main`.
3. Branch name format:
   `feature/<api-name>-<timestamp>`
   Example: `feature/init-chat-1709876543`
   Branch name must be unique every time and based on API name derived from cURL.

---

## 🧠 Step 2: Understand the cURL and Response
You must:
* Analyze HTTP method (GET/POST/PUT/DELETE)
* Extract endpoint
* Identify headers
* Extract request body
* Understand response structure
* Identify status code
* Identify required validations

---

## 🏗 Step 3: Framework-Aware Implementation
This project follows a **modular architecture**. You must intelligently decide what to create and what to skip.

### 📌 3.1 Enums
Create enum only if:
* HTTP method enum required
* Endpoint enum required
* Header keys enum required
* Response field constants needed
If already exists → reuse
If not required → skip

### 📌 3.2 Request DTO
Create request DTO only if:
* Request body exists and is not empty
* DTO does not already exist
Follow existing framework pattern:
* Private fields
* Getters, Setters
* Builder or createDefault() if applicable
If request DTO already exists → skip

### 📌 3.3 Response DTO
Create response DTO only if:
* Structured JSON response exists
* DTO does not already exist
Follow project conventions. If response DTO exists → reuse

### 📌 3.4 Controller
Create new controller only if:
* Endpoint is new
* No existing controller handles it
If controller exists:
* Add new method inside it
* Do not duplicate controllers
Follow: RequestSpecification usage, ResponseSpecification usage, Proper logging, Reusable structure

### 📌 3.5 Test Class (Maximum 3 Tests)
Create a new TestNG test class. Include at most 3 test cases:
1. Positive scenario (happy path)
2. Negative scenario (invalid input)
3. Edge case (optional but recommended)
If test file already exists: Update it, do not duplicate test class. Follow project’s TestNG conventions.

---

## 🧪 Step 4: Update TestNG Configuration
If needed:
* Update `testng.xml`
* Ensure new test class is included
* Do not break existing suites

---

## 🛠 Step 5: Mandatory Build Validation
Before raising PR, run:
```bash
./gradlew clean
./gradlew compileTestJava
./gradlew test
./gradlew spotlessApply
```
If any error occurs: Fix it, re-run commands, ensure build passes 100%.
No PR should be raised if tests fail, build fails, or formatting fails.

---

## 📦 Step 6: Raise Pull Request
After successful execution:
* Push branch
* Raise PR to `main`
* PR title format: `feat: add automation for <api-name>`
* Add description including: API endpoint, Method, Test cases added, Validation coverage

---

## 🚨 Critical Rules
* Do NOT duplicate existing classes
* Reuse framework utilities
* Follow project folder structure strictly
* Follow clean architecture
* Do not modify unrelated files
* Keep changes minimal and clean
* Ensure CI compatibility

---

## 🧠 Intelligence Requirement
You must:
* Detect if components already exist
* Avoid redundant DTOs
* Avoid duplicate controllers
* Respect existing patterns
* Maintain modularity
