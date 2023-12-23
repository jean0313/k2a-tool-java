package com.k2a.tool.gen.parsers;

import com.k2a.tool.gen.constant.SpecificationConstant;
import com.k2a.tool.gen.parsers.impl.SpecificationParser2X;
import com.k2a.tool.gen.parsers.impl.SpecificationParser3X;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpecificationParserFactory {
    private static final String V2 = "2";

    private static final String V3 = "3";

    public SpecificationParser create(String content) {
        JsonObject json = new JsonObject(content);

        String apiVersion = json.getString(SpecificationConstant.ASYNC_API);
        Assert.notNull(apiVersion, "Async API must not be null!");

        if (apiVersion.startsWith(V2)) {
            return new SpecificationParser2X(json);
        } else if (apiVersion.startsWith(V3)) {
            return new SpecificationParser3X(json);
        }
        throw new IllegalArgumentException("Error,not support Async API version:" + apiVersion);
    }
}
