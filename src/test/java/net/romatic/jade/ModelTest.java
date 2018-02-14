package net.romatic.jade;

import junit.framework.TestCase;
import net.romatic.com.carbon.Carbon;
import net.romatic.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelTest extends TestCase {

    @Test
    public void testModel() {
        List<User> users = new User().newQuery()
                .where("name", "hui")
                .orWhere("name", "xin")
                .whereIn("age", new int[]{18, 24})
                .whereHas("posts.tags", (query) -> ((Builder) query).where("name", "MySQL"))
                .take(3)
                .get();
    }

    @Test
    public void testHydrate() {
        Map<String, Object> attr = new HashMap<>();
        attr.put("id", 3);
        attr.put("name", "huiren");
        attr.put("created_at", "2010-01-01 00:00:00");

        System.out.println(JsonUtils.toJson(attr));

        User u = new User().newInstance(attr);
        System.out.println(u.toJson());
    }

    @Test
    public void test__set() throws NoSuchFieldException {
        User user = new User();
        user.__set("id", 3L);
        Assert.assertEquals(user.getId() - 3, 0);
    }

    @Test
    public void testFill() {
        Map<String, Object> attr = new HashMap<>();
        attr.put("id", 3);
        attr.put("name", "huiren");
        attr.put("created_at", "2010-01-01 00:00:00");

        User u = new User().fill(attr);

        System.out.println(u.toJson());
        u = JsonUtils.parseToObject("{\"createdAt\":\"2010-01-01 00:00:00\",\"id\":\"1\",\"name\":\"huiren\"}", User.class);
        System.out.println(u);
        System.out.println(u.toJson());

    }

    @Test
    public void testSerialize() {
        User u = new User();
        u.setId(3L);
        u.setName("huiren");
        u.setCreatedAt(Carbon.of(1994, 3, 13));

        Assert.assertEquals(u.toJson(), "{\"createdAt\":\"1994-03-13 00:00\",\"updatedAt\":null,\"deletedAt\":null,\"id\":3,\"name\":\"huiren\"}");
    }

    @Test
    public void testUnSerialize() {
        String json = "{\"createdAt\":\"1994-03-13 00:00\",\"updatedAt\":null,\"deletedAt\":null,\"id\":3,\"name\":\"huiren\"}";
        User u = JsonUtils.parseToObject(json, User.class);

        Assert.assertEquals(u.getId() - 3, 0);
        Assert.assertEquals(u.getName(), "huiren");
        Assert.assertEquals(u.getCreatedAt(), Carbon.of(1994, 3, 13));
        Assert.assertEquals(u.toJson(), json);
    }
}
