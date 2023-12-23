package com.k2a.tool.k2a.asyncapi.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Info {

    private String title;

    private String version;

    private String description;

    private String termsOfService;

    private Contact contact;

    private License license;

}