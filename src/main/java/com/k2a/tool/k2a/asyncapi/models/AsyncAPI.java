package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AsyncAPI {

    private String asyncapi = "2.6.0";

    private String id;

    private Info info;

    private Map<String, Server> servers = new HashMap<>();

    private String defaultContentType = "application/json";

    private Map<String, ChannelItem> channels = new HashMap<>();

    private Components components = new Components();

    public void addComponentsSchema(String name, Object schema) {
        components.getSchemas().put(name, schema);
    }

    public void addComponentsMessage(String name, Message message) {
        components.getMessages().put(name, message);
    }
}
