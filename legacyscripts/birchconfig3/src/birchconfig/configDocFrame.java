package birchconfig;

import java.io.File;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * <p>Title: configDocFrame</p>
 *
 * <p>Description: GUI for uninstalling BIRCH directories</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: University of Manitoba</p>
 *
 * @author Dr. Brian Fristensky
 *
 */
public class configDocFrame extends JFrame {

    // MainFrame calls configDocFrame
    MainFrame frame;

    // BP - BirchProperties passed from MainFrame
    BirchProperties BP;

    // Candidate URLs
    URLholder birchURLcandidate;
    URLholder birchHomeURLcandidate;


    // Title Panel
    JPanel TitlePanel = new JPanel();
    JLabel TitleLabel = new JLabel("Change URLs in BIRCH documentation");

    ButtonGroup buttonGroup1 = new ButtonGroup();

    // Panel - Make Documentation Web Accessible
    JPanel WebPanel = new JPanel();
    JRadioButton WebButton = new JRadioButton("Make Documentation  Web Accessible");
    JTextField birchURLText = new JTextField();
    JLabel birchURLtitle = new JLabel("URL for BIRCH Web site");
    JButton birchURLButton = new JButton("URL Builder");
    JTextField birchHomeURLText = new JTextField();
    JLabel birchHomeURLtitle = new JLabel("URL for BIRCH Home Directory");
    JButton birchHomeURLButton = new JButton("URL Builder");
    JRadioButton LocalButton = new JRadioButton("Make documentation accessible only by local login");
    JPanel LocalPanel = new JPanel();
    JTextField LocalBirchURLText = new JTextField();
    JLabel LocalBirchURLLabel = new JLabel("URL for BIRCH Web Site");
    JLabel LocalBirchHomeURLLabel = new JLabel("URL for BIRCH Home Directory");
    JTextField LocalBirchHomeText = new JTextField();

    // Panel - Return, Apply, Help
    JPanel RetApplyHelpPanel = new JPanel();
    JButton returnButton = new JButton("Return to Main Menu");
    JButton ApplyButton = new JButton("Apply");
    JButton helpButton = new JButton("Help");
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    GridBagLayout gridBagLayout5 = new GridBagLayout();
    TitledBorder titledBorder1 = new TitledBorder("");
    Border border1 = BorderFactory.createBevelBorder(BevelBorder.RAISED,
            Color.white, Color.white, new Color(103, 101, 98),
            new Color(148, 145, 140));


    // ---------------------   CONSTRUCTOR ---------------------------------
    public configDocFrame(MainFrame tempframe, BirchProperties tempBP) {
        try {
            BP = tempBP;
            frame = tempframe;
            jbInit(frame, BP);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit(MainFrame frame, BirchProperties BP) throws Exception {
        // Read BIRCH.properties
        String FN = "../local/admin/BIRCH.properties";
        try {
            BP.loadProps(FN);
        }
        catch (Exception e) {
            System.out.println(e);
            Toolkit.getDefaultToolkit().beep();
            String message = "Can't load properties from " + FN;
            JOptionPane.showMessageDialog(frame, message);
            this.dispose();
        }
        // Create candidate URLs
        birchURLcandidate = new URLholder(BP);
        birchURLcandidate.URLtitle = "URL for BIRCH web site";
        birchURLcandidate.URLTextField = this.birchURLText;
        birchURLcandidate.makeURLstring();
        birchHomeURLcandidate = new URLholder(BP);
        birchHomeURLcandidate.URLtitle = "URL for BIRCH home directory";
        birchHomeURLcandidate.useridDir = birchHomeURLcandidate.useridDir + "/birchhomedir";
        birchHomeURLcandidate.URLTextField = this.birchHomeURLText;
        birchHomeURLcandidate.makeURLstring();
        this.getContentPane().setBackground(Color.orange);
        setTitle("birchconfig");
        setSize(600,575);
        getContentPane().setLayout(gridBagLayout5);
        TitlePanel.setBackground(Color.white);
        TitlePanel.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));

        // Title Pane
        TitlePanel.setOpaque(false);
        TitlePanel.setLayout(gridBagLayout3);
        TitleLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));
        TitleLabel.setForeground(Color.red);
        TitleLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        TitleLabel.setOpaque(true);
        TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TitleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        TitleLabel.setBackground(Color.white);
        WebButton.setSelected(true);
        birchURLText.setBackground(Color.white);
        birchHomeURLText.setBackground(Color.white);
        LocalPanel.setLayout(gridBagLayout1);

        LocalBirchHomeText.setBackground(Color.white);
        LocalBirchHomeText.setEditable(false);
        LocalBirchHomeText.setText("file://" + BP.homedir);
        LocalBirchURLText.setBackground(Color.white);
        LocalBirchURLText.setEditable(false);
        LocalBirchURLText.setText("file://" + BP.homedir + "/public_html");
        LocalPanel.setBackground(SystemColor.window);
        LocalPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        LocalButton.setBackground(SystemColor.window);
        LocalButton.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
        WebPanel.setBackground(SystemColor.window);
        WebPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        birchHomeURLButton.setHorizontalAlignment(SwingConstants.LEFT);
        birchURLButton.setHorizontalTextPosition(SwingConstants.CENTER);
        TitlePanel.add(TitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 53, 5));

        // Panel - Make Documentation Web Accessible
        WebButton.setBackground(SystemColor.window);
        WebButton.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        WebButton.setForeground(Color.black);
        WebButton.setOpaque(false);


	// birchURL
        birchURLText.setText(birchURLcandidate.URLstr);
        birchURLButton.addActionListener(new configDocFrame_birchURLButton_actionAdapter(this));
        birchURLButton.setSize(new Dimension(125, 100));
        birchURLButton.setAlignmentX((float) 0.0);
        birchURLButton.setAlignmentY((float) 0.5);
        birchURLButton.setMaximumSize(new Dimension(125, 36));
        birchURLButton.setPreferredSize(new Dimension(125, 36));
        birchURLButton.setToolTipText("Launch URL Builder"); // birchHomeURL
        birchHomeURLText.setText(birchHomeURLcandidate.URLstr);
        birchHomeURLButton.addActionListener(new configDocFrame_birchHomeURLButton_actionAdapter(this));
        birchHomeURLButton.setSize(new Dimension(125, 100));
        birchHomeURLButton.setAlignmentX((float) 0.0);
        birchHomeURLButton.setAlignmentY((float) 0.5);
        birchHomeURLButton.setMaximumSize(new Dimension(125, 36));
        birchHomeURLButton.setPreferredSize(new Dimension(125, 36));
        birchHomeURLButton.setToolTipText("Launch URL Builder");
        birchHomeURLButton.setHorizontalTextPosition(SwingConstants.CENTER);


        WebPanel.setLayout(gridBagLayout4);
        buttonGroup1.add(WebButton);
        buttonGroup1.add(LocalButton);
        RetApplyHelpPanel.setLayout(gridBagLayout2);
        RetApplyHelpPanel.setOpaque(false);
        returnButton.setHorizontalTextPosition(SwingConstants.CENTER);
        returnButton.setHorizontalAlignment(SwingConstants.CENTER);
        returnButton.setText("Return to Main Menu");
        returnButton.addActionListener(new
                                       configDocFrame_returnButton_actionAdapter(this));
        ApplyButton.setHorizontalTextPosition(SwingConstants.CENTER);
        ApplyButton.addActionListener(new
                                          configDocFrame_ApplyButton_actionAdapter(this));
        ApplyButton.setText("Apply");
        helpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        helpButton.setHorizontalAlignment(SwingConstants.CENTER);
        helpButton.setText("Help");
        helpButton.addActionListener(new
                                     configDocFrame_helpButton_actionAdapter(this));

        RetApplyHelpPanel.add(returnButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 10, 0));
        RetApplyHelpPanel.add(ApplyButton,
                    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 10, 0));
        RetApplyHelpPanel.add(helpButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 10, 0));
        LocalPanel.add(LocalBirchURLLabel,
                       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.NONE,
                                              new Insets(13, 156, 0, 254), 0, 8));
        LocalPanel.add(LocalBirchHomeURLLabel,
                       new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.NONE,
                                              new Insets(15, 156, 0, 202), 19,
                                              9));
        LocalPanel.add(LocalBirchURLText,
                       new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.HORIZONTAL,
                                              new Insets(0, 34, 0, 125), 394,
                                              12));
        LocalPanel.add(LocalBirchHomeText,
                       new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.HORIZONTAL,
                                              new Insets(0, 36, 21, 125), 393,
                                              15));
        WebPanel.add(birchURLtitle, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(8, 140, 0, 121), 47, 8));
        WebPanel.add(birchHomeURLtitle,
                     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(20, 140, 0, 90), 43, 8));
        WebPanel.add(birchURLText, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 30, 0, 19), 397, 12));
        WebPanel.add(birchHomeURLText,
                     new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(0, 30, 19, 21), 397, 12));
        WebPanel.add(birchHomeURLButton,
                     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.NONE,
                                            new Insets(0, 3, 13, 4), 3, 17));
        WebPanel.add(birchURLButton,
                     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.NONE,
                                            new Insets(0, 3, 13, 4), 3, 17));
        this.getContentPane().add(TitlePanel,
                                  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(6, 18, 0, 15), 251, 30));
        this.getContentPane().add(RetApplyHelpPanel,
                                  new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 17, 7, 11), 245, 23));
        this.getContentPane().add(WebPanel,
                                  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 18, 0, 14), 0, -6));
        LocalPanel.add(LocalButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(15, 4, 0, 127), 187, 0));
        WebPanel.add(WebButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(6, 3, 0, 19), 122, 0));
        this.getContentPane().add(LocalPanel,
                                  new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
                , GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets(12, 18, 0, 13), 0, 8));
    }

    // ==========================   MAIN (not used) ==========================
    public static void main(String[] args) {
//        configDocFrame configDocFrame = new configDocFrame(frame,BP);
    }



    // ---------------------------- birchURLButton -----------------------
    public void birchURLButton_actionPerformed(ActionEvent e) {
            // Open a urlBuilder window and disable parent window
            urlBuilder builder = new urlBuilder(this,BP,birchURLcandidate);
            builder.setVisible(true);
            this.setEnabled(false);
        }

    class configDocFrame_birchURLButton_actionAdapter implements ActionListener {
        private configDocFrame adaptee;
        configDocFrame_birchURLButton_actionAdapter(configDocFrame adaptee) {
            this.adaptee = adaptee;
        }
        public void actionPerformed(ActionEvent e) {
            adaptee.birchURLButton_actionPerformed(e);
            }
        }

    // ---------------------------- birchHomeURLButton -----------------------
    public void birchHomeURLButton_actionPerformed(ActionEvent e) {
            // Open a urlBuilder window and disable parent window
            urlBuilder builder = new urlBuilder(this,BP,birchHomeURLcandidate);
            builder.setVisible(true);
            this.setEnabled(false);
        }

    class configDocFrame_birchHomeURLButton_actionAdapter implements ActionListener {
        private configDocFrame adaptee;
        configDocFrame_birchHomeURLButton_actionAdapter(configDocFrame adaptee) {
            this.adaptee = adaptee;
            }

        public void actionPerformed(ActionEvent e) {
            adaptee.birchHomeURLButton_actionPerformed(e);
            }
      }

    // ---------------------------- returnButton -----------------------
    public void returnButton_actionPerformed(ActionEvent e) {
        this.dispose();
        frame.setVisible(true);
    }

    class configDocFrame_returnButton_actionAdapter implements ActionListener {
        private configDocFrame adaptee;
        configDocFrame_returnButton_actionAdapter(configDocFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.returnButton_actionPerformed(e);
        }
    }


    // ---------------------------- ApplyButton Button -----------------------

    /**
     * Create a temporary param file.
     */
     private void maketempParamFile(String FN, String [] urlLines) {
        BufferedTextOutputFile outfile = new BufferedTextOutputFile();
        if (outfile.WriteOkay(FN)) {
           System.out.println("Creating " + FN);
           try {
               outfile.PW.println(urlLines[0]);
               outfile.PW.println(urlLines[1]);
               outfile.PW.flush();
               outfile.FW.close();
           }
          catch (Exception e) {
               System.out.println(e);
          }
        }
    }

    public void ApplyButton_actionPerformed(ActionEvent e) {

        Toolkit.getDefaultToolkit().beep();
        String message = "Do you really want to change URLs in documentation?";
        int response = JOptionPane.showConfirmDialog(frame, message);
        if (response == JOptionPane.YES_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // create oldURLs.param, containing the current URLs
            String [] oldstrings = new String [2];
            oldstrings[0]= BP.birchURL;
            oldstrings[1]= BP.birchHomeURL;
            maketempParamFile("oldURLs.param",oldstrings);

            // create newURLs.param, containing the new URLs
            String [] newstrings = new String [2];
            if (WebButton.isSelected()) {
                newstrings[0] = birchURLText.getText();
                newstrings[1] = birchHomeURLText.getText();
            }
            else {
                newstrings[0] = LocalBirchURLText.getText();
                newstrings[1] = LocalBirchHomeText.getText();
            }
            maketempParamFile("newURLs.param",newstrings);

            // Run customdoc
            customdoc.main();

            // Delete the temporary files.
            File oldfile = new File("oldURLs.param");
            //oldfile.delete();
            File newfile = new File("newURLs.param");
            //newfile.delete();

            // Tell the user we're done
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            Toolkit.getDefaultToolkit().beep();
            String changemessage = "Changes complete.";
            JOptionPane.showMessageDialog(frame, changemessage);

        }
        System.exit(0);

    }

    class configDocFrame_ApplyButton_actionAdapter implements
            ActionListener {
        private configDocFrame adaptee;
        configDocFrame_ApplyButton_actionAdapter(configDocFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {

            adaptee.ApplyButton_actionPerformed(e);
        }
    }


    // ---------------------------- helpButton -----------------------

    public void helpButton_actionPerformed(ActionEvent e) {
        helpFrame Help = new helpFrame("birchconfig/help/BIRCHweb.help.html");
        Help.setVisible(true);
    }

    class configDocFrame_helpButton_actionAdapter implements ActionListener {
        private configDocFrame adaptee;
        configDocFrame_helpButton_actionAdapter(configDocFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.helpButton_actionPerformed(e);
        }
    }
}




