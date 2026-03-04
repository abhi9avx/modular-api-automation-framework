import os
from typing import Optional, List
from agent.utils.logger import get_logger
from agent.providers.gemini_generator import GeminiGenerator, GeneratedFile
from agent.core.state_manager import StateManager

logger = get_logger("HealingStrategy")

class AdaptiveHealingStrategy:
    def __init__(self):
        self.generator = GeminiGenerator()
        self.state_manager = StateManager()

    def attempt_heal(self, job_id: str, attempt: int, error_logs: str, original_yaml: str, previous_files: List[GeneratedFile]) -> Optional[List[GeneratedFile]]:
        if not error_logs:
            return None
        logger.info(f"Self-Healing Strategy running... Attempt #{attempt}", job_id=job_id)
        prompt = self._build_tiered_prompt(attempt, error_logs, original_yaml, previous_files)
        original_construct = self.generator._construct_prompt
        self.generator._construct_prompt = lambda yml: prompt
        try:
            fixed_files = self.generator.generate_code(job_id, "HEALING_MODE")
            if fixed_files:
                # Filter out build.gradle or any other restricted files from LLM output
                filtered = [f for f in fixed_files if f.path != "build.gradle"]
                return filtered if filtered else None
            return None
        finally:
            self.generator._construct_prompt = original_construct

    def _build_tiered_prompt(self, attempt: int, logs: str, yaml: str, files: List[GeneratedFile]) -> str:
        base_instruct = """
        You are an expert SDET attempting to fix failing API execution tests.
        The previous code generation attempt failed during Docker validation (Gradle tests).
        
        ### STRICT CONSTRAINTS (CRITICAL)
        - **DO NOT USE SPRING FRAMEWORK OR SPRING BOOT.**
        - **DO NOT USE JUNIT (org.junit.x).** 
        - **USE ONLY TESTNG (org.testng.x).**
        - **DO NOT USE ALLURE (io.qameta.allure).**
        - **DO NOT GENERATE** `Basic.java`, `DtoComparisonUtil.java`, `LoggerUtil.java` or any other existing base framework files.
        - **DO NOT** modify `build.gradle`.
        - **DO NOT** add new dependencies.
        - **DO NOT** write markdown wrap (no ```json). Output ONLY the JSON object.

        ### MANDATORY TEST PATTERNS
        - **Tests MUST** extend `com.abhinav.tests.Basic`.
        - **Tests MUST** use `com.abhinav.framework.utils.DtoComparisonUtil.compareAndAssert(ExpectedDto.createDefault(), actual, "excludeField")`.
        - Package: `com.abhinav.tests`

        ### OUTPUT FORMAT (STRICT JSON ONLY)
        Return ONLY valid JSON. Output must be a JSON object conforming to the schema.
        Do NOT write markdown wrap (no ```json). 
        Do NOT add explanations.
        
        SCHEMA: {"files": [{"path": "...", "content": "..."}]}
        """
        files_str = "\n".join([f"--- FILE: {p.path} ---\n{p.content}" for p in files])
        if attempt == 1:
            return f"{base_instruct}\n\nTHE ERROR LOGS:\n{logs[-3000:]}\n\nTHE PREVIOUS FILES:\n{files_str}"
        else:
            return f"{base_instruct}\n\nThis is the SECOND failed attempt. A structural rewrite following the Mandatory Templates is REQUIRED.\n\nORIGINAL YAML:\n{yaml}\n\nTHE ERROR LOGS:\n{logs[-4000:]}\n\nTHE PREVIOUS FILES:\n{files_str}"
