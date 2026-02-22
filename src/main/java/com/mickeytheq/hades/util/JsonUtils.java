package com.mickeytheq.hades.util;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JsonUtils {
    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS,
                MapperFeature.AUTO_DETECT_SETTERS);

        mapper.registerModule(new HadesJacksonModule());

        return mapper;
    }

    public static ObjectMapper createDefaultObjectMapper(boolean prettyPrint) {
        ObjectMapper mapper = createDefaultObjectMapper();

        if (prettyPrint)
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
