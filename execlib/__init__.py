
import os
import sys

from subprocess import Popen
from subprocess import PIPE
from threading  import Thread

from log import *


try:
	from Queue import Queue, Empty
except ImportError:
	from queue import Queue, Empty  # python 3.x



ON_POSIX = 'posix' in sys.builtin_module_names

   


"""
stream_exec executes the command "command" using subprocess.Popen, setting
the cwd for execution to "path". If "callback" is set to a function pointer, 
then on each line of output a callback function is executed. "verbose" is a flag 
that determines if the output of the command should be sent to the console.
By default, output is sent to console.

Stream exec uses a message queue to prevent blocking and deadlock when doing 
cross-platform execution calls. This is particularly an issue when doing
filesystem calls on Windows, which is why this method was created.
"""
def stream_exec(command,path=None,verbose=True,callback=None):


	def enqueue_output(out, queue):
		for line in iter(out.readline, ''):
			queue.put(line)
		out.close()

	def read_output():
		# read line without blocking
		try:  line = q.get_nowait() # or q.get(timeout=.1)
		except Empty:
			return None
		else: # got line
			return line	

	cwd=os.getcwd()
	if (path!= None and os.path.isdir(path)):
		debug("Changing directory to " + path)
		os.chdir(path)


	proc = Popen(command,shell=True, stdout=PIPE, bufsize=-1, close_fds=ON_POSIX)
	debug("Dispatched command \""+command+"\"")
	q = Queue()
	t = Thread(target=enqueue_output, args=(proc.stdout, q))
	t.daemon = True # thread dies with the program
	t.start()
	
	output = ""
	while (proc.poll()==None):
		line=read_output()
		if (line!=None):
			if (verbose):
				print_console(line)
			if (callback!=None):
				callback()
			output=output+line
	os.chdir(cwd)
	return output
	
def cleanup():
	def cygwin_rm(path):
		cygwin_exec("rm "+path)

	def remove_tarball(prefix,rm_func):
		def try_remove(rm_func,path):
			if (os.path.lexists(path)):
				try:
					rm_func(path)
				except:
					error("Could not remove "+path)

		try_remove(rm_func,ARGS.install_dir+"/"+prefix+".tar.gz")
		try_remove(rm_func,ARGS.install_dir+"/"+prefix+".tar")
	

	if "winxp-32" in ARGS.platform:
		rm_func=cygwin_rm
	else:
		rm_func=os.remove
	
	remove_tarball("framework",rm_func)
	remove_tarball("cyghead",rm_func)
	
	if (not ARGS.multi_bins):
		remove_tarball(ARGS.platform,rm_func)
	else:
		for each in ARGS.platforms:
			remove_tarball(each,rm_func)
	
	if (ARGS.install_log!=None):
		ARGS.install_log.flush()
		ARGS.install_log.close()




