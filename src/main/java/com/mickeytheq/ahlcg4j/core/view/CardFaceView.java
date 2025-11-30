package com.mickeytheq.ahlcg4j.core.view;

import com.mickeytheq.ahlcg4j.core.Card;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;

import java.awt.*;

public interface CardFaceView<M extends CardFaceModel> {
    void initialiseView(Card card, CardFaceSide cardFaceSide, M cardFaceModel);

    Dimension getDimension();

    void createEditors(EditorContext editorContext);

    void paint(PaintContext paintContext);
}
