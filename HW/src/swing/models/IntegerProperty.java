package swing.models;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class IntegerProperty extends JPanel implements ChangeListener {
    private final Bean bean;
    private final JSpinner spinner;

    public IntegerProperty(Object bean, String property) {
        this.bean = new Bean(bean, property);
        this.bean.addChangeListener(this);
        
        spinner = new JSpinner();
        spinner.addChangeListener(this);

        add(new JLabel(property));
        add(spinner);
        spinner.setValue(1000);
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spinner) {
            bean.setValue(spinner.getValue());
        } else {
            spinner.setValue((Integer) bean.getValue());
        }
    }
}