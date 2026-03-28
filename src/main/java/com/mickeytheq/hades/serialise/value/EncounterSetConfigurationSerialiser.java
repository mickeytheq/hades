package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class EncounterSetConfigurationSerialiser implements ValueSerialiser<EncounterSetConfiguration> {
    @Override
    public JsonNode serialiseValue(EncounterSetConfiguration value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return TextNode.valueOf(value.getUniqueId());
    }

    @Override
    public EncounterSetConfiguration deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String encounterSetKey = jsonNode.asText();

        // we allow a failed match just set it to nothing/null in that case
        return projectContext.getProjectConfiguration().getEncounterSetConfigurations().stream().filter(o -> o.getUniqueId().equals(encounterSetKey)).findAny().orElse(null);
    }
}
