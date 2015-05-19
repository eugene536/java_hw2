package swing.layout;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;

import swing.common.BasicAction;
import swing.common.Demo;
import swing.common.Toolkit;

/**
 * Shows different layouts.
 * 
 * @author Georgiy Korneev
 */
public class LayoutDemo extends Demo {
	/**
	 * Creates a new demo.
	 */
    public LayoutDemo() {
        super("LayoutDemo");
        
        add(createTabbedPane());
        setJMenuBar(createMainMenu());
        
        setResizable(true);
    }
    
    /** Switches container to left-to-right mode. */
    private Action ltAction = new BasicAction(
            "Left-to-right",
            "Left-to-right, top-to-bottom",
            "/swing/images/ltor.gif",
            0, 
            null
    ) {
        public void actionPerformed(ActionEvent e) {
            applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
    };
    
    /** Switches container to right-to-left mode. */
    private Action rtAction = new BasicAction(
            "Right-to-left",
            "Right-to-left, top-to-bottom",
            "/swing/images/rtol.gif",
            0, 
            null
    ) {
        public void actionPerformed(ActionEvent e) {
            applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
    };
    
    protected JMenuBar createMainMenu() {
        JMenuBar menu = new JMenuBar();
        
        menu.add(createOrientationMenu());
        menu.add(Toolkit.createLookAndFeelMenu(this, KeyEvent.VK_L));
        
        return menu;
    }

    private JMenu createOrientationMenu() {
        JMenu menu = new JMenu("Orientaion");
        menu.setMnemonic('O');
        
        ButtonGroup group = new ButtonGroup();
        
        JRadioButtonMenuItem ltButton = new JRadioButtonMenuItem(ltAction);
        group.add(ltButton);
        menu.add(ltButton);
        ltButton.setSelected(true);
        
        JRadioButtonMenuItem rtButton = new JRadioButtonMenuItem(rtAction);
        group.add(rtButton);
        menu.add(rtButton);
        
        return menu;
    }

    private JComponent createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.add("GridBagLayout", new GridBagLayoutPane());
        
        tabbedPane.add("FlowLayout", new FlowLayoutPane());
        tabbedPane.add("BorderLayout", new BorderLayoutPane());
        tabbedPane.add("GridLayout", new GridLayoutPane());
        tabbedPane.add("CardLayout", new CardLayoutPane());
        tabbedPane.add("BoxLayout", new BoxLayoutPane());
        
        return tabbedPane;
    }


    public static void main(String[] args) {
        new LayoutDemo().setVisible(true);
    }
}
