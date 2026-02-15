package com.mickeytheq.hades.core.global;

import com.mickeytheq.hades.core.model.Card;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class BasicCardDatabase implements CardDatabase {
    private final List<Card> cards;

    public BasicCardDatabase(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public Collection<Card> getCards() {
        return cards;
    }

    @Override
    public Path getSourcePathForCard(Card card) {
        return Paths.get("/some/path/to/card.hades");
    }

    @Override
    public void register(Consumer<CardDatabaseLoader> loader, Object registerKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(Object registerKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Card card) {
        cards.removeIf(o -> card.getId().equals(o.getId()));
        cards.add(card);
    }
}
