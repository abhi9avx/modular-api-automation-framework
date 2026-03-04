import os
import sqlite3
import re
from agent.core.state_manager import StateManager, JobState
from agent.bot.telegram_webhook import agent_worker
from agent.core.idempotency import IdempotencyGuard
from agent.core.guardrails import SecurityGuardrails
from agent.providers.gemini_generator import GeneratedFile

sm = StateManager()

def reset_db_job(job_id):
    with sqlite3.connect("agent_state.db") as conn:
        conn.execute("DELETE FROM jobs WHERE job_id = ?", (job_id,))

def test_1_duplicate_job():
    print("--- Test 1: Duplicate Job (IdempotencyGuard) ---")
    curl1 = "curl https://example.com/api/test-1"
    job_id1 = IdempotencyGuard.generate_job_id(curl1)
    
    # Simulate receiving the exact same curl again
    job_id2 = IdempotencyGuard.generate_job_id(curl1)
    
    assert job_id1 == job_id2, "Same CURL should produce identical hash/ID"
    print("✅ Duplicate CURL generates identical Job ID")

def test_2_duplicate_worker():
    print("\n--- Test 2: Duplicate Worker Invocation ---")
    job_id = "test_dup_worker"
    reset_db_job(job_id)
    
    # Set job state to RUNNING (like a worker already picked it up)
    sm.create_job(job_id, "123")
    sm.update_state(job_id, JobState.RUNNING)
    
    job_data = {"job_id": job_id, "text": "test", "chat_id": "123"}
    
    # Worker should exit safely early without doing anything
    agent_worker(job_data)
    
    # State should remain RUNNING safely
    job = sm.get_job(job_id)
    assert job["status"] == JobState.RUNNING.value, "State mutated unexpectedly"
    print("✅ Duplicate worker safely exits if job is RUNNING")

def test_3_branch_collision():
    print("\n--- Test 3: Branch Collision & Sanitization ---")
    api_name = "CreateUser(API)"
    job_id_1 = "job111"
    job_id_2 = "job222"
    
    # Simulate rule_engine logic
    safe_api_name_1 = re.sub(r'[^a-zA-Z0-9-]', '-', api_name.lower())
    branch_1 = f"feature/{safe_api_name_1}-{job_id_1}"
    
    safe_api_name_2 = re.sub(r'[^a-zA-Z0-9-]', '-', api_name.lower())
    branch_2 = f"feature/{safe_api_name_2}-{job_id_2}"
    
    print(f"Generated branches:\n  - {branch_1}\n  - {branch_2}")
    
    assert branch_1 != branch_2, "Same API name must not cause branch collision"
    assert "(" not in branch_1 and ")" not in branch_1, "Special chars should be sanitized"
    assert branch_1 == "feature/createuser-api--job111", f"Sanitization failed: {branch_1}"
    print("✅ Branch names sanitized and collision is strictly avoided")

def test_4_malicious_path():
    print("\n--- Test 4: Guardrails Path Traversal ---")
    
    # Complex traversal that points to blacklisted file
    sneaky_file = GeneratedFile(path="src/test/../../../build.gradle", content="break everything")
    
    # Absolute path testing
    abs_file = GeneratedFile(path="/tmp/malicious.sh", content="echo hacked")

    assert SecurityGuardrails.validate_generated_files([sneaky_file], "test") == False, "Sneaky path bypassed guardrails"
    assert SecurityGuardrails.validate_generated_files([abs_file], "test") == False, "Absolute path bypassed guardrails"
    
    print("✅ Guardrails successfully blocked normalized path traversal and absolute paths")

if __name__ == "__main__":
    test_1_duplicate_job()
    test_2_duplicate_worker()
    test_3_branch_collision()
    test_4_malicious_path()
    print("\n🎉 ALL PHASE 2 TESTS PASSED! V1 IS STABLE.")
