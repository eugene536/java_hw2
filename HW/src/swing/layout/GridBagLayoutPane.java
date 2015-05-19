package swing.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import swing.common.TitledPanel;

public class GridBagLayoutPane extends JPanel implements ActionListener {
	private final GridBagLayout layout = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private final JPanel panel = new JPanel(layout); 
	
	private JComponent selected;
	private Border border;
	
    final Map<String, Integer> fillMap = new HashMap<String, Integer>();
    final JComboBox fill = new JComboBox(new String[]{"NONE", "HORIZONTAL", "VERTICAL", "BOTH"});
    
    final Map<String, Integer> anchorMap = new HashMap<String, Integer>();
    final JComboBox anchor = new JComboBox(new String[]{"CENTER", "PAGE_START", "PAGE_END", "LINE_START", "LINE_END", "FIRST_LINE_START", "FIRST_LINE_END", "LAST_LINE_START", "LAST_LINE_END"});
    
    final JSlider ipadx = new JSlider(0, 20);
    final JSlider ipady = new JSlider(0, 20);
    final JSlider gridx = new JSlider(-1, 10);
    final JSlider gridy = new JSlider(-1, 10);
    final JSlider gridw = new JSlider(1, 5);
    final JSlider gridh = new JSlider(1, 5);
    final JSlider weightx = new JSlider(0, 10);
    final JSlider weighty = new JSlider(0, 10);
    
	public GridBagLayoutPane() {
		super(new BorderLayout());
		
		add(createLayoutPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.PAGE_END);
		
	}

	private JComponent createLayoutPanel() {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        createButton("Button 1", c);
		
        c.gridwidth = GridBagConstraints.REMAINDER;
        createButton("Button 2", c);
        
        c.gridwidth = 1;
        c.gridheight = 2;
        createButton("Button 3", c);
        c.gridheight = 1;
        createButton("Button 4", c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        createButton("Button 5", c);
		
        c.gridwidth = 1;
        createButton("Button 6", c);
        createButton("Button 7", c);
		
		return new TitledPanel("Layout panel", panel);
	}
	
	private void createButton(String title, GridBagConstraints c) {
		JButton button = new JButton(title);
		button.addActionListener(this);
		panel.add(button, c);
		
		if (selected == null) {
			select(button);
		}
	}
	
	private void setLabels(JSlider slider) {
        slider.setLabelTable(slider.createStandardLabels(1));
        slider.setPaintLabels(true);
	}
	
	private JComponent createControlPanel() {
		ControlPanel controlPanel = new ControlPanel();
		
        fillMap.put("NONE", GridBagConstraints.NONE);
        fillMap.put("HORIZONTAL", GridBagConstraints.HORIZONTAL);
        fillMap.put("VERTICAL", GridBagConstraints.VERTICAL);
        fillMap.put("BOTH", GridBagConstraints.BOTH);
        
        fill.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	c.fill = fillMap.get(fill.getSelectedItem());
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
		
        anchorMap.put("CENTER", GridBagConstraints.CENTER);
        anchorMap.put("PAGE_START", GridBagConstraints.PAGE_START);
        anchorMap.put("PAGE_END", GridBagConstraints.PAGE_END);
        anchorMap.put("LINE_START", GridBagConstraints.LINE_START);
        anchorMap.put("LINE_END", GridBagConstraints.LINE_END);
        anchorMap.put("FIRST_LINE_START", GridBagConstraints.FIRST_LINE_START);
        anchorMap.put("FIRST_LINE_END", GridBagConstraints.FIRST_LINE_END);
        anchorMap.put("LAST_LINE_START", GridBagConstraints.LAST_LINE_START);
        anchorMap.put("LAST_LINE_END", GridBagConstraints.LAST_LINE_END);
        
        anchor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	c.anchor = anchorMap.get(anchor.getSelectedItem());
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
		
        ipadx.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.ipadx = ipadx.getValue();
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(ipadx);
		
        ipady.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.ipady = ipady.getValue();
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(ipady);
		
        gridx.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.gridx = gridx.getValue();
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(gridx);
		
        gridy.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.gridy = gridy.getValue();
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(gridy);
		
        gridw.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.gridwidth = gridw.getValue();
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(gridw);
		
        gridh.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.gridheight = gridh.getValue();
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(gridh);
        
        weightx.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.weightx = weightx.getValue() / 100.0;
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(weightx);
        
        weighty.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                c.weighty = weighty.getValue() / 100.0;
            	layout.setConstraints(selected, c);
                panel.doLayout();
            }
        });
        setLabels(weighty);
        
		controlPanel.add("fill", ' ', fill);
		controlPanel.add("anchor", ' ', anchor);
		controlPanel.add("ipadx", ' ', ipadx);
		controlPanel.add("ipady", ' ', ipady);
		controlPanel.add("gridx", ' ', gridx);
		controlPanel.add("gridy", ' ', gridy);
		controlPanel.add("gridwidth", ' ', gridw);
		controlPanel.add("gridheight", ' ', gridh);
		controlPanel.add("weightx", ' ', weightx);
		controlPanel.add("weighty", ' ', weighty);
		
		return new TitledPanel("Control panel", controlPanel);
	}
	
	private void updateSelection() {
		for (Map.Entry<String, Integer> entry : fillMap.entrySet()) {
			if (entry.getValue() == c.fill) {
				fill.setSelectedItem(entry.getKey());
			}
		}
		for (Map.Entry<String, Integer> entry : anchorMap.entrySet()) {
			if (entry.getValue() == c.anchor) {
				anchor.setSelectedItem(entry.getKey());
			}
		}
		ipadx.setValue(c.ipadx);
		ipady.setValue(c.ipady);
		gridx.setValue(c.gridx);
		gridy.setValue(c.gridy);
		gridw.setValue(c.gridwidth);
		gridh.setValue(c.gridheight);
		weightx.setValue((int) (c.weightx * 100));
		weighty.setValue((int) (c.weighty * 100));
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source instanceof JComponent) {
			select((JComponent) source);
		}
	}
	
	private void select(JComponent component) {
		if (selected != null) {
			selected.setBorder(border);
		}
		
		selected = component;
		c = layout.getConstraints(selected);
		border = selected.getBorder(); 
		selected.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 1),
				border
		));
		updateSelection();
	}
}
