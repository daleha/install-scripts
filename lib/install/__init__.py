#system includes
import getpass
import os
from shutil import move

#lib includes
import commonlib
from commonlib import stream_exec 

#install includes
import nobirch
import birchhome

from log import *


def install(args):

	label('Starting the intall process')	
	info("Using: "+args.installDir+" as installation directory")
    

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
		
		run_nobirch(args)
        move_local(args)	
        makeProperties(args)
		run_birchhome(args.installDir+"/",args.installDir+"/")
		set_platform(args)	#simply calling existing setplatform
		makeParamFile(args) #see if this can be omitted, its a total hack
		run_customdoc(args)
		run_htmldoc(args)	
		run_newuser(args)
	
		
	verify_install(args)
		
		




def run_nobirch(args):

	label("Nobirch")
	info("Running nobirch")
	os.chdir(os.path.join(args.installDir,"admin"))
	
	nobirch.run_uninstall(directory=args.installDir,quiet=True)

def move_local(args):
	info("Moving local-generic to local")
	cwd= os.getcwd()
	
	os.chdir(args.installDir)
	move("local-generic","local")	

	os.chdir(cwd)




def run_birchhome(args):
	
	label("Birchhome")
	info("Running birchhome")
#	birchhome.main(install_dir,homepath)
	stream_exec(args.installDir+"/install-birch/birchhome.sh",args.installDir+"/install-birch")
	
def set_platform(args):
	label("Setplatform")
	info("Setting birch platform to "+args.platform)
	stream_exec(args.installDir+"/install-birch/setplatform.sh"+.platform,args.installDir+"/install-birch")
		
		
def run_customdoc(args):
	label("Customdoc")

	info("Running customdoc.py")
	
	
#	customdoc=exec_shell_prefix+" "+install_dir+"/script/customdoc.py "+install_dir+"/install-birch/oldstr.param "+install_dir+"/local/admin/newstr.param "+install_dir+"/install-birch/htmldir.param"
	
#	exec_func(customdoc)



def run_htmldoc(args):
    label("Htmldoc")
	info("Running htmldoc")
	
#	htmldoc=exec_shell_prefix+" "+install_dir+"/script/htmldoc.py "+install_dir+" "+ARGS.platform
	
#	exec_func(htmldoc)



def run_newuser(args):
	label("Newuser")
	info("Running newuser")
	
	stream_exec("./newuser",args.installDir+"/admin")


def makeParamFile(args):
	info("Making paramfile for customdoc")
	paramfile= open( os.path.join(args.installDir,"/local/admin/newstr.param"),"w")

	paramfile.write("~\n")
	paramfile.write("file://"+args.installDir+"/public_html\n")
	paramfile.write("file://"+args.installDir+"\n")
	paramfile.write(args.admin_email+"\n")
	paramfile.write(args.installDir+"\n")
	paramfile.write(getpass.getuser()+"\n")
	paramfile.flush()
	paramfile.close()


def makeProperties(args):
	print_console("Writing BIRCH.properties for legacy script support")
	props = open( os.path.join(args.installDir,"/local/admin/BIRCH.properties"),"w")
	props.write("BirchProps.homedir="+args.installDir+"\n")
	props.write("BirchProps.adminUserid="+getpass.getuser()+"\n")	
	props.write("BirchProps.birchURL=file://"+args.installDir+"/public_html\n")	
	props.write("BirchProps.adminEmail="+args.admin_email+"\n")	
	props.write("BirchProps.platform="+args.platform+"\n")	

#BirchProps.BirchMasterCopy
	props.write("BirchProps.birchHomeURL=file://"+args.installDir+"\n")	
	props.flush()
	props.close()
	
	if (not os.path.exists( os.path.join(args.installDir,"/local/admin/BIRCH.properties"))):
		error("Error, properties not written!")
	else:
		info("Birch properties file written")


def showCustomDoc(args):
	htmlSuffix="/public_html/index.html"

	if "winxp-32" in ARGS.platform:
		#custom_html="/home/BIRCH"+htmlSuffix
		#cygwin_exec("browser "+custom_html)
	else:
		custom_html= os.path.join(args.installDir,htmlSuffix)
        import webbrowser
        webbrowser.open(custom_html)
		#stream_exec("python -c \"import webbrowser; webbrowser.open(\\\""+custom_html+"\\\")\"")



def verify_install(args):
		valid=False
		
		if (os.path.lexists(args.installDir+"/local")):
			info("BIRCH/local found, install appears to be valid")
			valid=True
		
		return valid
	

