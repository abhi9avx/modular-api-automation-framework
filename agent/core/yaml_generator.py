import os
import yaml
from datetime import datetime
from agent.utils.logger import get_logger

logger = get_logger("YamlGenerator")

class YamlGenerator:
    def __init__(self, output_dir="rules/pending"):
        self.output_dir = output_dir
        os.makedirs(output_dir, exist_ok=True)

    def generate_trigger(self, parsed_data: dict, job_id: str) -> str:
        filename = f"{parsed_data['api_name']}-{datetime.now().strftime('%Y%m%d_%H%M%S')}.yml"
        path = os.path.join(self.output_dir, filename)
        
        with open(path, "w") as f:
            yaml.dump(parsed_data, f)
            
        logger.info(f"Generated trigger artifact: {path}", job_id=job_id)
        return path
