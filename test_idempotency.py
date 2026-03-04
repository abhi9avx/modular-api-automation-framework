from agent.core.state_manager import StateManager
from agent.bot.telegram_webhook import agent_worker

def test_idempotency():
    sm = StateManager()
    job_id = "test_duplicate_job"
    chat_id = "123"

    # Step 1: Simulate the first successful run creating state
    sm.create_job(job_id, chat_id)
    sm.update_state(job_id, "COMPLETED", pr_link="https://github.com/abc")

    print(f"Current state: {sm.get_job(job_id)['status']}")

    # Step 2: Attempt to run the worker with the SAME job_id
    job_data = {
        "job_id": job_id,
        "text": "curl https://example.com",
        "chat_id": chat_id
    }

    # This should exit early and NOT re-run
    try:
        agent_worker(job_data)
        after_state = sm.get_job(job_id)['status']
        print(f"State after duplicate execution attempt: {after_state}")
    except Exception as e:
        print(f"Error during duplicate execution: {e}")

if __name__ == "__main__":
    test_idempotency()
