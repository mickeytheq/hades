package com.mickeytheq.hades.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.serialise.value.DistanceSerialiser;
import com.mickeytheq.hades.util.shape.Unit;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HadesJacksonModule extends SimpleModule {
    public HadesJacksonModule() {
        addSerializer(ImageProxy.class, new ImageSerialiser());
        addDeserializer(ImageProxy.class, new ImageDeserialiser());

        addSerializer(Distance.class, new DistanceSerialiser());
        addDeserializer(Distance.class, new DistanceDeserialiser());
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

    static class DistanceSerialiser extends StdSerializer<Distance> {
        protected DistanceSerialiser() {
            super(Distance.class);
        }

        @Override
        public void serialize(Distance value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField(com.mickeytheq.hades.serialise.value.DistanceSerialiser.AMOUNT_FIELD, value.getAmount());
            gen.writeStringField(com.mickeytheq.hades.serialise.value.DistanceSerialiser.UNIT_FIELD, value.getUnit().name());
            gen.writeEndObject();
        }
    }

    static class DistanceDeserialiser extends StdDeserializer<Distance> {
        protected DistanceDeserialiser() {
            super(Distance.class);
        }

        @Override
        public Distance deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode distanceNode = p.getCodec().readTree(p);

            return new Distance(distanceNode.get(com.mickeytheq.hades.serialise.value.DistanceSerialiser.AMOUNT_FIELD).asDouble(),
                    Unit.valueOf(distanceNode.get(com.mickeytheq.hades.serialise.value.DistanceSerialiser.UNIT_FIELD).asText()));
        }
    }
}
