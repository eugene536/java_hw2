package swing.layout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import swing.common.TitledPanel;

public class FlowLayoutPane extends JPanel {
	private final FlowLayout layout = new FlowLayout();
	private final JPanel panel = new JPanel(layout); 
	
	public FlowLayoutPane() {
		super(new BorderLayout());
		
		add(createLayoutPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.PAGE_END);
	}
	
	private JComponent createLayoutPanel() {
		panel.add(new JButton("S"));
		panel.add(new JButton("Short"));
		panel.add(new JButton("Normal"));
		panel.add(new JButton("<- Long ->"));
		panel.add(new JButton("<- Very long ->"));

		return new TitledPanel("Layout panel", panel);
	}
	
	private JComponent createControlPanel() {
		ControlPanel controlPanel = new ControlPanel();
		
		final Map<String, Integer> alignMap = new HashMap<String, Integer>();
		alignMap.put("LEADING", FlowLayout.LEADING);
		alignMap.put("CENTER", FlowLayout.CENTER);
		alignMap.put("TRAILING", FlowLayout.TRAILING);
		
		final JComboBox align = new JComboBox(new String[]{"LEADING", "CENTER", "TRAILING"});
		align.setSelectedItem("CENTER");
		align.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				layout.setAlignment(alignMap.get(align.getSelectedItem()));
				panel.doLayout();
			}
		});
		
		final JSlider hgap = new JSlider(0, 20);
		hgap.setValue(layout.getHgap());
		hgap.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setHgap(hgap.getValue());
				panel.doLayout();
			}
		});
		setLabels(hgap);
		
		final JSlider vgap = new JSlider(0, 20);
		vgap.setValue(layout.getHgap());
		vgap.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setVgap(vgap.getValue());
				panel.doLayout();
			}
		});
		setLabels(vgap);
		
		controlPanel.add("align", 'A', align);
		controlPanel.add("hgap", 'H', hgap);
		controlPanel.add("vgap", 'V', vgap);
		
		return new TitledPanel("Control panel", controlPanel);
	}
	
	private void setLabels(JSlider slider) {
        slider.setLabelTable(slider.createStandardLabels(1));
        slider.setPaintLabels(true);
	}
}
