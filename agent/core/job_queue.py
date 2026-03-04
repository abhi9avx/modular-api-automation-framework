import threading
import queue
from agent.utils.logger import get_logger

logger = get_logger("JobQueue")

class JobQueue:
    def __init__(self):
        self._queue = queue.Queue()
        self._workers = []

    def enqueue(self, job_data: dict):
        self._queue.put(job_data)
        logger.info(f"Enqueued job: {job_data.get('job_id')}")

    def start_workers(self, worker_func, num_workers=1):
        for i in range(num_workers):
            t = threading.Thread(target=self._worker_loop, args=(worker_func,), daemon=True)
            t.start()
            self._workers.append(t)
        logger.info(f"Started {num_workers} worker threads.")

    def _worker_loop(self, worker_func):
        while True:
            job_data = self._queue.get()
            try:
                worker_func(job_data)
            except Exception as e:
                logger.error(f"Worker crashed: {e}")
            finally:
                self._queue.task_done()
