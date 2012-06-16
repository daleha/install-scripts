import json
import os, sys

from log import *

import re

def validateEmail(email):

    if len(email) > 4:
        if re.match("^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$", email) != None:
            return True
    return False

class Args:

    def __init__(self,installType):
        self.installType = installType

    def setInstallDir(self,installDir):
        self.installDir = installDir
    def loadOpts(self):
        configpath = os.path.join(os.path.dirname(__file__),"config.json")
        if (os.path.exists(configpath)):
            info("Using configuration arguments from file "+configpath)
        else:
            from ui import prompt_user
            info("No configuration file specified, falling back to interactive setup")

            self.admin_email =  prompt_user("What is the administrator's email?",isbool=False)
            while( not validateEmail(self.admin_email)):
                info(self.admin_email+" is an invalid email address, please specify a valid one.")
                self.admin_email =  prompt_user("What is the administrator's email?",isbool=False)
            



 

def getArgs(filename):
    helpmsg = "Usage: "+filename+" [install|update] [installpath] \n\
                  If installpath is not specified, the parent directory (../) is assumed \n\
                  Example: "+filename+" install /home/someuser/BIRCH \n\
                  Example: "+filename+" update /home/someuser/BIRCH "

    if len(sys.argv) == 1:
        print helpmsg
    elif len( sys.argv ) > 1:

        args = None

        if sys.argv[1] == "install":
            args = Args ("install")

        elif sys.argv[1] == "update":
            args = Args ("update")
        else:
            print helpmsg

        if len (sys.argv  ) > 2:
            path = os.path.abspath(sys.argv[2])
        else:
            path= os.path.abspath(os.path.join(os.getcwd(),".."))

        if (os.path.isdir(path) and os.path.isdir( os.path.join(path,"admin"))):
            info("Detected a birch installation at "+path)
            args.setInstallDir(path)
        else:
            error("Unable te determine BIRCH directory, please specify path to where BIRCH is installed, or will be installed")


        if (args != None ):
            args.loadOpts() 

        return args

    
