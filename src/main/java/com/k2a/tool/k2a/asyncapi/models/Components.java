package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Components {

    private Map<String, Object> schemas = new HashMap<>();

    private Map<String, Object> messages = new HashMap<>();

    private Map<String, Object> securitySchemes;

    private Map<String, Object> serverBindings;

    private Map<String, Object> channelBindings;

    private Map<String, Object> operationBindings;

    private Map<String, Object> messageBindings;

}
