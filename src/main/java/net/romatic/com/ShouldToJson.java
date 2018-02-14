package net.romatic.com;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.romatic.utils.JsonUtils;

import java.io.IOException;

/**
 * @author zhrlnt@gmail.com
 */
public interface ShouldToJson<T> {
    /**
     * Json 序列化
     *
     * @return
     */
    default String toJson() {
        return JsonUtils.toJson(this);
    }

    /**
     * 从Json解析对象
     *
     * @param json
     * @return
     */
    default T parseJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (T) mapper.readValue(json, this.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
