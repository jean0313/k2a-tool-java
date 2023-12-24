package com.k2a.tool.gen;

import com.k2a.tool.gen.models.GenerateContext;
import com.k2a.tool.gen.models.GlobalContext;
import com.k2a.tool.gen.parsers.SpecificationParser;
import com.k2a.tool.gen.parsers.SpecificationParserFactory;
import com.k2a.tool.gen.renders.TemplateMetaManager;
import com.k2a.tool.gen.renders.TemplateRenderImpl;
import freemarker.cache.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectGenerateService {
    private static final Logger log = LoggerFactory.getLogger(ProjectGenerateService.class);
    @Autowired
    private TemplateLoader loader;

    @Autowired
    private TemplateMetaManager templateMetaManager;

    @Autowired
    private SpecificationParserFactory parserFactory;

    public void generate(String content, GlobalContext gCtx) {
        SpecificationParser parser = parserFactory.create(content);
        List<GenerateContext> ctxes = parser.parse();
        ProjectGenerator generator = new ProjectGenerator(ctxes, gCtx,
                new TemplateRenderImpl(loader),
                templateMetaManager);
        generator.generateProject();
    }
}
