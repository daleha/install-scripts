import os
from log import *

	
def check_depends():
	def check_depend(depend):
		#if the dependency is found, then this will evaluate to true, else, false
		found= os.popen("which "+depend).read().strip() !=""
		
		info("Dependancy \""+depend+"\" found: "+str(found))
		if (not found):
			return False
		else:
			return True
	
	dependancies=["csh","java","python"]
	foundall=True
	
	for each in dependancies:
		foundall=check_depend(each)	
		if (not foundall):
			error("Dependancy " + each+ " not found on system path!")
			return False
		
	return foundall


