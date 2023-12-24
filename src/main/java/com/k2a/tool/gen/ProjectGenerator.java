package com.k2a.tool.gen;

import com.k2a.tool.gen.enums.GenType;
import com.k2a.tool.gen.enums.SchemaFormat;
import com.k2a.tool.gen.models.GenerateContext;
import com.k2a.tool.gen.models.GlobalContext;
import com.k2a.tool.gen.renders.TemplateMeta;
import com.k2a.tool.gen.renders.TemplateMetaManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProjectGenerator {
    private List<GenerateContext> ctxes;

    private GlobalContext gCtx;

    private TemplateRender render;

    private TemplateMetaManager templateMetaManager;

    public ProjectGenerator(List<GenerateContext> ctxes,
                            GlobalContext gCtx,
                            TemplateRender render,
                            TemplateMetaManager templateMetaManager) {
        this.ctxes = ctxes;
        this.gCtx = gCtx;
        this.render = render;
        this.templateMetaManager = templateMetaManager;
    }

    public void generateProject() {
        generateProjectStructure();
        generateFiles();
        generateSchemaFiles();
    }

    private void generateProjectStructure() {
        // maven archetype:generate

    }

    private void generateSchemaFiles() {
        String destDir = gCtx.getDestDir();
        String artifactId = gCtx.getArtifactId();
        String schemaDir = String.join(File.separator, destDir, artifactId, "src", "main", "resources", "schema");
        new File(schemaDir).mkdirs();
        String avroDir = String.join(File.separator, destDir, artifactId, "src", "main", "resources", "avro");
        new File(avroDir).mkdirs();

        String dir = "";
        for (GenerateContext ctx : ctxes) {
            String messageSchema = ctx.getMessageSchema();
            String schemaName = ctx.getSchemaName();

            if (SchemaFormat.json.equals(ctx.getMessageSchemaFormat())) {
                dir = schemaDir;
            } else {
                dir = avroDir;
            }

            try (BufferedOutputStream buf = new BufferedOutputStream(
                    new FileOutputStream(String.join(File.separator, dir, String.format("%s.json", schemaName))))) {
                buf.write(messageSchema.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateFiles() {
        String packageBase = createPackageBaseDir();
        List<TemplateMeta> metas = templateMetaManager.getMetas();

        metas.stream()
                .filter(meta -> meta.getType().equals(GenType.common))
                .forEach(meta -> {
                    String filePath = meta.createFilePath(packageBase);
                    render.renderCommonTemplate(
                            meta.getTemplateName(),
                            filePath,
                            gCtx);
                });

        for (GenerateContext ctx : ctxes) {
            metas.stream()
                    .filter(meta -> !meta.getType().equals(GenType.common))
                    .forEach(meta -> {
                        String filePath = meta.createFilePath(packageBase, ctx);
                        render.renderTemplate(
                                meta.getTemplateName(),
                                filePath,
                                ctx,
                                gCtx);
                    });
        }
    }

    private String createPackageBaseDir() {
        String destDir = gCtx.getDestDir();
        String artifactId = gCtx.getArtifactId();
        String javaDir = String.join(File.separator, destDir, artifactId, "src", "main", "java");

        String groupId = gCtx.getGroupId();
        String packageName = gCtx.getPackageName();
        String groupDir = String.join(File.separator, groupId.split("\\."));
        String packageBaseDir = String.join(File.separator, javaDir, groupDir, packageName);
        new File(packageBaseDir).mkdirs();
        return packageBaseDir;
    }
}
