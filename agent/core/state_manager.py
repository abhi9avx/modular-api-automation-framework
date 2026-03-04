import sqlite3
import time
import os
from enum import Enum
from typing import Optional, Dict, Any
from agent.utils.logger import get_logger

logger = get_logger("StateManager")

class JobState(str, Enum):
    RECEIVED = "RECEIVED"
    PARSED = "PARSED"
    YAML_GENERATED = "YAML_GENERATED"
    CODE_GENERATED = "CODE_GENERATED"
    VALIDATING = "VALIDATING"
    FAILED_VALIDATION = "FAILED_VALIDATION"
    PR_CREATED = "PR_CREATED"
    COMPLETED = "COMPLETED"
    FAILED = "FAILED"

class StateManager:
    def __init__(self, db_path: str = "agent_state.db"):
        self.db_path = db_path
        self._init_db()

    def _init_db(self):
        with sqlite3.connect(self.db_path) as conn:
            conn.execute("""
                CREATE TABLE IF NOT EXISTS jobs (
                    job_id TEXT PRIMARY KEY,
                    chat_id TEXT,
                    status TEXT,
                    api_name TEXT,
                    pr_link TEXT,
                    error_message TEXT,
                    retry_count INTEGER DEFAULT 0,
                    created_at REAL,
                    updated_at REAL
                )
            """)
        logger.debug("SQLite Database initialized.")

    def create_job(self, job_id: str, chat_id: str):
        with sqlite3.connect(self.db_path) as conn:
            conn.execute(
                "INSERT INTO jobs (job_id, chat_id, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
                (job_id, chat_id, JobState.RECEIVED, time.time(), time.time())
            )
        logger.info(f"Job created.", job_id=job_id)

    def update_state(self, job_id: str, status: str, **kwargs):
        allowed_fields = {"api_name", "pr_link", "error_message"}
        set_clauses = ["status = ?", "updated_at = ?"]
        params = [status, time.time()]
        for k, v in kwargs.items():
            if k in allowed_fields:
                set_clauses.append(f"{k} = ?")
                params.append(v)
        params.append(job_id)
        with sqlite3.connect(self.db_path) as conn:
            conn.execute(f"UPDATE jobs SET {', '.join(set_clauses)} WHERE job_id = ?", params)
        logger.info(f"State transitioned to {status}", job_id=job_id)

    def increment_retry(self, job_id: str):
        with sqlite3.connect(self.db_path) as conn:
            conn.execute("UPDATE jobs SET retry_count = retry_count + 1, updated_at = ? WHERE job_id = ?", (time.time(), job_id))

    def get_job(self, job_id: str) -> Optional[Dict[str, Any]]:
        with sqlite3.connect(self.db_path) as conn:
            conn.row_factory = sqlite3.Row
            cursor = conn.execute("SELECT * FROM jobs WHERE job_id = ?", (job_id,))
            row = cursor.fetchone()
            return dict(row) if row else None
