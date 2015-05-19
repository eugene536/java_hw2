package swing.common;

import javax.swing.*;
import java.awt.*;

public class TitledPanel extends JPanel {
    public TitledPanel(String title, JComponent panel) {
        super(new BorderLayout());

        add(panel);

        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            title
        ));
    }
}
