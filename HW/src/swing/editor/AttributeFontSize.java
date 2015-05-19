package swing.editor;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.CaretEvent;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;


public class AttributeFontSize implements CaretAttributeListener {
	private final JComboBox comboBox = new JComboBox();
	
	public AttributeFontSize(JTextComponent text) {
		new CaretAttributeNotifier(text, StyleConstants.FontSize, this);

		comboBox.setRenderer(new BasicComboBoxRenderer(){
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				int size = (Integer) value;
				super.getListCellRendererComponent(list, size, index, isSelected, cellHasFocus);
				setFont(new Font(comboBox.getFont().getFamily(), 0, size));
				return this;
			}
			
		});

		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionEvent) {
				int size = (Integer) comboBox.getSelectedItem();
				new StyledEditorKit.FontSizeAction("" + size, size).actionPerformed(actionEvent);
			}
		});
		
		for (int i : new Integer[]{8, 9, 10, 12, 14, 16, 20, 24, 28, 36, 48, 72}) {
			comboBox.addItem(i);
		}
		comboBox.setPrototypeDisplayValue(72);
		comboBox.setSelectedItem(12);
	}

	public JComponent getToolbarComponent() {
		return comboBox;
	}

	public void caretUpdate(CaretEvent e, Object value) {
		if (value != null) {
			comboBox.setSelectedItem(value);
		} else {
			comboBox.setSelectedItem(8);
		}
	}
}
