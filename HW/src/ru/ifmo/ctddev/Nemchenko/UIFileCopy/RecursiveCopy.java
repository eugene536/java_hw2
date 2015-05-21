package ru.ifmo.ctddev.Nemchenko.UIFileCopy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class to perform copy files with updating GUI.
 * You have to pass handler which will be invoked on the event dispatching thread and
 * handled {@code CopyProperties} object, which contains all needed information
 * about current copy state.
 */
public class RecursiveCopy extends SwingWorker<CopyProperties, CopyProperties> implements FileVisitor<Path>, ActionListener {
    private static final int MAX_BUF_SIZE = 1024 * 1024;
    public static final int DELAY = 1000;
    private byte buf[];

    private Path sourcePath;
    private Path destinationPath;

    private final CopyProperties properties = new CopyProperties();
    private Consumer<CopyProperties> propertiesHandler;
    private Timer timer;

    /**
     * construct this RecursiveCopy
     * @param propertiesHandler handler which will be invoked when state changed
     * @param sourcePath target path to folder or file which will be copied to {@code destinationPath}
     * @param destinationPath destination path to folder or file
     */
    public RecursiveCopy(Consumer<CopyProperties> propertiesHandler, Path sourcePath, Path destinationPath) {
        buf = new byte[MAX_BUF_SIZE];
        this.propertiesHandler = propertiesHandler;
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;

        timer = new Timer(DELAY, this);
        timer.start();
        execute();
    }

    private void getTotalSize() {
        System.out.println(sourcePath);
        try {
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (isCancelled()) return FileVisitResult.TERMINATE;
                    properties.setTotalBytes(properties.getTotalBytes() + attrs.size());
                    publish(properties);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println("failed: " + file);
                    exc.printStackTrace();
                    return super.visitFileFailed(file, exc);
                }
            });
        } catch (IOException e) {
            properties.setError(e);
            publish(properties);
        }

    }

    /**
     * Invoked on the working thread, and do copy of files.
     * Invokes {@code handleProperties} every time when copy state changed.
     *
     * @return copy state
     */
    @Override
    protected CopyProperties doInBackground() {
        properties.setStartTime(System.currentTimeMillis());
        properties.setEvaluatingTotalSize(true);
        getTotalSize();
        properties.setEvaluatingTotalSize(false);

        try {
            Files.walkFileTree(sourcePath, this);
        } catch (IOException e) {
            properties.setError(e);
            publish(properties);
        }

        return properties;
    }


    /**
     * Updates GUI with changed copy state.
     *
     * @param chunks list of copy states, used only last entry.
     */
    @Override
    protected void process(List<CopyProperties> chunks) {
        // if error occured
        for (CopyProperties properties: chunks) {
            if (properties.getError() != null) {
                propertiesHandler.accept(properties);
                cancel(true);
            }
        }
        CopyProperties properties = chunks.get(chunks.size() - 1);
        propertiesHandler.accept(properties);
    }

    /**
     * Invoked for a directory before entries in the directory are visited
     * and creates these directories.
     *
     * @param   dir a reference to the directory
     * @param   attrs the directory's basic attributes
     * @return  the visit result
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        try {
            Path suffix = sourcePath.relativize(dir);
            Files.createDirectories(destinationPath.resolve(suffix));
        } catch (Exception e) {
            properties.setError(e);
            publish(properties);
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * Invoked for a file and do copy of it with update GUI invoking {@code handleProperties}
     *
     * @param file target file which will be copied
     * @param attrs the file's basic attributes
     * @return  the visit result
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        Path suffix = sourcePath.relativize(file);
        Path destinationFile = destinationPath.resolve(suffix);

        try (InputStream source = new BufferedInputStream(Files.newInputStream(file));
             OutputStream destination = new BufferedOutputStream(Files.newOutputStream(destinationFile))) {
            long startTime = System.currentTimeMillis();
            int lastReadBytes;
            while ((lastReadBytes = source.read(buf, 0, MAX_BUF_SIZE)) != -1) {
                if (isCancelled()) return FileVisitResult.TERMINATE;
                destination.write(buf, 0, lastReadBytes);

                long lastTime = System.currentTimeMillis() - startTime;
                synchronized (properties) {
                    properties.setCopiedBytes(properties.getCopiedBytes() + lastReadBytes);
                    properties.setLastCopiedBytes(lastReadBytes);
                    properties.setLastTime(lastTime);
                }
                startTime = System.currentTimeMillis();
            }
        } catch (IOException e) {
            if (isCancelled()) return FileVisitResult.TERMINATE;
            properties.setError(e);
            publish(properties);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        if (exc.getCause() != null) {
            System.out.println(exc.getCause().getMessage());
        } else {
            System.out.println(exc.getMessage());
        }
        cancel(true);
        return FileVisitResult.TERMINATE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * Invoked when copy of files finished.
     * Perform {@code propertiesHandler} with finished flag and stops timer.
     */
    @Override
    protected void done() {
        try {
            CopyProperties properties = get();
            properties.setFinishedCopying(true);
            propertiesHandler.accept(properties);
            timer.stop();
        } catch (Exception e) {
            if (!isCancelled()) {
                properties.setError(e);
                publish(properties);
            }
        }
    }

    /**
     * Invoked every second or when cancel button was pressed.
     * Perform {@code propertiesHandler} and pass current copy state to it.
     *
     * @param e for check what kind of event occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("cancel".equals(e.getActionCommand())) {
            if (isDone() || isCancelled()) {
                System.exit(0);
            } else {
                cancel(true);
                timer.stop();
                properties.setCanceled(true);
                propertiesHandler.accept(properties);
            }
        } else {
            synchronized (properties) {
                propertiesHandler.accept(properties);
            }
        }
    }
}
