package net.romatic;

import junit.framework.TestCase;
import net.romatic.utils.JsonUtils;

/**
 * @author huiren
 */
abstract public class TestCaseBase extends TestCase {

    public <T> void dump(T... objects) {
        for (T obj : objects) {
            if (null == obj
                    || obj.getClass().isPrimitive()) {
                print(obj);
            } else {
                print(JsonUtils.toJson(obj));
            }
        }
    }

    public <T> void print(T object) {
        System.out.println("Console => " + object);
    }
}
