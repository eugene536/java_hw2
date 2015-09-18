package ru.ifmo.ctddev.Nemchenko.reflection;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class can checks the relations between classes, which full names have to passing through input file.
 * <p>
 * target function: {@link #checkClasses(String)}
 * </p>
 */
public class Reflection {
    private final static String inFile = "in.txt";
    private String firstName;
    private String secondName;

    /**
     * only construct this class, and do nothing.
     */
    public Reflection() {
    }

    /**
     * Checks the relations between classes, which full names will be read from input file {@code inputFile}.
     * <p>
     * Prints messages on standard output:
     * <ul>
     *   <li> if classes are same </li>
     *   <li> if classes were declared in the same packages </li>
     *   <li> if one of these classes is parent of another </li>
     * </ul>
     * </p>
     * <p>
     *  And Print longest common ancestor and all common interfaces which classes implemented. </li>
     * </p>
     * @param inputFile target input file which will be used for reading full names of classes
     */
    public void checkClasses(String inputFile) {
        Charset charset = Charset.forName("UTF-8");
        Path inPath = Paths.get(inputFile);

        try (BufferedReader in = Files.newBufferedReader(inPath, charset)) {
            firstName = in.readLine();
            secondName = in.readLine();
        } catch (NoSuchFileException e) {
            System.err.println("no such file: " + e.getMessage());
            return;
        } catch (InvalidPathException e) {
            System.err.println("invalid path: " + e.getMessage());
            return;
        } catch (FileNotFoundException e) {
            System.err.println("file not found: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("IOException occured: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Class firstClass;
        Class secondClass;
        try {
            firstClass = Class.forName(firstName);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found: " + firstName);
            return;
        }

        try {
            secondClass = Class.forName(secondName);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found: " + secondName);
            return;
        }

        checkRelations(firstClass, secondClass);
    }

    private void checkRelations(Class firstClass, Class secondClass) {
        if (firstClass.equals(secondClass)) {
            System.out.println("They are same");
        } else {
            if (firstClass.getPackage() == secondClass.getPackage()) {
                System.out.println("They are in same packages");
            }

            if (firstClass.getSuperclass().equals(secondClass)) {
                System.out.println(secondName + " is parent of " + firstName);
            } else if (secondClass.getSuperclass().equals(firstClass)) {
                System.out.println(firstName + " is parent of " + secondName);
            }

            for (Class copyFirst = firstClass; copyFirst != null; copyFirst = copyFirst.getSuperclass()) {
                if (copyFirst.isAssignableFrom(secondClass)) {
                    System.out.println(copyFirst.getCanonicalName() + " is common ancestor of classes: "
                            + firstName + " and " + secondName);
                    break;
                }
            }

            Set<Class<?>> interfaces = new HashSet<>();
            getAllInterfaces(firstClass, interfaces);
            ArrayList<Class<?>> commonInterfaces = new ArrayList<>();
            for (Class curInterface : interfaces) {
                if (curInterface.isAssignableFrom(secondClass)) {
                    commonInterfaces.add(curInterface);
                }
            }

            if (!commonInterfaces.isEmpty()) {
                System.out.println();
                System.out.println("common interfaces: ");
                for (Class curInterface : commonInterfaces) {
                    System.out.println(curInterface.getCanonicalName());
                }
            }
        }
    }

    private void getAllInterfaces(Class clazz, Set<Class<?>> interfaces) {
        if (clazz == null) {
            return;
        }

        getAllInterfaces(clazz.getSuperclass(), interfaces);

        for (Class curInterface : clazz.getInterfaces()) {
            if (!interfaces.contains(curInterface)) {
                interfaces.add(curInterface);
                getAllInterfaces(curInterface, interfaces);
            }
        }
    }

    /**
     * Only for demonstrating work with Reflection class
     * @param args not used
     */
    public static void main(String... args) {
        new Reflection().checkClasses(inFile + "asdf");
    }
}
