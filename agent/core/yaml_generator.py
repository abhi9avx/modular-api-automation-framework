import yaml
import os
import time
from typing import Dict, Any
from agent.utils.logger import get_logger

logger = get_logger("YamlGenerator")

class YamlGenerator:
    def generate_trigger(self, structured_curl: Dict[str, Any], expected_response: str, job_id: str) -> str:
        url_path = structured_curl['url'].split('/')[-1] or "api"
        trigger_data = {
            "version": "1.0",
            "api_name": url_path.replace("-", "_").lower(),
            "request": structured_curl,
            "expected_response_sample": expected_response,
            "metadata": {
                "generated_at": time.strftime("%Y-%m-%d %H:%M:%S"),
                "job_id": job_id
            }
        }
        os.makedirs("rules/pending", exist_ok=True)
        filename = f"rules/pending/{trigger_data['api_name']}-{time.strftime('%Y%m%d_%H%M%S')}.yml"
        with open(filename, 'w') as f:
            yaml.dump(trigger_data, f, sort_keys=False)
        logger.info(f"Generated trigger artifact: {filename}", job_id=job_id)
        return filename
