package com.mickeytheq.hades.core.model.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class EntityUtils {
    private static final Logger logger = LogManager.getLogger(EntityUtils.class);

    public static void copyEntity(Object fromEntity, Object toEntity) {
        new CardCopier().copy(fromEntity, toEntity);
    }

    static class CardCopier {
        public void copy(Object sourceEntity, Object targetEntity) {
            EntityMetadata sourceMetadata = AnnotatedEntityMetadataBuilder.build(sourceEntity.getClass());
            EntityMetadata targetMetadata = AnnotatedEntityMetadataBuilder.build(targetEntity.getClass());

            copyEntity(sourceMetadata, sourceEntity, targetMetadata, targetEntity);
        }

        private void copyEntity(EntityMetadata fromEntityMetadata, Object fromEntity, EntityMetadata toEntityMetadata, Object toEntity) {
            for (PropertyMetadata property : fromEntityMetadata.getProperties()) {
                Optional<PropertyMetadata> targetPropertyOpt = toEntityMetadata.findProperty(property.getName());

                if (targetPropertyOpt.isPresent())
                    copyProperty(property, fromEntity, targetPropertyOpt.get(), toEntity);
                else
                    logger.info("Skipped property '" + property.getName() + "' on entity type '" + fromEntity.getClass() + " as there was no equivalent on the target class '" + toEntityMetadata.getEntityClass() + "'");
            }
        }

        private void copyProperty(PropertyMetadata fromProperty, Object fromEntity, PropertyMetadata toProperty, Object toEntity) {
            Object fromPropertyValue = fromProperty.getPropertyValue(fromEntity);

            if (!fromProperty.getPropertyClass().equals(toProperty.getPropertyClass())) {
                logger.info("Skipped property '" + fromProperty.getName() + "' on entity type '" + fromEntity.getClass() + "' with value '" + fromPropertyValue + "' although there was a property with a matching name the classes were different");
                return;
            }

            // value -> value, just do a straight reference copy as
            // value types are immutable
            if (fromProperty.isValue() && toProperty.isValue()) {
                // do a deep copy if required
                if (fromPropertyValue instanceof NeedsDeepCopy) {
                    fromPropertyValue = ((NeedsDeepCopy)fromPropertyValue).deepCopy();
                }

                toProperty.setPropertyValue(toEntity, fromPropertyValue);
                logger.info("Copied property '" + fromProperty.getName() + "' on entity type '" + fromEntity.getClass() + "' with value '" + fromPropertyValue + "'");
                return;
            }

            if (fromProperty.isEntity() && toProperty.isEntity()) {
                Object toPropertyValue = toProperty.getPropertyValue(toEntity);

                copyEntity(fromProperty.asEntity(), fromPropertyValue, toProperty.asEntity(), toPropertyValue);
                return;
            }

            logger.info("Skipped property '" + fromProperty.getName() + "' on entity type '" + fromEntity.getClass() + "' with value '" + fromPropertyValue + "' although there was a property with a matching name they were different fundamental types (e.g. entity vs value");
        }
    }
}
