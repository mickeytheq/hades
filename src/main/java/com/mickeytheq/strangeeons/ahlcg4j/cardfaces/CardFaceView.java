package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import com.mickeytheq.strangeeons.ahlcg4j.CardGameComponent;

import javax.swing.*;
import java.awt.*;

public interface CardFaceView<M extends CardFaceModel> {
    void initialiseView(Card card, CardFaceSide cardFaceSide, M cardFaceModel);

    Dimension getDimension();

    void createEditors(EditorContext editorContext);

    void paint(PaintContext paintContext);
}
