import subprocess
import os
import time
from agent.utils.logger import get_logger

logger = get_logger("DockerRunner")

class DockerRunner:
    def __init__(self, repo_path: str, image_name: str = "api-automation-agent:latest"):
        self.repo_path = repo_path
        self.image_name = image_name

    def _ensure_image(self, job_id: str):
        logger.info(f"Ensuring Docker image {self.image_name} is built...", job_id=job_id)
        subprocess.run(["docker", "build", "-t", self.image_name, "."], cwd=self.repo_path, check=True)

    def execute_validation(self, job_id: str) -> (bool, str):
        self._ensure_image(job_id)
        container_name = f"agent-validator-{job_id}-{int(time.time())}"
        
        # We run the actual test cycle here.
        cmd = ["docker", "run", "--rm", "--name", container_name, "-v", f"{os.path.abspath(self.repo_path)}:/app", "-w", "/app", self.image_name, "bash", "-c", "./gradlew clean compileTestJava test spotlessApply"]
        try:
            # Persistent 300s timeout for stability
            result = subprocess.run(cmd, cwd=self.repo_path, capture_output=True, text=True, timeout=300)
            if result.returncode == 0:
                return True, result.stdout
            return False, result.stdout + "\n" + result.stderr
        except subprocess.TimeoutExpired:
            logger.error(f"Validation timed out.", job_id=job_id)
            return False, "TIMEOUT: Docker validation cycle exceeded limit."
        except Exception as e:
            logger.error(f"Docker execution error: {e}", job_id=job_id)
            return False, str(e)
