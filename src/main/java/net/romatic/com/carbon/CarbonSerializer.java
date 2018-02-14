package net.romatic.com.carbon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author zhrlnt@gmail.com
 */
public class CarbonSerializer extends JsonSerializer<Carbon> {
    @Override
    public void serialize(Carbon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());
    }
}
