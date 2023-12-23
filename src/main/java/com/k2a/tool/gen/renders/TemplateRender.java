package com.k2a.tool.gen.renders;

import com.k2a.tool.gen.models.GenerateContext;
import com.k2a.tool.gen.models.GlobalContext;
import com.k2a.tool.gen.renders.methods.Capitalize;
import com.k2a.tool.gen.renders.methods.ToLowerCamelCase;
import com.k2a.tool.gen.renders.methods.ToUpperCamelCase;
import com.k2a.tool.gen.renders.methods.Uncapitalize;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class TemplateRender {

    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);

    public TemplateRender(TemplateLoader loader) {
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }

    public void renderTemplate(String tplName, String outPath, GenerateContext ctx, GlobalContext gCtx) {
        Map<String, Object> map = new HashMap<>();
        map.put("ctx", ctx);
        map.put("gCtx", gCtx);
        this.renderTemplate(tplName, outPath, map);
    }

    public void renderCommonTemplate(String tplName, String outPath, GlobalContext gCtx) {
        Map<String, Object> map = new HashMap<>();
        map.put("gCtx", gCtx);
        this.renderTemplate(tplName, outPath, map);
    }

    private void renderTemplate(String tplName, String outPath, Map<String, Object> map) {
        map.put("toLowerCamelCase", new ToLowerCamelCase());
        map.put("toUpperCamelCase", new ToUpperCamelCase());
        map.put("capitalize", new Capitalize());
        map.put("uncapitalize", new Uncapitalize());

        try (Writer fileWriter = new FileWriter(outPath)) {
            Template template = cfg.getTemplate(tplName);
            template.process(map, fileWriter);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
