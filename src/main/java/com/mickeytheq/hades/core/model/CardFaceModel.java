package com.mickeytheq.hades.core.model;

/**
 * Base interface for a card face model. It is paired with a {@link com.mickeytheq.hades.core.view.CardFaceView} in a 1-to-1 relationship.
 *
 * Ideally the model should be a reasonably dumb entity that has little to no logic and is simply a data repository with getters/setters
 *
 * A model must do the following
 * - holds all persistent data
 * - provides the backing data for populating visual editors and drawing content
 * - provides a means to serialise/deserialise
 * - has a zero-arg constructor to allow for deserialisation
 *
 * A model may do the following
 * - provide BASIC functions to interact with the model data. For example skill icons where there are 5 different fields. An accessor to get them as a list
 *   with nulls removed is allowed
 *
 * A model does NOT do the following - these should generally be handled by the corresponding {@link com.mickeytheq.hades.core.view.CardFaceView}
 *
 * - create any visual controls, for example those used by users to edit the card information
 * - performing any painting of the card face
 * - have any reference to or visibility of a {@link com.mickeytheq.hades.core.view.CardFaceView}. This is important to ensure models can be serialised/manipulated
 *   in bulk scenarios without any visual element being required
 * - have any reference to the owning {@link Card}
 * - have any reference to or visibility of the other {@link CardFaceModel} that may be present on the {@link Card}. For example the front face model should not be interacting
 *   in any way with the back face model. This allows model implementations to be independent of each other
 * - any awareness of whether the face is the front face or the back face on a {@link Card}
 */
public interface CardFaceModel {
}
