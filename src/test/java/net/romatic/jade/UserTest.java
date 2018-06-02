package net.romatic.jade;

import net.romatic.TestCaseBase;
import net.romatic.com.collection.Models;
import org.junit.Test;

import java.util.List;

/**
 * @author zhrlnt@gmail.com
 */
public class UserTest extends TestCaseBase {

    @Test
    public void testBasicQuery() {

        UserQuery query = User.query().where("id", 1);

        String sql = query
                .getQuery()
                .toSQL();
        System.out.println(sql);

        List<User> a = query.get();

        System.out.println(a.get(0).toJson());

        User u2 = User.query().first();
        System.out.println(u2.toJson());

        User u3 = User.query().find(1);
        System.out.println(u3.toJson());

        User.query().notDeleted().first();
    }

    public void testDemo() {
        User u = new User();
        System.out.println(u.id);
    }
}
