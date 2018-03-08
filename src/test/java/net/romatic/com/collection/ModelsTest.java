package net.romatic.com.collection;

import junit.framework.TestCase;
import net.romatic.jade.User;
import net.romatic.utils.JsonUtils;
import org.junit.Test;

/**
 * @author huiren
 */
public class ModelsTest extends TestCase {

    @Test
    public void testDemo() {
        Models<User> users = new Models();
        users.add((User) new User().parseJson("{\"id\":5}"));
        users.add((User) new User().parseJson("{\"id\":3}"));

        System.out.println(JsonUtils.toJson(users));
        System.out.println(JsonUtils.toJson(users.keyBy("id")));

        System.out.println(users.pluck("id"));

        System.out.println(
                users.filter(x -> x.getId() > 3).pluck("id")
        );
    }
}
