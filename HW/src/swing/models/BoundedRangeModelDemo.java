package swing.models;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JSlider;

import swing.common.Demo;
import swing.common.TitledPanel;

public class BoundedRangeModelDemo extends Demo {
    private BoundedRangeModel model = new DefaultBoundedRangeModel();

    private JCheckBox valueIsAdjusting = new BooleanProperty(model, "valueIsAdjusting");
    private JComponent value   = new IntegerProperty(model, "value");
    private JComponent minimum = new IntegerProperty(model, "minimum");
    private JComponent maximum = new IntegerProperty(model, "maximum");
    private JComponent extent  = new IntegerProperty(model, "extent");

    public BoundedRangeModelDemo() {
        super("BoundedRangeModelDemo");

        add(createDemoPanel(), BorderLayout.PAGE_START);
        add(createMainPanel(), BorderLayout.CENTER);

        pack();

        model.setMinimum(0);
        model.setValue(50);
        model.setMaximum(100);
        model.setExtent(0);
    }

    private Component createDemoPanel() {
        Box panel = new Box(BoxLayout.PAGE_AXIS);

        panel.add(Box.createVerticalStrut(5));
        panel.add(new JSlider(model));
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JProgressBar(model));
        panel.add(Box.createVerticalStrut(5));

        JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        scrollBar.setModel(model);
        panel.add(scrollBar);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    private Component createMainPanel() {
        JPanel panel = new JPanel();

        panel.add(value);
        panel.add(minimum);
        panel.add(maximum);
        panel.add(extent);
        panel.add(valueIsAdjusting);

        return new TitledPanel("Control panel", panel);
    }

    public static void main(String[] args) {
        new BoundedRangeModelDemo().setVisible(true);
    }
}

