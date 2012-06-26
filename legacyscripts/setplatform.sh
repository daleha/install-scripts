#!/bin/sh

# Set BIRCH_PLATFORM environment variable in platform.source
# and platform.profile.source by uncommenting one of the 
# lines in each file.

#Synopsis: setplatform.sh platform
#


if [ "$1" = solaris-sparc -o "$1" = solaris-amd64 -o "$1" = linux-intel -o "$1" = linux-x86_64 -o "$1" = osx-x86_64 -o "$1" = winxp-32  -o "$1" = win7-64 ]
  then
   platform=$1
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
   sed "s/#setenv BIRCH_PLATFORM $platform/setenv BIRCH_PLATFORM $platform/" \
     < ../local/admin/platform.source > temp
   cat temp  > ../local/admin/platform.source
   $RM_CMD -f temp

   sed "s/#BIRCH_PLATFORM=$platform/BIRCH_PLATFORM=$platform/" \
     < ../local/admin/platform.profile.source > temp
   cat temp > ../local/admin/platform.profile.source
   $RM_CMD -f temp

else
  echo 'setplatform.sh: invalid platform ('$1')'
  exit 1
fi
 

