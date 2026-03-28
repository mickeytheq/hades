package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.core.project.ProjectContext;

public interface ValueSerialiser<T> {
    JsonNode serialiseValue(T value, ObjectMapper objectMapper, ProjectContext projectContext);

    T deserialiseValue(JsonNode jsonNode, ProjectContext projectContext);
}
