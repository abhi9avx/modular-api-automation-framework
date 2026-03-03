import yaml
import os

class ConfigLoader:
    def __init__(self, config_path):
        self.config_path = config_path

    def load_config(self):
        if not os.path.exists(self.config_path):
            return {
                "max_attempts": 3,
                "artifacts_path": "build/test-results/test"
            }
        
        with open(self.config_path, "r") as f:
            return yaml.safe_load(f)
