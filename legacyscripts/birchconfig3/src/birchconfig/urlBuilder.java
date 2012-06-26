package birchconfig;

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
import java.net.InetAddress;
import java.net.Inet6Address;

/**
 * <p>Title: urlBuilder</p>
 *
 * <p>Description: GUI for building and testing URLs</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: University of Manitoba</p>
 *
 * @author Dr. Brian Fristensky
 *
 */
public class urlBuilder extends JFrame {

    // configDocFrame calls urlBuilder
    configDocFrame parentframe;

    // BP - BirchProperties passed from configDocFrame
    BirchProperties BP;

    // URL to build
    URLholder parentURL;
    URLholder testURL;

    // Title Panel
    JPanel TitlePanel = new JPanel();
    JLabel TitleLabel = new JLabel();
    JPanel builderPanel = new JPanel();
    JLabel birchURLtitle = new JLabel("URL to be tested");
    JLabel httpLabel = new JLabel();
    JLabel prefixLabel = new JLabel();
    JComboBox prefixComboBox = new JComboBox();
    JLabel dotLabel1 = new JLabel();
    JLabel secondaryLabel = new JLabel();
    JComboBox SecondLevelComboBox = new JComboBox();
    JLabel dotLabel2 = new JLabel();
    JLabel PrimaryLevelLabel = new JLabel();
    JLabel DomainLabel = new JLabel();
    JComboBox PrimaryLevelComboBox = new JComboBox();
    JLabel slashLabel = new JLabel();
    JComboBox TildaComboBox = new JComboBox();
    JLabel userDirLabel = new JLabel();
    JComboBox userDirComboBox = new JComboBox();
    JButton testURLButton = new JButton();

    // Panel - Return, Apply, Help
    JPanel RetApplyHelpPanel = new JPanel();
    JButton returnButton = new JButton("Return to Main Menu");
    JButton ApplyButton = new JButton("Apply");
    JButton helpButton = new JButton("Help");
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout4 = new GridBagLayout();


    // ---------------------   CONSTRUCTOR ---------------------------------
    /**
     *
     * @param tempframe configDocFrame
     * @param tempBP BirchProperties
     * @param tempURL URLholder
     */
    public urlBuilder(configDocFrame tempframe, BirchProperties tempBP, URLholder tempURL) {
        try {
            BP = tempBP;
            parentframe = tempframe;
            parentURL = tempURL;
            testURL = new URLholder();
            testURL.copyValues(parentURL);
            jbInit(parentframe, BP, testURL);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit(configDocFrame parentframe, BirchProperties BP, URLholder testURL) throws Exception {

        setTitle("birchconfig");
        setSize(750,300);
        getContentPane().setLayout(gridBagLayout4);
        this.getContentPane().setBackground(Color.orange);
        TitlePanel.setBackground(Color.white);

        // Title Panel
        TitlePanel.setOpaque(false);
        TitlePanel.setLayout(gridBagLayout3);
        TitleLabel.setFont(new java.awt.Font("Monotype Century Schoolbook",
                                             Font.BOLD, 20));
        TitleLabel.setForeground(Color.black);
        TitleLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        TitleLabel.setOpaque(true);
        TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TitleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        TitleLabel.setBackground(Color.white);
        TitleLabel.setText("URL Builder");
        RetApplyHelpPanel.setBackground(Color.orange);
        userDirComboBox.addActionListener(new
                urlBuilder_userDirComboBox_actionAdapter(this));
        prefixComboBox.addActionListener(new
                urlBuilder_prefixComboBox_actionAdapter(this));

    // Panel - Build URL --------------------------------------------
        birchURLtitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 16));
        birchURLtitle.setForeground(Color.blue);
        birchURLtitle.setHorizontalAlignment(SwingConstants.CENTER);
        birchURLtitle.setHorizontalTextPosition(SwingConstants.CENTER);
        birchURLtitle.setVerticalAlignment(SwingConstants.TOP);
        birchURLtitle.setText(testURL.URLtitle);
        builderPanel.setLayout(gridBagLayout1);
        builderPanel.setOpaque(false);
        builderPanel.setPreferredSize(new Dimension(700, 120));
        // http:
        httpLabel.setText("http://");
        // prefix
        prefixLabel.setFont(new java.awt.Font("Dialog", Font.ITALIC, 12));
        prefixLabel.setText("prefix");
        prefixComboBox.setEditable(true);
	prefixComboBox.addItem("");
        prefixComboBox.addItem("www");
        prefixComboBox.addItem("home");
        prefixComboBox.setBackground(Color.white);
        prefixComboBox.setMinimumSize(new Dimension(46, 26));
        prefixComboBox.setPreferredSize(new Dimension(46, 26));
        prefixComboBox.setSelectedItem(testURL.prefix);
        dotLabel1.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
        dotLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        dotLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        dotLabel1.setText(".");
        // Second Level Domain
        SecondLevelComboBox.setBackground(Color.white);
        SecondLevelComboBox.setEditable(true);
        SecondLevelComboBox.addItem("");
        SecondLevelComboBox.addItem(testURL.secondary);
        SecondLevelComboBox.setSelectedItem(testURL.secondary);
        InetAddress IPaddr = Inet6Address.getLocalHost();
        String hostName = IPaddr.getHostName();
        System.out.println("hostname from Ipaddr is [" + hostName + "]");
        // On some systems, the above doesn't work, so we try a
        // more complicated way to get the host name
        if (hostName.length() == 0) {
           runCommand runner = new runCommand();
           runner.runCommand("hostname > URLBUILDER_TEMP_NAME");
           BufferedTextInputFile tempfile = new BufferedTextInputFile();
           if (tempfile.OpenOkay("URLBUILDER_TEMP_NAME")) {
              hostName.equals(tempfile.nextLine());
           }
           tempfile.F.delete();
        }

        int rightdot = hostName.lastIndexOf(".");
        if (rightdot >= 0) {
            hostName = hostName.substring(0, rightdot);
           }
        if (hostName.startsWith("www.")) {
           int leftdot = hostName.indexOf(".");
           if (leftdot > 0) {
               hostName = hostName.substring(leftdot + 1);
              }

            }

        SecondLevelComboBox.addItem(hostName);
        String hostAddr = IPaddr.getHostAddress();
        SecondLevelComboBox.addItem(hostAddr); // dot
        dotLabel2.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
        dotLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        dotLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
        dotLabel2.setText("."); // Secondary Level Domain
        secondaryLabel.setFont(new java.awt.Font("Dialog", Font.ITALIC, 12));
        secondaryLabel.setText("2\u00B0 Level Domain");
        // Primary Level Domain
        PrimaryLevelLabel.setFont(new java.awt.Font("Dialog", Font.ITALIC, 12));
        PrimaryLevelLabel.setText("1\u00B0  Level");
        DomainLabel.setFont(new java.awt.Font("Dialog", Font.ITALIC, 12));
        DomainLabel.setText("Domain");
        PrimaryLevelComboBox.setBackground(Color.white);
        PrimaryLevelComboBox.setMinimumSize(new Dimension(25, 26));
        PrimaryLevelComboBox.setPreferredSize(new Dimension(28, 26));
        PrimaryLevelComboBox.setEditable(true);
        PrimaryLevelComboBox.addItem("");
        PrimaryLevelComboBox.addItem("ca");
        PrimaryLevelComboBox.addItem("com");
        PrimaryLevelComboBox.addItem("edu");
        PrimaryLevelComboBox.addItem("gov");
        PrimaryLevelComboBox.addItem("net");
        PrimaryLevelComboBox.addItem("org");
        PrimaryLevelComboBox.setSelectedItem(testURL.primary);
        // Right slash
        slashLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
        slashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        slashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        slashLabel.setText("/");
        // Tilda
        TildaComboBox.setBackground(Color.white);
        TildaComboBox.setToolTipText("Preceed userid with a tilda");
        TildaComboBox.addItem("");
        TildaComboBox.addItem("~");
        TildaComboBox.setSelectedItem(testURL.tilda);
        // user or directory
        userDirLabel.setFont(new java.awt.Font("Dialog", Font.ITALIC, 12));
        userDirLabel.setText("userid or directory");
        userDirComboBox.setBackground(Color.white);
        userDirComboBox.setEditable(true);
        // An alternative way to find the BIRCH home directory is to
        // assume that it is the parent of the current directory, which
        // is assumed to be install-birch.
        String PWD = System.getProperty("user.dir");
//        System.out.println("PWD: " + PWD);
        String parentDir = new String();

        int I = PWD.indexOf("/install-birch");
        if (I > -1) {
            parentDir = PWD.substring(0,I);
//            System.out.println("parentDir: " + parentDir);
            int J = parentDir.lastIndexOf("/");
            parentDir = parentDir.substring(J+1);
        }
//        System.out.println("parentDir: " + parentDir);
        if (testURL.URLtitle.equals("URL for BIRCH web site")) {
            // suggested choices for BIRCH Web site
            userDirComboBox.addItem("");
            userDirComboBox.addItem(BP.adminUserid);
            userDirComboBox.addItem("birch");
            userDirComboBox.addItem(parentDir + "/" + "public_html");
            userDirComboBox.addItem(BP.adminUserid + "/" + parentDir + "/" + "public_html");
           }
        else {
            // suggested choices for BIRCH Home Directory
            userDirComboBox.addItem("");
            userDirComboBox.addItem(BP.adminUserid + "/" + "birchhomedir");
            userDirComboBox.addItem("birch" + "/" + "birchhomedir");
            userDirComboBox.addItem(BP.adminUserid);
            userDirComboBox.addItem("birch");
            userDirComboBox.addItem(parentDir);
            userDirComboBox.addItem(BP.adminUserid + "/" + parentDir);
           }

        userDirComboBox.setSelectedItem(testURL.useridDir);

        // testURLButton
        testURLButton.addActionListener(new urlBuilder_testURLButton_actionAdapter(this));
        testURLButton.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
        testURLButton.setSize(new Dimension(125, 100));
        testURLButton.setAlignmentX((float) 0.0);
        testURLButton.setAlignmentY((float) 0.5);
        testURLButton.setMaximumSize(new Dimension(125, 36));
        testURLButton.setPreferredSize(new Dimension(125, 36));
        testURLButton.setToolTipText("open URL in browser");
        testURLButton.setHorizontalTextPosition(SwingConstants.CENTER);
        testURLButton.setText("TEST");
        RetApplyHelpPanel.setLayout(gridBagLayout2);
        RetApplyHelpPanel.setOpaque(false);
        returnButton.setHorizontalTextPosition(SwingConstants.CENTER);
        returnButton.setHorizontalAlignment(SwingConstants.CENTER);
        returnButton.setText("Return");
        returnButton.addActionListener(new
                                       urlBuilder_returnButton_actionAdapter(this));
        ApplyButton.setHorizontalTextPosition(SwingConstants.CENTER);
        ApplyButton.addActionListener(new
                                          urlBuilder_ApplyButton_actionAdapter(this));
        ApplyButton.setText("Apply");
        helpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        helpButton.setHorizontalAlignment(SwingConstants.CENTER);
        helpButton.setText("Help");
        helpButton.addActionListener(new
                                     urlBuilder_helpButton_actionAdapter(this));

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
        TitlePanel.add(TitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(13, 0, 0, 0), 53, 23));
        this.getContentPane().add(builderPanel,
                                  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(12, 7, 0, 8), 23, 0));
        this.getContentPane().add(TitlePanel,
                                  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(6, 7, 0, 54), 617, 17));
        this.getContentPane().add(RetApplyHelpPanel,
                                  new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 61, 7, 99), 283, 26));

        builderPanel.add(birchURLtitle,
                         new GridBagConstraints(3, 0, 6, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(10, 116, 0, 153),
                                                157, 10));
        builderPanel.add(testURLButton,
                         new GridBagConstraints(9, 2, 1, 2, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(8, 3, 3, 3), 16, 17));
        builderPanel.add(dotLabel1, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(8, 1, 15, 1), 1, 17));
        builderPanel.add(httpLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(11, 1, 15, 1), 1, 14));
        builderPanel.add(dotLabel2, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(9, 1, 15, 1), 3, 16));
        builderPanel.add(slashLabel,
                         new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(9, 0, 17, 2), 1, 14));
        builderPanel.add(TildaComboBox,
                         new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 2, 9, 2), 5, 1));
        builderPanel.add(userDirComboBox,
                         new GridBagConstraints(8, 3, 1, 1, 2.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 9, 7), 60, 0));
        builderPanel.add(userDirLabel,
                         new GridBagConstraints(8, 2, 1, 1, 1.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 0, 14), 100,
                                                18));
        builderPanel.add(secondaryLabel,
                         new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 7, 0, 20), 60, 19));
        builderPanel.add(PrimaryLevelLabel,
                         new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 0, 0), 0, 0));
        builderPanel.add(SecondLevelComboBox,
                         new GridBagConstraints(3, 3, 1, 1, 1.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 9, 2), 45, 1));
        builderPanel.add(DomainLabel,
                         new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 0, 0), 9, 1));
        builderPanel.add(prefixLabel,
                         new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 0, 8), 0, 0));
        builderPanel.add(PrimaryLevelComboBox,
                         new GridBagConstraints(5, 3, 1, 1, 0.5, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 9, 1), 25, 0));
        builderPanel.add(prefixComboBox,
                         new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 9, 0), 25, 0));
    }

    // ==========================   MAIN (not used) ==========================
    public static void main(String[] args) {
//        urlBuilder urlBuilder = new urlBuilder(parentframe,BP);
    }

    /** Read the URL components from UH and set the JTextField of UH
     *  to the new URL
     @param UH URLholder */
    private void setURLTextField (URLholder UH) {
        UH.prefix = (String) prefixComboBox.getSelectedItem();
        UH.secondary = (String) SecondLevelComboBox.getSelectedItem();
        UH.primary = (String) PrimaryLevelComboBox.getSelectedItem();
        UH.tilda = (String) TildaComboBox.getSelectedItem();
        UH.useridDir = (String) userDirComboBox.getSelectedItem();
        UH.makeURLstring();
        UH.URLTextField.setText(testURL.URLstr);
     }
    // ---------------------------- testURLButton -----------------------

        public void testURLButton_actionPerformed(ActionEvent e) {
            setURLTextField(testURL);
            testURL.makeURLstring();

            /** For the BIRCH Home directory, add "index.html"
            * to the path so that the help frame has a file
            * as its target, rather than a directory. At some sites,
            * Apache will not let you look at the directory itself, but
            * will allow you to look at specifified files.
            *
            */
           helpFrame Webpage;
           if (testURL.URLtitle.equals("URL for BIRCH web site")) {
               Webpage = new helpFrame(testURL.URLstr + "/birchconfig.web.target.html");
              }
           else {
               Webpage = new helpFrame(testURL.URLstr + "/birchconfig.homedir.target.html");
           }

            testURL.isValid = Webpage.isValid;
            if (testURL.isValid) {
               Webpage.setVisible(true);
            }
    }

    class urlBuilder_testURLButton_actionAdapter implements ActionListener {
        private urlBuilder adaptee;
        urlBuilder_testURLButton_actionAdapter(urlBuilder adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {

            adaptee.testURLButton_actionPerformed(e);
        }
}
    // ---------------------------- returnButton -----------------------
    public void returnButton_actionPerformed(ActionEvent e) {

        this.dispose();
        parentframe.setEnabled(true);
    }

    class urlBuilder_returnButton_actionAdapter implements ActionListener {
        private urlBuilder adaptee;
        urlBuilder_returnButton_actionAdapter(urlBuilder adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.returnButton_actionPerformed(e);
        }
    }


    // ---------------------------- ApplyButton Button -----------------------
    public void ApplyButton_actionPerformed(ActionEvent e) {

        // First we need to verify that none of the fields of testURL
        // have been changed without testing.
        boolean hasBeenTested;
        String currentURLString = testURL.URLstr;
        setURLTextField(testURL);
        testURL.makeURLstring();
        hasBeenTested = currentURLString.equals(testURL.URLstr);

        if (hasBeenTested) {
            if (testURL.isValid) {
                parentURL.copyValues(testURL);
                setURLTextField(parentURL);
            }
            else {
                  Toolkit.getDefaultToolkit().beep();
                  String message = "URL is not valid";
                  JOptionPane.showMessageDialog(this, message);
                 }
        }
       else {
             Toolkit.getDefaultToolkit().beep();
             String message = "URL must be tested before it can be applied.";
             JOptionPane.showMessageDialog(this, message);
            }

    }

    class urlBuilder_ApplyButton_actionAdapter implements
            ActionListener {
        private urlBuilder adaptee;
        urlBuilder_ApplyButton_actionAdapter(urlBuilder adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {

            adaptee.ApplyButton_actionPerformed(e);
        }
    }


    // ---------------------------- helpButton -----------------------

    public void helpButton_actionPerformed(ActionEvent e) {
        helpFrame Help = new helpFrame("birchconfig/help/URLbuilder.help.html");
        Help.setVisible(true);
    }

    public void userDirComboBox_actionPerformed(ActionEvent e) {

    }

    public void prefixComboBox_actionPerformed(ActionEvent e) {

    }

    class urlBuilder_helpButton_actionAdapter implements ActionListener {
        private urlBuilder adaptee;
        urlBuilder_helpButton_actionAdapter(urlBuilder adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.helpButton_actionPerformed(e);
        }
    }
}


class urlBuilder_prefixComboBox_actionAdapter implements ActionListener {
    private urlBuilder adaptee;
    urlBuilder_prefixComboBox_actionAdapter(urlBuilder adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.prefixComboBox_actionPerformed(e);
    }
}


class urlBuilder_userDirComboBox_actionAdapter implements ActionListener {
    private urlBuilder adaptee;
    urlBuilder_userDirComboBox_actionAdapter(urlBuilder adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.userDirComboBox_actionPerformed(e);
    }
}

