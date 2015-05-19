package swing.button;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import swing.common.Demo;
import swing.common.Toolkit;

public class ButtonDemo extends Demo {
    public ButtonDemo() {
        super("ButtonDemo");
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        setJMenuBar(createMainMenu());
        add(createButtonPanel());
        add(createToggleButtonPanel());
        add(createCheckBoxPanel());
        add(createRadioButtonPanel());
        add(createButtonGroupPanel());
        add(createShortcutPanel());
    }

    private JPanel createTitledPanel(JPanel panel, String title) {
        JPanel result = new JPanel(new BorderLayout());
        result.add(panel);
        result.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            title
        ));
        return result;
    }

    private JComponent createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton ordinaryButton = new JButton("Ordinary button");
        JButton disabledButton = new JButton("Disabled button");
        JButton pressedButton = new JButton("Pressed button");
        JButton defaultButton = new JButton("Default button");
        ordinaryButton.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK),
            "gotoText"
        );
        ordinaryButton.getActionMap().setParent(getRootPane().getActionMap());
        disabledButton.setEnabled(false);

        ImageIcon icon = Toolkit.loadIcon("/toolbarButtonGraphics/general/Help16.gif"); 
        JButton leftIconButton = new JButton("Left icon button", icon);
        JButton rightIconButton = new JButton("Right icon button", icon);
        JButton topIconButton = new JButton("Top icon button", icon);
        rightIconButton.setHorizontalTextPosition(JButton.LEFT);
        topIconButton.setVerticalTextPosition(JButton.BOTTOM);
        topIconButton.setHorizontalTextPosition(AbstractButton.CENTER);

        panel.add(ordinaryButton);
        panel.add(disabledButton);
        panel.add(pressedButton);
        panel.add(defaultButton);

        panel.add(leftIconButton);
        panel.add(rightIconButton);
        panel.add(topIconButton);

        getRootPane().setDefaultButton(defaultButton);

        return createTitledPanel(panel, "JButton");
    }

    private JComponent createToggleButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JToggleButton ordinaryToggleButton = new JToggleButton("Ordinary toggle button");
        JToggleButton selectedToggleButton = new JToggleButton("Selected toggle button", true);
        JToggleButton disabledToggleButton = new JToggleButton("Disabled toggle button");
        JToggleButton pressedToggleButton = new JToggleButton("Pressed toggle button");
        disabledToggleButton.setEnabled(false);

        panel.add(ordinaryToggleButton);
        panel.add(selectedToggleButton);
        panel.add(disabledToggleButton);
        panel.add(pressedToggleButton);

        return createTitledPanel(panel, "JToggleButton");
    }
    
    private JComponent createCheckBoxPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JCheckBox ordinaryCheckBox = new JCheckBox("Ordinary check box");
        JCheckBox selectedCheckBox = new JCheckBox("Selected check box", true);
        JCheckBox disabledCheckBox = new JCheckBox("Disabled check box");
        JCheckBox pressedCheckBox = new JCheckBox("Pressed check box");
        disabledCheckBox.setEnabled(false);

        panel.add(ordinaryCheckBox);
        panel.add(selectedCheckBox);
        panel.add(disabledCheckBox);
        panel.add(pressedCheckBox);

        return createTitledPanel(panel, "JCheckBox");
    }

    private JComponent createRadioButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JRadioButton ordinaryRadioButton = new JRadioButton("Ordinary radio button");
        JRadioButton selectedRadioButton = new JRadioButton("Selected radio button", true);
        JRadioButton disabledRadioButton = new JRadioButton("Disabled radio button");
        JRadioButton pressedRadioButton = new JRadioButton("Pressed radio button");
        disabledRadioButton.setEnabled(false);

        panel.add(ordinaryRadioButton);
        panel.add(selectedRadioButton);
        panel.add(disabledRadioButton);
        panel.add(pressedRadioButton);

        return createTitledPanel(panel, "JRadioButton");
    }


    private JComponent createButtonGroupPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        ButtonGroup group = new ButtonGroup();

        JCheckBox checkBox = new JCheckBox("Check Box");
        JRadioButton radioButton = new JRadioButton("Radio button");
        JToggleButton toggleButton = new JToggleButton("Toggle button");
        JButton button = new JButton("Button");

        panel.add(checkBox);
        panel.add(radioButton);
        panel.add(toggleButton);
        panel.add(button);

        group.add(checkBox);
        group.add(radioButton);
        group.add(toggleButton);
        group.add(button);

        return createTitledPanel(panel, "ButtonGroup");
    }

    private JComponent createShortcutPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton leftButton = new JButton("Left button");
        leftButton.setMnemonic(KeyEvent.VK_L);

        JButton middleButton = new JButton("Middle button");
        middleButton.setMnemonic(KeyEvent.VK_B);

        JButton rightButton = new JButton("Right button");
        rightButton.setMnemonic(KeyEvent.VK_N);

        panel.add(leftButton);
        panel.add(middleButton);
        panel.add(rightButton);

        JTextField textField = new JTextField("Sample text");
        JLabel fieldLabel = new JLabel("Text field");
        fieldLabel.setLabelFor(textField);
        fieldLabel.setDisplayedMnemonic(KeyEvent.VK_T);
        panel.add(fieldLabel);
        panel.add(textField);

        return createTitledPanel(panel, "Accelerators");
    }

    public static void main(String[] args) {
        new ButtonDemo().setVisible(true);
    }
}

