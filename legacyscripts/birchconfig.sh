#!/bin/sh
# birchconfig.sh Version 13 May 2007

# ----------------------- Header messages ----------------------------
echo '########################################################'
echo '                    BIRCHCONFIG' 
echo '########################################################' 
echo ""

echo '########################################################' > ../birchconfig.screen
echo '                    BIRCHCONFIG' >> ../birchconfig.screen
echo '########################################################' >> ../birchconfig.screen
echo "" >> ../birchconfig.screen
echo Today\'s date: `date` >> ../birchconfig.screen
echo "" >> ../birchconfig.screen
VERSION=`cat BIRCH.version`
if [ "$VERSION" = "D" ]
 then
   TIMESTAMP="(`cat BIRCH.version.timestamp`)"
 else
   TIMESTAMP=""
fi

MINI=`cat local-generic/admin/BIRCH.mini`
if [ "$MINI" = "true" ] 
   then
      echo miniBIRCH Version $VERSION $TIMESTAMP  >> ../birchconfig.screen
   else
      echo BIRCH Version $VERSION $TIMESTAMP  >> ../birchconfig.screen   
fi
echo '--------------------------------------------------------' >> ../birchconfig.screen
echo "" >> ../birchconfig.screen


# --------------- Check for required programs, shells, libraries etc.. --------------------
#                       /bin/csh
#                       java
#                       python

OKAY=1

# . . . . .  Check for csh by running a dummy csh script. If it fails,
# . . . . .  try to find out why.
echo Checking for csh
./testscripts/testcsh.csh
STATUS=$?
#echo "	STATUS $STATUS"

if [ $STATUS != 0 ]
   then
     OKAY=0
       # Check to see if csh is in the $PATH
     RESULT=`which csh`
     if [ ! -e $RESULT ] 
       then
         echo 	birchconfig.sh: csh is not in '$PATH'           
     fi
       # Check to see if csh is on the system
     if [ ! -e /bin/csh ] 
       then
         echo 	birchconfig.sh: /bin/csh not found          
     fi
   else
     echo '	csh OKAY'
fi
echo ""  

# . . . . .  Check for /usr/tmp
# This section was needed by an earlier version of GDE.
# GDE has now been fixed so that temp files go to /tmp,
# which, we can assume, is on all Unix/Linux systems.
#echo 'Checking for /usr/tmp (required by GDE)...'
#USRTMP=1
#if [ -d /usr/tmp ]
#   then 
#     
#       # Check to see if /usr/tmp is readable     
#     if [ ! -r /usr/tmp ] 
#       then
#         echo 	birchconfig.sh: /usr/tmp is not readable 
#	 USRTMP=0         
#     fi
#
#       # Check to see if /usr/tmp is writeable    
#     if [ ! -w /usr/tmp ] 
#       then
#         echo 	birchconfig.sh: /usr/tmp is not writeable 
#	 USRTMP=0         
#     fi
#
#       # Check to see if /usr/tmp is searchable    
#     if [ ! -x /usr/tmp ] 
#       then
#         echo 	birchconfig.sh: /usr/tmp is not searchable 
#	 USRTMP=0         
#     fi
#
#
#   else
#         echo 	birchconfig.sh: /usr/tmp not found
#	 USRTMP=0    
#fi
#
#if [ $USRTMP -eq 1 ] 
#  then
#      echo '	/usr/tmp OKAY' 
#  else 
#      OKAY=0
#fi
#echo ""  

# . . . . .  Check for python by running a dummy python script. If it fails,
# . . . . .  try to find out why.
echo Checking for python
python ./testscripts/testpython.py
STATUS=$?
#echo "	STATUS $STATUS"

if [ $STATUS != 0 ]
   then
     OKAY=0
       # Check to see if python is in the $PATH
     RESULT=`which python`
     if [ ! -e $RESULT ] 
       then
         echo 	birchconfig.sh: python is not in '$PATH'           
     fi
   else
     echo '	python OKAY'  
fi
echo ""

# . . . . .  Check for java by running java -version. If it fails,
# . . . . .  try to find out why.
echo Checking for java
# On Fedora and possible other Linux systems, Java defaults
# to gij, a GNU alternative
# java command, but I have never been able to get it to work.
# The Sun java from the JDK seems to work just fine, however.
# To get around the default java, you can set the variable
# $BIRCHJAVACMD to the name of the file with the original
# java command. This should go into $BIRCH/local/local.profile.source
# and local.cshrc.source so that birchconfig.sh will work in future updates of BIRCH.
if [ -z "$BIRCHJAVACMD" ] 
   then 
      BIRCHJAVACMD=`which java`
fi	
     
$BIRCHJAVACMD -version
STATUS=$?
#echo "	STATUS $STATUS"

if [ $STATUS != 0 ]
   then
     OKAY=0
       # Check to see if java is in the $PATH
     RESULT=`which java`
     if [ ! -e $RESULT ] 
       then
         echo 	birchconfig.sh: java is not in '$PATH'           
     fi  
   else
     COUNT=`$BIRCHJAVACMD -version | grep -c gij`
     if [ "$COUNT" -eq "0" ]
	then
	  echo ""
	  echo ""
	  echo '	Java OKAY'
	  echo ""
	else
	  OKAY=0
	  echo ""
	  echo    '>>> Your system uses GNU gij in place of Java.'  
	  echo    '>>> gij is still experimental and does not correctly run most'
	  echo    '>>> Java applications. Install Java from Sun Microsystems'
	  echo    '>>> before continuing. See'
	  echo    '>>> http://home.cc.umanitoba.ca/%7Epsgendb/birchadmin/inst.java.html'
     fi
fi


echo ""


# ----------------------- If okay, run birchconfig. Otherwise, exit ------------------------
if [ $OKAY -eq 1 ]
  then       
     $BIRCHJAVACMD  -jar birchconfig.jar | tee -a ../birchconfig.screen  
# It also works to hardcode the path as shown below, but most of the Java programs
# in BIRCH do not hard code the path to java, so you'd get things installed, but
# they still wouldn't find Java.
#     /usr/java/jdk1.5.0_06/bin/java -jar birchconfig.jar | tee -a ../birchconfig.screen
  else
     echo '>>>>>> birchconfig ERROR'
     echo One or more components required for BIRCH were not found on your
     echo system. See the Pre-installation steps web page for more detailed instructions. 
     exit
fi



