package com.k2a.tool.gen.models;

import lombok.Data;

@Data
public class GlobalContext {
    private String title;

    private String version;
    
    private String appDescription;

    private String brokerUrl;

    private String registryUrl;

    private String l3Domain;

    private String applicationCode;

    private String module;

    private String groupId;

    private String artifactId;

    private String description;

    private String packageName;

    private String releaseVersion;

    private String customCommitMessage;

    private String specFilePath;

    private String destDir;
}
