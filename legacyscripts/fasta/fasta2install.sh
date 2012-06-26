#!/bin/sh 
# fasta2install.sh  10/22/08


#------------- Set environment variables
#Check to see if $MAILID is set
if [ -z "$MAILID" ]
  then
  MAILID=`grep BirchProps.adminEmail ../../local/admin/BIRCH.properties |cut -f2 -d"="`
  if [ -z "$MAILID" ]
  	then
     MAILID=`whoami`'@'`hostname`
  fi
fi


# Make sure we can overwrite existing files
unset noclobber

# On some systems, cp is aliased to cp -i to force interactive mode.
# CP_CMD circumvents that alias
if [ -f /usr/bin/cp ]
  then
    CP_CMD=/usr/bin/cp
  else
    if [ -f /bin/cp ]
      then
        CP_CMD=/bin/cp
    else
        CP_CMD=cp
    fi
fi


if [ -z "$BIRCH" ]
	then
   BIRCH=`grep BirchProps.homedir ../../local/admin/BIRCH.properties |cut -f2 -d"="`
fi

if [ -z "$BIRCH_PLATFORM" ]
	then
   BIRCH_PLATFORM=`grep BirchProps.platform ../../local/admin/BIRCH.properties |cut -f2 -d"="`
   case $BIRCH_PLATFORM in
     "solaris-sparc")
       BIRCH_BIN=bin-solaris-sparc
       export BIRCH_BIN
       RM_CMD=/usr/bin/rm
       export RM_CMD
       ;;  
     "solaris-amd64")
       BIRCH_BIN=bin-solaris-amd64
       export BIRCH_BIN
       RM_CMD=/usr/bin/rm
       export RM_CMD
       ;;  
     "linux-intel")
       BIRCH_BIN=bin-linux-intel
       export BIRCH_BIN
       RM_CMD=/bin/rm
       export RM_CMD
       ;;
     "linux-x86_64")
       BIRCH_BIN=bin-linux-x86_64
       export BIRCH_BIN
       RM_CMD=/bin/rm
       export RM_CMD
       ;;
     "osx-x86_64")
       BIRCH_BIN=bin-osx-x86_64
       export BIRCH_BIN
       RM_CMD=/bin/rm
       export RM_CMD
       ;;
     "AIX")
       BIRCH_BIN=bin-AIX
       export BIRCH_BIN
       ;;
     "HP")
       BIRCH_BIN=bin-HP
       export BIRCH_BIN
       ;;
     *)
       BIRCH_BIN=bin-solaris-sparc
       export BIRCH_BIN
       RM_CMD=/usr/bin/rm
       export RM_CMD
       ;;
   esac
fi



   
HOST=ftp.ebi.ac.uk
DIR=pub/software/unix/fasta/fasta2 
USERID=anonymous
PASSWD=$MAILID
COMPFILE=fasta2.shar #symbolic link pointing to latest version

if [ -d fasta2 ]
	then
   $RM_CMD -rf fasta2
   fi
   
mkdir fasta2
cd fasta2

# ------------ Download archive file by FTP
# 
  # generate FTP command file
  # We need to run in the passive mode, which is required by
  # some firewalls.
  # There is a lot of inconsistency from system to system as far
  # as how ftp is run in the passive mode. One or more of these
  # works on each system, but none works on all systems:
  # ftp -p
  # ftp, input 'passive' from ftp.input
  # pftp
  # 
  # set FTP command
  RESULT=`which pftp | wc -w`
   if [ "$RESULT" = "1" ]
   	then
      FTPCOMMAND='pftp'
   else
      FTPCOMMAND='ftp -p'
   fi
   echo user $USERID $PASSWD > ftp.input
   echo cd $DIR >>  ftp.input
   echo bin >> ftp.input
   echo get $COMPFILE.Z  >>  ftp.input
   echo bye >> ftp.input
  # run FTP
  nice $FTPCOMMAND -i -n $HOST < ftp.input
  
echo FTP completed

#uncompress $COMPFILE
gunzip $COMPFILE.Z
SHARFILE=$COMPFILE
sh $SHARFILE
chmod a+r *


# -------------  Binary files
BINFILES="align align0 garnier grease"

# Default is HZ=100. See Makefile
if [  "$BIRCH_PLATFORM" = solaris-sparc -o "$BIRCH_PLATFORM" = solaris-amd64  ]
   then 

   # Not sure what this does, but we need it on solaris-sparc
   mv Makefile Makefile.bak
   sed -e "s/^HZ=100/HZ=60/" <Makefile.bak > Makefile


   #Check to see which compilers are available.
   COUNT=`which cc | egrep -c -e '^/.*/cc$'`
   if [ "$COUNT" = 1 ]
   then
      CC=1
   else
      CC=0
   fi 
      
   COUNT=`which gcc | egrep -c -e '^/.*/gcc$'`
   if [ "$COUNT" = 1 ]
   	then
      GCC=1
   else
      GCC=0
   fi    


   # Bill Pearson's Makefile.sun defaults to use the Sun cc compiler, which
   # is often not available on many Solaris systems. Even worse, there are
   # some systems in which 'which cc' reports a file, but trying to run
   # cc gives a message saying
   #
   # /usr/ucb/cc:  language optional software package not installed
   #
   # The following code
   # changes the Makefile to use gcc instead of cc. 

   if [ "$GCC" = "1" ]
   	then
       # Replace the sun cc command with the gcc command
       sed -e 's/^CC=.*/CC= gcc -g -O3/'  < Makefile > Makefile.fixed
       mv Makefile Makefile.original
       mv Makefile.fixed Makefile
   else
      if [ "$CC" = "0" ]
      	then
	echo "ERROR: no C compiler available. Can't compile fasta2 package."
	exit 
      fi        
   fi           
 
      
fi


# We do it this way, rather than just 'make' because as of 
# fasta21u1d0.shar.Z (10/28/02) make would terminate in an error
# if you try to make all targets on Red Hat Linux 7.2. This
# did work in Solaris. Anyway, this approach doesn't waste time
# compiling programs that won't be used in BIRCH
nice make $BINFILES

chmod a+rx $BINFILES

$CP_CMD -p $BINFILES $BIRCH/$BIRCH_BIN

# -------------- Manual pages
chmod 644 *.1
$CP_CMD -p *.1 $BIRCH/manl

# -------------- Documentation files
chmod 644 *.doc *.me
$CP_CMD -p COPYRIGHT *.doc *.me $doc/fasta

# -------------- Scoring Matrices, Sample sequences
chmod 644 *.mat *.aa *.seq
$CP_CMD -p *.mat *.aa *.seq $dat/fasta2




