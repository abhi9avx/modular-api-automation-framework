import os
import subprocess
from typing import List
from agent.utils.logger import get_logger
from agent.providers.gemini_generator import GeneratedFile

logger = get_logger("GitManager")

class GitManager:
    def __init__(self, repo_path: str = "."):
        self.repo_path = repo_path

    def prepare_branch(self, api_name: str, job_id: str) -> str:
        branch_name = f"feature/{api_name}-{job_id[:6]}"
        logger.info(f"Preparing branch: {branch_name}", job_id=job_id)
        try:
            subprocess.run(["git", "stash"], cwd=self.repo_path, capture_output=True)
            subprocess.run(["git", "checkout", "main"], cwd=self.repo_path, capture_output=True, check=True)
            subprocess.run(["git", "pull", "origin", "main"], cwd=self.repo_path, capture_output=True)
            subprocess.run(["git", "checkout", "-B", branch_name], cwd=self.repo_path, capture_output=True, check=True)
            return branch_name
        except subprocess.CalledProcessError as e:
            logger.error(f"Git checkout failed: {e.stderr.decode()}", job_id=job_id)
            raise e

    def write_files(self, files: List[GeneratedFile], job_id: str):
        for f in files:
            path = os.path.join(self.repo_path, f.path)
            os.makedirs(os.path.dirname(path), exist_ok=True)
            with open(path, 'w') as out:
                out.write(f.content)
            logger.debug(f"Wrote generated file: {f.path}", job_id=job_id)
        logger.info(f"Successfully wrote {len(files)}/{len(files)} files to disk.", job_id=job_id)

    def commit_and_push(self, branch: str, message: str, job_id: str):
        try:
            subprocess.run(["git", "add", "."], cwd=self.repo_path, check=True)
            subprocess.run(["git", "commit", "-m", message], cwd=self.repo_path, check=True)
            subprocess.run(["git", "push", "origin", branch], cwd=self.repo_path, check=True)
        except subprocess.CalledProcessError as e:
            logger.error(f"Git push failed: {e.stderr.decode()}", job_id=job_id)

    def create_pr(self, branch: str, title: str, body: str, job_id: str) -> str:
        try:
            cmd = ["gh", "pr", "create", "--title", title, "--body", body, "--base", "main", "--head", branch]
            result = subprocess.run(cmd, cwd=self.repo_path, capture_output=True, text=True, check=True)
            pr_link = result.stdout.strip()
            return pr_link
        except subprocess.CalledProcessError as e:
            logger.error(f"Failed to create PR using gh CLI: {e.stderr}", job_id=job_id)
            return "PR Creation Failed"
