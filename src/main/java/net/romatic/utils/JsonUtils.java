package net.romatic.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author zhrlnt@gmail.com
 */
public class JsonUtils {
    public static String toJson(Object object) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseToObject(String json, Class clz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (T) mapper.readValue(json, clz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param json
     * @param obj
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T parseToObject(String json, T obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            obj = (T) mapper.readValue(json, obj.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }
}
