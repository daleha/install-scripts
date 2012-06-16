#!/usr/bin/env python
import traceback
import os
import sys
import time


from log import initLogger
from log import info
from log import error

    
    
def main():

    initLogger()

    try:
        info("Started")
        start = time.time()

        #run_main(cwd) #this will produce a crash: must be called as module

        end = time.time()
        elapsed= end - start
        min = elapsed/60
        info("Installation took "+str(min)+" minutes.")


    except:
        
        err=traceback.format_exc()
        error(err)

    finally:
        info("Installation finished")



if __name__ == "__main__":
    sys.exit(main())
