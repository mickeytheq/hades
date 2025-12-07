package com.mickeytheq.hades.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new HadesJacksonModule());

        return mapper;
    }

    public static ObjectMapper createDefaultObjectMapper(boolean prettyPrint) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new HadesJacksonModule());

        if (prettyPrint)
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
