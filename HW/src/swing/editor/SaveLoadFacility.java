package swing.editor;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import swing.common.BasicAction;

public class SaveLoadFacility {
    private final JTextPane text;
    private final JFileChooser fileChooser = new JFileChooser();
    private File file = null;
    
    public SaveLoadFacility(JTextPane text) {
    	this.text = text;
    }

    private final Action openAction = new BasicAction(
            "Open...",
            "Open document",
            "/toolbarButtonGraphics/general/Open16.gif",
            KeyEvent.VK_O, 
            KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)
    ) {
		public void actionPerformed(ActionEvent event) {
			open();
		}
    };
    
    private final Action saveAction = new BasicAction(
            "Save",
            "Save document",
            "/toolbarButtonGraphics/general/Save16.gif",
            KeyEvent.VK_S, 
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)
    ) {
		public void actionPerformed(ActionEvent event) {
			save();
		}
    };
    
    private final Action saveAsAction = new BasicAction(
            "Save As...",
            "Save document as",
            "/toolbarButtonGraphics/general/SaveAs16.gif",
            KeyEvent.VK_A, 
            null
    ) {
		public void actionPerformed(ActionEvent event) {
			saveAs();
		}
    };
    
    public boolean open() {
		if (fileChooser.showOpenDialog(text) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (openFromFile(file)) {
				this.file = file;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }
    
    public boolean openFromFile(File file) {
		FileInputStream is;
		try {
			is = new FileInputStream(file);
			try {
				EditorKit editorKit = text.getEditorKit();
				Document document = text.getDocument();
				editorKit.read(is, document, 0);
			} finally {
				is.close();
			}
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(text, "Cannot open file: " + e.getMessage() + " " + e.getClass());
			return false;
		}
    }
    
    public boolean save() {
    	if (file != null) {
    		return saveToFile(file);
    	} else {
    		return saveAs();
    	}
    }
    
    public boolean saveAs() {
		if (fileChooser.showSaveDialog(text) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (saveToFile(file)) {
				this.file = file;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }

    public boolean saveToFile(File file) {
    	FileOutputStream os;
    	try {
    		os = new FileOutputStream (file);
    		try {
				EditorKit editorKit = text.getEditorKit();
				Document document = text.getDocument();
    			editorKit.write(os, document, 0, document.getLength());
    		} finally {
    			os.close();
    		}
    		return true;
    	} catch (Exception e) {
    		JOptionPane.showMessageDialog(text, "Cannot save file: " + e.getMessage());
    		return false;
    	}
    }

	public Action getOpenAction() {
		return openAction;
	}

	public Action getSaveAction() {
		return saveAction;
	}

	public Action getSaveAsAction() {
		return saveAsAction;
	}
}
