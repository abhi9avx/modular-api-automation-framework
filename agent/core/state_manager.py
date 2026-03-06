import sqlite3
import enum
from datetime import datetime
from agent.utils.logger import get_logger

logger = get_logger("StateManager")

class JobState(enum.Enum):
    CREATED = "CREATED"
    PARSED = "PARSED"
    YAML_GENERATED = "YAML_GENERATED"
    CODE_GENERATED = "CODE_GENERATED"
    VALIDATING = "VALIDATING"
    FAILED_VALIDATION = "FAILED_VALIDATION"
    PR_CREATED = "PR_CREATED"
    FAILED = "FAILED"

class StateManager:
    def __init__(self, db_path="agent_state.db"):
        self.db_path = db_path
        self._init_db()

    def _init_db(self):
        with sqlite3.connect(self.db_path) as conn:
            conn.execute("""
                CREATE TABLE IF NOT EXISTS jobs (
                    job_id TEXT PRIMARY KEY,
                    status TEXT,
                    raw_message TEXT,
                    trigger_path TEXT,
                    pr_link TEXT,
                    error_message TEXT,
                    created_at TIMESTAMP,
                    updated_at TIMESTAMP
                )
            """)
        logger.debug("SQLite Database initialized.")

    def create_job(self, job_id: str, raw_message: str):
        now = datetime.now()
        with sqlite3.connect(self.db_path) as conn:
            conn.execute("INSERT OR IGNORE INTO jobs (job_id, status, raw_message, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
                         (job_id, JobState.CREATED.value, raw_message, now, now))
        logger.info(f"Job created (or ignored if duplicate).", job_id=job_id)

    def update_state(self, job_id: str, status: JobState, trigger_path: str = None, pr_link: str = None, error_message: str = None):
        now = datetime.now()
        with sqlite3.connect(self.db_path) as conn:
            if trigger_path:
                conn.execute("UPDATE jobs SET status=?, trigger_path=?, updated_at=? WHERE job_id=?", (status.value, trigger_path, now, job_id))
            elif pr_link:
                conn.execute("UPDATE jobs SET status=?, pr_link=?, updated_at=? WHERE job_id=?", (status.value, pr_link, now, job_id))
            elif error_message:
                conn.execute("UPDATE jobs SET status=?, error_message=?, updated_at=? WHERE job_id=?", (status.value, error_message, now, job_id))
            else:
                conn.execute("UPDATE jobs SET status=?, updated_at=? WHERE job_id=?", (status.value, now, job_id))
        logger.info(f"State transitioned to {status}", job_id=job_id)

    def get_job(self, job_id: str):
        with sqlite3.connect(self.db_path) as conn:
            conn.row_factory = sqlite3.Row
            return conn.execute("SELECT * FROM jobs WHERE job_id=?", (job_id,)).fetchone()
