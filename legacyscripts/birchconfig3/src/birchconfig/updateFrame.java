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
public class updateFrame extends JFrame {
    JPanel TitlePanel = new JPanel();
    JLabel TitleLabel = new JLabel();
    JPanel jPanel1 = new JPanel();
    JButton returnButton = new JButton("Return to Main Menu");
    JButton updateButton = new JButton("Update BIRCH");
    JButton helpButton = new JButton("Help");
    MainFrame frame;
    ButtonGroup buttonGroup1 = new ButtonGroup();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();


    public updateFrame(MainFrame tempframe) {
        try {
            frame = tempframe;
            jbInit(frame);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit(MainFrame frame) throws Exception {
        setSize(new Dimension(400, 300));
        setTitle("birchconfig");
        TitleLabel.setFont(new java.awt.Font("Monotype Century Schoolbook",
                                          Font.BOLD, 20));
        TitleLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        TitleLabel.setOpaque(true);
        TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TitleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        TitleLabel.setBackground(Color.white);
        getContentPane().setLayout(gridBagLayout3);
        this.getContentPane().setBackground(Color.orange);
        TitleLabel.setText("Update BIRCH");
        TitlePanel.setOpaque(false);
        TitlePanel.setLayout(gridBagLayout2);
        jPanel1.setLayout(gridBagLayout1);
        helpButton.setOpaque(true);
        helpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        returnButton.setHorizontalTextPosition(SwingConstants.CENTER);
        updateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        updateButton.addActionListener(new
        updateFrame_updateButton_actionAdapter(this));
        jPanel1.setOpaque(false);
        returnButton.setHorizontalAlignment(SwingConstants.CENTER);
        returnButton.setText("Return to Main Menu");
        returnButton.addActionListener(new
                updateFrame_returnButton_actionAdapter(this));
        updateButton.setText("Update BIRCH");
        helpButton.setHorizontalAlignment(SwingConstants.CENTER);
        helpButton.setText("Help");
        helpButton.addActionListener(new updateFrame_helpButton_actionAdapter(this));
        jPanel1.add(updateButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(9, 0, 0, 0), 10, 0));
        jPanel1.add(returnButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(9, 28, 0, 0), 10, 0));
        TitlePanel.add(TitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(11, 2, 25, 5), 230, 0));
        this.getContentPane().add(jPanel1,
                                  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(36, 4, 43, 9), -4, 0));
        this.getContentPane().add(TitlePanel,
                                  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 4, 0, 0), 0, 0));
        jPanel1.add(helpButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(9, 0, 0, 38), 10, 0));
    }

    public static void main(String[] args) {
//        updateFrame updateframe = new updateFrame(frame,BP);
    }

    public void helpButton_actionPerformed(ActionEvent e) {
        helpFrame Help = new helpFrame("birchconfig/help/update.help.html");
        Help.setVisible(true);
    }

    public void returnButton_actionPerformed(ActionEvent e) {
       this.dispose();
       frame.setVisible(true);
    }

    public void updateButton_actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().beep();
        String message = "Do you really want to update BIRCH?";
        int response = JOptionPane.showConfirmDialog(frame,message) ;
        if (response == JOptionPane.YES_OPTION) {

           // Do the installation using the BIRCH properties file
           // as input.
           //
           String propFN = "../local/admin/BIRCH.properties";
           String [] propargs = new String[] {propFN};
           setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
           install.main(propargs);
           setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           Toolkit.getDefaultToolkit().beep();
           String updatemessage = "BIRCH update complete.";
           JOptionPane.showMessageDialog(frame, updatemessage) ;

        }
        System.exit(0);

    }

    public void jPanel3_ancestorAdded(AncestorEvent event) {

    }

    public void UpdateButton_actionPerformed(ActionEvent e) {

    }
}


class updateFrame_UpdateButton_actionAdapter implements ActionListener {
    private updateFrame adaptee;
    updateFrame_UpdateButton_actionAdapter(updateFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.UpdateButton_actionPerformed(e);
    }
}


class updateFrame_updateButton_actionAdapter implements ActionListener {
    private updateFrame adaptee;
    updateFrame_updateButton_actionAdapter(updateFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {

        adaptee.updateButton_actionPerformed(e);
    }
}


class updateFrame_helpButton_actionAdapter implements ActionListener {
    private updateFrame adaptee;
    updateFrame_helpButton_actionAdapter(updateFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.helpButton_actionPerformed(e);
    }
}


class updateFrame_returnButton_actionAdapter implements ActionListener {
    private updateFrame adaptee;
    updateFrame_returnButton_actionAdapter(updateFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.returnButton_actionPerformed(e);
    }
}
