import os
import requests

from dotenv import load_dotenv

load_dotenv()
load_dotenv(os.path.join(os.path.dirname(__file__), "../../.env"))
load_dotenv(os.path.join(os.getcwd(), "healer/.env"))

class TelegramManager:
    def __init__(self, token=None, chat_id=None):
        self.token = token or os.getenv("TELEGRAM_BOT_TOKEN")
        self.chat_id = chat_id or os.getenv("TELEGRAM_CHAT_ID")

    def send_message(self, text):
        if not self.token or not self.chat_id:
            print("Warning: Telegram credentials not found. Skipping notification.")
            return False

        url = f"https://api.telegram.org/bot{self.token}/sendMessage"
        payload = {
            "chat_id": self.chat_id,
            "text": text,
            "parse_mode": "HTML",
            "disable_web_page_preview": False
        }
        try:
            print(f"➜ Sending Telegram Notification...")
            response = requests.post(url, json=payload)
            if response.status_code != 200:
                print(f"✗ Telegram API Error ({response.status_code}): {response.text}")
            return response.status_code == 200
        except Exception as e:
            print(f"✗ Error sending Telegram notification: {e}")
            return False

    def send_healing_report(self, results):
        if not results:
            return

        print(f"ℹ Preparing report for {len(results)} results...")
        message = "<b>🤖 Self-Healing Report</b>\n\n"
        for res in results:
            status_emoji = "✅" if res['status'] == 'fixed' else "❌"
            message += f"{status_emoji} <b>Test</b>: <code>{res['test_name']}</code>\n"
            message += f"   <b>Result</b>: {res['status'].capitalize()}\n"
            
            if res.get('explanation'):
                # Escape HTML special characters in explanation
                expl = res['explanation'].replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
                message += f"   <b>Fix</b>: {expl[:120]}...\n"
            
            if res.get('pr_url'):
                message += f"   <b>PR</b>: <a href='{res['pr_url']}'>View Pull Request</a>\n"
            
            message += "\n"

        self.send_message(message)
