package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.MarkupUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.MigLayoutUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.TextStyleUtils;
import net.miginfocom.layout.LC;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;

public class CommonCardFieldsView {
    private static final Rectangle COPYRIGHT_DRAW_REGION = new Rectangle(137, 512, 101, 10);
    private static final Rectangle ARTIST_DRAW_REGION = new Rectangle(14, 512, 121, 10);


    private final CommonCardFieldsModel model;
    private JTextField titleEditor;
    private JTextField copyrightEditor;
    private JTextField traitsEditor;
    private JTextArea keywordsEditor;
    private JTextArea rulesEditor;
    private JTextArea flavorTextEditor;
    private JTextArea victoryEditor;
    private JTextField artistEditor;

    private PortraitView artPortraitView;

    public CommonCardFieldsView(CommonCardFieldsModel model) {
        this.model = model;
    }

    public void createEditors(EditorContext editorContext, Rectangle artPortraitDrawRegion) {
        // TODO: what about the helper tooltips for the legal traits etc
        titleEditor = EditorUtils.createTextField(30);
        traitsEditor = EditorUtils.createTextField(30);
        keywordsEditor = EditorUtils.createTextArea(6, 30);
        rulesEditor = EditorUtils.createTextArea(6, 30);
        flavorTextEditor = EditorUtils.createTextArea(6, 30);
        victoryEditor = EditorUtils.createTextArea(2, 30);
        copyrightEditor = EditorUtils.createTextField(30);
        artistEditor = EditorUtils.createTextField(30);

        EditorUtils.bindTextComponent(titleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTitle));
        EditorUtils.bindTextComponent(traitsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTraits));
        EditorUtils.bindTextComponent(keywordsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setKeywords));
        EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(model::setRules));
        EditorUtils.bindTextComponent(flavorTextEditor, editorContext.wrapConsumerWithMarkedChanged(model::setFlavourText));
        EditorUtils.bindTextComponent(victoryEditor, editorContext.wrapConsumerWithMarkedChanged(model::setVictory));
        EditorUtils.bindTextComponent(copyrightEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyright));
        EditorUtils.bindTextComponent(artistEditor, editorContext.wrapConsumerWithMarkedChanged(model::setArtist));

        titleEditor.setText(model.getTitle());
        traitsEditor.setText(model.getTraits());
        keywordsEditor.setText(model.getKeywords());
        rulesEditor.setText(model.getRules());
        flavorTextEditor.setText(model.getFlavourText());
        victoryEditor.setText(model.getVictory());
        copyrightEditor.setText(model.getCopyright());
        artistEditor.setText(model.getArtist());

        artPortraitView = PortraitView.createWithDefaultImage(getModel().getArtPortraitModel(), artPortraitDrawRegion, editorContext::markChanged);
    }

    public void addTitleEditorToPanel(JPanel panel) {
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.TITLE), titleEditor);
    }

    public void addNonTitleEditorsToPanel(JPanel panel) {
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.TRAITS), traitsEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.KEYWORDS), keywordsEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.RULES), rulesEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.FLAVOR), flavorTextEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.VICTORY), victoryEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.COPYRIGHT), copyrightEditor);
    }

    public JPanel createStandardArtPanel() {
        JPanel artistWithPortraitPanel = MigLayoutUtils.createPanel(new LC().insets("0"));

        PortraitPanel portraitPanel = new PortraitPanel();
        portraitPanel.setPanelTitle(Language.string(InterfaceConstants.PORTRAIT));
        portraitPanel.setPortrait(artPortraitView);

        artistWithPortraitPanel.add(portraitPanel, "wrap, pushx, growx");

        JPanel artistPanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.ARTIST));
        MigLayoutUtils.addLabelledComponentWrap(artistPanel, Language.string(InterfaceConstants.ARTIST), artistEditor);

        artistWithPortraitPanel.add(artistPanel, "wrap, pushx, growx");

        return artistWithPortraitPanel;
    }

    public CommonCardFieldsModel getModel() {
        return model;
    }

    public void paintArtPortrait(PaintContext paintContext) {
        artPortraitView.paint(paintContext);
    }

    public void paintTitle(PaintContext paintContext, Rectangle titleDrawRegion) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(getModel().getTitle());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), titleDrawRegion);
    }

    public void paintBodyCopyrightArtist(PaintContext paintContext, Rectangle bodyDrawRegion) {
        String bodyString = composeBodyString();
        paintBodyText(paintContext, bodyString, bodyDrawRegion);

        paintCopyright(paintContext);
        paintArtist(paintContext);
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

    private void paintBodyText(PaintContext paintContext, String bodyText, Rectangle bodyDrawRegion) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();

        MarkupUtils.applyBodyMarkupConfiguration(markupRenderer);

        markupRenderer.setMarkupText(bodyText);
        markupRenderer.draw(paintContext.getGraphics(), bodyDrawRegion);
    }

    public void paintCopyright(PaintContext paintContext) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getCopyrightTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(model.getCopyright());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), COPYRIGHT_DRAW_REGION);
    }

    public void paintArtist(PaintContext paintContext) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getArtistTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT);
        markupRenderer.setMarkupText(model.getArtist());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), ARTIST_DRAW_REGION);
    }
}
