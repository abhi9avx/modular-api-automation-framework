import os
import requests

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
            "parse_mode": "Markdown"
        }
        try:
            response = requests.post(url, json=payload)
            return response.status_code == 200
        except Exception as e:
            print(f"Error sending Telegram notification: {e}")
            return False

    def send_healing_report(self, results):
        """
        Results: list of dicts with {test_name, status, fix_suggestion, pr_url}
        """
        if not results:
            return

        message = "🤖 *Self-Healing Report*\n\n"
        for res in results:
            status_emoji = "✅" if res['status'] == 'fixed' else "❌"
            message += f"{status_emoji} *Test*: `{res['test_name']}`\n"
            message += f"   *Result*: {res['status'].capitalize()}\n"
            if res.get('explanation'):
                message += f"   *Fix*: {res['explanation'][:100]}...\n"
            if res.get('pr_url'):
                message += f"   *PR*: [View PR]({res['pr_url']})\n"
            message += "\n"

        self.send_message(message)
