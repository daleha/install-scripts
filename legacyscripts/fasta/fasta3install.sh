#!/bin/sh 
# fasta3install.sh  10/23/08


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


# On some systems, mv is aliased to mv -i. We need to get around this
# so that the script won't try to prompt the user when overwriting a file.
if [ -f /usr/bin/mv ]
  then
    MV_CMD=/usr/bin/mv
  else
    if [ -f /bin/mv ]
      then
        MV_CMD=/bin/mv
    else
        MV_CMD=mv
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
DIR=pub/software/unix/fasta
USERID=anonymous
PASSWD=$MAILID
COMPFILE=fasta3.tar #symbolic link pointing to latest version

if [ -d fasta3 ]
	then
   $RM_CMD -rf fasta3
   fi
   
mkdir fasta3
cd fasta3

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
   echo get $COMPFILE.gz  >>  ftp.input
   echo bye >> ftp.input
  # run FTP
  nice $FTPCOMMAND -i -n $HOST < ftp.input
  
echo FTP completed

#uncompress $COMPFILE
gunzip $COMPFILE.gz
tar xvfp $COMPFILE

# The name of the fasta directory changes with each release,
# so we have to do the following to get the name.
FASTADIR="`pwd`/`ls -1d fasta-*`"
if [ -d  $FASTADIR ]
then
   cd "$FASTADIR/make"
   chmod a+r *

   if [ "$BIRCH_PLATFORM" = solaris-sparc ]
	   then 
      # Default is HZ=100. See Makefile
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

      if [ "$GCC" = 1 ]
   	   then
	  # Replace the sun cc command with the gcc command
	  sed -e 's/^CC=.*/CC= gcc -g -O3/'  < Makefile.sun > Makefile.sun.fixed
	  mv Makefile.sun Makefile.sun.original
	  mv Makefile.sun.fixed Makefile.sun
      else
	 if [ "$CC" = 0 ]
      	   then
	   echo "fasta3install.sh\: no C compiler available. Can\'t compile fasta3 package."
	   exit 1
	 fi        
      fi           


   fi


   # In the new fasta35, all makefiles are in the make directory, but the correct
   # makefile must be run from the src directory

  cd ../src
  echo Current directory:`pwd`

   # -------------  Binary files

   case $BIRCH_PLATFORM in
     "solaris-sparc")
       nice make -f $FASTADIR/make/Makefile.sun
       ;;       
       # There are apparantly incompatabilities between Solaris x86 on AMD and
       # Solaris x86 on Intel hardware. Using Makefile.sun_x86, which uses the sun
       # cc compiler,  code compiled on AMD will not run on Intel. Conversely,
       # code compiled on Intel WILL run on AMD. It is likely that the Sun compiler
       # generates an AMD-specific hardware instruction  
       # It is not possible to simply change 'cc' to 'gcc' in Makefile.sun_x86
       # and get the code to compile. However, it is possible to use Makefile.linux64
       # which uses 'gcc',
       # directly on a Solaris AMD machine  and get the programs to compile. Both
       # work, but as of yet, neither has been tested extensively.
     "solaris-amd64")
       nice make -f $FASTADIR/make/Makefile.sun_x86
       ;;
     "linux-intel")
       nice make -f $FASTADIR/make/Makefile.linux32
       ;;
     "linux-x86_64")
       nice make -f $FASTADIR/make/Makefile.linux64
       ;; 
     "osx-x86_64")
       nice make -f $FASTADIR/make/Makefile.os_x86_64
       ;;            
     "AIX")
       nice make -f $FASTADIR/make/Makefile.ibm   
       ;;
     *)
       echo 'fasta3install.sh: Makefile not available for this platform, or '$BIRCH_PLATFORM' not set'
       exit 1
       ;;
   esac


   cd ../bin
   for FILE in `cat $BIRCH/install-birch/fasta/BINFILES`
       do
         chmod a+rx $FILE
         $MV_CMD -f $FILE $BIRCH/$BIRCH_BIN/
   done

  cd ../doc
  chmod 644 *
  
   # -------------- Manual pages
   $MV_CMD *.1 $BIRCH/manl

   # -------------- Documentation files
   $MV_CMD * $doc/fasta

   # -------------- Scoring Matrices
   cd ../data
   chmod 644 *
   $MV_CMD * $dat/fasta
   
    # -------------- Sample sequences
   cd ../seq
   chmod 644 *
   $MV_CMD * $dat/fasta  

else
   echo 'fasta3install.sh: fasta directory not found'
   exit 1
fi


