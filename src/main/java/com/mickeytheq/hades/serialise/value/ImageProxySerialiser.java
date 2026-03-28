package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class ImageProxySerialiser implements ValueSerialiser<ImageProxy> {
    @Override
    public JsonNode serialiseValue(ImageProxy value, ObjectMapper objectMapper, ProjectContext projectContext) {
        String identifier = value.save();

        return TextNode.valueOf(identifier);
    }

    @Override
    public ImageProxy deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        String imageIdentifier = jsonNode.asText();
        ImageProxy imageProxy = ImageProxy.createFor(imageIdentifier);
        return imageProxy;
    }
}
