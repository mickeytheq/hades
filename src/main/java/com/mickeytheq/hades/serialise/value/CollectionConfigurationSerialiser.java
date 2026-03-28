package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class CollectionConfigurationSerialiser implements ValueSerialiser<CollectionConfiguration> {
    @Override
    public JsonNode serialiseValue(CollectionConfiguration value, ObjectMapper objectMapper, ProjectContext projectContext) {
        return TextNode.valueOf(value.getUniqueId());
    }

    @Override
    public CollectionConfiguration deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String collectionKey = jsonNode.asText();

        // we allow a failed match just set it to nothing/null in that case
        return projectContext.getProjectConfiguration().getCollectionConfigurations().stream().filter(o -> o.getUniqueId().equals(collectionKey)).findAny().orElse(null);
    }
}
