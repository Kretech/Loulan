package net.romatic.jade;

/**
 * @author huiren
 */
public interface PostQuery extends Query<Post> {

    default UserQuery authorQuery() {
        System.out.println("...");
        return null;
    }

}
