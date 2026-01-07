package com.mickeytheq.hades.ui.cardreviewer;

import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.serialise.CardIO;

import java.nio.file.Path;
import java.util.List;

public class CardReviewSourceImpl implements ListItemSource<CardView> {
    private final List<Path> paths;
    private int currentIndex = -1;

    public CardReviewSourceImpl(List<Path> paths) {
        this.paths = paths;
    }

    @Override
    public CardView getCurrent() {
        Path currentPath = paths.get(currentIndex);
        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(currentPath);

        Card card = CardIO.readCard(currentPath, projectContext);
        CardView cardView = CardFaces.createCardView(card, projectContext);

        return cardView;
    }

    @Override
    public boolean next() {
        if ((currentIndex + 1) >= paths.size())
            return false;

        currentIndex++;

        return true;
    }

    @Override
    public boolean previous() {
        if (currentIndex <= 0)
            return false;

        currentIndex--;

        return true;
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public Integer getTotal() {
        return paths.size();
    }
}
