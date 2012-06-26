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

/**
 * <p>Title: uninstallFrame</p>
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
public class uninstallFrame extends JFrame {

    // MainFrame calls uninstallFrame
    MainFrame frame;

    // BP - BirchProperties passed from MainFrame
    BirchProperties BP;

    // Title Pane
    JPanel TitlePanel = new JPanel();
    JLabel TitleLabel = new JLabel();

    // Unpdate/Permanent Uninstall toggle buttons
    JPanel UpdatePermPanel = new JPanel();
    JToggleButton UpdateButton = new JToggleButton("Uninstall prior to update");
    JToggleButton PermUninstallButton = new JToggleButton("Permanent uninstall");
    ButtonGroup buttonGroup1 = new ButtonGroup();

    // BinaryCheckBox
    JPanel CheckBoxPanel = new JPanel();
    JCheckBox BinaryCheckBox = new JCheckBox();

    // Return, Uninstall, Help Panel
    JPanel RetUnHelpPanel = new JPanel();
    JButton returnButton = new JButton("Return to Main Menu");
    JButton uninstallButton = new JButton("Uninstall BIRCH");
    JButton helpButton = new JButton("Help");


    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    GridBagLayout gridBagLayout5 = new GridBagLayout();

    // ---------------------   CONSTRUCTOR ---------------------------------
    public uninstallFrame(MainFrame tempframe, BirchProperties tempBP) {
        try {
            BP = tempBP;
            frame = tempframe;
            jbInit(frame, BP);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit(MainFrame frame, BirchProperties BP) throws Exception {

        // uninstallFrame
        setTitle("birchconfig");
        setSize(new Dimension(480, 300));
        getContentPane().setLayout(gridBagLayout5);
        this.getContentPane().setBackground(Color.orange);

        // Title Pane
        TitlePanel.setOpaque(false);
        TitlePanel.setLayout(gridBagLayout3);
        TitleLabel.setFont(new java.awt.Font("Monotype Century Schoolbook",
                                             Font.BOLD, 20));
        TitleLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        TitleLabel.setOpaque(true);
        TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TitleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        TitleLabel.setBackground(Color.white);
        TitleLabel.setText("Uninstall BIRCH");
        CheckBoxPanel.setLayout(gridBagLayout4);
        TitlePanel.add(TitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, -54, 0, 54), 250, 0));

        // UpdatePermPanel - Update/PermanentUninstall buttons
        UpdatePermPanel.setLayout(gridBagLayout1);
        UpdatePermPanel.setOpaque(false);
        UpdateButton.setOpaque(true);
        UpdateButton.setHorizontalAlignment(SwingConstants.CENTER);
        UpdateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        UpdateButton.addActionListener(new
                                       uninstallFrame_UpdateButton_actionAdapter(this));
        PermUninstallButton.setOpaque(true);
        PermUninstallButton.setHorizontalAlignment(SwingConstants.CENTER);
        PermUninstallButton.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonGroup1.add(UpdateButton);
        buttonGroup1.add(PermUninstallButton);
        UpdateButton.setSelected(true);
        UpdatePermPanel.add(UpdateButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        UpdatePermPanel.add(PermUninstallButton,
                    new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 0, 0));

        // BinaryCheckBox
        CheckBoxPanel.setOpaque(false);
        CheckBoxPanel.addAncestorListener(new uninstallFrame_CheckBoxPanel_ancestorAdapter(this));
        BinaryCheckBox.setBackground(Color.orange);
        BinaryCheckBox.setOpaque(false);
        BinaryCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        BinaryCheckBox.setSelected(true);
        BinaryCheckBox.setText("Uninstall binaries and libraries"); // Return, Uninstall, Help Panel
        RetUnHelpPanel.setLayout(gridBagLayout2);
        RetUnHelpPanel.setOpaque(false);

        returnButton.setHorizontalTextPosition(SwingConstants.CENTER);
        returnButton.setHorizontalAlignment(SwingConstants.CENTER);
        returnButton.setText("Return to Main Menu");
        returnButton.addActionListener(new
                                       uninstallFrame_returnButton_actionAdapter(this));

        uninstallButton.setHorizontalTextPosition(SwingConstants.CENTER);
        uninstallButton.addActionListener(new
                                          uninstallFrame_uninstallButton_actionAdapter(this));
        uninstallButton.setText("Uninstall BIRCH");

        helpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        helpButton.setHorizontalAlignment(SwingConstants.CENTER);
        helpButton.setText("Help");
        helpButton.addActionListener(new
                                     uninstallFrame_helpButton_actionAdapter(this));


        RetUnHelpPanel.add(returnButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 10, 0));
        RetUnHelpPanel.add(uninstallButton,
                    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 10, 0));
        RetUnHelpPanel.add(helpButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 10, 0));
        CheckBoxPanel.add(BinaryCheckBox,
                          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.NONE,
                                                 new Insets(5, 115, 26, 115), 0,
                                                 0));
        this.getContentPane().add(RetUnHelpPanel,
                                  new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(22, 4, 35, 20), 110, 26));
        this.getContentPane().add(CheckBoxPanel,
                                  new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(32, 14, 0, 54), 0, 0));
        this.getContentPane().add(UpdatePermPanel,
                                  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(7, 4, 0, 20), 165, 16));
        this.getContentPane().add(TitlePanel,
                                  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 4, 0, 20), 25, 18));
    }

    // ==========================   MAIN (not used) ==========================
    public static void main(String[] args) {
//        uninstallFrame uninstallframe = new uninstallFrame(frame,BP);
    }


    // ==========================   ACTIONS   ==========================

    // ---------------------------- UpdateButton -----------------------
    class uninstallFrame_UpdateButton_actionAdapter implements ActionListener {
        private uninstallFrame adaptee;
        uninstallFrame_UpdateButton_actionAdapter(uninstallFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.UpdateButton_actionPerformed(e);
        }
    }


    public void UpdateButton_actionPerformed(ActionEvent e) {

    }


    // ---------------------------- returnButton -----------------------
    public void returnButton_actionPerformed(ActionEvent e) {
        this.dispose();
        frame.setVisible(true);
    }

    class uninstallFrame_returnButton_actionAdapter implements ActionListener {
        private uninstallFrame adaptee;
        uninstallFrame_returnButton_actionAdapter(uninstallFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.returnButton_actionPerformed(e);
        }
    }


    // ---------------------------- uninstallButton Button -----------------------
    public void uninstallButton_actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().beep();
        String message = "Do you really REALLY want to uninstall BIRCH?";
        int response = JOptionPane.showConfirmDialog(frame, message);
        if (response == JOptionPane.YES_OPTION) {
            // Get parameters from menu pand
            boolean PermUninstall = PermUninstallButton.isSelected();
            boolean DelBinaries = BinaryCheckBox.isSelected();

            // Run uninstall.
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            uninstall.main(PermUninstall, DelBinaries);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            Toolkit.getDefaultToolkit().beep();
            String uninstallmessage = "Uninstall complete.";
            JOptionPane.showMessageDialog(frame, uninstallmessage);

        }
        System.exit(0);

    }

    class uninstallFrame_uninstallButton_actionAdapter implements
            ActionListener {
        private uninstallFrame adaptee;
        uninstallFrame_uninstallButton_actionAdapter(uninstallFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {

            adaptee.uninstallButton_actionPerformed(e);
        }
    }


    // ---------------------------- helpButton -----------------------

    public void helpButton_actionPerformed(ActionEvent e) {
        helpFrame Help = new helpFrame("birchconfig/help/unInstall.help.html");
        Help.setVisible(true);
    }

    class uninstallFrame_helpButton_actionAdapter implements ActionListener {
        private uninstallFrame adaptee;
        uninstallFrame_helpButton_actionAdapter(uninstallFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e) {
            adaptee.helpButton_actionPerformed(e);
        }
    }


    // ---------------------------- CheckBoxPanel -----------------------
    public void CheckBoxPanel_ancestorAdded(AncestorEvent event) {

    }


    class uninstallFrame_CheckBoxPanel_ancestorAdapter implements AncestorListener {
        private uninstallFrame adaptee;
        uninstallFrame_CheckBoxPanel_ancestorAdapter(uninstallFrame adaptee) {
            this.adaptee = adaptee;
        }

        public void ancestorAdded(AncestorEvent event) {
            adaptee.CheckBoxPanel_ancestorAdded(event);
        }

        public void ancestorRemoved(AncestorEvent event) {
        }

        public void ancestorMoved(AncestorEvent event) {
        }
    }
}





