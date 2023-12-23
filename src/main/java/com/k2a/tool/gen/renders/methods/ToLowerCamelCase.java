package com.k2a.tool.gen.renders.methods;

import com.k2a.tool.util.Utils;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.util.Assert;

import java.util.List;

public class ToLowerCamelCase implements TemplateMethodModelEx {
    @Override
    public String exec(List arguments) {
        Assert.isTrue(arguments.size() != 0, "toCamelCase need at least one argument!");
        return Utils.toLowerCamelCase(arguments.get(0).toString());
    }
}
