package birchconfig;

import java.io.File;

//-----------------------------------------------
/**
 * We can probably delete this method now. I believe that after Jave 1.5, getenv
 * was restored to the API.
 * WARNING: This doesn't work. What seems to happen is that
 * echo runs, and prints out the rest of the string, rather than
 * taking the entire string as a command and arguments. Escaping
 * the quotes doesn't help.
 * Get the value of an environment varialbe. Similar to the C getenv command.
 */

public class getEnv {

    public static String getEnv (String varname) throws Exception  {
        String value = "";
        try {
            runCommand runner = new runCommand();
            String comstr = "nice echo \"$" + varname + "\" > GETENV_TEMP_FILE";
            runner.runCommand(comstr);
            BufferedTextInputFile tempfile = new BufferedTextInputFile();
            if (tempfile.OpenOkay("GETENV_TEMP_FILE")) {
               value.equals(tempfile.nextLine());
            }
           tempfile.F.delete();
            }
         catch (Exception e) {
             System.out.println(e);

            }
        System.out.println(varname + ": " + value);
        return value;
    }
}
