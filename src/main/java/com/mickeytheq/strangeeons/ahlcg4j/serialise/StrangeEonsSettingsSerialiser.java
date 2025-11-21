package com.mickeytheq.strangeeons.ahlcg4j.serialise;

import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.strangeeons.ahlcg4j.entity.EntityMetadata;
import com.mickeytheq.strangeeons.ahlcg4j.entity.EntityPropertyMetadata;
import com.mickeytheq.strangeeons.ahlcg4j.entity.PropertyMetadata;
import org.apache.commons.lang3.StringUtils;
import resources.Settings;

import java.util.Deque;
import java.util.LinkedList;

public class StrangeEonsSettingsSerialiser {
    public void serialiseCard(Settings settings, Card card) {
        serialiseCardFace(settings, card.getFrontFaceModel(), "Front");
        serialiseCardFace(settings, card.getBackFaceModel(), "Back");
    }

    public void serialiseCardFace(Settings settings, CardFaceModel model, String settingsPrefix) {
        EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(model.getClass());

        new Serialiser(settings, settingsPrefix).serialise(entityMetadata, model);
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

                String settingsKey = createSettingsKey(property.getName());
                String serialisableString = convertValueToString(value);

                settings.set(settingsKey, serialisableString);
            }
        }

        private String createSettingsKey(String suffix) {
            String prefix = StringUtils.join(settingsPrefixStack, ".");

            return prefix + "." + suffix;
        }

        private static String convertValueToString(Object value) {
            return value.toString();
        }
    }
}
