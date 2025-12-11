package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.view.utils.*;
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
    private JSpinner afterTraitsSpaceEditor;
    private JTextArea keywordsEditor;
    private JSpinner afterKeywordsSpaceEditor;
    private JTextArea rulesEditor;
    private JSpinner afterRulesSpaceEditor;
    private JTextArea flavourTextEditor;
    private JSpinner afterFlavourTextSpaceEditor;
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
        afterTraitsSpaceEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
        keywordsEditor = EditorUtils.createTextArea(6, 30);
        afterKeywordsSpaceEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
        rulesEditor = EditorUtils.createTextArea(6, 30);
        afterRulesSpaceEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
        flavourTextEditor = EditorUtils.createTextArea(6, 30);
        afterFlavourTextSpaceEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
        victoryEditor = EditorUtils.createTextArea(2, 30);
        copyrightEditor = EditorUtils.createTextField(30);

        EditorUtils.bindTextComponent(titleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTitle));
        EditorUtils.bindTextComponent(subtitleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setSubtitle));
        EditorUtils.bindToggleButton(uniqueEditor, editorContext.wrapConsumerWithMarkedChanged(model::setUnique));
        EditorUtils.bindTextComponent(traitsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTraits));
        EditorUtils.bindSpinner(afterTraitsSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterTraitsSpace));
        EditorUtils.bindTextComponent(keywordsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setKeywords));
        EditorUtils.bindSpinner(afterKeywordsSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterKeywordsSpace));
        EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(model::setRules));
        EditorUtils.bindSpinner(afterRulesSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterRulesSpace));
        EditorUtils.bindTextComponent(flavourTextEditor, editorContext.wrapConsumerWithMarkedChanged(model::setFlavourText));
        EditorUtils.bindSpinner(afterFlavourTextSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterFlavourTextSpace));
        EditorUtils.bindTextComponent(victoryEditor, editorContext.wrapConsumerWithMarkedChanged(model::setVictory));
        EditorUtils.bindTextComponent(copyrightEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyright));

        titleEditor.setText(model.getTitle());
        subtitleEditor.setText(model.getSubtitle());
        uniqueEditor.setSelected(model.getUnique() != null && model.getUnique());
        traitsEditor.setText(model.getTraits());
        afterTraitsSpaceEditor.setValue(model.getAfterTraitsSpace());
        keywordsEditor.setText(model.getKeywords());
        afterKeywordsSpaceEditor.setValue(model.getAfterKeywordsSpace());
        rulesEditor.setText(model.getRules());
        afterRulesSpaceEditor.setValue(model.getAfterRulesSpace());
        flavourTextEditor.setText(model.getFlavourText());
        afterFlavourTextSpaceEditor.setValue(model.getAfterFlavourTextSpace());
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
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.SUBTITLE), subtitleEditor);
    }

    // fix the width of spacing editors
    private static final String SPACING_EDITOR_CONSTRAINTS = "wrap, width 50:50:50";

    public void addNonTitleEditorsToPanel(JPanel panel, boolean includeVictory) {
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.TRAITS), traitsEditor);
        MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterTraitsSpaceEditor, SPACING_EDITOR_CONSTRAINTS);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.KEYWORDS), keywordsEditor);
        MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterKeywordsSpaceEditor, SPACING_EDITOR_CONSTRAINTS);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.RULES), rulesEditor);
        MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterRulesSpaceEditor, SPACING_EDITOR_CONSTRAINTS);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.FLAVOR), flavourTextEditor);

        if (includeVictory) {
            MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterFlavourTextSpaceEditor, SPACING_EDITOR_CONSTRAINTS);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.VICTORY), victoryEditor);
        }

        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.COPYRIGHT), copyrightEditor);
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

        addSpacing(sb, model.getAfterTraitsSpace());

        if (!StringUtils.isEmpty(model.getKeywords())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getKeywords());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        addSpacing(sb, model.getAfterKeywordsSpace());

        if (!StringUtils.isEmpty(model.getRules())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getRules());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        addSpacing(sb, model.getAfterRulesSpace());

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

        addSpacing(sb, model.getAfterFlavourTextSpace());

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

    private void addSpacing(StringBuilder sb, Integer spacing) {
        if (spacing == null)
            return;

        if (spacing == 0)
            return;

        sb.append(MarkupUtils.getSpacerMarkup(1, spacing));
    }

    public void paintCopyright(PaintContext paintContext) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getCopyrightTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(model.getCopyright());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), COPYRIGHT_DRAW_REGION);
    }
}
