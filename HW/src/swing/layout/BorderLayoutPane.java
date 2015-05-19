package swing.layout;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import swing.common.TitledPanel;

public class BorderLayoutPane extends JPanel {
    private final BorderLayout layout = new BorderLayout();
    private final JPanel panel = new JPanel(layout); 
    
    public BorderLayoutPane() {
        super(new BorderLayout());
        
        add(createLayoutPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.PAGE_END);
    }

    private JComponent createLayoutPanel() {
        panel.add(new JButton("PAGE_START"), BorderLayout.PAGE_START);
        panel.add(new JButton("PAGE_END"), BorderLayout.PAGE_END);
        panel.add(new JButton("LINE_START"), BorderLayout.LINE_START);
        panel.add(new JButton("LINE_END"), BorderLayout.LINE_END);
        panel.add(new JButton("CENTER"), BorderLayout.CENTER);
        
        return new TitledPanel("Layout panel", panel);
    }
    
    private JComponent createControlPanel() {
        ControlPanel controlPanel = new ControlPanel();
        
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
        
        controlPanel.add("hgap", 'H', hgap);
        controlPanel.add("vgap", 'V', vgap);
        
        return new TitledPanel("Control panel", controlPanel);
    }
    
    private void setLabels(JSlider slider) {
        slider.setLabelTable(slider.createStandardLabels(1));
        slider.setPaintLabels(true);
    }
}
