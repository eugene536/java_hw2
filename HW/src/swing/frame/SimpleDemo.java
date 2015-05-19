package swing.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SimpleDemo {
    public static JMenuBar createMainMenu() {
        JMenuBar mainMenu = new JMenuBar();
        mainMenu.add(createFileMenu());
        mainMenu.add(createDialogsMenu());
        mainMenu.add(createHelpMenu());
        return mainMenu;
    }

    private static JMenuItem createDialogsMenu() {
        final JMenu menu = new JMenu("Dialogs");
        menu.setMnemonic('D');
        addAction(menu, "ConfirmDialog", () -> JOptionPane.showConfirmDialog(menu, "Are you ready?", "ConfirmDialog", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE));
        addAction(menu, "InputDialog", () -> JOptionPane.showInputDialog(menu, "Enter your ID", "InputDialog", JOptionPane.QUESTION_MESSAGE));
        addAction(menu, "OptionDialog", () -> JOptionPane.showOptionDialog(menu, "Choose your destiny", "OptionDialog", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Variant 1", "Variant 2", "Variant 3"}, "Variant 2"));
        addAction(menu, "InputDialog/Select", () -> JOptionPane.showInputDialog(menu, "Choose again", "InputDialog", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Variant 1", "Variant 2", "Variant 3"}, "Variant 2"));
        addAction(menu, "MessageDialog", () -> JOptionPane.showMessageDialog(menu, "You will...", "MessageDialog", JOptionPane.ERROR_MESSAGE, null));
        return menu;
    }

    private static void addAction(JMenu menu, final String name, Runnable action) {
        menu.add(new AbstractAction(name) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                action.run();
            }
        });
    }

    private static JMenuItem createHelpMenu() {
        final JMenu menu = new JMenu("Help");
        menu.setMnemonic('H');
        return menu;
    }

    public static JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        fileMenu.add(new JMenuItem("Open"));
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem("Exit"));

        return fileMenu;
    }



    public static void main(String[] args) {
        JPanel panel = createPanel();

        JFrame frame = new JFrame("SimpleDemo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createPanel(), createPanel());
        split.setResizeWeight(0.5);
        split.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(split);
        frame.setJMenuBar(createMainMenu());
        frame.pack();
        frame.setVisible(true);
    }

    private static JPanel createPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton("<html>e=mc<sup>2</sup></html>"));
        panel.add(new JButton("Button 2"));
        panel.add(new JButton("Button 3"));
        panel.add(new JButton("Long-Named Button 4"));
        panel.add(new JButton("5"));
        return panel;
    }

    private static void layout() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout());

        panel.add(new JButton("Start"), BorderLayout.PAGE_START);
        panel.add(new JButton("End"), BorderLayout.PAGE_END);

        JFrame frame = new JFrame("SimpleDemo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}

