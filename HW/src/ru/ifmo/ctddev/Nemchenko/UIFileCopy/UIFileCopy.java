package ru.ifmo.ctddev.Nemchenko.UIFileCopy;

import javax.swing.*;
import java.awt.*;
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

    /**
     * construct this UIFileCopy
     */
    UIFileCopy() {
        super("File copy utility");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // window will be showed on the center
        getContentPane().setLayout(new GridBagLayout());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | UnsupportedLookAndFeelException | IllegalAccessException e) {
            // ignore
        }
        constraints = new GridBagConstraints();
        panel = new JPanel(new GridBagLayout());
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        getContentPane().add(panel, constraints);

        panel.setBorder(BorderFactory.createLineBorder(Color.gray));

        constraints = new GridBagConstraints();

        createProgress();

        constraints.insets = new Insets(10, 10, 10, 50);
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        totalSizeLabel = createLabel("", 0, 0);

        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;

        currentSpeedLabel = createLabel(CURRENT_SPEED, 0, 2);
        averageSpeedLabel = createLabel(AVERAGE_SPEED, 0, 3);
        expiredTimeLabel = createLabel(EXPIRED_TIME, 1, 2);
        remainingTimeLabel = createLabel(REMAINING_TIME, 1, 3);
        setLabelsVisibility(false);

        createCancelButton();

        pack();
        setVisible(true);

        RecursiveCopy recursiveCopy = new RecursiveCopy(this::propertiesHandler, sourcePath, destinationPath);
        cancel.addActionListener(recursiveCopy);
        cancel.setActionCommand("cancel");
    }

    private JLabel createLabel(String text, int gridx, int gridy) {
        JLabel label = new JLabel();
        label.setText(text);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
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
        System.out.println("canceled");
        remainingTimeLabel.setText(REMAINING_TIME + "0 s");
        cancel.setText("close");
    }

    private void finishedCopying(CopyProperties properties) {
        canceledCopying();
        progress.setValue(100);
        assert properties != null;
        totalSizeLabel.setText(byteToString(properties.getTotalBytes()) + "/" + byteToString(properties.getTotalBytes()));

        double expiredTime = milliToSeconds(System.currentTimeMillis() - properties.getStartTime());
        expiredTimeLabel.setText(EXPIRED_TIME + getTimeFormat((long) (expiredTime * 1000)));
    }

    private void propertiesHandler(CopyProperties properties) {
        boolean first_pack = false;

        if (properties.isFinishedCopying()) {
            finishedCopying(properties);
            return;
        } else if (properties.isCanceled()) {
            canceledCopying();
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

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.out.println("usage: <source location> <destination location>");
            return;
        }
        sourcePath = null;
        destinationPath = null;

        try {
            sourcePath = Paths.get(args[0]).toRealPath();
            destinationPath = Paths.get(args[1]).toRealPath();

            if (!Files.exists(sourcePath, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("can't find file: " + sourcePath);
                return;
            } else if (sourcePath.compareTo(destinationPath) == 0) {
                System.out.println("you cannot copy file into itself");
                return;
            }
        } catch (InvalidPathException ex) {
            System.out.println("Bad path: [" + ex.getInput() + "]");
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (Files.isDirectory(sourcePath)) {
            destinationPath = destinationPath.resolve(sourcePath.getFileName());
        }
        try {
            Files.createDirectories(destinationPath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(destinationPath);

        SwingUtilities.invokeLater(UIFileCopy::new);
    }

}
