package ru.ifmo.ctddev.Nemchenko.Implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * An {@link info.kgeorgiy.java.advanced.implementor.JarImpler} dummy implementation.
 * It may implement interfaces and abstract classes and zip them into jar file.
 * <p>
 * For the given type token
 * (by invoking {@link #implement(Class, java.io.File)} or {@link #implement(Class, java.io.File)})
 * or for the given class name (by passing through the command line to
 * {@link #main(String[])}) it will generate and write to the file
 * properly compiled implementation and can zip it if needed.
 * All methods of super-classes and super-interfaces of specified class,
 * that have not available implementations will be added to the source
 * code with returning corresponding default value.
 *
 * @author Eugene Nemchenko
 */
public class Implementor implements JarImpler {
    /**
     * system-dependent line feed
     */
    private static final String LF = String.format("%n");

    /**
     * copy of token given from {@link #implement(Class, java.io.File)}
     */
    Class<?> token;

    /**
     * using for indentation when realisation is writen to apropriate file.
     */
    private static final String INDENT = "    ";

    /**
     * using for indentation when realisation is writen to apropriate file.
     */
    private static final String DOUBLE_INDENT = "        ";

    /**
     * contain used methods by signature string representation
     */
    private HashMap<String, MyMethod> used = new HashMap<>();
    String s;
    /**
     * contain implemented methods by signature string representation
     */
    private HashMap<String, MyMethod> implemented = new HashMap<>();


    /**
     * path to implemented java file by invoking {@link #implement(Class, java.io.File)}
     */
    private Path pathToImplFile;

    /**
     * Construct exemplar of Implementor class, do nothing.
     */
    public Implementor() {
    }


    /**
     * Invokes {@link #implement(Class, java.io.File)} or {@link #implementJar(Class, java.io.File)}
     * with parameters passed through command line.
     *
     * @param args command line arguments.
     * @throws ImplerException if one of the following conditions is met:
     *                         <ul>
     *                         <li>Command line {@code args} are incorrect
     *                         <li>The passed class name cannot be located
     *                         <li>Error occurs during implementation of the class
     *                         </ul>
     */
    public static void main(String... args) throws ImplerException {
        if (args == null || args.length < 1 || args[0] == null) {
            System.out.println("please type full name of the class");
            return;
        }

        if (args.length == 1) {
            Implementor helper = new Implementor();
            Class inClass = null;
            try {
                inClass = Class.forName(args[0]);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found: " + args[0]);
            }
            helper.implement(inClass, new File(""));
            return;
        }

        if (args.length < 3 || args[1] == null || args[2] == null) {
            System.out.println("3 arguments required");
            return;
        }

        if (!args[0].equals("-jar")) {
            System.out.println("bad arguments; usage: -jar className fileName.jar");
            return;
        }

        Implementor helper = new Implementor();
        Class inClass = null;
        try {
            inClass = Class.forName(args[1]);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found: " + args[1]);
        }
        helper.implementJar(inClass, new File(args[2]));
    }

    /**
     * Produces code implementing class or interface specified by provided {@code token}
     * <p>
     * Generated class full name should be same as full name of the type token with {@code Impl} suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * {@code root} directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to {@code $root/java/util/ListImpl.java}
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be generated.
     */
    @Override
    public void implement(Class<?> token, File root) throws ImplerException {
        if (token == null) {
            throw new ImplerException("token has to be not null");
        }

        if (Modifier.isFinal(token.getModifiers())) {
            throw new ImplerException("token has to be not final");
        }

        if (token.isPrimitive()) {
            throw new ImplerException("token has to be non primitive type");
        }
        this.token = token;

        pathToImplFile = root.toPath().resolve(token.getPackage().getName().replace('.', File.separatorChar));
        String simpleName = token.getSimpleName() + "Impl";
        Path resultFile = Paths.get(simpleName + ".java");
        try {
            Files.createDirectories(pathToImplFile);
        } catch (IOException e) {
            throw new ImplerException(e);
        }

        pathToImplFile = pathToImplFile.resolve(resultFile);
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter out = Files.newBufferedWriter(pathToImplFile, charset)) {
            if (!token.getPackage().getName().isEmpty()) {
                out.write("package " + token.getPackage().getName() + ";" + LF + LF);
            }

            String whatDo = " extends ";
            if (Modifier.isInterface(token.getModifiers())) {
                whatDo = " implements ";
            }
            out.write("public class " + simpleName + whatDo + token.getName() + " {" + LF);

            addMethods(token);

            if (!token.isInterface()) {
                boolean constructorWritten = false;
                for (Constructor<?> constructor : token.getDeclaredConstructors()) {
                    int typesLength = constructor.getGenericParameterTypes().length;
                    if (!Modifier.isPrivate(constructor.getModifiers())) {
                        out.write(getModifiers(constructor));
                        out.write(simpleName + getParametersTypes(constructor)); // print signature

                        Type[] thrownExceptions = constructor.getGenericExceptionTypes();
                        if (thrownExceptions.length > 0) {
                            out.write(" throws ");
                            for (int i = 0; i < thrownExceptions.length - 1; i++) {
                                out.write(thrownExceptions[i].getTypeName() + ", ");
                            }
                            out.write(thrownExceptions[thrownExceptions.length - 1].getTypeName() + " ");
                        }

                        out.write("{" + LF);
                        if (typesLength > 0) {
                            out.write(DOUBLE_INDENT + "super(");
                            for (int i = 0; i < typesLength - 1; i++) {
                                out.write("arg" + i + ", ");
                            }
                            out.write("arg" + typesLength + ");");
                        }
                        out.write(LF + INDENT + "}" + LF);
                        constructorWritten = true;
                        break;
                    }
                }

                if (!constructorWritten) {
                    throw new ImplerException("need at least one not private constructor");
                }
            }

            for (HashMap.Entry<String, MyMethod> pair : used.entrySet()) {
                Method method = pair.getValue().getMethod();
                if (method.getAnnotations().length > 0) {
                    out.write(LF + INDENT + method.getDeclaredAnnotations()[0].toString());
                }
                out.write(getModifiers(method));
                out.write(method.getReturnType().getTypeName() + " ");
                out.write(pair.getKey()); // print signature
                out.write(getRealisation(method));
            }

            out.write("}" + LF);
        } catch (IOException e) {
            System.err.println("problem with file: " + e.getMessage());
            throw new ImplerException(e);
        }
    }

    /**
     * Add methods realisation to {@link #implemented} by signature of method
     *
     * @param clazz target class
     * @throws IOException when I/O error occurs
     */
    private void addMethods(Class<?> clazz) throws IOException {
        if (clazz == null) return;

        for (Method m : clazz.getDeclaredMethods()) {
            int modifiers = m.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                tryAddMethod(m);
            } else {
                String signature = getSignature(m);
                if (implemented.containsKey(signature)) {
                    implemented.get(signature).tryChange(m);
                } else {
                    implemented.put(signature, new MyMethod(m));
                }
            }
        }

        if (!clazz.isInterface())
            addMethods(clazz.getSuperclass());

        for (Class<?> nextInterface : clazz.getInterfaces()) {
            addMethods(nextInterface);
        }
    }

    /**
     * try add {@code method} to {@link #used}
     * if that {@code method} already implemented that nothing is done
     * if used contain signature of {@code method} that method will be inserted
     * if old method is assignable from {@code method}
     *
     * @param method which probably be inserted to @{link used}
     * @throws IOException if I/O Error occurs
     */
    private void tryAddMethod(Method method) throws IOException {
        String signature = getSignature(method);
        if (implemented.containsKey(signature)) {
            return;
        }
        if (used.containsKey(signature)) {
            used.get(signature).tryChange(method);
        } else {
            used.put(signature, new MyMethod(method));
        }
    }

    /**
     * return signature of {@code method}
     *
     * @param method target method
     * @return signature of {@code executable}
     * @since 1.8
     */
    private String getSignature(Method method) {
        return method.getName() + getParametersTypes(method);
    }

    /**
     * Generate string representation of signature from {@code executable}
     *
     * @param executable target executable
     * @return string representation of signature from {@code executable}
     * @since 1.8
     */
    private String getParametersTypes(Executable executable) {
        String result = "(";
        Type[] types = executable.getParameterTypes();
        if (types.length > 0) {
            for (int i = 0; i < types.length - 1; i++) {
                result += types[i].getTypeName() + " arg" + i + ", ";
            }
            String lastType = types[types.length - 1].getTypeName();
            if (executable.isVarArgs()) {
                result += lastType.substring(0, lastType.length() - 2) + "... arg" + types.length + ") ";
            } else {
                result += lastType + " arg" + types.length + ") ";
            }
        } else {
            result += ")";
        }
        return result;
    }

    /**
     * Generate string representation of modifiers from {@code executable}
     *
     * @param executable target executable
     * @return string representation of modifiers from {@code executable}
     * @since 1.8
     */
    private String getModifiers(Executable executable) {
        String result = LF;
        int modifiers = executable.getModifiers();
        if (Modifier.isPublic(modifiers)) {
            result += INDENT + "public ";
        } else {
            result += INDENT + "protected ";
        }

        return result;
    }

    /**
     * Generate string representation of realisation from {@code method}
     *
     * @param method target method
     * @return string representation of modifiers from {@code executable}
     */
    private String getRealisation(Method method) {
        String result = "";
        result += "{" + LF;
        if (method.getReturnType().isPrimitive()) {
            if (method.getReturnType() == boolean.class) {
                result += DOUBLE_INDENT + "return false;";
            } else if (method.getReturnType() == void.class) {
                result += "";
            } else {
                result += DOUBLE_INDENT + "return 0;";
            }
        } else {
            result += DOUBLE_INDENT + "return null;";
        }
        result += LF + INDENT + "}" + LF;
        return result;
    }

    /**
     * Produces <tt>.jar</tt> file implementing class or interface specified by provided <tt>token</tt>.
     * <p>
     * Generated class full name should be same as full name of the type token with <tt>Impl</tt> suffix
     * added.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <tt>.jar</tt> file.
     * @throws ImplerException when implementation cannot be generated.
     */
    @Override
    public void implementJar(Class<?> token, File jarFile) throws ImplerException {
        String curDir = System.getProperty("user.dir");
        implement(token, new File(curDir));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, pathToImplFile.toString());
        if (compilationResult != 0) {
            throw new ImplerException("error occured during compilation");
        }

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

        try (OutputStream jarFileStream = Files.newOutputStream(jarFile.toPath(), StandardOpenOption.CREATE_NEW);
             OutputStream os = new BufferedOutputStream(jarFileStream);
             JarOutputStream jos = new JarOutputStream(os, manifest)) {

            String pathToImpl = token.getPackage().getName().replace('.', File.separatorChar)
                    + File.separatorChar
                    + token.getSimpleName() + "Impl.class";
            ZipEntry entry = new ZipEntry(pathToImpl);
            jos.putNextEntry(entry);
            try (InputStream classFile = new FileInputStream(pathToImpl)) {
                int c;
                final int len = 1000;
                byte b[] = new byte[len];
                while ((c = classFile.read(b, 0, len)) != -1) {
                    jos.write(b, 0, c);
                }
            } catch (IOException e) {
                System.err.println("problem with .class file");
                throw new ImplerException(e);
            }
            jos.closeEntry();
        } catch (FileAlreadyExistsException e) {
            System.err.println("file " + jarFile.toString() + " already exist");
            throw new ImplerException(e);
        } catch (IOException e) {
            throw new ImplerException(e);
        }
    }

    /**
     * contain last exemplar of method
     */
    private class MyMethod {
        private Method method;

        public MyMethod(Method method) {
            this.method = method;
        }


        /**
         * @param method target method
         *               {@code method} will overwrite {@code this.method}
         *               if {@code this.method} is assignable from {@code method}
         */
        public void tryChange(Method method) {
            if (this.method.getReturnType().isAssignableFrom(method.getReturnType())) {
                this.method = method;
            }
        }

        public Method getMethod() {
            return method;
        }
    }
}
