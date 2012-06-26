package birchconfig;

import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

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

public class BirchConfig {
    boolean packFrame = false;
    BirchProperties BP = new BirchProperties();
    /**
     * Construct and show the application.
     */
    public BirchConfig() {

        // Write startup messages to console
         System.out.println("Userid: " + System.getProperty("user.name"));
         System.out.println("Current working directory: "+ System.getProperty("user.dir"));

        MainFrame frame = new MainFrame(BP);
        // Validate frames that have preset sizes
        // Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }

        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Application entry point.
     *
     * @param args String[]
     */


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.
                                             getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                new BirchConfig();
            }
        });
    }

    private void jbInit() throws Exception {
    }

    /**
     * <p>Title: BIRCH Administration Tool</p>
     *
     * <p>Description: GUI for installing, updating and configurihg BIRCH</p>
     *
     * <p>Copyright: Copyright (c) 2005</p>
     *
     * <p>Company: University of Manitoba</p>
     * @author Dr. Brian Fristensky
     * @version 0.1
     */

}


