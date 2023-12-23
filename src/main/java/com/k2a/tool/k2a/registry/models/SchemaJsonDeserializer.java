package com.k2a.tool.k2a.registry.models;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class SchemaJsonDeserializer extends JsonDeserializer<HashMap<String, Object>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public HashMap<String, Object> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectCodec codec = jsonParser.getCodec();

        String str = codec.readValue(jsonParser, String.class);
        HashMap<String, Object> map = objectMapper.readValue(str, new TypeReference<>() {
        });
        return map;
    }
}
