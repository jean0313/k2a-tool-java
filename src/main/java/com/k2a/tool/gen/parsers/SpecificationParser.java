package com.k2a.tool.gen.parsers;

import com.k2a.tool.gen.enums.SchemaFormat;
import com.k2a.tool.gen.models.GenerateContext;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface SpecificationParser {
    List<GenerateContext> parse();

    default SchemaFormat getSchemaFormat(JsonObject payloadObj) {
        if (payloadObj.containsKey("properties")) {
            return SchemaFormat.json;
        } else if (payloadObj.containsKey("fields")) {
            return SchemaFormat.avro;
        }
        throw new IllegalArgumentException("ERROR: schema not support:" + payloadObj);
    }
}
