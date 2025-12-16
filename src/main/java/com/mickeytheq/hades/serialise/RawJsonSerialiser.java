package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.util.JsonUtils;

import java.io.*;

/**
 * Handles converting a raw read/write stream from/to a generic JSON object tree
 */
public class RawJsonSerialiser {
    public static ObjectNode readRawCardJson(Reader reader) throws IOException {
        ObjectNode cardObjectNode = (ObjectNode) createSerialisationObjectMapper().readTree(reader);
        return cardObjectNode;
    }

    public static void writeRawCardJson(Writer writer, ObjectNode cardObjectNode) throws IOException {
        createSerialisationObjectMapper().writeValue(writer, cardObjectNode);
    }

    private static ObjectMapper createSerialisationObjectMapper() {
        return JsonUtils.createDefaultObjectMapper(true);
    }

}
