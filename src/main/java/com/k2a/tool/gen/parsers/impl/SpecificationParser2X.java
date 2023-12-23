package com.k2a.tool.gen.parsers.impl;

import com.k2a.tool.gen.constant.SpecificationConstant;
import com.k2a.tool.gen.enums.GenType;
import com.k2a.tool.gen.models.GenerateContext;
import com.k2a.tool.gen.parsers.SpecificationParser;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.Ref;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

public class SpecificationParser2X implements SpecificationParser {

    private JsonObject json;

    public SpecificationParser2X(JsonObject json) {
        this.json = json;
    }

    public SpecificationParser2X() {

    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    @Override
    public List<GenerateContext> parse() {
        Map<String, String> channelNameToMsgNameMapping = extractChannelMessageMapping(json);

        JsonObject resolved = Ref.resolve(json);
        JsonObject channelsObj = resolved.getJsonObject(SpecificationConstant.CHANNELS);
        Set<String> channelNames = channelsObj.fieldNames();
        List<GenerateContext> ctxes = new ArrayList<>();
        for (String channelName : channelNames) {
            GenerateContext ctx = new GenerateContext();
            ctx.setChannelName(channelName);

            String msgName = channelNameToMsgNameMapping.get(channelName);
            ctx.setMessageName(msgName);

            JsonObject channelItemObj = channelsObj.getJsonObject(channelName);
            parseOperation(SpecificationConstant.SUBSCRIBE, channelItemObj, ctx);
            if (!StringUtils.hasLength(ctx.getSchemaName())) {
                parseOperation(SpecificationConstant.PUBLISH, channelItemObj, ctx);
            }
            Assert.hasText(ctx.getSchemaName(), "ERROR: cann't found schemaName for message:" + msgName);
            ctxes.add(ctx);
        }
        return ctxes;
    }

    private void parseOperation(String op, JsonObject channelItemObj, GenerateContext ctx) {
        JsonObject channelObj = channelItemObj.getJsonObject(op);
        JsonObject payloadObj = Optional.ofNullable(channelObj)
                .map(v -> v.getJsonObject(SpecificationConstant.MESSAGE))
                .map(v -> v.getJsonObject(SpecificationConstant.PAYLOAD))
                .orElseThrow(() -> new IllegalArgumentException("ERROR: playload should not be null"));
        ctx.setType(SpecificationConstant.SUBSCRIBE.equals(op) ? GenType.subscribe : GenType.publish);
        ctx.setMessageSchema(payloadObj.toString());

        // schema format
        ctx.setMessageSchemaFormat(getSchemaFormat(payloadObj));

        String schemaName = Optional.of(payloadObj)
                .map(v -> v.getString(SpecificationConstant.NAME))
                .orElse("");
        ctx.setSchemaName(schemaName);
    }

    public Map<String, String> extractChannelMessageMapping(JsonObject unresolvedJson) {
        JsonObject channelsObj = unresolvedJson.getJsonObject(SpecificationConstant.CHANNELS);
        Map<String, String> map = new HashMap<>();
        Set<String> channelNames = channelsObj.fieldNames();
        for (String channelName : channelNames) {
            String messageName = tryToGetMsgName(channelsObj, channelName, SpecificationConstant.PUBLISH);
            if (!StringUtils.hasLength(messageName)) {
                messageName = tryToGetMsgName(channelsObj, channelName, SpecificationConstant.SUBSCRIBE);
            }
            Assert.hasLength(messageName, "ERROR: channel should use ref to link message");
            map.put(channelName, messageName);
        }
        return map;
    }

    private static String tryToGetMsgName(JsonObject channelsObj, String channelName, String op) {
        return Optional.ofNullable(channelsObj.getJsonObject(channelName))
                .map(v -> v.getJsonObject(op))
                .map(v -> v.getJsonObject(SpecificationConstant.MESSAGE))
                .map(v -> v.getString(SpecificationConstant.REF))
                .map(v -> {
                    String[] splits = v.split("/");
                    return splits[splits.length - 1];
                }).orElse("");
    }
}
