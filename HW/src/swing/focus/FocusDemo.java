package swing.focus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import swing.common.BasicAction;
import swing.common.Demo;
import swing.common.Toolkit;

public class FocusDemo extends Demo {
    private Action nextFocusAction = new BasicAction(
        "Focus next component",
        "Focus next component",
        "/toolbarButtonGraphics/navigation/Forward24.gif",
        KeyEvent.VK_N, 
        KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)
    ) {
        public void actionPerformed(ActionEvent e) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
        }
    };

    private Action previousFocusAction = new BasicAction(
        "Focus previous component",
        "Focus previous component",
        "/toolbarButtonGraphics/navigation/Back24.gif",
        KeyEvent.VK_P, 
        KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)
    ) {
        public void actionPerformed(ActionEvent e) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
        }
    };

    private Action upFocusAction = new BasicAction(
        "Up focus cycle",
        "Up focus cycle",
        "/toolbarButtonGraphics/navigation/Up24.gif",
        KeyEvent.VK_U, 
        KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)
    ) {
        public void actionPerformed(ActionEvent e) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().upFocusCycle();
        }
    };

    private Action downFocusAction = new BasicAction(
        "Down focus cycle",
        "Down focus cycle",
        "/toolbarButtonGraphics/navigation/Down24.gif",
        KeyEvent.VK_D, 
        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)
    ) {
        public void actionPerformed(ActionEvent e) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().downFocusCycle();
        }
    };

    private final Action[] focusActions = {
        previousFocusAction,
        nextFocusAction,
        null,
        upFocusAction,
        downFocusAction,
    };

    public FocusDemo() {
        super("FocusDemo");

        setJMenuBar(createMainMenu());
        add(createToolBar(focusActions), BorderLayout.PAGE_START);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    public JMenuBar createMainMenu() {
        JMenuBar menu = new JMenuBar();

        menu.add(createMenu("Focus", KeyEvent.VK_F, focusActions));
        menu.add(Toolkit.createLookAndFeelMenu(this, KeyEvent.VK_L));

        return menu;
    }

    private Component createMainPanel() {
        FocusPanel panel = new FocusPanel("Root panel", new BorderLayout());
        
        panel.add(createTestPanel(), BorderLayout.CENTER);
        panel.add(createDoublePanel(), BorderLayout.PAGE_END);
        
        return panel;
    }

    private Component createDoublePanel() {
        JPanel panel = new JPanel();
        new BoxLayout(panel, BoxLayout.LINE_AXIS);
        
        panel.add(createTestPanel("Left panel"));
        panel.add(createTestPanel("Right panel"));
        
        return panel;
    }

    private Component createTestPanel() {
        JPanel panel = new JPanel();

        panel.add(new JButton("Button 1"));
        panel.add(new JButton("Button 2"));
        panel.add(new JButton("Default button"));

        return panel;
    }

    private Component createTestPanel(String title) {
        FocusPanel panel = new FocusPanel(title, new BorderLayout());
        panel.add(createTestPanel(), BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        new FocusDemo().setVisible(true);
    }
}

