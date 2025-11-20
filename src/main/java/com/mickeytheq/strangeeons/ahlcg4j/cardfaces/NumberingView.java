package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.TextStyleUtils;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class NumberingView {
    private static final Rectangle COLLECTION_NUMBER_DRAW_REGION = new Rectangle(318, 512, 37, 10);
    private static final Rectangle ENCOUNTER_NUMBERS_DRAW_REGION = new Rectangle(247, 512, 55, 10);

    private final NumberingModel model;

    private final JTextField collectionNumberEditor;
    private final JTextField encounterNumberEditor;
    private final JTextField encounterTotalEditor;

    private final PortraitView encounterPortraitView;
    private final PortraitView collectionPortraitView;

    public NumberingView(NumberingModel model, ViewContext viewContext, Rectangle collectionPortraitDrawRegion, Rectangle encounterPortraitDrawRegion) {
        this.model = model;

        collectionPortraitView = PortraitView.createWithBlankImage(model.getCollectionPortraitModel(), collectionPortraitDrawRegion, viewContext::markChanged);
        collectionPortraitView.setBackgroundFilled(false);
        encounterPortraitView = PortraitView.createWithBlankImage(model.getEncounterPortraitModel(), encounterPortraitDrawRegion, viewContext::markChanged);
        encounterPortraitView.setBackgroundFilled(false);

        // collection
        collectionNumberEditor = EditorUtils.createTextField(8);
        collectionNumberEditor.setHorizontalAlignment(JTextField.RIGHT);

        // encounter
        encounterNumberEditor = EditorUtils.createTextField(8);
        encounterNumberEditor.setHorizontalAlignment(JTextField.RIGHT);
        encounterTotalEditor = EditorUtils.createTextField(4);

        EditorUtils.bindTextComponent(collectionNumberEditor, viewContext.wrapConsumerWithMarkedChanged(model::setCollectionNumber));
        EditorUtils.bindTextComponent(encounterNumberEditor, viewContext.wrapConsumerWithMarkedChanged(model::setEncounterNumber));
        EditorUtils.bindTextComponent(encounterTotalEditor, viewContext.wrapConsumerWithMarkedChanged(model::setEncounterTotal));

        collectionNumberEditor.setText(model.getCollectionNumber());
        encounterNumberEditor.setText(model.getEncounterNumber());
        encounterTotalEditor.setText(model.getEncounterTotal());
    }

    public JPanel createStandardCollectionEncounterPanel() {
        PortraitPanel collectionPortraitPanel = new PortraitPanel();
        collectionPortraitPanel.setPanelTitle("Collection portrait");
        collectionPortraitPanel.setPortrait(collectionPortraitView);

        JPanel collectionDetailPanel = new JPanel(new MigLayout());
        collectionDetailPanel.setBorder(BorderFactory.createTitledBorder("Collection"));
        collectionDetailPanel.add(new JLabel("Collection number: "), "aligny center");
        collectionDetailPanel.add(collectionNumberEditor, "wrap");

        PortraitPanel encounterPortraitPanel = new PortraitPanel();
        encounterPortraitPanel.setPanelTitle("Encounter portrait");
        encounterPortraitPanel.setPortrait(encounterPortraitView);

        JPanel encounterDetailPanel = new JPanel(new MigLayout());
        encounterDetailPanel.setBorder(BorderFactory.createTitledBorder("Encounter"));
        encounterDetailPanel.add(new JLabel("Encounter number: "), "aligny center");
        encounterDetailPanel.add(encounterNumberEditor, "split");
        encounterDetailPanel.add(new JLabel(" / "), "split");
        encounterDetailPanel.add(encounterTotalEditor, "split, wrap");

        // merge collection and encounter into a single tab
        JPanel collectionEncounterPanel = new JPanel(new MigLayout());
        collectionEncounterPanel.add(collectionDetailPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(collectionPortraitPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(encounterDetailPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(encounterPortraitPanel, "wrap, pushx, growx");

        return collectionEncounterPanel;
    }

    public void paintEncounterPortrait(PaintContext paintContext) {
        encounterPortraitView.paint(paintContext);
    }

    public void paintCollectionPortrait(PaintContext paintContext, boolean paintInverted) {
        // collection icon often needs inverting
        // the source icon is always black but the background on most cards is black as well therefore we want the icon inverted to white
        // this isn't always the case therefore it is at the discretion of the owning card face to decide
        collectionPortraitView.paint(paintContext, paintInverted);
    }

    public void paintEncounterNumbers(PaintContext paintContext) {
        if (!StringUtils.isEmpty(model.getEncounterNumber()) || !StringUtils.isEmpty(model.getEncounterTotal())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(TextStyleUtils.getEncounterNumberTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);

            String text = StringUtils.defaultIfEmpty(model.getEncounterNumber(), "") + " / " + StringUtils.defaultIfEmpty(model.getEncounterTotal(), "");

            markupRenderer.setMarkupText(text);
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), ENCOUNTER_NUMBERS_DRAW_REGION);
        }
    }

    public void paintCollectionNumber(PaintContext paintContext) {
        if (!StringUtils.isEmpty(model.getCollectionNumber())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(TextStyleUtils.getCollectionNumberTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);
            markupRenderer.setMarkupText(model.getCollectionNumber());
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), COLLECTION_NUMBER_DRAW_REGION);
        }
    }
}
