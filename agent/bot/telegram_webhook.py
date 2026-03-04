import os
import time
import threading
from fastapi import FastAPI, Request, BackgroundTasks
from dotenv import load_dotenv
from agent.utils.logger import get_logger
from agent.core.state_manager import StateManager, JobState
from agent.core.job_queue import JobQueue
from agent.core.idempotency import IdempotencyGuard
from agent.parser.curl_parser import CurlParser
from agent.core.yaml_generator import YamlGenerator
from agent.core.rule_engine import RuleEngine

load_dotenv()
app = FastAPI()
logger = get_logger("Webhook")
state_manager = StateManager()
job_queue = JobQueue()
parser = CurlParser()
yaml_gen = YamlGenerator()

ALLOWED_USERS = os.getenv("ALLOWED_TELEGRAM_USERS", "").split(",")

def agent_worker(job_data: dict):
    job_id = job_data["job_id"]
    engine = RuleEngine(os.getcwd(), state_manager)
    try:
        engine.execute(job_id, job_data["trigger_path"], is_dryrun=False)
    except Exception as e:
        logger.error(f"Execution crashed: {e}", job_id=job_id)

# Start workers
job_queue.start_workers(agent_worker)

@app.post("/webhook")
async def webhook_endpoint(request: Request):
    data = await request.json()
    logger.debug(f"Incoming webhook data: {data}")
    
    message = data.get("message", {})
    from_info = message.get("from", {})
    user_id = str(from_info.get("id", ""))
    username = str(from_info.get("username", ""))
    text = message.get("text", "")

    logger.info(f"Message from UserID={user_id}, Username={username}")

    if user_id not in ALLOWED_USERS and username not in ALLOWED_USERS:
        logger.warning(f"Unauthorized access from ID={user_id}, Username={username}")
        return {"status": "unauthorized"}

    if not text:
        return {"status": "no text"}

    job_id = IdempotencyGuard.generate_job_id(text)
    state_manager.create_job(job_id, text)
    
    parsed = parser.parse_full_message(text)
    trigger_path = yaml_gen.generate_trigger(parsed, job_id)
    state_manager.update_state(job_id, JobState.YAML_GENERATED, trigger_path=trigger_path)

    job_queue.enqueue({"job_id": job_id, "trigger_path": trigger_path})
    return {"status": "enqueued", "job_id": job_id}

@app.get("/health")
def health_check():
    return {"status": "ok"}
