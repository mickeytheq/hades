package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class CollectionInfoSerialiser implements ValueSerialiser<CollectionInfo> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, CollectionInfo value, ProjectContext projectContext) {
        currentNode.put(fieldName, value.getTag());
    }

    @Override
    public CollectionInfo deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String collectionKey = jsonNode.asText();

        // we allow a failed match just set it to nothing/null in that case
        return projectContext.getProjectConfiguration().getCollectionConfiguration().findCollectionInfo(collectionKey).orElse(null);
    }
}
