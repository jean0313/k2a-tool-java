package com.k2a.tool.gen.renders.methods;

import freemarker.template.TemplateMethodModelEx;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

public class Capitalize implements TemplateMethodModelEx {
    @Override
    public String exec(List arguments) {
        Assert.isTrue(arguments.size() != 0, "capitalize need at least one argument!");
        return StringUtils.capitalize(arguments.get(0).toString());
    }
}
