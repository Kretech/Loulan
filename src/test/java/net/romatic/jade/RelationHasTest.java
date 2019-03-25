package net.romatic.jade;

import net.romatic.TestCaseBase;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huiren
 */
public class RelationHasTest extends TestCaseBase {

    @Test
    public void testHas() {
        Query query = Post.query().has("author", (userQuery) -> {
            userQuery.where("id", 2);
        });

        dump(query.getQuery().toSQL());
    }

    @Test
    public void testHasAuthor() {
        // 查询作者名叫"huiren"的所有文章
        List<Post> posts = Post.query()
                .has("author", userQuery -> userQuery.where("name", "huiren"))
                .with("author")
                .get();
        Assert.assertEquals(posts.get(0).author.name, "huiren");
    }

    @Test
    public void testDemo() throws NoSuchMethodException {
        Tag.query().where(PostQuery::published, 3, "a");
//        Method method = PostQuery.class.getMethod("published");
//        dump(method.isDefault());
    }
}
