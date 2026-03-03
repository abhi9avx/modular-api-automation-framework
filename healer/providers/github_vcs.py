import os
import re
import subprocess
from github import Github, GithubException

class GitHubManager:
    def __init__(self, repo_path, token=None):
        self.repo_path = os.path.abspath(repo_path)
        self.token = token or os.getenv("GITHUB_TOKEN")
        self.github = Github(self.token) if self.token else None
        self.owner, self.repo_name = self._extract_repo_info()
        self.repo = None

        if self.github and self.owner and self.repo_name:
            try:
                self.repo = self.github.get_repo(f"{self.owner}/{self.repo_name}")
            except Exception as e:
                print(f"Warning: Could not access GitHub repo: {e}")

    def _extract_repo_info(self):
        try:
            result = subprocess.run(
                ["git", "config", "--get", "remote.origin.url"],
                cwd=self.repo_path,
                capture_output=True,
                text=True,
                check=True
            )
            remote_url = result.stdout.strip()

            https_match = re.match(r'https://github\.com/([^/]+)/(.+?)(?:\\.git)?$', remote_url)
            if https_match:
                return https_match.group(1), https_match.group(2)

            ssh_match = re.match(r'git@github\\.com:([^/]+)/(.+?)(?:\\.git)?$', remote_url)
            if ssh_match:
                return ssh_match.group(1), ssh_match.group(2)

            return None, None
        except Exception:
            return None, None

    def create_pr(self, branch_name, title, body, base="master"):
        if not self.repo:
            print("GitHub repository not accessible. PR cannot be created.")
            return None

        try:
            pr = self.repo.create_pull(title=title, body=body, head=branch_name, base=base)
            return pr.html_url
        except Exception as e:
            print(f"Error creating PR: {e}")
            return None

    def push_changes(self, branch_name):
        try:
            subprocess.run(["git", "add", "."], cwd=self.repo_path, check=True)
            subprocess.run(["git", "commit", "-m", "Self-healing: applied AI fix"], cwd=self.repo_path, check=True)
            subprocess.run(["git", "push", "origin", branch_name], cwd=self.repo_path, check=True)
            return True
        except Exception as e:
            print(f"Error pushing changes: {e}")
            return False
