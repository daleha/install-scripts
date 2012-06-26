package birchconfig;

import java.io.*;

/**Types and methods for safely opening or closing
an input file for reading lines of text. (8/3/98)
*/
public class BufferedTextInputFile {

    File F;
    FileReader FR;
    BufferedReader BR;
    String currentLine = "";
    int BYTE = 0;
    char CH;
    boolean EOF = false;

/**Open file and create FileReader and BufferedReader.
* @param S String containing name of file to be opened*/
    boolean OpenOkay (String S) {
      F = new File(S);
      try {FR = new FileReader(F);
           BR = new BufferedReader(FR);
           }
      catch (FileNotFoundException e) {System.out.println("File not found");};
      return F.canRead();
      } // OpenOkay

/** Get the next character from the BufferedReader  */
    char nextChar () {
	   try {EOF = ((BYTE = BR.read()) == -1);}
	   catch (IOException e){System.err.println(e.getMessage());} ;
	   CH = (char) BYTE;
	   return CH;
	} // nextChar


/** Get the next line of text from the BufferedReader and
place it in currentLine */
    String nextLine () {
	   try {EOF = ((currentLine = BR.readLine()) == null);}
	   catch (IOException e){System.err.println(e.getMessage());} ;
	   return currentLine;
	} // nextLine

} // BufferedTextInputFile
