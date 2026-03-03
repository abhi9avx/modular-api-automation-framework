import os
import requests
import json
from dataclasses import dataclass
from dotenv import load_dotenv

# Load .env from current dir or healer/ dir
load_dotenv()
load_dotenv(os.path.join(os.path.dirname(__file__), "../../.env"))
load_dotenv(os.path.join(os.getcwd(), "healer/.env"))

@dataclass
class FixSuggestion:
    explanation: str
    diff: str
    confidence: float

class GeminiClient:
    def __init__(self, api_key=None, model="gemini-2.0-flash"):
        self.api_key = api_key or os.getenv("GEMINI_API_KEY")
        self.model = model
        self.api_url = f"https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent"

    def get_fix_suggestion(self, failure_context, file_content, console_logs="") -> FixSuggestion:
        if not self.api_key or self.api_key == "your_key_here":
            print("✗ Error: GEMINI_API_KEY is not set or is still the default placeholder.")
            print("  Please update healer/.env with a valid Google AI API Key.")
            return None

        prompt = self._construct_prompt(failure_context, file_content, console_logs)

        try:
            payload = {
                "contents": [{"parts": [{"text": prompt}]}],
                "generationConfig": {
                    "response_mime_type": "application/json"
                }
            }
            headers = {"Content-Type": "application/json"}
            params = {"key": self.api_key}

            response = requests.post(self.api_url, headers=headers, params=params, json=payload, timeout=60)
            if response.status_code != 200:
                print(f"✗ Gemini API Error ({response.status_code}): {response.text}")
                return None
            
            return self._parse_response(response.json())
        except Exception as e:
            print(f"✗ Error communicating with Gemini: {e}")
            return None

    def _construct_prompt(self, failure, code, logs=""):
        return f"""
You are an expert test automation engineer specializing in Java, RestAssured, and TestNG.
Your task is to analyze test failures and propose precise code fixes.

--- FAILURE CONTEXT ---
Test Class: {failure.test_class}
Test Name: {failure.test_name}
Failure Type: {failure.failure_type}
Message: {failure.message}
Stack Trace:
{failure.stack_trace}

--- RECENT CONSOLE LOGS ---
{logs}

--- SOURCE CODE ---
{code}

--- INSTRUCTIONS ---
1. Analyze the failure and identify the root cause in the source code.
2. Provide a brief explanation of the fix.
3. Provide a Git-compatible diff (unified format) for the fix.
4. **CRITICAL**: Return ONLY a valid JSON object. 
5. **CRITICAL**: All double quotes (\") inside the 'diff' or 'explanation' strings MUST be escaped with a backslash (e.g., \\"). Specifically, Java code quotes like .post(\\"url\\") must become .post(\\\\"url\\\\") in the JSON string.

--- OUTPUT FORMAT ---
{{
  "explanation": "string",
  "diff": "string (unified diff format)",
  "confidence": number (0.0 to 1.0)
}}
"""

    def _parse_response(self, response_json) -> FixSuggestion:
        try:
            # Use raw text if response_json is just a wrapper, or extract from candidates
            if isinstance(response_json, dict) and 'candidates' in response_json:
                text = response_json['candidates'][0]['content']['parts'][0]['text']
            else:
                text = str(response_json)

            # 1. Clean up common Markdown artifacts
            text = text.strip()
            if text.startswith("```json"):
                text = text[7:]
            if text.endswith("```"):
                text = text[:-3]
            text = text.strip()

            # 2. Heuristic extraction of fields if json.loads fails
            # This handles cases where Gemini puts literal newlines or unescaped quotes
            try:
                data = json.loads(text)
            except json.JSONDecodeError:
                data = self._heuristic_json_parse(text)
                if not data:
                    print("✗ Critical: Failed to parse Gemini response even with heuristics.")
                    print(f"--- RAW TEXT ---\n{text}\n----------------")
                    return None

            return FixSuggestion(
                explanation=data.get("explanation", ""),
                diff=data.get("diff", ""),
                confidence=float(data.get("confidence", 0.0))
            )
        except Exception as e:
            print(f"✗ Unexpected error parsing Gemini response: {e}")
            return None

    def _heuristic_json_parse(self, text: str) -> dict:
        """
        Extracts explanation, diff, and confidence using regex when standard JSON parsing fails.
        """
        import re
        result = {}
        
        # Extract Explanation
        explanation_match = re.search(r'"explanation"\s*:\s*"(.*?)"\s*(?:,|\})', text, re.DOTALL)
        if explanation_match:
            result["explanation"] = explanation_match.group(1).replace('\\"', '"').replace('\\n', '\n')
        
        # Extract Diff - this is the tricky one
        # Look for "diff": " and then lazily capture until we see " followed by , "confidence" or }
        diff_match = re.search(r'"diff"\s*:\s*"(.*?)"\s*(?:,\s*"confidence"|\s*\})', text, re.DOTALL)
        if diff_match:
            diff_content = diff_match.group(1)
            # Fix common LLM mistakes in the extracted diff
            # 1. Convert literal newlines to \n
            diff_content = diff_content.replace('\n', '\\n')
            # 2. Try to unescape what was meant to be a literal "
            # This is hard, but let's assume if we have \" it's correct JSON, 
            # if we have raw " it's a mistake by the LLM.
            result["diff"] = diff_content.encode().decode('unicode_escape')
            
        # Extract Confidence
        confidence_match = re.search(r'"confidence"\s*:\s*([\d.]+)', text)
        if confidence_match:
            result["confidence"] = confidence_match.group(1)
            
        return result if "diff" in result else None

    def _clean_json(self, json_str: str) -> str:
        """Deprecated in favor of heuristic_json_parse which is more robust."""
        return json_str
