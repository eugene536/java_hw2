package swing.common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class Demo extends JFrame {
    public Demo(String title) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    protected JMenuBar createMainMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(swing.common.Toolkit.createLookAndFeelMenu(this, KeyEvent.VK_L));
        return menu;
    }

	public static JToolBar createToolBar(Action[] actions) {
	    JToolBar toolbar = new JToolBar();
	
	    for (Action action: actions) {
	        if (action != null) {
	            toolbar.add(action);
	        } else {
	            toolbar.addSeparator();
	        }
	    }
	
	    for (Component component : toolbar.getComponents()) {
	        component.setFocusable(false);
	    }
	
	    return toolbar;
	}
	
	public void setVisible(boolean value) {
		if (value) {
			pack();
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			Dimension demoSize = getSize();
			setLocation((screenSize.width - demoSize.width) / 2, (screenSize.height - demoSize.height) / 2);
		}
		super.setVisible(value);
	}

	public static JMenu createMenu(String title, int mnemonics, Action[] actions) {
	    JMenu menu = new JMenu(title);
	    menu.setMnemonic(KeyEvent.VK_F);
	
	    for (Action action: actions) {
	        if (action != null) {
	            menu.add(new JMenuItem(action));
	        } else {
	            menu.add(new JSeparator());
	        }
	    }
	
	    return menu;
	}
}

