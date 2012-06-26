package birchconfig;

import javax.swing.JTextField;

/** Holds components of URL as set by URLbuilder
* Objects of this class are used to store the values entered
* using URLbuilder so that each time you call the URLbuilder,
* it remembers what you entered last time.
 * <p>Fields:</p>
 * <ul>
 * <li>URLtitle - title used in URLbuilder window</li>
 * <li>prefix - eg. www</li>
 * <li>secondary - secondary level domain</li>
 * <li>primary - primary level domain eg. .com, .net</li>
 * <li>tilda - preceeds userid</li>
 * <li>useridDir - userid or directory name</li>
 * <li>URLstr - string representation of the above components, made using
 *   makeURLstring()</li>
 * <li>isValid - true if the URL points to an existing page</li>
 * <li>URLTextField - JTextfield, used for displaying the URLstr</li>
 * <li></li>
 * </ul>*/
public class URLholder {
    String URLtitle; // title used in URLbuilder
    String prefix;
    String secondary;
    String primary;
    String tilda;
    String useridDir;
    String URLstr;
    boolean isValid;
    JTextField URLTextField = new JTextField();

    /** Copy fields from another URLholder */
    void copyValues (URLholder UH) {
         this.URLtitle = UH.URLtitle;
         this.prefix = UH.prefix;
         this.secondary = UH.secondary;
         this.primary = UH.primary;
         this.tilda = UH.tilda;
         this.useridDir = UH.useridDir;
         this.makeURLstring();
         this.isValid = UH.isValid;
         this.URLTextField.setText(this.URLstr);
    }

    /** Set URLstr to a string representation of the URL, based
     * on the other variables.
     */

    void makeURLstring () {
        this.URLstr = "http://";
        if (!this.prefix.trim().equals("")) {
            this.URLstr = this.URLstr + this.prefix.trim() + ".";
        }
        this.URLstr = this.URLstr + this.secondary.trim() + "." + this.primary.trim();
        if (this.useridDir != null && this.useridDir.trim().length() > 0) {
            this.URLstr = this.URLstr + "/" + this.tilda.trim() + this.useridDir.trim();
        }
    }

    // Use this constructor to create a URLholder without initializing.
    public URLholder() {
        URLstr = "";
        isValid = false;
    }

    // Use this constructor to create a URLholder whose values
    // are guessed from the BirchProperties file.
    public URLholder(BirchProperties BP) {
        URLtitle = ""; // title used in URLbuilder
	int atindex = -1;
        prefix = "www";
        // We can make a wild guess that the email address of the
        // birch admin contains the name of the host eg.
        // psgendb@cc.umanitoba.ca
        secondary = "";
	
	if (BP.adminEmail != null) {
	    atindex = BP.adminEmail.indexOf("@");
	    if (BP.adminEmail.length() > atindex & atindex > 0) {
	       int rightdot = BP.adminEmail.lastIndexOf(".");
	       secondary = BP.adminEmail.substring(atindex+1,rightdot);
	       primary = BP.adminEmail.substring(rightdot+1);
	    }
	}
        else {
            primary = "";
            secondary = "";
        }
        primary = "ca";

        tilda = "~";
        useridDir = BP.adminUserid;
        URLstr = "";
        isValid = false;
    }
}
