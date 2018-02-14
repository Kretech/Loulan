package net.romatic.jade;

import net.romatic.query.QueryBuilder;

/**
 * @author huiren
 */
public class UserQueryImpl extends Builder<User> {

    public UserQueryImpl(QueryBuilder query) {
        super(query);
    }

    @Override
    public User first() {
        return super.first();
    }
}
