package net.romatic.jade;

import net.romatic.jade.annotation.Connection;

@Connection(name = "db0")
public class User extends Model {
    protected Long id;

    protected String name;

    public static UserQuery query() {
        return new User().newQueryProxy(new JadeProxyHandler(new User().newQuery()), UserQuery.class);
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
