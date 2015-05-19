package swing.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Toolkit {
    /** Toolkit is utility class. */
    private Toolkit() {}
    
    public static JMenu createLookAndFeelMenu(final JFrame frame, int mnemonic) {
        JMenu menu = new JMenu("Look & Feel");
        menu.setMnemonic(mnemonic);

        ButtonGroup group = new ButtonGroup();
        for (final UIManager.LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(                
                new AbstractAction(lafi.getName()){
                    public void actionPerformed(ActionEvent event) {
                        try {
                            UIManager.setLookAndFeel(lafi.getClassName());
                            SwingUtilities.updateComponentTreeUI(frame);
                            frame.pack();
                        } catch (Exception e) {
                            System.err.println("Cannot set look and feel: " + e.getMessage());
                        }
                    }
                }
            );
            group.add(item);
            menu.add(item);
        }

        return menu;
    }

    public static ImageIcon loadIcon(String path) {
        java.net.URL imageURL = BasicAction.class.getResource(path);
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        } else {
            return null;
        }
    }
}