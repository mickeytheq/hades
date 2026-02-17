package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class EncounterSetConfigurationSerialiser implements ValueSerialiser<EncounterSetConfiguration> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, EncounterSetConfiguration value, ProjectContext projectContext) {
        currentNode.put(fieldName, value.getUniqueId());
    }

    @Override
    public EncounterSetConfiguration deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String encounterSetKey = jsonNode.asText();

        // we allow a failed match just set it to nothing/null in that case
        return projectContext.getProjectConfiguration().getEncounterSetConfigurations().stream().filter(o -> o.getUniqueId().equals(encounterSetKey)).findAny().orElse(null);
    }
}
