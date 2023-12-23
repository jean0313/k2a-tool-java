package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Contact {

    private String name;

    private String url;

    private String email;

}