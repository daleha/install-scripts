#!/bin/csh

# Find out the following platform parameters:
#   operating system
#   processor family

# Operating system
set OS = `uname -s | tr -s '[:upper:]' '[:lower:]'`
switch ($OS)
   case sunos:   
    set OS = solaris
     breaksw
  case linux:
  
   set OS = linux
    breaksw
 default
   set OS = linux  
   breaksw 
endsw

# Processor family
set PROC = `uname -p | tr -s '[:upper:]' '[:lower:]'`
switch ($PROC)
   case sparc:
    set PROC = sparc
     breaksw
  case i686:
   set PROC = intel
    breaksw
 default
   set PROC = intel 
   breaksw 
endsw


# Map the result to supported BIRCH platforms
if ($OS == solaris) then
   switch ($PROC)
      case sparc:
       set PLATFORM = 'solaris-sparc'
	breaksw
     case intel:
      set PLATFORM = 'solaris-amd64'
       breaksw
    default
      set PLATFORM = 'solaris-sparc' 
      breaksw 
   endsw
else
   if ($OS == linux) then
      switch ($PROC)
	case intel:
	 set PLATFORM = 'linux-intel'
	  breaksw
       default
	 set PLATFORM = 'linux-intel' 
	 breaksw 
      endsw
  endif
else
   set PLATFORM = 'linux-intel'  
endif

echo $PLATFORM


