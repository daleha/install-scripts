#!/bin/sh

# This script is for use in workshops in which
# students create their own mini BIRCH sites. 
# It is NOT needed for a full standalone BIRCH installation.

# Most of the directories will merely be links
# to real BIRCH directories elsewhere on the system.
# birchconfig.sh downloads the birch admininstrative
# and documentation directories, and configures
# them. 

# Next, makelinks.sh makes symbolic links for mini-BIRCH.
# MASTER is the location of the master copy of BIRCH
# By making symbolic links for most of the BIRCH
# directories to those in $MASTER, each student
# can, in effect, create their own BIRCH site.

cd ..
MASTER=$1

if [ -d $MASTER ] 
   then
      LINKFILES=`cat install-birch/minibirch.links`
      for file in $LINKFILES
	 do
	 echo $file
	 ln -s $MASTER/$file $file
      done
else
   echo $MASTER not a directory
fi

