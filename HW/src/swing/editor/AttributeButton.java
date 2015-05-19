package swing.editor;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.event.CaretEvent;
import javax.swing.text.JTextComponent;


public class AttributeButton implements CaretAttributeListener {
    private final Action action;
    private final ButtonModel toolbarModel = new JToggleButton.ToggleButtonModel();
    private final ButtonModel menuModel = new JToggleButton.ToggleButtonModel();
    private final Object value;
    
    public AttributeButton(Action action, JTextComponent text, Object key, Object value) {
        new CaretAttributeNotifier(text, key, this);
        this.action = action;
        this.value = value;
    }

    public JComponent getToolbarComponent() {
        JToggleButton button = new JToggleButton(action);
        button.setModel(toolbarModel);
        button.setText("");
        return button;
    }
    
    public JMenuItem getMenuItem() {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
        item.setModel(menuModel);
        return item;
    }

    public void caretUpdate(CaretEvent e, Object value) {       
        toolbarModel.setSelected(this.value.equals(value));
        menuModel.setSelected(this.value.equals(value));
    }
}
