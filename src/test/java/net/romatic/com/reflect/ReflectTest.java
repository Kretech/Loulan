package net.romatic.com.reflect;

import net.romatic.TestCaseBase;
import net.romatic.jade.User;
import net.romatic.jade.annotation.BelongsTo;
import net.romatic.jade.annotation.Relation;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhrlnt@gmail.com
 */
public class ReflectTest extends TestCaseBase {

    @Test
    public void testListFields() {
        List fields = ReflectUtils.listFields(User.class);

        List<String> names = ReflectUtils.listFieldNames(User.class);
        System.out.println(names);
    }

    @Test
    public void testDemo() {
        Arrays.stream(BelongsTo.class.getAnnotations()).forEach(
                a ->
                        System.out.println(a)
        );

        Annotation annotation = BelongsTo.class.getAnnotation(Relation.class);
        System.out.println(0);
        System.out.println(annotation);
        //dump(BelongsTo.class.getAnnotations());
    }
}
