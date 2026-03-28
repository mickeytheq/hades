package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class BooleanSerialiser implements ValueSerialiser<Boolean> {
    @Override
    public JsonNode serialiseValue(Boolean value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return BooleanNode.valueOf(value);
    }

    @Override
    public Boolean deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        return jsonNode.asBoolean();
    }
}
