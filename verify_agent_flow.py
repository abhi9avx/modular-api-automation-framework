import os
import uuid
import time
from agent.bot.telegram_webhook import agent_worker
from agent.core.state_manager import StateManager

def run_test():
    print("Starting final internal E2E verification...")
    sm = StateManager()
    
    # Mock message with curl and response
    curl_message = """
    curl -X GET "https://httpbin.org/get" -H "accept: application/json"
    Expected Response:
    {
      "url": "https://httpbin.org/get"
    }
    """
    
    job_id = f"test_testng_{uuid.uuid4().hex[:8]}"
    chat_id = "973464781"
    
    # Initialize job state
    sm.create_job(job_id, chat_id)
    
    job_data = {
        "job_id": job_id,
        "text": curl_message,
        "chat_id": chat_id
    }
    
    # Execute worker
    agent_worker(job_data)
    
    # Wait for completion
    job = sm.get_job(job_id)
    print(f"Final Job Status: {job['status']}")
    if job['status'] == 'PR_CREATED':
        print(f"✅ Success! PR Link: {job['pr_link']}")
    else:
        print(f"❌ Failed. Error: {job.get('error_message')}")

if __name__ == "__main__":
    run_test()
