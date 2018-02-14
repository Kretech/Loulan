package net.romatic.com.carbon;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author zhrlnt@gmail.com
 */
public class CarbonUnSerializer extends JsonDeserializer<Carbon> {
    @Override
    public Carbon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String txt = p.getText();
        return Carbon.of(txt);
    }
}
