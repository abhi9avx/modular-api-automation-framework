import hashlib
import uuid

class IdempotencyGuard:
    @staticmethod
    def generate_job_id(message: str) -> str:
        # Normalize and hash the message
        clean_msg = "".join(message.split()).lower()
        hash_obj = hashlib.sha256(clean_msg.encode())
        return f"job_{hash_obj.hexdigest()[:12]}"
