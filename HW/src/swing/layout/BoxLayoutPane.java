package swing.layout;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import swing.common.TitledPanel;

public class BoxLayoutPane extends JPanel {
	private final JPanel panel = new JPanel(); 
	private final BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
	
	public BoxLayoutPane() {
		super(new BorderLayout());
	
		panel.setLayout(layout);
		add(createLayoutPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.PAGE_END);
	}

	private JComponent createLayoutPanel() {
		panel.add(new JButton("Button 1"));
		panel.add(new JButton("Button 2"));
		panel.add(new JButton("Button 3"));
		panel.add(new JButton("Long Button"));
		panel.add(new JButton("<html>Multiline<br>Button"));

		return new TitledPanel("Layout panel", panel);
	}
	
	private JComponent createControlPanel() {
		ControlPanel controlPanel = new ControlPanel();
		
		return new TitledPanel("Control panel", controlPanel);
	}
}
