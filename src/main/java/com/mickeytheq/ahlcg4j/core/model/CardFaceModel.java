package com.mickeytheq.ahlcg4j.core.model;

// TODO: do we still need this or is the annotation sufficient?
// TODO: keeping it does allow generic type-checking

/**
 * Base interface for a card face model. It is paired with a {@link com.mickeytheq.ahlcg4j.core.view.CardFaceView} in a 1-to-1 relationship.
 *
 * Ideally the model should be a reasonably dumb entity that has little to no logic and is simply a data repository with getters/setters
 *
 * A model does the following
 * - holds all persistent data
 * - provides the backing data for populating visual editors and drawing content
 * - provides a means to serialise/deserialise
 * - has a zero-arg constructor to allow for deserialisation
 *
 * A model does NOT do the following - these should generally be handled by the corresponding {@link com.mickeytheq.ahlcg4j.core.view.CardFaceView}
 *
 * - create any visual controls or elements
 * - performing any painting of the card face
 * - handle any complex business logic. basic helpers to aid accessing the model information is allowed
 * - have any reference to or visibility of a {@link com.mickeytheq.ahlcg4j.core.view.CardFaceView}. This is important to ensure models can be serialised/manipulated
 *   in bulk scenarios without any visual element being required
 * - any awareness of whether the face is the front or the back on a card
 */
public interface CardFaceModel {
}
