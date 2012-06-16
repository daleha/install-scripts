'''
Created on Jun 27, 2010

@author: Dale
'''
import os
import sys
import fileinput
from Globals import CONSOLE
from Globals import ARGS
from Globals import print_console



USER=os.environ.get("USER")
REPLACEKEY="/home/psgendb/BIRCHDEV"

common_files=['$BIRCH/admin/cshrc.source','$BIRCH/admin/profile.source','$BIRCH/admin/add_to_cshrc','$BIRCH/admin/add_to_login','$BIRCH/admin/add_to_profile','$BIRCH/admin/newuser','$BIRCH/dat/fasta/fastgbs','$BIRCH/dat/fasta/*.fil','$BIRCH/install-birch/htmldir.param','$BIRCH/dat/fasta/fastgbs','$BIRCH/dat/XLandscape/XLand','$BIRCH/admin.uninstall/cshrc.source','$BIRCH/admin.uninstall/profile.source']
birch_only_files=['$BIRCH/java/ArrayNorm/ArrayNorm.lax','$BIRCH/java/Bluejay/Bluejay.lax','$BIRCH/java/Jalview/Jalview.lax','$BIRCH/java/genographer/genograph.cfg','$BIRCH/pkg/NCBI/.ncbirc',"$BIRCH/admin/launchers/birch.desktop"]





"""
replace all instances of searchExp in file with replaceExp
"""
def replace_all(file,searchExp,replaceExp):
    print_console("Replacing lines in file"+file)
    if (os.path.lexists(file)):
        for line in fileinput.input(file, inplace=1):
            if searchExp in line:
                line = line.replace(searchExp,replaceExp)
            sys.stdout.write(line)
        
"""
Checks if the install is a minibirch
"""
def check_mini(home):
    props= open(home+"/local/admin/BIRCH.properties")
    
    for line in props:
        if "BirchProps.minibirch" in line:
            if (line.find("true")>=0):
                return True
            else:
                return False
            
"""
Sets up for a minibirch, needs a homedir as param
"""
def setup_mini(home):
    files_to_set=common_files
    set_birch_home(files_to_set,home)
    
"""
Sets up for a standard birch, needs a homedir as param
""" 
def setup_birch(home):
    files_to_set=common_files+birch_only_files
    set_birch_home(files_to_set,home)

"""
called by setup birch or setup mini, 
"""
def set_birch_home(files,home):
   
    if (home[len(home)-1]=="/"):
	home=home[0:len(home)-1]

    print_console("Using "+home+" as home directory for replacement")
 
    for each in files:
        each=each.replace("$BIRCH",home)
	if (os.path.lexists(each)):
		print_console("Setting location of BIRCH home directory as "+home+" in "+each)
		#replace all $BIRCH tags with the actual path to $BIRCH
		replace_all(each, "$BIRCH/",home+"/")#note that the slash is necessary because of vars like $BIRCH_DEBUG
		replace_all(each, "$BIRCH ",home)#note that the space is necessary

		#replace /home/psgendb tags with home
		replace_all(each,REPLACEKEY,home)

	else:
		print_console("The path "+each+" does not exist")
"""
A catch-all ldir replace to ensure custom menus are drawn:
done because there are so many ldirs scattered about, maintaining a list of them
is hopeless
"""
def replace_all_ldirs(home):

    if (home[len(home)-1]=="/"):
	home=home[0:len(home)-1]


    for dname, dirs, files in os.walk(home+"/dat"):
        for fname in files:
            if (fname=="ldir.param"):
                fpath = os.path.join(dname, fname)
                replace_all(fpath,REPLACEKEY,home)
        
"""
Sets up permissions
"""
def set_permissions(home):
   
    print_console("Setting permissions")
    os.popen("chmod a+rx .") 
    os.popen("chmod a+rx newuser "+home+"/install-birch/makelinks.sh")
    os.popen("chmod a+rx "+home)
    
    contents=os.listdir(home)

    
    for each in contents:
        if (os.path.isdir(each)):
            os.popen("chmod a+rx "+each)
            
    os.popen("chmod a+rx "+home+"/admin/launchers/birch.desktop")

"""
Replaces psgendb values in birchdb for ace
"""
def set_db_id(home,original):

    print_console("Setting values for acedb") 
    db_file=home+"/public_html/birchdb/wspec/passwd.wrm"
    replace_all(db_file,original,USER)
    os.popen("chmod a+r "+db_file)

def main(homedir,homepath):
    

        if (os.path.isdir(homedir)):
            mini = check_mini(homedir)
            print_console("minibirch = "+str(mini))
            
            os.chdir(homedir+"/admin")
            if (homepath!=None):
                home=homepath
            else:
                home=homedir
                
            if (mini):

		print_console("Minibirch is not currently supported by birchhome")
                setup_mini(home)
            else:
                setup_birch(home)
            
            replace_all_ldirs(home)
            set_permissions(home)
        
            set_db_id(home,"frist")
            set_db_id(home,"psgendb")
        else:
            print_console("No such directory: "+homedir+", exitting in failure")
            exit(1)
        
    



        
        
