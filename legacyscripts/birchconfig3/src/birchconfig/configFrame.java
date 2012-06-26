package birchconfig;

import javax.swing.JPanel;
import java.awt.Color;
//import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;

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
public class configFrame extends JFrame {
    public configFrame() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    public void jbInit() throws Exception {
        getContentPane().setLayout(flowLayout1);
        jPanel1.setBackground(Color.orange);
        jPanel1.setEnabled(true);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(borderLayout1);
        jPanel2.setBackground(Color.lightGray);
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setLayout(flowLayout1);
        jButton1.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton1.setText("Cancel");
        jButton2.setText("PerformAction");
        jButton3.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton3.setText("Help");
        jLabel1.setFont(new java.awt.Font("Dialog", Font.BOLD, 16));
        jLabel1.setText("BIRCH");
        jPanel3.setBackground(Color.white);
        jPanel4.setBackground(Color.orange);
        this.getContentPane().setLayout(xYLayout1);
        jPanel2.add(jButton1, null);
        jPanel2.add(jButton2, null);
        jPanel2.add(jButton3, null);
        jPanel3.add(jLabel1);
        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);
        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);
        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);
    }

    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    JLabel jLabel1 = new JLabel();
    JPanel jPanel4 = new JPanel();
    // IMPORT!!!
    //LayoutManager xYLayout1 = new XYLayout();
    LayoutManager xYLayout1 = new GridLayout();
    FlowLayout flowLayout1 = new FlowLayout();
}
