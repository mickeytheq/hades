package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.CardFaceTypeRegister;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.model.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.hades.core.model.entity.EntityMetadata;
import com.mickeytheq.hades.core.model.entity.EntityPropertyMetadata;
import com.mickeytheq.hades.core.model.entity.PropertyMetadata;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.core.view.component.DimensionExComponent;
import com.mickeytheq.hades.serialise.value.*;
import com.mickeytheq.hades.util.JsonUtils;
import com.mickeytheq.hades.util.VersionUtils;
import com.mickeytheq.hades.util.shape.DimensionEx;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Handles converting a generic JSON object tree from/to a Card entity
 */
public class JsonCardSerialiser {
    private static final Logger logger = LogManager.getLogger(JsonCardSerialiser.class);

    private static final String FRONT_FACE_FIELD_NAME = "Front";
    private static final String BACK_FACE_FIELD_NAME = "Back";
    private static final String CARD_FACE_TYPE_FIELD_NAME = "Type";

    private static final String UNIQUE_ID_FIELD_NAME = "UniqueId";
    private static final String COMMENTS_FIELD_NAME = "Comments";
    private static final String VERSION_FIELD_NAME = "Version";
    private static final String AUDIT_FIELD_NAME = "Audit";
    private static final String LAST_MODIFIED_FIELD_NAME = "LastModified";
    private static final String HADES_SOFTWARE_VERSION = "HadesSoftwareVersion";

    private static final int CURRENT_CARD_VERSION = 1;

    private static final Map<Class<?>, ValueSerialiser<?>> VALUE_SERIALISERS = new HashMap<>();

    private static final Map<MultiKey<Object>, CardFaceModelUpgrader> CARD_FACE_MODEL_UPGRADERS = new HashMap<>();

    private static final EntityMetadata CARD_ENTITY_METADATA = AnnotatedEntityMetadataBuilder.build(Card.class);

    static {
        VALUE_SERIALISERS.put(String.class, new StringSerialiser());

        VALUE_SERIALISERS.put(Integer.class, new IntegerSerialiser());
        VALUE_SERIALISERS.put(Integer.TYPE, new IntegerSerialiser());

        VALUE_SERIALISERS.put(Double.class, new DoubleSerialiser());
        VALUE_SERIALISERS.put(Double.TYPE, new DoubleSerialiser());

        VALUE_SERIALISERS.put(Boolean.class, new BooleanSerialiser());
        VALUE_SERIALISERS.put(Boolean.TYPE, new BooleanSerialiser());

        VALUE_SERIALISERS.put(Distance.class, new DistanceSerialiser());
        VALUE_SERIALISERS.put(DimensionEx.class, new DimensionExSerialiser());
        VALUE_SERIALISERS.put(Statistic.class, new StatisticSerialiser());
        VALUE_SERIALISERS.put(URL.class, new UrlSerialiser());
        VALUE_SERIALISERS.put(EncounterSetConfiguration.class, new EncounterSetConfigurationSerialiser());
        VALUE_SERIALISERS.put(CollectionConfiguration.class, new CollectionConfigurationSerialiser());
        VALUE_SERIALISERS.put(ImageProxy.class, new ImageProxySerialiser());
    }

    public static void registerCardFaceModelUpgrader(String typeCode, int fromVersion, int toVersion, CardFaceModelUpgrader upgrader) {
        if (toVersion != fromVersion + 1)
            throw new RuntimeException("Versions must be sequential with no gaps");

        CARD_FACE_MODEL_UPGRADERS.put(new MultiKey<>(typeCode, fromVersion, toVersion), upgrader);
    }

    private static CardFaceModelUpgrader getCardFaceModelUpgrader(String typeCode, int fromVersion, int toVersion) {
        if (toVersion != fromVersion + 1)
            throw new RuntimeException("Versions must be sequential with no gaps");

        CardFaceModelUpgrader upgrader = CARD_FACE_MODEL_UPGRADERS.get(new MultiKey<Object>(typeCode, fromVersion, toVersion));

        if (upgrader == null)
            throw new RuntimeException("No card face model upgrader exists to upgrade card face of type '" + typeCode + "' from version " + fromVersion + " to " + toVersion);

        return upgrader;
    }

    public static ObjectNode serialiseCard(Card card) {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        ObjectNode cardNode = objectMapper.createObjectNode();

        cardNode.put(VERSION_FIELD_NAME, CURRENT_CARD_VERSION);

        // serialise the card fields
        new Serialiser(objectMapper).serialiseEntity(CARD_ENTITY_METADATA, card, cardNode);

        // front face
        ObjectNode frontFaceNode = cardNode.putObject(FRONT_FACE_FIELD_NAME);
        serialiseCardFace(card.getFrontFaceModel(), objectMapper, frontFaceNode);

        // back face
        if (card.hasBack()) {
            ObjectNode backFaceNode = cardNode.putObject(BACK_FACE_FIELD_NAME);
            serialiseCardFace(card.getBackFaceModel(), objectMapper, backFaceNode);
        }

        // audit trail block
        ObjectNode metadataObjectNode = cardNode.putObject(AUDIT_FIELD_NAME);
        metadataObjectNode.put(LAST_MODIFIED_FIELD_NAME, ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        metadataObjectNode.put(HADES_SOFTWARE_VERSION, VersionUtils.getVersion());

        return cardNode;
    }

    private static void serialiseCardFace(CardFaceModel cardFaceModel, ObjectMapper objectMapper, ObjectNode faceNode) {
        CardFaceTypeRegister.CardFaceInfo cardFaceInfo = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass());
        faceNode.put(CARD_FACE_TYPE_FIELD_NAME, cardFaceInfo.getTypeCode());
        faceNode.put(VERSION_FIELD_NAME, cardFaceInfo.getVersion());

        EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(cardFaceModel.getClass());
        new Serialiser(objectMapper).serialiseEntity(entityMetadata, cardFaceModel, faceNode);
    }

    public static Card deserialiseCard(ObjectNode objectNode) {
        checkCardVersion(objectNode);

        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();

        // before beginning deserialisation remove nodes that are handled by separate serialisation or not deserialised at all
        // this is necessary as the deserialisation will error if there are fields that aren't mapped anywhere
        //
        // for example the front/back card faces are deserialised separately from the Card itself. this is because the card faces
        // have individual version control so must be done on their own so any upgrading can be done independently of the other face
        // and the card itself
        ObjectNode frontFaceNode = (ObjectNode)objectNode.remove(FRONT_FACE_FIELD_NAME);
        ObjectNode backFaceNode = (ObjectNode)objectNode.remove(BACK_FACE_FIELD_NAME);

        // audit fields are generated on serialisation but not mapped to the Card model
        objectNode.remove(AUDIT_FIELD_NAME);

        // remove the version
        objectNode.remove(VERSION_FIELD_NAME);

        // card fields
        Card card = new Card();

        // do the Card deserialisation. see above about this being separate from the card faces
        new Deserialiser(objectMapper).deserialiseEntity(CARD_ENTITY_METADATA, objectNode, card);

        // deserialise the card faces
        CardFaceModel frontFaceModel = deserialiseCardFace(objectMapper, frontFaceNode);
        card.setFrontFaceModel(frontFaceModel);

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
        // remove the type and version fields here
        // if left then the latter serialisation code will treat it as an unhandled field
        JsonNode typeCodeNode = faceNode.remove(CARD_FACE_TYPE_FIELD_NAME);

        if (typeCodeNode == null)
            throw new RuntimeException("No " + CARD_FACE_TYPE_FIELD_NAME + " field found on card face node '" + faceNode.toPrettyString() + "'");

        if (!typeCodeNode.isTextual())
            throw new RuntimeException(CARD_FACE_TYPE_FIELD_NAME + " field should be text but is not. JSON node '" + faceNode.toPrettyString() + "'");

        String typeCode = typeCodeNode.asText();

        JsonNode versionNode = faceNode.remove(VERSION_FIELD_NAME);

        if (versionNode == null)
            throw new RuntimeException("No " + VERSION_FIELD_NAME + " field found on card face node '" + faceNode.toPrettyString() + "'");

        if (!versionNode.isIntegralNumber())
            throw new RuntimeException(VERSION_FIELD_NAME + " field should be integer but is not. JSON node '" + faceNode.toPrettyString() + "'");

        int deserialisingCardVersion = versionNode.asInt();

        // look up the card face information
        CardFaceTypeRegister cardFaceTypeRegister = CardFaceTypeRegister.get();
        CardFaceTypeRegister.CardFaceInfo cardFaceInfo = cardFaceTypeRegister.getInfoForTypeCode(typeCode);

        // check the version and perform any upgrades
        checkCardFaceVersion(faceNode, cardFaceInfo, deserialisingCardVersion);

        // do the serialisation
        CardFaceModel cardFaceModel = Cards.createFaceModelForTypeCode(typeCode, ProjectContexts.getCurrentContext());

        EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(cardFaceModel.getClass());
        new Deserialiser(objectMapper).deserialiseEntity(entityMetadata, faceNode, cardFaceModel);

        return cardFaceModel;
    }

    private static void checkCardFaceVersion(ObjectNode faceNode, CardFaceTypeRegister.CardFaceInfo cardFaceInfo, int deserialisingCardVersion) {
        int modelVersion = cardFaceInfo.getVersion();
        String typeCode = cardFaceInfo.getTypeCode();

        if (deserialisingCardVersion > modelVersion)
            throw new RuntimeException("Found a card face with type code '" + typeCode + "' whose version " + deserialisingCardVersion + " is greater than the current model version " + modelVersion + ". JSON node '" + faceNode.toPrettyString() + "'");

        // versions the same, nothing to do
        if (deserialisingCardVersion == modelVersion)
            return;

        // otherwise an upgrade is required
        int currentVersion = deserialisingCardVersion;

        // perform step-by-step upgrades until the model version is reached
        while (currentVersion < modelVersion) {
            int upgradeToVersion = currentVersion + 1;
            CardFaceModelUpgrader upgrader = getCardFaceModelUpgrader(typeCode, currentVersion, upgradeToVersion);
            upgrader.upgrade(ProjectContexts.getCurrentContext(), faceNode);

            logger.info("Upgraded card of type '" + typeCode + "' from version " + currentVersion + " to version " + upgradeToVersion);

            currentVersion++;
        }
    }

    private static class Serialiser {
        private final ObjectMapper objectMapper;
        private final ProjectContext projectContext;

        public Serialiser(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            this.projectContext = ProjectContexts.getCurrentContext();
        }

        public void serialiseEntity(EntityMetadata entityMetadata, Object entity, ObjectNode currentNode) {
            for (PropertyMetadata propertyMetadata : entityMetadata.getProperties()) {
                Object value = propertyMetadata.getPropertyValue(entity);

                if (value == null)
                    continue;

                // property metadata may have awareness of when a property should be included
                // for example a default value like 0 for an integer property or an empty entity
                if (!propertyMetadata.shouldInclude(value))
                    continue;

                if (propertyMetadata.isValue()) {
                    serialiseValue(value, propertyMetadata, currentNode);
                    continue;
                }

                if (propertyMetadata.isEntity()) {
                    EntityPropertyMetadata entityPropertyMetadata = ((EntityPropertyMetadata)propertyMetadata);

                    if (!entityPropertyMetadata.shouldInclude(value))
                        continue;

                    ObjectNode childEntityNode = currentNode.putObject(propertyMetadata.getName());

                    serialiseEntity(propertyMetadata.asEntity(), value, childEntityNode);
                }
            }
        }

        private void serialiseValue(Object value, PropertyMetadata propertyMetadata, ObjectNode currentNode) {
            String propertyName = propertyMetadata.getName();

            // handle enums specially
            if (propertyMetadata.getPropertyClass().isEnum()) {
                currentNode.put(propertyName, ((Enum<?>)value).name());
                return;
            }

            ValueSerialiser valueSerialiser = VALUE_SERIALISERS.get(value.getClass());

            if (valueSerialiser == null)
                throw new RuntimeException("Value '" + value + "' of class '" + propertyMetadata.getPropertyClass().getName() + "' from property '" + propertyName + "' is not supported by any value serialiser");

            try {
                valueSerialiser.serialiseValue(propertyName, currentNode, value, projectContext);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialise value '" + value + "' of class '" + propertyMetadata.getPropertyClass().getName() + "' from property '" + propertyName + "'");
            }
        }
    }

    private static class Deserialiser {
        private final ObjectMapper objectMapper;
        private final ProjectContext projectContext;

        public Deserialiser(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            projectContext = ProjectContexts.getCurrentContext();
        }

        public void deserialiseEntity(EntityMetadata entityMetadata, ObjectNode currentNode, Object entity) {
            Iterator<String> fieldNames = currentNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();

                JsonNode fieldValueJsonNode = currentNode.get(fieldName);
                PropertyMetadata propertyMetadata = entityMetadata.getProperty(fieldName);

                if (propertyMetadata.isEntity()) {
                    if (!fieldValueJsonNode.isObject())
                        throw new RuntimeException("Found a field '" + fieldName + "' that with a corresponding property with name '" + propertyMetadata.getName() + "' where the property is an entity but the JSON field is not an object");

                    Object propertyValue = propertyMetadata.getPropertyValue(entity);

                    if (propertyValue == null)
                        throw new RuntimeException("Found a field '" + fieldName + "' that is a JSON object but the corresponding property with name '" + propertyMetadata.getName() + "' is null. Model entities should be initialised by their owning object during construction");

                    deserialiseEntity(propertyMetadata.asEntity(), (ObjectNode)fieldValueJsonNode, propertyValue);
                    continue;
                }

                if (propertyMetadata.isValue()) {
                    deserialiseValue(fieldValueJsonNode, propertyMetadata, entity);
                    continue;
                }

                throw new RuntimeException("Unhandled JSON node type '" + fieldValueJsonNode.getNodeType().name() + "'");
            }
        }

        private void deserialiseValue(JsonNode jsonNode, PropertyMetadata propertyMetadata, Object entity) {
            // handle enums specially
            if (propertyMetadata.getPropertyClass().isEnum()) {
                propertyMetadata.setPropertyValue(entity, Enum.valueOf(propertyMetadata.getPropertyClass().asSubclass(Enum.class), jsonNode.asText()));
                return;
            }

            ValueSerialiser<?> valueSerialiser = VALUE_SERIALISERS.get(propertyMetadata.getPropertyClass());

            if (valueSerialiser == null)
                throw new RuntimeException("No value serialiser found for raw JSON value '" + jsonNode.asText() + "' from property '" + propertyMetadata.getName() + "' on entity type '" + entity.getClass().getName() + "'");

            Object value;
            try {
                value = valueSerialiser.deserialiseValue(jsonNode, projectContext);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialise raw JSON value '" + jsonNode.asText() + "' from property '" + propertyMetadata.getName() + "' on entity type '" + entity.getClass().getName() + "'", e);
            }

            propertyMetadata.setPropertyValue(entity, value);
        }
    }
}
