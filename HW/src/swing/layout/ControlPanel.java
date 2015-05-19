package swing.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	private final GridBagConstraints c;
	
	public ControlPanel() {
		super(new GridBagLayout());
		
		c = new GridBagConstraints();
        c.insets = new Insets(1, 5, 1, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
	}
	
	public void add(String caption, char mnemonic, JComponent component) {
        JLabel label = new JLabel(caption);
        label.setLabelFor(component);
        label.setDisplayedMnemonic(mnemonic);
        
        c.weightx = 0;
        c.gridwidth = 1;
        add(label, c);
        
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(component, c);
	}
}
