package birchconfig;

import java.io.*;


/**Types and methods for safely opening or closing an output file for writing lines of text.
   The Java class PrintWriter provides overloaded classes for printing all sorts of
   primitive data types, such as int, double, float etc. as strings.
*/
public class BufferedTextOutputFile {
    File F;
    FileWriter FW;
    BufferedWriter BW;
    PrintWriter PW;

/**Open file and create FileWriter, BufferedWriter, and PrintWriter.
* @param S String containing name of file to be opened*/
    boolean WriteOkay (String S) {
      boolean OKAY = true;
      F = new File(S);
      try {FW = new FileWriter(F);
	   BW = new BufferedWriter(FW);
	   PW = new PrintWriter(BW);
	  }
      catch (IOException e) {OKAY = false;
			     System.out.println("I/O Error in BufferedTextOutputFile.WriteOkay");
			     System.err.println(e.getMessage());} ;
      return OKAY;
       } // WriteOkay


} // BufferedTextOutputFile

