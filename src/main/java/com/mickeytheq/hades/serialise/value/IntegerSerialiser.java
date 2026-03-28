package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.LongNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class IntegerSerialiser implements ValueSerialiser<Integer> {
    @Override
    public JsonNode serialiseValue(Integer value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return LongNode.valueOf(value);
    }

    @Override
    public Integer deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        return jsonNode.asInt();
    }
}
