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
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.util.JsonUtils;
import com.mickeytheq.hades.util.VersionUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

/**
 * Handles converting a generic JSON object tree from/to a Card entity
 */
public class JsonCardSerialiser {
    private static final String FRONT_FACE_FIELD_NAME = "Front";
    private static final String BACK_FACE_FIELD_NAME = "Back";
    private static final String CARD_FACE_TYPE_FIELD_NAME = "Type";

    private static final String UNIQUE_ID_FIELD_NAME = "UniqueId";
    private static final String COMMENTS_FIELD_NAME = "Comments";
    private static final String VERSION_FIELD_NAME = "Version";
    private static final String METADATA_FIELD_NAME = "Metadata";
    private static final String LAST_MODIFIED_FIELD_NAME = "LastModified";
    private static final String HADES_SOFTWARE_VERSION = "HadesSoftwareVersion";

    private static final int CURRENT_CARD_VERSION = 1;

    public static ObjectNode serialiseCard(Card card) {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        ObjectNode cardNode = objectMapper.createObjectNode();

        cardNode.put(VERSION_FIELD_NAME, CURRENT_CARD_VERSION);

        // front face
        ObjectNode frontFaceNode = cardNode.putObject(FRONT_FACE_FIELD_NAME);
        serialiseCardFace(card.getFrontFaceModel(), objectMapper, frontFaceNode);

        // back face
        if (card.hasBack()) {
            ObjectNode backFaceNode = cardNode.putObject(BACK_FACE_FIELD_NAME);
            serialiseCardFace(card.getBackFaceModel(), objectMapper, backFaceNode);
        }

        // general fields
        cardNode.put(UNIQUE_ID_FIELD_NAME, card.getId());

        if (!StringUtils.isEmpty(card.getComments()))
            cardNode.put(COMMENTS_FIELD_NAME, card.getComments());

        // metadata block
        ObjectNode metadataObjectNode = cardNode.putObject(METADATA_FIELD_NAME);
        metadataObjectNode.put(LAST_MODIFIED_FIELD_NAME, ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        metadataObjectNode.put(HADES_SOFTWARE_VERSION, VersionUtils.getVersion());

        return cardNode;
    }

    private static void serialiseCardFace(CardFaceModel cardFaceModel, ObjectMapper objectMapper, ObjectNode faceNode) {
        String typeCode = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass()).getTypeCode();
        faceNode.put(CARD_FACE_TYPE_FIELD_NAME, typeCode);

        new Serialiser(objectMapper).serialise(cardFaceModel, faceNode);
    }

    public static Card deserialiseCard(ObjectNode objectNode) {
        checkCardVersion(objectNode);

        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();

        Card card = new Card();

        // general fields
        card.setId(objectNode.get(UNIQUE_ID_FIELD_NAME).asText());

        JsonNode commentsNode = objectNode.get(COMMENTS_FIELD_NAME);

        if (commentsNode != null)
            card.setComments(commentsNode.asText());

        ObjectNode frontFaceNode = (ObjectNode)objectNode.get(FRONT_FACE_FIELD_NAME);

        CardFaceModel frontFaceModel = deserialiseCardFace(objectMapper, frontFaceNode);
        card.setFrontFaceModel(frontFaceModel);

        ObjectNode backFaceNode = (ObjectNode)objectNode.get(BACK_FACE_FIELD_NAME);

        if (backFaceNode != null) {
            CardFaceModel backFaceModel = deserialiseCardFace(objectMapper, backFaceNode);
            card.setBackFaceModel(backFaceModel);
        }

        return card;
    }

    private static void checkCardVersion(ObjectNode objectNode) {
        JsonNode jsonNode = objectNode.get(VERSION_FIELD_NAME);

        if (jsonNode == null)
            throw new RuntimeException("No version field found in Card JSON object");

        if (!jsonNode.isIntegralNumber())
            throw new RuntimeException("Version field in Card JSON object is not an integer");

        int cardVersion = jsonNode.asInt();

        if (cardVersion > CURRENT_CARD_VERSION)
            throw new RuntimeException("Card was created with a newer version of Hades");

        if (cardVersion < CURRENT_CARD_VERSION) {
            // TODO: execute an upgrade from cardVersion to CURRENT_CARD_VERSION
            throw new UnsupportedOperationException("Upgrades not implemented");
        }

        // same version, nothing to do
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

                // check for the NullDiscriminator interface and skip if it identifies this entity as null/empty
                if (value instanceof NullDiscriminator) {
                    if (((NullDiscriminator) value).isNull())
                        continue;
                }

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

            if (value instanceof ImageProxy) {
                ImageProxy imageProxy = (ImageProxy)value;

                String identifier = imageProxy.save();

                if (identifier != null)
                    currentNode.put(fieldName, identifier);

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
        private final ProjectContext projectContext;

        public Deserialiser(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            projectContext = ProjectContexts.getCurrentContext();
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

            if (propertyMetadata.getPropertyClass().equals(ImageProxy.class)) {
                String imageIdentifier = valueNode.asText();
                ImageProxy imageProxy = ImageProxy.createFor(imageIdentifier);
                propertyMetadata.setPropertyValue(entity, imageProxy);
                return;
            }

            if (propertyMetadata.getPropertyClass().equals(EncounterSetInfo.class)) {
                String encounterSetKey = valueNode.asText();

                projectContext.getProjectConfiguration().getEncounterSetConfiguration().findEncounterSetInfo(encounterSetKey).ifPresent(encounterSetInfo -> {
                    propertyMetadata.setPropertyValue(entity, encounterSetInfo);
                });

                return;
            }

            if (propertyMetadata.getPropertyClass().equals(CollectionInfo.class)) {
                String collectionKey = valueNode.asText();

                projectContext.getProjectConfiguration().getCollectionConfiguration().findCollectionInfo(collectionKey).ifPresent(collectionInfo -> {
                    propertyMetadata.setPropertyValue(entity, collectionInfo);
                });

                return;
            }

            throw new RuntimeException("Failed to deserialise raw JSON value '" + valueNode.asText() + "' from property '" + propertyMetadata.getName() + "' on entity type '" + entity.getClass().getName() + "'");
        }
    }
}
