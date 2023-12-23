package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class License {

    private String name;

    private String url;
}
