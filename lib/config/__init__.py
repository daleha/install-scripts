import json
import os, sys

from log import *


class Args:

    def __init__(self,installType):
        self.installType = installType

    def loadOpts(self):
        configpath = os.path.join(os.path.dirname(__file__),"config.json")
        if (os.path.exists(configpath)):
            info("Using arguments from file "+configpath)
        else:
            info("No configuration file specified, falling back to interactive setup")


 

def getArgs(filename):
    helpmsg = "Usage: "+filename+" [install|update] [installpath] \n\
                  If installpath is not specified, the parent directory (../) is assumed \n\
                  Example: "+filename+" install /home/someuser/BIRCH \n\
                  Example: "+filename+" update /home/someuser/BIRCH "

    if len(sys.argv) == 1:
        print helpmsg
    elif len( sys.argv ) > 1:

        if len (sys.argv  ) > 2:
            path = os.path.abspath(sys.argv[2])
        else:
            path= os.path.join(os.getcwd(),"..")

        if (os.path.isdir(path) and os.path.isdir( os.path.join(path,"admin"))):
            info("Detected a birch installation at "+path)
        else:
            error("Unable te determine BIRCH directory, please specify path to where BIRCH is installed, or will be installed")

        args = None

        if sys.argv[1] == "install":
            args = Args ("install")

        elif sys.argv[1] == "update":
            args = Args ("update")
        else:
            print helpmsg

        if (args != None ):
            args.loadOpts() 

        return args

    
