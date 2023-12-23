package com.k2a.tool.k2a.registry.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;

import java.util.Map;

@ToString
public class Schema {
    private String subject;
    private Integer version;
    private Integer id;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSchemaType(String schemaType) {
        this.schemaType = schemaType;
    }

    public Map<String, Object> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, Object> schema) {
        this.schema = schema;
    }

    private String schemaType = SchemaType.SCHEMA_TYPE_AVRO;

    @JsonDeserialize(using = SchemaJsonDeserializer.class)
    private Map<String, Object> schema;

    public String getSchemaType() {
        if (schema.containsKey("fields")) {
            return SchemaType.SCHEMA_TYPE_AVRO;
        } else if (schema.containsKey("properties")) {
            return SchemaType.SCHEMA_TYPE_JSON;
        }
        return SchemaType.SCHEMA_TYPE_AVRO;
    }
}

