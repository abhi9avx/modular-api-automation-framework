import shlex
import re
from typing import Dict, Any, Tuple
from agent.utils.logger import get_logger

logger = get_logger("CurlParser")

class CurlParser:
    @staticmethod
    def parse_full_message(message: str, job_id: str) -> Tuple[Dict[str, Any], str]:
        logger.debug(f"Parsing raw message of {len(message)} chars", job_id=job_id)
        # Split curl and response by "Expected Response:" or "Response:"
        parts = re.split(r'(?i)Expected Response:|Response:', message, maxsplit=1)
        curl_raw = parts[0].strip()
        expected_response = parts[1].strip() if len(parts) > 1 else ""
        structured_curl = CurlParser._parse_curl_command(curl_raw)
        return structured_curl, expected_response

    @staticmethod
    def _parse_curl_command(curl_command: str) -> Dict[str, Any]:
        try:
            tokens = shlex.split(curl_command)
        except ValueError:
            tokens = curl_command.split()
        
        parsed = {"method": "GET", "url": "", "headers": {}, "body": None}
        it = iter(tokens)
        for token in it:
            if token.startswith("http"):
                parsed["url"] = token.strip("'\"")
            elif token in ("-X", "--request"):
                parsed["method"] = next(it).upper()
            elif token in ("-H", "--header"):
                header_line = next(it)
                if ":" in header_line:
                    k, v = header_line.split(":", 1)
                    parsed["headers"][k.strip()] = v.strip()
            elif token in ("-d", "--data", "--data-raw"):
                parsed["body"] = next(it)
                if parsed["method"] == "GET":
                    parsed["method"] = "POST"
        return parsed
