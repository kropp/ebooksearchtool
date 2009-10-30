
import logging
import logging.handlers

from log_setting import *

# create logger
main_logger = logging.getLogger("main_logger")
main_logger.setLevel(logging.DEBUG)

handler = logging.handlers.RotatingFileHandler(
             'main.log', maxBytes=MAX_LOG_SIZE, backupCount=BACKUP_COUNT)
# create console handler and set level to debug
handler.setLevel(logging.DEBUG)
# create and set formatter
formatter = logging.Formatter("%(asctime)s - %(name)s - %(levelname)s - %(message)s")
handler.setFormatter(formatter)
main_logger.addHandler(handler)

main_logger.info("Start log")
print "logging"

