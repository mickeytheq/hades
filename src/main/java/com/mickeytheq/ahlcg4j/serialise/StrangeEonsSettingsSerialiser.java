package com.mickeytheq.ahlcg4j.serialise;

import com.mickeytheq.ahlcg4j.core.model.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.ahlcg4j.core.model.entity.EntityMetadata;
import com.mickeytheq.ahlcg4j.core.model.entity.EntityPropertyMetadata;
import com.mickeytheq.ahlcg4j.core.model.entity.PropertyMetadata;
import com.mickeytheq.ahlcg4j.strangeeons.gamecomponent.CardGameComponent;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import org.apache.commons.lang3.StringUtils;
import resources.Settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;

public class StrangeEonsSettingsSerialiser {
    public void serialiseCard(Settings settings, CardGameComponent cardGameComponent) {
        serialiseCardFace(settings, cardGameComponent.getCardView().getCard().getFrontFaceModel(), "Front");
        serialiseCardFace(settings, cardGameComponent.getCardView().getCard().getBackFaceModel(), "Back");
    }

    public void serialiseCardFace(Settings settings, CardFaceModel model, String settingsPrefix) {
        EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(model.getClass());

        new Serialiser(settings, settingsPrefix).serialise(entityMetadata, model);
    }

    public void deserialiseCardFace(Settings settings, CardFaceModel model, String settingsPrefix) {
        EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(model.getClass());

        new Deserialiser(settings, settingsPrefix).deserialise(entityMetadata, model);
    }

    static class Serialiser {
        private final Settings settings;
        private final Deque<String> settingsPrefixStack = new LinkedList<>();

        public Serialiser(Settings settings, String startingPrefix) {
            this.settings = settings;

            if (!StringUtils.isEmpty(startingPrefix))
                settingsPrefixStack.addLast(startingPrefix);
        }

        private void serialise(EntityMetadata entityMetadata, Object entity) {
            for (PropertyMetadata property : entityMetadata.getProperties()) {
                if (property.isEntity()) {
                    Object childEntity = property.getPropertyValue(entity);

                    settingsPrefixStack.addLast(property.getName());
                    try {
                        serialise(((EntityPropertyMetadata)property).getEntityMetadata(), childEntity);
                    }
                    finally {
                        settingsPrefixStack.removeLast();
                    }

                    continue;
                }

                Object value = property.getPropertyValue(entity);

                if (value == null)
                    continue;

                String settingsKey = createSettingsKey(settingsPrefixStack, property.getName());
                String serialisableString = convertValueToString(value);

                settings.set(settingsKey, serialisableString);
            }
        }

        private static String convertValueToString(Object value) {
            return value.toString();
        }
    }

    static class Deserialiser {
        private final Settings settings;
        private final Deque<String> settingsPrefixStack = new LinkedList<>();

        public Deserialiser(Settings settings, String startingPrefix) {
            this.settings = settings;

            if (!StringUtils.isEmpty(startingPrefix))
                settingsPrefixStack.addLast(startingPrefix);
        }

        public void deserialise(EntityMetadata entityMetadata, Object entity) {
            for (PropertyMetadata property : entityMetadata.getProperties()) {
                if (property.isEntity()) {
                    Object childEntity = property.getPropertyValue(entity);

                    settingsPrefixStack.addLast(property.getName());
                    try {
                        deserialise(((EntityPropertyMetadata)property).getEntityMetadata(), childEntity);
                    }
                    finally {
                        settingsPrefixStack.removeLast();
                    }

                    continue;
                }

                String settingsKey = createSettingsKey(settingsPrefixStack, property.getName());

                String settingsValue = settings.get(settingsKey);

                // TODO: do we need to do any enforcement of required properties?
                if (settingsValue == null)
                    continue;

                Object value = convertStringToValue(property, settingsValue);

                property.setPropertyValue(entity, value);
            }

            // TODO: detect any unconsumed settings and warn/error?
        }

        private Object convertStringToValue(PropertyMetadata propertyMetadata, String stringValue) {
            if (propertyMetadata.getPropertyClass().equals(String.class))
                return stringValue;

            if (propertyMetadata.getPropertyClass().equals(Integer.TYPE) || propertyMetadata.getPropertyClass().equals(Integer.class))
                return Integer.valueOf(stringValue);

            if (propertyMetadata.getPropertyClass().equals(Double.TYPE) || propertyMetadata.getPropertyClass().equals(Double.class))
                return Double.valueOf(stringValue);

            if (propertyMetadata.getPropertyClass().isEnum())
                return Enum.valueOf(propertyMetadata.getPropertyClass().asSubclass(Enum.class), stringValue);

            if (propertyMetadata.getPropertyClass().equals(URL.class)) {
                try {
                    return new URL(stringValue);
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Error parsing URL from string '" + stringValue + "'", e);
                }
            }

            throw new UnsupportedOperationException("Unsupported property class '" + propertyMetadata.getPropertyClass() + "' on property '" + propertyMetadata.getName() + "'");
        }
    }

    private static String createSettingsKey(Deque<String> settingsPrefixStack, String suffix) {
        String prefix = StringUtils.join(settingsPrefixStack, ".");

        return prefix + "." + suffix;
    }
}
