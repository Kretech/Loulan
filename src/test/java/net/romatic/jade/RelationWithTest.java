package net.romatic.jade;

import net.romatic.TestCaseBase;
import net.romatic.com.collection.Models;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author huiren
 */
public class RelationWithTest extends TestCaseBase {

    @Test
    public void testBelongsTo() {
        PostQuery query = Post.query().with("author");
        Post post = query.first();

        dump(post, post.author);
    }

    @Test
    public void testHasOne() {
        UserQuery query = User.query().with("post");
        User user = query.first();

        dump(user, user.post);
    }

    @Test
    public void testHasMany() {
        UserQuery query = User.query().with("posts");
        User user = query.first();

        user = User.query().with("posts", "publish_posts").first();
        dump(user, user.posts);
    }

    /**
     * m2m
     */
    @Test
    public void testBelongsToMany() {
        Post post = Post.query().with("tags").first();

        dump(post, post.tags);
    }

    @Test
    public void testLoadRelations() {
        Models posts = Post.query().with("author").get();

        dump(posts);
    }

    @Test
    public void testA() throws NoSuchFieldException {
        Field field = User.class.getDeclaredField("post");
        System.out.println(field);
        System.out.println(field.getClass());
        System.out.println(field.getDeclaringClass());
    }
}
