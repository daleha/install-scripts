"""
@modified: August 29 2011
@author: Dale Hamel
@contact: umhameld@cc.umanitoba.ca
The purpose of this class is to Provide support for the various actions that are repeated by several different python scripts
"""
import os
import sys

class HTMLWriter:
	"Methods for writing html to a file"
	  
	def __init__(self):
		"""
	 	  Initializes arguments:
	 		indentwidth=3
	 		col=0
	 		lpad=""
		  """
		self.indentwidth = 3
		self.col = 0 # current indentation column
		self.lpad = ""


	def indent(self):
		"""
		  **indent is not currently used by htmlwriter**
		  decrease indent using identwidth blank spaces
		  """
		  
		self.col = self.col + self.indentwidth
		self.lpad = ' '.rjust(self.col)
		
	def undent(self):
		"""
		  **undent is not currently used by htmlwriter**
		  decrease indent using identwidth blank spaces
		  """
		  
		self.col = self.col - self.indentwidth
		if self.col < 0:
			self.col = 0
			self.lpad = ""
		else:
			self.lpad = ' '.rjust(self.col)

	def start(self, htmlfile, tagname, attributes):
		"""
		  Write begin tag, with attributes
		  @param htmlfile: The name of the html file to write the tag for
		  @type htmlfile: str
		  @param tagname: The name of the tag
		  @type tagname: str
		  @param attributes: The tag information itself
		  @type attributes: str
		  """
		
		htmlfile.write('<' + tagname + attributes + '>\n')

	def end(self, htmlfile, tagname):
		"""
		  Write end tag
		  @param htmlfile: The of the html file to write tag for
		  @type htmlfile: str
		  @param tagname: The name of the tag to write
		  @type tagname: str
		  """
		  
		htmlfile.write('</' + tagname + '>\n')

	def page_title(self, htmlfile, title):
		"""
		  Write title
		  @param htmlfile: The name of the html file to add the title to
		  @type htmlfile: str
		  @param title: The title to write
		  @type title: str
		  """
		  
		htmlfile.write('<title>' + 'birch - ' + title + '</title>\n')
	  
	def link(self, htmlfile, url, attributes, text):
		"""
		  FIXME
		  @param htmlfile:
		  @type htmlfile:
		  @param url:
		  @type url:
		  @param attributes:
		  @type attributes:
		  @param text:
		  @type text:
		  """
		"Write hypertext link"
		htmlfile.write('<a href="' + url + '"' + attributes + '>' + text + '</a>')

	def start_page(self, htmlfile, title):
		"""
		  FIXME
		  @param htmlfile:
		  @type htmlfile:
		  @param title:
		  @type title:
		  """
		"Information at the top of each page is the same."
		htmlfile.write('<!-- this page generated automatically by htmldoc.py -->\n')
	  
		self.start(htmlfile, 'html', '')
		self.page_title(htmlfile, title)
		attributes = ' style ="background-color: rgb(255, 204, 0);"'
		self.start(htmlfile, 'body', attributes)
		# main heading, including birch logo which links to birch
		# home page.
		self.start(htmlfile, 'h1', '')
		url = '../../index.html'
		#text = '<img alt="birch - " src="../smallbirch.gif">'
		text = '<img alt="birch - " src="../../images/birch_white.png" height="68" width="100">'
		self.link(htmlfile, url, '', text)
		htmlfile.write(' ' + title)
		self.end(htmlfile, 'h1')
		htmlfile.write('<br>')

	def indent_text(self, htmlfile, text):
		"""
		  FIXME
		  @param htmlfile:
		  @type htmlfile:
		  @param text:
		  @type text:
		  """
		"indent a line of text"
		attributes = ' style="margin-left: 40px;"'
		self.start(htmlfile, 'div', attributes)
		htmlfile.write(text)
		self.end(htmlfile, 'div')
	  	  
	def end_page(self, htmlfile):
		"""
		  FIXME
		  @param htmlfile:
		  @type htmlfile:
		  """
		"html tags for end of page"
		self.end(htmlfile, 'body')
		self.end(htmlfile, 'html')
		
class Htmlutils:
	
	def __init__(self, CATDICT, PROGDICT):
		"""
	 		FIXME
	 		@param CATDICT:
	 		@type CATDICT:
	 		@param PROGDICT:
	 		@type PROGDICT:
	 		"""
		self.CATDICT = CATDICT
		self.PROGDICT = PROGDICT
	
	# - - - - - - - - - - - - - - - - - - - - - - - -
	def tokenize(self, line):
		"""
	 		FIXME
	 		@param line:
	 		@type line:
	 		"""
		"split up input line into tokens, where one or more spaces are seperators"
		"tokenize implicitly gets rid of the first token in the list"

		# parse the line into tokens
		tokens = line.split()

		# strip quotes that begin and end data values in .ace files
		i = 1
		while i < len(tokens):
			tokens[i] = tokens[i].strip('"')
			# get rid of \ escape characters added by acedb
			tokens[i] = tokens[i].replace('\\', '')
			i = i + 1
	 
		return tokens
		
	def cmp_to_key(self, mycmp):
		"""
			this function simplifies the transition to python 3 by eleiminating the need for the "cmp" function
		this was a recommended workaround for using "cmp"'s in sorts (recommended by: http://wiki.python.org/moin/HowTo/Sorting/)
			"""
		class K(object):
			def __init__(self, obj, * args):
				"""
			 			FIXME
			  			@param obj:
			  			@type obj:
			  			@param *args:
			  			@type *args:
			  			"""
				self.obj = obj
			def __lt__(self, other):
				"""
			  			FIXME
			  			@param other:
			  			@type other:
			  			"""
				return mycmp(self.obj, other.obj) < 0
			def __gt__(self, other):
				"""
			  			FIXME
			  			@param other:
			  			@type other:
			  			"""
				return mycmp(self.obj, other.obj) > 0
			def __hash__(self, other):
				"""
			  			FIXME
			  			@param other:
			  			@type other:
			  			"""
				return mycmp(self.obj, other.obj) == 0
			def __le__(self, other):
				"""
			  			FIXME
			  			@param other:
			  			@type other:
			  			"""
				return mycmp(self.obj, other.obj) <= 0
			def __ge__(self, other):
				"""
			  			FIXME
			  			@param other:
			  			@type other:
			  			"""
				return mycmp(self.obj, other.obj) >= 0
			def __ne__(self, other):
				"""
			  			FIXME
			  			@param other:
			  			@type other:
			  			"""
				return mycmp(self.obj, other.obj) != 0
		return K
				
	def name_to_url(self, name, doc_prefix):
		"""
		 	FIXME
		 	@param name:
		 	@type name:
		 	@param doc_prefix:
		 	@type doc_prefix:
		 	"""
		"Convert a Unix path to a URL"
		"If the path begins with an environment variable like $doc,"
		"assume that the name of the directory is the name of the variable"
		" ie. just delete the '$' and append the path to DOCPREFIX"
		"Otherwise, assume it is a URL of the form 'http:///'"
		
		if name[0] == '$':
			url = doc_prefix + '/' + name[1:]
			
		else:
			url = name
		return url

	def get_prefix(self, fn, p):
		"""
	 		FIXME
	 		@param fn:
	 		@type fn:
	 		@param p:
	 		@type p:
	 		"""
		"read $birch/install-birch/newstr.param, to get the prefixes"
		"needed for urls"
		file = open(fn, 'r')

		i = 1
		for line in file:
			
			if i == 3:
				p.DOCPREFIX = line.strip("\s")
				
			i = i + 1
		file.close()

		if (p.DOCPREFIX.find('http://') == 0) or (p.DOCPREFIX.find('file:///') == 0):
			okay = True
		else:
			okay = False

		return okay



