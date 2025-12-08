package com.mickeytheq.hades.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HadesJacksonModule extends SimpleModule {
    public HadesJacksonModule() {
        addSerializer(BufferedImage.class, new BufferedImageSerialiser());
        addDeserializer(BufferedImage.class, new BufferedImageDeserialiser());
    }

    static class BufferedImageSerialiser extends StdScalarSerializer<BufferedImage> {
        protected BufferedImageSerialiser() {
            super(BufferedImage.class);
        }

        @Override
        public void serialize(BufferedImage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            byte[] data = JsonUtils.serialiseBufferedImage(value);
            gen.writeBinary(data);
        }
    }

    static class BufferedImageDeserialiser extends StdScalarDeserializer<BufferedImage> {
        protected BufferedImageDeserialiser() {
            super(BufferedImage.class);
        }

        @Override
        public BufferedImage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            p.readBinaryValue(byteArrayOutputStream);

            return JsonUtils.deserialiseBufferedImage(byteArrayOutputStream.toByteArray());
        }
    }
}
