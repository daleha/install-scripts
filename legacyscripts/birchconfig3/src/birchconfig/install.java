package birchconfig;

import java.io.*;
import java.util.Date;
//import birchconfig.runCommand;
//import birchconfig.mv;

/**
 * <p>Title: Install a BIRCH system.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: University of Manitoba</p>
 *
 * <p>Description: This program calls external scripts to install a new BIRCH system
 * or update an existing one. It can be run as a method from birchconfig.jar,
 * or as a standalone commandline program</p>
 *  <p>Logfile: $BIRCH/install-birch/install.log.
 * <p>This program <b>must</b> be run from the $BIRCH/install-birch directory.</p>
 * <p>To avoid accidently clobbering the master copy of BIRCH, this program will stop
 * and print an error message if the userid of the person runing it is 'psgendb'.</p>
 @param -new do a new BIRCH installation. This option will create a new $BIRCH/local
 * directory by renaming 'local-generic' to 'local'. Without the -new switch,
 * install will update the BIRCH site using settings in $BIRCH/local
 *
 *
 *
 * @author Dr. Brian Fristensky
 * @version 0.1

 */


public class install {
    public install() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static runCommand runner = new runCommand();

    //-----------------------------------------------
    /**
     * Write a message to the logfile.
     */

    static void progressMessage(BufferedTextOutputFile logfile, String S) {
         logfile.PW.println(S);
         }


    //-----------------------------------------------
    /** Run the nobirch script. This script deletes lines from .cshrc, .profile,
    * .bashrc etc. which add BIRCH to the PATH and set environment variables
    *needed for BIRCH
    */
    static boolean runNobirch(BufferedTextOutputFile logfile) {
       boolean OKAY = true;
       String MSG = "Temporarily removing BIRCH access for this account";
       logfile.PW.print("nobirch: " + MSG + ": ");
       screenMsg scr = new screenMsg();
       scr.printSubHeading("nobirch",MSG);
       String COMMAND = "../admin/nobirch -Q";
       OKAY = runner.runCommand(COMMAND);

       if (OKAY) {
          logfile.PW.println("Succeeded");
          }
       else {
            logfile.PW.println("FAILED!");
            }
       return OKAY;
       }

    //-----------------------------------------------
    /** Run the script makelinks.sh. This script creates
     *  symbolic links to directories in the master copy
     *  of BIRCH, so that these directories do not need
     *  to be duplicated for each mini-BIRCH instance.
    */
    static boolean makeLinks(String BirchMasterCopy, BufferedTextOutputFile logfile) {

       boolean OKAY = true;
       String MSG = "Creating links to master copy of BIRCH";
       logfile.PW.print("Running makelinks.sh: " + MSG);
   //System.out.println("Running makelinks.sh");
       screenMsg scr = new screenMsg();
       scr.printSubHeading("makelinks.sh",MSG);
       OKAY = runner.runCommand("./makelinks.sh " + BirchMasterCopy);
       if (OKAY) {
           logfile.PW.println("Succeeded");
           }
       else {
           logfile.PW.println("FAILED!");
            }
      return OKAY;
      }


    //-----------------------------------------------
    /** rename local-generic directory to local
     *
     */

    static boolean makeLocal(String BIRCH, BufferedTextOutputFile logfile) {
       boolean OKAY = true;
       mv mover = new mv();
       String oldname = BIRCH + "/local-generic";
       String newname = BIRCH + "/local";
       String MSG = "Move " + oldname + " to " + newname;
       logfile.PW.print(MSG);
       screenMsg scr = new screenMsg();
       scr.printSubHeading("",MSG);
       try {
           OKAY = mover.mv(oldname,newname);
           }
       catch (Exception e) {
           OKAY = false;
           logfile.PW.println(e);
         }
       if (OKAY) {
          logfile.PW.println("Succeeded");
       }
       else {
           logfile.PW.println("FAILED!");
       }
       return OKAY;
       }

       //-----------------------------------------------
       /** Files added to local-generic since the previous release
        * will be missing from local. Copy these files from local-generic to local
       */
       static boolean copyLocal(BufferedTextOutputFile logfile) {

          boolean OKAY = true;
          String MSG = "Copying new files from local-generic to local";
          logfile.PW.print(MSG);
      //System.out.println("Running update-local.sh");
          screenMsg scr = new screenMsg();
          scr.printSubHeading("update-local.sh",MSG);
          OKAY = runner.runCommand("./update-local.sh ");
          if (OKAY) {
              logfile.PW.println("Succeeded");
              }
          else {
              logfile.PW.println("FAILED!");
               }
         return OKAY;
         }


    //-----------------------------------------------
    /** Run the script birchhome.sh. This script goes through
    * configuration files in BIRCH and changes '/home/psgendb'
    * to the local BIRCH home directory.
    */
    static boolean BirchHome(String BIRCH, BufferedTextOutputFile logfile) {

        boolean OKAY = true;
        String MSG = "Set BIRCH homedirectory to " + BIRCH + ": ";
        logfile.PW.print(MSG);
//System.out.println("Running birchhome.sh");
         screenMsg scr = new screenMsg();
         scr.printSubHeading("birchhome.sh",MSG);
         OKAY = runner.runCommand("./birchhome.sh");
         if (OKAY) {
             logfile.PW.println("Succeeded");
             }
         else {
                logfile.PW.println("FAILED!");
               }
         return OKAY;
         }


    //-----------------------------------------------
    /** Run the script setplatform.sh. This script uncomments
    * the appropriate line in profile.source and platform.profile.source
    * to set the enviroment variable BIRCH_PLATFORM
    */
    static boolean setPlatform(String platform, BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String MSG = "setplatform.sh: Set default platform to " + platform + ": ";
        logfile.PW.print(MSG);
        String COMMAND = "./setplatform.sh " + platform;
        screenMsg scr = new screenMsg();
        scr.printSubHeading("setplatform.sh",MSG);
        OKAY = runner.runCommand(COMMAND);
         if (OKAY) {
             logfile.PW.println("Succeeded");
             }
         else {
                logfile.PW.println("FAILED!");
               }
         return OKAY;
         }

    /**
     * makeParamFile - create the file $birch/local/admin/newstr.param,
     * which contains strings to be substituted for those in oldstr.param.
     */
    static void makeParamFile(BirchProperties tempBP, BufferedTextOutputFile logfile) {
       BirchProperties BP = tempBP;
       String FN = BP.homedir + "/local/admin/newstr.param";
       BufferedTextOutputFile outfile = new BufferedTextOutputFile();
       if (outfile.WriteOkay(FN)) {
           logfile.PW.println("Create substitution strings for customdoc.py: ");
           try {
               String S;
               S = "~";
               outfile.PW.println(S);
               S = "file://" + BP.homedir + "/public_html";
               outfile.PW.println(S);
               BP.birchURL = S;
               S = "file://" + BP.homedir;
               outfile.PW.println(S);
               BP.birchHomeURL = S;
               S = BP.adminEmail;
               outfile.PW.println(S);
               S = BP.homedir;
               outfile.PW.println(S);
               S = BP.adminUserid;
               outfile.PW.println(S);
               outfile.PW.flush();
               outfile.FW.close();
           }
          catch (Exception e) {
               System.out.println(e);
          }
       }
    }

    //-----------------------------------------------
    /** Run the script customdoc.py. This script substitutes
    * strings (eg. URLs, directory names, userid of BIRCH admin.,
    * email address  of BIRCH admin.) in HTML documentation from
    * the original  values to local values
    */
    static boolean customDoc(BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String MSG = "birchconfig: Changing HMTL documentation to correspond to local directory structure: ";
        logfile.PW.print(MSG);
        screenMsg scr = new screenMsg();
        scr.printSubHeading("customdoc.py",MSG);
        String COMMAND = "python ./customdoc.py oldstr.param newstr.param htmldir.param";
        OKAY = runner.runCommand(COMMAND);

         if (OKAY) {
             logfile.PW.println("Succeeded");
             }
         else {
                logfile.PW.println("FAILED!");
               }
         return OKAY;
         }

    //-----------------------------------------------
    /** Run the script htmldoc.py. This script creates web pages
    * organizing documentation by category, package, and an index.
    */
    static boolean htmlDoc(String BIRCH, BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String MSG = "Creating web pages that index documentation for all programs";
        logfile.PW.print("htmldoc.py: : " + MSG);
        screenMsg scr = new screenMsg();
        scr.printSubHeading("htmldoc.py",MSG);
        String COMMAND =  "python ./htmldoc.py";
        OKAY = runner.runCommand(COMMAND);

         if (OKAY) {
             logfile.PW.println("Succeeded");
             }
         else {
                logfile.PW.println("FAILED!");
               }
         return OKAY;
         }

    //-----------------------------------------------
    /** Run the script makemenus.py. This script creates .GDEmenus
    * files for all platforms.
    */
    static boolean makemenus(String BIRCH, BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String MSG = "Creating GDE menus";
        logfile.PW.print("makemenus.py: " + MSG + ": ");
        screenMsg scr = new screenMsg();
        scr.printSubHeading("makemenus.py",MSG);
        String COMMAND = "./runmakemenus.sh";
        OKAY = runner.runCommand(COMMAND);

         if (OKAY) {
             logfile.PW.println("Succeeded");
             }
         else {
                logfile.PW.println("FAILED!");
               }
         return OKAY;
         }

    //-----------------------------------------------
    /** Run the script fasta2install.sh and fasta3install.sh.
    * These scripts download the fasta2 and fasta3 packages by FTP
    * compile the programs, and install programs and documentation.
    */
    static boolean makefasta(String BIRCH, BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String COMMAND;
        // fasta2
        String MSG = "Downloading and compiling fasta2 package";
        logfile.PW.print("installfasta2.sh: " + MSG + ": ");
        screenMsg scr = new screenMsg();
        scr.printSubHeading("installfasta2.sh",MSG);
        COMMAND = "./installfasta2.sh";
        OKAY = runner.runCommand(COMMAND);
        if (OKAY) {
           logfile.PW.println("Succeeded");
                   }
        else {
           logfile.PW.println("FAILED!");
             }

        // fasta3
        MSG = "Downloading and compiling fasta3 package";
        logfile.PW.print("installfasta3.sh: " + MSG + ": ");
        scr.printSubHeading("installfasta3.sh",MSG);
        COMMAND = "./installfasta3.sh";
        OKAY = runner.runCommand(COMMAND);

         if (OKAY) {
             logfile.PW.println("Succeeded");
             }
         else {
                logfile.PW.println("FAILED!");
               }

         return OKAY;
         }

     //-----------------------------------------------
     /** Run the newuser script. This script adds lines to .cshrc, .profile,
     * .bashrc etc. to add BIRCH to the PATH and set environment variables
     * needed for BIRCH
     */
     static boolean runNewUser(BufferedTextOutputFile logfile) {
     boolean OKAY = true;
     String COMMAND;
     String MSG = "Setting up your account to use BIRCH ";
     logfile.PW.print("newuser: " + MSG);
     screenMsg scr = new screenMsg();
     scr.printSubHeading("newuser",MSG);
     COMMAND = "../admin/newuser";
     OKAY = runner.runCommand(COMMAND);

     if (OKAY) {
           logfile.PW.println("Succeeded");
        }
     else {
           logfile.PW.println("FAILED!");
          }
           return OKAY;
         }

    //============================= MAIN =====================================
    public static boolean main(String[] args) {
       BufferedTextOutputFile logfile = new BufferedTextOutputFile();
       boolean OKAY = true;

       // read command line arguments
       boolean newInstallation = false;
       BirchProperties BP = new BirchProperties();
       for (int i=0; i < args.length; i++) {
           if (args[i].equals("-new")) {
               newInstallation = true;
               }
          else {
             BP.loadProps(args[i]);
               }
       }

       // Install or update BIRCH
       System.out.println("BirchProperties read. BIRCH home dir: " + BP.homedir);
       if (logfile.WriteOkay("../birchconfig.install.log")) {
          if (newInstallation) {
             progressMessage(logfile,"New BIRCH Installation");
             }
          else {
               progressMessage(logfile,"Updating BIRCH");
               }
          Date D = new Date();
          String TimeStamp = D.toString();
          progressMessage(logfile,TimeStamp);
          progressMessage(logfile,"");
          }

       // For testing purposes, we allow psgendb to get as far as this
       // point. Since psgendb is the the userid for the master BIRCH
       // site, we don't want to accidently clobber the master copy of
       // BIRCH!
       String userid = System.getProperty("user.name");
       //if (userid.equals("psgendb")) {
       if (BP.homedir.contains("BIRCHDEV")) {    
          progressMessage(logfile,"UPDATE ABORTED! - install on BIRCHDEV prohibited");
          System.out.println("UPDATE ABORTED! - install on BIRCHDEV prohibited");
          }
       else {
            // for update, temporarily remove BIRCH access for this account.
            if  ( ! newInstallation) {runNobirch(logfile);}

            // create links to master copy of BIRCH
            if (BP.minibirch.equals("true")) {makeLinks(BP.BirchMasterCopy,logfile);}

            // move local-generic to local
            if (newInstallation)
               {OKAY = makeLocal(BP.homedir,logfile);}
            else
               {OKAY = copyLocal(logfile);}

            if (OKAY) {
                    // run birchhome.sh
                    OKAY = BirchHome(BP.homedir, logfile);
                    if (OKAY) {
                            // set BIRCH_PLATFORM in platform.source and platform.profile.source
                            if (newInstallation) {OKAY = setPlatform(BP.platform, logfile);}

                            if ( OKAY ) {
                               // run customdoc.py
                               if ( newInstallation ) {
                                   makeParamFile(BP, logfile);
                               }

                                OKAY = customDoc(logfile);
                                if (OKAY) {
                                        // run htmldoc.py
                                        OKAY = htmlDoc(BP.homedir, logfile);
                                        if (OKAY) {
                                                // generate GDE menu files
                                                //OKAY = makemenus(BP.homedir, logfile);
                                                //if (OKAY) {
                                                        // install FASTA packages
                                                        //if (BP.minibirch.equals("false") && (OKAY) ) {
                                                        //    makefasta(BP.homedir, logfile);
                                                        //}
                                                        if ( OKAY ) {
                                                           // set up .rc files so that BIRCH Administrator can use BIRCH
                                                           OKAY = runNewUser(logfile);
                                                           }

                                                //}

                                         }
                                }

                          }
                   }
            }

       }

    logfile.PW.close();
    return OKAY;
    } // MAIN

    private void jbInit() throws Exception {
    }
}
