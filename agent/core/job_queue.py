import threading
import queue
from typing import Callable, Any
from agent.utils.logger import get_logger

logger = get_logger("JobQueue")

class JobQueue:
    def __init__(self):
        self.queue = queue.Queue()
        self.worker_threads = []

    def start_workers(self, num_workers: int, worker_func: Callable):
        logger.info("JobQueue operating in Local In-Memory mode.")
        for i in range(num_workers):
            t = threading.Thread(target=self._worker_loop, args=(worker_func, i), daemon=True)
            t.name = f"AgentWorker-{i}"
            t.start()
            self.worker_threads.append(t)
            logger.debug(f"Started worker thread: {t.name}")

    def enqueue(self, job_data: Any):
        self.queue.put(job_data)
        logger.info(f"Enqueued job", job_id=job_data.get("job_id", "SYSTEM"))

    def _worker_loop(self, worker_func: Callable, worker_id: int):
        while True:
            job_data = self.queue.get()
            if job_data is None:
                break
            try:
                worker_func(job_data)
            except Exception as e:
                logger.exception(f"Worker {worker_id} failed to process job: {e}")
            finally:
                self.queue.task_done()
