package birchconfig;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Title: BIRCH Administration Tool</p>
 *
 * <p>Description: GUI for installing, updating and configurihg BIRCH</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: University of Manitoba</p>
 *
 * @author Dr. Brian Fristensky
 * @version 0.1
 */
public class MainFrame extends JFrame {
    JPanel contentPane;
    JLabel statusBar = new JLabel();
    JLabel jLabel1 = new JLabel();
    TitledBorder titledBorder1 = new TitledBorder("");
    JButton InstallButton = new JButton();
    JButton UpdateButton = new JButton();
    JButton UninstallButton = new JButton();
    JPanel TitlePanel = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JPanel ButtonPanel = new JPanel();
    //VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    BirchProperties BP;
    JButton exitButton = new JButton();
    JButton configButton = new JButton();
    public MainFrame(BirchProperties TBP) {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // In order for BP to be passed as an object to
            // internal classes, it must
            // be declared in MainFrame (above). TBP is a
            // temporary handle that we can use to get BP
            // to point to the BP that was passed to us from
            // BirchConfig.
            BP = TBP;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        // There is an incompatability between Java 1.4.2 and Java1.5
        // in how setBackground works. In 1.5, it initially
        // sets the color, but then something changes it
        // to gray. 1.4.2 works correctly.
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        this.getContentPane().setBackground(Color.orange);
        setSize(new Dimension(450, 300));
        setTitle("birchconfig - BIRCH Admin Tool");
        statusBar.setText(" ");
        statusBar.setBounds(new Rectangle(0, 37, 3, 18));
        jLabel1.setBackground(Color.white);
        jLabel1.setFont(new java.awt.Font("Monotype Century Schoolbook",
                                          Font.BOLD, 20));
        jLabel1.setBorder(titledBorder1);
        jLabel1.setOpaque(true);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setIcon(null);
        jLabel1.setText("BIRCH Admin Tool");
        InstallButton.setText("New BIRCH Installation");
        InstallButton.addActionListener(new MainFrame_InstallButton_actionAdapter(this));
        UpdateButton.setText("Update an existing BIRCH installation");
        UpdateButton.addActionListener(new MainFrame_UpdateButton_actionAdapter(this));
        UninstallButton.setAction(null);
        UninstallButton.setText("Uninstall BIRCH");
        UninstallButton.addActionListener(new
                MainFrame_UninstallButton_actionAdapter(this));
        TitlePanel.setBorder(BorderFactory.createRaisedBevelBorder());
        TitlePanel.setBounds(new Rectangle(7, 13, 391, 35));
        TitlePanel.setLayout(gridLayout1);
        ButtonPanel.setBackground(Color.orange);
        ButtonPanel.setOpaque(false);
        ButtonPanel.setToolTipText("");
        ButtonPanel.setBounds(new Rectangle(6, 55, 389, 240));
        ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.PAGE_AXIS));
        exitButton.setText("Exit");
        exitButton.addActionListener(new MainFrame_exitButton_actionAdapter(this));
        configButton.setText("Set Web Location for Documentation");
        configButton.addActionListener(new MainFrame_configButton_actionAdapter(this));
        contentPane.add(statusBar, null);
        contentPane.add(TitlePanel);
        TitlePanel.add(jLabel1);
        contentPane.add(ButtonPanel);
	InstallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	configButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	UpdateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	UninstallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ButtonPanel.add(InstallButton);
        ButtonPanel.add(configButton);
        ButtonPanel.add(UpdateButton);
        ButtonPanel.add(UninstallButton);
        ButtonPanel.add(exitButton);
        contentPane.setBackground(Color.ORANGE);

    } // jbinit


    public void InstallButton_actionPerformed(ActionEvent e) {
           // Open an InstallFrame window and close main window
          InstallFrame InstF = new InstallFrame(this, BP);
          this.setVisible(false);
          InstF.setVisible(true);
    }


    class MainFrame_InstallButton_actionAdapter implements ActionListener {
        private MainFrame adaptee;
        MainFrame_InstallButton_actionAdapter(MainFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.InstallButton_actionPerformed(e);
        }
    }


    public void UninstallButton_actionPerformed(ActionEvent e) {
            // Open an uninstallFrame window and close main window
           uninstallFrame uninstall = new uninstallFrame(this,BP);
           uninstall.setVisible(true);
           this.setVisible(false);
    }

    public void UpdateButton_actionPerformed(ActionEvent e) {
        // Open an InstallFrame window and close main window
        updateFrame UF = new updateFrame(this);
        UF.setVisible(true);
        this.setVisible(false);
    }

    public void configButton_actionPerformed(ActionEvent e) {
       // Open a configDocFrame and close the main window
       configDocFrame CF = new configDocFrame(this,BP);
       CF.setVisible(true);
       this.setVisible(false);
    }

    public void exitButton_actionPerformed(ActionEvent e) {
        System.exit(0);
    }



}  // class MainFrame


class MainFrame_exitButton_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_exitButton_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.exitButton_actionPerformed(e);
    }
}


class MainFrame_configButton_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_configButton_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.configButton_actionPerformed(e);
    }
}


class MainFrame_UpdateButton_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_UpdateButton_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.UpdateButton_actionPerformed(e);
    }
}


class MainFrame_UninstallButton_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_UninstallButton_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.UninstallButton_actionPerformed(e);
    }
}

