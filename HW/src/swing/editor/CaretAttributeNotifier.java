package swing.editor;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public class CaretAttributeNotifier implements CaretListener {
	private final Object key;
	private final CaretAttributeListener listener;
	
	public CaretAttributeNotifier(JTextComponent text, Object key, CaretAttributeListener listener) {
		text.addCaretListener(this);
		this.key = key;
		this.listener = listener;
	}
	
	public void caretUpdate(CaretEvent e) {
		JTextComponent component = (JTextComponent) e.getSource();
		
		Element element = component.getDocument().getDefaultRootElement();
		int index;
		while ((index = element.getElementIndex(e.getDot() - 1)) >= 0) {
			element = element.getElement(index);
		}
		AttributeSet attributes = element.getAttributes();
		listener.caretUpdate(e, attributes.getAttribute(key));
	}
}
