package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class StringSerialiser implements ValueSerialiser<String> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, String value, ProjectContext projectContext) {
        currentNode.put(fieldName, value);
    }

    @Override
    public String deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        return jsonNode.asText();
    }
}
