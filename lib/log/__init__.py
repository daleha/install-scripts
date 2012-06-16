import logging
from datetime import datetime

def initLogger():
    d = datetime
    t = d.today()
    datestr = t.strftime("%y_%m_%d_%H_%M_%S")
    logging.basicConfig(filename="getbirch_log_"+datestr+".log", level=logging.INFO)


def debug(message):
    logging.debug(message) 
    print(message)


def info(message):
    logging.info(message) 
    print(message)


def error(message):
    logging.error(message) 
    print(message)
