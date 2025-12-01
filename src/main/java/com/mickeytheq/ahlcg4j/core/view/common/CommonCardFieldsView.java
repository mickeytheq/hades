package com.mickeytheq.ahlcg4j.core.view.common;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.ahlcg4j.core.view.utils.*;
import net.miginfocom.layout.LC;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;

public class CommonCardFieldsView {
    private static final Rectangle COPYRIGHT_DRAW_REGION = new Rectangle(274, 1024, 202, 20);
    private static final Rectangle ARTIST_DRAW_REGION = new Rectangle(28, 1024, 242, 20);


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
    private JTextField artistEditor;

    private PortraitView artPortraitView;

    public CommonCardFieldsView(CommonCardFieldsModel model, Dimension artPortraitDimension) {
        this.model = model;

        artPortraitView = PortraitView.createWithDefaultImage(getModel().getArtPortraitModel(), artPortraitDimension);
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
        artistEditor = EditorUtils.createTextField(30);

        EditorUtils.bindTextComponent(titleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTitle));
        EditorUtils.bindTextComponent(subtitleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setSubtitle));
        EditorUtils.bindToggleButton(uniqueEditor, editorContext.wrapConsumerWithMarkedChanged(model::setUnique));
        EditorUtils.bindTextComponent(traitsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTraits));
        EditorUtils.bindTextComponent(keywordsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setKeywords));
        EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(model::setRules));
        EditorUtils.bindTextComponent(flavorTextEditor, editorContext.wrapConsumerWithMarkedChanged(model::setFlavourText));
        EditorUtils.bindTextComponent(victoryEditor, editorContext.wrapConsumerWithMarkedChanged(model::setVictory));
        EditorUtils.bindTextComponent(copyrightEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyright));
        EditorUtils.bindTextComponent(artistEditor, editorContext.wrapConsumerWithMarkedChanged(model::setArtist));

        titleEditor.setText(model.getTitle());
        subtitleEditor.setText(model.getSubtitle());
        uniqueEditor.setSelected(model.getUnique() != null && model.getUnique());
        traitsEditor.setText(model.getTraits());
        keywordsEditor.setText(model.getKeywords());
        rulesEditor.setText(model.getRules());
        flavorTextEditor.setText(model.getFlavourText());
        victoryEditor.setText(model.getVictory());
        copyrightEditor.setText(model.getCopyright());
        artistEditor.setText(model.getArtist());
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

    public void addNonTitleEditorsToPanel(JPanel panel) {
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.TRAITS), traitsEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.KEYWORDS), keywordsEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.RULES), rulesEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.FLAVOR), flavorTextEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.VICTORY), victoryEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.COPYRIGHT), copyrightEditor);
    }

    public JPanel createStandardArtPanel(EditorContext editorContext) {
        JPanel artistWithPortraitPanel = MigLayoutUtils.createPanel(new LC().insets("0"));

        PortraitPanel portraitPanel = artPortraitView.createPortraitPanel(editorContext, Language.string(InterfaceConstants.PORTRAIT));

        artistWithPortraitPanel.add(portraitPanel, "wrap, pushx, growx");

        JPanel artistPanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.ARTIST));
        MigLayoutUtils.addLabelledComponentWrap(artistPanel, Language.string(InterfaceConstants.ARTIST), artistEditor);

        artistWithPortraitPanel.add(artistPanel, "wrap, pushx, growx");

        return artistWithPortraitPanel;
    }

    public CommonCardFieldsModel getModel() {
        return model;
    }

    public void paintArtPortrait(PaintContext paintContext, Rectangle drawRegion) {
        artPortraitView.paint(paintContext, drawRegion, false);
    }

    public void paintTitles(PaintContext paintContext, Rectangle titleDrawRegion, Rectangle subtitleDrawRegion) {
        paintTitle(paintContext, titleDrawRegion);
        paintSubtitle(paintContext, subtitleDrawRegion);
    }

    public void paintTitle(PaintContext paintContext, Rectangle titleDrawRegion) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);

        MarkupUtils.applyTagMarkupConfiguration(markupRenderer);

        String titleText = getModel().getTitle();

        if (getModel().getUnique() != null && getModel().getUnique())
            titleText = "<uni>" + titleText;

        markupRenderer.setMarkupText(titleText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), titleDrawRegion);
    }

    public void paintSubtitle(PaintContext paintContext, Rectangle subtitleDrawRegion) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getSubtitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(getModel().getSubtitle());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), subtitleDrawRegion);
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
        markupRenderer.setDefaultStyle(TextStyleUtils.getBodyTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_LEFT);
        markupRenderer.setLineTightness(0.6f * 0.9f);
        markupRenderer.setTextFitting(MarkupRenderer.FIT_SCALE_TEXT);

        MarkupUtils.applyTagMarkupConfiguration(markupRenderer);

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
