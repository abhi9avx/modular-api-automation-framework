import os
import time
from typing import List
from agent.utils.logger import get_logger
from agent.providers.gemini_generator import GeneratedFile

logger = get_logger("Guardrails")

class SecurityGuardrails:
    @staticmethod
    def validate_generated_files(files: List[GeneratedFile], job_id: str) -> bool:
        # Blacklist of files that should NEVER be generated/overwritten by LLM (critical infras only)
        blacklist = {"build.gradle", "pom.xml", "settings.gradle"}
        
        for f in files:
            path = f.path
            filename = os.path.basename(path)
            
            # Blacklist check
            if filename in blacklist:
                logger.error(f"Guardrail Violation: Attempted to generate blacklisted file - {path}", job_id=job_id)
                return False
                
            if ".." in path or path.startswith("/"):
                logger.error(f"Guardrail Violation: Path traversal detected - {path}", job_id=job_id)
                return False
            
            # Strict directory enforcement
            if filename.endswith("Test.java"):
                if not path.startswith("src/test/java/"):
                    logger.error(f"Guardrail Violation: Tests must be in src/test/java/ - {path}", job_id=job_id)
                    return False
            elif filename.endswith("Controller.java") or filename.endswith("Dto.java") or filename.endswith("Response.java") or filename.endswith("Request.java") or filename.endswith("ResponseDto.java") or filename.endswith("RequestDto.java"):
                if not path.startswith("src/main/java/"):
                    logger.error(f"Guardrail Violation: Source files must be in src/main/java/ - {path}", job_id=job_id)
                    return False
            
            # General allowed prefixes
            allowed_prefixes = ("src/main/java/", "src/test/java/", "rules/pending/")
            if not any(path.startswith(p) for p in allowed_prefixes):
                logger.error(f"Guardrail Violation: Path not in allowed directories - {path}", job_id=job_id)
                return False
                
        return True

    @staticmethod
    def enforce_execution_limits(attempt: int, max_retries: int, start_time: float, max_sla_seconds: int, job_id: str) -> bool:
        if attempt > max_retries:
            logger.error(f"Execution Limit Violation: Max retries ({max_retries}) exceeded.", job_id=job_id)
            return False
        elapsed = time.time() - start_time
        if elapsed > max_sla_seconds:
            logger.error(f"Execution SLA Violation: Time elapsed ({elapsed}s) exceeds max allowed limit ({max_sla_seconds}s).", job_id=job_id)
            return False
        return True
