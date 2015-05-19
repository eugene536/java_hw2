package swing.layout;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import swing.common.TitledPanel;

public class GridLayoutPane extends JPanel {
	private final GridLayout layout = new GridLayout();
	private final JPanel panel = new JPanel(layout); 
	
	public GridLayoutPane() {
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
		final JSlider rows = new JSlider(0, 6);
		rows.setValue(layout.getRows());
		rows.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setRows(rows.getValue());
				panel.doLayout();
			}
		});
		controlPanel.add("rows", 'R', rows);
		setLabels(rows);
		
		final JSlider columns = new JSlider(0, 6);
		columns.setValue(layout.getColumns());
		columns.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setColumns(columns.getValue());
				panel.doLayout();
			}
		});
		controlPanel.add("columns", 'C', columns);
		setLabels(columns);
		
		final JSlider hgap = new JSlider(0, 20);
		hgap.setValue(layout.getHgap());
		hgap.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				layout.setHgap(hgap.getValue());
				panel.doLayout();
			}
		});
		controlPanel.add("hgap", 'H', hgap);
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
