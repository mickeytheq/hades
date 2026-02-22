package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;
import com.mickeytheq.hades.util.shape.Unit;

public class DistanceSerialiser implements ValueSerialiser<Distance> {
    public static final String AMOUNT_FIELD = "Amount";
    public static final String UNIT_FIELD = "Unit";

    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, Distance value, ProjectContext projectContext) {
        ObjectNode objectNode = currentNode.putObject(fieldName);
        objectNode.put(AMOUNT_FIELD, value.getAmount());
        objectNode.put(UNIT_FIELD, value.getUnit().name());
    }

    @Override
    public Distance deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        JsonNode amountNode = jsonNode.get(AMOUNT_FIELD);
        JsonNode unitNode = jsonNode.get(UNIT_FIELD);

        double amount = amountNode.asDouble();
        Unit unit = Unit.valueOf(unitNode.asText());

        return new Distance(amount, unit);
    }
}
