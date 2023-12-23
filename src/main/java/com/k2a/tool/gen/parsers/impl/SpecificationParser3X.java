package com.k2a.tool.gen.parsers.impl;

import com.k2a.tool.gen.constant.SpecificationConstant;
import com.k2a.tool.gen.enums.GenType;
import com.k2a.tool.gen.models.GenerateContext;
import com.k2a.tool.gen.parsers.SpecificationParser;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.Ref;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;

public class SpecificationParser3X implements SpecificationParser {
    private static final Logger log = LoggerFactory.getLogger(SpecificationParser3X.class);
    private JsonObject json;

    public SpecificationParser3X(JsonObject json) {
        this.json = json;
    }

    @Override
    public List<GenerateContext> parse() {
        JsonObject opsMap = json.getJsonObject(SpecificationConstant.OPERATIONS);
        Set<String> opNames = opsMap.fieldNames();
        Map<String, Operation> cNameToOps = new HashMap<>();
        for (String opName : opNames) {
            String channelName = Optional.ofNullable(opsMap.getJsonObject(opName))
                    .map(v -> v.getJsonObject(SpecificationConstant.CHANNEL))
                    .filter(v -> v.containsKey(SpecificationConstant.REF))
                    .map(v -> v.getString(SpecificationConstant.REF))
                    .map(v -> {
                        String[] splits = v.split("/");
                        return splits[splits.length - 1];
                    })
                    .orElseThrow(() -> new IllegalArgumentException("ERROR: Operations should use ref to link channel!"));
            log.debug("channel: {}  --> op: {}", channelName, opName);
            String action = opsMap.getJsonObject(opName).getString(SpecificationConstant.ACTION);
            cNameToOps.put(channelName, new Operation(opName, channelName, action));
        }

        JsonObject resolved = Ref.resolve(json);
        JsonObject channelsObj = resolved.getJsonObject(SpecificationConstant.CHANNELS);

        List<GenerateContext> ctxes = new ArrayList<>();
        Set<String> channelNames = channelsObj.fieldNames();
        for (String channelName : channelNames) {
            GenerateContext ctx = new GenerateContext();
            ctx.setChannelName(channelName);

            GenType genType = Optional.ofNullable(cNameToOps.get(channelName))
                    .map(Operation::getAction)
                    .map(v -> {
                        switch (v) {
                            case SpecificationConstant.OP_SEND -> {
                                return GenType.publish;
                            }
                            case SpecificationConstant.OP_RECEIVE -> {
                                return GenType.subscribe;
                            }
                            default -> throw new IllegalArgumentException("ERROR: Operation[" + v + "] not support!");
                        }
                    }).orElse(GenType.publish);
            ctx.setType(genType);

            JsonObject channelObj = channelsObj.getJsonObject(channelName);
            JsonObject messagesObj = channelObj.getJsonObject(SpecificationConstant.MESSAGES);
            Set<String> messageNames = messagesObj.fieldNames();

            Assert.isTrue(messageNames.size() == 1, "ERROR: channel only supports one message, error channel[" + channelName + "]");
            for (String messageName : messageNames) {
                ctx.setMessageName(messageName);

                JsonObject messageObj = messagesObj.getJsonObject(messageName);
                JsonObject payloadObj = messageObj.getJsonObject(SpecificationConstant.PAYLOAD);

                ctx.setMessageSchemaFormat(getSchemaFormat(payloadObj));

                String schemaName = Optional.ofNullable(payloadObj)
                        .map(v -> v.getString("name"))
                        .orElse(messageName);
                ctx.setSchemaName(schemaName);
                ctx.setMessageSchema(payloadObj.toString());
            }
            ctxes.add(ctx);
        }
        return ctxes;
    }

    @Getter
    @AllArgsConstructor
    private static class Operation {
        private String operationName;
        private String channelName;
        private String action;
    }
}
