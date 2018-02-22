# Loulan

[Laravel-Eloquent](https://laravel.com/docs/eloquent) for java



[TOC]

## Why ?

https://www.jianshu.com/p/f3bf0af7a1ea



## Quick Start

```java
//  作者叫 庆，标签为 java 的所有已发布文章
List<Post> posts = Post.query
  .with("author", "tags")
  .has("author.name", "庆")
  .has("tags", query -> query.whereIn("name", "java"))
  .published()
  .get();
```



## Scope

### Plugins

#### Soft Delete

```java
@SoftDelete()
class Post extends Model { 
}
```



### Custom Query

```java
interface PostQuery extends Query<Post> {
  
  default PostQuery published(Query query) {
    return query.where("status", "published");
  }
  
}
```



## Hooks



## Eager Loading

```java
Post post = Post.query.with("author", "tags").first();
System.out.println(post.author.name);
```



## Scalable

```java
@Connection(name = "db0")
@Table(name = "post")
class Post extends Model {
  
  public String getTable() {
    return super.getTable() + getId() % 10;
  }

}
```

properties:

```properties
loulan.connection.default = "db0"

loulan.connections.db0.dsn = "jdbc:mysql://127.0.0.1:3306/test"
loulan.connections.db0.user = "user"

loulan.connections.db1.dsn = "jdbc:mysql://127.0.0.1:3306/test"
loulan.connections.db1.read.user = "reader"
loulan.connections.db1.write.user = "writer"
```

