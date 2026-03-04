import os
import re
from typing import List, Dict, Any, Optional
from pydantic import BaseModel, Field
from agent.utils.logger import get_logger
from agent.utils.json_utils import extract_json
from google import genai
from google.genai import types

logger = get_logger("GeminiGenerator")

class GeneratedFile(BaseModel):
    path: str
    content: str

class GeminiOutput(BaseModel):
    files: List[GeneratedFile]

class GeminiGenerator:
    def __init__(self):
        self.api_key = os.getenv("GEMINI_API_KEY")
        if not self.api_key:
            raise ValueError("GEMINI_API_KEY environment variable not set.")
        self.client = genai.Client(api_key=self.api_key)
        self.prompt_template_path = "agent/prompts/rule_prompt.txt"

    def _construct_prompt(self, yaml_content: str) -> str:
        with open(self.prompt_template_path, 'r') as f:
            template = f.read()
        return template.replace("{{YAML_CONTENT}}", yaml_content)

    def generate_code(self, job_id: str, yaml_content: str) -> Optional[List[GeneratedFile]]:
        logger.info("Sending code generation request to gemini-2.0-flash", job_id=job_id)
        prompt = self._construct_prompt(yaml_content)
        try:
            response = self.client.models.generate_content(
                model='gemini-2.0-flash',
                contents=prompt,
                config=types.GenerateContentConfig(
                    temperature=0.1,
                    response_mime_type="application/json",
                    response_schema=GeminiOutput
                )
            )
            raw_output = response.text
            parsed_data = extract_json(raw_output)
            if not parsed_data:
                logger.error(f"Failed to extract JSON from Gemini output.\nRaw Output:\n{raw_output[:500]}...", job_id=job_id)
                return None
            gemini_out = GeminiOutput(**parsed_data)
            return gemini_out.files
        except Exception as e:
            logger.exception(f"Gemini API Error or Validation Failure: {e}", job_id=job_id)
            return None
