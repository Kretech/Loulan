package net.romatic.jade;

/**
 * @author huiren
 */
public interface PostQuery extends Query<Post> {

    default PostQuery published() {
        return where("status", "published");
    }

    default UserQuery authorQuery() {
        System.out.println("...");
        return null;
    }

}
