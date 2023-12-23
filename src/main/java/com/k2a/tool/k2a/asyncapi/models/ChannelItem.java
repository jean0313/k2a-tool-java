package com.k2a.tool.k2a.asyncapi.models;

import com.k2a.tool.k2a.asyncapi.ModelUtil;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChannelItem {

    private String description;

    private List<String> servers = List.of(ModelUtil.SERVER_KAFKA_NAME);

    private Operation subscribe;

    private Operation publish;

    private Map<String, Object> bindings;
}