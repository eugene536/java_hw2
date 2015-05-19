package swing.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;

import swing.common.Demo;
import swing.common.Toolkit;

public class Editor extends Demo {
    private final HTMLEditorKit editorKit = new HTMLEditorKit();
    private final JTextPane textPane = new JTextPane();
    
    private final Action copyAction = setupAction(
        new StyledEditorKit.CopyAction(),
        "Copy",
        "Copy selected text to the clipboard",
        "/toolbarButtonGraphics/general/Copy16.gif",
        KeyEvent.VK_C, 
        KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_MASK)
    );

    private final Action cutAction = setupAction(
    
        new StyledEditorKit.CutAction(),
        "Cut",
        "Cuts selected text to the clipboard",
        "/toolbarButtonGraphics/general/Cut16.gif",
        KeyEvent.VK_T, 
        KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK)
    );

    private final Action pasteAction = setupAction(
        new StyledEditorKit.PasteAction(),
        "Paste",
        "Paste text ftom the clipboard",
        "/toolbarButtonGraphics/general/Paste16.gif",
        KeyEvent.VK_P, 
        KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_MASK)
    );
    
    private final AttributeButton bold = new AttributeButton(                   
                setupAction(
                                new StyledEditorKit.BoldAction(),
                                "Bold",
                                "Bold",
                                "/toolbarButtonGraphics/text/Bold16.gif",
                                KeyEvent.VK_B, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Bold,
                Boolean.TRUE
    ); 
    
    private final AttributeButton italic = new AttributeButton(
                setupAction(
                                new StyledEditorKit.ItalicAction(),
                                "Italic",
                                "Italic",
                                "/toolbarButtonGraphics/text/Italic16.gif",
                                KeyEvent.VK_I, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Italic,
                Boolean.TRUE
    ); 
    
    private final AttributeButton underline = new AttributeButton(
                setupAction(
                                new StyledEditorKit.UnderlineAction(),
                                "Underline",
                                "Underline",
                                "/toolbarButtonGraphics/text/Underline16.gif",
                                KeyEvent.VK_U, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Underline,
                Boolean.TRUE
    );
    
    private final AttributeButton alignLeft = new AttributeButton(
                setupAction(
                                new StyledEditorKit.AlignmentAction("", StyleConstants.ALIGN_LEFT),
                                "Align left",
                                "Align left",
                                "/toolbarButtonGraphics/text/AlignLeft16.gif",
                                KeyEvent.VK_L, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Alignment,
                StyleConstants.ALIGN_LEFT
    );
    
    private final AttributeButton alignCenter = new AttributeButton(
                setupAction(
                                new StyledEditorKit.AlignmentAction("", StyleConstants.ALIGN_CENTER),
                                "Align center",
                                "Align center",
                                "/toolbarButtonGraphics/text/AlignCenter16.gif",
                                KeyEvent.VK_R, 
                                KeyStroke.getKeyStroke(0, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Alignment,
                StyleConstants.ALIGN_CENTER
    );
    
    private final AttributeButton alignRight = new AttributeButton(
                setupAction(
                                new StyledEditorKit.AlignmentAction("", StyleConstants.ALIGN_RIGHT),
                                "Align right",
                                "Align right",
                                "/toolbarButtonGraphics/text/AlignRight16.gif",
                                KeyEvent.VK_R, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Alignment,
                StyleConstants.ALIGN_RIGHT
    );
    
    private final AttributeButton alignJustify = new AttributeButton(
                setupAction(
                                new StyledEditorKit.AlignmentAction("", StyleConstants.ALIGN_JUSTIFIED),
                                "Justify",
                                "Justify",
                                "/toolbarButtonGraphics/text/AlignJustify16.gif",
                                KeyEvent.VK_J, 
                                KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK)
                ),
                textPane,
                StyleConstants.Alignment,
                StyleConstants.ALIGN_JUSTIFIED
    );
    
    private final AttributeFontFamily fontFamily = new AttributeFontFamily(textPane);
    private final AttributeFontSize fontSize = new AttributeFontSize(textPane);
    
    private final UndoRedoFacility undoRedoFacility = new UndoRedoFacility();
    private final SaveLoadFacility saveLoadFacility = new SaveLoadFacility(textPane);

    private final Action[] editActions = {
                undoRedoFacility.getUndoAction(),
                undoRedoFacility.getRedoAction(),
                null,
            cutAction,
            copyAction,
            pasteAction,
    };
    
    private final Action[] fileActions = {
                saveLoadFacility.getOpenAction(),
                saveLoadFacility.getSaveAction(),
                saveLoadFacility.getSaveAsAction(),
    };
    
    public Editor() {
        super("Simple Editor");
        
        textPane.setEditorKit(editorKit);
        textPane.getDocument().addUndoableEditListener(undoRedoFacility.getUndoListener());
        
        setJMenuBar(createMainMenu());
        add(createToolBar(), BorderLayout.PAGE_START);
        add(createEditorPane(), BorderLayout.CENTER);
    }

    protected JMenuBar createMainMenu() {
        JMenuBar menu = new JMenuBar();
        
        menu.add(createMenu("File", KeyEvent.VK_F, fileActions));        
        menu.add(createMenu("Edit", KeyEvent.VK_E, editActions));        
        menu.add(createFormatMenu());        
        menu.add(Toolkit.createLookAndFeelMenu(this, KeyEvent.VK_L));
        
        return menu;
    }

        private JMenu createFormatMenu() {
                JMenu menu = new JMenu("Format");
                menu.setMnemonic('O');
                
                menu.add(bold.getMenuItem());
                menu.add(italic.getMenuItem());
                menu.add(underline.getMenuItem());
                
                return menu;
        }

        private JToolBar createToolBar() {
            JToolBar toolbar = new JToolBar();
        
                toolbar.add(saveLoadFacility.getOpenAction());
                toolbar.add(saveLoadFacility.getSaveAction());
                toolbar.add(saveLoadFacility.getSaveAsAction());
        toolbar.addSeparator();
        
        toolbar.add(cutAction);
        toolbar.add(copyAction);
        toolbar.add(pasteAction);
        toolbar.addSeparator();
        
        toolbar.add(undoRedoFacility.getUndoAction());
        toolbar.add(undoRedoFacility.getRedoAction());
        toolbar.addSeparator();
        
        toolbar.add(bold.getToolbarComponent());
        toolbar.add(italic.getToolbarComponent());
        toolbar.add(underline.getToolbarComponent());
        toolbar.addSeparator();
        
        toolbar.add(alignLeft.getToolbarComponent());
        toolbar.add(alignCenter.getToolbarComponent());
        toolbar.add(alignRight.getToolbarComponent());
        toolbar.add(alignJustify.getToolbarComponent());
        toolbar.addSeparator();
        
        toolbar.add(fontSize.getToolbarComponent());
        toolbar.add(fontFamily.getToolbarComponent());
        
            for (Component component : toolbar.getComponents()) {
                component.setFocusable(false);
            }
        
            return toolbar;
        }
        
    private Action setupAction(
            Action action, String name, String description, String icon, 
            int mnemonicKey, KeyStroke acceleratorKey
    ) {
        action.putValue(Action.NAME, name);
        action.putValue(Action.LONG_DESCRIPTION, description);

        action.putValue(
            Action.SMALL_ICON, 
            icon == null 
            ? Toolkit.loadIcon("/swing/images/empty16.gif") 
            : Toolkit.loadIcon(icon)
        );
        action.putValue(Action.MNEMONIC_KEY, mnemonicKey);
        action.putValue(Action.ACCELERATOR_KEY, acceleratorKey);

        return action;
    }

    private JComponent createEditorPane() {
        textPane.setPreferredSize(new Dimension(0, 300));
        return new JScrollPane(textPane);
    }
    
        public static void main(String[] args) {
        new Editor().setVisible(true);
    }
}