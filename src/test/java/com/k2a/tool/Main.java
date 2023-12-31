package com.k2a.tool;

import org.apache.avro.Schema;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.io.FileInputStream;

public class Main {

    public static void main(String[] args) {
        final Schema.Parser parser = new Schema.Parser();
        try (FileInputStream fis = new FileInputStream("src/test/resources/sendpayment.avsc")) {
            byte[] buf = new byte[4096];
            fis.read(buf);
            final Schema schema = parser.parse(new String(buf));
            final SpecificCompiler compiler = new SpecificCompiler(schema);
            compiler.setStringType(GenericData.StringType.String);
            compiler.setTemplateDir("src/main/resources/avro-templates/");
            compiler.compileToDestination(new File("sendpayment.avsc"), new File("out/"));
        } catch (Exception ex) {
        }
    }
}
