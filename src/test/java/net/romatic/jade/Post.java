package net.romatic.jade;

import net.romatic.com.collection.Models;
import net.romatic.jade.annotation.*;

/**
 * @author huiren
 */
@Connection(name = "db0")
public class Post extends Model {

    protected Long id;
    protected String title;

    @Column("author_id")
    protected Long authorId;

    protected String content;

    //@BelongsTo(localKey = "authorId", relatedKey = "id")
    @BelongsTo()
    @Foreign(keyName = "authorId")
    protected User author;

    @BelongsToMany(localKey = "post_id", relatedKey = "tag_id", table = "post_tag")
    @MappingClass(Tag.class)
    protected Models<Tag> tags;

    public static PostQuery query() {
        return new Post().newQuery();
    }

    public Models<Tag> getTags() {
        return tags;
    }

    public void setTags(Models<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
