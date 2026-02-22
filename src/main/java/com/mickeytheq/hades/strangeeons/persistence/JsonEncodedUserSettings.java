package com.mickeytheq.hades.strangeeons.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.Settings;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class JsonEncodedUserSettings {
    private static final Logger logger = LogManager.getLogger(JsonEncodedUserSettings.class);

    public static void load(String settingsKey, Object mappedObject) {
        String settingsValue = Settings.getUser().get(settingsKey);

        if (StringUtils.isEmpty(settingsValue))
            return;

        ObjectMapper mapper = JsonUtils.createDefaultObjectMapper();

        StringReader reader = new StringReader(settingsValue);
        try {
            mapper.readerForUpdating(mappedObject).readValue(reader);
        } catch (IOException e) {
            // if there's a failure then just exit quietly
            // worst case the settings values have been lost
            logger.warn("Error deserialising from setting key '" + settingsKey + "'", e);
        }
    }

    public static void save(String settingsKey, Object mappedObject) {
        ObjectMapper mapper = JsonUtils.createDefaultObjectMapper();

        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, mappedObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Settings.getUser().set(settingsKey, writer.toString());
    }
}
