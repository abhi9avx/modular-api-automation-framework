---
description: Modular API Automation Auto-Implementation Flow
---

// turbo-all
1. **Branch Strategy**
   - Pull `main`.
   - Create `feature/<api-name>-<timestamp>`.

2. **Analysis**
   - Extract Method, Endpoint, Headers, Body, and Response structure from cURL.

3. **Implementation**
   - Reuse or create Enums (Method, Endpoint, Headers).
   - Reuse or create Request/Response DTOs (Private fields, Getters/Setters, Builder/createDefault).
   - Reuse or create Controller (RequestSpec, ResponseSpec, Logging).
   - Update/Create Test Class (Max 3 tests: Positive, Negative, Edge).

4. **Configuration**
   - Update `testng.xml` if needed.

5. **Build Validation**
   - Run: `./gradlew clean compileTestJava test spotlessApply`.

6. **Pull Request**
   - Push branch and raise PR to `main`.
   - Title: `feat: add automation for <api-name>`.
