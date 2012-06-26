#!/bin/sh -f

# Delete BIRCH directories, remove BIRCH access for BIRCH Administrator
#
# Synopsis: UNINSTALL-birch.sh [-Qn]
#
#         -Q Quiet option. Do not prompt user to continue.
#         -n do NOT delete the binaries and libraries directories
#

if [ -f "../local/admin/BIRCH.properties" ]
   then
     BIRCH=`grep BirchProps.homedir ../local/admin/BIRCH.properties |cut -f2 -d"="`
   else # Deprecated
     BIRCH=`cat ../local/admin/birchdir.param`
fi

# Make sure that this script doesn't run in BIRCHDEV, which 
# would clobber the master copy of BIRCH.

RESULT=`echo $BIRCH |grep -c BIRCHDEV`
if [ ${RESULT} -eq "1"  ] 
   then
   echo '>>> UNINSTALL-birch.sh cannot be run in BIRCHDEV'
   echo '>>> Doing so would clobber the master copy of BIRCH'
   exit 1
else
   #####################################################################
   # Read options from the command line and set parameters,
   # or prompt user for parameters at terminal.
   ####################################################################
   QUIET=""
   DELETEBINARIES="y"
   numargs=$#
   if [ "$numargs" != "0" ]
     then
     #---------------------------------------------------
     #parameters given in command line
     index=1
     while [ $index -le $numargs ]
	do
          a=$1
          case $a in
	     "-Q")
	          QUIET="Q"
	          ;;
	     "-n")
	          DELETEBINARIES="n"
	     ;;
	     esac
          index=`expr $index + 1`
          shift
          done #while

     fi


     # RM_CMD - command to be used for removing files and directories
    if [ -f /usr/bin/rm ]
       then
       RM_CMD=/usr/bin/rm
    else
       if [ -f /bin/rm ]
	  then
	  RM_CMD=/bin/rm
       else
	  RM_CMD=rm
       fi
    fi

    if [ "$QUIET" = "Q" ]
       then
       CHOICE='y'
    else
       echo '>>> You are about to remove the BIRCH system\!'
       echo '>>> Type y to continue, n to exit'
       read CHOICE
    fi
   
    if [ $CHOICE = 'y' ]
	then

       cd ..
       
       #. . . . . . Turn off BIRCH access for BIRCH Administrator
       admin/nobirch -Q
       
    
       #. . . . . .  delete BIRCH directories

       for directory in acedb acedb4.9l admin dat doc java labace local-generic \
             manl ncbi pkg public_html script
          do
	  echo Removing $directory
	  $RM_CMD -rf $directory
	  done
       $RM_CMD tutorials birchconfig.homedir.target.html
       echo Other BIRCH files and directories must be removed manually. 

       #. . . . . .  delete BIRCH binaries and libraries
       if [ "$QUIET" = "Q" ]
	then
	  CHOICE=$DELETEBINARIES	  	      
       else
	  echo '>>> To delete the existing binaries and libraries, type y'
	  echo '>>> If you are installing the BIRCH framework, but wish to'
	  echo '>>> keep the existing binaries, type n'
	  read CHOICE
       fi      

       if [ "$CHOICE" = 'y' ]
	then
	  for directory in bin-solaris-sparc bin-solaris-amd64 bin-linux-x86_64 bin-osx-x86_64 \
               bin-linux-intel lib-solaris-sparc lib-solaris-amd64 lib-linux-intel lib-linux-x86_64 lib-osx-x86_64 \
	       bin-winxp-32 bin-win7-64 lib-winxp32 lib-win7-64
            do
	     echo Removing $directory
	     $RM_CMD -rf $directory
	     done
       else
	  echo '>>> Binaries and libraries will not be deleted.'
       fi

       #. . . . . .  delete install-birch     
       $RM_CMD -rf install-birch
     
   else
     echo '>>> exiting program. No files deleted.'
   fi 
fi

