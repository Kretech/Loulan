# Serializable · 序列化



## Json



### Model to Json string

```java
User u = User.query().first();
String str = u.toJson();
```



### JSON to Model

```java
String str = "";
User u = new User().parseJson(str);
```



### Custom Json Serializer

Json 序列化相关的接口都在 `net.romatic.com.ShouldToJson` 里，所以只需要在你自己的 `ModelBase` 里重写这两个方法即可。

```java
abstract class ModelBase extends Model {

	@Override
    String toJson() { return ""; }
    
}
```

 

