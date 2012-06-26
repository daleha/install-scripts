package birchconfig;

import java.io.*;
import java.util.Date;


/**
 * <p>Title: Uninstall</p>
 *
 * <p>Description: Delete BIRCH directories, and remove BIRCH access for BIRCH Administrator.</p>
 * <p>Logfile: $BIRCH/BIRCH.uninstall.log.
 * <p>To avoid accidently clobbering the master copy of BIRCH, this program will stop
 * and print an error message if the userid of the person runing it is 'psgendb'.</p>
 @param PermUninstall - Permanent Uninstall. If true also move admin.uninstall to admin. This directory
 contains scripts that remove BIRCH access for each
 user, the next time the user logs in.

 @param DelBinaries - Delete Binaries. If true, delete bin directories in $BIRCH.

 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: University of Manitoba</p>
 *
 * @author Dr. Brian Fristensky
 * @version 0.1
 */


public class uninstall {

    static uninstall uninstaller = new uninstall();
    static runCommand runner = new runCommand();

    //-----------------------------------------------
    // Write a message to the logfile.
    static void progressMessage(BufferedTextOutputFile logfile, String S) {
         logfile.PW.println(S);
         }

    //-----------------------------------------------
    // Run the script UNINSTALL-birch.sh. This script deletes
    // most of the BIRCH directories, except $BIRCH/local.
    static boolean deleteDirs(String BIRCH, boolean DelBinaries, BufferedTextOutputFile logfile) {

        boolean OKAY = true;
        String MSG = "Deleting BIRCH directories";
        logfile.PW.println(MSG);
        logfile.PW.print("Running UNINSTALL-birch.sh: ");
        //System.out.println("Running UNINSTALL-birch.sh");
        screenMsg scr = new screenMsg();
        scr.printSubHeading("UNINSTALL-birch.sh",MSG);
        String COMMAND = "chmod u+x UNINSTALL-birch.sh";
        OKAY = runner.runCommand(COMMAND);
        COMMAND = "./UNINSTALL-birch.sh -Q";
         if (! DelBinaries) {
             COMMAND = COMMAND + " -n";
            }
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
    // rename admin.uninstall directory to admin
    static boolean makeAdmin(String BIRCH, BufferedTextOutputFile logfile) {
       boolean OKAY = true;
       mv mover = new mv();
       String oldname = BIRCH + "/admin.uninstall";
       String newname = BIRCH + "/admin";
       String MSG = "Rename " + oldname + " to " + newname;
       logfile.PW.print(MSG);
       screenMsg scr = new screenMsg();
       scr.printSubHeading("",MSG);
       String COMMAND = "chmod u+x UNINSTALL-birch.sh";
       try {
           OKAY = mover.mv(oldname,newname);
           }
       catch (Exception e) {
           OKAY = false;
         };
       if (OKAY) {
          logfile.PW.println("Succeeded");
       }
       else {
           logfile.PW.println("FAILED!");
       }
       return OKAY;
       }


    //============================= MAIN =====================================
    public static void main(boolean PermUninstall, boolean DelBinaries) {
        BufferedTextOutputFile logfile = new BufferedTextOutputFile();
       // Get the path for the BIRCH home directory, assuming it
       // is the parent of the current working directory, which should
       // be install-birch.
       File currentdir = new File(System.getProperty("user.dir"));
       String BIRCH = currentdir.getParent();
       System.out.println("BIRCH home dir: " + BIRCH);
       if (logfile.WriteOkay("../birchconfig.uninstall.log")) {
          progressMessage(logfile,"Beginning uninstall");
          Date D = new Date();
          String TimeStamp = D.toString();
          progressMessage(logfile,TimeStamp);
          progressMessage(logfile,"");
          }

       // For testing purposes, we allow birchconfig to get as far as this
       // point. Since BIRCHDEV is the master copy of BIRDH, we don't allow
       // an automated uninstall!
       String userid = System.getProperty("user.name");
       //if (userid.equals("psgendb")) {
       if (BIRCH.contains("BIRCHDEV")) {    
          progressMessage(logfile,"UNINSTALL ABORTED! - uninstall of BIRCHDEV prohibited");
          System.out.println("UNINSTALL ABORTED! - uninstall of BIRCHDEV is prohibited");
       }
       else {

            // run UNINSTALL-birch.sh
            deleteDirs(BIRCH, DelBinaries, logfile);

            // move admin.uninstall to admin
            if (PermUninstall) {
                makeAdmin(BIRCH, logfile);
               }
             }
    logfile.PW.close();
    } // MAIN
}
