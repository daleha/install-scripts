package birchconfig;

import java.io.*;
import java.util.Date;
//import birchconfig.runCommand;
//import birchconfig.mv;
// bogus comment as a test

/**
 * <p>Title: Change URLs in BIRCH web pages for access by local webserver.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: University of Manitoba</p>
 *
 * <p>Description: This program calls customdoc.py.</p>
 *  <p>Logfile: $BIRCH/install-birch/install.log.
 * <p>This program <b>must</b> be run from the $BIRCH/install-birch directory.</p>
 * <p>To avoid accidently clobbering the master copy of BIRCH, this program will stop
 * and print an error message if the userid of the person runing it is 'psgendb'.</p>
 @param - none
 *
 *
 *
 * @author Dr. Brian Fristensky
 * @version 0.1

 */


public class customdoc {
    public customdoc() {
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
               outfile.PW.println("~");
               outfile.PW.println(BP.birchURL);
               outfile.PW.println(BP.birchHomeURL);
               outfile.PW.println(BP.adminEmail);
               outfile.PW.println(BP.homedir);
               outfile.PW.println(BP.adminUserid);
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
    static boolean runCustomDoc(BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String MSG = "Changing HMTL documentation to correspond to local directory structure";
        logfile.PW.print("birchconfig: " + MSG + ": ");
        screenMsg scr = new screenMsg();
        scr.printSubHeading("customdoc.py",MSG);
        String COMMAND = "python ./customdoc.py oldURLs.param newURLs.param htmldir.param";
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
    /**
    * <P> Read in newURLs.param and update BIRCH properties.</P>
    * @param BP BirchProperties
     */
     static void readNewURLs(String [] newstrings) {
        BufferedTextInputFile infile = new BufferedTextInputFile();
        if (infile.OpenOkay("newURLs.param")) {
           String line = infile.nextLine();
           for (int i=0; i<=1; i++) {
                newstrings[i] = line;
                line = infile.nextLine();
               }
           }
    }

    //-----------------------------------------------------
    /** Run the script htmldoc.py. This script generates
    *   HMTL pages with links to documentation files. URLs from
    *   links come from BIRCH.properties
    */
    static boolean runHtmlDoc(BufferedTextOutputFile logfile) {
        boolean OKAY = true;
        String MSG = "Rebuilding program documentation pages to correspond to local directory structure";
        logfile.PW.print("birchconfig: " + MSG + ": ");
        screenMsg scr = new screenMsg();
        scr.printSubHeading("htmldoc.py: ",MSG);
        String COMMAND = "python ./htmldoc.py";
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
    public static void main() {
       BufferedTextOutputFile logfile = new BufferedTextOutputFile();
       boolean OKAY = true;


       // Install or update BIRCH

       if (logfile.WriteOkay("../birchconfig.customdoc.log")) {
          progressMessage(logfile,"Changing URLs in BIRCH documentation");
          Date D = new Date();
          String TimeStamp = D.toString();
          progressMessage(logfile,TimeStamp);
          progressMessage(logfile,"");
          }

       // For testing purposes, we allow birchconfig to get as far as this
       // point. Since BIRCHDEV is the master copy of BIRCH, we don't allow
       // customdoc.py to run on BIRCHDEV.
       String userid = System.getProperty("user.name");
       BirchProperties BP = new BirchProperties();
       System.out.println("BirchProperties read. BIRCH home dir: " + BP.homedir);
       BP.loadProps("../local/admin/BIRCH.properties");       
       //if (userid.equals("psgendb")) {
       if (BP.homedir.contains("BIRCHDEV")) {
          progressMessage(logfile,"UPDATE ABORTED! - running customdoc.py on BIRCHDEV prohibited");
          System.out.println("UPDATE ABORTED! - running customdoc.py on BIRCHDEV prohibited");
          }
       else {
            // run customdoc.py
            if (runCustomDoc(logfile)) {
                // Update newstr.param and BIRCH.properties with the new URLs
                // read command line arguments

                String [] newstrings = new String [2];
                readNewURLs(newstrings);
                BP.birchURL= newstrings[0];
                BP.birchHomeURL= newstrings[1];
                BP.writeProps("../local/admin/BIRCH.properties");
                makeParamFile(BP, logfile);
                // run htmldoc.py to update the URLs for documentation files,
                // based on the new URLS in BIRCH.properties
                runHtmlDoc(logfile);
            }
            else {
                 progressMessage(logfile,"Update of URLs in documentation failed.\n");
                 progressMessage(logfile,"See birchconfig.customdoc.log");
            }
       }

    logfile.PW.close();
    } // MAIN

    private void jbInit() throws Exception {
    }
}
