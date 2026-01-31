package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlSerialiser implements ValueSerialiser<URL> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, URL value, ProjectContext projectContext) {
        currentNode.put(fieldName, value.toExternalForm());
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
