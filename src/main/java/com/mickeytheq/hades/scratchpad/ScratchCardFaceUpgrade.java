package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.serialise.JsonCardSerialiser;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ScratchCardFaceUpgrade {
    public static void main(String[] args) {
        Path path = Paths.get("D:\\Temp\\Circus Ex Mortis SE-Hades\\01 - One Night Only\\Scenario Cards\\Token Effects.hades");

        JsonCardSerialiser.registerCardFaceModelUpgrader("ScenarioReference", 1, 2, (projectContext, faceNode) -> {
            faceNode.put("Smeg", "DUMMY");
        });

        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(path);
        Card card = CardIO.readCard(path, projectContext);
        int i = 1;
    }
}
