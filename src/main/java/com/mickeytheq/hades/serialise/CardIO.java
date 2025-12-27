package com.mickeytheq.hades.serialise;

import ca.cgjennings.apps.arkham.project.Project;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main utility methods for reading and writing the Card model from/to an output
 *
 * The implementation is deliberately in two steps
 *
 * The first step takes the raw input and converts it into a generic JSON object.
 * The second step takes the JSON object and converts it into the card + card face object model.
 *
 * The reason these two steps are distinct is to support changes to the card/face object model over time. Because
 * the first stage will always succeed, as long as JSON is always the syntax, any upgrades from older to newer
 * card model versions can occur between the two steps. This allows the upgrade code to work on something more
 * structured than a raw input string/stream. The alternative is to have older versions of the card models with the upgrade
 * code upgrading from one structure to another. But given most changes are likely minor it's more pragmatic to just work
 * on the JSON structure and the card model always represents the latest version
 */
public class CardIO {
    public static final String HADES_FILE_EXTENSION = "hades";

    public static Card readCard(Path path, ProjectContext projectContext) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                ObjectNode cardObjectNode = RawJsonSerialiser.readRawCardJson(reader);

                // TODO: perform version detection and automated upgrades
                // TODO: do this here or in the JsonCardSerialiser
                // TODO: do we want versions on the Card the CardFaces or both?

                Card card = JsonCardSerialiser.deserialiseCard(cardObjectNode);

                return card;
            } catch (IOException e) {
                throw new RuntimeException("Error opening/reading card from path '" + path + "'", e);
            }
        });
    }

    public static void writeCard(Path path, Card card, ProjectContext projectContext) {
        ProjectContexts.withContext(projectContext, () -> {
            ObjectNode cardObjectNode = JsonCardSerialiser.serialiseCard(card);

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                RawJsonSerialiser.writeRawCardJson(writer, cardObjectNode);
            } catch (IOException e) {
                throw new RuntimeException("Error saving/writing card to path '" + path + "'", e);
            }
        });
    }

    public static void writeCard(Writer writer, Card card, ProjectContext projectContext) {
        ProjectContexts.withContext(projectContext, () -> {
            ObjectNode cardObjectNode = JsonCardSerialiser.serialiseCard(card);

            try {
                RawJsonSerialiser.writeRawCardJson(writer, cardObjectNode);
            } catch (IOException e) {
                throw new RuntimeException("Error saving/writing card to Writer", e);
            }
        });
    }
}
