from typing import List
from agent.providers.gemini_generator import GeneratedFile

class AdaptiveHealingStrategy:
    def build_prompt(self, yaml: str, files: List[GeneratedFile], logs: str, attempt: int) -> str:
        base_instruct = f"""
        # SELF-HEALING ACTION REQUIRED
        The previously generated code FAILED validation. You MUST fix the errors below.
        
        ### CONSTRAINTS
        - **DO NOT** hallucinate new classes or write completely new logic. **ONLY** fix the compilation/testing errors shown in the logs.
        - **DO NOT** use Spring Framework or Spring Boot.
        - **DO NOT** use JUnit. Use TestNG `org.testng.annotations.Test` and `org.testng.Assert`.
        - **DO NOT** use Allure `@Step`. 
        - **DO NOT** add new dependencies.
        - **DO NOT** write markdown wrap (no ```json). Output ONLY the JSON object.

        ### FRAMEWORK PACKAGE STRUCTURE
        - Tests (`.java` files extending `Basic`): `src/test/java/com/abhinav/tests/`
        - Controllers (`.java` files making RestAssured calls): `src/main/java/com/abhinav/framework/controller/`
        - DTOs (`.java` Request/Response records/classes): `src/main/java/com/abhinav/framework/dto/`

        ### MANDATORY TEST PATTERNS
        - **Tests MUST** extend `com.abhinav.tests.Basic`.
        - **Tests MUST** use `com.abhinav.framework.utils.DtoComparisonUtil.compareAndAssert(ExpectedDto.createDefault(), actual, "excludeField")`.

        ### OUTPUT FORMAT (STRICT JSON ONLY)
        Return ONLY valid JSON. Output must be a JSON object conforming to the schema.
        Do NOT write markdown wrap (no ```json). 
        Do NOT add explanations.
        
        SCHEMA: {{"files": [{{"path": "...", "content": "..."}}]}}
        """
        files_str = "\n".join([f"--- FILE: {p.path} ---\n{p.content}" for p in files])
        if attempt == 1:
            return f"{base_instruct}\n\nTHE ERROR LOGS:\n{logs[-3000:]}\n\nTHE PREVIOUS FILES:\n{files_str}"
        else:
            return f"{base_instruct}\n\nThis is the SECOND failed attempt. A structural rewrite following the Mandatory Templates is REQUIRED.\n\nORIGINAL YAML:\n{yaml}\n\nTHE ERROR LOGS:\n{logs[-4000:]}\n\nTHE PREVIOUS FILES:\n{files_str}"
