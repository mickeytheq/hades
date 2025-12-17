package com.mickeytheq.hades.util;

import com.mickeytheq.hades.strangeeons.plugin.HadesPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class VersionUtils {
    private static final float VERSION;

    static {
        VERSION = loadVersion();
    }

    public static float getVersion() {
        return VERSION;
    }

    private static float loadVersion() {
        try (InputStream inputStream = HadesPlugin.class.getResourceAsStream("/hades-metadata")) {
            if (inputStream == null)
                throw new RuntimeException("No hades-metadata resource/file found in classpath root");

            Properties properties = new Properties();
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String versionStr = properties.getProperty("version");

            if (versionStr == null)
                throw new RuntimeException("No version found in metadata file");

            return Float.parseFloat(properties.getProperty("version"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
