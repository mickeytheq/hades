package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class CollectionConfigurationSerialiser implements ValueSerialiser<CollectionConfiguration> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, CollectionConfiguration value, ProjectContext projectContext) {
        currentNode.put(fieldName, value.getTag());
    }

    @Override
    public CollectionConfiguration deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String collectionKey = jsonNode.asText();

        // we allow a failed match just set it to nothing/null in that case
        return projectContext.getProjectConfiguration().getCollectionConfigurations().stream().filter(o -> o.getUniqueId().equals(collectionKey)).findAny().orElse(null);
    }
}
