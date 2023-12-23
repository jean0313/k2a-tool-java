package com.k2a.tool.gen.renders;

import freemarker.cache.TemplateLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClasspathTemplateLoader implements TemplateLoader {

    @Autowired
    private ResourcePatternResolver resourceResolver;

    private final Map<String, String> templates = new HashMap<>();

    @Override
    public Object findTemplateSource(String name) {
        return templates.get(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(templateSource.toString());
    }

    @Override
    public void closeTemplateSource(Object templateSource) {
    }

    @PostConstruct
    public void loadTemplates() throws IOException {
        Resource[] resources = resourceResolver.getResources("classpath:templates/*");
        for (Resource resource : resources) {
            String content = resource.getContentAsString(StandardCharsets.UTF_8);
            templates.put(resource.getFilename(), content);
        }
    }
}
