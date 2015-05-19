package ru.ifmo.ctddev.Nemchenko.Walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by eugene on 2/19/15.
 */
public class PrintRecFnv extends SimpleFileVisitor<Path> {
    public static final int CNT_BYTES = 1024;
    private BufferedWriter out;

    PrintRecFnv(BufferedWriter out) {
        this.out = out;
    }

    public static String getHash(Path file) {
        int result = 0x811c9dc5;
        int prime = 0x01000193;

        try (InputStream is = Files.newInputStream(file)) {
            byte buff[] = new byte[CNT_BYTES];
            int len;
            while ((len = is.read(buff)) != -1) {
                for (int i = 0; i < len; i++) {
                    result = (result * prime) ^ (buff[i] & 0xff);
                }
            }
        } catch (IOException e) {
            result = 0;
        }

        return String.format("%08x %s%n", result, file.toString());
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        out.write(getHash(file));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        int result = 0;
        out.write(String.format("%8x %s%n", result, file.toString()));
        return FileVisitResult.CONTINUE;
    }
}
