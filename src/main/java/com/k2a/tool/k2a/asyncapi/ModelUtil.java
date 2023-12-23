package com.k2a.tool.k2a.asyncapi;

import java.util.HashMap;
import java.util.Map;

public class ModelUtil {
    public static final String SERVER_KAFKA_NAME = "cluster";
    public static final String SERVER_REGISTRY_NAME = "schema-registry";

    public static Map<String, String> ref(String ref) {
        Map<String, String> map = new HashMap<>();
        map.put("$ref", ref);
        return map;
    }
}
