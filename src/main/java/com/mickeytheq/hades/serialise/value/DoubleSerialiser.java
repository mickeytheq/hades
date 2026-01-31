package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class DoubleSerialiser implements ValueSerialiser<Double> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, Double value, ProjectContext projectContext) {
        currentNode.put(fieldName, value);
    }

    @Override
    public Double deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        return jsonNode.asDouble();
    }
}
