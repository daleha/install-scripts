#!/bin/sh

# updatelocal.sh
# During a BIRCH update, this script checks to make sure
# that all files found in the new copy of $BIRCH/local-generic
# are also present in the existing $BIRCH/local directory. 
# Files not found in $BIRCH/local are copied from $BIRCH/local-generic
# 

# Set variables for local-generic, local, and current
# directories

CDIR=$PWD
#echo CDIR  $CDIR
LOCAL="../local"
echo LOCAL  "$LOCAL"
LOCALGENERIC="../local-generic"
echo LOCALGENERIC "$LOCALGENERIC"

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

unset noclobber


#---------------------------------------------------------------------------
# When a directory is not found, copy whole directory
# in a single step. Otherwise, copy one file at a time.
for name in `cat NewLocalFiles.list`
   do
      # Note: we have to check to see if $name is a link FIRST, before checking
      # to see if it is a file or directory. Apparently, the shell considers a 
      # link to also be a file, so
      # -f $LOCALGENERIC/$name would be true for a link.
     if [ -h "$LOCALGENERIC/$name" ] # symbolic link
	  then
	      if [ ! -h "$LOCAL/$name" ]
	         then
	           echo 'Copying link' "$LOCAL/$name"
		   cd $LOCALGENERIC
		        # TARGET is the link target. If the system defaults to ls -F, either a *
		        # of a @ would be appended to the target. We use sed to get rid of those characters. 
		   TARGET=`ls -l $name | cut -d ">" -f2 | sed -e "s/[*@]$//"`
		   cd $LOCAL
		   ln -s $TARGET $name 
	           cd $CDIR
	      fi   
     elif  [ -d "$LOCALGENERIC/$name" ] # directory
	 then
	   if  [ ! -d "$LOCAL/$name" ]
	     then
	       echo 'Copying directory ' "$LOCAL/$name" 
	       cp -rp $LOCALGENERIC/$name $LOCAL/$name
	   fi
	   
     elif [ -f "$LOCALGENERIC/$name" ] #file
	  then
	      if [ ! -f "$LOCAL/$name" ]
	         then
	           echo 'Copying file' "$LOCAL/$name" 
	           cp -p $LOCALGENERIC/$name $LOCAL/$name
	      fi	 	         
     fi
done
