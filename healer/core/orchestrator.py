import os
import time
import subprocess
from healer.utils.config_loader import ConfigLoader
from healer.core.failure_analyzer import FailureAnalyzer
from healer.providers.gemini import GeminiClient
from healer.providers.docker_rt import DockerManager
from healer.providers.github_vcs import GitHubManager
from healer.utils.patcher import Patcher
from healer.providers.telegram import TelegramManager

class HealerOrchestrator:
    def __init__(self, repo_path, config_path):
        self.repo_path = os.path.abspath(repo_path)
        self.cl = ConfigLoader(config_path)
        self.config = self.cl.load_config()
        self.max_attempts = self.config.get("max_attempts", 3)
        self.artifacts_path = os.path.join(self.repo_path, self.config.get("artifacts_path", "build/test-results/test"))
        self.gemini_client = GeminiClient(model=self.config.get("model", "gemini-2.0-flash"))
        self.telegram_manager = TelegramManager()
        self.healing_results = []

    def run_repair_loop(self, test_filter=None, use_docker=False, create_pr=False):
        success = False
        for attempt in range(1, self.max_attempts + 1):
            print(f"\n=== Repair Attempt {attempt}/{self.max_attempts} ===")

            # 1. Run Tests
            command = ["./gradlew", "test"]
            if test_filter:
                command.extend(["--tests", test_filter])

            print(f"➜ Running Tests {'in Docker' if use_docker else 'locally'}...")
            if use_docker:
                dm = DockerManager()
                if not dm.check_docker():
                    print("✗ Docker is not available. Falling back to local run.")
                    result = subprocess.run(command, cwd=self.repo_path, capture_output=True, text=True)
                else:
                    dm.build_image()
                    res = dm.run_tests(self.repo_path, command=command)
                    # Mock result class
                    class MockResult:
                        def __init__(self, exit_code, stdout):
                            self.returncode = exit_code
                            self.stdout = stdout
                    result = MockResult(res["exit_code"], res["logs"])
            else:
                result = subprocess.run(command, cwd=self.repo_path, capture_output=True, text=True)
            
            if result.returncode == 0:
                print("✓ All tests passed!")
                success = True
                break
            
            print(f"✗ Tests Failed (Exit Code: {result.returncode})")
            
            # 2. Analyze Failures
            analyzer = FailureAnalyzer(self.artifacts_path)
            failures = analyzer.analyze()
            
            scripting_failures = [f for f in failures if f.is_scripting_issue]
            if not scripting_failures:
                print("⚠ Tests failed but no fixable scripting issues found.")
                break
                
            target = scripting_failures[0]
            print(f"ℹ Targeting: {target.test_class}.{target.test_name}")
            
            # 3. Find Source File
            search_path = os.path.join(self.repo_path, "src/test/java")
            target_file = None
            for root, _, files in os.walk(search_path):
                potential_name = target.test_class.split('.')[-1] + ".java"
                if potential_name in files:
                    target_file = os.path.join(root, potential_name)
                    break
            
            if not target_file:
                print(f"✗ Could not find source file for {target.test_class}")
                break
                
            print(f"ℹ Found source file: {target_file}")
            with open(target_file, "r") as f:
                code = f.read()

            # 4. Get AI Suggestion
            print("➜ Consulting Gemini for fix...")
            suggestion = self.gemini_client.get_fix_suggestion(target, code, result.stdout[-2000:])
            
            if not suggestion or suggestion.confidence < 0.5:
                print("⚠ Gemini couldn't provide a high-confidence fix.")
                self.healing_results.append({
                    "test_name": target.test_name,
                    "status": "no_fix_found"
                })
                break
                
            print(f"✓ Fix identified (Confidence: {suggestion.confidence})")
            print(f"ℹ Explanation: {suggestion.explanation}")
            
            # 5. Apply Patch
            print("➜ Applying fix...")
            if Patcher.apply_patch(self.repo_path, suggestion.diff):
                print("✓ Patch applied successfully.")
                
                res_entry = {
                    "test_name": target.test_name,
                    "status": "fixed",
                    "explanation": suggestion.explanation
                }
                
                if create_pr:
                    print("➜ GitHub Integration: Creating Pull Request...")
                    gh = GitHubManager(self.repo_path)
                    branch_name = f"healer-fix-{int(time.time())}"
                    subprocess.run(["git", "checkout", "-b", branch_name], cwd=self.repo_path)
                    if gh.push_changes(branch_name):
                        pr_url = gh.create_pr(
                            branch_name=branch_name,
                            title=f"Self-healing: Fixed {target.test_name}",
                            body=f"AI-generated fix for {target.test_class}.{target.test_name}\n\n**Explanation:** {suggestion.explanation}"
                        )
                        if pr_url:
                            print(f"✓ Pull Request created: {pr_url}")
                            res_entry["pr_url"] = pr_url
                        else:
                            print("✗ Applied fix but failed to create PR.")
                
                self.healing_results.append(res_entry)
            else:
                print("✗ Failed to apply patch.")
                self.healing_results.append({
                    "test_name": target.test_name,
                    "status": "patch_failed"
                })
                break

        # Final Notification
        if self.telegram_manager:
            self.telegram_manager.send_healing_report(self.healing_results)

        return success
