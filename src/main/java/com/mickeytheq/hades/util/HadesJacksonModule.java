package com.mickeytheq.hades.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(value, "png", byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
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
            return ImageIO.read(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        }
    }
}
