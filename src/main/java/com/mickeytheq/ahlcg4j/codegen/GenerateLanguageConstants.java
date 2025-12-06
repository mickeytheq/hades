package com.mickeytheq.ahlcg4j.codegen;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class GenerateLanguageConstants {
    public static void main(String[] args) {
        new GenerateLanguageConstants().run();
    }

    private void run() {
        generateGameResources();
        generateInterfaceResources();
    }

    private void generateGameResources() {
        Properties properties = loadProperties("/language/AHLCG-Game.properties");
        String fileContent = generateFileContent("GameConstants", properties);
        writeConstantFile(fileContent, "GameConstants.java");
    }

    private void generateInterfaceResources() {
        Properties properties = loadProperties("/language/AHLCG-Interface.properties");
        String fileContent = generateFileContent("InterfaceConstants", properties);
        writeConstantFile(fileContent, "InterfaceConstants.java");
    }

    private void writeConstantFile(String fileContent, String filename) {
        File targetDirectory = getTargetDirectory();

        try {
            FileUtils.writeStringToFile(new File(targetDirectory, filename), fileContent, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            throw new RuntimeException("Error writing constant file", e);
        }
    }

    private Properties loadProperties(String resourcePath) {
        URL gameResource = GenerateLanguageConstants.class.getResource(resourcePath);

        if (gameResource == null)
            throw new RuntimeException("Could not find language file at resource path " + resourcePath);

        Properties properties = new Properties();
        try (InputStream inputStream = gameResource.openStream()) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading language file at resource path " + resourcePath, e);
        }

        return properties;
    }

    private String generateFileContent(String className, Properties properties) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        String targetPackage = getClass().getPackage().getName();
        targetPackage = targetPackage.substring(0, targetPackage.lastIndexOf("."));
        targetPackage = targetPackage + ".codegenerated";

        printWriter.println("package " + targetPackage + ";");
        printWriter.println();

        printWriter.println("@SuppressWarnings(\"unused\")");
        printWriter.println("public class " + className + " {");

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            printWriter.println("    // English value: " + makeSingleLineValue(entry.getValue()));
            printWriter.println("    public static final String " + makeKey(entry.getKey().toString()) + " = \"" + makeValue(entry.getKey().toString()) + "\";");
        }
        printWriter.println("}");

        return stringWriter.toString();
    }

    private String makeSingleLineValue(Object value) {
        String text = String.valueOf(value);

        text = StringUtils.replace(text, "\r", "");
        text = StringUtils.replace(text, "\n", "");
        text = StringUtils.replace(text, "\t", " ");

        return text;
    }

    private String makeKey(String rawKey) {
        String key = StringUtils.replace(rawKey, "-", "_");
        key = key.toUpperCase();

        key = StringUtils.removeStart(key, "AHLCG4J_");

        return key;
    }

    private String makeValue(String rawValue) {
        String value = rawValue;

        value = StringUtils.replace(value, "\n", "\\n");
        value = StringUtils.replace(value, "\r", "\\r");
        value = StringUtils.replace(value, "\"", "\\\"");

        return value;
    }

    private File getTargetDirectory() {
        File userDir = new File(System.getProperty("user.dir"));
        File targetFile = new File(userDir, "src/main/java/com/mickeytheq/ahlcg4j/codegenerated/");

        return targetFile;
    }
}
