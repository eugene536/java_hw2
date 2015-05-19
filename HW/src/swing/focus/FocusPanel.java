/**
 * 
 */
package swing.focus;

import java.awt.Color;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

final class FocusPanel extends JPanel implements FocusListener {
    private final Border activeBorder;
    private final Border passiveBorder;

    public FocusPanel(String title, LayoutManager layout) {
        super(layout);

        activeBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.RED, 3),
            title
        );

        passiveBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            title
        );
        setBorder(passiveBorder);

        setFocusable(true);
        addFocusListener(this);
        setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
    }

    public void focusGained(FocusEvent e) {
        setBorder(activeBorder);
    }

    public void focusLost(FocusEvent e) {
        setBorder(passiveBorder);
    }

    @Override
    public boolean isFocusCycleRoot() {
        return true;
    }
}