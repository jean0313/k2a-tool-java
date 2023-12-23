package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Message {

    private String messageId;

    private Object headers;

    private Object payload;

    private Object correlationId;

    private String schemaFormat;

    private String contentType;

    private String name;

    private String title;

    private String summary;

    private String description;

    private Map<String, Object> bindings;
}