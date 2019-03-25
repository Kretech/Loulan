package net.romatic.cmd;

import net.romatic.utils.WordUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhrlnt@gmail.com
 */
public class QueryProxyGenerator {
    private static QueryProxyGenerator ourInstance = new QueryProxyGenerator();

    public static QueryProxyGenerator getInstance() {
        return ourInstance;
    }

    private QueryProxyGenerator() {
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String className = "net.romatic.jade.User";
        String folder = "src/main/java";
        //String folder = "src/test/java";

        Class clz = Class.forName(className);


        List<Method> methods = new ArrayList<>();
        for (Method method : clz.getDeclaredMethods()) {
            methods.add(method);
        }

        String newClassName = clz.getSimpleName() + "Query";
        String filename = folder + "/" + className.replaceAll("\\.", "/") + "Query.java";

        StringBuffer buf = new StringBuffer();
        buf.append("package " + clz.getPackage().getName() + ";\n");
        buf.append("public interface " + newClassName + " {\n");
        for (Method method : methods) {

            int mod = method.getModifiers();
            if (!Modifier.isPublic(mod)) {
                continue;
            }

            if (!(method.getName().length() > 5 && "scope".equals(method.getName().substring(0, 5)))) {
                continue;
            }

            String newMethodName = WordUtils.lowerFirst(method.getName().substring(5));

            //  @todo method.getModifiers()
            for (Annotation annotation : method.getAnnotations()) {
                buf.append(String.format("@%s\n", annotation.getClass().getName()));
            }
            String params = String.join(", ", Arrays.stream(method.getParameters())
                    .skip(1)
                    .map(parameter -> {
                        String name;

                        if (parameter.getType().isPrimitive()) {
                            name = parameter.getName();
                        } else {
                            name = WordUtils.lowerFirst(parameter.getType().getSimpleName());
                        }

                        return parameter.getType().getName() + " " + name;
                    }).toArray(String[]::new));

            buf.append(String.format("\t%s %s(%s);\n\n", method.getReturnType().getName(), newMethodName, params));
        }
        buf.append("}");

        System.out.println(buf);

//        FileUtils.write(new File(filename), buf);
    }

    public static void writeToFile() {

    }
}
