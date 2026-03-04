import requests
import json
import time
import sqlite3

def run_smoke_test():
    url = "http://localhost:8080/webhook"
    payload = {
        "message": {
            "from": {"username": "abhi9avx"},
            "text": "curl https://httpbin.org/get\nExpected Response:\n{\n  \"url\": \"https://httpbin.org/get\"\n}"
        }
    }
    
    # 1. Trigger Webhook
    print("Testing Webhook endpoint...")
    resp = requests.post(url, json=payload)
    print(f"Response: {resp.status_code} - {resp.json()}")
    assert resp.status_code == 200
    job_id = resp.json()["job_id"]
    
    # 2. Check Idempotency immediately
    print("\nTesting Idempotency (sending same request again)...")
    resp2 = requests.post(url, json=payload)
    print(f"Response: {resp2.status_code} - {resp2.json()}")
    assert "duplicate" in resp2.json().get("note", "").lower()
    
    # 3. Monitor Database for transitions
    print("\nMonitoring Job State in SQLite...")
    time.sleep(10) # Give it time to start processing
    
    with sqlite3.connect("agent_state.db") as conn:
        conn.row_factory = sqlite3.Row
        job = conn.execute("SELECT * FROM jobs WHERE job_id = ?", (job_id,)).fetchone()
        if job:
            print(f"Job Status: {job['status']}")
            print(f"Trigger Path: {job['trigger_path']}")
        else:
            print("❌ Job not found in database!")
            exit(1)

    print("\n✅ Smoke Test Part 1 Passed (Webhook/Idempotency/State)!")

if __name__ == "__main__":
    run_smoke_test()
