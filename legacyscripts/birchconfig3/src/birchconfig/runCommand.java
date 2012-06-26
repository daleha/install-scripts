package birchconfig;

import java.io.InputStreamReader;
import java.io.BufferedReader;


public class runCommand {

        /**
         * Run an external program.
         *
         * @param COMMAND String: command string to be passed to shell
         * @return boolean
         *
         * <p>Adapted from:
         * <ul>http://www.particle.kth.se/~lindsey/JavaCourse/Book/Part3/Chapter23\
         *     runextProg.html </ul>

         */
        public static boolean runCommand(String COMMAND) {
        boolean OKAY = true;

        try {
            String [] TOKENS = COMMAND.split(" ");
            Runtime rt = Runtime.getRuntime();
            Process P = rt.exec(TOKENS);
            InputStreamReader reader = new InputStreamReader(P.getInputStream());
            BufferedReader buf_reader = new BufferedReader(reader);
            String LINE;
            while ((LINE = buf_reader.readLine()) != null) {
                System.out.println(LINE);
            }
            P.waitFor();
            if (P.exitValue() == 0) {
                OKAY = true;
            }
            else {
                OKAY = false;
            }
//             P.destroy();
        } catch (Exception e) {
            OKAY = false;
            System.out.println(COMMAND);
            System.err.println(e.getMessage());
        }

        return OKAY;
    }
}
