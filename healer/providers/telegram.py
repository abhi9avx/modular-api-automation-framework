from agent.config import settings

class TelegramManager:
    def __init__(self, token=None, chat_id=None):
        self.token = token or settings.TELEGRAM_BOT_TOKEN
        self.chat_id = chat_id or settings.TELEGRAM_CHAT_ID

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

        print(f"ℹ Preparing professional report for {len(results)} results...")
        
        # Header with branding
        message = "<b>✨ API Self-Healing Report</b>\n"
        message += "━━━━━━━━━━━━━━━━━━\n\n"

        for res in results:
            status_emoji = "✅" if res['status'] == 'fixed' else "❌"
            status_text = "FIXED" if res['status'] == 'fixed' else res['status'].upper()
            
            # Test & Status Section
            message += f"{status_emoji} <b>Test Case:</b> <code>{res['test_name']}</code>\n"
            message += f"📊 <b>Status:</b> <code>{status_text}</code>\n"
            
            # Analysis Section
            if res.get('explanation'):
                expl = res['explanation'].replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
                message += f"\n🔍 <b>AI Analysis:</b>\n<i>{expl}</i>\n"
            
            # Action Section (Buttons/Links)
            if res.get('pr_url'):
                message += f"\n🛠 <b>Action Required:</b>\n"
                message += f"└ 🚀 <a href='{res['pr_url']}'><b>Review & Merge Pull Request</b></a>\n"
            
            message += "\n"

        message += "━━━━━━━━━━━━━━━━━━\n"
        message += "<i>Automated repair by Healer Agent</i>"

        self.send_message(message)
