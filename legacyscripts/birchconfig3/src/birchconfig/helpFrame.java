package birchconfig;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Display a file or web page in a JEditorPane.
 *
 * <p>Fields:
 * <ul>
 * <li>boolean isValid - true if the file or page exists and can be read.</li>
 * </ul>
 */
public class helpFrame extends JFrame {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private JButton okayButton = new JButton();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JEditorPane jEditorPane1 = new JEditorPane();
    private String helpfile = new String();
    public boolean isValid = false;
    String errorMsg;
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**
     *
     * @param filename String - URL to be displayed
     */
    public helpFrame(String filename) {
        try {
            helpfile = filename;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void jbInit() throws Exception {

        // Set up the window
        getContentPane().setLayout(borderLayout1);
        setSize(new Dimension(550, 300));
        this.getContentPane().setBackground(UIManager.getColor(
           "TextPane.selectionBackground"));
        jEditorPane1.setBorder(BorderFactory.createLoweredBevelBorder());
        jEditorPane1.setEditable(false);
        jEditorPane1.setMargin(new Insets(20, 20, 12, 20));
        jEditorPane1.setContentType("text/html");

        okayButton.addActionListener(new helpFrame_okayButton_actionAdapter(this));
        this.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        jPanel1.setLayout(gridBagLayout1);
        this.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jScrollPane1.getViewport().add(jEditorPane1);
        jPanel1.add(okayButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 249, 5, 250), 0, 0));
        okayButton.setText("OK");

        // Try to open up the file or URL.
        // set isValid = false if this fails.
        this.isValid = false;
        if (helpfile.startsWith("http://")) {
           URL testpage = new URL(helpfile);
           errorMsg = "Can't open connection to " + helpfile;
            try {
                //-------------------------------------------------------
                // Adapted from:
                //      http://www.rgagnon.com/javadetails/java-0059.html
                //-------------------------------------------------------
                //HttpURLConnection.setFollowRedirects(false);
                // note : you may also need
                //        HttpURLConnection.setInstanceFollowRedirects(false)
                HttpURLConnection con =
                   (HttpURLConnection) new URL(helpfile).openConnection();
                con.setRequestMethod("HEAD");

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    this.isValid=true;
                }
                else {
                    errorMsg = con.getResponseMessage() + ":  " + helpfile;
                }
           }
            catch (IOException ex) {
//                Toolkit.getDefaultToolkit().beep();
//                JOptionPane.showMessageDialog(this,errorMsg);
///                this.dispose();
//                System.out.println(ex);
                this.isValid = false;
                }
        }

        else {
            File F = new File(helpfile);
            this.isValid = F.exists();
            helpfile = "file://" + F.getAbsolutePath();
            errorMsg = "Can't open file: " + helpfile;
            }

        if (this.isValid) {
            jEditorPane1.setPage(helpfile);
        }
        else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this,errorMsg);
        }
    }

    public static void main(String[] args) {
//        helpFrame helpframe = new helpFrame();
    }


    public void okayButton_actionPerformed(ActionEvent e) {
           this.dispose();
    }
}


class helpFrame_okayButton_actionAdapter implements ActionListener {
    private helpFrame adaptee;
    helpFrame_okayButton_actionAdapter(helpFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {

        adaptee.okayButton_actionPerformed(e);
    }
}
