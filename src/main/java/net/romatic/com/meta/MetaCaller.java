package net.romatic.com.meta;

import java.lang.reflect.Method;

/**
 * @author zhrlnt@gmail.com
 */
public interface MetaCaller {

    /**
     * call method on this
     *
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    default <T> T __call(String methodName, Object... args) {
//        System.out.print("=>" + methodName + "(");
        try {
            Class[] classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classes[i] = args[i].getClass();
//                System.out.print(classes[i].getName() + " ");
            }
//            System.out.println(")");
            Method method = this.getClass().getMethod(methodName, classes);
            return (T) method.invoke(this, args);
        } catch (NoSuchMethodException e) {
            System.out.println("\t=> Not Found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return null;
    }

}
