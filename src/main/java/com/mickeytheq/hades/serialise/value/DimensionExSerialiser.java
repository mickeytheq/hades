package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.Unit;

public class DimensionExSerialiser implements ValueSerialiser<DimensionEx> {
    public static final String WIDTH_FIELD = "Width";
    public static final String HEIGHT_FIELD = "Height";
    public static final String UNIT_FIELD = "Unit";

    @Override
    public void serialiseValue(String fieldName, ObjectNode currentNode, DimensionEx value, ProjectContext projectContext) {
        ObjectNode objectNode = currentNode.putObject(fieldName);
        objectNode.put(WIDTH_FIELD, value.getWidth());
        objectNode.put(HEIGHT_FIELD, value.getWidth());
        objectNode.put(UNIT_FIELD, value.getUnit().name());
    }

    @Override
    public DimensionEx deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        JsonNode widthNode = jsonNode.get(WIDTH_FIELD);
        JsonNode heightNode = jsonNode.get(HEIGHT_FIELD);
        JsonNode unitNode = jsonNode.get(UNIT_FIELD);

        double width = widthNode.asDouble();
        double height = heightNode.asDouble();
        Unit unit = Unit.valueOf(unitNode.asText());

        return DimensionEx.create(unit, width, height);
    }
}
