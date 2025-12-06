package com.mickeytheq.hades.core.view;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.awt.*;

/**
 * Provides the 'view' on top of a {@link CardFaceModel}
 *
 * The {@link CardFaceView} is created on top of a {@link CardFaceModel} when visual elements are required.
 * The view may not be required for certain bulk operations
 *
 * A view does the following
 * - creates visual editors that allows users to edit the model information
 * - paints/draws the visual card face
 *
 * A view does NOT do the following
 * - have any persistent state. any persistent information must be managed by the model
 * - handle any serialisation. this is the model's responsibility
 */
public interface CardFaceView {
    /**
     * called immediately after the view is constructed to set the owning {@link CardView}, the side this view is for
     * and the model used to back this view
     */
    void initialiseView(CardView cardView, CardFaceSide cardFaceSide, CardFaceModel cardFaceModel);

    /**
     * Returns the dimension of the card face being drawn
     */
    Dimension getDimension();

    /**
     * Instructs the view to build editor controls for a user to interact with the model.
     *
     * Typically called each time a card is opened for editing
     */
    void createEditors(EditorContext editorContext);

    /**
     * Instructs the view to draw the card face model content
     *
     * Typically called after a change is made to an editor control (when a user is editing) or when exporting a card image
     */
    void paint(PaintContext paintContext);
}
