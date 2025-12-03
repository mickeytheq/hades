package com.mickeytheq.ahlcg4j.core.view.common;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.ahlcg4j.core.view.utils.*;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;

public class CommonCardFieldsView {
    private static final Rectangle COPYRIGHT_DRAW_REGION = new Rectangle(274, 1024, 202, 20);


    private final CommonCardFieldsModel model;
    private JTextField titleEditor;
    private JTextField subtitleEditor;
    private JToggleButton uniqueEditor;
    private JTextField copyrightEditor;
    private JTextField traitsEditor;
    private JTextArea keywordsEditor;
    private JTextArea rulesEditor;
    private JTextArea flavorTextEditor;
    private JTextArea victoryEditor;

    public CommonCardFieldsView(CommonCardFieldsModel model) {
        this.model = model;
    }

    public void createEditors(EditorContext editorContext) {
        // TODO: what about the helper tooltips for the legal traits etc
        titleEditor = EditorUtils.createTextField(30);
        subtitleEditor = EditorUtils.createTextField(30);
        uniqueEditor = new JToggleButton(ImageUtilities.createIconForSize(ImageUtils.loadImage(ImageUtils.UNIQUE_STAR_ICON_RESOURCE), 12));
        traitsEditor = EditorUtils.createTextField(30);
        keywordsEditor = EditorUtils.createTextArea(6, 30);
        rulesEditor = EditorUtils.createTextArea(6, 30);
        flavorTextEditor = EditorUtils.createTextArea(6, 30);
        victoryEditor = EditorUtils.createTextArea(2, 30);
        copyrightEditor = EditorUtils.createTextField(30);

        EditorUtils.bindTextComponent(titleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTitle));
        EditorUtils.bindTextComponent(subtitleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setSubtitle));
        EditorUtils.bindToggleButton(uniqueEditor, editorContext.wrapConsumerWithMarkedChanged(model::setUnique));
        EditorUtils.bindTextComponent(traitsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTraits));
        EditorUtils.bindTextComponent(keywordsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setKeywords));
        EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(model::setRules));
        EditorUtils.bindTextComponent(flavorTextEditor, editorContext.wrapConsumerWithMarkedChanged(model::setFlavourText));
        EditorUtils.bindTextComponent(victoryEditor, editorContext.wrapConsumerWithMarkedChanged(model::setVictory));
        EditorUtils.bindTextComponent(copyrightEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyright));

        titleEditor.setText(model.getTitle());
        subtitleEditor.setText(model.getSubtitle());
        uniqueEditor.setSelected(model.getUnique() != null && model.getUnique());
        traitsEditor.setText(model.getTraits());
        keywordsEditor.setText(model.getKeywords());
        rulesEditor.setText(model.getRules());
        flavorTextEditor.setText(model.getFlavourText());
        victoryEditor.setText(model.getVictory());
        copyrightEditor.setText(model.getCopyright());
    }

    public void addTitleEditorsToPanel(JPanel panel, boolean uniqueOption, boolean subtitleOption) {
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.TITLE));

        // have the unique button share the same layout cell as the title text editor
        if (uniqueOption) {
            panel.add(uniqueEditor, "split, wmax 35, hmin 25, hmax 25");
        }

        panel.add(titleEditor, "wrap, pushx, growx");

        if (subtitleOption)
            MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.SUBTITLE), subtitleEditor);
    }

    public void addNonTitleEditorsToPanel(JPanel panel, boolean includeVictory) {
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.TRAITS), traitsEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.KEYWORDS), keywordsEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.RULES), rulesEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.FLAVOR), flavorTextEditor);

        if (includeVictory)
            MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.VICTORY), victoryEditor);

        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.COPYRIGHT), copyrightEditor);
    }

    public CommonCardFieldsModel getModel() {
        return model;
    }

    public void paintTitles(PaintContext paintContext, Rectangle titleDrawRegion, Rectangle subtitleDrawRegion) {
        paintTitle(paintContext, titleDrawRegion);
        paintSubtitle(paintContext, subtitleDrawRegion);
    }

    public void paintTitle(PaintContext paintContext, Rectangle titleDrawRegion) {
        PaintUtils.paintTitle(paintContext, titleDrawRegion, getModel().getTitle(), getModel().isUnique());
    }

    public void paintSubtitle(PaintContext paintContext, Rectangle subtitleDrawRegion) {
        PaintUtils.paintSubtitle(paintContext, subtitleDrawRegion, getModel().getSubtitle());
    }

    public void paintBodyAndCopyright(PaintContext paintContext, Rectangle bodyDrawRegion) {
        paintBodyAndCopyright(paintContext, bodyDrawRegion, PageShape.RECTANGLE_SHAPE);
    }

    public void paintBodyAndCopyright(PaintContext paintContext, Rectangle bodyDrawRegion, PageShape pageShape) {
        String bodyString = composeBodyString();
        PaintUtils.paintBodyText(paintContext, bodyString, bodyDrawRegion, pageShape);

        paintCopyright(paintContext);
    }

    private String composeBodyString() {
        // TODO: optional spacing parameters

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(model.getTraits())) {
            // if there's no traits some space is added instead
            sb.append(MarkupUtils.getSpacerMarkup(1, 6));
        } else {
            sb.append("<center>");
            sb.append("<ts>");
            sb.append(model.getTraits());
            sb.append("</ts>");

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 0.5));
        }

        if (!StringUtils.isEmpty(model.getKeywords())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getKeywords());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(model.getRules())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getRules());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(model.getFlavourText())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<center>");
            sb.append("<fs>");
            sb.append(model.getFlavourText());
            sb.append("</fs>");

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(model.getVictory())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<center>");
            sb.append("<vic>");
            sb.append(model.getVictory());
            sb.append("</vic>");
        }

        return sb.toString();
    }

    public void paintCopyright(PaintContext paintContext) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getCopyrightTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(model.getCopyright());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), COPYRIGHT_DRAW_REGION);
    }
}
