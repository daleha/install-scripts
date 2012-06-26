package birchconfig;

import java.io.File;

//-----------------------------------------------
/**
 * Rename a file or directory. Similar to the Unix mv command.
 */

public class mv {

    public static boolean mv (String oldname, String newname) throws Exception  {
        boolean OKAY = true;
        File oldfile = new File(oldname);
        File newfile = new File(newname);
        try {
            OKAY = oldfile.renameTo(newfile);
            }
         catch (Exception e) {
             OKAY = false;

            }
        return OKAY;
    }
}
