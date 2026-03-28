package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlSerialiser implements ValueSerialiser<URL> {
    @Override
    public JsonNode serialiseValue(URL value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return TextNode.valueOf(value.toExternalForm());
    }

    @Override
    public URL deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String text = jsonNode.asText();
        try {
            return new URL(text);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error parsing URL from string '" + text + "'", e);
        }
    }
}
