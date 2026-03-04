import os
from fastapi import FastAPI, Request
from pydantic import BaseModel, Field
from typing import Optional, Dict, Any
from dotenv import load_dotenv
from agent.utils.logger import get_logger
from agent.core.state_manager import StateManager
from agent.core.job_queue import JobQueue
from agent.core.idempotency import IdempotencyGuard
from agent.parser.curl_parser import CurlParser
from agent.core.yaml_generator import YamlGenerator
from agent.core.rule_engine import RuleEngine

load_dotenv()
logger = get_logger("TelegramWebhook")
app = FastAPI()
state_manager = StateManager()
job_queue = JobQueue()
idempotency = IdempotencyGuard()
engine = RuleEngine()

ALLOWED_USERS = [int(u) for u in os.getenv("ALLOWED_TELEGRAM_USERS", "").split(",") if u]

class TelegramMessage(BaseModel):
    chat: Dict[str, Any]
    from_user: Dict[str, Any] = Field(..., alias="from")
    text: str

class TelegramWebhookPayload(BaseModel):
    update_id: int
    message: Optional[TelegramMessage]

def agent_worker(job_data: Dict[str, Any]):
    job_id = job_data["job_id"]
    text = job_data["text"]
    chat_id = job_data["chat_id"]
    logger.info("Worker processing job.", job_id=job_id)
    
    parser = CurlParser()
    structured_curl, expected_resp = parser.parse_full_message(text, job_id)
    state_manager.update_state(job_id, "PARSED")
    
    yaml_gen = YamlGenerator()
    trigger_path = yaml_gen.generate_trigger(structured_curl, expected_resp, job_id)
    state_manager.update_state(job_id, "YAML_GENERATED", api_name=structured_curl['url'].split('/')[-1])
    
    logger.debug(f"Handoff to Rule Engine. Trigger: {trigger_path}", job_id=job_id)
    engine.execute(job_id, trigger_path, is_dryrun=False)

@app.on_event("startup")
def startup_event():
    job_queue.start_workers(num_workers=2, worker_func=agent_worker)

@app.post("/webhook")
async def webhook_endpoint(payload: TelegramWebhookPayload):
    if not payload.message or not payload.message.text:
        return {"ok": True}
    
    user_id = payload.message.from_user.get("id")
    if user_id not in ALLOWED_USERS:
        logger.warning(f"Unauthorized access attempt from user: {user_id}")
        return {"ok": False, "reason": "Unauthorized"}
    
    text = payload.message.text
    job_id = idempotency.generate_job_id(text)
    
    if idempotency.is_already_processing(job_id):
        return {"ok": True, "job_id": job_id, "status": "already_processing"}
    
    state_manager.create_job(job_id, str(payload.message.chat["id"]))
    job_queue.enqueue({"job_id": job_id, "text": text, "chat_id": payload.message.chat["id"]})
    
    return {"ok": True, "job_id": job_id}
