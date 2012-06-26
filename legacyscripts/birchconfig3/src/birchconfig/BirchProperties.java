package birchconfig;

import java.util.Properties;
import java.io.*;

/**
 * <p>Title: BIRCH Properties</p>
 *
 * <p>The file $BIRCH/local/admin/BIRCH.properties is created during a new
 * BIRCH install. It is read when updating to a new BIRCH version.  </p>
 * Fields:
 * <ul>
 * <li>String homedir - BIRCH home directory</li>
 * <li>String adminUserid - userid of BIRCH Administrator</li>
 * <li>String adminEmail - email address of BIRCH Administrator</li>
 * <li>String platform - default platform: os-architecture</li>
 * <li>String birchURL - URL of BIRCH web site</li>
 * <li>String birchHomeURL - URL of BIRCH home directory</li>
 * <li>String [] newstrParam - lines for newstr.param file, used by customdoc.py</li>
 * <li>String [] minibirchParamFile - lines from local/admin/BIRCH.mini</li>
 * <li>String minibirch - "false" (default) = complete, "true" = mini</li>
 * <li>String BirchMasterCopy - directory containing the master copy of BIRCH,
 * which is linked to by each mini-Birch instance</li>
 * <li>Properties props - data to be written to local/admin/BIRCH.properties</li>
 *
 */
public class BirchProperties {
   String homedir;
   String adminUserid;
   String adminEmail;
   String platform;
   String birchURL;
   String birchHomeURL;
   String [] newstrParam = new String [6];
   String [] minibirchParam = new String[1];
   String minibirch;
   String BirchMasterCopy = "";
   Properties props = new Properties();

   /**
    *   Guess values for install parameters in BirchProps, using System.getProperty,
    *   or reading values from text files.
    *   GuessProps is only called during a new install, not during an update.
    */

   public void GuessProps() {
        // Home Directory is the parent of the install-birch directory,
        // ie. the current directory.
        File tfile = new File(System.getProperty("user.dir"));
        this.homedir = tfile.getParent();

        // BIRCH Administrator's userid
        this.adminUserid = System.getProperty("user.name");
        // BIRCH Administrator's email address
        this.adminEmail = "";
        // Default os-platform
        // The following platforms are supported:
        //     solaris-sparc
        //     linux-intel
        this.platform = "solaris-sparc";
        String OS = System.getProperty("os.name");
        System.out.println("os.name: " + OS);
        String PLAT = System.getProperty("os.arch");
        System.out.println("os.arch: " + PLAT.toLowerCase());
        String VERSION = System.getProperty("os.version");
        System.out.println("os.version: " + VERSION);
        String DATAMODEL = System.getProperty("os.arch.data.model");
        if (DATAMODEL == null) {
             DATAMODEL = "";
        }
        System.out.println("os.arch.data.model: " + DATAMODEL);
        if (OS.toLowerCase().indexOf("linux") > -1 ) {
            if ( (PLAT.indexOf("amd64") > -1) || (PLAT.indexOf("x86_64") > -1) || (DATAMODEL.indexOf("x86_64") > -1) ) {
                this.platform = "linux-x86_64";
            }
            else {
                this.platform = "linux-intel";
            }
        }
        else if (OS.indexOf("SunOS") > -1) {
             if (PLAT.indexOf("sparc") > -1) {
                 this.platform = "solaris-sparc";
             }
             else {
                 this.platform = "solaris-amd64";
             }
        }
        else if (OS.indexOf("Mac OS X") > -1) {
                 this.platform = "osx-x86_64";
        }
        else if (OS.indexOf("Windows XP") > -1) {
                 this.platform = "winxp-32";
        }
        else if (OS.indexOf("Windows 7") > -1) { //not yet verified as "Windows 7" 
                 this.platform = "win7-64";
        }                
        else {
                 this.platform = null;
        }
        System.out.println("Platform set to: " + this.platform);
        this.birchHomeURL = "file://" + this.homedir;
        this.birchURL = "file://" + this.homedir  + "/public_html";
        // local-generic is a symbolic link to the BIRCH local-generic directory
        String paramfile = "local-generic/admin/BIRCH.mini";
        readParamFile(paramfile,minibirchParam);
	this.minibirch = "";
	if (minibirchParam[0] != null) {
	    this.minibirch = minibirchParam[0].trim();
	}
        System.out.println("miniBIRCH: " + this.minibirch);
   }

   /** Read all lines in a parameter file
    @param FN - filename  */
  private void readParamFile(String FN, String [] paramlines) {
         BufferedTextInputFile paramfile = new BufferedTextInputFile();
         if (paramfile.OpenOkay(FN)) {
            String line;
            line = paramfile.nextLine();
            int j = 0;
            while ((line != null) & (j < paramlines.length)) {
                  paramlines[j] = line;
                  line = paramfile.nextLine();
                  j++;
            }
         }
        }

    /**
     * load Properties from a properties file
     @param FN - filename for BIRCH.properties
     */
    public void loadProps(String FN) {

//        Properties props = new Properties();
        try {
             props.load(new BufferedInputStream(new FileInputStream(FN)));
             // Home Directory
             this.homedir = props.getProperty("BirchProps.homedir","");
             // BIRCH Administrator's userid
             this.adminUserid = props.getProperty("BirchProps.adminUserid","");
             // BIRCH Administrator's email address
             this.adminEmail = props.getProperty("BirchProps.adminEmail","");
             // Default os-platform
             // The following platforms are supported:
             //     solaris-sparc
             //     solaris-amd64
             //     linux-x86_64
             //     linux-intel
             //     osx-x86_64
             this.platform = props.getProperty("BirchProps.platform","");
             // URL for BIRCH Web site
             this.birchURL =  props.getProperty("BirchProps.birchURL","");
             // URL for BIRCH Home Directory
             this.birchHomeURL = props.getProperty("BirchProps.birchHomeURL","");
             // Find out whether this is a mini-BIRCH installation
             this.minibirch = props.getProperty("BirchProps.minibirch","");
             // To update from BIRCH A1.98 or earlier, minibirch was
             // not present in the BIRCH.properties file. We can get it
             // from the local-generic/admin/BIRCH.mini file.
             if (this.minibirch.equals(""))  {
                String paramfile = this.homedir + "/local-generic/admin/BIRCH.mini";
                readParamFile(paramfile,minibirchParam);
                this.minibirch = minibirchParam[0].trim();
                String propFN = this.homedir + "/local/admin/BIRCH.properties";
                writeProps(propFN);
             }
             if (this.minibirch.equals("true")) {
                this.BirchMasterCopy = props.getProperty("BirchProps.BirchMasterCopy","");
             }
             // To update from BIRCH A1.9 or earlier, birchHomeURL and BIRCHURL were
             // not present in the BIRCH.properties file. We can get these
             // from lines 2 and 3 of the newstr.param file.
             if ((this.birchURL.equals("")) | (this.birchHomeURL.equals(""))) {
                 String paramfile = this.homedir + "/local/admin/newstr.param";
                 readParamFile(paramfile,newstrParam);
                 this.birchURL = newstrParam[1];
                 this.birchHomeURL = newstrParam[2];
                 String propFN = this.homedir + "/local/admin/BIRCH.properties";
                 writeProps(propFN);
             }
        }
        catch (Exception ex) {
        }
    }

    /**
     * write Properties to a properties file
     @param FN - filename for BIRCH.properties
     */
    public void writeProps(String FN) {
//        Properties props = new Properties();
        try {
            // A blank email address would make it more difficult
            // to change to an active email address using customdoc.py.
            // Put in a string that can be easily substituted later on.
            if (this.adminEmail.trim().equals("")) {
               this.adminEmail = "noname@unknown_host";
            }
            System.out.println("Writing BIRCH.properties file");
            System.out.println(this.homedir);
            System.out.println(this.adminUserid);
            System.out.println(this.adminEmail);
            System.out.println(this.platform);
            System.out.println(this.birchURL);
            System.out.println(this.birchHomeURL);
            System.out.println(this.minibirch);
            System.out.println(this.BirchMasterCopy);
             // Home Directory
             props.setProperty("BirchProps.homedir",this.homedir);
             // BIRCH Administrator's userid
             props.setProperty("BirchProps.adminUserid",this.adminUserid);
             // BIRCH Administrator's email address
             props.setProperty("BirchProps.adminEmail",this.adminEmail);
             // Default os-platform
             // The following platforms are supported:
             //     solaris-sparc
             //     solaris-amd64
             //     linux-intel
             //     linux-x86_64
             //     osx-x86_64
             //     winxp-32
             //     win7-64
             props.setProperty("BirchProps.platform",this.platform);
             // URL for BIRCH Web site
             props.setProperty("BirchProps.birchURL",this.birchURL);
             // URL for BIRCH Home Directory
             props.setProperty("BirchProps.birchHomeURL",this.birchHomeURL);
             // minibirch
             props.setProperty("BirchProps.minibirch", this.minibirch);
             props.setProperty("BirchProps.BirchMasterCopy", this.BirchMasterCopy);
             FileOutputStream out = new FileOutputStream(FN);
             props.store(out,"Birch Properties");
             out.close();
        }
        catch (Exception ex) {
             System.out.println(ex);
        }
    }

    // CONSTRUCTOR
    public BirchProperties() {

        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void jbInit() throws Exception {
    }
}
