import json
import re
from typing import Optional, Dict, Any

def extract_json(text: str) -> Optional[Dict[str, Any]]:
    # Remove markdown code blocks if present
    text = re.sub(r'```json\s*(.*?)\s*```', r'\1', text, flags=re.DOTALL)
    text = text.strip()
    try:
        return json.loads(text)
    except json.JSONDecodeError:
        # Fallback: find first { and last }
        match = re.search(r'\{.*\}', text, re.DOTALL)
        if match:
            try:
                return json.loads(match.group(0))
            except json.JSONDecodeError:
                return None
    return None
