package swing.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import swing.common.Demo;

public class ListDemo extends Demo {
    private final static ListCellRenderer renderer = new ListCellRenderer() {
        private final Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        private final Border normalBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        
        private final JLabel label = new JLabel();

        {
            label.setOpaque(true);
        }

        public JLabel getListCellRendererComponent(JList list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            label.setText(value.toString());
            Integer v = (Integer) value;
            if (v < 0) {                
                label.setBackground(Color.RED);
            } else {
                label.setBackground(Color.WHITE);
            }
            label.setBorder(isSelected ? selectedBorder : normalBorder);
            return label;
        }
    };

    public ListDemo() {
        super("ListDemo");

        add(createMainPanel(), BorderLayout.CENTER);

        pack();
    }

    private Component createMainPanel() {
        JPanel panel = new JPanel();

        JList list2 = new JList(new Object[]{1, 2, 3, -1, -2, -3, 4});
        list2.setPreferredSize(new Dimension(50, list2.getPreferredSize().height));
        panel.add(list2);

        JList list = new JList(new Object[]{1, 2, 3, -1, -2, -3, 4});
        list.setPreferredSize(new Dimension(50, list.getPreferredSize().height));
        list.setCellRenderer(renderer);
        panel.add(list);

        JComboBox comboBox = new JComboBox(new Object[]{1, 2, 3, -1, -2, -3, 4});
        comboBox.setRenderer(renderer);
        //comboBox.setEditable(true);
        panel.add(comboBox, BorderLayout.LINE_END);

        return panel;
    }

    public static void main(String[] args) {
        new ListDemo().setVisible(true);
    }
}

