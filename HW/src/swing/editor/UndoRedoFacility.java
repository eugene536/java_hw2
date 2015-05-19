package swing.editor;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import swing.common.BasicAction;

public class UndoRedoFacility {
    private final UndoManager undoManager = new UndoManager();

    private final Action undoAction = new BasicAction(
        "Undo",
        "Undo last edit",
        "/toolbarButtonGraphics/general/Undo16.gif",
        KeyEvent.VK_U, 
        KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)
    ) {
		public void actionPerformed(ActionEvent event) {
			try {
				undoManager.undo();
			} catch (CannotUndoException e) {
				JOptionPane.showMessageDialog(null, "Cannot undo: " + e.getMessage());
			} finally {
				updateState();
			}
		}
    };

    private final Action redoAction = new BasicAction(
        "Redo",
        "Redo last edit",
        "/toolbarButtonGraphics/general/Redo16.gif",
        KeyEvent.VK_R, 
        KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK)
    ) {
		public void actionPerformed(ActionEvent event) {
			try {
				undoManager.redo();				
			} catch (CannotRedoException e) {
				JOptionPane.showMessageDialog(null, "Cannot redo: " + e.getMessage());
			} finally {
				updateState();
			}
		}
    };

    private final UndoableEditListener undoListener = new UndoableEditListener() {
    	public void undoableEditHappened(UndoableEditEvent e) {
    		undoManager.addEdit(e.getEdit());
    		updateState();
    	}
    };
    
    private void updateState() {
    	undoAction.setEnabled(undoManager.canUndo());
    	redoAction.setEnabled(undoManager.canRedo());
    }

	public Action getRedoAction() {
		return redoAction;
	}

	public Action getUndoAction() {
		return undoAction;
	}

	public UndoableEditListener getUndoListener() {
		return undoListener;
	}
}
