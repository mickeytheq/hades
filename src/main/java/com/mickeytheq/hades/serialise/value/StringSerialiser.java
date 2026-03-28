package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class StringSerialiser implements ValueSerialiser<String> {
    @Override
    public JsonNode serialiseValue(String value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return TextNode.valueOf(value);
    }

    @Override
    public String deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        return jsonNode.asText();
    }
}
