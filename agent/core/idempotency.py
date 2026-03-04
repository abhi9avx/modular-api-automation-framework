import hashlib
from agent.utils.logger import get_logger
from agent.core.state_manager import StateManager, JobState

logger = get_logger("Idempotency")

class IdempotencyGuard:
    def __init__(self):
        self.state_manager = StateManager()

    def generate_job_id(self, message_text: str) -> str:
        return hashlib.sha256(message_text.encode()).hexdigest()[:8]

    def is_already_processing(self, job_id: str) -> bool:
        job = self.state_manager.get_job(job_id)
        if job and job["status"] not in (JobState.FAILED, JobState.COMPLETED):
            return True
        return False
