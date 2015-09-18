package ru.ifmo.ctddev.Nemchenko.exam.spoon;

import sun.java2d.xr.MutableInteger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by eugene on 2015/06/11.
 */
public class SpoonInterpreter {
    private ArrayList<Integer> cells;
    private Scanner in;
    private PrintStream out;
    private int maxIterations;
    private int maxMemory;
    private int curCell;

    public SpoonInterpreter(InputStream in, OutputStream out, int maxIterations, int maxMemory) {
        this();
        this.in = new Scanner(in);
        this.out = new PrintStream(out);
        this.maxIterations = maxIterations;
        this.maxMemory = maxMemory;
    }

    public SpoonInterpreter(InputStream in, OutputStream out) {
        this();
        this.in = new Scanner(in);
        this.out = new PrintStream(out);
    }

    public SpoonInterpreter() {
        cells = new ArrayList<>();
        in = new Scanner(System.in);
        out = System.out;
    }

    public void runProgram(String program) {
        String curCommand = "";
        int cellBeforeLoop = -1;
        int iBeforeLoop = -1;
        int maxCommandLen = 7;
        int cntCommands = 0;
        cells.add(0);
        for (int i = 0; i < program.length(); i++) {
            if (cntCommands >= maxIterations) {

            }
            boolean validCommand = false;
            for (int j = 0; j < maxCommandLen && i + j <= program.length(); j++) {
                switch (program.substring(i, i + j)) {
                    case "1":
                        cells.set(curCell, cells.get(curCell) + 1);
                        break;
                    case "000":
                        cells.set(curCell, cells.get(curCell) - 1);
                        break;
                    case "010":
                        curCell++;
                        if (curCell == cells.size()) {
                            cells.add(0);
                        }
                        break;
                    case "011":
                        curCell--;
                        break;
                    case "0010110":
                        cells.set(curCell, in.nextInt());
                        break;
                    case "001010":
                        out.print((char) cells.get(curCell).intValue());
                        break;
                    case "00100": // begin of loop
                        cellBeforeLoop = curCell;
                        iBeforeLoop = i;
                        break;
                    case "0011": // end of loop
                        if (cells.get(curCell) > 0) {
                            curCell = cellBeforeLoop;
                            i = iBeforeLoop - (j - 1) - 1;
                        }
                        break;
                    default:
                        continue;
                }
                validCommand = true;
                i += j - 1;
                cntCommands++;
                break;
            }

            if (!validCommand) {
                System.err.println("invalid command on position: " + i);
                System.exit(0);
            }
        }
    }

    public static void main(String... args) {
        new SpoonInterpreter().runProgram("111 11111110010001011111110101111111111010111010101101101101100000110101100101001010010101111111001010001010111001010010110010100110111111111111111110010100100010101110010100000000000000000000010100000000000000000000000000010100101001010010001010");
    }
}
