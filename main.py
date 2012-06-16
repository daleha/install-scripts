#!/usr/bin/env python




def main():
    from config import getArgs

    args = getArgs(__file__)

#	if "winxp-32" in ARGS.platform:
#		print_console("This is a windows install")
#		has_depends=True
#	else:
#		has_depends=check_depends()
#	
#	if (not has_depends):
#		print_console("A required dependancy is not present on system path, BIRCH cannot be installed")
#		shutdown()
	

    if ( args != None ):
        pass


if __name__ == "__main__":
    from lib import boot_strap 
    boot_strap(main)
