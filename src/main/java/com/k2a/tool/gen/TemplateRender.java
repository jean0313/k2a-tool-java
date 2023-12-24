package com.k2a.tool.gen;

import com.k2a.tool.gen.models.GenerateContext;
import com.k2a.tool.gen.models.GlobalContext;

public interface TemplateRender {
    void renderTemplate(String tplName, String outPath, GenerateContext ctx, GlobalContext gCtx);

    void renderCommonTemplate(String tplName, String outPath, GlobalContext gCtx);
}
