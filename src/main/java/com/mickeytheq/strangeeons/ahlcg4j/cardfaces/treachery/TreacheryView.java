package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.WeaknessType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.*;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.GameConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.*;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class TreacheryView extends BaseCardFaceView<Treachery> {
    private static final URL DEFAULT_TEMPLATE_RESOURCE = Treachery.class.getResource("/templates/AHLCG-Treachery.jp2");
    private static final URL WEAKNESS_TEMPLATE_RESOURCE = Treachery.class.getResource("/templates/AHLCG-WeaknessTreachery.jp2");

    private static final URL BASIC_WEAKNESS_OVERLAY_RESOURCE = Treachery.class.getResource("/overlays/AHLCG-BasicWeakness.png");
    private static final URL BASIC_WEAKNESS_ICON_RESOURCE = Treachery.class.getResource("/icons/AHLCG-BasicWeakness.png");

    private JComboBox<WeaknessType> weaknessTypeEditor;
    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;

    // locations to draw portraits
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(320, 510, 13, 13);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(175, 254, 28, 28);
    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(17, 0, 344, 298);

    // locations to draw other elements
    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(137, 286, 104, 14);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(39, 307, 299, 29);
    private static final Rectangle BODY_NON_WEAKNESS_DRAW_REGION = new Rectangle(30, 340, 318, 160);
    private static final Rectangle BODY_WEAKNESS_DRAW_REGION = new Rectangle(30, 357, 318, 145);

    private static final Rectangle BASIC_WEAKNESS_OVERLAY_DRAW_REGION = new Rectangle(156, 243, 66, 41);
    private static final Rectangle BASIC_WEAKNESS_ICON_DRAW_REGION = new Rectangle(175, 253, 28, 28);
    private static final Rectangle WEAKNESS_SUBTYPE_DRAW_REGION = new Rectangle(88, 335, 200, 17);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), getViewContext(), ART_PORTRAIT_DRAW_REGION);
        numberingView = new NumberingView(getModel().getNumberingModel(), getViewContext(), COLLECTION_PORTRAIT_DRAW_REGION, ENCOUNTER_PORTRAIT_DRAW_REGION);
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        ViewContext viewContext = getViewContext();

        weaknessTypeEditor = new JComboBox<>();
        weaknessTypeEditor.addItem(WeaknessType.None);
        weaknessTypeEditor.addItem(WeaknessType.Basic);
        weaknessTypeEditor.addItem(WeaknessType.Investigator);
        weaknessTypeEditor.addItem(WeaknessType.Story);

        EditorUtils.bindComboBox(weaknessTypeEditor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setWeaknessType(value)));

        JPanel generalPanel = MigLayoutUtils.createPanel("General");

        commonCardFieldsView.addTitleEditorToPanel(generalPanel);

        generalPanel.add(new JLabel("Weakness type"));
        generalPanel.add(weaknessTypeEditor, "wrap, pushx, growx");

        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel);

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");

        mainPanel.add(commonCardFieldsView.createStandardArtPanel(), "wrap, pushx, growx");

        // add the panel to the main tab control
        tabbedPane.addTab(getModel().getCardFaceSide().name(), mainPanel);
        tabbedPane.addTab("Collection / encounter", numberingView.createStandardCollectionEncounterPanel());
    }

    @Override
    public BufferedImage loadTemplateImage() {
        URL templateUrl;
        if (getModel().getWeaknessType() != WeaknessType.None)
            templateUrl = WEAKNESS_TEMPLATE_RESOURCE;
        else
            templateUrl = DEFAULT_TEMPLATE_RESOURCE;

        return ImageUtils.loadImage(templateUrl);
    }

    @Override
    protected void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        commonCardFieldsView.paintArtPortrait(paintContext);

        // draw the template
        paintContext.getGraphics().drawImage(loadTemplateImage(), 0, 0, null);

        MarkupRenderer markupRenderer;

        // label
        markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getLargeLabelTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(Language.gstring(GameConstants.LABEL_TREACHERY).toUpperCase());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), LABEL_DRAW_REGION);

        // title
        // TODO: move this into CommonCardFieldsView
        markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(getModel().getCommonCardFieldsModel().getTitle());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), TITLE_DRAW_REGION);

        if (getModel().getWeaknessType() == WeaknessType.None)
            paintNonWeaknessContent(paintContext);
        else
            paintWeaknessContent(paintContext);
    }

    private void paintNonWeaknessContent(PaintContext paintContext) {
        numberingView.paintEncounterPortrait(paintContext);
        numberingView.paintEncounterNumbers(paintContext);
        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);

        commonCardFieldsView.paint(paintContext, BODY_NON_WEAKNESS_DRAW_REGION);
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        // draw the weakness overlay
        // this is the circular area either the standard basic weakness icon goes in or the encounter icon
        // for story weaknesses
        WeaknessType weaknessType = getModel().getWeaknessType();

        if (weaknessType == WeaknessType.Basic || weaknessType == WeaknessType.Story) {
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(BASIC_WEAKNESS_OVERLAY_RESOURCE), BASIC_WEAKNESS_OVERLAY_DRAW_REGION);

            if (weaknessType == WeaknessType.Basic) {
                ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(BASIC_WEAKNESS_ICON_RESOURCE), BASIC_WEAKNESS_ICON_DRAW_REGION);
            }
            else {
                numberingView.paintEncounterPortrait(paintContext);
                numberingView.paintEncounterNumbers(paintContext);
            }
        }

        // the weakness/basic weakness fixed text
        String subTypeText = Language.gstring(GameConstants.LABEL_WEAKNESS);

        if (weaknessType == WeaknessType.Basic)
            subTypeText = Language.gstring(GameConstants.LABEL_BASICWEAKNESS);

        MarkupRenderer markupRenderer = new MarkupRenderer(getSheet().getTemplateResolution());
        markupRenderer.setDefaultStyle(TextStyleUtils.getSubTypeTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(subTypeText.toUpperCase());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), WEAKNESS_SUBTYPE_DRAW_REGION);

        commonCardFieldsView.paint(paintContext, BODY_WEAKNESS_DRAW_REGION);

        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);
    }
}
