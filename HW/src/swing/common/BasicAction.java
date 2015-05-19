package swing.common;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public abstract class BasicAction extends AbstractAction {
    public BasicAction(
            String name, String description, String icon, 
            int mnemonicKey, KeyStroke acceleratorKey
    ) {
        super(name);
        putValue(SHORT_DESCRIPTION, description);

        putValue(SMALL_ICON, icon == null ? Toolkit.loadIcon("/swing/images/empty16.gif") : Toolkit.loadIcon(icon));
        putValue(MNEMONIC_KEY, mnemonicKey);
        putValue(ACCELERATOR_KEY, acceleratorKey);
    }
}
