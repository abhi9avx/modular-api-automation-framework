import os
from pathlib import Path
from dotenv import load_dotenv

# Base directory of the project
BASE_DIR = Path(__file__).resolve().parents[2]
ENV_PATH = BASE_DIR / ".env"

# Force load the root .env file exclusively
load_dotenv(dotenv_path=ENV_PATH, override=True)

# Centralized Settings
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
TELEGRAM_BOT_TOKEN = os.getenv("TELEGRAM_BOT_TOKEN")
TELEGRAM_CHAT_ID = os.getenv("TELEGRAM_CHAT_ID")
ALLOWED_TELEGRAM_USERS = os.getenv("ALLOWED_TELEGRAM_USERS", "").split(",")
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")

# Safety Guard
if not GEMINI_API_KEY or GEMINI_API_KEY == "your_key_here":
    raise RuntimeError("❌ Invalid GEMINI_API_KEY loaded! Please check your root .env file.")

if not TELEGRAM_BOT_TOKEN or TELEGRAM_BOT_TOKEN == "your_token_here":
    raise RuntimeError("❌ Invalid TELEGRAM_BOT_TOKEN loaded!")
