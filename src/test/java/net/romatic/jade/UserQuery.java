package net.romatic.jade;

public interface UserQuery extends Query<User> {

    /**
     * 未删除的
     *
     * @return
     */
    default UserQuery notDeleted() {
        return where("", 0);
    }

}