package swing.frame;

import javax.swing.*;
import java.awt.*;

public class JSplitPaneDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JSplitPaneDemo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createPanel(), createPanel());
        split.setResizeWeight(0.5);
        split.setOneTouchExpandable(true);
//        split.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(split);
        frame.pack();
        frame.setVisible(true);
    }

    private static JScrollPane createPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton("<html>e=mc<sup>2</sup></html>"));
        panel.add(new JButton("Button 2"));
        panel.add(new JButton("Button 3"));
        panel.add(new JButton("Long-Named Button 4"));
        panel.add(new JButton("5"));
        return new JScrollPane(panel);
    }
}

