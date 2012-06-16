#system includes
import getpass
import os
from shutil import move
from subprocess import Popen
from subprocess import PIPE

#lib includes
import commonlib
from commonlib import stream_exec 
from commonlib import print_label 


#install includes
import nobirch
import birchhome
from commonlib import *
#from util import quote_dos_path
#from cygcfg import cygwin_exec
#from cygcfg import *




def main_install():

	print_label('Starting the intall process')	
	print_console("The current version of BIRCH is "+ARGS.NEWEST_VERSION)
	print_console("Using: "+ARGS.install_dir+" as installation directory")
    

	if "winxp-32" in ARGS.platform:

		#quotedBasePath=quote_dos_path(ARGS.install_dir).replace("\"","\\\"")+"\\\""
		#print_console("Retrieving unix compatibility layer")
		#get_cygwin_installer()
		#ARGS.jython_path=quote_dos_path(ARGS.jython_path.replace("\\","/"))
		#
		#install_cygwin()
		#extract_tarballs(windows=True)

		#print_console("Making birch local")
		#cygwin_exec("mv /home/BIRCH/local-generic /home/BIRCH/local")
		#print_console("Making birch properties")
		#makeProperties("/home/BIRCH")
		#print_console("Running birch home")
		##run_birchhome(ARGS.install_dir+"/","/home/BIRCH")

		#command="cd  /home/BIRCH/install-birch/ && ./birchhome.sh"
		#cygwin_exec(command)
		#set_platform(exec_func=cygwin_exec,install_dir="/home/BIRCH")
		#makeParamFile("/home/BIRCH")

		##these seem broken, everything else is fine
		#run_customdoc(cygwin_exec,"/usr/bin/python","/home/BIRCH")
		#run_htmldoc(cygwin_exec,"/usr/bin/python","/home/BIRCH")
		#cygwin_exec("/home/BIRCH/admin/newuser")
		
	else:
		
		extract_tarballs()
	
		print_console("Archives extracted.")
		run_nobirch()
		if (not ARGS.is_update):
			move_local()	
			makeProperties(ARGS.install_dir)

		run_birchhome(ARGS.install_dir+"/",ARGS.install_dir+"/")
		set_platform()	#simply calling existing setplatform
		makeParamFile(ARGS.install_dir) #see if this can be omitted, its a total hack
		run_customdoc(stream_exec,"java -jar "+ARGS.jython_path,ARGS.install_dir)
		run_htmldoc(stream_exec,"java -jar "+ARGS.jython_path,ARGS.install_dir)	
		run_newuser()
	
		
	verify_install()
		
		




def run_nobirch():

	print_label("Nobirch")
	print_console("Running nobirch")
	os.chdir(ARGS.install_dir+"/admin")
	
	nobirch.run_uninstall(directory=ARGS.install_dir,quiet=True)

def move_local():
	print_console("Moving local-generic to local")
	cwd= os.getcwd()
	
	os.chdir(ARGS.install_dir)
	move("local-generic","local")	

	os.chdir(cwd)


def update_local():
	print_label("Update started")
	print_console("Updating birch local")
	stream_exec(ARGS.install_dir+"/install-birch/update-local.sh")


def run_birchhome(install_dir,homepath):
	
	print_label("Birchhome")
	print_console("Running birchhome")
#	birchhome.main(install_dir,homepath)# need to update this once default install is using BIRCHDEV
	stream_exec(ARGS.install_dir+"/install-birch/birchhome.sh",ARGS.install_dir+"/install-birch")
	
def set_platform(exec_func=stream_exec,install_dir=ARGS.install_dir):
	print_label("Setplatform")
	print_console("Setting birch platform to "+ARGS.platform)
	if (install_dir==None):
		print_console("install dir is none!")
		install_dir=ARGS.install_dir
	print_console("Using install dir: "+install_dir)
	
	exec_func(install_dir+"/install-birch/setplatform.sh "+ARGS.platform,install_dir+"/install-birch")
	#need to run setplatform
		
		
def run_customdoc(exec_func,exec_shell_prefix,install_dir):
	print_label("Customdoc")

	print_console("Running customdoc.py")
	#fails, need to give it the params correctly
	
	"""
	The solution is probably to fix the module directly
	
	getbirch.py: Running customdoc.py
	aceback (most recent call last):
	File "/home/umhameld/BIRCH/script/customdoc.py", line 135, in <module>
	  DIRFN = sys.argv[3]
	dexError: index out of range: 3
	tbirch.py: Running htmldoc.py
		"""
	
	if(exec_func==None):
		exec_func=stream_exec	
	
	if exec_shell_prefix ==None:
		print_console("No shell specified to run customdoc.")
		raise Exception
	
	customdoc=exec_shell_prefix+" "+install_dir+"/script/customdoc.py "+install_dir+"/install-birch/oldstr.param "+install_dir+"/local/admin/newstr.param "+install_dir+"/install-birch/htmldir.param"
	print_console(ARGS.jython_path)
	
	exec_func(customdoc)



def run_htmldoc(exec_func,exec_shell_prefix,install_dir):
	print_label("Htmldoc")
	print_console("Running htmldoc.py")
	
	if(exec_func==None):
		exec_func=stream_exec	
	
	if exec_shell_prefix ==None:
		print_console("No shell specified to run customdoc.")
		raise Exception
	htmldoc=exec_shell_prefix+" "+install_dir+"/script/htmldoc.py "+install_dir+" "+ARGS.platform
	
	print_console(ARGS.jython_path)
	
	exec_func(htmldoc)



def run_newuser():
	print_label("Newuser")
	print_console("Running newuser")
	
	stream_exec("./newuser",ARGS.install_dir+"/admin")


def makeParamFile(install_dir):
	print_console("Making paramfile for customdoc")
	paramfile= open(ARGS.install_dir+"/local/admin/newstr.param","w")

	paramfile.write("~\n")
	paramfile.write("file://"+install_dir+"/public_html\n")
	paramfile.write("file://"+install_dir+"\n")
	paramfile.write(ARGS.admin_email+"\n")
	paramfile.write(install_dir+"\n")
	paramfile.write(getpass.getuser()+"\n")
	paramfile.flush()
	paramfile.close()


def makeProperties(install_dir):
	print_console("Writing BIRCH.properties for legacy script support")
	props = open(ARGS.install_dir+"/local/admin/BIRCH.properties","w")
	props.write("BirchProps.homedir="+install_dir+"\n")

	props.write("BirchProps.adminUserid="+getpass.getuser()+"\n")	
	props.write("BirchProps.birchURL=file://"+install_dir+"/public_html\n")	
	props.write("BirchProps.adminEmail="+ARGS.admin_email+"\n")	
	props.write("BirchProps.platform="+ARGS.platform+"\n")	
#BirchProps.BirchMasterCopy
	props.write("BirchProps.birchHomeURL=file://"+install_dir+"\n")	
	if ARGS.is_mini:
		mini="true"
	else:
		mini="false"

	props.write("BirchProps.minibirch="+mini+"\n")	
	props.flush()
	props.close()
	
	if (not os.path.exists(ARGS.install_dir+"/local/admin/BIRCH.properties")):
		print_console("Error, properties not written!")
	else:
		print_console("Birch properties file written")


def showCustomDoc():
	htmlSuffix="/public_html/index.html"

	if "winxp-32" in ARGS.platform:
		#custom_html="/home/BIRCH"+htmlSuffix
		#cygwin_exec("browser "+custom_html)
	else:
		custom_html=ARGS.install_dir+htmlSuffix
		stream_exec("python -c \"import webbrowser; webbrowser.open(\\\""+custom_html+"\\\")\"")



def verify_install():
		valid=False
		
		if (os.path.lexists(ARGS.install_dir+"/local")):
			print_console("BIRCH/local found, install appears to be valid")
			valid=True
		
		return valid
	

