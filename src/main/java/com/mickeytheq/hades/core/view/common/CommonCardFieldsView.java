package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import ca.cgjennings.layout.TextStyle;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.view.component.DistanceComponent;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;

public class CommonCardFieldsView {
    private static final RectangleEx COPYRIGHT_DRAW_REGION = RectangleEx.millimeters(23.20, 86.70, 17.10, 1.69);


    private final CommonCardFieldsModel model;
    private JTextField titleEditor;
    private JTextField subtitleEditor;
    private JToggleButton uniqueEditor;

    private JTextField copyrightEditor;

    private JTextField traitsEditor;
    private DistanceComponent afterTraitsSpaceEditor;
    private JTextArea keywordsEditor;
    private DistanceComponent afterKeywordsSpaceEditor;
    private JTextArea rulesEditor;
    private DistanceComponent afterRulesSpaceEditor;
    private JTextArea flavourTextEditor;
    private DistanceComponent afterFlavourTextSpaceEditor;
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
        afterTraitsSpaceEditor = new DistanceComponent();
        keywordsEditor = EditorUtils.createTextArea(6, 30);
        afterKeywordsSpaceEditor = new DistanceComponent();
        rulesEditor = EditorUtils.createTextArea(6, 30);
        afterRulesSpaceEditor = new DistanceComponent();
        flavourTextEditor = EditorUtils.createTextArea(6, 30);
        afterFlavourTextSpaceEditor = new DistanceComponent();
        victoryEditor = EditorUtils.createTextArea(2, 30);
        copyrightEditor = EditorUtils.createTextField(30);

        EditorUtils.bindTextComponent(titleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTitle));
        EditorUtils.bindTextComponent(subtitleEditor, editorContext.wrapConsumerWithMarkedChanged(model::setSubtitle));
        EditorUtils.bindToggleButton(uniqueEditor, editorContext.wrapConsumerWithMarkedChanged(model::setUnique));
        EditorUtils.bindTextComponent(traitsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTraits));
        EditorUtils.bindDistanceComponent(afterTraitsSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterTraitsSpacing));
        EditorUtils.bindTextComponent(keywordsEditor, editorContext.wrapConsumerWithMarkedChanged(model::setKeywords));
        EditorUtils.bindDistanceComponent(afterKeywordsSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterKeywordsSpacing));
        EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(model::setRules));
        EditorUtils.bindDistanceComponent(afterRulesSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterRulesSpacing));
        EditorUtils.bindTextComponent(flavourTextEditor, editorContext.wrapConsumerWithMarkedChanged(model::setFlavourText));
        EditorUtils.bindDistanceComponent(afterFlavourTextSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterFlavourTextSpacing));
        EditorUtils.bindTextComponent(victoryEditor, editorContext.wrapConsumerWithMarkedChanged(model::setVictory));
        EditorUtils.bindTextComponent(copyrightEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyright));

        titleEditor.setText(model.getTitle());
        subtitleEditor.setText(model.getSubtitle());
        uniqueEditor.setSelected(model.getUnique() != null && model.getUnique());
        traitsEditor.setText(model.getTraits());
        afterTraitsSpaceEditor.setDistance(model.getAfterTraitsSpacing());
        keywordsEditor.setText(model.getKeywords());
        afterKeywordsSpaceEditor.setDistance(model.getAfterKeywordsSpacing());
        rulesEditor.setText(model.getRules());
        afterRulesSpaceEditor.setDistance(model.getAfterRulesSpacing());
        flavourTextEditor.setText(model.getFlavourText());
        afterFlavourTextSpaceEditor.setDistance(model.getAfterFlavourTextSpacing());
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

    public void addNonTitleEditorsToPanel(JPanel panel, boolean includeVictory) {
        addTraitsEditorToPanel(panel);

        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.KEYWORDS), keywordsEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.SPACING), afterKeywordsSpaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.RULES), rulesEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.SPACING), afterRulesSpaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.FLAVOR), flavourTextEditor);

        if (includeVictory) {
            addVictoryEditorsToPanel(panel);
        }

        addCopyrightEditorToPanel(panel);
    }

    public void addTraitsEditorToPanel(JPanel panel) {
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.TRAITS), traitsEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.SPACING), afterTraitsSpaceEditor);
    }

    public void addVictoryEditorsToPanel(JPanel panel) {
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.SPACING), afterFlavourTextSpaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.VICTORY), victoryEditor);
    }

    public void addCopyrightEditorToPanel(JPanel panel) {
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
        PaintUtils.paintTitle(paintContext, titleDrawRegion, getModel().getTitle(), getModel().isUnique(),
                MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER,
                true);
    }

    public void paintTitleMultiline(PaintContext paintContext, Rectangle titleDrawRegion) {
        PaintUtils.paintTitle(paintContext, titleDrawRegion, getModel().getTitle(), getModel().isUnique(),
                MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER,
                true);
    }

    public void paintTitleMultilineRotated(PaintContext paintContext, Rectangle drawRegion) {
        // the title is drawn vertically from bottom to top
        // to achieve this we have to create a rotate transform, apply it to the Graphics2D so that the text
        // drawing transforms
        // make sure to restore any existing transform afterwards
        AffineTransform oldTransform = paintContext.getGraphics().getTransform();
        try {
            AffineTransform rotationTransform = new AffineTransform();
            rotationTransform.rotate(-Math.PI / 2, drawRegion.getCenterX(), drawRegion.getCenterY());

            // we also have to rotate the draw region as this will also be transformed during the draw
            Rectangle rotatedDrawRegion = rotationTransform.createTransformedShape(drawRegion).getBounds();

            // the existing transform needs to be preserved so concatenate the two transforms together, doing the existing one
            // first followed by our rotation. the existing transform may be doing up/down scaling for higher/lower quality viewing/exports
            // we use preConcatenate so that our new transform is being altered. we don't want to change the oldTransform
            // as that needs to be restored later
            rotationTransform.preConcatenate(oldTransform);

            paintContext.getGraphics().setTransform(rotationTransform);
            paintTitleMultiline(paintContext, rotatedDrawRegion);
        }
        finally {
            paintContext.getGraphics().setTransform(oldTransform);
        }
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

        addSpacing(sb, model.getAfterTraitsSpacing());

        if (!StringUtils.isEmpty(model.getKeywords())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getKeywords());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        addSpacing(sb, model.getAfterKeywordsSpacing());

        if (!StringUtils.isEmpty(model.getRules())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getRules());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        addSpacing(sb, model.getAfterRulesSpacing());

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

        addSpacing(sb, model.getAfterFlavourTextSpacing());

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

    private void addSpacing(StringBuilder sb, Distance spacing) {
        if (spacing == null)
            return;

        if (spacing.getAmount() == 0)
            return;

        // TODO: convert the amount to pixels for other units or use the unit natively if possible
        sb.append(MarkupUtils.getSpacerMarkup(1, spacing.getAmount()));
    }

    public void paintCopyright(PaintContext paintContext) {
        paintCopyright(paintContext, paintContext.toPixelRect(COPYRIGHT_DRAW_REGION), TextStyleUtils.getCopyrightTextStyle());
    }

    public void paintCopyright(PaintContext paintContext, Rectangle drawRegion, TextStyle textStyle) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(textStyle);
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(model.getCopyright());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }
}
