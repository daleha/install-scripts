#!/usr/bin/env python
"""
  July 18, 2008, Dr. Brian Fristensky, University of Manitoba
   
   htmldoc.py - Create web pages for BIRCH documentation from birchdb database

   Synopsis: htmldoc.py

@modified: May 26 2010
@author: Dale Hamel
@contact: umhameld@cc.umanitoba.ca
"""

import os
import re
import shutil
import sys

blib = os.environ.get("BIRCHLIB")
sys.path.append(blib)

from htmllib import Htmlutils
from htmllib import HTMLWriter

PROGRAM = "htmldoc.py: "
USAGE = "\n\t USAGE: htmldoc.py [current takes no paramters]"
PROGS = []
CATDICT = {}
PROGDICT = {}
PKGDICT = {}
DOCDICT = {}
HT = Htmlutils(CATDICT, PROGDICT)


# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
class Parameters:
	"Wrapper class for global parameters"

	def __init__(self, installDir, platform):
        self.BIRCHPATH = installDir
        self.BIRCH_PLATFORM = platform
        self.BIRCHWEBPATH = os.path.join( self.BIRCHPATH , 'public_html' )
		self.BIRCHDBPATH = os.path.join( self.BIRCHPATH , 'public_html/birchdb' )
		self.BIRCHLOCALDBPATH = os.path.join ( self.BIRCHPATH , 'local/public_html/birchdb' )
		self.BFN = "" # birchdb.ace
		self.LFN = "" # birchdb.local.ace
		self.INFILE = ""
		self.OFN = ""
		self.OUTFILE = ""
		# default prefix for http path to birch documentation
		self.DOCPREFIX = 'file://' + self.BIRCHWEBPATH + '/doc'




    def loadFromProps(self):
		"""
		  Get the location of the BIRCH home directory, BIRCHPATH 
		  Preferrably, we get it from the BIRCH.properties file,
		  but if that isn't found, we get it from the BIRCH
		  environment variable. The one catch is that since
		  we don't already know the location of BIRCHPATH,
		  the former will only work if the current working
		  directory is install-birch. Normally, this would
		  only be used when installing or updating BIRCH
		  Otherwise, the environment variable will be read.   
		  """	   
		FN = '../local/admin/BIRCH.properties'
		if  os.path.exists(FN):
			print "htmldoc.py: Reading $BIRCH from BIRCH.properties"
			self.BIRCHPATH = ""
			self.BIRCH_PLATFORM = ""
			FILE = open(FN, 'r')

			for LINE in FILE:
				TOKENS = LINE.split("=")
				if TOKENS[0] == 'BirchProps.homedir':
					self.BIRCHPATH = TOKENS[1].strip()
				if TOKENS[0] == 'BirchProps.platform':
					self.BIRCH_PLATFORM = TOKENS[1].strip()
			FILE.close()
		else:
			print "htmldoc.py: Reading $BIRCH and $BIRCH_PLATFORM environment variables."
			self.BIRCHPATH = os.environ['BIRCH']

		# If BIRCH_PLATFORM is set in the environment, we want that
		# to supersede the value in BIRCH.properties
		plat = ""
		try:
			plat = os.environ['BIRCH_PLATFORM']
		except:
			pass
		if plat != "":
			self.BIRCH_PLATFORM = plat
			 
		# Everything else depends on BIRCHPATH
		print "htmldoc.py: BIRCHPATH set to: " + self.BIRCHPATH
		print "htmldoc.py: BIRCH_PLATFORM set to: " + self.BIRCH_PLATFORM
		self.BIRCHWEBPATH = self.BIRCHPATH + '/public_html'
		self.BIRCHDBPATH = self.BIRCHPATH + '/public_html/birchdb'
		self.BIRCHLOCALDBPATH = self.BIRCHPATH + '/local/public_html/birchdb'
		self.BFN = "" # birchdb.ace
		self.LFN = "" # birchdb.local.ace
		self.INFILE = ""
		self.OFN = ""
		self.OUTFILE = ""
		# default prefix for http path to birch documentation
		self.DOCPREFIX = 'file://' + self.BIRCHWEBPATH + '/doc'

        # Read in the prefix to begin URLs that will be used as links
        # to documentation files.
        os.chdir( os.path.join(self.BIRCHPATH , '/install-birch' ))
        HT.get_prefix('newstr.param', self)



# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def DUMPTOACE(P):
	"""
	Dump the core BIRCH and local databases to .ace files
	@param P: Instasnce of Paramters object
	@type P: Parameters 
	"""
	
	os.chdir(P.BIRCHDBPATH)
	TACEDIR = P.BIRCHPATH + "/bin-" + P.BIRCH_PLATFORM
		
	# Call tace to dump the core birch data to .ace files:
	#	  category.ace, package.ace, program.ace, file.ace
	COMMAND = './tbirchdb.sh ' + P.BIRCHPATH + ' ' + P.BIRCHDBPATH + ' ' + TACEDIR + ' tace.input'
	os.system(COMMAND) 
		  
	# Call tace to dump the core birch data to a .ace files
	#	  category.ace, package.ace, program.ace, file.ace
	if os.path.exists(P.BIRCHLOCALDBPATH + '/database/ACEDB.wrm'):
		os.chdir(P.BIRCHLOCALDBPATH)
		COMMAND = './tbirchdb.sh ' + P.BIRCHPATH + ' ' + P.BIRCHLOCALDBPATH + ' ' + TACEDIR + ' tace.input'
		os.system(COMMAND)



# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def GetStr(LINE):
	"""
	Read the first string delimited by double quotes
	@param LINE: The line to be cleaned up
	@type LINE: str
	"""
	
	I = LINE.find('"')
	J = LINE.rfind('"')
	LINE = LINE[I + 1:J]
	# Get rid of \ escape characters added by ACEDB
	LINE = LINE.replace('\\', '')
	return LINE  

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
def AddToList(L, NAME):
	"""
	If a list contains NAME, do nothing
	If it does not exist, append it to the list
	@param L: The list to check for name
	@type L: list
	@param NAME: The name to be checked for
	@type NAME: str
	"""
	
	if NAME in L:
		pass
	else:
		L.append(NAME)
				
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



class Category:

	def __init__(self):
		"""
	 	  Initializes arguments:
	 		name=""
	 		program=[]
	 		pkg=[]
	 	  """
		self.name = ""
		self.program = []
		self.pkg = []



# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def ReadCategory(FN, CATS):
	"""
	Read category.ace into a list of category objects
	@param FN: THe name of the file to be read
	@type FN: str
	@param CATS: The categories object to be read into
	@type CATS: list
	"""
	

	# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	def AssignCategory(NAME):
		"""
		"If a category called NAME already exists, return a pointer"
	"If it does not exist, create a new category object and return a pointer"
		@param NAME: The name of the category to check for
		@type NAME: str
		"""
	
		if NAME in CATDICT:
			C = CATDICT[NAME]
		else:
			C = Category()
			CATS.append(C)
			C.name = NAME
		return C

	if os.path.exists(FN):
		FILE = open(FN, 'r')


		for LINE in FILE:
			if re.match('Category\s:\s".+"', LINE):
				C = AssignCategory(GetStr(LINE))
			elif re.match('Program\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(C.program, TOKENS[1])
			elif re.match('Package\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(C.pkg, TOKENS[1])


		FILE.close()


	   
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
class Package:

	def __init__(self):
		"""
	 	  Initializes arguments:
	 		name=""
	 		description=""
	 		category=[]
	 		doc=[]
	 		program=[]
	 		data=[]
	 		platform=[]
	 		installation=[]	 
	 	  """
		self.name = ""
		self.description = ""
		self.category = []
		self.doc = []
		self.program = []
		self.data = []
		self.platform = []
		self.installation = []
	  	  
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def ReadPackage(FN, PKGS):
	"""
	Read package.ace into a list of Package objects
	@param FN: The name of the file to be read in
	@type FN: str
	@param PKGS: The list of package objects to be read into
	@type PKGS:list
	"""
	

	# -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
	def AssignPackage(NAME):
		"""
		If a package called NAME already exists, return a pointer
	If it does not exist, create a new package object and return a pointer
		@param NAME:The name to attempt to assign
		@type NAME:str
		"""
	
		if NAME in PKGDICT:
			PK = PKGDICT[NAME]
		else:
			PK = Package()
			PKGS.append(PK)
			PK.name = NAME
		return PK

	if os.path.exists(FN):	
		FILE = open(FN, 'r')
		for LINE in FILE:
			if re.match('Package\s:\s".+"', LINE):
				PK = AssignPackage(GetStr(LINE))	  
			elif re.match('Description\s+".+"', LINE):
				PK.description = GetStr(LINE)
			elif re.match('Category\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(PK.category, TOKENS[1])
			elif re.match('Documentation\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(PK.doc, TOKENS[1])
			elif re.match('Program\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(PK.program, TOKENS[1])
			elif re.match('Data\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(PK.data, TOKENS[1])
			elif re.match('Platform\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				AddToList(PK.platform, TOKENS[1])
			elif re.match('BIRCH', LINE):
				AddToList(PK.installation, "BIRCH")
			elif re.match('local', LINE):
				AddToList(PK.installation, "local")


		FILE.close()   

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
class Program:

	def __init__(self):
		"""
		  Initializes arguments:
	 		name=""
	 		description=""
	 		category=[]
	 		interface=[]
	 		package=""
	 		doc=[]
	 		data=[]
	 		sampleinput=[]
	 		sampleoutput=[]
	 		platform=[]		 
		  """
		self.name = ""
		self.description = ""
		self.category = []
		self.interface = []
		self.package = ""
		self.doc = []
		self.data = []
		self.sampleinput = []
		self.sampleoutput = []
		self.platform = []

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def ReadProgram(FN, PROGS):
	"""
	Read program.ace into a list of Program objects
	@param FN: The name of the file to be read in
	@type FN: str
	@param PROGS: The list of Program objects to read the file into
	@type PROGS: list
	"""
	

	# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	def AssignProgram(NAME):
		"""
		If a program called NAME already exists, return a pointer
	If it does not exist, create a new program object and return a pointer
		@param NAME: The name to attempt to assign
		@type NAME: str
		"""
	
		if NAME in PROGDICT:
			PR = PROGDICT[NAME]
		else:
			PR = Program()
			PROGS.append(PR)
			PR.name = NAME
		return PR

	if os.path.exists(FN):	
			FILE = open(FN, 'r')
			for LINE in FILE:
				if re.match('Program\s:\s".+"', LINE):
					PR = AssignProgram(GetStr(LINE))
				elif re.match('Description\s+".+"', LINE):
					PR.description = GetStr(LINE)
				elif re.match('Category\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					AddToList(PR.category, TOKENS[1])

			# In ACeDB .ace files, constants the Interface field
			# does not actually contain the tag 'Interface'
			# It simply contains the values of the field.
				elif re.match('command\s+".+"', LINE):
					PR.interface.append(['command', GetStr(LINE)])
				elif re.match('interactive\s+".+"', LINE):
					PR.interface.append(['interactive', GetStr(LINE)])
				elif re.match('gui\s+".+"', LINE):
					PR.interface.append(['gui', GetStr(LINE)])
				elif re.match('birch\s+".+"', LINE):
		     			PR.interface.append(['birch', GetStr(LINE)])
	     			elif re.match('bldna\s+".+"', LINE):
		     			PR.interface.append(['bldna', GetStr(LINE)])
	     			elif re.match('blprotein\s+".+"', LINE):
		     			PR.interface.append(['blprotein', GetStr(LINE)])
	    			elif re.match('bldata\s+".+"', LINE):
		     			PR.interface.append(['bldata', GetStr(LINE)])
	     			elif re.match('blmarker\s+".+"', LINE):
		     			PR.interface.append(['blmarker', GetStr(LINE)])
	     			elif re.match('bltree\s+".+"', LINE):
		     			PR.interface.append(['bltree', GetStr(LINE)])
				elif re.match('Package\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					PR.package = TOKENS[1]
				elif re.match('Documentation\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					AddToList(PR.doc, TOKENS[1])
				elif re.match('Data\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					AddToList(PR.data, TOKENS[1])
				elif re.match('Sample_input\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					AddToList(PR.sampleinput, TOKENS[1])
				elif re.match('Sample_output\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					AddToList(PR.sampleoutput, TOKENS[1])
				elif re.match('Platform\s+".+"', LINE):
					TOKENS = HT.tokenize(LINE)
					AddToList(PR.platform, TOKENS[1])



			FILE.close()   		  		

def write_category_page(catfn):
		"""
	 		FIXME
	 		@param catfn:
	 		@type catfn:
	 		"""
		"write html page listing program categories."

# -   -   -   -   -   -   -   -   -   -   -
		def writecat(c):
			"""
			 FIXME
			 @param c:
			 @type c:
			 """
			"write a page for a given category"
			
			catfn = c.name + '.html'
			catpagefile = open(catfn, 'w')
			h = HTMLWriter ()
			h.start_page(catpagefile, 'category: ' + c.name)
			h.start(catpagefile, 'hr', '')
			h.start(catpagefile, 'h1', '')
			catpagefile.write('programs:')
			h.end(catpagefile, 'h1')
			h.start(catpagefile, 'ul', '')
			c.program.sort(key=HT.cmp_to_key(lambda x, y: cmp(x.lower(), y.lower())))
			for pname in c.program:
				h.start(catpagefile, 'li', '')
				url = '../program/' + pname + '.html'
				#print("Progdict length:"+str(len(PROGDICT)))
				text = pname + ' - ' + PROGDICT[pname].description + '\n'
				h.link(catpagefile, url, '', text)
				h.end(catpagefile, 'li')
			h.end(catpagefile, 'ul')
			h.end_page(catpagefile)
			catpagefile.close()
			os.chmod(catfn, 0644)


		catfile = open(catfn, 'w')
		h = HTMLWriter ()
		h.start_page(catfile, 'programs by category')
		h.start(catfile, 'hr', '')
		h.start(catfile, 'br', '')

	# for each category, write an html page containing
	# a list of programs in that category
		h.start(catfile, 'ul', '')
		catlist = CATDICT.keys()
		catlist.sort(key=HT.cmp_to_key(lambda x, y: cmp(x.lower(), y.lower())))
		for catkey in catlist:
			c = CATDICT[catkey]
			writecat(c)
			h.start(catfile, 'li', '')
			url = c.name + '.html'
			text = c.name + '\n'
			h.link(catfile, url, '', text)
			h.end(catfile, 'li')
		h.end(catfile, 'ul')
		h.end_page(catfile)
		catfile.close()
		os.chmod(catfn, 0644)

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
class File:

	def __init__(self):
		"""
		  Initializes arguments:
	 		name=""
	 		description=""
	 		command='\"$ACE_FILE_LAUNCHER\" '
	 		path=""
		  """
		self.name = ""
		self.description = ""
		self.command = '\"$ACE_FILE_LAUNCHER\" '
		self.path = ""

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def ReadDocFiles(FN, DOCFILES):
	"""
	Read file.ace into a list of file objects
	@param FN: The name of the file to be read in
	@type FN: str
	@param DOCFILES: the list of file objects to be read into
	@type DOCFILES: list
	"""
	

	# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	def AssignFile(NAME):
		"""
		If a file called NAME already exists, return a pointer
	If it does not exist, create a new file object and return a pointer
		@param NAME: The name to attempt to assign
		@type NAME: str 
		"""
	
		if NAME in DOCDICT:
			FL = DOCDICT[NAME]
		else:
			FL = File()
			DOCFILES.append(FL)
			FL.name = NAME
		return FL

	if os.path.exists(FN):
		FILE = open(FN, 'r')
		for LINE in FILE:
			if re.match('File\s:\s".+"', LINE):
				FL = AssignFile(GetStr(LINE))
			elif re.match('Description\s+".+"', LINE):
				FL.description = GetStr(LINE)
			elif re.match('Pick_me_to_call\s+".+"', LINE):
				TOKENS = HT.tokenize(LINE)
				FL.path = (TOKENS[2])


		FILE.close()
	
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



def FreshDir(DIRNAME):
	"""
	Delete an existing directory and create an empty directory
	with the same name.
	@param DIRNAME: The name of the directory to be refreshed
	@type DIRNAME: str
	"""
	
	if os.path.isdir(DIRNAME):
		shutil.rmtree(DIRNAME)
	os.mkdir(DIRNAME)
	os.chmod(DIRNAME, 0755)

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

def WritePackagePage(PKGFN, P):
	"""
	Write HTML page listing program packages.
	@param PKGFN: The name of the html page to be created
	@type PKGFN: str
	@param P: An instance of the Paramaters object
	@type P: Paramters object
	"""
	

	# -   -   -   -   -   -   -   -   -   -   -
	def WritePkg(PK):
		"""
		Write a page for a given package
		@param PK: The name of the package
		@type PK: str
		"""
	
		PKGFN = PK.name + '.html'
		PKGPAGEFILE = open(PKGFN, 'w')
		H = HTMLWriter ()
		H.start_page(PKGPAGEFILE, 'Package: ' + PK.name)

	# Program information is in a table.
#	ATTRIBUTES = ' style="border: 1px solid";'
		ATTRIBUTES = ' style="font-family: helvetica,arial,sans-serif;"'
		H.start(PKGPAGEFILE, 'table', ATTRIBUTES)
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'tr', ATTRIBUTES)

	# Title row
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(51, 255, 51);"'
		H.start(PKGPAGEFILE, 'td', ATTRIBUTES)
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'big', ATTRIBUTES)
		PKGPAGEFILE.write(PK.name + ' - ' + PK.description)
		H.end(PKGPAGEFILE, 'big')
		H.end(PKGPAGEFILE, 'td')
		H.end(PKGPAGEFILE, 'tr')


	# Documentation rows
		DocRows(PKGPAGEFILE, H, 'Documentation', PK.doc, DOCDICT, P)

	# Datafile rows
		DocRows(PKGPAGEFILE, H, 'Data', PK.data, DOCDICT, P)


	# Platform row
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
		H.start(PKGPAGEFILE, 'td', ATTRIBUTES)
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'big', ATTRIBUTES)
		URL = '../Doc_definitions.html#Platforms'
		TEXT = 	'Platforms'
		ATTRIBUTES = 'target="FrameWindow"'
		H.link(PKGPAGEFILE, URL, ATTRIBUTES, TEXT)
		PKGPAGEFILE.write(': ')
	
		H.end(PKGPAGEFILE, 'big')
		I = 0
		for PLAT in PK.platform:
			PKGPAGEFILE.write(PLAT)
			I = I + 1
			if I < len(PK.platform):
				PKGPAGEFILE.write(', ')
		H.end(PKGPAGEFILE, 'td')
		H.end(PKGPAGEFILE, 'tr')

				# Installation row
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
		H.start(PKGPAGEFILE, 'td', ATTRIBUTES)
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'big', ATTRIBUTES)
		URL = '../Doc_definitions.html#Installation'
		TEXT = 	'Installation'
		ATTRIBUTES = 'target="FrameWindow"'
		H.link(PKGPAGEFILE, URL, ATTRIBUTES, TEXT)
		PKGPAGEFILE.write(': ')
	
		H.end(PKGPAGEFILE, 'big')
		I = 0
		for INST in PK.installation:
			PKGPAGEFILE.write(INST)
			I = I + 1
			if I < len(PK.installation):
				PKGPAGEFILE.write(', ')
		H.end(PKGPAGEFILE, 'td')
		H.end(PKGPAGEFILE, 'tr')

		# Program rows
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
		H.start(PKGPAGEFILE, 'td', ATTRIBUTES)
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'big', ATTRIBUTES)
		URL = '../Doc_definitions.html#Programs'
		TEXT = 	'Programs'
		ATTRIBUTES = 'target="FrameWindow"'
		H.link(PKGPAGEFILE, URL, ATTRIBUTES, TEXT)
		PKGPAGEFILE.write(': ')
	
		H.end(PKGPAGEFILE, 'big')
		H.end(PKGPAGEFILE, 'td')
		H.end(PKGPAGEFILE, 'tr')
	
		ATTRIBUTES = ''
		H.start(PKGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(255, 255, 255);"'
		H.start(PKGPAGEFILE, 'td', ATTRIBUTES)
		H.start(PKGPAGEFILE, 'ul', '')
		PK.program.sort(key=HT.cmp_to_key(lambda x, y: cmp(x.lower(), y.lower())))
		for PNAME in PK.program:
			H.start(PKGPAGEFILE, 'li', '')
			URL = '../program/' + PNAME + '.html'
			TEXT = PNAME + ' - ' + PROGDICT[PNAME].description + '\n'
			H.link(PKGPAGEFILE, URL, '', TEXT)
			H.end(PKGPAGEFILE, 'li')
		H.end(PKGPAGEFILE, 'ul')
		H.end(PKGPAGEFILE, 'td')
		H.end(PKGPAGEFILE, 'tr')
		H.end(PKGPAGEFILE, 'table')

		H.end_page(PKGPAGEFILE)
		PKGPAGEFILE.close()
		os.chmod(PKGFN, 0644)


	PKGFILE = open(PKGFN, 'w')
	H = HTMLWriter ()
	H.start_page(PKGFILE, 'Programs by Package')
	H.start(PKGFILE, 'hr', '')
	H.start(PKGFILE, 'br', '')

	# For each package, write an HTML page containing
	# a list of programs in that package
	H.start(PKGFILE, 'ul', '')
	PKGLIST = PKGDICT.keys()

	PKGLIST.sort(key=HT.cmp_to_key(lambda x, y: cmp(x.lower(), y.lower())))
	for PKGKEY in PKGLIST:
		PK = PKGDICT[PKGKEY]
		WritePkg(PK)
		H.start(PKGFILE, 'li', '')
		URL = PK.name + '.html'
		TEXT = PK.name + ' - ' + PKGDICT[PK.name].description + '\n'		
		H.link(PKGFILE, URL, '', TEXT)
		H.end(PKGFILE, 'li')
	
	H.end(PKGFILE, 'ul')
	H.end_page(PKGFILE)
	PKGFILE.close()
	os.chmod(PKGFN, 0644)

	
# -   -   -   -   -   -   -   -   -   -   -
def DocRows(OUTFILE, H, HEADING, FILELIST, D, P):
	"""
	Write rows for documentation lines:
	Documentation, Data, Sample input, Sample output
	
	@param OUTFILE: The name of the output file
	@type OUTFILE: str
	@param H: An instance of HTMLWriter
	@type H: HTMLWriter
	@param HEADING: The heading to place in each row
	@type HEADING: str
	@param FILELIST: The list of files to be documented
	@type FILELIST: list
	@param D: The dictionary to use for documentation
	@type D: dictionary
	@param P: An instance of the Parameters class
	@type P: Parameters object
	"""
	
	
	# Documentation rows - title row is 1 column, other rows are 2 columns
	ATTRIBUTES = ''
	H.start(OUTFILE, 'tr', ATTRIBUTES)
	ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
	H.start(OUTFILE, 'td', ATTRIBUTES)
	ATTRIBUTES = ''
	H.start(OUTFILE, 'big', ATTRIBUTES)
	URL = '../Doc_definitions.html#' + HEADING.replace(' ', '_')
	TEXT = HEADING
	ATTRIBUTES = 'target="FrameWindow"'
	H.link(OUTFILE, URL, ATTRIBUTES, TEXT)
	H.end(OUTFILE, 'big')
	H.end(OUTFILE, 'td')
	H.end(OUTFILE, 'tr')
	

	for DOCNAME in FILELIST:
		ATTRIBUTES = ''
		if DOCNAME in D:
			DOCUMENT = D[DOCNAME]
			if len(DOCUMENT.path) > 0:
				H.start(OUTFILE, 'tr', ATTRIBUTES)
				ATTRIBUTES = ' border="0" colspan="1" rowspan="1" style= "background-color: rgb(255, 255, 255);"'
				H.start(OUTFILE, 'td', ATTRIBUTES)
				DOCTYPE = DOCUMENT.description
				H.indent_text(OUTFILE, DOCTYPE)
				H.end(OUTFILE, 'td')
				ATTRIBUTES = ' border="0" colspan="1" rowspan="1" style= "background-color: rgb(255, 255, 255);"'
				H.start(OUTFILE, 'td', ATTRIBUTES)
				URL = HT.name_to_url(DOCUMENT.path, P.DOCPREFIX)
				
				TEXT = DOCUMENT.path
				H.link(OUTFILE, URL, '', TEXT)
				H.end(OUTFILE, 'td')
				H.end(OUTFILE, 'tr')

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def WriteProgramPage(PROGFN, P):
	"""
	Write HTML page listing programs in alphabetical order.
	@param PROGFN: The basename of the page to be written
	@type PROGFN: str
	@param P: An instance of the Parameters class
	@type P: Paramters object
	"""
	

	# -   -   -   -   -   -   -   -   -   -   -
	def WriteProg(PR, P):
		"""
		Write a page for a given program
		@param PR: The name of the program to write the page for
		@type PR: str
		@param P: An instance of the Parameters class
		@type P: Parameters object
		"""
	
	
		PROGFN = PR.name + '.html'
		PROGPAGEFILE = open(PROGFN, 'w')
		H = HTMLWriter ()
		H.start_page(PROGPAGEFILE, PR.name)
	
		# Program information is in a table.
#		ATTRIBUTES = ' style="border: 1px solid";'
   		ATTRIBUTES = ' style="font-family: helvetica,arial,sans-serif;"'
	 	H.start(PROGPAGEFILE, 'table', ATTRIBUTES)
	  	ATTRIBUTES = ''
	   	H.start(PROGPAGEFILE, 'tr', ATTRIBUTES)

	 	# Title row
	  	ATTRIBUTES = ''
	   	H.start(PROGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(51, 255, 51);"'
		H.start(PROGPAGEFILE, 'td', ATTRIBUTES)
	 	ATTRIBUTES = ''
	  	H.start(PROGPAGEFILE, 'big', ATTRIBUTES)
	   	PROGPAGEFILE.write(PR.name + ' - ' + PR.description)
		H.end(PROGPAGEFILE, 'big')
	 	H.end(PROGPAGEFILE, 'td')
	  	H.end(PROGPAGEFILE, 'tr')

	 	# Command rows - title row is 1 column, other rows are 2 columns
	  	ATTRIBUTES = ''
	   	H.start(PROGPAGEFILE, 'tr', ATTRIBUTES)
		ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
	 	H.start(PROGPAGEFILE, 'td', ATTRIBUTES)
	  	ATTRIBUTES = ''
	   	H.start(PROGPAGEFILE, 'big', ATTRIBUTES)
		URL = '../Doc_definitions.html#Launching'
	 	TEXT = 	'Launching the program'
	  	ATTRIBUTES = 'target="FrameWindow"'
	   	H.link(PROGPAGEFILE, URL, ATTRIBUTES, TEXT)
	
	 	H.end(PROGPAGEFILE, 'big')
	  	H.end(PROGPAGEFILE, 'td')
	   	H.end(PROGPAGEFILE, 'tr')
		
			

	 	for COMMAND in PR.interface:
			ATTRIBUTES = ''
			#print("Trying to write: "+PROGPAGEFILE.name+" file is closed="+str(PROGPAGEFILE.closed))
		 	H.start(PROGPAGEFILE, 'tr', ATTRIBUTES)
		  	ATTRIBUTES = ' border="0" colspan="1" rowspan="1" style= "background-color: rgb(255, 255, 255);"'
		   	H.start(PROGPAGEFILE, 'td', ATTRIBUTES)
			H.indent_text(PROGPAGEFILE, COMMAND[0])
		 	H.end(PROGPAGEFILE, 'td')
		  	ATTRIBUTES = ' border="0" colspan="1" rowspan="1" style= "background-color: rgb(255, 255, 255);"'
		   	H.start(PROGPAGEFILE, 'td', ATTRIBUTES)
			ATTRIBUTES = ' face="Courier New,Courier"'
		 	H.start(PROGPAGEFILE, 'font', ATTRIBUTES)
		  	PROGPAGEFILE.write(COMMAND[1])
		   	H.end(PROGPAGEFILE, 'font')
			H.end(PROGPAGEFILE, 'td')
		 	H.end(PROGPAGEFILE, 'tr')


	   	# Documentation rows
		DocRows(PROGPAGEFILE, H, 'Documentation', PR.doc, DOCDICT, P)

	   	# Datafile rows
		DocRows(PROGPAGEFILE, H, 'Data', PR.data, DOCDICT, P)

	   	# Sample input rows
		DocRows(PROGPAGEFILE, H, 'Sample input', PR.sampleinput, DOCDICT, P)

   	   	# Sample output rows
	  	DocRows(PROGPAGEFILE, H, 'Sample output', PR.sampleoutput, DOCDICT, P)


   	   	# Package row
	   	ATTRIBUTES = ''
   		H.start(PROGPAGEFILE, 'tr', ATTRIBUTES)
	   	ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
		H.start(PROGPAGEFILE, 'td', ATTRIBUTES)
		ATTRIBUTES = ''
	  	H.start(PROGPAGEFILE, 'big', ATTRIBUTES)
		URL = '../Doc_definitions.html#Package'
		TEXT = 	'Package'
	  	ATTRIBUTES = 'target="FrameWindow"'
		H.link(PROGPAGEFILE, URL, ATTRIBUTES, TEXT)
		PROGPAGEFILE.write(': ')
	  	H.end(PROGPAGEFILE, 'big')
		URL = '../package/' + PR.package + '.html'
		TEXT = PR.package
	  	H.link(PROGPAGEFILE, URL, '', TEXT)
   		H.end(PROGPAGEFILE, 'td')
	   	H.end(PROGPAGEFILE, 'tr')

	   	# Platform row
		ATTRIBUTES = ''
		H.start(PROGPAGEFILE, 'tr', ATTRIBUTES)
	  	ATTRIBUTES = ' colspan="2" rowspan="1" style= "background-color: rgb(204, 204, 204);"'
		H.start(PROGPAGEFILE, 'td', ATTRIBUTES)
		ATTRIBUTES = ''
	  	H.start(PROGPAGEFILE, 'big', ATTRIBUTES)
		URL = '../Doc_definitions.html#Platforms'
		TEXT = 	'Platforms'
	  	ATTRIBUTES = 'target="FrameWindow"'
		H.link(PROGPAGEFILE, URL, ATTRIBUTES, TEXT)
		PROGPAGEFILE.write(': ')
	  	H.end(PROGPAGEFILE, 'big')
		I = 0
		for PLAT in PR.platform:
			PROGPAGEFILE.write(PLAT)
		  	I = I + 1
			if I < len(PR.platform):
			  	PROGPAGEFILE.write(', ')
		H.end(PROGPAGEFILE, 'td')
	  	H.end(PROGPAGEFILE, 'tr')
	
		H.end(PROGPAGEFILE, 'table')
	  	H.end_page(PROGPAGEFILE)
		PROGPAGEFILE.close()
		os.chmod(PROGFN, 0644)


	PROGFILE = open(PROGFN, 'w')
	h = HTMLWriter ()	
	h.start_page(PROGFILE, 'Program Index')
	h.start(PROGFILE, 'hr', '')
	h.start(PROGFILE, 'ul', '')

	# For each program, write an HTML page containing
	# all information for that program
	PROGLIST = PROGDICT.keys()
	PROGLIST.sort(key=HT.cmp_to_key(lambda x, y: cmp(x.lower(), y.lower())))
	for PROGKEY in PROGLIST:
		PR = PROGDICT[PROGKEY]
		WriteProg(PR, P)
		h.start(PROGFILE, 'li', '')
		URL = PR.name + '.html'
		TEXT = PR.name + ' - ' + PROGDICT[PR.name].description + '\n'		
		h.link(PROGFILE, URL, '', TEXT)
		h.end(PROGFILE, 'li')
	
	h.end(PROGFILE, 'ul')
	h.end_page(PROGFILE)
	PROGFILE.close()
	os.chmod(PROGFN, 0644)

				  		  		  
#======================== MAIN PROCEDURE ==========================

def main( params = None ):	

    if ( params == None ) :
        P = Parameters()
        P.loadFromProps():
	
	# Use tace to generate birchdb.ace and birchdb.local.ace, which contain
	# a complete dump of both the core birch documentation objects and those
	# from the local implementation.
	
	DUMPTOACE(P)
	
	# Read in birchdb.ace, and then birchdb.local.ace. If an object is present
	# in both files, the object from birchdb.local.ace replaces the object
	# from birchdb.ace. Thus, local versions of documentation can replace
	# documents from the birch core.
	
	# Read Categories
	CATS = []
	
	os.chdir(P.BIRCHDBPATH)
	ReadCategory('category.ace', CATS)
	for C in CATS:
		CATDICT[C.name] = C
	if os.path.exists(P.BIRCHLOCALDBPATH):
		os.chdir(P.BIRCHLOCALDBPATH)
		ReadCategory('category.ace', CATS)
		for C in CATS:
			CATDICT[C.name] = C
	
	# Read Packages
	PKGS = []
	
	os.chdir(P.BIRCHDBPATH)
	ReadPackage('package.ace', PKGS)
	for PK in PKGS:
		PKGDICT[PK.name] = PK
	if os.path.exists(P.BIRCHLOCALDBPATH):
		os.chdir(P.BIRCHLOCALDBPATH)
		ReadPackage('package.ace', PKGS)
		for PK in PKGS:
			PKGDICT[PK.name] = PK
	
	# Read programs
	
	os.chdir(P.BIRCHDBPATH)
	ReadProgram('program.ace', PROGS)
	#print("Progs:"+str(len(PROGS)))
	for PR in PROGS:
		PROGDICT[PR.name] = PR
		#print("Read in program:"+str(PR))
	if os.path.exists(P.BIRCHLOCALDBPATH):
		os.chdir(P.BIRCHLOCALDBPATH)
		ReadProgram('program.ace', PROGS)
		for PR in PROGS:
			PROGDICT[PR.name] = PR
	
	# Read documentation file names
	DOCFILES = []
	
	os.chdir(P.BIRCHDBPATH)
	ReadDocFiles('file.ace', DOCFILES)
	for FL in DOCFILES:
		DOCDICT[FL.name] = FL
	if os.path.exists(P.BIRCHLOCALDBPATH):
		os.chdir(P.BIRCHLOCALDBPATH)
		ReadDocFiles('file.ace', DOCFILES)
		for FL in DOCFILES:
			DOCDICT[FL.name] = FL
	
	
    # Remove existing directories for category, program and package,
	# and create new empty directories
	# This ensures that old files that are no longer needed (such
	# as files describing programs or packages that have been removed)
	# do not clutter up the directories.
	os.chdir(P.BIRCHPATH + '/public_html/birchdoc')
	FreshDir('category')
	FreshDir('package')
	FreshDir('program')
	
	if OKAY:
	
		# - - - - - - - - - -  Output to the public_html/birchdb directory - - - - - -
	
		# For each category write a category page in HTML
	
		os.chdir('category')
		CATFN = 'category.html'
		write_category_page(CATFN)
	   
		# For each all programs write an HTML page with an alphabetical list of programs
		os.chdir('../program')
		PROGFN = 'program.html'
		WriteProgramPage(PROGFN, P)
	
		# For each package, write a package page in HTML
			# For each category write a category page in HTML
		os.chdir('../package')
		PKGFN = 'package.html'
		WritePackagePage(PKGFN, P)
	

if __name__=="__main__":
    main()
