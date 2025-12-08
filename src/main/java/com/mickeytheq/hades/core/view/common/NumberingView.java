package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.graphics.filters.InversionFilter;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.project.CollectionInfo;
import com.mickeytheq.hades.core.project.EncounterSetInfo;
import com.mickeytheq.hades.core.project.ProjectConfiguration;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.model.common.NumberingModel;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Optional;

public class NumberingView {
    private static final Rectangle COLLECTION_NUMBER_DRAW_REGION = new Rectangle(636, 1024, 74, 20);
    private static final Rectangle ENCOUNTER_NUMBERS_DRAW_REGION = new Rectangle(494, 1024, 110, 20);

    private final NumberingModel model;

    private JComboBox<CollectionInfo> collectionEditor;
    private JTextField collectionNumberEditor;
    private JComboBox<EncounterSetInfo> encounterSetEditor;
    private JTextField encounterNumberEditor;
    private JTextField encounterTotalEditor;

    public NumberingView(NumberingModel model, Dimension collectionPortraitDimension, Dimension encounterPortraitDimension) {
        this.model = model;
    }

    public void createEditors(EditorContext editorContext) {
        ProjectConfiguration projectConfiguration = ProjectConfiguration.get();

        // collection
        collectionEditor = EditorUtils.createNullableComboBox();

        for (CollectionInfo collectionInfo : projectConfiguration.getCollectionConfiguration().getCollectionInfos()) {
            collectionEditor.addItem(collectionInfo);
        }

        collectionNumberEditor = EditorUtils.createTextField(8);
        collectionNumberEditor.setHorizontalAlignment(JTextField.RIGHT);

        // encounter
        encounterSetEditor = EditorUtils.createNullableComboBox();

        for (EncounterSetInfo encounterSetInfo : projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos()) {
            encounterSetEditor.addItem(encounterSetInfo);
        }

        encounterNumberEditor = EditorUtils.createTextField(8);
        encounterNumberEditor.setHorizontalAlignment(JTextField.RIGHT);
        encounterTotalEditor = EditorUtils.createTextField(4);

        EditorUtils.bindComboBox(collectionEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollection));
        EditorUtils.bindTextComponent(collectionNumberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollectionNumber));
        EditorUtils.bindComboBox(encounterSetEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterSet));
        EditorUtils.bindTextComponent(encounterNumberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterNumber));
        EditorUtils.bindTextComponent(encounterTotalEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterTotal));

        collectionEditor.setSelectedItem(model.getCollection());
        collectionNumberEditor.setText(model.getCollectionNumber());
        encounterSetEditor.setSelectedItem(model.getEncounterSet());
        encounterNumberEditor.setText(model.getEncounterNumber());
        encounterTotalEditor.setText(model.getEncounterTotal());
    }

    public JPanel createStandardCollectionEncounterPanel(EditorContext editorContext) {
        JPanel collectionDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COLLECTION));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTION), collectionEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTIONNUMBER), collectionNumberEditor);

        JPanel encounterDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.ENCOUNTERSET));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(encounterDetailPanel, Language.string(InterfaceConstants.ENCOUNTERSET), encounterSetEditor);
        MigLayoutUtils.addLabel(encounterDetailPanel, "Encounter number");
        encounterDetailPanel.add(encounterNumberEditor, "split");
        encounterDetailPanel.add(new JLabel(" / "), "split");
        encounterDetailPanel.add(encounterTotalEditor, "split, wrap");

        // merge collection and encounter into a single tab
        JPanel collectionEncounterPanel = MigLayoutUtils.createOrganiserPanel();
        collectionEncounterPanel.add(collectionDetailPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(encounterDetailPanel, "wrap, pushx, growx");

        return collectionEncounterPanel;
    }

    public void paintEncounterPortrait(PaintContext paintContext, Rectangle encounterPortraitDrawRegion) {
        if (model.getEncounterSet() == null)
            return;

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), model.getEncounterSet().getImage(), encounterPortraitDrawRegion);
    }

    public void paintCollectionPortrait(PaintContext paintContext, Rectangle collectionPortraitDrawRegion, boolean paintInverted) {
        if (model.getCollection() == null)
            return;

        // collection icon sometimes needs inverting
        // the source icon is always black but the background on most cards is black as well therefore we want the icon inverted to white
        // this isn't always the case therefore it is at the discretion of the owning card face to decide
        BufferedImage collectionImage = model.getCollection().getImage();

        if (paintInverted) {
            BufferedImageOp inversionOp = new InversionFilter();
            collectionImage = inversionOp.filter(collectionImage, null);
        }

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), collectionImage, collectionPortraitDrawRegion);
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
