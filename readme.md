# Loulan

[Laravel-Eloquent](https://laravel.com/docs/eloquent) for java



[TOC]

## Why ?

https://www.jianshu.com/p/f3bf0af7a1ea



## Overview

```java
//  作者叫 庆，标签为 java 的所有已发布文章
List<Post> posts = Post.query
  .with("author", "tags")				// 预加载，自动把数据组装到 post.author 对象 
  .has("author.name", "庆")				// 一对多嵌套查询
  .has("tags", query -> query.whereIn("name", "java"))	// 多对多嵌套查询
  .published()						// 封装好的业务查询
  .get();
```



相当于以下三条 SQL 的结果合并起来。当然，真的想合并到对应的属性里，你还得一堆java代码。现在有了 Loulan，只需要上面几行代码。

```sql
--	post
SELECT post.*
FROM post
where status = 'published' and where post.author_id in (
	select id
	FROM author
	where name = '庆'
) and where post.id in (
	select post_id
	FROM post_tag
	where tag_id in (
		select id
	from tag
	where `name` in "java"
	)
)

-- author
select *
from author
where id in (post1.id, post2.id, ...);

-- tag
select *
from tag
where id in (
	select tag_id
	from tag
	where post_id in (post1.id, ...)
)
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

