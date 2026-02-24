package com.mickeytheq.hades.scratchpad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.hades.serialise.JsonCardSerialiser;
import com.mickeytheq.hades.util.JsonUtils;

import java.io.IOException;
import java.io.StringWriter;

public class SerialiseScratch {
    public static void main(String[] args) throws Exception {
        new SerialiseScratch().run();
    }

    private void run() throws IOException {
        Event model = new Event();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");
        model.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon3(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon4(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon5(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setCost("3");
        model.getPlayerCardFieldsModel().setLevel(5);

        Card card = Cards.createCardModel(model, null);
        ObjectNode objectNode = JsonCardSerialiser.serialiseCard(card);

        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, objectNode);

        System.out.println(stringWriter.toString());

        Card newCard = JsonCardSerialiser.deserialiseCard(objectNode);

    }
}
