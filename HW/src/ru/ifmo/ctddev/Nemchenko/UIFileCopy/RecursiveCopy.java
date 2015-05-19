// TBD:
// update ui properly, when resizing
// copy small files
// NoSuchFileException
// FileAlreadyExist
// PermissionDenied
// javaDoc
// recursion =(
// FileSystemException: /home/eugene/java/IdeaProjects/HW/destination/./destination/de

package ru.ifmo.ctddev.Nemchenko.UIFileCopy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Consumer;

public class RecursiveCopy extends SwingWorker<CopyProperties, CopyProperties> implements FileVisitor<Path>, ActionListener {
    private static final int MAX_BUF_SIZE = 1024 * 1024;
    private byte buf[];

    private Path sourcePath;
    private Path destinationPath;

    public long startTime;
    private final CopyProperties properties = new CopyProperties();
    private Consumer<CopyProperties> propertiesHandler;
    private Timer timer;

    public RecursiveCopy(Consumer<CopyProperties> propertiesHandler, Path sourcePath, Path destinationPath) {
        buf = new byte[MAX_BUF_SIZE];
        startTime = System.currentTimeMillis();
        this.propertiesHandler = propertiesHandler;
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;

        timer = new Timer(1000, this);
        timer.start();
        execute();
    }

    private void getTotalSize() throws IOException {
        System.out.println(sourcePath);
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (isCancelled()) return FileVisitResult.TERMINATE;
                properties.setTotalBytes(properties.getTotalBytes() + attrs.size());
                publish(properties);
                return FileVisitResult.CONTINUE;
            }
        });

    }

    @Override
    protected CopyProperties doInBackground() throws Exception {
        properties.setStartTime(System.currentTimeMillis());
        properties.setEvaluatingTotalSize(true);
        getTotalSize();
        properties.setEvaluatingTotalSize(false);

        Files.walkFileTree(sourcePath, this);

        return properties;
    }


    @Override
    protected void process(List<CopyProperties> chunks) {
        CopyProperties properties = chunks.get(chunks.size() - 1);
        propertiesHandler.accept(properties);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        try {
            Path suffix = sourcePath.relativize(dir);
            Files.createDirectories(destinationPath.resolve(suffix));
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            // ignoring
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        return FileVisitResult.CONTINUE;
    }

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
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        exc.printStackTrace();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    protected void done() {
        try {
            CopyProperties properties = get();
            properties.setFinishedCopying(true);
            propertiesHandler.accept(properties);
            timer.stop();
        } catch (Exception e) {
            if (!isCancelled()) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("cancel".equals(e.getActionCommand())) {
            cancel(true);
            timer.stop();
        } else {
            synchronized (properties) {
                propertiesHandler.accept(properties);
            }
        }
    }
}
