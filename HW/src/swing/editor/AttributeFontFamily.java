package swing.editor;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
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


public class AttributeFontFamily implements CaretAttributeListener {
	private final JComboBox comboBox = new JComboBox();
	
	public AttributeFontFamily(JTextComponent text) {
		new CaretAttributeNotifier(text, StyleConstants.FontFamily, this);

		comboBox.setRenderer(new BasicComboBoxRenderer(){
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Font font = new Font((String) value, 0, comboBox.getFont().getSize());
				super.getListCellRendererComponent(list, font.getFamily(), index, isSelected, cellHasFocus);
				setFont(font);
				return this;
			}
			
		});

		
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionEvent) {
				String fontFamily = (String) comboBox.getSelectedItem();
				new StyledEditorKit.FontFamilyAction(fontFamily, fontFamily).actionPerformed(actionEvent);
			}
		});
		
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (String fontFamily : environment.getAvailableFontFamilyNames()) {
			comboBox.addItem(fontFamily);
		}
	}

	public JComponent getToolbarComponent() {
		return comboBox;
	}

	public void caretUpdate(CaretEvent e, Object value) {
		comboBox.setSelectedItem(value);
	}
}
