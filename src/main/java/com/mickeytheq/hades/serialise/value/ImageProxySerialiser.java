package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class ImageProxySerialiser implements ValueSerialiser<ImageProxy> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, ImageProxy value, ProjectContext projectContext) {
        String identifier = value.save();

        if (identifier != null)
            currentNode.put(fieldName, identifier);
    }

    @Override
    public ImageProxy deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String imageIdentifier = jsonNode.asText();
        ImageProxy imageProxy = ImageProxy.createFor(imageIdentifier);
        return imageProxy;
    }
}
