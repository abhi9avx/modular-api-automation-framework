import os
import subprocess
import time
from typing import Tuple
from agent.utils.logger import get_logger

logger = get_logger("DockerRunner")

class DockerRunner:
    def __init__(self, repo_path: str = "."):
        self.repo_path = repo_path
        self.dockerfile = "Dockerfile"
        self.image_name = "api-automation-agent:latest"

    def build_image(self, job_id: str) -> bool:
        logger.info(f"Ensuring Docker image {self.image_name} is built...", job_id=job_id)
        cmd = ["docker", "build", "-t", self.image_name, "."]
        try:
            subprocess.run(cmd, cwd=self.repo_path, capture_output=True, check=True)
            return True
        except Exception:
            return False

    def execute_validation(self, job_id: str, attempt: int = 1) -> Tuple[bool, str]:
        if not self.build_image(job_id):
            return False, "Docker Build Failed"
        logger.info(f"Running validation flow in Docker (Attempt {attempt})...", job_id=job_id)
        container_name = f"agent-validator-{job_id}-{int(time.time())}"
        # We run the actual test cycle here.
        cmd = ["docker", "run", "--rm", "--name", container_name, "-v", f"{os.path.abspath(self.repo_path)}:/app", "-w", "/app", self.image_name, "bash", "-c", "./gradlew clean compileTestJava test spotlessApply"]
        try:
            result = subprocess.run(cmd, cwd=self.repo_path, capture_output=True, text=True, timeout=300)
            if result.returncode == 0:
                return True, result.stdout
            return False, result.stdout + "\n" + result.stderr
        except Exception as e:
            return False, str(e)
