package birchconfig;


public class screenMsg {


    //-----------------------------------------------
    /**
    * Begin each section of the logfiles with a subheading.
    * @param Prog - Name of program to run
    * @param Message - Message to print to logfiles
    */
    public static void printSubHeading(String Prog, String Message) {
        System.out.println();
        System.out.println();
        String sepline = " - - - - - - - - - - ";
        System.out.println(sepline + Prog + sepline);
        if (Prog != "") {
            System.out.println("Running " + Prog);
        }
        System.out.println(Message);
        System.out.println();
    }
}
