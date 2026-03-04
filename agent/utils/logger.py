import logging
import os
from typing import Any

class JobIdLoggerAdapter(logging.LoggerAdapter):
    def process(self, msg: str, kwargs: Any) -> tuple[str, Any]:
        job_id = kwargs.pop("job_id", self.extra.get("job_id", "SYSTEM"))
        return f"JobID={job_id} | {msg}", kwargs

def get_logger(name: str):
    logger = logging.getLogger(name)
    if not logger.handlers:
        logger.setLevel(logging.DEBUG)
        handler = logging.StreamHandler()
        formatter = logging.Formatter('%(asctime)s | [%(levelname)s] | %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
        handler.setFormatter(formatter)
        logger.addHandler(handler)
    return JobIdLoggerAdapter(logger, {"job_id": "SYSTEM"})
