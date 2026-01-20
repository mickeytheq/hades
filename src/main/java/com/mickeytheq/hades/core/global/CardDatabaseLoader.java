package com.mickeytheq.hades.core.global;

import com.mickeytheq.hades.core.model.Card;

import java.nio.file.Path;

// Loader that is passed when registering cards
// This separate Loader allows a set of cards to be registered in a batch and opens the potential to
// do the load in a background thread without a major refactor
public interface CardDatabaseLoader {
    Object getRegisterKey();

    void registerCard(Card card, Path sourcePath);
}
