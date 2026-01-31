package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class EncounterSetInfoSerialiser implements ValueSerialiser<EncounterSetInfo> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, EncounterSetInfo value, ProjectContext projectContext) {
        currentNode.put(fieldName, value.getTag());
    }

    @Override
    public EncounterSetInfo deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String encounterSetKey = jsonNode.asText();

        // we allow a failed match just set it to nothing/null in that case
        return projectContext.getProjectConfiguration().getEncounterSetConfiguration().findEncounterSetInfo(encounterSetKey).orElse(null);
    }
}
