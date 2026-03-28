import json
import re

def extract_json(text: str):
    """Robust JSON extraction from LLM text."""
    try:
        # Try finding JSON block
        match = re.search(r'\{.*\}', text, re.DOTALL)
        if match:
            return json.loads(match.group(0))
        return json.loads(text)
    except Exception:
        return None
