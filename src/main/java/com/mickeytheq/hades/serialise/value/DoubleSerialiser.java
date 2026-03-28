package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class DoubleSerialiser implements ValueSerialiser<Double> {
    @Override
    public JsonNode serialiseValue(Double value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return DoubleNode.valueOf(value);
    }

    @Override
    public Double deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        return jsonNode.asDouble();
    }
}
