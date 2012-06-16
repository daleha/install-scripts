#!!!! this will be $BIRCH/java/jython
import os
import sys
import fileinput

from Globals import CONSOLE
from Globals import ARGS
from Globals import print_console


'''
Created on 2010-11-08

@author: umhameld
'''

FILE_LIST=[".login", ".tcshrc",  ".cshrc", ".profile", ".bash_profile", ".bash_login", ".bashrc", ".zshenv"]
BIRCH_DIR=os.getenv('BIRCH')
def remove_birch(filename):
    if os.path.lexists(filename):
	    for line in fileinput.input(filename, inplace=1):
		if '#_BIRCH' in line:
			line = ""
		sys.stdout.write(line)




def run_uninstall(directory=None,quiet=False):
    answer =None
    
    if (("-Q" in sys.argv) or quiet):
        print_console("Running in quiet mode")
        answer='y'
    else:
        print_console (">>> You are about remove access to the BIRCH system for this account!")

       
    
        while (answer != "y" and answer !="n"):
            answer = raw_input(">>> Type y to continue, n to exit\n")
    
        if (answer=="n"):
            print_console("User cancelled operation.")
            exit()

    if(directory==None):
        os.system("python $BIRCH/admin/rmlauncher.py")
        directory="$BIRCH"
    elif(os.path.lexists(directory)):
        os.system("python "+directory+"/script/rmlauncher.py")
    
    os.chdir(os.getenv('HOME'))

    for file in FILE_LIST:
        remove_birch(os.getenv('HOME')+"/"+file)

    print_console("Done!")
    print_console("Logout and login again so that the changes can take effect.")
    print_console("If you wish to restore BIRCH access, type")
    print_console(directory+"/admin/newuser")




if __name__ == "__main__":
    run_uninstall()
