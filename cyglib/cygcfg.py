
#system defined libs
import urllib2
import os
import tarfile
import getpass
import shutil
from shutil import move
from subprocess import Popen
from subprocess import PIPE

from commonlib import wget
from commonlib import untar
from commonlib import stream_exec
from commonlib import print_console

from util import quote_dos_path

from Globals import ARGS
from Globals import CONSOLE


CYGWIN_URL=ARGS.BASE_URL+"Gitdev/cyghead.tar"
INSTALLER_NAME="cyghead.tar"
DEFAULT_OPTIONS="-q -L " #for offline install
EGGFILE="setuptools-0.6c11-py2.6.egg"
#CYGWIN_URL="http://cygwin.com/setup.exe"
#CYGWIN_MIRRORS_URL="http://www.cygwin.com/mirrors.lst"
#DEFAULT_MIRROR="ftp://mirrors.kernel.org/sourceware/cygwin/"
#DEFAULT_MIRROR="ftp://ftp.esat.net/mirrors/sources.redhat.com/pub/cygwin/"
#DEFAULT_MIRROR="http://mirror.cpsc.ucalgary.ca/mirror/cygwin.com/;mirror.cpsc.ucalgary.ca"
#DEFAULT_OPTIONS="-q -s "+DEFAULT_MIRROR #for online install

member_count=0
member_total=0

def get_mirrors():
	print_console("Fetching list of cygwin mirrors")
	mirrors=urllib2.urlopen(CYGWIN_MIRRORS_URL) 
	
	mirrorlist=list()

	for line in mirrors:
		toke=line.split(";")
		url=toke[0]
		name=toke[1]
		country=toke[2]
		state=toke[3]
		mirror=(url,name,country,state)
		mirrorlist.append(mirror)

	return mirrorlist	

def get_cygwin_installer():
	cwd=os.getcwd()
	os.chdir(ARGS.install_dir)
	wget(CYGWIN_URL,INSTALLER_NAME,CONSOLE)		
	untar(INSTALLER_NAME)
	if (os.path.lexists(ARGS.install_dir+"/cyghead")):	
		move(ARGS.install_dir+"/cyghead/cygpak",ARGS.install_dir+"/cygpak")	
		move(ARGS.install_dir+"/cyghead/setup.exe",ARGS.install_dir+"/setup.exe")
	os.chdir(cwd)


def win_to_unix_path(win_path):
	unix_path = win_path.replace("\\", "/")
	unix_path = unix_path.replace(" ", "\ ")
	return unix_path

def set_max_members(new_max_members):
	global max_members 
	max_members= new_max_members

def cygwin_untar(windows_path,filename, directory ="/home/BIRCH",noroot=False):
	global member_total
	global member_count

	tarball = tarfile.open(windows_path)

	print_console("Calculating tar info, this may take some time")
	info=tarball.getmembers()
	tarball.close()

	member_total=len(info)
	member_count=0
	print_console("There are "+str(member_total)+ " members in "+filename)
	CONSOLE.setProgress(0)
	print_console("Beginning the extraction process")
			
	cmd = "cd "+directory+" && tar -xvpf "+filename
	
	cygwin_exec(cmd,callbackfunc=untar_callback,verbosity=False)

	print_console("Extracted "+str(member_count)+ " members from archive.")
	if (noroot==True):
		if (info[0].name.find("pax_global_header")>=0):
			print_console("Trimming global header")
			info.pop(0)
		rootpath=info.pop(0)
		print_console("Using "+rootpath.name+" as root tar dir.")

		contents=os.listdir(rootpath.name)
		for each in contents:
			print_console("Moving "+each+" to rebased root path.")
			shutil.move(rootpath.name+"/"+each,os.getcwd()+"/"+each)

		shutil.rmtree(rootpath.name)

	member_count=0
	member_total=0

	CONSOLE.setProgress(0)
	CONSOLE.hideProgress()

def untar_callback():
	global member_count
	global member_total

	member_count = member_count+1
	percent=int((float(member_count)/member_total)*100)	
	CONSOLE.setProgress(percent)
		

def install_cygwin():
	cwd=os.getcwd()
	os.chdir(ARGS.install_dir)

	COMMAND=quote_dos_path(ARGS.install_dir)+"/setup.exe"+" "+DEFAULT_OPTIONS+" -l " + quote_dos_path(ARGS.CYGPAK_DIR) + " -R " + quote_dos_path(ARGS.CYGWIN_DIR)+" -P subversion,wget,tcsh,python "
	print_console("Using command: "+COMMAND)

	while (not os.path.lexists(ARGS.CYGWIN_DIR)):#this is for windows 7, keep nagging for auth
		output=stream_exec(COMMAND,verbose=False)



	print_console("Linking BIRCH into home directory")
	cygwin_exec("cd /home && ln -s "+quote_dos_path(ARGS.install_dir)+" /home/BIRCH")
	cygwin_exec("cd && ln -s /home/BIRCH ")

	if (os.path.lexists(ARGS.install_dir+"/cyghead") ):	
		print_console("Setting up cygwin to work with windows java")
		print_console("Installing easy_install and cygwinreg")
		cygwin_exec("cd /home/BIRCH/cyghead && sh "+EGGFILE)
		cygwin_exec("easy_install cygwinreg")

		cygwin_exec("mv /home/BIRCH/cyghead/java_bridge /bin/java")
		cygwin_exec("mv /home/BIRCH/cyghead/browser_bridge /bin/browser")
		cygwin_exec("mv /home/BIRCH/cyghead/apt-get /bin/apt-get")

		cygwin_exec("chmod +x /bin/java")
		cygwin_exec("chmod +x /bin/browser")
		cygwin_exec("chmod +x /bin/apt-cyg")
		print_console("Finished installing cygwin java compatability layer")
	else:
		print_console("Error! cyghead not found")
		raise Exception
	os.chdir(cwd)

def cygwin_exec(command,verbosity=True,callbackfunc=None):

	command=command.replace("\"","\\\"")
	cmd=quote_dos_path(ARGS.CYGWIN_DIR)+"/bin/bash.exe -lc \""+command+"\""

	output=stream_exec(cmd,verbose=verbosity,callback=callbackfunc)
	return output

