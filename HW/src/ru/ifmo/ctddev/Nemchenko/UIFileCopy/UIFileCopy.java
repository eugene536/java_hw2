package ru.ifmo.ctddev.Nemchenko.UIFileCopy;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.text.DecimalFormat;

import static ru.ifmo.ctddev.Nemchenko.UIFileCopy.CopyProperties.*;

/**
 * Class to create GUI and to do copy of files or folders
 * with showing progress and updating copy state.
 * You must pass source path and destination in arguments of program.
 */
public class UIFileCopy extends JFrame {
    private static Path sourcePath;
    private static Path destinationPath;
    private GridBagConstraints constraints;

    private static final String TITLE = "File copy utility";
    private static final String AVERAGE_SPEED = "Average speed: ";
    private static final String EXPIRED_TIME = "Expired time: ";
    private static final String REMAINING_TIME = "Remaining time: ";
    private static final String CANCEL = "cancel";
    private static final String CURRENT_SPEED = "Current speed: ";

    private JButton cancel;
    private JLabel expiredTimeLabel;
    private JLabel remainingTimeLabel;
    private JLabel currentSpeedLabel;
    private JLabel averageSpeedLabel;
    private JProgressBar progress;
    private JLabel totalSizeLabel;
    private JPanel panel;
    private static JDialog dialog;

    /**
     * construct this UIFileCopy
     */
    UIFileCopy() {
        super(TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // window will be showed on the center
        getContentPane().setLayout(new GridBagLayout());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | UnsupportedLookAndFeelException | IllegalAccessException ignore) {
        }

        constraints = new GridBagConstraints();
        createPanel();
        createProgress();

        String forGUI = "           ";
        totalSizeLabel = createLabel(forGUI, 0, 0, 2, GridBagConstraints.CENTER);
        currentSpeedLabel = createLabel(CURRENT_SPEED, 0, 2, 1, GridBagConstraints.FIRST_LINE_END);
        averageSpeedLabel = createLabel(AVERAGE_SPEED, 0, 3, 1, GridBagConstraints.FIRST_LINE_END);
        expiredTimeLabel = createLabel(EXPIRED_TIME, 1, 2, 1, GridBagConstraints.FIRST_LINE_START);
        remainingTimeLabel = createLabel(REMAINING_TIME, 1, 3, 1, GridBagConstraints.FIRST_LINE_START);
        setLabelsVisibility(false);

        createCancelButton();

        pack();
        setVisible(true);

        RecursiveCopy recursiveCopy = new RecursiveCopy(this::propertiesHandler, sourcePath, destinationPath);
        cancel.setActionCommand("cancel");
        cancel.addActionListener(recursiveCopy);
    }

    private void createPanel() {
        panel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        TitledBorder centerBorder = BorderFactory.createTitledBorder("Copying");
        centerBorder.setTitleJustification(TitledBorder.LEFT);
        panel.setBorder(centerBorder);

        getContentPane().add(panel, constraints);
    }

    private JLabel createLabel(String text, int gridx, int gridy, int width, int anchor) {
        JLabel label = new JLabel();

        label.setFont(new Font("monospace", Font.PLAIN, 14));
        FontMetrics fm = label.getFontMetrics(label.getFont());
        int w = fm.stringWidth(text + "10 h 20 m 20 s");
        int h = fm.getHeight();
        label.setMinimumSize(new Dimension(w, h));
        label.setPreferredSize(new Dimension(w, h));

        label.setText(text);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = anchor;
        constraints.insets = new Insets(10, 10, 10, 50);
        constraints.gridwidth = width;
        constraints.weightx = 0.5;
        panel.add(label, constraints);
        return label;
    }

    private void createProgress() {
        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setIndeterminate(true);
        progress.setValue(0);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weightx = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(progress, constraints);
    }

    private void createCancelButton() {
        cancel = new JButton(CANCEL);
        constraints.anchor = GridBagConstraints.LAST_LINE_END;
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(cancel, constraints);
    }

    private void setLabelsVisibility(boolean visible) {
        currentSpeedLabel.setVisible(visible);
        averageSpeedLabel.setVisible(visible);
        expiredTimeLabel.setVisible(visible);
        remainingTimeLabel.setVisible(visible);
    }

    private void canceledCopying() {
        remainingTimeLabel.setText(REMAINING_TIME + "0 s");
        cancel.setText("close");

        TitledBorder centerBorder = BorderFactory.createTitledBorder("Canceled");
        centerBorder.setTitleJustification(TitledBorder.LEFT);
        centerBorder.setTitleColor(Color.red);
        panel.setBorder(centerBorder);
    }

    private void finishedCopying(CopyProperties properties) {
        canceledCopying();
        progress.setValue(100);
        assert properties != null;
        totalSizeLabel.setText(byteToString(properties.getTotalBytes()) + "/" + byteToString(properties.getTotalBytes()));

        double expiredTime = milliToSeconds(System.currentTimeMillis() - properties.getStartTime());
        expiredTimeLabel.setText(EXPIRED_TIME + getTimeFormat((long) (expiredTime * 1000)));

        TitledBorder centerBorder = BorderFactory.createTitledBorder("Finished");
        centerBorder.setTitleJustification(TitledBorder.LEFT);
        centerBorder.setTitleColor(Color.red);
        panel.setBorder(centerBorder);
    }

    private void propertiesHandler(CopyProperties properties) {
        if (dialog != null && dialog.isVisible()) return;
        boolean first_pack = false;

        if (properties.isFinishedCopying()) {
            finishedCopying(properties);
            return;
        } else if (properties.isCanceled()) {
            canceledCopying();
            return;
        } else if (properties.getError() != null) { // something go wrong, =(
            if (properties.getError() instanceof AccessDeniedException) {
                showErrorDialog("Access denied: " + properties.getError().getMessage(), this);
            } else {
                showErrorDialog(properties.getError().getMessage(), this);
            }
            return;
        }

        if (!properties.isEvaluatingTotalSize()) {
            DecimalFormat outFormat = new DecimalFormat("#0.0");
            if (properties.getLastTime() == 0) return;

            double curSpeedValue = bytesToMega(properties.getLastCopiedBytes()) / milliToSeconds(properties.getLastTime());
            String curSpeed = outFormat.format(curSpeedValue) + " Mb/s";
            currentSpeedLabel.setText(CURRENT_SPEED + curSpeed);

            double expiredTime = milliToSeconds(System.currentTimeMillis() - properties.getStartTime());
            double averageSpeedValue = bytesToMega(properties.getCopiedBytes()) / expiredTime;
            String averageSpeed = outFormat.format(averageSpeedValue) + " Mb/s";
            averageSpeedLabel.setText(AVERAGE_SPEED + averageSpeed);
            expiredTimeLabel.setText(EXPIRED_TIME + getTimeFormat((long) (expiredTime * 1000)));

            double remainingTime = bytesToMega(properties.getTotalBytes() - properties.getCopiedBytes()) / averageSpeedValue;
            remainingTimeLabel.setText(REMAINING_TIME + getTimeFormat((long) (remainingTime * 1000)));

            int percentage = (int) Math.round(properties.getCopiedBytes() * 100.0 / properties.getTotalBytes());
            progress.setValue(percentage);

            if (progress.isIndeterminate()) {
                progress.setIndeterminate(false);
                setLabelsVisibility(true);
                first_pack = true;
            }
        }
        String totalSize = byteToString(properties.getTotalBytes());
        if (totalSizeLabel.getText().equals("")) {
            first_pack = true;
        }

        totalSizeLabel.setText(byteToString(properties.getCopiedBytes()) + "/" + totalSize);

        if (first_pack) {
            pack();
        }

        properties.setLastCopiedBytes(0);
    }

    private static void showErrorDialog(String message, JFrame frame) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        if (dialog != null && dialog.isVisible()) return;
        dialog = new JDialog(frame, "Click a button", true);

        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();

            if (dialog.isVisible() && (e.getSource() == optionPane)
                    && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                System.exit(0);
            }
        });
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void showError(String message) {
        SwingUtilities.invokeLater(() -> showErrorDialog(message, null));
    }

    private static boolean checkSubdirectory(Path destinationPath, Path sourcePath) {
        if (destinationPath == null || sourcePath == null) {
            return false;
        }

        if (destinationPath.compareTo(sourcePath) == 0) {
            return true;
        }

        return checkSubdirectory(destinationPath.getParent(), sourcePath);
    }

    public static void main(String[] args) {
        if (true) {
            args[0] = "source/new";
            args[1] = "destination/new";

//            args[0] = "source/2";
//            args[1] = "destination/2";
//
//            args[0] = "source";
//            args[1] = "destination";
//
//            args[0] = "source_error/new";
//            args[1] = "destination/new";
//
//            args[0] = "asdfasdf";
//            args[1] = "destination";

//            args = null;
        }
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            showError("usage: <source location> <destination location>");
            return;
        }
        sourcePath = null;
        destinationPath = null;

        try {
            sourcePath = Paths.get(args[0]);
            destinationPath = Paths.get(args[1]);

            if (!Files.exists(sourcePath, LinkOption.NOFOLLOW_LINKS)) {
                showError("can't find file: " + sourcePath);
                return;
            }

            if (Files.exists(destinationPath)) {
                if (checkSubdirectory(destinationPath.toRealPath(), sourcePath.toRealPath())) {
                    showError("you cannot copy file into itself");
                    return;
                }
            }
        } catch (InvalidPathException ex) {
            showError("Bad path: [" + ex.getInput() + "]");
            return;
        } catch (IOException e) {
            //ignore
        }

        if (Files.isDirectory(sourcePath)) {
            destinationPath = destinationPath.resolve(sourcePath.getFileName());
        }

        try {
            Files.createDirectories(destinationPath.getParent());
        } catch (IOException e) {
            showError("Bad path: [" + e.getMessage() + "]");
        }
        SwingUtilities.invokeLater(UIFileCopy::new);
    }
}
