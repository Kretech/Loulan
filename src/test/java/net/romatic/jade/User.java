package net.romatic.jade;

import net.romatic.jade.annotation.Connection;
import net.romatic.jade.annotation.Query;

@Connection(name = "db0")
@Query(UserQuery.class)
public class User extends Model {
    protected Long id;

    protected String name;

    public static UserQuery query() {
        return new User().newQuery();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
