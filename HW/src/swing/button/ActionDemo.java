package swing.button;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import swing.common.BasicAction;
import swing.common.Demo;
import swing.common.TitledPanel;
import swing.common.Toolkit;

public class ActionDemo extends Demo {
        private final Action action = new BasicAction(
                "BasicAction",
                "Perform BasicAction",
                "/toolbarButtonGraphics/general/Help24.gif",
                KeyEvent.VK_B, 
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)
        ) {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ActionDemo.this, "Action performed: " + getValue(NAME));
        }
        };
        
        private final Action[] actions = {action};
        
    public ActionDemo() {
        super("ActionDemo");
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK),
            "basicAction"
        );
        getRootPane().getActionMap().put("basicAction", action);

        setJMenuBar(createMainMenu());
        add(createToolBar(actions), BorderLayout.PAGE_START);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    public JMenuBar createMainMenu() {
        JMenuBar menu = new JMenuBar();

        menu.add(createMenu("Menu", KeyEvent.VK_M, actions));
        menu.add(Toolkit.createLookAndFeelMenu(this, KeyEvent.VK_L));

        return menu;
    }
    
    private Component createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createButtonPanel(), BorderLayout.PAGE_START);
        panel.add(createControlPanel(), BorderLayout.CENTER);
        return panel;
    }

        private Component createButtonPanel() {
        JPanel panel = new JPanel();
        
        panel.add(new JButton(action));
        
        return panel;
    }
    
    private void addWithLabel(JPanel panel, JComponent component, String text) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1, 5, 1, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel label = new JLabel(text);
        label.setLabelFor(component);           
        panel.add(label, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        panel.add(component, c);
        c.gridwidth = 1;
    }
    
    private Component createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        JTextField nameField = new JTextField();
        new StringActionListener(action, Action.NAME, nameField);
        addWithLabel(panel, nameField, "Name");
        
        JTextField shortDescriptionField = new JTextField("zzz");
        new StringActionListener(action, Action.SHORT_DESCRIPTION, shortDescriptionField);
        addWithLabel(panel, shortDescriptionField, "Short description");
        
        JTextField longDescriptionField = new JTextField();
        new StringActionListener(action, Action.LONG_DESCRIPTION, shortDescriptionField);
        addWithLabel(panel, longDescriptionField, "Long description");
        
        JCheckBox enabled = new JCheckBox();
        new EnabledActionListener(action, enabled);
        addWithLabel(panel, enabled, "Enabled");
        
                return new TitledPanel("Control panel", panel);
        }
    
    private static class StringActionListener extends FocusAdapter implements ActionListener, FocusListener {
        private final Action action;
        private final String key;
        private final JTextField text;
        
        public StringActionListener(Action action, String key, JTextField text) {
                this.action = action;
                this.key = key;
                this.text = text;
                text.setText((String) action.getValue(key));
                
                text.addActionListener(this);
                text.addFocusListener(this);
        }
        
                public void actionPerformed(ActionEvent e) {
                        action.putValue(key, text.getText());
                }

                public void focusLost(FocusEvent e) {
                        action.putValue(key, text.getText());
                }
    }
    
    private static class EnabledActionListener extends FocusAdapter implements ActionListener{
        private final Action action;
        private final AbstractButton button;
        
        public EnabledActionListener(Action action, AbstractButton button) {
                this.action = action;
                this.button = button;
                button.setSelected(action.isEnabled());
                button.addActionListener(this);
        }
        
                public void actionPerformed(ActionEvent e) {
                        action.setEnabled(button.isSelected());
                }
    }
    
    public static void main(String[] args) {
        new ActionDemo().setVisible(true);
    }
}

