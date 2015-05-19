//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.ifmo.ctddev.Nemchenko.Walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Walk {
    public static void main(String... args) throws IOException {
        if (args != null && args.length == 2 && args[0] != null && args[1] != null) {
            Charset charset = Charset.forName("UTF-8");
            Path inPath = Paths.get(args[0]);
            Path outPath = Paths.get(args[1]);

            try (BufferedReader in = Files.newBufferedReader(inPath, charset);
                 BufferedWriter out = Files.newBufferedWriter(outPath, charset)) {
                String line;
                while ((line = in.readLine()) != null) {
                    Path curPath = Paths.get(line);
                    if (Files.isDirectory(curPath)) {
                        try {
                            Files.walkFileTree(curPath, new PrintRecFnv(out));
                        } catch (IOException e) {
                            System.err.println("out of memmory");
                        }
                    } else {
                        out.write(PrintRecFnv.getHash(curPath));
                    }
                }
            } catch (NoSuchFileException var47) {
                System.err.println("no such file: " + var47.getMessage());
            } catch (InvalidPathException var48) {
                System.err.println("invalid path: %s%n" + var48.getMessage());
            } catch (FileNotFoundException var49) {
                System.err.println("file not found: " + var49.getMessage());
            } catch (IOException var50) {
                System.err.println("can\'t open file");
            }

        } else {
            System.err.println("please type input.txt and output.txt");
        }
    }
}

