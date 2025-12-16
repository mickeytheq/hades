package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CardIO {
    public static final String HADES_FILE_EXTENSION = "hades";

    public static Card readCard(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            ObjectNode cardObjectNode = RawJsonSerialiser.readRawCardJson(reader);

            // TODO: perform version detection and automated upgrades
            // TODO: do this here or in the JsonCardSerialiser

            Card card = JsonCardSerialiser.deserialiseCard(cardObjectNode);

            return card;
        } catch (IOException e) {
            throw new RuntimeException("Error opening/reading card from path '" + path + "'", e);
        }
    }

    public static void writeCard(Path path, Card card) {
        ObjectNode cardObjectNode = JsonCardSerialiser.serialiseCard(card);

        try (Writer writer = Files.newBufferedWriter(path)) {
            RawJsonSerialiser.writeRawCardJson(writer, cardObjectNode);
        } catch (IOException e) {
            throw new RuntimeException("Error saving/writing card to path '" + path + "'", e);
        }
    }
}
