package com.terfehr.homehub.infrastructure.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Locale;

/**
 * A Jackson deserializer that normalizes string input by trimming
 * whitespace, collapsing multiple whitespace characters into one,
 * and converting the value to lower case using {@link Locale#ROOT}.
 * <p>
 * Non-string JSON tokens (numbers, booleans, objects, arrays) will
 * yield {@code null} when parsed via {@link JsonParser#getValueAsString()}.
 */
public class TrimAndLowerCaseStringDeserializer extends StdDeserializer<String> {

    public TrimAndLowerCaseStringDeserializer() {
        super(String.class);
    }

    /**
     * Deserializes a JSON value and normalizes it.
     *
     * @param jsonParser             the JSON parser positioned at the value to deserialize
     * @param deserializationContext the deserialization context
     * @return a trimmed and lower-cased string, or {@code null} if the JSON value is null
     * @throws IOException if an I/O or parsing error occurs
     */
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        return value != null ? value.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT) : null;
    }
}
