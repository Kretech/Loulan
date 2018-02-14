package net.romatic.jade;

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

//    @Scope
//    public static UserQuery notDeleted(UserQuery query) {
//        return query;
//    }

    public void setName(String name) {
        this.name = name;
    }
}
