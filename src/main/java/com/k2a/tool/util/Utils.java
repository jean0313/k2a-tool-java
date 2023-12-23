package com.k2a.tool.util;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static String toLowerCamelCase(String name) {
        return StringUtils.uncapitalize(toUpperCamelCase(name));
    }

    public static String toUpperCamelCase(String name) {
        Assert.notNull(name, "name cannot be null!");
        String[] splits = name.split("[-|_]");
        return Arrays.stream(splits).map(StringUtils::capitalize).collect(Collectors.joining());
    }
}
