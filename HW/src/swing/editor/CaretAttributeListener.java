package swing.editor;

import javax.swing.event.CaretEvent;

public interface CaretAttributeListener {
	public void caretUpdate(CaretEvent e, Object value);
}
