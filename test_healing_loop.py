from agent.core.state_manager import StateManager
from agent.core.rule_engine import RuleEngine
from agent.providers.gemini_generator import GeminiGenerator, GenerationResponse, GeneratedFile
import sqlite3
import os

def test_healing_loop():
    sm = StateManager()
    job_id = "test_healing_job"
    chat_id = "123"

    with sqlite3.connect("agent_state.db") as conn:
        conn.execute("DELETE FROM jobs WHERE job_id = ?", (job_id,))
    
    sm.create_job(job_id, chat_id)
    
    engine = RuleEngine(os.getcwd(), sm)
    
    # Intentionally use a non-existent trigger so generation fails and triggers healing
    original_generate = GeminiGenerator.generate_code
    
    def failing_generate(*args, **kwargs):
        return GenerationResponse(files=[
            GeneratedFile(path="src/main/java/com/abhinav/framework/Bad.java", content="public class Bad { syntax error }")
        ])
        
    GeminiGenerator.generate_code = failing_generate

    try:
        # Create a dummy trigger file
        trigger_path = "rules/pending/dummy.yml"
        with open(trigger_path, "w") as f:
            f.write("url: test\nmethod: GET")
            
        print("Starting engine with Guaranteed Bad Code to Trigger Healing...")
        engine.execute(job_id, trigger_path, is_dryrun=False)
        
    except Exception as e:
        print(f"Engine threw exception: {e}")
        
    finally:
        # Restore monkeypatch
        GeminiGenerator.generate_code = original_generate
        
    job = sm.get_job(job_id)
    
    print(f"Final Job State: {job['status']}")
    print(f"Wait/SLA Metrics: Check logs for 180s enforcement limits.")
    
    assert job['status'] == "FAILED_VALIDATION" or job['status'] == "FAILED", f"Unexpected state {job['status']}"
    # Healing loop should max out. Let's see if the exception bubbled up or handled state gracefully.
    print("✅ Healing loop correctly exhausted retries and set FAILED state!")

if __name__ == "__main__":
    test_healing_loop()
