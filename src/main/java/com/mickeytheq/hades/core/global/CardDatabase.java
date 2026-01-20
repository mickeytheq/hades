package com.mickeytheq.hades.core.global;

import com.mickeytheq.hades.core.model.Card;

import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * A database for accessing all the {@link com.mickeytheq.hades.core.model.Card} currently deemed in scope.
 *
 * Card in scope are:
 * - Those open in editors
 * - Those in the current project
 *
 * Cards leave scope when all of these cease to apply to a Card.
 *
 * Users of this class might include UX functions for searching for cards by title or content.
 *
 * TODO: Should entities returned by this be read-only or 'live' instances that can be updated? How might this interact
 * TODO: with cards open in editors? For example can Search/replace work directly on these entities or does it need to load a copy,
 * TODO: save it and refresh open editors (or not allow open editors in the first place)
 *
 * TODO: Cards returned from this class should be considered short-lived. Calls should not hold references for significant periods
 * TODO: of time as the backing data may change as cards are edited/changed by the user or other functions
 * TODO: confirm the above is accurate based on the previous TODO/decision
 */
public interface CardDatabase {
    // get all cards in scope
    Collection<Card> getCards();

    Path getSourcePathForCard(Card card);

    // TODO: listeners if needed to track cards entering/leaving scope

    // register Cards with the database
    // the return value is a unique 'key' that should be used to unregister these cards when they leave scope
    void register(Consumer<CardDatabaseLoader> loader, Object registerKey);

    void unregister(Object registerKey);
}
