package swing.layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import swing.common.TitledPanel;

public class CardLayoutPane extends JPanel {
	private final CardLayout layout = new CardLayout();
	private final JPanel panel = new JPanel(layout); 
	
	public CardLayoutPane() {
		super(new BorderLayout());
		
		add(createLayoutPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.PAGE_END);
	}

	private JComponent createLayoutPanel() {
		panel.add(new JButton("Button 1"), "1");
		panel.add(new JButton("Button 2"), "2");
		panel.add(new JButton("Button 3"), "3");
		panel.add(new JButton("Button 4"), "4");
		panel.add(new JButton("Button 5"), "5");
		panel.add(new JButton("Button 6"), "6");
		
		return new TitledPanel("Layout panel", panel);
	}
	
	private JComponent createControlPanel() {
		ControlPanel controlPanel = new ControlPanel();
		
		final JSlider card = new JSlider(1, 6);
		card.setValue(layout.getHgap());
		card.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.show(panel, "" + card.getValue());
				panel.doLayout();
			}
		});
		controlPanel.add("card", 'C', card);
		setLabels(card);
		
		final JSlider hgap = new JSlider(0, 20);
		hgap.setValue(layout.getHgap());
		hgap.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setHgap(hgap.getValue());
				panel.doLayout();
			}
		});
		controlPanel.add("hgap", 'C', hgap);
		setLabels(hgap);
		
		final JSlider vgap = new JSlider(0, 20);
		vgap.setValue(layout.getHgap());
		vgap.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setVgap(vgap.getValue());
				panel.doLayout();
			}
		});
		controlPanel.add("vgap", 'V', vgap);
		setLabels(vgap);
		
		return new TitledPanel("Control panel", controlPanel);
	}
	
	private void setLabels(JSlider slider) {
        slider.setLabelTable(slider.createStandardLabels(1));
        slider.setPaintLabels(true);
	}
}
