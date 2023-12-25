package com.k2a.tool;

import com.k2a.tool.gen.ProjectGenerateService;
import com.k2a.tool.gen.models.GlobalContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;


@SpringBootApplication
public class K2aToolApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(K2aToolApplication.class, args);
    }

    @Autowired
    private ProjectGenerateService projectGenerateService;

    @Override
    public void run(String... args) throws Exception {
        GlobalContext gCtx = new GlobalContext();
        gCtx.setGroupId("com.app");
        gCtx.setArtifactId("app-pay");
        gCtx.setDescription("this is a app");
        gCtx.setVersion("1.0");
        gCtx.setPackageName("pay");
        gCtx.setDestDir("gen");

        String content = Files.readString(Path.of("3x.json"));
        projectGenerateService.generate(content, gCtx);
    }
}
