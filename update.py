
#system includes
import os 
import tarfile
import datetime
import traceback
import shutil

#lib includes
import commonlib
from commonlib import print_label
from commonlib import stream_exec

#var includes
from Globals import ARGS
from Globals import print_console

#core includes
from install import main_install
from install import run_nobirch

#jython includes
import javax.swing.JOptionPane as JOptionPane
import javax.swing.JFileChooser as JFileChooser

birch_files=['local-generic', 'labace', 'script', 'install-birch', 'bin-linux-intel','bin-linux-x86_64','bin-solaris-sparc','bin-solaris-amd64','bin-osx-x86_64', 'dat', 'admin.uninstall', 'doc', 'pkg', 'java', 'public_html', 'manl', 'birchconfig.homedir.target.html', 'tutorials', 'lib-linux-intel','lib-linux-x86_64','lib-solaris-sparc','lib-solaris-amd64','lib-osx-x86_64', 'admin']


def compress_old_birch():
	
	print_label("Archiving old BIRCH")	
	filename="birch_backup"+str(datetime.datetime.now()).replace(" ","")+".tar"
	filename=filename.replace(":","")
	filename=filename.replace("-","")
	print_console("Saving old BIRCH in "+filename)
	tar = tarfile.open(filename, "w")
	
	os.chdir(ARGS.install_dir)
	
	for each in birch_files:
		if (os.path.lexists(ARGS.install_dir+"/"+each)):
			print_console("Archiving old birch directory "+ each)
			
			
			try:
				tar.add(each)
			except IOError:
				print_console("Could not add file: \""+each+"\" to archive:")
				ioerr=traceback.format_exc()
				print_console(str(ioerr))
				message="An IO error occurred, the file \""+each+"\"could not be added to the archive, do you still wish to proceed?"
				cont=JOptionPane.showConfirmDialog(None,message, "Input",JOptionPane.YES_NO_OPTION);
				
				if (cont==JOptionPane.NO_OPTION):
					print_console("User aborted installation")
					JOptionPane.showMessageDialog("It is recommended that you archive manually archive your previous BIRCH installation before you proceed with the update.")
					commonlib.shutdown()
				
			except:
				err=traceback.format_exc()
				print_console(err)
				print_console("Unhandled exception occurred, this is a bug. For safety reasons, the update has been aborted")
				JOptionPane.showMessageDialog(None,"Unhandled exception occurred, this is a bug. For safety reasons, the update has been aborted")
				commonlib.shutdown()
			
	tar.close()
			
def purge_old_birch():
	
	print_label("Purging old BIRCH")	
	os.chdir(ARGS.install_dir)
	contents=os.listdir(os.getcwd())
	
	
	if  "install-birch" in contents:
		for each in birch_files:
			
			if (each!="local" and each.count("birch_backup")!=1 and os.path.lexists(ARGS.install_dir+"/"+each)):
				print_console("Removing old BIRCH component: "+each)
				filename=ARGS.install_dir+"/"+each
				
				
				if (os.path.isdir(filename)):
					try:
						shutil.rmtree(filename)
						if (os.path.isdir(filename)):
							os.remove(filename)
					except:
						err=traceback.format_exc()
						print_console(err)
						JOptionPane.showMessageDialog(None, "An error occurred deleting file: "+filename)
	else:
		pass
						
		
def check_directory(path):

	if (os.path.lexists(path)):
		contents=os.listdir(path)
		
		if (contents.count("install-birch")==0):
			print_console("The selected directory "+path+" does NOT contain a BIRCH installation!")
			message="The selected path does NOT contain a BIRCH installation.\nPlease select the base directory of the installation that you wish to update,\nOr click \"no\" to cancel update."
			reload = JOptionPane.showConfirmDialog(None,message, "Input",JOptionPane.YES_NO_OPTION);
		
			if (reload==JOptionPane.NO_OPTION):
				print_console("User aborted install.")
				commonlib.shutdown()
			
			fc = JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(None);
			path = fc.getSelectedFile().getPath();
			
			check_directory(path)
			
		else:
			ARGS.install_dir=path
			print_console("The selected directory "+path+" contains a BIRCH installation, it will be updated")



def update_birch():
		

		
		print_label("Updating BIRCH")	
		
		check_directory(ARGS.install_dir)	
		
		
		os.chdir(ARGS.install_dir)
		
		if (ARGS.make_backup):
			compress_old_birch()

		run_nobirch()
		purge_old_birch()				
		main_install()
		
		
		print_console("Update complete")
		
		
		
