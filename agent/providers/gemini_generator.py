import os
from typing import List, Optional
from pydantic import BaseModel
import google.generativeai as genai
from agent.utils.logger import get_logger
from agent.utils.json_utils import extract_json

logger = get_logger("GeminiGenerator")

class GeneratedFile(BaseModel):
    path: str
    content: str

class GenerationResponse(BaseModel):
    files: List[GeneratedFile]

class GeminiGenerator:
    def __init__(self):
        api_key = os.getenv("GEMINI_API_KEY")
        logger.info(f"Using GEMINI_API_KEY starting with: {str(api_key)[:10]}...", job_id="SYSTEM")
        genai.configure(api_key=api_key)
        self.model = genai.GenerativeModel('gemini-2.0-flash')

    def generate_code(self, prompt: str, job_id: str) -> Optional[GenerationResponse]:
        logger.info(f"Sending code generation request to gemini-2.0-flash", job_id=job_id)
        try:
            response = self.model.generate_content(prompt)
            data = extract_json(response.text)
            if data and "files" in data:
                return GenerationResponse(**data)
            
            logger.error(f"Failed to extract files from JSON. Raw text: {response.text[:500]}...", job_id=job_id)
            return None
        except Exception as e:
            logger.error(f"Generation error: {e}", job_id=job_id)
            return None
