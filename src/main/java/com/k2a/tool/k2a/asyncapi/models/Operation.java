package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Operation {

    private String operationId;

    private String summary;

    private String description;

    private List<Map<String, List<String>>> security;

    private Map<String, Object> bindings;

    private Object message;

}
