package swing.models;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import swing.common.Demo;
import swing.common.TitledPanel;

public class ButtonModelDemo extends Demo {
    private ButtonModel model = new JToggleButton.ToggleButtonModel();

    private JCheckBox rollover = new BooleanProperty(model, "rollover");
    private JCheckBox armed = new BooleanProperty(model, "armed");
    private JCheckBox pressed = new BooleanProperty(model, "pressed");
    private JCheckBox selected = new BooleanProperty(model, "selected");
    private JCheckBox enabled = new BooleanProperty(model, "enabled");

    public ButtonModelDemo() {
        super("ButtonModelDemo");

        add(createDemoPanel(), BorderLayout.PAGE_START);
        add(createMainPanel(), BorderLayout.CENTER);

        pack();
    }

    private Component createDemoPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton button = new JButton("Button");
        button.setModel(model);
        panel.add(button);


        JToggleButton toggleButton = new JToggleButton("Toggle button");
        toggleButton.setModel(model);
        panel.add(toggleButton);
        
        JCheckBox checkBox = new JCheckBox("Check box");
        checkBox.setModel(model);
        panel.add(checkBox);

        JRadioButton radioButton = new JRadioButton("Radio button");
        radioButton.setModel(model);
        panel.add(radioButton);

        return panel;
    }

    private Component createMainPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(armed);
        panel.add(enabled);
        panel.add(pressed);
        panel.add(rollover);
        panel.add(selected);

        return new TitledPanel("Control panel", panel);
    }

    public static void main(String[] args) {
        new ButtonModelDemo().setVisible(true);
    }
}

