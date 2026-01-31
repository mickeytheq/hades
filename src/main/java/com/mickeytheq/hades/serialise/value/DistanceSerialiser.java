package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;
import com.mickeytheq.hades.util.shape.Unit;

public class DistanceSerialiser implements ValueSerialiser<Distance> {
    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, Distance value, ProjectContext projectContext) {
        ObjectNode objectNode = currentNode.putObject(fieldName);
        objectNode.put("Amount", value.getAmount());
        objectNode.put("Unit", value.getUnit().name());
    }

    @Override
    public Distance deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        JsonNode amountNode = jsonNode.get("Amount");
        JsonNode unitNode = jsonNode.get("Unit");

        double amount = amountNode.asDouble();
        Unit unit = Unit.valueOf(unitNode.asText());

        return new Distance(amount, unit);
    }
}
