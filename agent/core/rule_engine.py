import os
import time
import yaml
import re
from agent.utils.logger import get_logger
from agent.core.state_manager import StateManager, JobState
from agent.providers.gemini_generator import GeminiGenerator
from agent.core.git_manager import GitManager
from agent.worker.docker_runner import DockerRunner
from agent.core.healing_strategy import AdaptiveHealingStrategy
from agent.core.guardrails import SecurityGuardrails
from healer.providers.telegram import TelegramManager

logger = get_logger("RuleEngine")

class RuleEngine:
    def __init__(self, repo_path: str, state_manager: StateManager):
        self.repo_path = repo_path
        self.state_manager = state_manager
        self.generator = GeminiGenerator()
        self.healer = AdaptiveHealingStrategy()
        self.docker = DockerRunner(repo_path)
        
        with open("agent/prompts/rule_prompt.txt", "r") as f:
            self.system_prompt = f.read()

    def execute(self, job_id: str, trigger_path: str, is_dryrun=False):
        logger.info(f"Rule Engine starting for trigger: {trigger_path}", job_id=job_id)
        with open(trigger_path, "r") as f:
            trigger_data = yaml.safe_load(f)

        api_name = trigger_data.get("api_name", "unknown")
        safe_api_name = re.sub(r'[^a-zA-Z0-9-]', '-', api_name.lower())
        branch_name = f"feature/{safe_api_name}-{job_id}"
        
        # 1. Generate Initial Code
        prompt = f"{self.system_prompt}\n\nORIGINAL REQUEST YAML:\n{yaml.dump(trigger_data)}"
        response = self.generator.generate_code(prompt, job_id)
        if not response:
            self.state_manager.update_state(job_id, JobState.FAILED, error_message="Initial generation failed")
            return

        self.state_manager.update_state(job_id, JobState.CODE_GENERATED)
        
        # 2. Preparation & Validation Loop
        git = GitManager(self.repo_path)
        if not is_dryrun:
            git.prepare_branch(branch_name, job_id)

        current_files = response.files
        error_logs = ""
        success = False
        max_retries = 3
        start_time = time.time()

        for attempt in range(1, max_retries + 1):
            if not SecurityGuardrails.enforce_execution_limits(attempt, max_retries, start_time, 900, job_id):
                break

            if not SecurityGuardrails.validate_generated_files(current_files, job_id):
                break

            # Write files
            for f in current_files:
                full_path = os.path.join(self.repo_path, f.path)
                os.makedirs(os.path.dirname(full_path), exist_ok=True)
                with open(full_path, "w") as out:
                    out.write(f.content)
                logger.debug(f"Wrote generated file: {f.path}", job_id=job_id)

            logger.info(f"Successfully wrote {len(current_files)}/{len(current_files)} files to disk.", job_id=job_id)
            self.state_manager.update_state(job_id, JobState.VALIDATING)

            # Validate in Docker
            result_ok, logs = self.docker.execute_validation(job_id)
            if result_ok:
                success = True
                break
            else:
                self.state_manager.update_state(job_id, JobState.FAILED_VALIDATION, error_message=logs[-2000:])
                error_logs = logs
                logger.info(f"Self-Healing Strategy running... Attempt #{attempt}", job_id=job_id)
                heal_prompt = self.healer.build_prompt(yaml.dump(trigger_data), current_files, error_logs, attempt)
                heal_res = self.generator.generate_code(heal_prompt, job_id)
                if heal_res:
                    current_files = heal_res.files
                else:
                    break

        if success:
            if not is_dryrun:
                pr_link = git.commit_and_push(branch_name, f"🤖 Automated API generation for {api_name}", job_id)
                if pr_link:
                    self.state_manager.update_state(job_id, JobState.PR_CREATED, pr_link=pr_link)
                    try:
                        telegram = TelegramManager()
                        telegram.send_message(f"🚀 *API Automation Success*\n\nI have successfully generated the API tests and created a Pull Request.\n\n🔗 *PR Link*: {pr_link}")
                        logger.info("Sent Telegram notification successfully.", job_id=job_id)
                    except Exception as e:
                        logger.error(f"Failed to send Telegram notification: {e}", job_id=job_id)
                else:
                    self.state_manager.update_state(job_id, JobState.FAILED, error_message="PR creation failed")
            else:
                logger.info("Dry run successful, skipping commit/push.", job_id=job_id)
        else:
            self.state_manager.update_state(job_id, JobState.FAILED, error_message="Validation loop exhausted")
