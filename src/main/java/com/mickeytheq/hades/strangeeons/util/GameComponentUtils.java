package com.mickeytheq.hades.strangeeons.util;

import ca.cgjennings.apps.arkham.component.GameComponent;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;
import resources.ResourceKit;

import java.nio.file.Path;

public class GameComponentUtils {
    public static GameComponent loadGameComponent(Path path) {

        if (path.toString().endsWith("." + CardIO.HADES_FILE_EXTENSION)) {
            ProjectContext projectContext = StandardProjectContext.getContextForContentPath(path);

            Card card = CardIO.readCard(path, projectContext);
            CardView cardView = CardFaces.createCardView(card, projectContext);
            CardGameComponent cardGameComponent = new CardGameComponent(cardView);
            return cardGameComponent;
        }
        else {
            return ResourceKit.getGameComponentFromFile(path.toFile(), true);
        }
    }
}
