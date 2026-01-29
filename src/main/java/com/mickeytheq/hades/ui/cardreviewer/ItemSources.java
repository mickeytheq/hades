package com.mickeytheq.hades.ui.cardreviewer;

import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.serialise.CardIO;

import java.nio.file.Path;

public class ItemSources {
    public static ItemSource<Card> cardFromPath(ItemSource<Path> pathSource) {
        return new TransformingItemSource<>(pathSource, path -> {
            ProjectContext projectContext = StandardProjectContext.getContextForContentPath(path);

            return CardIO.readCard(path, projectContext);
        });
    }

    public static ItemSource<CardView> cardViewFromPath(ItemSource<Path> pathSource) {
        return new TransformingItemSource<>(pathSource, path -> {
            ProjectContext projectContext = StandardProjectContext.getContextForContentPath(path);

            Card card = CardIO.readCard(path, projectContext);
            CardView cardView = CardFaces.createCardView(card, projectContext);

            return cardView;
        });
    }
}
