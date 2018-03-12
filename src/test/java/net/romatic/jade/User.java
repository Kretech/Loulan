package net.romatic.jade;

import javafx.geometry.Pos;
import net.romatic.com.collection.Models;
import net.romatic.jade.annotation.*;
import net.romatic.jade.annotation.Query;

import java.util.List;

@Connection(name = "db0")
@Query(UserQuery.class)
public class User extends Model {
    protected Long id;

    protected String name;

    @HasOne(relatedKey = "author_id")
    protected Post post;

    @HasMany(relatedKey = "author_id")
    @MappingClass(Post.class)
    protected Models<Post> posts;

    public static UserQuery query() {
        return new User().newQuery();
    }

    public Models<Post> getPosts() {
        return posts;
    }

    public void setPosts(Models<Post> posts) {
        this.posts = posts;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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
