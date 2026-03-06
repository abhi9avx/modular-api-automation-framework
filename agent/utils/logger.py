import logging
import sys

def get_logger(name: str):
    logger = logging.getLogger(name)
    if not logger.handlers:
        handler = logging.StreamHandler(sys.stdout)
        formatter = logging.Formatter('%(asctime)s | [%(levelname)s] | JobID=%(job_id)s | %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
        handler.setFormatter(formatter)
        logger.addHandler(handler)
        logger.setLevel(logging.DEBUG)
    return JobIdLoggerAdapter(logger, {'job_id': 'SYSTEM'})

class JobIdLoggerAdapter(logging.LoggerAdapter):
    def process(self, msg, kwargs):
        job_id = kwargs.get('job_id', self.extra.get('job_id', 'SYSTEM'))
        # remove job_id from kwargs so it doesn't cause errors if passed to standard logger
        if 'job_id' in kwargs: 
            del kwargs['job_id']
        return f"{msg}", {**kwargs, 'extra': {'job_id': job_id}}
