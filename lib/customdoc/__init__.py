#!/usr/local/bin/python
# December  7, 2004, Dr. Brian Fristensky, University of Manitoba

# Description: Customize local copy of BIRCH documentation by 
# converting URLs and other strings in HTML files to correspond
# to local files and directory structures.

# Synopsis: customdoc.py oldstrings newstrings htmldirs

# Files: oldstrings      old strings to be replaced
#        newstrings      new strings to replace old strings
#        htmldirs        directories in which to change HTML files

import sys
import os
import string
import re

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# Read in old and new strings, striping 
# leading and trailing whitespace, including
# newline characters.
def GETLIST(FN) :
    LST = []
    FILE = open(FN,'r')
    LINE = FILE.readline()
    while LINE != '':
          LST.append(string.strip(LINE))
	  LINE = FILE.readline()
    FILE.close()
    return LST

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def CHANGEFILE(HTMLFN) :
    # Run through INFILE, changing old URLs to new URLs
    # Note that these changes are consecutive. However,
    # lines containing PROTECT are not changed.
    CHANGED = 0

    HTMLFILE = open(HTMLFN,'r')
    TEMPFILE = open(TEMPFN,'w')
    DELETE = 0
    REPLACE = 0
    LINE = HTMLFILE.readline()
    while LINE != '':
          if string.find(LINE,BEGIN_DELETE) >= 0 :
	     DELETE = 1
	     CHANGED = 1
	  elif string.find(LINE,END_DELETE) >= 0 :
	     DELETE = 0
	  elif re.match(BEGIN_REPLACE,LINE) >= 0 :
	     TEMPFILE.write(LINE)	  
	     REPLACE = 1
	     RFN = re.split('"',LINE)
	     REPLACEFN = "local/public_html/" + RFN[1]
	     if os.path.exists(REPLACEFN) :
		REPLACEFILE = open(REPLACEFN,'r')
        	REPLINE = REPLACEFILE.readline()
        	while REPLINE != '':
        	   TEMPFILE.write(REPLINE)
		   REPLINE = REPLACEFILE.readline()
		   print REPLINE
        	REPLACEFILE.close()	     	     
	     CHANGED = 1
	  elif string.find(LINE,END_REPLACE) >= 0 :
	     REPLACE = 0
	     TEMPFILE.write("\n")
	     TEMPFILE.write(LINE)	     
          else :
	     if REPLACE == 1 :
	        pass 	  
	     elif DELETE == 0 :
        	#if line contains PROTECT
		#   do nothing
		if string.find(LINE,PROTECT) == -1 :
		    # Otherwise, replace each string in OLDLIST with
		    # its counterpart in NEWLIST. 
		    I = 0;
		    for STR in OLDLIST :
	        	if string.find(LINE, OLDLIST[I]) != -1 :
	        	   LINE = string.replace(LINE, OLDLIST[I], NEWLIST[I])
			   CHANGED = 1
			   print LINE
			I = I + 1	          	     
        	TEMPFILE.write(LINE)
	     
	  LINE = HTMLFILE.readline()
    HTMLFILE.close()	  
    TEMPFILE.close()

    # If file has changed, overwrite original file.
    # Delete temporary file.
    if CHANGED == 0 :
       os.remove(TEMPFN)
    else :
       os.remove(HTMLFN)
       os.rename(TEMPFN,HTMLFN)
       os.chmod(HTMLFN,0644)
    return
      
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
def TRAVERSE(P) :
    os.chdir(P)
    print (P)
    
    # Make lists of all files and directories in the 
    # current directory.
    ALLFILES = os.listdir(os.curdir)
    HTMLFILES = []
    DIRECTORIES = []
    for NAME in ALLFILES :
	if os.path.isdir(NAME) and not os.path.islink(NAME):
	   DIRECTORIES.append(NAME)
	elif NAME[-5:] == '.html' :
	   HTMLFILES.append(NAME)
	   
    # list HTML files
    for FILE in HTMLFILES :	   
        CHANGEFILE(FILE)

    # Visit all directories recursively
    for D in DIRECTORIES :
        TRAVERSE(D)
	
    # Don't forget to return to the parent directory.	
    os.chdir(os.pardir)
    return


#======================== MAIN PROCEDURE ==========================

#---------- Set global variables
OLDSTRFN = sys.argv[1]
NEWSTRFN = sys.argv[2]
DIRFN = sys.argv[3]

TEMPFN = str(os.getpid()) + '.TEMP'
PROTECT = "<!-- DON'T CHANGE -->"
BEGIN_DELETE = "<!-- BEGIN DELETE -->"
END_DELETE = "<!-- END DELETE -->"
#BEGIN_REPLACE = '<!-- BEGIN REPLACE name =".*" -->'
BEGIN_REPLACE = '<!-- BEGIN REPLACE name="'

END_REPLACE = "<!-- END REPLACE -->"

# Read in list of strings to change
OLDLIST = GETLIST(OLDSTRFN)
NEWLIST = GETLIST(NEWSTRFN)
DIRLIST = GETLIST(DIRFN)

OLDLEN = len(OLDLIST)
NEWLEN = len(NEWLIST)
DIRLEN = len(DIRLIST)

if OLDLEN == 0 :
   print  OLDSTRFN + " has 0 elements. Doing nothing."
elif NEWLEN == 0 :
   print  NEWSTRFN + " has 0 elements. Doing nothing."
elif OLDLEN != NEWLEN :
   print OLDSTRFN + ' and ' + NEWSTRFN + ' must have the same number of elements' 
   print "Doing nothing."
elif DIRLEN == 0:
   print  DIRFN + " has 0 elements. Doing nothing."
else: 
   # Traverse the directory tree recursively, changing all HTML
   # files to use new strings.
   for PNAME in DIRLIST : 
       TRAVERSE(PNAME)

# debugging
print "OLDLIST:"
print OLDLIST
print "NEWLIST:"
print NEWLIST
