#!/usr/bin/env python




def main():
    from config import getArgs

    args = getArgs(__file__)

    if ( args != None ):
        pass
#
#
#    except:
#        
#        err=traceback.format_exc()
#        error(err)
#
#    finally:
#        info("Installation finished")



if __name__ == "__main__":
    from lib import boot_strap 
    boot_strap(main)
