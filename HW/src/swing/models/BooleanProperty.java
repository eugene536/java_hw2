package swing.models;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class BooleanProperty extends JCheckBox implements ChangeListener, ActionListener {
    private final Bean bean;

    public BooleanProperty(Object bean, String property) {
        super(property);
        this.bean = new Bean(bean, property);
        this.bean.addChangeListener(this);
        addActionListener(this);
    }

    public void stateChanged(ChangeEvent e) {
        setSelected((Boolean) bean.getValue());
    }

    public void actionPerformed(ActionEvent e) {
        bean.setValue(isSelected());
    }
}