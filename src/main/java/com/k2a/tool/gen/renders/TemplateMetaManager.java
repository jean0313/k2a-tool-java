package com.k2a.tool.gen.renders;

import com.k2a.tool.util.Utils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class TemplateMetaManager {
    private final List<TemplateMeta> metas = new ArrayList<>();

    @PostConstruct
    public void init() {
        metas.add(TemplateMeta.ofCommon("Application.tmpl", "", "Application.java"));
        metas.add(TemplateMeta.ofCommon("CommonConsumerValidator.tmpl", "validator", "CommonConsumerValidator.java"));
        metas.add(TemplateMeta.ofCommon("CommonProducerValidator.tmpl", "validator", "CommonProducerValidator.java"));
        metas.add(TemplateMeta.ofCommon("PublisherService.tmpl", "service", "PublisherService.java"));

        metas.add(TemplateMeta.ofSubscribe("ConsumerConfig.tmpl", "config",
                ctx -> String.format("%sConsumerConfig.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
        metas.add(TemplateMeta.ofSubscribe("ConsumerProcessor.tmpl", String.join(File.separator, "consumer", "processing"),
                ctx -> String.format("%sConsumerProcessor.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
        metas.add(TemplateMeta.ofSubscribe("ConsumerService.tmpl", "service",
                ctx -> String.format("%sConsumerService.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
        metas.add(TemplateMeta.ofSubscribe("ConsumerValidator.tmpl", "validator",
                ctx -> String.format("%sConsumerValidator.java", Utils.toUpperCamelCase(ctx.getChannelName()))));

        metas.add(TemplateMeta.ofPublish("AbstractProducerResponse.tmpl", "response",
                ctx -> String.format("Abstract%sProducerResponse.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
        metas.add(TemplateMeta.ofPublish("ProducerConfig.tmpl", "config",
                ctx -> String.format("%sProducerConfig.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
        metas.add(TemplateMeta.ofPublish("ProducerValidator.tmpl", "validator",
                ctx -> String.format("%sProducerValidator.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
        metas.add(TemplateMeta.ofPublish("PublisherServiceImpl.tmpl", "service",
                ctx -> String.format("%sPublisherServiceImpl.java", Utils.toUpperCamelCase(ctx.getChannelName()))));
    }

    public List<TemplateMeta> getMetas() {
        return Collections.unmodifiableList(metas);
    }
}
