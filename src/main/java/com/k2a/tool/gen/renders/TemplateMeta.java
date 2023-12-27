package com.k2a.tool.gen.renders;

import com.k2a.tool.gen.enums.GenType;
import com.k2a.tool.gen.models.GenerateContext;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.File;

@Getter
public class TemplateMeta {
    private GenType type;
    private String templateName;

    // base on groupId/packageName/relativePath
    private String relativePath;

    private NameFormatter nameFormatter;
    private String fileName;

    private TemplateMeta(GenType type, String templateName, String relativePath, NameFormatter nameFormatter) {
        this.type = type;
        this.templateName = templateName;
        this.relativePath = relativePath;
        this.nameFormatter = nameFormatter;
    }

    public static TemplateMeta ofSubscribe(String templateName, String relativePath, NameFormatter nameFormatter) {
        return new TemplateMeta(GenType.subscribe, templateName, relativePath, nameFormatter);
    }

    public static TemplateMeta ofPublish(String templateName, String relativePath, NameFormatter nameFormatter) {
        return new TemplateMeta(GenType.publish, templateName, relativePath, nameFormatter);
    }

    public static TemplateMeta of(GenType type, String templateName, String relativePath, NameFormatter nameFormatter) {
        return new TemplateMeta(type, templateName, relativePath, nameFormatter);
    }

    public TemplateMeta(GenType type, String templateName, String relativePath) {
        this.type = type;
        this.templateName = templateName;
        this.relativePath = relativePath;
    }

    private TemplateMeta(GenType type, String templateName) {
        this.type = type;
        this.templateName = templateName;
        this.relativePath = "";
    }

    public static TemplateMeta ofCommon(String templateName) {
        return new TemplateMeta(GenType.common, templateName, "");
    }

    public static TemplateMeta ofCommon(String templateName, String relativePath) {
        return new TemplateMeta(GenType.common, templateName, relativePath);
    }

    public static TemplateMeta ofCommon(String templateName, String relativePath, String fileName) {
        return new TemplateMeta(GenType.common, templateName, relativePath, fileName);
    }

    private TemplateMeta(GenType type, String templateName, String relativePath, String fileName) {
        this.type = type;
        this.templateName = templateName;
        this.relativePath = relativePath;
        this.fileName = fileName;
    }

    public String createFilePath(String base) {
        return createFilePath(base, null);
    }

    public String createFilePath(String base, GenerateContext ctx) {
        String fileDir = String.join(File.separator, base, relativePath);
        new File(fileDir).mkdirs();

        String name = "";
        if (nameFormatter != null) {
            name = nameFormatter.format(ctx);
        } else if (StringUtils.hasLength(fileName)) {
            name = fileName;
        } else {
            String[] splits = templateName.split("\\.");
            name = splits[0];
        }
        return String.join(File.separator, fileDir, name);
    }
}