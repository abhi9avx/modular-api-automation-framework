import shlex
import re
from typing import Dict, Any

class CurlParser:
    def parse_full_message(self, message: str) -> Dict[str, Any]:
        """Extracts api_name, curl, and expected result from a raw telegram message."""
        api_name_match = re.search(r'API Name:\s*(.*)', message)
        api_name = api_name_match.group(1).split('\n')[0].strip() if api_name_match else "unknown"

        curl_match = re.search(r'curl\s+(.*)', message, re.DOTALL)
        curl_raw = curl_match.group(0) if curl_match else ""
        
        # Crude split for expected result if it exists after curl block
        expected_result = ""
        if "Expected Result:" in message:
            expected_result = message.split("Expected Result:")[1].strip()

        return {
            "api_name": api_name,
            "curl": curl_raw,
            "expected_result": expected_result
        }
