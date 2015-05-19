package swing.frame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BorderDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BorderDemo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(createPanel());
        frame.pack();
        frame.setVisible(true);
    }

    private static JPanel createPanel() {
        JPanel panel = new JPanel(new GridLayout(9, 1, 1, 3));
        panel.add(createLabel(BorderFactory.createEmptyBorder(3, 3, 3, 3), "size = 3"));
        panel.add(createLabel(BorderFactory.createLineBorder(Color.BLACK, 3), "color = black"));
        panel.add(createLabel(BorderFactory.createRaisedBevelBorder(), "raised"));
        panel.add(createLabel(BorderFactory.createLoweredBevelBorder(), "lowered"));
        panel.add(createLabel(BorderFactory.createRaisedSoftBevelBorder(), "raised"));
        panel.add(createLabel(BorderFactory.createLoweredSoftBevelBorder(), "lowered"));
        panel.add(createLabel(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "raised"));
        panel.add(createLabel(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "lowered"));
        panel.add(createLabel(BorderFactory.createTitledBorder(null, "Title", TitledBorder.LEADING, TitledBorder.ABOVE_TOP), "leading, above top"));
        panel.add(createLabel(BorderFactory.createTitledBorder(null, "Title", TitledBorder.CENTER, TitledBorder.TOP), "center, top"));
        panel.add(createLabel(BorderFactory.createTitledBorder(null, "Title", TitledBorder.LEADING, TitledBorder.BELOW_TOP), "leading, below top"));
        panel.add(createLabel(BorderFactory.createTitledBorder(null, "Title", TitledBorder.TRAILING, TitledBorder.ABOVE_BOTTOM), "trailing, above bottom"));
        panel.add(createLabel(BorderFactory.createTitledBorder(null, "Title", TitledBorder.CENTER, TitledBorder.BOTTOM), "center, bottom"));
        panel.add(createLabel(BorderFactory.createTitledBorder(null, "Title", TitledBorder.TRAILING, TitledBorder.BELOW_BOTTOM), "trailing, below bottom"));
        panel.add(createLabel(BorderFactory.createMatteBorder(-1, -1, -1, -1, new ImageIcon(BorderDemo.class.getResource("Matte.png"))), "icon"));
        panel.add(createLabel(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLUE), "color"));
        panel.add(createLabel(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLUE, 3),
                        BorderFactory.createLoweredBevelBorder()
                )
        ), "stack of 3"));

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private static JComponent createLabel(final Border border, final String comment) {
        final JLabel label = new JLabel(border.getClass().getSimpleName() + " " + comment, SwingConstants.CENTER);
        label.setBorder(border);
//        label.setHorizontalTextPosition(SwingConstants.CENTER);
        return label;
    }
}

