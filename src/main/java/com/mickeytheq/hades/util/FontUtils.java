package com.mickeytheq.hades.util;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FontUtils {
    public static void loadFont(String resourcePath) {
        try (InputStream inputStream = FontUtils.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null)
                throw new RuntimeException("Font file not found at resource path '" + resourcePath + "'");

            loadFont(inputStream, resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from font file '" + resourcePath + "'", e);
        }
    }

    public static void loadFont(Path path) {
        if (!Files.exists(path))
            throw new RuntimeException("Font file not found at path '" + path + "'");

        try (InputStream inputStream = Files.newInputStream(path)) {
            loadFont(inputStream, path.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error reading from font file '" + path + "'", e);
        }
    }

    public static void loadFont(InputStream inputStream, String location) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException e) {
            throw new RuntimeException("Format error loading font file '" + location + "'", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from font file '" + location + "'", e);
        }

        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
    }
}
