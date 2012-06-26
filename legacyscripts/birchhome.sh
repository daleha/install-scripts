#!/bin/sh



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
   echo '>>> birchhome.sh cannot be run in BIRCHDEV'
   echo '>>> Doing so would clobber the master copy of BIRCH'
   exit 1
else
   if  [ -d "$BIRCH" ]
      then
      cd ../admin
      MINIBIRCH=`grep BirchProps.minibirch ../local/admin/BIRCH.properties |cut -f2 -d"="`
      if [ "$MINIBIRCH" = "true" ]
	 then
            FILELIST="cshrc.source
	    profile.source
	    add_to_cshrc
	    add_to_login
	    add_to_profile
	    newuser
	    $BIRCH/dat/fasta/fastgbs
	    $BIRCH/dat/fasta/*.fil
	    $BIRCH/install-birch/htmldir.param 
	    $BIRCH/dat/fasta/fastgbs
	    $BIRCH/dat/birch/ldir.param
	    $BIRCH/dat/bldna/ldir.param 
	    $BIRCH/dat/blprotein/ldir.param 
	    $BIRCH/dat/bltree/ldir.param 
	    $BIRCH/dat/blmarker/ldir.param 
	    $BIRCH/dat/XLandscape/XLand
	    $BIRCH/admin/launchers/birch.desktop	    
	    $BIRCH/admin.uninstall/cshrc.source 
	    $BIRCH/admin.uninstall/profile.source"
	else	  
            FILELIST="cshrc.source
	    profile.source
	    add_to_cshrc
	    add_to_login
	    add_to_profile
	    newuser
	    $BIRCH/dat/fasta/fastgbs
	    $BIRCH/dat/fasta/*.fil
	    $BIRCH/install-birch/htmldir.param 
	    $BIRCH/dat/fasta/fastgbs
	    $BIRCH/dat/birch/ldir.param
	    $BIRCH/dat/bldna/ldir.param 
	    $BIRCH/dat/blprotein/ldir.param 
	    $BIRCH/dat/bltree/ldir.param 
	    $BIRCH/dat/blmarker/ldir.param
	    $BIRCH/dat/XLandscape/XLand
	    $BIRCH/admin/launchers/birch.desktop
	    $BIRCH/admin.uninstall/cshrc.source 
	    $BIRCH/admin.uninstall/profile.source
	    $BIRCH/java/ArrayNorm/ArrayNorm.lax
	    $BIRCH/java/Bluejay/Bluejay.lax
	    $BIRCH/java/Jalview/Jalview.lax
	    $BIRCH/java/genographer/genograph.cfg
	    $BIRCH/pkg/NCBI/.ncbirc"
      fi
      
      unset noclobber
      for file in $FILELIST 
	 do
	 echo Setting location of BIRCH home directory as $BIRCH in $file
	 cat $file | sed s%/home/psgendb/BIRCHDEV%$BIRCH%g > temp.$$
	 cat temp.$$ > $file
         chmod a+r $file 
	 done
      chmod a+rx . 
      chmod a+rx newuser $BIRCH/install-birch/makelinks.sh

      # Make sure that all directories in $BIRCH are world
      # readable and world executable
      chmod a+rx $BIRCH
      cd $BIRCH
      for file in `ls`
#	  for file in $( ls )
        do
	  if [ -d $file ]
	    then 
	      chmod a+rx $file
          fi
	done
      # Set the userid of the birchdb database administrator
      cd $BIRCH/public_html/birchdb/wspec
      sed s%psgendb%`$BIRCH/script/whoami`%g < passwd.wrm > temp.$$
      cat temp.$$ > passwd.wrm
      chmod a+r passwd.wrm

      # Set the userid of the birchdb database administrator
      cd $BIRCH/local/public_html/birchdb/wspec
      sed s%psgendb%`$BIRCH/script/whoami`%g < passwd.wrm > temp.$$
      cat temp.$$ > passwd.wrm
      chmod a+r passwd.wrm      
      
   else
      echo No such directory: $BIRCH. Exiting
      exit 1
   fi
fi

