package com.k2a.tool.gen.models;

import com.k2a.tool.gen.enums.GenType;
import com.k2a.tool.gen.enums.SchemaFormat;
import lombok.Data;

@Data
public class GenerateContext {
    private String channelName;

    private GenType type;

    private String messageName;

    private String schemaName;

    private String messageSchema;

    private SchemaFormat messageSchemaFormat;

}
