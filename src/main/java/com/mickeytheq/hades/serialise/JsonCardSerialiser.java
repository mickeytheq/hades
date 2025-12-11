package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.CardFaceTypeRegister;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.hades.core.model.entity.EntityMetadata;
import com.mickeytheq.hades.core.model.entity.PropertyMetadata;
import com.mickeytheq.hades.core.project.CollectionInfo;
import com.mickeytheq.hades.core.project.EncounterSetInfo;
import com.mickeytheq.hades.core.project.ProjectConfigurations;
import com.mickeytheq.hades.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class JsonCardSerialiser {
    private static final String FRONT_FACE_FIELD_NAME = "Front";
    private static final String BACK_FACE_FIELD_NAME = "Back";
    private static final String CARD_FACE_TYPE_FIELD_NAME = "Type";
    private static final String COMMENTS_FIELD_NAME = "Comments";

    // TODO: version?
    public static ObjectNode serialiseCard(Card card) {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        ObjectNode cardNode = objectMapper.createObjectNode();

        ObjectNode frontFaceNode = cardNode.putObject(FRONT_FACE_FIELD_NAME);
        serialiseCardFace(card.getFrontFaceModel(), objectMapper, frontFaceNode);

        if (card.hasBack()) {
            ObjectNode backFaceNode = cardNode.putObject(BACK_FACE_FIELD_NAME);
            serialiseCardFace(card.getBackFaceModel(), objectMapper, backFaceNode);
        }

        if (!StringUtils.isEmpty(card.getComments()))
            cardNode.put(COMMENTS_FIELD_NAME, card.getComments());

        return cardNode;
    }

    private static void serialiseCardFace(CardFaceModel cardFaceModel, ObjectMapper objectMapper, ObjectNode faceNode) {
        String typeCode = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass()).getTypeCode();
        faceNode.put(CARD_FACE_TYPE_FIELD_NAME, typeCode);

        new Serialiser(objectMapper).serialise(cardFaceModel, faceNode);
    }

    public static Card deserialiseCard(ObjectNode objectNode) {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();

        Card card = new Card();

        ObjectNode frontFaceNode = (ObjectNode)objectNode.get(FRONT_FACE_FIELD_NAME);

        CardFaceModel frontFaceModel = deserialiseCardFace(objectMapper, frontFaceNode);
        card.setFrontFaceModel(frontFaceModel);

        ObjectNode backFaceNode = (ObjectNode)objectNode.get(BACK_FACE_FIELD_NAME);

        if (backFaceNode != null) {
            CardFaceModel backFaceModel = deserialiseCardFace(objectMapper, backFaceNode);
            card.setBackFaceModel(backFaceModel);
        }

        JsonNode commentsNode = objectNode.get(COMMENTS_FIELD_NAME);

        if (commentsNode != null)
            card.setComments(commentsNode.asText());

        return card;
    }

    private static CardFaceModel deserialiseCardFace(ObjectMapper objectMapper, ObjectNode faceNode) {
        // remove the type field here - if it is left then the latter serialisation code
        // will treat it as an unhandled field
        JsonNode typeField = faceNode.remove(CARD_FACE_TYPE_FIELD_NAME);

        String typeCode = typeField.asText();

        CardFaceModel cardFaceModel = CardFaces.createFaceModelForTypeCode(typeCode);

        new Deserialiser(objectMapper).deserialise(faceNode, cardFaceModel);

        return cardFaceModel;
    }

    private static class Serialiser {
        private final ObjectMapper objectMapper;

        public Serialiser(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public void serialise(CardFaceModel cardFaceModel, ObjectNode faceNode) {
            EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(cardFaceModel.getClass());

            serialiseEntity(entityMetadata, cardFaceModel, faceNode);
        }

        private void serialiseEntity(EntityMetadata entityMetadata, Object entity, ObjectNode currentNode) {
            for (PropertyMetadata propertyMetadata : entityMetadata.getProperties()) {
                Object value = propertyMetadata.getPropertyValue(entity);

                if (value == null)
                    continue;

                if (propertyMetadata.isValue()) {
                    serialiseValue(value, propertyMetadata.getName(), currentNode);
                    continue;
                }

                if (propertyMetadata.isEntity()) {
                    ObjectNode childEntityNode = currentNode.putObject(propertyMetadata.getName());

                    serialiseEntity(propertyMetadata.asEntity(), value, childEntityNode);
                }
            }
        }

        private void serialiseValue(Object value, String fieldName, ObjectNode currentNode) {
            // TODO: replace all this hardcoding with plug-in serialisers for each class/type
            if (value instanceof String) {
                currentNode.put(fieldName, (String)value);
                return;
            }

            if (value.getClass().isEnum()) {
                currentNode.put(fieldName, ((Enum<?>)value).name());
                return;
            }

            if (value instanceof Integer) {
                currentNode.put(fieldName, (Integer)value);
                return;
            }

            if (value instanceof Double) {
                currentNode.put(fieldName, (Double)value);
                return;
            }

            if (value instanceof Boolean) {
                currentNode.put(fieldName, (Boolean)value);
                return;
            }

            if (value instanceof URL) {
                currentNode.put(fieldName, ((URL)value).toExternalForm());
                return;
            }

            if (value instanceof BufferedImage) {
                try {
                    currentNode.put(fieldName, JsonUtils.serialiseBufferedImage((BufferedImage)value));
                } catch (IOException e) {
                    throw new RuntimeException("Error serialising BufferedImage  from property '" + fieldName + "' ", e);
                }
                return;
            }

            if (value instanceof EncounterSetInfo) {
                currentNode.put(fieldName, ((EncounterSetInfo)value).getTag());
                return;
            }

            if (value instanceof CollectionInfo) {
                currentNode.put(fieldName, ((CollectionInfo)value).getTag());
                return;
            }

            throw new RuntimeException("Value type '" + value.toString() + "' of class '" + value.getClass().getName() + "' from property '" + fieldName + "' is not supported");
        }
    }

    private static class Deserialiser {
        private final ObjectMapper objectMapper;

        public Deserialiser(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public void deserialise(ObjectNode faceNode, CardFaceModel cardFaceModel) {
            EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(cardFaceModel.getClass());

            deserialiseEntity(entityMetadata, faceNode, cardFaceModel);
        }

        private void deserialiseEntity(EntityMetadata entityMetadata, ObjectNode currentNode, Object entity) {
            Iterator<String> fieldNames = currentNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();

                JsonNode fieldValueJsonNode = currentNode.get(fieldName);
                PropertyMetadata propertyMetadata = entityMetadata.getProperty(fieldName);

                if (fieldValueJsonNode.isObject()) {
                    if (!propertyMetadata.isEntity())
                        throw new RuntimeException("Found a field '" + fieldName + "' that is a JSON object but the corresponding property with name '" + propertyMetadata.getName() + "' is not an entity");

                    Object propertyValue = propertyMetadata.getPropertyValue(entity);

                    if (propertyValue == null) {
                        // TODO: construct a new instance or fail?
                    }

                    deserialiseEntity(propertyMetadata.asEntity(), (ObjectNode)fieldValueJsonNode, propertyValue);
                    continue;
                }

                if (fieldValueJsonNode.isValueNode()) {
                    deserialiseValue(fieldValueJsonNode, propertyMetadata, entity);
                    continue;
                }

                throw new RuntimeException("Unhandled JSON node type '" + fieldValueJsonNode.getNodeType().name() + "'");
            }
        }

        private void deserialiseValue(JsonNode valueNode, PropertyMetadata propertyMetadata, Object entity) {
            if (propertyMetadata.getPropertyClass().equals(String.class)) {
                propertyMetadata.setPropertyValue(entity, valueNode.asText());
                return;
            }

            if (propertyMetadata.getPropertyClass().isEnum()) {
                propertyMetadata.setPropertyValue(entity, Enum.valueOf(propertyMetadata.getPropertyClass().asSubclass(Enum.class), valueNode.asText()));
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(Integer.class) || propertyMetadata.getPropertyClass().equals(Integer.TYPE)) {
                propertyMetadata.setPropertyValue(entity, valueNode.asInt());
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(Boolean.class) || propertyMetadata.getPropertyClass().equals(Boolean.TYPE)) {
                propertyMetadata.setPropertyValue(entity, valueNode.asBoolean());
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(Double.class) || propertyMetadata.getPropertyClass().equals(Double.TYPE)) {
                propertyMetadata.setPropertyValue(entity, valueNode.asDouble());
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(URL.class)) {
                String text = valueNode.asText();
                try {
                    propertyMetadata.setPropertyValue(entity, new URL(text));
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Error parsing URL from string '" + text + "' from property '" + propertyMetadata.getName() + "' on entity type '" + entity.getClass().getName() + "'",e);
                }
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(BufferedImage.class)) {
                try {
                    byte[] value = valueNode.binaryValue();
                    BufferedImage image = JsonUtils.deserialiseBufferedImage(value);
                    propertyMetadata.setPropertyValue(entity, image);
                } catch (IOException e) {
                    throw new RuntimeException("Error parsing buffered image from property '" + propertyMetadata.getName() + "' on entity type '" + entity.getClass().getName() + "'",e);
                }
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(EncounterSetInfo.class)) {
                String encounterSetKey = valueNode.asText();

                ProjectConfigurations.get().getEncounterSetConfiguration().findEncounterSetInfo(encounterSetKey).ifPresent(encounterSetInfo -> {
                    propertyMetadata.setPropertyValue(entity, encounterSetInfo);
                });

                return;
            }

            if (propertyMetadata.getPropertyClass().equals(CollectionInfo.class)) {
                String collectionKey = valueNode.asText();

                ProjectConfigurations.get().getCollectionConfiguration().findCollectionInfo(collectionKey).ifPresent(collectionInfo -> {
                    propertyMetadata.setPropertyValue(entity, collectionInfo);
                });

                return;
            }

            throw new RuntimeException("Failed to deserialise raw JSON value '" + valueNode.asText() + "' from property '" + propertyMetadata.getName() + "' on entity type '" + entity.getClass().getName() + "'");
        }
    }
}
