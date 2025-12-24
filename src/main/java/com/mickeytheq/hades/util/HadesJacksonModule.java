package com.mickeytheq.hades.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.mickeytheq.hades.core.model.image.ImageProxy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HadesJacksonModule extends SimpleModule {
    public HadesJacksonModule() {
        addSerializer(ImageProxy.class, new ImageSerialiser());
        addDeserializer(ImageProxy.class, new ImageDeserialiser());
    }

    static class ImageSerialiser extends StdScalarSerializer<ImageProxy> {
        protected ImageSerialiser() {
            super(ImageProxy.class);
        }

        @Override
        public void serialize(ImageProxy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            String identifier = value.save();

            if (identifier == null)
                return;

            gen.writeString(identifier);
        }
    }

    static class ImageDeserialiser extends StdScalarDeserializer<ImageProxy> {
        protected ImageDeserialiser() {
            super(ImageProxy.class);
        }

        @Override
        public ImageProxy deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String identifier = p.getValueAsString();

            return ImageProxy.createFor(identifier);
        }
    }
}
