package ru.ifmo.ctddev.Nemchenko.UIFileCopy;

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.text.DecimalFormat;

import static ru.ifmo.ctddev.Nemchenko.UIFileCopy.CopyProperties.*;

/**
 * Created by eugene on 2015/05/11.
 */
public class UIFileCopy extends JFrame {
    private static Path sourcePath;
    private static Path destinationPath;
    private GridBagConstraints constraints;

    public static final String AVERAGE_SPEED = "Average speed: ";
    public static final String EXPIRED_TIME = "Expired time: ";
    public static final String REMAINING_TIME = "Remaining time: ";
    public static final String CANCEL = "cancel";
    public static final String CURRENT_SPEED = "Current speed: ";

    private JButton cancel;
    private JLabel expiredTimeLabel;
    private JLabel remainingTimeLabel;
    private JLabel currentSpeedLabel;
    private JLabel averageSpeedLabel;
    private JProgressBar progress;
    private JLabel totalSizeLabel;

    private RecursiveCopy recursiveCopy;

    UIFileCopy() {
        super("File copy utility");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // window will be showed on the center
        getContentPane().setLayout(new GridBagLayout());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
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

        recursiveCopy = new RecursiveCopy(this::propertiesHandler, sourcePath, destinationPath);
        cancel.addActionListener(recursiveCopy);
        cancel.setActionCommand("cancel");
    }

    private JLabel createLabel(String text, int gridx, int gridy) {
        JLabel label = new JLabel();
        label.setText(text);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        getContentPane().add(label, constraints);
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
        constraints.weighty = 0.5;
        constraints.weightx = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        getContentPane().add(progress, constraints);
    }

    private void createCancelButton() {
        cancel = new JButton(CANCEL);
        constraints.anchor = GridBagConstraints.LAST_LINE_END;
        constraints.gridx = 1;
        constraints.gridy = 4;
        getContentPane().add(cancel, constraints);
    }

    public void setLabelsVisibility(boolean visible) {
        currentSpeedLabel.setVisible(visible);
        averageSpeedLabel.setVisible(visible);
        expiredTimeLabel.setVisible(visible);
        remainingTimeLabel.setVisible(visible);
    }

    private void finishedCopiyng(CopyProperties properties) {
        DecimalFormat outFormat = new DecimalFormat("#0.0");
        progress.setValue(100);
        remainingTimeLabel.setText(REMAINING_TIME + "0 s");
        assert properties != null;
        totalSizeLabel.setText(byteToString(properties.getCopiedBytes()) + "/" + byteToString(properties.getTotalBytes()));

        double expiredTime = milliToSeconds(System.currentTimeMillis() - properties.getStartTime());
        expiredTimeLabel.setText(EXPIRED_TIME + getTimeFormat((long) (expiredTime * 1000)));
    }

    private void propertiesHandler(CopyProperties properties) {
        boolean first_pack = false;

        if (properties.isFinishedCopying()) {
            finishedCopiyng(properties);
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
            sourcePath = Paths.get(args[0]);
            destinationPath = Paths.get(args[1]);
        } catch (InvalidPathException ex) {
            System.out.println("Bad path: [" + ex.getInput() + "]");
            return;
        }
        if (Files.isDirectory(sourcePath)) {
            destinationPath = destinationPath.resolve(sourcePath.getFileName());
        }
        System.out.println(destinationPath);

        SwingUtilities.invokeLater(UIFileCopy::new);
    }

}
