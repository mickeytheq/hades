package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;

public interface ValueSerialiser<T> {
    void serialiseValue(String fieldName, ObjectNode currentNode, T value, ProjectContext projectContext);

    T deserialiseValue(JsonNode jsonNode, ProjectContext projectContext);
}
