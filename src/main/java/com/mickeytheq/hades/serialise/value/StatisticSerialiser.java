package com.mickeytheq.hades.serialise.value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.serialise.ValueSerialiser;

public class StatisticSerialiser implements ValueSerialiser<Statistic> {
    private static final String PER_INVESTIGATOR_FIELD = "PerInvestigator";
    private static final String VALUE_FIELD = "Value";

    @Override
    public JsonNode serialiseValue(Statistic value, ObjectMapper objectMapper, ProjectContext projectContext) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(VALUE_FIELD, value.getValue());
        objectNode.put(PER_INVESTIGATOR_FIELD, value.isPerInvestigator());
        return objectNode;
    }

    @Override
    public Statistic deserialiseValue(JsonNode jsonNode, ProjectContext projectContext) {
        JsonNode valueNode = jsonNode.get(VALUE_FIELD);
        JsonNode perInvestigatorNode = jsonNode.get(PER_INVESTIGATOR_FIELD);

        String value = valueNode.asText();
        boolean perInvestigator = perInvestigatorNode.asBoolean();

        return new Statistic(value, perInvestigator);
    }
}
